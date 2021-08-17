package com.example.easy_attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

public class StartingAnimation extends AppCompatActivity {
    TextView easyTextView,attendanceTextView;
    LottieAnimationView lottieAnimationView;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_animation);

        easyTextView = (TextView) findViewById(R.id.easy_textView);
        attendanceTextView=(TextView) findViewById(R.id.attendance_textView);
        lottieAnimationView = findViewById(R.id.lottie_student);

        lottieAnimationView.animate().rotationY(600).translationY(-1400).setDuration(1500).setStartDelay(1500);
        easyTextView.animate().rotationY(300).translationY(1400).setDuration(1500).setStartDelay(1500);
        attendanceTextView.animate().rotationY(300).translationY(1400).setDuration(1500).setStartDelay(1500);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                        Intent i = new Intent(StartingAnimation.this,LogIn.class);
                        startActivity(i);
                        finish();
            }
        },3000);

    }
}