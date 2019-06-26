// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.data.Entry;
import java.text.DecimalFormat;

public class DefaultValueFormatter implements IValueFormatter
{
    protected int mDecimalDigits;
    protected DecimalFormat mFormat;
    
    public DefaultValueFormatter(final int n) {
        this.setup(n);
    }
    
    public int getDecimalDigits() {
        return this.mDecimalDigits;
    }
    
    @Override
    public String getFormattedValue(final float n, final Entry entry, final int n2, final ViewPortHandler viewPortHandler) {
        return this.mFormat.format(n);
    }
    
    public void setup(final int mDecimalDigits) {
        this.mDecimalDigits = mDecimalDigits;
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mDecimalDigits; ++i) {
            if (i == 0) {
                sb.append(".");
            }
            sb.append("0");
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("###,###,###,##0");
        sb2.append(sb.toString());
        this.mFormat = new DecimalFormat(sb2.toString());
    }
}
