package com.inmoglass.factorytools.microtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.Log;
import com.inmoglass.factorytools.R;

import java.io.File;
import java.io.IOException;

/**
 * 麦克风测试项
 *
 * @author Administrator
 * @date 2021-11-25
 */
public class MicroTestActivity extends AbstractTestActivity {
    private static final String TAG = MicroTestActivity.class.getSimpleName();
    private Button startRecordBtn;
    private Button endRecordBtn;
    private Button playRecordBtn;
    private TextView recordTimeTv;

    private static final int PERMISSIONS_REQUEST_CODE = 10;
    private static final String[] PERMISSIONS_REQUIRED = {Manifest.permission.RECORD_AUDIO};

    Thread timeThread; // 记录录音时长的线程
    boolean isRecording = false; //录音状态
    final String audioSaveDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiodemo/";
    MediaRecorder mMediaRecorder; // MediaRecorder 实例
    String fileName; // 录音文件的名称
    String filePath; // 录音文件存储路径
    int timeCount; // 录音时长 计数
    final int TIME_COUNT = 0x101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.micro_test_item);
        setContentView(R.layout.activity_micro_test);
        if (!checkMicroIsAvailable()) {
            Toast.makeText(this, "本设备不支持麦克风", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
        } else {
            getMicroPermission();
        }
        initView();
    }

    /**
     * 获取麦克风权限
     */
    private void getMicroPermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);
    }

    /**
     * 检测麦克风是否存在
     *
     * @return
     */
    private boolean checkMicroIsAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    private void initView() {
        startRecordBtn = findViewById(R.id.btn_start_record);
        endRecordBtn = findViewById(R.id.btn_end_record);
        playRecordBtn = findViewById(R.id.btn_start_play);
        recordTimeTv = findViewById(R.id.tv_record_time);

        startRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecord();
            }
        });
        endRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endRecord();
            }
        });
        playRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlayRecord();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 开始录音
     */
    private void startRecord() {
        startRecordBtn.setEnabled(false);
        endRecordBtn.setEnabled(true);
        start();
        isRecording = true;
        // 初始化录音时长记录
        timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                countTime();
            }
        });
        timeThread.start();
    }

    private void countTime() {
        while (isRecording) {
            Log.d(TAG, "正在录音");
            timeCount++;
            Message msg = Message.obtain();
            msg.what = TIME_COUNT;
            msg.obj = timeCount;
            myHandler.sendMessage(msg);
            try {
                timeThread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "结束录音");
        timeCount = 0;
        Message msg = Message.obtain();
        msg.what = TIME_COUNT;
        msg.obj = timeCount;
        myHandler.sendMessage(msg);
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_COUNT:
                    int count = (int) msg.obj;
                    Log.d(TAG, "count == " + count);
                    if (isRecording) {
                        recordTimeTv.setText("正在录音：" + FormatMiss(count));
                    } else {
                        recordTimeTv.setText(FormatMiss(count));
                    }
                    break;
            }
        }
    };

    // 格式化 录音时长为 时:分:秒
    public static String FormatMiss(int miss) {
        String hh = miss / 3600 < 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 < 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 < 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    private void start() {
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            fileName = "test.m4a";
            File destDir = new File(audioSaveDir);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            filePath = audioSaveDir + fileName;
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            Log.e(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    /**
     * 停止录音
     */
    private void endRecord() {
        startRecordBtn.setEnabled(true);
        endRecordBtn.setEnabled(false);
        stopRecord();
        isRecording = false;
    }

    private void stopRecord() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            filePath = "";
        } catch (RuntimeException e) {
            Log.e(TAG, e.toString());
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            File file = new File(filePath);
            if (file.exists())
                file.delete();
            filePath = "";
        }
    }

    /**
     * 开始播放录音
     */
    private void startPlayRecord() {
        String testFilePath = audioSaveDir + "test.m4a";
        File destFile = new File(testFilePath);
        MediaPlayer mMediaPlayer;
        if (destFile.exists()) {
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(testFilePath);
                mMediaPlayer.setLooping(false);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                Integer mediaDuration = (mMediaPlayer.getDuration() / 1000);
                LogUtils.i(TAG, "time=" + mediaDuration);

                new CountDownTimer(mediaDuration * 1000, 1000) {
                    @Override
                    public void onFinish() {
                        cancel();
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        recordTimeTv.setText("正在播放：" + FormatMiss((int) millisUntilFinished / 1000));
                    }
                }.start();

            } catch (IOException e) {
                Log.e(TAG, "playRing=>error: ", e);
                mMediaPlayer = null;
            }
        }
    }
}
