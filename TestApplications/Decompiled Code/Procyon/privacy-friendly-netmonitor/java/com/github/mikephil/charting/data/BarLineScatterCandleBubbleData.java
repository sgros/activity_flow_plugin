// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

public abstract class BarLineScatterCandleBubbleData<T extends IBarLineScatterCandleBubbleDataSet<? extends Entry>> extends ChartData<T>
{
    public BarLineScatterCandleBubbleData() {
    }
    
    public BarLineScatterCandleBubbleData(final List<T> list) {
        super(list);
    }
    
    public BarLineScatterCandleBubbleData(final T... array) {
        super(array);
    }
}
