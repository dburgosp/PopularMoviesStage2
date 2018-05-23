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
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            /**
             * Called when the end of a media source is reached during playback.
             *
             * @param mp the MediaPlayer that reached the end of the file
             */
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        } );
    }
}
