// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import android.graphics.Canvas;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.highlight.RadarHighlighter;
import com.github.mikephil.charting.renderer.RadarChartRenderer;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.RectF;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import android.util.AttributeSet;
import android.graphics.Color;
import android.content.Context;
import com.github.mikephil.charting.renderer.YAxisRendererRadarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.XAxisRendererRadarChart;
import com.github.mikephil.charting.data.RadarData;

public class RadarChart extends PieRadarChartBase<RadarData>
{
    private boolean mDrawWeb;
    private float mInnerWebLineWidth;
    private int mSkipWebLineCount;
    private int mWebAlpha;
    private int mWebColor;
    private int mWebColorInner;
    private float mWebLineWidth;
    protected XAxisRendererRadarChart mXAxisRenderer;
    private YAxis mYAxis;
    protected YAxisRendererRadarChart mYAxisRenderer;
    
    public RadarChart(final Context context) {
        super(context);
        this.mWebLineWidth = 2.5f;
        this.mInnerWebLineWidth = 1.5f;
        this.mWebColor = Color.rgb(122, 122, 122);
        this.mWebColorInner = Color.rgb(122, 122, 122);
        this.mWebAlpha = 150;
        this.mDrawWeb = true;
        this.mSkipWebLineCount = 0;
    }
    
    public RadarChart(final Context context, final AttributeSet set) {
        super(context, set);
        this.mWebLineWidth = 2.5f;
        this.mInnerWebLineWidth = 1.5f;
        this.mWebColor = Color.rgb(122, 122, 122);
        this.mWebColorInner = Color.rgb(122, 122, 122);
        this.mWebAlpha = 150;
        this.mDrawWeb = true;
        this.mSkipWebLineCount = 0;
    }
    
    public RadarChart(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mWebLineWidth = 2.5f;
        this.mInnerWebLineWidth = 1.5f;
        this.mWebColor = Color.rgb(122, 122, 122);
        this.mWebColorInner = Color.rgb(122, 122, 122);
        this.mWebAlpha = 150;
        this.mDrawWeb = true;
        this.mSkipWebLineCount = 0;
    }
    
    @Override
    protected void calcMinMax() {
        super.calcMinMax();
        this.mYAxis.calculate(((RadarData)this.mData).getYMin(YAxis.AxisDependency.LEFT), ((RadarData)this.mData).getYMax(YAxis.AxisDependency.LEFT));
        this.mXAxis.calculate(0.0f, (float)((RadarData)this.mData).getMaxEntryCountSet().getEntryCount());
    }
    
    public float getFactor() {
        final RectF contentRect = this.mViewPortHandler.getContentRect();
        return Math.min(contentRect.width() / 2.0f, contentRect.height() / 2.0f) / this.mYAxis.mAxisRange;
    }
    
