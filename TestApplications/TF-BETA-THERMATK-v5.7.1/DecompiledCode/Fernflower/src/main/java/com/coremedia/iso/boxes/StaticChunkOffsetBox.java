package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class StaticChunkOffsetBox extends ChunkOffsetBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   private long[] chunkOffsets = new long[0];

   static {
      ajc$preClinit();
   }

   public StaticChunkOffsetBox() {
      super("stco");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("StaticChunkOffsetBox.java", StaticChunkOffsetBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getChunkOffsets", "com.coremedia.iso.boxes.StaticChunkOffsetBox", "", "", "", "[J"), 39);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setChunkOffsets", "com.coremedia.iso.boxes.StaticChunkOffsetBox", "[J", "chunkOffsets", "", "void"), 48);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      int var2 = CastUtils.l2i(IsoTypeReader.readUInt32(var1));
      this.chunkOffsets = new long[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.chunkOffsets[var3] = IsoTypeReader.readUInt32(var1);
      }

   }

   public long[] getChunkOffsets() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.chunkOffsets;
   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      IsoTypeWriter.writeUInt32(var1, (long)this.chunkOffsets.length);
      long[] var2 = this.chunkOffsets;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         IsoTypeWriter.writeUInt32(var1, var2[var4]);
      }

   }

   protected long getContentSize() {
      return (long)(this.chunkOffsets.length * 4 + 8);
   }

   public void setChunkOffsets(long[] var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_1, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.chunkOffsets = var1;
   }
}
