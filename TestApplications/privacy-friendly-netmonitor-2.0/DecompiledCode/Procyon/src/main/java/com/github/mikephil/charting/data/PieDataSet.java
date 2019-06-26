// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

public class PieDataSet extends DataSet<PieEntry> implements IPieDataSet
{
    private boolean mAutomaticallyDisableSliceSpacing;
    private float mShift;
    private float mSliceSpace;
    private int mValueLineColor;
    private float mValueLinePart1Length;
    private float mValueLinePart1OffsetPercentage;
    private float mValueLinePart2Length;
    private boolean mValueLineVariableLength;
    private float mValueLineWidth;
    private ValuePosition mXValuePosition;
    private ValuePosition mYValuePosition;
    
    public PieDataSet(final List<PieEntry> list, final String s) {
        super(list, s);
        this.mSliceSpace = 0.0f;
        this.mShift = 18.0f;
        this.mXValuePosition = ValuePosition.INSIDE_SLICE;
        this.mYValuePosition = ValuePosition.INSIDE_SLICE;
        this.mValueLineColor = -16777216;
        this.mValueLineWidth = 1.0f;
        this.mValueLinePart1OffsetPercentage = 75.0f;
        this.mValueLinePart1Length = 0.3f;
        this.mValueLinePart2Length = 0.4f;
        this.mValueLineVariableLength = true;
    }
    
    @Override
    protected void calcMinMax(final PieEntry pieEntry) {
        if (pieEntry == null) {
            return;
        }
        this.calcMinMaxY(pieEntry);
    }
    
    @Override
    public DataSet<PieEntry> copy() {
        final ArrayList<PieEntry> list = new ArrayList<PieEntry>();
        for (int i = 0; i < this.mValues.size(); ++i) {
            list.add(((PieEntry)this.mValues.get(i)).copy());
        }
        final PieDataSet set = new PieDataSet(list, this.getLabel());
        set.mColors = this.mColors;
        set.mSliceSpace = this.mSliceSpace;
        set.mShift = this.mShift;
        return set;
    }
    
    @Override
    public float getSelectionShift() {
        return this.mShift;
    }
    
    @Override
    public float getSliceSpace() {
        return this.mSliceSpace;
    }
    
    @Override
    public int getValueLineColor() {
        return this.mValueLineColor;
    }
    
    @Override
    public float getValueLinePart1Length() {
        return this.mValueLinePart1Length;
    }
    
    @Override
    public float getValueLinePart1OffsetPercentage() {
        return this.mValueLinePart1OffsetPercentage;
    }
    
    @Override
    public float getValueLinePart2Length() {
        return this.mValueLinePart2Length;
    }
    
    @Override
    public float getValueLineWidth() {
        return this.mValueLineWidth;
    }
    
    @Override
    public ValuePosition getXValuePosition() {
        return this.mXValuePosition;
    }
    
    @Override
    public ValuePosition getYValuePosition() {
        return this.mYValuePosition;
    }
    
    @Override
    public boolean isAutomaticallyDisableSliceSpacingEnabled() {
        return this.mAutomaticallyDisableSliceSpacing;
    }
    
    @Override
    public boolean isValueLineVariableLength() {
        return this.mValueLineVariableLength;
    }
    
    public void setAutomaticallyDisableSliceSpacing(final boolean mAutomaticallyDisableSliceSpacing) {
        this.mAutomaticallyDisableSliceSpacing = mAutomaticallyDisableSliceSpacing;
    }
    
    public void setSelectionShift(final float n) {
        this.mShift = Utils.convertDpToPixel(n);
    }
    
    public void setSliceSpace(float n) {
        float n2 = n;
        if (n > 20.0f) {
            n2 = 20.0f;
        }
        n = n2;
        if (n2 < 0.0f) {
            n = 0.0f;
        }
        this.mSliceSpace = Utils.convertDpToPixel(n);
    }
    
    public void setValueLineColor(final int mValueLineColor) {
        this.mValueLineColor = mValueLineColor;
    }
    
    public void setValueLinePart1Length(final float mValueLinePart1Length) {
        this.mValueLinePart1Length = mValueLinePart1Length;
    }
    
    public void setValueLinePart1OffsetPercentage(final float mValueLinePart1OffsetPercentage) {
        this.mValueLinePart1OffsetPercentage = mValueLinePart1OffsetPercentage;
    }
    
    public void setValueLinePart2Length(final float mValueLinePart2Length) {
        this.mValueLinePart2Length = mValueLinePart2Length;
    }
    
    public void setValueLineVariableLength(final boolean mValueLineVariableLength) {
        this.mValueLineVariableLength = mValueLineVariableLength;
    }
    
    public void setValueLineWidth(final float mValueLineWidth) {
        this.mValueLineWidth = mValueLineWidth;
    }
    
    public void setXValuePosition(final ValuePosition mxValuePosition) {
        this.mXValuePosition = mxValuePosition;
    }
    
    public void setYValuePosition(final ValuePosition myValuePosition) {
        this.mYValuePosition = myValuePosition;
    }
    
    public enum ValuePosition
    {
        INSIDE_SLICE, 
        OUTSIDE_SLICE;
    }
}
