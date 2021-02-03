package com.example.spacewars;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class Enemy extends Ship {
    boolean killed = false, gettingDamage = false;
    float speed;
    int health = 3;
    int k = 0;

    public Enemy(float xc, float yc, float rs, float rcp, int s, int cp, float speed) {
        super(xc, yc, rs, rcp, s, cp);
        this.speed = speed;
    }

    public void Move(){
        yc += speed;
    }

    public void Draw(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(sColor);
        canvas.drawCircle(xc, yc, rs, p);
        p.setColor(cpColor);
        canvas.drawCircle(xc, yc, rcp, p);
        if (gettingDamage) {
            p.setColor(MainActivity.damageColor);
            canvas.drawCircle(xc, yc, rs, p);
            k++;
            if (k == 10) {
                k = 0;
                gettingDamage = false;
            }
        }

    }


    public void damage() {
        if (health > 0) {
            health--;
            gettingDamage = true;
        }
        if (health == 0)
            killed = true;
    }
}
