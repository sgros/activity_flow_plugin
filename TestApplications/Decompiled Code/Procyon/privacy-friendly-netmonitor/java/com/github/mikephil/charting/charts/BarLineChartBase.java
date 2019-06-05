// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.jobs.AnimatedZoomJob;
import com.github.mikephil.charting.jobs.ZoomJob;
import android.view.MotionEvent;
import com.github.mikephil.charting.data.ChartData;
import android.graphics.Color;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.highlight.Highlight;
import android.graphics.Canvas;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import android.annotation.TargetApi;
import com.github.mikephil.charting.jobs.AnimatedMoveViewJob;
import android.os.Build$VERSION;
import android.view.View;
import com.github.mikephil.charting.jobs.MoveViewJob;
import android.util.Log;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.components.XAxis;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import android.graphics.RectF;
import com.github.mikephil.charting.utils.Transformer;
import android.graphics.Matrix;
import com.github.mikephil.charting.listener.OnDrawListener;
import android.graphics.Paint;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.components.YAxis;
import android.annotation.SuppressLint;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;

@SuppressLint({ "RtlHardcoded" })
public abstract class BarLineChartBase<T extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>> extends Chart<T> implements BarLineScatterCandleBubbleDataProvider
{
    private long drawCycles;
    protected boolean mAutoScaleMinMaxEnabled;
    protected YAxis mAxisLeft;
    protected YAxisRenderer mAxisRendererLeft;
    protected YAxisRenderer mAxisRendererRight;
    protected YAxis mAxisRight;
    protected Paint mBorderPaint;
    protected boolean mClipValuesToContent;
    private boolean mCustomViewPortEnabled;
    protected boolean mDoubleTapToZoomEnabled;
    private boolean mDragXEnabled;
    private boolean mDragYEnabled;
    protected boolean mDrawBorders;
    protected boolean mDrawGridBackground;
    protected OnDrawListener mDrawListener;
    protected Matrix mFitScreenMatrixBuffer;
    protected float[] mGetPositionBuffer;
    protected Paint mGridBackgroundPaint;
    protected boolean mHighlightPerDragEnabled;
    protected boolean mKeepPositionOnRotation;
    protected Transformer mLeftAxisTransformer;
    protected int mMaxVisibleCount;
    protected float mMinOffset;
    private RectF mOffsetsBuffer;
    protected float[] mOnSizeChangedBuffer;
    protected boolean mPinchZoomEnabled;
    protected Transformer mRightAxisTransformer;
    private boolean mScaleXEnabled;
    private boolean mScaleYEnabled;
    protected XAxisRenderer mXAxisRenderer;
    protected Matrix mZoomMatrixBuffer;
    protected MPPointD posForGetHighestVisibleX;
    protected MPPointD posForGetLowestVisibleX;
    private long totalTime;
    
    public BarLineChartBase(final Context context) {
        super(context);
        this.mMaxVisibleCount = 100;
        this.mAutoScaleMinMaxEnabled = false;
        this.mPinchZoomEnabled = false;
        this.mDoubleTapToZoomEnabled = true;
        this.mHighlightPerDragEnabled = true;
        this.mDragXEnabled = true;
        this.mDragYEnabled = true;
        this.mScaleXEnabled = true;
        this.mScaleYEnabled = true;
        this.mDrawGridBackground = false;
        this.mDrawBorders = false;
        this.mClipValuesToContent = false;
        this.mMinOffset = 15.0f;
        this.mKeepPositionOnRotation = false;
        this.totalTime = 0L;
        this.drawCycles = 0L;
        this.mOffsetsBuffer = new RectF();
        this.mZoomMatrixBuffer = new Matrix();
        this.mFitScreenMatrixBuffer = new Matrix();
        this.mCustomViewPortEnabled = false;
        this.mGetPositionBuffer = new float[2];
        this.posForGetLowestVisibleX = MPPointD.getInstance(0.0, 0.0);
        this.posForGetHighestVisibleX = MPPointD.getInstance(0.0, 0.0);
        this.mOnSizeChangedBuffer = new float[2];
    }
    
