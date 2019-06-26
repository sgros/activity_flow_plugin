package com.google.android.exoplayer2.mediacodec;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.media.MediaCodecInfo.AudioCapabilities;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecInfo.VideoCapabilities;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

@TargetApi(16)
public final class MediaCodecInfo {
    public final boolean adaptive;
    public final CodecCapabilities capabilities;
    private final boolean isVideo;
    public final String mimeType;
    public final String name;
    public final boolean passthrough;
    public final boolean secure;
    public final boolean tunneling;

    public static MediaCodecInfo newPassthroughInstance(String str) {
        return new MediaCodecInfo(str, null, null, true, false, false);
    }

    public static MediaCodecInfo newInstance(String str, String str2, CodecCapabilities codecCapabilities, boolean z, boolean z2) {
        return new MediaCodecInfo(str, str2, codecCapabilities, false, z, z2);
    }

    private MediaCodecInfo(String str, String str2, CodecCapabilities codecCapabilities, boolean z, boolean z2, boolean z3) {
        Assertions.checkNotNull(str);
        this.name = str;
        this.mimeType = str2;
        this.capabilities = codecCapabilities;
        this.passthrough = z;
        boolean z4 = true;
        z2 = (z2 || codecCapabilities == null || !isAdaptive(codecCapabilities)) ? false : true;
        this.adaptive = z2;
        z2 = codecCapabilities != null && isTunneling(codecCapabilities);
        this.tunneling = z2;
        if (!z3 && (codecCapabilities == null || !isSecure(codecCapabilities))) {
            z4 = false;
        }
        this.secure = z4;
        this.isVideo = MimeTypes.isVideo(str2);
    }

    public String toString() {
        return this.name;
    }

    public CodecProfileLevel[] getProfileLevels() {
        CodecCapabilities codecCapabilities = this.capabilities;
        if (codecCapabilities != null) {
            CodecProfileLevel[] codecProfileLevelArr = codecCapabilities.profileLevels;
            if (codecProfileLevelArr != null) {
                return codecProfileLevelArr;
            }
        }
        return new CodecProfileLevel[0];
    }

