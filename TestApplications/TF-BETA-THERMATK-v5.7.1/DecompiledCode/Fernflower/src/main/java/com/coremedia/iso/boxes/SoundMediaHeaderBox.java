package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class SoundMediaHeaderBox extends AbstractMediaHeaderBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   private float balance;

   static {
      ajc$preClinit();
   }

   public SoundMediaHeaderBox() {
      super("smhd");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("SoundMediaHeaderBox.java", SoundMediaHeaderBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getBalance", "com.coremedia.iso.boxes.SoundMediaHeaderBox", "", "", "", "float"), 36);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SoundMediaHeaderBox", "", "", "", "java.lang.String"), 58);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      this.balance = IsoTypeReader.readFixedPoint88(var1);
      IsoTypeReader.readUInt16(var1);
   }

   public float getBalance() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.balance;
   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      IsoTypeWriter.writeFixedPoint88(var1, (double)this.balance);
      IsoTypeWriter.writeUInt16(var1, 0);
   }

   protected long getContentSize() {
      return 8L;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_1, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder("SoundMediaHeaderBox[balance=");
      var2.append(this.getBalance());
      var2.append("]");
      return var2.toString();
   }
}
