package com.google.android.exoplayer2.text.dvb;

import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;

public final class DvbDecoder extends SimpleSubtitleDecoder {
   private final DvbParser parser;

   public DvbDecoder(List var1) {
      super("DvbDecoder");
      ParsableByteArray var2 = new ParsableByteArray((byte[])var1.get(0));
      this.parser = new DvbParser(var2.readUnsignedShort(), var2.readUnsignedShort());
   }

   protected DvbSubtitle decode(byte[] var1, int var2, boolean var3) {
      if (var3) {
         this.parser.reset();
      }

      return new DvbSubtitle(this.parser.decode(var1, var2));
   }
}
