// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import java.util.Iterator;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;

public class BubbleData extends BarLineScatterCandleBubbleData<IBubbleDataSet>
{
    public BubbleData() {
    }
    
    public BubbleData(final List<IBubbleDataSet> list) {
        super(list);
    }
    
    public BubbleData(final IBubbleDataSet... array) {
        super(array);
    }
    
    public void setHighlightCircleWidth(final float highlightCircleWidth) {
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            ((IBubbleDataSet)iterator.next()).setHighlightCircleWidth(highlightCircleWidth);
        }
    }
}
