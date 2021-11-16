package com.inmoglass.factorytools.ringtest;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.Log;
import com.inmoglass.factorytools.R;

import java.io.IOException;

public class RingTestActivity extends AbstractTestActivity {

    private TextView mRingFilePathTv;
    private TextView mPlayTimeTv;
    private MediaPlayer mMediaPlayer;

    private String mRingFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ring_test);
        setTitle(R.string.ring_test_title);
        mRingFilePath = "/system/media/audio/alarms/Beep.ogg";

        mRingFilePathTv = (TextView) findViewById(R.id.ring_file_Path);
        mPlayTimeTv = (TextView) findViewById(R.id.play_time);
        mRingFilePathTv.setText(getString(R.string.ring_tip_title, mRingFilePath));
    }

    @Override
    protected void onStart() {
        super.onStart();
        playRing();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRing();
    }

    private void playRing() {
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mRingFilePath);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            Log.e(this, "playRing=>error: ", e);
            mMediaPlayer = null;
        }
    }

    private void stopRing() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
