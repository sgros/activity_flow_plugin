// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.components.AxisBase;
import java.text.DecimalFormat;

public class PercentFormatter implements IValueFormatter, IAxisValueFormatter
{
    protected DecimalFormat mFormat;
    
    public PercentFormatter() {
        this.mFormat = new DecimalFormat("###,###,##0.0");
    }
    
    public PercentFormatter(final DecimalFormat mFormat) {
        this.mFormat = mFormat;
    }
    
    public int getDecimalDigits() {
        return 1;
    }
    
    @Override
    public String getFormattedValue(final float n, final AxisBase axisBase) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mFormat.format(n));
        sb.append(" %");
        return sb.toString();
    }
    
    @Override
    public String getFormattedValue(final float n, final Entry entry, final int n2, final ViewPortHandler viewPortHandler) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mFormat.format(n));
        sb.append(" %");
        return sb.toString();
    }
}
