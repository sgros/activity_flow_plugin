package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

public class PieChartRenderer extends DataRenderer {
   protected Canvas mBitmapCanvas;
   private RectF mCenterTextLastBounds = new RectF();
   private CharSequence mCenterTextLastValue;
   private StaticLayout mCenterTextLayout;
   private TextPaint mCenterTextPaint;
   protected PieChart mChart;
   protected WeakReference mDrawBitmap;
   protected Path mDrawCenterTextPathBuffer = new Path();
   protected RectF mDrawHighlightedRectF = new RectF();
   private Paint mEntryLabelsPaint;
   private Path mHoleCirclePath = new Path();
   protected Paint mHolePaint;
   private RectF mInnerRectBuffer = new RectF();
   private Path mPathBuffer = new Path();
   private RectF[] mRectBuffer = new RectF[]{new RectF(), new RectF(), new RectF()};
   protected Paint mTransparentCirclePaint;
   protected Paint mValueLinePaint;

   public PieChartRenderer(PieChart var1, ChartAnimator var2, ViewPortHandler var3) {
      super(var2, var3);
      this.mChart = var1;
      this.mHolePaint = new Paint(1);
      this.mHolePaint.setColor(-1);
      this.mHolePaint.setStyle(Style.FILL);
      this.mTransparentCirclePaint = new Paint(1);
      this.mTransparentCirclePaint.setColor(-1);
      this.mTransparentCirclePaint.setStyle(Style.FILL);
      this.mTransparentCirclePaint.setAlpha(105);
      this.mCenterTextPaint = new TextPaint(1);
      this.mCenterTextPaint.setColor(-16777216);
      this.mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12.0F));
      this.mValuePaint.setTextSize(Utils.convertDpToPixel(13.0F));
      this.mValuePaint.setColor(-1);
      this.mValuePaint.setTextAlign(Align.CENTER);
      this.mEntryLabelsPaint = new Paint(1);
      this.mEntryLabelsPaint.setColor(-1);
      this.mEntryLabelsPaint.setTextAlign(Align.CENTER);
      this.mEntryLabelsPaint.setTextSize(Utils.convertDpToPixel(13.0F));
      this.mValueLinePaint = new Paint(1);
      this.mValueLinePaint.setStyle(Style.STROKE);
   }

   protected float calculateMinimumRadiusForSpacedSlice(MPPointF var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = var7 / 2.0F;
      float var9 = var1.x;
      double var10 = (double)((var6 + var7) * 0.017453292F);
      var7 = var9 + (float)Math.cos(var10) * var2;
      var9 = var1.y + (float)Math.sin(var10) * var2;
      float var12 = var1.x;
      var10 = (double)((var6 + var8) * 0.017453292F);
      var6 = (float)Math.cos(var10);
      float var13 = var1.y;
      var8 = (float)Math.sin(var10);
      return (float)((double)(var2 - (float)(Math.sqrt(Math.pow((double)(var7 - var4), 2.0D) + Math.pow((double)(var9 - var5), 2.0D)) / 2.0D * Math.tan(0.017453292519943295D * ((180.0D - (double)var3) / 2.0D)))) - Math.sqrt(Math.pow((double)(var12 + var6 * var2 - (var7 + var4) / 2.0F), 2.0D) + Math.pow((double)(var13 + var8 * var2 - (var9 + var5) / 2.0F), 2.0D)));
   }

   protected void drawCenterText(Canvas var1) {
      CharSequence var2 = this.mChart.getCenterText();
      if (this.mChart.isDrawCenterTextEnabled() && var2 != null) {
         MPPointF var3 = this.mChart.getCenterCircleBox();
         MPPointF var4 = this.mChart.getCenterTextOffset();
         float var5 = var3.x + var4.x;
         float var6 = var3.y + var4.y;
         float var7;
         if (this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled()) {
            var7 = this.mChart.getRadius() * (this.mChart.getHoleRadius() / 100.0F);
         } else {
            var7 = this.mChart.getRadius();
         }

         RectF var8 = this.mRectBuffer[0];
         var8.left = var5 - var7;
         var8.top = var6 - var7;
         var8.right = var5 + var7;
         var8.bottom = var6 + var7;
         RectF var9 = this.mRectBuffer[1];
         var9.set(var8);
         var7 = this.mChart.getCenterTextRadiusPercent() / 100.0F;
         if ((double)var7 > 0.0D) {
            var9.inset((var9.width() - var9.width() * var7) / 2.0F, (var9.height() - var9.height() * var7) / 2.0F);
         }

         if (!var2.equals(this.mCenterTextLastValue) || !var9.equals(this.mCenterTextLastBounds)) {
            this.mCenterTextLastBounds.set(var9);
            this.mCenterTextLastValue = var2;
            var7 = this.mCenterTextLastBounds.width();
            this.mCenterTextLayout = new StaticLayout(var2, 0, var2.length(), this.mCenterTextPaint, (int)Math.max(Math.ceil((double)var7), 1.0D), Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
         }

         var7 = (float)this.mCenterTextLayout.getHeight();
         var1.save();
         if (VERSION.SDK_INT >= 18) {
            Path var10 = this.mDrawCenterTextPathBuffer;
            var10.reset();
            var10.addOval(var8, Direction.CW);
            var1.clipPath(var10);
         }

         var1.translate(var9.left, var9.top + (var9.height() - var7) / 2.0F);
         this.mCenterTextLayout.draw(var1);
         var1.restore();
         MPPointF.recycleInstance(var3);
         MPPointF.recycleInstance(var4);
      }

   }

   public void drawData(Canvas var1) {
      int var2 = (int)this.mViewPortHandler.getChartWidth();
      int var3 = (int)this.mViewPortHandler.getChartHeight();
      if (this.mDrawBitmap == null || ((Bitmap)this.mDrawBitmap.get()).getWidth() != var2 || ((Bitmap)this.mDrawBitmap.get()).getHeight() != var3) {
         if (var2 <= 0 || var3 <= 0) {
            return;
         }

         this.mDrawBitmap = new WeakReference(Bitmap.createBitmap(var2, var3, Config.ARGB_4444));
         this.mBitmapCanvas = new Canvas((Bitmap)this.mDrawBitmap.get());
      }

      ((Bitmap)this.mDrawBitmap.get()).eraseColor(0);
      Iterator var4 = ((PieData)this.mChart.getData()).getDataSets().iterator();

      while(var4.hasNext()) {
         IPieDataSet var5 = (IPieDataSet)var4.next();
         if (var5.isVisible() && var5.getEntryCount() > 0) {
            this.drawDataSet(var1, var5);
         }
      }

   }

   protected void drawDataSet(Canvas var1, IPieDataSet var2) {
      PieChartRenderer var3 = this;
      IPieDataSet var4 = var2;
      float var5 = this.mChart.getRotationAngle();
      float var6 = this.mAnimator.getPhaseX();
      float var7 = this.mAnimator.getPhaseY();
      RectF var8 = this.mChart.getCircleBox();
      int var9 = var2.getEntryCount();
      float[] var10 = this.mChart.getDrawAngles();
      MPPointF var31 = this.mChart.getCenterCircleBox();
      float var11 = this.mChart.getRadius();
      boolean var12 = this.mChart.isDrawHoleEnabled();
      boolean var13 = true;
      boolean var14;
      if (var12 && !this.mChart.isDrawSlicesUnderHoleEnabled()) {
         var14 = true;
      } else {
         var14 = false;
      }

      float var15;
      if (var14) {
         var15 = this.mChart.getHoleRadius() / 100.0F * var11;
      } else {
         var15 = 0.0F;
      }

      int var16 = 0;

      int var17;
      int var18;
      for(var17 = var16; var16 < var9; var17 = var18) {
         var18 = var17;
         if (Math.abs(((PieEntry)var4.getEntryForIndex(var16)).getY()) > Utils.FLOAT_EPSILON) {
            var18 = var17 + 1;
         }

         ++var16;
      }

      float var19;
      if (var17 <= 1) {
         var19 = 0.0F;
      } else {
         var19 = this.getSliceSpace(var4);
      }

      byte var20 = 0;
      float var21 = 0.0F;
      var18 = var9;
      boolean var36 = var13;

      for(int var35 = var20; var35 < var18; ++var35) {
         float var22 = var10[var35];
         if (Math.abs(var2.getEntryForIndex(var35).getY()) > Utils.FLOAT_EPSILON && !var3.mChart.needsHighlight(var35)) {
            boolean var34;
            if (var19 > 0.0F && var22 <= 180.0F) {
               var34 = var36;
            } else {
               var34 = false;
            }

            var3.mRenderPaint.setColor(var2.getColor(var35));
            float var23;
            if (var17 == 1) {
               var23 = 0.0F;
            } else {
               var23 = var19 / (0.017453292F * var11);
            }

            float var24 = var5 + (var21 + var23 / 2.0F) * var7;
            float var25 = (var22 - var23) * var7;
            var23 = var25;
            if (var25 < 0.0F) {
               var23 = 0.0F;
            }

            var3.mPathBuffer.reset();
            var25 = var31.x;
            double var26 = (double)(var24 * 0.017453292F);
            float var28 = var25 + (float)Math.cos(var26) * var11;
            float var29 = var31.y + (float)Math.sin(var26) * var11;
            if (var23 >= 360.0F && var23 % 360.0F <= Utils.FLOAT_EPSILON) {
               var3.mPathBuffer.addCircle(var31.x, var31.y, var11, Direction.CW);
            } else {
               var3.mPathBuffer.moveTo(var28, var29);
               var3.mPathBuffer.arcTo(var8, var24, var23);
            }

            var3.mInnerRectBuffer.set(var31.x - var15, var31.y - var15, var31.x + var15, var31.y + var15);
            PieChartRenderer var33;
            if (!var14 || var15 <= 0.0F && !var34) {
               boolean var37 = true;
               MPPointF var30 = var31;
               var33 = var3;
               var31 = var31;
               var36 = var37;
               if (var23 % 360.0F > Utils.FLOAT_EPSILON) {
                  if (var34) {
                     var25 = var23 / 2.0F;
                     var23 = var3.calculateMinimumRadiusForSpacedSlice(var30, var11, var22 * var7, var28, var29, var24, var23);
                     var28 = var30.x;
                     var26 = (double)((var24 + var25) * 0.017453292F);
                     var25 = (float)Math.cos(var26);
                     var24 = var30.y;
                     var29 = (float)Math.sin(var26);
                     var3.mPathBuffer.lineTo(var28 + var25 * var23, var24 + var23 * var29);
                     var33 = var3;
                     var31 = var30;
                     var36 = var37;
                  } else {
                     var3.mPathBuffer.lineTo(var30.x, var30.y);
                     var36 = var37;
                     var31 = var30;
                     var33 = var3;
                  }
               }
            } else {
               if (var34) {
                  var24 = var3.calculateMinimumRadiusForSpacedSlice(var31, var11, var22 * var7, var28, var29, var24, var23);
                  var25 = var24;
                  if (var24 < 0.0F) {
                     var25 = -var24;
                  }

                  var25 = Math.max(var15, var25);
               } else {
                  var25 = var15;
               }

               var24 = var23;
               if (var17 != 1 && var25 != 0.0F) {
                  var23 = var19 / (0.017453292F * var25);
               } else {
                  var23 = 0.0F;
               }

               var29 = var23 / 2.0F;
               var28 = (var22 - var23) * var7;
               var23 = var28;
               if (var28 < 0.0F) {
                  var23 = 0.0F;
               }

               var28 = (var21 + var29) * var7 + var5 + var23;
               if (var24 >= 360.0F && var24 % 360.0F <= Utils.FLOAT_EPSILON) {
                  this.mPathBuffer.addCircle(var31.x, var31.y, var25, Direction.CCW);
               } else {
                  Path var32 = this.mPathBuffer;
                  var24 = var31.x;
                  var26 = (double)(var28 * 0.017453292F);
                  var32.lineTo(var24 + (float)Math.cos(var26) * var25, var31.y + var25 * (float)Math.sin(var26));
                  this.mPathBuffer.arcTo(this.mInnerRectBuffer, var28, -var23);
               }

               var36 = true;
               var33 = this;
            }

            var33.mPathBuffer.close();
            var33.mBitmapCanvas.drawPath(var33.mPathBuffer, var33.mRenderPaint);
            var3 = var33;
         }

         var21 += var22 * var6;
      }

      MPPointF.recycleInstance(var31);
   }

   protected void drawEntryLabel(Canvas var1, String var2, float var3, float var4) {
      var1.drawText(var2, var3, var4, this.mEntryLabelsPaint);
   }

   public void drawExtras(Canvas var1) {
      this.drawHole(var1);
      var1.drawBitmap((Bitmap)this.mDrawBitmap.get(), 0.0F, 0.0F, (Paint)null);
      this.drawCenterText(var1);
   }

   public void drawHighlighted(Canvas var1, Highlight[] var2) {
      float var3 = this.mAnimator.getPhaseX();
      float var4 = this.mAnimator.getPhaseY();
      float var5 = this.mChart.getRotationAngle();
      float[] var6 = this.mChart.getDrawAngles();
      float[] var31 = this.mChart.getAbsoluteAngles();
      MPPointF var7 = this.mChart.getCenterCircleBox();
      float var8 = this.mChart.getRadius();
      boolean var9;
      if (this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled()) {
         var9 = true;
      } else {
         var9 = false;
      }

      boolean var10 = false;
      float var11;
      if (var9) {
         var11 = this.mChart.getHoleRadius() / 100.0F * var8;
      } else {
         var11 = 0.0F;
      }

      RectF var12 = this.mDrawHighlightedRectF;
      var12.set(0.0F, 0.0F, 0.0F, 0.0F);

      for(int var13 = 0; var13 < var2.length; ++var13) {
         int var15 = (int)var2[var13].getX();
         if (var15 < var6.length) {
            IPieDataSet var14 = ((PieData)this.mChart.getData()).getDataSetByIndex(var2[var13].getDataSetIndex());
            if (var14 != null && var14.isHighlightEnabled()) {
               int var16 = var14.getEntryCount();
               int var17 = 0;

               int var18;
               int var19;
               for(var18 = var17; var17 < var16; var18 = var19) {
                  var19 = var18;
                  if (Math.abs(((PieEntry)var14.getEntryForIndex(var17)).getY()) > Utils.FLOAT_EPSILON) {
                     var19 = var18 + 1;
                  }

                  ++var17;
               }

               float var20;
               if (var15 == 0) {
                  var20 = 0.0F;
               } else {
                  var20 = var31[var15 - 1] * var3;
               }

               float var21;
               if (var18 <= 1) {
                  var21 = 0.0F;
               } else {
                  var21 = var14.getSliceSpace();
               }

               float var22 = var6[var15];
               float var23 = var14.getSelectionShift();
               float var24 = var8 + var23;
               var12.set(this.mChart.getCircleBox());
               var23 = -var23;
               var12.inset(var23, var23);
               boolean var33;
               if (var21 > 0.0F && var22 <= 180.0F) {
                  var33 = true;
               } else {
                  var33 = false;
               }

               this.mRenderPaint.setColor(var14.getColor(var15));
               float var25;
               if (var18 == 1) {
                  var25 = 0.0F;
               } else {
                  var25 = var21 / (0.017453292F * var8);
               }

               if (var18 == 1) {
                  var23 = 0.0F;
               } else {
                  var23 = var21 / (0.017453292F * var24);
               }

               float var26 = var5 + (var20 + var25 / 2.0F) * var4;
               var25 = (var22 - var25) * var4;
               var10 = false;
               if (var25 < 0.0F) {
                  var25 = 0.0F;
               }

               float var27 = (var20 + var23 / 2.0F) * var4 + var5;
               float var28 = (var22 - var23) * var4;
               var23 = var28;
               if (var28 < 0.0F) {
                  var23 = 0.0F;
               }

               this.mPathBuffer.reset();
               double var29;
               Path var32;
               if (var25 >= 360.0F && var25 % 360.0F <= Utils.FLOAT_EPSILON) {
                  this.mPathBuffer.addCircle(var7.x, var7.y, var24, Direction.CW);
               } else {
                  var32 = this.mPathBuffer;
                  var28 = var7.x;
                  var29 = (double)(var27 * 0.017453292F);
                  var32.moveTo(var28 + (float)Math.cos(var29) * var24, var7.y + var24 * (float)Math.sin(var29));
                  this.mPathBuffer.arcTo(var12, var27, var23);
               }

               if (var33) {
                  var23 = var7.x;
                  var29 = (double)(var26 * 0.017453292F);
                  var23 = this.calculateMinimumRadiusForSpacedSlice(var7, var8, var22 * var4, (float)Math.cos(var29) * var8 + var23, var7.y + (float)Math.sin(var29) * var8, var26, var25);
               } else {
                  var23 = 0.0F;
               }

               this.mInnerRectBuffer.set(var7.x - var11, var7.y - var11, var7.x + var11, var7.y + var11);
               if (var9 && (var11 > 0.0F || var33)) {
                  if (var33) {
                     var28 = var23;
                     if (var23 < 0.0F) {
                        var28 = -var23;
                     }

                     var23 = Math.max(var11, var28);
                  } else {
                     var23 = var11;
                  }

                  if (var18 != 1 && var23 != 0.0F) {
                     var21 /= 0.017453292F * var23;
                  } else {
                     var21 = 0.0F;
                  }

                  var26 = var21 / 2.0F;
                  var28 = (var22 - var21) * var4;
                  var21 = var28;
                  if (var28 < 0.0F) {
                     var21 = 0.0F;
                  }

                  var20 = var5 + (var20 + var26) * var4 + var21;
                  if (var25 >= 360.0F && var25 % 360.0F <= Utils.FLOAT_EPSILON) {
                     this.mPathBuffer.addCircle(var7.x, var7.y, var23, Direction.CCW);
                  } else {
                     var32 = this.mPathBuffer;
                     var25 = var7.x;
                     var29 = (double)(var20 * 0.017453292F);
                     var32.lineTo(var25 + (float)Math.cos(var29) * var23, var7.y + var23 * (float)Math.sin(var29));
                     this.mPathBuffer.arcTo(this.mInnerRectBuffer, var20, -var21);
                  }
               } else if (var25 % 360.0F > Utils.FLOAT_EPSILON) {
                  if (var33) {
                     var21 = var25 / 2.0F;
                     var20 = var7.x;
                     var29 = (double)((var26 + var21) * 0.017453292F);
                     var21 = (float)Math.cos(var29);
                     var25 = var7.y;
                     var28 = (float)Math.sin(var29);
                     this.mPathBuffer.lineTo(var20 + var21 * var23, var25 + var23 * var28);
                  } else {
                     this.mPathBuffer.lineTo(var7.x, var7.y);
                  }
               }

               this.mPathBuffer.close();
               this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
            }
         }
      }

      MPPointF.recycleInstance(var7);
   }

   protected void drawHole(Canvas var1) {
      if (this.mChart.isDrawHoleEnabled() && this.mBitmapCanvas != null) {
         float var2 = this.mChart.getRadius();
         float var3 = this.mChart.getHoleRadius() / 100.0F * var2;
         MPPointF var6 = this.mChart.getCenterCircleBox();
         if (Color.alpha(this.mHolePaint.getColor()) > 0) {
            this.mBitmapCanvas.drawCircle(var6.x, var6.y, var3, this.mHolePaint);
         }

         if (Color.alpha(this.mTransparentCirclePaint.getColor()) > 0 && this.mChart.getTransparentCircleRadius() > this.mChart.getHoleRadius()) {
            int var4 = this.mTransparentCirclePaint.getAlpha();
            float var5 = this.mChart.getTransparentCircleRadius() / 100.0F;
            this.mTransparentCirclePaint.setAlpha((int)((float)var4 * this.mAnimator.getPhaseX() * this.mAnimator.getPhaseY()));
            this.mHoleCirclePath.reset();
            this.mHoleCirclePath.addCircle(var6.x, var6.y, var2 * var5, Direction.CW);
            this.mHoleCirclePath.addCircle(var6.x, var6.y, var3, Direction.CCW);
            this.mBitmapCanvas.drawPath(this.mHoleCirclePath, this.mTransparentCirclePaint);
            this.mTransparentCirclePaint.setAlpha(var4);
         }

         MPPointF.recycleInstance(var6);
      }

   }

   protected void drawRoundedSlices(Canvas var1) {
      if (this.mChart.isDrawRoundedSlicesEnabled()) {
         IPieDataSet var2 = ((PieData)this.mChart.getData()).getDataSet();
         if (var2.isVisible()) {
            float var3 = this.mAnimator.getPhaseX();
            float var4 = this.mAnimator.getPhaseY();
            MPPointF var5 = this.mChart.getCenterCircleBox();
            float var6 = this.mChart.getRadius();
            float var7 = (var6 - this.mChart.getHoleRadius() * var6 / 100.0F) / 2.0F;
            float[] var17 = this.mChart.getDrawAngles();
            float var8 = this.mChart.getRotationAngle();

            for(int var9 = 0; var9 < var2.getEntryCount(); ++var9) {
               float var10 = var17[var9];
               if (Math.abs(var2.getEntryForIndex(var9).getY()) > Utils.FLOAT_EPSILON) {
                  double var11 = (double)(var6 - var7);
                  double var13 = (double)((var8 + var10) * var4);
                  float var15 = (float)(Math.cos(Math.toRadians(var13)) * var11 + (double)var5.x);
                  float var16 = (float)(var11 * Math.sin(Math.toRadians(var13)) + (double)var5.y);
                  this.mRenderPaint.setColor(var2.getColor(var9));
                  this.mBitmapCanvas.drawCircle(var15, var16, var7, this.mRenderPaint);
               }

               var8 += var10 * var3;
            }

            MPPointF.recycleInstance(var5);
         }
      }
   }

   public void drawValues(Canvas var1) {
      MPPointF var2 = this.mChart.getCenterCircleBox();
      float var3 = this.mChart.getRadius();
      float var4 = this.mChart.getRotationAngle();
      float[] var5 = this.mChart.getDrawAngles();
      float[] var6 = this.mChart.getAbsoluteAngles();
      float var7 = this.mAnimator.getPhaseX();
      float var8 = this.mAnimator.getPhaseY();
      float var9 = this.mChart.getHoleRadius() / 100.0F;
      float var10 = var3 / 10.0F * 3.6F;
      if (this.mChart.isDrawHoleEnabled()) {
         var10 = (var3 - var3 * var9) / 2.0F;
      }

      float var11 = var3 - var10;
      PieData var12 = (PieData)this.mChart.getData();
      List var13 = var12.getDataSets();
      float var14 = var12.getYValueSum();
      boolean var15 = this.mChart.isDrawEntryLabelsEnabled();
      var1.save();
      float var16 = Utils.convertDpToPixel(5.0F);
      int var17 = 0;
      int var18 = var17;

      for(var10 = var4; var18 < var13.size(); var10 = var4) {
         IPieDataSet var19 = (IPieDataSet)var13.get(var18);
         boolean var20 = var19.isDrawValuesEnabled();
         float[] var49;
         PieData var50;
         List var52;
         MPPointF var54;
         if (!var20 && !var15) {
            var54 = var2;
            var4 = var3;
            var49 = var5;
            var50 = var12;
            var52 = var13;
            var3 = var10;
            var10 = var4;
         } else {
            PieDataSet.ValuePosition var22 = var19.getXValuePosition();
            PieDataSet.ValuePosition var23 = var19.getYValuePosition();
            this.applyValueTextStyle(var19);
            float var24 = (float)Utils.calcTextHeight(this.mValuePaint, "Q") + Utils.convertDpToPixel(4.0F);
            IValueFormatter var25 = var19.getValueFormatter();
            int var26 = var19.getEntryCount();
            Paint var21 = this.mValueLinePaint;
            var21.setColor(var19.getValueLineColor());
            this.mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(var19.getValueLineWidth()));
            float var27 = this.getSliceSpace(var19);
            MPPointF var28 = MPPointF.getInstance(var19.getIconsOffset());
            var28.x = Utils.convertDpToPixel(var28.x);
            var28.y = Utils.convertDpToPixel(var28.y);
            byte var29 = 0;
            float[] var56 = var6;
            MPPointF var51 = var2;
            int var30 = var18;
            IPieDataSet var53 = var19;

            for(var18 = var29; var18 < var26; var5 = var5) {
               PieEntry var55 = (PieEntry)var53.getEntryForIndex(var18);
               if (var17 == 0) {
                  var4 = 0.0F;
               } else {
                  var4 = var56[var17 - 1] * var7;
               }

               float var31 = var10 + (var4 + (var5[var17] - var27 / (0.017453292F * var11) / 2.0F) / 2.0F) * var8;
               float var32;
               if (this.mChart.isUsePercentValuesEnabled()) {
                  var32 = var55.getY() / var14 * 100.0F;
               } else {
                  var32 = var55.getY();
               }

               double var33 = (double)(var31 * 0.017453292F);
               float var35 = (float)Math.cos(var33);
               float var36 = (float)Math.sin(var33);
               boolean var37;
               if (var15 && var22 == PieDataSet.ValuePosition.OUTSIDE_SLICE) {
                  var37 = true;
               } else {
                  var37 = false;
               }

               boolean var38;
               if (var20 && var23 == PieDataSet.ValuePosition.OUTSIDE_SLICE) {
                  var38 = true;
               } else {
                  var38 = false;
               }

               boolean var60;
               if (var15 && var22 == PieDataSet.ValuePosition.INSIDE_SLICE) {
                  var60 = true;
               } else {
                  var60 = false;
               }

               boolean var39;
               if (var20 && var23 == PieDataSet.ValuePosition.INSIDE_SLICE) {
                  var39 = true;
               } else {
                  var39 = false;
               }

               float var41;
               float var42;
               if (var37 || var38) {
                  float var40 = var53.getValueLinePart1Length();
                  var4 = var53.getValueLinePart2Length();
                  var41 = var53.getValueLinePart1OffsetPercentage() / 100.0F;
                  if (this.mChart.isDrawHoleEnabled()) {
                     var42 = var3 * var9;
                     var42 += (var3 - var42) * var41;
                  } else {
                     var42 = var3 * var41;
                  }

                  if (var53.isValueLineVariableLength()) {
                     var4 = var4 * var11 * (float)Math.abs(Math.sin(var33));
                  } else {
                     var4 *= var11;
                  }

                  var41 = var51.x;
                  float var43 = var51.y;
                  float var44 = (1.0F + var40) * var11;
                  var40 = var51.x + var44 * var35;
                  var44 = var44 * var36 + var51.y;
                  var33 = (double)var31 % 360.0D;
                  if (var33 >= 90.0D && var33 <= 270.0D) {
                     var4 = var40 - var4;
                     this.mValuePaint.setTextAlign(Align.RIGHT);
                     if (var37) {
                        this.mEntryLabelsPaint.setTextAlign(Align.RIGHT);
                     }

                     var31 = var4;
                     var4 -= var16;
                  } else {
                     var31 = var40 + var4;
                     this.mValuePaint.setTextAlign(Align.LEFT);
                     if (var37) {
                        this.mEntryLabelsPaint.setTextAlign(Align.LEFT);
                     }

                     var4 = var31 + var16;
                  }

                  if (var53.getValueLineColor() != 1122867) {
                     var1.drawLine(var42 * var35 + var41, var42 * var36 + var43, var40, var44, this.mValueLinePaint);
                     var1.drawLine(var40, var44, var31, var44, this.mValueLinePaint);
                  }

                  if (var37 && var38) {
                     int var61 = var53.getValueTextColor(var18);
                     this.drawValue(var1, var25, var32, var55, 0, var4, var44, var61);
                     if (var18 < var12.getEntryCount() && var55.getLabel() != null) {
                        this.drawEntryLabel(var1, var55.getLabel(), var4, var44 + var24);
                     }
                  } else if (var37) {
                     if (var18 < var12.getEntryCount() && var55.getLabel() != null) {
                        this.drawEntryLabel(var1, var55.getLabel(), var4, var44 + var24 / 2.0F);
                     }
                  } else if (var38) {
                     this.drawValue(var1, var25, var32, var55, 0, var4, var44 + var24 / 2.0F, var53.getValueTextColor(var18));
                  }
               }

               MPPointF var45 = var51;
               if (var60 || var39) {
                  var4 = var11 * var35 + var51.x;
                  var42 = var11 * var36 + var51.y;
                  this.mValuePaint.setTextAlign(Align.CENTER);
                  if (var60 && var39) {
                     this.drawValue(var1, var25, var32, var55, 0, var4, var42, var53.getValueTextColor(var18));
                     if (var18 < var12.getEntryCount() && var55.getLabel() != null) {
                        this.drawEntryLabel(var1, var55.getLabel(), var4, var42 + var24);
                     }
                  } else if (var60) {
                     if (var18 < var12.getEntryCount() && var55.getLabel() != null) {
                        this.drawEntryLabel(var1, var55.getLabel(), var4, var42 + var24 / 2.0F);
                     }
                  } else if (var39) {
                     this.drawValue(var1, var25, var32, var55, 0, var4, var42 + var24 / 2.0F, var53.getValueTextColor(var18));
                  }
               }

               if (var55.getIcon() != null && var53.isDrawIconsEnabled()) {
                  Drawable var47 = var55.getIcon();
                  var4 = var28.y;
                  var45 = var51;
                  var41 = var51.x;
                  var32 = var28.y;
                  var42 = var51.y;
                  var31 = var28.x;
                  Utils.drawImage(var1, var47, (int)((var11 + var4) * var35 + var41), (int)((var11 + var32) * var36 + var42 + var31), var47.getIntrinsicWidth(), var47.getIntrinsicHeight());
               }

               ++var17;
               ++var18;
               var12 = var12;
               var51 = var45;
            }

            var54 = var51;
            var4 = var3;
            var3 = var10;
            var49 = var5;
            var6 = var56;
            var50 = var12;
            MPPointF.recycleInstance(var28);
            var10 = var4;
            var52 = var13;
            var18 = var30;
         }

         ++var18;
         var4 = var3;
         var5 = var49;
         var2 = var54;
         var13 = var52;
         var12 = var50;
         var3 = var10;
      }

      MPPointF.recycleInstance(var2);
      var1.restore();
   }

   public TextPaint getPaintCenterText() {
      return this.mCenterTextPaint;
   }

   public Paint getPaintEntryLabels() {
      return this.mEntryLabelsPaint;
   }

   public Paint getPaintHole() {
      return this.mHolePaint;
   }

   public Paint getPaintTransparentCircle() {
      return this.mTransparentCirclePaint;
   }

   protected float getSliceSpace(IPieDataSet var1) {
      if (!var1.isAutomaticallyDisableSliceSpacingEnabled()) {
         return var1.getSliceSpace();
      } else {
         float var2;
         if (var1.getSliceSpace() / this.mViewPortHandler.getSmallestContentExtension() > var1.getYMin() / ((PieData)this.mChart.getData()).getYValueSum() * 2.0F) {
            var2 = 0.0F;
         } else {
            var2 = var1.getSliceSpace();
         }

         return var2;
      }
   }

   public void initBuffers() {
   }

   public void releaseBitmap() {
      if (this.mBitmapCanvas != null) {
         this.mBitmapCanvas.setBitmap((Bitmap)null);
         this.mBitmapCanvas = null;
      }

      if (this.mDrawBitmap != null) {
         ((Bitmap)this.mDrawBitmap.get()).recycle();
         this.mDrawBitmap.clear();
         this.mDrawBitmap = null;
      }

   }
}
