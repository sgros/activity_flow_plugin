// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.Format;

public final class TrackSelectionUtil
{
    public static int[] getFormatBitrates(final Format[] array, final int[] array2) {
        final int length = array.length;
        int[] array3 = array2;
        if (array2 == null) {
            array3 = new int[length];
        }
        for (int i = 0; i < length; ++i) {
            array3[i] = array[i].bitrate;
        }
        return array3;
    }
}
