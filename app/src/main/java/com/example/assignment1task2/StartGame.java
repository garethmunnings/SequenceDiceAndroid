package com.example.assignment1task2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        RadioGroup group = findViewById(R.id.optionGroup);
        group.setOnCheckedChangeListener((group1, checkedId) -> {
            if (checkedId == R.id.twoPlayer) {
                numberOfPlayers = 2;
            } else if (checkedId == R.id.threePlayer) {
                numberOfPlayers = 3;
            } else if (checkedId == R.id.fourPlayer) {
                numberOfPlayers = 4;
            }
        });
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> {

            Intent intent = new Intent(StartGame.this, SequenceDiceController.class);
            intent.putExtra("numberOfPlayers", numberOfPlayers);
            startActivity(intent);
        });


    }
}