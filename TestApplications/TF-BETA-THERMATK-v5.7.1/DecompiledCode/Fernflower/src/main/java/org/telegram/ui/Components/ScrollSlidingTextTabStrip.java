package org.telegram.ui.Components;

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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class ScrollSlidingTextTabStrip extends HorizontalScrollView {
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
   private ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate delegate;
   private SparseIntArray idToPosition;
   private int indicatorWidth;
   private int indicatorX;
   private CubicBezierInterpolator interpolator;
   private long lastAnimationTime;
   private SparseIntArray positionToId;
   private SparseIntArray positionToWidth;
   private int prevLayoutWidth;
   private int previousPosition;
   private int selectedTabId = -1;
   private GradientDrawable selectorDrawable;
   private int tabCount;
   private LinearLayout tabsContainer;
   private boolean useSameWidth;

   public ScrollSlidingTextTabStrip(Context var1) {
      super(var1);
      this.interpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
      this.positionToId = new SparseIntArray(5);
      this.idToPosition = new SparseIntArray(5);
      this.positionToWidth = new SparseIntArray(5);
      this.animationRunnable = new Runnable() {
         public void run() {
            if (ScrollSlidingTextTabStrip.this.animatingIndicator) {
               long var1 = SystemClock.elapsedRealtime() - ScrollSlidingTextTabStrip.this.lastAnimationTime;
               long var3 = var1;
               if (var1 > 17L) {
                  var3 = 17L;
               }

               ScrollSlidingTextTabStrip var5 = ScrollSlidingTextTabStrip.this;
               var5.animationTime = var5.animationTime + (float)var3 / 200.0F;
               var5 = ScrollSlidingTextTabStrip.this;
               var5.setAnimationIdicatorProgress(var5.interpolator.getInterpolation(ScrollSlidingTextTabStrip.this.animationTime));
               if (ScrollSlidingTextTabStrip.this.animationTime > 1.0F) {
                  ScrollSlidingTextTabStrip.this.animationTime = 1.0F;
               }

               if (ScrollSlidingTextTabStrip.this.animationTime < 1.0F) {
                  AndroidUtilities.runOnUIThread(ScrollSlidingTextTabStrip.this.animationRunnable);
               } else {
                  ScrollSlidingTextTabStrip.this.animatingIndicator = false;
                  ScrollSlidingTextTabStrip.this.setEnabled(true);
                  if (ScrollSlidingTextTabStrip.this.delegate != null) {
                     ScrollSlidingTextTabStrip.this.delegate.onPageScrolled(1.0F);
                  }
               }

            }
         }
      };
      this.selectorDrawable = new GradientDrawable(Orientation.LEFT_RIGHT, (int[])null);
      float var2 = AndroidUtilities.dpf2(3.0F);
      this.selectorDrawable.setCornerRadii(new float[]{var2, var2, var2, var2, 0.0F, 0.0F, 0.0F, 0.0F});
      this.selectorDrawable.setColor(Theme.getColor("actionBarTabLine"));
      this.setFillViewport(true);
      this.setWillNotDraw(false);
      this.setHorizontalScrollBarEnabled(false);
      this.tabsContainer = new LinearLayout(var1);
      this.tabsContainer.setOrientation(0);
      this.tabsContainer.setLayoutParams(new LayoutParams(-1, -1));
      this.addView(this.tabsContainer);
   }

   private int getChildWidth(TextView var1) {
      Layout var2 = var1.getLayout();
      return var2 != null ? (int)Math.ceil((double)var2.getLineWidth(0)) + AndroidUtilities.dp(2.0F) : var1.getMeasuredWidth();
   }

   private void scrollToChild(int var1) {
      if (this.tabCount != 0) {
         TextView var2 = (TextView)this.tabsContainer.getChildAt(var1);
         if (var2 != null) {
            var1 = this.getScrollX();
            int var3 = var2.getLeft();
            int var4 = var2.getMeasuredWidth();
            if (var3 < var1) {
               this.smoothScrollTo(var3, 0);
            } else {
               var3 += var4;
               if (var3 > var1 + this.getWidth()) {
                  this.smoothScrollTo(var3, 0);
               }
            }

         }
      }
   }

   private void setAnimationProgressInernal(TextView var1, TextView var2, float var3) {
      int var4 = Theme.getColor("actionBarTabActiveText");
      int var5 = Theme.getColor("actionBarTabUnactiveText");
      int var6 = Color.red(var4);
      int var7 = Color.green(var4);
      int var8 = Color.blue(var4);
      int var9 = Color.alpha(var4);
      int var10 = Color.red(var5);
      int var11 = Color.green(var5);
      var4 = Color.blue(var5);
      var5 = Color.alpha(var5);
      var2.setTextColor(Color.argb((int)((float)var9 + (float)(var5 - var9) * var3), (int)((float)var6 + (float)(var10 - var6) * var3), (int)((float)var7 + (float)(var11 - var7) * var3), (int)((float)var8 + (float)(var4 - var8) * var3)));
      var1.setTextColor(Color.argb((int)((float)var5 + (float)(var9 - var5) * var3), (int)((float)var10 + (float)(var6 - var10) * var3), (int)((float)var11 + (float)(var7 - var11) * var3), (int)((float)var4 + (float)(var8 - var4) * var3)));
      var6 = this.animateIndicatorStartX;
      this.indicatorX = (int)((float)var6 + (float)(this.animateIndicatorToX - var6) * var3);
      var6 = this.animateIndicatorStartWidth;
      this.indicatorWidth = (int)((float)var6 + (float)(this.animateIndicatorToWidth - var6) * var3);
      this.invalidate();
   }

   public void addTextTab(int var1, CharSequence var2) {
      int var3 = this.tabCount++;
      if (var3 == 0 && this.selectedTabId == -1) {
         this.selectedTabId = var1;
      }

      this.positionToId.put(var3, var1);
      this.idToPosition.put(var1, var3);
      int var4 = this.selectedTabId;
      if (var4 != -1 && var4 == var1) {
         this.currentPosition = var3;
         this.prevLayoutWidth = 0;
      }

      TextView var5 = new TextView(this.getContext());
      var5.setGravity(17);
      var5.setText(var2);
      var5.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("actionBarTabSelector"), 3));
      var5.setTextSize(1, 14.0F);
      var5.setSingleLine(true);
      var5.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var5.setPadding(AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F), 0);
      var5.setOnClickListener(new _$$Lambda$ScrollSlidingTextTabStrip$9m5R2Lhhk_z3JuL73ha6pd_2NRk(this, var1));
      var1 = (int)Math.ceil((double)var5.getPaint().measureText(var2, 0, var2.length())) + AndroidUtilities.dp(16.0F);
      this.allTextWidth += var1;
      this.positionToWidth.put(var3, var1);
      this.tabsContainer.addView(var5, LayoutHelper.createLinear(0, -1));
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      boolean var5 = super.drawChild(var1, var2, var3);
      if (var2 == this.tabsContainer) {
         int var6 = this.getMeasuredHeight();
         this.selectorDrawable.setBounds(this.indicatorX, var6 - AndroidUtilities.dpr(4.0F), this.indicatorX + this.indicatorWidth, var6);
         this.selectorDrawable.draw(var1);
      }

      return var5;
   }

   public void finishAddingTabs() {
      int var1 = this.tabsContainer.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         TextView var3 = (TextView)this.tabsContainer.getChildAt(var2);
         int var4 = this.currentPosition;
         String var5 = "actionBarTabActiveText";
         String var6;
         if (var4 == var2) {
            var6 = "actionBarTabActiveText";
         } else {
            var6 = "actionBarTabUnactiveText";
         }

         var3.setTag(var6);
         if (this.currentPosition == var2) {
            var6 = var5;
         } else {
            var6 = "actionBarTabUnactiveText";
         }

         var3.setTextColor(Theme.getColor(var6));
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

   public int getNextPageId(boolean var1) {
      SparseIntArray var2 = this.positionToId;
      int var3 = this.currentPosition;
      byte var4;
      if (var1) {
         var4 = 1;
      } else {
         var4 = -1;
      }

      return var2.get(var3 + var4, -1);
   }

   public Drawable getSelectorDrawable() {
      return this.selectorDrawable;
   }

   public View getTabsContainer() {
      return this.tabsContainer;
   }

   public int getTabsCount() {
      return this.tabCount;
   }

   public boolean hasTab(int var1) {
      boolean var2;
      if (this.idToPosition.get(var1, -1) != -1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isAnimatingIndicator() {
      return this.animatingIndicator;
   }

   // $FF: synthetic method
   public void lambda$addTextTab$0$ScrollSlidingTextTabStrip(int var1, View var2) {
      int var3 = this.tabsContainer.indexOfChild(var2);
      if (var3 >= 0) {
         int var4 = this.currentPosition;
         if (var3 != var4) {
            boolean var5;
            if (var4 < var3) {
               var5 = true;
            } else {
               var5 = false;
            }

            this.previousPosition = this.currentPosition;
            this.currentPosition = var3;
            this.selectedTabId = var1;
            if (this.animatingIndicator) {
               AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
               this.animatingIndicator = false;
            }

            this.animationTime = 0.0F;
            this.animatingIndicator = true;
            this.animateIndicatorStartX = this.indicatorX;
            this.animateIndicatorStartWidth = this.indicatorWidth;
            TextView var6 = (TextView)var2;
            this.animateIndicatorToWidth = this.getChildWidth(var6);
            this.animateIndicatorToX = var6.getLeft() + (var6.getMeasuredWidth() - this.animateIndicatorToWidth) / 2;
            this.setEnabled(false);
            AndroidUtilities.runOnUIThread(this.animationRunnable, 16L);
            ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate var7 = this.delegate;
            if (var7 != null) {
               var7.onPageSelected(var1, var5);
            }

            this.scrollToChild(var3);
         }
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      var3 = this.prevLayoutWidth;
      var2 = var4 - var2;
      if (var3 != var2) {
         this.prevLayoutWidth = var2;
         if (this.animatingIndicator) {
            AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
            this.animatingIndicator = false;
            this.setEnabled(true);
            ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate var6 = this.delegate;
            if (var6 != null) {
               var6.onPageScrolled(1.0F);
            }
         }

         TextView var7 = (TextView)this.tabsContainer.getChildAt(this.currentPosition);
         if (var7 != null) {
            this.indicatorWidth = this.getChildWidth(var7);
            this.indicatorX = var7.getLeft() + (var7.getMeasuredWidth() - this.indicatorWidth) / 2;
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var1);
      int var4 = this.tabsContainer.getChildCount();

      for(int var5 = 0; var5 < var4; ++var5) {
         android.widget.LinearLayout.LayoutParams var6 = (android.widget.LinearLayout.LayoutParams)this.tabsContainer.getChildAt(var5).getLayoutParams();
         int var7 = this.allTextWidth;
         if (var7 > var3) {
            var6.weight = 0.0F;
            var6.width = -2;
         } else if (this.useSameWidth) {
            var6.weight = 1.0F / (float)var4;
            var6.width = 0;
         } else {
            var6.weight = 1.0F / (float)var7 * (float)this.positionToWidth.get(var5);
            var6.width = 0;
         }
      }

      if (this.allTextWidth > var3) {
         this.tabsContainer.setWeightSum(0.0F);
      } else {
         this.tabsContainer.setWeightSum(1.0F);
      }

      super.onMeasure(var1, var2);
   }

   public void onPageScrolled(int var1, int var2) {
      if (this.currentPosition != var1) {
         this.currentPosition = var1;
         if (var1 < this.tabsContainer.getChildCount()) {
            int var3 = 0;

            while(true) {
               int var4 = this.tabsContainer.getChildCount();
               boolean var5 = true;
               if (var3 >= var4) {
                  if (var2 == var1 && var1 > 1) {
                     this.scrollToChild(var1 - 1);
                  } else {
                     this.scrollToChild(var1);
                  }

                  this.invalidate();
                  return;
               }

               View var6 = this.tabsContainer.getChildAt(var3);
               if (var3 != var1) {
                  var5 = false;
               }

               var6.setSelected(var5);
               ++var3;
            }
         }
      }
   }

   public void removeTabs() {
      this.positionToId.clear();
      this.idToPosition.clear();
      this.positionToWidth.clear();
      this.tabsContainer.removeAllViews();
      this.allTextWidth = 0;
      this.tabCount = 0;
   }

   public void selectTabWithId(int var1, float var2) {
      int var3 = this.idToPosition.get(var1, -1);
      if (var3 >= 0) {
         float var4;
         if (var2 < 0.0F) {
            var4 = 0.0F;
         } else {
            var4 = var2;
            if (var2 > 1.0F) {
               var4 = 1.0F;
            }
         }

         TextView var5 = (TextView)this.tabsContainer.getChildAt(this.currentPosition);
         TextView var6 = (TextView)this.tabsContainer.getChildAt(var3);
         if (var5 != null && var6 != null) {
            this.animateIndicatorStartWidth = this.getChildWidth(var5);
            this.animateIndicatorStartX = var5.getLeft() + (var5.getMeasuredWidth() - this.animateIndicatorStartWidth) / 2;
            this.animateIndicatorToWidth = this.getChildWidth(var6);
            this.animateIndicatorToX = var6.getLeft() + (var6.getMeasuredWidth() - this.animateIndicatorToWidth) / 2;
            this.setAnimationProgressInernal(var6, var5, var4);
         }

         if (var4 >= 1.0F) {
            this.currentPosition = var3;
            this.selectedTabId = var1;
         }

      }
   }

   @Keep
   public void setAnimationIdicatorProgress(float var1) {
      this.animationIdicatorProgress = var1;
      this.setAnimationProgressInernal((TextView)this.tabsContainer.getChildAt(this.currentPosition), (TextView)this.tabsContainer.getChildAt(this.previousPosition), var1);
      ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate var2 = this.delegate;
      if (var2 != null) {
         var2.onPageScrolled(var1);
      }

   }

   public void setDelegate(ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate var1) {
      this.delegate = var1;
   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      int var2 = this.tabsContainer.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.tabsContainer.getChildAt(var3).setEnabled(var1);
      }

   }

   public void setInitialTabId(int var1) {
      this.selectedTabId = var1;
   }

   public void setUseSameWidth(boolean var1) {
      this.useSameWidth = var1;
   }

   public interface ScrollSlidingTabStripDelegate {
      void onPageScrolled(float var1);

      void onPageSelected(int var1, boolean var2);
   }
}
