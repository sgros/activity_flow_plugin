// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.highlight.CombinedHighlighter;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.LineData;
import android.util.Log;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider;
import com.github.mikephil.charting.data.CombinedData;

public class CombinedChart extends BarLineChartBase<CombinedData> implements CombinedDataProvider
{
    private boolean mDrawBarShadow;
    protected DrawOrder[] mDrawOrder;
    private boolean mDrawValueAboveBar;
    protected boolean mHighlightFullBarEnabled;
    
    public CombinedChart(final Context context) {
        super(context);
        this.mDrawValueAboveBar = true;
        this.mHighlightFullBarEnabled = false;
        this.mDrawBarShadow = false;
    }
    
    public CombinedChart(final Context context, final AttributeSet set) {
        super(context, set);
        this.mDrawValueAboveBar = true;
        this.mHighlightFullBarEnabled = false;
        this.mDrawBarShadow = false;
    }
    
    public CombinedChart(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mDrawValueAboveBar = true;
        this.mHighlightFullBarEnabled = false;
        this.mDrawBarShadow = false;
    }
    
    @Override
    protected void drawMarkers(final Canvas canvas) {
        if (this.mMarker != null && this.isDrawMarkersEnabled() && this.valuesToHighlight()) {
            for (int i = 0; i < this.mIndicesToHighlight.length; ++i) {
                final Highlight highlight = this.mIndicesToHighlight[i];
                final IBarLineScatterCandleBubbleDataSet<? extends Entry> dataSetByHighlight = ((CombinedData)this.mData).getDataSetByHighlight(highlight);
                final Entry entryForHighlight = ((CombinedData)this.mData).getEntryForHighlight(highlight);
                if (entryForHighlight != null) {
                    if (dataSetByHighlight.getEntryIndex(entryForHighlight) <= dataSetByHighlight.getEntryCount() * this.mAnimator.getPhaseX()) {
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
    
    @Override
    public BarData getBarData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData)this.mData).getBarData();
    }
    
    @Override
    public BubbleData getBubbleData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData)this.mData).getBubbleData();
    }
    
    @Override
    public CandleData getCandleData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData)this.mData).getCandleData();
    }
    
    @Override
    public CombinedData getCombinedData() {
        return (CombinedData)this.mData;
    }
    
    public DrawOrder[] getDrawOrder() {
        return this.mDrawOrder;
    }
    
    @Override
    public Highlight getHighlightByTouchPoint(final float n, final float n2) {
        if (this.mData == null) {
            Log.e("MPAndroidChart", "Can't select by touch. No data set.");
            return null;
        }
        final Highlight highlight = this.getHighlighter().getHighlight(n, n2);
        if (highlight != null && this.isHighlightFullBarEnabled()) {
            return new Highlight(highlight.getX(), highlight.getY(), highlight.getXPx(), highlight.getYPx(), highlight.getDataSetIndex(), -1, highlight.getAxis());
        }
        return highlight;
    }
    
    @Override
    public LineData getLineData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData)this.mData).getLineData();
    }
    
    @Override
    public ScatterData getScatterData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData)this.mData).getScatterData();
    }
    
    @Override
    protected void init() {
        super.init();
        this.mDrawOrder = new DrawOrder[] { DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.LINE, DrawOrder.CANDLE, DrawOrder.SCATTER };
        this.setHighlighter(new CombinedHighlighter(this, this));
        this.setHighlightFullBarEnabled(true);
        this.mRenderer = new CombinedChartRenderer(this, this.mAnimator, this.mViewPortHandler);
    }
    
    @Override
    public boolean isDrawBarShadowEnabled() {
        return this.mDrawBarShadow;
    }
    
    @Override
    public boolean isDrawValueAboveBarEnabled() {
        return this.mDrawValueAboveBar;
    }
    
    @Override
    public boolean isHighlightFullBarEnabled() {
        return this.mHighlightFullBarEnabled;
    }
    
    @Override
    public void setData(final CombinedData data) {
        super.setData(data);
        this.setHighlighter(new CombinedHighlighter(this, this));
        ((CombinedChartRenderer)this.mRenderer).createRenderers();
        this.mRenderer.initBuffers();
    }
    
    public void setDrawBarShadow(final boolean mDrawBarShadow) {
        this.mDrawBarShadow = mDrawBarShadow;
    }
    
    public void setDrawOrder(final DrawOrder[] mDrawOrder) {
        if (mDrawOrder != null && mDrawOrder.length > 0) {
            this.mDrawOrder = mDrawOrder;
        }
    }
    
    public void setDrawValueAboveBar(final boolean mDrawValueAboveBar) {
        this.mDrawValueAboveBar = mDrawValueAboveBar;
    }
    
    public void setHighlightFullBarEnabled(final boolean mHighlightFullBarEnabled) {
        this.mHighlightFullBarEnabled = mHighlightFullBarEnabled;
    }
    
    public enum DrawOrder
    {
        BAR, 
        BUBBLE, 
        CANDLE, 
        LINE, 
        SCATTER;
    }
}
