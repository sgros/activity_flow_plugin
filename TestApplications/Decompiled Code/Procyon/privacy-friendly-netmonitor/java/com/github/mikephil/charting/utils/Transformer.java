// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import android.graphics.RectF;
import java.util.List;
import android.graphics.Path;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import android.graphics.Matrix;

public class Transformer
{
    private Matrix mMBuffer1;
    private Matrix mMBuffer2;
    protected Matrix mMatrixOffset;
    protected Matrix mMatrixValueToPx;
    protected Matrix mPixelToValueMatrixBuffer;
    protected ViewPortHandler mViewPortHandler;
    float[] ptsBuffer;
    protected float[] valuePointsForGenerateTransformedValuesBubble;
    protected float[] valuePointsForGenerateTransformedValuesCandle;
    protected float[] valuePointsForGenerateTransformedValuesLine;
    protected float[] valuePointsForGenerateTransformedValuesScatter;
    
    public Transformer(final ViewPortHandler mViewPortHandler) {
        this.mMatrixValueToPx = new Matrix();
        this.mMatrixOffset = new Matrix();
        this.valuePointsForGenerateTransformedValuesScatter = new float[1];
        this.valuePointsForGenerateTransformedValuesBubble = new float[1];
        this.valuePointsForGenerateTransformedValuesLine = new float[1];
        this.valuePointsForGenerateTransformedValuesCandle = new float[1];
        this.mPixelToValueMatrixBuffer = new Matrix();
        this.ptsBuffer = new float[2];
        this.mMBuffer1 = new Matrix();
        this.mMBuffer2 = new Matrix();
        this.mViewPortHandler = mViewPortHandler;
    }
    
    public float[] generateTransformedValuesBubble(final IBubbleDataSet set, final float n, final int n2, int i) {
        final int n3 = (i - n2 + 1) * 2;
        if (this.valuePointsForGenerateTransformedValuesBubble.length != n3) {
            this.valuePointsForGenerateTransformedValuesBubble = new float[n3];
        }
        final float[] valuePointsForGenerateTransformedValuesBubble = this.valuePointsForGenerateTransformedValuesBubble;
        Entry entryForIndex;
        for (i = 0; i < n3; i += 2) {
            entryForIndex = ((IDataSet<Entry>)set).getEntryForIndex(i / 2 + n2);
            if (entryForIndex != null) {
                valuePointsForGenerateTransformedValuesBubble[i] = entryForIndex.getX();
                valuePointsForGenerateTransformedValuesBubble[i + 1] = entryForIndex.getY() * n;
            }
            else {
                valuePointsForGenerateTransformedValuesBubble[i + 1] = (valuePointsForGenerateTransformedValuesBubble[i] = 0.0f);
            }
        }
        this.getValueToPixelMatrix().mapPoints(valuePointsForGenerateTransformedValuesBubble);
        return valuePointsForGenerateTransformedValuesBubble;
    }
    
    public float[] generateTransformedValuesCandle(final ICandleDataSet set, final float n, final float n2, final int n3, int i) {
        final int n4 = (int)((i - n3) * n + 1.0f) * 2;
        if (this.valuePointsForGenerateTransformedValuesCandle.length != n4) {
            this.valuePointsForGenerateTransformedValuesCandle = new float[n4];
        }
        final float[] valuePointsForGenerateTransformedValuesCandle = this.valuePointsForGenerateTransformedValuesCandle;
        CandleEntry candleEntry;
        for (i = 0; i < n4; i += 2) {
            candleEntry = set.getEntryForIndex(i / 2 + n3);
            if (candleEntry != null) {
                valuePointsForGenerateTransformedValuesCandle[i] = candleEntry.getX();
                valuePointsForGenerateTransformedValuesCandle[i + 1] = candleEntry.getHigh() * n2;
            }
            else {
                valuePointsForGenerateTransformedValuesCandle[i + 1] = (valuePointsForGenerateTransformedValuesCandle[i] = 0.0f);
            }
        }
        this.getValueToPixelMatrix().mapPoints(valuePointsForGenerateTransformedValuesCandle);
        return valuePointsForGenerateTransformedValuesCandle;
    }
    
