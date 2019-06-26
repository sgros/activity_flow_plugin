package com.googlecode.mp4parser;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.FullBox;
import java.nio.ByteBuffer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.internal.Conversions;
import org.aspectj.runtime.reflect.Factory;

public abstract class AbstractFullBox extends AbstractBox implements FullBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   private int flags;
   private int version;

   static {
      ajc$preClinit();
   }

   protected AbstractFullBox(String var1) {
      super(var1);
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("AbstractFullBox.java", AbstractFullBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setVersion", "com.googlecode.mp4parser.AbstractFullBox", "int", "version", "", "void"), 51);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setFlags", "com.googlecode.mp4parser.AbstractFullBox", "int", "flags", "", "void"), 64);
   }

   public int getFlags() {
      if (!super.isParsed) {
         this.parseDetails();
      }

      return this.flags;
   }

   public int getVersion() {
      if (!super.isParsed) {
         this.parseDetails();
      }

      return this.version;
   }

   protected final long parseVersionAndFlags(ByteBuffer var1) {
      this.version = IsoTypeReader.readUInt8(var1);
      this.flags = IsoTypeReader.readUInt24(var1);
      return 4L;
   }

   public void setFlags(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_1, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.flags = var1;
   }

   public void setVersion(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_0, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.version = var1;
   }

   protected final void writeVersionAndFlags(ByteBuffer var1) {
      IsoTypeWriter.writeUInt8(var1, this.version);
      IsoTypeWriter.writeUInt24(var1, this.flags);
   }
}
