// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import androidx.annotation.Keep;
import android.widget.LinearLayout$LayoutParams;
import android.view.View$MeasureSpec;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.view.View$OnClickListener;
import android.graphics.Color;
import android.text.Layout;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.drawable.GradientDrawable$Orientation;
import org.telegram.messenger.AndroidUtilities;
import android.os.SystemClock;
import android.content.Context;
import android.widget.LinearLayout;
import android.graphics.drawable.GradientDrawable;
import android.util.SparseIntArray;
import android.widget.HorizontalScrollView;

public class ScrollSlidingTextTabStrip extends HorizontalScrollView
{
    private int allTextWidth;
    private int animateIndicatorStartWidth;
    private int animateIndicatorStartX;
    private int animateIndicatorToWidth;
    private int animateIndicatorToX;
    private boolean animatingIndicator;
    private float animationIdicatorProgress;
    private Runnable animationRunnable;
    private boolean animationRunning;
    private float animationTime;
    private int currentPosition;
    private ScrollSlidingTabStripDelegate delegate;
    private SparseIntArray idToPosition;
    private int indicatorWidth;
    private int indicatorX;
    private CubicBezierInterpolator interpolator;
    private long lastAnimationTime;
    private SparseIntArray positionToId;
    private SparseIntArray positionToWidth;
    private int prevLayoutWidth;
    private int previousPosition;
    private int selectedTabId;
    private GradientDrawable selectorDrawable;
    private int tabCount;
    private LinearLayout tabsContainer;
    private boolean useSameWidth;
    
    public ScrollSlidingTextTabStrip(final Context context) {
        super(context);
        this.selectedTabId = -1;
        this.interpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.positionToId = new SparseIntArray(5);
        this.idToPosition = new SparseIntArray(5);
        this.positionToWidth = new SparseIntArray(5);
        this.animationRunnable = new Runnable() {
            @Override
            public void run() {
                if (!ScrollSlidingTextTabStrip.this.animatingIndicator) {
                    return;
                }
                long n;
                if ((n = SystemClock.elapsedRealtime() - ScrollSlidingTextTabStrip.this.lastAnimationTime) > 17L) {
                    n = 17L;
                }
                final ScrollSlidingTextTabStrip this$0 = ScrollSlidingTextTabStrip.this;
                this$0.animationTime += n / 200.0f;
                final ScrollSlidingTextTabStrip this$2 = ScrollSlidingTextTabStrip.this;
                this$2.setAnimationIdicatorProgress(this$2.interpolator.getInterpolation(ScrollSlidingTextTabStrip.this.animationTime));
                if (ScrollSlidingTextTabStrip.this.animationTime > 1.0f) {
                    ScrollSlidingTextTabStrip.this.animationTime = 1.0f;
                }
                if (ScrollSlidingTextTabStrip.this.animationTime < 1.0f) {
                    AndroidUtilities.runOnUIThread(ScrollSlidingTextTabStrip.this.animationRunnable);
                }
                else {
                    ScrollSlidingTextTabStrip.this.animatingIndicator = false;
                    ScrollSlidingTextTabStrip.this.setEnabled(true);
                    if (ScrollSlidingTextTabStrip.this.delegate != null) {
                        ScrollSlidingTextTabStrip.this.delegate.onPageScrolled(1.0f);
                    }
                }
            }
        };
        this.selectorDrawable = new GradientDrawable(GradientDrawable$Orientation.LEFT_RIGHT, (int[])null);
        final float dpf2 = AndroidUtilities.dpf2(3.0f);
        this.selectorDrawable.setCornerRadii(new float[] { dpf2, dpf2, dpf2, dpf2, 0.0f, 0.0f, 0.0f, 0.0f });
        this.selectorDrawable.setColor(Theme.getColor("actionBarTabLine"));
        this.setFillViewport(true);
        this.setWillNotDraw(false);
        this.setHorizontalScrollBarEnabled(false);
        (this.tabsContainer = new LinearLayout(context)).setOrientation(0);
        this.tabsContainer.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        this.addView((View)this.tabsContainer);
    }
    
    private int getChildWidth(final TextView textView) {
        final Layout layout = textView.getLayout();
        if (layout != null) {
            return (int)Math.ceil(layout.getLineWidth(0)) + AndroidUtilities.dp(2.0f);
        }
        return textView.getMeasuredWidth();
    }
    
    private void scrollToChild(int scrollX) {
        if (this.tabCount == 0) {
            return;
        }
        final TextView textView = (TextView)this.tabsContainer.getChildAt(scrollX);
        if (textView == null) {
            return;
        }
        scrollX = this.getScrollX();
        final int left = textView.getLeft();
        final int measuredWidth = textView.getMeasuredWidth();
        if (left < scrollX) {
            this.smoothScrollTo(left, 0);
        }
        else {
            final int n = left + measuredWidth;
            if (n > scrollX + this.getWidth()) {
                this.smoothScrollTo(n, 0);
            }
        }
    }
    
