package com.example.assignment1task2;
import android.content.Intent;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

public class GameOverFragment extends DialogFragment {
    Button homeButton;
    TextView winLabelTV;
    TextView roundsPlayedTV;
    TextView tokensPlacedTV;
    TextView tokensRemovedTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        homeButton = view.findViewById(R.id.homeButton);
        winLabelTV = view.findViewById(R.id.winLabelTV);
        roundsPlayedTV = view.findViewById(R.id.roundsPlayedTV);
        tokensPlacedTV = view.findViewById(R.id.tokensPlacedTV);
        tokensRemovedTV = view.findViewById(R.id.tokensRemovedTV);

        if(getArguments() != null){
            String s = getArguments().getString("player");
            winLabelTV.setText(s);

            int numberOfRoundsPlayed = getArguments().getInt("roundsPlayed");
            int numberOfTokensPlaced = getArguments().getInt("tokensPlaced");
            int numberOfTokensRemoved = getArguments().getInt("tokensRemoved");

            roundsPlayedTV.setText("Rounds played: " + String.valueOf(numberOfRoundsPlayed));
            tokensPlacedTV.setText("Tokens placed: " + String.valueOf(numberOfTokensPlaced));
            tokensRemovedTV.setText("Tokens removed: " + String.valueOf(numberOfTokensRemoved));
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();

            // Get screen width
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setDimAmount(0.5f);
        }
        getDialog().getWindow().setDimAmount(0.5f);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StartGame.class);
            startActivity(intent);
        });
    }
}