// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import android.graphics.drawable.Drawable;
import java.util.List;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.Transformer;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import java.util.Iterator;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;

public class CandleStickChartRenderer extends LineScatterCandleRadarRenderer
{
    private float[] mBodyBuffers;
    protected CandleDataProvider mChart;
    private float[] mCloseBuffers;
    private float[] mOpenBuffers;
    private float[] mRangeBuffers;
    private float[] mShadowBuffers;
    
    public CandleStickChartRenderer(final CandleDataProvider mChart, final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mShadowBuffers = new float[8];
        this.mBodyBuffers = new float[4];
        this.mRangeBuffers = new float[4];
        this.mOpenBuffers = new float[4];
        this.mCloseBuffers = new float[4];
        this.mChart = mChart;
    }
    
    @Override
    public void drawData(final Canvas canvas) {
        for (final ICandleDataSet set : this.mChart.getCandleData().getDataSets()) {
            if (set.isVisible()) {
                this.drawDataSet(canvas, set);
            }
        }
    }
    
    protected void drawDataSet(final Canvas canvas, final ICandleDataSet set) {
        final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
        final float phaseY = this.mAnimator.getPhaseY();
        final float barSpace = set.getBarSpace();
        final boolean showCandleBar = set.getShowCandleBar();
        this.mXBounds.set(this.mChart, set);
        this.mRenderPaint.setStrokeWidth(set.getShadowWidth());
        for (int i = this.mXBounds.min; i <= this.mXBounds.range + this.mXBounds.min; ++i) {
            final CandleEntry candleEntry = set.getEntryForIndex(i);
            if (candleEntry != null) {
                final float x = candleEntry.getX();
                final float open = candleEntry.getOpen();
                final float close = candleEntry.getClose();
                final float high = candleEntry.getHigh();
                final float low = candleEntry.getLow();
                if (showCandleBar) {
                    this.mShadowBuffers[0] = x;
                    this.mShadowBuffers[2] = x;
                    this.mShadowBuffers[4] = x;
                    this.mShadowBuffers[6] = x;
                    if (open > close) {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = open * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = close * phaseY;
                    }
                    else if (open < close) {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = close * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = open * phaseY;
                    }
                    else {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = open * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = this.mShadowBuffers[3];
                    }
                    transformer.pointValuesToPixel(this.mShadowBuffers);
                    if (set.getShadowColorSameAsCandle()) {
                        if (open > close) {
                            final Paint mRenderPaint = this.mRenderPaint;
                            int color;
                            if (set.getDecreasingColor() == 1122867) {
                                color = set.getColor(i);
                            }
                            else {
                                color = set.getDecreasingColor();
                            }
                            mRenderPaint.setColor(color);
                        }
                        else if (open < close) {
                            final Paint mRenderPaint2 = this.mRenderPaint;
                            int color2;
                            if (set.getIncreasingColor() == 1122867) {
                                color2 = set.getColor(i);
                            }
                            else {
                                color2 = set.getIncreasingColor();
                            }
                            mRenderPaint2.setColor(color2);
                        }
                        else {
                            final Paint mRenderPaint3 = this.mRenderPaint;
                            int color3;
                            if (set.getNeutralColor() == 1122867) {
                                color3 = set.getColor(i);
                            }
                            else {
                                color3 = set.getNeutralColor();
                            }
                            mRenderPaint3.setColor(color3);
                        }
                    }
                    else {
                        final Paint mRenderPaint4 = this.mRenderPaint;
                        int color4;
                        if (set.getShadowColor() == 1122867) {
                            color4 = set.getColor(i);
                        }
                        else {
                            color4 = set.getShadowColor();
                        }
                        mRenderPaint4.setColor(color4);
                    }
                    this.mRenderPaint.setStyle(Paint$Style.STROKE);
                    canvas.drawLines(this.mShadowBuffers, this.mRenderPaint);
                    this.mBodyBuffers[0] = x - 0.5f + barSpace;
                    this.mBodyBuffers[1] = close * phaseY;
                    this.mBodyBuffers[2] = x + 0.5f - barSpace;
                    this.mBodyBuffers[3] = open * phaseY;
                    transformer.pointValuesToPixel(this.mBodyBuffers);
                    if (open > close) {
                        if (set.getDecreasingColor() == 1122867) {
                            this.mRenderPaint.setColor(set.getColor(i));
                        }
                        else {
                            this.mRenderPaint.setColor(set.getDecreasingColor());
                        }
                        this.mRenderPaint.setStyle(set.getDecreasingPaintStyle());
                        canvas.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[3], this.mBodyBuffers[2], this.mBodyBuffers[1], this.mRenderPaint);
                    }
                    else if (open < close) {
                        if (set.getIncreasingColor() == 1122867) {
                            this.mRenderPaint.setColor(set.getColor(i));
                        }
                        else {
                            this.mRenderPaint.setColor(set.getIncreasingColor());
                        }
                        this.mRenderPaint.setStyle(set.getIncreasingPaintStyle());
                        canvas.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
                    }
                    else {
                        if (set.getNeutralColor() == 1122867) {
                            this.mRenderPaint.setColor(set.getColor(i));
                        }
                        else {
                            this.mRenderPaint.setColor(set.getNeutralColor());
                        }
                        canvas.drawLine(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
                    }
                }
                else {
                    this.mRangeBuffers[0] = x;
                    this.mRangeBuffers[1] = high * phaseY;
                    this.mRangeBuffers[2] = x;
                    this.mRangeBuffers[3] = low * phaseY;
                    this.mOpenBuffers[0] = x - 0.5f + barSpace;
                    final float[] mOpenBuffers = this.mOpenBuffers;
                    final float n = open * phaseY;
                    mOpenBuffers[1] = n;
                    this.mOpenBuffers[2] = x;
                    this.mOpenBuffers[3] = n;
                    this.mCloseBuffers[0] = 0.5f + x - barSpace;
                    final float[] mCloseBuffers = this.mCloseBuffers;
                    final float n2 = close * phaseY;
                    mCloseBuffers[1] = n2;
                    this.mCloseBuffers[2] = x;
                    this.mCloseBuffers[3] = n2;
                    transformer.pointValuesToPixel(this.mRangeBuffers);
                    transformer.pointValuesToPixel(this.mOpenBuffers);
                    transformer.pointValuesToPixel(this.mCloseBuffers);
                    int color5;
                    if (open > close) {
                        if (set.getDecreasingColor() == 1122867) {
                            color5 = set.getColor(i);
                        }
                        else {
                            color5 = set.getDecreasingColor();
                        }
                    }
                    else if (open < close) {
                        if (set.getIncreasingColor() == 1122867) {
                            color5 = set.getColor(i);
                        }
                        else {
                            color5 = set.getIncreasingColor();
                        }
                    }
                    else if (set.getNeutralColor() == 1122867) {
                        color5 = set.getColor(i);
                    }
                    else {
                        color5 = set.getNeutralColor();
                    }
                    this.mRenderPaint.setColor(color5);
                    canvas.drawLine(this.mRangeBuffers[0], this.mRangeBuffers[1], this.mRangeBuffers[2], this.mRangeBuffers[3], this.mRenderPaint);
                    canvas.drawLine(this.mOpenBuffers[0], this.mOpenBuffers[1], this.mOpenBuffers[2], this.mOpenBuffers[3], this.mRenderPaint);
                    canvas.drawLine(this.mCloseBuffers[0], this.mCloseBuffers[1], this.mCloseBuffers[2], this.mCloseBuffers[3], this.mRenderPaint);
                }
            }
        }
    }
    
