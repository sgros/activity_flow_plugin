package org.telegram.p004ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import org.telegram.messenger.AndroidUtilities;

/* renamed from: org.telegram.ui.Components.PagerSlidingTabStrip */
public class PagerSlidingTabStrip extends HorizontalScrollView {
    private int currentPosition = 0;
    private float currentPositionOffset = 0.0f;
    private LayoutParams defaultTabLayoutParams;
    public OnPageChangeListener delegatePageListener;
    private int dividerPadding = AndroidUtilities.m26dp(12.0f);
    private int indicatorColor = -10066330;
    private int indicatorHeight = AndroidUtilities.m26dp(8.0f);
    private int lastScrollX = 0;
    private final PageListener pageListener = new PageListener(this, null);
    private ViewPager pager;
    private Paint rectPaint;
    private int scrollOffset = AndroidUtilities.m26dp(52.0f);
    private boolean shouldExpand = false;
    private int tabCount;
    private int tabPadding = AndroidUtilities.m26dp(24.0f);
    private LinearLayout tabsContainer;
    private int underlineColor = 436207616;
    private int underlineHeight = AndroidUtilities.m26dp(2.0f);

    /* renamed from: org.telegram.ui.Components.PagerSlidingTabStrip$1 */
    class C28701 implements OnGlobalLayoutListener {
        C28701() {
        }

        public void onGlobalLayout() {
            PagerSlidingTabStrip.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            PagerSlidingTabStrip pagerSlidingTabStrip = PagerSlidingTabStrip.this;
            pagerSlidingTabStrip.currentPosition = pagerSlidingTabStrip.pager.getCurrentItem();
            pagerSlidingTabStrip = PagerSlidingTabStrip.this;
            pagerSlidingTabStrip.scrollToChild(pagerSlidingTabStrip.currentPosition, 0);
        }
    }

    /* renamed from: org.telegram.ui.Components.PagerSlidingTabStrip$IconTabProvider */
    public interface IconTabProvider {
        boolean canScrollToTab(int i);

        void customOnDraw(Canvas canvas, int i);

        Drawable getPageIconDrawable(int i);
    }

    /* renamed from: org.telegram.ui.Components.PagerSlidingTabStrip$PageListener */
    private class PageListener implements OnPageChangeListener {
        private PageListener() {
        }

        /* synthetic */ PageListener(PagerSlidingTabStrip pagerSlidingTabStrip, C28701 c28701) {
            this();
        }

        public void onPageScrolled(int i, float f, int i2) {
            PagerSlidingTabStrip.this.currentPosition = i;
            PagerSlidingTabStrip.this.currentPositionOffset = f;
            PagerSlidingTabStrip pagerSlidingTabStrip = PagerSlidingTabStrip.this;
            pagerSlidingTabStrip.scrollToChild(i, (int) (((float) pagerSlidingTabStrip.tabsContainer.getChildAt(i).getWidth()) * f));
            PagerSlidingTabStrip.this.invalidate();
            OnPageChangeListener onPageChangeListener = PagerSlidingTabStrip.this.delegatePageListener;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrolled(i, f, i2);
            }
        }

