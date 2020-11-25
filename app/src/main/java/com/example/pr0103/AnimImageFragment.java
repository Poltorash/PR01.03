package com.example.pr0103;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class AnimImageFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anim_image, container, false);
        Init(view);
        return view;
    }

    private void Init(View view) {
        Animation animation = AnimationUtils.loadAnimation(
                getContext(), R.anim.logo_rotate_animation);
        view.findViewById(R.id.imageForLoaded).startAnimation(animation);
    }
}
