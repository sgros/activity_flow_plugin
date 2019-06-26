// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class MapPlaceholderDrawable extends Drawable
{
    private Paint linePaint;
    private Paint paint;
    
    public MapPlaceholderDrawable() {
        (this.paint = new Paint()).setColor(-2172970);
        (this.linePaint = new Paint()).setColor(-3752002);
        this.linePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0f));
    }
    
    public void draw(final Canvas canvas) {
        canvas.drawRect(this.getBounds(), this.paint);
        final int dp = AndroidUtilities.dp(9.0f);
        final int n = this.getBounds().width() / dp;
        final int n2 = this.getBounds().height() / dp;
        final int left = this.getBounds().left;
        final int top = this.getBounds().top;
        final int n3 = 0;
        int n4 = 0;
        int i;
        while (true) {
            i = n3;
            if (n4 >= n) {
                break;
            }
            ++n4;
            final float n5 = (float)(dp * n4 + left);
            canvas.drawLine(n5, (float)top, n5, (float)(this.getBounds().height() + top), this.linePaint);
        }
        while (i < n2) {
            final float n6 = (float)left;
            ++i;
            final float n7 = (float)(dp * i + top);
            canvas.drawLine(n6, n7, (float)(this.getBounds().width() + left), n7, this.linePaint);
        }
    }
    
    public int getIntrinsicHeight() {
        return 0;
    }
    
    public int getIntrinsicWidth() {
        return 0;
    }
    
    public int getOpacity() {
        return 0;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
}
