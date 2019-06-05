package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class CandleStickChartRenderer extends LineScatterCandleRadarRenderer {
    private float[] mBodyBuffers = new float[4];
    protected CandleDataProvider mChart;
    private float[] mCloseBuffers = new float[4];
    private float[] mOpenBuffers = new float[4];
    private float[] mRangeBuffers = new float[4];
    private float[] mShadowBuffers = new float[8];

    public void drawExtras(Canvas canvas) {
    }

    public void initBuffers() {
    }

    public CandleStickChartRenderer(CandleDataProvider candleDataProvider, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mChart = candleDataProvider;
    }

    public void drawData(Canvas canvas) {
        for (ICandleDataSet iCandleDataSet : this.mChart.getCandleData().getDataSets()) {
            if (iCandleDataSet.isVisible()) {
                drawDataSet(canvas, iCandleDataSet);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawDataSet(Canvas canvas, ICandleDataSet iCandleDataSet) {
        ICandleDataSet iCandleDataSet2 = iCandleDataSet;
        Transformer transformer = this.mChart.getTransformer(iCandleDataSet.getAxisDependency());
        float phaseY = this.mAnimator.getPhaseY();
        float barSpace = iCandleDataSet.getBarSpace();
        boolean showCandleBar = iCandleDataSet.getShowCandleBar();
        this.mXBounds.set(this.mChart, iCandleDataSet2);
        this.mRenderPaint.setStrokeWidth(iCandleDataSet.getShadowWidth());
        for (int i = this.mXBounds.min; i <= this.mXBounds.range + this.mXBounds.min; i++) {
            CandleEntry candleEntry = (CandleEntry) iCandleDataSet2.getEntryForIndex(i);
            Canvas canvas2;
            if (candleEntry == null) {
                canvas2 = canvas;
            } else {
                float x = candleEntry.getX();
                float open = candleEntry.getOpen();
                float close = candleEntry.getClose();
                float high = candleEntry.getHigh();
                float low = candleEntry.getLow();
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
                    } else if (open < close) {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = close * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = open * phaseY;
                    } else {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = open * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = this.mShadowBuffers[3];
                    }
                    transformer.pointValuesToPixel(this.mShadowBuffers);
                    Paint paint;
                    int color;
                    if (!iCandleDataSet.getShadowColorSameAsCandle()) {
                        paint = this.mRenderPaint;
                        if (iCandleDataSet.getShadowColor() == ColorTemplate.COLOR_NONE) {
                            color = iCandleDataSet2.getColor(i);
                        } else {
                            color = iCandleDataSet.getShadowColor();
                        }
                        paint.setColor(color);
                    } else if (open > close) {
                        paint = this.mRenderPaint;
                        if (iCandleDataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE) {
                            color = iCandleDataSet2.getColor(i);
                        } else {
                            color = iCandleDataSet.getDecreasingColor();
                        }
                        paint.setColor(color);
                    } else if (open < close) {
                        paint = this.mRenderPaint;
                        if (iCandleDataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE) {
                            color = iCandleDataSet2.getColor(i);
                        } else {
                            color = iCandleDataSet.getIncreasingColor();
                        }
                        paint.setColor(color);
                    } else {
                        paint = this.mRenderPaint;
                        if (iCandleDataSet.getNeutralColor() == ColorTemplate.COLOR_NONE) {
                            color = iCandleDataSet2.getColor(i);
                        } else {
                            color = iCandleDataSet.getNeutralColor();
                        }
                        paint.setColor(color);
                    }
                    this.mRenderPaint.setStyle(Style.STROKE);
                    canvas2 = canvas;
                    canvas2.drawLines(this.mShadowBuffers, this.mRenderPaint);
                    this.mBodyBuffers[0] = (x - 0.5f) + barSpace;
                    this.mBodyBuffers[1] = close * phaseY;
                    this.mBodyBuffers[2] = (x + 0.5f) - barSpace;
                    this.mBodyBuffers[3] = open * phaseY;
                    transformer.pointValuesToPixel(this.mBodyBuffers);
                    if (open > close) {
                        if (iCandleDataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE) {
                            this.mRenderPaint.setColor(iCandleDataSet2.getColor(i));
                        } else {
                            this.mRenderPaint.setColor(iCandleDataSet.getDecreasingColor());
                        }
                        this.mRenderPaint.setStyle(iCandleDataSet.getDecreasingPaintStyle());
                        canvas2.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[3], this.mBodyBuffers[2], this.mBodyBuffers[1], this.mRenderPaint);
                    } else if (open < close) {
                        if (iCandleDataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE) {
                            this.mRenderPaint.setColor(iCandleDataSet2.getColor(i));
                        } else {
                            this.mRenderPaint.setColor(iCandleDataSet.getIncreasingColor());
                        }
                        this.mRenderPaint.setStyle(iCandleDataSet.getIncreasingPaintStyle());
                        canvas2.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
                    } else {
                        if (iCandleDataSet.getNeutralColor() == ColorTemplate.COLOR_NONE) {
                            this.mRenderPaint.setColor(iCandleDataSet2.getColor(i));
                        } else {
                            this.mRenderPaint.setColor(iCandleDataSet.getNeutralColor());
                        }
                        canvas2.drawLine(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
                    }
                } else {
                    int color2;
                    canvas2 = canvas;
                    this.mRangeBuffers[0] = x;
                    this.mRangeBuffers[1] = high * phaseY;
                    this.mRangeBuffers[2] = x;
                    this.mRangeBuffers[3] = low * phaseY;
                    this.mOpenBuffers[0] = (x - 0.5f) + barSpace;
                    float f = open * phaseY;
                    this.mOpenBuffers[1] = f;
                    this.mOpenBuffers[2] = x;
                    this.mOpenBuffers[3] = f;
                    this.mCloseBuffers[0] = (0.5f + x) - barSpace;
                    high = close * phaseY;
                    this.mCloseBuffers[1] = high;
                    this.mCloseBuffers[2] = x;
                    this.mCloseBuffers[3] = high;
                    transformer.pointValuesToPixel(this.mRangeBuffers);
                    transformer.pointValuesToPixel(this.mOpenBuffers);
                    transformer.pointValuesToPixel(this.mCloseBuffers);
                    if (open > close) {
                        if (iCandleDataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE) {
                            color2 = iCandleDataSet2.getColor(i);
                        } else {
                            color2 = iCandleDataSet.getDecreasingColor();
                        }
                    } else if (open < close) {
                        if (iCandleDataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE) {
                            color2 = iCandleDataSet2.getColor(i);
                        } else {
                            color2 = iCandleDataSet.getIncreasingColor();
                        }
                    } else if (iCandleDataSet.getNeutralColor() == ColorTemplate.COLOR_NONE) {
                        color2 = iCandleDataSet2.getColor(i);
                    } else {
                        color2 = iCandleDataSet.getNeutralColor();
                    }
                    this.mRenderPaint.setColor(color2);
                    Canvas canvas3 = canvas2;
                    canvas3.drawLine(this.mRangeBuffers[0], this.mRangeBuffers[1], this.mRangeBuffers[2], this.mRangeBuffers[3], this.mRenderPaint);
                    canvas3.drawLine(this.mOpenBuffers[0], this.mOpenBuffers[1], this.mOpenBuffers[2], this.mOpenBuffers[3], this.mRenderPaint);
                    canvas3.drawLine(this.mCloseBuffers[0], this.mCloseBuffers[1], this.mCloseBuffers[2], this.mCloseBuffers[3], this.mRenderPaint);
                }
            }
        }
    }

    public void drawValues(Canvas canvas) {
        if (isDrawingValuesAllowed(this.mChart)) {
            List dataSets = this.mChart.getCandleData().getDataSets();
            for (int i = 0; i < dataSets.size(); i++) {
                ICandleDataSet iCandleDataSet = (ICandleDataSet) dataSets.get(i);
                if (shouldDrawValues(iCandleDataSet)) {
                    applyValueTextStyle(iCandleDataSet);
                    Transformer transformer = this.mChart.getTransformer(iCandleDataSet.getAxisDependency());
                    this.mXBounds.set(this.mChart, iCandleDataSet);
                    float[] generateTransformedValuesCandle = transformer.generateTransformedValuesCandle(iCandleDataSet, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), this.mXBounds.min, this.mXBounds.max);
                    float convertDpToPixel = Utils.convertDpToPixel(5.0f);
                    MPPointF instance = MPPointF.getInstance(iCandleDataSet.getIconsOffset());
                    instance.f488x = Utils.convertDpToPixel(instance.f488x);
                    instance.f489y = Utils.convertDpToPixel(instance.f489y);
                    int i2 = 0;
                    while (i2 < generateTransformedValuesCandle.length) {
                        float f = generateTransformedValuesCandle[i2];
                        float f2 = generateTransformedValuesCandle[i2 + 1];
                        if (!this.mViewPortHandler.isInBoundsRight(f)) {
                            break;
                        }
                        int i3;
                        MPPointF mPPointF;
                        if (this.mViewPortHandler.isInBoundsLeft(f) && this.mViewPortHandler.isInBoundsY(f2)) {
                            float f3;
                            float f4;
                            CandleEntry candleEntry;
                            int i4 = i2 / 2;
                            CandleEntry candleEntry2 = (CandleEntry) iCandleDataSet.getEntryForIndex(this.mXBounds.min + i4);
                            if (iCandleDataSet.isDrawValuesEnabled()) {
                                CandleEntry candleEntry3 = candleEntry2;
                                f3 = f2;
                                f4 = f;
                                i3 = i2;
                                mPPointF = instance;
                                drawValue(canvas, iCandleDataSet.getValueFormatter(), candleEntry2.getHigh(), candleEntry2, i, f, f2 - convertDpToPixel, iCandleDataSet.getValueTextColor(i4));
                                candleEntry = candleEntry3;
                            } else {
                                f3 = f2;
                                f4 = f;
                                i3 = i2;
                                mPPointF = instance;
                                candleEntry = candleEntry2;
                            }
                            if (candleEntry.getIcon() != null && iCandleDataSet.isDrawIconsEnabled()) {
                                Drawable icon = candleEntry.getIcon();
                                Utils.drawImage(canvas, icon, (int) (f4 + mPPointF.f488x), (int) (f3 + mPPointF.f489y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                            }
                        } else {
                            i3 = i2;
                            mPPointF = instance;
                        }
                        i2 = i3 + 2;
                        instance = mPPointF;
                    }
                    MPPointF.recycleInstance(instance);
                }
            }
        }
    }

    public void drawHighlighted(Canvas canvas, Highlight[] highlightArr) {
        CandleData candleData = this.mChart.getCandleData();
        for (Highlight highlight : highlightArr) {
            ICandleDataSet iCandleDataSet = (ICandleDataSet) candleData.getDataSetByIndex(highlight.getDataSetIndex());
            if (iCandleDataSet != null && iCandleDataSet.isHighlightEnabled()) {
                CandleEntry candleEntry = (CandleEntry) iCandleDataSet.getEntryForXValue(highlight.getX(), highlight.getY());
                if (isInBoundsX(candleEntry, iCandleDataSet)) {
                    MPPointD pixelForValues = this.mChart.getTransformer(iCandleDataSet.getAxisDependency()).getPixelForValues(candleEntry.getX(), ((candleEntry.getLow() * this.mAnimator.getPhaseY()) + (candleEntry.getHigh() * this.mAnimator.getPhaseY())) / 2.0f);
                    highlight.setDraw((float) pixelForValues.f486x, (float) pixelForValues.f487y);
                    drawHighlightLines(canvas, (float) pixelForValues.f486x, (float) pixelForValues.f487y, iCandleDataSet);
                }
            }
        }
    }
}
