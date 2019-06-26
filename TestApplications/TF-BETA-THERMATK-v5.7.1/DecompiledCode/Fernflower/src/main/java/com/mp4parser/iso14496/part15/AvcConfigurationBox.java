package com.mp4parser.iso14496.part15;

import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.internal.Conversions;
import org.aspectj.runtime.reflect.Factory;

public final class AvcConfigurationBox extends AbstractBox {
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
   public AvcDecoderConfigurationRecord avcDecoderConfigurationRecord = new AvcDecoderConfigurationRecord();

   static {
      ajc$preClinit();
   }

   public AvcConfigurationBox() {
      super("avcC");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("AvcConfigurationBox.java", AvcConfigurationBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getConfigurationVersion", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 44);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getAvcProfileIndication", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 48);
      ajc$tjp_10 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setAvcLevelIndication", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "avcLevelIndication", "", "void"), 84);
      ajc$tjp_11 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setLengthSizeMinusOne", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "lengthSizeMinusOne", "", "void"), 88);
      ajc$tjp_12 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setSequenceParameterSets", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "java.util.List", "sequenceParameterSets", "", "void"), 92);
      ajc$tjp_13 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setPictureParameterSets", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "java.util.List", "pictureParameterSets", "", "void"), 96);
      ajc$tjp_14 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getChromaFormat", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 100);
      ajc$tjp_15 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setChromaFormat", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "chromaFormat", "", "void"), 104);
      ajc$tjp_16 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getBitDepthLumaMinus8", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 108);
      ajc$tjp_17 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setBitDepthLumaMinus8", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "bitDepthLumaMinus8", "", "void"), 112);
      ajc$tjp_18 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getBitDepthChromaMinus8", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 116);
      ajc$tjp_19 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setBitDepthChromaMinus8", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "bitDepthChromaMinus8", "", "void"), 120);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getProfileCompatibility", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 52);
      ajc$tjp_20 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getSequenceParameterSetExts", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "java.util.List"), 124);
      ajc$tjp_21 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setSequenceParameterSetExts", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "java.util.List", "sequenceParameterSetExts", "", "void"), 128);
      ajc$tjp_22 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "hasExts", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "boolean"), 132);
      ajc$tjp_23 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setHasExts", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "boolean", "hasExts", "", "void"), 136);
      ajc$tjp_24 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getContentSize", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "long"), 147);
      ajc$tjp_25 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getContent", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "java.nio.ByteBuffer", "byteBuffer", "", "void"), 153);
      ajc$tjp_26 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getSPS", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "[Ljava.lang.String;"), 158);
      ajc$tjp_27 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getPPS", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "[Ljava.lang.String;"), 162);
      ajc$tjp_28 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getavcDecoderConfigurationRecord", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "com.mp4parser.iso14496.part15.AvcDecoderConfigurationRecord"), 167);
      ajc$tjp_29 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "java.lang.String"), 172);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getAvcLevelIndication", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 56);
      ajc$tjp_4 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getLengthSizeMinusOne", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 60);
      ajc$tjp_5 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getSequenceParameterSets", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "java.util.List"), 64);
      ajc$tjp_6 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getPictureParameterSets", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "java.util.List"), 68);
      ajc$tjp_7 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setConfigurationVersion", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "configurationVersion", "", "void"), 72);
      ajc$tjp_8 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setAvcProfileIndication", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "avcProfileIndication", "", "void"), 76);
      ajc$tjp_9 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setProfileCompatibility", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "profileCompatibility", "", "void"), 80);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.avcDecoderConfigurationRecord = new AvcDecoderConfigurationRecord(var1);
   }

   public void getContent(ByteBuffer var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_25, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.getContent(var1);
   }

   public long getContentSize() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_24, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.avcDecoderConfigurationRecord.getContentSize();
   }

   public void setAvcLevelIndication(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_10, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.avcLevelIndication = var1;
   }

   public void setAvcProfileIndication(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_8, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.avcProfileIndication = var1;
   }

   public void setBitDepthChromaMinus8(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_19, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.bitDepthChromaMinus8 = var1;
   }

   public void setBitDepthLumaMinus8(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_17, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.bitDepthLumaMinus8 = var1;
   }

   public void setChromaFormat(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_15, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.chromaFormat = var1;
   }

   public void setConfigurationVersion(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_7, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.configurationVersion = var1;
   }

   public void setLengthSizeMinusOne(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_11, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.lengthSizeMinusOne = var1;
   }

   public void setPictureParameterSets(List var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_13, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.pictureParameterSets = var1;
   }

   public void setProfileCompatibility(int var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_9, this, this, Conversions.intObject(var1));
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.profileCompatibility = var1;
   }

   public void setSequenceParameterSets(List var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_12, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.avcDecoderConfigurationRecord.sequenceParameterSets = var1;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_29, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder("AvcConfigurationBox{avcDecoderConfigurationRecord=");
      var2.append(this.avcDecoderConfigurationRecord);
      var2.append('}');
      return var2.toString();
   }
}
