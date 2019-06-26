// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;

public class CandleData extends BarLineScatterCandleBubbleData<ICandleDataSet>
{
    public CandleData() {
    }
    
    public CandleData(final List<ICandleDataSet> list) {
        super(list);
    }
    
    public CandleData(final ICandleDataSet... array) {
        super(array);
    }
}
