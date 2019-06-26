// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import android.graphics.Typeface;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import java.io.IOException;
import android.provider.MediaStore$Images$Media;
import android.content.ContentValues;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import android.os.Environment;
import android.graphics.Bitmap$CompressFormat;
import java.util.Iterator;
import android.text.TextUtils;
import android.graphics.Paint$Align;
import android.graphics.Color;
import com.github.mikephil.charting.utils.Utils;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.os.Build$VERSION;
import android.util.Log;
import com.github.mikephil.charting.formatter.IValueFormatter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import com.github.mikephil.charting.utils.MPPointF;
import android.graphics.Canvas;
import android.view.ViewParent;
import com.github.mikephil.charting.animation.EasingFunction;
import com.github.mikephil.charting.animation.Easing;
import android.graphics.drawable.Drawable$Callback;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.renderer.LegendRenderer;
import com.github.mikephil.charting.components.Legend;
import java.util.ArrayList;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.IHighlighter;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.components.Description;
import android.graphics.Paint;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.animation.ChartAnimator;
import android.annotation.SuppressLint;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import android.view.ViewGroup;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.data.ChartData;

@SuppressLint({ "NewApi" })
public abstract class Chart<T extends ChartData<? extends IDataSet<? extends Entry>>> extends ViewGroup implements ChartInterface
{
    public static final String LOG_TAG = "MPAndroidChart";
    public static final int PAINT_CENTER_TEXT = 14;
    public static final int PAINT_DESCRIPTION = 11;
    public static final int PAINT_GRID_BACKGROUND = 4;
    public static final int PAINT_HOLE = 13;
    public static final int PAINT_INFO = 7;
    public static final int PAINT_LEGEND_LABEL = 18;
    protected ChartAnimator mAnimator;
    protected ChartTouchListener mChartTouchListener;
    protected T mData;
    protected DefaultValueFormatter mDefaultValueFormatter;
    protected Paint mDescPaint;
    protected Description mDescription;
    private boolean mDragDecelerationEnabled;
    private float mDragDecelerationFrictionCoef;
    protected boolean mDrawMarkers;
    private float mExtraBottomOffset;
    private float mExtraLeftOffset;
    private float mExtraRightOffset;
    private float mExtraTopOffset;
    private OnChartGestureListener mGestureListener;
    protected boolean mHighLightPerTapEnabled;
    protected IHighlighter mHighlighter;
    protected Highlight[] mIndicesToHighlight;
    protected Paint mInfoPaint;
    protected ArrayList<Runnable> mJobs;
    protected Legend mLegend;
    protected LegendRenderer mLegendRenderer;
    protected boolean mLogEnabled;
    protected IMarker mMarker;
    protected float mMaxHighlightDistance;
    private String mNoDataText;
    private boolean mOffsetsCalculated;
    protected DataRenderer mRenderer;
    protected OnChartValueSelectedListener mSelectionListener;
    protected boolean mTouchEnabled;
    private boolean mUnbind;
    protected ViewPortHandler mViewPortHandler;
    protected XAxis mXAxis;
    
    public Chart(final Context context) {
        super(context);
        this.mLogEnabled = false;
        this.mData = null;
        this.mHighLightPerTapEnabled = true;
        this.mDragDecelerationEnabled = true;
        this.mDragDecelerationFrictionCoef = 0.9f;
        this.mDefaultValueFormatter = new DefaultValueFormatter(0);
        this.mTouchEnabled = true;
        this.mNoDataText = "No chart data available.";
        this.mViewPortHandler = new ViewPortHandler();
        this.mExtraTopOffset = 0.0f;
        this.mExtraRightOffset = 0.0f;
        this.mExtraBottomOffset = 0.0f;
        this.mExtraLeftOffset = 0.0f;
        this.mOffsetsCalculated = false;
        this.mMaxHighlightDistance = 0.0f;
        this.mDrawMarkers = true;
        this.mJobs = new ArrayList<Runnable>();
        this.mUnbind = false;
        this.init();
    }
    
