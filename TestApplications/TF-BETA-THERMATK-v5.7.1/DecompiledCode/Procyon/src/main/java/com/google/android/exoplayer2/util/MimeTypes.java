// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import android.text.TextUtils;
import java.util.ArrayList;

public final class MimeTypes
{
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
    private static final ArrayList<CustomMimeType> customMimeTypes;
    
    static {
        customMimeTypes = new ArrayList<CustomMimeType>();
    }
    
    private MimeTypes() {
    }
    
    public static String getAudioMediaMimeType(final String s) {
        if (s == null) {
            return null;
        }
        final String[] splitCodecs = Util.splitCodecs(s);
        for (int length = splitCodecs.length, i = 0; i < length; ++i) {
            final String mediaMimeType = getMediaMimeType(splitCodecs[i]);
            if (mediaMimeType != null && isAudio(mediaMimeType)) {
                return mediaMimeType;
            }
        }
        return null;
    }
    
    private static String getCustomMimeTypeForCodec(final String s) {
        for (int size = MimeTypes.customMimeTypes.size(), i = 0; i < size; ++i) {
            final CustomMimeType customMimeType = MimeTypes.customMimeTypes.get(i);
            if (s.startsWith(customMimeType.codecPrefix)) {
                return customMimeType.mimeType;
            }
        }
        return null;
    }
    
    public static int getEncoding(final String s) {
        int n = 0;
        Label_0153: {
            switch (s.hashCode()) {
                case 1556697186: {
                    if (s.equals("audio/true-hd")) {
                        n = 5;
                        break Label_0153;
                    }
                    break;
                }
                case 1505942594: {
                    if (s.equals("audio/vnd.dts.hd")) {
                        n = 4;
                        break Label_0153;
                    }
                    break;
                }
                case 1504578661: {
                    if (s.equals("audio/eac3")) {
                        n = 1;
                        break Label_0153;
                    }
                    break;
                }
                case 187078296: {
                    if (s.equals("audio/ac3")) {
                        n = 0;
                        break Label_0153;
                    }
                    break;
                }
                case -1095064472: {
                    if (s.equals("audio/vnd.dts")) {
                        n = 3;
                        break Label_0153;
                    }
                    break;
                }
                case -2123537834: {
                    if (s.equals("audio/eac3-joc")) {
                        n = 2;
                        break Label_0153;
                    }
                    break;
                }
            }
            n = -1;
        }
        if (n == 0) {
            return 5;
        }
        if (n == 1 || n == 2) {
            return 6;
        }
        if (n == 3) {
            return 7;
        }
        if (n == 4) {
            return 8;
        }
        if (n != 5) {
            return 0;
        }
        return 14;
    }
    
    public static String getMediaMimeType(String mimeTypeFromMp4ObjectType) {
        String s = null;
        if (mimeTypeFromMp4ObjectType == null) {
            return null;
        }
        final String lowerInvariant = Util.toLowerInvariant(mimeTypeFromMp4ObjectType.trim());
        if (lowerInvariant.startsWith("avc1") || lowerInvariant.startsWith("avc3")) {
            return "video/avc";
        }
        if (lowerInvariant.startsWith("hev1") || lowerInvariant.startsWith("hvc1")) {
            return "video/hevc";
        }
        if (lowerInvariant.startsWith("vp9") || lowerInvariant.startsWith("vp09")) {
            return "video/x-vnd.on2.vp9";
        }
        if (lowerInvariant.startsWith("vp8") || lowerInvariant.startsWith("vp08")) {
            return "video/x-vnd.on2.vp8";
        }
        Label_0175: {
            if (!lowerInvariant.startsWith("mp4a")) {
                break Label_0175;
            }
            mimeTypeFromMp4ObjectType = s;
            String substring;
            Label_0257_Outer:Label_0221_Outer:
            while (true) {
                if (!lowerInvariant.startsWith("mp4a.")) {
                    break Label_0164;
                }
                substring = lowerInvariant.substring(5);
                mimeTypeFromMp4ObjectType = s;
                if (substring.length() < 2) {
                    break Label_0164;
                }
                try {
                    mimeTypeFromMp4ObjectType = getMimeTypeFromMp4ObjectType(Integer.parseInt(Util.toUpperInvariant(substring.substring(0, 2)), 16));
                    if ((s = mimeTypeFromMp4ObjectType) == null) {
                        s = "audio/mp4a-latm";
                    }
                    return s;
                    // iftrue(Label_0293:, !lowerInvariant.startsWith("opus"))
                    // iftrue(Label_0324:, lowerInvariant.startsWith("dtsh") || lowerInvariant.startsWith("dtsl"))
                    // iftrue(Label_0234:, !lowerInvariant.startsWith("ec+3"))
                    // iftrue(Label_0333:, lowerInvariant.startsWith("ac-3") || lowerInvariant.startsWith("dac3"))
                    // iftrue(Label_0306:, !lowerInvariant.startsWith("vorbis"))
                    // iftrue(Label_0319:, !lowerInvariant.startsWith("flac"))
                    // iftrue(Label_0327:, lowerInvariant.startsWith("dtsc") || lowerInvariant.startsWith("dtse"))
                    while (true) {
                    Label_0198:
                        while (true) {
                            while (true) {
                                return "audio/opus";
                                Label_0324: {
                                    return "audio/vnd.dts.hd";
                                }
                                continue Label_0257_Outer;
                            }
                            return "audio/eac3-joc";
                            break Label_0198;
                            Label_0330: {
                                return "audio/eac3";
                            }
                            Label_0293:
                            return "audio/vorbis";
                            Label_0319:
                            return getCustomMimeTypeForCodec(lowerInvariant);
                            Label_0306:
                            return "audio/flac";
                            Label_0333:
                            return "audio/ac3";
                            Label_0327:
                            return "audio/vnd.dts";
                            Label_0234:
                            continue Label_0221_Outer;
                        }
                        continue;
                    }
                }
                // iftrue(Label_0330:, lowerInvariant.startsWith("ec-3") || lowerInvariant.startsWith("dec3"))
                catch (NumberFormatException ex) {
                    mimeTypeFromMp4ObjectType = s;
                    continue;
                }
                break;
            }
        }
    }
    
