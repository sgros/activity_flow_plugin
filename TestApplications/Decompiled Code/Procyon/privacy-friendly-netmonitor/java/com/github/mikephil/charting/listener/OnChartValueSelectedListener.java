// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.listener;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;

public interface OnChartValueSelectedListener
{
    void onNothingSelected();
    
    void onValueSelected(final Entry p0, final Highlight p1);
}
