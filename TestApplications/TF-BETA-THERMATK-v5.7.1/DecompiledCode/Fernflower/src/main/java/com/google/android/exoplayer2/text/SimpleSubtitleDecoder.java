package com.google.android.exoplayer2.text;

import com.google.android.exoplayer2.decoder.SimpleDecoder;
import java.nio.ByteBuffer;

public abstract class SimpleSubtitleDecoder extends SimpleDecoder implements SubtitleDecoder {
   private final String name;

   protected SimpleSubtitleDecoder(String var1) {
      super(new SubtitleInputBuffer[2], new SubtitleOutputBuffer[2]);
      this.name = var1;
      this.setInitialInputBufferSize(1024);
   }

   protected final SubtitleInputBuffer createInputBuffer() {
      return new SubtitleInputBuffer();
   }

   protected final SubtitleOutputBuffer createOutputBuffer() {
      return new SimpleSubtitleOutputBuffer(this);
   }

   protected final SubtitleDecoderException createUnexpectedDecodeException(Throwable var1) {
      return new SubtitleDecoderException("Unexpected decode error", var1);
   }

   protected abstract Subtitle decode(byte[] var1, int var2, boolean var3) throws SubtitleDecoderException;

   protected final SubtitleDecoderException decode(SubtitleInputBuffer var1, SubtitleOutputBuffer var2, boolean var3) {
      try {
         ByteBuffer var4 = var1.data;
         Subtitle var6 = this.decode(var4.array(), var4.limit(), var3);
         var2.setContent(var1.timeUs, var6, var1.subsampleOffsetUs);
         var2.clearFlag(Integer.MIN_VALUE);
         return null;
      } catch (SubtitleDecoderException var5) {
         return var5;
      }
   }

   public final String getName() {
      return this.name;
   }

   protected final void releaseOutputBuffer(SubtitleOutputBuffer var1) {
      super.releaseOutputBuffer(var1);
   }

   public void setPositionUs(long var1) {
   }
}