    private void setAnimationProgressInernal(final TextView textView, final TextView textView2, final float n) {
        final int color = Theme.getColor("actionBarTabActiveText");
        final int color2 = Theme.getColor("actionBarTabUnactiveText");
        final int red = Color.red(color);
        final int green = Color.green(color);
        final int blue = Color.blue(color);
        final int alpha = Color.alpha(color);
        final int red2 = Color.red(color2);
        final int green2 = Color.green(color2);
        final int blue2 = Color.blue(color2);
        final int alpha2 = Color.alpha(color2);
        textView2.setTextColor(Color.argb((int)(alpha + (alpha2 - alpha) * n), (int)(red + (red2 - red) * n), (int)(green + (green2 - green) * n), (int)(blue + (blue2 - blue) * n)));
        textView.setTextColor(Color.argb((int)(alpha2 + (alpha - alpha2) * n), (int)(red2 + (red - red2) * n), (int)(green2 + (green - green2) * n), (int)(blue2 + (blue - blue2) * n)));
        final int animateIndicatorStartX = this.animateIndicatorStartX;
        this.indicatorX = (int)(animateIndicatorStartX + (this.animateIndicatorToX - animateIndicatorStartX) * n);
        final int animateIndicatorStartWidth = this.animateIndicatorStartWidth;
        this.indicatorWidth = (int)(animateIndicatorStartWidth + (this.animateIndicatorToWidth - animateIndicatorStartWidth) * n);
        this.invalidate();
    }
    
