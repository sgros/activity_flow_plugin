// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import android.graphics.Path$Direction;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.utils.MPPointF;
import java.util.Iterator;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.data.RadarData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import android.graphics.Paint;
import android.graphics.Path;
import com.github.mikephil.charting.charts.RadarChart;

public class RadarChartRenderer extends LineRadarRenderer
{
    protected RadarChart mChart;
    protected Path mDrawDataSetSurfacePathBuffer;
    protected Path mDrawHighlightCirclePathBuffer;
    protected Paint mHighlightCirclePaint;
    protected Paint mWebPaint;
    
    public RadarChartRenderer(final RadarChart mChart, final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mDrawDataSetSurfacePathBuffer = new Path();
        this.mDrawHighlightCirclePathBuffer = new Path();
        this.mChart = mChart;
        (this.mHighlightPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(2.0f);
        this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
        (this.mWebPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.mHighlightCirclePaint = new Paint(1);
    }
    
    @Override
    public void drawData(final Canvas canvas) {
        final RadarData radarData = this.mChart.getData();
        final int entryCount = radarData.getMaxEntryCountSet().getEntryCount();
        for (final IRadarDataSet set : radarData.getDataSets()) {
            if (set.isVisible()) {
                this.drawDataSet(canvas, set, entryCount);
            }
        }
    }
    
    protected void drawDataSet(final Canvas canvas, final IRadarDataSet set, final int n) {
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float sliceAngle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final MPPointF centerOffsets = this.mChart.getCenterOffsets();
        final MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        final Path mDrawDataSetSurfacePathBuffer = this.mDrawDataSetSurfacePathBuffer;
        mDrawDataSetSurfacePathBuffer.reset();
        int i = 0;
        int n2 = 0;
        while (i < set.getEntryCount()) {
            this.mRenderPaint.setColor(set.getColor(i));
            Utils.getPosition(centerOffsets, (set.getEntryForIndex(i).getY() - this.mChart.getYChartMin()) * factor * phaseY, i * sliceAngle * phaseX + this.mChart.getRotationAngle(), instance);
            if (!Float.isNaN(instance.x)) {
                if (n2 == 0) {
                    mDrawDataSetSurfacePathBuffer.moveTo(instance.x, instance.y);
                    n2 = 1;
                }
                else {
                    mDrawDataSetSurfacePathBuffer.lineTo(instance.x, instance.y);
                }
            }
            ++i;
        }
        if (set.getEntryCount() > n) {
            mDrawDataSetSurfacePathBuffer.lineTo(centerOffsets.x, centerOffsets.y);
        }
        mDrawDataSetSurfacePathBuffer.close();
        if (set.isDrawFilledEnabled()) {
            final Drawable fillDrawable = set.getFillDrawable();
            if (fillDrawable != null) {
                this.drawFilledPath(canvas, mDrawDataSetSurfacePathBuffer, fillDrawable);
            }
            else {
                this.drawFilledPath(canvas, mDrawDataSetSurfacePathBuffer, set.getFillColor(), set.getFillAlpha());
            }
        }
        this.mRenderPaint.setStrokeWidth(set.getLineWidth());
        this.mRenderPaint.setStyle(Paint$Style.STROKE);
        if (!set.isDrawFilledEnabled() || set.getFillAlpha() < 255) {
            canvas.drawPath(mDrawDataSetSurfacePathBuffer, this.mRenderPaint);
        }
        MPPointF.recycleInstance(centerOffsets);
        MPPointF.recycleInstance(instance);
    }
    
    @Override
    public void drawExtras(final Canvas canvas) {
        this.drawWeb(canvas);
    }
    
    public void drawHighlightCircle(final Canvas canvas, final MPPointF mpPointF, float convertDpToPixel, float convertDpToPixel2, final int color, final int color2, final float n) {
        canvas.save();
        convertDpToPixel2 = Utils.convertDpToPixel(convertDpToPixel2);
        convertDpToPixel = Utils.convertDpToPixel(convertDpToPixel);
        if (color != 1122867) {
            final Path mDrawHighlightCirclePathBuffer = this.mDrawHighlightCirclePathBuffer;
            mDrawHighlightCirclePathBuffer.reset();
            mDrawHighlightCirclePathBuffer.addCircle(mpPointF.x, mpPointF.y, convertDpToPixel2, Path$Direction.CW);
            if (convertDpToPixel > 0.0f) {
                mDrawHighlightCirclePathBuffer.addCircle(mpPointF.x, mpPointF.y, convertDpToPixel, Path$Direction.CCW);
            }
            this.mHighlightCirclePaint.setColor(color);
            this.mHighlightCirclePaint.setStyle(Paint$Style.FILL);
            canvas.drawPath(mDrawHighlightCirclePathBuffer, this.mHighlightCirclePaint);
        }
        if (color2 != 1122867) {
            this.mHighlightCirclePaint.setColor(color2);
            this.mHighlightCirclePaint.setStyle(Paint$Style.STROKE);
            this.mHighlightCirclePaint.setStrokeWidth(Utils.convertDpToPixel(n));
            canvas.drawCircle(mpPointF.x, mpPointF.y, convertDpToPixel2, this.mHighlightCirclePaint);
        }
        canvas.restore();
    }
    
    @Override
    public void drawHighlighted(final Canvas canvas, final Highlight[] array) {
        final float sliceAngle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final MPPointF centerOffsets = this.mChart.getCenterOffsets();
        final MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        final RadarData radarData = this.mChart.getData();
        for (final Highlight highlight : array) {
            final IRadarDataSet set = radarData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set != null) {
                if (set.isHighlightEnabled()) {
                    final RadarEntry radarEntry = set.getEntryForIndex((int)highlight.getX());
                    if (this.isInBoundsX(radarEntry, set)) {
                        Utils.getPosition(centerOffsets, (radarEntry.getY() - this.mChart.getYChartMin()) * factor * this.mAnimator.getPhaseY(), highlight.getX() * sliceAngle * this.mAnimator.getPhaseX() + this.mChart.getRotationAngle(), instance);
                        highlight.setDraw(instance.x, instance.y);
                        this.drawHighlightLines(canvas, instance.x, instance.y, set);
                        if (set.isDrawHighlightCircleEnabled() && !Float.isNaN(instance.x) && !Float.isNaN(instance.y)) {
                            int n;
                            if ((n = set.getHighlightCircleStrokeColor()) == 1122867) {
                                n = set.getColor(0);
                            }
                            int colorWithAlpha = n;
                            if (set.getHighlightCircleStrokeAlpha() < 255) {
                                colorWithAlpha = ColorTemplate.colorWithAlpha(n, set.getHighlightCircleStrokeAlpha());
                            }
                            this.drawHighlightCircle(canvas, instance, set.getHighlightCircleInnerRadius(), set.getHighlightCircleOuterRadius(), set.getHighlightCircleFillColor(), colorWithAlpha, set.getHighlightCircleStrokeWidth());
                        }
                    }
                }
            }
        }
        MPPointF.recycleInstance(centerOffsets);
        MPPointF.recycleInstance(instance);
    }
    
    @Override
    public void drawValues(final Canvas canvas) {
        float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        float sliceAngle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final MPPointF centerOffsets = this.mChart.getCenterOffsets();
        MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        MPPointF instance2 = MPPointF.getInstance(0.0f, 0.0f);
        final float convertDpToPixel = Utils.convertDpToPixel(5.0f);
        float n2;
        MPPointF mpPointF2;
        float n3;
        MPPointF mpPointF3;
        float n6;
        MPPointF mpPointF5;
        for (int i = 0; i < this.mChart.getData().getDataSetCount(); ++i, n6 = n3, sliceAngle = n2, mpPointF5 = mpPointF3, instance2 = mpPointF2, phaseX = n6, instance = mpPointF5) {
            final IRadarDataSet set = this.mChart.getData().getDataSetByIndex(i);
            if (!this.shouldDrawValues(set)) {
                final float n = phaseX;
                n2 = sliceAngle;
                final MPPointF mpPointF = instance;
                mpPointF2 = instance2;
                n3 = n;
                mpPointF3 = mpPointF;
            }
            else {
                this.applyValueTextStyle(set);
                final MPPointF instance3 = MPPointF.getInstance(set.getIconsOffset());
                instance3.x = Utils.convertDpToPixel(instance3.x);
                instance3.y = Utils.convertDpToPixel(instance3.y);
                for (int j = 0; j < set.getEntryCount(); ++j) {
                    final RadarEntry radarEntry = set.getEntryForIndex(j);
                    final float y = radarEntry.getY();
                    final float yChartMin = this.mChart.getYChartMin();
                    final float n4 = j * sliceAngle * phaseX;
                    Utils.getPosition(centerOffsets, (y - yChartMin) * factor * phaseY, n4 + this.mChart.getRotationAngle(), instance);
                    if (set.isDrawValuesEnabled()) {
                        this.drawValue(canvas, set.getValueFormatter(), radarEntry.getY(), radarEntry, i, instance.x, instance.y - convertDpToPixel, set.getValueTextColor(j));
                    }
                    if (radarEntry.getIcon() != null && set.isDrawIconsEnabled()) {
                        final Drawable icon = radarEntry.getIcon();
                        Utils.getPosition(centerOffsets, radarEntry.getY() * factor * phaseY + instance3.y, n4 + this.mChart.getRotationAngle(), instance2);
                        instance2.y += instance3.x;
                        Utils.drawImage(canvas, icon, (int)instance2.x, (int)instance2.y, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                    }
                }
                final float n5 = phaseX;
                n2 = sliceAngle;
                final MPPointF mpPointF4 = instance2;
                MPPointF.recycleInstance(instance3);
                mpPointF3 = instance;
                n3 = n5;
                mpPointF2 = mpPointF4;
            }
        }
        MPPointF.recycleInstance(centerOffsets);
        MPPointF.recycleInstance(instance);
        MPPointF.recycleInstance(instance2);
    }
    
    protected void drawWeb(final Canvas canvas) {
        final float sliceAngle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final float rotationAngle = this.mChart.getRotationAngle();
        final MPPointF centerOffsets = this.mChart.getCenterOffsets();
        this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidth());
        this.mWebPaint.setColor(this.mChart.getWebColor());
        this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
        final int skipWebLineCount = this.mChart.getSkipWebLineCount();
        final int entryCount = this.mChart.getData().getMaxEntryCountSet().getEntryCount();
        final MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        for (int i = 0; i < entryCount; i += 1 + skipWebLineCount) {
            Utils.getPosition(centerOffsets, this.mChart.getYRange() * factor, i * sliceAngle + rotationAngle, instance);
            canvas.drawLine(centerOffsets.x, centerOffsets.y, instance.x, instance.y, this.mWebPaint);
        }
        MPPointF.recycleInstance(instance);
        this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidthInner());
        this.mWebPaint.setColor(this.mChart.getWebColorInner());
        this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
        final int mEntryCount = this.mChart.getYAxis().mEntryCount;
        final MPPointF instance2 = MPPointF.getInstance(0.0f, 0.0f);
        final MPPointF instance3 = MPPointF.getInstance(0.0f, 0.0f);
        for (int j = 0; j < mEntryCount; ++j) {
            int k = 0;
            while (k < this.mChart.getData().getEntryCount()) {
                final float n = (this.mChart.getYAxis().mEntries[j] - this.mChart.getYChartMin()) * factor;
                Utils.getPosition(centerOffsets, n, k * sliceAngle + rotationAngle, instance2);
                ++k;
                Utils.getPosition(centerOffsets, n, k * sliceAngle + rotationAngle, instance3);
                canvas.drawLine(instance2.x, instance2.y, instance3.x, instance3.y, this.mWebPaint);
            }
        }
        MPPointF.recycleInstance(instance2);
        MPPointF.recycleInstance(instance3);
    }
    
    public Paint getWebPaint() {
        return this.mWebPaint;
    }
    
    @Override
    public void initBuffers() {
    }
}
