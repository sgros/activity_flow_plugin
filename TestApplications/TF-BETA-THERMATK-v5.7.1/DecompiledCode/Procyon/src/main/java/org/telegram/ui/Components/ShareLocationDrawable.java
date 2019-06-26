// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class ShareLocationDrawable extends Drawable
{
    private Drawable drawable;
    private Drawable drawableLeft;
    private Drawable drawableRight;
    private boolean isSmall;
    private long lastUpdateTime;
    private float[] progress;
    
    public ShareLocationDrawable(final Context context, final boolean isSmall) {
        this.lastUpdateTime = 0L;
        this.progress = new float[] { 0.0f, -0.5f };
        this.isSmall = isSmall;
        if (isSmall) {
            this.drawable = context.getResources().getDrawable(2131165830);
            this.drawableLeft = context.getResources().getDrawable(2131165831);
            this.drawableRight = context.getResources().getDrawable(2131165832);
        }
        else {
            this.drawable = context.getResources().getDrawable(2131165281);
            this.drawableLeft = context.getResources().getDrawable(2131165282);
            this.drawableRight = context.getResources().getDrawable(2131165283);
        }
    }
    
    private void update() {
        final long currentTimeMillis = System.currentTimeMillis();
        long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final long n2 = 16L;
        if (n > 16L) {
            n = n2;
        }
        for (int i = 0; i < 2; ++i) {
            final float[] progress = this.progress;
            if (progress[i] >= 1.0f) {
                progress[i] = 0.0f;
            }
            final float[] progress2 = this.progress;
            progress2[i] += n / 1300.0f;
            if (progress2[i] > 1.0f) {
                progress2[i] = 1.0f;
            }
        }
        this.invalidateSelf();
    }
    
    public void draw(final Canvas canvas) {
        float n;
        if (this.isSmall) {
            n = 30.0f;
        }
        else {
            n = 120.0f;
        }
        final int dp = AndroidUtilities.dp(n);
        final int n2 = this.getBounds().top + (this.getIntrinsicHeight() - dp) / 2;
        final int n3 = this.getBounds().left + (this.getIntrinsicWidth() - dp) / 2;
        final Drawable drawable = this.drawable;
        drawable.setBounds(n3, n2, drawable.getIntrinsicWidth() + n3, this.drawable.getIntrinsicHeight() + n2);
        this.drawable.draw(canvas);
        for (int i = 0; i < 2; ++i) {
            final float[] progress = this.progress;
            if (progress[i] >= 0.0f) {
                final float n4 = progress[i] * 0.5f + 0.5f;
                float n5;
                if (this.isSmall) {
                    n5 = 2.5f;
                }
                else {
                    n5 = 5.0f;
                }
                final int dp2 = AndroidUtilities.dp(n5 * n4);
                float n6;
                if (this.isSmall) {
                    n6 = 6.5f;
                }
                else {
                    n6 = 18.0f;
                }
                final int dp3 = AndroidUtilities.dp(n6 * n4);
                float n7;
                if (this.isSmall) {
                    n7 = 6.0f;
                }
                else {
                    n7 = 15.0f;
                }
                final int dp4 = AndroidUtilities.dp(n7 * this.progress[i]);
                final float[] progress2 = this.progress;
                float n8;
                if (progress2[i] < 0.5f) {
                    n8 = progress2[i] / 0.5f;
                }
                else {
                    n8 = 1.0f - (progress2[i] - 0.5f) / 0.5f;
                }
                final boolean isSmall = this.isSmall;
                final float n9 = 42.0f;
                float n10;
                if (isSmall) {
                    n10 = 7.0f;
                }
                else {
                    n10 = 42.0f;
                }
                final int n11 = AndroidUtilities.dp(n10) + n3 - dp4;
                final int n12 = this.drawable.getIntrinsicHeight() / 2;
                int dp5;
                if (this.isSmall) {
                    dp5 = 0;
                }
                else {
                    dp5 = AndroidUtilities.dp(7.0f);
                }
                final int n13 = n12 + n2 - dp5;
                final Drawable drawableLeft = this.drawableLeft;
                final int n14 = (int)(n8 * 255.0f);
                drawableLeft.setAlpha(n14);
                final Drawable drawableLeft2 = this.drawableLeft;
                final int n15 = n13 - dp3;
                final int n16 = n13 + dp3;
                drawableLeft2.setBounds(n11 - dp2, n15, n11 + dp2, n16);
                this.drawableLeft.draw(canvas);
                final int intrinsicWidth = this.drawable.getIntrinsicWidth();
                float n17 = n9;
                if (this.isSmall) {
                    n17 = 7.0f;
                }
                final int n18 = intrinsicWidth + n3 - AndroidUtilities.dp(n17) + dp4;
                this.drawableRight.setAlpha(n14);
                this.drawableRight.setBounds(n18 - dp2, n15, n18 + dp2, n16);
                this.drawableRight.draw(canvas);
            }
        }
        this.update();
    }
    
    public int getIntrinsicHeight() {
        float n;
        if (this.isSmall) {
            n = 40.0f;
        }
        else {
            n = 180.0f;
        }
        return AndroidUtilities.dp(n);
    }
    
    public int getIntrinsicWidth() {
        float n;
        if (this.isSmall) {
            n = 40.0f;
        }
        else {
            n = 120.0f;
        }
        return AndroidUtilities.dp(n);
    }
    
    public int getOpacity() {
        return 0;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.drawable.setColorFilter(colorFilter);
        this.drawableLeft.setColorFilter(colorFilter);
        this.drawableRight.setColorFilter(colorFilter);
    }
}
