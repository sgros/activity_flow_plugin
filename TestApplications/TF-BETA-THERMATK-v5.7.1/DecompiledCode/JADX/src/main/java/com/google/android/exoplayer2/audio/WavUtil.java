package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Util;

public final class WavUtil {
    public static final int DATA_FOURCC = Util.getIntegerCodeForString("data");
    public static final int FMT_FOURCC = Util.getIntegerCodeForString("fmt ");
    public static final int RIFF_FOURCC = Util.getIntegerCodeForString("RIFF");
    public static final int WAVE_FOURCC = Util.getIntegerCodeForString("WAVE");

    public static int getEncodingForType(int i, int i2) {
        if (i != 1) {
            int i3 = 0;
            if (i == 3) {
                if (i2 == 32) {
                    i3 = 4;
                }
                return i3;
            } else if (i != 65534) {
                if (i != 6) {
                    return i != 7 ? 0 : 268435456;
                } else {
                    return 536870912;
                }
            }
        }
        return Util.getPcmEncoding(i2);
    }
}
