package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
   public static final int HORIZONTAL = 0;
   private static final int INDEX_BOTTOM = 2;
   private static final int INDEX_CENTER_VERTICAL = 0;
   private static final int INDEX_FILL = 3;
   private static final int INDEX_TOP = 1;
   public static final int SHOW_DIVIDER_BEGINNING = 1;
   public static final int SHOW_DIVIDER_END = 4;
   public static final int SHOW_DIVIDER_MIDDLE = 2;
   public static final int SHOW_DIVIDER_NONE = 0;
   public static final int VERTICAL = 1;
   private static final int VERTICAL_GRAVITY_COUNT = 4;
   private boolean mBaselineAligned;
   private int mBaselineAlignedChildIndex;
   private int mBaselineChildTop;
   private Drawable mDivider;
   private int mDividerHeight;
   private int mDividerPadding;
   private int mDividerWidth;
   private int mGravity;
   private int[] mMaxAscent;
   private int[] mMaxDescent;
   private int mOrientation;
   private int mShowDividers;
   private int mTotalLength;
   private boolean mUseLargestChild;
   private float mWeightSum;

   public LinearLayoutCompat(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public LinearLayoutCompat(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public LinearLayoutCompat(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mBaselineAligned = true;
      this.mBaselineAlignedChildIndex = -1;
      this.mBaselineChildTop = 0;
      this.mGravity = 8388659;
      TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var1, var2, R.styleable.LinearLayoutCompat, var3, 0);
      var3 = var5.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
      if (var3 >= 0) {
         this.setOrientation(var3);
      }

      var3 = var5.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
      if (var3 >= 0) {
         this.setGravity(var3);
      }

      boolean var4 = var5.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
      if (!var4) {
         this.setBaselineAligned(var4);
      }

      this.mWeightSum = var5.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
      this.mBaselineAlignedChildIndex = var5.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
      this.mUseLargestChild = var5.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
      this.setDividerDrawable(var5.getDrawable(R.styleable.LinearLayoutCompat_divider));
      this.mShowDividers = var5.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
      this.mDividerPadding = var5.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
      var5.recycle();
   }

   private void forceUniformHeight(int var1, int var2) {
      int var3 = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824);

      for(int var4 = 0; var4 < var1; ++var4) {
         View var5 = this.getVirtualChildAt(var4);
         if (var5.getVisibility() != 8) {
            LinearLayoutCompat.LayoutParams var6 = (LinearLayoutCompat.LayoutParams)var5.getLayoutParams();
            if (var6.height == -1) {
               int var7 = var6.width;
               var6.width = var5.getMeasuredWidth();
               this.measureChildWithMargins(var5, var2, 0, var3, 0);
               var6.width = var7;
            }
         }
      }

   }

   private void forceUniformWidth(int var1, int var2) {
      int var3 = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);

      for(int var4 = 0; var4 < var1; ++var4) {
         View var5 = this.getVirtualChildAt(var4);
         if (var5.getVisibility() != 8) {
            LinearLayoutCompat.LayoutParams var6 = (LinearLayoutCompat.LayoutParams)var5.getLayoutParams();
            if (var6.width == -1) {
               int var7 = var6.height;
               var6.height = var5.getMeasuredHeight();
               this.measureChildWithMargins(var5, var3, 0, var2, 0);
               var6.height = var7;
            }
         }
      }

   }

   private void setChildFrame(View var1, int var2, int var3, int var4, int var5) {
      var1.layout(var2, var3, var4 + var2, var5 + var3);
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof LinearLayoutCompat.LayoutParams;
   }

   void drawDividersHorizontal(Canvas var1) {
      int var2 = this.getVirtualChildCount();
      boolean var3 = ViewUtils.isLayoutRtl(this);

      int var4;
      View var5;
      LinearLayoutCompat.LayoutParams var6;
      for(var4 = 0; var4 < var2; ++var4) {
         var5 = this.getVirtualChildAt(var4);
         if (var5 != null && var5.getVisibility() != 8 && this.hasDividerBeforeChildAt(var4)) {
            var6 = (LinearLayoutCompat.LayoutParams)var5.getLayoutParams();
            int var7;
            if (var3) {
               var7 = var5.getRight() + var6.rightMargin;
            } else {
               var7 = var5.getLeft() - var6.leftMargin - this.mDividerWidth;
            }

            this.drawVerticalDivider(var1, var7);
         }
      }

      if (this.hasDividerBeforeChildAt(var2)) {
         var5 = this.getVirtualChildAt(var2 - 1);
         if (var5 == null) {
            if (var3) {
               var4 = this.getPaddingLeft();
            } else {
               var4 = this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
            }
         } else {
            var6 = (LinearLayoutCompat.LayoutParams)var5.getLayoutParams();
            if (var3) {
               var4 = var5.getLeft() - var6.leftMargin - this.mDividerWidth;
            } else {
               var4 = var5.getRight() + var6.rightMargin;
            }
         }

         this.drawVerticalDivider(var1, var4);
      }

   }

   void drawDividersVertical(Canvas var1) {
      int var2 = this.getVirtualChildCount();

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         View var4 = this.getVirtualChildAt(var3);
         if (var4 != null && var4.getVisibility() != 8 && this.hasDividerBeforeChildAt(var3)) {
            LinearLayoutCompat.LayoutParams var5 = (LinearLayoutCompat.LayoutParams)var4.getLayoutParams();
            this.drawHorizontalDivider(var1, var4.getTop() - var5.topMargin - this.mDividerHeight);
         }
      }

      if (this.hasDividerBeforeChildAt(var2)) {
         View var7 = this.getVirtualChildAt(var2 - 1);
         if (var7 == null) {
            var3 = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
         } else {
            LinearLayoutCompat.LayoutParams var6 = (LinearLayoutCompat.LayoutParams)var7.getLayoutParams();
            var3 = var7.getBottom() + var6.bottomMargin;
         }

         this.drawHorizontalDivider(var1, var3);
      }

   }

   void drawHorizontalDivider(Canvas var1, int var2) {
      this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, var2, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + var2);
      this.mDivider.draw(var1);
   }

   void drawVerticalDivider(Canvas var1, int var2) {
      this.mDivider.setBounds(var2, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + var2, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
      this.mDivider.draw(var1);
   }

   protected LinearLayoutCompat.LayoutParams generateDefaultLayoutParams() {
      if (this.mOrientation == 0) {
         return new LinearLayoutCompat.LayoutParams(-2, -2);
      } else {
         return this.mOrientation == 1 ? new LinearLayoutCompat.LayoutParams(-1, -2) : null;
      }
   }

   public LinearLayoutCompat.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new LinearLayoutCompat.LayoutParams(this.getContext(), var1);
   }

   protected LinearLayoutCompat.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return new LinearLayoutCompat.LayoutParams(var1);
   }

   public int getBaseline() {
      if (this.mBaselineAlignedChildIndex < 0) {
         return super.getBaseline();
      } else if (this.getChildCount() <= this.mBaselineAlignedChildIndex) {
         throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
      } else {
         View var1 = this.getChildAt(this.mBaselineAlignedChildIndex);
         int var2 = var1.getBaseline();
         if (var2 == -1) {
            if (this.mBaselineAlignedChildIndex == 0) {
               return -1;
            } else {
               throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
         } else {
            int var3 = this.mBaselineChildTop;
            int var4 = var3;
            if (this.mOrientation == 1) {
               int var5 = this.mGravity & 112;
               var4 = var3;
               if (var5 != 48) {
                  if (var5 != 16) {
                     if (var5 != 80) {
                        var4 = var3;
                     } else {
                        var4 = this.getBottom() - this.getTop() - this.getPaddingBottom() - this.mTotalLength;
                     }
                  } else {
                     var4 = var3 + (this.getBottom() - this.getTop() - this.getPaddingTop() - this.getPaddingBottom() - this.mTotalLength) / 2;
                  }
               }
            }

            return var4 + ((LinearLayoutCompat.LayoutParams)var1.getLayoutParams()).topMargin + var2;
         }
      }
   }

   public int getBaselineAlignedChildIndex() {
      return this.mBaselineAlignedChildIndex;
   }

   int getChildrenSkipCount(View var1, int var2) {
      return 0;
   }

   public Drawable getDividerDrawable() {
      return this.mDivider;
   }

   public int getDividerPadding() {
      return this.mDividerPadding;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getDividerWidth() {
      return this.mDividerWidth;
   }

   public int getGravity() {
      return this.mGravity;
   }

   int getLocationOffset(View var1) {
      return 0;
   }

   int getNextLocationOffset(View var1) {
      return 0;
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public int getShowDividers() {
      return this.mShowDividers;
   }

   View getVirtualChildAt(int var1) {
      return this.getChildAt(var1);
   }

   int getVirtualChildCount() {
      return this.getChildCount();
   }

   public float getWeightSum() {
      return this.mWeightSum;
   }

   protected boolean hasDividerBeforeChildAt(int var1) {
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      if (var1 == 0) {
         if ((this.mShowDividers & 1) != 0) {
            var4 = true;
         }

         return var4;
      } else if (var1 == this.getChildCount()) {
         var4 = var2;
         if ((this.mShowDividers & 4) != 0) {
            var4 = true;
         }

         return var4;
      } else if ((this.mShowDividers & 2) == 0) {
         return false;
      } else {
         --var1;

         while(true) {
            var4 = var3;
            if (var1 < 0) {
               break;
            }

            if (this.getChildAt(var1).getVisibility() != 8) {
               var4 = true;
               break;
            }

            --var1;
         }

         return var4;
      }
   }

   public boolean isBaselineAligned() {
      return this.mBaselineAligned;
   }

   public boolean isMeasureWithLargestChildEnabled() {
      return this.mUseLargestChild;
   }

   void layoutHorizontal(int var1, int var2, int var3, int var4) {
      boolean var5 = ViewUtils.isLayoutRtl(this);
      int var6 = this.getPaddingTop();
      int var7 = var4 - var2;
      int var8 = this.getPaddingBottom();
      int var9 = this.getPaddingBottom();
      int var10 = this.getVirtualChildCount();
      var4 = this.mGravity;
      var2 = this.mGravity & 112;
      boolean var11 = this.mBaselineAligned;
      int[] var12 = this.mMaxAscent;
      int[] var13 = this.mMaxDescent;
      var4 = GravityCompat.getAbsoluteGravity(var4 & 8388615, ViewCompat.getLayoutDirection(this));
      boolean var14 = true;
      if (var4 != 1) {
         if (var4 != 5) {
            var1 = this.getPaddingLeft();
         } else {
            var1 = this.getPaddingLeft() + var3 - var1 - this.mTotalLength;
         }
      } else {
         var4 = this.getPaddingLeft();
         var1 = (var3 - var1 - this.mTotalLength) / 2 + var4;
      }

      int var15;
      byte var16;
      if (var5) {
         var15 = var10 - 1;
         var16 = -1;
      } else {
         var15 = 0;
         var16 = 1;
      }

      var4 = 0;

      for(var3 = var6; var4 < var10; ++var4) {
         int var17 = var15 + var16 * var4;
         View var18 = this.getVirtualChildAt(var17);
         if (var18 == null) {
            var1 += this.measureNullChild(var17);
         } else if (var18.getVisibility() == 8) {
            var14 = true;
         } else {
            int var19 = var18.getMeasuredWidth();
            int var20 = var18.getMeasuredHeight();
            LinearLayoutCompat.LayoutParams var21 = (LinearLayoutCompat.LayoutParams)var18.getLayoutParams();
            int var22;
            if (var11 && var21.height != -1) {
               var22 = var18.getBaseline();
            } else {
               var22 = -1;
            }

            int var23 = var21.gravity;
            int var24 = var23;
            if (var23 < 0) {
               var24 = var2;
            }

            var24 &= 112;
            if (var24 != 16) {
               if (var24 != 48) {
                  if (var24 != 80) {
                     var24 = var3;
                  } else {
                     var23 = var7 - var8 - var20 - var21.bottomMargin;
                     var24 = var23;
                     if (var22 != -1) {
                        var24 = var18.getMeasuredHeight();
                        var24 = var23 - (var13[2] - (var24 - var22));
                     }
                  }
               } else {
                  var24 = var21.topMargin + var3;
                  if (var22 != -1) {
                     var24 += var12[1] - var22;
                  }
               }
            } else {
               var24 = (var7 - var6 - var9 - var20) / 2 + var3 + var21.topMargin - var21.bottomMargin;
            }

            var14 = true;
            var22 = var1;
            if (this.hasDividerBeforeChildAt(var17)) {
               var22 = var1 + this.mDividerWidth;
            }

            var1 = var21.leftMargin + var22;
            this.setChildFrame(var18, var1 + this.getLocationOffset(var18), var24, var19, var20);
            var24 = var21.rightMargin;
            var22 = this.getNextLocationOffset(var18);
            var4 += this.getChildrenSkipCount(var18, var17);
            var1 += var19 + var24 + var22;
         }
      }

   }

   void layoutVertical(int var1, int var2, int var3, int var4) {
      int var5 = this.getPaddingLeft();
      int var6 = var3 - var1;
      int var7 = this.getPaddingRight();
      int var8 = this.getPaddingRight();
      int var9 = this.getVirtualChildCount();
      var1 = this.mGravity & 112;
      int var10 = this.mGravity;
      if (var1 != 16) {
         if (var1 != 80) {
            var1 = this.getPaddingTop();
         } else {
            var1 = this.getPaddingTop() + var4 - var2 - this.mTotalLength;
         }
      } else {
         var1 = this.getPaddingTop();
         var1 += (var4 - var2 - this.mTotalLength) / 2;
      }

      for(var2 = 0; var2 < var9; ++var2) {
         View var11 = this.getVirtualChildAt(var2);
         if (var11 == null) {
            var3 = var1 + this.measureNullChild(var2);
         } else {
            var3 = var1;
            if (var11.getVisibility() != 8) {
               int var12 = var11.getMeasuredWidth();
               int var13 = var11.getMeasuredHeight();
               LinearLayoutCompat.LayoutParams var14 = (LinearLayoutCompat.LayoutParams)var11.getLayoutParams();
               var4 = var14.gravity;
               var3 = var4;
               if (var4 < 0) {
                  var3 = var10 & 8388615;
               }

               var3 = GravityCompat.getAbsoluteGravity(var3, ViewCompat.getLayoutDirection(this)) & 7;
               if (var3 != 1) {
                  if (var3 != 5) {
                     var3 = var14.leftMargin + var5;
                  } else {
                     var3 = var6 - var7 - var12 - var14.rightMargin;
                  }
               } else {
                  var3 = (var6 - var5 - var8 - var12) / 2 + var5 + var14.leftMargin - var14.rightMargin;
               }

               var4 = var1;
               if (this.hasDividerBeforeChildAt(var2)) {
                  var4 = var1 + this.mDividerHeight;
               }

               var1 = var4 + var14.topMargin;
               this.setChildFrame(var11, var3, var1 + this.getLocationOffset(var11), var12, var13);
               var3 = var14.bottomMargin;
               var4 = this.getNextLocationOffset(var11);
               var2 += this.getChildrenSkipCount(var11, var2);
               var1 += var13 + var3 + var4;
               continue;
            }
         }

         var1 = var3;
      }

   }

   void measureChildBeforeLayout(View var1, int var2, int var3, int var4, int var5, int var6) {
      this.measureChildWithMargins(var1, var3, var4, var5, var6);
   }

   void measureHorizontal(int var1, int var2) {
      this.mTotalLength = 0;
      int var3 = this.getVirtualChildCount();
      int var4 = MeasureSpec.getMode(var1);
      int var5 = MeasureSpec.getMode(var2);
      if (this.mMaxAscent == null || this.mMaxDescent == null) {
         this.mMaxAscent = new int[4];
         this.mMaxDescent = new int[4];
      }

      int[] var6 = this.mMaxAscent;
      int[] var7 = this.mMaxDescent;
      var6[3] = -1;
      var6[2] = -1;
      var6[1] = -1;
      var6[0] = -1;
      var7[3] = -1;
      var7[2] = -1;
      var7[1] = -1;
      var7[0] = -1;
      boolean var8 = this.mBaselineAligned;
      boolean var9 = this.mUseLargestChild;
      int var10 = 1073741824;
      boolean var11;
      if (var4 == 1073741824) {
         var11 = true;
      } else {
         var11 = false;
      }

      int var12 = 0;
      int var13 = var12;
      int var14 = var12;
      int var15 = var12;
      int var16 = var12;
      int var17 = var12;
      int var18 = var12;
      boolean var19 = true;
      float var20 = 0.0F;

      int var21;
      int var23;
      int var28;
      for(var21 = Integer.MIN_VALUE; var12 < var3; var10 = var23) {
         label319: {
            View var22 = this.getVirtualChildAt(var12);
            if (var22 == null) {
               this.mTotalLength += this.measureNullChild(var12);
            } else {
               if (var22.getVisibility() != 8) {
                  if (this.hasDividerBeforeChildAt(var12)) {
                     this.mTotalLength += this.mDividerWidth;
                  }

                  LinearLayoutCompat.LayoutParams var24;
                  label356: {
                     var24 = (LinearLayoutCompat.LayoutParams)var22.getLayoutParams();
                     var20 += var24.weight;
                     if (var4 == var10 && var24.width == 0 && var24.weight > 0.0F) {
                        if (var11) {
                           this.mTotalLength += var24.leftMargin + var24.rightMargin;
                        } else {
                           var10 = this.mTotalLength;
                           this.mTotalLength = Math.max(var10, var24.leftMargin + var10 + var24.rightMargin);
                        }

                        if (!var8) {
                           var14 = 1;
                           break label356;
                        }

                        var10 = MeasureSpec.makeMeasureSpec(0, 0);
                        var22.measure(var10, var10);
                        var10 = var21;
                     } else {
                        if (var24.width == 0 && var24.weight > 0.0F) {
                           var24.width = -2;
                           var10 = 0;
                        } else {
                           var10 = Integer.MIN_VALUE;
                        }

                        if (var20 == 0.0F) {
                           var23 = this.mTotalLength;
                        } else {
                           var23 = 0;
                        }

                        this.measureChildBeforeLayout(var22, var12, var1, var23, var2, 0);
                        if (var10 != Integer.MIN_VALUE) {
                           var24.width = var10;
                        }

                        var23 = var22.getMeasuredWidth();
                        if (var11) {
                           this.mTotalLength += var24.leftMargin + var23 + var24.rightMargin + this.getNextLocationOffset(var22);
                        } else {
                           var10 = this.mTotalLength;
                           this.mTotalLength = Math.max(var10, var10 + var23 + var24.leftMargin + var24.rightMargin + this.getNextLocationOffset(var22));
                        }

                        var10 = var21;
                        if (var9) {
                           var10 = Math.max(var23, var21);
                        }
                     }

                     var21 = var10;
                  }

                  int var27 = var12;
                  var28 = 1073741824;
                  byte var33;
                  if (var5 != 1073741824 && var24.height == -1) {
                     var33 = 1;
                     var18 = var33;
                  } else {
                     var33 = 0;
                  }

                  var10 = var24.topMargin + var24.bottomMargin;
                  var23 = var22.getMeasuredHeight() + var10;
                  int var29 = View.combineMeasuredStates(var17, var22.getMeasuredState());
                  if (var8) {
                     int var30 = var22.getBaseline();
                     if (var30 != -1) {
                        if (var24.gravity < 0) {
                           var17 = this.mGravity;
                        } else {
                           var17 = var24.gravity;
                        }

                        var17 = ((var17 & 112) >> 4 & -2) >> 1;
                        var6[var17] = Math.max(var6[var17], var30);
                        var7[var17] = Math.max(var7[var17], var23 - var30);
                     }
                  }

                  var13 = Math.max(var13, var23);
                  if (var19 && var24.height == -1) {
                     var19 = true;
                  } else {
                     var19 = false;
                  }

                  if (var24.weight > 0.0F) {
                     if (var33 == 0) {
                        var10 = var23;
                     }

                     var12 = Math.max(var16, var10);
                  } else {
                     if (var33 != 0) {
                        var23 = var10;
                     }

                     var15 = Math.max(var15, var23);
                     var12 = var16;
                  }

                  var10 = this.getChildrenSkipCount(var22, var27) + var27;
                  var17 = var29;
                  var16 = var12;
                  var12 = var28;
                  break label319;
               }

               var12 += this.getChildrenSkipCount(var22, var12);
            }

            var23 = var12;
            var12 = var10;
            var10 = var23;
         }

         var23 = var12;
         var12 = var10 + 1;
      }

      if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(var3)) {
         this.mTotalLength += this.mDividerWidth;
      }

      label283: {
         if (var6[1] == -1 && var6[0] == -1 && var6[2] == -1) {
            var13 = var13;
            if (var6[3] == -1) {
               break label283;
            }
         }

         var13 = Math.max(var13, Math.max(var6[3], Math.max(var6[0], Math.max(var6[1], var6[2]))) + Math.max(var7[3], Math.max(var7[0], Math.max(var7[1], var7[2]))));
      }

      LinearLayoutCompat.LayoutParams var37;
      View var38;
      if (var9 && (var4 == Integer.MIN_VALUE || var4 == 0)) {
         this.mTotalLength = 0;

         for(var12 = 0; var12 < var3; ++var12) {
            var38 = this.getVirtualChildAt(var12);
            if (var38 == null) {
               this.mTotalLength += this.measureNullChild(var12);
            } else if (var38.getVisibility() == 8) {
               var12 += this.getChildrenSkipCount(var38, var12);
            } else {
               var37 = (LinearLayoutCompat.LayoutParams)var38.getLayoutParams();
               if (var11) {
                  this.mTotalLength += var37.leftMargin + var21 + var37.rightMargin + this.getNextLocationOffset(var38);
               } else {
                  var10 = this.mTotalLength;
                  this.mTotalLength = Math.max(var10, var10 + var21 + var37.leftMargin + var37.rightMargin + this.getNextLocationOffset(var38));
               }
            }
         }
      }

      var10 = var4;
      this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
      var28 = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumWidth()), var1, 0);
      var23 = (16777215 & var28) - this.mTotalLength;
      boolean var35;
      if (var14 != 0 || var23 != 0 && var20 > 0.0F) {
         if (this.mWeightSum > 0.0F) {
            var20 = this.mWeightSum;
         }

         var6[3] = -1;
         var6[2] = -1;
         var6[1] = -1;
         var6[0] = -1;
         var7[3] = -1;
         var7[2] = -1;
         var7[1] = -1;
         var7[0] = -1;
         this.mTotalLength = 0;
         var21 = var15;
         var14 = 0;
         var12 = -1;
         var35 = var19;
         var4 = var3;
         int var36 = var17;

         for(var16 = var23; var14 < var4; ++var14) {
            var38 = this.getVirtualChildAt(var14);
            if (var38 != null && var38.getVisibility() != 8) {
               var37 = (LinearLayoutCompat.LayoutParams)var38.getLayoutParams();
               float var31 = var37.weight;
               if (var31 > 0.0F) {
                  var17 = (int)((float)var16 * var31 / var20);
                  var20 -= var31;
                  var3 = this.getPaddingTop();
                  var23 = this.getPaddingBottom();
                  var13 = var16 - var17;
                  var3 = getChildMeasureSpec(var2, var3 + var23 + var37.topMargin + var37.bottomMargin, var37.height);
                  if (var37.width == 0 && var10 == 1073741824) {
                     if (var17 > 0) {
                        var16 = var17;
                     } else {
                        var16 = 0;
                     }

                     var38.measure(MeasureSpec.makeMeasureSpec(var16, 1073741824), var3);
                  } else {
                     var17 += var38.getMeasuredWidth();
                     var16 = var17;
                     if (var17 < 0) {
                        var16 = 0;
                     }

                     var38.measure(MeasureSpec.makeMeasureSpec(var16, 1073741824), var3);
                  }

                  var36 = View.combineMeasuredStates(var36, var38.getMeasuredState() & -16777216);
                  var16 = var13;
               }

               if (var11) {
                  this.mTotalLength += var38.getMeasuredWidth() + var37.leftMargin + var37.rightMargin + this.getNextLocationOffset(var38);
               } else {
                  var13 = this.mTotalLength;
                  this.mTotalLength = Math.max(var13, var38.getMeasuredWidth() + var13 + var37.leftMargin + var37.rightMargin + this.getNextLocationOffset(var38));
               }

               boolean var34;
               if (var5 != 1073741824 && var37.height == -1) {
                  var34 = true;
               } else {
                  var34 = false;
               }

               var23 = var37.topMargin + var37.bottomMargin;
               var3 = var38.getMeasuredHeight() + var23;
               var17 = Math.max(var12, var3);
               if (var34) {
                  var12 = var23;
               } else {
                  var12 = var3;
               }

               var12 = Math.max(var21, var12);
               if (var35 && var37.height == -1) {
                  var35 = true;
               } else {
                  var35 = false;
               }

               if (var8) {
                  var13 = var38.getBaseline();
                  if (var13 != -1) {
                     if (var37.gravity < 0) {
                        var21 = this.mGravity;
                     } else {
                        var21 = var37.gravity;
                     }

                     var21 = ((var21 & 112) >> 4 & -2) >> 1;
                     var6[var21] = Math.max(var6[var21], var13);
                     var7[var21] = Math.max(var7[var21], var3 - var13);
                  }
               }

               var21 = var12;
               var12 = var17;
            }
         }

         var16 = var4;
         this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
         if (var6[1] == -1 && var6[0] == -1 && var6[2] == -1 && var6[3] == -1) {
            var4 = var12;
            var17 = var36;
            var12 = var16;
         } else {
            var4 = Math.max(var12, Math.max(var6[3], Math.max(var6[0], Math.max(var6[1], var6[2]))) + Math.max(var7[3], Math.max(var7[0], Math.max(var7[1], var7[2]))));
            var12 = var16;
            var17 = var36;
         }
      } else {
         var15 = Math.max(var15, var16);
         if (var9 && var4 != 1073741824) {
            for(var4 = 0; var4 < var3; ++var4) {
               View var32 = this.getVirtualChildAt(var4);
               if (var32 != null && var32.getVisibility() != 8 && ((LinearLayoutCompat.LayoutParams)var32.getLayoutParams()).weight > 0.0F) {
                  var32.measure(MeasureSpec.makeMeasureSpec(var21, 1073741824), MeasureSpec.makeMeasureSpec(var32.getMeasuredHeight(), 1073741824));
               }
            }
         }

         var21 = var15;
         var12 = var3;
         var4 = var13;
         var35 = var19;
      }

      if (!var35 && var5 != 1073741824) {
         var4 = var21;
      }

      this.setMeasuredDimension(var28 | -16777216 & var17, View.resolveSizeAndState(Math.max(var4 + this.getPaddingTop() + this.getPaddingBottom(), this.getSuggestedMinimumHeight()), var2, var17 << 16));
      if (var18 != 0) {
         this.forceUniformHeight(var12, var1);
      }

   }

   int measureNullChild(int var1) {
      return 0;
   }

   void measureVertical(int var1, int var2) {
      int var3 = 0;
      this.mTotalLength = 0;
      int var4 = this.getVirtualChildCount();
      int var5 = MeasureSpec.getMode(var1);
      int var6 = MeasureSpec.getMode(var2);
      int var7 = this.mBaselineAlignedChildIndex;
      boolean var8 = this.mUseLargestChild;
      int var9 = 0;
      int var10 = var9;
      int var11 = var9;
      int var12 = var9;
      int var13 = var9;
      int var14 = var9;
      float var15 = 0.0F;
      boolean var16 = true;

      int var17;
      View var18;
      int var19;
      int var20;
      LinearLayoutCompat.LayoutParams var21;
      int var24;
      int var25;
      int var31;
      boolean var33;
      for(var17 = Integer.MIN_VALUE; var11 < var4; var16 = var33) {
         label243: {
            var18 = this.getVirtualChildAt(var11);
            if (var18 == null) {
               this.mTotalLength += this.measureNullChild(var11);
            } else {
               if (var18.getVisibility() != 8) {
                  if (this.hasDividerBeforeChildAt(var11)) {
                     this.mTotalLength += this.mDividerHeight;
                  }

                  var21 = (LinearLayoutCompat.LayoutParams)var18.getLayoutParams();
                  var15 += var21.weight;
                  if (var6 == 1073741824 && var21.height == 0 && var21.weight > 0.0F) {
                     var19 = this.mTotalLength;
                     var12 = var21.topMargin;
                     this.mTotalLength = Math.max(var19, var12 + var19 + var21.bottomMargin);
                     var12 = 1;
                  } else {
                     if (var21.height == 0 && var21.weight > 0.0F) {
                        var21.height = -2;
                        var19 = 0;
                     } else {
                        var19 = Integer.MIN_VALUE;
                     }

                     if (var15 == 0.0F) {
                        var20 = this.mTotalLength;
                     } else {
                        var20 = 0;
                     }

                     this.measureChildBeforeLayout(var18, var11, var1, 0, var2, var20);
                     if (var19 != Integer.MIN_VALUE) {
                        var21.height = var19;
                     }

                     var20 = var18.getMeasuredHeight();
                     var19 = this.mTotalLength;
                     this.mTotalLength = Math.max(var19, var19 + var20 + var21.topMargin + var21.bottomMargin + this.getNextLocationOffset(var18));
                     if (var8) {
                        var17 = Math.max(var20, var17);
                     }
                  }

                  var24 = var11;
                  if (var7 >= 0 && var7 == var11 + 1) {
                     this.mBaselineChildTop = this.mTotalLength;
                  }

                  if (var11 < var7 && var21.weight > 0.0F) {
                     throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                  }

                  byte var30;
                  if (var5 != 1073741824 && var21.width == -1) {
                     var30 = 1;
                     var13 = var30;
                  } else {
                     var30 = 0;
                  }

                  var19 = var21.leftMargin + var21.rightMargin;
                  var20 = var18.getMeasuredWidth() + var19;
                  var25 = Math.max(var9, var20);
                  var3 = View.combineMeasuredStates(var3, var18.getMeasuredState());
                  boolean var28;
                  if (var16 && var21.width == -1) {
                     var28 = true;
                  } else {
                     var28 = false;
                  }

                  if (var21.weight > 0.0F) {
                     if (var30 == 0) {
                        var19 = var20;
                     }

                     var31 = Math.max(var10, var19);
                  } else {
                     if (var30 != 0) {
                        var20 = var19;
                     }

                     var14 = Math.max(var14, var20);
                     var31 = var10;
                  }

                  var10 = this.getChildrenSkipCount(var18, var11);
                  var33 = var28;
                  var9 = var25;
                  var11 = var3;
                  var3 = var17;
                  var20 = var10 + var24;
                  var17 = var11;
                  var10 = var31;
                  break label243;
               }

               var11 += this.getChildrenSkipCount(var18, var11);
            }

            var19 = var3;
            var3 = var17;
            var17 = var19;
            var20 = var11;
            var33 = var16;
         }

         var11 = var20 + 1;
         var31 = var17;
         var17 = var3;
         var3 = var31;
      }

      var11 = var14;
      if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(var4)) {
         this.mTotalLength += this.mDividerHeight;
      }

      if (var8 && (var6 == Integer.MIN_VALUE || var6 == 0)) {
         this.mTotalLength = 0;

         for(var14 = 0; var14 < var4; ++var14) {
            var18 = this.getVirtualChildAt(var14);
            if (var18 == null) {
               this.mTotalLength += this.measureNullChild(var14);
            } else if (var18.getVisibility() == 8) {
               var14 += this.getChildrenSkipCount(var18, var14);
            } else {
               var21 = (LinearLayoutCompat.LayoutParams)var18.getLayoutParams();
               var19 = this.mTotalLength;
               this.mTotalLength = Math.max(var19, var19 + var17 + var21.topMargin + var21.bottomMargin + this.getNextLocationOffset(var18));
            }
         }
      }

      var19 = var6;
      this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
      var20 = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumHeight()), var2, 0);
      var14 = (16777215 & var20) - this.mTotalLength;
      if (var12 == 0 && (var14 == 0 || var15 <= 0.0F)) {
         var14 = Math.max(var11, var10);
         if (var8 && var6 != 1073741824) {
            for(var6 = 0; var6 < var4; ++var6) {
               var18 = this.getVirtualChildAt(var6);
               if (var18 != null && var18.getVisibility() != 8 && ((LinearLayoutCompat.LayoutParams)var18.getLayoutParams()).weight > 0.0F) {
                  var18.measure(MeasureSpec.makeMeasureSpec(var18.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(var17, 1073741824));
               }
            }
         }

         var6 = var14;
      } else {
         if (this.mWeightSum > 0.0F) {
            var15 = this.mWeightSum;
         }

         this.mTotalLength = 0;
         byte var29 = 0;
         var6 = var9;
         var9 = var3;
         var17 = var11;

         for(var3 = var29; var3 < var4; ++var3) {
            View var34 = this.getVirtualChildAt(var3);
            if (var34.getVisibility() != 8) {
               LinearLayoutCompat.LayoutParams var32 = (LinearLayoutCompat.LayoutParams)var34.getLayoutParams();
               float var26 = var32.weight;
               if (var26 > 0.0F) {
                  var10 = (int)((float)var14 * var26 / var15);
                  var12 = this.getPaddingLeft();
                  var24 = this.getPaddingRight();
                  var11 = var14 - var10;
                  var25 = var32.leftMargin;
                  var14 = var32.rightMargin;
                  var7 = var32.width;
                  var15 -= var26;
                  var12 = getChildMeasureSpec(var1, var12 + var24 + var25 + var14, var7);
                  if (var32.height == 0 && var19 == 1073741824) {
                     if (var10 > 0) {
                        var14 = var10;
                     } else {
                        var14 = 0;
                     }

                     var34.measure(var12, MeasureSpec.makeMeasureSpec(var14, 1073741824));
                  } else {
                     var10 += var34.getMeasuredHeight();
                     var14 = var10;
                     if (var10 < 0) {
                        var14 = 0;
                     }

                     var34.measure(var12, MeasureSpec.makeMeasureSpec(var14, 1073741824));
                  }

                  var9 = View.combineMeasuredStates(var9, var34.getMeasuredState() & -256);
                  var14 = var11;
               }

               var12 = var32.leftMargin + var32.rightMargin;
               var11 = var34.getMeasuredWidth() + var12;
               var10 = Math.max(var6, var11);
               boolean var27;
               if (var5 != 1073741824 && var32.width == -1) {
                  var27 = true;
               } else {
                  var27 = false;
               }

               if (var27) {
                  var11 = var12;
               }

               var17 = Math.max(var17, var11);
               if (var16 && var32.width == -1) {
                  var27 = true;
               } else {
                  var27 = false;
               }

               var31 = this.mTotalLength;
               this.mTotalLength = Math.max(var31, var31 + var34.getMeasuredHeight() + var32.topMargin + var32.bottomMargin + this.getNextLocationOffset(var34));
               var16 = var27;
               var6 = var10;
            }
         }

         this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
         var3 = var9;
         var9 = var6;
         var6 = var17;
      }

      var17 = var9;
      if (!var16) {
         var17 = var9;
         if (var5 != 1073741824) {
            var17 = var6;
         }
      }

      this.setMeasuredDimension(View.resolveSizeAndState(Math.max(var17 + this.getPaddingLeft() + this.getPaddingRight(), this.getSuggestedMinimumWidth()), var1, var3), var20);
      if (var13 != 0) {
         this.forceUniformWidth(var4, var2);
      }

   }

   protected void onDraw(Canvas var1) {
      if (this.mDivider != null) {
         if (this.mOrientation == 1) {
            this.drawDividersVertical(var1);
         } else {
            this.drawDividersHorizontal(var1);
         }

      }
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      if (VERSION.SDK_INT >= 14) {
         super.onInitializeAccessibilityEvent(var1);
         var1.setClassName(LinearLayoutCompat.class.getName());
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      if (VERSION.SDK_INT >= 14) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setClassName(LinearLayoutCompat.class.getName());
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (this.mOrientation == 1) {
         this.layoutVertical(var2, var3, var4, var5);
      } else {
         this.layoutHorizontal(var2, var3, var4, var5);
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (this.mOrientation == 1) {
         this.measureVertical(var1, var2);
      } else {
         this.measureHorizontal(var1, var2);
      }

   }

   public void setBaselineAligned(boolean var1) {
      this.mBaselineAligned = var1;
   }

   public void setBaselineAlignedChildIndex(int var1) {
      if (var1 >= 0 && var1 < this.getChildCount()) {
         this.mBaselineAlignedChildIndex = var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("base aligned child index out of range (0, ");
         var2.append(this.getChildCount());
         var2.append(")");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public void setDividerDrawable(Drawable var1) {
      if (var1 != this.mDivider) {
         this.mDivider = var1;
         boolean var2 = false;
         if (var1 != null) {
            this.mDividerWidth = var1.getIntrinsicWidth();
            this.mDividerHeight = var1.getIntrinsicHeight();
         } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
         }

         if (var1 == null) {
            var2 = true;
         }

         this.setWillNotDraw(var2);
         this.requestLayout();
      }
   }

   public void setDividerPadding(int var1) {
      this.mDividerPadding = var1;
   }

   public void setGravity(int var1) {
      if (this.mGravity != var1) {
         int var2 = var1;
         if ((8388615 & var1) == 0) {
            var2 = var1 | 8388611;
         }

         var1 = var2;
         if ((var2 & 112) == 0) {
            var1 = var2 | 48;
         }

         this.mGravity = var1;
         this.requestLayout();
      }

   }

   public void setHorizontalGravity(int var1) {
      var1 &= 8388615;
      if ((8388615 & this.mGravity) != var1) {
         this.mGravity = var1 | this.mGravity & -8388616;
         this.requestLayout();
      }

   }

   public void setMeasureWithLargestChildEnabled(boolean var1) {
      this.mUseLargestChild = var1;
   }

   public void setOrientation(int var1) {
      if (this.mOrientation != var1) {
         this.mOrientation = var1;
         this.requestLayout();
      }

   }

   public void setShowDividers(int var1) {
      if (var1 != this.mShowDividers) {
         this.requestLayout();
      }

      this.mShowDividers = var1;
   }

   public void setVerticalGravity(int var1) {
      var1 &= 112;
      if ((this.mGravity & 112) != var1) {
         this.mGravity = var1 | this.mGravity & -113;
         this.requestLayout();
      }

   }

   public void setWeightSum(float var1) {
      this.mWeightSum = Math.max(0.0F, var1);
   }

   public boolean shouldDelayChildPressedState() {
      return false;
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface DividerMode {
   }

   public static class LayoutParams extends MarginLayoutParams {
      public int gravity = -1;
      public float weight;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.weight = 0.0F;
      }

      public LayoutParams(int var1, int var2, float var3) {
         super(var1, var2);
         this.weight = var3;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.LinearLayoutCompat_Layout);
         this.weight = var3.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0F);
         this.gravity = var3.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
         var3.recycle();
      }

      public LayoutParams(LinearLayoutCompat.LayoutParams var1) {
         super(var1);
         this.weight = var1.weight;
         this.gravity = var1.gravity;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface OrientationMode {
   }
}
