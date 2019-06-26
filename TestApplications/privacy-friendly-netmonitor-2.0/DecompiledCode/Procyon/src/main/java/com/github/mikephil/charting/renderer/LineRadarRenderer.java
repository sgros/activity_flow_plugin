// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import android.graphics.drawable.Drawable;
import android.graphics.Paint$Style;
import android.graphics.Path;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;

public abstract class LineRadarRenderer extends LineScatterCandleRadarRenderer
{
    public LineRadarRenderer(final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
    }
    
    private boolean clipPathSupported() {
        return Utils.getSDKInt() >= 18;
    }
    
    protected void drawFilledPath(final Canvas canvas, final Path path, int color, int color2) {
        color = ((color & 0xFFFFFF) | color2 << 24);
        if (this.clipPathSupported()) {
            color2 = canvas.save();
            canvas.clipPath(path);
            canvas.drawColor(color);
            canvas.restoreToCount(color2);
        }
        else {
            final Paint$Style style = this.mRenderPaint.getStyle();
            color2 = this.mRenderPaint.getColor();
            this.mRenderPaint.setStyle(Paint$Style.FILL);
            this.mRenderPaint.setColor(color);
            canvas.drawPath(path, this.mRenderPaint);
            this.mRenderPaint.setColor(color2);
            this.mRenderPaint.setStyle(style);
        }
    }
    
    protected void drawFilledPath(final Canvas canvas, final Path path, final Drawable drawable) {
        if (this.clipPathSupported()) {
            final int save = canvas.save();
            canvas.clipPath(path);
            drawable.setBounds((int)this.mViewPortHandler.contentLeft(), (int)this.mViewPortHandler.contentTop(), (int)this.mViewPortHandler.contentRight(), (int)this.mViewPortHandler.contentBottom());
            drawable.draw(canvas);
            canvas.restoreToCount(save);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fill-drawables not (yet) supported below API level 18, this code was run on API level ");
        sb.append(Utils.getSDKInt());
        sb.append(".");
        throw new RuntimeException(sb.toString());
    }
}
