package com.example.android.popularmoviesstage2.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.android.popularmoviesstage2.R;

public class SoundsUtils {
    private SoundsUtils() {
    }

    public static void buttonClick(Context context){
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ping_pong_ball_hit);
        mediaPlayer.start();
    }
}
