package com.google.android.exoplayer2.util;

import android.text.TextUtils;
import java.util.ArrayList;

public final class MimeTypes {
    public static final String APPLICATION_CAMERA_MOTION = "application/x-camera-motion";
    public static final String APPLICATION_CEA608 = "application/cea-608";
    public static final String APPLICATION_CEA708 = "application/cea-708";
    public static final String APPLICATION_DVBSUBS = "application/dvbsubs";
    public static final String APPLICATION_EMSG = "application/x-emsg";
    public static final String APPLICATION_EXIF = "application/x-exif";
    public static final String APPLICATION_ICY = "application/x-icy";
    public static final String APPLICATION_ID3 = "application/id3";
    public static final String APPLICATION_M3U8 = "application/x-mpegURL";
    public static final String APPLICATION_MP4 = "application/mp4";
    public static final String APPLICATION_MP4CEA608 = "application/x-mp4-cea-608";
    public static final String APPLICATION_MP4VTT = "application/x-mp4-vtt";
    public static final String APPLICATION_MPD = "application/dash+xml";
    public static final String APPLICATION_PGS = "application/pgs";
    public static final String APPLICATION_RAWCC = "application/x-rawcc";
    public static final String APPLICATION_SCTE35 = "application/x-scte35";
    public static final String APPLICATION_SS = "application/vnd.ms-sstr+xml";
    public static final String APPLICATION_SUBRIP = "application/x-subrip";
    public static final String APPLICATION_TTML = "application/ttml+xml";
    public static final String APPLICATION_TX3G = "application/x-quicktime-tx3g";
    public static final String APPLICATION_VOBSUB = "application/vobsub";
    public static final String APPLICATION_WEBM = "application/webm";
    public static final String AUDIO_AAC = "audio/mp4a-latm";
    public static final String AUDIO_AC3 = "audio/ac3";
    public static final String AUDIO_ALAC = "audio/alac";
    public static final String AUDIO_ALAW = "audio/g711-alaw";
    public static final String AUDIO_AMR_NB = "audio/3gpp";
    public static final String AUDIO_AMR_WB = "audio/amr-wb";
    public static final String AUDIO_DTS = "audio/vnd.dts";
    public static final String AUDIO_DTS_EXPRESS = "audio/vnd.dts.hd;profile=lbr";
    public static final String AUDIO_DTS_HD = "audio/vnd.dts.hd";
    public static final String AUDIO_E_AC3 = "audio/eac3";
    public static final String AUDIO_E_AC3_JOC = "audio/eac3-joc";
    public static final String AUDIO_FLAC = "audio/flac";
    public static final String AUDIO_MLAW = "audio/g711-mlaw";
    public static final String AUDIO_MP4 = "audio/mp4";
    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final String AUDIO_MPEG_L1 = "audio/mpeg-L1";
    public static final String AUDIO_MPEG_L2 = "audio/mpeg-L2";
    public static final String AUDIO_MSGSM = "audio/gsm";
    public static final String AUDIO_OPUS = "audio/opus";
    public static final String AUDIO_RAW = "audio/raw";
    public static final String AUDIO_TRUEHD = "audio/true-hd";
    public static final String AUDIO_UNKNOWN = "audio/x-unknown";
    public static final String AUDIO_VORBIS = "audio/vorbis";
    public static final String AUDIO_WEBM = "audio/webm";
    public static final String BASE_TYPE_APPLICATION = "application";
    public static final String BASE_TYPE_AUDIO = "audio";
    public static final String BASE_TYPE_TEXT = "text";
    public static final String BASE_TYPE_VIDEO = "video";
    public static final String TEXT_SSA = "text/x-ssa";
    public static final String TEXT_VTT = "text/vtt";
    public static final String VIDEO_H263 = "video/3gpp";
    public static final String VIDEO_H264 = "video/avc";
    public static final String VIDEO_H265 = "video/hevc";
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String VIDEO_MP4V = "video/mp4v-es";
    public static final String VIDEO_MPEG = "video/mpeg";
    public static final String VIDEO_MPEG2 = "video/mpeg2";
    public static final String VIDEO_UNKNOWN = "video/x-unknown";
    public static final String VIDEO_VC1 = "video/wvc1";
    public static final String VIDEO_VP8 = "video/x-vnd.on2.vp8";
    public static final String VIDEO_VP9 = "video/x-vnd.on2.vp9";
    public static final String VIDEO_WEBM = "video/webm";
    private static final ArrayList<CustomMimeType> customMimeTypes = new ArrayList();

    private static final class CustomMimeType {
        public final String codecPrefix;
        public final String mimeType;
        public final int trackType;

        public CustomMimeType(String str, String str2, int i) {
            this.mimeType = str;
            this.codecPrefix = str2;
            this.trackType = i;
        }
    }

