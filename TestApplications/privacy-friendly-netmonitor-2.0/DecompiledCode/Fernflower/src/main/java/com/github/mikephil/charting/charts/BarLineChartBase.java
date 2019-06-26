package com.github.mikephil.charting.charts;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.jobs.AnimatedMoveViewJob;
import com.github.mikephil.charting.jobs.AnimatedZoomJob;
import com.github.mikephil.charting.jobs.MoveViewJob;
import com.github.mikephil.charting.jobs.ZoomJob;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import com.github.mikephil.charting.listener.OnDrawListener;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;

@SuppressLint({"RtlHardcoded"})
public abstract class BarLineChartBase extends Chart implements BarLineScatterCandleBubbleDataProvider {
   private long drawCycles = 0L;
   protected boolean mAutoScaleMinMaxEnabled = false;
   protected YAxis mAxisLeft;
   protected YAxisRenderer mAxisRendererLeft;
   protected YAxisRenderer mAxisRendererRight;
   protected YAxis mAxisRight;
   protected Paint mBorderPaint;
   protected boolean mClipValuesToContent = false;
   private boolean mCustomViewPortEnabled = false;
   protected boolean mDoubleTapToZoomEnabled = true;
   private boolean mDragXEnabled = true;
   private boolean mDragYEnabled = true;
   protected boolean mDrawBorders = false;
   protected boolean mDrawGridBackground = false;
   protected OnDrawListener mDrawListener;
   protected Matrix mFitScreenMatrixBuffer = new Matrix();
   protected float[] mGetPositionBuffer = new float[2];
   protected Paint mGridBackgroundPaint;
   protected boolean mHighlightPerDragEnabled = true;
   protected boolean mKeepPositionOnRotation = false;
   protected Transformer mLeftAxisTransformer;
   protected int mMaxVisibleCount = 100;
   protected float mMinOffset = 15.0F;
   private RectF mOffsetsBuffer = new RectF();
   protected float[] mOnSizeChangedBuffer = new float[2];
   protected boolean mPinchZoomEnabled = false;
   protected Transformer mRightAxisTransformer;
   private boolean mScaleXEnabled = true;
   private boolean mScaleYEnabled = true;
   protected XAxisRenderer mXAxisRenderer;
   protected Matrix mZoomMatrixBuffer = new Matrix();
   protected MPPointD posForGetHighestVisibleX = MPPointD.getInstance(0.0D, 0.0D);
   protected MPPointD posForGetLowestVisibleX = MPPointD.getInstance(0.0D, 0.0D);
   private long totalTime = 0L;

   public BarLineChartBase(Context var1) {
      super(var1);
   }

