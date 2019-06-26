// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.charts.PieChart;

public class PieHighlighter extends PieRadarHighlighter<PieChart>
{
    public PieHighlighter(final PieChart pieChart) {
        super(pieChart);
    }
    
    @Override
    protected Highlight getClosestHighlight(final int n, final float n2, final float n3) {
        final IPieDataSet dataSet = ((PieChart)this.mChart).getData().getDataSet();
        return new Highlight((float)n, ((IDataSet<Entry>)dataSet).getEntryForIndex(n).getY(), n2, n3, 0, dataSet.getAxisDependency());
    }
}
