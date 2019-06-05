package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.text.DecimalFormat;

public class LargeValueFormatter implements IValueFormatter, IAxisValueFormatter {
    private static final int MAX_LENGTH = 5;
    private static String[] SUFFIX = new String[]{"", "k", "m", "b", "t"};
    private DecimalFormat mFormat;
    private String mText;

    public int getDecimalDigits() {
        return 0;
    }

    public LargeValueFormatter() {
        this.mText = "";
        this.mFormat = new DecimalFormat("###E00");
    }

    public LargeValueFormatter(String str) {
        this();
        this.mText = str;
    }

    public String getFormattedValue(float f, Entry entry, int i, ViewPortHandler viewPortHandler) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(makePretty((double) f));
        stringBuilder.append(this.mText);
        return stringBuilder.toString();
    }

    public String getFormattedValue(float f, AxisBase axisBase) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(makePretty((double) f));
        stringBuilder.append(this.mText);
        return stringBuilder.toString();
    }

    public void setAppendix(String str) {
        this.mText = str;
    }

    public void setSuffix(String[] strArr) {
        SUFFIX = strArr;
    }

    private String makePretty(double d) {
        String format = this.mFormat.format(d);
        int numericValue = Character.getNumericValue(format.charAt(format.length() - 1));
        int numericValue2 = Character.getNumericValue(format.charAt(format.length() - 2));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(numericValue2);
        stringBuilder.append("");
        stringBuilder.append(numericValue);
        format = format.replaceAll("E[0-9][0-9]", SUFFIX[Integer.valueOf(stringBuilder.toString()).intValue() / 3]);
        while (true) {
            if (format.length() <= 5 && !format.matches("[0-9]+\\.[a-z]")) {
                return format;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(format.substring(0, format.length() - 2));
            stringBuilder2.append(format.substring(format.length() - 1));
            format = stringBuilder2.toString();
        }
    }
}