    public static String getMimeTypeFromMp4ObjectType(int i) {
        if (i == 32) {
            return VIDEO_MP4V;
        }
        if (i == 33) {
            return "video/avc";
        }
        if (i == 35) {
            return VIDEO_H265;
        }
        if (i != 64) {
            if (i == 163) {
                return VIDEO_VC1;
            }
            if (i == 177) {
                return VIDEO_VP9;
            }
            if (i == 165) {
                return AUDIO_AC3;
            }
            if (i == 166) {
                return AUDIO_E_AC3;
            }
            switch (i) {
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                    return VIDEO_MPEG2;
                case 102:
                case 103:
                case 104:
                    break;
                case 105:
                case 107:
                    return AUDIO_MPEG;
                case 106:
                    return VIDEO_MPEG;
                default:
                    switch (i) {
                        case 169:
                        case 172:
                            return AUDIO_DTS;
                        case 170:
                        case 171:
                            return AUDIO_DTS_HD;
                        case 173:
                            return AUDIO_OPUS;
                        default:
                            return null;
                    }
            }
        }
        return AUDIO_AAC;
    }

    public static void registerCustomMimeType(String str, String str2, int i) {
        CustomMimeType customMimeType = new CustomMimeType(str, str2, i);
        int size = customMimeTypes.size();
        for (i = 0; i < size; i++) {
            if (str.equals(((CustomMimeType) customMimeTypes.get(i)).mimeType)) {
                customMimeTypes.remove(i);
                break;
            }
        }
        customMimeTypes.add(customMimeType);
    }

    public static boolean isAudio(String str) {
        return BASE_TYPE_AUDIO.equals(getTopLevelType(str));
    }

    public static boolean isVideo(String str) {
        return BASE_TYPE_VIDEO.equals(getTopLevelType(str));
    }

    public static boolean isText(String str) {
        return BASE_TYPE_TEXT.equals(getTopLevelType(str));
    }

    public static boolean isApplication(String str) {
        return BASE_TYPE_APPLICATION.equals(getTopLevelType(str));
    }

    public static String getVideoMediaMimeType(String str) {
        if (str == null) {
            return null;
        }
        for (String mediaMimeType : Util.splitCodecs(str)) {
            String mediaMimeType2 = getMediaMimeType(mediaMimeType2);
            if (mediaMimeType2 != null && isVideo(mediaMimeType2)) {
                return mediaMimeType2;
            }
        }
        return null;
    }

    public static String getAudioMediaMimeType(String str) {
        if (str == null) {
            return null;
        }
        for (String mediaMimeType : Util.splitCodecs(str)) {
            String mediaMimeType2 = getMediaMimeType(mediaMimeType2);
            if (mediaMimeType2 != null && isAudio(mediaMimeType2)) {
                return mediaMimeType2;
            }
        }
        return null;
    }

    public static String getMediaMimeType(String str) {
        String str2 = null;
        if (str == null) {
            return null;
        }
        str = Util.toLowerInvariant(str.trim());
        if (str.startsWith("avc1") || str.startsWith("avc3")) {
            return "video/avc";
        }
        if (str.startsWith("hev1") || str.startsWith("hvc1")) {
            return VIDEO_H265;
        }
        if (str.startsWith("vp9") || str.startsWith("vp09")) {
            return VIDEO_VP9;
        }
        if (str.startsWith("vp8") || str.startsWith("vp08")) {
            return VIDEO_VP8;
        }
        if (str.startsWith("mp4a")) {
            if (str.startsWith("mp4a.")) {
                str = str.substring(5);
                if (str.length() >= 2) {
                    try {
                        str2 = getMimeTypeFromMp4ObjectType(Integer.parseInt(Util.toUpperInvariant(str.substring(0, 2)), 16));
                    } catch (NumberFormatException unused) {
                    }
                }
            }
            if (str2 == null) {
                str2 = AUDIO_AAC;
            }
            return str2;
        } else if (str.startsWith("ac-3") || str.startsWith("dac3")) {
            return AUDIO_AC3;
        } else {
            if (str.startsWith("ec-3") || str.startsWith("dec3")) {
                return AUDIO_E_AC3;
            }
            if (str.startsWith("ec+3")) {
                return AUDIO_E_AC3_JOC;
            }
            if (str.startsWith("dtsc") || str.startsWith("dtse")) {
                return AUDIO_DTS;
            }
            if (str.startsWith("dtsh") || str.startsWith("dtsl")) {
                return AUDIO_DTS_HD;
            }
            if (str.startsWith("opus")) {
                return AUDIO_OPUS;
            }
            if (str.startsWith("vorbis")) {
                return AUDIO_VORBIS;
            }
            if (str.startsWith("flac")) {
                return AUDIO_FLAC;
            }
            return getCustomMimeTypeForCodec(str);
        }
    }

