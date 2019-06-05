// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;

public class BarHighlighter extends ChartHighlighter<BarDataProvider>
{
    public BarHighlighter(final BarDataProvider barDataProvider) {
        super(barDataProvider);
    }
    
    protected int getClosestStackIndex(final Range[] array, final float n) {
        final int n2 = 0;
        if (array != null && array.length != 0) {
            int n3;
            for (int length = array.length, i = n3 = 0; i < length; ++i) {
                if (array[i].contains(n)) {
                    return n3;
                }
                ++n3;
            }
            final int max = Math.max(array.length - 1, 0);
            int n4 = n2;
            if (n > array[max].to) {
                n4 = max;
            }
            return n4;
        }
        return 0;
    }
    
    @Override
    protected BarLineScatterCandleBubbleData getData() {
        return ((BarDataProvider)this.mChart).getBarData();
    }
    
    @Override
    protected float getDistance(final float n, final float n2, final float n3, final float n4) {
        return Math.abs(n - n3);
    }
    
    @Override
    public Highlight getHighlight(final float n, final float n2) {
        final Highlight highlight = super.getHighlight(n, n2);
        if (highlight == null) {
            return null;
        }
        final MPPointD valsForTouch = this.getValsForTouch(n, n2);
        final IBarDataSet set = ((BarDataProvider)this.mChart).getBarData().getDataSetByIndex(highlight.getDataSetIndex());
        if (set.isStacked()) {
            return this.getStackedHighlight(highlight, set, (float)valsForTouch.x, (float)valsForTouch.y);
        }
        MPPointD.recycleInstance(valsForTouch);
        return highlight;
    }
    
    public Highlight getStackedHighlight(Highlight highlight, final IBarDataSet set, final float n, final float n2) {
        final BarEntry barEntry = set.getEntryForXValue(n, n2);
        if (barEntry == null) {
            return null;
        }
        if (barEntry.getYVals() == null) {
            return highlight;
        }
        final Range[] ranges = barEntry.getRanges();
        if (ranges.length > 0) {
            final int closestStackIndex = this.getClosestStackIndex(ranges, n2);
            final MPPointD pixelForValues = ((BarDataProvider)this.mChart).getTransformer(set.getAxisDependency()).getPixelForValues(highlight.getX(), ranges[closestStackIndex].to);
            highlight = new Highlight(barEntry.getX(), barEntry.getY(), (float)pixelForValues.x, (float)pixelForValues.y, highlight.getDataSetIndex(), closestStackIndex, highlight.getAxis());
            MPPointD.recycleInstance(pixelForValues);
            return highlight;
        }
        return null;
    }
}
