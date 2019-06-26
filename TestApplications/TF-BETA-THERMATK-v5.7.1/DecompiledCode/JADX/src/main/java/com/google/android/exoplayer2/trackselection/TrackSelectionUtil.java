package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.Format;

public final class TrackSelectionUtil {
    public static int[] getFormatBitrates(Format[] formatArr, int[] iArr) {
        int length = formatArr.length;
        if (iArr == null) {
            iArr = new int[length];
        }
        for (int i = 0; i < length; i++) {
            iArr[i] = formatArr[i].bitrate;
        }
        return iArr;
    }
}
