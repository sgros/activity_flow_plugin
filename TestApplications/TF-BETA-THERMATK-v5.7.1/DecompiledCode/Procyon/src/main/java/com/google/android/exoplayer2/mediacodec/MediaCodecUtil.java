// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.mediacodec;

import android.media.MediaCodecList;
import android.text.TextUtils;
import android.media.MediaCodecInfo$CodecProfileLevel;
import java.util.regex.Matcher;
import android.media.MediaCodecInfo$CodecCapabilities;
import java.util.ArrayList;
import java.util.Collection;
import java.io.Serializable;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Log;
import android.util.Pair;
import com.google.android.exoplayer2.util.Util;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.Map;
import android.util.SparseIntArray;
import android.annotation.TargetApi;
import android.annotation.SuppressLint;

@SuppressLint({ "InlinedApi" })
@TargetApi(16)
public final class MediaCodecUtil
{
    private static final SparseIntArray AVC_LEVEL_NUMBER_TO_CONST;
    private static final SparseIntArray AVC_PROFILE_NUMBER_TO_CONST;
    private static final Map<String, Integer> HEVC_CODEC_STRING_TO_PROFILE_LEVEL;
    private static final SparseIntArray MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE;
    private static final Pattern PROFILE_PATTERN;
    private static final RawAudioCodecComparator RAW_AUDIO_CODEC_COMPARATOR;
    private static final HashMap<CodecKey, List<MediaCodecInfo>> decoderInfosCache;
    private static int maxH264DecodableFrameSize;
    
