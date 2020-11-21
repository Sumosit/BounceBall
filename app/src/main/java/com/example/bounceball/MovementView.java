package com.example.bounceball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MovementView extends SurfaceView implements SurfaceHolder.Callback {
    private int xPos;
    private int yPos;

    private int xVel;
    private int yVel;

    private int width;
    private int height;

    private int circleRadius;
    private Paint circlePaint;

    UpdateThread updateThread;

    public MovementView(Context context) {
        super(context);
        getHolder().addCallback(this);

        circleRadius = 60;
        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);

        xVel = 4;
        yVel = 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawCircle(xPos, yPos, circleRadius, circlePaint);
    }

    public void updatePhysics() {

        xPos += xVel;
        yPos += yVel;

        if (yPos - circleRadius < 0 || yPos + circleRadius > height) {
            if (yPos - circleRadius < 0) {
                yPos = circleRadius;
            } else {
                yPos = height - circleRadius;
            }
            yVel *= -1;
        }

        if (xPos - circleRadius < 0 || xPos + circleRadius > width) {
            if (xPos - circleRadius < 0) {
                xPos = circleRadius;
            } else {
                xPos = width - circleRadius;
            }
            xVel *= -1;
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {

        Rect surfaceFrame = holder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();

        xPos = width / 2;
        yPos = circleRadius;

        updateThread = new UpdateThread(this);
        updateThread.setRunning(true);
        updateThread.start();
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;

        updateThread.setRunning(false);
        while (retry) {
            try {
                updateThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }
}