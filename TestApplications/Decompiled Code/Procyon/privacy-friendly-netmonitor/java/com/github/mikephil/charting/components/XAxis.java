// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.Utils;

public class XAxis extends AxisBase
{
    private boolean mAvoidFirstLastClipping;
    public int mLabelHeight;
    public int mLabelRotatedHeight;
    public int mLabelRotatedWidth;
    protected float mLabelRotationAngle;
    public int mLabelWidth;
    private XAxisPosition mPosition;
    
    public XAxis() {
        this.mLabelWidth = 1;
        this.mLabelHeight = 1;
        this.mLabelRotatedWidth = 1;
        this.mLabelRotatedHeight = 1;
        this.mLabelRotationAngle = 0.0f;
        this.mAvoidFirstLastClipping = false;
        this.mPosition = XAxisPosition.TOP;
        this.mYOffset = Utils.convertDpToPixel(4.0f);
    }
    
    public float getLabelRotationAngle() {
        return this.mLabelRotationAngle;
    }
    
    public XAxisPosition getPosition() {
        return this.mPosition;
    }
    
    public boolean isAvoidFirstLastClippingEnabled() {
        return this.mAvoidFirstLastClipping;
    }
    
    public void setAvoidFirstLastClipping(final boolean mAvoidFirstLastClipping) {
        this.mAvoidFirstLastClipping = mAvoidFirstLastClipping;
    }
    
    public void setLabelRotationAngle(final float mLabelRotationAngle) {
        this.mLabelRotationAngle = mLabelRotationAngle;
    }
    
    public void setPosition(final XAxisPosition mPosition) {
        this.mPosition = mPosition;
    }
    
    public enum XAxisPosition
    {
        BOTH_SIDED, 
        BOTTOM, 
        BOTTOM_INSIDE, 
        TOP, 
        TOP_INSIDE;
    }
}
