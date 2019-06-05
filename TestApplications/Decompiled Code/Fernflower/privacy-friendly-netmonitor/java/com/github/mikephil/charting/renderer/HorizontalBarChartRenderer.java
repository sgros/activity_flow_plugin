package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.buffer.HorizontalBarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class HorizontalBarChartRenderer extends BarChartRenderer {
   private RectF mBarShadowRectBuffer = new RectF();

   public HorizontalBarChartRenderer(BarDataProvider var1, ChartAnimator var2, ViewPortHandler var3) {
      super(var1, var2, var3);
      this.mValuePaint.setTextAlign(Align.LEFT);
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

      float var9 = this.mAnimator.getPhaseX();
      float var10 = this.mAnimator.getPhaseY();
      float var11;
      int var12;
      int var13;
      if (this.mChart.isDrawBarShadowEnabled()) {
         this.mShadowPaint.setColor(var2.getBarShadowColor());
         var11 = this.mChart.getBarData().getBarWidth() / 2.0F;
         var12 = Math.min((int)Math.ceil((double)((float)var2.getEntryCount() * var9)), var2.getEntryCount());

         for(var13 = 0; var13 < var12; ++var13) {
            var5 = ((BarEntry)var2.getEntryForIndex(var13)).getX();
            this.mBarShadowRectBuffer.top = var5 - var11;
            this.mBarShadowRectBuffer.bottom = var5 + var11;
            var4.rectValueToPixel(this.mBarShadowRectBuffer);
            if (this.mViewPortHandler.isInBoundsTop(this.mBarShadowRectBuffer.bottom)) {
               if (!this.mViewPortHandler.isInBoundsBottom(this.mBarShadowRectBuffer.top)) {
                  break;
               }

               this.mBarShadowRectBuffer.left = this.mViewPortHandler.contentLeft();
               this.mBarShadowRectBuffer.right = this.mViewPortHandler.contentRight();
               var1.drawRect(this.mBarShadowRectBuffer, this.mShadowPaint);
            }
         }
      }

      BarBuffer var14 = this.mBarBuffers[var3];
      var14.setPhases(var9, var10);
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

      var13 = var6;
      if (var16) {
         this.mRenderPaint.setColor(var2.getColor());
         var13 = var6;
      }

      for(; var13 < var14.size(); var13 += 4) {
         ViewPortHandler var17 = this.mViewPortHandler;
         float[] var15 = var14.buffer;
         int var20 = var13 + 3;
         if (!var17.isInBoundsTop(var15[var20])) {
            break;
         }

         var17 = this.mViewPortHandler;
         var15 = var14.buffer;
         var12 = var13 + 1;
         if (var17.isInBoundsBottom(var15[var12])) {
            if (!var16) {
               this.mRenderPaint.setColor(var2.getColor(var13 / 4));
            }

            var11 = var14.buffer[var13];
            var5 = var14.buffer[var12];
            float[] var18 = var14.buffer;
            int var19 = var13 + 2;
            var1.drawRect(var11, var5, var18[var19], var14.buffer[var20], this.mRenderPaint);
            if (var8) {
               var1.drawRect(var14.buffer[var13], var14.buffer[var12], var14.buffer[var19], var14.buffer[var20], this.mBarBorderPaint);
            }
         }
      }

   }

   protected void drawValue(Canvas var1, String var2, float var3, float var4, int var5) {
      this.mValuePaint.setColor(var5);
      var1.drawText(var2, var3, var4, this.mValuePaint);
   }

   public void drawValues(Canvas var1) {
      if (this.isDrawingValuesAllowed(this.mChart)) {
         List var2 = this.mChart.getBarData().getDataSets();
         float var3 = Utils.convertDpToPixel(5.0F);
         boolean var4 = this.mChart.isDrawValueAboveBarEnabled();

         for(int var5 = 0; var5 < this.mChart.getBarData().getDataSetCount(); ++var5) {
            IBarDataSet var6 = (IBarDataSet)var2.get(var5);
            if (this.shouldDrawValues(var6)) {
               boolean var7 = this.mChart.isInverted(var6.getAxisDependency());
               this.applyValueTextStyle(var6);
               float var8 = (float)Utils.calcTextHeight(this.mValuePaint, "10") / 2.0F;
               IValueFormatter var9 = var6.getValueFormatter();
               BarBuffer var10 = this.mBarBuffers[var5];
               float var11 = this.mAnimator.getPhaseY();
               MPPointF var12 = MPPointF.getInstance(var6.getIconsOffset());
               var12.x = Utils.convertDpToPixel(var12.x);
               var12.y = Utils.convertDpToPixel(var12.y);
               int var13;
               float var14;
               boolean var15;
               int var19;
               float var20;
               float var21;
               float var22;
               float var23;
               float var24;
               float var25;
               MPPointF var43;
               if (!var6.isStacked()) {
                  var13 = 0;
                  var14 = var8;
                  var15 = var7;
                  IValueFormatter var17 = var9;
                  BarBuffer var18 = var10;

                  MPPointF var34;
                  for(var34 = var12; (float)var13 < (float)var18.buffer.length * this.mAnimator.getPhaseX(); var13 += 4) {
                     float[] var37 = var18.buffer;
                     var19 = var13 + 1;
                     var20 = (var37[var19] + var18.buffer[var13 + 3]) / 2.0F;
                     if (!this.mViewPortHandler.isInBoundsTop(var18.buffer[var19])) {
                        break;
                     }

                     if (this.mViewPortHandler.isInBoundsX(var18.buffer[var13]) && this.mViewPortHandler.isInBoundsBottom(var18.buffer[var19])) {
                        BarEntry var36 = (BarEntry)var6.getEntryForIndex(var13 / 4);
                        var21 = var36.getY();
                        String var38 = var17.getFormattedValue(var21, var36, var5, this.mViewPortHandler);
                        var8 = (float)Utils.calcTextWidth(this.mValuePaint, var38);
                        if (var4) {
                           var22 = var3;
                        } else {
                           var22 = -(var8 + var3);
                        }

                        if (var4) {
                           var23 = -(var8 + var3);
                        } else {
                           var23 = var3;
                        }

                        var24 = var22;
                        var25 = var23;
                        if (var15) {
                           var24 = -var22 - var8;
                           var25 = -var23 - var8;
                        }

                        var22 = var24;
                        if (var6.isDrawValuesEnabled()) {
                           var24 = var18.buffer[var13 + 2];
                           if (var21 >= 0.0F) {
                              var23 = var22;
                           } else {
                              var23 = var25;
                           }

                           this.drawValue(var1, var38, var23 + var24, var20 + var14, var6.getValueTextColor(var13 / 2));
                        }

                        IValueFormatter var39 = var17;
                        var17 = var17;
                        if (var36.getIcon() != null) {
                           var17 = var39;
                           if (var6.isDrawIconsEnabled()) {
                              Drawable var42 = var36.getIcon();
                              var23 = var18.buffer[var13 + 2];
                              if (var21 < 0.0F) {
                                 var22 = var25;
                              }

                              var24 = var34.x;
                              var25 = var34.y;
                              Utils.drawImage(var1, var42, (int)(var23 + var22 + var24), (int)(var20 + var25), var42.getIntrinsicWidth(), var42.getIntrinsicHeight());
                              var17 = var39;
                           }
                        }
                     }
                  }

                  var43 = var34;
                  var25 = var3;
                  var15 = var4;
                  var2 = var2;
               } else {
                  List var44 = var2;
                  MPPointF var41 = var12;
                  Transformer var26 = this.mChart.getTransformer(var6.getAxisDependency());
                  var19 = 0;
                  var13 = 0;

                  label212:
                  while(true) {
                     float[] var35;
                     label210:
                     while(true) {
                        var43 = var41;
                        var25 = var3;
                        var15 = var4;
                        var2 = var44;
                        if ((float)var19 >= (float)var6.getEntryCount() * this.mAnimator.getPhaseX()) {
                           break label212;
                        }

                        BarEntry var40 = (BarEntry)var6.getEntryForIndex(var19);
                        int var27 = var6.getValueTextColor(var19);
                        var35 = var40.getYVals();
                        int var29;
                        float[] var45;
                        if (var35 == null) {
                           ViewPortHandler var28 = this.mViewPortHandler;
                           var45 = var10.buffer;
                           var29 = var13 + 1;
                           if (!var28.isInBoundsTop(var45[var29])) {
                              var43 = var41;
                              var25 = var3;
                              var15 = var4;
                              var2 = var44;
                              break label212;
                           }

                           if (!this.mViewPortHandler.isInBoundsX(var10.buffer[var13]) || !this.mViewPortHandler.isInBoundsBottom(var10.buffer[var29])) {
                              continue;
                           }

                           String var46 = var9.getFormattedValue(var40.getY(), var40, var5, this.mViewPortHandler);
                           var14 = (float)Utils.calcTextWidth(this.mValuePaint, var46);
                           if (var4) {
                              var22 = var3;
                           } else {
                              var22 = -(var14 + var3);
                           }

                           if (var4) {
                              var23 = -(var14 + var3);
                           } else {
                              var23 = var3;
                           }

                           var24 = var22;
                           var25 = var23;
                           if (var7) {
                              var24 = -var22 - var14;
                              var25 = -var23 - var14;
                           }

                           var22 = var24;
                           if (var6.isDrawValuesEnabled()) {
                              var24 = var10.buffer[var13 + 2];
                              if (var40.getY() >= 0.0F) {
                                 var23 = var22;
                              } else {
                                 var23 = var25;
                              }

                              this.drawValue(var1, var46, var24 + var23, var10.buffer[var29] + var8, var27);
                           }

                           var15 = var4;
                           if (var40.getIcon() != null) {
                              var15 = var4;
                              if (var6.isDrawIconsEnabled()) {
                                 Drawable var47 = var40.getIcon();
                                 var23 = var10.buffer[var13 + 2];
                                 if (var40.getY() < 0.0F) {
                                    var22 = var25;
                                 }

                                 var14 = var10.buffer[var29];
                                 var24 = var41.x;
                                 var25 = var41.y;
                                 Utils.drawImage(var1, var47, (int)(var23 + var22 + var24), (int)(var14 + var25), var47.getIntrinsicWidth(), var47.getIntrinsicHeight());
                                 var15 = var4;
                              }
                           }
                        } else {
                           var25 = var3;
                           float[] var48 = var35;
                           var45 = new float[var35.length * 2];
                           var22 = -var40.getNegativeSum();
                           var21 = 0.0F;
                           int var30 = 0;

                           for(var29 = 0; var30 < var45.length; var22 = var24) {
                              label201: {
                                 var20 = var48[var29];
                                 if (var20 == 0.0F) {
                                    var23 = var20;
                                    var14 = var21;
                                    var24 = var22;
                                    if (var21 == 0.0F) {
                                       break label201;
                                    }

                                    if (var22 == 0.0F) {
                                       var23 = var20;
                                       var14 = var21;
                                       var24 = var22;
                                       break label201;
                                    }
                                 }

                                 if (var20 >= 0.0F) {
                                    var23 = var21 + var20;
                                    var14 = var23;
                                    var24 = var22;
                                 } else {
                                    var24 = var22 - var20;
                                    var14 = var21;
                                    var23 = var22;
                                 }
                              }

                              var45[var30] = var23 * var11;
                              var30 += 2;
                              ++var29;
                              var21 = var14;
                           }

                           var26.pointValuesToPixel(var45);
                           var29 = 0;

                           while(true) {
                              var15 = var4;
                              if (var29 >= var45.length) {
                                 break;
                              }

                              float var31 = var48[var29 / 2];
                              String var32 = var9.getFormattedValue(var31, var40, var5, this.mViewPortHandler);
                              float var33 = (float)Utils.calcTextWidth(this.mValuePaint, var32);
                              if (var4) {
                                 var24 = var25;
                              } else {
                                 var24 = -(var33 + var25);
                              }

                              if (var4) {
                                 var20 = -(var33 + var25);
                              } else {
                                 var20 = var25;
                              }

                              var14 = var24;
                              var23 = var20;
                              if (var7) {
                                 var14 = -var24 - var33;
                                 var23 = -var20 - var33;
                              }

                              boolean var49;
                              if ((var31 != 0.0F || var22 != 0.0F || var21 <= 0.0F) && var31 >= 0.0F) {
                                 var49 = false;
                              } else {
                                 var49 = true;
                              }

                              var24 = var45[var29];
                              if (var49) {
                                 var14 = var23;
                              }

                              var24 += var14;
                              var23 = (var10.buffer[var13 + 1] + var10.buffer[var13 + 3]) / 2.0F;
                              if (!this.mViewPortHandler.isInBoundsTop(var23)) {
                                 break label210;
                              }

                              if (this.mViewPortHandler.isInBoundsX(var24) && this.mViewPortHandler.isInBoundsBottom(var23)) {
                                 if (var6.isDrawValuesEnabled()) {
                                    this.drawValue(var1, var32, var24, var23 + var8, var27);
                                 }

                                 if (var40.getIcon() != null && var6.isDrawIconsEnabled()) {
                                    Drawable var50 = var40.getIcon();
                                    Utils.drawImage(var1, var50, (int)(var24 + var41.x), (int)(var23 + var41.y), var50.getIntrinsicWidth(), var50.getIntrinsicHeight());
                                 }
                              }

                              var29 += 2;
                           }
                        }

                        var4 = var15;
                        break;
                     }

                     if (var35 == null) {
                        var13 += 4;
                     } else {
                        var13 += 4 * var35.length;
                     }

                     ++var19;
                  }
               }

               var3 = var25;
               var4 = var15;
               MPPointF.recycleInstance(var43);
            }
         }
      }

   }

   public void initBuffers() {
      BarData var1 = this.mChart.getBarData();
      this.mBarBuffers = new HorizontalBarBuffer[var1.getDataSetCount()];

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

         var4[var2] = new HorizontalBarBuffer(var5 * 4 * var6, var1.getDataSetCount(), var3.isStacked());
      }

   }

   protected boolean isDrawingValuesAllowed(ChartInterface var1) {
      boolean var2;
      if ((float)var1.getData().getEntryCount() < (float)var1.getMaxVisibleCount() * this.mViewPortHandler.getScaleY()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   protected void prepareBarHighlight(float var1, float var2, float var3, float var4, Transformer var5) {
      this.mBarRect.set(var2, var1 - var4, var3, var1 + var4);
      var5.rectToPixelPhaseHorizontal(this.mBarRect, this.mAnimator.getPhaseY());
   }

   protected void setHighlightDrawPos(Highlight var1, RectF var2) {
      var1.setDraw(var2.centerY(), var2.right);
   }
}
