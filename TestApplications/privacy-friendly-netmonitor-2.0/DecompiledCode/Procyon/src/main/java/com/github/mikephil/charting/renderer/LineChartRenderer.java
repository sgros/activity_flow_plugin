// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import android.graphics.Path$Direction;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import java.util.Iterator;
import android.graphics.drawable.Drawable;
import android.graphics.PathEffect;
import com.github.mikephil.charting.utils.Transformer;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import java.util.HashMap;
import android.graphics.Bitmap;
import java.lang.ref.WeakReference;
import android.graphics.Paint;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import android.graphics.Bitmap$Config;
import android.graphics.Canvas;
import android.graphics.Path;

public class LineChartRenderer extends LineRadarRenderer
{
    protected Path cubicFillPath;
    protected Path cubicPath;
    protected Canvas mBitmapCanvas;
    protected Bitmap$Config mBitmapConfig;
    protected LineDataProvider mChart;
    protected Paint mCirclePaintInner;
    private float[] mCirclesBuffer;
    protected WeakReference<Bitmap> mDrawBitmap;
    protected Path mGenerateFilledPathBuffer;
    private HashMap<IDataSet, DataSetImageCache> mImageCaches;
    private float[] mLineBuffer;
    
    public LineChartRenderer(final LineDataProvider mChart, final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mBitmapConfig = Bitmap$Config.ARGB_8888;
        this.cubicPath = new Path();
        this.cubicFillPath = new Path();
        this.mLineBuffer = new float[4];
        this.mGenerateFilledPathBuffer = new Path();
        this.mImageCaches = new HashMap<IDataSet, DataSetImageCache>();
        this.mCirclesBuffer = new float[2];
        this.mChart = mChart;
        (this.mCirclePaintInner = new Paint(1)).setStyle(Paint$Style.FILL);
        this.mCirclePaintInner.setColor(-1);
    }
    
    private void generateFilledPath(final ILineDataSet set, int n, final int n2, final Path path) {
        final float fillLinePosition = set.getFillFormatter().getFillLinePosition(set, this.mChart);
        final float phaseY = this.mAnimator.getPhaseY();
        final boolean b = set.getMode() == LineDataSet.Mode.STEPPED;
        path.reset();
        final Entry entryForIndex = set.getEntryForIndex(n);
        path.moveTo(entryForIndex.getX(), fillLinePosition);
        path.lineTo(entryForIndex.getX(), entryForIndex.getY() * phaseY);
        ++n;
        Entry entryForIndex2 = null;
        Entry entry;
        while (true) {
            entry = entryForIndex2;
            if (n > n2) {
                break;
            }
            entryForIndex2 = set.getEntryForIndex(n);
            if (b && entry != null) {
                path.lineTo(entryForIndex2.getX(), entry.getY() * phaseY);
            }
            path.lineTo(entryForIndex2.getX(), entryForIndex2.getY() * phaseY);
            ++n;
        }
        if (entry != null) {
            path.lineTo(entry.getX(), fillLinePosition);
        }
        path.close();
    }
    