    public float[] generateTransformedValuesLine(final ILineDataSet set, final float n, final float n2, final int n3, int i) {
        final int n4 = ((int)((i - n3) * n) + 1) * 2;
        if (this.valuePointsForGenerateTransformedValuesLine.length != n4) {
            this.valuePointsForGenerateTransformedValuesLine = new float[n4];
        }
        final float[] valuePointsForGenerateTransformedValuesLine = this.valuePointsForGenerateTransformedValuesLine;
        Entry entryForIndex;
        for (i = 0; i < n4; i += 2) {
            entryForIndex = set.getEntryForIndex(i / 2 + n3);
            if (entryForIndex != null) {
                valuePointsForGenerateTransformedValuesLine[i] = entryForIndex.getX();
                valuePointsForGenerateTransformedValuesLine[i + 1] = entryForIndex.getY() * n2;
            }
            else {
                valuePointsForGenerateTransformedValuesLine[i + 1] = (valuePointsForGenerateTransformedValuesLine[i] = 0.0f);
            }
        }
        this.getValueToPixelMatrix().mapPoints(valuePointsForGenerateTransformedValuesLine);
        return valuePointsForGenerateTransformedValuesLine;
    }
    
    public float[] generateTransformedValuesScatter(final IScatterDataSet set, final float n, final float n2, final int n3, int i) {
        final int n4 = (int)((i - n3) * n + 1.0f) * 2;
        if (this.valuePointsForGenerateTransformedValuesScatter.length != n4) {
            this.valuePointsForGenerateTransformedValuesScatter = new float[n4];
        }
        final float[] valuePointsForGenerateTransformedValuesScatter = this.valuePointsForGenerateTransformedValuesScatter;
        Entry entryForIndex;
        for (i = 0; i < n4; i += 2) {
            entryForIndex = set.getEntryForIndex(i / 2 + n3);
            if (entryForIndex != null) {
                valuePointsForGenerateTransformedValuesScatter[i] = entryForIndex.getX();
                valuePointsForGenerateTransformedValuesScatter[i + 1] = entryForIndex.getY() * n2;
            }
            else {
                valuePointsForGenerateTransformedValuesScatter[i + 1] = (valuePointsForGenerateTransformedValuesScatter[i] = 0.0f);
            }
        }
        this.getValueToPixelMatrix().mapPoints(valuePointsForGenerateTransformedValuesScatter);
        return valuePointsForGenerateTransformedValuesScatter;
    }
    
    public Matrix getOffsetMatrix() {
        return this.mMatrixOffset;
    }
    
    public MPPointD getPixelForValues(final float n, final float n2) {
        this.ptsBuffer[0] = n;
        this.ptsBuffer[1] = n2;
        this.pointValuesToPixel(this.ptsBuffer);
        return MPPointD.getInstance(this.ptsBuffer[0], this.ptsBuffer[1]);
    }
    
    public Matrix getPixelToValueMatrix() {
        this.getValueToPixelMatrix().invert(this.mMBuffer2);
        return this.mMBuffer2;
    }
    
    public Matrix getValueMatrix() {
        return this.mMatrixValueToPx;
    }
    
    public Matrix getValueToPixelMatrix() {
        this.mMBuffer1.set(this.mMatrixValueToPx);
        this.mMBuffer1.postConcat(this.mViewPortHandler.mMatrixTouch);
        this.mMBuffer1.postConcat(this.mMatrixOffset);
        return this.mMBuffer1;
    }
    
    public MPPointD getValuesByTouchPoint(final float n, final float n2) {
        final MPPointD instance = MPPointD.getInstance(0.0, 0.0);
        this.getValuesByTouchPoint(n, n2, instance);
        return instance;
    }
    
    public void getValuesByTouchPoint(final float n, final float n2, final MPPointD mpPointD) {
        this.ptsBuffer[0] = n;
        this.ptsBuffer[1] = n2;
        this.pixelsToValue(this.ptsBuffer);
        mpPointD.x = this.ptsBuffer[0];
        mpPointD.y = this.ptsBuffer[1];
    }
    
    public void pathValueToPixel(final Path path) {
        path.transform(this.mMatrixValueToPx);
        path.transform(this.mViewPortHandler.getMatrixTouch());
        path.transform(this.mMatrixOffset);
    }
    
