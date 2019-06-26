// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.graphics.RectF;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.view.View;

public class BottomPagesView extends View
{
    private float animatedProgress;
    private String colorKey;
    private int currentPage;
    private DecelerateInterpolator decelerateInterpolator;
    private int pagesCount;
    private Paint paint;
    private float progress;
    private RectF rect;
    private int scrollPosition;
    private String selectedColorKey;
    private ViewPager viewPager;
    
    public BottomPagesView(final Context context, final ViewPager viewPager, final int pagesCount) {
        super(context);
        this.paint = new Paint(1);
        this.decelerateInterpolator = new DecelerateInterpolator();
        this.rect = new RectF();
        this.viewPager = viewPager;
        this.pagesCount = pagesCount;
    }
    
    protected void onDraw(final Canvas canvas) {
        AndroidUtilities.dp(5.0f);
        final String colorKey = this.colorKey;
        if (colorKey != null) {
            this.paint.setColor((Theme.getColor(colorKey) & 0xFFFFFF) | 0xB4000000);
        }
        else {
            this.paint.setColor(-4473925);
        }
        this.currentPage = this.viewPager.getCurrentItem();
        for (int i = 0; i < this.pagesCount; ++i) {
            if (i != this.currentPage) {
                final int n = AndroidUtilities.dp(11.0f) * i;
                this.rect.set((float)n, 0.0f, (float)(n + AndroidUtilities.dp(5.0f)), (float)AndroidUtilities.dp(5.0f));
                canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.5f), (float)AndroidUtilities.dp(2.5f), this.paint);
            }
        }
        final String selectedColorKey = this.selectedColorKey;
        if (selectedColorKey != null) {
            this.paint.setColor(Theme.getColor(selectedColorKey));
        }
        else {
            this.paint.setColor(-13851168);
        }
        final int n2 = this.currentPage * AndroidUtilities.dp(11.0f);
        if (this.progress != 0.0f) {
            if (this.scrollPosition >= this.currentPage) {
                this.rect.set((float)n2, 0.0f, n2 + AndroidUtilities.dp(5.0f) + AndroidUtilities.dp(11.0f) * this.progress, (float)AndroidUtilities.dp(5.0f));
            }
            else {
                this.rect.set(n2 - AndroidUtilities.dp(11.0f) * (1.0f - this.progress), 0.0f, (float)(n2 + AndroidUtilities.dp(5.0f)), (float)AndroidUtilities.dp(5.0f));
            }
        }
        else {
            this.rect.set((float)n2, 0.0f, (float)(n2 + AndroidUtilities.dp(5.0f)), (float)AndroidUtilities.dp(5.0f));
        }
        canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.5f), (float)AndroidUtilities.dp(2.5f), this.paint);
    }
    
    public void setColor(final String colorKey, final String selectedColorKey) {
        this.colorKey = colorKey;
        this.selectedColorKey = selectedColorKey;
    }
    
    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
        this.invalidate();
    }
    
    public void setPageOffset(final int scrollPosition, final float progress) {
        this.progress = progress;
        this.scrollPosition = scrollPosition;
        this.invalidate();
    }
}
