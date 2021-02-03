package com.example.spacewars;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;


public class DrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private Handler spawn = new Handler();
    float rcp, rs, x, y, yce, xce;
    Context context;
    private volatile boolean running = true;//флаг для остановки потока

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.context = context;
        DisplayMetrics displaymetrics = MainActivity.dm;
        y = displaymetrics.heightPixels;
        x = displaymetrics.widthPixels;
        float xc = x / 2;
        float yc = y - y / 5;
        yce = 0;
        rcp = x / 10;
        rs = x / 7;
        Game.player = new Ship(xc, yc, rs, rcp,
                context.getResources().getColor(R.color.player_ship_color),
                context.getResources().getColor(R.color.cockpit_color));
        spawn.removeCallbacks(enemySpawner);
        spawn.postDelayed(enemySpawner, 100);
    }

    public void requestStop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    Paint p = new Paint();
                    p.setSubpixelText(true);
                    canvas.drawColor(Color.BLACK);
                    p.setStyle(Paint.Style.FILL);
                    p.setTextSize(50);
                    p.setColor(Color.WHITE);
                    canvas.drawText("Score: " + String.valueOf(Game.player.score), x / 20, y / 30, p);
                    Game.player.Draw(canvas);
                    Game.player.Move(Game.tx);
                    for (Enemy e: Game.enemies) {
                        if (!e.killed) {
                            e.Draw(canvas);
                            e.Move();
                        }
                        if (e.yc >= y) {
                            e.killed = true;
                            Game.enemies.remove(e);
                            if (Game.spawnTime > 1000)
                                Game.spawnTime -= 1000;
                            if (Game.speed < 20)
                                Game.speed += 1.5;
                        }
                    }
                    for (Enemy e: Game.enemies) {
                        for (Bullet b: Game.player.bullets) {
                            if ((!b.destroyed) && (b.r + e.rs >= Math.sqrt(Math.pow((e.xc - b.x), 2) + Math.pow((e.yc - b.y), 2)))) {
                                e.damage();
                                b.destroyed = true;
                                Game.player.bullets.remove(b);
                                if (e.killed) {
                                    Game.enemies.remove(e);
                                    Game.player.score += 100;
                                    if (Game.spawnTime > 1000)
                                        Game.spawnTime -= 500;
                                    if (Game.speed < 20)
                                        Game.speed += 0.5;
                                }
                            }
                        }
                    }

                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private Runnable enemySpawner = new Runnable() {
        @Override
        public void run() {
            int pos = (int) (Math.random() * 2);
            if (pos == 0)
                xce = rs;
            else
                xce = x - rs;
            yce = 2 * rs;
            Enemy enemy = new Enemy(xce, yce, rs, rcp,
                    context.getResources().getColor(R.color.enemy_ship_color),
                    context.getResources().getColor(R.color.cockpit_color), Game.speed);
            Game.enemies.add(enemy);
            spawn.postDelayed(this, Game.spawnTime);
        }
    };
}
