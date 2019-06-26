// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.mediacodec;

import java.util.List;
import com.google.android.exoplayer2.video.ColorInfo;
import java.nio.ByteBuffer;
import android.media.MediaFormat;
import android.annotation.TargetApi;

@TargetApi(16)
public final class MediaFormatUtil
{
    public static void maybeSetByteBuffer(final MediaFormat mediaFormat, final String s, final byte[] array) {
        if (array != null) {
            mediaFormat.setByteBuffer(s, ByteBuffer.wrap(array));
        }
    }
    
    public static void maybeSetColorInfo(final MediaFormat mediaFormat, final ColorInfo colorInfo) {
        if (colorInfo != null) {
            maybeSetInteger(mediaFormat, "color-transfer", colorInfo.colorTransfer);
            maybeSetInteger(mediaFormat, "color-standard", colorInfo.colorSpace);
            maybeSetInteger(mediaFormat, "color-range", colorInfo.colorRange);
            maybeSetByteBuffer(mediaFormat, "hdr-static-info", colorInfo.hdrStaticInfo);
        }
    }
    
    public static void maybeSetFloat(final MediaFormat mediaFormat, final String s, final float n) {
        if (n != -1.0f) {
            mediaFormat.setFloat(s, n);
        }
    }
    
    public static void maybeSetInteger(final MediaFormat mediaFormat, final String s, final int n) {
        if (n != -1) {
            mediaFormat.setInteger(s, n);
        }
    }
    
    public static void setCsdBuffers(final MediaFormat mediaFormat, final List<byte[]> list) {
        for (int i = 0; i < list.size(); ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append("csd-");
            sb.append(i);
            mediaFormat.setByteBuffer(sb.toString(), ByteBuffer.wrap(list.get(i)));
        }
    }
}