    protected void drawCircles(final Canvas canvas) {
        this.mRenderPaint.setStyle(Paint$Style.FILL);
        final float phaseY = this.mAnimator.getPhaseY();
        this.mCirclesBuffer[0] = 0.0f;
        this.mCirclesBuffer[1] = 0.0f;
        final List<ILineDataSet> dataSets = this.mChart.getLineData().getDataSets();
        for (int i = 0; i < dataSets.size(); ++i) {
            final ILineDataSet key = dataSets.get(i);
            if (key.isVisible() && key.isDrawCirclesEnabled()) {
                if (key.getEntryCount() != 0) {
                    this.mCirclePaintInner.setColor(key.getCircleHoleColor());
                    final Transformer transformer = this.mChart.getTransformer(key.getAxisDependency());
                    this.mXBounds.set(this.mChart, key);
                    final float circleRadius = key.getCircleRadius();
                    final float circleHoleRadius = key.getCircleHoleRadius();
                    final boolean b = key.isDrawCircleHoleEnabled() && circleHoleRadius < circleRadius && circleHoleRadius > 0.0f;
                    final boolean b2 = b && key.getCircleHoleColor() == 1122867;
                    DataSetImageCache value;
                    if (this.mImageCaches.containsKey(key)) {
                        value = this.mImageCaches.get(key);
                    }
                    else {
                        value = new DataSetImageCache();
                        this.mImageCaches.put(key, value);
                    }
                    if (value.init(key)) {
                        value.fill(key, b, b2);
                    }
                    for (int range = this.mXBounds.range, min = this.mXBounds.min, j = this.mXBounds.min; j <= range + min; ++j) {
                        final Entry entryForIndex = key.getEntryForIndex(j);
                        if (entryForIndex == null) {
                            break;
                        }
                        this.mCirclesBuffer[0] = entryForIndex.getX();
                        this.mCirclesBuffer[1] = entryForIndex.getY() * phaseY;
                        transformer.pointValuesToPixel(this.mCirclesBuffer);
                        if (!this.mViewPortHandler.isInBoundsRight(this.mCirclesBuffer[0])) {
                            break;
                        }
                        if (this.mViewPortHandler.isInBoundsLeft(this.mCirclesBuffer[0])) {
                            if (this.mViewPortHandler.isInBoundsY(this.mCirclesBuffer[1])) {
                                final Bitmap bitmap = value.getBitmap(j);
                                if (bitmap != null) {
                                    canvas.drawBitmap(bitmap, this.mCirclesBuffer[0] - circleRadius, this.mCirclesBuffer[1] - circleRadius, (Paint)null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected void drawCubicBezier(final ILineDataSet set) {
        Math.max(0.0f, Math.min(1.0f, this.mAnimator.getPhaseX()));
        final float phaseY = this.mAnimator.getPhaseY();
        final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
        this.mXBounds.set(this.mChart, set);
        final float cubicIntensity = set.getCubicIntensity();
        this.cubicPath.reset();
        if (this.mXBounds.range >= 1) {
            final int n = this.mXBounds.min + 1;
            final int min = this.mXBounds.min;
            final int range = this.mXBounds.range;
            Entry entryForIndex = set.getEntryForIndex(Math.max(n - 2, 0));
            Entry entryForIndex2 = set.getEntryForIndex(Math.max(n - 1, 0));
            int n2 = -1;
            if (entryForIndex2 == null) {
                return;
            }
            this.cubicPath.moveTo(entryForIndex2.getX(), entryForIndex2.getY() * phaseY);
            int i = this.mXBounds.min + 1;
            Entry entryForIndex3 = entryForIndex2;
            while (i <= this.mXBounds.range + this.mXBounds.min) {
                if (n2 != i) {
                    entryForIndex3 = set.getEntryForIndex(i);
                }
                final int n3 = i + 1;
                if (n3 < set.getEntryCount()) {
                    i = n3;
                }
                final Entry entryForIndex4 = set.getEntryForIndex(i);
                this.cubicPath.cubicTo(entryForIndex2.getX() + (entryForIndex3.getX() - entryForIndex.getX()) * cubicIntensity, (entryForIndex2.getY() + (entryForIndex3.getY() - entryForIndex.getY()) * cubicIntensity) * phaseY, entryForIndex3.getX() - (entryForIndex4.getX() - entryForIndex2.getX()) * cubicIntensity, (entryForIndex3.getY() - (entryForIndex4.getY() - entryForIndex2.getY()) * cubicIntensity) * phaseY, entryForIndex3.getX(), entryForIndex3.getY() * phaseY);
                entryForIndex = entryForIndex2;
                entryForIndex2 = entryForIndex3;
                entryForIndex3 = entryForIndex4;
                n2 = i;
                i = n3;
            }
        }
        if (set.isDrawFilledEnabled()) {
            this.cubicFillPath.reset();
            this.cubicFillPath.addPath(this.cubicPath);
            this.drawCubicFill(this.mBitmapCanvas, set, this.cubicFillPath, transformer, this.mXBounds);
        }
        this.mRenderPaint.setColor(set.getColor());
        this.mRenderPaint.setStyle(Paint$Style.STROKE);
        transformer.pathValueToPixel(this.cubicPath);
        this.mBitmapCanvas.drawPath(this.cubicPath, this.mRenderPaint);
        this.mRenderPaint.setPathEffect((PathEffect)null);
    }
    
    protected void drawCubicFill(final Canvas canvas, final ILineDataSet set, final Path path, final Transformer transformer, final XBounds xBounds) {
        final float fillLinePosition = set.getFillFormatter().getFillLinePosition(set, this.mChart);
        path.lineTo(set.getEntryForIndex(xBounds.min + xBounds.range).getX(), fillLinePosition);
        path.lineTo(set.getEntryForIndex(xBounds.min).getX(), fillLinePosition);
        path.close();
        transformer.pathValueToPixel(path);
        final Drawable fillDrawable = set.getFillDrawable();
        if (fillDrawable != null) {
            this.drawFilledPath(canvas, path, fillDrawable);
        }
        else {
            this.drawFilledPath(canvas, path, set.getFillColor(), set.getFillAlpha());
        }
    }
    
    @Override
    public void drawData(final Canvas canvas) {
        final int n = (int)this.mViewPortHandler.getChartWidth();
        final int n2 = (int)this.mViewPortHandler.getChartHeight();
        if (this.mDrawBitmap == null || this.mDrawBitmap.get().getWidth() != n || this.mDrawBitmap.get().getHeight() != n2) {
            if (n <= 0 || n2 <= 0) {
                return;
            }
            this.mDrawBitmap = new WeakReference<Bitmap>(Bitmap.createBitmap(n, n2, this.mBitmapConfig));
            this.mBitmapCanvas = new Canvas((Bitmap)this.mDrawBitmap.get());
        }
        this.mDrawBitmap.get().eraseColor(0);
        for (final ILineDataSet set : this.mChart.getLineData().getDataSets()) {
            if (set.isVisible()) {
                this.drawDataSet(canvas, set);
            }
        }
        canvas.drawBitmap((Bitmap)this.mDrawBitmap.get(), 0.0f, 0.0f, this.mRenderPaint);
    }
    
    protected void drawDataSet(final Canvas canvas, final ILineDataSet set) {
        if (set.getEntryCount() < 1) {
            return;
        }
        this.mRenderPaint.setStrokeWidth(set.getLineWidth());
        this.mRenderPaint.setPathEffect((PathEffect)set.getDashPathEffect());
        switch (LineChartRenderer$1.$SwitchMap$com$github$mikephil$charting$data$LineDataSet$Mode[set.getMode().ordinal()]) {
            default: {
                this.drawLinear(canvas, set);
                break;
            }
            case 4: {
                this.drawHorizontalBezier(set);
                break;
            }
            case 3: {
                this.drawCubicBezier(set);
                break;
            }
        }
        this.mRenderPaint.setPathEffect((PathEffect)null);
    }
    
    @Override
    public void drawExtras(final Canvas canvas) {
        this.drawCircles(canvas);
    }
    
    @Override
    public void drawHighlighted(final Canvas canvas, final Highlight[] array) {
        final LineData lineData = this.mChart.getLineData();
        for (int i = 0; i < array.length; ++i) {
            final Highlight highlight = array[i];
            final ILineDataSet set = lineData.getDataSetByIndex(highlight.getDataSetIndex());
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
    
    protected void drawHorizontalBezier(final ILineDataSet set) {
        final float phaseY = this.mAnimator.getPhaseY();
        final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
        this.mXBounds.set(this.mChart, set);
        this.cubicPath.reset();
        if (this.mXBounds.range >= 1) {
            Entry entryForIndex = set.getEntryForIndex(this.mXBounds.min);
            this.cubicPath.moveTo(entryForIndex.getX(), entryForIndex.getY() * phaseY);
            Entry entryForIndex2;
            for (int i = this.mXBounds.min + 1; i <= this.mXBounds.range + this.mXBounds.min; ++i, entryForIndex = entryForIndex2) {
                entryForIndex2 = set.getEntryForIndex(i);
                final float n = entryForIndex.getX() + (entryForIndex2.getX() - entryForIndex.getX()) / 2.0f;
                this.cubicPath.cubicTo(n, entryForIndex.getY() * phaseY, n, entryForIndex2.getY() * phaseY, entryForIndex2.getX(), entryForIndex2.getY() * phaseY);
            }
        }
        if (set.isDrawFilledEnabled()) {
            this.cubicFillPath.reset();
            this.cubicFillPath.addPath(this.cubicPath);
            this.drawCubicFill(this.mBitmapCanvas, set, this.cubicFillPath, transformer, this.mXBounds);
        }
        this.mRenderPaint.setColor(set.getColor());
        this.mRenderPaint.setStyle(Paint$Style.STROKE);
        transformer.pathValueToPixel(this.cubicPath);
        this.mBitmapCanvas.drawPath(this.cubicPath, this.mRenderPaint);
        this.mRenderPaint.setPathEffect((PathEffect)null);
    }
    
    protected void drawLinear(final Canvas canvas, final ILineDataSet set) {
        final int entryCount = set.getEntryCount();
        final boolean drawSteppedEnabled = set.isDrawSteppedEnabled();
        int b;
        if (drawSteppedEnabled) {
            b = 4;
        }
        else {
            b = 2;
        }
        final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
        final float phaseY = this.mAnimator.getPhaseY();
        this.mRenderPaint.setStyle(Paint$Style.STROKE);
        Canvas mBitmapCanvas;
        if (set.isDashedLineEnabled()) {
            mBitmapCanvas = this.mBitmapCanvas;
        }
        else {
            mBitmapCanvas = canvas;
        }
        this.mXBounds.set(this.mChart, set);
        if (set.isDrawFilledEnabled() && entryCount > 0) {
            this.drawLinearFill(canvas, set, transformer, this.mXBounds);
        }
        if (set.getColors().size() > 1) {
            final int length = this.mLineBuffer.length;
            final int n = b * 2;
            if (length <= n) {
                this.mLineBuffer = new float[b * 4];
            }
            for (int i = this.mXBounds.min; i <= this.mXBounds.range + this.mXBounds.min; ++i) {
                final Entry entryForIndex = set.getEntryForIndex(i);
                if (entryForIndex != null) {
                    this.mLineBuffer[0] = entryForIndex.getX();
                    this.mLineBuffer[1] = entryForIndex.getY() * phaseY;
                    if (i < this.mXBounds.max) {
                        final Entry entryForIndex2 = set.getEntryForIndex(i + 1);
                        if (entryForIndex2 == null) {
                            break;
                        }
                        if (drawSteppedEnabled) {
                            this.mLineBuffer[2] = entryForIndex2.getX();
                            this.mLineBuffer[3] = this.mLineBuffer[1];
                            this.mLineBuffer[4] = this.mLineBuffer[2];
                            this.mLineBuffer[5] = this.mLineBuffer[3];
                            this.mLineBuffer[6] = entryForIndex2.getX();
                            this.mLineBuffer[7] = entryForIndex2.getY() * phaseY;
                        }
                        else {
                            this.mLineBuffer[2] = entryForIndex2.getX();
                            this.mLineBuffer[3] = entryForIndex2.getY() * phaseY;
                        }
                    }
                    else {
                        this.mLineBuffer[2] = this.mLineBuffer[0];
                        this.mLineBuffer[3] = this.mLineBuffer[1];
                    }
                    transformer.pointValuesToPixel(this.mLineBuffer);
                    if (!this.mViewPortHandler.isInBoundsRight(this.mLineBuffer[0])) {
                        break;
                    }
                    if (this.mViewPortHandler.isInBoundsLeft(this.mLineBuffer[2])) {
                        if (this.mViewPortHandler.isInBoundsTop(this.mLineBuffer[1]) || this.mViewPortHandler.isInBoundsBottom(this.mLineBuffer[3])) {
                            this.mRenderPaint.setColor(set.getColor(i));
                            mBitmapCanvas.drawLines(this.mLineBuffer, 0, n, this.mRenderPaint);
                        }
                    }
                }
            }
        }
        else {
            final int length2 = this.mLineBuffer.length;
            final int n2 = entryCount * b;
            if (length2 < Math.max(n2, b) * 2) {
                this.mLineBuffer = new float[Math.max(n2, b) * 4];
            }
            if (set.getEntryForIndex(this.mXBounds.min) != null) {
                int j = this.mXBounds.min;
                int n3 = 0;
                while (j <= this.mXBounds.range + this.mXBounds.min) {
                    int n4;
                    if (j == 0) {
                        n4 = 0;
                    }
                    else {
                        n4 = j - 1;
                    }
                    final Entry entryForIndex3 = set.getEntryForIndex(n4);
                    final Entry entryForIndex4 = set.getEntryForIndex(j);
                    int n5 = n3;
                    if (entryForIndex3 != null) {
                        if (entryForIndex4 == null) {
                            n5 = n3;
                        }
                        else {
                            final float[] mLineBuffer = this.mLineBuffer;
                            final int n6 = n3 + 1;
                            mLineBuffer[n3] = entryForIndex3.getX();
                            final float[] mLineBuffer2 = this.mLineBuffer;
                            final int n7 = n6 + 1;
                            mLineBuffer2[n6] = entryForIndex3.getY() * phaseY;
                            int n8 = n7;
                            if (drawSteppedEnabled) {
                                final float[] mLineBuffer3 = this.mLineBuffer;
                                final int n9 = n7 + 1;
                                mLineBuffer3[n7] = entryForIndex4.getX();
                                final float[] mLineBuffer4 = this.mLineBuffer;
                                final int n10 = n9 + 1;
                                mLineBuffer4[n9] = entryForIndex3.getY() * phaseY;
                                final float[] mLineBuffer5 = this.mLineBuffer;
                                final int n11 = n10 + 1;
                                mLineBuffer5[n10] = entryForIndex4.getX();
                                final float[] mLineBuffer6 = this.mLineBuffer;
                                n8 = n11 + 1;
                                mLineBuffer6[n11] = entryForIndex3.getY() * phaseY;
                            }
                            final float[] mLineBuffer7 = this.mLineBuffer;
                            n5 = n8 + 1;
                            mLineBuffer7[n8] = entryForIndex4.getX();
                            this.mLineBuffer[n5] = entryForIndex4.getY() * phaseY;
                            ++n5;
                        }
                    }
                    ++j;
                    n3 = n5;
                }
                if (n3 > 0) {
                    transformer.pointValuesToPixel(this.mLineBuffer);
                    final int max = Math.max((this.mXBounds.range + 1) * b, b);
                    this.mRenderPaint.setColor(set.getColor());
                    mBitmapCanvas.drawLines(this.mLineBuffer, 0, max * 2, this.mRenderPaint);
                }
            }
        }
        this.mRenderPaint.setPathEffect((PathEffect)null);
    }
    
    protected void drawLinearFill(final Canvas canvas, final ILineDataSet set, final Transformer transformer, final XBounds xBounds) {
        final Path mGenerateFilledPathBuffer = this.mGenerateFilledPathBuffer;
        final int min = xBounds.min;
        final int n = xBounds.range + xBounds.min;
        int n2 = 0;
        int i;
        int n3;
        do {
            i = n2 * 128 + min;
            if ((n3 = i + 128) > n) {
                n3 = n;
            }
            if (i <= n3) {
                this.generateFilledPath(set, i, n3, mGenerateFilledPathBuffer);
                transformer.pathValueToPixel(mGenerateFilledPathBuffer);
                final Drawable fillDrawable = set.getFillDrawable();
                if (fillDrawable != null) {
                    this.drawFilledPath(canvas, mGenerateFilledPathBuffer, fillDrawable);
                }
                else {
                    this.drawFilledPath(canvas, mGenerateFilledPathBuffer, set.getFillColor(), set.getFillAlpha());
                }
            }
            ++n2;
        } while (i <= n3);
    }
    
    @Override
    public void drawValues(final Canvas canvas) {
        if (this.isDrawingValuesAllowed(this.mChart)) {
            final List<ILineDataSet> dataSets = this.mChart.getLineData().getDataSets();
            for (int i = 0; i < dataSets.size(); ++i) {
                final ILineDataSet set = dataSets.get(i);
                if (this.shouldDrawValues(set)) {
                    this.applyValueTextStyle(set);
                    final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
                    int n = (int)(set.getCircleRadius() * 1.75f);
                    if (!set.isDrawCirclesEnabled()) {
                        n /= 2;
                    }
                    this.mXBounds.set(this.mChart, set);
                    final float[] generateTransformedValuesLine = transformer.generateTransformedValuesLine(set, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), this.mXBounds.min, this.mXBounds.max);
                    final MPPointF instance = MPPointF.getInstance(set.getIconsOffset());
                    instance.x = Utils.convertDpToPixel(instance.x);
                    instance.y = Utils.convertDpToPixel(instance.y);
                    for (int j = 0; j < generateTransformedValuesLine.length; j += 2) {
                        final float n2 = generateTransformedValuesLine[j];
                        final float n3 = generateTransformedValuesLine[j + 1];
                        if (!this.mViewPortHandler.isInBoundsRight(n2)) {
                            break;
                        }
                        if (this.mViewPortHandler.isInBoundsLeft(n2)) {
                            if (this.mViewPortHandler.isInBoundsY(n3)) {
                                final int n4 = j / 2;
                                final Entry entryForIndex = set.getEntryForIndex(this.mXBounds.min + n4);
                                if (set.isDrawValuesEnabled()) {
                                    this.drawValue(canvas, set.getValueFormatter(), entryForIndex.getY(), entryForIndex, i, n2, n3 - n, set.getValueTextColor(n4));
                                }
                                final MPPointF mpPointF = instance;
                                if (entryForIndex.getIcon() != null && set.isDrawIconsEnabled()) {
                                    final Drawable icon = entryForIndex.getIcon();
                                    Utils.drawImage(canvas, icon, (int)(n2 + mpPointF.x), (int)(n3 + mpPointF.y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                }
                            }
                        }
                    }
                    MPPointF.recycleInstance(instance);
                }
            }
        }
    }
    
    public Bitmap$Config getBitmapConfig() {
        return this.mBitmapConfig;
    }
    
    @Override
    public void initBuffers() {
    }
    
    public void releaseBitmap() {
        if (this.mBitmapCanvas != null) {
            this.mBitmapCanvas.setBitmap((Bitmap)null);
            this.mBitmapCanvas = null;
        }
        if (this.mDrawBitmap != null) {
            this.mDrawBitmap.get().recycle();
            this.mDrawBitmap.clear();
            this.mDrawBitmap = null;
        }
    }
    
    public void setBitmapConfig(final Bitmap$Config mBitmapConfig) {
        this.mBitmapConfig = mBitmapConfig;
        this.releaseBitmap();
    }
    
    private class DataSetImageCache
    {
        private Bitmap[] circleBitmaps;
        private Path mCirclePathBuffer;
        
        private DataSetImageCache() {
            this.mCirclePathBuffer = new Path();
        }
        
        protected void fill(final ILineDataSet set, final boolean b, final boolean b2) {
            final int circleColorCount = set.getCircleColorCount();
            final float circleRadius = set.getCircleRadius();
            final float circleHoleRadius = set.getCircleHoleRadius();
            for (int i = 0; i < circleColorCount; ++i) {
                final Bitmap$Config argb_4444 = Bitmap$Config.ARGB_4444;
                final int n = (int)(circleRadius * 2.1);
                final Bitmap bitmap = Bitmap.createBitmap(n, n, argb_4444);
                final Canvas canvas = new Canvas(bitmap);
                this.circleBitmaps[i] = bitmap;
                LineChartRenderer.this.mRenderPaint.setColor(set.getCircleColor(i));
                if (b2) {
                    this.mCirclePathBuffer.reset();
                    this.mCirclePathBuffer.addCircle(circleRadius, circleRadius, circleRadius, Path$Direction.CW);
                    this.mCirclePathBuffer.addCircle(circleRadius, circleRadius, circleHoleRadius, Path$Direction.CCW);
                    canvas.drawPath(this.mCirclePathBuffer, LineChartRenderer.this.mRenderPaint);
                }
                else {
                    canvas.drawCircle(circleRadius, circleRadius, circleRadius, LineChartRenderer.this.mRenderPaint);
                    if (b) {
                        canvas.drawCircle(circleRadius, circleRadius, circleHoleRadius, LineChartRenderer.this.mCirclePaintInner);
                    }
                }
            }
        }
        
        protected Bitmap getBitmap(final int n) {
            return this.circleBitmaps[n % this.circleBitmaps.length];
        }
        
        protected boolean init(final ILineDataSet set) {
            final int circleColorCount = set.getCircleColorCount();
            final Bitmap[] circleBitmaps = this.circleBitmaps;
            boolean b = true;
            if (circleBitmaps == null) {
                this.circleBitmaps = new Bitmap[circleColorCount];
            }
            else if (this.circleBitmaps.length != circleColorCount) {
                this.circleBitmaps = new Bitmap[circleColorCount];
            }
            else {
                b = false;
            }
            return b;
        }
    }
}
