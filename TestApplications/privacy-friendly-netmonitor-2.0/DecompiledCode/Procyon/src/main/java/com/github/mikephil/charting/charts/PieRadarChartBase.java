// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import android.annotation.SuppressLint;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.os.Build$VERSION;
import com.github.mikephil.charting.animation.Easing;
import android.view.View;
import android.view.MotionEvent;
import android.graphics.RectF;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import com.github.mikephil.charting.components.XAxis;
import android.util.Log;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.components.Legend;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.data.ChartData;

public abstract class PieRadarChartBase<T extends ChartData<? extends IDataSet<? extends Entry>>> extends Chart<T>
{
    protected float mMinOffset;
    private float mRawRotationAngle;
    protected boolean mRotateEnabled;
    private float mRotationAngle;
    
    public PieRadarChartBase(final Context context) {
        super(context);
        this.mRotationAngle = 270.0f;
        this.mRawRotationAngle = 270.0f;
        this.mRotateEnabled = true;
        this.mMinOffset = 0.0f;
    }
    
    public PieRadarChartBase(final Context context, final AttributeSet set) {
        super(context, set);
        this.mRotationAngle = 270.0f;
        this.mRawRotationAngle = 270.0f;
        this.mRotateEnabled = true;
        this.mMinOffset = 0.0f;
    }
    
