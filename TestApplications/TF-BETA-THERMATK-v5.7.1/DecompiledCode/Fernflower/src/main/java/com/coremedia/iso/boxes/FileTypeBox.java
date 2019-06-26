package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class FileTypeBox extends AbstractBox {
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
   private List compatibleBrands = Collections.emptyList();
   private String majorBrand;
   private long minorVersion;

   static {
      ajc$preClinit();
   }

   public FileTypeBox() {
      super("ftyp");
   }

   public FileTypeBox(String var1, long var2, List var4) {
      super("ftyp");
      this.majorBrand = var1;
      this.minorVersion = var2;
      this.compatibleBrands = var4;
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("FileTypeBox.java", FileTypeBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getMajorBrand", "com.coremedia.iso.boxes.FileTypeBox", "", "", "", "java.lang.String"), 85);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setMajorBrand", "com.coremedia.iso.boxes.FileTypeBox", "java.lang.String", "majorBrand", "", "void"), 94);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setMinorVersion", "com.coremedia.iso.boxes.FileTypeBox", "long", "minorVersion", "", "void"), 103);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getMinorVersion", "com.coremedia.iso.boxes.FileTypeBox", "", "", "", "long"), 113);
      ajc$tjp_4 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getCompatibleBrands", "com.coremedia.iso.boxes.FileTypeBox", "", "", "", "java.util.List"), 122);
      ajc$tjp_5 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setCompatibleBrands", "com.coremedia.iso.boxes.FileTypeBox", "java.util.List", "compatibleBrands", "", "void"), 126);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.majorBrand = IsoTypeReader.read4cc(var1);
      this.minorVersion = IsoTypeReader.readUInt32(var1);
      int var2 = var1.remaining() / 4;
      this.compatibleBrands = new LinkedList();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.compatibleBrands.add(IsoTypeReader.read4cc(var1));
      }

   }

   protected void getContent(ByteBuffer var1) {
      var1.put(IsoFile.fourCCtoBytes(this.majorBrand));
      IsoTypeWriter.writeUInt32(var1, this.minorVersion);
      Iterator var2 = this.compatibleBrands.iterator();

      while(var2.hasNext()) {
         var1.put(IsoFile.fourCCtoBytes((String)var2.next()));
      }

   }

   protected long getContentSize() {
      return (long)(this.compatibleBrands.size() * 4 + 8);
   }

   public String getMajorBrand() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.majorBrand;
   }

   public long getMinorVersion() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_3, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.minorVersion;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("FileTypeBox[");
      var1.append("majorBrand=");
      var1.append(this.getMajorBrand());
      var1.append(";");
      var1.append("minorVersion=");
      var1.append(this.getMinorVersion());
      Iterator var2 = this.compatibleBrands.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.append(";");
         var1.append("compatibleBrand=");
         var1.append(var3);
      }

      var1.append("]");
      return var1.toString();
   }
}
