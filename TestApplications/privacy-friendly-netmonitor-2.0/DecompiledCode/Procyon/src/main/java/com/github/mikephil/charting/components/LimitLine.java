// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.Utils;
import android.graphics.Color;
import android.graphics.Paint$Style;
import android.graphics.DashPathEffect;

public class LimitLine extends ComponentBase
{
    private DashPathEffect mDashPathEffect;
    private String mLabel;
    private LimitLabelPosition mLabelPosition;
    private float mLimit;
    private int mLineColor;
    private float mLineWidth;
    private Paint$Style mTextStyle;
    
    public LimitLine(final float mLimit) {
        this.mLimit = 0.0f;
        this.mLineWidth = 2.0f;
        this.mLineColor = Color.rgb(237, 91, 91);
        this.mTextStyle = Paint$Style.FILL_AND_STROKE;
        this.mLabel = "";
        this.mDashPathEffect = null;
        this.mLabelPosition = LimitLabelPosition.RIGHT_TOP;
        this.mLimit = mLimit;
    }
    
    public LimitLine(final float mLimit, final String mLabel) {
        this.mLimit = 0.0f;
        this.mLineWidth = 2.0f;
        this.mLineColor = Color.rgb(237, 91, 91);
        this.mTextStyle = Paint$Style.FILL_AND_STROKE;
        this.mLabel = "";
        this.mDashPathEffect = null;
        this.mLabelPosition = LimitLabelPosition.RIGHT_TOP;
        this.mLimit = mLimit;
        this.mLabel = mLabel;
    }
    
    public void disableDashedLine() {
        this.mDashPathEffect = null;
    }
    
    public void enableDashedLine(final float n, final float n2, final float n3) {
        this.mDashPathEffect = new DashPathEffect(new float[] { n, n2 }, n3);
    }
    
    public DashPathEffect getDashPathEffect() {
        return this.mDashPathEffect;
    }
    
    public String getLabel() {
        return this.mLabel;
    }
    
    public LimitLabelPosition getLabelPosition() {
        return this.mLabelPosition;
    }
    
    public float getLimit() {
        return this.mLimit;
    }
    
    public int getLineColor() {
        return this.mLineColor;
    }
    
    public float getLineWidth() {
        return this.mLineWidth;
    }
    
    public Paint$Style getTextStyle() {
        return this.mTextStyle;
    }
    
    public boolean isDashedLineEnabled() {
        return this.mDashPathEffect != null;
    }
    
    public void setLabel(final String mLabel) {
        this.mLabel = mLabel;
    }
    
    public void setLabelPosition(final LimitLabelPosition mLabelPosition) {
        this.mLabelPosition = mLabelPosition;
    }
    
    public void setLineColor(final int mLineColor) {
        this.mLineColor = mLineColor;
    }
    
    public void setLineWidth(float n) {
        float n2 = n;
        if (n < 0.2f) {
            n2 = 0.2f;
        }
        n = n2;
        if (n2 > 12.0f) {
            n = 12.0f;
        }
        this.mLineWidth = Utils.convertDpToPixel(n);
    }
    
    public void setTextStyle(final Paint$Style mTextStyle) {
        this.mTextStyle = mTextStyle;
    }
    
    public enum LimitLabelPosition
    {
        LEFT_BOTTOM, 
        LEFT_TOP, 
        RIGHT_BOTTOM, 
        RIGHT_TOP;
    }
}
