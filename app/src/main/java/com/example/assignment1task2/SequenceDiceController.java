package com.example.assignment1task2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import SequenceDice.Board;
import SequenceDice.Cell;
import SequenceDice.GameEvent;
import SequenceDice.GameModel;
import SequenceDice.GameObserver;
import SequenceDice.Leaderboard;
import SequenceDice.Player;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SequenceDiceController extends AppCompatActivity implements GameObserver {
    GameModel gameModel;
    GridLayout gridLayout;
    TextView playerTurnHeadingTV;
    TextView rollDiceTV;
    Button rollDiceButton;

    Drawable playerTurnHeadingBackground;
    Drawable cellButtonBackground;
    int[] colors;
    View.OnClickListener rollDiceButtonMainFunction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        colors = new int[]{ContextCompat.getColor(this, R.color.green), ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red)};


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //link entities

        playerTurnHeadingTV = findViewById(R.id.playerTurnHeadingTV);
        rollDiceTV = findViewById(R.id.rollDiceTV);
        rollDiceButton = findViewById(R.id.rollDiceButton);

        rollDiceButtonMainFunction = v -> {
            gameModel.rollDice();
        };
        rollDiceButton.setOnClickListener(rollDiceButtonMainFunction);

        gridLayout = findViewById(R.id.boardGridLayout);


        playerTurnHeadingBackground = ContextCompat.getDrawable(this, R.drawable.player_turn_heading_background);
        cellButtonBackground = ContextCompat.getDrawable(this, R.drawable.cell_button_background);

        //end region
        Intent intent = getIntent();

        int numberOfPlayers = intent.getIntExtra("numberOfPlayers", 2);
        String[] playerNames = intent.getStringArrayExtra("playerNamesList");
        gameModel = new GameModel(numberOfPlayers, playerNames);
        gameModel.addObserver(this);

        drawGrid();
        gameModel.startGame();
    }

    @Override
    public void onGameEvent(GameEvent event) {
        switch (event.getType()) {
            case NEXT_PLAYER_TURN:
                //updates player turn heading
                playerTurnHeadingTV.setText(event.getMessage());
                if(gameModel.getNumOfPlayers() > 3) {
                    playerTurnHeadingBackground.setTint(colors[gameModel.getCurrentTeam().getNumber() - 1]);
                }
                else
                    playerTurnHeadingBackground.setTint(colors[gameModel.getCurrentPlayer().getNumber()]);
                playerTurnHeadingTV.setBackground(playerTurnHeadingBackground);

                rollDiceTV.setText("Roll the dice");
                break;
            case NO_POSSIBLE_MOVE:
                rollDiceTV.setText(event.getMessage());
                rollDiceButton.setText("Forfeit turn");
                rollDiceButton.setOnClickListener(v -> {
                    gameModel.nextTurn();
                    rollDiceButton.setText("Roll Dice");
                    rollDiceButton.setOnClickListener(rollDiceButtonMainFunction);
                });
                break;
            case DICE_ROll:
                if(gameModel.getBoard().validCellExists((int)event.getCargo()))
                    rollDiceTV.setText(event.getMessage());
                else
                    rollDiceTV.setText("You rolled " + event.getCargo() + ", no valid cell exists, replace an opponents token.");
                break;
            case WILD_DICE_ROLL:
                rollDiceTV.setText(event.getMessage());
                break;
            case DEFENSIVE_DICE_ROLL:
                rollDiceTV.setText(event.getMessage());
                break;
            case EXTRA_TURN_DICE_ROLL:
                rollDiceTV.setText(event.getMessage());
                break;
            case GAME_OVER:
                GameOverFragment gof = new GameOverFragment();
                Bundle bundle = new Bundle();
                bundle.putString("player", event.getMessage());
                gof.setArguments(bundle);
                gof.show(getSupportFragmentManager(), "Game Over");

                //update leaderboard with playernames
                Leaderboard lb = new Leaderboard(this);
                Player winner = (Player)event.getCargo();
                Log.d("winner", winner.getName());
                lb.updateLeaderboard(winner.getName(), 1);
                break;
        }
    }

    public void drawGrid(){
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
}