    @Override
    public int getIndexForAngle(float sliceAngle) {
        final float normalizedAngle = Utils.getNormalizedAngle(sliceAngle - this.getRotationAngle());
        sliceAngle = this.getSliceAngle();
        final int entryCount = ((RadarData)this.mData).getMaxEntryCountSet().getEntryCount();
        final int n = 0;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= entryCount) {
                break;
            }
            final int n4 = n2 + 1;
            if (n4 * sliceAngle - sliceAngle / 2.0f > normalizedAngle) {
                n3 = n2;
                break;
            }
            n2 = n4;
        }
        return n3;
    }
    
    @Override
    public float getRadius() {
        final RectF contentRect = this.mViewPortHandler.getContentRect();
        return Math.min(contentRect.width() / 2.0f, contentRect.height() / 2.0f);
    }
    
    @Override
    protected float getRequiredBaseOffset() {
        float convertDpToPixel;
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
            convertDpToPixel = (float)this.mXAxis.mLabelRotatedWidth;
        }
        else {
            convertDpToPixel = Utils.convertDpToPixel(10.0f);
        }
        return convertDpToPixel;
    }
    
    @Override
    protected float getRequiredLegendOffset() {
        return this.mLegendRenderer.getLabelPaint().getTextSize() * 4.0f;
    }
    
    public int getSkipWebLineCount() {
        return this.mSkipWebLineCount;
    }
    
    public float getSliceAngle() {
        return 360.0f / ((RadarData)this.mData).getMaxEntryCountSet().getEntryCount();
    }
    
    public int getWebAlpha() {
        return this.mWebAlpha;
    }
    
    public int getWebColor() {
        return this.mWebColor;
    }
    
    public int getWebColorInner() {
        return this.mWebColorInner;
    }
    
    public float getWebLineWidth() {
        return this.mWebLineWidth;
    }
    
    public float getWebLineWidthInner() {
        return this.mInnerWebLineWidth;
    }
    
    public YAxis getYAxis() {
        return this.mYAxis;
    }
    
    @Override
    public float getYChartMax() {
        return this.mYAxis.mAxisMaximum;
    }
    
    @Override
    public float getYChartMin() {
        return this.mYAxis.mAxisMinimum;
    }
    
    public float getYRange() {
        return this.mYAxis.mAxisRange;
    }
    
    @Override
    protected void init() {
        super.init();
        this.mYAxis = new YAxis(YAxis.AxisDependency.LEFT);
        this.mWebLineWidth = Utils.convertDpToPixel(1.5f);
        this.mInnerWebLineWidth = Utils.convertDpToPixel(0.75f);
        this.mRenderer = new RadarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mYAxisRenderer = new YAxisRendererRadarChart(this.mViewPortHandler, this.mYAxis, this);
        this.mXAxisRenderer = new XAxisRendererRadarChart(this.mViewPortHandler, this.mXAxis, this);
        this.mHighlighter = new RadarHighlighter(this);
    }
    
    @Override
    public void notifyDataSetChanged() {
        if (this.mData == null) {
            return;
        }
        this.calcMinMax();
        this.mYAxisRenderer.computeAxis(this.mYAxis.mAxisMinimum, this.mYAxis.mAxisMaximum, this.mYAxis.isInverted());
        this.mXAxisRenderer.computeAxis(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisMaximum, false);
        if (this.mLegend != null && !this.mLegend.isLegendCustom()) {
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
        if (this.mXAxis.isEnabled()) {
            this.mXAxisRenderer.computeAxis(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisMaximum, false);
        }
        this.mXAxisRenderer.renderAxisLabels(canvas);
        if (this.mDrawWeb) {
            this.mRenderer.drawExtras(canvas);
        }
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLimitLinesBehindDataEnabled()) {
            this.mYAxisRenderer.renderLimitLines(canvas);
        }
        this.mRenderer.drawData(canvas);
        if (this.valuesToHighlight()) {
            this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
        }
        if (this.mYAxis.isEnabled() && !this.mYAxis.isDrawLimitLinesBehindDataEnabled()) {
            this.mYAxisRenderer.renderLimitLines(canvas);
        }
        this.mYAxisRenderer.renderAxisLabels(canvas);
        this.mRenderer.drawValues(canvas);
        this.mLegendRenderer.renderLegend(canvas);
        this.drawDescription(canvas);
        this.drawMarkers(canvas);
    }
    
    public void setDrawWeb(final boolean mDrawWeb) {
        this.mDrawWeb = mDrawWeb;
    }
    
    public void setSkipWebLineCount(final int b) {
        this.mSkipWebLineCount = Math.max(0, b);
    }
    
    public void setWebAlpha(final int mWebAlpha) {
        this.mWebAlpha = mWebAlpha;
    }
    
    public void setWebColor(final int mWebColor) {
        this.mWebColor = mWebColor;
    }
    
    public void setWebColorInner(final int mWebColorInner) {
        this.mWebColorInner = mWebColorInner;
    }
    
    public void setWebLineWidth(final float n) {
        this.mWebLineWidth = Utils.convertDpToPixel(n);
    }
    
    public void setWebLineWidthInner(final float n) {
        this.mInnerWebLineWidth = Utils.convertDpToPixel(n);
    }
}