    public void addTextTab(int selectedTabId, final CharSequence text) {
        final int currentPosition = this.tabCount++;
        if (currentPosition == 0 && this.selectedTabId == -1) {
            this.selectedTabId = selectedTabId;
        }
        this.positionToId.put(currentPosition, selectedTabId);
        this.idToPosition.put(selectedTabId, currentPosition);
        final int selectedTabId2 = this.selectedTabId;
        if (selectedTabId2 != -1 && selectedTabId2 == selectedTabId) {
            this.currentPosition = currentPosition;
            this.prevLayoutWidth = 0;
        }
        final TextView textView = new TextView(this.getContext());
        textView.setGravity(17);
        textView.setText(text);
        textView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("actionBarTabSelector"), 3));
        textView.setTextSize(1, 14.0f);
        textView.setSingleLine(true);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
        textView.setOnClickListener((View$OnClickListener)new _$$Lambda$ScrollSlidingTextTabStrip$9m5R2Lhhk_z3JuL73ha6pd_2NRk(this, selectedTabId));
        selectedTabId = (int)Math.ceil(textView.getPaint().measureText(text, 0, text.length())) + AndroidUtilities.dp(16.0f);
        this.allTextWidth += selectedTabId;
        this.positionToWidth.put(currentPosition, selectedTabId);
        this.tabsContainer.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1));
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, final long n) {
        final boolean drawChild = super.drawChild(canvas, view, n);
        if (view == this.tabsContainer) {
            final int measuredHeight = this.getMeasuredHeight();
            this.selectorDrawable.setBounds(this.indicatorX, measuredHeight - AndroidUtilities.dpr(4.0f), this.indicatorX + this.indicatorWidth, measuredHeight);
            this.selectorDrawable.draw(canvas);
        }
        return drawChild;
    }
    
    public void finishAddingTabs() {
        for (int childCount = this.tabsContainer.getChildCount(), i = 0; i < childCount; ++i) {
            final TextView textView = (TextView)this.tabsContainer.getChildAt(i);
            final int currentPosition = this.currentPosition;
            final String s = "actionBarTabActiveText";
            String tag;
            if (currentPosition == i) {
                tag = "actionBarTabActiveText";
            }
            else {
                tag = "actionBarTabUnactiveText";
            }
            textView.setTag((Object)tag);
            String s2;
            if (this.currentPosition == i) {
                s2 = s;
            }
            else {
                s2 = "actionBarTabUnactiveText";
            }
            textView.setTextColor(Theme.getColor(s2));
        }
    }
    
    public float getAnimationIdicatorProgress() {
        return this.animationIdicatorProgress;
    }
    
    public int getCurrentPosition() {
        return this.currentPosition;
    }
    
    public int getCurrentTabId() {
        return this.selectedTabId;
    }
    
    public int getFirstTabId() {
        return this.positionToId.get(0, 0);
    }
    
    public int getNextPageId(final boolean b) {
        final SparseIntArray positionToId = this.positionToId;
        final int currentPosition = this.currentPosition;
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = -1;
        }
        return positionToId.get(currentPosition + n, -1);
    }
    
    public Drawable getSelectorDrawable() {
        return (Drawable)this.selectorDrawable;
    }
    
    public View getTabsContainer() {
        return (View)this.tabsContainer;
    }
    
    public int getTabsCount() {
        return this.tabCount;
    }
    
    public boolean hasTab(final int n) {
        return this.idToPosition.get(n, -1) != -1;
    }
    
    public boolean isAnimatingIndicator() {
        return this.animatingIndicator;
    }
    
    protected void onLayout(final boolean b, int prevLayoutWidth, int prevLayoutWidth2, final int n, final int n2) {
        super.onLayout(b, prevLayoutWidth, prevLayoutWidth2, n, n2);
        prevLayoutWidth2 = this.prevLayoutWidth;
        prevLayoutWidth = n - prevLayoutWidth;
        if (prevLayoutWidth2 != prevLayoutWidth) {
            this.prevLayoutWidth = prevLayoutWidth;
            if (this.animatingIndicator) {
                AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
                this.animatingIndicator = false;
                this.setEnabled(true);
                final ScrollSlidingTabStripDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.onPageScrolled(1.0f);
                }
            }
            final TextView textView = (TextView)this.tabsContainer.getChildAt(this.currentPosition);
            if (textView != null) {
                this.indicatorWidth = this.getChildWidth(textView);
                this.indicatorX = textView.getLeft() + (textView.getMeasuredWidth() - this.indicatorWidth) / 2;
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        final int size = View$MeasureSpec.getSize(n);
        for (int childCount = this.tabsContainer.getChildCount(), i = 0; i < childCount; ++i) {
            final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)this.tabsContainer.getChildAt(i).getLayoutParams();
            final int allTextWidth = this.allTextWidth;
            if (allTextWidth > size) {
                linearLayout$LayoutParams.weight = 0.0f;
                linearLayout$LayoutParams.width = -2;
            }
            else if (this.useSameWidth) {
                linearLayout$LayoutParams.weight = 1.0f / childCount;
                linearLayout$LayoutParams.width = 0;
            }
            else {
                linearLayout$LayoutParams.weight = 1.0f / allTextWidth * this.positionToWidth.get(i);
                linearLayout$LayoutParams.width = 0;
            }
        }
        if (this.allTextWidth > size) {
            this.tabsContainer.setWeightSum(0.0f);
        }
        else {
            this.tabsContainer.setWeightSum(1.0f);
        }
        super.onMeasure(n, n2);
    }
    
    public void onPageScrolled(final int currentPosition, final int n) {
        if (this.currentPosition == currentPosition) {
            return;
        }
        if ((this.currentPosition = currentPosition) >= this.tabsContainer.getChildCount()) {
            return;
        }
        int n2 = 0;
        while (true) {
            final int childCount = this.tabsContainer.getChildCount();
            boolean selected = true;
            if (n2 >= childCount) {
                break;
            }
            final View child = this.tabsContainer.getChildAt(n2);
            if (n2 != currentPosition) {
                selected = false;
            }
            child.setSelected(selected);
            ++n2;
        }
        if (n == currentPosition && currentPosition > 1) {
            this.scrollToChild(currentPosition - 1);
        }
        else {
            this.scrollToChild(currentPosition);
        }
        this.invalidate();
    }
    
    public void removeTabs() {
        this.positionToId.clear();
        this.idToPosition.clear();
        this.positionToWidth.clear();
        this.tabsContainer.removeAllViews();
        this.allTextWidth = 0;
        this.tabCount = 0;
    }
    
    public void selectTabWithId(final int selectedTabId, final float n) {
        final int value = this.idToPosition.get(selectedTabId, -1);
        if (value < 0) {
            return;
        }
        float n2;
        if (n < 0.0f) {
            n2 = 0.0f;
        }
        else {
            n2 = n;
            if (n > 1.0f) {
                n2 = 1.0f;
            }
        }
        final TextView textView = (TextView)this.tabsContainer.getChildAt(this.currentPosition);
        final TextView textView2 = (TextView)this.tabsContainer.getChildAt(value);
        if (textView != null && textView2 != null) {
            this.animateIndicatorStartWidth = this.getChildWidth(textView);
            this.animateIndicatorStartX = textView.getLeft() + (textView.getMeasuredWidth() - this.animateIndicatorStartWidth) / 2;
            this.animateIndicatorToWidth = this.getChildWidth(textView2);
            this.animateIndicatorToX = textView2.getLeft() + (textView2.getMeasuredWidth() - this.animateIndicatorToWidth) / 2;
            this.setAnimationProgressInernal(textView2, textView, n2);
        }
        if (n2 >= 1.0f) {
            this.currentPosition = value;
            this.selectedTabId = selectedTabId;
        }
    }
    
    @Keep
    public void setAnimationIdicatorProgress(final float animationIdicatorProgress) {
        this.animationIdicatorProgress = animationIdicatorProgress;
        this.setAnimationProgressInernal((TextView)this.tabsContainer.getChildAt(this.currentPosition), (TextView)this.tabsContainer.getChildAt(this.previousPosition), animationIdicatorProgress);
        final ScrollSlidingTabStripDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onPageScrolled(animationIdicatorProgress);
        }
    }
    
    public void setDelegate(final ScrollSlidingTabStripDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        for (int childCount = this.tabsContainer.getChildCount(), i = 0; i < childCount; ++i) {
            this.tabsContainer.getChildAt(i).setEnabled(b);
        }
    }
    
    public void setInitialTabId(final int selectedTabId) {
        this.selectedTabId = selectedTabId;
    }
    
    public void setUseSameWidth(final boolean useSameWidth) {
        this.useSameWidth = useSameWidth;
    }
    
    public interface ScrollSlidingTabStripDelegate
    {
        void onPageScrolled(final float p0);
        
        void onPageSelected(final int p0, final boolean p1);
    }
}
