// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

public class PieData extends ChartData<IPieDataSet>
{
    public PieData() {
    }
    
    public PieData(final IPieDataSet set) {
        super(new IPieDataSet[] { set });
    }
    
    public IPieDataSet getDataSet() {
        return (IPieDataSet)this.mDataSets.get(0);
    }
    
    @Override
    public IPieDataSet getDataSetByIndex(final int n) {
        IPieDataSet dataSet;
        if (n == 0) {
            dataSet = this.getDataSet();
        }
        else {
            dataSet = null;
        }
        return dataSet;
    }
    
    @Override
    public IPieDataSet getDataSetByLabel(final String s, final boolean b) {
        IPieDataSet set = null;
        if (b) {
            if (s.equalsIgnoreCase(((IPieDataSet)this.mDataSets.get(0)).getLabel())) {
                set = (IPieDataSet)this.mDataSets.get(0);
            }
        }
        else if (s.equals(((IPieDataSet)this.mDataSets.get(0)).getLabel())) {
            set = (IPieDataSet)this.mDataSets.get(0);
        }
        return set;
    }
    
    @Override
    public Entry getEntryForHighlight(final Highlight highlight) {
        return ((IDataSet<Entry>)this.getDataSet()).getEntryForIndex((int)highlight.getX());
    }
    
    public float getYValueSum() {
        float n = 0.0f;
        for (int i = 0; i < this.getDataSet().getEntryCount(); ++i) {
            n += this.getDataSet().getEntryForIndex(i).getY();
        }
        return n;
    }
    
    public void setDataSet(final IPieDataSet set) {
        this.mDataSets.clear();
        this.mDataSets.add((T)set);
        this.notifyDataChanged();
    }
}
