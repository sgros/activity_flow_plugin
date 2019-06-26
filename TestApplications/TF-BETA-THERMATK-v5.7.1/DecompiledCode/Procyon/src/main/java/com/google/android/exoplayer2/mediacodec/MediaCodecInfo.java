// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.mediacodec;

import com.google.android.exoplayer2.Format;
import android.util.Pair;
import android.media.MediaCodecInfo$AudioCapabilities;
import android.media.MediaCodecInfo$CodecProfileLevel;
import android.graphics.Point;
import android.media.MediaCodecInfo$VideoCapabilities;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Assertions;
import android.media.MediaCodecInfo$CodecCapabilities;
import android.annotation.TargetApi;

@TargetApi(16)
public final class MediaCodecInfo
{
    public final boolean adaptive;
    public final MediaCodecInfo$CodecCapabilities capabilities;
    private final boolean isVideo;
    public final String mimeType;
    public final String name;
    public final boolean passthrough;
    public final boolean secure;
    public final boolean tunneling;
    
    private MediaCodecInfo(final String s, final String mimeType, final MediaCodecInfo$CodecCapabilities capabilities, final boolean passthrough, final boolean b, final boolean b2) {
        Assertions.checkNotNull(s);
        this.name = s;
        this.mimeType = mimeType;
        this.capabilities = capabilities;
        this.passthrough = passthrough;
        final boolean b3 = true;
        this.adaptive = (!b && capabilities != null && isAdaptive(capabilities));
        this.tunneling = (capabilities != null && isTunneling(capabilities));
        boolean secure = b3;
        if (!b2) {
            secure = (capabilities != null && isSecure(capabilities) && b3);
        }
        this.secure = secure;
        this.isVideo = MimeTypes.isVideo(mimeType);
    }
    
