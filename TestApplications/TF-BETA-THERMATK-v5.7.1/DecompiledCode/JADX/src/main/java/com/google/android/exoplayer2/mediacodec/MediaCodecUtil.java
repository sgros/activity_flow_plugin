package com.google.android.exoplayer2.mediacodec;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecList;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseIntArray;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;

@SuppressLint({"InlinedApi"})
@TargetApi(16)
public final class MediaCodecUtil {
    private static final SparseIntArray AVC_LEVEL_NUMBER_TO_CONST = new SparseIntArray();
    private static final SparseIntArray AVC_PROFILE_NUMBER_TO_CONST = new SparseIntArray();
    private static final Map<String, Integer> HEVC_CODEC_STRING_TO_PROFILE_LEVEL = new HashMap();
    private static final SparseIntArray MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE = new SparseIntArray();
    private static final Pattern PROFILE_PATTERN = Pattern.compile("^\\D?(\\d+)$");
    private static final RawAudioCodecComparator RAW_AUDIO_CODEC_COMPARATOR = new RawAudioCodecComparator();
    private static final HashMap<CodecKey, List<MediaCodecInfo>> decoderInfosCache = new HashMap();
    private static int maxH264DecodableFrameSize = -1;

    private static final class CodecKey {
        public final String mimeType;
        public final boolean secure;

        public CodecKey(String str, boolean z) {
            this.mimeType = str;
            this.secure = z;
        }

        public int hashCode() {
            String str = this.mimeType;
            return (((str == null ? 0 : str.hashCode()) + 31) * 31) + (this.secure ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != CodecKey.class) {
                return false;
            }
            CodecKey codecKey = (CodecKey) obj;
            if (!(TextUtils.equals(this.mimeType, codecKey.mimeType) && this.secure == codecKey.secure)) {
                z = false;
            }
            return z;
        }
    }

    public static class DecoderQueryException extends Exception {
        private DecoderQueryException(Throwable th) {
            super("Failed to query underlying media codecs", th);
        }
    }

    private interface MediaCodecListCompat {
        int getCodecCount();

        MediaCodecInfo getCodecInfoAt(int i);

        boolean isSecurePlaybackSupported(String str, CodecCapabilities codecCapabilities);

        boolean secureDecodersExplicit();
    }

    private static final class RawAudioCodecComparator implements Comparator<MediaCodecInfo> {
        private RawAudioCodecComparator() {
        }

        public int compare(MediaCodecInfo mediaCodecInfo, MediaCodecInfo mediaCodecInfo2) {
            return scoreMediaCodecInfo(mediaCodecInfo) - scoreMediaCodecInfo(mediaCodecInfo2);
        }

        private static int scoreMediaCodecInfo(MediaCodecInfo mediaCodecInfo) {
            String str = mediaCodecInfo.name;
            if (str.startsWith("OMX.google") || str.startsWith("c2.android")) {
                return -1;
            }
            return (Util.SDK_INT >= 26 || !str.equals("OMX.MTK.AUDIO.DECODER.RAW")) ? 0 : 1;
        }
    }

    private static final class MediaCodecListCompatV16 implements MediaCodecListCompat {
        public boolean secureDecodersExplicit() {
            return false;
        }

        private MediaCodecListCompatV16() {
        }

        public int getCodecCount() {
            return MediaCodecList.getCodecCount();
        }

        public MediaCodecInfo getCodecInfoAt(int i) {
            return MediaCodecList.getCodecInfoAt(i);
        }

        public boolean isSecurePlaybackSupported(String str, CodecCapabilities codecCapabilities) {
            return "video/avc".equals(str);
        }
    }

    @TargetApi(21)
    private static final class MediaCodecListCompatV21 implements MediaCodecListCompat {
        private final int codecKind;
        private MediaCodecInfo[] mediaCodecInfos;

        public boolean secureDecodersExplicit() {
            return true;
        }

        public MediaCodecListCompatV21(boolean z) {
            this.codecKind = z;
        }

        public int getCodecCount() {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos.length;
        }

        public MediaCodecInfo getCodecInfoAt(int i) {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos[i];
        }

        public boolean isSecurePlaybackSupported(String str, CodecCapabilities codecCapabilities) {
            return codecCapabilities.isFeatureSupported("secure-playback");
        }

        private void ensureMediaCodecInfosInitialized() {
            if (this.mediaCodecInfos == null) {
                this.mediaCodecInfos = new MediaCodecList(this.codecKind).getCodecInfos();
            }
        }
    }

