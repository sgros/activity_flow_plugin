// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.BarHighlighter;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import android.util.Log;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import android.graphics.RectF;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.components.YAxis;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.data.BarData;

public class BarChart extends BarLineChartBase<BarData> implements BarDataProvider
{
    private boolean mDrawBarShadow;
    private boolean mDrawValueAboveBar;
    private boolean mFitBars;
    protected boolean mHighlightFullBarEnabled;
    
    public BarChart(final Context context) {
        super(context);
        this.mHighlightFullBarEnabled = false;
        this.mDrawValueAboveBar = true;
        this.mDrawBarShadow = false;
        this.mFitBars = false;
    }
    
    public BarChart(final Context context, final AttributeSet set) {
        super(context, set);
        this.mHighlightFullBarEnabled = false;
        this.mDrawValueAboveBar = true;
        this.mDrawBarShadow = false;
        this.mFitBars = false;
    }
    
    public BarChart(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mHighlightFullBarEnabled = false;
        this.mDrawValueAboveBar = true;
        this.mDrawBarShadow = false;
        this.mFitBars = false;
    }
    
    @Override
    protected void calcMinMax() {
        if (this.mFitBars) {
            this.mXAxis.calculate(((BarData)this.mData).getXMin() - ((BarData)this.mData).getBarWidth() / 2.0f, ((BarData)this.mData).getXMax() + ((BarData)this.mData).getBarWidth() / 2.0f);
        }
        else {
            this.mXAxis.calculate(((BarData)this.mData).getXMin(), ((BarData)this.mData).getXMax());
        }
        this.mAxisLeft.calculate(((BarData)this.mData).getYMin(YAxis.AxisDependency.LEFT), ((BarData)this.mData).getYMax(YAxis.AxisDependency.LEFT));
        this.mAxisRight.calculate(((BarData)this.mData).getYMin(YAxis.AxisDependency.RIGHT), ((BarData)this.mData).getYMax(YAxis.AxisDependency.RIGHT));
    }
    
    public RectF getBarBounds(final BarEntry barEntry) {
        final RectF rectF = new RectF();
        this.getBarBounds(barEntry, rectF);
        return rectF;
    }
    
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
        rectF.set(x - n, n2, x + n, y);
        this.getTransformer(set.getAxisDependency()).rectValueToPixel(rectF);
    }
    
    @Override
    public BarData getBarData() {
        return (BarData)this.mData;
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
    
    public void groupBars(final float n, final float n2, final float n3) {
        if (this.getBarData() == null) {
            throw new RuntimeException("You need to set data for the chart before grouping bars.");
        }
        this.getBarData().groupBars(n, n2, n3);
        this.notifyDataSetChanged();
    }
    
    public void highlightValue(final float n, final int n2, final int n3) {
        this.highlightValue(new Highlight(n, n2, n3), false);
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new BarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.setHighlighter(new BarHighlighter(this));
        this.getXAxis().setSpaceMin(0.5f);
        this.getXAxis().setSpaceMax(0.5f);
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
    
    public void setDrawBarShadow(final boolean mDrawBarShadow) {
        this.mDrawBarShadow = mDrawBarShadow;
    }
    
    public void setDrawValueAboveBar(final boolean mDrawValueAboveBar) {
        this.mDrawValueAboveBar = mDrawValueAboveBar;
    }
    
    public void setFitBars(final boolean mFitBars) {
        this.mFitBars = mFitBars;
    }
    
    public void setHighlightFullBarEnabled(final boolean mHighlightFullBarEnabled) {
        this.mHighlightFullBarEnabled = mHighlightFullBarEnabled;
    }
}
