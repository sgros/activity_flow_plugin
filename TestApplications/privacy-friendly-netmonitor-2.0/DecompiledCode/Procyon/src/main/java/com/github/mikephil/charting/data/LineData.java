// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class LineData extends BarLineScatterCandleBubbleData<ILineDataSet>
{
    public LineData() {
    }
    
    public LineData(final List<ILineDataSet> list) {
        super(list);
    }
    
    public LineData(final ILineDataSet... array) {
        super(array);
    }
}