    public BarLineChartBase(final Context context, final AttributeSet set) {
        super(context, set);
        this.mMaxVisibleCount = 100;
        this.mAutoScaleMinMaxEnabled = false;
        this.mPinchZoomEnabled = false;
        this.mDoubleTapToZoomEnabled = true;
        this.mHighlightPerDragEnabled = true;
        this.mDragXEnabled = true;
        this.mDragYEnabled = true;
        this.mScaleXEnabled = true;
        this.mScaleYEnabled = true;
        this.mDrawGridBackground = false;
        this.mDrawBorders = false;
        this.mClipValuesToContent = false;
        this.mMinOffset = 15.0f;
        this.mKeepPositionOnRotation = false;
        this.totalTime = 0L;
        this.drawCycles = 0L;
        this.mOffsetsBuffer = new RectF();
        this.mZoomMatrixBuffer = new Matrix();
        this.mFitScreenMatrixBuffer = new Matrix();
        this.mCustomViewPortEnabled = false;
        this.mGetPositionBuffer = new float[2];
        this.posForGetLowestVisibleX = MPPointD.getInstance(0.0, 0.0);
        this.posForGetHighestVisibleX = MPPointD.getInstance(0.0, 0.0);
        this.mOnSizeChangedBuffer = new float[2];
    }
    
