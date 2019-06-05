// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint$Style;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable$Callback;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;

class CutoutDrawable extends GradientDrawable
{
    private final RectF cutoutBounds;
    private final Paint cutoutPaint;
    private int savedLayer;
    
    CutoutDrawable() {
        this.cutoutPaint = new Paint(1);
        this.setPaintStyles();
        this.cutoutBounds = new RectF();
    }
    
    private void postDraw(final Canvas canvas) {
        if (!this.useHardwareLayer(this.getCallback())) {
            canvas.restoreToCount(this.savedLayer);
        }
    }
    
    private void preDraw(final Canvas canvas) {
        final Drawable$Callback callback = this.getCallback();
        if (this.useHardwareLayer(callback)) {
            ((View)callback).setLayerType(2, (Paint)null);
        }
        else {
            this.saveCanvasLayer(canvas);
        }
    }
    
    private void saveCanvasLayer(final Canvas canvas) {
        if (Build$VERSION.SDK_INT >= 21) {
            this.savedLayer = canvas.saveLayer(0.0f, 0.0f, (float)canvas.getWidth(), (float)canvas.getHeight(), (Paint)null);
        }
        else {
            this.savedLayer = canvas.saveLayer(0.0f, 0.0f, (float)canvas.getWidth(), (float)canvas.getHeight(), (Paint)null, 31);
        }
    }
    
    private void setPaintStyles() {
        this.cutoutPaint.setStyle(Paint$Style.FILL_AND_STROKE);
        this.cutoutPaint.setColor(-1);
        this.cutoutPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_OUT));
    }
    
    private boolean useHardwareLayer(final Drawable$Callback drawable$Callback) {
        return drawable$Callback instanceof View;
    }
    
    public void draw(final Canvas canvas) {
        this.preDraw(canvas);
        super.draw(canvas);
        canvas.drawRect(this.cutoutBounds, this.cutoutPaint);
        this.postDraw(canvas);
    }
    
    boolean hasCutout() {
        return this.cutoutBounds.isEmpty() ^ true;
    }
    
    void removeCutout() {
        this.setCutout(0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    void setCutout(final float n, final float n2, final float n3, final float n4) {
        if (n != this.cutoutBounds.left || n2 != this.cutoutBounds.top || n3 != this.cutoutBounds.right || n4 != this.cutoutBounds.bottom) {
            this.cutoutBounds.set(n, n2, n3, n4);
            this.invalidateSelf();
        }
    }
    
    void setCutout(final RectF rectF) {
        this.setCutout(rectF.left, rectF.top, rectF.right, rectF.bottom);
    }
}
