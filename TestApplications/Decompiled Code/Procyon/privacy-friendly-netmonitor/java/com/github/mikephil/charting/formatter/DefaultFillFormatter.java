// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class DefaultFillFormatter implements IFillFormatter
{
    @Override
    public float getFillLinePosition(final ILineDataSet set, final LineDataProvider lineDataProvider) {
        final float yChartMax = lineDataProvider.getYChartMax();
        float yChartMin = lineDataProvider.getYChartMin();
        final LineData lineData = lineDataProvider.getLineData();
        final float yMax = set.getYMax();
        float n = 0.0f;
        if (yMax <= 0.0f || set.getYMin() >= 0.0f) {
            n = yChartMax;
            if (lineData.getYMax() > 0.0f) {
                n = 0.0f;
            }
            if (lineData.getYMin() < 0.0f) {
                yChartMin = 0.0f;
            }
            if (set.getYMin() >= 0.0f) {
                n = yChartMin;
            }
        }
        return n;
    }
}
