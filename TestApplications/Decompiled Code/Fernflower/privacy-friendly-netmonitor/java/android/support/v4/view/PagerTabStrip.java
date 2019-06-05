package android.support.v4.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;

public class PagerTabStrip extends PagerTitleStrip {
   private static final int FULL_UNDERLINE_HEIGHT = 1;
   private static final int INDICATOR_HEIGHT = 3;
   private static final int MIN_PADDING_BOTTOM = 6;
   private static final int MIN_STRIP_HEIGHT = 32;
   private static final int MIN_TEXT_SPACING = 64;
   private static final int TAB_PADDING = 16;
   private static final int TAB_SPACING = 32;
   private static final String TAG = "PagerTabStrip";
   private boolean mDrawFullUnderline;
   private boolean mDrawFullUnderlineSet;
   private int mFullUnderlineHeight;
   private boolean mIgnoreTap;
   private int mIndicatorColor;
   private int mIndicatorHeight;
   private float mInitialMotionX;
   private float mInitialMotionY;
   private int mMinPaddingBottom;
   private int mMinStripHeight;
   private int mMinTextSpacing;
   private int mTabAlpha;
   private int mTabPadding;
   private final Paint mTabPaint;
   private final Rect mTempRect;
   private int mTouchSlop;

   public PagerTabStrip(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public PagerTabStrip(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mTabPaint = new Paint();
      this.mTempRect = new Rect();
      this.mTabAlpha = 255;
      this.mDrawFullUnderline = false;
      this.mDrawFullUnderlineSet = false;
      this.mIndicatorColor = this.mTextColor;
      this.mTabPaint.setColor(this.mIndicatorColor);
      float var3 = var1.getResources().getDisplayMetrics().density;
      this.mIndicatorHeight = (int)(3.0F * var3 + 0.5F);
      this.mMinPaddingBottom = (int)(6.0F * var3 + 0.5F);
      this.mMinTextSpacing = (int)(64.0F * var3);
      this.mTabPadding = (int)(16.0F * var3 + 0.5F);
      this.mFullUnderlineHeight = (int)(1.0F * var3 + 0.5F);
      this.mMinStripHeight = (int)(32.0F * var3 + 0.5F);
      this.mTouchSlop = ViewConfiguration.get(var1).getScaledTouchSlop();
      this.setPadding(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
      this.setTextSpacing(this.getTextSpacing());
      this.setWillNotDraw(false);
      this.mPrevText.setFocusable(true);
      this.mPrevText.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            PagerTabStrip.this.mPager.setCurrentItem(PagerTabStrip.this.mPager.getCurrentItem() - 1);
         }
      });
      this.mNextText.setFocusable(true);
      this.mNextText.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            PagerTabStrip.this.mPager.setCurrentItem(PagerTabStrip.this.mPager.getCurrentItem() + 1);
         }
      });
      if (this.getBackground() == null) {
         this.mDrawFullUnderline = true;
      }

   }

   public boolean getDrawFullUnderline() {
      return this.mDrawFullUnderline;
   }

   int getMinHeight() {
      return Math.max(super.getMinHeight(), this.mMinStripHeight);
   }

   @ColorInt
   public int getTabIndicatorColor() {
      return this.mIndicatorColor;
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      int var2 = this.getHeight();
      int var3 = this.mCurrText.getLeft();
      int var4 = this.mTabPadding;
      int var5 = this.mCurrText.getRight();
      int var6 = this.mTabPadding;
      int var7 = this.mIndicatorHeight;
      this.mTabPaint.setColor(this.mTabAlpha << 24 | this.mIndicatorColor & 16777215);
      float var8 = (float)(var3 - var4);
      float var9 = (float)(var2 - var7);
      float var10 = (float)(var5 + var6);
      float var11 = (float)var2;
      var1.drawRect(var8, var9, var10, var11, this.mTabPaint);
      if (this.mDrawFullUnderline) {
         this.mTabPaint.setColor(-16777216 | this.mIndicatorColor & 16777215);
         var1.drawRect((float)this.getPaddingLeft(), (float)(var2 - this.mFullUnderlineHeight), (float)(this.getWidth() - this.getPaddingRight()), var11, this.mTabPaint);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = var1.getAction();
      if (var2 != 0 && this.mIgnoreTap) {
         return false;
      } else {
         float var3 = var1.getX();
         float var4 = var1.getY();
         switch(var2) {
         case 0:
            this.mInitialMotionX = var3;
            this.mInitialMotionY = var4;
            this.mIgnoreTap = false;
            break;
         case 1:
            if (var3 < (float)(this.mCurrText.getLeft() - this.mTabPadding)) {
               this.mPager.setCurrentItem(this.mPager.getCurrentItem() - 1);
            } else if (var3 > (float)(this.mCurrText.getRight() + this.mTabPadding)) {
               this.mPager.setCurrentItem(this.mPager.getCurrentItem() + 1);
            }
            break;
         case 2:
            if (Math.abs(var3 - this.mInitialMotionX) > (float)this.mTouchSlop || Math.abs(var4 - this.mInitialMotionY) > (float)this.mTouchSlop) {
               this.mIgnoreTap = true;
            }
         }

         return true;
      }
   }

   public void setBackgroundColor(@ColorInt int var1) {
      super.setBackgroundColor(var1);
      if (!this.mDrawFullUnderlineSet) {
         boolean var2;
         if ((var1 & -16777216) == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.mDrawFullUnderline = var2;
      }

   }

   public void setBackgroundDrawable(Drawable var1) {
      super.setBackgroundDrawable(var1);
      if (!this.mDrawFullUnderlineSet) {
         boolean var2;
         if (var1 == null) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.mDrawFullUnderline = var2;
      }

   }

   public void setBackgroundResource(@DrawableRes int var1) {
      super.setBackgroundResource(var1);
      if (!this.mDrawFullUnderlineSet) {
         boolean var2;
         if (var1 == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.mDrawFullUnderline = var2;
      }

   }

   public void setDrawFullUnderline(boolean var1) {
      this.mDrawFullUnderline = var1;
      this.mDrawFullUnderlineSet = true;
      this.invalidate();
   }

   public void setPadding(int var1, int var2, int var3, int var4) {
      int var5 = var4;
      if (var4 < this.mMinPaddingBottom) {
         var5 = this.mMinPaddingBottom;
      }

      super.setPadding(var1, var2, var3, var5);
   }

   public void setTabIndicatorColor(@ColorInt int var1) {
      this.mIndicatorColor = var1;
      this.mTabPaint.setColor(this.mIndicatorColor);
      this.invalidate();
   }

   public void setTabIndicatorColorResource(@ColorRes int var1) {
      this.setTabIndicatorColor(ContextCompat.getColor(this.getContext(), var1));
   }

   public void setTextSpacing(int var1) {
      int var2 = var1;
      if (var1 < this.mMinTextSpacing) {
         var2 = this.mMinTextSpacing;
      }

      super.setTextSpacing(var2);
   }

   void updateTextPositions(int var1, float var2, boolean var3) {
      Rect var4 = this.mTempRect;
      int var5 = this.getHeight();
      int var6 = this.mCurrText.getLeft();
      int var7 = this.mTabPadding;
      int var8 = this.mCurrText.getRight();
      int var9 = this.mTabPadding;
      int var10 = var5 - this.mIndicatorHeight;
      var4.set(var6 - var7, var10, var8 + var9, var5);
      super.updateTextPositions(var1, var2, var3);
      this.mTabAlpha = (int)(Math.abs(var2 - 0.5F) * 2.0F * 255.0F);
      var4.union(this.mCurrText.getLeft() - this.mTabPadding, var10, this.mCurrText.getRight() + this.mTabPadding, var5);
      this.invalidate(var4);
   }
}
