package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class DataEntryUrlBox extends AbstractFullBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;

   static {
      ajc$preClinit();
   }

   public DataEntryUrlBox() {
      super("url ");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("DataEntryUrlBox.java", DataEntryUrlBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.DataEntryUrlBox", "", "", "", "java.lang.String"), 51);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
   }

   protected long getContentSize() {
      return 4L;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return "DataEntryUrlBox[]";
   }
}
