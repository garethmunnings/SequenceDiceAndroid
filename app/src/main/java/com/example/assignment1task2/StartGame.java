package com.example.assignment1task2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import Network.Client;
import Network.ClientManager;

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
            LinearLayout[] playerLayouts = {player1LL, player2LL, player3LL, player4LL};

            for (int i = 0; i < numberOfPlayers; i++) {
                EditText editText = (EditText) playerLayouts[i].getChildAt(1);
                playerNames[i] = editText.getText().toString();
            }

            Toast toast = new Toast(this);
            toast.setText("Waiting for other players to join the queue");
            toast.show();

            new Thread(() -> {
                try {
                    Client client = new Client(StartGame.this, "10.0.2.2", 12345, numberOfPlayers);
                    client.run(); // Or just let the Client constructor handle its own listener thread
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        });
    }
}