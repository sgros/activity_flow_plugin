package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

public interface IValueFormatter {
   String getFormattedValue(float var1, Entry var2, int var3, ViewPortHandler var4);
}
