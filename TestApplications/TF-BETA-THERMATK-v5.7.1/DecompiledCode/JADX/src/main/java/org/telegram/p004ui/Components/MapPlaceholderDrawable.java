package org.telegram.p004ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.AndroidUtilities;

/* renamed from: org.telegram.ui.Components.MapPlaceholderDrawable */
public class MapPlaceholderDrawable extends Drawable {
    private Paint linePaint;
    private Paint paint = new Paint();

    public int getIntrinsicHeight() {
        return 0;
    }

    public int getIntrinsicWidth() {
        return 0;
    }

    public int getOpacity() {
        return 0;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public MapPlaceholderDrawable() {
        this.paint.setColor(-2172970);
        this.linePaint = new Paint();
        this.linePaint.setColor(-3752002);
        this.linePaint.setStrokeWidth((float) AndroidUtilities.m26dp(1.0f));
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(getBounds(), this.paint);
        int dp = AndroidUtilities.m26dp(9.0f);
        int width = getBounds().width() / dp;
        int height = getBounds().height() / dp;
        int i = getBounds().left;
        int i2 = getBounds().top;
        int i3 = 0;
        int i4 = 0;
        while (i4 < width) {
            i4++;
            float f = (float) ((dp * i4) + i);
            canvas.drawLine(f, (float) i2, f, (float) (getBounds().height() + i2), this.linePaint);
        }
        while (i3 < height) {
            i3++;
            float f2 = (float) ((dp * i3) + i2);
            canvas.drawLine((float) i, f2, (float) (getBounds().width() + i), f2, this.linePaint);
        }
    }
}