    public Chart(final Context context, final AttributeSet set) {
        super(context, set);
        this.mLogEnabled = false;
        this.mData = null;
        this.mHighLightPerTapEnabled = true;
        this.mDragDecelerationEnabled = true;
        this.mDragDecelerationFrictionCoef = 0.9f;
        this.mDefaultValueFormatter = new DefaultValueFormatter(0);
        this.mTouchEnabled = true;
        this.mNoDataText = "No chart data available.";
        this.mViewPortHandler = new ViewPortHandler();
        this.mExtraTopOffset = 0.0f;
        this.mExtraRightOffset = 0.0f;
        this.mExtraBottomOffset = 0.0f;
        this.mExtraLeftOffset = 0.0f;
        this.mOffsetsCalculated = false;
        this.mMaxHighlightDistance = 0.0f;
        this.mDrawMarkers = true;
        this.mJobs = new ArrayList<Runnable>();
        this.mUnbind = false;
        this.init();
    }
    
    public Chart(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mLogEnabled = false;
        this.mData = null;
        this.mHighLightPerTapEnabled = true;
        this.mDragDecelerationEnabled = true;
        this.mDragDecelerationFrictionCoef = 0.9f;
        this.mDefaultValueFormatter = new DefaultValueFormatter(0);
        this.mTouchEnabled = true;
        this.mNoDataText = "No chart data available.";
        this.mViewPortHandler = new ViewPortHandler();
        this.mExtraTopOffset = 0.0f;
        this.mExtraRightOffset = 0.0f;
        this.mExtraBottomOffset = 0.0f;
        this.mExtraLeftOffset = 0.0f;
        this.mOffsetsCalculated = false;
        this.mMaxHighlightDistance = 0.0f;
        this.mDrawMarkers = true;
        this.mJobs = new ArrayList<Runnable>();
        this.mUnbind = false;
        this.init();
    }
    
