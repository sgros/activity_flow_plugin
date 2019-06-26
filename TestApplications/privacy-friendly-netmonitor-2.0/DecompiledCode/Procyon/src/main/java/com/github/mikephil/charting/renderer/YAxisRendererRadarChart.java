// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import java.util.List;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import android.graphics.PathEffect;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.utils.MPPointF;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Path;
import com.github.mikephil.charting.charts.RadarChart;

public class YAxisRendererRadarChart extends YAxisRenderer
{
    private RadarChart mChart;
    private Path mRenderLimitLinesPathBuffer;
    
    public YAxisRendererRadarChart(final ViewPortHandler viewPortHandler, final YAxis yAxis, final RadarChart mChart) {
        super(viewPortHandler, yAxis, null);
        this.mRenderLimitLinesPathBuffer = new Path();
        this.mChart = mChart;
    }
    
    @Override
    protected void computeAxisValues(float n, float n2) {
        final int labelCount = this.mAxis.getLabelCount();
        final double v = Math.abs(n2 - n);
        if (labelCount != 0 && v > 0.0 && !Double.isInfinite(v)) {
            double a;
            final double n3 = a = Utils.roundToNextSignificant(v / labelCount);
            if (this.mAxis.isGranularityEnabled()) {
                a = n3;
                if (n3 < this.mAxis.getGranularity()) {
                    a = this.mAxis.getGranularity();
                }
            }
            final double n4 = Utils.roundToNextSignificant(Math.pow(10.0, (int)Math.log10(a)));
            double floor = a;
            if ((int)(a / n4) > 5) {
                floor = Math.floor(10.0 * n4);
            }
            final int centerAxisLabelsEnabled = this.mAxis.isCenterAxisLabelsEnabled() ? 1 : 0;
            int n5;
            if (this.mAxis.isForceLabelsEnabled()) {
                n2 = (float)v / (labelCount - 1);
                if (this.mAxis.mEntries.length < (this.mAxis.mEntryCount = labelCount)) {
                    this.mAxis.mEntries = new float[labelCount];
                }
                for (int i = 0; i < labelCount; ++i) {
                    this.mAxis.mEntries[i] = n;
                    n += n2;
                }
                n5 = labelCount;
            }
            else {
                double n6;
                if (floor == 0.0) {
                    n6 = 0.0;
                }
                else {
                    n6 = Math.ceil(n / floor) * floor;
                }
                double n7 = n6;
                if (centerAxisLabelsEnabled != 0) {
                    n7 = n6 - floor;
                }
                double nextUp;
                if (floor == 0.0) {
                    nextUp = 0.0;
                }
                else {
                    nextUp = Utils.nextUp(Math.floor(n2 / floor) * floor);
                }
                int n10;
                if (floor != 0.0) {
                    double n8 = n7;
                    int n9 = centerAxisLabelsEnabled;
                    while (true) {
                        n10 = n9;
                        if (n8 > nextUp) {
                            break;
                        }
                        ++n9;
                        n8 += floor;
                    }
                }
                else {
                    n10 = centerAxisLabelsEnabled;
                }
                final int mEntryCount = n10 + 1;
                if (this.mAxis.mEntries.length < (this.mAxis.mEntryCount = mEntryCount)) {
                    this.mAxis.mEntries = new float[mEntryCount];
                }
                int n11 = 0;
                while (true) {
                    n5 = mEntryCount;
                    if (n11 >= mEntryCount) {
                        break;
                    }
                    double n12 = n7;
                    if (n7 == 0.0) {
                        n12 = 0.0;
                    }
                    this.mAxis.mEntries[n11] = (float)n12;
                    n7 = n12 + floor;
                    ++n11;
                }
            }
            if (floor < 1.0) {
                this.mAxis.mDecimals = (int)Math.ceil(-Math.log10(floor));
            }
            else {
                this.mAxis.mDecimals = 0;
            }
            if (centerAxisLabelsEnabled != 0) {
                if (this.mAxis.mCenteredEntries.length < n5) {
                    this.mAxis.mCenteredEntries = new float[n5];
                }
                n = (this.mAxis.mEntries[1] - this.mAxis.mEntries[0]) / 2.0f;
                for (int j = 0; j < n5; ++j) {
                    this.mAxis.mCenteredEntries[j] = this.mAxis.mEntries[j] + n;
                }
            }
            this.mAxis.mAxisMinimum = this.mAxis.mEntries[0];
            this.mAxis.mAxisMaximum = this.mAxis.mEntries[n5 - 1];
            this.mAxis.mAxisRange = Math.abs(this.mAxis.mAxisMaximum - this.mAxis.mAxisMinimum);
            return;
        }
        this.mAxis.mEntries = new float[0];
        this.mAxis.mCenteredEntries = new float[0];
        this.mAxis.mEntryCount = 0;
    }
    
    @Override
    public void renderAxisLabels(final Canvas canvas) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            final MPPointF centerOffsets = this.mChart.getCenterOffsets();
            final MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
            final float factor = this.mChart.getFactor();
            int i = (this.mYAxis.isDrawBottomYLabelEntryEnabled() ^ true) ? 1 : 0;
            int mEntryCount;
            if (this.mYAxis.isDrawTopYLabelEntryEnabled()) {
                mEntryCount = this.mYAxis.mEntryCount;
            }
            else {
                mEntryCount = this.mYAxis.mEntryCount - 1;
            }
            while (i < mEntryCount) {
                Utils.getPosition(centerOffsets, (this.mYAxis.mEntries[i] - this.mYAxis.mAxisMinimum) * factor, this.mChart.getRotationAngle(), instance);
                canvas.drawText(this.mYAxis.getFormattedLabel(i), instance.x + 10.0f, instance.y, this.mAxisLabelPaint);
                ++i;
            }
            MPPointF.recycleInstance(centerOffsets);
            MPPointF.recycleInstance(instance);
        }
    }
    
    @Override
    public void renderLimitLines(final Canvas canvas) {
        final List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines == null) {
            return;
        }
        final float sliceAngle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final MPPointF centerOffsets = this.mChart.getCenterOffsets();
        final MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        for (int i = 0; i < limitLines.size(); ++i) {
            final LimitLine limitLine = limitLines.get(i);
            if (limitLine.isEnabled()) {
                this.mLimitLinePaint.setColor(limitLine.getLineColor());
                this.mLimitLinePaint.setPathEffect((PathEffect)limitLine.getDashPathEffect());
                this.mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
                final float limit = limitLine.getLimit();
                final float yChartMin = this.mChart.getYChartMin();
                final Path mRenderLimitLinesPathBuffer = this.mRenderLimitLinesPathBuffer;
                mRenderLimitLinesPathBuffer.reset();
                for (int j = 0; j < this.mChart.getData().getMaxEntryCountSet().getEntryCount(); ++j) {
                    Utils.getPosition(centerOffsets, (limit - yChartMin) * factor, j * sliceAngle + this.mChart.getRotationAngle(), instance);
                    if (j == 0) {
                        mRenderLimitLinesPathBuffer.moveTo(instance.x, instance.y);
                    }
                    else {
                        mRenderLimitLinesPathBuffer.lineTo(instance.x, instance.y);
                    }
                }
                mRenderLimitLinesPathBuffer.close();
                canvas.drawPath(mRenderLimitLinesPathBuffer, this.mLimitLinePaint);
            }
        }
        MPPointF.recycleInstance(centerOffsets);
        MPPointF.recycleInstance(instance);
    }
}