    public static int getTrackType(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        if (isAudio(str)) {
            return 1;
        }
        if (isVideo(str)) {
            return 2;
        }
        if (isText(str) || APPLICATION_CEA608.equals(str) || APPLICATION_CEA708.equals(str) || APPLICATION_MP4CEA608.equals(str) || APPLICATION_SUBRIP.equals(str) || APPLICATION_TTML.equals(str) || APPLICATION_TX3G.equals(str) || APPLICATION_MP4VTT.equals(str) || APPLICATION_RAWCC.equals(str) || APPLICATION_VOBSUB.equals(str) || APPLICATION_PGS.equals(str) || APPLICATION_DVBSUBS.equals(str)) {
            return 3;
        }
        if (APPLICATION_ID3.equals(str) || APPLICATION_EMSG.equals(str) || APPLICATION_SCTE35.equals(str)) {
            return 4;
        }
        if (APPLICATION_CAMERA_MOTION.equals(str)) {
            return 5;
        }
        return getTrackTypeForCustomMimeType(str);
    }

    public static int getEncoding(java.lang.String r7) {
        /*
        r0 = r7.hashCode();
        r1 = 0;
        r2 = 4;
        r3 = 3;
        r4 = 2;
        r5 = 1;
        r6 = 5;
        switch(r0) {
            case -2123537834: goto L_0x0040;
            case -1095064472: goto L_0x0036;
            case 187078296: goto L_0x002c;
            case 1504578661: goto L_0x0022;
            case 1505942594: goto L_0x0018;
            case 1556697186: goto L_0x000e;
            default: goto L_0x000d;
        };
    L_0x000d:
        goto L_0x004a;
    L_0x000e:
        r0 = "audio/true-hd";
        r7 = r7.equals(r0);
        if (r7 == 0) goto L_0x004a;
    L_0x0016:
        r7 = 5;
        goto L_0x004b;
    L_0x0018:
        r0 = "audio/vnd.dts.hd";
        r7 = r7.equals(r0);
        if (r7 == 0) goto L_0x004a;
    L_0x0020:
        r7 = 4;
        goto L_0x004b;
    L_0x0022:
        r0 = "audio/eac3";
        r7 = r7.equals(r0);
        if (r7 == 0) goto L_0x004a;
    L_0x002a:
        r7 = 1;
        goto L_0x004b;
    L_0x002c:
        r0 = "audio/ac3";
        r7 = r7.equals(r0);
        if (r7 == 0) goto L_0x004a;
    L_0x0034:
        r7 = 0;
        goto L_0x004b;
    L_0x0036:
        r0 = "audio/vnd.dts";
        r7 = r7.equals(r0);
        if (r7 == 0) goto L_0x004a;
    L_0x003e:
        r7 = 3;
        goto L_0x004b;
    L_0x0040:
        r0 = "audio/eac3-joc";
        r7 = r7.equals(r0);
        if (r7 == 0) goto L_0x004a;
    L_0x0048:
        r7 = 2;
        goto L_0x004b;
    L_0x004a:
        r7 = -1;
    L_0x004b:
        if (r7 == 0) goto L_0x0062;
    L_0x004d:
        if (r7 == r5) goto L_0x0060;
    L_0x004f:
        if (r7 == r4) goto L_0x0060;
    L_0x0051:
        if (r7 == r3) goto L_0x005e;
    L_0x0053:
        if (r7 == r2) goto L_0x005b;
    L_0x0055:
        if (r7 == r6) goto L_0x0058;
    L_0x0057:
        return r1;
    L_0x0058:
        r7 = 14;
        return r7;
    L_0x005b:
        r7 = 8;
        return r7;
    L_0x005e:
        r7 = 7;
        return r7;
    L_0x0060:
        r7 = 6;
        return r7;
    L_0x0062:
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.MimeTypes.getEncoding(java.lang.String):int");
    }

    public static int getTrackTypeOfCodec(String str) {
        return getTrackType(getMediaMimeType(str));
    }

    private static String getTopLevelType(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(47);
        if (indexOf != -1) {
            return str.substring(0, indexOf);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid mime type: ");
        stringBuilder.append(str);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static String getCustomMimeTypeForCodec(String str) {
        int size = customMimeTypes.size();
        for (int i = 0; i < size; i++) {
            CustomMimeType customMimeType = (CustomMimeType) customMimeTypes.get(i);
            if (str.startsWith(customMimeType.codecPrefix)) {
                return customMimeType.mimeType;
            }
        }
        return null;
    }

    private static int getTrackTypeForCustomMimeType(String str) {
        int size = customMimeTypes.size();
        for (int i = 0; i < size; i++) {
            CustomMimeType customMimeType = (CustomMimeType) customMimeTypes.get(i);
            if (str.equals(customMimeType.mimeType)) {
                return customMimeType.trackType;
            }
        }
        return -1;
    }

    private MimeTypes() {
    }
}
