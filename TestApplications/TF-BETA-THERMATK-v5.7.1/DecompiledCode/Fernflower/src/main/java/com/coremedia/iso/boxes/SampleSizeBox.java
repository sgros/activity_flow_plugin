package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class SampleSizeBox extends AbstractFullBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_2;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_3;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_4;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_5;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_6;
   int sampleCount;
   private long sampleSize;
   private long[] sampleSizes = new long[0];

   static {
      ajc$preClinit();
   }

   public SampleSizeBox() {
      super("stsz");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("SampleSizeBox.java", SampleSizeBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getSampleSize", "com.coremedia.iso.boxes.SampleSizeBox", "", "", "", "long"), 50);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setSampleSize", "com.coremedia.iso.boxes.SampleSizeBox", "long", "sampleSize", "", "void"), 54);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getSampleSizeAtIndex", "com.coremedia.iso.boxes.SampleSizeBox", "int", "index", "", "long"), 59);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getSampleCount", "com.coremedia.iso.boxes.SampleSizeBox", "", "", "", "long"), 67);
      ajc$tjp_4 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getSampleSizes", "com.coremedia.iso.boxes.SampleSizeBox", "", "", "", "[J"), 76);
      ajc$tjp_5 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setSampleSizes", "com.coremedia.iso.boxes.SampleSizeBox", "[J", "sampleSizes", "", "void"), 80);
      ajc$tjp_6 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SampleSizeBox", "", "", "", "java.lang.String"), 119);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      this.sampleSize = IsoTypeReader.readUInt32(var1);
      this.sampleCount = CastUtils.l2i(IsoTypeReader.readUInt32(var1));
      if (this.sampleSize == 0L) {
         this.sampleSizes = new long[this.sampleCount];

         for(int var2 = 0; var2 < this.sampleCount; ++var2) {
            this.sampleSizes[var2] = IsoTypeReader.readUInt32(var1);
         }
      }

   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      IsoTypeWriter.writeUInt32(var1, this.sampleSize);
      if (this.sampleSize == 0L) {
         IsoTypeWriter.writeUInt32(var1, (long)this.sampleSizes.length);
         long[] var2 = this.sampleSizes;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            IsoTypeWriter.writeUInt32(var1, var2[var4]);
         }
      } else {
         IsoTypeWriter.writeUInt32(var1, (long)this.sampleCount);
      }

   }

   protected long getContentSize() {
      int var1;
      if (this.sampleSize == 0L) {
         var1 = this.sampleSizes.length * 4;
      } else {
         var1 = 0;
      }

      return (long)(var1 + 12);
   }

   public long getSampleCount() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_3, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      int var2;
      if (this.sampleSize > 0L) {
         var2 = this.sampleCount;
      } else {
         var2 = this.sampleSizes.length;
      }

      return (long)var2;
   }

   public long getSampleSize() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.sampleSize;
   }

   public void setSampleSizes(long[] var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_5, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.sampleSizes = var1;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_6, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder("SampleSizeBox[sampleSize=");
      var2.append(this.getSampleSize());
      var2.append(";sampleCount=");
      var2.append(this.getSampleCount());
      var2.append("]");
      return var2.toString();
   }
}
