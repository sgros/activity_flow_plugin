package com.google.android.exoplayer2.mediacodec;

import java.util.Collections;
import java.util.List;

public interface MediaCodecSelector {
   MediaCodecSelector DEFAULT = new MediaCodecSelector() {
      public List getDecoderInfos(String var1, boolean var2) throws MediaCodecUtil.DecoderQueryException {
         List var3 = MediaCodecUtil.getDecoderInfos(var1, var2);
         if (var3.isEmpty()) {
            var3 = Collections.emptyList();
         } else {
            var3 = Collections.singletonList(var3.get(0));
         }

         return var3;
      }

      public MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
         return MediaCodecUtil.getPassthroughDecoderInfo();
      }
   };
   MediaCodecSelector DEFAULT_WITH_FALLBACK = new MediaCodecSelector() {
      public List getDecoderInfos(String var1, boolean var2) throws MediaCodecUtil.DecoderQueryException {
         return MediaCodecUtil.getDecoderInfos(var1, var2);
      }

      public MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
         return MediaCodecUtil.getPassthroughDecoderInfo();
      }
   };

   List getDecoderInfos(String var1, boolean var2) throws MediaCodecUtil.DecoderQueryException;

   MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException;
}