    private void unbindDrawables(final View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback((Drawable$Callback)null);
        }
        if (view instanceof ViewGroup) {
            int n = 0;
            ViewGroup viewGroup;
            while (true) {
                viewGroup = (ViewGroup)view;
                if (n >= viewGroup.getChildCount()) {
                    break;
                }
                this.unbindDrawables(viewGroup.getChildAt(n));
                ++n;
            }
            viewGroup.removeAllViews();
        }
    }
    
    public void addViewportJob(final Runnable e) {
        if (this.mViewPortHandler.hasChartDimens()) {
            this.post(e);
        }
        else {
            this.mJobs.add(e);
        }
    }
    
    public void animateX(final int n) {
        this.mAnimator.animateX(n);
    }
    
    public void animateX(final int n, final Easing.EasingOption easingOption) {
        this.mAnimator.animateX(n, easingOption);
    }
    
    public void animateX(final int n, final EasingFunction easingFunction) {
        this.mAnimator.animateX(n, easingFunction);
    }
    
    public void animateXY(final int n, final int n2) {
        this.mAnimator.animateXY(n, n2);
    }
    
    public void animateXY(final int n, final int n2, final Easing.EasingOption easingOption, final Easing.EasingOption easingOption2) {
        this.mAnimator.animateXY(n, n2, easingOption, easingOption2);
    }
    
    public void animateXY(final int n, final int n2, final EasingFunction easingFunction, final EasingFunction easingFunction2) {
        this.mAnimator.animateXY(n, n2, easingFunction, easingFunction2);
    }
    
    public void animateY(final int n) {
        this.mAnimator.animateY(n);
    }
    
    public void animateY(final int n, final Easing.EasingOption easingOption) {
        this.mAnimator.animateY(n, easingOption);
    }
    
    public void animateY(final int n, final EasingFunction easingFunction) {
        this.mAnimator.animateY(n, easingFunction);
    }
    
    protected abstract void calcMinMax();
    
    protected abstract void calculateOffsets();
    
    public void clear() {
        this.mData = null;
        this.mOffsetsCalculated = false;
        this.mIndicesToHighlight = null;
        this.mChartTouchListener.setLastHighlighted(null);
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
        final ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }
    
    protected void drawDescription(final Canvas canvas) {
        if (this.mDescription != null && this.mDescription.isEnabled()) {
            final MPPointF position = this.mDescription.getPosition();
            this.mDescPaint.setTypeface(this.mDescription.getTypeface());
            this.mDescPaint.setTextSize(this.mDescription.getTextSize());
            this.mDescPaint.setColor(this.mDescription.getTextColor());
            this.mDescPaint.setTextAlign(this.mDescription.getTextAlign());
            float x;
            float y;
            if (position == null) {
                x = this.getWidth() - this.mViewPortHandler.offsetRight() - this.mDescription.getXOffset();
                y = this.getHeight() - this.mViewPortHandler.offsetBottom() - this.mDescription.getYOffset();
            }
            else {
                x = position.x;
                y = position.y;
            }
            canvas.drawText(this.mDescription.getText(), x, y, this.mDescPaint);
        }
    }
    
    protected void drawMarkers(final Canvas canvas) {
        if (this.mMarker != null && this.isDrawMarkersEnabled() && this.valuesToHighlight()) {
            for (int i = 0; i < this.mIndicesToHighlight.length; ++i) {
                final Highlight highlight = this.mIndicesToHighlight[i];
                final IDataSet<? extends Entry> dataSetByIndex = ((ChartData<IDataSet<? extends Entry>>)this.mData).getDataSetByIndex(highlight.getDataSetIndex());
                final Entry entryForHighlight = this.mData.getEntryForHighlight(this.mIndicesToHighlight[i]);
                final int entryIndex = dataSetByIndex.getEntryIndex(entryForHighlight);
                if (entryForHighlight != null) {
                    if (entryIndex <= dataSetByIndex.getEntryCount() * this.mAnimator.getPhaseX()) {
                        final float[] markerPosition = this.getMarkerPosition(highlight);
                        if (this.mViewPortHandler.isInBounds(markerPosition[0], markerPosition[1])) {
                            this.mMarker.refreshContent(entryForHighlight, highlight);
                            this.mMarker.draw(canvas, markerPosition[0], markerPosition[1]);
                        }
                    }
                }
            }
        }
    }
    
    public void enableScroll() {
        final ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
    }
    
    public ChartAnimator getAnimator() {
        return this.mAnimator;
    }
    
    public MPPointF getCenter() {
        return MPPointF.getInstance(this.getWidth() / 2.0f, this.getHeight() / 2.0f);
    }
    
    public MPPointF getCenterOfView() {
        return this.getCenter();
    }
    
    public MPPointF getCenterOffsets() {
        return this.mViewPortHandler.getContentCenter();
    }
    
    public Bitmap getChartBitmap() {
        final Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap$Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        final Drawable background = this.getBackground();
        if (background != null) {
            background.draw(canvas);
        }
        else {
            canvas.drawColor(-1);
        }
        this.draw(canvas);
        return bitmap;
    }
    
    public RectF getContentRect() {
        return this.mViewPortHandler.getContentRect();
    }
    
    public T getData() {
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
    
    public Highlight getHighlightByTouchPoint(final float n, final float n2) {
        if (this.mData == null) {
            Log.e("MPAndroidChart", "Can't select by touch. No data set.");
            return null;
        }
        return this.getHighlighter().getHighlight(n, n2);
    }
    
    public Highlight[] getHighlighted() {
        return this.mIndicesToHighlight;
    }
    
    public IHighlighter getHighlighter() {
        return this.mHighlighter;
    }
    
    public ArrayList<Runnable> getJobs() {
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
    
    protected float[] getMarkerPosition(final Highlight highlight) {
        return new float[] { highlight.getDrawX(), highlight.getDrawY() };
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
    
    public Paint getPaint(final int n) {
        if (n == 7) {
            return this.mInfoPaint;
        }
        if (n != 11) {
            return null;
        }
        return this.mDescPaint;
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
    
    public void highlightValue(final float n, final float n2, final int n3) {
        this.highlightValue(n, n2, n3, true);
    }
    
    public void highlightValue(final float n, final float n2, final int n3, final boolean b) {
        if (n3 >= 0 && n3 < this.mData.getDataSetCount()) {
            this.highlightValue(new Highlight(n, n2, n3), b);
        }
        else {
            this.highlightValue(null, b);
        }
    }
    
    public void highlightValue(final float n, final int n2) {
        this.highlightValue(n, n2, true);
    }
    
    public void highlightValue(final float n, final int n2, final boolean b) {
        this.highlightValue(n, Float.NaN, n2, b);
    }
    
    public void highlightValue(final Highlight highlight) {
        this.highlightValue(highlight, false);
    }
    
    public void highlightValue(Highlight highlight, final boolean b) {
        Entry entryForHighlight;
        if (highlight == null) {
            this.mIndicesToHighlight = null;
            entryForHighlight = null;
        }
        else {
            if (this.mLogEnabled) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Highlighted: ");
                sb.append(highlight.toString());
                Log.i("MPAndroidChart", sb.toString());
            }
            entryForHighlight = this.mData.getEntryForHighlight(highlight);
            if (entryForHighlight == null) {
                this.mIndicesToHighlight = null;
                highlight = null;
            }
            else {
                this.mIndicesToHighlight = new Highlight[] { highlight };
            }
        }
        this.setLastHighlighted(this.mIndicesToHighlight);
        if (b && this.mSelectionListener != null) {
            if (!this.valuesToHighlight()) {
                this.mSelectionListener.onNothingSelected();
            }
            else {
                this.mSelectionListener.onValueSelected(entryForHighlight, highlight);
            }
        }
        this.invalidate();
    }
    
    public void highlightValues(final Highlight[] mIndicesToHighlight) {
        this.setLastHighlighted(this.mIndicesToHighlight = mIndicesToHighlight);
        this.invalidate();
    }
    
    protected void init() {
        this.setWillNotDraw(false);
        if (Build$VERSION.SDK_INT < 11) {
            this.mAnimator = new ChartAnimator();
        }
        else {
            this.mAnimator = new ChartAnimator((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    Chart.this.postInvalidate();
                }
            });
        }
        Utils.init(this.getContext());
        this.mMaxHighlightDistance = Utils.convertDpToPixel(500.0f);
        this.mDescription = new Description();
        this.mLegend = new Legend();
        this.mLegendRenderer = new LegendRenderer(this.mViewPortHandler, this.mLegend);
        this.mXAxis = new XAxis();
        this.mDescPaint = new Paint(1);
        (this.mInfoPaint = new Paint(1)).setColor(Color.rgb(247, 189, 51));
        this.mInfoPaint.setTextAlign(Paint$Align.CENTER);
        this.mInfoPaint.setTextSize(Utils.convertDpToPixel(12.0f));
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
        return this.mData == null || this.mData.getEntryCount() <= 0;
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
            this.unbindDrawables((View)this);
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.mData == null) {
            if (TextUtils.isEmpty((CharSequence)this.mNoDataText) ^ true) {
                final MPPointF center = this.getCenter();
                canvas.drawText(this.mNoDataText, center.x, center.y, this.mInfoPaint);
            }
            return;
        }
        if (!this.mOffsetsCalculated) {
            this.calculateOffsets();
            this.mOffsetsCalculated = true;
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            this.getChildAt(i).layout(n, n2, n3, n4);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        final int n3 = (int)Utils.convertDpToPixel(50.0f);
        this.setMeasuredDimension(Math.max(this.getSuggestedMinimumWidth(), resolveSize(n3, n)), Math.max(this.getSuggestedMinimumHeight(), resolveSize(n3, n2)));
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "OnSizeChanged()");
        }
        if (n > 0 && n2 > 0 && n < 10000 && n2 < 10000) {
            if (this.mLogEnabled) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Setting chart dimens, width: ");
                sb.append(n);
                sb.append(", height: ");
                sb.append(n2);
                Log.i("MPAndroidChart", sb.toString());
            }
            this.mViewPortHandler.setChartDimens((float)n, (float)n2);
        }
        else if (this.mLogEnabled) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("*Avoiding* setting chart dimens! width: ");
            sb2.append(n);
            sb2.append(", height: ");
            sb2.append(n2);
            Log.w("MPAndroidChart", sb2.toString());
        }
        this.notifyDataSetChanged();
        final Iterator<Runnable> iterator = this.mJobs.iterator();
        while (iterator.hasNext()) {
            this.post((Runnable)iterator.next());
        }
        this.mJobs.clear();
        super.onSizeChanged(n, n2, n3, n4);
    }
    
    public void removeViewportJob(final Runnable o) {
        this.mJobs.remove(o);
    }
    
    public boolean saveToGallery(final String s, final int n) {
        return this.saveToGallery(s, "", "MPAndroidChart-Library Save", Bitmap$CompressFormat.JPEG, n);
    }
    
    public boolean saveToGallery(String string, String s, final String s2, final Bitmap$CompressFormat bitmap$CompressFormat, final int n) {
        int n2;
        if (n < 0 || (n2 = n) > 100) {
            n2 = 50;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        final File externalStorageDirectory = Environment.getExternalStorageDirectory();
        final StringBuilder sb = new StringBuilder();
        sb.append(externalStorageDirectory.getAbsolutePath());
        sb.append("/DCIM/");
        sb.append(s);
        final File file = new File(sb.toString());
        final boolean exists = file.exists();
        boolean b = false;
        if (!exists && !file.mkdirs()) {
            return false;
        }
        String s4 = null;
        switch (Chart$2.$SwitchMap$android$graphics$Bitmap$CompressFormat[bitmap$CompressFormat.ordinal()]) {
            default: {
                final String s3 = "image/jpeg";
                s = string;
                s4 = s3;
                if (string.endsWith(".jpg")) {
                    break;
                }
                s = string;
                s4 = s3;
                if (!string.endsWith(".jpeg")) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(string);
                    sb2.append(".jpg");
                    s = sb2.toString();
                    s4 = s3;
                    break;
                }
                break;
            }
            case 2: {
                final String s5 = "image/webp";
                s = string;
                s4 = s5;
                if (!string.endsWith(".webp")) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(string);
                    sb3.append(".webp");
                    s = sb3.toString();
                    s4 = s5;
                    break;
                }
                break;
            }
            case 1: {
                final String s6 = "image/png";
                s = string;
                s4 = s6;
                if (!string.endsWith(".png")) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(string);
                    sb4.append(".png");
                    s = sb4.toString();
                    s4 = s6;
                    break;
                }
                break;
            }
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(file.getAbsolutePath());
        sb5.append("/");
        sb5.append(s);
        string = sb5.toString();
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(string);
            this.getChartBitmap().compress(bitmap$CompressFormat, n2, (OutputStream)fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            final long length = new File(string).length();
            final ContentValues contentValues = new ContentValues(8);
            contentValues.put("title", s);
            contentValues.put("_display_name", s);
            contentValues.put("date_added", Long.valueOf(currentTimeMillis));
            contentValues.put("mime_type", s4);
            contentValues.put("description", s2);
            contentValues.put("orientation", Integer.valueOf(0));
            contentValues.put("_data", string);
            contentValues.put("_size", Long.valueOf(length));
            if (this.getContext().getContentResolver().insert(MediaStore$Images$Media.EXTERNAL_CONTENT_URI, contentValues) != null) {
                b = true;
            }
            return b;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean saveToPath(final String str, final String str2) {
        final Bitmap chartBitmap = this.getChartBitmap();
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(Environment.getExternalStorageDirectory().getPath());
            sb.append(str2);
            sb.append("/");
            sb.append(str);
            sb.append(".png");
            final FileOutputStream fileOutputStream = new FileOutputStream(sb.toString());
            chartBitmap.compress(Bitmap$CompressFormat.PNG, 40, (OutputStream)fileOutputStream);
            fileOutputStream.close();
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public void setData(final T mData) {
        this.mData = mData;
        this.mOffsetsCalculated = false;
        if (mData == null) {
            return;
        }
        this.setupDefaultFormatter(mData.getYMin(), mData.getYMax());
        for (final IDataSet set : this.mData.getDataSets()) {
            if (set.needsFormatter() || set.getValueFormatter() == this.mDefaultValueFormatter) {
                set.setValueFormatter(this.mDefaultValueFormatter);
            }
        }
        this.notifyDataSetChanged();
        if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "Data is set.");
        }
    }
    
    public void setDescription(final Description mDescription) {
        this.mDescription = mDescription;
    }
    
    public void setDragDecelerationEnabled(final boolean mDragDecelerationEnabled) {
        this.mDragDecelerationEnabled = mDragDecelerationEnabled;
    }
    
    public void setDragDecelerationFrictionCoef(float mDragDecelerationFrictionCoef) {
        float n = mDragDecelerationFrictionCoef;
        if (mDragDecelerationFrictionCoef < 0.0f) {
            n = 0.0f;
        }
        mDragDecelerationFrictionCoef = n;
        if (n >= 1.0f) {
            mDragDecelerationFrictionCoef = 0.999f;
        }
        this.mDragDecelerationFrictionCoef = mDragDecelerationFrictionCoef;
    }
    
    @Deprecated
    public void setDrawMarkerViews(final boolean drawMarkers) {
        this.setDrawMarkers(drawMarkers);
    }
    
    public void setDrawMarkers(final boolean mDrawMarkers) {
        this.mDrawMarkers = mDrawMarkers;
    }
    
    public void setExtraBottomOffset(final float n) {
        this.mExtraBottomOffset = Utils.convertDpToPixel(n);
    }
    
    public void setExtraLeftOffset(final float n) {
        this.mExtraLeftOffset = Utils.convertDpToPixel(n);
    }
    
    public void setExtraOffsets(final float extraLeftOffset, final float extraTopOffset, final float extraRightOffset, final float extraBottomOffset) {
        this.setExtraLeftOffset(extraLeftOffset);
        this.setExtraTopOffset(extraTopOffset);
        this.setExtraRightOffset(extraRightOffset);
        this.setExtraBottomOffset(extraBottomOffset);
    }
    
    public void setExtraRightOffset(final float n) {
        this.mExtraRightOffset = Utils.convertDpToPixel(n);
    }
    
    public void setExtraTopOffset(final float n) {
        this.mExtraTopOffset = Utils.convertDpToPixel(n);
    }
    
    public void setHardwareAccelerationEnabled(final boolean b) {
        if (Build$VERSION.SDK_INT >= 11) {
            if (b) {
                this.setLayerType(2, (Paint)null);
            }
            else {
                this.setLayerType(1, (Paint)null);
            }
        }
        else {
            Log.e("MPAndroidChart", "Cannot enable/disable hardware acceleration for devices below API level 11.");
        }
    }
    
    public void setHighlightPerTapEnabled(final boolean mHighLightPerTapEnabled) {
        this.mHighLightPerTapEnabled = mHighLightPerTapEnabled;
    }
    
    public void setHighlighter(final ChartHighlighter mHighlighter) {
        this.mHighlighter = mHighlighter;
    }
    
    protected void setLastHighlighted(final Highlight[] array) {
        if (array != null && array.length > 0 && array[0] != null) {
            this.mChartTouchListener.setLastHighlighted(array[0]);
        }
        else {
            this.mChartTouchListener.setLastHighlighted(null);
        }
    }
    
    public void setLogEnabled(final boolean mLogEnabled) {
        this.mLogEnabled = mLogEnabled;
    }
    
    public void setMarker(final IMarker mMarker) {
        this.mMarker = mMarker;
    }
    
    @Deprecated
    public void setMarkerView(final IMarker marker) {
        this.setMarker(marker);
    }
    
    public void setMaxHighlightDistance(final float n) {
        this.mMaxHighlightDistance = Utils.convertDpToPixel(n);
    }
    
    public void setNoDataText(final String mNoDataText) {
        this.mNoDataText = mNoDataText;
    }
    
    public void setNoDataTextColor(final int color) {
        this.mInfoPaint.setColor(color);
    }
    
    public void setNoDataTextTypeface(final Typeface typeface) {
        this.mInfoPaint.setTypeface(typeface);
    }
    
    public void setOnChartGestureListener(final OnChartGestureListener mGestureListener) {
        this.mGestureListener = mGestureListener;
    }
    
    public void setOnChartValueSelectedListener(final OnChartValueSelectedListener mSelectionListener) {
        this.mSelectionListener = mSelectionListener;
    }
    
    public void setOnTouchListener(final ChartTouchListener mChartTouchListener) {
        this.mChartTouchListener = mChartTouchListener;
    }
    
    public void setPaint(final Paint paint, final int n) {
        if (n != 7) {
            if (n == 11) {
                this.mDescPaint = paint;
            }
        }
        else {
            this.mInfoPaint = paint;
        }
    }
    
    public void setRenderer(final DataRenderer mRenderer) {
        if (mRenderer != null) {
            this.mRenderer = mRenderer;
        }
    }
    
    public void setTouchEnabled(final boolean mTouchEnabled) {
        this.mTouchEnabled = mTouchEnabled;
    }
    
    public void setUnbindEnabled(final boolean mUnbind) {
        this.mUnbind = mUnbind;
    }
    
    protected void setupDefaultFormatter(float a, final float a2) {
        if (this.mData != null && this.mData.getEntryCount() >= 2) {
            a = Math.abs(a2 - a);
        }
        else {
            a = Math.max(Math.abs(a), Math.abs(a2));
        }
        this.mDefaultValueFormatter.setup(Utils.getDecimals(a));
    }
    
    public boolean valuesToHighlight() {
        final Highlight[] mIndicesToHighlight = this.mIndicesToHighlight;
        boolean b2;
        final boolean b = b2 = false;
        if (mIndicesToHighlight != null) {
            b2 = b;
            if (this.mIndicesToHighlight.length > 0) {
                b2 = (this.mIndicesToHighlight[0] != null || b);
            }
        }
        return b2;
    }
}
