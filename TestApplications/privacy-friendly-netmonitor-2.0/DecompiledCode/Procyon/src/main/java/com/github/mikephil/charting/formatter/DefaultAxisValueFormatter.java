// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import java.text.DecimalFormat;

public class DefaultAxisValueFormatter implements IAxisValueFormatter
{
    protected int digits;
    protected DecimalFormat mFormat;
    
    public DefaultAxisValueFormatter(final int digits) {
        int i = 0;
        this.digits = 0;
        this.digits = digits;
        final StringBuffer sb = new StringBuffer();
        while (i < digits) {
            if (i == 0) {
                sb.append(".");
            }
            sb.append("0");
            ++i;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("###,###,###,##0");
        sb2.append(sb.toString());
        this.mFormat = new DecimalFormat(sb2.toString());
    }
    
    public int getDecimalDigits() {
        return this.digits;
    }
    
    @Override
    public String getFormattedValue(final float n, final AxisBase axisBase) {
        return this.mFormat.format(n);
    }
}