    @Override
    public void drawExtras(final Canvas canvas) {
    }
    
    @Override
    public void drawHighlighted(final Canvas canvas, final Highlight[] array) {
        final CandleData candleData = this.mChart.getCandleData();
        for (int i = 0; i < array.length; ++i) {
            final Highlight highlight = array[i];
            final ICandleDataSet set = candleData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set != null) {
                if (set.isHighlightEnabled()) {
                    final CandleEntry candleEntry = set.getEntryForXValue(highlight.getX(), highlight.getY());
                    if (this.isInBoundsX(candleEntry, set)) {
                        final MPPointD pixelForValues = this.mChart.getTransformer(set.getAxisDependency()).getPixelForValues(candleEntry.getX(), (candleEntry.getLow() * this.mAnimator.getPhaseY() + candleEntry.getHigh() * this.mAnimator.getPhaseY()) / 2.0f);
                        highlight.setDraw((float)pixelForValues.x, (float)pixelForValues.y);
                        this.drawHighlightLines(canvas, (float)pixelForValues.x, (float)pixelForValues.y, set);
                    }
                }
            }
        }
    }
    
    @Override
    public void drawValues(final Canvas canvas) {
        if (this.isDrawingValuesAllowed(this.mChart)) {
            final List<ICandleDataSet> dataSets = this.mChart.getCandleData().getDataSets();
            for (int i = 0; i < dataSets.size(); ++i) {
                final ICandleDataSet set = dataSets.get(i);
                if (this.shouldDrawValues(set)) {
                    this.applyValueTextStyle(set);
                    final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
                    this.mXBounds.set(this.mChart, set);
                    final float[] generateTransformedValuesCandle = transformer.generateTransformedValuesCandle(set, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), this.mXBounds.min, this.mXBounds.max);
                    final float convertDpToPixel = Utils.convertDpToPixel(5.0f);
                    final MPPointF instance = MPPointF.getInstance(set.getIconsOffset());
                    instance.x = Utils.convertDpToPixel(instance.x);
                    instance.y = Utils.convertDpToPixel(instance.y);
                    for (int j = 0; j < generateTransformedValuesCandle.length; j += 2) {
                        final float n = generateTransformedValuesCandle[j];
                        final float n2 = generateTransformedValuesCandle[j + 1];
                        if (!this.mViewPortHandler.isInBoundsRight(n)) {
                            break;
                        }
                        if (this.mViewPortHandler.isInBoundsLeft(n)) {
                            if (this.mViewPortHandler.isInBoundsY(n2)) {
                                final int n3 = j / 2;
                                final CandleEntry candleEntry = set.getEntryForIndex(this.mXBounds.min + n3);
                                if (set.isDrawValuesEnabled()) {
                                    this.drawValue(canvas, set.getValueFormatter(), candleEntry.getHigh(), candleEntry, i, n, n2 - convertDpToPixel, set.getValueTextColor(n3));
                                }
                                final MPPointF mpPointF = instance;
                                if (candleEntry.getIcon() != null && set.isDrawIconsEnabled()) {
                                    final Drawable icon = candleEntry.getIcon();
                                    Utils.drawImage(canvas, icon, (int)(n + mpPointF.x), (int)(n2 + mpPointF.y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                }
                            }
                        }
                    }
                    MPPointF.recycleInstance(instance);
                }
            }
        }
    }
    
    @Override
    public void initBuffers() {
    }
}
