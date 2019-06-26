package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.DateHelper;
import java.nio.ByteBuffer;
import java.util.Date;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.internal.Conversions;
import org.aspectj.runtime.reflect.Factory;

public class MediaHeaderBox extends AbstractFullBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_10;
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
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_7;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_8;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_9;
   private Date creationTime = new Date();
   private long duration;
   private String language = "eng";
   private Date modificationTime = new Date();
   private long timescale;

   static {
      ajc$preClinit();
   }

   public MediaHeaderBox() {
      super("mdhd");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("MediaHeaderBox.java", MediaHeaderBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getCreationTime", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "java.util.Date"), 46);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getModificationTime", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "java.util.Date"), 50);
      ajc$tjp_10 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "java.lang.String"), 118);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getTimescale", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "long"), 54);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getDuration", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "long"), 58);
      ajc$tjp_4 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getLanguage", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "java.lang.String"), 62);
      ajc$tjp_5 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setCreationTime", "com.coremedia.iso.boxes.MediaHeaderBox", "java.util.Date", "creationTime", "", "void"), 79);
      ajc$tjp_6 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setModificationTime", "com.coremedia.iso.boxes.MediaHeaderBox", "java.util.Date", "modificationTime", "", "void"), 83);
      ajc$tjp_7 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setTimescale", "com.coremedia.iso.boxes.MediaHeaderBox", "long", "timescale", "", "void"), 87);
      ajc$tjp_8 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setDuration", "com.coremedia.iso.boxes.MediaHeaderBox", "long", "duration", "", "void"), 91);
      ajc$tjp_9 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setLanguage", "com.coremedia.iso.boxes.MediaHeaderBox", "java.lang.String", "language", "", "void"), 95);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      if (this.getVersion() == 1) {
         this.creationTime = DateHelper.convert(IsoTypeReader.readUInt64(var1));
         this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt64(var1));
         this.timescale = IsoTypeReader.readUInt32(var1);
         this.duration = IsoTypeReader.readUInt64(var1);
      } else {
         this.creationTime = DateHelper.convert(IsoTypeReader.readUInt32(var1));
         this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt32(var1));
         this.timescale = IsoTypeReader.readUInt32(var1);
         this.duration = IsoTypeReader.readUInt32(var1);
      }

      this.language = IsoTypeReader.readIso639(var1);
      IsoTypeReader.readUInt16(var1);
   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      if (this.getVersion() == 1) {
         IsoTypeWriter.writeUInt64(var1, DateHelper.convert(this.creationTime));
         IsoTypeWriter.writeUInt64(var1, DateHelper.convert(this.modificationTime));
         IsoTypeWriter.writeUInt32(var1, this.timescale);
         IsoTypeWriter.writeUInt64(var1, this.duration);
      } else {
         IsoTypeWriter.writeUInt32(var1, DateHelper.convert(this.creationTime));
         IsoTypeWriter.writeUInt32(var1, DateHelper.convert(this.modificationTime));
         IsoTypeWriter.writeUInt32(var1, this.timescale);
         IsoTypeWriter.writeUInt32(var1, this.duration);
      }

      IsoTypeWriter.writeIso639(var1, this.language);
      IsoTypeWriter.writeUInt16(var1, 0);
   }

   protected long getContentSize() {
      long var1;
      if (this.getVersion() == 1) {
         var1 = 32L;
      } else {
         var1 = 20L;
      }

      return var1 + 2L + 2L;
   }

   public Date getCreationTime() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.creationTime;
   }

   public long getDuration() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_3, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.duration;
   }

   public String getLanguage() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_4, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.language;
   }

   public Date getModificationTime() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_1, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.modificationTime;
   }

   public long getTimescale() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_2, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.timescale;
   }

   public void setCreationTime(Date var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_5, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.creationTime = var1;
   }

   public void setDuration(long var1) {
      JoinPoint var3 = Factory.makeJP(ajc$tjp_8, this, this, Conversions.longObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var3);
      this.duration = var1;
   }

   public void setLanguage(String var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_9, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.language = var1;
   }

   public void setTimescale(long var1) {
      JoinPoint var3 = Factory.makeJP(ajc$tjp_7, this, this, Conversions.longObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var3);
      this.timescale = var1;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_10, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder();
      var2.append("MediaHeaderBox[");
      var2.append("creationTime=");
      var2.append(this.getCreationTime());
      var2.append(";");
      var2.append("modificationTime=");
      var2.append(this.getModificationTime());
      var2.append(";");
      var2.append("timescale=");
      var2.append(this.getTimescale());
      var2.append(";");
      var2.append("duration=");
      var2.append(this.getDuration());
      var2.append(";");
      var2.append("language=");
      var2.append(this.getLanguage());
      var2.append("]");
      return var2.toString();
   }
}
