// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import android.graphics.drawable.Drawable;
import java.util.List;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.data.BubbleData;
import android.graphics.Color;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import java.util.Iterator;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;

public class BubbleChartRenderer extends BarLineScatterCandleBubbleRenderer
{
    private float[] _hsvBuffer;
    protected BubbleDataProvider mChart;
    private float[] pointBuffer;
    private float[] sizeBuffer;
    
    public BubbleChartRenderer(final BubbleDataProvider mChart, final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.sizeBuffer = new float[4];
        this.pointBuffer = new float[2];
        this._hsvBuffer = new float[3];
        this.mChart = mChart;
        this.mRenderPaint.setStyle(Paint$Style.FILL);
        this.mHighlightPaint.setStyle(Paint$Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(Utils.convertDpToPixel(1.5f));
    }
    
    @Override
    public void drawData(final Canvas canvas) {
        for (final IBubbleDataSet set : this.mChart.getBubbleData().getDataSets()) {
            if (set.isVisible()) {
                this.drawDataSet(canvas, set);
            }
        }
    }
    
    protected void drawDataSet(final Canvas canvas, final IBubbleDataSet set) {
        final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
        final float phaseY = this.mAnimator.getPhaseY();
        this.mXBounds.set(this.mChart, set);
        this.sizeBuffer[0] = 0.0f;
        this.sizeBuffer[2] = 1.0f;
        transformer.pointValuesToPixel(this.sizeBuffer);
        final boolean normalizeSizeEnabled = set.isNormalizeSizeEnabled();
        final float min = Math.min(Math.abs(this.mViewPortHandler.contentBottom() - this.mViewPortHandler.contentTop()), Math.abs(this.sizeBuffer[2] - this.sizeBuffer[0]));
        for (int i = this.mXBounds.min; i <= this.mXBounds.range + this.mXBounds.min; ++i) {
            final BubbleEntry bubbleEntry = set.getEntryForIndex(i);
            this.pointBuffer[0] = bubbleEntry.getX();
            this.pointBuffer[1] = bubbleEntry.getY() * phaseY;
            transformer.pointValuesToPixel(this.pointBuffer);
            final float n = this.getShapeSize(bubbleEntry.getSize(), set.getMaxSize(), min, normalizeSizeEnabled) / 2.0f;
            if (this.mViewPortHandler.isInBoundsTop(this.pointBuffer[1] + n)) {
                if (this.mViewPortHandler.isInBoundsBottom(this.pointBuffer[1] - n)) {
                    if (this.mViewPortHandler.isInBoundsLeft(this.pointBuffer[0] + n)) {
                        if (!this.mViewPortHandler.isInBoundsRight(this.pointBuffer[0] - n)) {
                            break;
                        }
                        this.mRenderPaint.setColor(set.getColor((int)bubbleEntry.getX()));
                        canvas.drawCircle(this.pointBuffer[0], this.pointBuffer[1], n, this.mRenderPaint);
                    }
                }
            }
        }
    }
    
    @Override
    public void drawExtras(final Canvas canvas) {
    }
    
    @Override
    public void drawHighlighted(final Canvas canvas, final Highlight[] array) {
        final BubbleData bubbleData = this.mChart.getBubbleData();
        final float phaseY = this.mAnimator.getPhaseY();
        for (final Highlight highlight : array) {
            final IBubbleDataSet set = bubbleData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set != null) {
                if (set.isHighlightEnabled()) {
                    final BubbleEntry bubbleEntry = set.getEntryForXValue(highlight.getX(), highlight.getY());
                    if (bubbleEntry.getY() == highlight.getY()) {
                        if (this.isInBoundsX(bubbleEntry, set)) {
                            final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
                            this.sizeBuffer[0] = 0.0f;
                            this.sizeBuffer[2] = 1.0f;
                            transformer.pointValuesToPixel(this.sizeBuffer);
                            final boolean normalizeSizeEnabled = set.isNormalizeSizeEnabled();
                            final float min = Math.min(Math.abs(this.mViewPortHandler.contentBottom() - this.mViewPortHandler.contentTop()), Math.abs(this.sizeBuffer[2] - this.sizeBuffer[0]));
                            this.pointBuffer[0] = bubbleEntry.getX();
                            this.pointBuffer[1] = bubbleEntry.getY() * phaseY;
                            transformer.pointValuesToPixel(this.pointBuffer);
                            highlight.setDraw(this.pointBuffer[0], this.pointBuffer[1]);
                            final float n = this.getShapeSize(bubbleEntry.getSize(), set.getMaxSize(), min, normalizeSizeEnabled) / 2.0f;
                            if (this.mViewPortHandler.isInBoundsTop(this.pointBuffer[1] + n)) {
                                if (this.mViewPortHandler.isInBoundsBottom(this.pointBuffer[1] - n)) {
                                    if (this.mViewPortHandler.isInBoundsLeft(this.pointBuffer[0] + n)) {
                                        if (!this.mViewPortHandler.isInBoundsRight(this.pointBuffer[0] - n)) {
                                            break;
                                        }
                                        final int color = set.getColor((int)bubbleEntry.getX());
                                        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), this._hsvBuffer);
                                        final float[] hsvBuffer = this._hsvBuffer;
                                        hsvBuffer[2] *= 0.5f;
                                        this.mHighlightPaint.setColor(Color.HSVToColor(Color.alpha(color), this._hsvBuffer));
                                        this.mHighlightPaint.setStrokeWidth(set.getHighlightCircleWidth());
                                        canvas.drawCircle(this.pointBuffer[0], this.pointBuffer[1], n, this.mHighlightPaint);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void drawValues(final Canvas canvas) {
        final BubbleData bubbleData = this.mChart.getBubbleData();
        if (bubbleData == null) {
            return;
        }
        if (this.isDrawingValuesAllowed(this.mChart)) {
            final List<IBubbleDataSet> dataSets = bubbleData.getDataSets();
            final float n = (float)Utils.calcTextHeight(this.mValuePaint, "1");
            for (int i = 0; i < dataSets.size(); ++i) {
                final IBubbleDataSet set = dataSets.get(i);
                if (this.shouldDrawValues(set)) {
                    this.applyValueTextStyle(set);
                    float max = Math.max(0.0f, Math.min(1.0f, this.mAnimator.getPhaseX()));
                    final float phaseY = this.mAnimator.getPhaseY();
                    this.mXBounds.set(this.mChart, set);
                    final float[] generateTransformedValuesBubble = this.mChart.getTransformer(set.getAxisDependency()).generateTransformedValuesBubble(set, phaseY, this.mXBounds.min, this.mXBounds.max);
                    if (max == 1.0f) {
                        max = phaseY;
                    }
                    final MPPointF instance = MPPointF.getInstance(set.getIconsOffset());
                    instance.x = Utils.convertDpToPixel(instance.x);
                    instance.y = Utils.convertDpToPixel(instance.y);
                    for (int j = 0; j < generateTransformedValuesBubble.length; j += 2) {
                        final int n2 = j / 2;
                        final int valueTextColor = set.getValueTextColor(this.mXBounds.min + n2);
                        final int argb = Color.argb(Math.round(255.0f * max), Color.red(valueTextColor), Color.green(valueTextColor), Color.blue(valueTextColor));
                        final float n3 = generateTransformedValuesBubble[j];
                        final float n4 = generateTransformedValuesBubble[j + 1];
                        if (!this.mViewPortHandler.isInBoundsRight(n3)) {
                            break;
                        }
                        if (this.mViewPortHandler.isInBoundsLeft(n3)) {
                            if (this.mViewPortHandler.isInBoundsY(n4)) {
                                final BubbleEntry bubbleEntry = set.getEntryForIndex(n2 + this.mXBounds.min);
                                if (set.isDrawValuesEnabled()) {
                                    this.drawValue(canvas, set.getValueFormatter(), bubbleEntry.getSize(), bubbleEntry, i, n3, n4 + 0.5f * n, argb);
                                }
                                final MPPointF mpPointF = instance;
                                if (bubbleEntry.getIcon() != null && set.isDrawIconsEnabled()) {
                                    final Drawable icon = bubbleEntry.getIcon();
                                    Utils.drawImage(canvas, icon, (int)(n3 + mpPointF.x), (int)(n4 + mpPointF.y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                }
                            }
                        }
                    }
                    MPPointF.recycleInstance(instance);
                }
            }
        }
    }
    
    protected float getShapeSize(final float n, final float n2, final float n3, final boolean b) {
        float n4 = n;
        if (b) {
            if (n2 == 0.0f) {
                n4 = 1.0f;
            }
            else {
                n4 = (float)Math.sqrt(n / n2);
            }
        }
        return n3 * n4;
    }
    
    @Override
    public void initBuffers() {
    }
}
