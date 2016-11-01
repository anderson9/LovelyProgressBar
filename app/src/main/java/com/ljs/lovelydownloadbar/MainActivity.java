package com.ljs.lovelydownloadbar;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljs.lovelyprogressbar.LovelyProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    LovelyProgressBar mloadbar;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mloadbar = (LovelyProgressBar) findViewById(R.id.loadbar);
       mloadbar.setOnLoadListener(new LovelyProgressBar.OnLoadListener() {
           @Override
           public void onAnimSuccess() {
               Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onAnimError() {
               Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();

           }
       });

    }

    public void clickbt(View view) {
        switch (view.getId()) {
            case R.id.load:
                progress=0;
                mloadbar.startload();
                new CountDownTimer(11000, 100) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        mloadbar.setProgress(progress++);
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();

                break;
            case R.id.err:
                mloadbar.errorLoad();
                break;
            case R.id.finish:
                mloadbar.succesLoad();
                break;
        }
    }


}