package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class SyncSampleBox extends AbstractFullBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_2;
   private long[] sampleNumber;

   static {
      ajc$preClinit();
   }

   public SyncSampleBox() {
      super("stss");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("SyncSampleBox.java", SyncSampleBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getSampleNumber", "com.coremedia.iso.boxes.SyncSampleBox", "", "", "", "[J"), 46);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SyncSampleBox", "", "", "", "java.lang.String"), 77);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setSampleNumber", "com.coremedia.iso.boxes.SyncSampleBox", "[J", "sampleNumber", "", "void"), 81);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      int var2 = CastUtils.l2i(IsoTypeReader.readUInt32(var1));
      this.sampleNumber = new long[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.sampleNumber[var3] = IsoTypeReader.readUInt32(var1);
      }

   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      IsoTypeWriter.writeUInt32(var1, (long)this.sampleNumber.length);
      long[] var2 = this.sampleNumber;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         IsoTypeWriter.writeUInt32(var1, var2[var4]);
      }

   }

   protected long getContentSize() {
      return (long)(this.sampleNumber.length * 4 + 8);
   }

   public void setSampleNumber(long[] var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_2, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.sampleNumber = var1;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_1, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder("SyncSampleBox[entryCount=");
      var2.append(this.sampleNumber.length);
      var2.append("]");
      return var2.toString();
   }
}
