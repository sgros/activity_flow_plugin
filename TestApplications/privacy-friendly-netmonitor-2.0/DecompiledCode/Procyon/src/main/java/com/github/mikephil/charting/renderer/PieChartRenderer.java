// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.formatter.IValueFormatter;
import java.util.List;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import android.graphics.Color;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.PieEntry;
import java.util.Iterator;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.data.PieData;
import android.graphics.Bitmap$Config;
import android.graphics.Path$Direction;
import android.os.Build$VERSION;
import android.text.Layout$Alignment;
import com.github.mikephil.charting.utils.MPPointF;
import android.graphics.Paint$Align;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap;
import java.lang.ref.WeakReference;
import com.github.mikephil.charting.charts.PieChart;
import android.text.TextPaint;
import android.text.StaticLayout;
import android.graphics.RectF;
import android.graphics.Canvas;

public class PieChartRenderer extends DataRenderer
{
    protected Canvas mBitmapCanvas;
    private RectF mCenterTextLastBounds;
    private CharSequence mCenterTextLastValue;
    private StaticLayout mCenterTextLayout;
    private TextPaint mCenterTextPaint;
    protected PieChart mChart;
    protected WeakReference<Bitmap> mDrawBitmap;
    protected Path mDrawCenterTextPathBuffer;
    protected RectF mDrawHighlightedRectF;
    private Paint mEntryLabelsPaint;
    private Path mHoleCirclePath;
    protected Paint mHolePaint;
    private RectF mInnerRectBuffer;
    private Path mPathBuffer;
    private RectF[] mRectBuffer;
    protected Paint mTransparentCirclePaint;
    protected Paint mValueLinePaint;
    