        public void onPageScrollStateChanged(int i) {
            if (i == 0) {
                PagerSlidingTabStrip pagerSlidingTabStrip = PagerSlidingTabStrip.this;
                pagerSlidingTabStrip.scrollToChild(pagerSlidingTabStrip.pager.getCurrentItem(), 0);
            }
            OnPageChangeListener onPageChangeListener = PagerSlidingTabStrip.this.delegatePageListener;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(i);
            }
        }

        public void onPageSelected(int i) {
            OnPageChangeListener onPageChangeListener = PagerSlidingTabStrip.this.delegatePageListener;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(i);
            }
            int i2 = 0;
            while (i2 < PagerSlidingTabStrip.this.tabsContainer.getChildCount()) {
                PagerSlidingTabStrip.this.tabsContainer.getChildAt(i2).setSelected(i2 == i);
                i2++;
            }
        }
    }

    public PagerSlidingTabStrip(Context context) {
        super(context);
        setFillViewport(true);
        setWillNotDraw(false);
        this.tabsContainer = new LinearLayout(context);
        this.tabsContainer.setOrientation(0);
        this.tabsContainer.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addView(this.tabsContainer);
        this.rectPaint = new Paint();
        this.rectPaint.setAntiAlias(true);
        this.rectPaint.setStyle(Style.FILL);
        this.defaultTabLayoutParams = new LayoutParams(-2, -1);
    }

    public void setViewPager(ViewPager viewPager) {
        this.pager = viewPager;
        if (viewPager.getAdapter() != null) {
            viewPager.setOnPageChangeListener(this.pageListener);
            notifyDataSetChanged();
            return;
        }
        throw new IllegalStateException("ViewPager does not have adapter instance.");
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.delegatePageListener = onPageChangeListener;
    }

    public void notifyDataSetChanged() {
        this.tabsContainer.removeAllViews();
        this.tabCount = this.pager.getAdapter().getCount();
        for (int i = 0; i < this.tabCount; i++) {
            if (this.pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) this.pager.getAdapter()).getPageIconDrawable(i), this.pager.getAdapter().getPageTitle(i));
            }
        }
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new C28701());
    }

    public View getTab(int i) {
        return (i < 0 || i >= this.tabsContainer.getChildCount()) ? null : this.tabsContainer.getChildAt(i);
    }

    private void addIconTab(final int i, Drawable drawable, CharSequence charSequence) {
        C28712 c28712 = new ImageView(getContext()) {
            /* Access modifiers changed, original: protected */
            public void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                if (PagerSlidingTabStrip.this.pager.getAdapter() instanceof IconTabProvider) {
                    ((IconTabProvider) PagerSlidingTabStrip.this.pager.getAdapter()).customOnDraw(canvas, i);
                }
            }
        };
        boolean z = true;
        c28712.setFocusable(true);
        c28712.setImageDrawable(drawable);
        c28712.setScaleType(ScaleType.CENTER);
        c28712.setOnClickListener(new C2607-$$Lambda$PagerSlidingTabStrip$uPwWgO9fi9vraIMG_OGzLnle64Y(this, i));
        this.tabsContainer.addView(c28712);
        if (i != this.currentPosition) {
            z = false;
        }
        c28712.setSelected(z);
        c28712.setContentDescription(charSequence);
    }

    public /* synthetic */ void lambda$addIconTab$0$PagerSlidingTabStrip(int i, View view) {
        if (!(this.pager.getAdapter() instanceof IconTabProvider) || ((IconTabProvider) this.pager.getAdapter()).canScrollToTab(i)) {
            this.pager.setCurrentItem(i, false);
        }
    }

    private void updateTabStyles() {
        for (int i = 0; i < this.tabCount; i++) {
            View childAt = this.tabsContainer.getChildAt(i);
            childAt.setLayoutParams(this.defaultTabLayoutParams);
            if (this.shouldExpand) {
                childAt.setPadding(0, 0, 0, 0);
                childAt.setLayoutParams(new LayoutParams(-1, -1, 1.0f));
            } else {
                int i2 = this.tabPadding;
                childAt.setPadding(i2, 0, i2, 0);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.shouldExpand && MeasureSpec.getMode(i) != 0) {
            this.tabsContainer.measure(getMeasuredWidth() | 1073741824, i2);
        }
    }

    private void scrollToChild(int i, int i2) {
        if (this.tabCount != 0) {
            int left = this.tabsContainer.getChildAt(i).getLeft() + i2;
            if (i > 0 || i2 > 0) {
                left -= this.scrollOffset;
            }
            if (left != this.lastScrollX) {
                this.lastScrollX = left;
                scrollTo(left, 0);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode() && this.tabCount != 0) {
            float left;
            int height = getHeight();
            if (this.underlineHeight != 0) {
                this.rectPaint.setColor(this.underlineColor);
                canvas.drawRect(0.0f, (float) (height - this.underlineHeight), (float) this.tabsContainer.getWidth(), (float) height, this.rectPaint);
            }
            View childAt = this.tabsContainer.getChildAt(this.currentPosition);
            float left2 = (float) childAt.getLeft();
            float right = (float) childAt.getRight();
            if (this.currentPositionOffset > 0.0f) {
                int i = this.currentPosition;
                if (i < this.tabCount - 1) {
                    View childAt2 = this.tabsContainer.getChildAt(i + 1);
                    left = (float) childAt2.getLeft();
                    float right2 = (float) childAt2.getRight();
                    float f = this.currentPositionOffset;
                    left2 = (left * f) + ((1.0f - f) * left2);
                    right = (right2 * f) + ((1.0f - f) * right);
                }
            }
            float f2 = right;
            left = left2;
            if (this.indicatorHeight != 0) {
                this.rectPaint.setColor(this.indicatorColor);
                canvas.drawRect(left, (float) (height - this.indicatorHeight), f2, (float) height, this.rectPaint);
            }
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (!this.shouldExpand) {
            post(new C2461-$$Lambda$87rOzK5QchuVBC_94tLIHh4T_gY(this));
        }
    }

    public void setIndicatorColor(int i) {
        this.indicatorColor = i;
        invalidate();
    }

    public void setIndicatorColorResource(int i) {
        this.indicatorColor = getResources().getColor(i);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int i) {
        this.indicatorHeight = i;
        invalidate();
    }

    public int getIndicatorHeight() {
        return this.indicatorHeight;
    }

    public void setUnderlineColor(int i) {
        this.underlineColor = i;
        invalidate();
    }

    public void setUnderlineColorResource(int i) {
        this.underlineColor = getResources().getColor(i);
        invalidate();
    }

    public int getUnderlineColor() {
        return this.underlineColor;
    }

    public void setUnderlineHeight(int i) {
        this.underlineHeight = i;
        invalidate();
    }

    public int getUnderlineHeight() {
        return this.underlineHeight;
    }

    public void setDividerPadding(int i) {
        this.dividerPadding = i;
        invalidate();
    }

    public int getDividerPadding() {
        return this.dividerPadding;
    }

    public void setScrollOffset(int i) {
        this.scrollOffset = i;
        invalidate();
    }

    public int getScrollOffset() {
        return this.scrollOffset;
    }

    public void setShouldExpand(boolean z) {
        this.shouldExpand = z;
        this.tabsContainer.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        updateTabStyles();
        requestLayout();
    }

    public boolean getShouldExpand() {
        return this.shouldExpand;
    }

    public void setTabPaddingLeftRight(int i) {
        this.tabPadding = i;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return this.tabPadding;
    }
}
