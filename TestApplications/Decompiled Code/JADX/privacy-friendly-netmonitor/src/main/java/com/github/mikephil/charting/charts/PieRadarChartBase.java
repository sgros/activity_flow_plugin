package com.github.mikephil.charting.charts;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.animation.Easing.EasingOption;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public abstract class PieRadarChartBase<T extends ChartData<? extends IDataSet<? extends Entry>>> extends Chart<T> {
    protected float mMinOffset = 0.0f;
    private float mRawRotationAngle = 270.0f;
    protected boolean mRotateEnabled = true;
    private float mRotationAngle = 270.0f;

    /* renamed from: com.github.mikephil.charting.charts.PieRadarChartBase$1 */
    class C04271 implements AnimatorUpdateListener {
        C04271() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            PieRadarChartBase.this.postInvalidate();
        }
    }

    /* Access modifiers changed, original: protected */
    public void calcMinMax() {
    }

    public abstract int getIndexForAngle(float f);

    public abstract float getRadius();

    public abstract float getRequiredBaseOffset();

    public abstract float getRequiredLegendOffset();

    public float getYChartMax() {
        return 0.0f;
    }

    public float getYChartMin() {
        return 0.0f;
    }

    public PieRadarChartBase(Context context) {
        super(context);
    }

    public PieRadarChartBase(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PieRadarChartBase(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* Access modifiers changed, original: protected */
    public void init() {
        super.init();
        this.mChartTouchListener = new PieRadarChartTouchListener(this);
    }

    public int getMaxVisibleCount() {
        return this.mData.getEntryCount();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mTouchEnabled || this.mChartTouchListener == null) {
            return super.onTouchEvent(motionEvent);
        }
        return this.mChartTouchListener.onTouch(this, motionEvent);
    }

    public void computeScroll() {
        if (this.mChartTouchListener instanceof PieRadarChartTouchListener) {
            ((PieRadarChartTouchListener) this.mChartTouchListener).computeScroll();
        }
    }

    public void notifyDataSetChanged() {
        if (this.mData != null) {
            calcMinMax();
            if (this.mLegend != null) {
                this.mLegendRenderer.computeLegend(this.mData);
            }
            calculateOffsets();
        }
    }

    /* JADX WARNING: Missing block: B:14:0x007e, code skipped:
            r2 = r0;
            r0 = 0.0f;
            r3 = r0;
     */
    /* JADX WARNING: Missing block: B:15:0x0083, code skipped:
            r3 = r0;
            r0 = 0.0f;
            r2 = r0;
     */
    /* JADX WARNING: Missing block: B:45:0x0179, code skipped:
            r0 = 0.0f;
            r2 = r0;
     */
    /* JADX WARNING: Missing block: B:46:0x017b, code skipped:
            r3 = r2;
     */
    /* JADX WARNING: Missing block: B:47:0x017c, code skipped:
            r1 = r1 + getRequiredBaseOffset();
            r0 = r0 + getRequiredBaseOffset();
            r3 = r3 + getRequiredBaseOffset();
            r2 = r2 + getRequiredBaseOffset();
     */
    public void calculateOffsets() {
        /*
        r9 = this;
        r0 = r9.mLegend;
        r1 = 0;
        if (r0 == 0) goto L_0x0191;
    L_0x0005:
        r0 = r9.mLegend;
        r0 = r0.isEnabled();
        if (r0 == 0) goto L_0x0191;
    L_0x000d:
        r0 = r9.mLegend;
        r0 = r0.isDrawInsideEnabled();
        if (r0 != 0) goto L_0x0191;
    L_0x0015:
        r0 = r9.mLegend;
        r0 = r0.mNeededWidth;
        r2 = r9.mViewPortHandler;
        r2 = r2.getChartWidth();
        r3 = r9.mLegend;
        r3 = r3.getMaxSizePercent();
        r2 = r2 * r3;
        r0 = java.lang.Math.min(r0, r2);
        r2 = com.github.mikephil.charting.charts.PieRadarChartBase.C04282.f47x9c9dbef;
        r3 = r9.mLegend;
        r3 = r3.getOrientation();
        r3 = r3.ordinal();
        r2 = r2[r3];
        switch(r2) {
            case 1: goto L_0x0088;
            case 2: goto L_0x003d;
            default: goto L_0x003b;
        };
    L_0x003b:
        goto L_0x0179;
    L_0x003d:
        r0 = r9.mLegend;
        r0 = r0.getVerticalAlignment();
        r2 = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP;
        if (r0 == r2) goto L_0x0051;
    L_0x0047:
        r0 = r9.mLegend;
        r0 = r0.getVerticalAlignment();
        r2 = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM;
        if (r0 != r2) goto L_0x0179;
    L_0x0051:
        r0 = r9.getRequiredLegendOffset();
        r2 = r9.mLegend;
        r2 = r2.mNeededHeight;
        r2 = r2 + r0;
        r0 = r9.mViewPortHandler;
        r0 = r0.getChartHeight();
        r3 = r9.mLegend;
        r3 = r3.getMaxSizePercent();
        r0 = r0 * r3;
        r0 = java.lang.Math.min(r2, r0);
        r2 = com.github.mikephil.charting.charts.PieRadarChartBase.C04282.f48xc926f1ec;
        r3 = r9.mLegend;
        r3 = r3.getVerticalAlignment();
        r3 = r3.ordinal();
        r2 = r2[r3];
        switch(r2) {
            case 1: goto L_0x0083;
            case 2: goto L_0x007e;
            default: goto L_0x007c;
        };
    L_0x007c:
        goto L_0x0179;
    L_0x007e:
        r2 = r0;
        r0 = r1;
        r3 = r0;
        goto L_0x017c;
    L_0x0083:
        r3 = r0;
        r0 = r1;
        r2 = r0;
        goto L_0x017c;
    L_0x0088:
        r2 = r9.mLegend;
        r2 = r2.getHorizontalAlignment();
        r3 = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.LEFT;
        if (r2 == r3) goto L_0x00a0;
    L_0x0092:
        r2 = r9.mLegend;
        r2 = r2.getHorizontalAlignment();
        r3 = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.RIGHT;
        if (r2 != r3) goto L_0x009d;
    L_0x009c:
        goto L_0x00a0;
    L_0x009d:
        r0 = r1;
        goto L_0x0120;
    L_0x00a0:
        r2 = r9.mLegend;
        r2 = r2.getVerticalAlignment();
        r3 = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.CENTER;
        if (r2 != r3) goto L_0x00b2;
    L_0x00aa:
        r2 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r2 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r2);
        r0 = r0 + r2;
        goto L_0x0120;
    L_0x00b2:
        r2 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r2 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r2);
        r0 = r0 + r2;
        r2 = r9.mLegend;
        r2 = r2.mNeededHeight;
        r3 = r9.mLegend;
        r3 = r3.mTextHeightMax;
        r2 = r2 + r3;
        r3 = r9.getCenter();
        r4 = r9.mLegend;
        r4 = r4.getHorizontalAlignment();
        r5 = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.RIGHT;
        r6 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        if (r4 != r5) goto L_0x00da;
    L_0x00d2:
        r4 = r9.getWidth();
        r4 = (float) r4;
        r4 = r4 - r0;
        r4 = r4 + r6;
        goto L_0x00dc;
    L_0x00da:
        r4 = r0 - r6;
    L_0x00dc:
        r2 = r2 + r6;
        r5 = r9.distanceToCenter(r4, r2);
        r6 = r9.getRadius();
        r4 = r9.getAngleForPoint(r4, r2);
        r4 = r9.getPosition(r3, r6, r4);
        r6 = r4.f488x;
        r7 = r4.f489y;
        r6 = r9.distanceToCenter(r6, r7);
        r7 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r7 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r7);
        r8 = r3.f489y;
        r2 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
        if (r2 < 0) goto L_0x0111;
    L_0x0101:
        r2 = r9.getHeight();
        r2 = (float) r2;
        r2 = r2 - r0;
        r8 = r9.getWidth();
        r8 = (float) r8;
        r2 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
        if (r2 <= 0) goto L_0x0111;
    L_0x0110:
        goto L_0x011a;
    L_0x0111:
        r0 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r0 >= 0) goto L_0x0119;
    L_0x0115:
        r6 = r6 - r5;
        r7 = r7 + r6;
        r0 = r7;
        goto L_0x011a;
    L_0x0119:
        r0 = r1;
    L_0x011a:
        com.github.mikephil.charting.utils.MPPointF.recycleInstance(r3);
        com.github.mikephil.charting.utils.MPPointF.recycleInstance(r4);
    L_0x0120:
        r2 = com.github.mikephil.charting.charts.PieRadarChartBase.C04282.f46x2787f53e;
        r3 = r9.mLegend;
        r3 = r3.getHorizontalAlignment();
        r3 = r3.ordinal();
        r2 = r2[r3];
        switch(r2) {
            case 1: goto L_0x0174;
            case 2: goto L_0x0172;
            case 3: goto L_0x0132;
            default: goto L_0x0131;
        };
    L_0x0131:
        goto L_0x0179;
    L_0x0132:
        r0 = com.github.mikephil.charting.charts.PieRadarChartBase.C04282.f48xc926f1ec;
        r2 = r9.mLegend;
        r2 = r2.getVerticalAlignment();
        r2 = r2.ordinal();
        r0 = r0[r2];
        switch(r0) {
            case 1: goto L_0x015b;
            case 2: goto L_0x0144;
            default: goto L_0x0143;
        };
    L_0x0143:
        goto L_0x0179;
    L_0x0144:
        r0 = r9.mLegend;
        r0 = r0.mNeededHeight;
        r2 = r9.mViewPortHandler;
        r2 = r2.getChartHeight();
        r3 = r9.mLegend;
        r3 = r3.getMaxSizePercent();
        r2 = r2 * r3;
        r0 = java.lang.Math.min(r0, r2);
        goto L_0x007e;
    L_0x015b:
        r0 = r9.mLegend;
        r0 = r0.mNeededHeight;
        r2 = r9.mViewPortHandler;
        r2 = r2.getChartHeight();
        r3 = r9.mLegend;
        r3 = r3.getMaxSizePercent();
        r2 = r2 * r3;
        r0 = java.lang.Math.min(r0, r2);
        goto L_0x0083;
    L_0x0172:
        r2 = r1;
        goto L_0x017b;
    L_0x0174:
        r2 = r1;
        r3 = r2;
        r1 = r0;
        r0 = r3;
        goto L_0x017c;
    L_0x0179:
        r0 = r1;
        r2 = r0;
    L_0x017b:
        r3 = r2;
    L_0x017c:
        r4 = r9.getRequiredBaseOffset();
        r1 = r1 + r4;
        r4 = r9.getRequiredBaseOffset();
        r0 = r0 + r4;
        r4 = r9.getRequiredBaseOffset();
        r3 = r3 + r4;
        r4 = r9.getRequiredBaseOffset();
        r2 = r2 + r4;
        goto L_0x0194;
    L_0x0191:
        r0 = r1;
        r2 = r0;
        r3 = r2;
    L_0x0194:
        r4 = r9.mMinOffset;
        r4 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r4);
        r5 = r9 instanceof com.github.mikephil.charting.charts.RadarChart;
        if (r5 == 0) goto L_0x01b5;
    L_0x019e:
        r5 = r9.getXAxis();
        r6 = r5.isEnabled();
        if (r6 == 0) goto L_0x01b5;
    L_0x01a8:
        r6 = r5.isDrawLabelsEnabled();
        if (r6 == 0) goto L_0x01b5;
    L_0x01ae:
        r5 = r5.mLabelRotatedWidth;
        r5 = (float) r5;
        r4 = java.lang.Math.max(r4, r5);
    L_0x01b5:
        r5 = r9.getExtraTopOffset();
        r3 = r3 + r5;
        r5 = r9.getExtraRightOffset();
        r0 = r0 + r5;
        r5 = r9.getExtraBottomOffset();
        r2 = r2 + r5;
        r5 = r9.getExtraLeftOffset();
        r1 = r1 + r5;
        r1 = java.lang.Math.max(r4, r1);
        r3 = java.lang.Math.max(r4, r3);
        r0 = java.lang.Math.max(r4, r0);
        r5 = r9.getRequiredBaseOffset();
        r2 = java.lang.Math.max(r5, r2);
        r2 = java.lang.Math.max(r4, r2);
        r4 = r9.mViewPortHandler;
        r4.restrainViewPort(r1, r3, r0, r2);
        r4 = r9.mLogEnabled;
        if (r4 == 0) goto L_0x0218;
    L_0x01ea:
        r4 = "MPAndroidChart";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "offsetLeft: ";
        r5.append(r6);
        r5.append(r1);
        r1 = ", offsetTop: ";
        r5.append(r1);
        r5.append(r3);
        r1 = ", offsetRight: ";
        r5.append(r1);
        r5.append(r0);
        r0 = ", offsetBottom: ";
        r5.append(r0);
        r5.append(r2);
        r0 = r5.toString();
        android.util.Log.i(r4, r0);
    L_0x0218:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.charts.PieRadarChartBase.calculateOffsets():void");
    }

    public float getAngleForPoint(float f, float f2) {
        MPPointF centerOffsets = getCenterOffsets();
        double d = (double) (f - centerOffsets.f488x);
        double d2 = (double) (f2 - centerOffsets.f489y);
        f2 = (float) Math.toDegrees(Math.acos(d2 / Math.sqrt((d * d) + (d2 * d2))));
        if (f > centerOffsets.f488x) {
            f2 = 360.0f - f2;
        }
        f2 += 90.0f;
        if (f2 > 360.0f) {
            f2 -= 360.0f;
        }
        MPPointF.recycleInstance(centerOffsets);
        return f2;
    }

    public MPPointF getPosition(MPPointF mPPointF, float f, float f2) {
        MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        getPosition(mPPointF, f, f2, instance);
        return instance;
    }

    public void getPosition(MPPointF mPPointF, float f, float f2, MPPointF mPPointF2) {
        double d = (double) f;
        double d2 = (double) f2;
        mPPointF2.f488x = (float) (((double) mPPointF.f488x) + (Math.cos(Math.toRadians(d2)) * d));
        mPPointF2.f489y = (float) (((double) mPPointF.f489y) + (d * Math.sin(Math.toRadians(d2))));
    }

    public float distanceToCenter(float f, float f2) {
        MPPointF centerOffsets = getCenterOffsets();
        if (f > centerOffsets.f488x) {
            f -= centerOffsets.f488x;
        } else {
            f = centerOffsets.f488x - f;
        }
        if (f2 > centerOffsets.f489y) {
            f2 -= centerOffsets.f489y;
        } else {
            f2 = centerOffsets.f489y - f2;
        }
        f = (float) Math.sqrt(Math.pow((double) f, 2.0d) + Math.pow((double) f2, 2.0d));
        MPPointF.recycleInstance(centerOffsets);
        return f;
    }

    public void setRotationAngle(float f) {
        this.mRawRotationAngle = f;
        this.mRotationAngle = Utils.getNormalizedAngle(this.mRawRotationAngle);
    }

    public float getRawRotationAngle() {
        return this.mRawRotationAngle;
    }

    public float getRotationAngle() {
        return this.mRotationAngle;
    }

    public void setRotationEnabled(boolean z) {
        this.mRotateEnabled = z;
    }

    public boolean isRotationEnabled() {
        return this.mRotateEnabled;
    }

    public float getMinOffset() {
        return this.mMinOffset;
    }

    public void setMinOffset(float f) {
        this.mMinOffset = f;
    }

    public float getDiameter() {
        RectF contentRect = this.mViewPortHandler.getContentRect();
        contentRect.left += getExtraLeftOffset();
        contentRect.top += getExtraTopOffset();
        contentRect.right -= getExtraRightOffset();
        contentRect.bottom -= getExtraBottomOffset();
        return Math.min(contentRect.width(), contentRect.height());
    }

    @SuppressLint({"NewApi"})
    public void spin(int i, float f, float f2, EasingOption easingOption) {
        if (VERSION.SDK_INT >= 11) {
            setRotationAngle(f);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "rotationAngle", new float[]{f, f2});
            ofFloat.setDuration((long) i);
            ofFloat.setInterpolator(Easing.getEasingFunctionFromOption(easingOption));
            ofFloat.addUpdateListener(new C04271());
            ofFloat.start();
        }
    }
}