   public BarLineChartBase(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public BarLineChartBase(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void autoScale() {
      float var1 = this.getLowestVisibleX();
      float var2 = this.getHighestVisibleX();
      ((BarLineScatterCandleBubbleData)this.mData).calcMinMaxY(var1, var2);
      this.mXAxis.calculate(((BarLineScatterCandleBubbleData)this.mData).getXMin(), ((BarLineScatterCandleBubbleData)this.mData).getXMax());
      if (this.mAxisLeft.isEnabled()) {
         this.mAxisLeft.calculate(((BarLineScatterCandleBubbleData)this.mData).getYMin(YAxis.AxisDependency.LEFT), ((BarLineScatterCandleBubbleData)this.mData).getYMax(YAxis.AxisDependency.LEFT));
      }

      if (this.mAxisRight.isEnabled()) {
         this.mAxisRight.calculate(((BarLineScatterCandleBubbleData)this.mData).getYMin(YAxis.AxisDependency.RIGHT), ((BarLineScatterCandleBubbleData)this.mData).getYMax(YAxis.AxisDependency.RIGHT));
      }

      this.calculateOffsets();
   }

   protected void calcMinMax() {
      this.mXAxis.calculate(((BarLineScatterCandleBubbleData)this.mData).getXMin(), ((BarLineScatterCandleBubbleData)this.mData).getXMax());
      this.mAxisLeft.calculate(((BarLineScatterCandleBubbleData)this.mData).getYMin(YAxis.AxisDependency.LEFT), ((BarLineScatterCandleBubbleData)this.mData).getYMax(YAxis.AxisDependency.LEFT));
      this.mAxisRight.calculate(((BarLineScatterCandleBubbleData)this.mData).getYMin(YAxis.AxisDependency.RIGHT), ((BarLineScatterCandleBubbleData)this.mData).getYMax(YAxis.AxisDependency.RIGHT));
   }

   protected void calculateLegendOffsets(RectF var1) {
      var1.left = 0.0F;
      var1.right = 0.0F;
      var1.top = 0.0F;
      var1.bottom = 0.0F;
      if (this.mLegend != null && this.mLegend.isEnabled() && !this.mLegend.isDrawInsideEnabled()) {
         switch(this.mLegend.getOrientation()) {
         case VERTICAL:
            switch(this.mLegend.getHorizontalAlignment()) {
            case LEFT:
               var1.left += Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getXOffset();
               return;
            case RIGHT:
               var1.right += Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getXOffset();
               return;
            case CENTER:
               switch(this.mLegend.getVerticalAlignment()) {
               case TOP:
                  var1.top += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
                  return;
               case BOTTOM:
                  var1.bottom += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
                  return;
               }
            default:
               return;
            }
         case HORIZONTAL:
            switch(this.mLegend.getVerticalAlignment()) {
            case TOP:
               var1.top += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
               if (this.getXAxis().isEnabled() && this.getXAxis().isDrawLabelsEnabled()) {
                  var1.top += (float)this.getXAxis().mLabelRotatedHeight;
               }
               break;
            case BOTTOM:
               var1.bottom += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
               if (this.getXAxis().isEnabled() && this.getXAxis().isDrawLabelsEnabled()) {
                  var1.bottom += (float)this.getXAxis().mLabelRotatedHeight;
               }
            }
         }
      }

   }

   public void calculateOffsets() {
      if (!this.mCustomViewPortEnabled) {
         this.calculateLegendOffsets(this.mOffsetsBuffer);
         float var1 = this.mOffsetsBuffer.left + 0.0F;
         float var2 = this.mOffsetsBuffer.top + 0.0F;
         float var3 = this.mOffsetsBuffer.right + 0.0F;
         float var4 = 0.0F + this.mOffsetsBuffer.bottom;
         float var5 = var1;
         if (this.mAxisLeft.needsOffset()) {
            var5 = var1 + this.mAxisLeft.getRequiredWidthSpace(this.mAxisRendererLeft.getPaintAxisLabels());
         }

         float var6 = var3;
         if (this.mAxisRight.needsOffset()) {
            var6 = var3 + this.mAxisRight.getRequiredWidthSpace(this.mAxisRendererRight.getPaintAxisLabels());
         }

         var3 = var4;
         var1 = var2;
         if (this.mXAxis.isEnabled()) {
            var3 = var4;
            var1 = var2;
            if (this.mXAxis.isDrawLabelsEnabled()) {
               float var7 = (float)this.mXAxis.mLabelRotatedHeight + this.mXAxis.getYOffset();
               if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                  var3 = var4 + var7;
                  var1 = var2;
               } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                  var1 = var2 + var7;
                  var3 = var4;
               } else {
                  var3 = var4;
                  var1 = var2;
                  if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                     var3 = var4 + var7;
                     var1 = var2 + var7;
                  }
               }
            }
         }

         var1 += this.getExtraTopOffset();
         var6 += this.getExtraRightOffset();
         var3 += this.getExtraBottomOffset();
         var5 += this.getExtraLeftOffset();
         var2 = Utils.convertDpToPixel(this.mMinOffset);
         this.mViewPortHandler.restrainViewPort(Math.max(var2, var5), Math.max(var2, var1), Math.max(var2, var6), Math.max(var2, var3));
         if (this.mLogEnabled) {
            StringBuilder var8 = new StringBuilder();
            var8.append("offsetLeft: ");
            var8.append(var5);
            var8.append(", offsetTop: ");
            var8.append(var1);
            var8.append(", offsetRight: ");
            var8.append(var6);
            var8.append(", offsetBottom: ");
            var8.append(var3);
            Log.i("MPAndroidChart", var8.toString());
            var8 = new StringBuilder();
            var8.append("Content: ");
            var8.append(this.mViewPortHandler.getContentRect().toString());
            Log.i("MPAndroidChart", var8.toString());
         }
      }