    private static int avcLevelToMaxFrameSize(int i) {
        if (i == 1 || i == 2) {
            return 25344;
        }
        switch (i) {
            case 8:
            case 16:
            case 32:
                return 101376;
            case 64:
                return 202752;
            case 128:
            case 256:
                return 414720;
            case 512:
                return 921600;
            case 1024:
                return 1310720;
            case 2048:
            case 4096:
                return 2097152;
            case MessagesController.UPDATE_MASK_CHAT /*8192*/:
                return 2228224;
            case 16384:
                return 5652480;
            case 32768:
            case MessagesController.UPDATE_MASK_CHECK /*65536*/:
                return 9437184;
            default:
                return -1;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:51:0x00e9 in {17, 20, 21, 25, 26, 31, 33, 41, 43, 44, 45, 46, 47, 50} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private static java.util.ArrayList<com.google.android.exoplayer2.mediacodec.MediaCodecInfo> getDecoderInfosInternal(com.google.android.exoplayer2.mediacodec.MediaCodecUtil.CodecKey r17, com.google.android.exoplayer2.mediacodec.MediaCodecUtil.MediaCodecListCompat r18, java.lang.String r19) throws com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException {
        /*
        r1 = r17;
        r2 = r18;
        r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x00e1 }
        r3.<init>();	 Catch:{ Exception -> 0x00e1 }
        r4 = r1.mimeType;	 Catch:{ Exception -> 0x00e1 }
        r5 = r18.getCodecCount();	 Catch:{ Exception -> 0x00e1 }
        r6 = r18.secureDecodersExplicit();	 Catch:{ Exception -> 0x00e1 }
        r8 = 0;	 Catch:{ Exception -> 0x00e1 }
        if (r8 >= r5) goto L_0x00e0;	 Catch:{ Exception -> 0x00e1 }
        r9 = r2.getCodecInfoAt(r8);	 Catch:{ Exception -> 0x00e1 }
        r10 = r9.getName();	 Catch:{ Exception -> 0x00e1 }
        r11 = r19;	 Catch:{ Exception -> 0x00e1 }
        r0 = isCodecUsableDecoder(r9, r10, r6, r11);	 Catch:{ Exception -> 0x00e1 }
        if (r0 == 0) goto L_0x00d6;	 Catch:{ Exception -> 0x00e1 }
        r12 = r9.getSupportedTypes();	 Catch:{ Exception -> 0x00e1 }
        r13 = r12.length;	 Catch:{ Exception -> 0x00e1 }
        r14 = 0;	 Catch:{ Exception -> 0x00e1 }
        if (r14 >= r13) goto L_0x00d6;	 Catch:{ Exception -> 0x00e1 }
        r15 = r12[r14];	 Catch:{ Exception -> 0x00e1 }
        r0 = r15.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x00e1 }
        if (r0 == 0) goto L_0x00cc;
        r0 = r9.getCapabilitiesForType(r15);	 Catch:{ Exception -> 0x007f }
        r7 = r2.isSecurePlaybackSupported(r4, r0);	 Catch:{ Exception -> 0x007f }
        r2 = codecNeedsDisableAdaptationWorkaround(r10);	 Catch:{ Exception -> 0x007f }
        if (r6 == 0) goto L_0x004f;
        r16 = r5;
        r5 = r1.secure;	 Catch:{ Exception -> 0x004d }
        if (r5 == r7) goto L_0x004b;	 Catch:{ Exception -> 0x004d }
        goto L_0x0051;	 Catch:{ Exception -> 0x004d }
        r5 = 0;	 Catch:{ Exception -> 0x004d }
        goto L_0x0058;	 Catch:{ Exception -> 0x004d }
        r0 = move-exception;	 Catch:{ Exception -> 0x004d }
        goto L_0x0082;	 Catch:{ Exception -> 0x004d }
        r16 = r5;	 Catch:{ Exception -> 0x004d }
        if (r6 != 0) goto L_0x0060;	 Catch:{ Exception -> 0x004d }
        r5 = r1.secure;	 Catch:{ Exception -> 0x004d }
        if (r5 != 0) goto L_0x0060;	 Catch:{ Exception -> 0x004d }
        goto L_0x004b;	 Catch:{ Exception -> 0x004d }
        r0 = com.google.android.exoplayer2.mediacodec.MediaCodecInfo.newInstance(r10, r4, r0, r2, r5);	 Catch:{ Exception -> 0x004d }
        r3.add(r0);	 Catch:{ Exception -> 0x004d }
        goto L_0x00ce;	 Catch:{ Exception -> 0x004d }
        r5 = 0;	 Catch:{ Exception -> 0x004d }
        if (r6 != 0) goto L_0x00ce;	 Catch:{ Exception -> 0x004d }
        if (r7 == 0) goto L_0x00ce;	 Catch:{ Exception -> 0x004d }
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x004d }
        r7.<init>();	 Catch:{ Exception -> 0x004d }
        r7.append(r10);	 Catch:{ Exception -> 0x004d }
        r5 = ".secure";	 Catch:{ Exception -> 0x004d }
        r7.append(r5);	 Catch:{ Exception -> 0x004d }
        r5 = r7.toString();	 Catch:{ Exception -> 0x004d }
        r7 = 1;	 Catch:{ Exception -> 0x004d }
        r0 = com.google.android.exoplayer2.mediacodec.MediaCodecInfo.newInstance(r5, r4, r0, r2, r7);	 Catch:{ Exception -> 0x004d }
        r3.add(r0);	 Catch:{ Exception -> 0x004d }
        return r3;
        r0 = move-exception;
        r16 = r5;
        r2 = com.google.android.exoplayer2.util.Util.SDK_INT;	 Catch:{ Exception -> 0x00e1 }
        r5 = 23;
        r7 = "MediaCodecUtil";
        if (r2 > r5) goto L_0x00aa;
        r2 = r3.isEmpty();	 Catch:{ Exception -> 0x00e1 }
        if (r2 != 0) goto L_0x00aa;	 Catch:{ Exception -> 0x00e1 }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e1 }
        r0.<init>();	 Catch:{ Exception -> 0x00e1 }
        r2 = "Skipping codec ";	 Catch:{ Exception -> 0x00e1 }
        r0.append(r2);	 Catch:{ Exception -> 0x00e1 }
        r0.append(r10);	 Catch:{ Exception -> 0x00e1 }
        r2 = " (failed to query capabilities)";	 Catch:{ Exception -> 0x00e1 }
        r0.append(r2);	 Catch:{ Exception -> 0x00e1 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x00e1 }
        com.google.android.exoplayer2.util.Log.m14e(r7, r0);	 Catch:{ Exception -> 0x00e1 }
        goto L_0x00ce;	 Catch:{ Exception -> 0x00e1 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e1 }
        r1.<init>();	 Catch:{ Exception -> 0x00e1 }
        r2 = "Failed to query codec ";	 Catch:{ Exception -> 0x00e1 }
        r1.append(r2);	 Catch:{ Exception -> 0x00e1 }
        r1.append(r10);	 Catch:{ Exception -> 0x00e1 }
        r2 = " (";	 Catch:{ Exception -> 0x00e1 }
        r1.append(r2);	 Catch:{ Exception -> 0x00e1 }
        r1.append(r15);	 Catch:{ Exception -> 0x00e1 }
        r2 = ")";	 Catch:{ Exception -> 0x00e1 }
        r1.append(r2);	 Catch:{ Exception -> 0x00e1 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00e1 }
        com.google.android.exoplayer2.util.Log.m14e(r7, r1);	 Catch:{ Exception -> 0x00e1 }
        throw r0;	 Catch:{ Exception -> 0x00e1 }
        r16 = r5;
        r14 = r14 + 1;
        r2 = r18;
        r5 = r16;
        goto L_0x002c;
        r16 = r5;
        r8 = r8 + 1;
        r2 = r18;
        r5 = r16;
        goto L_0x0014;
        return r3;
        r0 = move-exception;
        r1 = new com.google.android.exoplayer2.mediacodec.MediaCodecUtil$DecoderQueryException;
        r2 = 0;
        r1.<init>(r0);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.getDecoderInfosInternal(com.google.android.exoplayer2.mediacodec.MediaCodecUtil$CodecKey, com.google.android.exoplayer2.mediacodec.MediaCodecUtil$MediaCodecListCompat, java.lang.String):java.util.ArrayList");
    }

    static {
        AVC_PROFILE_NUMBER_TO_CONST.put(66, 1);
        AVC_PROFILE_NUMBER_TO_CONST.put(77, 2);
        AVC_PROFILE_NUMBER_TO_CONST.put(88, 4);
        AVC_PROFILE_NUMBER_TO_CONST.put(100, 8);
        AVC_PROFILE_NUMBER_TO_CONST.put(110, 16);
        AVC_PROFILE_NUMBER_TO_CONST.put(122, 32);
        AVC_PROFILE_NUMBER_TO_CONST.put(244, 64);
        AVC_LEVEL_NUMBER_TO_CONST.put(10, 1);
        AVC_LEVEL_NUMBER_TO_CONST.put(11, 4);
        AVC_LEVEL_NUMBER_TO_CONST.put(12, 8);
        AVC_LEVEL_NUMBER_TO_CONST.put(13, 16);
        AVC_LEVEL_NUMBER_TO_CONST.put(20, 32);
        AVC_LEVEL_NUMBER_TO_CONST.put(21, 64);
        AVC_LEVEL_NUMBER_TO_CONST.put(22, 128);
        AVC_LEVEL_NUMBER_TO_CONST.put(30, 256);
        AVC_LEVEL_NUMBER_TO_CONST.put(31, 512);
        AVC_LEVEL_NUMBER_TO_CONST.put(32, 1024);
        AVC_LEVEL_NUMBER_TO_CONST.put(40, 2048);
        AVC_LEVEL_NUMBER_TO_CONST.put(41, 4096);
        AVC_LEVEL_NUMBER_TO_CONST.put(42, MessagesController.UPDATE_MASK_CHAT);
        AVC_LEVEL_NUMBER_TO_CONST.put(50, 16384);
        AVC_LEVEL_NUMBER_TO_CONST.put(51, 32768);
        AVC_LEVEL_NUMBER_TO_CONST.put(52, MessagesController.UPDATE_MASK_CHECK);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L30", Integer.valueOf(1));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L60", Integer.valueOf(4));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L63", Integer.valueOf(16));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L90", Integer.valueOf(64));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L93", Integer.valueOf(256));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L120", Integer.valueOf(1024));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L123", Integer.valueOf(4096));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L150", Integer.valueOf(16384));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L153", Integer.valueOf(MessagesController.UPDATE_MASK_CHECK));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L156", Integer.valueOf(262144));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L180", Integer.valueOf(1048576));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L183", Integer.valueOf(4194304));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L186", Integer.valueOf(ConnectionsManager.FileTypePhoto));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H30", Integer.valueOf(2));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H60", Integer.valueOf(8));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H63", Integer.valueOf(32));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H90", Integer.valueOf(128));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H93", Integer.valueOf(512));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H120", Integer.valueOf(2048));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H123", Integer.valueOf(MessagesController.UPDATE_MASK_CHAT));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H150", Integer.valueOf(32768));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H153", Integer.valueOf(MessagesController.UPDATE_MASK_REORDER));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H156", Integer.valueOf(524288));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H180", Integer.valueOf(2097152));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H183", Integer.valueOf(8388608));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H186", Integer.valueOf(ConnectionsManager.FileTypeVideo));
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(1, 1);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(2, 2);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(3, 3);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(4, 4);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(5, 5);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(6, 6);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(17, 17);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(20, 20);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(23, 23);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(29, 29);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(39, 39);
        MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(42, 42);
    }

    public static MediaCodecInfo getPassthroughDecoderInfo() throws DecoderQueryException {
        MediaCodecInfo decoderInfo = getDecoderInfo(MimeTypes.AUDIO_RAW, false);
        if (decoderInfo == null) {
            return null;
        }
        return MediaCodecInfo.newPassthroughInstance(decoderInfo.name);
    }

    public static MediaCodecInfo getDecoderInfo(String str, boolean z) throws DecoderQueryException {
        List decoderInfos = getDecoderInfos(str, z);
        return decoderInfos.isEmpty() ? null : (MediaCodecInfo) decoderInfos.get(0);
    }

    public static synchronized List<MediaCodecInfo> getDecoderInfos(String str, boolean z) throws DecoderQueryException {
        synchronized (MediaCodecUtil.class) {
            CodecKey codecKey = new CodecKey(str, z);
            List list = (List) decoderInfosCache.get(codecKey);
            if (list != null) {
                return list;
            }
            MediaCodecListCompat mediaCodecListCompatV21 = Util.SDK_INT >= 21 ? new MediaCodecListCompatV21(z) : new MediaCodecListCompatV16();
            ArrayList decoderInfosInternal = getDecoderInfosInternal(codecKey, mediaCodecListCompatV21, str);
            if (z && decoderInfosInternal.isEmpty() && 21 <= Util.SDK_INT && Util.SDK_INT <= 23) {
                mediaCodecListCompatV21 = new MediaCodecListCompatV16();
                decoderInfosInternal = getDecoderInfosInternal(codecKey, mediaCodecListCompatV21, str);
                if (!decoderInfosInternal.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("MediaCodecList API didn't list secure decoder for: ");
                    stringBuilder.append(str);
                    stringBuilder.append(". Assuming: ");
                    stringBuilder.append(((MediaCodecInfo) decoderInfosInternal.get(0)).name);
                    Log.m18w("MediaCodecUtil", stringBuilder.toString());
                }
            }
            if (MimeTypes.AUDIO_E_AC3_JOC.equals(str)) {
                decoderInfosInternal.addAll(getDecoderInfosInternal(new CodecKey(MimeTypes.AUDIO_E_AC3, codecKey.secure), mediaCodecListCompatV21, str));
            }
            applyWorkarounds(str, decoderInfosInternal);
            List unmodifiableList = Collections.unmodifiableList(decoderInfosInternal);
            decoderInfosCache.put(codecKey, unmodifiableList);
            return unmodifiableList;
        }
    }

    public static int maxH264DecodableFrameSize() throws DecoderQueryException {
        if (maxH264DecodableFrameSize == -1) {
            int i = 0;
            MediaCodecInfo decoderInfo = getDecoderInfo("video/avc", false);
            if (decoderInfo != null) {
                CodecProfileLevel[] profileLevels = decoderInfo.getProfileLevels();
                int length = profileLevels.length;
                int i2 = 0;
                while (i < length) {
                    i2 = Math.max(avcLevelToMaxFrameSize(profileLevels[i].level), i2);
                    i++;
                }
                i = Math.max(i2, Util.SDK_INT >= 21 ? 345600 : 172800);
            }
            maxH264DecodableFrameSize = i;
        }
        return maxH264DecodableFrameSize;
    }

    /* JADX WARNING: Missing block: B:12:0x0034, code skipped:
            if (r3.equals("hev1") != false) goto L_0x004c;
     */
    public static android.util.Pair<java.lang.Integer, java.lang.Integer> getCodecProfileAndLevel(java.lang.String r10) {
        /*
        r0 = 0;
        if (r10 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = "\\.";
        r1 = r10.split(r1);
        r2 = 0;
        r3 = r1[r2];
        r4 = -1;
        r5 = r3.hashCode();
        r6 = 4;
        r7 = 3;
        r8 = 2;
        r9 = 1;
        switch(r5) {
            case 3006243: goto L_0x0041;
            case 3006244: goto L_0x0037;
            case 3199032: goto L_0x002e;
            case 3214780: goto L_0x0024;
            case 3356560: goto L_0x001a;
            default: goto L_0x0019;
        };
    L_0x0019:
        goto L_0x004b;
    L_0x001a:
        r2 = "mp4a";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x004b;
    L_0x0022:
        r2 = 4;
        goto L_0x004c;
    L_0x0024:
        r2 = "hvc1";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x004b;
    L_0x002c:
        r2 = 1;
        goto L_0x004c;
    L_0x002e:
        r5 = "hev1";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x004b;
    L_0x0036:
        goto L_0x004c;
    L_0x0037:
        r2 = "avc2";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x004b;
    L_0x003f:
        r2 = 3;
        goto L_0x004c;
    L_0x0041:
        r2 = "avc1";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x004b;
    L_0x0049:
        r2 = 2;
        goto L_0x004c;
    L_0x004b:
        r2 = -1;
    L_0x004c:
        if (r2 == 0) goto L_0x0061;
    L_0x004e:
        if (r2 == r9) goto L_0x0061;
    L_0x0050:
        if (r2 == r8) goto L_0x005c;
    L_0x0052:
        if (r2 == r7) goto L_0x005c;
    L_0x0054:
        if (r2 == r6) goto L_0x0057;
    L_0x0056:
        return r0;
    L_0x0057:
        r10 = getAacCodecProfileAndLevel(r10, r1);
        return r10;
    L_0x005c:
        r10 = getAvcProfileAndLevel(r10, r1);
        return r10;
    L_0x0061:
        r10 = getHevcProfileAndLevel(r10, r1);
        return r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.getCodecProfileAndLevel(java.lang.String):android.util.Pair");
    }

    /* JADX WARNING: Missing block: B:66:0x010f, code skipped:
            if (com.google.android.exoplayer2.util.Util.DEVICE.startsWith("HM") != false) goto L_0x0111;
     */
    /* JADX WARNING: Missing block: B:95:0x0196, code skipped:
            if ("SO-02E".equals(com.google.android.exoplayer2.util.Util.DEVICE) != false) goto L_0x0198;
     */
    /* JADX WARNING: Missing block: B:108:0x01cb, code skipped:
            if ("C1605".equals(com.google.android.exoplayer2.util.Util.DEVICE) != false) goto L_0x01cd;
     */
    /* JADX WARNING: Missing block: B:133:0x023c, code skipped:
            if ("SCV31".equals(com.google.android.exoplayer2.util.Util.DEVICE) != false) goto L_0x023e;
     */
    private static boolean isCodecUsableDecoder(android.media.MediaCodecInfo r5, java.lang.String r6, boolean r7, java.lang.String r8) {
        /*
        r5 = r5.isEncoder();
        r0 = 0;
        if (r5 != 0) goto L_0x02b0;
    L_0x0007:
        if (r7 != 0) goto L_0x0013;
    L_0x0009:
        r5 = ".secure";
        r5 = r6.endsWith(r5);
        if (r5 == 0) goto L_0x0013;
    L_0x0011:
        goto L_0x02b0;
    L_0x0013:
        r5 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r7 = 21;
        if (r5 >= r7) goto L_0x004a;
    L_0x0019:
        r5 = "CIPAACDecoder";
        r5 = r5.equals(r6);
        if (r5 != 0) goto L_0x0049;
    L_0x0021:
        r5 = "CIPMP3Decoder";
        r5 = r5.equals(r6);
        if (r5 != 0) goto L_0x0049;
    L_0x0029:
        r5 = "CIPVorbisDecoder";
        r5 = r5.equals(r6);
        if (r5 != 0) goto L_0x0049;
    L_0x0031:
        r5 = "CIPAMRNBDecoder";
        r5 = r5.equals(r6);
        if (r5 != 0) goto L_0x0049;
    L_0x0039:
        r5 = "AACDecoder";
        r5 = r5.equals(r6);
        if (r5 != 0) goto L_0x0049;
    L_0x0041:
        r5 = "MP3Decoder";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x004a;
    L_0x0049:
        return r0;
    L_0x004a:
        r5 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r7 = 18;
        if (r5 >= r7) goto L_0x0059;
    L_0x0050:
        r5 = "OMX.SEC.MP3.Decoder";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x0059;
    L_0x0058:
        return r0;
    L_0x0059:
        r5 = "OMX.SEC.mp3.dec";
        r5 = r5.equals(r6);
        r1 = "SM-G350";
        r2 = "GT-S7580";
        r3 = "GT-I9152";
        if (r5 == 0) goto L_0x00c6;
    L_0x0067:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r5 = r5.startsWith(r3);
        if (r5 != 0) goto L_0x00c5;
    L_0x006f:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r4 = "GT-I9515";
        r5 = r5.startsWith(r4);
        if (r5 != 0) goto L_0x00c5;
    L_0x0079:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r4 = "GT-P5220";
        r5 = r5.startsWith(r4);
        if (r5 != 0) goto L_0x00c5;
    L_0x0083:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r5 = r5.startsWith(r2);
        if (r5 != 0) goto L_0x00c5;
    L_0x008b:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r5 = r5.startsWith(r1);
        if (r5 != 0) goto L_0x00c5;
    L_0x0093:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r4 = "SM-G386";
        r5 = r5.startsWith(r4);
        if (r5 != 0) goto L_0x00c5;
    L_0x009d:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r4 = "SM-T231";
        r5 = r5.startsWith(r4);
        if (r5 != 0) goto L_0x00c5;
    L_0x00a7:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r4 = "SM-T530";
        r5 = r5.startsWith(r4);
        if (r5 != 0) goto L_0x00c5;
    L_0x00b1:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r4 = "SCH-I535";
        r5 = r5.startsWith(r4);
        if (r5 != 0) goto L_0x00c5;
    L_0x00bb:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r4 = "SPH-L710";
        r5 = r5.startsWith(r4);
        if (r5 == 0) goto L_0x00c6;
    L_0x00c5:
        return r0;
    L_0x00c6:
        r5 = "OMX.brcm.audio.mp3.decoder";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x00e7;
    L_0x00ce:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r5 = r5.startsWith(r3);
        if (r5 != 0) goto L_0x00e6;
    L_0x00d6:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r5 = r5.startsWith(r2);
        if (r5 != 0) goto L_0x00e6;
    L_0x00de:
        r5 = com.google.android.exoplayer2.util.Util.MODEL;
        r5 = r5.startsWith(r1);
        if (r5 == 0) goto L_0x00e7;
    L_0x00e6:
        return r0;
    L_0x00e7:
        r5 = com.google.android.exoplayer2.util.Util.SDK_INT;
        if (r5 >= r7) goto L_0x0112;
    L_0x00eb:
        r5 = "OMX.MTK.AUDIO.DECODER.AAC";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x0112;
    L_0x00f3:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "a70";
        r5 = r7.equals(r5);
        if (r5 != 0) goto L_0x0111;
    L_0x00fd:
        r5 = com.google.android.exoplayer2.util.Util.MANUFACTURER;
        r7 = "Xiaomi";
        r5 = r7.equals(r5);
        if (r5 == 0) goto L_0x0112;
    L_0x0107:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "HM";
        r5 = r5.startsWith(r7);
        if (r5 == 0) goto L_0x0112;
    L_0x0111:
        return r0;
    L_0x0112:
        r5 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r7 = 16;
        if (r5 != r7) goto L_0x0199;
    L_0x0118:
        r5 = "OMX.qcom.audio.decoder.mp3";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x0199;
    L_0x0120:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "dlxu";
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0198;
    L_0x012a:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "protou";
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0198;
    L_0x0134:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "ville";
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0198;
    L_0x013e:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "villeplus";
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0198;
    L_0x0148:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "villec2";
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0198;
    L_0x0152:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "gee";
        r5 = r5.startsWith(r1);
        if (r5 != 0) goto L_0x0198;
    L_0x015c:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "C6602";
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0198;
    L_0x0166:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "C6603";
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0198;
    L_0x0170:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "C6606";
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0198;
    L_0x017a:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "C6616";
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0198;
    L_0x0184:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "L36h";
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0198;
    L_0x018e:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "SO-02E";
        r5 = r1.equals(r5);
        if (r5 == 0) goto L_0x0199;
    L_0x0198:
        return r0;
    L_0x0199:
        r5 = com.google.android.exoplayer2.util.Util.SDK_INT;
        if (r5 != r7) goto L_0x01ce;
    L_0x019d:
        r5 = "OMX.qcom.audio.decoder.aac";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x01ce;
    L_0x01a5:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "C1504";
        r5 = r7.equals(r5);
        if (r5 != 0) goto L_0x01cd;
    L_0x01af:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "C1505";
        r5 = r7.equals(r5);
        if (r5 != 0) goto L_0x01cd;
    L_0x01b9:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "C1604";
        r5 = r7.equals(r5);
        if (r5 != 0) goto L_0x01cd;
    L_0x01c3:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "C1605";
        r5 = r7.equals(r5);
        if (r5 == 0) goto L_0x01ce;
    L_0x01cd:
        return r0;
    L_0x01ce:
        r5 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r7 = 24;
        r1 = "samsung";
        if (r5 >= r7) goto L_0x023f;
    L_0x01d6:
        r5 = "OMX.SEC.aac.dec";
        r5 = r5.equals(r6);
        if (r5 != 0) goto L_0x01e6;
    L_0x01de:
        r5 = "OMX.Exynos.AAC.Decoder";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x023f;
    L_0x01e6:
        r5 = com.google.android.exoplayer2.util.Util.MANUFACTURER;
        r5 = r1.equals(r5);
        if (r5 == 0) goto L_0x023f;
    L_0x01ee:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "zeroflte";
        r5 = r5.startsWith(r7);
        if (r5 != 0) goto L_0x023e;
    L_0x01f8:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "zerolte";
        r5 = r5.startsWith(r7);
        if (r5 != 0) goto L_0x023e;
    L_0x0202:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "zenlte";
        r5 = r5.startsWith(r7);
        if (r5 != 0) goto L_0x023e;
    L_0x020c:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "SC-05G";
        r5 = r7.equals(r5);
        if (r5 != 0) goto L_0x023e;
    L_0x0216:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "marinelteatt";
        r5 = r7.equals(r5);
        if (r5 != 0) goto L_0x023e;
    L_0x0220:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "404SC";
        r5 = r7.equals(r5);
        if (r5 != 0) goto L_0x023e;
    L_0x022a:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "SC-04G";
        r5 = r7.equals(r5);
        if (r5 != 0) goto L_0x023e;
    L_0x0234:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r7 = "SCV31";
        r5 = r7.equals(r5);
        if (r5 == 0) goto L_0x023f;
    L_0x023e:
        return r0;
    L_0x023f:
        r5 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r7 = "jflte";
        r2 = 19;
        if (r5 > r2) goto L_0x0288;
    L_0x0247:
        r5 = "OMX.SEC.vp8.dec";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x0288;
    L_0x024f:
        r5 = com.google.android.exoplayer2.util.Util.MANUFACTURER;
        r5 = r1.equals(r5);
        if (r5 == 0) goto L_0x0288;
    L_0x0257:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "d2";
        r5 = r5.startsWith(r1);
        if (r5 != 0) goto L_0x0287;
    L_0x0261:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "serrano";
        r5 = r5.startsWith(r1);
        if (r5 != 0) goto L_0x0287;
    L_0x026b:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r5 = r5.startsWith(r7);
        if (r5 != 0) goto L_0x0287;
    L_0x0273:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "santos";
        r5 = r5.startsWith(r1);
        if (r5 != 0) goto L_0x0287;
    L_0x027d:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "t0";
        r5 = r5.startsWith(r1);
        if (r5 == 0) goto L_0x0288;
    L_0x0287:
        return r0;
    L_0x0288:
        r5 = com.google.android.exoplayer2.util.Util.SDK_INT;
        if (r5 > r2) goto L_0x029d;
    L_0x028c:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;
        r5 = r5.startsWith(r7);
        if (r5 == 0) goto L_0x029d;
    L_0x0294:
        r5 = "OMX.qcom.video.decoder.vp8";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x029d;
    L_0x029c:
        return r0;
    L_0x029d:
        r5 = "audio/eac3-joc";
        r5 = r5.equals(r8);
        if (r5 == 0) goto L_0x02ae;
    L_0x02a5:
        r5 = "OMX.MTK.AUDIO.DECODER.DSPAC3";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x02ae;
    L_0x02ad:
        return r0;
    L_0x02ae:
        r5 = 1;
        return r5;
    L_0x02b0:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.isCodecUsableDecoder(android.media.MediaCodecInfo, java.lang.String, boolean, java.lang.String):boolean");
    }

    private static void applyWorkarounds(String str, List<MediaCodecInfo> list) {
        if (MimeTypes.AUDIO_RAW.equals(str)) {
            Collections.sort(list, RAW_AUDIO_CODEC_COMPARATOR);
        }
    }

    /* JADX WARNING: Missing block: B:5:0x0018, code skipped:
            if ("Nexus 10".equals(com.google.android.exoplayer2.util.Util.MODEL) != false) goto L_0x001a;
     */
    private static boolean codecNeedsDisableAdaptationWorkaround(java.lang.String r2) {
        /*
        r0 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r1 = 22;
        if (r0 > r1) goto L_0x002c;
    L_0x0006:
        r0 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "ODROID-XU3";
        r0 = r1.equals(r0);
        if (r0 != 0) goto L_0x001a;
    L_0x0010:
        r0 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "Nexus 10";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x002c;
    L_0x001a:
        r0 = "OMX.Exynos.AVC.Decoder";
        r0 = r0.equals(r2);
        if (r0 != 0) goto L_0x002a;
    L_0x0022:
        r0 = "OMX.Exynos.AVC.Decoder.secure";
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x002c;
    L_0x002a:
        r2 = 1;
        goto L_0x002d;
    L_0x002c:
        r2 = 0;
    L_0x002d:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.codecNeedsDisableAdaptationWorkaround(java.lang.String):boolean");
    }

    private static Pair<Integer, Integer> getHevcProfileAndLevel(String str, String[] strArr) {
        String str2 = "Ignoring malformed HEVC codec string: ";
        String str3 = "MediaCodecUtil";
        StringBuilder stringBuilder;
        if (strArr.length < 4) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(str);
            Log.m18w(str3, stringBuilder.toString());
            return null;
        }
        Matcher matcher = PROFILE_PATTERN.matcher(strArr[1]);
        if (matcher.matches()) {
            int i;
            str = matcher.group(1);
            if ("1".equals(str)) {
                i = 1;
            } else if ("2".equals(str)) {
                i = 2;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown HEVC profile string: ");
                stringBuilder.append(str);
                Log.m18w(str3, stringBuilder.toString());
                return null;
            }
            Integer num = (Integer) HEVC_CODEC_STRING_TO_PROFILE_LEVEL.get(strArr[3]);
            if (num != null) {
                return new Pair(Integer.valueOf(i), num);
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unknown HEVC level string: ");
            stringBuilder2.append(matcher.group(1));
            Log.m18w(str3, stringBuilder2.toString());
            return null;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(str2);
        stringBuilder.append(str);
        Log.m18w(str3, stringBuilder.toString());
        return null;
    }

    private static Pair<Integer, Integer> getAvcProfileAndLevel(String str, String[] strArr) {
        String str2 = "Ignoring malformed AVC codec string: ";
        String str3 = "MediaCodecUtil";
        StringBuilder stringBuilder;
        if (strArr.length < 2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(str);
            Log.m18w(str3, stringBuilder.toString());
            return null;
        }
        try {
            Integer valueOf;
            Integer num;
            if (strArr[1].length() == 6) {
                Integer valueOf2 = Integer.valueOf(Integer.parseInt(strArr[1].substring(0, 2), 16));
                valueOf = Integer.valueOf(Integer.parseInt(strArr[1].substring(4), 16));
                num = valueOf2;
            } else if (strArr.length >= 3) {
                num = Integer.valueOf(Integer.parseInt(strArr[1]));
                valueOf = Integer.valueOf(Integer.parseInt(strArr[2]));
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(str);
                Log.m18w(str3, stringBuilder.toString());
                return null;
            }
            int i = AVC_PROFILE_NUMBER_TO_CONST.get(num.intValue(), -1);
            if (i == -1) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unknown AVC profile: ");
                stringBuilder2.append(num);
                Log.m18w(str3, stringBuilder2.toString());
                return null;
            }
            int i2 = AVC_LEVEL_NUMBER_TO_CONST.get(valueOf.intValue(), -1);
            if (i2 != -1) {
                return new Pair(Integer.valueOf(i), Integer.valueOf(i2));
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown AVC level: ");
            stringBuilder.append(valueOf);
            Log.m18w(str3, stringBuilder.toString());
            return null;
        } catch (NumberFormatException unused) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(str);
            Log.m18w(str3, stringBuilder.toString());
            return null;
        }
    }

    private static Pair<Integer, Integer> getAacCodecProfileAndLevel(String str, String[] strArr) {
        String str2 = "Ignoring malformed MP4A codec string: ";
        String str3 = "MediaCodecUtil";
        StringBuilder stringBuilder;
        if (strArr.length != 3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(str);
            Log.m18w(str3, stringBuilder.toString());
            return null;
        }
        try {
            if (MimeTypes.AUDIO_AAC.equals(MimeTypes.getMimeTypeFromMp4ObjectType(Integer.parseInt(strArr[1], 16)))) {
                int i = MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.get(Integer.parseInt(strArr[2]), -1);
                if (i != -1) {
                    return new Pair(Integer.valueOf(i), Integer.valueOf(0));
                }
            }
        } catch (NumberFormatException unused) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(str);
            Log.m18w(str3, stringBuilder.toString());
        }
        return null;
    }
}
