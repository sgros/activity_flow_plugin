package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;

abstract class TagPayloadReader {
   protected final TrackOutput output;

   protected TagPayloadReader(TrackOutput var1) {
      this.output = var1;
   }

   public final void consume(ParsableByteArray var1, long var2) throws ParserException {
      if (this.parseHeader(var1)) {
         this.parsePayload(var1, var2);
      }

   }

   protected abstract boolean parseHeader(ParsableByteArray var1) throws ParserException;

   protected abstract void parsePayload(ParsableByteArray var1, long var2) throws ParserException;

   public static final class UnsupportedFormatException extends ParserException {
      public UnsupportedFormatException(String var1) {
         super(var1);
      }
   }
}