    private static int adjustMaxInputChannelCount(final String str, final String anObject, final int i) {
        if (i <= 1) {
            if (Util.SDK_INT < 26 || i <= 0) {
                if (!"audio/mpeg".equals(anObject) && !"audio/3gpp".equals(anObject) && !"audio/amr-wb".equals(anObject) && !"audio/mp4a-latm".equals(anObject) && !"audio/vorbis".equals(anObject) && !"audio/opus".equals(anObject) && !"audio/raw".equals(anObject) && !"audio/flac".equals(anObject) && !"audio/g711-alaw".equals(anObject) && !"audio/g711-mlaw".equals(anObject)) {
                    if (!"audio/gsm".equals(anObject)) {
                        int j;
                        if ("audio/ac3".equals(anObject)) {
                            j = 6;
                        }
                        else if ("audio/eac3".equals(anObject)) {
                            j = 16;
                        }
                        else {
                            j = 30;
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append("AssumedMaxChannelAdjustment: ");
                        sb.append(str);
                        sb.append(", [");
                        sb.append(i);
                        sb.append(" to ");
                        sb.append(j);
                        sb.append("]");
                        Log.w("MediaCodecInfo", sb.toString());
                        return j;
                    }
                }
            }
        }
        return i;
    }
    
    @TargetApi(21)
    private static boolean areSizeAndRateSupportedV21(final MediaCodecInfo$VideoCapabilities mediaCodecInfo$VideoCapabilities, final int n, final int n2, final double n3) {
        boolean b;
        if (n3 != -1.0 && n3 > 0.0) {
            b = mediaCodecInfo$VideoCapabilities.areSizeAndRateSupported(n, n2, n3);
        }
        else {
            b = mediaCodecInfo$VideoCapabilities.isSizeSupported(n, n2);
        }
        return b;
    }
    
    private static boolean isAdaptive(final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
        return Util.SDK_INT >= 19 && isAdaptiveV19(mediaCodecInfo$CodecCapabilities);
    }
    
    @TargetApi(19)
    private static boolean isAdaptiveV19(final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
        return mediaCodecInfo$CodecCapabilities.isFeatureSupported("adaptive-playback");
    }
    
    private static boolean isSecure(final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
        return Util.SDK_INT >= 21 && isSecureV21(mediaCodecInfo$CodecCapabilities);
    }
    
    @TargetApi(21)
    private static boolean isSecureV21(final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
        return mediaCodecInfo$CodecCapabilities.isFeatureSupported("secure-playback");
    }
    
    private static boolean isTunneling(final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
        return Util.SDK_INT >= 21 && isTunnelingV21(mediaCodecInfo$CodecCapabilities);
    }
    
    @TargetApi(21)
    private static boolean isTunnelingV21(final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
        return mediaCodecInfo$CodecCapabilities.isFeatureSupported("tunneled-playback");
    }
    
    private void logAssumedSupport(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("AssumedSupport [");
        sb.append(str);
        sb.append("] [");
        sb.append(this.name);
        sb.append(", ");
        sb.append(this.mimeType);
        sb.append("] [");
        sb.append(Util.DEVICE_DEBUG_INFO);
        sb.append("]");
        Log.d("MediaCodecInfo", sb.toString());
    }
    
    private void logNoSupport(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("NoSupport [");
        sb.append(str);
        sb.append("] [");
        sb.append(this.name);
        sb.append(", ");
        sb.append(this.mimeType);
        sb.append("] [");
        sb.append(Util.DEVICE_DEBUG_INFO);
        sb.append("]");
        Log.d("MediaCodecInfo", sb.toString());
    }
    
    public static MediaCodecInfo newInstance(final String s, final String s2, final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities, final boolean b, final boolean b2) {
        return new MediaCodecInfo(s, s2, mediaCodecInfo$CodecCapabilities, false, b, b2);
    }
    
    public static MediaCodecInfo newPassthroughInstance(final String s) {
        return new MediaCodecInfo(s, null, null, true, false, false);
    }
    
    @TargetApi(21)
    public Point alignVideoSizeV21(final int n, final int n2) {
        final MediaCodecInfo$CodecCapabilities capabilities = this.capabilities;
        if (capabilities == null) {
            this.logNoSupport("align.caps");
            return null;
        }
        final MediaCodecInfo$VideoCapabilities videoCapabilities = capabilities.getVideoCapabilities();
        if (videoCapabilities == null) {
            this.logNoSupport("align.vCaps");
            return null;
        }
        final int widthAlignment = videoCapabilities.getWidthAlignment();
        final int heightAlignment = videoCapabilities.getHeightAlignment();
        return new Point(Util.ceilDivide(n, widthAlignment) * widthAlignment, Util.ceilDivide(n2, heightAlignment) * heightAlignment);
    }
    
    public MediaCodecInfo$CodecProfileLevel[] getProfileLevels() {
        final MediaCodecInfo$CodecCapabilities capabilities = this.capabilities;
        MediaCodecInfo$CodecProfileLevel[] profileLevels;
        if (capabilities == null || (profileLevels = capabilities.profileLevels) == null) {
            profileLevels = new MediaCodecInfo$CodecProfileLevel[0];
        }
        return profileLevels;
    }
    
    @TargetApi(21)
    public boolean isAudioChannelCountSupportedV21(final int i) {
        final MediaCodecInfo$CodecCapabilities capabilities = this.capabilities;
        if (capabilities == null) {
            this.logNoSupport("channelCount.caps");
            return false;
        }
        final MediaCodecInfo$AudioCapabilities audioCapabilities = capabilities.getAudioCapabilities();
        if (audioCapabilities == null) {
            this.logNoSupport("channelCount.aCaps");
            return false;
        }
        if (adjustMaxInputChannelCount(this.name, this.mimeType, audioCapabilities.getMaxInputChannelCount()) < i) {
            final StringBuilder sb = new StringBuilder();
            sb.append("channelCount.support, ");
            sb.append(i);
            this.logNoSupport(sb.toString());
            return false;
        }
        return true;
    }
    
    @TargetApi(21)
    public boolean isAudioSampleRateSupportedV21(final int i) {
        final MediaCodecInfo$CodecCapabilities capabilities = this.capabilities;
        if (capabilities == null) {
            this.logNoSupport("sampleRate.caps");
            return false;
        }
        final MediaCodecInfo$AudioCapabilities audioCapabilities = capabilities.getAudioCapabilities();
        if (audioCapabilities == null) {
            this.logNoSupport("sampleRate.aCaps");
            return false;
        }
        if (!audioCapabilities.isSampleRateSupported(i)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sampleRate.support, ");
            sb.append(i);
            this.logNoSupport(sb.toString());
            return false;
        }
        return true;
    }
    
    public boolean isCodecSupported(final String s) {
        if (s == null || this.mimeType == null) {
            return true;
        }
        final String mediaMimeType = MimeTypes.getMediaMimeType(s);
        if (mediaMimeType == null) {
            return true;
        }
        if (!this.mimeType.equals(mediaMimeType)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("codec.mime ");
            sb.append(s);
            sb.append(", ");
            sb.append(mediaMimeType);
            this.logNoSupport(sb.toString());
            return false;
        }
        final Pair<Integer, Integer> codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(s);
        if (codecProfileAndLevel == null) {
            return true;
        }
        final int intValue = (int)codecProfileAndLevel.first;
        final int intValue2 = (int)codecProfileAndLevel.second;
        if (!this.isVideo && intValue != 42) {
            return true;
        }
        for (final MediaCodecInfo$CodecProfileLevel mediaCodecInfo$CodecProfileLevel : this.getProfileLevels()) {
            if (mediaCodecInfo$CodecProfileLevel.profile == intValue && mediaCodecInfo$CodecProfileLevel.level >= intValue2) {
                return true;
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("codec.profileLevel, ");
        sb2.append(s);
        sb2.append(", ");
        sb2.append(mediaMimeType);
        this.logNoSupport(sb2.toString());
        return false;
    }
    
    public boolean isFormatSupported(final Format format) throws MediaCodecUtil.DecoderQueryException {
        final boolean codecSupported = this.isCodecSupported(format.codecs);
        final boolean b = false;
        boolean b2 = false;
        if (!codecSupported) {
            return false;
        }
        if (this.isVideo) {
            final int width = format.width;
            if (width > 0) {
                final int height = format.height;
                if (height > 0) {
                    if (Util.SDK_INT >= 21) {
                        return this.isVideoSizeAndRateSupportedV21(width, height, format.frameRate);
                    }
                    if (width * height <= MediaCodecUtil.maxH264DecodableFrameSize()) {
                        b2 = true;
                    }
                    if (!b2) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("legacyFrameSize, ");
                        sb.append(format.width);
                        sb.append("x");
                        sb.append(format.height);
                        this.logNoSupport(sb.toString());
                    }
                    return b2;
                }
            }
            return true;
        }
        if (Util.SDK_INT >= 21) {
            final int sampleRate = format.sampleRate;
            if (sampleRate != -1) {
                final boolean b3 = b;
                if (!this.isAudioSampleRateSupportedV21(sampleRate)) {
                    return b3;
                }
            }
            final int channelCount = format.channelCount;
            if (channelCount != -1) {
                final boolean b3 = b;
                if (!this.isAudioChannelCountSupportedV21(channelCount)) {
                    return b3;
                }
            }
        }
        return true;
    }
    
    public boolean isSeamlessAdaptationSupported(final Format format) {
        if (this.isVideo) {
            return this.adaptive;
        }
        final Pair<Integer, Integer> codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(format.codecs);
        return codecProfileAndLevel != null && (int)codecProfileAndLevel.first == 42;
    }
    
    public boolean isSeamlessAdaptationSupported(final Format format, final Format format2, final boolean b) {
        final boolean isVideo = this.isVideo;
        final boolean b2 = true;
        final boolean b3 = true;
        if (isVideo) {
            if (format.sampleMimeType.equals(format2.sampleMimeType) && format.rotationDegrees == format2.rotationDegrees && (this.adaptive || (format.width == format2.width && format.height == format2.height))) {
                if (!b) {
                    final boolean b4 = b3;
                    if (format2.colorInfo == null) {
                        return b4;
                    }
                }
                if (Util.areEqual(format.colorInfo, format2.colorInfo)) {
                    return b3;
                }
            }
            return false;
        }
        if ("audio/mp4a-latm".equals(this.mimeType) && format.sampleMimeType.equals(format2.sampleMimeType) && format.channelCount == format2.channelCount) {
            if (format.sampleRate == format2.sampleRate) {
                final Pair<Integer, Integer> codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(format.codecs);
                final Pair<Integer, Integer> codecProfileAndLevel2 = MediaCodecUtil.getCodecProfileAndLevel(format2.codecs);
                if (codecProfileAndLevel != null) {
                    if (codecProfileAndLevel2 != null) {
                        final int intValue = (int)codecProfileAndLevel.first;
                        final int intValue2 = (int)codecProfileAndLevel2.first;
                        return intValue == 42 && intValue2 == 42 && b2;
                    }
                }
            }
        }
        return false;
    }
    
    @TargetApi(21)
    public boolean isVideoSizeAndRateSupportedV21(final int n, final int n2, final double n3) {
        final MediaCodecInfo$CodecCapabilities capabilities = this.capabilities;
        if (capabilities == null) {
            this.logNoSupport("sizeAndRate.caps");
            return false;
        }
        final MediaCodecInfo$VideoCapabilities videoCapabilities = capabilities.getVideoCapabilities();
        if (videoCapabilities == null) {
            this.logNoSupport("sizeAndRate.vCaps");
            return false;
        }
        if (!areSizeAndRateSupportedV21(videoCapabilities, n, n2, n3)) {
            if (n >= n2 || !areSizeAndRateSupportedV21(videoCapabilities, n2, n, n3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("sizeAndRate.support, ");
                sb.append(n);
                sb.append("x");
                sb.append(n2);
                sb.append("x");
                sb.append(n3);
                this.logNoSupport(sb.toString());
                return false;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("sizeAndRate.rotated, ");
            sb2.append(n);
            sb2.append("x");
            sb2.append(n2);
            sb2.append("x");
            sb2.append(n3);
            this.logAssumedSupport(sb2.toString());
        }
        return true;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
