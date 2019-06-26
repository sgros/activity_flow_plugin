// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;

public abstract class BarLineScatterCandleBubbleRenderer extends DataRenderer
{
    protected XBounds mXBounds;
    
    public BarLineScatterCandleBubbleRenderer(final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mXBounds = new XBounds();
    }
    
    protected boolean isInBoundsX(final Entry entry, final IBarLineScatterCandleBubbleDataSet set) {
        if (entry == null) {
            return false;
        }
        final float n = (float)set.getEntryIndex(entry);
        return entry != null && n < set.getEntryCount() * this.mAnimator.getPhaseX();
    }
    
    protected boolean shouldDrawValues(final IDataSet set) {
        return set.isVisible() && (set.isDrawValuesEnabled() || set.isDrawIconsEnabled());
    }
    
    protected class XBounds
    {
        public int max;
        public int min;
        public int range;
        
        public void set(final BarLineScatterCandleBubbleDataProvider barLineScatterCandleBubbleDataProvider, final IBarLineScatterCandleBubbleDataSet set) {
            final float max = Math.max(0.0f, Math.min(1.0f, BarLineScatterCandleBubbleRenderer.this.mAnimator.getPhaseX()));
            final float lowestVisibleX = barLineScatterCandleBubbleDataProvider.getLowestVisibleX();
            final float highestVisibleX = barLineScatterCandleBubbleDataProvider.getHighestVisibleX();
            final Entry entryForXValue = set.getEntryForXValue(lowestVisibleX, Float.NaN, DataSet.Rounding.DOWN);
            final Entry entryForXValue2 = set.getEntryForXValue(highestVisibleX, Float.NaN, DataSet.Rounding.UP);
            final int n = 0;
            int entryIndex;
            if (entryForXValue == null) {
                entryIndex = 0;
            }
            else {
                entryIndex = set.getEntryIndex(entryForXValue);
            }
            this.min = entryIndex;
            int entryIndex2;
            if (entryForXValue2 == null) {
                entryIndex2 = n;
            }
            else {
                entryIndex2 = set.getEntryIndex(entryForXValue2);
            }
            this.max = entryIndex2;
            this.range = (int)((this.max - this.min) * max);
        }
    }
}
