package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.text.DecimalFormat;

public class PercentFormatter implements IValueFormatter, IAxisValueFormatter {
    protected DecimalFormat mFormat;

    public int getDecimalDigits() {
        return 1;
    }

    public PercentFormatter() {
        this.mFormat = new DecimalFormat("###,###,##0.0");
    }

    public PercentFormatter(DecimalFormat decimalFormat) {
        this.mFormat = decimalFormat;
    }

    public String getFormattedValue(float f, Entry entry, int i, ViewPortHandler viewPortHandler) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mFormat.format((double) f));
        stringBuilder.append(" %");
        return stringBuilder.toString();
    }

    public String getFormattedValue(float f, AxisBase axisBase) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mFormat.format((double) f));
        stringBuilder.append(" %");
        return stringBuilder.toString();
    }
}
