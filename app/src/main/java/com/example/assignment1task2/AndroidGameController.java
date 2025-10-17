package com.example.assignment1task2;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import Network.Client;
import Network.ClientManager;
import SequenceDice.Board;
import SequenceDice.Cell;
import SequenceDice.GameEvent;
import SequenceDice.GameEventType;
import SequenceDice.GameModel;
import SequenceDice.Leaderboard;
import SequenceDice.Player;

public class AndroidGameController extends AppCompatActivity {
    private GameModel gameModel;
    private Client client;
    private int playerNumber;
    private int serverId;

    GridLayout gridLayout;
    TextView playerTurnHeadingTV;
    TextView rollDiceTV;
    Button rollDiceButton;

    Drawable playerTurnHeadingBackground;
    Drawable cellButtonBackground;
    int[] colors;
    Button.OnClickListener rollDiceButtonMainFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        gridLayout = findViewById(R.id.boardGridLayout);
        playerTurnHeadingTV = findViewById(R.id.playerTurnHeadingTV);
        rollDiceTV = findViewById(R.id.rollDiceTV);
        rollDiceButton = findViewById(R.id.rollDiceButton);

        playerTurnHeadingBackground = ContextCompat.getDrawable(this, R.drawable.player_turn_heading_background);
        cellButtonBackground = ContextCompat.getDrawable(this, R.drawable.cell_button_background);

        colors = new int[]{
                ContextCompat.getColor(this, R.color.green),
                ContextCompat.getColor(this, R.color.blue),
                ContextCompat.getColor(this, R.color.red)
        };

        // Get data from Intent
        client = ClientManager.getClient();
        if(client != null)
            client.setController(this);

        serverId = getIntent().getIntExtra("gameId", -1) + 1;
        playerNumber = getIntent().getIntExtra("playerNumber", 1);

        int numOfPlayers = getIntent().getIntExtra("numOfPlayers", 2);

        // Initialize GameModel
        gameModel = new GameModel(numOfPlayers);
        Log.d("GAMECONTROLLER", "game model initialized");
        gameModel.setObserver(this::onGameEvent);

        // Initialize roll dice button
        rollDiceButtonMainFunction = v -> {
            if(gameModel.getCurrentPlayer().getNumber() == playerNumber)
                publish(new GameEvent(GameEventType.ROLL_DICE, "roll the dice", playerNumber));

        };
        rollDiceButton.setOnClickListener(rollDiceButtonMainFunction);

        drawGrid(); // draw grid now that GameModel is ready
    }

    public void onGameEvent(GameEvent event) {
        runOnUiThread(() -> {
            gameModel.setCurrentEvent(event);
            switch (event.getType()) {
                case NEXT_PLAYER_TURN:
                    gameModel.nextTurn((int) event.getCargo());
                    Log.d("GAMECONTROLLER", "Event Cargo: " + (int) event.getCargo());
                    //updates player turn heading
                    playerTurnHeadingTV.setText(event.getMessage());
                    if (gameModel.getNumOfPlayers() > 3) {
                        playerTurnHeadingBackground.setTint(colors[gameModel.getCurrentTeam().getNumber() - 1]);
                    } else
                        playerTurnHeadingBackground.setTint(colors[gameModel.getCurrentPlayer().getNumber()]);

                    playerTurnHeadingTV.setBackground(playerTurnHeadingBackground);
                    if(gameModel.getCurrentPlayer().getNumber() == playerNumber) {
                        rollDiceTV.setText("Roll the dice");
                    }
                    else{
                        rollDiceTV.setText("Waiting for player " + gameModel.getCurrentPlayer().getNumber() + " to roll the dice");
                    }
                    break;
                case NO_POSSIBLE_MOVE:
                    rollDiceTV.setText(event.getMessage());
//                rollDiceButton.setText("Forfeit turn");
//                rollDiceButton.setOnClickListener(v -> {
//                    gameModel.nextTurn();
//                    rollDiceButton.setText("Roll Dice");
//                    rollDiceButton.setOnClickListener(rollDiceButtonMainFunction);
//                });
                    break;
                case DICE_ROll_OUTCOME:
                    Log.d("DICE_ROLL_OUTCOME", event.getMessage());
                    Log.d("GAMECONTROLLER", String.valueOf(gameModel.getCurrentPlayer().getNumber()) + " vs " + playerNumber);
                    if (gameModel.getCurrentPlayer().getNumber() == playerNumber) {
                        if (gameModel.getBoard().validCellExists((int) event.getCargo()))
                            rollDiceTV.setText(event.getMessage());
                        else
                            rollDiceTV.setText("You rolled " + event.getCargo() + ", no valid cell exists, replace an opponents token.");
                    }
                    break;
                case WILD_DICE_ROLL:
                case DEFENSIVE_DICE_ROLL:
                case EXTRA_TURN_DICE_ROLL:
                    rollDiceTV.setText(event.getMessage());
                    break;
                case TOKEN_PLACED:
                    gameModel.placeToken((int[]) event.getCargo());
                    drawGrid();
                    break;
                case TOKEN_REMOVED:
                    gameModel.removeToken((int[]) event.getCargo());
                    drawGrid();
                    break;
                case GAME_OVER:
                    GameOverFragment gof = new GameOverFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("player", event.getMessage());
                    bundle.putInt("roundsPlayed", gameModel.getRoundsPlayed());
                    bundle.putInt("tokensPlaced", gameModel.getTokensPlaced());
                    bundle.putInt("tokensRemoved", gameModel.getTokensRemoved());
                    gof.setArguments(bundle);
                    gof.show(getSupportFragmentManager(), "Game Over");

                    //update leaderboard with playernames
                    Leaderboard lb = new Leaderboard(this);
                    Player winner = (Player) event.getCargo();
                    Log.d("winner", winner.getName());
                    lb.updateLeaderboard(winner.getName(), 1);
                    break;
            }
        });
        switch(event.getType()) {
            case PLACE_TOKEN:
            case REMOVE_TOKEN:
                new Thread(() -> client.publish(serverId, event)).start();
                Log.d("GAMECONTROLLER", "PLACE_TOKEN received");
                break;
        }
    }

    public void drawGrid() {
        gridLayout.removeAllViews();
        Board board = gameModel.getBoard();
        int[][] numbers = board.getNumbers();

        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                Button button = new Button(new ContextThemeWrapper(this, R.style.CellButtonText), null, 0);
                button.setText(String.valueOf(numbers[i][j]));
                button.setBackground(cellButtonBackground);

                if(numbers[i][j] == 2 || numbers[i][j] == 12)
                {
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey));
                }

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(i, 1f),
                        GridLayout.spec(j, 1f)
                );
                params.width = 0;
                params.height = 0;
                button.setLayoutParams(params);

                Cell[][] cells = board.getBoard();
                if(cells[i][j].getOccupant() != null){
                    //find out who the occupant belongs to
                    if(gameModel.getNumOfPlayers() <= 3) {
                        button.setBackgroundColor(colors[(cells[i][j].getOccupant().getNumber())]);
                    }
                    else {
                        if (cells[i][j].getOccupant().getNumber() == 1 || cells[i][j].getOccupant().getNumber() == 3)
                            button.setBackgroundColor(colors[0]);
                        else
                            button.setBackgroundColor(colors[1]);
                    }
                }

                int row = i;
                int col = j;
                button.setOnClickListener(v -> {
                    gameModel.processChoice(new int[]{row, col});

                    drawGrid();
                });

                gridLayout.addView(button);
            }
        }
    }
    public void publish(GameEvent event){
        new Thread(() -> {
            try {
                client.publish(serverId, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}