// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.utils;

public class TransformerHorizontalBarChart extends Transformer
{
    public TransformerHorizontalBarChart(final ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
    }
    
    @Override
    public void prepareMatrixOffset(final boolean b) {
        this.mMatrixOffset.reset();
        if (!b) {
            this.mMatrixOffset.postTranslate(this.mViewPortHandler.offsetLeft(), this.mViewPortHandler.getChartHeight() - this.mViewPortHandler.offsetBottom());
        }
        else {
            this.mMatrixOffset.setTranslate(-(this.mViewPortHandler.getChartWidth() - this.mViewPortHandler.offsetRight()), this.mViewPortHandler.getChartHeight() - this.mViewPortHandler.offsetBottom());
            this.mMatrixOffset.postScale(-1.0f, 1.0f);
        }
    }
}
