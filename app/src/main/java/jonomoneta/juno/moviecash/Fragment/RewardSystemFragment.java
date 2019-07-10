package jonomoneta.juno.moviecash.Fragment;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jonomoneta.juno.moviecash.Activity.ProfileActivity;
import jonomoneta.juno.moviecash.CustomDialog;
import jonomoneta.juno.moviecash.FullScreenPosterDialog;
import jonomoneta.juno.moviecash.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardSystemFragment extends Fragment {

    private TextView theaterIMG, sportsIMG, travelIMG, restaurantIMG, schoolIMG, mallIMG, gymIMG, posterEightIMG, posterNineIMG;

    public RewardSystemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward_system, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        sportsIMG = view.findViewById(R.id.sportsIMG);
        travelIMG = view.findViewById(R.id.travelIMG);
        restaurantIMG = view.findViewById(R.id.restaurantIMG);
        schoolIMG = view.findViewById(R.id.schoolIMG);
        mallIMG = view.findViewById(R.id.mallIMG);
        gymIMG = view.findViewById(R.id.gymIMG);
        theaterIMG = view.findViewById(R.id.theaterIMG);
        posterEightIMG = view.findViewById(R.id.posterEightIMG);
        posterNineIMG = view.findViewById(R.id.posterNineIMG);

        theaterIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.movie_theater_original);
                customDialog.show();
            }
        });

        sportsIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.sports_original);
                customDialog.show();
            }
        });

        travelIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.travel_original);
                customDialog.show();
            }
        });

        restaurantIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.restaurant_original);
                customDialog.show();
            }
        });

        schoolIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.school_original);
                customDialog.show();
            }
        });

        mallIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.malls_original);
                customDialog.show();
            }
        });

        gymIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.gym_original);
                customDialog.show();
            }
        });

        posterEightIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.poster_eight_original);
                customDialog.show();
            }
        });

        posterNineIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.poster_nine_original);
                customDialog.show();
            }
        });
    }

}
