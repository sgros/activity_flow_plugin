package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import java.util.Locale;
import org.telegram.ui.ActionBar.Theme;

public class NumberPicker extends LinearLayout {
   private static final int DEFAULT_LAYOUT_RESOURCE_ID = 0;
   private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300L;
   private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 800;
   private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
   private static final int SELECTOR_MIDDLE_ITEM_INDEX = 1;
   private static final int SELECTOR_WHEEL_ITEM_COUNT = 3;
   private static final int SIZE_UNSPECIFIED = -1;
   private static final int SNAP_SCROLL_DURATION = 300;
   private static final float TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9F;
   private static final int UNSCALED_DEFAULT_SELECTION_DIVIDERS_DISTANCE = 48;
   private static final int UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT = 2;
   private Scroller mAdjustScroller;
   private int mBottomSelectionDividerBottom;
   private NumberPicker.ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
   private boolean mComputeMaxWidth;
   private int mCurrentScrollOffset;
   private boolean mDecrementVirtualButtonPressed;
   private String[] mDisplayedValues;
   private Scroller mFlingScroller;
   private NumberPicker.Formatter mFormatter;
   private boolean mIncrementVirtualButtonPressed;
   private boolean mIngonreMoveEvents;
   private int mInitialScrollOffset = Integer.MIN_VALUE;
   private TextView mInputText;
   private long mLastDownEventTime;
   private float mLastDownEventY;
   private float mLastDownOrMoveEventY;
   private int mLastHandledDownDpadKeyCode = -1;
   private int mLastHoveredChildVirtualViewId;
   private long mLongPressUpdateInterval = 300L;
   private int mMaxHeight;
   private int mMaxValue;
   private int mMaxWidth;
   private int mMaximumFlingVelocity;
   private int mMinHeight;
   private int mMinValue;
   private int mMinWidth;
   private int mMinimumFlingVelocity;
   private NumberPicker.OnScrollListener mOnScrollListener;
   private NumberPicker.OnValueChangeListener mOnValueChangeListener;
   private NumberPicker.PressedStateHelper mPressedStateHelper;
   private int mPreviousScrollerY;
   private int mScrollState = 0;
   private Paint mSelectionDivider;
   private int mSelectionDividerHeight;
   private int mSelectionDividersDistance;
   private int mSelectorElementHeight;
   private final SparseArray mSelectorIndexToStringCache = new SparseArray();
   private final int[] mSelectorIndices = new int[3];
   private int mSelectorTextGapHeight;
   private Paint mSelectorWheelPaint;
   private int mSolidColor;
   private int mTextSize;
   private int mTopSelectionDividerTop;
   private int mTouchSlop;
   private int mValue;
   private VelocityTracker mVelocityTracker;
   private boolean mWrapSelectorWheel;

   public NumberPicker(Context var1) {
      super(var1);
      this.init();
   }

