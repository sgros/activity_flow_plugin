// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import java.util.Iterator;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

public class ScatterData extends BarLineScatterCandleBubbleData<IScatterDataSet>
{
    public ScatterData() {
    }
    
    public ScatterData(final List<IScatterDataSet> list) {
        super(list);
    }
    
    public ScatterData(final IScatterDataSet... array) {
        super(array);
    }
    
    public float getGreatestShapeSize() {
        final Iterator<T> iterator = this.mDataSets.iterator();
        float n = 0.0f;
        while (iterator.hasNext()) {
            final float scatterShapeSize = ((IScatterDataSet)iterator.next()).getScatterShapeSize();
            if (scatterShapeSize > n) {
                n = scatterShapeSize;
            }
        }
        return n;
    }
}
