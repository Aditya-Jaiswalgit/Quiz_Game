package com.bharatiscript.padaayifruits;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    private static MediaPlayer mediaPlayer;
    private static boolean isPaused = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer == null) {  // ✅ Create only once
            mediaPlayer = MediaPlayer.create(this, R.raw.bg_music);
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && !isPaused) {
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    public static void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPaused = true;
        }
    }

    public static void resumeMusic() {
        if (mediaPlayer != null && isPaused) {
            mediaPlayer.start();
            isPaused = false;
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


