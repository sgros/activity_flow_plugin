package org.telegram.p004ui.Components.voip;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.AndroidUtilities;

/* renamed from: org.telegram.ui.Components.voip.FabBackgroundDrawable */
public class FabBackgroundDrawable extends Drawable {
    private Paint bgPaint = new Paint(1);
    private Bitmap shadowBitmap;
    private Paint shadowPaint = new Paint();

    public int getOpacity() {
        return 0;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public FabBackgroundDrawable() {
        this.shadowPaint.setColor(1275068416);
    }

    public void draw(Canvas canvas) {
        if (this.shadowBitmap == null) {
            onBoundsChange(getBounds());
        }
        int min = Math.min(getBounds().width(), getBounds().height());
        Bitmap bitmap = this.shadowBitmap;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, (float) (getBounds().centerX() - (this.shadowBitmap.getWidth() / 2)), (float) (getBounds().centerY() - (this.shadowBitmap.getHeight() / 2)), this.shadowPaint);
        }
        min /= 2;
        float f = (float) min;
        canvas.drawCircle(f, f, (float) (min - AndroidUtilities.m26dp(4.0f)), this.bgPaint);
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect rect) {
        int min = Math.min(rect.width(), rect.height());
        if (min <= 0) {
            this.shadowBitmap = null;
            return;
        }
        this.shadowBitmap = Bitmap.createBitmap(min, min, Config.ALPHA_8);
        Canvas canvas = new Canvas(this.shadowBitmap);
        Paint paint = new Paint(1);
        paint.setShadowLayer((float) AndroidUtilities.m26dp(3.33333f), 0.0f, (float) AndroidUtilities.m26dp(0.666f), -1);
        min /= 2;
        float f = (float) min;
        canvas.drawCircle(f, f, (float) (min - AndroidUtilities.m26dp(4.0f)), paint);
    }

    public void setColor(int i) {
        this.bgPaint.setColor(i);
        invalidateSelf();
    }

    public boolean getPadding(Rect rect) {
        int dp = AndroidUtilities.m26dp(4.0f);
        rect.set(dp, dp, dp, dp);
        return true;
    }
}
