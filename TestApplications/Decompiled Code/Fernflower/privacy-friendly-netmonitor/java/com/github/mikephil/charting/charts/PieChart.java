package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.PieHighlighter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;

public class PieChart extends PieRadarChartBase {
   private float[] mAbsoluteAngles = new float[1];
   private CharSequence mCenterText = "";
   private MPPointF mCenterTextOffset = MPPointF.getInstance(0.0F, 0.0F);
   private float mCenterTextRadiusPercent = 100.0F;
   private RectF mCircleBox = new RectF();
   private float[] mDrawAngles = new float[1];
   private boolean mDrawCenterText = true;
   private boolean mDrawEntryLabels = true;
   private boolean mDrawHole = true;
   private boolean mDrawRoundedSlices = false;
   private boolean mDrawSlicesUnderHole = false;
   private float mHoleRadiusPercent = 50.0F;
   protected float mMaxAngle = 360.0F;
   protected float mTransparentCircleRadiusPercent = 55.0F;
   private boolean mUsePercentValues = false;

   public PieChart(Context var1) {
      super(var1);
   }

   public PieChart(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public PieChart(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   private float calcAngle(float var1) {
      return this.calcAngle(var1, ((PieData)this.mData).getYValueSum());
   }

   private float calcAngle(float var1, float var2) {
      return var1 / var2 * this.mMaxAngle;
   }

   private void calcAngles() {
      int var1 = ((PieData)this.mData).getEntryCount();
      int var2;
      if (this.mDrawAngles.length != var1) {
         this.mDrawAngles = new float[var1];
      } else {
         for(var2 = 0; var2 < var1; ++var2) {
            this.mDrawAngles[var2] = 0.0F;
         }
      }

      if (this.mAbsoluteAngles.length != var1) {
         this.mAbsoluteAngles = new float[var1];
      } else {
         for(var2 = 0; var2 < var1; ++var2) {
            this.mAbsoluteAngles[var2] = 0.0F;
         }
      }

      float var3 = ((PieData)this.mData).getYValueSum();
      List var4 = ((PieData)this.mData).getDataSets();
      var1 = 0;

      for(var2 = var1; var1 < ((PieData)this.mData).getDataSetCount(); ++var1) {
         IPieDataSet var5 = (IPieDataSet)var4.get(var1);

         for(int var6 = 0; var6 < var5.getEntryCount(); ++var6) {
            this.mDrawAngles[var2] = this.calcAngle(Math.abs(((PieEntry)var5.getEntryForIndex(var6)).getY()), var3);
            if (var2 == 0) {
               this.mAbsoluteAngles[var2] = this.mDrawAngles[var2];
            } else {
               this.mAbsoluteAngles[var2] = this.mAbsoluteAngles[var2 - 1] + this.mDrawAngles[var2];
            }

            ++var2;
         }
      }

   }

   protected void calcMinMax() {
      this.calcAngles();
   }

   public void calculateOffsets() {
      super.calculateOffsets();
      if (this.mData != null) {
         float var1 = this.getDiameter() / 2.0F;
         MPPointF var2 = this.getCenterOffsets();
         float var3 = ((PieData)this.mData).getDataSet().getSelectionShift();
         this.mCircleBox.set(var2.x - var1 + var3, var2.y - var1 + var3, var2.x + var1 - var3, var2.y + var1 - var3);
         MPPointF.recycleInstance(var2);
      }
   }

   public float[] getAbsoluteAngles() {
      return this.mAbsoluteAngles;
   }

   public MPPointF getCenterCircleBox() {
      return MPPointF.getInstance(this.mCircleBox.centerX(), this.mCircleBox.centerY());
   }

   public CharSequence getCenterText() {
      return this.mCenterText;
   }

   public MPPointF getCenterTextOffset() {
      return MPPointF.getInstance(this.mCenterTextOffset.x, this.mCenterTextOffset.y);
   }

   public float getCenterTextRadiusPercent() {
      return this.mCenterTextRadiusPercent;
   }

   public RectF getCircleBox() {
      return this.mCircleBox;
   }

   public int getDataSetIndexForIndex(int var1) {
      List var2 = ((PieData)this.mData).getDataSets();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         if (((IPieDataSet)var2.get(var3)).getEntryForXValue((float)var1, Float.NaN) != null) {
            return var3;
         }
      }

      return -1;
   }

   public float[] getDrawAngles() {
      return this.mDrawAngles;
   }

   public float getHoleRadius() {
      return this.mHoleRadiusPercent;
   }

   public int getIndexForAngle(float var1) {
      var1 = Utils.getNormalizedAngle(var1 - this.getRotationAngle());

      for(int var2 = 0; var2 < this.mAbsoluteAngles.length; ++var2) {
         if (this.mAbsoluteAngles[var2] > var1) {
            return var2;
         }
      }

      return -1;
   }

   protected float[] getMarkerPosition(Highlight var1) {
      MPPointF var2 = this.getCenterCircleBox();
      float var3 = this.getRadius();
      float var4 = var3 / 10.0F * 3.6F;
      if (this.isDrawHoleEnabled()) {
         var4 = (var3 - var3 / 100.0F * this.getHoleRadius()) / 2.0F;
      }

      float var5 = this.getRotationAngle();
      int var6 = (int)var1.getX();
      float var7 = this.mDrawAngles[var6] / 2.0F;
      double var8 = (double)(var3 - var4);
      var4 = (float)(Math.cos(Math.toRadians((double)((this.mAbsoluteAngles[var6] + var5 - var7) * this.mAnimator.getPhaseY()))) * var8 + (double)var2.x);
      var3 = (float)(var8 * Math.sin(Math.toRadians((double)((var5 + this.mAbsoluteAngles[var6] - var7) * this.mAnimator.getPhaseY()))) + (double)var2.y);
      MPPointF.recycleInstance(var2);
      return new float[]{var4, var3};
   }

   public float getMaxAngle() {
      return this.mMaxAngle;
   }

   public float getRadius() {
      return this.mCircleBox == null ? 0.0F : Math.min(this.mCircleBox.width() / 2.0F, this.mCircleBox.height() / 2.0F);
   }

   protected float getRequiredBaseOffset() {
      return 0.0F;
   }

   protected float getRequiredLegendOffset() {
      return this.mLegendRenderer.getLabelPaint().getTextSize() * 2.0F;
   }

   public float getTransparentCircleRadius() {
      return this.mTransparentCircleRadiusPercent;
   }

   @Deprecated
   public XAxis getXAxis() {
      throw new RuntimeException("PieChart has no XAxis");
   }

   protected void init() {
      super.init();
      this.mRenderer = new PieChartRenderer(this, this.mAnimator, this.mViewPortHandler);
      this.mXAxis = null;
      this.mHighlighter = new PieHighlighter(this);
   }

   public boolean isDrawCenterTextEnabled() {
      return this.mDrawCenterText;
   }

   public boolean isDrawEntryLabelsEnabled() {
      return this.mDrawEntryLabels;
   }

   public boolean isDrawHoleEnabled() {
      return this.mDrawHole;
   }

   public boolean isDrawRoundedSlicesEnabled() {
      return this.mDrawRoundedSlices;
   }

   public boolean isDrawSlicesUnderHoleEnabled() {
      return this.mDrawSlicesUnderHole;
   }

   public boolean isUsePercentValuesEnabled() {
      return this.mUsePercentValues;
   }

   public boolean needsHighlight(int var1) {
      if (!this.valuesToHighlight()) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.mIndicesToHighlight.length; ++var2) {
            if ((int)this.mIndicesToHighlight[var2].getX() == var1) {
               return true;
            }
         }

         return false;
      }
   }

   protected void onDetachedFromWindow() {
      if (this.mRenderer != null && this.mRenderer instanceof PieChartRenderer) {
         ((PieChartRenderer)this.mRenderer).releaseBitmap();
      }

      super.onDetachedFromWindow();
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mData != null) {
         this.mRenderer.drawData(var1);
         if (this.valuesToHighlight()) {
            this.mRenderer.drawHighlighted(var1, this.mIndicesToHighlight);
         }

         this.mRenderer.drawExtras(var1);
         this.mRenderer.drawValues(var1);
         this.mLegendRenderer.renderLegend(var1);
         this.drawDescription(var1);
         this.drawMarkers(var1);
      }
   }

   public void setCenterText(CharSequence var1) {
      if (var1 == null) {
         this.mCenterText = "";
      } else {
         this.mCenterText = var1;
      }

   }

   public void setCenterTextColor(int var1) {
      ((PieChartRenderer)this.mRenderer).getPaintCenterText().setColor(var1);
   }

   public void setCenterTextOffset(float var1, float var2) {
      this.mCenterTextOffset.x = Utils.convertDpToPixel(var1);
      this.mCenterTextOffset.y = Utils.convertDpToPixel(var2);
   }

   public void setCenterTextRadiusPercent(float var1) {
      this.mCenterTextRadiusPercent = var1;
   }

   public void setCenterTextSize(float var1) {
      ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTextSize(Utils.convertDpToPixel(var1));
   }

   public void setCenterTextSizePixels(float var1) {
      ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTextSize(var1);
   }

   public void setCenterTextTypeface(Typeface var1) {
      ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTypeface(var1);
   }

   public void setDrawCenterText(boolean var1) {
      this.mDrawCenterText = var1;
   }

   public void setDrawEntryLabels(boolean var1) {
      this.mDrawEntryLabels = var1;
   }

   public void setDrawHoleEnabled(boolean var1) {
      this.mDrawHole = var1;
   }

   @Deprecated
   public void setDrawSliceText(boolean var1) {
      this.mDrawEntryLabels = var1;
   }

   public void setDrawSlicesUnderHole(boolean var1) {
      this.mDrawSlicesUnderHole = var1;
   }

   public void setEntryLabelColor(int var1) {
      ((PieChartRenderer)this.mRenderer).getPaintEntryLabels().setColor(var1);
   }

   public void setEntryLabelTextSize(float var1) {
      ((PieChartRenderer)this.mRenderer).getPaintEntryLabels().setTextSize(Utils.convertDpToPixel(var1));
   }

   public void setEntryLabelTypeface(Typeface var1) {
      ((PieChartRenderer)this.mRenderer).getPaintEntryLabels().setTypeface(var1);
   }

   public void setHoleColor(int var1) {
      ((PieChartRenderer)this.mRenderer).getPaintHole().setColor(var1);
   }

   public void setHoleRadius(float var1) {
      this.mHoleRadiusPercent = var1;
   }

   public void setMaxAngle(float var1) {
      float var2 = var1;
      if (var1 > 360.0F) {
         var2 = 360.0F;
      }

      var1 = var2;
      if (var2 < 90.0F) {
         var1 = 90.0F;
      }

      this.mMaxAngle = var1;
   }

   public void setTransparentCircleAlpha(int var1) {
      ((PieChartRenderer)this.mRenderer).getPaintTransparentCircle().setAlpha(var1);
   }

   public void setTransparentCircleColor(int var1) {
      Paint var2 = ((PieChartRenderer)this.mRenderer).getPaintTransparentCircle();
      int var3 = var2.getAlpha();
      var2.setColor(var1);
      var2.setAlpha(var3);
   }

   public void setTransparentCircleRadius(float var1) {
      this.mTransparentCircleRadiusPercent = var1;
   }

   public void setUsePercentValues(boolean var1) {
      this.mUsePercentValues = var1;
   }
}
