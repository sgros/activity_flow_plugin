package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.DateHelper;
import com.googlecode.mp4parser.util.Matrix;
import java.nio.ByteBuffer;
import java.util.Date;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.internal.Conversions;
import org.aspectj.runtime.reflect.Factory;

public class MovieHeaderBox extends AbstractFullBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_10;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_11;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_12;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_13;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_14;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_15;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_16;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_17;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_18;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_19;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_2;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_20;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_21;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_22;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_23;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_24;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_25;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_26;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_27;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_28;
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
   private Date creationTime;
   private int currentTime;
   private long duration;
   private Matrix matrix;
   private Date modificationTime;
   private long nextTrackId;
   private int posterTime;
   private int previewDuration;
   private int previewTime;
   private double rate = 1.0D;
   private int selectionDuration;
   private int selectionTime;
   private long timescale;
   private float volume = 1.0F;

   static {
      ajc$preClinit();
   }

   public MovieHeaderBox() {
      super("mvhd");
      this.matrix = Matrix.ROTATE_0;
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("MovieHeaderBox.java", MovieHeaderBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getCreationTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "java.util.Date"), 63);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getModificationTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "java.util.Date"), 67);
      ajc$tjp_10 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setModificationTime", "com.coremedia.iso.boxes.MovieHeaderBox", "java.util.Date", "modificationTime", "", "void"), 203);
      ajc$tjp_11 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setTimescale", "com.coremedia.iso.boxes.MovieHeaderBox", "long", "timescale", "", "void"), 211);
      ajc$tjp_12 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "long", "duration", "", "void"), 215);
      ajc$tjp_13 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setRate", "com.coremedia.iso.boxes.MovieHeaderBox", "double", "rate", "", "void"), 222);
      ajc$tjp_14 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setVolume", "com.coremedia.iso.boxes.MovieHeaderBox", "float", "volume", "", "void"), 226);
      ajc$tjp_15 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setMatrix", "com.coremedia.iso.boxes.MovieHeaderBox", "com.googlecode.mp4parser.util.Matrix", "matrix", "", "void"), 230);
      ajc$tjp_16 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setNextTrackId", "com.coremedia.iso.boxes.MovieHeaderBox", "long", "nextTrackId", "", "void"), 234);
      ajc$tjp_17 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getPreviewTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), 238);
      ajc$tjp_18 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setPreviewTime", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "previewTime", "", "void"), 242);
      ajc$tjp_19 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getPreviewDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), 246);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getTimescale", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "long"), 71);
      ajc$tjp_20 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setPreviewDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "previewDuration", "", "void"), 250);
      ajc$tjp_21 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getPosterTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), 254);
      ajc$tjp_22 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setPosterTime", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "posterTime", "", "void"), 258);
      ajc$tjp_23 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getSelectionTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), 262);
      ajc$tjp_24 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setSelectionTime", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "selectionTime", "", "void"), 266);
      ajc$tjp_25 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getSelectionDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), 270);
      ajc$tjp_26 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setSelectionDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "selectionDuration", "", "void"), 274);
      ajc$tjp_27 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getCurrentTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), 278);
      ajc$tjp_28 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setCurrentTime", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "currentTime", "", "void"), 282);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "long"), 75);
      ajc$tjp_4 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getRate", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "double"), 79);
      ajc$tjp_5 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getVolume", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "float"), 83);
      ajc$tjp_6 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getMatrix", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "com.googlecode.mp4parser.util.Matrix"), 87);
      ajc$tjp_7 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getNextTrackId", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "long"), 91);
      ajc$tjp_8 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "java.lang.String"), 139);
      ajc$tjp_9 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setCreationTime", "com.coremedia.iso.boxes.MovieHeaderBox", "java.util.Date", "creationTime", "", "void"), 195);
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

      this.rate = IsoTypeReader.readFixedPoint1616(var1);
      this.volume = IsoTypeReader.readFixedPoint88(var1);
      IsoTypeReader.readUInt16(var1);
      IsoTypeReader.readUInt32(var1);
      IsoTypeReader.readUInt32(var1);
      this.matrix = Matrix.fromByteBuffer(var1);
      this.previewTime = var1.getInt();
      this.previewDuration = var1.getInt();
      this.posterTime = var1.getInt();
      this.selectionTime = var1.getInt();
      this.selectionDuration = var1.getInt();
      this.currentTime = var1.getInt();
      this.nextTrackId = IsoTypeReader.readUInt32(var1);
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

      IsoTypeWriter.writeFixedPoint1616(var1, this.rate);
      IsoTypeWriter.writeFixedPoint88(var1, (double)this.volume);
      IsoTypeWriter.writeUInt16(var1, 0);
      IsoTypeWriter.writeUInt32(var1, 0L);
      IsoTypeWriter.writeUInt32(var1, 0L);
      this.matrix.getContent(var1);
      var1.putInt(this.previewTime);
      var1.putInt(this.previewDuration);
      var1.putInt(this.posterTime);
      var1.putInt(this.selectionTime);
      var1.putInt(this.selectionDuration);
      var1.putInt(this.currentTime);
      IsoTypeWriter.writeUInt32(var1, this.nextTrackId);
   }

   protected long getContentSize() {
      long var1;
      if (this.getVersion() == 1) {
         var1 = 32L;
      } else {
         var1 = 20L;
      }

      return var1 + 80L;
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

   public Date getModificationTime() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_1, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.modificationTime;
   }

   public long getNextTrackId() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_7, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.nextTrackId;
   }

   public double getRate() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_4, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.rate;
   }

   public long getTimescale() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_2, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.timescale;
   }

   public float getVolume() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_5, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.volume;
   }

   public void setCreationTime(Date var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_9, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.creationTime = var1;
      if (DateHelper.convert(var1) >= 4294967296L) {
         this.setVersion(1);
      }

   }

   public void setDuration(long var1) {
      JoinPoint var3 = Factory.makeJP(ajc$tjp_12, this, this, Conversions.longObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var3);
      this.duration = var1;
      if (var1 >= 4294967296L) {
         this.setVersion(1);
      }

   }

   public void setMatrix(Matrix var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_15, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.matrix = var1;
   }

   public void setModificationTime(Date var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_10, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.modificationTime = var1;
      if (DateHelper.convert(var1) >= 4294967296L) {
         this.setVersion(1);
      }

   }

   public void setNextTrackId(long var1) {
      JoinPoint var3 = Factory.makeJP(ajc$tjp_16, this, this, Conversions.longObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var3);
      this.nextTrackId = var1;
   }

   public void setTimescale(long var1) {
      JoinPoint var3 = Factory.makeJP(ajc$tjp_11, this, this, Conversions.longObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var3);
      this.timescale = var1;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_8, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder();
      var2.append("MovieHeaderBox[");
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
      var2.append("rate=");
      var2.append(this.getRate());
      var2.append(";");
      var2.append("volume=");
      var2.append(this.getVolume());
      var2.append(";");
      var2.append("matrix=");
      var2.append(this.matrix);
      var2.append(";");
      var2.append("nextTrackId=");
      var2.append(this.getNextTrackId());
      var2.append("]");
      return var2.toString();
   }
}