    public PieRadarChartBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mRotationAngle = 270.0f;
        this.mRawRotationAngle = 270.0f;
        this.mRotateEnabled = true;
        this.mMinOffset = 0.0f;
    }
    
    @Override
    protected void calcMinMax() {
    }
    
    public void calculateOffsets() {
        final Legend mLegend = this.mLegend;
        final float n = 0.0f;
        float n2 = 0.0f;
        float n10;
        float n12;
        float n14;
        float n15;
        if (mLegend != null && this.mLegend.isEnabled() && !this.mLegend.isDrawInsideEnabled()) {
            final float min = Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent());
            float n4 = 0.0f;
            float n9 = 0.0f;
            float n8 = 0.0f;
            Label_0656: {
                Label_0650: {
                    Label_0647: {
                        float min2 = 0.0f;
                        Label_0217: {
                            float n3 = 0.0f;
                            Label_0203: {
                                switch (PieRadarChartBase$2.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendOrientation[this.mLegend.getOrientation().ordinal()]) {
                                    default: {
                                        break Label_0647;
                                    }
                                    case 2: {
                                        if (this.mLegend.getVerticalAlignment() != Legend.LegendVerticalAlignment.TOP && this.mLegend.getVerticalAlignment() != Legend.LegendVerticalAlignment.BOTTOM) {
                                            break Label_0647;
                                        }
                                        min2 = (n3 = Math.min(this.mLegend.mNeededHeight + this.getRequiredLegendOffset(), this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()));
                                        switch (PieRadarChartBase$2.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment[this.mLegend.getVerticalAlignment().ordinal()]) {
                                            default: {
                                                break Label_0647;
                                            }
                                            case 2: {
                                                break Label_0203;
                                            }
                                            case 1: {
                                                break Label_0217;
                                            }
                                        }
                                        break;
                                    }
                                    case 1: {
                                        if (this.mLegend.getHorizontalAlignment() != Legend.LegendHorizontalAlignment.LEFT && this.mLegend.getHorizontalAlignment() != Legend.LegendHorizontalAlignment.RIGHT) {
                                            n4 = 0.0f;
                                        }
                                        else if (this.mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.CENTER) {
                                            n4 = min + Utils.convertDpToPixel(13.0f);
                                        }
                                        else {
                                            final float n5 = min + Utils.convertDpToPixel(8.0f);
                                            final float mNeededHeight = this.mLegend.mNeededHeight;
                                            final float mTextHeightMax = this.mLegend.mTextHeightMax;
                                            final MPPointF center = this.getCenter();
                                            float n6;
                                            if (this.mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.RIGHT) {
                                                n6 = this.getWidth() - n5 + 15.0f;
                                            }
                                            else {
                                                n6 = n5 - 15.0f;
                                            }
                                            final float n7 = mNeededHeight + mTextHeightMax + 15.0f;
                                            final float distanceToCenter = this.distanceToCenter(n6, n7);
                                            final MPPointF position = this.getPosition(center, this.getRadius(), this.getAngleForPoint(n6, n7));
                                            final float distanceToCenter2 = this.distanceToCenter(position.x, position.y);
                                            final float convertDpToPixel = Utils.convertDpToPixel(5.0f);
                                            if (n7 >= center.y && this.getHeight() - n5 > this.getWidth()) {
                                                n4 = n5;
                                            }
                                            else if (distanceToCenter < distanceToCenter2) {
                                                n4 = convertDpToPixel + (distanceToCenter2 - distanceToCenter);
                                            }
                                            else {
                                                n4 = 0.0f;
                                            }
                                            MPPointF.recycleInstance(center);
                                            MPPointF.recycleInstance(position);
                                        }
                                        switch (PieRadarChartBase$2.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendHorizontalAlignment[this.mLegend.getHorizontalAlignment().ordinal()]) {
                                            default: {
                                                break Label_0647;
                                            }
                                            case 3: {
                                                switch (PieRadarChartBase$2.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment[this.mLegend.getVerticalAlignment().ordinal()]) {
                                                    default: {
                                                        break Label_0647;
                                                    }
                                                    case 2: {
                                                        n3 = Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                                                        break Label_0203;
                                                    }
                                                    case 1: {
                                                        min2 = Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                                                        break Label_0217;
                                                    }
                                                }
                                                break;
                                            }
                                            case 2: {
                                                break Label_0650;
                                            }
                                            case 1: {
                                                n8 = (n9 = 0.0f);
                                                n2 = n4;
                                                n4 = n9;
                                                break Label_0656;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            n8 = n3;
                            n4 = (n9 = 0.0f);
                            break Label_0656;
                        }
                        n9 = min2;
                        n4 = (n8 = 0.0f);
                        break Label_0656;
                    }
                    n4 = 0.0f;
                }
                n8 = 0.0f;
                n9 = 0.0f;
            }
            n10 = n2 + this.getRequiredBaseOffset();
            final float n11 = n4 + this.getRequiredBaseOffset();
            n12 = n9 + this.getRequiredBaseOffset();
            final float n13 = n8 + this.getRequiredBaseOffset();
            n14 = n11;
            n15 = n13;
        }
        else {
            n14 = 0.0f;
            n15 = (n12 = n14);
            n10 = n;
        }
        float n16;
        final float a = n16 = Utils.convertDpToPixel(this.mMinOffset);
        if (this instanceof RadarChart) {
            final XAxis xAxis = this.getXAxis();
            n16 = a;
            if (xAxis.isEnabled()) {
                n16 = a;
                if (xAxis.isDrawLabelsEnabled()) {
                    n16 = Math.max(a, (float)xAxis.mLabelRotatedWidth);
                }
            }
        }
        final float extraTopOffset = this.getExtraTopOffset();
        final float extraRightOffset = this.getExtraRightOffset();
        final float extraBottomOffset = this.getExtraBottomOffset();
        final float max = Math.max(n16, n10 + this.getExtraLeftOffset());
        final float max2 = Math.max(n16, n12 + extraTopOffset);
        final float max3 = Math.max(n16, n14 + extraRightOffset);
        final float max4 = Math.max(n16, Math.max(this.getRequiredBaseOffset(), n15 + extraBottomOffset));
        this.mViewPortHandler.restrainViewPort(max, max2, max3, max4);
        if (this.mLogEnabled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("offsetLeft: ");
            sb.append(max);
            sb.append(", offsetTop: ");
            sb.append(max2);
            sb.append(", offsetRight: ");
            sb.append(max3);
            sb.append(", offsetBottom: ");
            sb.append(max4);
            Log.i("MPAndroidChart", sb.toString());
        }
    }
    
    public void computeScroll() {
        if (this.mChartTouchListener instanceof PieRadarChartTouchListener) {
            ((PieRadarChartTouchListener)this.mChartTouchListener).computeScroll();
        }
    }
    
    public float distanceToCenter(float n, float n2) {
        final MPPointF centerOffsets = this.getCenterOffsets();
        if (n > centerOffsets.x) {
            n -= centerOffsets.x;
        }
        else {
            n = centerOffsets.x - n;
        }
        if (n2 > centerOffsets.y) {
            n2 -= centerOffsets.y;
        }
        else {
            n2 = centerOffsets.y - n2;
        }
        n = (float)Math.sqrt(Math.pow(n, 2.0) + Math.pow(n2, 2.0));
        MPPointF.recycleInstance(centerOffsets);
        return n;
    }
    
    public float getAngleForPoint(float n, float n2) {
        final MPPointF centerOffsets = this.getCenterOffsets();
        final double n3 = n - centerOffsets.x;
        final double n4 = n2 - centerOffsets.y;
        final float n5 = n2 = (float)Math.toDegrees(Math.acos(n4 / Math.sqrt(n3 * n3 + n4 * n4)));
        if (n > centerOffsets.x) {
            n2 = 360.0f - n5;
        }
        n2 = (n = n2 + 90.0f);
        if (n2 > 360.0f) {
            n = n2 - 360.0f;
        }
        MPPointF.recycleInstance(centerOffsets);
        return n;
    }
    
    public float getDiameter() {
        final RectF contentRect = this.mViewPortHandler.getContentRect();
        contentRect.left += this.getExtraLeftOffset();
        contentRect.top += this.getExtraTopOffset();
        contentRect.right -= this.getExtraRightOffset();
        contentRect.bottom -= this.getExtraBottomOffset();
        return Math.min(contentRect.width(), contentRect.height());
    }
    
    public abstract int getIndexForAngle(final float p0);
    
    public int getMaxVisibleCount() {
        return this.mData.getEntryCount();
    }
    
    public float getMinOffset() {
        return this.mMinOffset;
    }
    
    public MPPointF getPosition(final MPPointF mpPointF, final float n, final float n2) {
        final MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        this.getPosition(mpPointF, n, n2, instance);
        return instance;
    }
    
    public void getPosition(final MPPointF mpPointF, final float n, final float n2, final MPPointF mpPointF2) {
        final double n3 = mpPointF.x;
        final double n4 = n;
        final double n5 = n2;
        mpPointF2.x = (float)(n3 + Math.cos(Math.toRadians(n5)) * n4);
        mpPointF2.y = (float)(mpPointF.y + n4 * Math.sin(Math.toRadians(n5)));
    }
    
    public abstract float getRadius();
    
    public float getRawRotationAngle() {
        return this.mRawRotationAngle;
    }
    
    protected abstract float getRequiredBaseOffset();
    
    protected abstract float getRequiredLegendOffset();
    
    public float getRotationAngle() {
        return this.mRotationAngle;
    }
    
    public float getYChartMax() {
        return 0.0f;
    }
    
    public float getYChartMin() {
        return 0.0f;
    }
    
    @Override
    protected void init() {
        super.init();
        this.mChartTouchListener = new PieRadarChartTouchListener(this);
    }
    
    public boolean isRotationEnabled() {
        return this.mRotateEnabled;
    }
    
    @Override
    public void notifyDataSetChanged() {
        if (this.mData == null) {
            return;
        }
        this.calcMinMax();
        if (this.mLegend != null) {
            this.mLegendRenderer.computeLegend(this.mData);
        }
        this.calculateOffsets();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.mTouchEnabled && this.mChartTouchListener != null) {
            return this.mChartTouchListener.onTouch((View)this, motionEvent);
        }
        return super.onTouchEvent(motionEvent);
    }
    
    public void setMinOffset(final float mMinOffset) {
        this.mMinOffset = mMinOffset;
    }
    
    public void setRotationAngle(final float mRawRotationAngle) {
        this.mRawRotationAngle = mRawRotationAngle;
        this.mRotationAngle = Utils.getNormalizedAngle(this.mRawRotationAngle);
    }
    
    public void setRotationEnabled(final boolean mRotateEnabled) {
        this.mRotateEnabled = mRotateEnabled;
    }
    
    @SuppressLint({ "NewApi" })
    public void spin(final int n, final float rotationAngle, final float n2, final Easing.EasingOption easingOption) {
        if (Build$VERSION.SDK_INT < 11) {
            return;
        }
        this.setRotationAngle(rotationAngle);
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "rotationAngle", new float[] { rotationAngle, n2 });
        ofFloat.setDuration((long)n);
        ofFloat.setInterpolator((TimeInterpolator)Easing.getEasingFunctionFromOption(easingOption));
        ofFloat.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                PieRadarChartBase.this.postInvalidate();
            }
        });
        ofFloat.start();
    }
}
