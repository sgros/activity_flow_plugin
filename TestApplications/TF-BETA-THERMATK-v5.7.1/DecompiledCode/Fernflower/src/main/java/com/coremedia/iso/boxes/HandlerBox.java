package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class HandlerBox extends AbstractFullBox {
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
   public static final Map readableTypes;
   private long a;
   private long b;
   private long c;
   private String handlerType;
   private String name = null;
   private long shouldBeZeroButAppleWritesHereSomeValue;
   private boolean zeroTerm = true;

   static {
      ajc$preClinit();
      HashMap var0 = new HashMap();
      var0.put("odsm", "ObjectDescriptorStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
      var0.put("crsm", "ClockReferenceStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
      var0.put("sdsm", "SceneDescriptionStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
      var0.put("m7sm", "MPEG7Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
      var0.put("ocsm", "ObjectContentInfoStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
      var0.put("ipsm", "IPMP Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
      var0.put("mjsm", "MPEG-J Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
      var0.put("mdir", "Apple Meta Data iTunes Reader");
      var0.put("mp7b", "MPEG-7 binary XML");
      var0.put("mp7t", "MPEG-7 XML");
      var0.put("vide", "Video Track");
      var0.put("soun", "Sound Track");
      var0.put("hint", "Hint Track");
      var0.put("appl", "Apple specific");
      var0.put("meta", "Timed Metadata track - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
      readableTypes = Collections.unmodifiableMap(var0);
   }

   public HandlerBox() {
      super("hdlr");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("HandlerBox.java", HandlerBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getHandlerType", "com.coremedia.iso.boxes.HandlerBox", "", "", "", "java.lang.String"), 78);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setName", "com.coremedia.iso.boxes.HandlerBox", "java.lang.String", "name", "", "void"), 87);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setHandlerType", "com.coremedia.iso.boxes.HandlerBox", "java.lang.String", "handlerType", "", "void"), 91);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getName", "com.coremedia.iso.boxes.HandlerBox", "", "", "", "java.lang.String"), 95);
      ajc$tjp_4 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getHumanReadableTrackType", "com.coremedia.iso.boxes.HandlerBox", "", "", "", "java.lang.String"), 99);
      ajc$tjp_5 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.HandlerBox", "", "", "", "java.lang.String"), 149);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      this.shouldBeZeroButAppleWritesHereSomeValue = IsoTypeReader.readUInt32(var1);
      this.handlerType = IsoTypeReader.read4cc(var1);
      this.a = IsoTypeReader.readUInt32(var1);
      this.b = IsoTypeReader.readUInt32(var1);
      this.c = IsoTypeReader.readUInt32(var1);
      if (var1.remaining() > 0) {
         this.name = IsoTypeReader.readString(var1, var1.remaining());
         if (this.name.endsWith("\u0000")) {
            String var2 = this.name;
            this.name = var2.substring(0, var2.length() - 1);
            this.zeroTerm = true;
         } else {
            this.zeroTerm = false;
         }
      } else {
         this.zeroTerm = false;
      }

   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      IsoTypeWriter.writeUInt32(var1, this.shouldBeZeroButAppleWritesHereSomeValue);
      var1.put(IsoFile.fourCCtoBytes(this.handlerType));
      IsoTypeWriter.writeUInt32(var1, this.a);
      IsoTypeWriter.writeUInt32(var1, this.b);
      IsoTypeWriter.writeUInt32(var1, this.c);
      String var2 = this.name;
      if (var2 != null) {
         var1.put(Utf8.convert(var2));
      }

      if (this.zeroTerm) {
         var1.put((byte)0);
      }

   }

   protected long getContentSize() {
      int var1;
      if (this.zeroTerm) {
         var1 = Utf8.utf8StringLengthInBytes(this.name) + 25;
      } else {
         var1 = Utf8.utf8StringLengthInBytes(this.name) + 24;
      }

      return (long)var1;
   }

   public String getHandlerType() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.handlerType;
   }

   public String getName() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_3, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.name;
   }

   public void setHandlerType(String var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_2, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.handlerType = var1;
   }

   public void setName(String var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_1, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.name = var1;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_5, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder("HandlerBox[handlerType=");
      var2.append(this.getHandlerType());
      var2.append(";name=");
      var2.append(this.getName());
      var2.append("]");
      return var2.toString();
   }
}