   public NumberPicker(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   public NumberPicker(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   private void changeValueByOne(boolean var1) {
      this.mInputText.setVisibility(4);
      if (!this.moveToFinalScrollerPosition(this.mFlingScroller)) {
         this.moveToFinalScrollerPosition(this.mAdjustScroller);
      }

      this.mPreviousScrollerY = 0;
      if (var1) {
         this.mFlingScroller.startScroll(0, 0, 0, -this.mSelectorElementHeight, 300);
      } else {
         this.mFlingScroller.startScroll(0, 0, 0, this.mSelectorElementHeight, 300);
      }

      this.invalidate();
   }

   private void decrementSelectorIndices(int[] var1) {
      System.arraycopy(var1, 0, var1, 1, var1.length - 1);
      int var2 = var1[1] - 1;
      int var3 = var2;
      if (this.mWrapSelectorWheel) {
         var3 = var2;
         if (var2 < this.mMinValue) {
            var3 = this.mMaxValue;
         }
      }

      var1[0] = var3;
      this.ensureCachedScrollSelectorValue(var3);
   }

   private void ensureCachedScrollSelectorValue(int var1) {
      SparseArray var2 = this.mSelectorIndexToStringCache;
      if ((String)var2.get(var1) == null) {
         int var3 = this.mMinValue;
         String var4;
         if (var1 >= var3 && var1 <= this.mMaxValue) {
            String[] var5 = this.mDisplayedValues;
            if (var5 != null) {
               var4 = var5[var1 - var3];
            } else {
               var4 = this.formatNumber(var1);
            }
         } else {
            var4 = "";
         }

         var2.put(var1, var4);
      }
   }

   private boolean ensureScrollWheelAdjusted() {
      int var1 = this.mInitialScrollOffset - this.mCurrentScrollOffset;
      if (var1 != 0) {
         this.mPreviousScrollerY = 0;
         int var2 = Math.abs(var1);
         int var3 = this.mSelectorElementHeight;
         int var4 = var1;
         if (var2 > var3 / 2) {
            var4 = var3;
            if (var1 > 0) {
               var4 = -var3;
            }

            var4 += var1;
         }

         this.mAdjustScroller.startScroll(0, 0, 0, var4, 800);
         this.invalidate();
         return true;
      } else {
         return false;
      }
   }

   private void fling(int var1) {
      this.mPreviousScrollerY = 0;
      if (var1 > 0) {
         this.mFlingScroller.fling(0, 0, 0, var1, 0, 0, 0, Integer.MAX_VALUE);
      } else {
         this.mFlingScroller.fling(0, Integer.MAX_VALUE, 0, var1, 0, 0, 0, Integer.MAX_VALUE);
      }

      this.invalidate();
   }

   private String formatNumber(int var1) {
      NumberPicker.Formatter var2 = this.mFormatter;
      String var3;
      if (var2 != null) {
         var3 = var2.format(var1);
      } else {
         var3 = formatNumberWithLocale(var1);
      }

      return var3;
   }

   private static String formatNumberWithLocale(int var0) {
      return String.format(Locale.getDefault(), "%d", var0);
   }

   private int getSelectedPos(String var1) {
      boolean var10001;
      int var2;
      if (this.mDisplayedValues == null) {
         try {
            var2 = Integer.parseInt(var1);
            return var2;
         } catch (NumberFormatException var3) {
            var10001 = false;
         }
      } else {
         for(var2 = 0; var2 < this.mDisplayedValues.length; ++var2) {
            var1 = var1.toLowerCase();
            if (this.mDisplayedValues[var2].toLowerCase().startsWith(var1)) {
               return this.mMinValue + var2;
            }
         }

         try {
            var2 = Integer.parseInt(var1);
            return var2;
         } catch (NumberFormatException var4) {
            var10001 = false;
         }
      }

      return this.mMinValue;
   }

   private int getWrappedSelectorIndex(int var1) {
      int var2 = this.mMaxValue;
      int var3;
      if (var1 > var2) {
         var3 = this.mMinValue;
         return var3 + (var1 - var2) % (var2 - var3) - 1;
      } else {
         var3 = this.mMinValue;
         return var1 < var3 ? var2 - (var3 - var1) % (var2 - var3) + 1 : var1;
      }
   }

   private void incrementSelectorIndices(int[] var1) {
      System.arraycopy(var1, 1, var1, 0, var1.length - 1);
      int var2 = var1[var1.length - 2] + 1;
      int var3 = var2;
      if (this.mWrapSelectorWheel) {
         var3 = var2;
         if (var2 > this.mMaxValue) {
            var3 = this.mMinValue;
         }
      }

      var1[var1.length - 1] = var3;
      this.ensureCachedScrollSelectorValue(var3);
   }

   private void init() {
      this.mSolidColor = 0;
      this.mSelectionDivider = new Paint();
      this.mSelectionDivider.setColor(Theme.getColor("dialogButton"));
      this.mSelectionDividerHeight = (int)TypedValue.applyDimension(1, 2.0F, this.getResources().getDisplayMetrics());
      this.mSelectionDividersDistance = (int)TypedValue.applyDimension(1, 48.0F, this.getResources().getDisplayMetrics());
      this.mMinHeight = -1;
      this.mMaxHeight = (int)TypedValue.applyDimension(1, 180.0F, this.getResources().getDisplayMetrics());
      int var1 = this.mMinHeight;
      int var2;
      if (var1 != -1) {
         var2 = this.mMaxHeight;
         if (var2 != -1 && var1 > var2) {
            throw new IllegalArgumentException("minHeight > maxHeight");
         }
      }

      this.mMinWidth = (int)TypedValue.applyDimension(1, 64.0F, this.getResources().getDisplayMetrics());
      this.mMaxWidth = -1;
      var1 = this.mMinWidth;
      if (var1 != -1) {
         var2 = this.mMaxWidth;
         if (var2 != -1 && var1 > var2) {
            throw new IllegalArgumentException("minWidth > maxWidth");
         }
      }

      boolean var3;
      if (this.mMaxWidth == -1) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.mComputeMaxWidth = var3;
      this.mPressedStateHelper = new NumberPicker.PressedStateHelper();
      this.setWillNotDraw(false);
      this.mInputText = new TextView(this.getContext());
      this.mInputText.setGravity(17);
      this.mInputText.setSingleLine(true);
      this.mInputText.setTextColor(Theme.getColor("dialogTextBlack"));
      this.mInputText.setBackgroundResource(0);
      this.mInputText.setTextSize(1, 18.0F);
      this.addView(this.mInputText, new LayoutParams(-1, -2));
      ViewConfiguration var4 = ViewConfiguration.get(this.getContext());
      this.mTouchSlop = var4.getScaledTouchSlop();
      this.mMinimumFlingVelocity = var4.getScaledMinimumFlingVelocity();
      this.mMaximumFlingVelocity = var4.getScaledMaximumFlingVelocity() / 8;
      this.mTextSize = (int)this.mInputText.getTextSize();
      Paint var5 = new Paint();
      var5.setAntiAlias(true);
      var5.setTextAlign(Align.CENTER);
      var5.setTextSize((float)this.mTextSize);
      var5.setTypeface(this.mInputText.getTypeface());
      var5.setColor(this.mInputText.getTextColors().getColorForState(LinearLayout.ENABLED_STATE_SET, -1));
      this.mSelectorWheelPaint = var5;
      this.mFlingScroller = new Scroller(this.getContext(), (Interpolator)null, true);
      this.mAdjustScroller = new Scroller(this.getContext(), new DecelerateInterpolator(2.5F));
      this.updateInputTextView();
   }

   private void initializeFadingEdges() {
      this.setVerticalFadingEdgeEnabled(true);
      this.setFadingEdgeLength((this.getBottom() - this.getTop() - this.mTextSize) / 2);
   }

   private void initializeSelectorWheel() {
      this.initializeSelectorWheelIndices();
      int[] var1 = this.mSelectorIndices;
      int var2 = var1.length;
      int var3 = this.mTextSize;
      this.mSelectorTextGapHeight = (int)((float)(this.getBottom() - this.getTop() - var2 * var3) / (float)var1.length + 0.5F);
      this.mSelectorElementHeight = this.mTextSize + this.mSelectorTextGapHeight;
      this.mInitialScrollOffset = this.mInputText.getBaseline() + this.mInputText.getTop() - this.mSelectorElementHeight * 1;
      this.mCurrentScrollOffset = this.mInitialScrollOffset;
      this.updateInputTextView();
   }

   private void initializeSelectorWheelIndices() {
      this.mSelectorIndexToStringCache.clear();
      int[] var1 = this.mSelectorIndices;
      int var2 = this.getValue();

      for(int var3 = 0; var3 < this.mSelectorIndices.length; ++var3) {
         int var4 = var3 - 1 + var2;
         int var5 = var4;
         if (this.mWrapSelectorWheel) {
            var5 = this.getWrappedSelectorIndex(var4);
         }

         var1[var3] = var5;
         this.ensureCachedScrollSelectorValue(var1[var3]);
      }

   }

   private int makeMeasureSpec(int var1, int var2) {
      if (var2 == -1) {
         return var1;
      } else {
         int var3 = MeasureSpec.getSize(var1);
         int var4 = MeasureSpec.getMode(var1);
         if (var4 != Integer.MIN_VALUE) {
            if (var4 != 0) {
               if (var4 == 1073741824) {
                  return var1;
               } else {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Unknown measure mode: ");
                  var5.append(var4);
                  throw new IllegalArgumentException(var5.toString());
               }
            } else {
               return MeasureSpec.makeMeasureSpec(var2, 1073741824);
            }
         } else {
            return MeasureSpec.makeMeasureSpec(Math.min(var3, var2), 1073741824);
         }
      }
   }

   private boolean moveToFinalScrollerPosition(Scroller var1) {
      var1.forceFinished(true);
      int var2 = var1.getFinalY() - var1.getCurrY();
      int var3 = this.mCurrentScrollOffset;
      int var4 = this.mSelectorElementHeight;
      var4 = this.mInitialScrollOffset - (var3 + var2) % var4;
      if (var4 != 0) {
         int var5 = Math.abs(var4);
         int var6 = this.mSelectorElementHeight;
         var3 = var4;
         if (var5 > var6 / 2) {
            if (var4 > 0) {
               var3 = var4 - var6;
            } else {
               var3 = var4 + var6;
            }
         }

         this.scrollBy(0, var2 + var3);
         return true;
      } else {
         return false;
      }
   }

   private void notifyChange(int var1, int var2) {
      NumberPicker.OnValueChangeListener var3 = this.mOnValueChangeListener;
      if (var3 != null) {
         var3.onValueChange(this, var1, this.mValue);
      }

   }

   private void onScrollStateChange(int var1) {
      if (this.mScrollState != var1) {
         this.mScrollState = var1;
         NumberPicker.OnScrollListener var2 = this.mOnScrollListener;
         if (var2 != null) {
            var2.onScrollStateChange(this, var1);
         }

         if (var1 == 0) {
            AccessibilityManager var3 = (AccessibilityManager)this.getContext().getSystemService("accessibility");
            if (var3.isTouchExplorationEnabled()) {
               String[] var5 = this.mDisplayedValues;
               String var6;
               if (var5 == null) {
                  var6 = this.formatNumber(this.mValue);
               } else {
                  var6 = var5[this.mValue - this.mMinValue];
               }

               AccessibilityEvent var4 = AccessibilityEvent.obtain();
               var4.setEventType(16384);
               var4.getText().add(var6);
               var3.sendAccessibilityEvent(var4);
            }
         }

      }
   }

   private void onScrollerFinished(Scroller var1) {
      if (var1 == this.mFlingScroller) {
         if (!this.ensureScrollWheelAdjusted()) {
            this.updateInputTextView();
         }

         this.onScrollStateChange(0);
      } else if (this.mScrollState != 1) {
         this.updateInputTextView();
      }

   }

   private void postChangeCurrentByOneFromLongPress(boolean var1, long var2) {
      NumberPicker.ChangeCurrentByOneFromLongPressCommand var4 = this.mChangeCurrentByOneFromLongPressCommand;
      if (var4 == null) {
         this.mChangeCurrentByOneFromLongPressCommand = new NumberPicker.ChangeCurrentByOneFromLongPressCommand();
      } else {
         this.removeCallbacks(var4);
      }

      this.mChangeCurrentByOneFromLongPressCommand.setStep(var1);
      this.postDelayed(this.mChangeCurrentByOneFromLongPressCommand, var2);
   }

   private void removeAllCallbacks() {
      NumberPicker.ChangeCurrentByOneFromLongPressCommand var1 = this.mChangeCurrentByOneFromLongPressCommand;
      if (var1 != null) {
         this.removeCallbacks(var1);
      }

      this.mPressedStateHelper.cancel();
   }

   private void removeChangeCurrentByOneFromLongPress() {
      NumberPicker.ChangeCurrentByOneFromLongPressCommand var1 = this.mChangeCurrentByOneFromLongPressCommand;
      if (var1 != null) {
         this.removeCallbacks(var1);
      }

   }

   public static int resolveSizeAndState(int var0, int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      int var4 = MeasureSpec.getSize(var1);
      if (var3 != Integer.MIN_VALUE) {
         var1 = var0;
         if (var3 != 0) {
            if (var3 != 1073741824) {
               var1 = var0;
            } else {
               var1 = var4;
            }
         }
      } else {
         var1 = var0;
         if (var4 < var0) {
            var1 = 16777216 | var4;
         }
      }

      return var1 | -16777216 & var2;
   }

   private int resolveSizeAndStateRespectingMinSize(int var1, int var2, int var3) {
      return var1 != -1 ? resolveSizeAndState(Math.max(var1, var2), var3, 0) : var2;
   }

   private void setValueInternal(int var1, boolean var2) {
      if (this.mValue != var1) {
         if (this.mWrapSelectorWheel) {
            var1 = this.getWrappedSelectorIndex(var1);
         } else {
            var1 = Math.min(Math.max(var1, this.mMinValue), this.mMaxValue);
         }

         int var3 = this.mValue;
         this.mValue = var1;
         this.updateInputTextView();
         if (var2) {
            this.notifyChange(var3, var1);
         }

         this.initializeSelectorWheelIndices();
         this.invalidate();
      }
   }

   private void tryComputeMaxWidth() {
      if (this.mComputeMaxWidth) {
         String[] var1 = this.mDisplayedValues;
         int var2 = 0;
         int var3 = 0;
         int var4;
         float var7;
         if (var1 == null) {
            var4 = 0;

            float var5;
            for(var5 = 0.0F; var4 <= 9; var5 = var7) {
               float var6 = this.mSelectorWheelPaint.measureText(formatNumberWithLocale(var4));
               var7 = var5;
               if (var6 > var5) {
                  var7 = var6;
               }

               ++var4;
            }

            for(var4 = this.mMaxValue; var4 > 0; var4 /= 10) {
               ++var3;
            }

            var4 = (int)((float)var3 * var5);
         } else {
            int var8 = var1.length;

            for(var4 = 0; var2 < var8; var4 = var3) {
               String var9 = var1[var2];
               var7 = this.mSelectorWheelPaint.measureText(var9);
               var3 = var4;
               if (var7 > (float)var4) {
                  var3 = (int)var7;
               }

               ++var2;
            }
         }

         var4 += this.mInputText.getPaddingLeft() + this.mInputText.getPaddingRight();
         if (this.mMaxWidth != var4) {
            var3 = this.mMinWidth;
            if (var4 > var3) {
               this.mMaxWidth = var4;
            } else {
               this.mMaxWidth = var3;
            }

            this.invalidate();
         }

      }
   }

   private boolean updateInputTextView() {
      String[] var1 = this.mDisplayedValues;
      String var2;
      if (var1 == null) {
         var2 = this.formatNumber(this.mValue);
      } else {
         var2 = var1[this.mValue - this.mMinValue];
      }

      if (!TextUtils.isEmpty(var2) && !var2.equals(this.mInputText.getText().toString())) {
         this.mInputText.setText(var2);
         return true;
      } else {
         return false;
      }
   }

   public void computeScroll() {
      Scroller var1 = this.mFlingScroller;
      Scroller var2 = var1;
      if (var1.isFinished()) {
         var1 = this.mAdjustScroller;
         var2 = var1;
         if (var1.isFinished()) {
            return;
         }
      }

      var2.computeScrollOffset();
      int var3 = var2.getCurrY();
      if (this.mPreviousScrollerY == 0) {
         this.mPreviousScrollerY = var2.getStartY();
      }

      this.scrollBy(0, var3 - this.mPreviousScrollerY);
      this.mPreviousScrollerY = var3;
      if (var2.isFinished()) {
         this.onScrollerFinished(var2);
      } else {
         this.invalidate();
      }

   }

   protected int computeVerticalScrollExtent() {
      return this.getHeight();
   }

   protected int computeVerticalScrollOffset() {
      return this.mCurrentScrollOffset;
   }

   protected int computeVerticalScrollRange() {
      return (this.mMaxValue - this.mMinValue + 1) * this.mSelectorElementHeight;
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 != 19 && var2 != 20) {
         if (var2 == 23 || var2 == 66) {
            this.removeAllCallbacks();
         }
      } else {
         int var3 = var1.getAction();
         if (var3 != 0) {
            if (var3 == 1 && this.mLastHandledDownDpadKeyCode == var2) {
               this.mLastHandledDownDpadKeyCode = -1;
               return true;
            }
         } else {
            if (!this.mWrapSelectorWheel && var2 != 20) {
               if (this.getValue() <= this.getMinValue()) {
                  return super.dispatchKeyEvent(var1);
               }
            } else if (this.getValue() >= this.getMaxValue()) {
               return super.dispatchKeyEvent(var1);
            }

            this.requestFocus();
            this.mLastHandledDownDpadKeyCode = var2;
            this.removeAllCallbacks();
            if (this.mFlingScroller.isFinished()) {
               boolean var4;
               if (var2 == 20) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               this.changeValueByOne(var4);
            }

            return true;
         }
      }

      return super.dispatchKeyEvent(var1);
   }

   public boolean dispatchTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 1 || var2 == 3) {
         this.removeAllCallbacks();
      }

