// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Util;

public final class WavUtil
{
    public static final int DATA_FOURCC;
    public static final int FMT_FOURCC;
    public static final int RIFF_FOURCC;
    public static final int WAVE_FOURCC;
    
    static {
        RIFF_FOURCC = Util.getIntegerCodeForString("RIFF");
        WAVE_FOURCC = Util.getIntegerCodeForString("WAVE");
        FMT_FOURCC = Util.getIntegerCodeForString("fmt ");
        DATA_FOURCC = Util.getIntegerCodeForString("data");
    }
    
    public static int getEncodingForType(int n, final int n2) {
        if (n != 1) {
            final int n3 = 0;
            if (n == 3) {
                n = n3;
                if (n2 == 32) {
                    n = 4;
                }
                return n;
            }
            if (n != 65534) {
                if (n == 6) {
                    return 536870912;
                }
                if (n != 7) {
                    return 0;
                }
                return 268435456;
            }
        }
        return Util.getPcmEncoding(n2);
    }
}
