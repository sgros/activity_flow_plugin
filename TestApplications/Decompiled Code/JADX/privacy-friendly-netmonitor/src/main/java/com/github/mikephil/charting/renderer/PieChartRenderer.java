package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.support.p000v4.view.ViewCompat;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.lang.ref.WeakReference;

public class PieChartRenderer extends DataRenderer {
    protected Canvas mBitmapCanvas;
    private RectF mCenterTextLastBounds = new RectF();
    private CharSequence mCenterTextLastValue;
    private StaticLayout mCenterTextLayout;
    private TextPaint mCenterTextPaint;
    protected PieChart mChart;
    protected WeakReference<Bitmap> mDrawBitmap;
    protected Path mDrawCenterTextPathBuffer = new Path();
    protected RectF mDrawHighlightedRectF = new RectF();
    private Paint mEntryLabelsPaint;
    private Path mHoleCirclePath = new Path();
    protected Paint mHolePaint;
    private RectF mInnerRectBuffer = new RectF();
    private Path mPathBuffer = new Path();
    private RectF[] mRectBuffer = new RectF[]{new RectF(), new RectF(), new RectF()};
    protected Paint mTransparentCirclePaint;
    protected Paint mValueLinePaint;

    public void initBuffers() {
    }

    public PieChartRenderer(PieChart pieChart, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mChart = pieChart;
        this.mHolePaint = new Paint(1);
        this.mHolePaint.setColor(-1);
        this.mHolePaint.setStyle(Style.FILL);
        this.mTransparentCirclePaint = new Paint(1);
        this.mTransparentCirclePaint.setColor(-1);
        this.mTransparentCirclePaint.setStyle(Style.FILL);
        this.mTransparentCirclePaint.setAlpha(105);
        this.mCenterTextPaint = new TextPaint(1);
        this.mCenterTextPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12.0f));
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(13.0f));
        this.mValuePaint.setColor(-1);
        this.mValuePaint.setTextAlign(Align.CENTER);
        this.mEntryLabelsPaint = new Paint(1);
        this.mEntryLabelsPaint.setColor(-1);
        this.mEntryLabelsPaint.setTextAlign(Align.CENTER);
        this.mEntryLabelsPaint.setTextSize(Utils.convertDpToPixel(13.0f));
        this.mValueLinePaint = new Paint(1);
        this.mValueLinePaint.setStyle(Style.STROKE);
    }

    public Paint getPaintHole() {
        return this.mHolePaint;
    }

    public Paint getPaintTransparentCircle() {
        return this.mTransparentCirclePaint;
    }

    public TextPaint getPaintCenterText() {
        return this.mCenterTextPaint;
    }

    public Paint getPaintEntryLabels() {
        return this.mEntryLabelsPaint;
    }

    public void drawData(Canvas canvas) {
        int chartWidth = (int) this.mViewPortHandler.getChartWidth();
        int chartHeight = (int) this.mViewPortHandler.getChartHeight();
        if (!(this.mDrawBitmap != null && ((Bitmap) this.mDrawBitmap.get()).getWidth() == chartWidth && ((Bitmap) this.mDrawBitmap.get()).getHeight() == chartHeight)) {
            if (chartWidth > 0 && chartHeight > 0) {
                this.mDrawBitmap = new WeakReference(Bitmap.createBitmap(chartWidth, chartHeight, Config.ARGB_4444));
                this.mBitmapCanvas = new Canvas((Bitmap) this.mDrawBitmap.get());
            } else {
                return;
            }
        }
        ((Bitmap) this.mDrawBitmap.get()).eraseColor(0);
        for (IPieDataSet iPieDataSet : ((PieData) this.mChart.getData()).getDataSets()) {
            if (iPieDataSet.isVisible() && iPieDataSet.getEntryCount() > 0) {
                drawDataSet(canvas, iPieDataSet);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public float calculateMinimumRadiusForSpacedSlice(MPPointF mPPointF, float f, float f2, float f3, float f4, float f5, float f6) {
        MPPointF mPPointF2 = mPPointF;
        double d = (double) ((f5 + f6) * 0.017453292f);
        float cos = mPPointF2.f488x + (((float) Math.cos(d)) * f);
        float sin = mPPointF2.f489y + (((float) Math.sin(d)) * f);
        double d2 = (double) ((f5 + (f6 / 2.0f)) * 0.017453292f);
        return (float) (((double) (f - ((float) ((Math.sqrt(Math.pow((double) (cos - f3), 2.0d) + Math.pow((double) (sin - f4), 2.0d)) / 2.0d) * Math.tan(0.017453292519943295d * ((180.0d - ((double) f2)) / 2.0d)))))) - Math.sqrt(Math.pow((double) ((mPPointF2.f488x + (((float) Math.cos(d2)) * f)) - ((cos + f3) / 2.0f)), 2.0d) + Math.pow((double) ((mPPointF2.f489y + (((float) Math.sin(d2)) * f)) - ((sin + f4) / 2.0f)), 2.0d)));
    }

    /* Access modifiers changed, original: protected */
    public float getSliceSpace(IPieDataSet iPieDataSet) {
        if (!iPieDataSet.isAutomaticallyDisableSliceSpacingEnabled()) {
            return iPieDataSet.getSliceSpace();
        }
        float f;
        if (iPieDataSet.getSliceSpace() / this.mViewPortHandler.getSmallestContentExtension() > (iPieDataSet.getYMin() / ((PieData) this.mChart.getData()).getYValueSum()) * 2.0f) {
            f = 0.0f;
        } else {
            f = iPieDataSet.getSliceSpace();
        }
        return f;
    }

    /* Access modifiers changed, original: protected */
    public void drawDataSet(Canvas canvas, IPieDataSet iPieDataSet) {
        float f;
        PieChartRenderer pieChartRenderer = this;
        IPieDataSet iPieDataSet2 = iPieDataSet;
        float rotationAngle = pieChartRenderer.mChart.getRotationAngle();
        float phaseX = pieChartRenderer.mAnimator.getPhaseX();
        float phaseY = pieChartRenderer.mAnimator.getPhaseY();
        RectF circleBox = pieChartRenderer.mChart.getCircleBox();
        int entryCount = iPieDataSet.getEntryCount();
        float[] drawAngles = pieChartRenderer.mChart.getDrawAngles();
        MPPointF centerCircleBox = pieChartRenderer.mChart.getCenterCircleBox();
        float radius = pieChartRenderer.mChart.getRadius();
        int i = 1;
        Object obj = (!pieChartRenderer.mChart.isDrawHoleEnabled() || pieChartRenderer.mChart.isDrawSlicesUnderHoleEnabled()) ? null : 1;
        float holeRadius = obj != null ? (pieChartRenderer.mChart.getHoleRadius() / 100.0f) * radius : 0.0f;
        int i2 = 0;
        int i3 = i2;
        while (i2 < entryCount) {
            if (Math.abs(((PieEntry) iPieDataSet2.getEntryForIndex(i2)).getY()) > Utils.FLOAT_EPSILON) {
                i3++;
            }
            i2++;
        }
        if (i3 <= 1) {
            f = 0.0f;
        } else {
            f = pieChartRenderer.getSliceSpace(iPieDataSet2);
        }
        int i4 = 0;
        float f2 = 0.0f;
        while (i4 < entryCount) {
            int i5;
            int i6;
            float f3;
            float f4;
            float f5;
            RectF rectF;
            int i7;
            float[] fArr;
            int i8;
            float f6;
            MPPointF mPPointF;
            float f7 = drawAngles[i4];
            if (Math.abs(iPieDataSet2.getEntryForIndex(i4).getY()) <= Utils.FLOAT_EPSILON || pieChartRenderer.mChart.needsHighlight(i4)) {
                i5 = i4;
                i6 = i;
                f3 = radius;
                f4 = rotationAngle;
                f5 = phaseX;
                rectF = circleBox;
                i7 = entryCount;
                fArr = drawAngles;
                i8 = i3;
                f6 = holeRadius;
                mPPointF = centerCircleBox;
            } else {
                float calculateMinimumRadiusForSpacedSlice;
                i2 = (f <= 0.0f || f7 > 180.0f) ? 0 : i;
                pieChartRenderer.mRenderPaint.setColor(iPieDataSet2.getColor(i4));
                float f8 = i3 == 1 ? 0.0f : f / (0.017453292f * radius);
                float f9 = rotationAngle + ((f2 + (f8 / 2.0f)) * phaseY);
                f8 = (f7 - f8) * phaseY;
                if (f8 < 0.0f) {
                    f8 = 0.0f;
                }
                pieChartRenderer.mPathBuffer.reset();
                int i9 = i4;
                int i10 = i3;
                double d = (double) (f9 * 0.017453292f);
                i7 = entryCount;
                fArr = drawAngles;
                float cos = centerCircleBox.f488x + (((float) Math.cos(d)) * radius);
                float sin = centerCircleBox.f489y + (((float) Math.sin(d)) * radius);
                if (f8 < 360.0f || f8 % 360.0f > Utils.FLOAT_EPSILON) {
                    f5 = phaseX;
                    pieChartRenderer.mPathBuffer.moveTo(cos, sin);
                    pieChartRenderer.mPathBuffer.arcTo(circleBox, f9, f8);
                } else {
                    f5 = phaseX;
                    pieChartRenderer.mPathBuffer.addCircle(centerCircleBox.f488x, centerCircleBox.f489y, radius, Direction.CW);
                }
                float f10 = f8;
                pieChartRenderer.mInnerRectBuffer.set(centerCircleBox.f488x - holeRadius, centerCircleBox.f489y - holeRadius, centerCircleBox.f488x + holeRadius, centerCircleBox.f489y + holeRadius);
                if (obj == null) {
                    f6 = holeRadius;
                    f3 = radius;
                    f4 = rotationAngle;
                    rectF = circleBox;
                    i5 = i9;
                    i8 = i10;
                    phaseX = f10;
                    i6 = 1;
                    mPPointF = centerCircleBox;
                    f8 = 360.0f;
                } else if (holeRadius > 0.0f || i2 != 0) {
                    int i11;
                    MPPointF mPPointF2;
                    if (i2 != 0) {
                        phaseX = f10;
                        float f11 = radius;
                        rectF = circleBox;
                        i8 = i10;
                        i5 = i9;
                        f6 = holeRadius;
                        i11 = 1;
                        f3 = radius;
                        radius = f9;
                        mPPointF2 = centerCircleBox;
                        calculateMinimumRadiusForSpacedSlice = pieChartRenderer.calculateMinimumRadiusForSpacedSlice(centerCircleBox, f11, f7 * phaseY, cos, sin, radius, phaseX);
                        if (calculateMinimumRadiusForSpacedSlice < 0.0f) {
                            calculateMinimumRadiusForSpacedSlice = -calculateMinimumRadiusForSpacedSlice;
                        }
                        holeRadius = Math.max(f6, calculateMinimumRadiusForSpacedSlice);
                    } else {
                        f6 = holeRadius;
                        f3 = radius;
                        mPPointF2 = centerCircleBox;
                        rectF = circleBox;
                        i5 = i9;
                        i8 = i10;
                        phaseX = f10;
                        i11 = 1;
                    }
                    calculateMinimumRadiusForSpacedSlice = (i8 == i11 || holeRadius == 0.0f) ? 0.0f : f / (0.017453292f * holeRadius);
                    f8 = ((f2 + (calculateMinimumRadiusForSpacedSlice / 2.0f)) * phaseY) + rotationAngle;
                    calculateMinimumRadiusForSpacedSlice = (f7 - calculateMinimumRadiusForSpacedSlice) * phaseY;
                    if (calculateMinimumRadiusForSpacedSlice < 0.0f) {
                        calculateMinimumRadiusForSpacedSlice = 0.0f;
                    }
                    f8 += calculateMinimumRadiusForSpacedSlice;
                    if (phaseX < 360.0f || phaseX % 360.0f > Utils.FLOAT_EPSILON) {
                        i6 = i11;
                        pieChartRenderer = this;
                        double d2 = (double) (f8 * 0.017453292f);
                        f4 = rotationAngle;
                        pieChartRenderer.mPathBuffer.lineTo(mPPointF2.f488x + (((float) Math.cos(d2)) * holeRadius), mPPointF2.f489y + (holeRadius * ((float) Math.sin(d2))));
                        pieChartRenderer.mPathBuffer.arcTo(pieChartRenderer.mInnerRectBuffer, f8, -calculateMinimumRadiusForSpacedSlice);
                    } else {
                        i6 = i11;
                        pieChartRenderer = this;
                        pieChartRenderer.mPathBuffer.addCircle(mPPointF2.f488x, mPPointF2.f489y, holeRadius, Direction.CCW);
                        f4 = rotationAngle;
                    }
                    mPPointF = mPPointF2;
                    pieChartRenderer.mPathBuffer.close();
                    pieChartRenderer.mBitmapCanvas.drawPath(pieChartRenderer.mPathBuffer, pieChartRenderer.mRenderPaint);
                } else {
                    f6 = holeRadius;
                    f3 = radius;
                    f4 = rotationAngle;
                    rectF = circleBox;
                    i5 = i9;
                    i8 = i10;
                    phaseX = f10;
                    f8 = 360.0f;
                    i6 = 1;
                    mPPointF = centerCircleBox;
                }
                if (phaseX % f8 > Utils.FLOAT_EPSILON) {
                    if (i2 != 0) {
                        float f12 = f9 + (phaseX / 2.0f);
                        calculateMinimumRadiusForSpacedSlice = pieChartRenderer.calculateMinimumRadiusForSpacedSlice(mPPointF, f3, f7 * phaseY, cos, sin, f9, phaseX);
                        d = (double) (f12 * 0.017453292f);
                        pieChartRenderer.mPathBuffer.lineTo(mPPointF.f488x + (((float) Math.cos(d)) * calculateMinimumRadiusForSpacedSlice), mPPointF.f489y + (calculateMinimumRadiusForSpacedSlice * ((float) Math.sin(d))));
                    } else {
                        pieChartRenderer.mPathBuffer.lineTo(mPPointF.f488x, mPPointF.f489y);
                    }
                }
                pieChartRenderer.mPathBuffer.close();
                pieChartRenderer.mBitmapCanvas.drawPath(pieChartRenderer.mPathBuffer, pieChartRenderer.mRenderPaint);
            }
            f2 += f7 * f5;
            i4 = i5 + 1;
            centerCircleBox = mPPointF;
            i3 = i8;
            holeRadius = f6;
            radius = f3;
            i = i6;
            entryCount = i7;
            drawAngles = fArr;
            phaseX = f5;
            circleBox = rectF;
            rotationAngle = f4;
            iPieDataSet2 = iPieDataSet;
        }
        MPPointF.recycleInstance(centerCircleBox);
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x01ca  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x01c1  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01e0  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x01d2  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x023b  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0277  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x024e  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x02ee  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x02d6  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x0372  */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x035a  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0182  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0188 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x01c1  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x01ca  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x01d2  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01e0  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x020f  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x023b  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x024e  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0277  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x028a A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x02d6  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x02ee  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0308 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0326 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x035a  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x0372  */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x038e  */
    public void drawValues(android.graphics.Canvas r58) {
        /*
        r57 = this;
        r9 = r57;
        r10 = r58;
        r0 = r9.mChart;
        r11 = r0.getCenterCircleBox();
        r0 = r9.mChart;
        r12 = r0.getRadius();
        r0 = r9.mChart;
        r13 = r0.getRotationAngle();
        r0 = r9.mChart;
        r14 = r0.getDrawAngles();
        r0 = r9.mChart;
        r15 = r0.getAbsoluteAngles();
        r0 = r9.mAnimator;
        r16 = r0.getPhaseX();
        r0 = r9.mAnimator;
        r17 = r0.getPhaseY();
        r0 = r9.mChart;
        r0 = r0.getHoleRadius();
        r18 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r19 = r0 / r18;
        r0 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r0 = r12 / r0;
        r1 = 1080452710; // 0x40666666 float:3.6 double:5.33814566E-315;
        r0 = r0 * r1;
        r1 = r9.mChart;
        r1 = r1.isDrawHoleEnabled();
        r20 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r1 == 0) goto L_0x0050;
    L_0x004a:
        r0 = r12 * r19;
        r0 = r12 - r0;
        r0 = r0 / r20;
    L_0x0050:
        r21 = r12 - r0;
        r0 = r9.mChart;
        r0 = r0.getData();
        r8 = r0;
        r8 = (com.github.mikephil.charting.data.PieData) r8;
        r7 = r8.getDataSets();
        r22 = r8.getYValueSum();
        r0 = r9.mChart;
        r23 = r0.isDrawEntryLabelsEnabled();
        r58.save();
        r0 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r24 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r0);
        r25 = 0;
        r0 = r25;
        r6 = r0;
    L_0x0077:
        r1 = r7.size();
        if (r6 >= r1) goto L_0x0408;
    L_0x007d:
        r1 = r7.get(r6);
        r5 = r1;
        r5 = (com.github.mikephil.charting.interfaces.datasets.IPieDataSet) r5;
        r26 = r5.isDrawValuesEnabled();
        if (r26 != 0) goto L_0x009c;
    L_0x008a:
        if (r23 != 0) goto L_0x009c;
    L_0x008c:
        r33 = r6;
        r37 = r7;
        r7 = r11;
        r50 = r12;
        r41 = r13;
        r42 = r14;
        r44 = r15;
        r11 = r8;
        goto L_0x03f8;
    L_0x009c:
        r4 = r5.getXValuePosition();
        r3 = r5.getYValuePosition();
        r9.applyValueTextStyle(r5);
        r1 = r9.mValuePaint;
        r2 = "Q";
        r1 = com.github.mikephil.charting.utils.Utils.calcTextHeight(r1, r2);
        r1 = (float) r1;
        r2 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r2 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r2);
        r27 = r1 + r2;
        r28 = r5.getValueFormatter();
        r2 = r5.getEntryCount();
        r1 = r9.mValueLinePaint;
        r29 = r0;
        r0 = r5.getValueLineColor();
        r1.setColor(r0);
        r0 = r9.mValueLinePaint;
        r1 = r5.getValueLineWidth();
        r1 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r1);
        r0.setStrokeWidth(r1);
        r30 = r9.getSliceSpace(r5);
        r0 = r5.getIconsOffset();
        r1 = com.github.mikephil.charting.utils.MPPointF.getInstance(r0);
        r0 = r1.f488x;
        r0 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r0);
        r1.f488x = r0;
        r0 = r1.f489y;
        r0 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r0);
        r1.f489y = r0;
        r0 = r25;
    L_0x00f6:
        if (r0 >= r2) goto L_0x03e4;
    L_0x00f8:
        r31 = r5.getEntryForIndex(r0);
        r32 = r8;
        r8 = r31;
        r8 = (com.github.mikephil.charting.data.PieEntry) r8;
        if (r29 != 0) goto L_0x0107;
    L_0x0104:
        r31 = 0;
        goto L_0x010d;
    L_0x0107:
        r31 = r29 + -1;
        r31 = r15[r31];
        r31 = r31 * r16;
    L_0x010d:
        r33 = r14[r29];
        r34 = 1016003125; // 0x3c8efa35 float:0.017453292 double:5.0197224E-315;
        r35 = r34 * r21;
        r35 = r30 / r35;
        r35 = r35 / r20;
        r33 = r33 - r35;
        r33 = r33 / r20;
        r31 = r31 + r33;
        r31 = r31 * r17;
        r36 = r0;
        r0 = r13 + r31;
        r37 = r1;
        r1 = r9.mChart;
        r1 = r1.isUsePercentValuesEnabled();
        if (r1 == 0) goto L_0x0139;
    L_0x012e:
        r1 = r8.getY();
        r1 = r1 / r22;
        r1 = r1 * r18;
    L_0x0136:
        r31 = r1;
        goto L_0x013e;
    L_0x0139:
        r1 = r8.getY();
        goto L_0x0136;
    L_0x013e:
        r1 = r0 * r34;
        r38 = r2;
        r1 = (double) r1;
        r40 = r6;
        r39 = r7;
        r6 = java.lang.Math.cos(r1);
        r7 = (float) r6;
        r41 = r13;
        r42 = r14;
        r13 = java.lang.Math.sin(r1);
        r13 = (float) r13;
        if (r23 == 0) goto L_0x015d;
    L_0x0157:
        r14 = com.github.mikephil.charting.data.PieDataSet.ValuePosition.OUTSIDE_SLICE;
        if (r4 != r14) goto L_0x015d;
    L_0x015b:
        r14 = 1;
        goto L_0x015f;
    L_0x015d:
        r14 = r25;
    L_0x015f:
        if (r26 == 0) goto L_0x0167;
    L_0x0161:
        r6 = com.github.mikephil.charting.data.PieDataSet.ValuePosition.OUTSIDE_SLICE;
        if (r3 != r6) goto L_0x0167;
    L_0x0165:
        r6 = 1;
        goto L_0x0169;
    L_0x0167:
        r6 = r25;
    L_0x0169:
        if (r23 == 0) goto L_0x0173;
    L_0x016b:
        r44 = r15;
        r15 = com.github.mikephil.charting.data.PieDataSet.ValuePosition.INSIDE_SLICE;
        if (r4 != r15) goto L_0x0175;
    L_0x0171:
        r15 = 1;
        goto L_0x0177;
    L_0x0173:
        r44 = r15;
    L_0x0175:
        r15 = r25;
    L_0x0177:
        if (r26 == 0) goto L_0x0182;
    L_0x0179:
        r45 = r4;
        r4 = com.github.mikephil.charting.data.PieDataSet.ValuePosition.INSIDE_SLICE;
        if (r3 != r4) goto L_0x0184;
    L_0x017f:
        r43 = 1;
        goto L_0x0186;
    L_0x0182:
        r45 = r4;
    L_0x0184:
        r43 = r25;
    L_0x0186:
        if (r14 != 0) goto L_0x01a9;
    L_0x0188:
        if (r6 == 0) goto L_0x018b;
    L_0x018a:
        goto L_0x01a9;
    L_0x018b:
        r54 = r11;
        r50 = r12;
        r53 = r13;
        r55 = r15;
        r11 = r32;
        r12 = r36;
        r51 = r37;
        r35 = r38;
        r37 = r39;
        r33 = r40;
        r38 = r45;
        r36 = r3;
        r13 = r5;
        r39 = r7;
        r15 = r8;
        goto L_0x0306;
    L_0x01a9:
        r4 = r5.getValueLinePart1Length();
        r33 = r5.getValueLinePart2Length();
        r34 = r5.getValueLinePart1OffsetPercentage();
        r34 = r34 / r18;
        r46 = r3;
        r3 = r9.mChart;
        r3 = r3.isDrawHoleEnabled();
        if (r3 == 0) goto L_0x01ca;
    L_0x01c1:
        r3 = r12 * r19;
        r35 = r12 - r3;
        r35 = r35 * r34;
        r35 = r35 + r3;
        goto L_0x01cc;
    L_0x01ca:
        r35 = r12 * r34;
    L_0x01cc:
        r3 = r5.isValueLineVariableLength();
        if (r3 == 0) goto L_0x01e0;
    L_0x01d2:
        r33 = r33 * r21;
        r1 = java.lang.Math.sin(r1);
        r1 = java.lang.Math.abs(r1);
        r1 = (float) r1;
        r33 = r33 * r1;
        goto L_0x01e2;
    L_0x01e0:
        r33 = r33 * r21;
    L_0x01e2:
        r1 = r35 * r7;
        r2 = r11.f488x;
        r1 = r1 + r2;
        r35 = r35 * r13;
        r2 = r11.f489y;
        r2 = r35 + r2;
        r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = r3 + r4;
        r3 = r3 * r21;
        r4 = r3 * r7;
        r47 = r7;
        r7 = r11.f488x;
        r7 = r7 + r4;
        r3 = r3 * r13;
        r4 = r11.f489y;
        r34 = r3 + r4;
        r3 = (double) r0;
        r48 = 4645040803167600640; // 0x4076800000000000 float:0.0 double:360.0;
        r3 = r3 % r48;
        r48 = 4636033603912859648; // 0x4056800000000000 float:0.0 double:90.0;
        r0 = (r3 > r48 ? 1 : (r3 == r48 ? 0 : -1));
        if (r0 < 0) goto L_0x0230;
    L_0x020f:
        r48 = 4643457506423603200; // 0x4070e00000000000 float:0.0 double:270.0;
        r0 = (r3 > r48 ? 1 : (r3 == r48 ? 0 : -1));
        if (r0 > 0) goto L_0x0230;
    L_0x0218:
        r0 = r7 - r33;
        r3 = r9.mValuePaint;
        r4 = android.graphics.Paint.Align.RIGHT;
        r3.setTextAlign(r4);
        if (r14 == 0) goto L_0x022a;
    L_0x0223:
        r3 = r9.mEntryLabelsPaint;
        r4 = android.graphics.Paint.Align.RIGHT;
        r3.setTextAlign(r4);
    L_0x022a:
        r3 = r0 - r24;
        r33 = r0;
        r4 = r3;
        goto L_0x0245;
    L_0x0230:
        r33 = r7 + r33;
        r0 = r9.mValuePaint;
        r3 = android.graphics.Paint.Align.LEFT;
        r0.setTextAlign(r3);
        if (r14 == 0) goto L_0x0242;
    L_0x023b:
        r0 = r9.mEntryLabelsPaint;
        r3 = android.graphics.Paint.Align.LEFT;
        r0.setTextAlign(r3);
    L_0x0242:
        r0 = r33 + r24;
        r4 = r0;
    L_0x0245:
        r0 = r5.getValueLineColor();
        r3 = 1122867; // 0x112233 float:1.573472E-39 double:5.5477E-318;
        if (r0 == r3) goto L_0x0277;
    L_0x024e:
        r3 = r9.mValueLinePaint;
        r50 = r12;
        r12 = r36;
        r0 = r10;
        r51 = r37;
        r35 = r38;
        r37 = r3;
        r36 = r46;
        r3 = r7;
        r52 = r4;
        r38 = r45;
        r4 = r34;
        r53 = r13;
        r13 = r5;
        r5 = r37;
        r0.drawLine(r1, r2, r3, r4, r5);
        r5 = r9.mValueLinePaint;
        r1 = r7;
        r2 = r34;
        r3 = r33;
        r0.drawLine(r1, r2, r3, r4, r5);
        goto L_0x0288;
    L_0x0277:
        r52 = r4;
        r50 = r12;
        r53 = r13;
        r12 = r36;
        r51 = r37;
        r35 = r38;
        r38 = r45;
        r36 = r46;
        r13 = r5;
    L_0x0288:
        if (r14 == 0) goto L_0x02c5;
    L_0x028a:
        if (r6 == 0) goto L_0x02c5;
    L_0x028c:
        r5 = 0;
        r14 = r13.getValueTextColor(r12);
        r0 = r9;
        r1 = r10;
        r2 = r28;
        r3 = r31;
        r4 = r8;
        r33 = r40;
        r6 = r52;
        r37 = r39;
        r39 = r47;
        r7 = r34;
        r54 = r11;
        r55 = r15;
        r11 = r32;
        r15 = r8;
        r8 = r14;
        r0.drawValue(r1, r2, r3, r4, r5, r6, r7, r8);
        r0 = r11.getEntryCount();
        if (r12 >= r0) goto L_0x0306;
    L_0x02b3:
        r0 = r15.getLabel();
        if (r0 == 0) goto L_0x0306;
    L_0x02b9:
        r0 = r15.getLabel();
        r1 = r34 + r27;
        r7 = r52;
        r9.drawEntryLabel(r10, r0, r7, r1);
        goto L_0x0306;
    L_0x02c5:
        r54 = r11;
        r55 = r15;
        r11 = r32;
        r37 = r39;
        r33 = r40;
        r39 = r47;
        r7 = r52;
        r15 = r8;
        if (r14 == 0) goto L_0x02ee;
    L_0x02d6:
        r0 = r11.getEntryCount();
        if (r12 >= r0) goto L_0x0306;
    L_0x02dc:
        r0 = r15.getLabel();
        if (r0 == 0) goto L_0x0306;
    L_0x02e2:
        r0 = r15.getLabel();
        r1 = r27 / r20;
        r1 = r34 + r1;
        r9.drawEntryLabel(r10, r0, r7, r1);
        goto L_0x0306;
    L_0x02ee:
        if (r6 == 0) goto L_0x0306;
    L_0x02f0:
        r5 = 0;
        r0 = r27 / r20;
        r8 = r34 + r0;
        r14 = r13.getValueTextColor(r12);
        r0 = r9;
        r1 = r10;
        r2 = r28;
        r3 = r31;
        r4 = r15;
        r6 = r7;
        r7 = r8;
        r8 = r14;
        r0.drawValue(r1, r2, r3, r4, r5, r6, r7, r8);
    L_0x0306:
        if (r55 != 0) goto L_0x030f;
    L_0x0308:
        if (r43 == 0) goto L_0x030b;
    L_0x030a:
        goto L_0x030f;
    L_0x030b:
        r56 = r54;
        goto L_0x0388;
    L_0x030f:
        r7 = r21 * r39;
        r14 = r54;
        r0 = r14.f488x;
        r8 = r7 + r0;
        r0 = r21 * r53;
        r1 = r14.f489y;
        r32 = r0 + r1;
        r0 = r9.mValuePaint;
        r1 = android.graphics.Paint.Align.CENTER;
        r0.setTextAlign(r1);
        if (r55 == 0) goto L_0x0355;
    L_0x0326:
        if (r43 == 0) goto L_0x0355;
    L_0x0328:
        r5 = 0;
        r34 = r13.getValueTextColor(r12);
        r0 = r9;
        r1 = r10;
        r2 = r28;
        r3 = r31;
        r4 = r15;
        r6 = r8;
        r7 = r32;
        r56 = r14;
        r14 = r8;
        r8 = r34;
        r0.drawValue(r1, r2, r3, r4, r5, r6, r7, r8);
        r0 = r11.getEntryCount();
        if (r12 >= r0) goto L_0x0388;
    L_0x0345:
        r0 = r15.getLabel();
        if (r0 == 0) goto L_0x0388;
    L_0x034b:
        r0 = r15.getLabel();
        r1 = r32 + r27;
        r9.drawEntryLabel(r10, r0, r14, r1);
        goto L_0x0388;
    L_0x0355:
        r56 = r14;
        r14 = r8;
        if (r55 == 0) goto L_0x0372;
    L_0x035a:
        r0 = r11.getEntryCount();
        if (r12 >= r0) goto L_0x0388;
    L_0x0360:
        r0 = r15.getLabel();
        if (r0 == 0) goto L_0x0388;
    L_0x0366:
        r0 = r15.getLabel();
        r1 = r27 / r20;
        r1 = r32 + r1;
        r9.drawEntryLabel(r10, r0, r14, r1);
        goto L_0x0388;
    L_0x0372:
        if (r43 == 0) goto L_0x0388;
    L_0x0374:
        r5 = 0;
        r0 = r27 / r20;
        r7 = r32 + r0;
        r8 = r13.getValueTextColor(r12);
        r0 = r9;
        r1 = r10;
        r2 = r28;
        r3 = r31;
        r4 = r15;
        r6 = r14;
        r0.drawValue(r1, r2, r3, r4, r5, r6, r7, r8);
    L_0x0388:
        r0 = r15.getIcon();
        if (r0 == 0) goto L_0x03c4;
    L_0x038e:
        r0 = r13.isDrawIconsEnabled();
        if (r0 == 0) goto L_0x03c4;
    L_0x0394:
        r1 = r15.getIcon();
        r6 = r51;
        r0 = r6.f489y;
        r0 = r21 + r0;
        r0 = r0 * r39;
        r7 = r56;
        r2 = r7.f488x;
        r0 = r0 + r2;
        r2 = r6.f489y;
        r2 = r21 + r2;
        r2 = r2 * r53;
        r3 = r7.f489y;
        r2 = r2 + r3;
        r3 = r6.f488x;
        r2 = r2 + r3;
        r3 = (int) r0;
        r4 = (int) r2;
        r5 = r1.getIntrinsicWidth();
        r8 = r1.getIntrinsicHeight();
        r0 = r10;
        r2 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r8;
        com.github.mikephil.charting.utils.Utils.drawImage(r0, r1, r2, r3, r4, r5);
        goto L_0x03c8;
    L_0x03c4:
        r6 = r51;
        r7 = r56;
    L_0x03c8:
        r29 = r29 + 1;
        r0 = r12 + 1;
        r1 = r6;
        r8 = r11;
        r5 = r13;
        r6 = r33;
        r2 = r35;
        r3 = r36;
        r4 = r38;
        r13 = r41;
        r14 = r42;
        r15 = r44;
        r12 = r50;
        r11 = r7;
        r7 = r37;
        goto L_0x00f6;
    L_0x03e4:
        r33 = r6;
        r37 = r7;
        r7 = r11;
        r50 = r12;
        r41 = r13;
        r42 = r14;
        r44 = r15;
        r6 = r1;
        r11 = r8;
        com.github.mikephil.charting.utils.MPPointF.recycleInstance(r6);
        r0 = r29;
    L_0x03f8:
        r6 = r33 + 1;
        r8 = r11;
        r13 = r41;
        r14 = r42;
        r15 = r44;
        r12 = r50;
        r11 = r7;
        r7 = r37;
        goto L_0x0077;
    L_0x0408:
        r7 = r11;
        com.github.mikephil.charting.utils.MPPointF.recycleInstance(r7);
        r58.restore();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.renderer.PieChartRenderer.drawValues(android.graphics.Canvas):void");
    }

    /* Access modifiers changed, original: protected */
    public void drawEntryLabel(Canvas canvas, String str, float f, float f2) {
        canvas.drawText(str, f, f2, this.mEntryLabelsPaint);
    }

    public void drawExtras(Canvas canvas) {
        drawHole(canvas);
        canvas.drawBitmap((Bitmap) this.mDrawBitmap.get(), 0.0f, 0.0f, null);
        drawCenterText(canvas);
    }

    /* Access modifiers changed, original: protected */
    public void drawHole(Canvas canvas) {
        if (this.mChart.isDrawHoleEnabled() && this.mBitmapCanvas != null) {
            float radius = this.mChart.getRadius();
            float holeRadius = (this.mChart.getHoleRadius() / 100.0f) * radius;
            MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
            if (Color.alpha(this.mHolePaint.getColor()) > 0) {
                this.mBitmapCanvas.drawCircle(centerCircleBox.f488x, centerCircleBox.f489y, holeRadius, this.mHolePaint);
            }
            if (Color.alpha(this.mTransparentCirclePaint.getColor()) > 0 && this.mChart.getTransparentCircleRadius() > this.mChart.getHoleRadius()) {
                int alpha = this.mTransparentCirclePaint.getAlpha();
                radius *= this.mChart.getTransparentCircleRadius() / 100.0f;
                this.mTransparentCirclePaint.setAlpha((int) ((((float) alpha) * this.mAnimator.getPhaseX()) * this.mAnimator.getPhaseY()));
                this.mHoleCirclePath.reset();
                this.mHoleCirclePath.addCircle(centerCircleBox.f488x, centerCircleBox.f489y, radius, Direction.CW);
                this.mHoleCirclePath.addCircle(centerCircleBox.f488x, centerCircleBox.f489y, holeRadius, Direction.CCW);
                this.mBitmapCanvas.drawPath(this.mHoleCirclePath, this.mTransparentCirclePaint);
                this.mTransparentCirclePaint.setAlpha(alpha);
            }
            MPPointF.recycleInstance(centerCircleBox);
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawCenterText(Canvas canvas) {
        Canvas canvas2 = canvas;
        CharSequence centerText = this.mChart.getCenterText();
        if (this.mChart.isDrawCenterTextEnabled() && centerText != null) {
            float radius;
            MPPointF mPPointF;
            MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
            MPPointF centerTextOffset = this.mChart.getCenterTextOffset();
            float f = centerCircleBox.f488x + centerTextOffset.f488x;
            float f2 = centerCircleBox.f489y + centerTextOffset.f489y;
            if (!this.mChart.isDrawHoleEnabled() || this.mChart.isDrawSlicesUnderHoleEnabled()) {
                radius = this.mChart.getRadius();
            } else {
                radius = this.mChart.getRadius() * (this.mChart.getHoleRadius() / 100.0f);
            }
            RectF rectF = this.mRectBuffer[0];
            rectF.left = f - radius;
            rectF.top = f2 - radius;
            rectF.right = f + radius;
            rectF.bottom = f2 + radius;
            RectF rectF2 = this.mRectBuffer[1];
            rectF2.set(rectF);
            f = this.mChart.getCenterTextRadiusPercent() / 100.0f;
            if (((double) f) > Utils.DOUBLE_EPSILON) {
                rectF2.inset((rectF2.width() - (rectF2.width() * f)) / 2.0f, (rectF2.height() - (rectF2.height() * f)) / 2.0f);
            }
            if (centerText.equals(this.mCenterTextLastValue) && rectF2.equals(this.mCenterTextLastBounds)) {
                mPPointF = centerTextOffset;
            } else {
                this.mCenterTextLastBounds.set(rectF2);
                this.mCenterTextLastValue = centerText;
                mPPointF = centerTextOffset;
                StaticLayout staticLayout = r3;
                StaticLayout staticLayout2 = new StaticLayout(centerText, 0, centerText.length(), this.mCenterTextPaint, (int) Math.max(Math.ceil((double) this.mCenterTextLastBounds.width()), 1.0d), Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                this.mCenterTextLayout = staticLayout;
            }
            f = (float) this.mCenterTextLayout.getHeight();
            canvas.save();
            if (VERSION.SDK_INT >= 18) {
                Path path = this.mDrawCenterTextPathBuffer;
                path.reset();
                path.addOval(rectF, Direction.CW);
                canvas2.clipPath(path);
            }
            canvas2.translate(rectF2.left, rectF2.top + ((rectF2.height() - f) / 2.0f));
            this.mCenterTextLayout.draw(canvas2);
            canvas.restore();
            MPPointF.recycleInstance(centerCircleBox);
            MPPointF.recycleInstance(mPPointF);
        }
    }

    public void drawHighlighted(Canvas canvas, Highlight[] highlightArr) {
        Highlight[] highlightArr2 = highlightArr;
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float rotationAngle = this.mChart.getRotationAngle();
        float[] drawAngles = this.mChart.getDrawAngles();
        float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        float radius = this.mChart.getRadius();
        Object obj = (!this.mChart.isDrawHoleEnabled() || this.mChart.isDrawSlicesUnderHoleEnabled()) ? null : 1;
        float f = 0.0f;
        float holeRadius = obj != null ? (this.mChart.getHoleRadius() / 100.0f) * radius : 0.0f;
        RectF rectF = this.mDrawHighlightedRectF;
        rectF.set(0.0f, 0.0f, 0.0f, 0.0f);
        int i = 0;
        while (i < highlightArr2.length) {
            float f2;
            float f3;
            float[] fArr;
            float[] fArr2;
            int i2;
            RectF rectF2;
            float f4;
            int x = (int) highlightArr2[i].getX();
            if (x < drawAngles.length) {
                IPieDataSet dataSetByIndex = ((PieData) this.mChart.getData()).getDataSetByIndex(highlightArr2[i].getDataSetIndex());
                if (dataSetByIndex != null && dataSetByIndex.isHighlightEnabled()) {
                    float f5;
                    float f6;
                    float f7;
                    double d;
                    float f8;
                    int i3;
                    int i4;
                    int entryCount = dataSetByIndex.getEntryCount();
                    int i5 = 0;
                    int i6 = i5;
                    while (i5 < entryCount) {
                        int i7 = entryCount;
                        if (Math.abs(((PieEntry) dataSetByIndex.getEntryForIndex(i5)).getY()) > Utils.FLOAT_EPSILON) {
                            i6++;
                        }
                        i5++;
                        entryCount = i7;
                    }
                    if (x == 0) {
                        entryCount = 1;
                        f5 = 0.0f;
                    } else {
                        f5 = absoluteAngles[x - 1] * phaseX;
                        entryCount = 1;
                    }
                    if (i6 <= entryCount) {
                        f6 = 0.0f;
                    } else {
                        f6 = dataSetByIndex.getSliceSpace();
                    }
                    float f9 = drawAngles[x];
                    float selectionShift = dataSetByIndex.getSelectionShift();
                    f = radius + selectionShift;
                    int i8 = i;
                    rectF.set(this.mChart.getCircleBox());
                    selectionShift = -selectionShift;
                    rectF.inset(selectionShift, selectionShift);
                    Object obj2 = (f6 <= 0.0f || f9 > 180.0f) ? null : 1;
                    this.mRenderPaint.setColor(dataSetByIndex.getColor(x));
                    float f10 = i6 == 1 ? 0.0f : f6 / (0.017453292f * radius);
                    float f11 = i6 == 1 ? 0.0f : f6 / (0.017453292f * f);
                    float f12 = rotationAngle + ((f5 + (f10 / 2.0f)) * phaseY);
                    f10 = (f9 - f10) * phaseY;
                    f2 = 0.0f;
                    float f13 = f10 < 0.0f ? 0.0f : f10;
                    f10 = ((f5 + (f11 / 2.0f)) * phaseY) + rotationAngle;
                    f11 = (f9 - f11) * phaseY;
                    if (f11 < 0.0f) {
                        f11 = 0.0f;
                    }
                    this.mPathBuffer.reset();
                    if (f13 < 360.0f || f13 % 360.0f > Utils.FLOAT_EPSILON) {
                        f7 = holeRadius;
                        f3 = phaseX;
                        double d2 = (double) (f10 * 0.017453292f);
                        fArr = drawAngles;
                        fArr2 = absoluteAngles;
                        this.mPathBuffer.moveTo(centerCircleBox.f488x + (((float) Math.cos(d2)) * f), centerCircleBox.f489y + (f * ((float) Math.sin(d2))));
                        this.mPathBuffer.arcTo(rectF, f10, f11);
                    } else {
                        this.mPathBuffer.addCircle(centerCircleBox.f488x, centerCircleBox.f489y, f, Direction.CW);
                        f7 = holeRadius;
                        f3 = phaseX;
                        fArr = drawAngles;
                        fArr2 = absoluteAngles;
                    }
                    if (obj2 != null) {
                        d = (double) (f12 * 0.017453292f);
                        i2 = i8;
                        f8 = f7;
                        rectF2 = rectF;
                        i3 = 1;
                        i4 = i6;
                        f = calculateMinimumRadiusForSpacedSlice(centerCircleBox, radius, f9 * phaseY, (((float) Math.cos(d)) * radius) + centerCircleBox.f488x, centerCircleBox.f489y + (((float) Math.sin(d)) * radius), f12, f13);
                    } else {
                        rectF2 = rectF;
                        i4 = i6;
                        i2 = i8;
                        f8 = f7;
                        i3 = 1;
                        f = 0.0f;
                    }
                    this.mInnerRectBuffer.set(centerCircleBox.f488x - f8, centerCircleBox.f489y - f8, centerCircleBox.f488x + f8, centerCircleBox.f489y + f8);
                    if (obj == null || (f8 <= 0.0f && obj2 == null)) {
                        f4 = f8;
                        if (f13 % 360.0f > Utils.FLOAT_EPSILON) {
                            if (obj2 != null) {
                                d = (double) ((f12 + (f13 / 2.0f)) * 0.017453292f);
                                this.mPathBuffer.lineTo(centerCircleBox.f488x + (((float) Math.cos(d)) * f), centerCircleBox.f489y + (f * ((float) Math.sin(d))));
                            } else {
                                this.mPathBuffer.lineTo(centerCircleBox.f488x, centerCircleBox.f489y);
                            }
                        }
                    } else {
                        if (obj2 != null) {
                            if (f < 0.0f) {
                                f = -f;
                            }
                            holeRadius = Math.max(f8, f);
                        } else {
                            holeRadius = f8;
                        }
                        f = (i4 == i3 || holeRadius == 0.0f) ? 0.0f : f6 / (0.017453292f * holeRadius);
                        f5 = rotationAngle + ((f5 + (f / 2.0f)) * phaseY);
                        f = (f9 - f) * phaseY;
                        if (f < 0.0f) {
                            f = 0.0f;
                        }
                        f10 = f5 + f;
                        if (f13 < 360.0f || f13 % 360.0f > Utils.FLOAT_EPSILON) {
                            double d3 = (double) (f10 * 0.017453292f);
                            f4 = f8;
                            this.mPathBuffer.lineTo(centerCircleBox.f488x + (((float) Math.cos(d3)) * holeRadius), centerCircleBox.f489y + (holeRadius * ((float) Math.sin(d3))));
                            this.mPathBuffer.arcTo(this.mInnerRectBuffer, f10, -f);
                        } else {
                            this.mPathBuffer.addCircle(centerCircleBox.f488x, centerCircleBox.f489y, holeRadius, Direction.CCW);
                            f4 = f8;
                        }
                    }
                    this.mPathBuffer.close();
                    this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
                    i = i2 + 1;
                    f = f2;
                    phaseX = f3;
                    drawAngles = fArr;
                    absoluteAngles = fArr2;
                    rectF = rectF2;
                    holeRadius = f4;
                    highlightArr2 = highlightArr;
                }
            }
            i2 = i;
            rectF2 = rectF;
            f4 = holeRadius;
            f2 = f;
            f3 = phaseX;
            fArr = drawAngles;
            fArr2 = absoluteAngles;
            i = i2 + 1;
            f = f2;
            phaseX = f3;
            drawAngles = fArr;
            absoluteAngles = fArr2;
            rectF = rectF2;
            holeRadius = f4;
            highlightArr2 = highlightArr;
        }
        MPPointF.recycleInstance(centerCircleBox);
    }

    /* Access modifiers changed, original: protected */
    public void drawRoundedSlices(Canvas canvas) {
        if (this.mChart.isDrawRoundedSlicesEnabled()) {
            IPieDataSet dataSet = ((PieData) this.mChart.getData()).getDataSet();
            if (dataSet.isVisible()) {
                float phaseX = this.mAnimator.getPhaseX();
                float phaseY = this.mAnimator.getPhaseY();
                MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
                float radius = this.mChart.getRadius();
                float holeRadius = (radius - ((this.mChart.getHoleRadius() * radius) / 100.0f)) / 2.0f;
                float[] drawAngles = this.mChart.getDrawAngles();
                float rotationAngle = this.mChart.getRotationAngle();
                int i = 0;
                while (i < dataSet.getEntryCount()) {
                    float[] fArr;
                    float f;
                    float f2;
                    float f3;
                    float f4 = drawAngles[i];
                    if (Math.abs(dataSet.getEntryForIndex(i).getY()) > Utils.FLOAT_EPSILON) {
                        double d = (double) (radius - holeRadius);
                        double d2 = (double) ((rotationAngle + f4) * phaseY);
                        fArr = drawAngles;
                        f = rotationAngle;
                        f2 = phaseY;
                        f3 = phaseX;
                        phaseX = (float) ((Math.cos(Math.toRadians(d2)) * d) + ((double) centerCircleBox.f488x));
                        phaseY = (float) ((d * Math.sin(Math.toRadians(d2))) + ((double) centerCircleBox.f489y));
                        this.mRenderPaint.setColor(dataSet.getColor(i));
                        this.mBitmapCanvas.drawCircle(phaseX, phaseY, holeRadius, this.mRenderPaint);
                    } else {
                        f3 = phaseX;
                        f2 = phaseY;
                        fArr = drawAngles;
                        f = rotationAngle;
                    }
                    rotationAngle = f + (f4 * f3);
                    i++;
                    drawAngles = fArr;
                    phaseY = f2;
                    phaseX = f3;
                }
                MPPointF.recycleInstance(centerCircleBox);
            }
        }
    }

    public void releaseBitmap() {
        if (this.mBitmapCanvas != null) {
            this.mBitmapCanvas.setBitmap(null);
            this.mBitmapCanvas = null;
        }
        if (this.mDrawBitmap != null) {
            ((Bitmap) this.mDrawBitmap.get()).recycle();
            this.mDrawBitmap.clear();
            this.mDrawBitmap = null;
        }
    }
}
