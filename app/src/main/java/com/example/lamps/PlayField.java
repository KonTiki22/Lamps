package com.example.lamps;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

 class Lamp {
     float x;
     float y;
     float r;
        }

public class PlayField extends View {

    int padding = 20;
    int width;
    int height;
    float step;
    int r;
    int count = 0;
    int score = 50;
    int sumScore = 0;
    int n, m;
    Paint paint = new Paint();
    float touchX,  touchY;
    boolean arrayLamps[][];
    public PlayField(Context context, int n) {
        super(context);
        this.n = n;
        fillLampsArray();
    }

    void fillLampsArray() {
        arrayLamps = new boolean[n][n];
        Random random = new Random();
        for (int i = 0; i < arrayLamps.length; i++) {
            for (int j = 0; j < arrayLamps[i].length; j++) {
                boolean flag = random.nextBoolean();
                if(flag) arrayLamps[i][j] = true;
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = event.getX();
            touchY = event.getY();
        }
        if(n <=5) {
            calculation();
        }
        return super.onTouchEvent(event);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(3);
        width = canvas.getWidth();
        height = canvas.getHeight();
        step = (width-padding*2)/arrayLamps[0].length;
        r = (int) (step/2 - padding/2);
        float x = 0;
        float y = 0;

        if(checkWin()) {
            Paint fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            fontPaint.setTextSize(40);
            fontPaint.setStyle(Paint.Style.STROKE);
            sumScore += score;
            canvas.drawText("Уровень " + (n-2), padding, 200,  fontPaint);
            canvas.drawText("Поздравляем, вы выиграли!", padding, 300,  fontPaint);
            canvas.drawText("Cчет за уровень: " + score, padding, 400,  fontPaint);
            canvas.drawText("Общий счет: " + sumScore, padding, 500,  fontPaint);
            if(n < 5) {
                canvas.drawText("Для продолжения коснитесь экрана", padding, 600,  fontPaint);
            }
            count = 0;
            score = 50;
            n++;
            fillLampsArray();

        }
        else {
            for (int i = 0; i < arrayLamps.length; i++) {
                for (int j = 0; j < arrayLamps[i].length; j++) {
                    if(arrayLamps[i][j] != false) {
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    } else {
                        paint.setStyle(Paint.Style.STROKE);
                    }
                    x =  step * j + step/2 + padding;
                    y = step * i + step/2 + padding;

                    canvas.drawCircle(x,y,r,paint);

                }
            }
        }

    }

    boolean checkWin() {
        int sum = 0;
        for (boolean[] i: arrayLamps) {
            for(boolean j: i) {
                sum += j? 1: 0;
            }
        }
        if(sum == 0 || sum ==  arrayLamps.length*arrayLamps[0].length) {
            return true;
        }
        return false;
    }

    void calculation() {
        int i,j;
        float x,y;
        j = Double.valueOf(touchX).intValue() / (2 * r + padding);
        i = Double.valueOf(touchY).intValue() / (2 * r + padding);

        if (i < arrayLamps.length && j < arrayLamps[0].length) {
            x =  j*(2*r+padding) + padding + r;
            y = i*(2*r+padding) + padding + r;
            if(Math.pow(x - touchX, 2) + Math.pow(y-touchY,2) <= r*r) {
                count += 1;
                score = score > 1? score - 1: 1;
                //arrayLamps[i][j] = !arrayLamps[i][j];
                for (int k = Math.max(j-1,0); k <= Math.min(arrayLamps[0].length-1, j+1); k++) {
                    arrayLamps[i][k] = !arrayLamps[i][k];
                }
                for (int k = Math.max(i-1,0); k <= Math.min(arrayLamps.length-1, i+1); k++) {
                    if(k != i) arrayLamps[k][j] = !arrayLamps[k][j];
                }
                invalidate();
            }
        }
    }

}
