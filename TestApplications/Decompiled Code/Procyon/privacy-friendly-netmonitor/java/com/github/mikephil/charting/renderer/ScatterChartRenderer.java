// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import android.graphics.drawable.Drawable;
import java.util.List;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.utils.Transformer;
import android.util.Log;
import java.util.Iterator;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;

public class ScatterChartRenderer extends LineScatterCandleRadarRenderer
{
    protected ScatterDataProvider mChart;
    float[] mPixelBuffer;
    
    public ScatterChartRenderer(final ScatterDataProvider mChart, final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mPixelBuffer = new float[2];
        this.mChart = mChart;
    }
    
    @Override
    public void drawData(final Canvas canvas) {
        for (final IScatterDataSet set : this.mChart.getScatterData().getDataSets()) {
            if (set.isVisible()) {
                this.drawDataSet(canvas, set);
            }
        }
    }
    
    protected void drawDataSet(final Canvas canvas, final IScatterDataSet set) {
        final ViewPortHandler mViewPortHandler = this.mViewPortHandler;
        final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
        final float phaseY = this.mAnimator.getPhaseY();
        final IShapeRenderer shapeRenderer = set.getShapeRenderer();
        if (shapeRenderer == null) {
            Log.i("MISSING", "There's no IShapeRenderer specified for ScatterDataSet");
            return;
        }
        for (int n = (int)Math.min(Math.ceil(set.getEntryCount() * this.mAnimator.getPhaseX()), (float)set.getEntryCount()), i = 0; i < n; ++i) {
            final Entry entryForIndex = set.getEntryForIndex(i);
            this.mPixelBuffer[0] = entryForIndex.getX();
            this.mPixelBuffer[1] = entryForIndex.getY() * phaseY;
            transformer.pointValuesToPixel(this.mPixelBuffer);
            if (!mViewPortHandler.isInBoundsRight(this.mPixelBuffer[0])) {
                break;
            }
            if (mViewPortHandler.isInBoundsLeft(this.mPixelBuffer[0])) {
                if (mViewPortHandler.isInBoundsY(this.mPixelBuffer[1])) {
                    this.mRenderPaint.setColor(set.getColor(i / 2));
                    shapeRenderer.renderShape(canvas, set, this.mViewPortHandler, this.mPixelBuffer[0], this.mPixelBuffer[1], this.mRenderPaint);
                }
            }
        }
    }
    
    @Override
    public void drawExtras(final Canvas canvas) {
    }
    
    @Override
    public void drawHighlighted(final Canvas canvas, final Highlight[] array) {
        final ScatterData scatterData = this.mChart.getScatterData();
        for (int i = 0; i < array.length; ++i) {
            final Highlight highlight = array[i];
            final IScatterDataSet set = scatterData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set != null) {
                if (set.isHighlightEnabled()) {
                    final Entry entryForXValue = set.getEntryForXValue(highlight.getX(), highlight.getY());
                    if (this.isInBoundsX(entryForXValue, set)) {
                        final MPPointD pixelForValues = this.mChart.getTransformer(set.getAxisDependency()).getPixelForValues(entryForXValue.getX(), entryForXValue.getY() * this.mAnimator.getPhaseY());
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
            final List<IScatterDataSet> dataSets = this.mChart.getScatterData().getDataSets();
            for (int i = 0; i < this.mChart.getScatterData().getDataSetCount(); ++i) {
                final IScatterDataSet set = dataSets.get(i);
                if (this.shouldDrawValues(set)) {
                    this.applyValueTextStyle(set);
                    this.mXBounds.set(this.mChart, set);
                    final float[] generateTransformedValuesScatter = this.mChart.getTransformer(set.getAxisDependency()).generateTransformedValuesScatter(set, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), this.mXBounds.min, this.mXBounds.max);
                    final float convertDpToPixel = Utils.convertDpToPixel(set.getScatterShapeSize());
                    final MPPointF instance = MPPointF.getInstance(set.getIconsOffset());
                    instance.x = Utils.convertDpToPixel(instance.x);
                    instance.y = Utils.convertDpToPixel(instance.y);
                    for (int n = 0; n < generateTransformedValuesScatter.length && this.mViewPortHandler.isInBoundsRight(generateTransformedValuesScatter[n]); n += 2) {
                        if (this.mViewPortHandler.isInBoundsLeft(generateTransformedValuesScatter[n])) {
                            final ViewPortHandler mViewPortHandler = this.mViewPortHandler;
                            final int n2 = n + 1;
                            if (mViewPortHandler.isInBoundsY(generateTransformedValuesScatter[n2])) {
                                final int n3 = n / 2;
                                final Entry entryForIndex = set.getEntryForIndex(this.mXBounds.min + n3);
                                if (set.isDrawValuesEnabled()) {
                                    this.drawValue(canvas, set.getValueFormatter(), entryForIndex.getY(), entryForIndex, i, generateTransformedValuesScatter[n], generateTransformedValuesScatter[n2] - convertDpToPixel, set.getValueTextColor(n3 + this.mXBounds.min));
                                }
                                final MPPointF mpPointF = instance;
                                if (entryForIndex.getIcon() != null && set.isDrawIconsEnabled()) {
                                    final Drawable icon = entryForIndex.getIcon();
                                    Utils.drawImage(canvas, icon, (int)(generateTransformedValuesScatter[n] + mpPointF.x), (int)(generateTransformedValuesScatter[n2] + mpPointF.y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
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
