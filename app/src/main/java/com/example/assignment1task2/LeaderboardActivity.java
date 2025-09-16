package com.example.assignment1task2;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import SequenceDice.Leaderboard;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout leaderboardLL = findViewById(R.id.leaderboard);
        Leaderboard lb = new Leaderboard(this);
        List<Pair<String, Integer>> leaderboard = lb.loadLeaderboard();

        for(Pair p: leaderboard){
            TextView tv = new TextView(this);
            tv.setText(p.first + "      " + p.second + " wins");

            tv.setTextSize(25);
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setPadding(20, 8, 20, 8);
            tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tv.setAllCaps(true);
            leaderboardLL.addView(tv);
        }
    }
}