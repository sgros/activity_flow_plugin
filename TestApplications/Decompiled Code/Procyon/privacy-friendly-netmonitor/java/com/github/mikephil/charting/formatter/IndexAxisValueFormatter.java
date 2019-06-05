// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import java.util.Collection;

public class IndexAxisValueFormatter implements IAxisValueFormatter
{
    private int mValueCount;
    private String[] mValues;
    
    public IndexAxisValueFormatter() {
        this.mValues = new String[0];
        this.mValueCount = 0;
    }
    
    public IndexAxisValueFormatter(final Collection<String> collection) {
        this.mValues = new String[0];
        this.mValueCount = 0;
        if (collection != null) {
            this.setValues(collection.toArray(new String[collection.size()]));
        }
    }
    
    public IndexAxisValueFormatter(final String[] values) {
        this.mValues = new String[0];
        this.mValueCount = 0;
        if (values != null) {
            this.setValues(values);
        }
    }
    
    @Override
    public String getFormattedValue(final float a, final AxisBase axisBase) {
        final int round = Math.round(a);
        if (round >= 0 && round < this.mValueCount && round == (int)a) {
            return this.mValues[round];
        }
        return "";
    }
    
    public String[] getValues() {
        return this.mValues;
    }
    
    public void setValues(final String[] array) {
        String[] mValues = array;
        if (array == null) {
            mValues = new String[0];
        }
        this.mValues = mValues;
        this.mValueCount = mValues.length;
    }
}
