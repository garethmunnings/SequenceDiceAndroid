package com.example.assignment1task2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class StartGame extends AppCompatActivity {
    private int numberOfPlayers = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button rulesButton = findViewById(R.id.rulesButton);
        rulesButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartGame.this, RulesActivity.class);
            startActivity(intent);
        });

        Button leaderboardButton = findViewById(R.id.leaderboardButton);
        leaderboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartGame.this, LeaderboardActivity.class);
            startActivity(intent);
        });

        LinearLayout player1LL = findViewById(R.id.player1LL);
        LinearLayout player2LL = findViewById(R.id.player2LL);
        LinearLayout player3LL = findViewById(R.id.player3LL);
        LinearLayout player4LL = findViewById(R.id.player4LL);
        TextView blueTeamLabel = findViewById(R.id.blueTeamLabel);
        TextView greenTeamLabel = findViewById(R.id.greenTeamLabel);

        RadioGroup group = findViewById(R.id.optionGroup);
        group.setOnCheckedChangeListener((group1, checkedId) -> {
            if (checkedId == R.id.twoPlayer) {
                numberOfPlayers = 2;
                greenTeamLabel.setVisibility(View.GONE);
                blueTeamLabel.setVisibility(View.GONE);
                player3LL.setVisibility(View.GONE);
                player4LL.setVisibility(View.GONE);
            } else if (checkedId == R.id.threePlayer) {
                numberOfPlayers = 3;
                greenTeamLabel.setVisibility(View.GONE);
                blueTeamLabel.setVisibility(View.GONE);
                player3LL.setVisibility(View.VISIBLE);
                player4LL.setVisibility(View.GONE);
            } else if (checkedId == R.id.fourPlayer) {
                numberOfPlayers = 4;
                greenTeamLabel.setVisibility(View.VISIBLE);
                blueTeamLabel.setVisibility(View.VISIBLE);
                player3LL.setVisibility(View.VISIBLE);
                player4LL.setVisibility(View.VISIBLE);
            }
        });
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> {
            String[] playerNames = new String[numberOfPlayers];
            if(numberOfPlayers == 2){
                EditText et1 = (EditText)player1LL.getChildAt(1);
                playerNames[0] = et1.getText().toString();
                EditText et2 = (EditText)player2LL.getChildAt(1);
                playerNames[1] = et2.getText().toString();
            }
            else if(numberOfPlayers == 3){
                EditText et1 = (EditText)player1LL.getChildAt(1);
                playerNames[0] = et1.getText().toString();
                EditText et2 = (EditText)player2LL.getChildAt(1);
                playerNames[1] = et2.getText().toString();
                EditText et3 = (EditText)player3LL.getChildAt(1);
                playerNames[2] = et3.toString();
            }
            else{
                EditText et1 = (EditText)player1LL.getChildAt(1);
                playerNames[0] = et1.getText().toString();
                EditText et2 = (EditText)player2LL.getChildAt(1);
                playerNames[1] = et2.getText().toString();
                EditText et3 = (EditText)player3LL.getChildAt(1);
                playerNames[2] = et3.toString();
                EditText et4 = (EditText)player4LL.getChildAt(1);
                playerNames[3] = et4.toString();
            }
            Intent intent = new Intent(StartGame.this, SequenceDiceController.class);
            intent.putExtra("numberOfPlayers", numberOfPlayers);
            intent.putExtra("playerNamesList", playerNames);
            startActivity(intent);
        });


    }
}