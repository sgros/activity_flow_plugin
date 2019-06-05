package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import java.text.DecimalFormat;

public class DefaultAxisValueFormatter implements IAxisValueFormatter {
    protected int digits = 0;
    protected DecimalFormat mFormat;

    public DefaultAxisValueFormatter(int i) {
        int i2 = 0;
        this.digits = i;
        StringBuffer stringBuffer = new StringBuffer();
        while (i2 < i) {
            if (i2 == 0) {
                stringBuffer.append(".");
            }
            stringBuffer.append("0");
            i2++;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("###,###,###,##0");
        stringBuilder.append(stringBuffer.toString());
        this.mFormat = new DecimalFormat(stringBuilder.toString());
    }

    public String getFormattedValue(float f, AxisBase axisBase) {
        return this.mFormat.format((double) f);
    }

    public int getDecimalDigits() {
        return this.digits;
    }
}