    static {
        PROFILE_PATTERN = Pattern.compile("^\\D?(\\d+)$");
        RAW_AUDIO_CODEC_COMPARATOR = new RawAudioCodecComparator();
        decoderInfosCache = new HashMap<CodecKey, List<MediaCodecInfo>>();
        MediaCodecUtil.maxH264DecodableFrameSize = -1;
        (AVC_PROFILE_NUMBER_TO_CONST = new SparseIntArray()).put(66, 1);
        MediaCodecUtil.AVC_PROFILE_NUMBER_TO_CONST.put(77, 2);
        MediaCodecUtil.AVC_PROFILE_NUMBER_TO_CONST.put(88, 4);
        MediaCodecUtil.AVC_PROFILE_NUMBER_TO_CONST.put(100, 8);
        MediaCodecUtil.AVC_PROFILE_NUMBER_TO_CONST.put(110, 16);
        MediaCodecUtil.AVC_PROFILE_NUMBER_TO_CONST.put(122, 32);
        MediaCodecUtil.AVC_PROFILE_NUMBER_TO_CONST.put(244, 64);
        (AVC_LEVEL_NUMBER_TO_CONST = new SparseIntArray()).put(10, 1);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(11, 4);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(12, 8);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(13, 16);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(20, 32);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(21, 64);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(22, 128);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(30, 256);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(31, 512);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(32, 1024);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(40, 2048);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(41, 4096);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(42, 8192);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(50, 16384);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(51, 32768);
        MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.put(52, 65536);
        (HEVC_CODEC_STRING_TO_PROFILE_LEVEL = new HashMap<String, Integer>()).put("L30", 1);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L60", 4);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L63", 16);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L90", 64);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L93", 256);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L120", 1024);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L123", 4096);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L150", 16384);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L153", 65536);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L156", 262144);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L180", 1048576);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L183", 4194304);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L186", 16777216);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H30", 2);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H60", 8);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H63", 32);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H90", 128);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H93", 512);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H120", 2048);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H123", 8192);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H150", 32768);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H153", 131072);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H156", 524288);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H180", 2097152);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H183", 8388608);
        MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H186", 33554432);
        (MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE = new SparseIntArray()).put(1, 1);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(2, 2);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(3, 3);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(4, 4);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(5, 5);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(6, 6);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(17, 17);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(20, 20);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(23, 23);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(29, 29);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(39, 39);
        MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(42, 42);
    }
    
    private static void applyWorkarounds(final String anObject, final List<MediaCodecInfo> list) {
        if ("audio/raw".equals(anObject)) {
            Collections.sort((List<Object>)list, (Comparator<? super Object>)MediaCodecUtil.RAW_AUDIO_CODEC_COMPARATOR);
        }
    }
    
    private static int avcLevelToMaxFrameSize(final int n) {
        if (n == 1 || n == 2) {
            return 25344;
        }
        switch (n) {
            default: {
                return -1;
            }
            case 32768:
            case 65536: {
                return 9437184;
            }
            case 16384: {
                return 5652480;
            }
            case 8192: {
                return 2228224;
            }
            case 2048:
            case 4096: {
                return 2097152;
            }
            case 1024: {
                return 1310720;
            }
            case 512: {
                return 921600;
            }
            case 128:
            case 256: {
                return 414720;
            }
            case 64: {
                return 202752;
            }
            case 8:
            case 16:
            case 32: {
                return 101376;
            }
        }
    }
    
    private static boolean codecNeedsDisableAdaptationWorkaround(final String s) {
        return Util.SDK_INT <= 22 && ("ODROID-XU3".equals(Util.MODEL) || "Nexus 10".equals(Util.MODEL)) && ("OMX.Exynos.AVC.Decoder".equals(s) || "OMX.Exynos.AVC.Decoder.secure".equals(s));
    }
    
    private static Pair<Integer, Integer> getAacCodecProfileAndLevel(final String s, final String[] array) {
        if (array.length != 3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Ignoring malformed MP4A codec string: ");
            sb.append(s);
            Log.w("MediaCodecUtil", sb.toString());
            return null;
        }
        try {
            if ("audio/mp4a-latm".equals(MimeTypes.getMimeTypeFromMp4ObjectType(Integer.parseInt(array[1], 16)))) {
                final int value = MediaCodecUtil.MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.get(Integer.parseInt(array[2]), -1);
                if (value != -1) {
                    return (Pair<Integer, Integer>)new Pair((Object)value, (Object)0);
                }
            }
        }
        catch (NumberFormatException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Ignoring malformed MP4A codec string: ");
            sb2.append(s);
            Log.w("MediaCodecUtil", sb2.toString());
        }
        return null;
    }
    
    private static Pair<Integer, Integer> getAvcProfileAndLevel(String value, final String[] array) {
        if (array.length < 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Ignoring malformed AVC codec string: ");
            sb.append(value);
            Log.w("MediaCodecUtil", sb.toString());
            return null;
        }
        try {
            Integer value3;
            if (array[1].length() == 6) {
                final int int1 = Integer.parseInt(array[1].substring(0, 2), 16);
                final Serializable value2 = Integer.parseInt(array[1].substring(4), 16);
                value3 = int1;
                value = (String)value2;
            }
            else {
                if (array.length < 3) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Ignoring malformed AVC codec string: ");
                    sb2.append(value);
                    Log.w("MediaCodecUtil", sb2.toString());
                    return null;
                }
                final Integer value4 = Integer.parseInt(array[1]);
                value = (String)Integer.valueOf(Integer.parseInt(array[2]));
                value3 = value4;
            }
            final int value5 = MediaCodecUtil.AVC_PROFILE_NUMBER_TO_CONST.get((int)value3, -1);
            if (value5 == -1) {
                value = (String)new StringBuilder();
                ((StringBuilder)value).append("Unknown AVC profile: ");
                ((StringBuilder)value).append(value3);
                Log.w("MediaCodecUtil", ((StringBuilder)value).toString());
                return null;
            }
            final int value6 = MediaCodecUtil.AVC_LEVEL_NUMBER_TO_CONST.get((int)value, -1);
            if (value6 == -1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Unknown AVC level: ");
                sb3.append((Object)value);
                Log.w("MediaCodecUtil", sb3.toString());
                return null;
            }
            return (Pair<Integer, Integer>)new Pair((Object)value5, (Object)value6);
        }
        catch (NumberFormatException ex) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Ignoring malformed AVC codec string: ");
            sb4.append(value);
            Log.w("MediaCodecUtil", sb4.toString());
            return null;
        }
    }
    
    public static Pair<Integer, Integer> getCodecProfileAndLevel(final String s) {
        if (s == null) {
            return null;
        }
        final String[] split = s.split("\\.");
        int n = 0;
        final String s2 = split[0];
        Label_0154: {
            switch (s2.hashCode()) {
                case 3356560: {
                    if (s2.equals("mp4a")) {
                        n = 4;
                        break Label_0154;
                    }
                    break;
                }
                case 3214780: {
                    if (s2.equals("hvc1")) {
                        n = 1;
                        break Label_0154;
                    }
                    break;
                }
                case 3199032: {
                    if (s2.equals("hev1")) {
                        break Label_0154;
                    }
                    break;
                }
                case 3006244: {
                    if (s2.equals("avc2")) {
                        n = 3;
                        break Label_0154;
                    }
                    break;
                }
                case 3006243: {
                    if (s2.equals("avc1")) {
                        n = 2;
                        break Label_0154;
                    }
                    break;
                }
            }
            n = -1;
        }
        if (n == 0 || n == 1) {
            return getHevcProfileAndLevel(s, split);
        }
        if (n == 2 || n == 3) {
            return getAvcProfileAndLevel(s, split);
        }
        if (n != 4) {
            return null;
        }
        return getAacCodecProfileAndLevel(s, split);
    }
    
    public static MediaCodecInfo getDecoderInfo(final String s, final boolean b) throws DecoderQueryException {
        final List<MediaCodecInfo> decoderInfos = getDecoderInfos(s, b);
        MediaCodecInfo mediaCodecInfo;
        if (decoderInfos.isEmpty()) {
            mediaCodecInfo = null;
        }
        else {
            mediaCodecInfo = decoderInfos.get(0);
        }
        return mediaCodecInfo;
    }
    
    public static List<MediaCodecInfo> getDecoderInfos(final String s, final boolean b) throws DecoderQueryException {
        synchronized (MediaCodecUtil.class) {
            final CodecKey codecKey = new CodecKey(s, b);
            final List<MediaCodecInfo> list = MediaCodecUtil.decoderInfosCache.get(codecKey);
            if (list != null) {
                return list;
            }
            MediaCodecListCompat mediaCodecListCompat;
            if (Util.SDK_INT >= 21) {
                mediaCodecListCompat = new MediaCodecListCompatV21(b);
            }
            else {
                mediaCodecListCompat = new MediaCodecListCompatV16();
            }
            final ArrayList<MediaCodecInfo> decoderInfosInternal = getDecoderInfosInternal(codecKey, mediaCodecListCompat, s);
            MediaCodecListCompat mediaCodecListCompat2 = mediaCodecListCompat;
            List<? extends MediaCodecInfo> list2 = decoderInfosInternal;
            if (b) {
                mediaCodecListCompat2 = mediaCodecListCompat;
                list2 = decoderInfosInternal;
                if (decoderInfosInternal.isEmpty()) {
                    mediaCodecListCompat2 = mediaCodecListCompat;
                    list2 = decoderInfosInternal;
                    if (21 <= Util.SDK_INT) {
                        mediaCodecListCompat2 = mediaCodecListCompat;
                        list2 = decoderInfosInternal;
                        if (Util.SDK_INT <= 23) {
                            final MediaCodecListCompatV16 mediaCodecListCompatV16 = new MediaCodecListCompatV16();
                            final ArrayList<MediaCodecInfo> decoderInfosInternal2 = getDecoderInfosInternal(codecKey, (MediaCodecListCompat)mediaCodecListCompatV16, s);
                            mediaCodecListCompat2 = (MediaCodecListCompat)mediaCodecListCompatV16;
                            list2 = decoderInfosInternal2;
                            if (!decoderInfosInternal2.isEmpty()) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("MediaCodecList API didn't list secure decoder for: ");
                                sb.append(s);
                                sb.append(". Assuming: ");
                                sb.append(decoderInfosInternal2.get(0).name);
                                Log.w("MediaCodecUtil", sb.toString());
                                list2 = decoderInfosInternal2;
                                mediaCodecListCompat2 = (MediaCodecListCompat)mediaCodecListCompatV16;
                            }
                        }
                    }
                }
            }
            if ("audio/eac3-joc".equals(s)) {
                ((ArrayList<Object>)list2).addAll(getDecoderInfosInternal(new CodecKey("audio/eac3", codecKey.secure), mediaCodecListCompat2, s));
            }
            applyWorkarounds(s, (List<MediaCodecInfo>)list2);
            final List<Object> unmodifiableList = Collections.unmodifiableList((List<?>)list2);
            MediaCodecUtil.decoderInfosCache.put(codecKey, (List<MediaCodecInfo>)unmodifiableList);
            return (List<MediaCodecInfo>)unmodifiableList;
        }
    }
    
    private static ArrayList<MediaCodecInfo> getDecoderInfosInternal(final CodecKey codecKey, final MediaCodecListCompat mediaCodecListCompat, final String s) throws DecoderQueryException {
        try {
            final ArrayList<MediaCodecInfo> list = new ArrayList<MediaCodecInfo>();
            final String mimeType = codecKey.mimeType;
            int codecCount = mediaCodecListCompat.getCodecCount();
            final boolean secureDecodersExplicit = mediaCodecListCompat.secureDecodersExplicit();
            int n;
            for (int i = 0; i < codecCount; ++i, codecCount = n) {
                final android.media.MediaCodecInfo codecInfo = mediaCodecListCompat.getCodecInfoAt(i);
                final String name = codecInfo.getName();
                n = codecCount;
                if (isCodecUsableDecoder(codecInfo, name, secureDecodersExplicit, s)) {
                    final String[] supportedTypes = codecInfo.getSupportedTypes();
                    final int length = supportedTypes.length;
                    int n2 = 0;
                    while (true) {
                        n = codecCount;
                        if (n2 >= length) {
                            break;
                        }
                        final String str = supportedTypes[n2];
                        Label_0396: {
                            if (str.equalsIgnoreCase(mimeType)) {
                                Object capabilitiesForType = null;
                                Label_0270: {
                                    try {
                                        capabilitiesForType = codecInfo.getCapabilitiesForType(str);
                                        final boolean securePlaybackSupported = mediaCodecListCompat.isSecurePlaybackSupported(mimeType, (MediaCodecInfo$CodecCapabilities)capabilitiesForType);
                                        final boolean codecNeedsDisableAdaptationWorkaround = codecNeedsDisableAdaptationWorkaround(name);
                                        while (true) {
                                            Label_0174: {
                                                if (!secureDecodersExplicit) {
                                                    break Label_0174;
                                                }
                                                Label_0189: {
                                                    try {
                                                        if (codecKey.secure == securePlaybackSupported) {
                                                            break Label_0189;
                                                        }
                                                    }
                                                    catch (Exception capabilitiesForType) {
                                                        break Label_0270;
                                                    }
                                                    break Label_0174;
                                                }
                                                list.add(MediaCodecInfo.newInstance(name, mimeType, (MediaCodecInfo$CodecCapabilities)capabilitiesForType, codecNeedsDisableAdaptationWorkaround, false));
                                                break Label_0396;
                                            }
                                            if (!secureDecodersExplicit && !codecKey.secure) {
                                                continue;
                                            }
                                            break;
                                        }
                                        if (!secureDecodersExplicit && securePlaybackSupported) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append(name);
                                            sb.append(".secure");
                                            list.add(MediaCodecInfo.newInstance(sb.toString(), mimeType, (MediaCodecInfo$CodecCapabilities)capabilitiesForType, codecNeedsDisableAdaptationWorkaround, true));
                                            return list;
                                        }
                                        break Label_0396;
                                    }
                                    catch (Exception ex2) {}
                                }
                                if (Util.SDK_INT > 23 || list.isEmpty()) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("Failed to query codec ");
                                    sb2.append(name);
                                    sb2.append(" (");
                                    sb2.append(str);
                                    sb2.append(")");
                                    Log.e("MediaCodecUtil", sb2.toString());
                                    throw capabilitiesForType;
                                }
                                capabilitiesForType = new StringBuilder();
                                ((StringBuilder)capabilitiesForType).append("Skipping codec ");
                                ((StringBuilder)capabilitiesForType).append(name);
                                ((StringBuilder)capabilitiesForType).append(" (failed to query capabilities)");
                                Log.e("MediaCodecUtil", ((StringBuilder)capabilitiesForType).toString());
                            }
                        }
                        ++n2;
                    }
                }
            }
            return list;
        }
        catch (Exception ex) {
            throw new DecoderQueryException((Throwable)ex);
        }
    }
    
    private static Pair<Integer, Integer> getHevcProfileAndLevel(String group, final String[] array) {
        if (array.length < 4) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Ignoring malformed HEVC codec string: ");
            sb.append(group);
            Log.w("MediaCodecUtil", sb.toString());
            return null;
        }
        final Matcher matcher = MediaCodecUtil.PROFILE_PATTERN.matcher(array[1]);
        if (!matcher.matches()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Ignoring malformed HEVC codec string: ");
            sb2.append(group);
            Log.w("MediaCodecUtil", sb2.toString());
            return null;
        }
        group = matcher.group(1);
        int i;
        if ("1".equals(group)) {
            i = 1;
        }
        else {
            if (!"2".equals(group)) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Unknown HEVC profile string: ");
                sb3.append(group);
                Log.w("MediaCodecUtil", sb3.toString());
                return null;
            }
            i = 2;
        }
        final Integer n = MediaCodecUtil.HEVC_CODEC_STRING_TO_PROFILE_LEVEL.get(array[3]);
        if (n == null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Unknown HEVC level string: ");
            sb4.append(matcher.group(1));
            Log.w("MediaCodecUtil", sb4.toString());
            return null;
        }
        return (Pair<Integer, Integer>)new Pair((Object)i, (Object)n);
    }
    
    public static MediaCodecInfo getPassthroughDecoderInfo() throws DecoderQueryException {
        final MediaCodecInfo decoderInfo = getDecoderInfo("audio/raw", false);
        MediaCodecInfo passthroughInstance;
        if (decoderInfo == null) {
            passthroughInstance = null;
        }
        else {
            passthroughInstance = MediaCodecInfo.newPassthroughInstance(decoderInfo.name);
        }
        return passthroughInstance;
    }
    
    private static boolean isCodecUsableDecoder(final android.media.MediaCodecInfo mediaCodecInfo, final String anObject, final boolean b, final String anObject2) {
        return !mediaCodecInfo.isEncoder() && (b || !anObject.endsWith(".secure")) && (Util.SDK_INT >= 21 || (!"CIPAACDecoder".equals(anObject) && !"CIPMP3Decoder".equals(anObject) && !"CIPVorbisDecoder".equals(anObject) && !"CIPAMRNBDecoder".equals(anObject) && !"AACDecoder".equals(anObject) && !"MP3Decoder".equals(anObject))) && (Util.SDK_INT >= 18 || !"OMX.SEC.MP3.Decoder".equals(anObject)) && (!"OMX.SEC.mp3.dec".equals(anObject) || (!Util.MODEL.startsWith("GT-I9152") && !Util.MODEL.startsWith("GT-I9515") && !Util.MODEL.startsWith("GT-P5220") && !Util.MODEL.startsWith("GT-S7580") && !Util.MODEL.startsWith("SM-G350") && !Util.MODEL.startsWith("SM-G386") && !Util.MODEL.startsWith("SM-T231") && !Util.MODEL.startsWith("SM-T530") && !Util.MODEL.startsWith("SCH-I535") && !Util.MODEL.startsWith("SPH-L710"))) && (!"OMX.brcm.audio.mp3.decoder".equals(anObject) || (!Util.MODEL.startsWith("GT-I9152") && !Util.MODEL.startsWith("GT-S7580") && !Util.MODEL.startsWith("SM-G350"))) && (Util.SDK_INT >= 18 || !"OMX.MTK.AUDIO.DECODER.AAC".equals(anObject) || (!"a70".equals(Util.DEVICE) && (!"Xiaomi".equals(Util.MANUFACTURER) || !Util.DEVICE.startsWith("HM")))) && (Util.SDK_INT != 16 || !"OMX.qcom.audio.decoder.mp3".equals(anObject) || (!"dlxu".equals(Util.DEVICE) && !"protou".equals(Util.DEVICE) && !"ville".equals(Util.DEVICE) && !"villeplus".equals(Util.DEVICE) && !"villec2".equals(Util.DEVICE) && !Util.DEVICE.startsWith("gee") && !"C6602".equals(Util.DEVICE) && !"C6603".equals(Util.DEVICE) && !"C6606".equals(Util.DEVICE) && !"C6616".equals(Util.DEVICE) && !"L36h".equals(Util.DEVICE) && !"SO-02E".equals(Util.DEVICE))) && (Util.SDK_INT != 16 || !"OMX.qcom.audio.decoder.aac".equals(anObject) || (!"C1504".equals(Util.DEVICE) && !"C1505".equals(Util.DEVICE) && !"C1604".equals(Util.DEVICE) && !"C1605".equals(Util.DEVICE))) && (Util.SDK_INT >= 24 || (!"OMX.SEC.aac.dec".equals(anObject) && !"OMX.Exynos.AAC.Decoder".equals(anObject)) || !"samsung".equals(Util.MANUFACTURER) || (!Util.DEVICE.startsWith("zeroflte") && !Util.DEVICE.startsWith("zerolte") && !Util.DEVICE.startsWith("zenlte") && !"SC-05G".equals(Util.DEVICE) && !"marinelteatt".equals(Util.DEVICE) && !"404SC".equals(Util.DEVICE) && !"SC-04G".equals(Util.DEVICE) && !"SCV31".equals(Util.DEVICE))) && (Util.SDK_INT > 19 || !"OMX.SEC.vp8.dec".equals(anObject) || !"samsung".equals(Util.MANUFACTURER) || (!Util.DEVICE.startsWith("d2") && !Util.DEVICE.startsWith("serrano") && !Util.DEVICE.startsWith("jflte") && !Util.DEVICE.startsWith("santos") && !Util.DEVICE.startsWith("t0"))) && (Util.SDK_INT > 19 || !Util.DEVICE.startsWith("jflte") || !"OMX.qcom.video.decoder.vp8".equals(anObject)) && (!"audio/eac3-joc".equals(anObject2) || !"OMX.MTK.AUDIO.DECODER.DSPAC3".equals(anObject));
    }
    
    public static int maxH264DecodableFrameSize() throws DecoderQueryException {
        if (MediaCodecUtil.maxH264DecodableFrameSize == -1) {
            int max = 0;
            int i = 0;
            final MediaCodecInfo decoderInfo = getDecoderInfo("video/avc", false);
            if (decoderInfo != null) {
                final MediaCodecInfo$CodecProfileLevel[] profileLevels = decoderInfo.getProfileLevels();
                final int length = profileLevels.length;
                int max2 = 0;
                while (i < length) {
                    max2 = Math.max(avcLevelToMaxFrameSize(profileLevels[i].level), max2);
                    ++i;
                }
                int b;
                if (Util.SDK_INT >= 21) {
                    b = 345600;
                }
                else {
                    b = 172800;
                }
                max = Math.max(max2, b);
            }
            MediaCodecUtil.maxH264DecodableFrameSize = max;
        }
        return MediaCodecUtil.maxH264DecodableFrameSize;
    }
    
    private static final class CodecKey
    {
        public final String mimeType;
        public final boolean secure;
        
        public CodecKey(final String mimeType, final boolean secure) {
            this.mimeType = mimeType;
            this.secure = secure;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && o.getClass() == CodecKey.class) {
                final CodecKey codecKey = (CodecKey)o;
                if (!TextUtils.equals((CharSequence)this.mimeType, (CharSequence)codecKey.mimeType) || this.secure != codecKey.secure) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            final String mimeType = this.mimeType;
            int hashCode;
            if (mimeType == null) {
                hashCode = 0;
            }
            else {
                hashCode = mimeType.hashCode();
            }
            int n;
            if (this.secure) {
                n = 1231;
            }
            else {
                n = 1237;
            }
            return (hashCode + 31) * 31 + n;
        }
    }
    
    public static class DecoderQueryException extends Exception
    {
        private DecoderQueryException(final Throwable cause) {
            super("Failed to query underlying media codecs", cause);
        }
    }
    
    private interface MediaCodecListCompat
    {
        int getCodecCount();
        
        android.media.MediaCodecInfo getCodecInfoAt(final int p0);
        
        boolean isSecurePlaybackSupported(final String p0, final MediaCodecInfo$CodecCapabilities p1);
        
        boolean secureDecodersExplicit();
    }
    
    private static final class MediaCodecListCompatV16 implements MediaCodecListCompat
    {
        @Override
        public int getCodecCount() {
            return MediaCodecList.getCodecCount();
        }
        
        @Override
        public android.media.MediaCodecInfo getCodecInfoAt(final int n) {
            return MediaCodecList.getCodecInfoAt(n);
        }
        
        @Override
        public boolean isSecurePlaybackSupported(final String anObject, final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
            return "video/avc".equals(anObject);
        }
        
        @Override
        public boolean secureDecodersExplicit() {
            return false;
        }
    }
    
    @TargetApi(21)
    private static final class MediaCodecListCompatV21 implements MediaCodecListCompat
    {
        private final int codecKind;
        private android.media.MediaCodecInfo[] mediaCodecInfos;
        
        public MediaCodecListCompatV21(final boolean codecKind) {
            this.codecKind = (codecKind ? 1 : 0);
        }
        
        private void ensureMediaCodecInfosInitialized() {
            if (this.mediaCodecInfos == null) {
                this.mediaCodecInfos = new MediaCodecList(this.codecKind).getCodecInfos();
            }
        }
        
        @Override
        public int getCodecCount() {
            this.ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos.length;
        }
        
        @Override
        public android.media.MediaCodecInfo getCodecInfoAt(final int n) {
            this.ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos[n];
        }
        
        @Override
        public boolean isSecurePlaybackSupported(final String s, final MediaCodecInfo$CodecCapabilities mediaCodecInfo$CodecCapabilities) {
            return mediaCodecInfo$CodecCapabilities.isFeatureSupported("secure-playback");
        }
        
        @Override
        public boolean secureDecodersExplicit() {
            return true;
        }
    }
    
    private static final class RawAudioCodecComparator implements Comparator<MediaCodecInfo>
    {
        private static int scoreMediaCodecInfo(final MediaCodecInfo mediaCodecInfo) {
            final String name = mediaCodecInfo.name;
            if (name.startsWith("OMX.google") || name.startsWith("c2.android")) {
                return -1;
            }
            if (Util.SDK_INT < 26 && name.equals("OMX.MTK.AUDIO.DECODER.RAW")) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public int compare(final MediaCodecInfo mediaCodecInfo, final MediaCodecInfo mediaCodecInfo2) {
            return scoreMediaCodecInfo(mediaCodecInfo) - scoreMediaCodecInfo(mediaCodecInfo2);
        }
    }
}
