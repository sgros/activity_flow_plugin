// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.Util;

final class FixedSampleSizeRechunker
{
    public static Results rechunk(final int n, final long[] array, final int[] array2, final long n2) {
        final int a = 8192 / n;
        final int length = array2.length;
        final int n3 = 0;
        int i = 0;
        int n4 = 0;
        while (i < length) {
            n4 += Util.ceilDivide(array2[i], a);
            ++i;
        }
        final long[] array3 = new long[n4];
        final int[] array4 = new int[n4];
        final long[] array5 = new long[n4];
        final int[] array6 = new int[n4];
        int n5 = 0;
        int n6 = 0;
        int max = 0;
        for (int j = n3; j < array2.length; ++j) {
            int k = array2[j];
            long n7 = array[j];
            while (k > 0) {
                final int min = Math.min(a, k);
                array3[n6] = n7;
                array4[n6] = n * min;
                max = Math.max(max, array4[n6]);
                array5[n6] = n5 * n2;
                array6[n6] = 1;
                n7 += array4[n6];
                n5 += min;
                k -= min;
                ++n6;
            }
        }
        return new Results(array3, array4, max, array5, array6, n2 * n5);
    }
    
    public static final class Results
    {
        public final long duration;
        public final int[] flags;
        public final int maximumSize;
        public final long[] offsets;
        public final int[] sizes;
        public final long[] timestamps;
        
        private Results(final long[] offsets, final int[] sizes, final int maximumSize, final long[] timestamps, final int[] flags, final long duration) {
            this.offsets = offsets;
            this.sizes = sizes;
            this.maximumSize = maximumSize;
            this.timestamps = timestamps;
            this.flags = flags;
            this.duration = duration;
        }
    }
}
