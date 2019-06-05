// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import java.util.List;
import android.graphics.DashPathEffect;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;

public abstract class LineScatterCandleRadarDataSet<T extends Entry> extends BarLineScatterCandleBubbleDataSet<T> implements ILineScatterCandleRadarDataSet<T>
{
    protected boolean mDrawHorizontalHighlightIndicator;
    protected boolean mDrawVerticalHighlightIndicator;
    protected DashPathEffect mHighlightDashPathEffect;
    protected float mHighlightLineWidth;
    
    public LineScatterCandleRadarDataSet(final List<T> list, final String s) {
        super(list, s);
        this.mDrawVerticalHighlightIndicator = true;
        this.mDrawHorizontalHighlightIndicator = true;
        this.mHighlightLineWidth = 0.5f;
        this.mHighlightDashPathEffect = null;
        this.mHighlightLineWidth = Utils.convertDpToPixel(0.5f);
    }
    
    public void disableDashedHighlightLine() {
        this.mHighlightDashPathEffect = null;
    }
    
    public void enableDashedHighlightLine(final float n, final float n2, final float n3) {
        this.mHighlightDashPathEffect = new DashPathEffect(new float[] { n, n2 }, n3);
    }
    
    @Override
    public DashPathEffect getDashPathEffectHighlight() {
        return this.mHighlightDashPathEffect;
    }
    
    @Override
    public float getHighlightLineWidth() {
        return this.mHighlightLineWidth;
    }
    
    public boolean isDashedHighlightLineEnabled() {
        return this.mHighlightDashPathEffect != null;
    }
    
    @Override
    public boolean isHorizontalHighlightIndicatorEnabled() {
        return this.mDrawHorizontalHighlightIndicator;
    }
    
    @Override
    public boolean isVerticalHighlightIndicatorEnabled() {
        return this.mDrawVerticalHighlightIndicator;
    }
    
    public void setDrawHighlightIndicators(final boolean b) {
        this.setDrawVerticalHighlightIndicator(b);
        this.setDrawHorizontalHighlightIndicator(b);
    }
    
    public void setDrawHorizontalHighlightIndicator(final boolean mDrawHorizontalHighlightIndicator) {
        this.mDrawHorizontalHighlightIndicator = mDrawHorizontalHighlightIndicator;
    }
    
    public void setDrawVerticalHighlightIndicator(final boolean mDrawVerticalHighlightIndicator) {
        this.mDrawVerticalHighlightIndicator = mDrawVerticalHighlightIndicator;
    }
    
    public void setHighlightLineWidth(final float n) {
        this.mHighlightLineWidth = Utils.convertDpToPixel(n);
    }
}
