package com.google.android.exoplayer2.ext.ffmpeg;

import com.google.android.exoplayer2.audio.AudioDecoderException;

public final class FfmpegDecoderException extends AudioDecoderException {
   FfmpegDecoderException(String var1) {
      super(var1);
   }

   FfmpegDecoderException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
