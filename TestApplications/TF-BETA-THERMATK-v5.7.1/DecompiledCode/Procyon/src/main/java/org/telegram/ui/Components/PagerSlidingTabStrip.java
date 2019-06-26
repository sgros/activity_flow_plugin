// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.View$MeasureSpec;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.graphics.Canvas;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.graphics.Paint$Style;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.LinearLayout;
import android.graphics.Paint;
import androidx.viewpager.widget.ViewPager;
import android.widget.LinearLayout$LayoutParams;
import android.widget.HorizontalScrollView;

public class PagerSlidingTabStrip extends HorizontalScrollView
{
    private int currentPosition;
    private float currentPositionOffset;
    private LinearLayout$LayoutParams defaultTabLayoutParams;
    public ViewPager.OnPageChangeListener delegatePageListener;
    private int dividerPadding;
    private int indicatorColor;
    private int indicatorHeight;
    private int lastScrollX;
    private final PageListener pageListener;
    private ViewPager pager;
    private Paint rectPaint;
    private int scrollOffset;
    private boolean shouldExpand;
    private int tabCount;
    private int tabPadding;
    private LinearLayout tabsContainer;
    private int underlineColor;
    private int underlineHeight;
    
    public PagerSlidingTabStrip(final Context context) {
        super(context);
        this.pageListener = new PageListener();
        this.currentPosition = 0;
        this.currentPositionOffset = 0.0f;
        this.indicatorColor = -10066330;
        this.underlineColor = 436207616;
        this.shouldExpand = false;
        this.scrollOffset = AndroidUtilities.dp(52.0f);
        this.indicatorHeight = AndroidUtilities.dp(8.0f);
        this.underlineHeight = AndroidUtilities.dp(2.0f);
        this.dividerPadding = AndroidUtilities.dp(12.0f);
        this.tabPadding = AndroidUtilities.dp(24.0f);
        this.lastScrollX = 0;
        this.setFillViewport(true);
        this.setWillNotDraw(false);
        (this.tabsContainer = new LinearLayout(context)).setOrientation(0);
        this.tabsContainer.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        this.addView((View)this.tabsContainer);
        (this.rectPaint = new Paint()).setAntiAlias(true);
        this.rectPaint.setStyle(Paint$Style.FILL);
        this.defaultTabLayoutParams = new LinearLayout$LayoutParams(-2, -1);
    }
    
    private void addIconTab(final int n, final Drawable imageDrawable, final CharSequence contentDescription) {
        final ImageView imageView = new ImageView(this.getContext()) {
            protected void onDraw(final Canvas canvas) {
                super.onDraw(canvas);
                if (PagerSlidingTabStrip.this.pager.getAdapter() instanceof IconTabProvider) {
                    ((IconTabProvider)PagerSlidingTabStrip.this.pager.getAdapter()).customOnDraw(canvas, n);
                }
            }
        };
        boolean selected = true;
        imageView.setFocusable(true);
        imageView.setImageDrawable(imageDrawable);
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        imageView.setOnClickListener((View$OnClickListener)new _$$Lambda$PagerSlidingTabStrip$uPwWgO9fi9vraIMG_OGzLnle64Y(this, n));
        this.tabsContainer.addView((View)imageView);
        if (n != this.currentPosition) {
            selected = false;
        }
        imageView.setSelected(selected);
        imageView.setContentDescription(contentDescription);
    }
    
    private void scrollToChild(int lastScrollX, final int n) {
        if (this.tabCount == 0) {
            return;
        }
        final int n2 = this.tabsContainer.getChildAt(lastScrollX).getLeft() + n;
        Label_0039: {
            if (lastScrollX <= 0) {
                lastScrollX = n2;
                if (n <= 0) {
                    break Label_0039;
                }
            }
            lastScrollX = n2 - this.scrollOffset;
        }
        if (lastScrollX != this.lastScrollX) {
            this.scrollTo(this.lastScrollX = lastScrollX, 0);
        }
    }
    
