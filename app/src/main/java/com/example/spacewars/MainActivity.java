package com.example.spacewars;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    float downX, downY;
    static DisplayMetrics dm;
    static int damageColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DrawView drawView = new DrawView(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dm = getResources().getDisplayMetrics();
        int MIN_DISTANCE = dm.widthPixels / 8;
        damageColor = getColor(R.color.damage);
        drawView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean fire = true;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        downX = event.getX();
                        downY = event.getY();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        float upX = event.getX();
                        float upY = event.getY();
                        float deltaX = downX - upX;
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            if (deltaX < 0) { // Свайп вправо
                                Game.tx = dm.widthPixels - Game.player.rs;
                                Game.player.target = true;
                                return true;
                            }
                            if (deltaX > 0) { // Свайп вправо
                                Game.tx = Game.player.rs;
                                Game.player.target = true;
                                return true;
                            }
                            fire = false;
                        }
                    }
                    if (fire)
                        Game.player.Fire();
                }
                return false;
            }
        });
        setContentView(drawView);
    }

}