      this.prepareOffsetMatrix();
      this.prepareValuePxMatrix();
   }

   public void centerViewTo(float var1, float var2, YAxis.AxisDependency var3) {
      float var4 = this.getAxisRange(var3) / this.mViewPortHandler.getScaleY();
      float var5 = this.getXAxis().mAxisRange / this.mViewPortHandler.getScaleX();
      this.addViewportJob(MoveViewJob.getInstance(this.mViewPortHandler, var1 - var5 / 2.0F, var2 + var4 / 2.0F, this.getTransformer(var3), this));
   }

   @TargetApi(11)
   public void centerViewToAnimated(float var1, float var2, YAxis.AxisDependency var3, long var4) {
      if (VERSION.SDK_INT >= 11) {
         MPPointD var6 = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), var3);
         float var7 = this.getAxisRange(var3) / this.mViewPortHandler.getScaleY();
         float var8 = this.getXAxis().mAxisRange / this.mViewPortHandler.getScaleX();
         this.addViewportJob(AnimatedMoveViewJob.getInstance(this.mViewPortHandler, var1 - var8 / 2.0F, var2 + var7 / 2.0F, this.getTransformer(var3), this, (float)var6.x, (float)var6.y, var4));
         MPPointD.recycleInstance(var6);
      } else {
         Log.e("MPAndroidChart", "Unable to execute centerViewToAnimated(...) on API level < 11");
      }

   }

   public void centerViewToY(float var1, YAxis.AxisDependency var2) {
      float var3 = this.getAxisRange(var2) / this.mViewPortHandler.getScaleY();
      this.addViewportJob(MoveViewJob.getInstance(this.mViewPortHandler, 0.0F, var1 + var3 / 2.0F, this.getTransformer(var2), this));
   }

   public void computeScroll() {
      if (this.mChartTouchListener instanceof BarLineChartTouchListener) {
         ((BarLineChartTouchListener)this.mChartTouchListener).computeScroll();
      }

   }

   protected void drawGridBackground(Canvas var1) {
      if (this.mDrawGridBackground) {
         var1.drawRect(this.mViewPortHandler.getContentRect(), this.mGridBackgroundPaint);
      }

      if (this.mDrawBorders) {
         var1.drawRect(this.mViewPortHandler.getContentRect(), this.mBorderPaint);
      }

   }

   public void fitScreen() {
      Matrix var1 = this.mFitScreenMatrixBuffer;
      this.mViewPortHandler.fitScreen(var1);
      this.mViewPortHandler.refresh(var1, this, false);
      this.calculateOffsets();
      this.postInvalidate();
   }

   public YAxis getAxis(YAxis.AxisDependency var1) {
      return var1 == YAxis.AxisDependency.LEFT ? this.mAxisLeft : this.mAxisRight;
   }

   public YAxis getAxisLeft() {
      return this.mAxisLeft;
   }

   protected float getAxisRange(YAxis.AxisDependency var1) {
      return var1 == YAxis.AxisDependency.LEFT ? this.mAxisLeft.mAxisRange : this.mAxisRight.mAxisRange;
   }

   public YAxis getAxisRight() {
      return this.mAxisRight;
   }

   public IBarLineScatterCandleBubbleDataSet getDataSetByTouchPoint(float var1, float var2) {
      Highlight var3 = this.getHighlightByTouchPoint(var1, var2);
      return var3 != null ? (IBarLineScatterCandleBubbleDataSet)((BarLineScatterCandleBubbleData)this.mData).getDataSetByIndex(var3.getDataSetIndex()) : null;
   }

   public OnDrawListener getDrawListener() {
      return this.mDrawListener;
   }

   public Entry getEntryByTouchPoint(float var1, float var2) {
      Highlight var3 = this.getHighlightByTouchPoint(var1, var2);
      return var3 != null ? ((BarLineScatterCandleBubbleData)this.mData).getEntryForHighlight(var3) : null;
   }

   public float getHighestVisibleX() {
      this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.posForGetHighestVisibleX);
      return (float)Math.min((double)this.mXAxis.mAxisMaximum, this.posForGetHighestVisibleX.x);
   }

   public float getLowestVisibleX() {
      this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.posForGetLowestVisibleX);
      return (float)Math.max((double)this.mXAxis.mAxisMinimum, this.posForGetLowestVisibleX.x);
   }

   public int getMaxVisibleCount() {
      return this.mMaxVisibleCount;
   }

   public float getMinOffset() {
      return this.mMinOffset;
   }

   public Paint getPaint(int var1) {
      Paint var2 = super.getPaint(var1);
      if (var2 != null) {
         return var2;
      } else {
         return var1 != 4 ? null : this.mGridBackgroundPaint;
      }
   }

   public MPPointD getPixelForValues(float var1, float var2, YAxis.AxisDependency var3) {
      return this.getTransformer(var3).getPixelForValues(var1, var2);
   }

   public MPPointF getPosition(Entry var1, YAxis.AxisDependency var2) {
      if (var1 == null) {
         return null;
      } else {
         this.mGetPositionBuffer[0] = var1.getX();
         this.mGetPositionBuffer[1] = var1.getY();
         this.getTransformer(var2).pointValuesToPixel(this.mGetPositionBuffer);
         return MPPointF.getInstance(this.mGetPositionBuffer[0], this.mGetPositionBuffer[1]);
      }
   }

   public YAxisRenderer getRendererLeftYAxis() {
      return this.mAxisRendererLeft;
   }

   public YAxisRenderer getRendererRightYAxis() {
      return this.mAxisRendererRight;
   }

   public XAxisRenderer getRendererXAxis() {
      return this.mXAxisRenderer;
   }

   public float getScaleX() {
      return this.mViewPortHandler == null ? 1.0F : this.mViewPortHandler.getScaleX();
   }

   public float getScaleY() {
      return this.mViewPortHandler == null ? 1.0F : this.mViewPortHandler.getScaleY();
   }

   public Transformer getTransformer(YAxis.AxisDependency var1) {
      return var1 == YAxis.AxisDependency.LEFT ? this.mLeftAxisTransformer : this.mRightAxisTransformer;
   }

   public MPPointD getValuesByTouchPoint(float var1, float var2, YAxis.AxisDependency var3) {
      MPPointD var4 = MPPointD.getInstance(0.0D, 0.0D);
      this.getValuesByTouchPoint(var1, var2, var3, var4);
      return var4;
   }

   public void getValuesByTouchPoint(float var1, float var2, YAxis.AxisDependency var3, MPPointD var4) {
      this.getTransformer(var3).getValuesByTouchPoint(var1, var2, var4);
   }

   public float getVisibleXRange() {
      return Math.abs(this.getHighestVisibleX() - this.getLowestVisibleX());
   }

   public float getYChartMax() {
      return Math.max(this.mAxisLeft.mAxisMaximum, this.mAxisRight.mAxisMaximum);
   }

   public float getYChartMin() {
      return Math.min(this.mAxisLeft.mAxisMinimum, this.mAxisRight.mAxisMinimum);
   }

   public boolean hasNoDragOffset() {
      return this.mViewPortHandler.hasNoDragOffset();
   }

   protected void init() {
      super.init();
      this.mAxisLeft = new YAxis(YAxis.AxisDependency.LEFT);
      this.mAxisRight = new YAxis(YAxis.AxisDependency.RIGHT);
      this.mLeftAxisTransformer = new Transformer(this.mViewPortHandler);
      this.mRightAxisTransformer = new Transformer(this.mViewPortHandler);
      this.mAxisRendererLeft = new YAxisRenderer(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
      this.mAxisRendererRight = new YAxisRenderer(this.mViewPortHandler, this.mAxisRight, this.mRightAxisTransformer);
      this.mXAxisRenderer = new XAxisRenderer(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer);
      this.setHighlighter(new ChartHighlighter(this));
      this.mChartTouchListener = new BarLineChartTouchListener(this, this.mViewPortHandler.getMatrixTouch(), 3.0F);
      this.mGridBackgroundPaint = new Paint();
      this.mGridBackgroundPaint.setStyle(Style.FILL);
      this.mGridBackgroundPaint.setColor(Color.rgb(240, 240, 240));
      this.mBorderPaint = new Paint();
      this.mBorderPaint.setStyle(Style.STROKE);
      this.mBorderPaint.setColor(-16777216);
      this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0F));
   }

   public boolean isAnyAxisInverted() {
      if (this.mAxisLeft.isInverted()) {
         return true;
      } else {
         return this.mAxisRight.isInverted();
      }
   }

   public boolean isAutoScaleMinMaxEnabled() {
      return this.mAutoScaleMinMaxEnabled;
   }

   public boolean isClipValuesToContentEnabled() {
      return this.mClipValuesToContent;
   }

   public boolean isDoubleTapToZoomEnabled() {
      return this.mDoubleTapToZoomEnabled;
   }

   public boolean isDragEnabled() {
      boolean var1;
      if (!this.mDragXEnabled && !this.mDragYEnabled) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isDragXEnabled() {
      return this.mDragXEnabled;
   }

   public boolean isDragYEnabled() {
      return this.mDragYEnabled;
   }

   public boolean isDrawBordersEnabled() {
      return this.mDrawBorders;
   }

   public boolean isFullyZoomedOut() {
      return this.mViewPortHandler.isFullyZoomedOut();
   }

   public boolean isHighlightPerDragEnabled() {
      return this.mHighlightPerDragEnabled;
   }

   public boolean isInverted(YAxis.AxisDependency var1) {
      return this.getAxis(var1).isInverted();
   }

   public boolean isKeepPositionOnRotation() {
      return this.mKeepPositionOnRotation;
   }

   public boolean isPinchZoomEnabled() {
      return this.mPinchZoomEnabled;
   }

   public boolean isScaleXEnabled() {
      return this.mScaleXEnabled;
   }

   public boolean isScaleYEnabled() {
      return this.mScaleYEnabled;
   }

   public void moveViewTo(float var1, float var2, YAxis.AxisDependency var3) {
      float var4 = this.getAxisRange(var3) / this.mViewPortHandler.getScaleY();
      this.addViewportJob(MoveViewJob.getInstance(this.mViewPortHandler, var1, var2 + var4 / 2.0F, this.getTransformer(var3), this));
   }

   @TargetApi(11)
   public void moveViewToAnimated(float var1, float var2, YAxis.AxisDependency var3, long var4) {
      if (VERSION.SDK_INT >= 11) {
         MPPointD var6 = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), var3);
         float var7 = this.getAxisRange(var3) / this.mViewPortHandler.getScaleY();
         this.addViewportJob(AnimatedMoveViewJob.getInstance(this.mViewPortHandler, var1, var2 + var7 / 2.0F, this.getTransformer(var3), this, (float)var6.x, (float)var6.y, var4));
         MPPointD.recycleInstance(var6);
      } else {
         Log.e("MPAndroidChart", "Unable to execute moveViewToAnimated(...) on API level < 11");
      }

   }

   public void moveViewToX(float var1) {
      this.addViewportJob(MoveViewJob.getInstance(this.mViewPortHandler, var1, 0.0F, this.getTransformer(YAxis.AxisDependency.LEFT), this));
   }

   public void notifyDataSetChanged() {
      if (this.mData == null) {
         if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "Preparing... DATA NOT SET.");
         }

      } else {
         if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "Preparing...");
         }

         if (this.mRenderer != null) {
            this.mRenderer.initBuffers();
         }

         this.calcMinMax();
         this.mAxisRendererLeft.computeAxis(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisMaximum, this.mAxisLeft.isInverted());
         this.mAxisRendererRight.computeAxis(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisMaximum, this.mAxisRight.isInverted());
         this.mXAxisRenderer.computeAxis(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisMaximum, false);
         if (this.mLegend != null) {
            this.mLegendRenderer.computeLegend(this.mData);
         }

         this.calculateOffsets();
      }
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mData != null) {
         long var2 = System.currentTimeMillis();
         this.drawGridBackground(var1);
         if (this.mAutoScaleMinMaxEnabled) {
            this.autoScale();
         }

         if (this.mAxisLeft.isEnabled()) {
            this.mAxisRendererLeft.computeAxis(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisMaximum, this.mAxisLeft.isInverted());
         }

         if (this.mAxisRight.isEnabled()) {
            this.mAxisRendererRight.computeAxis(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisMaximum, this.mAxisRight.isInverted());
         }

         if (this.mXAxis.isEnabled()) {
            this.mXAxisRenderer.computeAxis(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisMaximum, false);
         }

         this.mXAxisRenderer.renderAxisLine(var1);
         this.mAxisRendererLeft.renderAxisLine(var1);
         this.mAxisRendererRight.renderAxisLine(var1);
         this.mXAxisRenderer.renderGridLines(var1);
         this.mAxisRendererLeft.renderGridLines(var1);
         this.mAxisRendererRight.renderGridLines(var1);
         if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLimitLinesBehindDataEnabled()) {
            this.mXAxisRenderer.renderLimitLines(var1);
         }

         if (this.mAxisLeft.isEnabled() && this.mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererLeft.renderLimitLines(var1);
         }

         if (this.mAxisRight.isEnabled() && this.mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererRight.renderLimitLines(var1);
         }

         int var4 = var1.save();
         var1.clipRect(this.mViewPortHandler.getContentRect());
         this.mRenderer.drawData(var1);
         if (this.valuesToHighlight()) {
            this.mRenderer.drawHighlighted(var1, this.mIndicesToHighlight);
         }

         var1.restoreToCount(var4);
         this.mRenderer.drawExtras(var1);
         if (this.mXAxis.isEnabled() && !this.mXAxis.isDrawLimitLinesBehindDataEnabled()) {
            this.mXAxisRenderer.renderLimitLines(var1);
         }

         if (this.mAxisLeft.isEnabled() && !this.mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererLeft.renderLimitLines(var1);
         }

         if (this.mAxisRight.isEnabled() && !this.mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererRight.renderLimitLines(var1);
         }

         this.mXAxisRenderer.renderAxisLabels(var1);
         this.mAxisRendererLeft.renderAxisLabels(var1);
         this.mAxisRendererRight.renderAxisLabels(var1);
         if (this.isClipValuesToContentEnabled()) {
            var4 = var1.save();
            var1.clipRect(this.mViewPortHandler.getContentRect());
            this.mRenderer.drawValues(var1);
            var1.restoreToCount(var4);
         } else {
            this.mRenderer.drawValues(var1);
         }

         this.mLegendRenderer.renderLegend(var1);
         this.drawDescription(var1);
         this.drawMarkers(var1);
         if (this.mLogEnabled) {
            var2 = System.currentTimeMillis() - var2;
            this.totalTime += var2;
            ++this.drawCycles;
            long var5 = this.totalTime / this.drawCycles;
            StringBuilder var7 = new StringBuilder();
            var7.append("Drawtime: ");
            var7.append(var2);
            var7.append(" ms, average: ");
            var7.append(var5);
            var7.append(" ms, cycles: ");
            var7.append(this.drawCycles);
            Log.i("MPAndroidChart", var7.toString());
         }

      }
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      float[] var5 = this.mOnSizeChangedBuffer;
      this.mOnSizeChangedBuffer[1] = 0.0F;
      var5[0] = 0.0F;
      if (this.mKeepPositionOnRotation) {
         this.mOnSizeChangedBuffer[0] = this.mViewPortHandler.contentLeft();
         this.mOnSizeChangedBuffer[1] = this.mViewPortHandler.contentTop();
         this.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(this.mOnSizeChangedBuffer);
      }

      super.onSizeChanged(var1, var2, var3, var4);
      if (this.mKeepPositionOnRotation) {
         this.getTransformer(YAxis.AxisDependency.LEFT).pointValuesToPixel(this.mOnSizeChangedBuffer);
         this.mViewPortHandler.centerViewPort(this.mOnSizeChangedBuffer, this);
      } else {
         this.mViewPortHandler.refresh(this.mViewPortHandler.getMatrixTouch(), this, true);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      super.onTouchEvent(var1);
      if (this.mChartTouchListener != null && this.mData != null) {
         return !this.mTouchEnabled ? false : this.mChartTouchListener.onTouch(this, var1);
      } else {
         return false;
      }
   }

   protected void prepareOffsetMatrix() {
      this.mRightAxisTransformer.prepareMatrixOffset(this.mAxisRight.isInverted());
      this.mLeftAxisTransformer.prepareMatrixOffset(this.mAxisLeft.isInverted());
   }

   protected void prepareValuePxMatrix() {
      if (this.mLogEnabled) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Preparing Value-Px Matrix, xmin: ");
         var1.append(this.mXAxis.mAxisMinimum);
         var1.append(", xmax: ");
         var1.append(this.mXAxis.mAxisMaximum);
         var1.append(", xdelta: ");
         var1.append(this.mXAxis.mAxisRange);
         Log.i("MPAndroidChart", var1.toString());
      }

      this.mRightAxisTransformer.prepareMatrixValuePx(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisRange, this.mAxisRight.mAxisRange, this.mAxisRight.mAxisMinimum);
      this.mLeftAxisTransformer.prepareMatrixValuePx(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisRange, this.mAxisLeft.mAxisRange, this.mAxisLeft.mAxisMinimum);
   }

   public void resetTracking() {
      this.totalTime = 0L;
      this.drawCycles = 0L;
   }

   public void resetViewPortOffsets() {
      this.mCustomViewPortEnabled = false;
      this.calculateOffsets();
   }

   public void resetZoom() {
      this.mViewPortHandler.resetZoom(this.mZoomMatrixBuffer);
      this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, this, false);
      this.calculateOffsets();
      this.postInvalidate();
   }

   public void setAutoScaleMinMaxEnabled(boolean var1) {
      this.mAutoScaleMinMaxEnabled = var1;
   }

   public void setBorderColor(int var1) {
      this.mBorderPaint.setColor(var1);
   }

   public void setBorderWidth(float var1) {
      this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(var1));
   }

   public void setClipValuesToContent(boolean var1) {
      this.mClipValuesToContent = var1;
   }

   public void setDoubleTapToZoomEnabled(boolean var1) {
      this.mDoubleTapToZoomEnabled = var1;
   }

   public void setDragEnabled(boolean var1) {
      this.mDragXEnabled = var1;
      this.mDragYEnabled = var1;
   }

   public void setDragOffsetX(float var1) {
      this.mViewPortHandler.setDragOffsetX(var1);
   }

   public void setDragOffsetY(float var1) {
      this.mViewPortHandler.setDragOffsetY(var1);
   }

   public void setDragXEnabled(boolean var1) {
      this.mDragXEnabled = var1;
   }

   public void setDragYEnabled(boolean var1) {
      this.mDragYEnabled = var1;
   }

   public void setDrawBorders(boolean var1) {
      this.mDrawBorders = var1;
   }

   public void setDrawGridBackground(boolean var1) {
      this.mDrawGridBackground = var1;
   }

   public void setGridBackgroundColor(int var1) {
      this.mGridBackgroundPaint.setColor(var1);
   }

   public void setHighlightPerDragEnabled(boolean var1) {
      this.mHighlightPerDragEnabled = var1;
   }

   public void setKeepPositionOnRotation(boolean var1) {
      this.mKeepPositionOnRotation = var1;
   }

   public void setMaxVisibleValueCount(int var1) {
      this.mMaxVisibleCount = var1;
   }

   public void setMinOffset(float var1) {
      this.mMinOffset = var1;
   }

   public void setOnDrawListener(OnDrawListener var1) {
      this.mDrawListener = var1;
   }

   public void setPaint(Paint var1, int var2) {
      super.setPaint(var1, var2);
      if (var2 == 4) {
         this.mGridBackgroundPaint = var1;
      }

   }

   public void setPinchZoom(boolean var1) {
      this.mPinchZoomEnabled = var1;
   }

   public void setRendererLeftYAxis(YAxisRenderer var1) {
      this.mAxisRendererLeft = var1;
   }

   public void setRendererRightYAxis(YAxisRenderer var1) {
      this.mAxisRendererRight = var1;
   }

   public void setScaleEnabled(boolean var1) {
      this.mScaleXEnabled = var1;
      this.mScaleYEnabled = var1;
   }

   public void setScaleMinima(float var1, float var2) {
      this.mViewPortHandler.setMinimumScaleX(var1);
      this.mViewPortHandler.setMinimumScaleY(var2);
   }

   public void setScaleXEnabled(boolean var1) {
      this.mScaleXEnabled = var1;
   }

   public void setScaleYEnabled(boolean var1) {
      this.mScaleYEnabled = var1;
   }

   public void setViewPortOffsets(final float var1, final float var2, final float var3, final float var4) {
      this.mCustomViewPortEnabled = true;
      this.post(new Runnable() {
         public void run() {
            BarLineChartBase.this.mViewPortHandler.restrainViewPort(var1, var2, var3, var4);
            BarLineChartBase.this.prepareOffsetMatrix();
            BarLineChartBase.this.prepareValuePxMatrix();
         }
      });
   }

   public void setVisibleXRange(float var1, float var2) {
      var1 = this.mXAxis.mAxisRange / var1;
      var2 = this.mXAxis.mAxisRange / var2;
      this.mViewPortHandler.setMinMaxScaleX(var1, var2);
   }

   public void setVisibleXRangeMaximum(float var1) {
      var1 = this.mXAxis.mAxisRange / var1;
      this.mViewPortHandler.setMinimumScaleX(var1);
   }

   public void setVisibleXRangeMinimum(float var1) {
      var1 = this.mXAxis.mAxisRange / var1;
      this.mViewPortHandler.setMaximumScaleX(var1);
   }

   public void setVisibleYRange(float var1, float var2, YAxis.AxisDependency var3) {
      var1 = this.getAxisRange(var3) / var1;
      var2 = this.getAxisRange(var3) / var2;
      this.mViewPortHandler.setMinMaxScaleY(var1, var2);
   }

   public void setVisibleYRangeMaximum(float var1, YAxis.AxisDependency var2) {
      var1 = this.getAxisRange(var2) / var1;
      this.mViewPortHandler.setMinimumScaleY(var1);
   }

   public void setVisibleYRangeMinimum(float var1, YAxis.AxisDependency var2) {
      var1 = this.getAxisRange(var2) / var1;
      this.mViewPortHandler.setMaximumScaleY(var1);
   }

   public void setXAxisRenderer(XAxisRenderer var1) {
      this.mXAxisRenderer = var1;
   }

   public void zoom(float var1, float var2, float var3, float var4) {
      this.mViewPortHandler.zoom(var1, var2, var3, -var4, this.mZoomMatrixBuffer);
      this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, this, false);
      this.calculateOffsets();
      this.postInvalidate();
   }

   public void zoom(float var1, float var2, float var3, float var4, YAxis.AxisDependency var5) {
      this.addViewportJob(ZoomJob.getInstance(this.mViewPortHandler, var1, var2, var3, var4, this.getTransformer(var5), var5, this));
   }

   @TargetApi(11)
   public void zoomAndCenterAnimated(float var1, float var2, float var3, float var4, YAxis.AxisDependency var5, long var6) {
      if (VERSION.SDK_INT >= 11) {
         MPPointD var8 = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), var5);
         this.addViewportJob(AnimatedZoomJob.getInstance(this.mViewPortHandler, this, this.getTransformer(var5), this.getAxis(var5), this.mXAxis.mAxisRange, var1, var2, this.mViewPortHandler.getScaleX(), this.mViewPortHandler.getScaleY(), var3, var4, (float)var8.x, (float)var8.y, var6));
         MPPointD.recycleInstance(var8);
      } else {
         Log.e("MPAndroidChart", "Unable to execute zoomAndCenterAnimated(...) on API level < 11");
      }

   }

   public void zoomIn() {
      MPPointF var1 = this.mViewPortHandler.getContentCenter();
      this.mViewPortHandler.zoomIn(var1.x, -var1.y, this.mZoomMatrixBuffer);
      this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, this, false);
      MPPointF.recycleInstance(var1);
      this.calculateOffsets();
      this.postInvalidate();
   }

   public void zoomOut() {
      MPPointF var1 = this.mViewPortHandler.getContentCenter();
      this.mViewPortHandler.zoomOut(var1.x, -var1.y, this.mZoomMatrixBuffer);
      this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, this, false);
      MPPointF.recycleInstance(var1);
      this.calculateOffsets();
      this.postInvalidate();
   }

   public void zoomToCenter(float var1, float var2) {
      MPPointF var3 = this.getCenterOffsets();
      Matrix var4 = this.mZoomMatrixBuffer;
      this.mViewPortHandler.zoom(var1, var2, var3.x, -var3.y, var4);
      this.mViewPortHandler.refresh(var4, this, false);
   }
}
