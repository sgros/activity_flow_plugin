package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class YAxisRendererRadarChart extends YAxisRenderer {
    private RadarChart mChart;
    private Path mRenderLimitLinesPathBuffer = new Path();

    public YAxisRendererRadarChart(ViewPortHandler viewPortHandler, YAxis yAxis, RadarChart radarChart) {
        super(viewPortHandler, yAxis, null);
        this.mChart = radarChart;
    }

    /* Access modifiers changed, original: protected */
    public void computeAxisValues(float f, float f2) {
        float f3 = f;
        float f4 = f2;
        int labelCount = this.mAxis.getLabelCount();
        double abs = (double) Math.abs(f4 - f3);
        if (labelCount == 0 || abs <= Utils.DOUBLE_EPSILON || Double.isInfinite(abs)) {
            this.mAxis.mEntries = new float[0];
            this.mAxis.mCenteredEntries = new float[0];
            this.mAxis.mEntryCount = 0;
            return;
        }
        int i;
        double roundToNextSignificant = (double) Utils.roundToNextSignificant(abs / ((double) labelCount));
        if (this.mAxis.isGranularityEnabled() && roundToNextSignificant < ((double) this.mAxis.getGranularity())) {
            roundToNextSignificant = (double) this.mAxis.getGranularity();
        }
        double roundToNextSignificant2 = (double) Utils.roundToNextSignificant(Math.pow(10.0d, (double) ((int) Math.log10(roundToNextSignificant))));
        if (((int) (roundToNextSignificant / roundToNextSignificant2)) > 5) {
            roundToNextSignificant = Math.floor(10.0d * roundToNextSignificant2);
        }
        boolean isCenterAxisLabelsEnabled = this.mAxis.isCenterAxisLabelsEnabled();
        if (this.mAxis.isForceLabelsEnabled()) {
            f4 = ((float) abs) / ((float) (labelCount - 1));
            this.mAxis.mEntryCount = labelCount;
            if (this.mAxis.mEntries.length < labelCount) {
                this.mAxis.mEntries = new float[labelCount];
            }
            float f5 = f3;
            for (i = 0; i < labelCount; i++) {
                this.mAxis.mEntries[i] = f5;
                f5 += f4;
            }
            i = labelCount;
        } else {
            double d;
            int i2;
            if (roundToNextSignificant == Utils.DOUBLE_EPSILON) {
                d = Utils.DOUBLE_EPSILON;
            } else {
                d = Math.ceil(((double) f3) / roundToNextSignificant) * roundToNextSignificant;
            }
            if (isCenterAxisLabelsEnabled) {
                d -= roundToNextSignificant;
            }
            double nextUp = roundToNextSignificant == Utils.DOUBLE_EPSILON ? Utils.DOUBLE_EPSILON : Utils.nextUp(Math.floor(((double) f4) / roundToNextSignificant) * roundToNextSignificant);
            if (roundToNextSignificant != Utils.DOUBLE_EPSILON) {
                i2 = isCenterAxisLabelsEnabled;
                for (roundToNextSignificant2 = d; roundToNextSignificant2 <= nextUp; roundToNextSignificant2 += roundToNextSignificant) {
                    i2++;
                }
            } else {
                i2 = isCenterAxisLabelsEnabled;
            }
            i = i2 + 1;
            this.mAxis.mEntryCount = i;
            if (this.mAxis.mEntries.length < i) {
                this.mAxis.mEntries = new float[i];
            }
            for (int i3 = 0; i3 < i; i3++) {
                if (d == Utils.DOUBLE_EPSILON) {
                    d = Utils.DOUBLE_EPSILON;
                }
                this.mAxis.mEntries[i3] = (float) d;
                d += roundToNextSignificant;
            }
        }
        if (roundToNextSignificant < 1.0d) {
            this.mAxis.mDecimals = (int) Math.ceil(-Math.log10(roundToNextSignificant));
        } else {
            this.mAxis.mDecimals = 0;
        }
        if (isCenterAxisLabelsEnabled) {
            if (this.mAxis.mCenteredEntries.length < i) {
                this.mAxis.mCenteredEntries = new float[i];
            }
            f4 = (this.mAxis.mEntries[1] - this.mAxis.mEntries[0]) / 2.0f;
            for (labelCount = 0; labelCount < i; labelCount++) {
                this.mAxis.mCenteredEntries[labelCount] = this.mAxis.mEntries[labelCount] + f4;
            }
        }
        this.mAxis.mAxisMinimum = this.mAxis.mEntries[0];
        this.mAxis.mAxisMaximum = this.mAxis.mEntries[i - 1];
        this.mAxis.mAxisRange = Math.abs(this.mAxis.mAxisMaximum - this.mAxis.mAxisMinimum);
    }

    public void renderAxisLabels(Canvas canvas) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            MPPointF centerOffsets = this.mChart.getCenterOffsets();
            MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
            float factor = this.mChart.getFactor();
            int isDrawBottomYLabelEntryEnabled = this.mYAxis.isDrawBottomYLabelEntryEnabled() ^ 1;
            int i = this.mYAxis.isDrawTopYLabelEntryEnabled() ? this.mYAxis.mEntryCount : this.mYAxis.mEntryCount - 1;
            while (isDrawBottomYLabelEntryEnabled < i) {
                Utils.getPosition(centerOffsets, (this.mYAxis.mEntries[isDrawBottomYLabelEntryEnabled] - this.mYAxis.mAxisMinimum) * factor, this.mChart.getRotationAngle(), instance);
                canvas.drawText(this.mYAxis.getFormattedLabel(isDrawBottomYLabelEntryEnabled), instance.f488x + 10.0f, instance.f489y, this.mAxisLabelPaint);
                isDrawBottomYLabelEntryEnabled++;
            }
            MPPointF.recycleInstance(centerOffsets);
            MPPointF.recycleInstance(instance);
        }
    }

    public void renderLimitLines(Canvas canvas) {
        List limitLines = this.mYAxis.getLimitLines();
        if (limitLines != null) {
            float sliceAngle = this.mChart.getSliceAngle();
            float factor = this.mChart.getFactor();
            MPPointF centerOffsets = this.mChart.getCenterOffsets();
            MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
            for (int i = 0; i < limitLines.size(); i++) {
                LimitLine limitLine = (LimitLine) limitLines.get(i);
                if (limitLine.isEnabled()) {
                    this.mLimitLinePaint.setColor(limitLine.getLineColor());
                    this.mLimitLinePaint.setPathEffect(limitLine.getDashPathEffect());
                    this.mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
                    float limit = (limitLine.getLimit() - this.mChart.getYChartMin()) * factor;
                    Path path = this.mRenderLimitLinesPathBuffer;
                    path.reset();
                    for (int i2 = 0; i2 < ((IRadarDataSet) ((RadarData) this.mChart.getData()).getMaxEntryCountSet()).getEntryCount(); i2++) {
                        Utils.getPosition(centerOffsets, limit, (((float) i2) * sliceAngle) + this.mChart.getRotationAngle(), instance);
                        if (i2 == 0) {
                            path.moveTo(instance.f488x, instance.f489y);
                        } else {
                            path.lineTo(instance.f488x, instance.f489y);
                        }
                    }
                    path.close();
                    canvas.drawPath(path, this.mLimitLinePaint);
                }
            }
            MPPointF.recycleInstance(centerOffsets);
            MPPointF.recycleInstance(instance);
        }
    }
}
