package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import androidx.viewpager.widget.ViewPager;
import org.telegram.messenger.AndroidUtilities;

public class PagerSlidingTabStrip extends HorizontalScrollView {
   private int currentPosition = 0;
   private float currentPositionOffset = 0.0F;
   private LayoutParams defaultTabLayoutParams;
   public ViewPager.OnPageChangeListener delegatePageListener;
   private int dividerPadding = AndroidUtilities.dp(12.0F);
   private int indicatorColor = -10066330;
   private int indicatorHeight = AndroidUtilities.dp(8.0F);
   private int lastScrollX = 0;
   private final PagerSlidingTabStrip.PageListener pageListener = new PagerSlidingTabStrip.PageListener();
   private ViewPager pager;
   private Paint rectPaint;
   private int scrollOffset = AndroidUtilities.dp(52.0F);
   private boolean shouldExpand = false;
   private int tabCount;
   private int tabPadding = AndroidUtilities.dp(24.0F);
   private LinearLayout tabsContainer;
   private int underlineColor = 436207616;
   private int underlineHeight = AndroidUtilities.dp(2.0F);

   public PagerSlidingTabStrip(Context var1) {
      super(var1);
      this.setFillViewport(true);
      this.setWillNotDraw(false);
      this.tabsContainer = new LinearLayout(var1);
      this.tabsContainer.setOrientation(0);
      this.tabsContainer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
      this.addView(this.tabsContainer);
      this.rectPaint = new Paint();
      this.rectPaint.setAntiAlias(true);
      this.rectPaint.setStyle(Style.FILL);
      this.defaultTabLayoutParams = new LayoutParams(-2, -1);
   }

   private void addIconTab(final int var1, Drawable var2, CharSequence var3) {
      ImageView var4 = new ImageView(this.getContext()) {
         protected void onDraw(Canvas var1x) {
            super.onDraw(var1x);
            if (PagerSlidingTabStrip.this.pager.getAdapter() instanceof PagerSlidingTabStrip.IconTabProvider) {
               ((PagerSlidingTabStrip.IconTabProvider)PagerSlidingTabStrip.this.pager.getAdapter()).customOnDraw(var1x, var1);
            }

         }
      };
      boolean var5 = true;
      var4.setFocusable(true);
      var4.setImageDrawable(var2);
      var4.setScaleType(ScaleType.CENTER);
      var4.setOnClickListener(new _$$Lambda$PagerSlidingTabStrip$uPwWgO9fi9vraIMG_OGzLnle64Y(this, var1));
      this.tabsContainer.addView(var4);
      if (var1 != this.currentPosition) {
         var5 = false;
      }

      var4.setSelected(var5);
      var4.setContentDescription(var3);
   }

   private void scrollToChild(int var1, int var2) {
      if (this.tabCount != 0) {
         label18: {
            int var3 = this.tabsContainer.getChildAt(var1).getLeft() + var2;
            if (var1 <= 0) {
               var1 = var3;
               if (var2 <= 0) {
                  break label18;
               }
            }

            var1 = var3 - this.scrollOffset;
         }

         if (var1 != this.lastScrollX) {
            this.lastScrollX = var1;
            this.scrollTo(var1, 0);
         }

      }
   }

