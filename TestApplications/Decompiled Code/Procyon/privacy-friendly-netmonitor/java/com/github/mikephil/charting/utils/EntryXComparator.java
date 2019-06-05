// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.data.Entry;
import java.util.Comparator;

public class EntryXComparator implements Comparator<Entry>
{
    @Override
    public int compare(final Entry entry, final Entry entry2) {
        final float n = entry.getX() - entry2.getX();
        if (n == 0.0f) {
            return 0;
        }
        if (n > 0.0f) {
            return 1;
        }
        return -1;
    }
}
