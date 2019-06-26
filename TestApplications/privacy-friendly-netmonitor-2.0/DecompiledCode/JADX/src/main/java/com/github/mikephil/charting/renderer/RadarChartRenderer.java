package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class RadarChartRenderer extends LineRadarRenderer {
    protected RadarChart mChart;
    protected Path mDrawDataSetSurfacePathBuffer = new Path();
    protected Path mDrawHighlightCirclePathBuffer = new Path();
    protected Paint mHighlightCirclePaint;
    protected Paint mWebPaint;

    public void initBuffers() {
    }

    public RadarChartRenderer(RadarChart radarChart, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mChart = radarChart;
        this.mHighlightPaint = new Paint(1);
        this.mHighlightPaint.setStyle(Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(2.0f);
        this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
        this.mWebPaint = new Paint(1);
        this.mWebPaint.setStyle(Style.STROKE);
        this.mHighlightCirclePaint = new Paint(1);
    }

    public Paint getWebPaint() {
        return this.mWebPaint;
    }

    public void drawData(Canvas canvas) {
        RadarData radarData = (RadarData) this.mChart.getData();
        int entryCount = ((IRadarDataSet) radarData.getMaxEntryCountSet()).getEntryCount();
        for (IRadarDataSet iRadarDataSet : radarData.getDataSets()) {
            if (iRadarDataSet.isVisible()) {
                drawDataSet(canvas, iRadarDataSet, entryCount);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawDataSet(Canvas canvas, IRadarDataSet iRadarDataSet, int i) {
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float sliceAngle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        MPPointF centerOffsets = this.mChart.getCenterOffsets();
        MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        Path path = this.mDrawDataSetSurfacePathBuffer;
        path.reset();
        int i2 = 0;
        int i3 = 0;
        while (i2 < iRadarDataSet.getEntryCount()) {
            this.mRenderPaint.setColor(iRadarDataSet.getColor(i2));
            Utils.getPosition(centerOffsets, ((((RadarEntry) iRadarDataSet.getEntryForIndex(i2)).getY() - this.mChart.getYChartMin()) * factor) * phaseY, ((((float) i2) * sliceAngle) * phaseX) + this.mChart.getRotationAngle(), instance);
            if (!Float.isNaN(instance.f488x)) {
                if (i3 == 0) {
                    path.moveTo(instance.f488x, instance.f489y);
                    i3 = 1;
                } else {
                    path.lineTo(instance.f488x, instance.f489y);
                }
            }
            i2++;
        }
        if (iRadarDataSet.getEntryCount() > i) {
            path.lineTo(centerOffsets.f488x, centerOffsets.f489y);
        }
        path.close();
        if (iRadarDataSet.isDrawFilledEnabled()) {
            Drawable fillDrawable = iRadarDataSet.getFillDrawable();
            if (fillDrawable != null) {
                drawFilledPath(canvas, path, fillDrawable);
            } else {
                drawFilledPath(canvas, path, iRadarDataSet.getFillColor(), iRadarDataSet.getFillAlpha());
            }
        }
        this.mRenderPaint.setStrokeWidth(iRadarDataSet.getLineWidth());
        this.mRenderPaint.setStyle(Style.STROKE);
        if (!iRadarDataSet.isDrawFilledEnabled() || iRadarDataSet.getFillAlpha() < 255) {
            canvas.drawPath(path, this.mRenderPaint);
        }
        MPPointF.recycleInstance(centerOffsets);
        MPPointF.recycleInstance(instance);
    }

    public void drawValues(Canvas canvas) {
        MPPointF mPPointF;
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float sliceAngle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        MPPointF centerOffsets = this.mChart.getCenterOffsets();
        MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        MPPointF instance2 = MPPointF.getInstance(0.0f, 0.0f);
        float convertDpToPixel = Utils.convertDpToPixel(5.0f);
        int i = 0;
        while (i < ((RadarData) this.mChart.getData()).getDataSetCount()) {
            float f;
            float f2;
            int i2;
            IRadarDataSet iRadarDataSet = (IRadarDataSet) ((RadarData) this.mChart.getData()).getDataSetByIndex(i);
            if (shouldDrawValues(iRadarDataSet)) {
                applyValueTextStyle(iRadarDataSet);
                MPPointF instance3 = MPPointF.getInstance(iRadarDataSet.getIconsOffset());
                instance3.f488x = Utils.convertDpToPixel(instance3.f488x);
                instance3.f489y = Utils.convertDpToPixel(instance3.f489y);
                int i3 = 0;
                while (i3 < iRadarDataSet.getEntryCount()) {
                    int i4;
                    MPPointF mPPointF2;
                    IRadarDataSet iRadarDataSet2;
                    RadarEntry radarEntry;
                    Entry entry = (RadarEntry) iRadarDataSet.getEntryForIndex(i3);
                    float f3 = (((float) i3) * sliceAngle) * phaseX;
                    Utils.getPosition(centerOffsets, ((entry.getY() - this.mChart.getYChartMin()) * factor) * phaseY, f3 + this.mChart.getRotationAngle(), instance);
                    if (iRadarDataSet.isDrawValuesEnabled()) {
                        IValueFormatter valueFormatter = iRadarDataSet.getValueFormatter();
                        float y = entry.getY();
                        float f4 = instance.f488x;
                        float f5 = instance.f489y - convertDpToPixel;
                        int valueTextColor = iRadarDataSet.getValueTextColor(i3);
                        Entry entry2 = entry;
                        float f6 = y;
                        i4 = i3;
                        f = phaseX;
                        mPPointF2 = instance3;
                        f2 = sliceAngle;
                        iRadarDataSet2 = iRadarDataSet;
                        float f7 = f4;
                        i2 = i;
                        mPPointF = instance;
                        instance = instance2;
                        drawValue(canvas, valueFormatter, f6, entry2, i, f7, f5, valueTextColor);
                        radarEntry = entry2;
                    } else {
                        i4 = i3;
                        i2 = i;
                        f = phaseX;
                        f2 = sliceAngle;
                        mPPointF = instance;
                        mPPointF2 = instance3;
                        iRadarDataSet2 = iRadarDataSet;
                        instance = instance2;
                        radarEntry = entry;
                    }
                    if (radarEntry.getIcon() != null && iRadarDataSet2.isDrawIconsEnabled()) {
                        Drawable icon = radarEntry.getIcon();
                        Utils.getPosition(centerOffsets, ((radarEntry.getY() * factor) * phaseY) + mPPointF2.f489y, f3 + this.mChart.getRotationAngle(), instance);
                        instance.f489y += mPPointF2.f488x;
                        Utils.drawImage(canvas, icon, (int) instance.f488x, (int) instance.f489y, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                    }
                    i3 = i4 + 1;
                    instance3 = mPPointF2;
                    iRadarDataSet = iRadarDataSet2;
                    instance2 = instance;
                    i = i2;
                    phaseX = f;
                    sliceAngle = f2;
                    instance = mPPointF;
                }
                i2 = i;
                f = phaseX;
                f2 = sliceAngle;
                mPPointF = instance;
                instance = instance2;
                MPPointF.recycleInstance(instance3);
            } else {
                i2 = i;
                f = phaseX;
                f2 = sliceAngle;
                mPPointF = instance;
                instance = instance2;
            }
            i = i2 + 1;
            instance2 = instance;
            phaseX = f;
            sliceAngle = f2;
            instance = mPPointF;
        }
        mPPointF = instance;
        instance = instance2;
        MPPointF.recycleInstance(centerOffsets);
        MPPointF.recycleInstance(mPPointF);
        MPPointF.recycleInstance(instance);
    }

    public void drawExtras(Canvas canvas) {
        drawWeb(canvas);
    }

    /* Access modifiers changed, original: protected */
    public void drawWeb(Canvas canvas) {
        int i;
        float sliceAngle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        float rotationAngle = this.mChart.getRotationAngle();
        MPPointF centerOffsets = this.mChart.getCenterOffsets();
        this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidth());
        this.mWebPaint.setColor(this.mChart.getWebColor());
        this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
        int skipWebLineCount = 1 + this.mChart.getSkipWebLineCount();
        int entryCount = ((IRadarDataSet) ((RadarData) this.mChart.getData()).getMaxEntryCountSet()).getEntryCount();
        MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        for (i = 0; i < entryCount; i += skipWebLineCount) {
            Utils.getPosition(centerOffsets, this.mChart.getYRange() * factor, (((float) i) * sliceAngle) + rotationAngle, instance);
            canvas.drawLine(centerOffsets.f488x, centerOffsets.f489y, instance.f488x, instance.f489y, this.mWebPaint);
        }
        MPPointF.recycleInstance(instance);
        this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidthInner());
        this.mWebPaint.setColor(this.mChart.getWebColorInner());
        this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
        entryCount = this.mChart.getYAxis().mEntryCount;
        MPPointF instance2 = MPPointF.getInstance(0.0f, 0.0f);
        MPPointF instance3 = MPPointF.getInstance(0.0f, 0.0f);
        for (int i2 = 0; i2 < entryCount; i2++) {
            i = 0;
            while (i < ((RadarData) this.mChart.getData()).getEntryCount()) {
                float yChartMin = (this.mChart.getYAxis().mEntries[i2] - this.mChart.getYChartMin()) * factor;
                Utils.getPosition(centerOffsets, yChartMin, (((float) i) * sliceAngle) + rotationAngle, instance2);
                i++;
                Utils.getPosition(centerOffsets, yChartMin, (((float) i) * sliceAngle) + rotationAngle, instance3);
                canvas.drawLine(instance2.f488x, instance2.f489y, instance3.f488x, instance3.f489y, this.mWebPaint);
            }
        }
        MPPointF.recycleInstance(instance2);
        MPPointF.recycleInstance(instance3);
    }

    public void drawHighlighted(Canvas canvas, Highlight[] highlightArr) {
        Highlight[] highlightArr2 = highlightArr;
        float sliceAngle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        MPPointF centerOffsets = this.mChart.getCenterOffsets();
        MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        RadarData radarData = (RadarData) this.mChart.getData();
        int length = highlightArr2.length;
        int i = 0;
        while (i < length) {
            int i2;
            int highlightCircleFillColor;
            Highlight highlight = highlightArr2[i];
            IRadarDataSet iRadarDataSet = (IRadarDataSet) radarData.getDataSetByIndex(highlight.getDataSetIndex());
            if (iRadarDataSet != null && iRadarDataSet.isHighlightEnabled()) {
                RadarEntry radarEntry = (RadarEntry) iRadarDataSet.getEntryForIndex((int) highlight.getX());
                if (isInBoundsX(radarEntry, iRadarDataSet)) {
                    Utils.getPosition(centerOffsets, ((radarEntry.getY() - this.mChart.getYChartMin()) * factor) * this.mAnimator.getPhaseY(), ((highlight.getX() * sliceAngle) * this.mAnimator.getPhaseX()) + this.mChart.getRotationAngle(), instance);
                    highlight.setDraw(instance.f488x, instance.f489y);
                    Canvas canvas2 = canvas;
                    drawHighlightLines(canvas2, instance.f488x, instance.f489y, iRadarDataSet);
                    if (!(!iRadarDataSet.isDrawHighlightCircleEnabled() || Float.isNaN(instance.f488x) || Float.isNaN(instance.f489y))) {
                        int highlightCircleStrokeColor = iRadarDataSet.getHighlightCircleStrokeColor();
                        if (highlightCircleStrokeColor == ColorTemplate.COLOR_NONE) {
                            highlightCircleStrokeColor = iRadarDataSet.getColor(0);
                        }
                        if (iRadarDataSet.getHighlightCircleStrokeAlpha() < 255) {
                            highlightCircleStrokeColor = ColorTemplate.colorWithAlpha(highlightCircleStrokeColor, iRadarDataSet.getHighlightCircleStrokeAlpha());
                        }
                        i2 = highlightCircleStrokeColor;
                        float highlightCircleInnerRadius = iRadarDataSet.getHighlightCircleInnerRadius();
                        float highlightCircleOuterRadius = iRadarDataSet.getHighlightCircleOuterRadius();
                        highlightCircleFillColor = iRadarDataSet.getHighlightCircleFillColor();
                        int i3 = highlightCircleFillColor;
                        highlightCircleFillColor = i;
                        i = i2;
                        i2 = length;
                        drawHighlightCircle(canvas2, instance, highlightCircleInnerRadius, highlightCircleOuterRadius, i3, i, iRadarDataSet.getHighlightCircleStrokeWidth());
                        i = highlightCircleFillColor + 1;
                        length = i2;
                    }
                }
            }
            highlightCircleFillColor = i;
            i2 = length;
            i = highlightCircleFillColor + 1;
            length = i2;
        }
        MPPointF.recycleInstance(centerOffsets);
        MPPointF.recycleInstance(instance);
    }

    public void drawHighlightCircle(Canvas canvas, MPPointF mPPointF, float f, float f2, int i, int i2, float f3) {
        canvas.save();
        f2 = Utils.convertDpToPixel(f2);
        f = Utils.convertDpToPixel(f);
        if (i != ColorTemplate.COLOR_NONE) {
            Path path = this.mDrawHighlightCirclePathBuffer;
            path.reset();
            path.addCircle(mPPointF.f488x, mPPointF.f489y, f2, Direction.CW);
            if (f > 0.0f) {
                path.addCircle(mPPointF.f488x, mPPointF.f489y, f, Direction.CCW);
            }
            this.mHighlightCirclePaint.setColor(i);
            this.mHighlightCirclePaint.setStyle(Style.FILL);
            canvas.drawPath(path, this.mHighlightCirclePaint);
        }
        if (i2 != ColorTemplate.COLOR_NONE) {
            this.mHighlightCirclePaint.setColor(i2);
            this.mHighlightCirclePaint.setStyle(Style.STROKE);
            this.mHighlightCirclePaint.setStrokeWidth(Utils.convertDpToPixel(f3));
            canvas.drawCircle(mPPointF.f488x, mPPointF.f489y, f2, this.mHighlightCirclePaint);
        }
        canvas.restore();
    }
}
