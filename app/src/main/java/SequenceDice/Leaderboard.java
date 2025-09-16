/**
 * Author: Gareth Munnings
 * Created on 2025/09/16
 */

package SequenceDice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Leaderboard {
    private final SharedPreferences prefs;


    public Leaderboard(Context context){
        prefs = context.getSharedPreferences("leaderboard", Context.MODE_PRIVATE);
    }

    public List<Pair<String, Integer>> loadLeaderboard(){
        List<Pair<String, Integer>> leaderboard = new ArrayList<>();
        Map<String, ?> allEntries = prefs.getAll();

        for(Map.Entry<String, ?> entry : allEntries.entrySet()){
            String key = entry.getKey();
            int value = (int) entry.getValue();
            leaderboard.add(new Pair<>(key, value));
        }
        return leaderboard;
    }

    public void updateLeaderboard(String player, int score){
        if(prefs.getString(player, null) != null){
            int oldScore = Integer.parseInt(prefs.getString(player, null));
            score += oldScore;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(player, String.valueOf(score));

    }
}
