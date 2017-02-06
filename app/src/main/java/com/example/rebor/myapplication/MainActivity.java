package com.example.rebor.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import android.net.Uri;

public class MainActivity extends AppCompatActivity implements OnPreparedListener, OnErrorListener, OnCompletionListener {

    private Button mBtnMediaPlayPause,mBtnMediaStop,mBtnMediaGoto;

    private ToggleButton mBtnMediaRepeat;


    private EditText mEdtMediaGoto;

    private MediaPlayer mMediaPlayer = null;

    private Boolean mbIsInitial = true;

    @Override
    protected void onStop() {
        super.onStop();

        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        mBtnMediaPlayPause.setText("Play");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        mp.release();
        mp = null;

        Toast.makeText(MainActivity.this,"Erro,Stop play",Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.seekTo(0);
        mp.start();

        Toast.makeText(MainActivity.this,"Start play",Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnMediaPlayPause = (Button)findViewById(R.id.btnMediaPlayPause);
        mBtnMediaStop = (Button)findViewById(R.id.btnMediaStop);
        mBtnMediaRepeat = (ToggleButton) findViewById(R.id.btnMediaRepeat);
        mBtnMediaGoto = (Button) findViewById(R.id.btnMediaGoto);

        mEdtMediaGoto = (EditText) findViewById(R.id.edtMediaGoto);

        mBtnMediaPlayPause.setOnClickListener(btnMediaPlayPauseOnClick);
        mBtnMediaStop.setOnClickListener(btnMediaStopOnClick);
        mBtnMediaRepeat.setOnClickListener(btnMediaRepeatOnClick);
        mBtnMediaGoto.setOnClickListener(btnMediaGotoOnClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer = new MediaPlayer();

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.butterflies);

        try {

            mMediaPlayer.setDataSource(this,uri);
        }catch (Exception e) {

            Toast.makeText(MainActivity.this,"Music files are wrong!",Toast.LENGTH_LONG).show();
        }
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    private View.OnClickListener btnMediaPlayPauseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mMediaPlayer.isPlaying()) {

                mBtnMediaPlayPause.setText("Play");
                mMediaPlayer.pause();
            }else {

                mBtnMediaPlayPause.setText("Pause");
                if (mbIsInitial) {
                    mMediaPlayer.prepareAsync();
                    mbIsInitial = false;
                }else
                    mMediaPlayer.start();
            }
        }
    };

    private View.OnClickListener btnMediaStopOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mMediaPlayer.stop();

            mbIsInitial = true;
            mBtnMediaPlayPause.setText("Play");
        }
    };

    private View.OnClickListener btnMediaRepeatOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (((ToggleButton)v).isChecked())
                mMediaPlayer.setLooping(true);
            else
                mMediaPlayer.setLooping(false);
        }
    };


    private View.OnClickListener btnMediaGotoOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mEdtMediaGoto.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this,
                        "Please key in pos where to play ( In second)",
                        Toast.LENGTH_LONG).show();
                return;
            }

            int seconds = Integer.parseInt(mEdtMediaGoto.getText().toString());
            mMediaPlayer.seekTo(seconds * 1000);
        }
    };

}