    public static String getMimeTypeFromMp4ObjectType(final int n) {
        if (n == 32) {
            return "video/mp4v-es";
        }
        if (n == 33) {
            return "video/avc";
        }
        if (n != 35) {
            if (n != 64) {
                if (n == 163) {
                    return "video/wvc1";
                }
                if (n == 177) {
                    return "video/x-vnd.on2.vp9";
                }
                if (n == 165) {
                    return "audio/ac3";
                }
                if (n == 166) {
                    return "audio/eac3";
                }
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                return null;
                            }
                            case 173: {
                                return "audio/opus";
                            }
                            case 170:
                            case 171: {
                                return "audio/vnd.dts.hd";
                            }
                            case 169:
                            case 172: {
                                return "audio/vnd.dts";
                            }
                        }
                        break;
                    }
                    case 106: {
                        return "video/mpeg";
                    }
                    case 105:
                    case 107: {
                        return "audio/mpeg";
                    }
                    case 96:
                    case 97:
                    case 98:
                    case 99:
                    case 100:
                    case 101: {
                        return "video/mpeg2";
                    }
                    case 102:
                    case 103:
                    case 104: {
                        break;
                    }
                }
            }
            return "audio/mp4a-latm";
        }
        return "video/hevc";
    }
    
    private static String getTopLevelType(final String str) {
        if (str == null) {
            return null;
        }
        final int index = str.indexOf(47);
        if (index != -1) {
            return str.substring(0, index);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid mime type: ");
        sb.append(str);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static int getTrackType(final String anObject) {
        if (TextUtils.isEmpty((CharSequence)anObject)) {
            return -1;
        }
        if (isAudio(anObject)) {
            return 1;
        }
        if (isVideo(anObject)) {
            return 2;
        }
        if (isText(anObject) || "application/cea-608".equals(anObject) || "application/cea-708".equals(anObject) || "application/x-mp4-cea-608".equals(anObject) || "application/x-subrip".equals(anObject) || "application/ttml+xml".equals(anObject) || "application/x-quicktime-tx3g".equals(anObject) || "application/x-mp4-vtt".equals(anObject) || "application/x-rawcc".equals(anObject) || "application/vobsub".equals(anObject) || "application/pgs".equals(anObject) || "application/dvbsubs".equals(anObject)) {
            return 3;
        }
        if ("application/id3".equals(anObject) || "application/x-emsg".equals(anObject) || "application/x-scte35".equals(anObject)) {
            return 4;
        }
        if ("application/x-camera-motion".equals(anObject)) {
            return 5;
        }
        return getTrackTypeForCustomMimeType(anObject);
    }
    
    private static int getTrackTypeForCustomMimeType(final String s) {
        for (int size = MimeTypes.customMimeTypes.size(), i = 0; i < size; ++i) {
            final CustomMimeType customMimeType = MimeTypes.customMimeTypes.get(i);
            if (s.equals(customMimeType.mimeType)) {
                return customMimeType.trackType;
            }
        }
        return -1;
    }
    
    public static int getTrackTypeOfCodec(final String s) {
        return getTrackType(getMediaMimeType(s));
    }
    
    public static String getVideoMediaMimeType(final String s) {
        if (s == null) {
            return null;
        }
        final String[] splitCodecs = Util.splitCodecs(s);
        for (int length = splitCodecs.length, i = 0; i < length; ++i) {
            final String mediaMimeType = getMediaMimeType(splitCodecs[i]);
            if (mediaMimeType != null && isVideo(mediaMimeType)) {
                return mediaMimeType;
            }
        }
        return null;
    }
    
    public static boolean isApplication(final String s) {
        return "application".equals(getTopLevelType(s));
    }
    
    public static boolean isAudio(final String s) {
        return "audio".equals(getTopLevelType(s));
    }
    
    public static boolean isText(final String s) {
        return "text".equals(getTopLevelType(s));
    }
    
    public static boolean isVideo(final String s) {
        return "video".equals(getTopLevelType(s));
    }
    
    public static void registerCustomMimeType(final String s, final String s2, int i) {
        final CustomMimeType e = new CustomMimeType(s, s2, i);
        int size;
        for (size = MimeTypes.customMimeTypes.size(), i = 0; i < size; ++i) {
            if (s.equals(MimeTypes.customMimeTypes.get(i).mimeType)) {
                MimeTypes.customMimeTypes.remove(i);
                break;
            }
        }
        MimeTypes.customMimeTypes.add(e);
    }
    
    private static final class CustomMimeType
    {
        public final String codecPrefix;
        public final String mimeType;
        public final int trackType;
        
        public CustomMimeType(final String mimeType, final String codecPrefix, final int trackType) {
            this.mimeType = mimeType;
            this.codecPrefix = codecPrefix;
            this.trackType = trackType;
        }
    }
}