    public BarLineChartBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mMaxVisibleCount = 100;
        this.mAutoScaleMinMaxEnabled = false;
        this.mPinchZoomEnabled = false;
        this.mDoubleTapToZoomEnabled = true;
        this.mHighlightPerDragEnabled = true;
        this.mDragXEnabled = true;
        this.mDragYEnabled = true;
        this.mScaleXEnabled = true;
        this.mScaleYEnabled = true;
        this.mDrawGridBackground = false;
        this.mDrawBorders = false;
        this.mClipValuesToContent = false;
        this.mMinOffset = 15.0f;
        this.mKeepPositionOnRotation = false;
        this.totalTime = 0L;
        this.drawCycles = 0L;
        this.mOffsetsBuffer = new RectF();
        this.mZoomMatrixBuffer = new Matrix();
        this.mFitScreenMatrixBuffer = new Matrix();
        this.mCustomViewPortEnabled = false;
        this.mGetPositionBuffer = new float[2];
        this.posForGetLowestVisibleX = MPPointD.getInstance(0.0, 0.0);
        this.posForGetHighestVisibleX = MPPointD.getInstance(0.0, 0.0);
        this.mOnSizeChangedBuffer = new float[2];
    }
    
    protected void autoScale() {
        this.mData.calcMinMaxY(this.getLowestVisibleX(), this.getHighestVisibleX());
        this.mXAxis.calculate(this.mData.getXMin(), this.mData.getXMax());
        if (this.mAxisLeft.isEnabled()) {
            this.mAxisLeft.calculate(this.mData.getYMin(YAxis.AxisDependency.LEFT), this.mData.getYMax(YAxis.AxisDependency.LEFT));
        }
        if (this.mAxisRight.isEnabled()) {
            this.mAxisRight.calculate(this.mData.getYMin(YAxis.AxisDependency.RIGHT), this.mData.getYMax(YAxis.AxisDependency.RIGHT));
        }
        this.calculateOffsets();
    }
    
    @Override
    protected void calcMinMax() {
        this.mXAxis.calculate(this.mData.getXMin(), this.mData.getXMax());
        this.mAxisLeft.calculate(this.mData.getYMin(YAxis.AxisDependency.LEFT), this.mData.getYMax(YAxis.AxisDependency.LEFT));
        this.mAxisRight.calculate(this.mData.getYMin(YAxis.AxisDependency.RIGHT), this.mData.getYMax(YAxis.AxisDependency.RIGHT));
    }
    
    protected void calculateLegendOffsets(final RectF rectF) {
        rectF.left = 0.0f;
        rectF.right = 0.0f;
        rectF.top = 0.0f;
        rectF.bottom = 0.0f;
        Label_0552: {
            if (this.mLegend != null && this.mLegend.isEnabled() && !this.mLegend.isDrawInsideEnabled()) {
                switch (BarLineChartBase$2.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendOrientation[this.mLegend.getOrientation().ordinal()]) {
                    case 2: {
                        switch (BarLineChartBase$2.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment[this.mLegend.getVerticalAlignment().ordinal()]) {
                            default: {
                                break Label_0552;
                            }
                            case 2: {
                                rectF.bottom += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
                                if (this.getXAxis().isEnabled() && this.getXAxis().isDrawLabelsEnabled()) {
                                    rectF.bottom += this.getXAxis().mLabelRotatedHeight;
                                    break Label_0552;
                                }
                                break Label_0552;
                            }
                            case 1: {
                                rectF.top += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
                                if (this.getXAxis().isEnabled() && this.getXAxis().isDrawLabelsEnabled()) {
                                    rectF.top += this.getXAxis().mLabelRotatedHeight;
                                    break Label_0552;
                                }
                                break Label_0552;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (BarLineChartBase$2.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendHorizontalAlignment[this.mLegend.getHorizontalAlignment().ordinal()]) {
                            default: {
                                break Label_0552;
                            }
                            case 3: {
                                switch (BarLineChartBase$2.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment[this.mLegend.getVerticalAlignment().ordinal()]) {
                                    default: {
                                        break Label_0552;
                                    }
                                    case 2: {
                                        rectF.bottom += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
                                        break Label_0552;
                                    }
                                    case 1: {
                                        rectF.top += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
                                        break Label_0552;
                                    }
                                }
                                break;
                            }
                            case 2: {
                                rectF.right += Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getXOffset();
                                break Label_0552;
                            }
                            case 1: {
                                rectF.left += Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getXOffset();
                                break Label_0552;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
    
    public void calculateOffsets() {
        if (!this.mCustomViewPortEnabled) {
            this.calculateLegendOffsets(this.mOffsetsBuffer);
            final float n = this.mOffsetsBuffer.left + 0.0f;
            final float n2 = this.mOffsetsBuffer.top + 0.0f;
            final float n3 = this.mOffsetsBuffer.right + 0.0f;
            final float n4 = 0.0f + this.mOffsetsBuffer.bottom;
            float n5 = n;
            if (this.mAxisLeft.needsOffset()) {
                n5 = n + this.mAxisLeft.getRequiredWidthSpace(this.mAxisRendererLeft.getPaintAxisLabels());
            }
            float n6 = n3;
            if (this.mAxisRight.needsOffset()) {
                n6 = n3 + this.mAxisRight.getRequiredWidthSpace(this.mAxisRendererRight.getPaintAxisLabels());
            }
            float n7 = n4;
            float n8 = n2;
            if (this.mXAxis.isEnabled()) {
                n7 = n4;
                n8 = n2;
                if (this.mXAxis.isDrawLabelsEnabled()) {
                    final float n9 = this.mXAxis.mLabelRotatedHeight + this.mXAxis.getYOffset();
                    if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                        n7 = n4 + n9;
                        n8 = n2;
                    }
                    else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                        n8 = n2 + n9;
                        n7 = n4;
                    }
                    else {
                        n7 = n4;
                        n8 = n2;
                        if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                            n7 = n4 + n9;
                            n8 = n2 + n9;
                        }
                    }
                }
            }
            final float n10 = n8 + this.getExtraTopOffset();
            final float n11 = n6 + this.getExtraRightOffset();
            final float n12 = n7 + this.getExtraBottomOffset();
            final float n13 = n5 + this.getExtraLeftOffset();
            final float convertDpToPixel = Utils.convertDpToPixel(this.mMinOffset);
            this.mViewPortHandler.restrainViewPort(Math.max(convertDpToPixel, n13), Math.max(convertDpToPixel, n10), Math.max(convertDpToPixel, n11), Math.max(convertDpToPixel, n12));
            if (this.mLogEnabled) {
                final StringBuilder sb = new StringBuilder();
                sb.append("offsetLeft: ");
                sb.append(n13);
                sb.append(", offsetTop: ");
                sb.append(n10);
                sb.append(", offsetRight: ");
                sb.append(n11);
                sb.append(", offsetBottom: ");
                sb.append(n12);
                Log.i("MPAndroidChart", sb.toString());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Content: ");
                sb2.append(this.mViewPortHandler.getContentRect().toString());
                Log.i("MPAndroidChart", sb2.toString());
            }
        }
        this.prepareOffsetMatrix();
        this.prepareValuePxMatrix();
    }
    
    public void centerViewTo(final float n, final float n2, final YAxis.AxisDependency axisDependency) {
        this.addViewportJob(MoveViewJob.getInstance(this.mViewPortHandler, n - this.getXAxis().mAxisRange / this.mViewPortHandler.getScaleX() / 2.0f, n2 + this.getAxisRange(axisDependency) / this.mViewPortHandler.getScaleY() / 2.0f, this.getTransformer(axisDependency), (View)this));
    }
    
    @TargetApi(11)
    public void centerViewToAnimated(final float n, final float n2, final YAxis.AxisDependency axisDependency, final long n3) {
        if (Build$VERSION.SDK_INT >= 11) {
            final MPPointD valuesByTouchPoint = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axisDependency);
            this.addViewportJob(AnimatedMoveViewJob.getInstance(this.mViewPortHandler, n - this.getXAxis().mAxisRange / this.mViewPortHandler.getScaleX() / 2.0f, n2 + this.getAxisRange(axisDependency) / this.mViewPortHandler.getScaleY() / 2.0f, this.getTransformer(axisDependency), (View)this, (float)valuesByTouchPoint.x, (float)valuesByTouchPoint.y, n3));
            MPPointD.recycleInstance(valuesByTouchPoint);
        }
        else {
            Log.e("MPAndroidChart", "Unable to execute centerViewToAnimated(...) on API level < 11");
        }
    }
    
    public void centerViewToY(final float n, final YAxis.AxisDependency axisDependency) {
        this.addViewportJob(MoveViewJob.getInstance(this.mViewPortHandler, 0.0f, n + this.getAxisRange(axisDependency) / this.mViewPortHandler.getScaleY() / 2.0f, this.getTransformer(axisDependency), (View)this));
    }
    
    public void computeScroll() {
        if (this.mChartTouchListener instanceof BarLineChartTouchListener) {
            ((BarLineChartTouchListener)this.mChartTouchListener).computeScroll();
        }
    }
    
    protected void drawGridBackground(final Canvas canvas) {
        if (this.mDrawGridBackground) {
            canvas.drawRect(this.mViewPortHandler.getContentRect(), this.mGridBackgroundPaint);
        }
        if (this.mDrawBorders) {
            canvas.drawRect(this.mViewPortHandler.getContentRect(), this.mBorderPaint);
        }
    }
    
    public void fitScreen() {
        final Matrix mFitScreenMatrixBuffer = this.mFitScreenMatrixBuffer;
        this.mViewPortHandler.fitScreen(mFitScreenMatrixBuffer);
        this.mViewPortHandler.refresh(mFitScreenMatrixBuffer, (View)this, false);
        this.calculateOffsets();
        this.postInvalidate();
    }
    
    public YAxis getAxis(final YAxis.AxisDependency axisDependency) {
        if (axisDependency == YAxis.AxisDependency.LEFT) {
            return this.mAxisLeft;
        }
        return this.mAxisRight;
    }
    
    public YAxis getAxisLeft() {
        return this.mAxisLeft;
    }
    
    protected float getAxisRange(final YAxis.AxisDependency axisDependency) {
        if (axisDependency == YAxis.AxisDependency.LEFT) {
            return this.mAxisLeft.mAxisRange;
        }
        return this.mAxisRight.mAxisRange;
    }
    
    public YAxis getAxisRight() {
        return this.mAxisRight;
    }
    
    public IBarLineScatterCandleBubbleDataSet getDataSetByTouchPoint(final float n, final float n2) {
        final Highlight highlightByTouchPoint = this.getHighlightByTouchPoint(n, n2);
        if (highlightByTouchPoint != null) {
            return (IBarLineScatterCandleBubbleDataSet)this.mData.getDataSetByIndex(highlightByTouchPoint.getDataSetIndex());
        }
        return null;
    }
    
    public OnDrawListener getDrawListener() {
        return this.mDrawListener;
    }
    
    public Entry getEntryByTouchPoint(final float n, final float n2) {
        final Highlight highlightByTouchPoint = this.getHighlightByTouchPoint(n, n2);
        if (highlightByTouchPoint != null) {
            return this.mData.getEntryForHighlight(highlightByTouchPoint);
        }
        return null;
    }
    
    @Override
    public float getHighestVisibleX() {
        this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.posForGetHighestVisibleX);
        return (float)Math.min(this.mXAxis.mAxisMaximum, this.posForGetHighestVisibleX.x);
    }
    
    @Override
    public float getLowestVisibleX() {
        this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.posForGetLowestVisibleX);
        return (float)Math.max(this.mXAxis.mAxisMinimum, this.posForGetLowestVisibleX.x);
    }
    
    public int getMaxVisibleCount() {
        return this.mMaxVisibleCount;
    }
    
    public float getMinOffset() {
        return this.mMinOffset;
    }
    
    @Override
    public Paint getPaint(final int n) {
        final Paint paint = super.getPaint(n);
        if (paint != null) {
            return paint;
        }
        if (n != 4) {
            return null;
        }
        return this.mGridBackgroundPaint;
    }
    
    public MPPointD getPixelForValues(final float n, final float n2, final YAxis.AxisDependency axisDependency) {
        return this.getTransformer(axisDependency).getPixelForValues(n, n2);
    }
    
    public MPPointF getPosition(final Entry entry, final YAxis.AxisDependency axisDependency) {
        if (entry == null) {
            return null;
        }
        this.mGetPositionBuffer[0] = entry.getX();
        this.mGetPositionBuffer[1] = entry.getY();
        this.getTransformer(axisDependency).pointValuesToPixel(this.mGetPositionBuffer);
        return MPPointF.getInstance(this.mGetPositionBuffer[0], this.mGetPositionBuffer[1]);
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
        if (this.mViewPortHandler == null) {
            return 1.0f;
        }
        return this.mViewPortHandler.getScaleX();
    }
    
    public float getScaleY() {
        if (this.mViewPortHandler == null) {
            return 1.0f;
        }
        return this.mViewPortHandler.getScaleY();
    }
    
    @Override
    public Transformer getTransformer(final YAxis.AxisDependency axisDependency) {
        if (axisDependency == YAxis.AxisDependency.LEFT) {
            return this.mLeftAxisTransformer;
        }
        return this.mRightAxisTransformer;
    }
    
    public MPPointD getValuesByTouchPoint(final float n, final float n2, final YAxis.AxisDependency axisDependency) {
        final MPPointD instance = MPPointD.getInstance(0.0, 0.0);
        this.getValuesByTouchPoint(n, n2, axisDependency, instance);
        return instance;
    }
    
    public void getValuesByTouchPoint(final float n, final float n2, final YAxis.AxisDependency axisDependency, final MPPointD mpPointD) {
        this.getTransformer(axisDependency).getValuesByTouchPoint(n, n2, mpPointD);
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
    
    @Override
    protected void init() {
        super.init();
        this.mAxisLeft = new YAxis(YAxis.AxisDependency.LEFT);
        this.mAxisRight = new YAxis(YAxis.AxisDependency.RIGHT);
        this.mLeftAxisTransformer = new Transformer(this.mViewPortHandler);
        this.mRightAxisTransformer = new Transformer(this.mViewPortHandler);
        this.mAxisRendererLeft = new YAxisRenderer(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
        this.mAxisRendererRight = new YAxisRenderer(this.mViewPortHandler, this.mAxisRight, this.mRightAxisTransformer);
        this.mXAxisRenderer = new XAxisRenderer(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer);
        this.setHighlighter(new ChartHighlighter((T)this));
        this.mChartTouchListener = new BarLineChartTouchListener(this, this.mViewPortHandler.getMatrixTouch(), 3.0f);
        (this.mGridBackgroundPaint = new Paint()).setStyle(Paint$Style.FILL);
        this.mGridBackgroundPaint.setColor(Color.rgb(240, 240, 240));
        (this.mBorderPaint = new Paint()).setStyle(Paint$Style.STROKE);
        this.mBorderPaint.setColor(-16777216);
        this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0f));
    }
    
    public boolean isAnyAxisInverted() {
        return this.mAxisLeft.isInverted() || this.mAxisRight.isInverted();
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
        return this.mDragXEnabled || this.mDragYEnabled;
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
    
    @Override
    public boolean isInverted(final YAxis.AxisDependency axisDependency) {
        return this.getAxis(axisDependency).isInverted();
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
    
    public void moveViewTo(final float n, final float n2, final YAxis.AxisDependency axisDependency) {
        this.addViewportJob(MoveViewJob.getInstance(this.mViewPortHandler, n, n2 + this.getAxisRange(axisDependency) / this.mViewPortHandler.getScaleY() / 2.0f, this.getTransformer(axisDependency), (View)this));
    }
    
    @TargetApi(11)
    public void moveViewToAnimated(final float n, final float n2, final YAxis.AxisDependency axisDependency, final long n3) {
        if (Build$VERSION.SDK_INT >= 11) {
            final MPPointD valuesByTouchPoint = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axisDependency);
            this.addViewportJob(AnimatedMoveViewJob.getInstance(this.mViewPortHandler, n, n2 + this.getAxisRange(axisDependency) / this.mViewPortHandler.getScaleY() / 2.0f, this.getTransformer(axisDependency), (View)this, (float)valuesByTouchPoint.x, (float)valuesByTouchPoint.y, n3));
            MPPointD.recycleInstance(valuesByTouchPoint);
        }
        else {
            Log.e("MPAndroidChart", "Unable to execute moveViewToAnimated(...) on API level < 11");
        }
    }
    
    public void moveViewToX(final float n) {
        this.addViewportJob(MoveViewJob.getInstance(this.mViewPortHandler, n, 0.0f, this.getTransformer(YAxis.AxisDependency.LEFT), (View)this));
    }
    
    @Override
    public void notifyDataSetChanged() {
        if (this.mData == null) {
            if (this.mLogEnabled) {
                Log.i("MPAndroidChart", "Preparing... DATA NOT SET.");
            }
            return;
        }
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
    
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mData == null) {
            return;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        this.drawGridBackground(canvas);
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
        this.mXAxisRenderer.renderAxisLine(canvas);
        this.mAxisRendererLeft.renderAxisLine(canvas);
        this.mAxisRendererRight.renderAxisLine(canvas);
        this.mXAxisRenderer.renderGridLines(canvas);
        this.mAxisRendererLeft.renderGridLines(canvas);
        this.mAxisRendererRight.renderGridLines(canvas);
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLimitLinesBehindDataEnabled()) {
            this.mXAxisRenderer.renderLimitLines(canvas);
        }
        if (this.mAxisLeft.isEnabled() && this.mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererLeft.renderLimitLines(canvas);
        }
        if (this.mAxisRight.isEnabled() && this.mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererRight.renderLimitLines(canvas);
        }
        final int save = canvas.save();
        canvas.clipRect(this.mViewPortHandler.getContentRect());
        this.mRenderer.drawData(canvas);
        if (this.valuesToHighlight()) {
            this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
        }
        canvas.restoreToCount(save);
        this.mRenderer.drawExtras(canvas);
        if (this.mXAxis.isEnabled() && !this.mXAxis.isDrawLimitLinesBehindDataEnabled()) {
            this.mXAxisRenderer.renderLimitLines(canvas);
        }
        if (this.mAxisLeft.isEnabled() && !this.mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererLeft.renderLimitLines(canvas);
        }
        if (this.mAxisRight.isEnabled() && !this.mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererRight.renderLimitLines(canvas);
        }
        this.mXAxisRenderer.renderAxisLabels(canvas);
        this.mAxisRendererLeft.renderAxisLabels(canvas);
        this.mAxisRendererRight.renderAxisLabels(canvas);
        if (this.isClipValuesToContentEnabled()) {
            final int save2 = canvas.save();
            canvas.clipRect(this.mViewPortHandler.getContentRect());
            this.mRenderer.drawValues(canvas);
            canvas.restoreToCount(save2);
        }
        else {
            this.mRenderer.drawValues(canvas);
        }
        this.mLegendRenderer.renderLegend(canvas);
        this.drawDescription(canvas);
        this.drawMarkers(canvas);
        if (this.mLogEnabled) {
            final long lng = System.currentTimeMillis() - currentTimeMillis;
            this.totalTime += lng;
            ++this.drawCycles;
            final long lng2 = this.totalTime / this.drawCycles;
            final StringBuilder sb = new StringBuilder();
            sb.append("Drawtime: ");
            sb.append(lng);
            sb.append(" ms, average: ");
            sb.append(lng2);
            sb.append(" ms, cycles: ");
            sb.append(this.drawCycles);
            Log.i("MPAndroidChart", sb.toString());
        }
    }
    
    @Override
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        this.mOnSizeChangedBuffer[0] = (this.mOnSizeChangedBuffer[1] = 0.0f);
        if (this.mKeepPositionOnRotation) {
            this.mOnSizeChangedBuffer[0] = this.mViewPortHandler.contentLeft();
            this.mOnSizeChangedBuffer[1] = this.mViewPortHandler.contentTop();
            this.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(this.mOnSizeChangedBuffer);
        }
        super.onSizeChanged(n, n2, n3, n4);
        if (this.mKeepPositionOnRotation) {
            this.getTransformer(YAxis.AxisDependency.LEFT).pointValuesToPixel(this.mOnSizeChangedBuffer);
            this.mViewPortHandler.centerViewPort(this.mOnSizeChangedBuffer, (View)this);
        }
        else {
            this.mViewPortHandler.refresh(this.mViewPortHandler.getMatrixTouch(), (View)this, true);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return this.mChartTouchListener != null && this.mData != null && this.mTouchEnabled && this.mChartTouchListener.onTouch((View)this, motionEvent);
    }
    
    protected void prepareOffsetMatrix() {
        this.mRightAxisTransformer.prepareMatrixOffset(this.mAxisRight.isInverted());
        this.mLeftAxisTransformer.prepareMatrixOffset(this.mAxisLeft.isInverted());
    }
    
    protected void prepareValuePxMatrix() {
        if (this.mLogEnabled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Preparing Value-Px Matrix, xmin: ");
            sb.append(this.mXAxis.mAxisMinimum);
            sb.append(", xmax: ");
            sb.append(this.mXAxis.mAxisMaximum);
            sb.append(", xdelta: ");
            sb.append(this.mXAxis.mAxisRange);
            Log.i("MPAndroidChart", sb.toString());
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
        this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, (View)this, false);
        this.calculateOffsets();
        this.postInvalidate();
    }
    
    public void setAutoScaleMinMaxEnabled(final boolean mAutoScaleMinMaxEnabled) {
        this.mAutoScaleMinMaxEnabled = mAutoScaleMinMaxEnabled;
    }
    
    public void setBorderColor(final int color) {
        this.mBorderPaint.setColor(color);
    }
    
    public void setBorderWidth(final float n) {
        this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(n));
    }
    
    public void setClipValuesToContent(final boolean mClipValuesToContent) {
        this.mClipValuesToContent = mClipValuesToContent;
    }
    
    public void setDoubleTapToZoomEnabled(final boolean mDoubleTapToZoomEnabled) {
        this.mDoubleTapToZoomEnabled = mDoubleTapToZoomEnabled;
    }
    
    public void setDragEnabled(final boolean b) {
        this.mDragXEnabled = b;
        this.mDragYEnabled = b;
    }
    
    public void setDragOffsetX(final float dragOffsetX) {
        this.mViewPortHandler.setDragOffsetX(dragOffsetX);
    }
    
    public void setDragOffsetY(final float dragOffsetY) {
        this.mViewPortHandler.setDragOffsetY(dragOffsetY);
    }
    
    public void setDragXEnabled(final boolean mDragXEnabled) {
        this.mDragXEnabled = mDragXEnabled;
    }
    
    public void setDragYEnabled(final boolean mDragYEnabled) {
        this.mDragYEnabled = mDragYEnabled;
    }
    
    public void setDrawBorders(final boolean mDrawBorders) {
        this.mDrawBorders = mDrawBorders;
    }
    
    public void setDrawGridBackground(final boolean mDrawGridBackground) {
        this.mDrawGridBackground = mDrawGridBackground;
    }
    
    public void setGridBackgroundColor(final int color) {
        this.mGridBackgroundPaint.setColor(color);
    }
    
    public void setHighlightPerDragEnabled(final boolean mHighlightPerDragEnabled) {
        this.mHighlightPerDragEnabled = mHighlightPerDragEnabled;
    }
    
    public void setKeepPositionOnRotation(final boolean mKeepPositionOnRotation) {
        this.mKeepPositionOnRotation = mKeepPositionOnRotation;
    }
    
    public void setMaxVisibleValueCount(final int mMaxVisibleCount) {
        this.mMaxVisibleCount = mMaxVisibleCount;
    }
    
    public void setMinOffset(final float mMinOffset) {
        this.mMinOffset = mMinOffset;
    }
    
    public void setOnDrawListener(final OnDrawListener mDrawListener) {
        this.mDrawListener = mDrawListener;
    }
    
    @Override
    public void setPaint(final Paint mGridBackgroundPaint, final int n) {
        super.setPaint(mGridBackgroundPaint, n);
        if (n == 4) {
            this.mGridBackgroundPaint = mGridBackgroundPaint;
        }
    }
    
    public void setPinchZoom(final boolean mPinchZoomEnabled) {
        this.mPinchZoomEnabled = mPinchZoomEnabled;
    }
    
    public void setRendererLeftYAxis(final YAxisRenderer mAxisRendererLeft) {
        this.mAxisRendererLeft = mAxisRendererLeft;
    }
    
    public void setRendererRightYAxis(final YAxisRenderer mAxisRendererRight) {
        this.mAxisRendererRight = mAxisRendererRight;
    }
    
    public void setScaleEnabled(final boolean b) {
        this.mScaleXEnabled = b;
        this.mScaleYEnabled = b;
    }
    
    public void setScaleMinima(final float minimumScaleX, final float minimumScaleY) {
        this.mViewPortHandler.setMinimumScaleX(minimumScaleX);
        this.mViewPortHandler.setMinimumScaleY(minimumScaleY);
    }
    
    public void setScaleXEnabled(final boolean mScaleXEnabled) {
        this.mScaleXEnabled = mScaleXEnabled;
    }
    
    public void setScaleYEnabled(final boolean mScaleYEnabled) {
        this.mScaleYEnabled = mScaleYEnabled;
    }
    
    public void setViewPortOffsets(final float n, final float n2, final float n3, final float n4) {
        this.mCustomViewPortEnabled = true;
        this.post((Runnable)new Runnable() {
            @Override
            public void run() {
                BarLineChartBase.this.mViewPortHandler.restrainViewPort(n, n2, n3, n4);
                BarLineChartBase.this.prepareOffsetMatrix();
                BarLineChartBase.this.prepareValuePxMatrix();
            }
        });
    }
    
    public void setVisibleXRange(float n, float n2) {
        n = this.mXAxis.mAxisRange / n;
        n2 = this.mXAxis.mAxisRange / n2;
        this.mViewPortHandler.setMinMaxScaleX(n, n2);
    }
    
    public void setVisibleXRangeMaximum(float minimumScaleX) {
        minimumScaleX = this.mXAxis.mAxisRange / minimumScaleX;
        this.mViewPortHandler.setMinimumScaleX(minimumScaleX);
    }
    
    public void setVisibleXRangeMinimum(float maximumScaleX) {
        maximumScaleX = this.mXAxis.mAxisRange / maximumScaleX;
        this.mViewPortHandler.setMaximumScaleX(maximumScaleX);
    }
    
    public void setVisibleYRange(float n, float n2, final YAxis.AxisDependency axisDependency) {
        n = this.getAxisRange(axisDependency) / n;
        n2 = this.getAxisRange(axisDependency) / n2;
        this.mViewPortHandler.setMinMaxScaleY(n, n2);
    }
    
    public void setVisibleYRangeMaximum(float minimumScaleY, final YAxis.AxisDependency axisDependency) {
        minimumScaleY = this.getAxisRange(axisDependency) / minimumScaleY;
        this.mViewPortHandler.setMinimumScaleY(minimumScaleY);
    }
    
    public void setVisibleYRangeMinimum(float maximumScaleY, final YAxis.AxisDependency axisDependency) {
        maximumScaleY = this.getAxisRange(axisDependency) / maximumScaleY;
        this.mViewPortHandler.setMaximumScaleY(maximumScaleY);
    }
    
    public void setXAxisRenderer(final XAxisRenderer mxAxisRenderer) {
        this.mXAxisRenderer = mxAxisRenderer;
    }
    
    public void zoom(final float n, final float n2, final float n3, final float n4) {
        this.mViewPortHandler.zoom(n, n2, n3, -n4, this.mZoomMatrixBuffer);
        this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, (View)this, false);
        this.calculateOffsets();
        this.postInvalidate();
    }
    
    public void zoom(final float n, final float n2, final float n3, final float n4, final YAxis.AxisDependency axisDependency) {
        this.addViewportJob(ZoomJob.getInstance(this.mViewPortHandler, n, n2, n3, n4, this.getTransformer(axisDependency), axisDependency, (View)this));
    }
    
    @TargetApi(11)
    public void zoomAndCenterAnimated(final float n, final float n2, final float n3, final float n4, final YAxis.AxisDependency axisDependency, final long n5) {
        if (Build$VERSION.SDK_INT >= 11) {
            final MPPointD valuesByTouchPoint = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axisDependency);
            this.addViewportJob(AnimatedZoomJob.getInstance(this.mViewPortHandler, (View)this, this.getTransformer(axisDependency), this.getAxis(axisDependency), this.mXAxis.mAxisRange, n, n2, this.mViewPortHandler.getScaleX(), this.mViewPortHandler.getScaleY(), n3, n4, (float)valuesByTouchPoint.x, (float)valuesByTouchPoint.y, n5));
            MPPointD.recycleInstance(valuesByTouchPoint);
        }
        else {
            Log.e("MPAndroidChart", "Unable to execute zoomAndCenterAnimated(...) on API level < 11");
        }
    }
    
    public void zoomIn() {
        final MPPointF contentCenter = this.mViewPortHandler.getContentCenter();
        this.mViewPortHandler.zoomIn(contentCenter.x, -contentCenter.y, this.mZoomMatrixBuffer);
        this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, (View)this, false);
        MPPointF.recycleInstance(contentCenter);
        this.calculateOffsets();
        this.postInvalidate();
    }
    
    public void zoomOut() {
        final MPPointF contentCenter = this.mViewPortHandler.getContentCenter();
        this.mViewPortHandler.zoomOut(contentCenter.x, -contentCenter.y, this.mZoomMatrixBuffer);
        this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, (View)this, false);
        MPPointF.recycleInstance(contentCenter);
        this.calculateOffsets();
        this.postInvalidate();
    }
    
    public void zoomToCenter(final float n, final float n2) {
        final MPPointF centerOffsets = this.getCenterOffsets();
        final Matrix mZoomMatrixBuffer = this.mZoomMatrixBuffer;
        this.mViewPortHandler.zoom(n, n2, centerOffsets.x, -centerOffsets.y, mZoomMatrixBuffer);
        this.mViewPortHandler.refresh(mZoomMatrixBuffer, (View)this, false);
    }
}
