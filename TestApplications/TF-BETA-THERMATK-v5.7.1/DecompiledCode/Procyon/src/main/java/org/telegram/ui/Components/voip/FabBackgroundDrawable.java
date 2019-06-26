// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.voip;

import android.graphics.ColorFilter;
import android.graphics.Bitmap$Config;
import android.graphics.Rect;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class FabBackgroundDrawable extends Drawable
{
    private Paint bgPaint;
    private Bitmap shadowBitmap;
    private Paint shadowPaint;
    
    public FabBackgroundDrawable() {
        this.bgPaint = new Paint(1);
        (this.shadowPaint = new Paint()).setColor(1275068416);
    }
    
    public void draw(final Canvas canvas) {
        if (this.shadowBitmap == null) {
            this.onBoundsChange(this.getBounds());
        }
        final int min = Math.min(this.getBounds().width(), this.getBounds().height());
        final Bitmap shadowBitmap = this.shadowBitmap;
        if (shadowBitmap != null) {
            canvas.drawBitmap(shadowBitmap, (float)(this.getBounds().centerX() - this.shadowBitmap.getWidth() / 2), (float)(this.getBounds().centerY() - this.shadowBitmap.getHeight() / 2), this.shadowPaint);
        }
        final int n = min / 2;
        final float n2 = (float)n;
        canvas.drawCircle(n2, n2, (float)(n - AndroidUtilities.dp(4.0f)), this.bgPaint);
    }
    
    public int getOpacity() {
        return 0;
    }
    
    public boolean getPadding(final Rect rect) {
        final int dp = AndroidUtilities.dp(4.0f);
        rect.set(dp, dp, dp, dp);
        return true;
    }
    
    protected void onBoundsChange(final Rect rect) {
        final int min = Math.min(rect.width(), rect.height());
        if (min <= 0) {
            this.shadowBitmap = null;
            return;
        }
        this.shadowBitmap = Bitmap.createBitmap(min, min, Bitmap$Config.ALPHA_8);
        final Canvas canvas = new Canvas(this.shadowBitmap);
        final Paint paint = new Paint(1);
        paint.setShadowLayer((float)AndroidUtilities.dp(3.33333f), 0.0f, (float)AndroidUtilities.dp(0.666f), -1);
        final int n = min / 2;
        final float n2 = (float)n;
        canvas.drawCircle(n2, n2, (float)(n - AndroidUtilities.dp(4.0f)), paint);
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColor(final int color) {
        this.bgPaint.setColor(color);
        this.invalidateSelf();
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
}
