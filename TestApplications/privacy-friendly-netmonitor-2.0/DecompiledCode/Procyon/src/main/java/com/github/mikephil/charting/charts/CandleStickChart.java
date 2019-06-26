// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.renderer.CandleStickChartRenderer;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.data.CandleData;

public class CandleStickChart extends BarLineChartBase<CandleData> implements CandleDataProvider
{
    public CandleStickChart(final Context context) {
        super(context);
    }
    
    public CandleStickChart(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public CandleStickChart(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    public CandleData getCandleData() {
        return (CandleData)this.mData;
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new CandleStickChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.getXAxis().setSpaceMin(0.5f);
        this.getXAxis().setSpaceMax(0.5f);
    }
}
