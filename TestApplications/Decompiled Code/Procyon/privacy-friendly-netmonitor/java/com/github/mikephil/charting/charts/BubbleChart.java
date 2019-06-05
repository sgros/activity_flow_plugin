// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.renderer.BubbleChartRenderer;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.data.BubbleData;

public class BubbleChart extends BarLineChartBase<BubbleData> implements BubbleDataProvider
{
    public BubbleChart(final Context context) {
        super(context);
    }
    
    public BubbleChart(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public BubbleChart(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    public BubbleData getBubbleData() {
        return (BubbleData)this.mData;
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new BubbleChartRenderer(this, this.mAnimator, this.mViewPortHandler);
    }
}
