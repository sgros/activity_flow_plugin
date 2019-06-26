package org.telegram.p004ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.SystemClock;
import android.text.Layout;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.ScrollSlidingTextTabStrip */
public class ScrollSlidingTextTabStrip extends HorizontalScrollView {
    private int allTextWidth;
    private int animateIndicatorStartWidth;
    private int animateIndicatorStartX;
    private int animateIndicatorToWidth;
    private int animateIndicatorToX;
    private boolean animatingIndicator;
    private float animationIdicatorProgress;
    private Runnable animationRunnable = new C29341();
    private boolean animationRunning;
    private float animationTime;
    private int currentPosition;
    private ScrollSlidingTabStripDelegate delegate;
    private SparseIntArray idToPosition = new SparseIntArray(5);
    private int indicatorWidth;
    private int indicatorX;
    private CubicBezierInterpolator interpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
    private long lastAnimationTime;
    private SparseIntArray positionToId = new SparseIntArray(5);
    private SparseIntArray positionToWidth = new SparseIntArray(5);
    private int prevLayoutWidth;
    private int previousPosition;
    private int selectedTabId = -1;
    private GradientDrawable selectorDrawable = new GradientDrawable(Orientation.LEFT_RIGHT, null);
    private int tabCount;
    private LinearLayout tabsContainer;
    private boolean useSameWidth;

    /* renamed from: org.telegram.ui.Components.ScrollSlidingTextTabStrip$1 */
    class C29341 implements Runnable {
        C29341() {
        }