   private void updateTabStyles() {
      for(int var1 = 0; var1 < this.tabCount; ++var1) {
         View var2 = this.tabsContainer.getChildAt(var1);
         var2.setLayoutParams(this.defaultTabLayoutParams);
         if (this.shouldExpand) {
            var2.setPadding(0, 0, 0, 0);
            var2.setLayoutParams(new LayoutParams(-1, -1, 1.0F));
         } else {
            int var3 = this.tabPadding;
            var2.setPadding(var3, 0, var3, 0);
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

   public View getTab(int var1) {
      return var1 >= 0 && var1 < this.tabsContainer.getChildCount() ? this.tabsContainer.getChildAt(var1) : null;
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

   // $FF: synthetic method
   public void lambda$addIconTab$0$PagerSlidingTabStrip(int var1, View var2) {
      if (!(this.pager.getAdapter() instanceof PagerSlidingTabStrip.IconTabProvider) || ((PagerSlidingTabStrip.IconTabProvider)this.pager.getAdapter()).canScrollToTab(var1)) {
         this.pager.setCurrentItem(var1, false);
      }
   }

   public void notifyDataSetChanged() {
      this.tabsContainer.removeAllViews();
      this.tabCount = this.pager.getAdapter().getCount();

      for(int var1 = 0; var1 < this.tabCount; ++var1) {
         if (this.pager.getAdapter() instanceof PagerSlidingTabStrip.IconTabProvider) {
            this.addIconTab(var1, ((PagerSlidingTabStrip.IconTabProvider)this.pager.getAdapter()).getPageIconDrawable(var1), this.pager.getAdapter().getPageTitle(var1));
         }
      }

      this.updateTabStyles();
      this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
         public void onGlobalLayout() {
            PagerSlidingTabStrip.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            PagerSlidingTabStrip var1 = PagerSlidingTabStrip.this;
            var1.currentPosition = var1.pager.getCurrentItem();
            var1 = PagerSlidingTabStrip.this;
            var1.scrollToChild(var1.currentPosition, 0);
         }
      });
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (!this.isInEditMode() && this.tabCount != 0) {
         int var2 = this.getHeight();
         if (this.underlineHeight != 0) {
            this.rectPaint.setColor(this.underlineColor);
            var1.drawRect(0.0F, (float)(var2 - this.underlineHeight), (float)this.tabsContainer.getWidth(), (float)var2, this.rectPaint);
         }

         View var3 = this.tabsContainer.getChildAt(this.currentPosition);
         float var4 = (float)var3.getLeft();
         float var5 = (float)var3.getRight();
         float var6 = var5;
         float var7 = var4;
         if (this.currentPositionOffset > 0.0F) {
            int var8 = this.currentPosition;
            var6 = var5;
            var7 = var4;
            if (var8 < this.tabCount - 1) {
               var3 = this.tabsContainer.getChildAt(var8 + 1);
               var7 = (float)var3.getLeft();
               float var9 = (float)var3.getRight();
               var6 = this.currentPositionOffset;
               var7 = var7 * var6 + (1.0F - var6) * var4;
               var6 = var9 * var6 + (1.0F - var6) * var5;
            }
         }

         if (this.indicatorHeight != 0) {
            this.rectPaint.setColor(this.indicatorColor);
            var1.drawRect(var7, (float)(var2 - this.indicatorHeight), var6, (float)var2, this.rectPaint);
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (this.shouldExpand && MeasureSpec.getMode(var1) != 0) {
         var1 = this.getMeasuredWidth();
         this.tabsContainer.measure(var1 | 1073741824, var2);
      }

   }

   public void onSizeChanged(int var1, int var2, int var3, int var4) {
      if (!this.shouldExpand) {
         this.post(new _$$Lambda$87rOzK5QchuVBC_94tLIHh4T_gY(this));
      }

   }

   public void setDividerPadding(int var1) {
      this.dividerPadding = var1;
      this.invalidate();
   }

   public void setIndicatorColor(int var1) {
      this.indicatorColor = var1;
      this.invalidate();
   }

   public void setIndicatorColorResource(int var1) {
      this.indicatorColor = this.getResources().getColor(var1);
      this.invalidate();
   }

   public void setIndicatorHeight(int var1) {
      this.indicatorHeight = var1;
      this.invalidate();
   }

   public void setOnPageChangeListener(ViewPager.OnPageChangeListener var1) {
      this.delegatePageListener = var1;
   }

   public void setScrollOffset(int var1) {
      this.scrollOffset = var1;
      this.invalidate();
   }

   public void setShouldExpand(boolean var1) {
      this.shouldExpand = var1;
      this.tabsContainer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
      this.updateTabStyles();
      this.requestLayout();
   }

   public void setTabPaddingLeftRight(int var1) {
      this.tabPadding = var1;
      this.updateTabStyles();
   }

   public void setUnderlineColor(int var1) {
      this.underlineColor = var1;
      this.invalidate();
   }

   public void setUnderlineColorResource(int var1) {
      this.underlineColor = this.getResources().getColor(var1);
      this.invalidate();
   }

   public void setUnderlineHeight(int var1) {
      this.underlineHeight = var1;
      this.invalidate();
   }

   public void setViewPager(ViewPager var1) {
      this.pager = var1;
      if (var1.getAdapter() != null) {
         var1.setOnPageChangeListener(this.pageListener);
         this.notifyDataSetChanged();
      } else {
         throw new IllegalStateException("ViewPager does not have adapter instance.");
      }
   }

   public interface IconTabProvider {
      boolean canScrollToTab(int var1);

      void customOnDraw(Canvas var1, int var2);

      Drawable getPageIconDrawable(int var1);
   }

   private class PageListener implements ViewPager.OnPageChangeListener {
      private PageListener() {
      }

      // $FF: synthetic method
      PageListener(Object var2) {
         this();
      }

      public void onPageScrollStateChanged(int var1) {
         if (var1 == 0) {
            PagerSlidingTabStrip var2 = PagerSlidingTabStrip.this;
            var2.scrollToChild(var2.pager.getCurrentItem(), 0);
         }

         ViewPager.OnPageChangeListener var3 = PagerSlidingTabStrip.this.delegatePageListener;
         if (var3 != null) {
            var3.onPageScrollStateChanged(var1);
         }

      }

      public void onPageScrolled(int var1, float var2, int var3) {
         PagerSlidingTabStrip.this.currentPosition = var1;
         PagerSlidingTabStrip.this.currentPositionOffset = var2;
         PagerSlidingTabStrip var4 = PagerSlidingTabStrip.this;
         var4.scrollToChild(var1, (int)((float)var4.tabsContainer.getChildAt(var1).getWidth() * var2));
         PagerSlidingTabStrip.this.invalidate();
         ViewPager.OnPageChangeListener var5 = PagerSlidingTabStrip.this.delegatePageListener;
         if (var5 != null) {
            var5.onPageScrolled(var1, var2, var3);
         }

      }

      public void onPageSelected(int var1) {
         ViewPager.OnPageChangeListener var2 = PagerSlidingTabStrip.this.delegatePageListener;
         if (var2 != null) {
            var2.onPageSelected(var1);
         }

         for(int var3 = 0; var3 < PagerSlidingTabStrip.this.tabsContainer.getChildCount(); ++var3) {
            View var5 = PagerSlidingTabStrip.this.tabsContainer.getChildAt(var3);
            boolean var4;
            if (var3 == var1) {
               var4 = true;
            } else {
               var4 = false;
            }

            var5.setSelected(var4);
         }

      }
   }
}
