package com.example.assignment1task2;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import SequenceDice.Board;
import SequenceDice.GameEvent;
import SequenceDice.GameModel;
import SequenceDice.GameObserver;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements GameObserver {

    GameModel gameModel;
    GridLayout gridLayout;
    TextView playerTurnHeadingTV;
    TextView rollDiceTV;
    Button rollDiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



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

        //end region

        gameModel = new GameModel(2);
        gameModel.addObserver(this);

        drawGrid();

        gameModel.startGame();
    }

    public void drawGrid(){
        gridLayout = findViewById(R.id.boardGridLayout);
        Board board = gameModel.getBoard();
        int[][] numbers = board.getNumbers();

        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                Button button = new Button(this);

                button.setText(String.valueOf(numbers[i][j]));

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 120;
                params.height = 120;
                button.setLayoutParams(params);

                int row = i;
                int col = j;
                button.setOnClickListener(v -> {
                    if(gameModel.processChoice(new int[]{row, col})) {
                        String colour = gameModel.getCurrentPlayer().getColour();
                        button.setBackgroundColor(Color.parseColor(colour));
                    }

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
                break;
            case DICE_ROll:
                rollDiceTV.setText(event.getMessage());
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
                System.out.println(event.getMessage());
                break;
        }
    }
}