// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.renderer.ScatterChartRenderer;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.data.ScatterData;

public class ScatterChart extends BarLineChartBase<ScatterData> implements ScatterDataProvider
{
    public ScatterChart(final Context context) {
        super(context);
    }
    
    public ScatterChart(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public ScatterChart(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    public ScatterData getScatterData() {
        return (ScatterData)this.mData;
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new ScatterChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.getXAxis().setSpaceMin(0.5f);
        this.getXAxis().setSpaceMax(0.5f);
    }
    
    public enum ScatterShape
    {
        CHEVRON_DOWN("CHEVRON_DOWN"), 
        CHEVRON_UP("CHEVRON_UP"), 
        CIRCLE("CIRCLE"), 
        CROSS("CROSS"), 
        SQUARE("SQUARE"), 
        TRIANGLE("TRIANGLE"), 
        X("X");
        
        private final String shapeIdentifier;
        
        private ScatterShape(final String shapeIdentifier) {
            this.shapeIdentifier = shapeIdentifier;
        }
        
        public static ScatterShape[] getAllDefaultShapes() {
            return new ScatterShape[] { ScatterShape.SQUARE, ScatterShape.CIRCLE, ScatterShape.TRIANGLE, ScatterShape.CROSS, ScatterShape.X, ScatterShape.CHEVRON_UP, ScatterShape.CHEVRON_DOWN };
        }
        
        @Override
        public String toString() {
            return this.shapeIdentifier;
        }
    }
}
