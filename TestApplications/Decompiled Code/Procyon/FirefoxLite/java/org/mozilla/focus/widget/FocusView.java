// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.support.v4.content.ContextCompat;
import android.graphics.Path$FillType;
import android.graphics.Path$Direction;
import android.graphics.Canvas;
import org.mozilla.focus.utils.ViewUtils;
import android.app.Activity;
import kotlin.jvm.internal.Intrinsics;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public final class FocusView extends View
{
    private int centerX;
    private int centerY;
    private final Path path;
    private int radius;
    private int statusBarOffset;
    private final Paint transparentPaint;
    
    public FocusView(final Context context, final int centerX, final int centerY, final int radius) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context);
        this.transparentPaint = new Paint();
        this.path = new Path();
        this.centerX = centerX;
        this.centerY = centerY;
        this.statusBarOffset = ViewUtils.getStatusBarHeight((Activity)context);
        this.radius = radius;
        this.initPaints();
    }
    
    private final void initPaints() {
        this.transparentPaint.setColor(0);
        this.transparentPaint.setStrokeWidth(10.0f);
    }
    
    protected void onDraw(final Canvas canvas) {
        Intrinsics.checkParameterIsNotNull(canvas, "canvas");
        super.onDraw(canvas);
        this.path.reset();
        this.path.addCircle((float)this.centerX, (float)(this.centerY - this.statusBarOffset), (float)this.radius, Path$Direction.CW);
        this.path.setFillType(Path$FillType.INVERSE_EVEN_ODD);
        canvas.drawCircle((float)this.centerX, (float)(this.centerY - this.statusBarOffset), (float)this.radius, this.transparentPaint);
        canvas.clipPath(this.path);
        canvas.drawColor(ContextCompat.getColor(this.getContext(), 2131099821));
    }
}
