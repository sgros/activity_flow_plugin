// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import java.util.Arrays;
import com.github.mikephil.charting.highlight.Highlight;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

public class RadarData extends ChartData<IRadarDataSet>
{
    private List<String> mLabels;
    
    public RadarData() {
    }
    
    public RadarData(final List<IRadarDataSet> list) {
        super(list);
    }
    
    public RadarData(final IRadarDataSet... array) {
        super(array);
    }
    
    @Override
    public Entry getEntryForHighlight(final Highlight highlight) {
        return ((IDataSet<Entry>)this.getDataSetByIndex(highlight.getDataSetIndex())).getEntryForIndex((int)highlight.getX());
    }
    
    public List<String> getLabels() {
        return this.mLabels;
    }
    
    public void setLabels(final List<String> mLabels) {
        this.mLabels = mLabels;
    }
    
    public void setLabels(final String... a) {
        this.mLabels = Arrays.asList(a);
    }
}
