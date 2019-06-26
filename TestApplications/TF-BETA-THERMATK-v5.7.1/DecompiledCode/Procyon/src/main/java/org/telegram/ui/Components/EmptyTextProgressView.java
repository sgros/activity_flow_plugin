// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class EmptyTextProgressView extends FrameLayout
{
    private boolean inLayout;
    private RadialProgressView progressBar;
    private boolean showAtCenter;
    private TextView textView;
    
    public EmptyTextProgressView(final Context context) {
        super(context);
        (this.progressBar = new RadialProgressView(context)).setVisibility(4);
        this.addView((View)this.progressBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f));
        (this.textView = new TextView(context)).setTextSize(1, 20.0f);
        this.textView.setTextColor(Theme.getColor("emptyListPlaceholder"));
        this.textView.setGravity(17);
        this.textView.setVisibility(4);
        this.textView.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.textView.setText((CharSequence)LocaleController.getString("NoResult", 2131559943));
        this.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f));
        this.setOnTouchListener((View$OnTouchListener)_$$Lambda$EmptyTextProgressView$AeVTSCBshpCl6wf4siSABV33AKw.INSTANCE);
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    protected void onLayout(final boolean b, final int n, int i, final int n2, int n3) {
        this.inLayout = true;
        final int n4 = n3 - i;
        int childCount;
        View child;
        int n5;
        for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                n5 = (n2 - n - child.getMeasuredWidth()) / 2;
                if (this.showAtCenter) {
                    n3 = (n4 / 2 - child.getMeasuredHeight()) / 2;
                }
                else {
                    n3 = (n4 - child.getMeasuredHeight()) / 2;
                }
                child.layout(n5, n3, child.getMeasuredWidth() + n5, child.getMeasuredHeight() + n3);
            }
        }
        this.inLayout = false;
    }
    
    public void requestLayout() {
        if (!this.inLayout) {
            super.requestLayout();
        }
    }
    
    public void setProgressBarColor(final int progressColor) {
        this.progressBar.setProgressColor(progressColor);
    }
    
    public void setShowAtCenter(final boolean showAtCenter) {
        this.showAtCenter = showAtCenter;
    }
    
    public void setText(final String text) {
        this.textView.setText((CharSequence)text);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
    
    public void setTextSize(final int n) {
        this.textView.setTextSize(1, (float)n);
    }
    
    public void setTopImage(final int n) {
        if (n == 0) {
            this.textView.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
        }
        else {
            final Drawable mutate = this.getContext().getResources().getDrawable(n).mutate();
            if (mutate != null) {
                mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("emptyListPlaceholder"), PorterDuff$Mode.MULTIPLY));
            }
            this.textView.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, mutate, (Drawable)null, (Drawable)null);
            this.textView.setCompoundDrawablePadding(AndroidUtilities.dp(1.0f));
        }
    }
    
    public void showProgress() {
        this.textView.setVisibility(4);
        this.progressBar.setVisibility(0);
    }
    
    public void showTextView() {
        this.textView.setVisibility(0);
        this.progressBar.setVisibility(4);
    }
}
