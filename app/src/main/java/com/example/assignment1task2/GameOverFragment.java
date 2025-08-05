package com.example.assignment1task2;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

import com.example.assignment1task2.R;

import org.w3c.dom.Text;

public class GameOverFragment extends DialogFragment {
    Button homeButton;
    TextView winLabelTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        homeButton = view.findViewById(R.id.homeButton);
        winLabelTV = view.findViewById(R.id.titleTopTV);



        if(getArguments() != null){
            String s = getArguments().getString("player");
            String top = s.split(" ")[0] + " " + s.split(" ")[1];
            String bottom = getArguments().getString(s.split(" ")[2]);
            winLabelTV.setText(getArguments().getString(top));
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

            int screenHeight = displayMetrics.heightPixels;


            int dialogHeight = (int) (screenHeight * 0.50);
            window.setLayout(dialogHeight, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setDimAmount(0.5f); // Optional: dim background
        }
        getDialog().getWindow().setDimAmount(0.5f); // Maintain dimming
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartGame.class);
                startActivity(intent);
            }
        });
    }
}