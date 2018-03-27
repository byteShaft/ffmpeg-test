package com.om26er.ffmpegtest;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private FFmpeg ffmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ffmpeg = FFmpeg.getInstance(getApplicationContext());
        loadFFMpegBinary();
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String file = dir + "/output.mp4";
        String command = String.format("ffmpeg -ss 00:01:15 -t 00:00:30 -i http://192.168.1.4:8000/video.MP4 -c copy %s", file);
        execFFmpegBinary(command.split(" "));
    }

    private void loadFFMpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    System.out.println("FAILED");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    System.out.println("My name is OK");
                    System.out.println(s);
                }

                @Override
                public void onSuccess(String s) {
                    System.out.println("My name is anothny");
                    System.out.println(s);
                }

                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command : ffmpeg "+command);
                }

                @Override
                public void onStart() {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg "+command);
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }
}
