package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.List;

public class Legend extends ComponentBase {
   private List mCalculatedLabelBreakPoints;
   private List mCalculatedLabelSizes;
   private List mCalculatedLineSizes;
   private Legend.LegendDirection mDirection;
   private boolean mDrawInside;
   private LegendEntry[] mEntries;
   private LegendEntry[] mExtraEntries;
   private DashPathEffect mFormLineDashEffect;
   private float mFormLineWidth;
   private float mFormSize;
   private float mFormToTextSpace;
   private Legend.LegendHorizontalAlignment mHorizontalAlignment;
   private boolean mIsLegendCustom;
   private float mMaxSizePercent;
   public float mNeededHeight;
   public float mNeededWidth;
   private Legend.LegendOrientation mOrientation;
   private Legend.LegendForm mShape;
   private float mStackSpace;
   public float mTextHeightMax;
   public float mTextWidthMax;
   private Legend.LegendVerticalAlignment mVerticalAlignment;
   private boolean mWordWrapEnabled;
   private float mXEntrySpace;
   private float mYEntrySpace;

   public Legend() {
      this.mEntries = new LegendEntry[0];
      this.mIsLegendCustom = false;
      this.mHorizontalAlignment = Legend.LegendHorizontalAlignment.LEFT;
      this.mVerticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
      this.mOrientation = Legend.LegendOrientation.HORIZONTAL;
      this.mDrawInside = false;
      this.mDirection = Legend.LegendDirection.LEFT_TO_RIGHT;
      this.mShape = Legend.LegendForm.SQUARE;
      this.mFormSize = 8.0F;
      this.mFormLineWidth = 3.0F;
      this.mFormLineDashEffect = null;
      this.mXEntrySpace = 6.0F;
      this.mYEntrySpace = 0.0F;
      this.mFormToTextSpace = 5.0F;
      this.mStackSpace = 3.0F;
      this.mMaxSizePercent = 0.95F;
      this.mNeededWidth = 0.0F;
      this.mNeededHeight = 0.0F;
      this.mTextHeightMax = 0.0F;
      this.mTextWidthMax = 0.0F;
      this.mWordWrapEnabled = false;
      this.mCalculatedLabelSizes = new ArrayList(16);
      this.mCalculatedLabelBreakPoints = new ArrayList(16);
      this.mCalculatedLineSizes = new ArrayList(16);
      this.mTextSize = Utils.convertDpToPixel(10.0F);
      this.mXOffset = Utils.convertDpToPixel(5.0F);
      this.mYOffset = Utils.convertDpToPixel(3.0F);
   }

   @Deprecated
   public Legend(List var1, List var2) {
      this(Utils.convertIntegers(var1), Utils.convertStrings(var2));
   }

