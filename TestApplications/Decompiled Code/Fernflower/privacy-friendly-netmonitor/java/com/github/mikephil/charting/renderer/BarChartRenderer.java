package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class BarChartRenderer extends BarLineScatterCandleBubbleRenderer {
   protected Paint mBarBorderPaint;
   protected BarBuffer[] mBarBuffers;
   protected RectF mBarRect = new RectF();
   private RectF mBarShadowRectBuffer = new RectF();
   protected BarDataProvider mChart;
   protected Paint mShadowPaint;

   public BarChartRenderer(BarDataProvider var1, ChartAnimator var2, ViewPortHandler var3) {
      super(var2, var3);
      this.mChart = var1;
      this.mHighlightPaint = new Paint(1);
      this.mHighlightPaint.setStyle(Style.FILL);
      this.mHighlightPaint.setColor(Color.rgb(0, 0, 0));
      this.mHighlightPaint.setAlpha(120);
      this.mShadowPaint = new Paint(1);
      this.mShadowPaint.setStyle(Style.FILL);
      this.mBarBorderPaint = new Paint(1);
      this.mBarBorderPaint.setStyle(Style.STROKE);
   }

   public void drawData(Canvas var1) {
      BarData var2 = this.mChart.getBarData();

      for(int var3 = 0; var3 < var2.getDataSetCount(); ++var3) {
         IBarDataSet var4 = (IBarDataSet)var2.getDataSetByIndex(var3);
         if (var4.isVisible()) {
            this.drawDataSet(var1, var4, var3);
         }
      }

   }

   protected void drawDataSet(Canvas var1, IBarDataSet var2, int var3) {
      Transformer var4 = this.mChart.getTransformer(var2.getAxisDependency());
      this.mBarBorderPaint.setColor(var2.getBarBorderColor());
      this.mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(var2.getBarBorderWidth()));
      float var5 = var2.getBarBorderWidth();
      byte var6 = 0;
      boolean var7 = true;
      boolean var8;
      if (var5 > 0.0F) {
         var8 = true;
      } else {
         var8 = false;
      }

      var5 = this.mAnimator.getPhaseX();
      float var9 = this.mAnimator.getPhaseY();
      float var10;
      int var11;
      int var12;
      if (this.mChart.isDrawBarShadowEnabled()) {
         this.mShadowPaint.setColor(var2.getBarShadowColor());
         var10 = this.mChart.getBarData().getBarWidth() / 2.0F;
         var11 = Math.min((int)Math.ceil((double)((float)var2.getEntryCount() * var5)), var2.getEntryCount());

         for(var12 = 0; var12 < var11; ++var12) {
            float var13 = ((BarEntry)var2.getEntryForIndex(var12)).getX();
            this.mBarShadowRectBuffer.left = var13 - var10;
            this.mBarShadowRectBuffer.right = var13 + var10;
            var4.rectValueToPixel(this.mBarShadowRectBuffer);
            if (this.mViewPortHandler.isInBoundsLeft(this.mBarShadowRectBuffer.right)) {
               if (!this.mViewPortHandler.isInBoundsRight(this.mBarShadowRectBuffer.left)) {
                  break;
               }

               this.mBarShadowRectBuffer.top = this.mViewPortHandler.contentTop();
               this.mBarShadowRectBuffer.bottom = this.mViewPortHandler.contentBottom();
               var1.drawRect(this.mBarShadowRectBuffer, this.mShadowPaint);
            }
         }
      }

      BarBuffer var14 = this.mBarBuffers[var3];
      var14.setPhases(var5, var9);
      var14.setDataSet(var3);
      var14.setInverted(this.mChart.isInverted(var2.getAxisDependency()));
      var14.setBarWidth(this.mChart.getBarData().getBarWidth());
      var14.feed(var2);
      var4.pointValuesToPixel(var14.buffer);
      boolean var16;
      if (var2.getColors().size() == 1) {
         var16 = var7;
      } else {
         var16 = false;
      }

      var12 = var6;
      if (var16) {
         this.mRenderPaint.setColor(var2.getColor());
         var12 = var6;
      }

      for(; var12 < var14.size(); var12 += 4) {
         ViewPortHandler var15 = this.mViewPortHandler;
         float[] var17 = var14.buffer;
         int var19 = var12 + 2;
         if (var15.isInBoundsLeft(var17[var19])) {
            if (!this.mViewPortHandler.isInBoundsRight(var14.buffer[var12])) {
               break;
            }

            if (!var16) {
               this.mRenderPaint.setColor(var2.getColor(var12 / 4));
            }

            var10 = var14.buffer[var12];
            var17 = var14.buffer;
            int var18 = var12 + 1;
            var9 = var17[var18];
            var5 = var14.buffer[var19];
            var17 = var14.buffer;
            var11 = var12 + 3;
            var1.drawRect(var10, var9, var5, var17[var11], this.mRenderPaint);
            if (var8) {
               var1.drawRect(var14.buffer[var12], var14.buffer[var18], var14.buffer[var19], var14.buffer[var11], this.mBarBorderPaint);
            }
         }
      }

   }

   public void drawExtras(Canvas var1) {
   }

   public void drawHighlighted(Canvas var1, Highlight[] var2) {
      BarData var3 = this.mChart.getBarData();
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Highlight var6 = var2[var5];
         IBarDataSet var7 = (IBarDataSet)var3.getDataSetByIndex(var6.getDataSetIndex());
         if (var7 != null && var7.isHighlightEnabled()) {
            BarEntry var8 = (BarEntry)var7.getEntryForXValue(var6.getX(), var6.getY());
            if (this.isInBoundsX(var8, var7)) {
               Transformer var9 = this.mChart.getTransformer(var7.getAxisDependency());
               this.mHighlightPaint.setColor(var7.getHighLightColor());
               this.mHighlightPaint.setAlpha(var7.getHighLightAlpha());
               boolean var10;
               if (var6.getStackIndex() >= 0 && var8.isStacked()) {
                  var10 = true;
               } else {
                  var10 = false;
               }

               float var11;
               float var12;
               if (var10) {
                  if (this.mChart.isHighlightFullBarEnabled()) {
                     var11 = var8.getPositiveSum();
                     var12 = -var8.getNegativeSum();
                  } else {
                     Range var13 = var8.getRanges()[var6.getStackIndex()];
                     var11 = var13.from;
                     var12 = var13.to;
                  }
               } else {
                  var11 = var8.getY();
                  var12 = 0.0F;
               }

               this.prepareBarHighlight(var8.getX(), var11, var12, var3.getBarWidth() / 2.0F, var9);
               this.setHighlightDrawPos(var6, this.mBarRect);
               var1.drawRect(this.mBarRect, this.mHighlightPaint);
            }
         }
      }

   }

   public void drawValues(Canvas var1) {
      if (this.isDrawingValuesAllowed(this.mChart)) {
         List var2 = this.mChart.getBarData().getDataSets();
         float var3 = Utils.convertDpToPixel(4.5F);
         boolean var4 = this.mChart.isDrawValueAboveBarEnabled();

         float var7;
         for(int var5 = 0; var5 < this.mChart.getBarData().getDataSetCount(); var3 = var7) {
            IBarDataSet var6 = (IBarDataSet)var2.get(var5);
            List var8;
            if (!this.shouldDrawValues(var6)) {
               var7 = var3;
               var8 = var2;
            } else {
               this.applyValueTextStyle(var6);
               boolean var9 = this.mChart.isInverted(var6.getAxisDependency());
               float var10 = (float)Utils.calcTextHeight(this.mValuePaint, "8");
               float var11;
               if (var4) {
                  var11 = -var3;
               } else {
                  var11 = var10 + var3;
               }

               float var12;
               if (var4) {
                  var12 = var10 + var3;
               } else {
                  var12 = -var3;
               }

               float var13 = var11;
               var7 = var12;
               if (var9) {
                  var13 = -var11 - var10;
                  var7 = -var12 - var10;
               }

               BarBuffer var14 = this.mBarBuffers[var5];
               float var15 = this.mAnimator.getPhaseY();
               MPPointF var16 = MPPointF.getInstance(var6.getIconsOffset());
               var16.x = Utils.convertDpToPixel(var16.x);
               var16.y = Utils.convertDpToPixel(var16.y);
               int var17;
               float[] var19;
               int var20;
               int var21;
               float var22;
               MPPointF var41;
               if (!var6.isStacked()) {
                  var17 = 0;
                  var8 = var2;
                  BarBuffer var18 = var14;

                  MPPointF var32;
                  for(var32 = var16; (float)var17 < (float)var18.buffer.length * this.mAnimator.getPhaseX(); var17 += 4) {
                     var12 = (var18.buffer[var17] + var18.buffer[var17 + 2]) / 2.0F;
                     if (!this.mViewPortHandler.isInBoundsRight(var12)) {
                        break;
                     }

                     ViewPortHandler var37 = this.mViewPortHandler;
                     var19 = var18.buffer;
                     var20 = var17 + 1;
                     if (var37.isInBoundsY(var19[var20]) && this.mViewPortHandler.isInBoundsLeft(var12)) {
                        var21 = var17 / 4;
                        BarEntry var35 = (BarEntry)var6.getEntryForIndex(var21);
                        var10 = var35.getY();
                        if (var6.isDrawValuesEnabled()) {
                           IValueFormatter var38 = var6.getValueFormatter();
                           if (var10 >= 0.0F) {
                              var11 = var18.buffer[var20] + var13;
                           } else {
                              var11 = var18.buffer[var17 + 3] + var7;
                           }

                           this.drawValue(var1, var38, var10, var35, var5, var12, var11, var6.getValueTextColor(var21));
                        }

                        if (var35.getIcon() != null && var6.isDrawIconsEnabled()) {
                           Drawable var36 = var35.getIcon();
                           if (var10 >= 0.0F) {
                              var11 = var18.buffer[var20] + var13;
                           } else {
                              var11 = var18.buffer[var17 + 3] + var7;
                           }

                           var22 = var32.x;
                           var10 = var32.y;
                           Utils.drawImage(var1, var36, (int)(var12 + var22), (int)(var11 + var10), var36.getIntrinsicWidth(), var36.getIntrinsicHeight());
                        }
                     }
                  }

                  var11 = var3;
                  var41 = var32;
                  var21 = var5;
               } else {
                  MPPointF var40 = var16;
                  List var39 = var2;
                  Transformer var33 = this.mChart.getTransformer(var6.getAxisDependency());
                  var17 = 0;
                  var20 = 0;
                  var9 = var4;

                  while(true) {
                     var11 = var3;
                     var4 = var9;
                     var41 = var40;
                     var21 = var5;
                     var8 = var39;
                     if ((float)var17 >= (float)var6.getEntryCount() * this.mAnimator.getPhaseX()) {
                        break;
                     }

                     label216: {
                        BarEntry var23 = (BarEntry)var6.getEntryForIndex(var17);
                        float[] var34 = var23.getYVals();
                        var22 = (var14.buffer[var20] + var14.buffer[var20 + 2]) / 2.0F;
                        int var24 = var6.getValueTextColor(var17);
                        float var26;
                        if (var34 == null) {
                           if (!this.mViewPortHandler.isInBoundsRight(var22)) {
                              var11 = var3;
                              var4 = var9;
                              var41 = var40;
                              var21 = var5;
                              var8 = var39;
                              break;
                           }

                           ViewPortHandler var44 = this.mViewPortHandler;
                           var19 = var14.buffer;
                           var21 = var20 + 1;
                           if (!var44.isInBoundsY(var19[var21]) || !this.mViewPortHandler.isInBoundsLeft(var22)) {
                              var21 = var20;
                              break label216;
                           }

                           if (var6.isDrawValuesEnabled()) {
                              IValueFormatter var42 = var6.getValueFormatter();
                              var12 = var23.getY();
                              var10 = var14.buffer[var21];
                              if (var23.getY() >= 0.0F) {
                                 var11 = var13;
                              } else {
                                 var11 = var7;
                              }

                              this.drawValue(var1, var42, var12, var23, var5, var22, var10 + var11, var24);
                           }

                           if (var23.getIcon() != null && var6.isDrawIconsEnabled()) {
                              Drawable var43 = var23.getIcon();
                              var12 = var14.buffer[var21];
                              if (var23.getY() >= 0.0F) {
                                 var11 = var13;
                              } else {
                                 var11 = var7;
                              }

                              var26 = var40.x;
                              var10 = var40.y;
                              Utils.drawImage(var1, var43, (int)(var22 + var26), (int)(var12 + var11 + var10), var43.getIntrinsicWidth(), var43.getIntrinsicHeight());
                           }
                        } else {
                           float[] var25 = var34;
                           var19 = new float[var34.length * 2];
                           var11 = -var23.getNegativeSum();
                           var10 = 0.0F;
                           int var27 = 0;

                           for(var21 = 0; var27 < var19.length; var11 = var26) {
                              float var29;
                              label165: {
                                 float var28 = var25[var21];
                                 if (var28 == 0.0F) {
                                    var12 = var28;
                                    var29 = var10;
                                    var26 = var11;
                                    if (var10 == 0.0F) {
                                       break label165;
                                    }

                                    if (var11 == 0.0F) {
                                       var12 = var28;
                                       var29 = var10;
                                       var26 = var11;
                                       break label165;
                                    }
                                 }

                                 if (var28 >= 0.0F) {
                                    var12 = var10 + var28;
                                    var29 = var12;
                                    var26 = var11;
                                 } else {
                                    var26 = var11 - var28;
                                    var29 = var10;
                                    var12 = var11;
                                 }
                              }

                              var19[var27 + 1] = var12 * var15;
                              var27 += 2;
                              ++var21;
                              var10 = var29;
                           }

                           var33.pointValuesToPixel(var19);

                           for(var21 = 0; var21 < var19.length; var21 += 2) {
                              int var30 = var21 / 2;
                              var12 = var25[var30];
                              boolean var45;
                              if ((var12 != 0.0F || var11 != 0.0F || var10 <= 0.0F) && var12 >= 0.0F) {
                                 var45 = false;
                              } else {
                                 var45 = true;
                              }

                              var26 = var19[var21 + 1];
                              if (var45) {
                                 var12 = var7;
                              } else {
                                 var12 = var13;
                              }

                              var12 += var26;
                              if (!this.mViewPortHandler.isInBoundsRight(var22)) {
                                 break;
                              }

                              if (this.mViewPortHandler.isInBoundsY(var12) && this.mViewPortHandler.isInBoundsLeft(var22)) {
                                 if (var6.isDrawValuesEnabled()) {
                                    this.drawValue(var1, var6.getValueFormatter(), var25[var30], var23, var5, var22, var12, var24);
                                 }

                                 if (var23.getIcon() != null && var6.isDrawIconsEnabled()) {
                                    Drawable var31 = var23.getIcon();
                                    Utils.drawImage(var1, var31, (int)(var22 + var40.x), (int)(var12 + var40.y), var31.getIntrinsicWidth(), var31.getIntrinsicHeight());
                                 }
                              }
                           }
                        }

                        if (var34 == null) {
                           var21 = var20 + 4;
                        } else {
                           var21 = var20 + 4 * var34.length;
                        }

                        ++var17;
                     }

                     var20 = var21;
                  }
               }

               var7 = var11;
               var5 = var21;
               MPPointF.recycleInstance(var41);
            }

            ++var5;
            var2 = var8;
         }
      }

   }

   public void initBuffers() {
      BarData var1 = this.mChart.getBarData();
      this.mBarBuffers = new BarBuffer[var1.getDataSetCount()];

      for(int var2 = 0; var2 < this.mBarBuffers.length; ++var2) {
         IBarDataSet var3 = (IBarDataSet)var1.getDataSetByIndex(var2);
         BarBuffer[] var4 = this.mBarBuffers;
         int var5 = var3.getEntryCount();
         int var6;
         if (var3.isStacked()) {
            var6 = var3.getStackSize();
         } else {
            var6 = 1;
         }

         var4[var2] = new BarBuffer(var5 * 4 * var6, var1.getDataSetCount(), var3.isStacked());
      }

   }

   protected void prepareBarHighlight(float var1, float var2, float var3, float var4, Transformer var5) {
      this.mBarRect.set(var1 - var4, var2, var1 + var4, var3);
      var5.rectToPixelPhase(this.mBarRect, this.mAnimator.getPhaseY());
   }

   protected void setHighlightDrawPos(Highlight var1, RectF var2) {
      var1.setDraw(var2.centerX(), var2.top);
   }
}