      return super.dispatchTouchEvent(var1);
   }

   public boolean dispatchTrackballEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 1 || var2 == 3) {
         this.removeAllCallbacks();
      }

      return super.dispatchTrackballEvent(var1);
   }

   public void finishScroll() {
      if (!this.mFlingScroller.isFinished() || !this.mAdjustScroller.isFinished()) {
         this.mFlingScroller.forceFinished(true);
         this.mAdjustScroller.forceFinished(true);
         this.mCurrentScrollOffset = this.mInitialScrollOffset;
         this.invalidate();
      }

   }

   protected float getBottomFadingEdgeStrength() {
      return 0.9F;
   }

   public String[] getDisplayedValues() {
      return this.mDisplayedValues;
   }

   public int getMaxValue() {
      return this.mMaxValue;
   }

   public int getMinValue() {
      return this.mMinValue;
   }

   public int getSolidColor() {
      return this.mSolidColor;
   }

   protected float getTopFadingEdgeStrength() {
      return 0.9F;
   }

   public int getValue() {
      return this.mValue;
   }

   public boolean getWrapSelectorWheel() {
      return this.mWrapSelectorWheel;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.removeAllCallbacks();
   }

   protected void onDraw(Canvas var1) {
      float var2 = (float)((this.getRight() - this.getLeft()) / 2);
      float var3 = (float)this.mCurrentScrollOffset;
      int[] var4 = this.mSelectorIndices;

      int var5;
      int var6;
      for(var5 = 0; var5 < var4.length; ++var5) {
         var6 = var4[var5];
         String var7 = (String)this.mSelectorIndexToStringCache.get(var6);
         if (var5 != 1 || this.mInputText.getVisibility() != 0) {
            var1.drawText(var7, var2, var3, this.mSelectorWheelPaint);
         }

         var3 += (float)this.mSelectorElementHeight;
      }

      var5 = this.mTopSelectionDividerTop;
      var6 = this.mSelectionDividerHeight;
      var1.drawRect(0.0F, (float)var5, (float)this.getRight(), (float)(var6 + var5), this.mSelectionDivider);
      var5 = this.mBottomSelectionDividerBottom;
      var1.drawRect(0.0F, (float)(var5 - this.mSelectionDividerHeight), (float)this.getRight(), (float)var5, this.mSelectionDivider);
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      if (!this.isEnabled()) {
         return false;
      } else if (var1.getActionMasked() != 0) {
         return false;
      } else {
         this.removeAllCallbacks();
         this.mInputText.setVisibility(4);
         float var2 = var1.getY();
         this.mLastDownEventY = var2;
         this.mLastDownOrMoveEventY = var2;
         this.mLastDownEventTime = var1.getEventTime();
         this.mIngonreMoveEvents = false;
         var2 = this.mLastDownEventY;
         if (var2 < (float)this.mTopSelectionDividerTop) {
            if (this.mScrollState == 0) {
               this.mPressedStateHelper.buttonPressDelayed(2);
            }
         } else if (var2 > (float)this.mBottomSelectionDividerBottom && this.mScrollState == 0) {
            this.mPressedStateHelper.buttonPressDelayed(1);
         }

         this.getParent().requestDisallowInterceptTouchEvent(true);
         if (!this.mFlingScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
            this.onScrollStateChange(0);
         } else if (!this.mAdjustScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
         } else {
            var2 = this.mLastDownEventY;
            if (var2 < (float)this.mTopSelectionDividerTop) {
               this.postChangeCurrentByOneFromLongPress(false, (long)ViewConfiguration.getLongPressTimeout());
            } else if (var2 > (float)this.mBottomSelectionDividerBottom) {
               this.postChangeCurrentByOneFromLongPress(true, (long)ViewConfiguration.getLongPressTimeout());
            }
         }

         return true;
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var5 = this.getMeasuredWidth();
      var4 = this.getMeasuredHeight();
      var3 = this.mInputText.getMeasuredWidth();
      var2 = this.mInputText.getMeasuredHeight();
      var5 = (var5 - var3) / 2;
      var4 = (var4 - var2) / 2;
      this.mInputText.layout(var5, var4, var3 + var5, var2 + var4);
      if (var1) {
         this.initializeSelectorWheel();
         this.initializeFadingEdges();
         var3 = this.getHeight();
         var2 = this.mSelectionDividersDistance;
         var4 = (var3 - var2) / 2;
         var3 = this.mSelectionDividerHeight;
         this.mTopSelectionDividerTop = var4 - var3;
         this.mBottomSelectionDividerBottom = this.mTopSelectionDividerTop + var3 * 2 + var2;
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(this.makeMeasureSpec(var1, this.mMaxWidth), this.makeMeasureSpec(var2, this.mMaxHeight));
      this.setMeasuredDimension(this.resolveSizeAndStateRespectingMinSize(this.mMinWidth, this.getMeasuredWidth(), var1), this.resolveSizeAndStateRespectingMinSize(this.mMinHeight, this.getMeasuredHeight(), var2));
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         this.mVelocityTracker.addMovement(var1);
         int var2 = var1.getActionMasked();
         if (var2 != 1) {
            if (var2 == 2 && !this.mIngonreMoveEvents) {
               float var3 = var1.getY();
               if (this.mScrollState != 1) {
                  if ((int)Math.abs(var3 - this.mLastDownEventY) > this.mTouchSlop) {
                     this.removeAllCallbacks();
                     this.onScrollStateChange(1);
                  }
               } else {
                  this.scrollBy(0, (int)(var3 - this.mLastDownOrMoveEventY));
                  this.invalidate();
               }

               this.mLastDownOrMoveEventY = var3;
            }
         } else {
            this.removeChangeCurrentByOneFromLongPress();
            this.mPressedStateHelper.cancel();
            VelocityTracker var4 = this.mVelocityTracker;
            var4.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
            var2 = (int)var4.getYVelocity();
            if (Math.abs(var2) > this.mMinimumFlingVelocity) {
               this.fling(var2);
               this.onScrollStateChange(2);
            } else {
               var2 = (int)var1.getY();
               int var5 = (int)Math.abs((float)var2 - this.mLastDownEventY);
               long var6 = var1.getEventTime();
               long var8 = this.mLastDownEventTime;
               if (var5 <= this.mTouchSlop && var6 - var8 < (long)ViewConfiguration.getTapTimeout()) {
                  var2 = var2 / this.mSelectorElementHeight - 1;
                  if (var2 > 0) {
                     this.changeValueByOne(true);
                     this.mPressedStateHelper.buttonTapped(1);
                  } else if (var2 < 0) {
                     this.changeValueByOne(false);
                     this.mPressedStateHelper.buttonTapped(2);
                  }
               } else {
                  this.ensureScrollWheelAdjusted();
               }

               this.onScrollStateChange(0);
            }

            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
         }

         return true;
      }
   }

   public void scrollBy(int var1, int var2) {
      int[] var3 = this.mSelectorIndices;
      if (!this.mWrapSelectorWheel && var2 > 0 && var3[1] <= this.mMinValue) {
         this.mCurrentScrollOffset = this.mInitialScrollOffset;
      } else if (!this.mWrapSelectorWheel && var2 < 0 && var3[1] >= this.mMaxValue) {
         this.mCurrentScrollOffset = this.mInitialScrollOffset;
      } else {
         this.mCurrentScrollOffset += var2;

         while(true) {
            var1 = this.mCurrentScrollOffset;
            if (var1 - this.mInitialScrollOffset <= this.mSelectorTextGapHeight) {
               while(true) {
                  var1 = this.mCurrentScrollOffset;
                  if (var1 - this.mInitialScrollOffset >= -this.mSelectorTextGapHeight) {
                     return;
                  }

                  this.mCurrentScrollOffset = var1 + this.mSelectorElementHeight;
                  this.incrementSelectorIndices(var3);
                  this.setValueInternal(var3[1], true);
                  if (!this.mWrapSelectorWheel && var3[1] >= this.mMaxValue) {
                     this.mCurrentScrollOffset = this.mInitialScrollOffset;
                  }
               }
            }

            this.mCurrentScrollOffset = var1 - this.mSelectorElementHeight;
            this.decrementSelectorIndices(var3);
            this.setValueInternal(var3[1], true);
            if (!this.mWrapSelectorWheel && var3[1] <= this.mMinValue) {
               this.mCurrentScrollOffset = this.mInitialScrollOffset;
            }
         }
      }
   }

   public void setDisplayedValues(String[] var1) {
      if (this.mDisplayedValues != var1) {
         this.mDisplayedValues = var1;
         this.updateInputTextView();
         this.initializeSelectorWheelIndices();
         this.tryComputeMaxWidth();
      }
   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      this.mInputText.setEnabled(var1);
   }

   public void setFormatter(NumberPicker.Formatter var1) {
      if (var1 != this.mFormatter) {
         this.mFormatter = var1;
         this.initializeSelectorWheelIndices();
         this.updateInputTextView();
      }
   }

   public void setMaxValue(int var1) {
      if (this.mMaxValue != var1) {
         if (var1 >= 0) {
            this.mMaxValue = var1;
            var1 = this.mMaxValue;
            if (var1 < this.mValue) {
               this.mValue = var1;
            }

            boolean var2;
            if (this.mMaxValue - this.mMinValue > this.mSelectorIndices.length) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.setWrapSelectorWheel(var2);
            this.initializeSelectorWheelIndices();
            this.updateInputTextView();
            this.tryComputeMaxWidth();
            this.invalidate();
         } else {
            throw new IllegalArgumentException("maxValue must be >= 0");
         }
      }
   }

   public void setMinValue(int var1) {
      if (this.mMinValue != var1) {
         if (var1 >= 0) {
            this.mMinValue = var1;
            var1 = this.mMinValue;
            if (var1 > this.mValue) {
               this.mValue = var1;
            }

            boolean var2;
            if (this.mMaxValue - this.mMinValue > this.mSelectorIndices.length) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.setWrapSelectorWheel(var2);
            this.initializeSelectorWheelIndices();
            this.updateInputTextView();
            this.tryComputeMaxWidth();
            this.invalidate();
         } else {
            throw new IllegalArgumentException("minValue must be >= 0");
         }
      }
   }

   public void setOnLongPressUpdateInterval(long var1) {
      this.mLongPressUpdateInterval = var1;
   }

   public void setOnScrollListener(NumberPicker.OnScrollListener var1) {
      this.mOnScrollListener = var1;
   }

   public void setOnValueChangedListener(NumberPicker.OnValueChangeListener var1) {
      this.mOnValueChangeListener = var1;
   }

   public void setSelectorColor(int var1) {
      this.mSelectionDivider.setColor(var1);
   }

   public void setTextColor(int var1) {
      this.mInputText.setTextColor(var1);
      this.mSelectorWheelPaint.setColor(var1);
   }

   public void setValue(int var1) {
      this.setValueInternal(var1, false);
   }

   public void setWrapSelectorWheel(boolean var1) {
      boolean var2;
      if (this.mMaxValue - this.mMinValue >= this.mSelectorIndices.length) {
         var2 = true;
      } else {
         var2 = false;
      }

      if ((!var1 || var2) && var1 != this.mWrapSelectorWheel) {
         this.mWrapSelectorWheel = var1;
      }

   }

   class ChangeCurrentByOneFromLongPressCommand implements Runnable {
      private boolean mIncrement;

      private void setStep(boolean var1) {
         this.mIncrement = var1;
      }

      public void run() {
         NumberPicker.this.changeValueByOne(this.mIncrement);
         NumberPicker var1 = NumberPicker.this;
         var1.postDelayed(this, var1.mLongPressUpdateInterval);
      }
   }

   public interface Formatter {
      String format(int var1);
   }

   public interface OnScrollListener {
      int SCROLL_STATE_FLING = 2;
      int SCROLL_STATE_IDLE = 0;
      int SCROLL_STATE_TOUCH_SCROLL = 1;

      void onScrollStateChange(NumberPicker var1, int var2);
   }

   public interface OnValueChangeListener {
      void onValueChange(NumberPicker var1, int var2, int var3);
   }

   class PressedStateHelper implements Runnable {
      public static final int BUTTON_DECREMENT = 2;
      public static final int BUTTON_INCREMENT = 1;
      private final int MODE_PRESS = 1;
      private final int MODE_TAPPED = 2;
      private int mManagedButton;
      private int mMode;

      public void buttonPressDelayed(int var1) {
         this.cancel();
         this.mMode = 1;
         this.mManagedButton = var1;
         NumberPicker.this.postDelayed(this, (long)ViewConfiguration.getTapTimeout());
      }

      public void buttonTapped(int var1) {
         this.cancel();
         this.mMode = 2;
         this.mManagedButton = var1;
         NumberPicker.this.post(this);
      }

      public void cancel() {
         this.mMode = 0;
         this.mManagedButton = 0;
         NumberPicker.this.removeCallbacks(this);
         NumberPicker var1;
         if (NumberPicker.this.mIncrementVirtualButtonPressed) {
            NumberPicker.this.mIncrementVirtualButtonPressed = false;
            var1 = NumberPicker.this;
            var1.invalidate(0, var1.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
         }

         NumberPicker.this.mDecrementVirtualButtonPressed = false;
         if (NumberPicker.this.mDecrementVirtualButtonPressed) {
            var1 = NumberPicker.this;
            var1.invalidate(0, 0, var1.getRight(), NumberPicker.this.mTopSelectionDividerTop);
         }

      }

      public void run() {
         int var1 = this.mMode;
         NumberPicker var2;
         if (var1 != 1) {
            if (var1 == 2) {
               var1 = this.mManagedButton;
               if (var1 != 1) {
                  if (var1 == 2) {
                     if (!NumberPicker.this.mDecrementVirtualButtonPressed) {
                        NumberPicker.this.postDelayed(this, (long)ViewConfiguration.getPressedStateDuration());
                     }

                     var2 = NumberPicker.this;
                     var2.mDecrementVirtualButtonPressed = var2.mDecrementVirtualButtonPressed ^ true;
                     var2 = NumberPicker.this;
                     var2.invalidate(0, 0, var2.getRight(), NumberPicker.this.mTopSelectionDividerTop);
                  }
               } else {
                  if (!NumberPicker.this.mIncrementVirtualButtonPressed) {
                     NumberPicker.this.postDelayed(this, (long)ViewConfiguration.getPressedStateDuration());
                  }

                  var2 = NumberPicker.this;
                  var2.mIncrementVirtualButtonPressed = var2.mIncrementVirtualButtonPressed ^ true;
                  var2 = NumberPicker.this;
                  var2.invalidate(0, var2.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
               }
            }
         } else {
            var1 = this.mManagedButton;
            if (var1 != 1) {
               if (var1 == 2) {
                  NumberPicker.this.mDecrementVirtualButtonPressed = true;
                  var2 = NumberPicker.this;
                  var2.invalidate(0, 0, var2.getRight(), NumberPicker.this.mTopSelectionDividerTop);
               }
            } else {
               NumberPicker.this.mIncrementVirtualButtonPressed = true;
               var2 = NumberPicker.this;
               var2.invalidate(0, var2.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
            }
         }

      }
   }
}
