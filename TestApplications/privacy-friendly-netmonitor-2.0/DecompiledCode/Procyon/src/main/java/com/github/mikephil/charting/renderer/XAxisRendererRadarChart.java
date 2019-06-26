// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.charts.RadarChart;

public class XAxisRendererRadarChart extends XAxisRenderer
{
    private RadarChart mChart;
    
    public XAxisRendererRadarChart(final ViewPortHandler viewPortHandler, final XAxis xAxis, final RadarChart mChart) {
        super(viewPortHandler, xAxis, null);
        this.mChart = mChart;
    }
    
    @Override
    public void renderAxisLabels(final Canvas canvas) {
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
            final float labelRotationAngle = this.mXAxis.getLabelRotationAngle();
            final MPPointF instance = MPPointF.getInstance(0.5f, 0.25f);
            this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
            final float sliceAngle = this.mChart.getSliceAngle();
            final float factor = this.mChart.getFactor();
            final MPPointF centerOffsets = this.mChart.getCenterOffsets();
            final MPPointF instance2 = MPPointF.getInstance(0.0f, 0.0f);
            for (int i = 0; i < this.mChart.getData().getMaxEntryCountSet().getEntryCount(); ++i) {
                final IAxisValueFormatter valueFormatter = this.mXAxis.getValueFormatter();
                final float n = (float)i;
                final String formattedValue = valueFormatter.getFormattedValue(n, this.mXAxis);
                Utils.getPosition(centerOffsets, this.mChart.getYRange() * factor + this.mXAxis.mLabelRotatedWidth / 2.0f, (n * sliceAngle + this.mChart.getRotationAngle()) % 360.0f, instance2);
                this.drawLabel(canvas, formattedValue, instance2.x, instance2.y - this.mXAxis.mLabelRotatedHeight / 2.0f, instance, labelRotationAngle);
            }
            MPPointF.recycleInstance(centerOffsets);
            MPPointF.recycleInstance(instance2);
            MPPointF.recycleInstance(instance);
        }
    }
    
    @Override
    public void renderLimitLines(final Canvas canvas) {
    }
}
