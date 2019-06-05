// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

import android.graphics.Canvas;
import android.graphics.Paint$Style;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.graphics.Paint;

public class FpsCounter
{
    private static final Paint FPS_PAINT;
    private static final Paint FPS_PAINT_STROKE;
    private static final int ONE_SECOND = 1000;
    private int fps;
    private int frameCounter;
    private long previousTime;
    private boolean showFpsCounter;
    
    static {
        FPS_PAINT = new Paint(1);
        FPS_PAINT_STROKE = new Paint(1);
    }
    
    FpsCounter() {
        this.previousTime = SystemClock.uptimeMillis();
        configureFpsPaint();
    }
    
    private static void configureFpsPaint() {
        FpsCounter.FPS_PAINT.setTypeface(Typeface.defaultFromStyle(1));
        FpsCounter.FPS_PAINT.setTextSize(20.0f);
        FpsCounter.FPS_PAINT_STROKE.setColor(-16777216);
        FpsCounter.FPS_PAINT_STROKE.setTypeface(Typeface.defaultFromStyle(1));
        FpsCounter.FPS_PAINT_STROKE.setTextSize(20.0f);
        FpsCounter.FPS_PAINT_STROKE.setColor(-1);
        FpsCounter.FPS_PAINT_STROKE.setStyle(Paint$Style.STROKE);
        FpsCounter.FPS_PAINT_STROKE.setStrokeWidth(3.0f);
    }
    
    void draw(final Canvas canvas) {
        final long uptimeMillis = SystemClock.uptimeMillis();
        final long n = uptimeMillis - this.previousTime;
        if (n > 1000L) {
            this.fps = Math.round(this.frameCounter * 1000.0f / n);
            this.previousTime = uptimeMillis;
            this.frameCounter = 0;
        }
        canvas.drawText(String.valueOf(this.fps), 20.0f, 30.0f, FpsCounter.FPS_PAINT_STROKE);
        canvas.drawText(String.valueOf(this.fps), 20.0f, 30.0f, FpsCounter.FPS_PAINT);
        ++this.frameCounter;
    }
    
    public boolean isShowFpsCounter() {
        return this.showFpsCounter;
    }
    
    public void setFpsCounter(final boolean showFpsCounter) {
        this.showFpsCounter = showFpsCounter;
    }
}