    /* JADX WARNING: Missing block: B:29:0x006a, code skipped:
            if (isAudioChannelCountSupportedV21(r7) == false) goto L_0x006d;
     */
    public boolean isFormatSupported(com.google.android.exoplayer2.Format r7) throws com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException {
        /*
        r6 = this;
        r0 = r7.codecs;
        r0 = r6.isCodecSupported(r0);
        r1 = 0;
        if (r0 != 0) goto L_0x000a;
    L_0x0009:
        return r1;
    L_0x000a:
        r0 = r6.isVideo;
        r2 = 21;
        r3 = 1;
        if (r0 == 0) goto L_0x0053;
    L_0x0011:
        r0 = r7.width;
        if (r0 <= 0) goto L_0x0052;
    L_0x0015:
        r4 = r7.height;
        if (r4 > 0) goto L_0x001a;
    L_0x0019:
        goto L_0x0052;
    L_0x001a:
        r5 = com.google.android.exoplayer2.util.Util.SDK_INT;
        if (r5 < r2) goto L_0x0026;
    L_0x001e:
        r7 = r7.frameRate;
        r1 = (double) r7;
        r7 = r6.isVideoSizeAndRateSupportedV21(r0, r4, r1);
        return r7;
    L_0x0026:
        r0 = r0 * r4;
        r2 = com.google.android.exoplayer2.mediacodec.MediaCodecUtil.maxH264DecodableFrameSize();
        if (r0 > r2) goto L_0x002f;
    L_0x002e:
        r1 = 1;
    L_0x002f:
        if (r1 != 0) goto L_0x0051;
    L_0x0031:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r2 = "legacyFrameSize, ";
        r0.append(r2);
        r2 = r7.width;
        r0.append(r2);
        r2 = "x";
        r0.append(r2);
        r7 = r7.height;
        r0.append(r7);
        r7 = r0.toString();
        r6.logNoSupport(r7);
    L_0x0051:
        return r1;
    L_0x0052:
        return r3;
    L_0x0053:
        r0 = com.google.android.exoplayer2.util.Util.SDK_INT;
        if (r0 < r2) goto L_0x006c;
    L_0x0057:
        r0 = r7.sampleRate;
        r2 = -1;
        if (r0 == r2) goto L_0x0062;
    L_0x005c:
        r0 = r6.isAudioSampleRateSupportedV21(r0);
        if (r0 == 0) goto L_0x006d;
    L_0x0062:
        r7 = r7.channelCount;
        if (r7 == r2) goto L_0x006c;
    L_0x0066:
        r7 = r6.isAudioChannelCountSupportedV21(r7);
        if (r7 == 0) goto L_0x006d;
    L_0x006c:
        r1 = 1;
    L_0x006d:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecInfo.isFormatSupported(com.google.android.exoplayer2.Format):boolean");
    }

    public boolean isCodecSupported(String str) {
        if (str == null || this.mimeType == null) {
            return true;
        }
        String mediaMimeType = MimeTypes.getMediaMimeType(str);
        if (mediaMimeType == null) {
            return true;
        }
        String str2 = ", ";
        StringBuilder stringBuilder;
        if (this.mimeType.equals(mediaMimeType)) {
            Pair codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(str);
            if (codecProfileAndLevel == null) {
                return true;
            }
            int intValue = ((Integer) codecProfileAndLevel.first).intValue();
            int intValue2 = ((Integer) codecProfileAndLevel.second).intValue();
            if (!this.isVideo && intValue != 42) {
                return true;
            }
            for (CodecProfileLevel codecProfileLevel : getProfileLevels()) {
                if (codecProfileLevel.profile == intValue && codecProfileLevel.level >= intValue2) {
                    return true;
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("codec.profileLevel, ");
            stringBuilder.append(str);
            stringBuilder.append(str2);
            stringBuilder.append(mediaMimeType);
            logNoSupport(stringBuilder.toString());
            return false;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("codec.mime ");
        stringBuilder.append(str);
        stringBuilder.append(str2);
        stringBuilder.append(mediaMimeType);
        logNoSupport(stringBuilder.toString());
        return false;
    }

    public boolean isSeamlessAdaptationSupported(Format format) {
        if (this.isVideo) {
            return this.adaptive;
        }
        Pair codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(format.codecs);
        boolean z = codecProfileAndLevel != null && ((Integer) codecProfileAndLevel.first).intValue() == 42;
        return z;
    }

    public boolean isSeamlessAdaptationSupported(Format format, Format format2, boolean z) {
        boolean z2 = true;
        if (this.isVideo) {
            if (!(format.sampleMimeType.equals(format2.sampleMimeType) && format.rotationDegrees == format2.rotationDegrees && ((this.adaptive || (format.width == format2.width && format.height == format2.height)) && ((!z && format2.colorInfo == null) || Util.areEqual(format.colorInfo, format2.colorInfo))))) {
                z2 = false;
            }
            return z2;
        }
        if (MimeTypes.AUDIO_AAC.equals(this.mimeType) && format.sampleMimeType.equals(format2.sampleMimeType) && format.channelCount == format2.channelCount && format.sampleRate == format2.sampleRate) {
            Pair codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(format.codecs);
            Pair codecProfileAndLevel2 = MediaCodecUtil.getCodecProfileAndLevel(format2.codecs);
            if (!(codecProfileAndLevel == null || codecProfileAndLevel2 == null)) {
                int intValue = ((Integer) codecProfileAndLevel.first).intValue();
                int intValue2 = ((Integer) codecProfileAndLevel2.first).intValue();
                if (!(intValue == 42 && intValue2 == 42)) {
                    z2 = false;
                }
                return z2;
            }
        }
        return false;
    }

    @TargetApi(21)
    public boolean isVideoSizeAndRateSupportedV21(int i, int i2, double d) {
        CodecCapabilities codecCapabilities = this.capabilities;
        if (codecCapabilities == null) {
            logNoSupport("sizeAndRate.caps");
            return false;
        }
        VideoCapabilities videoCapabilities = codecCapabilities.getVideoCapabilities();
        if (videoCapabilities == null) {
            logNoSupport("sizeAndRate.vCaps");
            return false;
        }
        if (!areSizeAndRateSupportedV21(videoCapabilities, i, i2, d)) {
            String str = "x";
            StringBuilder stringBuilder;
            if (i >= i2 || !areSizeAndRateSupportedV21(videoCapabilities, i2, i, d)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("sizeAndRate.support, ");
                stringBuilder.append(i);
                stringBuilder.append(str);
                stringBuilder.append(i2);
                stringBuilder.append(str);
                stringBuilder.append(d);
                logNoSupport(stringBuilder.toString());
                return false;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("sizeAndRate.rotated, ");
            stringBuilder.append(i);
            stringBuilder.append(str);
            stringBuilder.append(i2);
            stringBuilder.append(str);
            stringBuilder.append(d);
            logAssumedSupport(stringBuilder.toString());
        }
        return true;
    }

    @TargetApi(21)
    public Point alignVideoSizeV21(int i, int i2) {
        CodecCapabilities codecCapabilities = this.capabilities;
        if (codecCapabilities == null) {
            logNoSupport("align.caps");
            return null;
        }
        VideoCapabilities videoCapabilities = codecCapabilities.getVideoCapabilities();
        if (videoCapabilities == null) {
            logNoSupport("align.vCaps");
            return null;
        }
        int widthAlignment = videoCapabilities.getWidthAlignment();
        int heightAlignment = videoCapabilities.getHeightAlignment();
        return new Point(Util.ceilDivide(i, widthAlignment) * widthAlignment, Util.ceilDivide(i2, heightAlignment) * heightAlignment);
    }

    @TargetApi(21)
    public boolean isAudioSampleRateSupportedV21(int i) {
        CodecCapabilities codecCapabilities = this.capabilities;
        if (codecCapabilities == null) {
            logNoSupport("sampleRate.caps");
            return false;
        }
        AudioCapabilities audioCapabilities = codecCapabilities.getAudioCapabilities();
        if (audioCapabilities == null) {
            logNoSupport("sampleRate.aCaps");
            return false;
        } else if (audioCapabilities.isSampleRateSupported(i)) {
            return true;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("sampleRate.support, ");
            stringBuilder.append(i);
            logNoSupport(stringBuilder.toString());
            return false;
        }
    }

    @TargetApi(21)
    public boolean isAudioChannelCountSupportedV21(int i) {
        CodecCapabilities codecCapabilities = this.capabilities;
        if (codecCapabilities == null) {
            logNoSupport("channelCount.caps");
            return false;
        }
        AudioCapabilities audioCapabilities = codecCapabilities.getAudioCapabilities();
        if (audioCapabilities == null) {
            logNoSupport("channelCount.aCaps");
            return false;
        } else if (adjustMaxInputChannelCount(this.name, this.mimeType, audioCapabilities.getMaxInputChannelCount()) >= i) {
            return true;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("channelCount.support, ");
            stringBuilder.append(i);
            logNoSupport(stringBuilder.toString());
            return false;
        }
    }

    private void logNoSupport(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NoSupport [");
        stringBuilder.append(str);
        str = "] [";
        stringBuilder.append(str);
        stringBuilder.append(this.name);
        stringBuilder.append(", ");
        stringBuilder.append(this.mimeType);
        stringBuilder.append(str);
        stringBuilder.append(Util.DEVICE_DEBUG_INFO);
        stringBuilder.append("]");
        Log.m12d("MediaCodecInfo", stringBuilder.toString());
    }

    private void logAssumedSupport(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AssumedSupport [");
        stringBuilder.append(str);
        str = "] [";
        stringBuilder.append(str);
        stringBuilder.append(this.name);
        stringBuilder.append(", ");
        stringBuilder.append(this.mimeType);
        stringBuilder.append(str);
        stringBuilder.append(Util.DEVICE_DEBUG_INFO);
        stringBuilder.append("]");
        Log.m12d("MediaCodecInfo", stringBuilder.toString());
    }

    private static int adjustMaxInputChannelCount(String str, String str2, int i) {
        if (i > 1 || ((Util.SDK_INT >= 26 && i > 0) || MimeTypes.AUDIO_MPEG.equals(str2) || MimeTypes.AUDIO_AMR_NB.equals(str2) || MimeTypes.AUDIO_AMR_WB.equals(str2) || MimeTypes.AUDIO_AAC.equals(str2) || MimeTypes.AUDIO_VORBIS.equals(str2) || MimeTypes.AUDIO_OPUS.equals(str2) || MimeTypes.AUDIO_RAW.equals(str2) || MimeTypes.AUDIO_FLAC.equals(str2) || MimeTypes.AUDIO_ALAW.equals(str2) || MimeTypes.AUDIO_MLAW.equals(str2) || MimeTypes.AUDIO_MSGSM.equals(str2))) {
            return i;
        }
        int i2 = MimeTypes.AUDIO_AC3.equals(str2) ? 6 : MimeTypes.AUDIO_E_AC3.equals(str2) ? 16 : 30;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AssumedMaxChannelAdjustment: ");
        stringBuilder.append(str);
        stringBuilder.append(", [");
        stringBuilder.append(i);
        stringBuilder.append(" to ");
        stringBuilder.append(i2);
        stringBuilder.append("]");
        Log.m18w("MediaCodecInfo", stringBuilder.toString());
        return i2;
    }

    private static boolean isAdaptive(CodecCapabilities codecCapabilities) {
        return Util.SDK_INT >= 19 && isAdaptiveV19(codecCapabilities);
    }

    @TargetApi(19)
    private static boolean isAdaptiveV19(CodecCapabilities codecCapabilities) {
        return codecCapabilities.isFeatureSupported("adaptive-playback");
    }

    private static boolean isTunneling(CodecCapabilities codecCapabilities) {
        return Util.SDK_INT >= 21 && isTunnelingV21(codecCapabilities);
    }

    @TargetApi(21)
    private static boolean isTunnelingV21(CodecCapabilities codecCapabilities) {
        return codecCapabilities.isFeatureSupported("tunneled-playback");
    }

    private static boolean isSecure(CodecCapabilities codecCapabilities) {
        return Util.SDK_INT >= 21 && isSecureV21(codecCapabilities);
    }

    @TargetApi(21)
    private static boolean isSecureV21(CodecCapabilities codecCapabilities) {
        return codecCapabilities.isFeatureSupported("secure-playback");
    }

    @TargetApi(21)
    private static boolean areSizeAndRateSupportedV21(VideoCapabilities videoCapabilities, int i, int i2, double d) {
        if (d == -1.0d || d <= 0.0d) {
            return videoCapabilities.isSizeSupported(i, i2);
        }
        return videoCapabilities.areSizeAndRateSupported(i, i2, d);
    }
}
