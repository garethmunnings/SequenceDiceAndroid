package com.example.assignment1task2;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import SequenceDice.Board;
import SequenceDice.Cell;
import SequenceDice.GameEvent;
import SequenceDice.GameModel;
import SequenceDice.GameObserver;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements GameObserver {
    GameModel gameModel;
    GridLayout gridLayout;
    TextView playerTurnHeadingTV;
    TextView rollDiceTV;
    Button rollDiceButton;
    TextView gameOverTV;

    Drawable playerTurnHeadingBackground;
    Drawable cellButtonBackground;
    int[] colorsDark;

    //TODO make sure all edge cases are fixed (rolling defensive roll on first roll)
    //TODO welcome screen
    //TODO game over fragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        colorsDark = new int[]{ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.green), ContextCompat.getColor(this, R.color.blue)};

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //link entities

        playerTurnHeadingTV = findViewById(R.id.playerTurnHeadingTV);
        rollDiceTV = findViewById(R.id.rollDiceTV);
        rollDiceButton = findViewById(R.id.rollDiceButton);
        rollDiceButton.setOnClickListener(v -> {
            gameModel.rollDice();
        });
        gridLayout = findViewById(R.id.boardGridLayout);
        gameOverTV = findViewById(R.id.gameOverTV);

        playerTurnHeadingBackground = ContextCompat.getDrawable(this, R.drawable.player_turn_heading_background);
        cellButtonBackground = ContextCompat.getDrawable(this, R.drawable.cell_button_background);

        //end region

        gameModel = new GameModel(3);
        gameModel.addObserver(this);

        drawGrid();

        gameModel.startGame();
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
                    button.setBackgroundColor(colorsDark[(cells[i][j].getOccupant().getNumber())]);
                }

                int row = i;
                int col = j;
                button.setOnClickListener(v -> {
                    if(gameModel.processChoice(new int[]{row, col})){
                        rollDiceTV.setText("Roll the dice");
                    }
                    drawGrid();
                });

                gridLayout.addView(button);
            }
        }
    }

    @Override
    public void onGameEvent(GameEvent event) {
        int[] choice;
        switch (event.getType()) {
            case NEXT_PLAYER_TURN:
                playerTurnHeadingTV.setText(event.getMessage());


                playerTurnHeadingBackground.setTint(colorsDark[gameModel.getCurrentPlayer().getNumber()]);
                playerTurnHeadingTV.setBackground(playerTurnHeadingBackground);
                break;
            case DICE_ROll:
                if(gameModel.getBoard().validCellExists((int)event.getCargo()))
                    rollDiceTV.setText(event.getMessage());
                else
                    rollDiceTV.setText(event.getMessage() + ", no valid cell exists, replace an opponents token.");
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
                gameOverTV.setText(event.getMessage());
                gameOverTV.setVisibility(TextView.VISIBLE);
                break;
        }
    }
}