package com.google.android.exoplayer2.ext.ffmpeg;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;

public final class FfmpegLibrary {
    private static native String ffmpegGetVersion();

    private static native boolean ffmpegHasDecoder(String str);

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.ffmpeg");
    }

    private FfmpegLibrary() {
    }

    public static String getVersion() {
        return ffmpegGetVersion();
    }

    public static boolean supportsFormat(String str, int i) {
        str = getCodecName(str, i);
        return str != null && ffmpegHasDecoder(str);
    }

    static java.lang.String getCodecName(java.lang.String r1, int r2) {
        /*
        r0 = r1.hashCode();
        switch(r0) {
            case -2123537834: goto L_0x00b9;
            case -1606874997: goto L_0x00ae;
            case -1095064472: goto L_0x00a3;
            case -1003765268: goto L_0x0098;
            case -432837260: goto L_0x008e;
            case -432837259: goto L_0x0084;
            case -53558318: goto L_0x007a;
            case 187078296: goto L_0x0070;
            case 187094639: goto L_0x0065;
            case 1503095341: goto L_0x005a;
            case 1504470054: goto L_0x004e;
            case 1504578661: goto L_0x0043;
            case 1504619009: goto L_0x0037;
            case 1504831518: goto L_0x002c;
            case 1504891608: goto L_0x0020;
            case 1505942594: goto L_0x0014;
            case 1556697186: goto L_0x0009;
            default: goto L_0x0007;
        };
    L_0x0007:
        goto L_0x00c3;
    L_0x0009:
        r0 = "audio/true-hd";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x0011:
        r1 = 7;
        goto L_0x00c4;
    L_0x0014:
        r0 = "audio/vnd.dts.hd";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x001c:
        r1 = 9;
        goto L_0x00c4;
    L_0x0020:
        r0 = "audio/opus";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x0028:
        r1 = 11;
        goto L_0x00c4;
    L_0x002c:
        r0 = "audio/mpeg";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x0034:
        r1 = 1;
        goto L_0x00c4;
    L_0x0037:
        r0 = "audio/flac";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x003f:
        r1 = 14;
        goto L_0x00c4;
    L_0x0043:
        r0 = "audio/eac3";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x004b:
        r1 = 5;
        goto L_0x00c4;
    L_0x004e:
        r0 = "audio/alac";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x0056:
        r1 = 15;
        goto L_0x00c4;
    L_0x005a:
        r0 = "audio/3gpp";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x0062:
        r1 = 12;
        goto L_0x00c4;
    L_0x0065:
        r0 = "audio/raw";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x006d:
        r1 = 16;
        goto L_0x00c4;
    L_0x0070:
        r0 = "audio/ac3";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x0078:
        r1 = 4;
        goto L_0x00c4;
    L_0x007a:
        r0 = "audio/mp4a-latm";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x0082:
        r1 = 0;
        goto L_0x00c4;
    L_0x0084:
        r0 = "audio/mpeg-L2";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x008c:
        r1 = 3;
        goto L_0x00c4;
    L_0x008e:
        r0 = "audio/mpeg-L1";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x0096:
        r1 = 2;
        goto L_0x00c4;
    L_0x0098:
        r0 = "audio/vorbis";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x00a0:
        r1 = 10;
        goto L_0x00c4;
    L_0x00a3:
        r0 = "audio/vnd.dts";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x00ab:
        r1 = 8;
        goto L_0x00c4;
    L_0x00ae:
        r0 = "audio/amr-wb";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x00b6:
        r1 = 13;
        goto L_0x00c4;
    L_0x00b9:
        r0 = "audio/eac3-joc";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x00c1:
        r1 = 6;
        goto L_0x00c4;
    L_0x00c3:
        r1 = -1;
    L_0x00c4:
        r0 = 0;
        switch(r1) {
            case 0: goto L_0x00f9;
            case 1: goto L_0x00f6;
            case 2: goto L_0x00f6;
            case 3: goto L_0x00f6;
            case 4: goto L_0x00f3;
            case 5: goto L_0x00f0;
            case 6: goto L_0x00f0;
            case 7: goto L_0x00ed;
            case 8: goto L_0x00ea;
            case 9: goto L_0x00ea;
            case 10: goto L_0x00e7;
            case 11: goto L_0x00e4;
            case 12: goto L_0x00e1;
            case 13: goto L_0x00de;
            case 14: goto L_0x00db;
            case 15: goto L_0x00d8;
            case 16: goto L_0x00c9;
            default: goto L_0x00c8;
        };
    L_0x00c8:
        return r0;
    L_0x00c9:
        r1 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        if (r2 != r1) goto L_0x00d0;
    L_0x00cd:
        r1 = "pcm_mulaw";
        return r1;
    L_0x00d0:
        r1 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        if (r2 != r1) goto L_0x00d7;
    L_0x00d4:
        r1 = "pcm_alaw";
        return r1;
    L_0x00d7:
        return r0;
    L_0x00d8:
        r1 = "alac";
        return r1;
    L_0x00db:
        r1 = "flac";
        return r1;
    L_0x00de:
        r1 = "amrwb";
        return r1;
    L_0x00e1:
        r1 = "amrnb";
        return r1;
    L_0x00e4:
        r1 = "opus";
        return r1;
    L_0x00e7:
        r1 = "vorbis";
        return r1;
    L_0x00ea:
        r1 = "dca";
        return r1;
    L_0x00ed:
        r1 = "truehd";
        return r1;
    L_0x00f0:
        r1 = "eac3";
        return r1;
    L_0x00f3:
        r1 = "ac3";
        return r1;
    L_0x00f6:
        r1 = "mp3";
        return r1;
    L_0x00f9:
        r1 = "aac";
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.ext.ffmpeg.FfmpegLibrary.getCodecName(java.lang.String, int):java.lang.String");
    }
}
