// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.listener;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.DataSet;

public interface OnDrawListener
{
    void onDrawFinished(final DataSet<?> p0);
    
    void onEntryAdded(final Entry p0);
    
    void onEntryMoved(final Entry p0);
}
