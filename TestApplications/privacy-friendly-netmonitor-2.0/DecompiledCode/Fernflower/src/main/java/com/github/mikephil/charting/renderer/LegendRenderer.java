package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LegendRenderer extends Renderer {
   protected List computedEntries = new ArrayList(16);
   protected FontMetrics legendFontMetrics = new FontMetrics();
   protected Legend mLegend;
   protected Paint mLegendFormPaint;
   protected Paint mLegendLabelPaint;
   private Path mLineFormPath = new Path();

   public LegendRenderer(ViewPortHandler var1, Legend var2) {
      super(var1);
      this.mLegend = var2;
      this.mLegendLabelPaint = new Paint(1);
      this.mLegendLabelPaint.setTextSize(Utils.convertDpToPixel(9.0F));
      this.mLegendLabelPaint.setTextAlign(Align.LEFT);
      this.mLegendFormPaint = new Paint(1);
      this.mLegendFormPaint.setStyle(Style.FILL);
   }

   public void computeLegend(ChartData var1) {
      ChartData var2 = var1;
      if (!this.mLegend.isLegendCustom()) {
         this.computedEntries.clear();

         for(int var3 = 0; var3 < var1.getDataSetCount(); ++var3) {
            IDataSet var4 = var2.getDataSetByIndex(var3);
            List var5 = var4.getColors();
            int var6 = var4.getEntryCount();
            int var9;
            if (var4 instanceof IBarDataSet) {
               IBarDataSet var7 = (IBarDataSet)var4;
               if (var7.isStacked()) {
                  String[] var8 = var7.getStackLabels();

                  for(var9 = 0; var9 < var5.size() && var9 < var7.getStackSize(); ++var9) {
                     this.computedEntries.add(new LegendEntry(var8[var9 % var8.length], var4.getForm(), var4.getFormSize(), var4.getFormLineWidth(), var4.getFormLineDashEffect(), (Integer)var5.get(var9)));
                  }

                  if (var7.getLabel() != null) {
                     this.computedEntries.add(new LegendEntry(var4.getLabel(), Legend.LegendForm.NONE, Float.NaN, Float.NaN, (DashPathEffect)null, 1122867));
                  }
                  continue;
               }
            }

            if (var4 instanceof IPieDataSet) {
               IPieDataSet var11 = (IPieDataSet)var4;

               for(var9 = 0; var9 < var5.size() && var9 < var6; ++var9) {
                  this.computedEntries.add(new LegendEntry(((PieEntry)var11.getEntryForIndex(var9)).getLabel(), var4.getForm(), var4.getFormSize(), var4.getFormLineWidth(), var4.getFormLineDashEffect(), (Integer)var5.get(var9)));
               }

               if (var11.getLabel() != null) {
                  this.computedEntries.add(new LegendEntry(var4.getLabel(), Legend.LegendForm.NONE, Float.NaN, Float.NaN, (DashPathEffect)null, 1122867));
               }
            } else {
               label102: {
                  if (var4 instanceof ICandleDataSet) {
                     ICandleDataSet var12 = (ICandleDataSet)var4;
                     if (var12.getDecreasingColor() != 1122867) {
                        var6 = var12.getDecreasingColor();
                        var9 = var12.getIncreasingColor();
                        this.computedEntries.add(new LegendEntry((String)null, var4.getForm(), var4.getFormSize(), var4.getFormLineWidth(), var4.getFormLineDashEffect(), var6));
                        this.computedEntries.add(new LegendEntry(var4.getLabel(), var4.getForm(), var4.getFormSize(), var4.getFormLineWidth(), var4.getFormLineDashEffect(), var9));
                        break label102;
                     }
                  }

                  for(var9 = 0; var9 < var5.size() && var9 < var6; ++var9) {
                     String var13;
                     if (var9 < var5.size() - 1 && var9 < var6 - 1) {
                        var13 = null;
                     } else {
                        var13 = var1.getDataSetByIndex(var3).getLabel();
                     }

                     this.computedEntries.add(new LegendEntry(var13, var4.getForm(), var4.getFormSize(), var4.getFormLineWidth(), var4.getFormLineDashEffect(), (Integer)var5.get(var9)));
                  }
               }
            }

            var2 = var1;
         }

         if (this.mLegend.getExtraEntries() != null) {
            Collections.addAll(this.computedEntries, this.mLegend.getExtraEntries());
         }

         this.mLegend.setEntries(this.computedEntries);
      }

      Typeface var10 = this.mLegend.getTypeface();
      if (var10 != null) {
         this.mLegendLabelPaint.setTypeface(var10);
      }

      this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
      this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
      this.mLegend.calculateDimensions(this.mLegendLabelPaint, this.mViewPortHandler);
   }

   protected void drawForm(Canvas var1, float var2, float var3, LegendEntry var4, Legend var5) {
      if (var4.formColor != 1122868 && var4.formColor != 1122867 && var4.formColor != 0) {
         int var6 = var1.save();
         Legend.LegendForm var7 = var4.form;
         Legend.LegendForm var8 = var7;
         if (var7 == Legend.LegendForm.DEFAULT) {
            var8 = var5.getForm();
         }

         this.mLegendFormPaint.setColor(var4.formColor);
         float var9;
         if (Float.isNaN(var4.formSize)) {
            var9 = var5.getFormSize();
         } else {
            var9 = var4.formSize;
         }

         float var10 = Utils.convertDpToPixel(var9);
         var9 = var10 / 2.0F;
         switch(var8) {
         case NONE:
         case EMPTY:
         default:
            break;
         case DEFAULT:
         case CIRCLE:
            this.mLegendFormPaint.setStyle(Style.FILL);
            var1.drawCircle(var2 + var9, var3, var9, this.mLegendFormPaint);
            break;
         case SQUARE:
            this.mLegendFormPaint.setStyle(Style.FILL);
            var1.drawRect(var2, var3 - var9, var2 + var10, var3 + var9, this.mLegendFormPaint);
            break;
         case LINE:
            if (Float.isNaN(var4.formLineWidth)) {
               var9 = var5.getFormLineWidth();
            } else {
               var9 = var4.formLineWidth;
            }

            var9 = Utils.convertDpToPixel(var9);
            DashPathEffect var11;
            if (var4.formLineDashEffect == null) {
               var11 = var5.getFormLineDashEffect();
            } else {
               var11 = var4.formLineDashEffect;
            }

            this.mLegendFormPaint.setStyle(Style.STROKE);
            this.mLegendFormPaint.setStrokeWidth(var9);
            this.mLegendFormPaint.setPathEffect(var11);
            this.mLineFormPath.reset();
            this.mLineFormPath.moveTo(var2, var3);
            this.mLineFormPath.lineTo(var2 + var10, var3);
            var1.drawPath(this.mLineFormPath, this.mLegendFormPaint);
         }

         var1.restoreToCount(var6);
      }
   }

   protected void drawLabel(Canvas var1, float var2, float var3, String var4) {
      var1.drawText(var4, var2, var3, this.mLegendLabelPaint);
   }

   public Paint getFormPaint() {
      return this.mLegendFormPaint;
   }

   public Paint getLabelPaint() {
      return this.mLegendLabelPaint;
   }

   public void renderLegend(Canvas var1) {
      if (this.mLegend.isEnabled()) {
         Typeface var2 = this.mLegend.getTypeface();
         if (var2 != null) {
            this.mLegendLabelPaint.setTypeface(var2);
         }

         this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
         this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
         float var3 = Utils.getLineHeight(this.mLegendLabelPaint, this.legendFontMetrics);
         float var4 = Utils.getLineSpacing(this.mLegendLabelPaint, this.legendFontMetrics) + Utils.convertDpToPixel(this.mLegend.getYEntrySpace());
         float var5 = var3 - (float)Utils.calcTextHeight(this.mLegendLabelPaint, "ABC") / 2.0F;
         LegendEntry[] var6 = this.mLegend.getEntries();
         float var7 = Utils.convertDpToPixel(this.mLegend.getFormToTextSpace());
         float var8 = Utils.convertDpToPixel(this.mLegend.getXEntrySpace());
         Legend.LegendOrientation var31 = this.mLegend.getOrientation();
         Legend.LegendHorizontalAlignment var9 = this.mLegend.getHorizontalAlignment();
         Legend.LegendVerticalAlignment var10 = this.mLegend.getVerticalAlignment();
         Legend.LegendDirection var11 = this.mLegend.getDirection();
         float var12 = Utils.convertDpToPixel(this.mLegend.getFormSize());
         float var13 = Utils.convertDpToPixel(this.mLegend.getStackSpace());
         float var14 = this.mLegend.getYOffset();
         float var15 = this.mLegend.getXOffset();
         float var16;
         float var17;
         switch(var9) {
         case LEFT:
            if (var31 != Legend.LegendOrientation.VERTICAL) {
               var15 += this.mViewPortHandler.contentLeft();
            }

            var16 = var15;
            if (var11 == Legend.LegendDirection.RIGHT_TO_LEFT) {
               var16 = var15 + this.mLegend.mNeededWidth;
            }
            break;
         case RIGHT:
            if (var31 == Legend.LegendOrientation.VERTICAL) {
               var15 = this.mViewPortHandler.getChartWidth() - var15;
            } else {
               var15 = this.mViewPortHandler.contentRight() - var15;
            }

            var16 = var15;
            if (var11 == Legend.LegendDirection.LEFT_TO_RIGHT) {
               var16 = var15 - this.mLegend.mNeededWidth;
            }
            break;
         case CENTER:
            if (var31 == Legend.LegendOrientation.VERTICAL) {
               var16 = this.mViewPortHandler.getChartWidth() / 2.0F;
            } else {
               var16 = this.mViewPortHandler.contentLeft() + this.mViewPortHandler.contentWidth() / 2.0F;
            }

            if (var11 == Legend.LegendDirection.LEFT_TO_RIGHT) {
               var17 = var15;
            } else {
               var17 = -var15;
            }

            var16 += var17;
            if (var31 == Legend.LegendOrientation.VERTICAL) {
               double var18 = (double)var16;
               double var20;
               if (var11 == Legend.LegendDirection.LEFT_TO_RIGHT) {
                  var20 = (double)(-this.mLegend.mNeededWidth) / 2.0D + (double)var15;
               } else {
                  var20 = (double)this.mLegend.mNeededWidth / 2.0D - (double)var15;
               }

               var16 = (float)(var18 + var20);
            }
            break;
         default:
            var16 = 0.0F;
         }

         var17 = var7;
         int var22;
         float var26;
         switch(var31) {
         case HORIZONTAL:
            var7 = var7;
            List var27 = this.mLegend.getCalculatedLineSizes();
            List var33 = this.mLegend.getCalculatedLabelSizes();
            List var36 = this.mLegend.getCalculatedLabelBreakPoints();
            var15 = var14;
            switch(var10) {
            case TOP:
               break;
            case BOTTOM:
               var15 = this.mViewPortHandler.getChartHeight() - var14 - this.mLegend.mNeededHeight;
               break;
            case CENTER:
               var15 = var14 + (this.mViewPortHandler.getChartHeight() - this.mLegend.mNeededHeight) / 2.0F;
               break;
            default:
               var15 = 0.0F;
            }

            int var37 = var6.length;
            float var28 = var15;
            var14 = var16;
            var22 = 0;
            int var35 = 0;
            var15 = var8;
            var26 = var5;
            var17 = var16;

            for(var16 = var28; var22 < var37; var16 = var8) {
               LegendEntry var34 = var6[var22];
               boolean var29;
               if (var34.form != Legend.LegendForm.NONE) {
                  var29 = true;
               } else {
                  var29 = false;
               }

               if (Float.isNaN(var34.formSize)) {
                  var28 = var12;
               } else {
                  var28 = Utils.convertDpToPixel(var34.formSize);
               }

               if (var22 < var36.size() && (Boolean)var36.get(var22)) {
                  var8 = var16 + var3 + var4;
                  var16 = var17;
               } else {
                  var8 = var16;
                  var16 = var14;
               }

               if (var16 == var17 && var9 == Legend.LegendHorizontalAlignment.CENTER && var35 < var27.size()) {
                  if (var11 == Legend.LegendDirection.RIGHT_TO_LEFT) {
                     var14 = ((FSize)var27.get(var35)).width;
                  } else {
                     var14 = -((FSize)var27.get(var35)).width;
                  }

                  var16 += var14 / 2.0F;
                  ++var35;
               }

               boolean var30;
               if (var34.label == null) {
                  var30 = true;
               } else {
                  var30 = false;
               }

               if (var29) {
                  var14 = var16;
                  if (var11 == Legend.LegendDirection.RIGHT_TO_LEFT) {
                     var14 = var16 - var28;
                  }

                  this.drawForm(var1, var14, var8 + var26, var34, this.mLegend);
                  var16 = var14;
                  if (var11 == Legend.LegendDirection.LEFT_TO_RIGHT) {
                     var16 = var14 + var28;
                  }
               }

               if (!var30) {
                  var14 = var16;
                  if (var29) {
                     if (var11 == Legend.LegendDirection.RIGHT_TO_LEFT) {
                        var14 = -var7;
                     } else {
                        var14 = var7;
                     }

                     var14 += var16;
                  }

                  var16 = var14;
                  if (var11 == Legend.LegendDirection.RIGHT_TO_LEFT) {
                     var16 = var14 - ((FSize)var33.get(var22)).width;
                  }

                  var14 = var16;
                  this.drawLabel(var1, var16, var8 + var3, var34.label);
                  var16 = var16;
                  if (var11 == Legend.LegendDirection.LEFT_TO_RIGHT) {
                     var16 = var14 + ((FSize)var33.get(var22)).width;
                  }

                  if (var11 == Legend.LegendDirection.RIGHT_TO_LEFT) {
                     var14 = -var15;
                  } else {
                     var14 = var15;
                  }

                  var14 += var16;
               } else {
                  if (var11 == Legend.LegendDirection.RIGHT_TO_LEFT) {
                     var14 = -var13;
                  } else {
                     var14 = var13;
                  }

                  var14 += var16;
               }

               ++var22;
            }

            return;
         case VERTICAL:
            switch(var10) {
            case TOP:
               if (var9 == Legend.LegendHorizontalAlignment.CENTER) {
                  var15 = 0.0F;
               } else {
                  var15 = this.mViewPortHandler.contentTop();
               }

               var15 += var14;
               break;
            case BOTTOM:
               if (var9 == Legend.LegendHorizontalAlignment.CENTER) {
                  var15 = this.mViewPortHandler.getChartHeight();
               } else {
                  var15 = this.mViewPortHandler.contentBottom();
               }

               var15 -= this.mLegend.mNeededHeight + var14;
               break;
            case CENTER:
               var15 = this.mViewPortHandler.getChartHeight() / 2.0F - this.mLegend.mNeededHeight / 2.0F + this.mLegend.getYOffset();
               break;
            default:
               var15 = 0.0F;
            }

            var8 = var15;
            var14 = 0.0F;
            var22 = 0;
            boolean var23 = false;
            var15 = var13;
            var13 = var8;

            for(Legend.LegendDirection var32 = var11; var22 < var6.length; var15 = var7) {
               LegendEntry var24 = var6[var22];
               boolean var25;
               if (var24.form != Legend.LegendForm.NONE) {
                  var25 = true;
               } else {
                  var25 = false;
               }

               if (Float.isNaN(var24.formSize)) {
                  var26 = var12;
               } else {
                  var26 = Utils.convertDpToPixel(var24.formSize);
               }

               if (var25) {
                  if (var32 == Legend.LegendDirection.LEFT_TO_RIGHT) {
                     var8 = var16 + var14;
                  } else {
                     var8 = var16 - (var26 - var14);
                  }

                  var7 = var8;
                  this.drawForm(var1, var8, var13 + var5, var24, this.mLegend);
                  var8 = var8;
                  if (var32 == Legend.LegendDirection.LEFT_TO_RIGHT) {
                     var8 = var7 + var26;
                  }
               } else {
                  var8 = var16;
               }

               var7 = var15;
               if (var24.label == null) {
                  var15 = var14 + var26 + var15;
                  var23 = true;
               } else {
                  if (var25 && !var23) {
                     if (var32 == Legend.LegendDirection.LEFT_TO_RIGHT) {
                        var15 = var17;
                     } else {
                        var15 = -var17;
                     }

                     var15 += var8;
                  } else if (var23) {
                     var15 = var16;
                  } else {
                     var15 = var8;
                  }

                  var8 = var15;
                  if (var32 == Legend.LegendDirection.RIGHT_TO_LEFT) {
                     var8 = var15 - (float)Utils.calcTextWidth(this.mLegendLabelPaint, var24.label);
                  }

                  if (!var23) {
                     this.drawLabel(var1, var8, var13 + var3, var24.label);
                  } else {
                     var13 += var3 + var4;
                     this.drawLabel(var1, var8, var13 + var3, var24.label);
                  }

                  var13 += var3 + var4;
                  var15 = 0.0F;
               }

               ++var22;
               var14 = var15;
            }
         }

      }
   }
}