    public void pathValuesToPixel(final List<Path> list) {
        for (int i = 0; i < list.size(); ++i) {
            this.pathValueToPixel(list.get(i));
        }
    }
    
    public void pixelsToValue(final float[] array) {
        final Matrix mPixelToValueMatrixBuffer = this.mPixelToValueMatrixBuffer;
        mPixelToValueMatrixBuffer.reset();
        this.mMatrixOffset.invert(mPixelToValueMatrixBuffer);
        mPixelToValueMatrixBuffer.mapPoints(array);
        this.mViewPortHandler.getMatrixTouch().invert(mPixelToValueMatrixBuffer);
        mPixelToValueMatrixBuffer.mapPoints(array);
        this.mMatrixValueToPx.invert(mPixelToValueMatrixBuffer);
        mPixelToValueMatrixBuffer.mapPoints(array);
    }
    
    public void pointValuesToPixel(final float[] array) {
        this.mMatrixValueToPx.mapPoints(array);
        this.mViewPortHandler.getMatrixTouch().mapPoints(array);
        this.mMatrixOffset.mapPoints(array);
    }
    
    public void prepareMatrixOffset(final boolean b) {
        this.mMatrixOffset.reset();
        if (!b) {
            this.mMatrixOffset.postTranslate(this.mViewPortHandler.offsetLeft(), this.mViewPortHandler.getChartHeight() - this.mViewPortHandler.offsetBottom());
        }
        else {
            this.mMatrixOffset.setTranslate(this.mViewPortHandler.offsetLeft(), -this.mViewPortHandler.offsetTop());
            this.mMatrixOffset.postScale(1.0f, -1.0f);
        }
    }
    
    public void prepareMatrixValuePx(final float n, float n2, float n3, final float n4) {
        final float v = this.mViewPortHandler.contentWidth() / n2;
        final float v2 = this.mViewPortHandler.contentHeight() / n3;
        n2 = v;
        if (Float.isInfinite(v)) {
            n2 = 0.0f;
        }
        n3 = v2;
        if (Float.isInfinite(v2)) {
            n3 = 0.0f;
        }
        this.mMatrixValueToPx.reset();
        this.mMatrixValueToPx.postTranslate(-n, -n4);
        this.mMatrixValueToPx.postScale(n2, -n3);
    }
    
    public void rectToPixelPhase(final RectF rectF, final float n) {
        rectF.top *= n;
        rectF.bottom *= n;
        this.mMatrixValueToPx.mapRect(rectF);
        this.mViewPortHandler.getMatrixTouch().mapRect(rectF);
        this.mMatrixOffset.mapRect(rectF);
    }
    
    public void rectToPixelPhaseHorizontal(final RectF rectF, final float n) {
        rectF.left *= n;
        rectF.right *= n;
        this.mMatrixValueToPx.mapRect(rectF);
        this.mViewPortHandler.getMatrixTouch().mapRect(rectF);
        this.mMatrixOffset.mapRect(rectF);
    }
    
    public void rectValueToPixel(final RectF rectF) {
        this.mMatrixValueToPx.mapRect(rectF);
        this.mViewPortHandler.getMatrixTouch().mapRect(rectF);
        this.mMatrixOffset.mapRect(rectF);
    }
    
    public void rectValueToPixelHorizontal(final RectF rectF) {
        this.mMatrixValueToPx.mapRect(rectF);
        this.mViewPortHandler.getMatrixTouch().mapRect(rectF);
        this.mMatrixOffset.mapRect(rectF);
    }
    
    public void rectValueToPixelHorizontal(final RectF rectF, final float n) {
        rectF.left *= n;
        rectF.right *= n;
        this.mMatrixValueToPx.mapRect(rectF);
        this.mViewPortHandler.getMatrixTouch().mapRect(rectF);
        this.mMatrixOffset.mapRect(rectF);
    }
    
    public void rectValuesToPixel(final List<RectF> list) {
        final Matrix valueToPixelMatrix = this.getValueToPixelMatrix();
        for (int i = 0; i < list.size(); ++i) {
            valueToPixelMatrix.mapRect((RectF)list.get(i));
        }
    }
}