   @Deprecated
   public Legend(int[] var1, String[] var2) {
      this();
      if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
         } else {
            ArrayList var3 = new ArrayList();

            for(int var4 = 0; var4 < Math.min(var1.length, var2.length); ++var4) {
               LegendEntry var5 = new LegendEntry();
               var5.formColor = var1[var4];
               var5.label = var2[var4];
               if (var5.formColor == 1122868) {
                  var5.form = Legend.LegendForm.NONE;
               } else if (var5.formColor == 1122867 || var5.formColor == 0) {
                  var5.form = Legend.LegendForm.EMPTY;
               }

               var3.add(var5);
            }

            this.mEntries = (LegendEntry[])var3.toArray(new LegendEntry[var3.size()]);
         }
      } else {
         throw new IllegalArgumentException("colors array or labels array is NULL");
      }
   }

   public Legend(LegendEntry[] var1) {
      this();
      if (var1 == null) {
         throw new IllegalArgumentException("entries array is NULL");
      } else {
         this.mEntries = var1;
      }
   }

   public void calculateDimensions(Paint var1, ViewPortHandler var2) {
      float var3 = Utils.convertDpToPixel(this.mFormSize);
      float var4 = Utils.convertDpToPixel(this.mStackSpace);
      float var5 = Utils.convertDpToPixel(this.mFormToTextSpace);
      float var6 = Utils.convertDpToPixel(this.mXEntrySpace);
      float var7 = Utils.convertDpToPixel(this.mYEntrySpace);
      boolean var8 = this.mWordWrapEnabled;
      LegendEntry[] var9 = this.mEntries;
      int var10 = var9.length;
      this.mTextWidthMax = this.getMaximumEntryWidth(var1);
      this.mTextHeightMax = this.getMaximumEntryHeight(var1);
      float var11;
      float var12;
      float var17;
      float var18;
      float var19;
      float var21;
      int var30;
      switch(this.mOrientation) {
      case VERTICAL:
         var11 = Utils.getLineHeight(var1);
         var30 = 0;
         var21 = 0.0F;
         var6 = 0.0F;
         boolean var28 = false;

         for(var17 = 0.0F; var30 < var10; var17 = var18) {
            LegendEntry var24 = var9[var30];
            boolean var29;
            if (var24.form != Legend.LegendForm.NONE) {
               var29 = true;
            } else {
               var29 = false;
            }

            if (Float.isNaN(var24.formSize)) {
               var19 = var3;
            } else {
               var19 = Utils.convertDpToPixel(var24.formSize);
            }

            String var25 = var24.label;
            if (!var28) {
               var17 = 0.0F;
            }

            var12 = var17;
            if (var29) {
               var18 = var17;
               if (var28) {
                  var18 = var17 + var4;
               }

               var12 = var18 + var19;
            }

            if (var25 == null) {
               var17 = var12 + var19;
               var18 = var17;
               if (var30 < var10 - 1) {
                  var18 = var17 + var4;
               }

               var28 = true;
            } else {
               if (var29 && !var28) {
                  var18 = var12 + var5;
                  var17 = var21;
                  var19 = var6;
                  var29 = var28;
               } else {
                  var17 = var21;
                  var19 = var6;
                  var29 = var28;
                  var18 = var12;
                  if (var28) {
                     var17 = Math.max(var21, var12);
                     var19 = var6 + var11 + var7;
                     var29 = false;
                     var18 = 0.0F;
                  }
               }

               var12 = var18 + (float)Utils.calcTextWidth(var1, var25);
               var21 = var17;
               var6 = var19;
               var28 = var29;
               var18 = var12;
               if (var30 < var10 - 1) {
                  var6 = var19 + var11 + var7;
                  var21 = var17;
                  var28 = var29;
                  var18 = var12;
               }
            }

            var21 = Math.max(var21, var18);
            ++var30;
         }

         this.mNeededWidth = var21;
         this.mNeededHeight = var6;
         break;
      case HORIZONTAL:
         var11 = Utils.getLineHeight(var1);
         var12 = Utils.getLineSpacing(var1) + var7;
         float var13 = var2.contentWidth();
         float var14 = this.mMaxSizePercent;
         this.mCalculatedLabelBreakPoints.clear();
         this.mCalculatedLabelSizes.clear();
         this.mCalculatedLineSizes.clear();
         int var15 = 0;
         int var16 = -1;
         var17 = 0.0F;
         var7 = 0.0F;
         var18 = 0.0F;
         LegendEntry[] var23 = var9;

         for(var19 = var3; var15 < var10; var7 = var21) {
            LegendEntry var26 = var23[var15];
            boolean var20;
            if (var26.form != Legend.LegendForm.NONE) {
               var20 = true;
            } else {
               var20 = false;
            }

            if (Float.isNaN(var26.formSize)) {
               var21 = var19;
            } else {
               var21 = Utils.convertDpToPixel(var26.formSize);
            }

            String var27 = var26.label;
            this.mCalculatedLabelBreakPoints.add(false);
            if (var16 == -1) {
               var3 = 0.0F;
            } else {
               var3 = var7 + var4;
            }

            List var22;
            if (var27 != null) {
               this.mCalculatedLabelSizes.add(Utils.calcTextSize(var1, var27));
               if (var20) {
                  var21 += var5;
               } else {
                  var21 = 0.0F;
               }

               var21 = var3 + var21 + ((FSize)this.mCalculatedLabelSizes.get(var15)).width;
            } else {
               var22 = this.mCalculatedLabelSizes;
               var22.add(FSize.getInstance(0.0F, 0.0F));
               if (!var20) {
                  var21 = 0.0F;
               }

               var3 += var21;
               var21 = var3;
               if (var16 == -1) {
                  var21 = var3;
                  var16 = var15;
               }
            }

            if (var27 != null || var15 == var10 - 1) {
               if (var18 == 0.0F) {
                  var3 = 0.0F;
               } else {
                  var3 = var6;
               }

               if (var8 && var18 != 0.0F && var13 * var14 - var18 < var3 + var21) {
                  this.mCalculatedLineSizes.add(FSize.getInstance(var18, var11));
                  var18 = Math.max(var17, var18);
                  var22 = this.mCalculatedLabelBreakPoints;
                  if (var16 > -1) {
                     var30 = var16;
                  } else {
                     var30 = var15;
                  }

                  var22.set(var30, true);
                  var17 = var21;
               } else {
                  var3 = var18 + var3 + var21;
                  var18 = var17;
                  var17 = var3;
               }

               if (var15 == var10 - 1) {
                  this.mCalculatedLineSizes.add(FSize.getInstance(var17, var11));
                  var3 = Math.max(var18, var17);
                  var18 = var17;
                  var17 = var3;
               } else {
                  var3 = var17;
                  var17 = var18;
                  var18 = var3;
               }
            }

            if (var27 != null) {
               var16 = -1;
            }

            ++var15;
         }

         this.mNeededWidth = var17;
         var21 = (float)this.mCalculatedLineSizes.size();
         if (this.mCalculatedLineSizes.size() == 0) {
            var15 = 0;
         } else {
            var15 = this.mCalculatedLineSizes.size() - 1;
         }

         this.mNeededHeight = var11 * var21 + var12 * (float)var15;
      }

      this.mNeededHeight += this.mYOffset;
      this.mNeededWidth += this.mXOffset;
   }

   public List getCalculatedLabelBreakPoints() {
      return this.mCalculatedLabelBreakPoints;
   }

   public List getCalculatedLabelSizes() {
      return this.mCalculatedLabelSizes;
   }

   public List getCalculatedLineSizes() {
      return this.mCalculatedLineSizes;
   }

   @Deprecated
   public int[] getColors() {
      LegendEntry[] var1 = this.mEntries;
      int var2 = 0;

      int[] var4;
      for(var4 = new int[var1.length]; var2 < this.mEntries.length; ++var2) {
         int var3;
         if (this.mEntries[var2].form == Legend.LegendForm.NONE) {
            var3 = 1122868;
         } else if (this.mEntries[var2].form == Legend.LegendForm.EMPTY) {
            var3 = 1122867;
         } else {
            var3 = this.mEntries[var2].formColor;
         }

         var4[var2] = var3;
      }

      return var4;
   }

   public Legend.LegendDirection getDirection() {
      return this.mDirection;
   }

   public LegendEntry[] getEntries() {
      return this.mEntries;
   }

   @Deprecated
   public int[] getExtraColors() {
      LegendEntry[] var1 = this.mExtraEntries;
      int var2 = 0;

      int[] var4;
      for(var4 = new int[var1.length]; var2 < this.mExtraEntries.length; ++var2) {
         int var3;
         if (this.mExtraEntries[var2].form == Legend.LegendForm.NONE) {
            var3 = 1122868;
         } else if (this.mExtraEntries[var2].form == Legend.LegendForm.EMPTY) {
            var3 = 1122867;
         } else {
            var3 = this.mExtraEntries[var2].formColor;
         }

         var4[var2] = var3;
      }

      return var4;
   }

   public LegendEntry[] getExtraEntries() {
      return this.mExtraEntries;
   }

   @Deprecated
   public String[] getExtraLabels() {
      LegendEntry[] var1 = this.mExtraEntries;
      int var2 = 0;

      String[] var3;
      for(var3 = new String[var1.length]; var2 < this.mExtraEntries.length; ++var2) {
         var3[var2] = this.mExtraEntries[var2].label;
      }

      return var3;
   }

   public Legend.LegendForm getForm() {
      return this.mShape;
   }

   public DashPathEffect getFormLineDashEffect() {
      return this.mFormLineDashEffect;
   }

   public float getFormLineWidth() {
      return this.mFormLineWidth;
   }

   public float getFormSize() {
      return this.mFormSize;
   }

   public float getFormToTextSpace() {
      return this.mFormToTextSpace;
   }

   public Legend.LegendHorizontalAlignment getHorizontalAlignment() {
      return this.mHorizontalAlignment;
   }

   @Deprecated
   public String[] getLabels() {
      LegendEntry[] var1 = this.mEntries;
      int var2 = 0;

      String[] var3;
      for(var3 = new String[var1.length]; var2 < this.mEntries.length; ++var2) {
         var3[var2] = this.mEntries[var2].label;
      }

      return var3;
   }

   public float getMaxSizePercent() {
      return this.mMaxSizePercent;
   }

   public float getMaximumEntryHeight(Paint var1) {
      LegendEntry[] var2 = this.mEntries;
      float var3 = 0.0F;
      int var4 = 0;

      float var7;
      for(int var5 = var2.length; var4 < var5; var3 = var7) {
         String var6 = var2[var4].label;
         if (var6 == null) {
            var7 = var3;
         } else {
            float var8 = (float)Utils.calcTextHeight(var1, var6);
            var7 = var3;
            if (var8 > var3) {
               var7 = var8;
            }
         }

         ++var4;
      }

      return var3;
   }

   public float getMaximumEntryWidth(Paint var1) {
      float var2 = Utils.convertDpToPixel(this.mFormToTextSpace);
      LegendEntry[] var3 = this.mEntries;
      float var4 = 0.0F;
      int var5 = 0;
      int var6 = var3.length;

      float var7;
      float var9;
      for(var7 = 0.0F; var5 < var6; var7 = var9) {
         LegendEntry var8 = var3[var5];
         if (Float.isNaN(var8.formSize)) {
            var9 = this.mFormSize;
         } else {
            var9 = var8.formSize;
         }

         float var10 = Utils.convertDpToPixel(var9);
         var9 = var7;
         if (var10 > var7) {
            var9 = var10;
         }

         String var11 = var8.label;
         if (var11 == null) {
            var7 = var4;
         } else {
            var10 = (float)Utils.calcTextWidth(var1, var11);
            var7 = var4;
            if (var10 > var4) {
               var7 = var10;
            }
         }

         ++var5;
         var4 = var7;
      }

      return var4 + var7 + var2;
   }

   public Legend.LegendOrientation getOrientation() {
      return this.mOrientation;
   }

   @Deprecated
   public Legend.LegendPosition getPosition() {
      if (this.mOrientation == Legend.LegendOrientation.VERTICAL && this.mHorizontalAlignment == Legend.LegendHorizontalAlignment.CENTER && this.mVerticalAlignment == Legend.LegendVerticalAlignment.CENTER) {
         return Legend.LegendPosition.PIECHART_CENTER;
      } else {
         Legend.LegendPosition var1;
         if (this.mOrientation == Legend.LegendOrientation.HORIZONTAL) {
            if (this.mVerticalAlignment == Legend.LegendVerticalAlignment.TOP) {
               if (this.mHorizontalAlignment == Legend.LegendHorizontalAlignment.LEFT) {
                  var1 = Legend.LegendPosition.ABOVE_CHART_LEFT;
               } else if (this.mHorizontalAlignment == Legend.LegendHorizontalAlignment.RIGHT) {
                  var1 = Legend.LegendPosition.ABOVE_CHART_RIGHT;
               } else {
                  var1 = Legend.LegendPosition.ABOVE_CHART_CENTER;
               }

               return var1;
            } else {
               if (this.mHorizontalAlignment == Legend.LegendHorizontalAlignment.LEFT) {
                  var1 = Legend.LegendPosition.BELOW_CHART_LEFT;
               } else if (this.mHorizontalAlignment == Legend.LegendHorizontalAlignment.RIGHT) {
                  var1 = Legend.LegendPosition.BELOW_CHART_RIGHT;
               } else {
                  var1 = Legend.LegendPosition.BELOW_CHART_CENTER;
               }

               return var1;
            }
         } else if (this.mHorizontalAlignment == Legend.LegendHorizontalAlignment.LEFT) {
            if (this.mVerticalAlignment == Legend.LegendVerticalAlignment.TOP && this.mDrawInside) {
               var1 = Legend.LegendPosition.LEFT_OF_CHART_INSIDE;
            } else if (this.mVerticalAlignment == Legend.LegendVerticalAlignment.CENTER) {
               var1 = Legend.LegendPosition.LEFT_OF_CHART_CENTER;
            } else {
               var1 = Legend.LegendPosition.LEFT_OF_CHART;
            }

            return var1;
         } else {
            if (this.mVerticalAlignment == Legend.LegendVerticalAlignment.TOP && this.mDrawInside) {
               var1 = Legend.LegendPosition.RIGHT_OF_CHART_INSIDE;
            } else if (this.mVerticalAlignment == Legend.LegendVerticalAlignment.CENTER) {
               var1 = Legend.LegendPosition.RIGHT_OF_CHART_CENTER;
            } else {
               var1 = Legend.LegendPosition.RIGHT_OF_CHART;
            }

            return var1;
         }
      }
   }

   public float getStackSpace() {
      return this.mStackSpace;
   }

   public Legend.LegendVerticalAlignment getVerticalAlignment() {
      return this.mVerticalAlignment;
   }

   public float getXEntrySpace() {
      return this.mXEntrySpace;
   }

   public float getYEntrySpace() {
      return this.mYEntrySpace;
   }

   public boolean isDrawInsideEnabled() {
      return this.mDrawInside;
   }

   public boolean isLegendCustom() {
      return this.mIsLegendCustom;
   }

   public boolean isWordWrapEnabled() {
      return this.mWordWrapEnabled;
   }

   public void resetCustom() {
      this.mIsLegendCustom = false;
   }

   public void setCustom(List var1) {
      this.mEntries = (LegendEntry[])var1.toArray(new LegendEntry[var1.size()]);
      this.mIsLegendCustom = true;
   }

   public void setCustom(LegendEntry[] var1) {
      this.mEntries = var1;
      this.mIsLegendCustom = true;
   }

   public void setDirection(Legend.LegendDirection var1) {
      this.mDirection = var1;
   }

   public void setDrawInside(boolean var1) {
      this.mDrawInside = var1;
   }

   public void setEntries(List var1) {
      this.mEntries = (LegendEntry[])var1.toArray(new LegendEntry[var1.size()]);
   }

   public void setExtra(List var1) {
      this.mExtraEntries = (LegendEntry[])var1.toArray(new LegendEntry[var1.size()]);
   }

   @Deprecated
   public void setExtra(List var1, List var2) {
      this.setExtra(Utils.convertIntegers(var1), Utils.convertStrings(var2));
   }

   public void setExtra(int[] var1, String[] var2) {
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < Math.min(var1.length, var2.length); ++var4) {
         LegendEntry var5 = new LegendEntry();
         var5.formColor = var1[var4];
         var5.label = var2[var4];
         if (var5.formColor != 1122868 && var5.formColor != 0) {
            if (var5.formColor == 1122867) {
               var5.form = Legend.LegendForm.EMPTY;
            }
         } else {
            var5.form = Legend.LegendForm.NONE;
         }

         var3.add(var5);
      }

      this.mExtraEntries = (LegendEntry[])var3.toArray(new LegendEntry[var3.size()]);
   }

   public void setExtra(LegendEntry[] var1) {
      LegendEntry[] var2 = var1;
      if (var1 == null) {
         var2 = new LegendEntry[0];
      }

      this.mExtraEntries = var2;
   }

   public void setForm(Legend.LegendForm var1) {
      this.mShape = var1;
   }

   public void setFormLineDashEffect(DashPathEffect var1) {
      this.mFormLineDashEffect = var1;
   }

   public void setFormLineWidth(float var1) {
      this.mFormLineWidth = var1;
   }

   public void setFormSize(float var1) {
      this.mFormSize = var1;
   }

   public void setFormToTextSpace(float var1) {
      this.mFormToTextSpace = var1;
   }

   public void setHorizontalAlignment(Legend.LegendHorizontalAlignment var1) {
      this.mHorizontalAlignment = var1;
   }

   public void setMaxSizePercent(float var1) {
      this.mMaxSizePercent = var1;
   }

   public void setOrientation(Legend.LegendOrientation var1) {
      this.mOrientation = var1;
   }

   @Deprecated
   public void setPosition(Legend.LegendPosition var1) {
      Legend.LegendHorizontalAlignment var2;
      Legend.LegendVerticalAlignment var4;
      switch(var1) {
      case LEFT_OF_CHART:
      case LEFT_OF_CHART_INSIDE:
      case LEFT_OF_CHART_CENTER:
         this.mHorizontalAlignment = Legend.LegendHorizontalAlignment.LEFT;
         if (var1 == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {
            var4 = Legend.LegendVerticalAlignment.CENTER;
         } else {
            var4 = Legend.LegendVerticalAlignment.TOP;
         }

         this.mVerticalAlignment = var4;
         this.mOrientation = Legend.LegendOrientation.VERTICAL;
         break;
      case RIGHT_OF_CHART:
      case RIGHT_OF_CHART_INSIDE:
      case RIGHT_OF_CHART_CENTER:
         this.mHorizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT;
         if (var1 == Legend.LegendPosition.RIGHT_OF_CHART_CENTER) {
            var4 = Legend.LegendVerticalAlignment.CENTER;
         } else {
            var4 = Legend.LegendVerticalAlignment.TOP;
         }

         this.mVerticalAlignment = var4;
         this.mOrientation = Legend.LegendOrientation.VERTICAL;
         break;
      case ABOVE_CHART_LEFT:
      case ABOVE_CHART_CENTER:
      case ABOVE_CHART_RIGHT:
         if (var1 == Legend.LegendPosition.ABOVE_CHART_LEFT) {
            var2 = Legend.LegendHorizontalAlignment.LEFT;
         } else if (var1 == Legend.LegendPosition.ABOVE_CHART_RIGHT) {
            var2 = Legend.LegendHorizontalAlignment.RIGHT;
         } else {
            var2 = Legend.LegendHorizontalAlignment.CENTER;
         }

         this.mHorizontalAlignment = var2;
         this.mVerticalAlignment = Legend.LegendVerticalAlignment.TOP;
         this.mOrientation = Legend.LegendOrientation.HORIZONTAL;
         break;
      case BELOW_CHART_LEFT:
      case BELOW_CHART_CENTER:
      case BELOW_CHART_RIGHT:
         if (var1 == Legend.LegendPosition.BELOW_CHART_LEFT) {
            var2 = Legend.LegendHorizontalAlignment.LEFT;
         } else if (var1 == Legend.LegendPosition.BELOW_CHART_RIGHT) {
            var2 = Legend.LegendHorizontalAlignment.RIGHT;
         } else {
            var2 = Legend.LegendHorizontalAlignment.CENTER;
         }

         this.mHorizontalAlignment = var2;
         this.mVerticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
         this.mOrientation = Legend.LegendOrientation.HORIZONTAL;
         break;
      case PIECHART_CENTER:
         this.mHorizontalAlignment = Legend.LegendHorizontalAlignment.CENTER;
         this.mVerticalAlignment = Legend.LegendVerticalAlignment.CENTER;
         this.mOrientation = Legend.LegendOrientation.VERTICAL;
      }

      boolean var3;
      if (var1 != Legend.LegendPosition.LEFT_OF_CHART_INSIDE && var1 != Legend.LegendPosition.RIGHT_OF_CHART_INSIDE) {
         var3 = false;
      } else {
         var3 = true;
      }

      this.mDrawInside = var3;
   }

   public void setStackSpace(float var1) {
      this.mStackSpace = var1;
   }

   public void setVerticalAlignment(Legend.LegendVerticalAlignment var1) {
      this.mVerticalAlignment = var1;
   }

   public void setWordWrapEnabled(boolean var1) {
      this.mWordWrapEnabled = var1;
   }

   public void setXEntrySpace(float var1) {
      this.mXEntrySpace = var1;
   }

   public void setYEntrySpace(float var1) {
      this.mYEntrySpace = var1;
   }

   public static enum LegendDirection {
      LEFT_TO_RIGHT,
      RIGHT_TO_LEFT;
   }

   public static enum LegendForm {
      CIRCLE,
      DEFAULT,
      EMPTY,
      LINE,
      NONE,
      SQUARE;
   }

   public static enum LegendHorizontalAlignment {
      CENTER,
      LEFT,
      RIGHT;
   }

   public static enum LegendOrientation {
      HORIZONTAL,
      VERTICAL;
   }

   @Deprecated
   public static enum LegendPosition {
      ABOVE_CHART_CENTER,
      ABOVE_CHART_LEFT,
      ABOVE_CHART_RIGHT,
      BELOW_CHART_CENTER,
      BELOW_CHART_LEFT,
      BELOW_CHART_RIGHT,
      LEFT_OF_CHART,
      LEFT_OF_CHART_CENTER,
      LEFT_OF_CHART_INSIDE,
      PIECHART_CENTER,
      RIGHT_OF_CHART,
      RIGHT_OF_CHART_CENTER,
      RIGHT_OF_CHART_INSIDE;
   }

   public static enum LegendVerticalAlignment {
      BOTTOM,
      CENTER,
      TOP;
   }
}
