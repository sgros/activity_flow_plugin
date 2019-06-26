package com.google.android.exoplayer2.metadata.emsg;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataDecoder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class EventMessageDecoder implements MetadataDecoder {
   public Metadata decode(MetadataInputBuffer var1) {
      ByteBuffer var2 = var1.data;
      byte[] var10 = var2.array();
      int var3 = var2.limit();
      ParsableByteArray var11 = new ParsableByteArray(var10, var3);
      String var4 = var11.readNullTerminatedString();
      Assertions.checkNotNull(var4);
      var4 = (String)var4;
      String var5 = var11.readNullTerminatedString();
      Assertions.checkNotNull(var5);
      var5 = (String)var5;
      long var6 = var11.readUnsignedInt();
      long var8 = Util.scaleLargeTimestamp(var11.readUnsignedInt(), 1000000L, var6);
      return new Metadata(new Metadata.Entry[]{new EventMessage(var4, var5, Util.scaleLargeTimestamp(var11.readUnsignedInt(), 1000L, var6), var11.readUnsignedInt(), Arrays.copyOfRange(var10, var11.getPosition(), var3), var8)});
   }
}
