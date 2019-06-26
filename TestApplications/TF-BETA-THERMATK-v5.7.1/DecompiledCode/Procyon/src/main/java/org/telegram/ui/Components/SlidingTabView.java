// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.view.View;
import android.view.View$OnClickListener;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Typeface;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

public class SlidingTabView extends LinearLayout
{
    private float animateTabXTo;
    private SlidingTabViewDelegate delegate;
    private DecelerateInterpolator interpolator;
    private Paint paint;
    private int selectedTab;
    private long startAnimationTime;
    private float startAnimationX;
    private int tabCount;
    private float tabWidth;
    private float tabX;
    private long totalAnimationDiff;
    
    public SlidingTabView(final Context context) {
        super(context);
        this.selectedTab = 0;
        this.tabCount = 0;
        this.tabWidth = 0.0f;
        this.tabX = 0.0f;
        this.animateTabXTo = 0.0f;
        this.paint = new Paint();
        this.startAnimationTime = 0L;
        this.totalAnimationDiff = 0L;
        this.startAnimationX = 0.0f;
        this.setOrientation(0);
        this.setWeightSum(100.0f);
        this.paint.setColor(-1);
        this.setWillNotDraw(false);
        this.interpolator = new DecelerateInterpolator();
    }
    
    private void animateToTab(final int n) {
        this.animateTabXTo = n * this.tabWidth;
        this.startAnimationX = this.tabX;
        this.totalAnimationDiff = 0L;
        this.startAnimationTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    private void didSelectTab(final int selectedTab) {
        if (this.selectedTab == selectedTab) {
            return;
        }
        this.animateToTab(this.selectedTab = selectedTab);
        final SlidingTabViewDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.didSelectTab(selectedTab);
        }
    }
    
    public void addTextTab(final int n, final String text) {
        final TextView textView = new TextView(this.getContext());
        textView.setText((CharSequence)text);
        textView.setFocusable(true);
        textView.setGravity(17);
        textView.setSingleLine();
        textView.setTextColor(-1);
        textView.setTextSize(1, 14.0f);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
        textView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                SlidingTabView.this.didSelectTab(n);
            }
        });
        this.addView((View)textView);
        final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)textView.getLayoutParams();
        layoutParams.height = -1;
        layoutParams.width = 0;
        layoutParams.weight = 50.0f;
        textView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        ++this.tabCount;
    }
    
    public int getSeletedTab() {
        return this.selectedTab;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.tabX != this.animateTabXTo) {
            final long currentTimeMillis = System.currentTimeMillis();
            final long startAnimationTime = this.startAnimationTime;
            this.startAnimationTime = System.currentTimeMillis();
            this.totalAnimationDiff += currentTimeMillis - startAnimationTime;
            final long totalAnimationDiff = this.totalAnimationDiff;
            if (totalAnimationDiff > 200L) {
                this.totalAnimationDiff = 200L;
                this.tabX = this.animateTabXTo;
            }
            else {
                this.tabX = this.startAnimationX + this.interpolator.getInterpolation(totalAnimationDiff / 200.0f) * (this.animateTabXTo - this.startAnimationX);
                this.invalidate();
            }
        }
        canvas.drawRect(this.tabX, (float)(this.getHeight() - AndroidUtilities.dp(2.0f)), this.tabX + this.tabWidth, (float)this.getHeight(), this.paint);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.tabWidth = (n3 - n) / (float)this.tabCount;
        final float n5 = this.tabWidth * this.selectedTab;
        this.tabX = n5;
        this.animateTabXTo = n5;
    }
    
    public void setDelegate(final SlidingTabViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public interface SlidingTabViewDelegate
    {
        void didSelectTab(final int p0);
    }
}