        public void run() {
            if (ScrollSlidingTextTabStrip.this.animatingIndicator) {
                long elapsedRealtime = SystemClock.elapsedRealtime() - ScrollSlidingTextTabStrip.this.lastAnimationTime;
                if (elapsedRealtime > 17) {
                    elapsedRealtime = 17;
                }
                ScrollSlidingTextTabStrip scrollSlidingTextTabStrip = ScrollSlidingTextTabStrip.this;
                scrollSlidingTextTabStrip.animationTime = scrollSlidingTextTabStrip.animationTime + (((float) elapsedRealtime) / 200.0f);
                ScrollSlidingTextTabStrip scrollSlidingTextTabStrip2 = ScrollSlidingTextTabStrip.this;
                scrollSlidingTextTabStrip2.setAnimationIdicatorProgress(scrollSlidingTextTabStrip2.interpolator.getInterpolation(ScrollSlidingTextTabStrip.this.animationTime));
                if (ScrollSlidingTextTabStrip.this.animationTime > 1.0f) {
                    ScrollSlidingTextTabStrip.this.animationTime = 1.0f;
                }
                if (ScrollSlidingTextTabStrip.this.animationTime < 1.0f) {
                    AndroidUtilities.runOnUIThread(ScrollSlidingTextTabStrip.this.animationRunnable);
                } else {
                    ScrollSlidingTextTabStrip.this.animatingIndicator = false;
                    ScrollSlidingTextTabStrip.this.setEnabled(true);
                    if (ScrollSlidingTextTabStrip.this.delegate != null) {
                        ScrollSlidingTextTabStrip.this.delegate.onPageScrolled(1.0f);
                    }
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ScrollSlidingTextTabStrip$ScrollSlidingTabStripDelegate */
    public interface ScrollSlidingTabStripDelegate {
        void onPageScrolled(float f);

        void onPageSelected(int i, boolean z);
    }

    public ScrollSlidingTextTabStrip(Context context) {
        super(context);
        float dpf2 = AndroidUtilities.dpf2(3.0f);
        this.selectorDrawable.setCornerRadii(new float[]{dpf2, dpf2, dpf2, dpf2, 0.0f, 0.0f, 0.0f, 0.0f});
        this.selectorDrawable.setColor(Theme.getColor(Theme.key_actionBarTabLine));
        setFillViewport(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        this.tabsContainer = new LinearLayout(context);
        this.tabsContainer.setOrientation(0);
        this.tabsContainer.setLayoutParams(new LayoutParams(-1, -1));
        addView(this.tabsContainer);
    }

    public void setDelegate(ScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate) {
        this.delegate = scrollSlidingTabStripDelegate;
    }

    public boolean isAnimatingIndicator() {
        return this.animatingIndicator;
    }

    private void setAnimationProgressInernal(TextView textView, TextView textView2, float f) {
        int color = Theme.getColor(Theme.key_actionBarTabActiveText);
        int color2 = Theme.getColor(Theme.key_actionBarTabUnactiveText);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        color = Color.alpha(color);
        int red2 = Color.red(color2);
        int green2 = Color.green(color2);
        int blue2 = Color.blue(color2);
        color2 = Color.alpha(color2);
        textView2.setTextColor(Color.argb((int) (((float) color) + (((float) (color2 - color)) * f)), (int) (((float) red) + (((float) (red2 - red)) * f)), (int) (((float) green) + (((float) (green2 - green)) * f)), (int) (((float) blue) + (((float) (blue2 - blue)) * f))));
        color = Color.argb((int) (((float) color2) + (((float) (color - color2)) * f)), (int) (((float) red2) + (((float) (red - red2)) * f)), (int) (((float) green2) + (((float) (green - green2)) * f)), (int) (((float) blue2) + (((float) (blue - blue2)) * f)));
        TextView textView3 = textView;
        textView.setTextColor(color);
        color = this.animateIndicatorStartX;
        this.indicatorX = (int) (((float) color) + (((float) (this.animateIndicatorToX - color)) * f));
        color = this.animateIndicatorStartWidth;
        this.indicatorWidth = (int) (((float) color) + (((float) (this.animateIndicatorToWidth - color)) * f));
        invalidate();
    }

    @Keep
    public void setAnimationIdicatorProgress(float f) {
        this.animationIdicatorProgress = f;
        setAnimationProgressInernal((TextView) this.tabsContainer.getChildAt(this.currentPosition), (TextView) this.tabsContainer.getChildAt(this.previousPosition), f);
        ScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate = this.delegate;
        if (scrollSlidingTabStripDelegate != null) {
            scrollSlidingTabStripDelegate.onPageScrolled(f);
        }
    }

    public void setUseSameWidth(boolean z) {
        this.useSameWidth = z;
    }

    public Drawable getSelectorDrawable() {
        return this.selectorDrawable;
    }

    public View getTabsContainer() {
        return this.tabsContainer;
    }

    public float getAnimationIdicatorProgress() {
        return this.animationIdicatorProgress;
    }

    public int getNextPageId(boolean z) {
        return this.positionToId.get(this.currentPosition + (z ? 1 : -1), -1);
    }

    public void removeTabs() {
        this.positionToId.clear();
        this.idToPosition.clear();
        this.positionToWidth.clear();
        this.tabsContainer.removeAllViews();
        this.allTextWidth = 0;
        this.tabCount = 0;
    }

    public int getTabsCount() {
        return this.tabCount;
    }

    public boolean hasTab(int i) {
        return this.idToPosition.get(i, -1) != -1;
    }

    public void addTextTab(int i, CharSequence charSequence) {
        int i2 = this.tabCount;
        this.tabCount = i2 + 1;
        if (i2 == 0 && this.selectedTabId == -1) {
            this.selectedTabId = i;
        }
        this.positionToId.put(i2, i);
        this.idToPosition.put(i, i2);
        int i3 = this.selectedTabId;
        if (i3 != -1 && i3 == i) {
            this.currentPosition = i2;
            this.prevLayoutWidth = 0;
        }
        TextView textView = new TextView(getContext());
        textView.setGravity(17);
        textView.setText(charSequence);
        textView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(Theme.key_actionBarTabSelector), 3));
        textView.setTextSize(1, 14.0f);
        textView.setSingleLine(true);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setPadding(AndroidUtilities.m26dp(8.0f), 0, AndroidUtilities.m26dp(8.0f), 0);
        textView.setOnClickListener(new C2660-$$Lambda$ScrollSlidingTextTabStrip$9m5R2Lhhk-z3JuL73ha6pd-2NRk(this, i));
        i = ((int) Math.ceil((double) textView.getPaint().measureText(charSequence, 0, charSequence.length()))) + AndroidUtilities.m26dp(16.0f);
        this.allTextWidth += i;
        this.positionToWidth.put(i2, i);
        this.tabsContainer.addView(textView, LayoutHelper.createLinear(0, -1));
    }

    public /* synthetic */ void lambda$addTextTab$0$ScrollSlidingTextTabStrip(int i, View view) {
        int indexOfChild = this.tabsContainer.indexOfChild(view);
        if (indexOfChild >= 0) {
            int i2 = this.currentPosition;
            if (indexOfChild != i2) {
                boolean z = i2 < indexOfChild;
                this.previousPosition = this.currentPosition;
                this.currentPosition = indexOfChild;
                this.selectedTabId = i;
                if (this.animatingIndicator) {
                    AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
                    this.animatingIndicator = false;
                }
                this.animationTime = 0.0f;
                this.animatingIndicator = true;
                this.animateIndicatorStartX = this.indicatorX;
                this.animateIndicatorStartWidth = this.indicatorWidth;
                TextView textView = (TextView) view;
                this.animateIndicatorToWidth = getChildWidth(textView);
                this.animateIndicatorToX = textView.getLeft() + ((textView.getMeasuredWidth() - this.animateIndicatorToWidth) / 2);
                setEnabled(false);
                AndroidUtilities.runOnUIThread(this.animationRunnable, 16);
                ScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate = this.delegate;
                if (scrollSlidingTabStripDelegate != null) {
                    scrollSlidingTabStripDelegate.onPageSelected(i, z);
                }
                scrollToChild(indexOfChild);
            }
        }
    }

    public void finishAddingTabs() {
        int childCount = this.tabsContainer.getChildCount();
        int i = 0;
        while (i < childCount) {
            TextView textView = (TextView) this.tabsContainer.getChildAt(i);
            int i2 = this.currentPosition;
            String str = Theme.key_actionBarTabActiveText;
            String str2 = Theme.key_actionBarTabUnactiveText;
            textView.setTag(i2 == i ? str : str2);
            if (this.currentPosition != i) {
                str = str2;
            }
            textView.setTextColor(Theme.getColor(str));
            i++;
        }
    }

    public int getCurrentTabId() {
        return this.selectedTabId;
    }

    public void setInitialTabId(int i) {
        this.selectedTabId = i;
    }

    public int getFirstTabId() {
        return this.positionToId.get(0, 0);
    }

    /* Access modifiers changed, original: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        boolean drawChild = super.drawChild(canvas, view, j);
        if (view == this.tabsContainer) {
            int measuredHeight = getMeasuredHeight();
            this.selectorDrawable.setBounds(this.indicatorX, measuredHeight - AndroidUtilities.dpr(4.0f), this.indicatorX + this.indicatorWidth, measuredHeight);
            this.selectorDrawable.draw(canvas);
        }
        return drawChild;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i);
        int childCount = this.tabsContainer.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.tabsContainer.getChildAt(i3).getLayoutParams();
            int i4 = this.allTextWidth;
            if (i4 > size) {
                layoutParams.weight = 0.0f;
                layoutParams.width = -2;
            } else if (this.useSameWidth) {
                layoutParams.weight = 1.0f / ((float) childCount);
                layoutParams.width = 0;
            } else {
                layoutParams.weight = (1.0f / ((float) i4)) * ((float) this.positionToWidth.get(i3));
                layoutParams.width = 0;
            }
        }
        if (this.allTextWidth > size) {
            this.tabsContainer.setWeightSum(0.0f);
        } else {
            this.tabsContainer.setWeightSum(1.0f);
        }
        super.onMeasure(i, i2);
    }

    private void scrollToChild(int i) {
        if (this.tabCount != 0) {
            TextView textView = (TextView) this.tabsContainer.getChildAt(i);
            if (textView != null) {
                int scrollX = getScrollX();
                int left = textView.getLeft();
                i = textView.getMeasuredWidth();
                if (left < scrollX) {
                    smoothScrollTo(left, 0);
                } else {
                    left += i;
                    if (left > scrollX + getWidth()) {
                        smoothScrollTo(left, 0);
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        i3 -= i;
        if (this.prevLayoutWidth != i3) {
            this.prevLayoutWidth = i3;
            if (this.animatingIndicator) {
                AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
                this.animatingIndicator = false;
                setEnabled(true);
                ScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate = this.delegate;
                if (scrollSlidingTabStripDelegate != null) {
                    scrollSlidingTabStripDelegate.onPageScrolled(1.0f);
                }
            }
            TextView textView = (TextView) this.tabsContainer.getChildAt(this.currentPosition);
            if (textView != null) {
                this.indicatorWidth = getChildWidth(textView);
                this.indicatorX = textView.getLeft() + ((textView.getMeasuredWidth() - this.indicatorWidth) / 2);
            }
        }
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        int childCount = this.tabsContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.tabsContainer.getChildAt(i).setEnabled(z);
        }
    }

    public void selectTabWithId(int i, float f) {
        int i2 = this.idToPosition.get(i, -1);
        if (i2 >= 0) {
            if (f < 0.0f) {
                f = 0.0f;
            } else if (f > 1.0f) {
                f = 1.0f;
            }
            TextView textView = (TextView) this.tabsContainer.getChildAt(this.currentPosition);
            TextView textView2 = (TextView) this.tabsContainer.getChildAt(i2);
            if (!(textView == null || textView2 == null)) {
                this.animateIndicatorStartWidth = getChildWidth(textView);
                this.animateIndicatorStartX = textView.getLeft() + ((textView.getMeasuredWidth() - this.animateIndicatorStartWidth) / 2);
                this.animateIndicatorToWidth = getChildWidth(textView2);
                this.animateIndicatorToX = textView2.getLeft() + ((textView2.getMeasuredWidth() - this.animateIndicatorToWidth) / 2);
                setAnimationProgressInernal(textView2, textView, f);
            }
            if (f >= 1.0f) {
                this.currentPosition = i2;
                this.selectedTabId = i;
            }
        }
    }

    private int getChildWidth(TextView textView) {
        Layout layout = textView.getLayout();
        if (layout != null) {
            return ((int) Math.ceil((double) layout.getLineWidth(0))) + AndroidUtilities.m26dp(2.0f);
        }
        return textView.getMeasuredWidth();
    }

    public void onPageScrolled(int i, int i2) {
        if (this.currentPosition != i) {
            this.currentPosition = i;
            if (i < this.tabsContainer.getChildCount()) {
                int i3 = 0;
                while (true) {
                    boolean z = true;
                    if (i3 >= this.tabsContainer.getChildCount()) {
                        break;
                    }
                    View childAt = this.tabsContainer.getChildAt(i3);
                    if (i3 != i) {
                        z = false;
                    }
                    childAt.setSelected(z);
                    i3++;
                }
                if (i2 != i || i <= 1) {
                    scrollToChild(i);
                } else {
                    scrollToChild(i - 1);
                }
                invalidate();
            }
        }
    }
}