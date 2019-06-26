// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.data.Entry;
import java.text.DecimalFormat;

public class StackedValueFormatter implements IValueFormatter
{
    private String mAppendix;
    private boolean mDrawWholeStack;
    private DecimalFormat mFormat;
    
    public StackedValueFormatter(final boolean mDrawWholeStack, final String mAppendix, final int n) {
        this.mDrawWholeStack = mDrawWholeStack;
        this.mAppendix = mAppendix;
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; ++i) {
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
    
    @Override
    public String getFormattedValue(final float n, final Entry entry, final int n2, final ViewPortHandler viewPortHandler) {
        if (!this.mDrawWholeStack && entry instanceof BarEntry) {
            final BarEntry barEntry = (BarEntry)entry;
            final float[] yVals = barEntry.getYVals();
            if (yVals != null) {
                if (yVals[yVals.length - 1] == n) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.mFormat.format(barEntry.getY()));
                    sb.append(this.mAppendix);
                    return sb.toString();
                }
                return "";
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mFormat.format(n));
        sb2.append(this.mAppendix);
        return sb2.toString();
    }
}
