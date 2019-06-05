package com.github.mikephil.charting.charts;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.animation.EasingFunction;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.IHighlighter;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.renderer.LegendRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressLint({"NewApi"})
public abstract class Chart extends ViewGroup implements ChartInterface {
   public static final String LOG_TAG = "MPAndroidChart";
   public static final int PAINT_CENTER_TEXT = 14;
   public static final int PAINT_DESCRIPTION = 11;
   public static final int PAINT_GRID_BACKGROUND = 4;
   public static final int PAINT_HOLE = 13;
   public static final int PAINT_INFO = 7;
   public static final int PAINT_LEGEND_LABEL = 18;
   protected ChartAnimator mAnimator;
   protected ChartTouchListener mChartTouchListener;
   protected ChartData mData = null;
   protected DefaultValueFormatter mDefaultValueFormatter = new DefaultValueFormatter(0);
   protected Paint mDescPaint;
   protected Description mDescription;
   private boolean mDragDecelerationEnabled = true;
   private float mDragDecelerationFrictionCoef = 0.9F;
   protected boolean mDrawMarkers = true;
   private float mExtraBottomOffset = 0.0F;
   private float mExtraLeftOffset = 0.0F;
   private float mExtraRightOffset = 0.0F;
   private float mExtraTopOffset = 0.0F;
   private OnChartGestureListener mGestureListener;
   protected boolean mHighLightPerTapEnabled = true;
   protected IHighlighter mHighlighter;
   protected Highlight[] mIndicesToHighlight;
   protected Paint mInfoPaint;
   protected ArrayList mJobs = new ArrayList();
   protected Legend mLegend;
   protected LegendRenderer mLegendRenderer;
   protected boolean mLogEnabled = false;
   protected IMarker mMarker;
   protected float mMaxHighlightDistance = 0.0F;
   private String mNoDataText = "No chart data available.";
   private boolean mOffsetsCalculated = false;
   protected DataRenderer mRenderer;
   protected OnChartValueSelectedListener mSelectionListener;
   protected boolean mTouchEnabled = true;
   private boolean mUnbind = false;
   protected ViewPortHandler mViewPortHandler = new ViewPortHandler();
   protected XAxis mXAxis;

   public Chart(Context var1) {
      super(var1);
      this.init();
   }

