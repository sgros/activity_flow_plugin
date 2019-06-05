package android.support.v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils.TruncateAt;
import android.text.method.SingleLineTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Locale;

@ViewPager.DecorView
public class PagerTitleStrip extends ViewGroup {
   private static final int[] ATTRS = new int[]{16842804, 16842901, 16842904, 16842927};
   private static final float SIDE_ALPHA = 0.6F;
   private static final int[] TEXT_ATTRS = new int[]{16843660};
   private static final int TEXT_SPACING = 16;
   TextView mCurrText;
   private int mGravity;
   private int mLastKnownCurrentPage;
   float mLastKnownPositionOffset;
   TextView mNextText;
   private int mNonPrimaryAlpha;
   private final PagerTitleStrip.PageListener mPageListener;
   ViewPager mPager;
   TextView mPrevText;
   private int mScaledTextSpacing;
   int mTextColor;
   private boolean mUpdatingPositions;
   private boolean mUpdatingText;
   private WeakReference mWatchingAdapter;

   public PagerTitleStrip(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public PagerTitleStrip(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mLastKnownCurrentPage = -1;
      this.mLastKnownPositionOffset = -1.0F;
      this.mPageListener = new PagerTitleStrip.PageListener();
      TextView var3 = new TextView(var1);
      this.mPrevText = var3;
      this.addView(var3);
      var3 = new TextView(var1);
      this.mCurrText = var3;
      this.addView(var3);
      var3 = new TextView(var1);
      this.mNextText = var3;
      this.addView(var3);
      TypedArray var7 = var1.obtainStyledAttributes(var2, ATTRS);
      boolean var4 = false;
      int var5 = var7.getResourceId(0, 0);
      if (var5 != 0) {
         TextViewCompat.setTextAppearance(this.mPrevText, var5);
         TextViewCompat.setTextAppearance(this.mCurrText, var5);
         TextViewCompat.setTextAppearance(this.mNextText, var5);
      }

      int var6 = var7.getDimensionPixelSize(1, 0);
      if (var6 != 0) {
         this.setTextSize(0, (float)var6);
      }

      if (var7.hasValue(2)) {
         var6 = var7.getColor(2, 0);
         this.mPrevText.setTextColor(var6);
         this.mCurrText.setTextColor(var6);
         this.mNextText.setTextColor(var6);
      }

      this.mGravity = var7.getInteger(3, 80);
      var7.recycle();
      this.mTextColor = this.mCurrText.getTextColors().getDefaultColor();
      this.setNonPrimaryAlpha(0.6F);
      this.mPrevText.setEllipsize(TruncateAt.END);
      this.mCurrText.setEllipsize(TruncateAt.END);
      this.mNextText.setEllipsize(TruncateAt.END);
      if (var5 != 0) {
         var7 = var1.obtainStyledAttributes(var5, TEXT_ATTRS);
         var4 = var7.getBoolean(0, false);
         var7.recycle();
      }

      if (var4) {
         setSingleLineAllCaps(this.mPrevText);
         setSingleLineAllCaps(this.mCurrText);
         setSingleLineAllCaps(this.mNextText);
      } else {
         this.mPrevText.setSingleLine();
         this.mCurrText.setSingleLine();
         this.mNextText.setSingleLine();
      }

      this.mScaledTextSpacing = (int)(16.0F * var1.getResources().getDisplayMetrics().density);
   }

   private static void setSingleLineAllCaps(TextView var0) {
      var0.setTransformationMethod(new PagerTitleStrip.SingleLineAllCapsTransform(var0.getContext()));
   }

   int getMinHeight() {
      Drawable var1 = this.getBackground();
      int var2;
      if (var1 != null) {
         var2 = var1.getIntrinsicHeight();
      } else {
         var2 = 0;
      }

      return var2;
   }

   public int getTextSpacing() {
      return this.mScaledTextSpacing;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      ViewParent var1 = this.getParent();
      if (!(var1 instanceof ViewPager)) {
         throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
      } else {
         ViewPager var3 = (ViewPager)var1;
         PagerAdapter var2 = var3.getAdapter();
         var3.setInternalPageChangeListener(this.mPageListener);
         var3.addOnAdapterChangeListener(this.mPageListener);
         this.mPager = var3;
         PagerAdapter var4;
         if (this.mWatchingAdapter != null) {
            var4 = (PagerAdapter)this.mWatchingAdapter.get();
         } else {
            var4 = null;
         }

         this.updateAdapter(var4, var2);
      }
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (this.mPager != null) {
         this.updateAdapter(this.mPager.getAdapter(), (PagerAdapter)null);
         this.mPager.setInternalPageChangeListener((ViewPager.OnPageChangeListener)null);
         this.mPager.removeOnAdapterChangeListener(this.mPageListener);
         this.mPager = null;
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (this.mPager != null) {
         float var6 = this.mLastKnownPositionOffset;
         float var7 = 0.0F;
         if (var6 >= 0.0F) {
            var7 = this.mLastKnownPositionOffset;
         }

         this.updateTextPositions(this.mLastKnownCurrentPage, var7, true);
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (MeasureSpec.getMode(var1) != 1073741824) {
         throw new IllegalStateException("Must measure with an exact width");
      } else {
         int var3 = this.getPaddingTop() + this.getPaddingBottom();
         int var4 = getChildMeasureSpec(var2, var3, -2);
         int var5 = MeasureSpec.getSize(var1);
         var1 = getChildMeasureSpec(var1, (int)((float)var5 * 0.2F), -2);
         this.mPrevText.measure(var1, var4);
         this.mCurrText.measure(var1, var4);
         this.mNextText.measure(var1, var4);
         if (MeasureSpec.getMode(var2) == 1073741824) {
            var1 = MeasureSpec.getSize(var2);
         } else {
            var1 = this.mCurrText.getMeasuredHeight();
            var1 = Math.max(this.getMinHeight(), var1 + var3);
         }

         this.setMeasuredDimension(var5, View.resolveSizeAndState(var1, var2, this.mCurrText.getMeasuredState() << 16));
      }
   }

   public void requestLayout() {
      if (!this.mUpdatingText) {
         super.requestLayout();
      }

   }

   public void setGravity(int var1) {
      this.mGravity = var1;
      this.requestLayout();
   }

   public void setNonPrimaryAlpha(@FloatRange(from = 0.0D,to = 1.0D) float var1) {
      this.mNonPrimaryAlpha = (int)(var1 * 255.0F) & 255;
      int var2 = this.mNonPrimaryAlpha << 24 | this.mTextColor & 16777215;
      this.mPrevText.setTextColor(var2);
      this.mNextText.setTextColor(var2);
   }

   public void setTextColor(@ColorInt int var1) {
      this.mTextColor = var1;
      this.mCurrText.setTextColor(var1);
      var1 = this.mNonPrimaryAlpha << 24 | this.mTextColor & 16777215;
      this.mPrevText.setTextColor(var1);
      this.mNextText.setTextColor(var1);
   }

   public void setTextSize(int var1, float var2) {
      this.mPrevText.setTextSize(var1, var2);
      this.mCurrText.setTextSize(var1, var2);
      this.mNextText.setTextSize(var1, var2);
   }

   public void setTextSpacing(int var1) {
      this.mScaledTextSpacing = var1;
      this.requestLayout();
   }

   void updateAdapter(PagerAdapter var1, PagerAdapter var2) {
      if (var1 != null) {
         var1.unregisterDataSetObserver(this.mPageListener);
         this.mWatchingAdapter = null;
      }

      if (var2 != null) {
         var2.registerDataSetObserver(this.mPageListener);
         this.mWatchingAdapter = new WeakReference(var2);
      }

      if (this.mPager != null) {
         this.mLastKnownCurrentPage = -1;
         this.mLastKnownPositionOffset = -1.0F;
         this.updateText(this.mPager.getCurrentItem(), var2);
         this.requestLayout();
      }

   }

   void updateText(int var1, PagerAdapter var2) {
      int var3;
      if (var2 != null) {
         var3 = var2.getCount();
      } else {
         var3 = 0;
      }

      this.mUpdatingText = true;
      Object var4 = null;
      CharSequence var5;
      if (var1 >= 1 && var2 != null) {
         var5 = var2.getPageTitle(var1 - 1);
      } else {
         var5 = null;
      }

      this.mPrevText.setText(var5);
      TextView var6 = this.mCurrText;
      if (var2 != null && var1 < var3) {
         var5 = var2.getPageTitle(var1);
      } else {
         var5 = null;
      }

      var6.setText(var5);
      int var7 = var1 + 1;
      var5 = (CharSequence)var4;
      if (var7 < var3) {
         var5 = (CharSequence)var4;
         if (var2 != null) {
            var5 = var2.getPageTitle(var7);
         }
      }

      this.mNextText.setText(var5);
      var3 = MeasureSpec.makeMeasureSpec(Math.max(0, (int)((float)(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight()) * 0.8F)), Integer.MIN_VALUE);
      var7 = MeasureSpec.makeMeasureSpec(Math.max(0, this.getHeight() - this.getPaddingTop() - this.getPaddingBottom()), Integer.MIN_VALUE);
      this.mPrevText.measure(var3, var7);
      this.mCurrText.measure(var3, var7);
      this.mNextText.measure(var3, var7);
      this.mLastKnownCurrentPage = var1;
      if (!this.mUpdatingPositions) {
         this.updateTextPositions(var1, this.mLastKnownPositionOffset, false);
      }

      this.mUpdatingText = false;
   }

   void updateTextPositions(int var1, float var2, boolean var3) {
      if (var1 != this.mLastKnownCurrentPage) {
         this.updateText(var1, this.mPager.getAdapter());
      } else if (!var3 && var2 == this.mLastKnownPositionOffset) {
         return;
      }

      this.mUpdatingPositions = true;
      int var4 = this.mPrevText.getMeasuredWidth();
      int var5 = this.mCurrText.getMeasuredWidth();
      int var6 = this.mNextText.getMeasuredWidth();
      int var7 = var5 / 2;
      int var8 = this.getWidth();
      int var9 = this.getHeight();
      int var10 = this.getPaddingLeft();
      int var11 = this.getPaddingRight();
      var1 = this.getPaddingTop();
      int var12 = this.getPaddingBottom();
      int var13 = var11 + var7;
      float var14 = 0.5F + var2;
      float var15 = var14;
      if (var14 > 1.0F) {
         var15 = var14 - 1.0F;
      }

      var7 = var8 - var13 - (int)((float)(var8 - (var10 + var7) - var13) * var15) - var7;
      var13 = var5 + var7;
      int var16 = this.mPrevText.getBaseline();
      var5 = this.mCurrText.getBaseline();
      int var17 = this.mNextText.getBaseline();
      int var18 = Math.max(Math.max(var16, var5), var17);
      var16 = var18 - var16;
      var5 = var18 - var5;
      var18 -= var17;
      var17 = this.mPrevText.getMeasuredHeight();
      int var19 = this.mCurrText.getMeasuredHeight();
      int var20 = this.mNextText.getMeasuredHeight();
      var19 = Math.max(Math.max(var17 + var16, var19 + var5), var20 + var18);
      var17 = this.mGravity & 112;
      if (var17 != 16) {
         if (var17 != 80) {
            var12 = var16 + var1;
            var5 += var1;
            var1 += var18;
         } else {
            var1 = var9 - var12 - var19;
            var12 = var16 + var1;
            var5 += var1;
            var1 += var18;
         }
      } else {
         var1 = (var9 - var1 - var12 - var19) / 2;
         var12 = var16 + var1;
         var5 += var1;
         var1 += var18;
      }

      this.mCurrText.layout(var7, var5, var13, this.mCurrText.getMeasuredHeight() + var5);
      var5 = Math.min(var10, var7 - this.mScaledTextSpacing - var4);
      this.mPrevText.layout(var5, var12, var4 + var5, this.mPrevText.getMeasuredHeight() + var12);
      var12 = Math.max(var8 - var11 - var6, var13 + this.mScaledTextSpacing);
      this.mNextText.layout(var12, var1, var12 + var6, this.mNextText.getMeasuredHeight() + var1);
      this.mLastKnownPositionOffset = var2;
      this.mUpdatingPositions = false;
   }

   private class PageListener extends DataSetObserver implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener {
      private int mScrollState;

      PageListener() {
      }

      public void onAdapterChanged(ViewPager var1, PagerAdapter var2, PagerAdapter var3) {
         PagerTitleStrip.this.updateAdapter(var2, var3);
      }

      public void onChanged() {
         PagerTitleStrip.this.updateText(PagerTitleStrip.this.mPager.getCurrentItem(), PagerTitleStrip.this.mPager.getAdapter());
         float var1 = PagerTitleStrip.this.mLastKnownPositionOffset;
         float var2 = 0.0F;
         if (var1 >= 0.0F) {
            var2 = PagerTitleStrip.this.mLastKnownPositionOffset;
         }

         PagerTitleStrip.this.updateTextPositions(PagerTitleStrip.this.mPager.getCurrentItem(), var2, true);
      }

      public void onPageScrollStateChanged(int var1) {
         this.mScrollState = var1;
      }

      public void onPageScrolled(int var1, float var2, int var3) {
         var3 = var1;
         if (var2 > 0.5F) {
            var3 = var1 + 1;
         }

         PagerTitleStrip.this.updateTextPositions(var3, var2, false);
      }

      public void onPageSelected(int var1) {
         if (this.mScrollState == 0) {
            PagerTitleStrip.this.updateText(PagerTitleStrip.this.mPager.getCurrentItem(), PagerTitleStrip.this.mPager.getAdapter());
            float var2 = PagerTitleStrip.this.mLastKnownPositionOffset;
            float var3 = 0.0F;
            if (var2 >= 0.0F) {
               var3 = PagerTitleStrip.this.mLastKnownPositionOffset;
            }

            PagerTitleStrip.this.updateTextPositions(PagerTitleStrip.this.mPager.getCurrentItem(), var3, true);
         }

      }
   }

   private static class SingleLineAllCapsTransform extends SingleLineTransformationMethod {
      private Locale mLocale;

      SingleLineAllCapsTransform(Context var1) {
         this.mLocale = var1.getResources().getConfiguration().locale;
      }

      public CharSequence getTransformation(CharSequence var1, View var2) {
         var1 = super.getTransformation(var1, var2);
         String var3;
         if (var1 != null) {
            var3 = var1.toString().toUpperCase(this.mLocale);
         } else {
            var3 = null;
         }

         return var3;
      }
   }
}
