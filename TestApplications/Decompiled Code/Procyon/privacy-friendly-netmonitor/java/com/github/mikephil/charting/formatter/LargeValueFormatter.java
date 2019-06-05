// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.components.AxisBase;
import java.text.DecimalFormat;

public class LargeValueFormatter implements IValueFormatter, IAxisValueFormatter
{
    private static final int MAX_LENGTH = 5;
    private static String[] SUFFIX;
    private DecimalFormat mFormat;
    private String mText;
    
    static {
        LargeValueFormatter.SUFFIX = new String[] { "", "k", "m", "b", "t" };
    }
    
    public LargeValueFormatter() {
        this.mText = "";
        this.mFormat = new DecimalFormat("###E00");
    }
    
    public LargeValueFormatter(final String mText) {
        this();
        this.mText = mText;
    }
    
    private String makePretty(final double number) {
        final String format = this.mFormat.format(number);
        final int numericValue = Character.getNumericValue(format.charAt(format.length() - 1));
        final int numericValue2 = Character.getNumericValue(format.charAt(format.length() - 2));
        final StringBuilder sb = new StringBuilder();
        sb.append(numericValue2);
        sb.append("");
        sb.append(numericValue);
        String s;
        StringBuilder sb2;
        for (s = format.replaceAll("E[0-9][0-9]", LargeValueFormatter.SUFFIX[Integer.valueOf(sb.toString()) / 3]); s.length() > 5 || s.matches("[0-9]+\\.[a-z]"); s = sb2.toString()) {
            sb2 = new StringBuilder();
            sb2.append(s.substring(0, s.length() - 2));
            sb2.append(s.substring(s.length() - 1));
        }
        return s;
    }
    
    public int getDecimalDigits() {
        return 0;
    }
    
    @Override
    public String getFormattedValue(final float n, final AxisBase axisBase) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makePretty(n));
        sb.append(this.mText);
        return sb.toString();
    }
    
    @Override
    public String getFormattedValue(final float n, final Entry entry, final int n2, final ViewPortHandler viewPortHandler) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makePretty(n));
        sb.append(this.mText);
        return sb.toString();
    }
    
    public void setAppendix(final String mText) {
        this.mText = mText;
    }
    
    public void setSuffix(final String[] suffix) {
        LargeValueFormatter.SUFFIX = suffix;
    }
}