   public Chart(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   public Chart(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   private void unbindDrawables(View var1) {
      if (var1.getBackground() != null) {
         var1.getBackground().setCallback((Callback)null);
      }

      if (var1 instanceof ViewGroup) {
         int var2 = 0;

         while(true) {
            ViewGroup var3 = (ViewGroup)var1;
            if (var2 >= var3.getChildCount()) {
               var3.removeAllViews();
               break;
            }

            this.unbindDrawables(var3.getChildAt(var2));
            ++var2;
         }
      }

   }

   public void addViewportJob(Runnable var1) {
      if (this.mViewPortHandler.hasChartDimens()) {
         this.post(var1);
      } else {
         this.mJobs.add(var1);
      }

   }

   public void animateX(int var1) {
      this.mAnimator.animateX(var1);
   }

   public void animateX(int var1, Easing.EasingOption var2) {
      this.mAnimator.animateX(var1, var2);
   }

   public void animateX(int var1, EasingFunction var2) {
      this.mAnimator.animateX(var1, var2);
   }

   public void animateXY(int var1, int var2) {
      this.mAnimator.animateXY(var1, var2);
   }

   public void animateXY(int var1, int var2, Easing.EasingOption var3, Easing.EasingOption var4) {
      this.mAnimator.animateXY(var1, var2, var3, var4);
   }

   public void animateXY(int var1, int var2, EasingFunction var3, EasingFunction var4) {
      this.mAnimator.animateXY(var1, var2, var3, var4);
   }

   public void animateY(int var1) {
      this.mAnimator.animateY(var1);
   }

   public void animateY(int var1, Easing.EasingOption var2) {
      this.mAnimator.animateY(var1, var2);
   }

   public void animateY(int var1, EasingFunction var2) {
      this.mAnimator.animateY(var1, var2);
   }

   protected abstract void calcMinMax();

   protected abstract void calculateOffsets();

   public void clear() {
      this.mData = null;
      this.mOffsetsCalculated = false;
      this.mIndicesToHighlight = null;
      this.mChartTouchListener.setLastHighlighted((Highlight)null);
      this.invalidate();
   }

   public void clearAllViewportJobs() {
      this.mJobs.clear();
   }

   public void clearValues() {
      this.mData.clearValues();
      this.invalidate();
   }

   public void disableScroll() {
      ViewParent var1 = this.getParent();
      if (var1 != null) {
         var1.requestDisallowInterceptTouchEvent(true);
      }

   }

   protected void drawDescription(Canvas var1) {
      if (this.mDescription != null && this.mDescription.isEnabled()) {
         MPPointF var2 = this.mDescription.getPosition();
         this.mDescPaint.setTypeface(this.mDescription.getTypeface());
         this.mDescPaint.setTextSize(this.mDescription.getTextSize());
         this.mDescPaint.setColor(this.mDescription.getTextColor());
         this.mDescPaint.setTextAlign(this.mDescription.getTextAlign());
         float var3;
         float var4;
         if (var2 == null) {
            var3 = (float)this.getWidth() - this.mViewPortHandler.offsetRight() - this.mDescription.getXOffset();
            var4 = (float)this.getHeight() - this.mViewPortHandler.offsetBottom() - this.mDescription.getYOffset();
         } else {
            var3 = var2.x;
            var4 = var2.y;
         }

         var1.drawText(this.mDescription.getText(), var3, var4, this.mDescPaint);
      }

   }

   protected void drawMarkers(Canvas var1) {
      if (this.mMarker != null && this.isDrawMarkersEnabled() && this.valuesToHighlight()) {
         for(int var2 = 0; var2 < this.mIndicesToHighlight.length; ++var2) {
            Highlight var3 = this.mIndicesToHighlight[var2];
            IDataSet var4 = this.mData.getDataSetByIndex(var3.getDataSetIndex());
            Entry var5 = this.mData.getEntryForHighlight(this.mIndicesToHighlight[var2]);
            int var6 = var4.getEntryIndex(var5);
            if (var5 != null && (float)var6 <= (float)var4.getEntryCount() * this.mAnimator.getPhaseX()) {
               float[] var7 = this.getMarkerPosition(var3);
               if (this.mViewPortHandler.isInBounds(var7[0], var7[1])) {
                  this.mMarker.refreshContent(var5, var3);
                  this.mMarker.draw(var1, var7[0], var7[1]);
               }
            }
         }

      }
   }

   public void enableScroll() {
      ViewParent var1 = this.getParent();
      if (var1 != null) {
         var1.requestDisallowInterceptTouchEvent(false);
      }

   }

   public ChartAnimator getAnimator() {
      return this.mAnimator;
   }

   public MPPointF getCenter() {
      return MPPointF.getInstance((float)this.getWidth() / 2.0F, (float)this.getHeight() / 2.0F);
   }

   public MPPointF getCenterOfView() {
      return this.getCenter();
   }

   public MPPointF getCenterOffsets() {
      return this.mViewPortHandler.getContentCenter();
   }

   public Bitmap getChartBitmap() {
      Bitmap var1 = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Config.RGB_565);
      Canvas var2 = new Canvas(var1);
      Drawable var3 = this.getBackground();
      if (var3 != null) {
         var3.draw(var2);
      } else {
         var2.drawColor(-1);
      }

      this.draw(var2);
      return var1;
   }

   public RectF getContentRect() {
      return this.mViewPortHandler.getContentRect();
   }

   public ChartData getData() {
      return this.mData;
   }

   public IValueFormatter getDefaultValueFormatter() {
      return this.mDefaultValueFormatter;
   }

   public Description getDescription() {
      return this.mDescription;
   }

   public float getDragDecelerationFrictionCoef() {
      return this.mDragDecelerationFrictionCoef;
   }

   public float getExtraBottomOffset() {
      return this.mExtraBottomOffset;
   }

   public float getExtraLeftOffset() {
      return this.mExtraLeftOffset;
   }

   public float getExtraRightOffset() {
      return this.mExtraRightOffset;
   }

   public float getExtraTopOffset() {
      return this.mExtraTopOffset;
   }

   public Highlight getHighlightByTouchPoint(float var1, float var2) {
      if (this.mData == null) {
         Log.e("MPAndroidChart", "Can't select by touch. No data set.");
         return null;
      } else {
         return this.getHighlighter().getHighlight(var1, var2);
      }
   }

   public Highlight[] getHighlighted() {
      return this.mIndicesToHighlight;
   }

   public IHighlighter getHighlighter() {
      return this.mHighlighter;
   }

   public ArrayList getJobs() {
      return this.mJobs;
   }

   public Legend getLegend() {
      return this.mLegend;
   }

   public LegendRenderer getLegendRenderer() {
      return this.mLegendRenderer;
   }

   public IMarker getMarker() {
      return this.mMarker;
   }

   protected float[] getMarkerPosition(Highlight var1) {
      return new float[]{var1.getDrawX(), var1.getDrawY()};
   }

   @Deprecated
   public IMarker getMarkerView() {
      return this.getMarker();
   }

   public float getMaxHighlightDistance() {
      return this.mMaxHighlightDistance;
   }

   public OnChartGestureListener getOnChartGestureListener() {
      return this.mGestureListener;
   }

   public ChartTouchListener getOnTouchListener() {
      return this.mChartTouchListener;
   }

   public Paint getPaint(int var1) {
      if (var1 != 7) {
         return var1 != 11 ? null : this.mDescPaint;
      } else {
         return this.mInfoPaint;
      }
   }

   public DataRenderer getRenderer() {
      return this.mRenderer;
   }

   public ViewPortHandler getViewPortHandler() {
      return this.mViewPortHandler;
   }

   public XAxis getXAxis() {
      return this.mXAxis;
   }

   public float getXChartMax() {
      return this.mXAxis.mAxisMaximum;
   }

   public float getXChartMin() {
      return this.mXAxis.mAxisMinimum;
   }

   public float getXRange() {
      return this.mXAxis.mAxisRange;
   }

   public float getYMax() {
      return this.mData.getYMax();
   }

   public float getYMin() {
      return this.mData.getYMin();
   }

   public void highlightValue(float var1, float var2, int var3) {
      this.highlightValue(var1, var2, var3, true);
   }

   public void highlightValue(float var1, float var2, int var3, boolean var4) {
      if (var3 >= 0 && var3 < this.mData.getDataSetCount()) {
         this.highlightValue(new Highlight(var1, var2, var3), var4);
      } else {
         this.highlightValue((Highlight)null, var4);
      }

   }

   public void highlightValue(float var1, int var2) {
      this.highlightValue(var1, var2, true);
   }

   public void highlightValue(float var1, int var2, boolean var3) {
      this.highlightValue(var1, Float.NaN, var2, var3);
   }

   public void highlightValue(Highlight var1) {
      this.highlightValue(var1, false);
   }

   public void highlightValue(Highlight var1, boolean var2) {
      Entry var3;
      if (var1 == null) {
         this.mIndicesToHighlight = null;
         var3 = null;
      } else {
         if (this.mLogEnabled) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Highlighted: ");
            var4.append(var1.toString());
            Log.i("MPAndroidChart", var4.toString());
         }

         var3 = this.mData.getEntryForHighlight(var1);
         if (var3 == null) {
            this.mIndicesToHighlight = null;
            var1 = null;
         } else {
            this.mIndicesToHighlight = new Highlight[]{var1};
         }
      }

      this.setLastHighlighted(this.mIndicesToHighlight);
      if (var2 && this.mSelectionListener != null) {
         if (!this.valuesToHighlight()) {
            this.mSelectionListener.onNothingSelected();
         } else {
            this.mSelectionListener.onValueSelected(var3, var1);
         }
      }

      this.invalidate();
   }

