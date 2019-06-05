package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class ScatterChartRenderer extends LineScatterCandleRadarRenderer {
    protected ScatterDataProvider mChart;
    float[] mPixelBuffer = new float[2];

    public void drawExtras(Canvas canvas) {
    }

    public void initBuffers() {
    }

    public ScatterChartRenderer(ScatterDataProvider scatterDataProvider, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mChart = scatterDataProvider;
    }

    public void drawData(Canvas canvas) {
        for (IScatterDataSet iScatterDataSet : this.mChart.getScatterData().getDataSets()) {
            if (iScatterDataSet.isVisible()) {
                drawDataSet(canvas, iScatterDataSet);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawDataSet(Canvas canvas, IScatterDataSet iScatterDataSet) {
        IScatterDataSet iScatterDataSet2 = iScatterDataSet;
        ViewPortHandler viewPortHandler = this.mViewPortHandler;
        Transformer transformer = this.mChart.getTransformer(iScatterDataSet.getAxisDependency());
        float phaseY = this.mAnimator.getPhaseY();
        IShapeRenderer shapeRenderer = iScatterDataSet.getShapeRenderer();
        if (shapeRenderer == null) {
            Log.i("MISSING", "There's no IShapeRenderer specified for ScatterDataSet");
            return;
        }
        int min = (int) Math.min(Math.ceil((double) (((float) iScatterDataSet.getEntryCount()) * this.mAnimator.getPhaseX())), (double) ((float) iScatterDataSet.getEntryCount()));
        for (int i = 0; i < min; i++) {
            Entry entryForIndex = iScatterDataSet2.getEntryForIndex(i);
            this.mPixelBuffer[0] = entryForIndex.getX();
            this.mPixelBuffer[1] = entryForIndex.getY() * phaseY;
            transformer.pointValuesToPixel(this.mPixelBuffer);
            if (!viewPortHandler.isInBoundsRight(this.mPixelBuffer[0])) {
                break;
            }
            if (viewPortHandler.isInBoundsLeft(this.mPixelBuffer[0]) && viewPortHandler.isInBoundsY(this.mPixelBuffer[1])) {
                this.mRenderPaint.setColor(iScatterDataSet2.getColor(i / 2));
                shapeRenderer.renderShape(canvas, iScatterDataSet2, this.mViewPortHandler, this.mPixelBuffer[0], this.mPixelBuffer[1], this.mRenderPaint);
            }
        }
    }

    public void drawValues(Canvas canvas) {
        ScatterChartRenderer scatterChartRenderer = this;
        if (scatterChartRenderer.isDrawingValuesAllowed(scatterChartRenderer.mChart)) {
            List dataSets = scatterChartRenderer.mChart.getScatterData().getDataSets();
            int i = 0;
            while (i < scatterChartRenderer.mChart.getScatterData().getDataSetCount()) {
                IScatterDataSet iScatterDataSet = (IScatterDataSet) dataSets.get(i);
                if (scatterChartRenderer.shouldDrawValues(iScatterDataSet)) {
                    scatterChartRenderer.applyValueTextStyle(iScatterDataSet);
                    scatterChartRenderer.mXBounds.set(scatterChartRenderer.mChart, iScatterDataSet);
                    float[] generateTransformedValuesScatter = scatterChartRenderer.mChart.getTransformer(iScatterDataSet.getAxisDependency()).generateTransformedValuesScatter(iScatterDataSet, scatterChartRenderer.mAnimator.getPhaseX(), scatterChartRenderer.mAnimator.getPhaseY(), scatterChartRenderer.mXBounds.min, scatterChartRenderer.mXBounds.max);
                    float convertDpToPixel = Utils.convertDpToPixel(iScatterDataSet.getScatterShapeSize());
                    MPPointF instance = MPPointF.getInstance(iScatterDataSet.getIconsOffset());
                    instance.f488x = Utils.convertDpToPixel(instance.f488x);
                    instance.f489y = Utils.convertDpToPixel(instance.f489y);
                    int i2 = 0;
                    while (i2 < generateTransformedValuesScatter.length && scatterChartRenderer.mViewPortHandler.isInBoundsRight(generateTransformedValuesScatter[i2])) {
                        int i3;
                        MPPointF mPPointF;
                        if (scatterChartRenderer.mViewPortHandler.isInBoundsLeft(generateTransformedValuesScatter[i2])) {
                            int i4 = i2 + 1;
                            if (scatterChartRenderer.mViewPortHandler.isInBoundsY(generateTransformedValuesScatter[i4])) {
                                Entry entry;
                                int i5 = i2 / 2;
                                Entry entryForIndex = iScatterDataSet.getEntryForIndex(scatterChartRenderer.mXBounds.min + i5);
                                if (iScatterDataSet.isDrawValuesEnabled()) {
                                    IValueFormatter valueFormatter = iScatterDataSet.getValueFormatter();
                                    float y = entryForIndex.getY();
                                    float f = generateTransformedValuesScatter[i2];
                                    float f2 = generateTransformedValuesScatter[i4] - convertDpToPixel;
                                    int valueTextColor = iScatterDataSet.getValueTextColor(i5 + scatterChartRenderer.mXBounds.min);
                                    entry = entryForIndex;
                                    float f3 = f;
                                    i3 = i2;
                                    mPPointF = instance;
                                    scatterChartRenderer.drawValue(canvas, valueFormatter, y, entryForIndex, i, f3, f2, valueTextColor);
                                } else {
                                    entry = entryForIndex;
                                    i3 = i2;
                                    mPPointF = instance;
                                }
                                if (entry.getIcon() != null && iScatterDataSet.isDrawIconsEnabled()) {
                                    Drawable icon = entry.getIcon();
                                    Utils.drawImage(canvas, icon, (int) (generateTransformedValuesScatter[i3] + mPPointF.f488x), (int) (generateTransformedValuesScatter[i4] + mPPointF.f489y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                }
                                i2 = i3 + 2;
                                instance = mPPointF;
                                scatterChartRenderer = this;
                            }
                        }
                        i3 = i2;
                        mPPointF = instance;
                        i2 = i3 + 2;
                        instance = mPPointF;
                        scatterChartRenderer = this;
                    }
                    MPPointF.recycleInstance(instance);
                }
                i++;
                scatterChartRenderer = this;
            }
        }
    }

    public void drawHighlighted(Canvas canvas, Highlight[] highlightArr) {
        ScatterData scatterData = this.mChart.getScatterData();
        for (Highlight highlight : highlightArr) {
            IScatterDataSet iScatterDataSet = (IScatterDataSet) scatterData.getDataSetByIndex(highlight.getDataSetIndex());
            if (iScatterDataSet != null && iScatterDataSet.isHighlightEnabled()) {
                Entry entryForXValue = iScatterDataSet.getEntryForXValue(highlight.getX(), highlight.getY());
                if (isInBoundsX(entryForXValue, iScatterDataSet)) {
                    MPPointD pixelForValues = this.mChart.getTransformer(iScatterDataSet.getAxisDependency()).getPixelForValues(entryForXValue.getX(), entryForXValue.getY() * this.mAnimator.getPhaseY());
                    highlight.setDraw((float) pixelForValues.f486x, (float) pixelForValues.f487y);
                    drawHighlightLines(canvas, (float) pixelForValues.f486x, (float) pixelForValues.f487y, iScatterDataSet);
                }
            }
        }
    }
}