    private void updateTabStyles() {
        for (int i = 0; i < this.tabCount; ++i) {
            final View child = this.tabsContainer.getChildAt(i);
            child.setLayoutParams((ViewGroup$LayoutParams)this.defaultTabLayoutParams);
            if (this.shouldExpand) {
                child.setPadding(0, 0, 0, 0);
                child.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -1, 1.0f));
            }
            else {
                final int tabPadding = this.tabPadding;
                child.setPadding(tabPadding, 0, tabPadding, 0);
            }
        }
    }
    
    public int getDividerPadding() {
        return this.dividerPadding;
    }
    
    public int getIndicatorColor() {
        return this.indicatorColor;
    }
    
    public int getIndicatorHeight() {
        return this.indicatorHeight;
    }
    
    public int getScrollOffset() {
        return this.scrollOffset;
    }
    
    public boolean getShouldExpand() {
        return this.shouldExpand;
    }
    
    public View getTab(final int n) {
        if (n >= 0 && n < this.tabsContainer.getChildCount()) {
            return this.tabsContainer.getChildAt(n);
        }
        return null;
    }
    
    public int getTabPaddingLeftRight() {
        return this.tabPadding;
    }
    
    public int getUnderlineColor() {
        return this.underlineColor;
    }
    
    public int getUnderlineHeight() {
        return this.underlineHeight;
    }
    
    public void notifyDataSetChanged() {
        this.tabsContainer.removeAllViews();
        this.tabCount = this.pager.getAdapter().getCount();
        for (int i = 0; i < this.tabCount; ++i) {
            if (this.pager.getAdapter() instanceof IconTabProvider) {
                this.addIconTab(i, ((IconTabProvider)this.pager.getAdapter()).getPageIconDrawable(i), this.pager.getAdapter().getPageTitle(i));
            }
        }
        this.updateTabStyles();
        this.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                PagerSlidingTabStrip.this.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                final PagerSlidingTabStrip this$0 = PagerSlidingTabStrip.this;
                this$0.currentPosition = this$0.pager.getCurrentItem();
                final PagerSlidingTabStrip this$2 = PagerSlidingTabStrip.this;
                this$2.scrollToChild(this$2.currentPosition, 0);
            }
        });
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (!this.isInEditMode()) {
            if (this.tabCount != 0) {
                final int height = this.getHeight();
                if (this.underlineHeight != 0) {
                    this.rectPaint.setColor(this.underlineColor);
                    canvas.drawRect(0.0f, (float)(height - this.underlineHeight), (float)this.tabsContainer.getWidth(), (float)height, this.rectPaint);
                }
                final View child = this.tabsContainer.getChildAt(this.currentPosition);
                final float n = (float)child.getLeft();
                float n3;
                final float n2 = n3 = (float)child.getRight();
                float n4 = n;
                if (this.currentPositionOffset > 0.0f) {
                    final int currentPosition = this.currentPosition;
                    n3 = n2;
                    n4 = n;
                    if (currentPosition < this.tabCount - 1) {
                        final View child2 = this.tabsContainer.getChildAt(currentPosition + 1);
                        final float n5 = (float)child2.getLeft();
                        final float n6 = (float)child2.getRight();
                        final float currentPositionOffset = this.currentPositionOffset;
                        n4 = n5 * currentPositionOffset + (1.0f - currentPositionOffset) * n;
                        n3 = n6 * currentPositionOffset + (1.0f - currentPositionOffset) * n2;
                    }
                }
                if (this.indicatorHeight != 0) {
                    this.rectPaint.setColor(this.indicatorColor);
                    canvas.drawRect(n4, (float)(height - this.indicatorHeight), n3, (float)height, this.rectPaint);
                }
            }
        }
    }
    
    protected void onMeasure(int measuredWidth, final int n) {
        super.onMeasure(measuredWidth, n);
        if (this.shouldExpand) {
            if (View$MeasureSpec.getMode(measuredWidth) != 0) {
                measuredWidth = this.getMeasuredWidth();
                this.tabsContainer.measure(measuredWidth | 0x40000000, n);
            }
        }
    }
    
    public void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        if (!this.shouldExpand) {
            this.post((Runnable)new _$$Lambda$87rOzK5QchuVBC_94tLIHh4T_gY(this));
        }
    }
    
    public void setDividerPadding(final int dividerPadding) {
        this.dividerPadding = dividerPadding;
        this.invalidate();
    }
    
    public void setIndicatorColor(final int indicatorColor) {
        this.indicatorColor = indicatorColor;
        this.invalidate();
    }
    
    public void setIndicatorColorResource(final int n) {
        this.indicatorColor = this.getResources().getColor(n);
        this.invalidate();
    }
    
    public void setIndicatorHeight(final int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        this.invalidate();
    }
    
    public void setOnPageChangeListener(final ViewPager.OnPageChangeListener delegatePageListener) {
        this.delegatePageListener = delegatePageListener;
    }
    
    public void setScrollOffset(final int scrollOffset) {
        this.scrollOffset = scrollOffset;
        this.invalidate();
    }
    
    public void setShouldExpand(final boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        this.tabsContainer.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        this.updateTabStyles();
        this.requestLayout();
    }
    
    public void setTabPaddingLeftRight(final int tabPadding) {
        this.tabPadding = tabPadding;
        this.updateTabStyles();
    }
    
    public void setUnderlineColor(final int underlineColor) {
        this.underlineColor = underlineColor;
        this.invalidate();
    }
    
    public void setUnderlineColorResource(final int n) {
        this.underlineColor = this.getResources().getColor(n);
        this.invalidate();
    }
    
    public void setUnderlineHeight(final int underlineHeight) {
        this.underlineHeight = underlineHeight;
        this.invalidate();
    }
    
    public void setViewPager(final ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() != null) {
            pager.setOnPageChangeListener((ViewPager.OnPageChangeListener)this.pageListener);
            this.notifyDataSetChanged();
            return;
        }
        throw new IllegalStateException("ViewPager does not have adapter instance.");
    }
    
    public interface IconTabProvider
    {
        boolean canScrollToTab(final int p0);
        
        void customOnDraw(final Canvas p0, final int p1);
        
        Drawable getPageIconDrawable(final int p0);
    }
    
    private class PageListener implements OnPageChangeListener
    {
        @Override
        public void onPageScrollStateChanged(final int n) {
            if (n == 0) {
                final PagerSlidingTabStrip this$0 = PagerSlidingTabStrip.this;
                this$0.scrollToChild(this$0.pager.getCurrentItem(), 0);
            }
            final OnPageChangeListener delegatePageListener = PagerSlidingTabStrip.this.delegatePageListener;
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(n);
            }
        }
        
        @Override
        public void onPageScrolled(final int n, final float n2, final int n3) {
            PagerSlidingTabStrip.this.currentPosition = n;
            PagerSlidingTabStrip.this.currentPositionOffset = n2;
            final PagerSlidingTabStrip this$0 = PagerSlidingTabStrip.this;
            this$0.scrollToChild(n, (int)(this$0.tabsContainer.getChildAt(n).getWidth() * n2));
            PagerSlidingTabStrip.this.invalidate();
            final OnPageChangeListener delegatePageListener = PagerSlidingTabStrip.this.delegatePageListener;
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(n, n2, n3);
            }
        }
        
        @Override
        public void onPageSelected(final int n) {
            final OnPageChangeListener delegatePageListener = PagerSlidingTabStrip.this.delegatePageListener;
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(n);
            }
            for (int i = 0; i < PagerSlidingTabStrip.this.tabsContainer.getChildCount(); ++i) {
                PagerSlidingTabStrip.this.tabsContainer.getChildAt(i).setSelected(i == n);
            }
        }
    }
}
