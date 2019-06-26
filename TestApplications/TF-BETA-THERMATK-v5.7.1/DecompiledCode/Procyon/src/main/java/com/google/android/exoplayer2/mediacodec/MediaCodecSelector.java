// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.mediacodec;

import java.util.Collections;
import java.util.List;

public interface MediaCodecSelector
{
    public static final MediaCodecSelector DEFAULT = new MediaCodecSelector() {
        @Override
        public List<MediaCodecInfo> getDecoderInfos(final String s, final boolean b) throws MediaCodecUtil.DecoderQueryException {
            final List<MediaCodecInfo> decoderInfos = MediaCodecUtil.getDecoderInfos(s, b);
            List<MediaCodecInfo> list;
            if (decoderInfos.isEmpty()) {
                list = Collections.emptyList();
            }
            else {
                list = Collections.singletonList(decoderInfos.get(0));
            }
            return list;
        }
        
        @Override
        public MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    };
    public static final MediaCodecSelector DEFAULT_WITH_FALLBACK = new MediaCodecSelector() {
        @Override
        public List<MediaCodecInfo> getDecoderInfos(final String s, final boolean b) throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getDecoderInfos(s, b);
        }
        
        @Override
        public MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    };
    
    List<MediaCodecInfo> getDecoderInfos(final String p0, final boolean p1) throws MediaCodecUtil.DecoderQueryException;
    
    MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException;
}
