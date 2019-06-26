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

public class TrackHeaderBox extends AbstractFullBox {
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
   private static final JoinPoint.StaticPart ajc$tjp_29;
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
   private int alternateGroup;
   private Date creationTime;
   private long duration;
   private double height;
   private int layer;
   private Matrix matrix;
   private Date modificationTime;
   private long trackId;
   private float volume;
   private double width;

   static {
      ajc$preClinit();
   }

   public TrackHeaderBox() {
      super("tkhd");
      this.matrix = Matrix.ROTATE_0;
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("TrackHeaderBox.java", TrackHeaderBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getCreationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.util.Date"), 60);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getModificationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.util.Date"), 64);
      ajc$tjp_10 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getContent", "com.coremedia.iso.boxes.TrackHeaderBox", "java.nio.ByteBuffer", "byteBuffer", "", "void"), 142);
      ajc$tjp_11 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.lang.String"), 170);
      ajc$tjp_12 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setCreationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "java.util.Date", "creationTime", "", "void"), 196);
      ajc$tjp_13 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setModificationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "java.util.Date", "modificationTime", "", "void"), 203);
      ajc$tjp_14 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setTrackId", "com.coremedia.iso.boxes.TrackHeaderBox", "long", "trackId", "", "void"), 211);
      ajc$tjp_15 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setDuration", "com.coremedia.iso.boxes.TrackHeaderBox", "long", "duration", "", "void"), 215);
      ajc$tjp_16 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setLayer", "com.coremedia.iso.boxes.TrackHeaderBox", "int", "layer", "", "void"), 222);
      ajc$tjp_17 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setAlternateGroup", "com.coremedia.iso.boxes.TrackHeaderBox", "int", "alternateGroup", "", "void"), 226);
      ajc$tjp_18 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setVolume", "com.coremedia.iso.boxes.TrackHeaderBox", "float", "volume", "", "void"), 230);
      ajc$tjp_19 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setMatrix", "com.coremedia.iso.boxes.TrackHeaderBox", "com.googlecode.mp4parser.util.Matrix", "matrix", "", "void"), 234);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getTrackId", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "long"), 68);
      ajc$tjp_20 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setWidth", "com.coremedia.iso.boxes.TrackHeaderBox", "double", "width", "", "void"), 238);
      ajc$tjp_21 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setHeight", "com.coremedia.iso.boxes.TrackHeaderBox", "double", "height", "", "void"), 242);
      ajc$tjp_22 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "isEnabled", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 247);
      ajc$tjp_23 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "isInMovie", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 251);
      ajc$tjp_24 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "isInPreview", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 255);
      ajc$tjp_25 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "isInPoster", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 259);
      ajc$tjp_26 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setEnabled", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "enabled", "", "void"), 263);
      ajc$tjp_27 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setInMovie", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inMovie", "", "void"), 271);
      ajc$tjp_28 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setInPreview", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inPreview", "", "void"), 279);
      ajc$tjp_29 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setInPoster", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inPoster", "", "void"), 287);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getDuration", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "long"), 72);
      ajc$tjp_4 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getLayer", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "int"), 76);
      ajc$tjp_5 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getAlternateGroup", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "int"), 80);
      ajc$tjp_6 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getVolume", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "float"), 84);
      ajc$tjp_7 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getMatrix", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "com.googlecode.mp4parser.util.Matrix"), 88);
      ajc$tjp_8 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getWidth", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "double"), 92);
      ajc$tjp_9 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getHeight", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "double"), 96);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      if (this.getVersion() == 1) {
         this.creationTime = DateHelper.convert(IsoTypeReader.readUInt64(var1));
         this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt64(var1));
         this.trackId = IsoTypeReader.readUInt32(var1);
         IsoTypeReader.readUInt32(var1);
         this.duration = var1.getLong();
         if (this.duration < -1L) {
            throw new RuntimeException("The tracks duration is bigger than Long.MAX_VALUE");
         }
      } else {
         this.creationTime = DateHelper.convert(IsoTypeReader.readUInt32(var1));
         this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt32(var1));
         this.trackId = IsoTypeReader.readUInt32(var1);
         IsoTypeReader.readUInt32(var1);
         this.duration = IsoTypeReader.readUInt32(var1);
      }

      IsoTypeReader.readUInt32(var1);
      IsoTypeReader.readUInt32(var1);
      this.layer = IsoTypeReader.readUInt16(var1);
      this.alternateGroup = IsoTypeReader.readUInt16(var1);
      this.volume = IsoTypeReader.readFixedPoint88(var1);
      IsoTypeReader.readUInt16(var1);
      this.matrix = Matrix.fromByteBuffer(var1);
      this.width = IsoTypeReader.readFixedPoint1616(var1);
      this.height = IsoTypeReader.readFixedPoint1616(var1);
   }

   public int getAlternateGroup() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_5, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.alternateGroup;
   }

   public void getContent(ByteBuffer var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_10, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.writeVersionAndFlags(var1);
      if (this.getVersion() == 1) {
         IsoTypeWriter.writeUInt64(var1, DateHelper.convert(this.creationTime));
         IsoTypeWriter.writeUInt64(var1, DateHelper.convert(this.modificationTime));
         IsoTypeWriter.writeUInt32(var1, this.trackId);
         IsoTypeWriter.writeUInt32(var1, 0L);
         IsoTypeWriter.writeUInt64(var1, this.duration);
      } else {
         IsoTypeWriter.writeUInt32(var1, DateHelper.convert(this.creationTime));
         IsoTypeWriter.writeUInt32(var1, DateHelper.convert(this.modificationTime));
         IsoTypeWriter.writeUInt32(var1, this.trackId);
         IsoTypeWriter.writeUInt32(var1, 0L);
         IsoTypeWriter.writeUInt32(var1, this.duration);
      }

      IsoTypeWriter.writeUInt32(var1, 0L);
      IsoTypeWriter.writeUInt32(var1, 0L);
      IsoTypeWriter.writeUInt16(var1, this.layer);
      IsoTypeWriter.writeUInt16(var1, this.alternateGroup);
      IsoTypeWriter.writeFixedPoint88(var1, (double)this.volume);
      IsoTypeWriter.writeUInt16(var1, 0);
      this.matrix.getContent(var1);
      IsoTypeWriter.writeFixedPoint1616(var1, this.width);
      IsoTypeWriter.writeFixedPoint1616(var1, this.height);
   }

   protected long getContentSize() {
      long var1;
      if (this.getVersion() == 1) {
         var1 = 36L;
      } else {
         var1 = 24L;
      }

      return var1 + 60L;
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

   public double getHeight() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_9, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.height;
   }

   public int getLayer() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_4, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.layer;
   }

   public Date getModificationTime() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_1, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.modificationTime;
   }

   public long getTrackId() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_2, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.trackId;
   }

   public float getVolume() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_6, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.volume;
   }

   public double getWidth() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_8, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.width;
   }

   public void setAlternateGroup(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_17, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.alternateGroup = var1;
   }

   public void setCreationTime(Date var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_12, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.creationTime = var1;
      if (DateHelper.convert(var1) >= 4294967296L) {
         this.setVersion(1);
      }

   }

   public void setDuration(long var1) {
      JoinPoint var3 = Factory.makeJP(ajc$tjp_15, this, this, Conversions.longObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var3);
      this.duration = var1;
      if (var1 >= 4294967296L) {
         this.setFlags(1);
      }

   }

   public void setEnabled(boolean var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_26, this, this, Conversions.booleanObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      if (var1) {
         this.setFlags(this.getFlags() | 1);
      } else {
         this.setFlags(this.getFlags() & -2);
      }

   }

   public void setHeight(double var1) {
      JoinPoint var3 = Factory.makeJP(ajc$tjp_21, this, this, Conversions.doubleObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var3);
      this.height = var1;
   }

   public void setInMovie(boolean var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_27, this, this, Conversions.booleanObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      if (var1) {
         this.setFlags(this.getFlags() | 2);
      } else {
         this.setFlags(this.getFlags() & -3);
      }

   }

   public void setInPreview(boolean var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_28, this, this, Conversions.booleanObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      if (var1) {
         this.setFlags(this.getFlags() | 4);
      } else {
         this.setFlags(this.getFlags() & -5);
      }

   }

   public void setLayer(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_16, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.layer = var1;
   }

   public void setMatrix(Matrix var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_19, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.matrix = var1;
   }

   public void setModificationTime(Date var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_13, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.modificationTime = var1;
      if (DateHelper.convert(var1) >= 4294967296L) {
         this.setVersion(1);
      }

   }

   public void setTrackId(long var1) {
      JoinPoint var3 = Factory.makeJP(ajc$tjp_14, this, this, Conversions.longObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var3);
      this.trackId = var1;
   }

   public void setVolume(float var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_18, this, this, Conversions.floatObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.volume = var1;
   }

   public void setWidth(double var1) {
      JoinPoint var3 = Factory.makeJP(ajc$tjp_20, this, this, Conversions.doubleObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var3);
      this.width = var1;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_11, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder();
      var2.append("TrackHeaderBox[");
      var2.append("creationTime=");
      var2.append(this.getCreationTime());
      var2.append(";");
      var2.append("modificationTime=");
      var2.append(this.getModificationTime());
      var2.append(";");
      var2.append("trackId=");
      var2.append(this.getTrackId());
      var2.append(";");
      var2.append("duration=");
      var2.append(this.getDuration());
      var2.append(";");
      var2.append("layer=");
      var2.append(this.getLayer());
      var2.append(";");
      var2.append("alternateGroup=");
      var2.append(this.getAlternateGroup());
      var2.append(";");
      var2.append("volume=");
      var2.append(this.getVolume());
      var2.append(";");
      var2.append("matrix=");
      var2.append(this.matrix);
      var2.append(";");
      var2.append("width=");
      var2.append(this.getWidth());
      var2.append(";");
      var2.append("height=");
      var2.append(this.getHeight());
      var2.append("]");
      return var2.toString();
   }
}
