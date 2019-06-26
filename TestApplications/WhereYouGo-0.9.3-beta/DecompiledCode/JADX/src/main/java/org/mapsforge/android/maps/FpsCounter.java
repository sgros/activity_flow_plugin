package org.mapsforge.android.maps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.SystemClock;

public class FpsCounter {
    private static final Paint FPS_PAINT = new Paint(1);
    private static final Paint FPS_PAINT_STROKE = new Paint(1);
    private static final int ONE_SECOND = 1000;
    private int fps;
    private int frameCounter;
    private long previousTime = SystemClock.uptimeMillis();
    private boolean showFpsCounter;

    private static void configureFpsPaint() {
        FPS_PAINT.setTypeface(Typeface.defaultFromStyle(1));
        FPS_PAINT.setTextSize(20.0f);
        FPS_PAINT_STROKE.setColor(-16777216);
        FPS_PAINT_STROKE.setTypeface(Typeface.defaultFromStyle(1));
        FPS_PAINT_STROKE.setTextSize(20.0f);
        FPS_PAINT_STROKE.setColor(-1);
        FPS_PAINT_STROKE.setStyle(Style.STROKE);
        FPS_PAINT_STROKE.setStrokeWidth(3.0f);
    }

    FpsCounter() {
        configureFpsPaint();
    }

    public boolean isShowFpsCounter() {
        return this.showFpsCounter;
    }

    public void setFpsCounter(boolean showFpsCounter) {
        this.showFpsCounter = showFpsCounter;
    }

    /* Access modifiers changed, original: 0000 */
    public void draw(Canvas canvas) {
        long currentTime = SystemClock.uptimeMillis();
        long elapsedTime = currentTime - this.previousTime;
        if (elapsedTime > 1000) {
            this.fps = Math.round((((float) this.frameCounter) * 1000.0f) / ((float) elapsedTime));
            this.previousTime = currentTime;
            this.frameCounter = 0;
        }
        canvas.drawText(String.valueOf(this.fps), 20.0f, 30.0f, FPS_PAINT_STROKE);
        canvas.drawText(String.valueOf(this.fps), 20.0f, 30.0f, FPS_PAINT);
        this.frameCounter++;
    }
}
