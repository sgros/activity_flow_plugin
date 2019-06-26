// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.charts.PieChart;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.charts.PieRadarChartBase;

public abstract class PieRadarHighlighter<T extends PieRadarChartBase> implements IHighlighter
{
    protected T mChart;
    protected List<Highlight> mHighlightBuffer;
    
    public PieRadarHighlighter(final T mChart) {
        this.mHighlightBuffer = new ArrayList<Highlight>();
        this.mChart = mChart;
    }
    
    protected abstract Highlight getClosestHighlight(final int p0, final float p1, final float p2);
    
    @Override
    public Highlight getHighlight(final float n, final float n2) {
        if (this.mChart.distanceToCenter(n, n2) > this.mChart.getRadius()) {
            return null;
        }
        float angleForPoint = this.mChart.getAngleForPoint(n, n2);
        if (this.mChart instanceof PieChart) {
            angleForPoint /= this.mChart.getAnimator().getPhaseY();
        }
        final int indexForAngle = this.mChart.getIndexForAngle(angleForPoint);
        if (indexForAngle >= 0 && indexForAngle < this.mChart.getData().getMaxEntryCountSet().getEntryCount()) {
            return this.getClosestHighlight(indexForAngle, n, n2);
        }
        return null;
    }
}
