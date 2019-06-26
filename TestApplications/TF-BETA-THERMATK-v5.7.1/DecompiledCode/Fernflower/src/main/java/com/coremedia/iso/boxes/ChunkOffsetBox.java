package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public abstract class ChunkOffsetBox extends AbstractFullBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;

   static {
      ajc$preClinit();
   }

   public ChunkOffsetBox(String var1) {
      super(var1);
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("ChunkOffsetBox.java", ChunkOffsetBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.ChunkOffsetBox", "", "", "", "java.lang.String"), 18);
   }

   public abstract long[] getChunkOffsets();

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder(String.valueOf(this.getClass().getSimpleName()));
      var2.append("[entryCount=");
      var2.append(this.getChunkOffsets().length);
      var2.append("]");
      return var2.toString();
   }
}
