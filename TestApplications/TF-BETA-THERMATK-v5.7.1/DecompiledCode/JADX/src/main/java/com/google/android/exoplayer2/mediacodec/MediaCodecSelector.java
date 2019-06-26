package com.google.android.exoplayer2.mediacodec;

import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import java.util.Collections;
import java.util.List;

public interface MediaCodecSelector {
    public static final MediaCodecSelector DEFAULT = new C01681();
    public static final MediaCodecSelector DEFAULT_WITH_FALLBACK = new C01692();

    /* renamed from: com.google.android.exoplayer2.mediacodec.MediaCodecSelector$1 */
    static class C01681 implements MediaCodecSelector {
        C01681() {
        }

        public List<MediaCodecInfo> getDecoderInfos(String str, boolean z) throws DecoderQueryException {
            List decoderInfos = MediaCodecUtil.getDecoderInfos(str, z);
            if (decoderInfos.isEmpty()) {
                return Collections.emptyList();
            }
            return Collections.singletonList(decoderInfos.get(0));
        }

        public MediaCodecInfo getPassthroughDecoderInfo() throws DecoderQueryException {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    }

    /* renamed from: com.google.android.exoplayer2.mediacodec.MediaCodecSelector$2 */
    static class C01692 implements MediaCodecSelector {
        C01692() {
        }

        public List<MediaCodecInfo> getDecoderInfos(String str, boolean z) throws DecoderQueryException {
            return MediaCodecUtil.getDecoderInfos(str, z);
        }

        public MediaCodecInfo getPassthroughDecoderInfo() throws DecoderQueryException {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    }

    List<MediaCodecInfo> getDecoderInfos(String str, boolean z) throws DecoderQueryException;

    MediaCodecInfo getPassthroughDecoderInfo() throws DecoderQueryException;
}
