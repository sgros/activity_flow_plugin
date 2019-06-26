// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.renderer.XAxisRendererHorizontalBarChart;
import com.github.mikephil.charting.renderer.YAxisRendererHorizontalBarChart;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.HorizontalBarHighlighter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer;
import com.github.mikephil.charting.utils.TransformerHorizontalBarChart;
import com.github.mikephil.charting.utils.HorizontalViewPortHandler;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import android.util.Log;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.components.XAxis;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.RectF;

public class HorizontalBarChart extends BarChart
{
    protected float[] mGetPositionBuffer;
    private RectF mOffsetsBuffer;
    
    public HorizontalBarChart(final Context context) {
        super(context);
        this.mOffsetsBuffer = new RectF();
        this.mGetPositionBuffer = new float[2];
    }
    
    public HorizontalBarChart(final Context context, final AttributeSet set) {
        super(context, set);
        this.mOffsetsBuffer = new RectF();
        this.mGetPositionBuffer = new float[2];
    }
    
    public HorizontalBarChart(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mOffsetsBuffer = new RectF();
        this.mGetPositionBuffer = new float[2];
    }
    
    @Override
    public void calculateOffsets() {
        this.calculateLegendOffsets(this.mOffsetsBuffer);
        final float n = this.mOffsetsBuffer.left + 0.0f;
        final float n2 = this.mOffsetsBuffer.top + 0.0f;
        final float n3 = this.mOffsetsBuffer.right + 0.0f;
        final float n4 = 0.0f + this.mOffsetsBuffer.bottom;
        float n5 = n2;
        if (this.mAxisLeft.needsOffset()) {
            n5 = n2 + this.mAxisLeft.getRequiredHeightSpace(this.mAxisRendererLeft.getPaintAxisLabels());
        }
        float n6 = n4;
        if (this.mAxisRight.needsOffset()) {
            n6 = n4 + this.mAxisRight.getRequiredHeightSpace(this.mAxisRendererRight.getPaintAxisLabels());
        }
        final float n7 = (float)this.mXAxis.mLabelRotatedWidth;
        float n8 = n;
        float n9 = n3;
        if (this.mXAxis.isEnabled()) {
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                n8 = n + n7;
                n9 = n3;
            }
            else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                n9 = n3 + n7;
                n8 = n;
            }
            else {
                n8 = n;
                n9 = n3;
                if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                    n8 = n + n7;
                    n9 = n3 + n7;
                }
            }
        }
        final float n10 = n5 + this.getExtraTopOffset();
        final float n11 = n9 + this.getExtraRightOffset();
        final float n12 = n6 + this.getExtraBottomOffset();
        final float n13 = n8 + this.getExtraLeftOffset();
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
        this.prepareOffsetMatrix();
        this.prepareValuePxMatrix();
    }
    
    @Override
    public void getBarBounds(final BarEntry barEntry, final RectF rectF) {
        final IBarDataSet set = ((BarData)this.mData).getDataSetForEntry(barEntry);
        if (set == null) {
            rectF.set(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
            return;
        }
        float y = barEntry.getY();
        final float x = barEntry.getX();
        final float n = ((BarData)this.mData).getBarWidth() / 2.0f;
        float n2;
        if (y >= 0.0f) {
            n2 = y;
        }
        else {
            n2 = 0.0f;
        }
        if (y > 0.0f) {
            y = 0.0f;
        }
        rectF.set(n2, x - n, y, x + n);
        this.getTransformer(set.getAxisDependency()).rectValueToPixel(rectF);
    }
    
    @Override
    public float getHighestVisibleX() {
        this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.posForGetHighestVisibleX);
        return (float)Math.min(this.mXAxis.mAxisMaximum, this.posForGetHighestVisibleX.y);
    }
    
    @Override
    public Highlight getHighlightByTouchPoint(final float n, final float n2) {
        if (this.mData == null) {
            if (this.mLogEnabled) {
                Log.e("MPAndroidChart", "Can't select by touch. No data set.");
            }
            return null;
        }
        return this.getHighlighter().getHighlight(n2, n);
    }
    
    @Override
    public float getLowestVisibleX() {
        this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.posForGetLowestVisibleX);
        return (float)Math.max(this.mXAxis.mAxisMinimum, this.posForGetLowestVisibleX.y);
    }
    
    @Override
    protected float[] getMarkerPosition(final Highlight highlight) {
        return new float[] { highlight.getDrawY(), highlight.getDrawX() };
    }
    
    @Override
    public MPPointF getPosition(final Entry entry, final YAxis.AxisDependency axisDependency) {
        if (entry == null) {
            return null;
        }
        final float[] mGetPositionBuffer = this.mGetPositionBuffer;
        mGetPositionBuffer[0] = entry.getY();
        mGetPositionBuffer[1] = entry.getX();
        this.getTransformer(axisDependency).pointValuesToPixel(mGetPositionBuffer);
        return MPPointF.getInstance(mGetPositionBuffer[0], mGetPositionBuffer[1]);
    }
    
    @Override
    protected void init() {
        this.mViewPortHandler = new HorizontalViewPortHandler();
        super.init();
        this.mLeftAxisTransformer = new TransformerHorizontalBarChart(this.mViewPortHandler);
        this.mRightAxisTransformer = new TransformerHorizontalBarChart(this.mViewPortHandler);
        this.mRenderer = new HorizontalBarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.setHighlighter(new HorizontalBarHighlighter(this));
        this.mAxisRendererLeft = new YAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
        this.mAxisRendererRight = new YAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mAxisRight, this.mRightAxisTransformer);
        this.mXAxisRenderer = new XAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer, this);
    }
    
    @Override
    protected void prepareValuePxMatrix() {
        this.mRightAxisTransformer.prepareMatrixValuePx(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisRange, this.mXAxis.mAxisRange, this.mXAxis.mAxisMinimum);
        this.mLeftAxisTransformer.prepareMatrixValuePx(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisRange, this.mXAxis.mAxisRange, this.mXAxis.mAxisMinimum);
    }
    
    @Override
    public void setVisibleXRange(float n, float n2) {
        n = this.mXAxis.mAxisRange / n;
        n2 = this.mXAxis.mAxisRange / n2;
        this.mViewPortHandler.setMinMaxScaleY(n, n2);
    }
    
    @Override
    public void setVisibleXRangeMaximum(float minimumScaleY) {
        minimumScaleY = this.mXAxis.mAxisRange / minimumScaleY;
        this.mViewPortHandler.setMinimumScaleY(minimumScaleY);
    }
    
    @Override
    public void setVisibleXRangeMinimum(float maximumScaleY) {
        maximumScaleY = this.mXAxis.mAxisRange / maximumScaleY;
        this.mViewPortHandler.setMaximumScaleY(maximumScaleY);
    }
    
    @Override
    public void setVisibleYRange(float n, float n2, final YAxis.AxisDependency axisDependency) {
        n = this.getAxisRange(axisDependency) / n;
        n2 = this.getAxisRange(axisDependency) / n2;
        this.mViewPortHandler.setMinMaxScaleX(n, n2);
    }
    
    @Override
    public void setVisibleYRangeMaximum(float minimumScaleX, final YAxis.AxisDependency axisDependency) {
        minimumScaleX = this.getAxisRange(axisDependency) / minimumScaleX;
        this.mViewPortHandler.setMinimumScaleX(minimumScaleX);
    }
    
    @Override
    public void setVisibleYRangeMinimum(float maximumScaleX, final YAxis.AxisDependency axisDependency) {
        maximumScaleX = this.getAxisRange(axisDependency) / maximumScaleX;
        this.mViewPortHandler.setMaximumScaleX(maximumScaleX);
    }
}