    public PieChartRenderer(final PieChart mChart, final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mCenterTextLastBounds = new RectF();
        this.mRectBuffer = new RectF[] { new RectF(), new RectF(), new RectF() };
        this.mPathBuffer = new Path();
        this.mInnerRectBuffer = new RectF();
        this.mHoleCirclePath = new Path();
        this.mDrawCenterTextPathBuffer = new Path();
        this.mDrawHighlightedRectF = new RectF();
        this.mChart = mChart;
        (this.mHolePaint = new Paint(1)).setColor(-1);
        this.mHolePaint.setStyle(Paint$Style.FILL);
        (this.mTransparentCirclePaint = new Paint(1)).setColor(-1);
        this.mTransparentCirclePaint.setStyle(Paint$Style.FILL);
        this.mTransparentCirclePaint.setAlpha(105);
        (this.mCenterTextPaint = new TextPaint(1)).setColor(-16777216);
        this.mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12.0f));
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(13.0f));
        this.mValuePaint.setColor(-1);
        this.mValuePaint.setTextAlign(Paint$Align.CENTER);
        (this.mEntryLabelsPaint = new Paint(1)).setColor(-1);
        this.mEntryLabelsPaint.setTextAlign(Paint$Align.CENTER);
        this.mEntryLabelsPaint.setTextSize(Utils.convertDpToPixel(13.0f));
        (this.mValueLinePaint = new Paint(1)).setStyle(Paint$Style.STROKE);
    }
    
    protected float calculateMinimumRadiusForSpacedSlice(final MPPointF mpPointF, final float n, final float n2, final float n3, final float n4, float n5, float n6) {
        final float n7 = n6 / 2.0f;
        final float x = mpPointF.x;
        final double n8 = (n5 + n6) * 0.017453292f;
        n6 = x + (float)Math.cos(n8) * n;
        final float n9 = mpPointF.y + (float)Math.sin(n8) * n;
        final float x2 = mpPointF.x;
        final double n10 = (n5 + n7) * 0.017453292f;
        n5 = (float)Math.cos(n10);
        return (float)(n - (float)(Math.sqrt(Math.pow(n6 - n3, 2.0) + Math.pow(n9 - n4, 2.0)) / 2.0 * Math.tan(0.017453292519943295 * ((180.0 - n2) / 2.0))) - Math.sqrt(Math.pow(x2 + n5 * n - (n6 + n3) / 2.0f, 2.0) + Math.pow(mpPointF.y + (float)Math.sin(n10) * n - (n9 + n4) / 2.0f, 2.0)));
    }
    
    protected void drawCenterText(final Canvas canvas) {
        final CharSequence centerText = this.mChart.getCenterText();
        if (this.mChart.isDrawCenterTextEnabled() && centerText != null) {
            final MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
            final MPPointF centerTextOffset = this.mChart.getCenterTextOffset();
            final float n = centerCircleBox.x + centerTextOffset.x;
            final float n2 = centerCircleBox.y + centerTextOffset.y;
            float radius;
            if (this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled()) {
                radius = this.mChart.getRadius() * (this.mChart.getHoleRadius() / 100.0f);
            }
            else {
                radius = this.mChart.getRadius();
            }
            final RectF rectF = this.mRectBuffer[0];
            rectF.left = n - radius;
            rectF.top = n2 - radius;
            rectF.right = n + radius;
            rectF.bottom = n2 + radius;
            final RectF rectF2 = this.mRectBuffer[1];
            rectF2.set(rectF);
            final float n3 = this.mChart.getCenterTextRadiusPercent() / 100.0f;
            if (n3 > 0.0) {
                rectF2.inset((rectF2.width() - rectF2.width() * n3) / 2.0f, (rectF2.height() - rectF2.height() * n3) / 2.0f);
            }
            if (!centerText.equals(this.mCenterTextLastValue) || !rectF2.equals((Object)this.mCenterTextLastBounds)) {
                this.mCenterTextLastBounds.set(rectF2);
                this.mCenterTextLastValue = centerText;
                this.mCenterTextLayout = new StaticLayout(centerText, 0, centerText.length(), this.mCenterTextPaint, (int)Math.max(Math.ceil(this.mCenterTextLastBounds.width()), 1.0), Layout$Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            }
            final float n4 = (float)this.mCenterTextLayout.getHeight();
            canvas.save();
            if (Build$VERSION.SDK_INT >= 18) {
                final Path mDrawCenterTextPathBuffer = this.mDrawCenterTextPathBuffer;
                mDrawCenterTextPathBuffer.reset();
                mDrawCenterTextPathBuffer.addOval(rectF, Path$Direction.CW);
                canvas.clipPath(mDrawCenterTextPathBuffer);
            }
            canvas.translate(rectF2.left, rectF2.top + (rectF2.height() - n4) / 2.0f);
            this.mCenterTextLayout.draw(canvas);
            canvas.restore();
            MPPointF.recycleInstance(centerCircleBox);
            MPPointF.recycleInstance(centerTextOffset);
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
            this.mDrawBitmap = new WeakReference<Bitmap>(Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_4444));
            this.mBitmapCanvas = new Canvas((Bitmap)this.mDrawBitmap.get());
        }
        this.mDrawBitmap.get().eraseColor(0);
        for (final IPieDataSet set : this.mChart.getData().getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0) {
                this.drawDataSet(canvas, set);
            }
        }
    }
    
    protected void drawDataSet(final Canvas canvas, final IPieDataSet set) {
        PieChartRenderer pieChartRenderer = this;
        final float rotationAngle = pieChartRenderer.mChart.getRotationAngle();
        final float phaseX = pieChartRenderer.mAnimator.getPhaseX();
        final float phaseY = pieChartRenderer.mAnimator.getPhaseY();
        final RectF circleBox = pieChartRenderer.mChart.getCircleBox();
        final int entryCount = set.getEntryCount();
        final float[] drawAngles = pieChartRenderer.mChart.getDrawAngles();
        MPPointF centerCircleBox = pieChartRenderer.mChart.getCenterCircleBox();
        final float radius = pieChartRenderer.mChart.getRadius();
        final boolean drawHoleEnabled = pieChartRenderer.mChart.isDrawHoleEnabled();
        final int n = 1;
        final boolean b = drawHoleEnabled && !pieChartRenderer.mChart.isDrawSlicesUnderHoleEnabled();
        float a;
        if (b) {
            a = pieChartRenderer.mChart.getHoleRadius() / 100.0f * radius;
        }
        else {
            a = 0.0f;
        }
        int n2;
        int n3;
        for (int i = n2 = 0; i < entryCount; ++i, n2 = n3) {
            n3 = n2;
            if (Math.abs(set.getEntryForIndex(i).getY()) > Utils.FLOAT_EPSILON) {
                n3 = n2 + 1;
            }
        }
        float sliceSpace;
        if (n2 <= 1) {
            sliceSpace = 0.0f;
        }
        else {
            sliceSpace = pieChartRenderer.getSliceSpace(set);
        }
        final int n4 = 0;
        float n5 = 0.0f;
        final int n6 = entryCount;
        int n7 = n;
        for (int j = n4; j < n6; ++j) {
            final float n8 = drawAngles[j];
            if (Math.abs(set.getEntryForIndex(j).getY()) > Utils.FLOAT_EPSILON && !pieChartRenderer.mChart.needsHighlight(j)) {
                int n9;
                if (sliceSpace > 0.0f && n8 <= 180.0f) {
                    n9 = n7;
                }
                else {
                    n9 = 0;
                }
                pieChartRenderer.mRenderPaint.setColor(set.getColor(j));
                float n10;
                if (n2 == 1) {
                    n10 = 0.0f;
                }
                else {
                    n10 = sliceSpace / (0.017453292f * radius);
                }
                final float n11 = rotationAngle + (n5 + n10 / 2.0f) * phaseY;
                float n12;
                if ((n12 = (n8 - n10) * phaseY) < 0.0f) {
                    n12 = 0.0f;
                }
                pieChartRenderer.mPathBuffer.reset();
                final float x = centerCircleBox.x;
                final double n13 = n11 * 0.017453292f;
                final float n14 = x + (float)Math.cos(n13) * radius;
                final float n15 = centerCircleBox.y + (float)Math.sin(n13) * radius;
                if (n12 >= 360.0f && n12 % 360.0f <= Utils.FLOAT_EPSILON) {
                    pieChartRenderer.mPathBuffer.addCircle(centerCircleBox.x, centerCircleBox.y, radius, Path$Direction.CW);
                }
                else {
                    pieChartRenderer.mPathBuffer.moveTo(n14, n15);
                    pieChartRenderer.mPathBuffer.arcTo(circleBox, n11, n12);
                }
                pieChartRenderer.mInnerRectBuffer.set(centerCircleBox.x - a, centerCircleBox.y - a, centerCircleBox.x + a, centerCircleBox.y + a);
                PieChartRenderer pieChartRenderer2;
                if (b && (a > 0.0f || n9 != 0)) {
                    float max;
                    if (n9 != 0) {
                        float calculateMinimumRadiusForSpacedSlice;
                        final float n16 = calculateMinimumRadiusForSpacedSlice = pieChartRenderer.calculateMinimumRadiusForSpacedSlice(centerCircleBox, radius, n8 * phaseY, n14, n15, n11, n12);
                        if (n16 < 0.0f) {
                            calculateMinimumRadiusForSpacedSlice = -n16;
                        }
                        max = Math.max(a, calculateMinimumRadiusForSpacedSlice);
                    }
                    else {
                        max = a;
                    }
                    final float n17 = n12;
                    float n18;
                    if (n2 != 1 && max != 0.0f) {
                        n18 = sliceSpace / (0.017453292f * max);
                    }
                    else {
                        n18 = 0.0f;
                    }
                    final float n19 = n18 / 2.0f;
                    float n20;
                    if ((n20 = (n8 - n18) * phaseY) < 0.0f) {
                        n20 = 0.0f;
                    }
                    final float n21 = (n5 + n19) * phaseY + rotationAngle + n20;
                    if (n17 >= 360.0f && n17 % 360.0f <= Utils.FLOAT_EPSILON) {
                        this.mPathBuffer.addCircle(centerCircleBox.x, centerCircleBox.y, max, Path$Direction.CCW);
                    }
                    else {
                        final Path mPathBuffer = this.mPathBuffer;
                        final float x2 = centerCircleBox.x;
                        final double n22 = n21 * 0.017453292f;
                        mPathBuffer.lineTo(x2 + (float)Math.cos(n22) * max, centerCircleBox.y + max * (float)Math.sin(n22));
                        this.mPathBuffer.arcTo(this.mInnerRectBuffer, n21, -n20);
                    }
                    n7 = 1;
                    pieChartRenderer2 = this;
                }
                else {
                    final int n23 = 1;
                    final MPPointF mpPointF = centerCircleBox;
                    pieChartRenderer2 = pieChartRenderer;
                    centerCircleBox = mpPointF;
                    n7 = n23;
                    if (n12 % 360.0f > Utils.FLOAT_EPSILON) {
                        if (n9 != 0) {
                            final float n24 = n12 / 2.0f;
                            final float calculateMinimumRadiusForSpacedSlice2 = pieChartRenderer.calculateMinimumRadiusForSpacedSlice(mpPointF, radius, n8 * phaseY, n14, n15, n11, n12);
                            final float x3 = mpPointF.x;
                            final double n25 = (n11 + n24) * 0.017453292f;
                            pieChartRenderer.mPathBuffer.lineTo(x3 + (float)Math.cos(n25) * calculateMinimumRadiusForSpacedSlice2, mpPointF.y + calculateMinimumRadiusForSpacedSlice2 * (float)Math.sin(n25));
                            pieChartRenderer2 = pieChartRenderer;
                            centerCircleBox = mpPointF;
                            n7 = n23;
                        }
                        else {
                            pieChartRenderer.mPathBuffer.lineTo(mpPointF.x, mpPointF.y);
                            n7 = n23;
                            centerCircleBox = mpPointF;
                            pieChartRenderer2 = pieChartRenderer;
                        }
                    }
                }
                pieChartRenderer2.mPathBuffer.close();
                pieChartRenderer2.mBitmapCanvas.drawPath(pieChartRenderer2.mPathBuffer, pieChartRenderer2.mRenderPaint);
                pieChartRenderer = pieChartRenderer2;
            }
            n5 += n8 * phaseX;
        }
        MPPointF.recycleInstance(centerCircleBox);
    }
    
    protected void drawEntryLabel(final Canvas canvas, final String s, final float n, final float n2) {
        canvas.drawText(s, n, n2, this.mEntryLabelsPaint);
    }
    
    @Override
    public void drawExtras(final Canvas canvas) {
        this.drawHole(canvas);
        canvas.drawBitmap((Bitmap)this.mDrawBitmap.get(), 0.0f, 0.0f, (Paint)null);
        this.drawCenterText(canvas);
    }
    
    @Override
    public void drawHighlighted(final Canvas canvas, final Highlight[] array) {
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float rotationAngle = this.mChart.getRotationAngle();
        final float[] drawAngles = this.mChart.getDrawAngles();
        final float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        final MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        final float radius = this.mChart.getRadius();
        final boolean b = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled();
        float a;
        if (b) {
            a = this.mChart.getHoleRadius() / 100.0f * radius;
        }
        else {
            a = 0.0f;
        }
        final RectF mDrawHighlightedRectF = this.mDrawHighlightedRectF;
        mDrawHighlightedRectF.set(0.0f, 0.0f, 0.0f, 0.0f);
        for (int i = 0; i < array.length; ++i) {
            final int n = (int)array[i].getX();
            if (n < drawAngles.length) {
                final IPieDataSet dataSetByIndex = this.mChart.getData().getDataSetByIndex(array[i].getDataSetIndex());
                if (dataSetByIndex != null) {
                    if (dataSetByIndex.isHighlightEnabled()) {
                        int n2;
                        int n3;
                        for (int entryCount = dataSetByIndex.getEntryCount(), j = n2 = 0; j < entryCount; ++j, n2 = n3) {
                            n3 = n2;
                            if (Math.abs(dataSetByIndex.getEntryForIndex(j).getY()) > Utils.FLOAT_EPSILON) {
                                n3 = n2 + 1;
                            }
                        }
                        float n4;
                        if (n == 0) {
                            n4 = 0.0f;
                        }
                        else {
                            n4 = absoluteAngles[n - 1] * phaseX;
                        }
                        float sliceSpace;
                        if (n2 <= 1) {
                            sliceSpace = 0.0f;
                        }
                        else {
                            sliceSpace = dataSetByIndex.getSliceSpace();
                        }
                        final float n5 = drawAngles[n];
                        final float selectionShift = dataSetByIndex.getSelectionShift();
                        final float n6 = radius + selectionShift;
                        mDrawHighlightedRectF.set(this.mChart.getCircleBox());
                        final float n7 = -selectionShift;
                        mDrawHighlightedRectF.inset(n7, n7);
                        final boolean b2 = sliceSpace > 0.0f && n5 <= 180.0f;
                        this.mRenderPaint.setColor(dataSetByIndex.getColor(n));
                        float n8;
                        if (n2 == 1) {
                            n8 = 0.0f;
                        }
                        else {
                            n8 = sliceSpace / (0.017453292f * radius);
                        }
                        float n9;
                        if (n2 == 1) {
                            n9 = 0.0f;
                        }
                        else {
                            n9 = sliceSpace / (0.017453292f * n6);
                        }
                        final float n10 = rotationAngle + (n4 + n8 / 2.0f) * phaseY;
                        float n11 = (n5 - n8) * phaseY;
                        if (n11 < 0.0f) {
                            n11 = 0.0f;
                        }
                        final float n12 = (n4 + n9 / 2.0f) * phaseY + rotationAngle;
                        float n13;
                        if ((n13 = (n5 - n9) * phaseY) < 0.0f) {
                            n13 = 0.0f;
                        }
                        this.mPathBuffer.reset();
                        if (n11 >= 360.0f && n11 % 360.0f <= Utils.FLOAT_EPSILON) {
                            this.mPathBuffer.addCircle(centerCircleBox.x, centerCircleBox.y, n6, Path$Direction.CW);
                        }
                        else {
                            final Path mPathBuffer = this.mPathBuffer;
                            final float x = centerCircleBox.x;
                            final double n14 = n12 * 0.017453292f;
                            mPathBuffer.moveTo(x + (float)Math.cos(n14) * n6, centerCircleBox.y + n6 * (float)Math.sin(n14));
                            this.mPathBuffer.arcTo(mDrawHighlightedRectF, n12, n13);
                        }
                        float calculateMinimumRadiusForSpacedSlice;
                        if (b2) {
                            final float x2 = centerCircleBox.x;
                            final double n15 = n10 * 0.017453292f;
                            calculateMinimumRadiusForSpacedSlice = this.calculateMinimumRadiusForSpacedSlice(centerCircleBox, radius, n5 * phaseY, (float)Math.cos(n15) * radius + x2, centerCircleBox.y + (float)Math.sin(n15) * radius, n10, n11);
                        }
                        else {
                            calculateMinimumRadiusForSpacedSlice = 0.0f;
                        }
                        this.mInnerRectBuffer.set(centerCircleBox.x - a, centerCircleBox.y - a, centerCircleBox.x + a, centerCircleBox.y + a);
                        if (b && (a > 0.0f || b2)) {
                            float max;
                            if (b2) {
                                float b3 = calculateMinimumRadiusForSpacedSlice;
                                if (calculateMinimumRadiusForSpacedSlice < 0.0f) {
                                    b3 = -calculateMinimumRadiusForSpacedSlice;
                                }
                                max = Math.max(a, b3);
                            }
                            else {
                                max = a;
                            }
                            float n16;
                            if (n2 != 1 && max != 0.0f) {
                                n16 = sliceSpace / (0.017453292f * max);
                            }
                            else {
                                n16 = 0.0f;
                            }
                            final float n17 = n16 / 2.0f;
                            float n18;
                            if ((n18 = (n5 - n16) * phaseY) < 0.0f) {
                                n18 = 0.0f;
                            }
                            final float n19 = rotationAngle + (n4 + n17) * phaseY + n18;
                            if (n11 >= 360.0f && n11 % 360.0f <= Utils.FLOAT_EPSILON) {
                                this.mPathBuffer.addCircle(centerCircleBox.x, centerCircleBox.y, max, Path$Direction.CCW);
                            }
                            else {
                                final Path mPathBuffer2 = this.mPathBuffer;
                                final float x3 = centerCircleBox.x;
                                final double n20 = n19 * 0.017453292f;
                                mPathBuffer2.lineTo(x3 + (float)Math.cos(n20) * max, centerCircleBox.y + max * (float)Math.sin(n20));
                                this.mPathBuffer.arcTo(this.mInnerRectBuffer, n19, -n18);
                            }
                        }
                        else if (n11 % 360.0f > Utils.FLOAT_EPSILON) {
                            if (b2) {
                                final float n21 = n11 / 2.0f;
                                final float x4 = centerCircleBox.x;
                                final double n22 = (n10 + n21) * 0.017453292f;
                                this.mPathBuffer.lineTo(x4 + (float)Math.cos(n22) * calculateMinimumRadiusForSpacedSlice, centerCircleBox.y + calculateMinimumRadiusForSpacedSlice * (float)Math.sin(n22));
                            }
                            else {
                                this.mPathBuffer.lineTo(centerCircleBox.x, centerCircleBox.y);
                            }
                        }
                        this.mPathBuffer.close();
                        this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
                    }
                }
            }
        }
        MPPointF.recycleInstance(centerCircleBox);
    }
    
    protected void drawHole(final Canvas canvas) {
        if (this.mChart.isDrawHoleEnabled() && this.mBitmapCanvas != null) {
            final float radius = this.mChart.getRadius();
            final float n = this.mChart.getHoleRadius() / 100.0f * radius;
            final MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
            if (Color.alpha(this.mHolePaint.getColor()) > 0) {
                this.mBitmapCanvas.drawCircle(centerCircleBox.x, centerCircleBox.y, n, this.mHolePaint);
            }
            if (Color.alpha(this.mTransparentCirclePaint.getColor()) > 0 && this.mChart.getTransparentCircleRadius() > this.mChart.getHoleRadius()) {
                final int alpha = this.mTransparentCirclePaint.getAlpha();
                final float n2 = this.mChart.getTransparentCircleRadius() / 100.0f;
                this.mTransparentCirclePaint.setAlpha((int)(alpha * this.mAnimator.getPhaseX() * this.mAnimator.getPhaseY()));
                this.mHoleCirclePath.reset();
                this.mHoleCirclePath.addCircle(centerCircleBox.x, centerCircleBox.y, radius * n2, Path$Direction.CW);
                this.mHoleCirclePath.addCircle(centerCircleBox.x, centerCircleBox.y, n, Path$Direction.CCW);
                this.mBitmapCanvas.drawPath(this.mHoleCirclePath, this.mTransparentCirclePaint);
                this.mTransparentCirclePaint.setAlpha(alpha);
            }
            MPPointF.recycleInstance(centerCircleBox);
        }
    }
    
    protected void drawRoundedSlices(final Canvas canvas) {
        if (!this.mChart.isDrawRoundedSlicesEnabled()) {
            return;
        }
        final IPieDataSet dataSet = this.mChart.getData().getDataSet();
        if (!dataSet.isVisible()) {
            return;
        }
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        final float radius = this.mChart.getRadius();
        final float n = (radius - this.mChart.getHoleRadius() * radius / 100.0f) / 2.0f;
        final float[] drawAngles = this.mChart.getDrawAngles();
        float rotationAngle = this.mChart.getRotationAngle();
        for (int i = 0; i < dataSet.getEntryCount(); ++i) {
            final float n2 = drawAngles[i];
            if (Math.abs(((IDataSet<Entry>)dataSet).getEntryForIndex(i).getY()) > Utils.FLOAT_EPSILON) {
                final double n3 = radius - n;
                final double n4 = (rotationAngle + n2) * phaseY;
                final float n5 = (float)(Math.cos(Math.toRadians(n4)) * n3 + centerCircleBox.x);
                final float n6 = (float)(n3 * Math.sin(Math.toRadians(n4)) + centerCircleBox.y);
                this.mRenderPaint.setColor(dataSet.getColor(i));
                this.mBitmapCanvas.drawCircle(n5, n6, n, this.mRenderPaint);
            }
            rotationAngle += n2 * phaseX;
        }
        MPPointF.recycleInstance(centerCircleBox);
    }
    
    @Override
    public void drawValues(final Canvas canvas) {
        MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        float radius = this.mChart.getRadius();
        final float rotationAngle = this.mChart.getRotationAngle();
        float[] drawAngles = this.mChart.getDrawAngles();
        float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float n = this.mChart.getHoleRadius() / 100.0f;
        float n2 = radius / 10.0f * 3.6f;
        if (this.mChart.isDrawHoleEnabled()) {
            n2 = (radius - radius * n) / 2.0f;
        }
        final float n3 = radius - n2;
        PieData pieData = this.mChart.getData();
        List<IPieDataSet> dataSets = pieData.getDataSets();
        final float yValueSum = pieData.getYValueSum();
        final boolean drawEntryLabelsEnabled = this.mChart.isDrawEntryLabelsEnabled();
        canvas.save();
        final float convertDpToPixel = Utils.convertDpToPixel(5.0f);
        int i;
        int n4 = i = 0;
        float n5 = rotationAngle;
        while (i < dataSets.size()) {
            final IPieDataSet set = dataSets.get(i);
            final boolean drawValuesEnabled = set.isDrawValuesEnabled();
            MPPointF mpPointF;
            float[] array;
            PieData pieData2;
            List<IPieDataSet> list2;
            float n7;
            float n8;
            if (!drawValuesEnabled && !drawEntryLabelsEnabled) {
                final List<IPieDataSet> list = dataSets;
                mpPointF = centerCircleBox;
                final float n6 = radius;
                array = drawAngles;
                pieData2 = pieData;
                list2 = list;
                n7 = n5;
                n8 = n6;
            }
            else {
                final PieDataSet.ValuePosition xValuePosition = set.getXValuePosition();
                final PieDataSet.ValuePosition yValuePosition = set.getYValuePosition();
                this.applyValueTextStyle(set);
                final float n9 = Utils.calcTextHeight(this.mValuePaint, "Q") + Utils.convertDpToPixel(4.0f);
                final IValueFormatter valueFormatter = set.getValueFormatter();
                final int entryCount = set.getEntryCount();
                this.mValueLinePaint.setColor(set.getValueLineColor());
                this.mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(set.getValueLineWidth()));
                final float sliceSpace = this.getSliceSpace(set);
                final MPPointF instance = MPPointF.getInstance(set.getIconsOffset());
                instance.x = Utils.convertDpToPixel(instance.x);
                instance.y = Utils.convertDpToPixel(instance.y);
                final int n10 = 0;
                final float[] array2 = absoluteAngles;
                MPPointF mpPointF2 = centerCircleBox;
                final List<IPieDataSet> list3 = dataSets;
                final int n11 = i;
                final IPieDataSet set2 = set;
                PieData pieData3;
                MPPointF mpPointF4;
                float[] array3;
                MPPointF mpPointF7;
                for (int j = n10; j < entryCount; ++j, array3 = drawAngles, mpPointF7 = mpPointF4, pieData = pieData3, mpPointF2 = mpPointF7, drawAngles = array3) {
                    final PieEntry pieEntry = set2.getEntryForIndex(j);
                    float n12;
                    if (n4 == 0) {
                        n12 = 0.0f;
                    }
                    else {
                        n12 = array2[n4 - 1] * phaseX;
                    }
                    final float n13 = n5 + (n12 + (drawAngles[n4] - sliceSpace / (0.017453292f * n3) / 2.0f) / 2.0f) * phaseY;
                    float y;
                    if (this.mChart.isUsePercentValuesEnabled()) {
                        y = pieEntry.getY() / yValueSum * 100.0f;
                    }
                    else {
                        y = pieEntry.getY();
                    }
                    final double a = n13 * 0.017453292f;
                    final float n14 = (float)Math.cos(a);
                    final float n15 = (float)Math.sin(a);
                    final boolean b = drawEntryLabelsEnabled && xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                    final boolean b2 = drawValuesEnabled && yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                    final boolean b3 = drawEntryLabelsEnabled && xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                    final boolean b4 = drawValuesEnabled && yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                    if (b || b2) {
                        final float valueLinePart1Length = set2.getValueLinePart1Length();
                        final float valueLinePart2Length = set2.getValueLinePart2Length();
                        final float n16 = set2.getValueLinePart1OffsetPercentage() / 100.0f;
                        float n18;
                        if (this.mChart.isDrawHoleEnabled()) {
                            final float n17 = radius * n;
                            n18 = (radius - n17) * n16 + n17;
                        }
                        else {
                            n18 = radius * n16;
                        }
                        float n19;
                        if (set2.isValueLineVariableLength()) {
                            n19 = valueLinePart2Length * n3 * (float)Math.abs(Math.sin(a));
                        }
                        else {
                            n19 = valueLinePart2Length * n3;
                        }
                        final float x = mpPointF2.x;
                        final float y2 = mpPointF2.y;
                        final float n20 = (1.0f + valueLinePart1Length) * n3;
                        final float n21 = mpPointF2.x + n20 * n14;
                        final float n22 = n20 * n15 + mpPointF2.y;
                        final double n23 = n13 % 360.0;
                        float n25;
                        float n26;
                        if (n23 >= 90.0 && n23 <= 270.0) {
                            final float n24 = n21 - n19;
                            this.mValuePaint.setTextAlign(Paint$Align.RIGHT);
                            if (b) {
                                this.mEntryLabelsPaint.setTextAlign(Paint$Align.RIGHT);
                            }
                            n25 = n24;
                            n26 = n24 - convertDpToPixel;
                        }
                        else {
                            n25 = n21 + n19;
                            this.mValuePaint.setTextAlign(Paint$Align.LEFT);
                            if (b) {
                                this.mEntryLabelsPaint.setTextAlign(Paint$Align.LEFT);
                            }
                            n26 = n25 + convertDpToPixel;
                        }
                        if (set2.getValueLineColor() != 1122867) {
                            canvas.drawLine(n18 * n14 + x, n18 * n15 + y2, n21, n22, this.mValueLinePaint);
                            canvas.drawLine(n21, n22, n25, n22, this.mValueLinePaint);
                        }
                        final IPieDataSet set3 = set2;
                        final int n27 = j;
                        if (b && b2) {
                            final int valueTextColor = set3.getValueTextColor(n27);
                            final PieEntry pieEntry2 = pieEntry;
                            this.drawValue(canvas, valueFormatter, y, pieEntry, 0, n26, n22, valueTextColor);
                            if (n27 < pieData.getEntryCount() && pieEntry2.getLabel() != null) {
                                this.drawEntryLabel(canvas, pieEntry2.getLabel(), n26, n22 + n9);
                            }
                        }
                        else {
                            final PieEntry pieEntry3 = pieEntry;
                            if (b) {
                                if (n27 < pieData.getEntryCount() && pieEntry3.getLabel() != null) {
                                    this.drawEntryLabel(canvas, pieEntry3.getLabel(), n26, n22 + n9 / 2.0f);
                                }
                            }
                            else if (b2) {
                                this.drawValue(canvas, valueFormatter, y, pieEntry3, 0, n26, n22 + n9 / 2.0f, set3.getValueTextColor(n27));
                            }
                        }
                    }
                    final MPPointF mpPointF3 = mpPointF2;
                    pieData3 = pieData;
                    if (b3 || b4) {
                        final float n28 = n3 * n14 + mpPointF3.x;
                        final float n29 = n3 * n15 + mpPointF3.y;
                        this.mValuePaint.setTextAlign(Paint$Align.CENTER);
                        if (b3 && b4) {
                            this.drawValue(canvas, valueFormatter, y, pieEntry, 0, n28, n29, set2.getValueTextColor(j));
                            if (j < pieData3.getEntryCount() && pieEntry.getLabel() != null) {
                                this.drawEntryLabel(canvas, pieEntry.getLabel(), n28, n29 + n9);
                            }
                        }
                        else if (b3) {
                            if (j < pieData3.getEntryCount() && pieEntry.getLabel() != null) {
                                this.drawEntryLabel(canvas, pieEntry.getLabel(), n28, n29 + n9 / 2.0f);
                            }
                        }
                        else if (b4) {
                            this.drawValue(canvas, valueFormatter, y, pieEntry, 0, n28, n29 + n9 / 2.0f, set2.getValueTextColor(j));
                        }
                    }
                    mpPointF4 = mpPointF3;
                    if (pieEntry.getIcon() != null && set2.isDrawIconsEnabled()) {
                        final Drawable icon = pieEntry.getIcon();
                        final MPPointF mpPointF5 = instance;
                        final float y3 = mpPointF5.y;
                        final MPPointF mpPointF6 = mpPointF4;
                        Utils.drawImage(canvas, icon, (int)((n3 + y3) * n14 + mpPointF6.x), (int)((n3 + mpPointF5.y) * n15 + mpPointF6.y + mpPointF5.x), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                    }
                    ++n4;
                }
                final List<IPieDataSet> list4 = list3;
                mpPointF = mpPointF2;
                final float n30 = radius;
                n7 = n5;
                array = drawAngles;
                absoluteAngles = array2;
                pieData2 = pieData;
                MPPointF.recycleInstance(instance);
                n8 = n30;
                list2 = list4;
                i = n11;
            }
            ++i;
            final PieData pieData4 = pieData2;
            final float n31 = n7;
            drawAngles = array;
            centerCircleBox = mpPointF;
            dataSets = list2;
            pieData = pieData4;
            radius = n8;
            n5 = n31;
        }
        MPPointF.recycleInstance(centerCircleBox);
        canvas.restore();
    }
    
    public TextPaint getPaintCenterText() {
        return this.mCenterTextPaint;
    }
    
    public Paint getPaintEntryLabels() {
        return this.mEntryLabelsPaint;
    }
    
    public Paint getPaintHole() {
        return this.mHolePaint;
    }
    
    public Paint getPaintTransparentCircle() {
        return this.mTransparentCirclePaint;
    }
    
    protected float getSliceSpace(final IPieDataSet set) {
        if (!set.isAutomaticallyDisableSliceSpacingEnabled()) {
            return set.getSliceSpace();
        }
        float sliceSpace;
        if (set.getSliceSpace() / this.mViewPortHandler.getSmallestContentExtension() > set.getYMin() / this.mChart.getData().getYValueSum() * 2.0f) {
            sliceSpace = 0.0f;
        }
        else {
            sliceSpace = set.getSliceSpace();
        }
        return sliceSpace;
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
}