   public void highlightValues(Highlight[] var1) {
      this.mIndicesToHighlight = var1;
      this.setLastHighlighted(var1);
      this.invalidate();
   }

   protected void init() {
      this.setWillNotDraw(false);
      if (VERSION.SDK_INT < 11) {
         this.mAnimator = new ChartAnimator();
      } else {
         this.mAnimator = new ChartAnimator(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1) {
               Chart.this.postInvalidate();
            }
         });
      }

      Utils.init(this.getContext());
      this.mMaxHighlightDistance = Utils.convertDpToPixel(500.0F);
      this.mDescription = new Description();
      this.mLegend = new Legend();
      this.mLegendRenderer = new LegendRenderer(this.mViewPortHandler, this.mLegend);
      this.mXAxis = new XAxis();
      this.mDescPaint = new Paint(1);
      this.mInfoPaint = new Paint(1);
      this.mInfoPaint.setColor(Color.rgb(247, 189, 51));
      this.mInfoPaint.setTextAlign(Align.CENTER);
      this.mInfoPaint.setTextSize(Utils.convertDpToPixel(12.0F));
      if (this.mLogEnabled) {
         Log.i("", "Chart.init()");
      }

   }

   public boolean isDragDecelerationEnabled() {
      return this.mDragDecelerationEnabled;
   }

   @Deprecated
   public boolean isDrawMarkerViewsEnabled() {
      return this.isDrawMarkersEnabled();
   }

   public boolean isDrawMarkersEnabled() {
      return this.mDrawMarkers;
   }

   public boolean isEmpty() {
      if (this.mData == null) {
         return true;
      } else {
         return this.mData.getEntryCount() <= 0;
      }
   }

   public boolean isHighlightPerTapEnabled() {
      return this.mHighLightPerTapEnabled;
   }

   public boolean isLogEnabled() {
      return this.mLogEnabled;
   }

   public abstract void notifyDataSetChanged();

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (this.mUnbind) {
         this.unbindDrawables(this);
      }

   }

   protected void onDraw(Canvas var1) {
      if (this.mData == null) {
         if (TextUtils.isEmpty(this.mNoDataText) ^ true) {
            MPPointF var2 = this.getCenter();
            var1.drawText(this.mNoDataText, var2.x, var2.y, this.mInfoPaint);
         }

      } else {
         if (!this.mOffsetsCalculated) {
            this.calculateOffsets();
            this.mOffsetsCalculated = true;
         }

      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      for(int var6 = 0; var6 < this.getChildCount(); ++var6) {
         this.getChildAt(var6).layout(var2, var3, var4, var5);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      int var3 = (int)Utils.convertDpToPixel(50.0F);
      this.setMeasuredDimension(Math.max(this.getSuggestedMinimumWidth(), resolveSize(var3, var1)), Math.max(this.getSuggestedMinimumHeight(), resolveSize(var3, var2)));
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      if (this.mLogEnabled) {
         Log.i("MPAndroidChart", "OnSizeChanged()");
      }

      StringBuilder var5;
      if (var1 > 0 && var2 > 0 && var1 < 10000 && var2 < 10000) {
         if (this.mLogEnabled) {
            var5 = new StringBuilder();
            var5.append("Setting chart dimens, width: ");
            var5.append(var1);
            var5.append(", height: ");
            var5.append(var2);
            Log.i("MPAndroidChart", var5.toString());
         }

         this.mViewPortHandler.setChartDimens((float)var1, (float)var2);
      } else if (this.mLogEnabled) {
         var5 = new StringBuilder();
         var5.append("*Avoiding* setting chart dimens! width: ");
         var5.append(var1);
         var5.append(", height: ");
         var5.append(var2);
         Log.w("MPAndroidChart", var5.toString());
      }

      this.notifyDataSetChanged();
      Iterator var6 = this.mJobs.iterator();

      while(var6.hasNext()) {
         this.post((Runnable)var6.next());
      }

      this.mJobs.clear();
      super.onSizeChanged(var1, var2, var3, var4);
   }

   public void removeViewportJob(Runnable var1) {
      this.mJobs.remove(var1);
   }

   public boolean saveToGallery(String var1, int var2) {
      return this.saveToGallery(var1, "", "MPAndroidChart-Library Save", CompressFormat.JPEG, var2);
   }

   public boolean saveToGallery(String var1, String var2, String var3, CompressFormat var4, int var5) {
      int var6;
      label43: {
         if (var5 >= 0) {
            var6 = var5;
            if (var5 <= 100) {
               break label43;
            }
         }

         var6 = 50;
      }

      long var7 = System.currentTimeMillis();
      File var9 = Environment.getExternalStorageDirectory();
      StringBuilder var10 = new StringBuilder();
      var10.append(var9.getAbsolutePath());
      var10.append("/DCIM/");
      var10.append(var2);
      File var11 = new File(var10.toString());
      boolean var12 = var11.exists();
      boolean var13 = false;
      if (!var12 && !var11.mkdirs()) {
         return false;
      } else {
         StringBuilder var18;
         String var20;
         String var21;
         switch(var4) {
         case PNG:
            var21 = "image/png";
            var2 = var1;
            var20 = var21;
            if (!var1.endsWith(".png")) {
               var18 = new StringBuilder();
               var18.append(var1);
               var18.append(".png");
               var2 = var18.toString();
               var20 = var21;
            }
            break;
         case WEBP:
            var21 = "image/webp";
            var2 = var1;
            var20 = var21;
            if (!var1.endsWith(".webp")) {
               var18 = new StringBuilder();
               var18.append(var1);
               var18.append(".webp");
               var2 = var18.toString();
               var20 = var21;
            }
            break;
         default:
            var21 = "image/jpeg";
            var2 = var1;
            var20 = var21;
            if (!var1.endsWith(".jpg")) {
               var2 = var1;
               var20 = var21;
               if (!var1.endsWith(".jpeg")) {
                  var18 = new StringBuilder();
                  var18.append(var1);
                  var18.append(".jpg");
                  var2 = var18.toString();
                  var20 = var21;
               }
            }
         }

         StringBuilder var17 = new StringBuilder();
         var17.append(var11.getAbsolutePath());
         var17.append("/");
         var17.append(var2);
         var1 = var17.toString();

         try {
            FileOutputStream var22 = new FileOutputStream(var1);
            this.getChartBitmap().compress(var4, var6, var22);
            var22.flush();
            var22.close();
         } catch (IOException var16) {
            var16.printStackTrace();
            return false;
         }

         long var14 = (new File(var1)).length();
         ContentValues var19 = new ContentValues(8);
         var19.put("title", var2);
         var19.put("_display_name", var2);
         var19.put("date_added", var7);
         var19.put("mime_type", var20);
         var19.put("description", var3);
         var19.put("orientation", 0);
         var19.put("_data", var1);
         var19.put("_size", var14);
         if (this.getContext().getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, var19) != null) {
            var13 = true;
         }

         return var13;
      }
   }

   public boolean saveToPath(String var1, String var2) {
      Bitmap var3 = this.getChartBitmap();

      try {
         StringBuilder var5 = new StringBuilder();
         var5.append(Environment.getExternalStorageDirectory().getPath());
         var5.append(var2);
         var5.append("/");
         var5.append(var1);
         var5.append(".png");
         FileOutputStream var4 = new FileOutputStream(var5.toString());
         var3.compress(CompressFormat.PNG, 40, var4);
         var4.close();
         return true;
      } catch (Exception var6) {
         var6.printStackTrace();
         return false;
      }
   }

   public void setData(ChartData var1) {
      this.mData = var1;
      this.mOffsetsCalculated = false;
      if (var1 != null) {
         this.setupDefaultFormatter(var1.getYMin(), var1.getYMax());
         Iterator var2 = this.mData.getDataSets().iterator();

         while(true) {
            IDataSet var3;
            do {
               if (!var2.hasNext()) {
                  this.notifyDataSetChanged();
                  if (this.mLogEnabled) {
                     Log.i("MPAndroidChart", "Data is set.");
                  }

                  return;
               }

               var3 = (IDataSet)var2.next();
            } while(!var3.needsFormatter() && var3.getValueFormatter() != this.mDefaultValueFormatter);

            var3.setValueFormatter(this.mDefaultValueFormatter);
         }
      }
   }

   public void setDescription(Description var1) {
      this.mDescription = var1;
   }

   public void setDragDecelerationEnabled(boolean var1) {
      this.mDragDecelerationEnabled = var1;
   }

   public void setDragDecelerationFrictionCoef(float var1) {
      float var2 = var1;
      if (var1 < 0.0F) {
         var2 = 0.0F;
      }

      var1 = var2;
      if (var2 >= 1.0F) {
         var1 = 0.999F;
      }

      this.mDragDecelerationFrictionCoef = var1;
   }

   @Deprecated
   public void setDrawMarkerViews(boolean var1) {
      this.setDrawMarkers(var1);
   }

   public void setDrawMarkers(boolean var1) {
      this.mDrawMarkers = var1;
   }

   public void setExtraBottomOffset(float var1) {
      this.mExtraBottomOffset = Utils.convertDpToPixel(var1);
   }

   public void setExtraLeftOffset(float var1) {
      this.mExtraLeftOffset = Utils.convertDpToPixel(var1);
   }

   public void setExtraOffsets(float var1, float var2, float var3, float var4) {
      this.setExtraLeftOffset(var1);
      this.setExtraTopOffset(var2);
      this.setExtraRightOffset(var3);
      this.setExtraBottomOffset(var4);
   }

   public void setExtraRightOffset(float var1) {
      this.mExtraRightOffset = Utils.convertDpToPixel(var1);
   }

   public void setExtraTopOffset(float var1) {
      this.mExtraTopOffset = Utils.convertDpToPixel(var1);
   }

   public void setHardwareAccelerationEnabled(boolean var1) {
      if (VERSION.SDK_INT >= 11) {
         if (var1) {
            this.setLayerType(2, (Paint)null);
         } else {
            this.setLayerType(1, (Paint)null);
         }
      } else {
         Log.e("MPAndroidChart", "Cannot enable/disable hardware acceleration for devices below API level 11.");
      }

   }

   public void setHighlightPerTapEnabled(boolean var1) {
      this.mHighLightPerTapEnabled = var1;
   }

   public void setHighlighter(ChartHighlighter var1) {
      this.mHighlighter = var1;
   }

   protected void setLastHighlighted(Highlight[] var1) {
      if (var1 != null && var1.length > 0 && var1[0] != null) {
         this.mChartTouchListener.setLastHighlighted(var1[0]);
      } else {
         this.mChartTouchListener.setLastHighlighted((Highlight)null);
      }

   }

   public void setLogEnabled(boolean var1) {
      this.mLogEnabled = var1;
   }

   public void setMarker(IMarker var1) {
      this.mMarker = var1;
   }

   @Deprecated
   public void setMarkerView(IMarker var1) {
      this.setMarker(var1);
   }

   public void setMaxHighlightDistance(float var1) {
      this.mMaxHighlightDistance = Utils.convertDpToPixel(var1);
   }

   public void setNoDataText(String var1) {
      this.mNoDataText = var1;
   }

   public void setNoDataTextColor(int var1) {
      this.mInfoPaint.setColor(var1);
   }

   public void setNoDataTextTypeface(Typeface var1) {
      this.mInfoPaint.setTypeface(var1);
   }

   public void setOnChartGestureListener(OnChartGestureListener var1) {
      this.mGestureListener = var1;
   }

   public void setOnChartValueSelectedListener(OnChartValueSelectedListener var1) {
      this.mSelectionListener = var1;
   }

   public void setOnTouchListener(ChartTouchListener var1) {
      this.mChartTouchListener = var1;
   }

   public void setPaint(Paint var1, int var2) {
      if (var2 != 7) {
         if (var2 == 11) {
            this.mDescPaint = var1;
         }
      } else {
         this.mInfoPaint = var1;
      }

   }

   public void setRenderer(DataRenderer var1) {
      if (var1 != null) {
         this.mRenderer = var1;
      }

   }

   public void setTouchEnabled(boolean var1) {
      this.mTouchEnabled = var1;
   }

   public void setUnbindEnabled(boolean var1) {
      this.mUnbind = var1;
   }

   protected void setupDefaultFormatter(float var1, float var2) {
      if (this.mData != null && this.mData.getEntryCount() >= 2) {
         var1 = Math.abs(var2 - var1);
      } else {
         var1 = Math.max(Math.abs(var1), Math.abs(var2));
      }

      int var3 = Utils.getDecimals(var1);
      this.mDefaultValueFormatter.setup(var3);
   }

   public boolean valuesToHighlight() {
      Highlight[] var1 = this.mIndicesToHighlight;
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 != null) {
         var3 = var2;
         if (this.mIndicesToHighlight.length > 0) {
            if (this.mIndicesToHighlight[0] == null) {
               var3 = var2;
            } else {
               var3 = true;
            }
         }
      }

      return var3;
   }
}
