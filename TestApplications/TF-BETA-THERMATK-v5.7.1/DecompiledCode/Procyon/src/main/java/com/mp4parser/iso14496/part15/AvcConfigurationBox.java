// 
// Decompiled by Procyon v0.5.34
// 

package com.mp4parser.iso14496.part15;

import java.util.List;
import org.aspectj.runtime.internal.Conversions;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import org.aspectj.lang.JoinPoint;
import com.googlecode.mp4parser.AbstractBox;

public final class AvcConfigurationBox extends AbstractBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_10;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_11;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_12;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_13;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_15;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_17;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_19;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_24;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_25;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_29;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_7;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_8;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_9;
    public AvcDecoderConfigurationRecord avcDecoderConfigurationRecord;
    
    static {
        ajc$preClinit();
    }
    
    public AvcConfigurationBox() {
        super("avcC");
        this.avcDecoderConfigurationRecord = new AvcDecoderConfigurationRecord();
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("AvcConfigurationBox.java", AvcConfigurationBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getConfigurationVersion", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 44);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getAvcProfileIndication", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 48);
        ajc$tjp_10 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setAvcLevelIndication", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "avcLevelIndication", "", "void"), 84);
        ajc$tjp_11 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setLengthSizeMinusOne", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "lengthSizeMinusOne", "", "void"), 88);
        ajc$tjp_12 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setSequenceParameterSets", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "java.util.List", "sequenceParameterSets", "", "void"), 92);
        ajc$tjp_13 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setPictureParameterSets", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "java.util.List", "pictureParameterSets", "", "void"), 96);
        ajc$tjp_14 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getChromaFormat", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 100);
        ajc$tjp_15 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setChromaFormat", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "chromaFormat", "", "void"), 104);
        ajc$tjp_16 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getBitDepthLumaMinus8", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 108);
        ajc$tjp_17 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setBitDepthLumaMinus8", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "bitDepthLumaMinus8", "", "void"), 112);
        ajc$tjp_18 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getBitDepthChromaMinus8", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 116);
        ajc$tjp_19 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setBitDepthChromaMinus8", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "bitDepthChromaMinus8", "", "void"), 120);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getProfileCompatibility", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 52);
        ajc$tjp_20 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getSequenceParameterSetExts", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "java.util.List"), 124);
        ajc$tjp_21 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setSequenceParameterSetExts", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "java.util.List", "sequenceParameterSetExts", "", "void"), 128);
        ajc$tjp_22 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "hasExts", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "boolean"), 132);
        ajc$tjp_23 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setHasExts", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "boolean", "hasExts", "", "void"), 136);
        ajc$tjp_24 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getContentSize", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "long"), 147);
        ajc$tjp_25 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getContent", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "java.nio.ByteBuffer", "byteBuffer", "", "void"), 153);
        ajc$tjp_26 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getSPS", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "[Ljava.lang.String;"), 158);
        ajc$tjp_27 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getPPS", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "[Ljava.lang.String;"), 162);
        ajc$tjp_28 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getavcDecoderConfigurationRecord", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "com.mp4parser.iso14496.part15.AvcDecoderConfigurationRecord"), 167);
        ajc$tjp_29 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "toString", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "java.lang.String"), 172);
        ajc$tjp_3 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getAvcLevelIndication", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 56);
        ajc$tjp_4 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getLengthSizeMinusOne", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "int"), 60);
        ajc$tjp_5 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getSequenceParameterSets", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "java.util.List"), 64);
        ajc$tjp_6 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getPictureParameterSets", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "", "", "", "java.util.List"), 68);
        ajc$tjp_7 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setConfigurationVersion", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "configurationVersion", "", "void"), 72);
        ajc$tjp_8 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setAvcProfileIndication", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "avcProfileIndication", "", "void"), 76);
        ajc$tjp_9 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setProfileCompatibility", "com.mp4parser.iso14496.part15.AvcConfigurationBox", "int", "profileCompatibility", "", "void"), 80);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.avcDecoderConfigurationRecord = new AvcDecoderConfigurationRecord(byteBuffer);
    }
    
    public void getContent(final ByteBuffer byteBuffer) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_25, this, this, byteBuffer));
        this.avcDecoderConfigurationRecord.getContent(byteBuffer);
    }
    
    public long getContentSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_24, this, this));
        return this.avcDecoderConfigurationRecord.getContentSize();
    }
    
    public void setAvcLevelIndication(final int avcLevelIndication) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_10, this, this, Conversions.intObject(avcLevelIndication)));
        this.avcDecoderConfigurationRecord.avcLevelIndication = avcLevelIndication;
    }
    
    public void setAvcProfileIndication(final int avcProfileIndication) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_8, this, this, Conversions.intObject(avcProfileIndication)));
        this.avcDecoderConfigurationRecord.avcProfileIndication = avcProfileIndication;
    }
    
    public void setBitDepthChromaMinus8(final int bitDepthChromaMinus8) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_19, this, this, Conversions.intObject(bitDepthChromaMinus8)));
        this.avcDecoderConfigurationRecord.bitDepthChromaMinus8 = bitDepthChromaMinus8;
    }
    
    public void setBitDepthLumaMinus8(final int bitDepthLumaMinus8) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_17, this, this, Conversions.intObject(bitDepthLumaMinus8)));
        this.avcDecoderConfigurationRecord.bitDepthLumaMinus8 = bitDepthLumaMinus8;
    }
    
    public void setChromaFormat(final int chromaFormat) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_15, this, this, Conversions.intObject(chromaFormat)));
        this.avcDecoderConfigurationRecord.chromaFormat = chromaFormat;
    }
    
    public void setConfigurationVersion(final int configurationVersion) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_7, this, this, Conversions.intObject(configurationVersion)));
        this.avcDecoderConfigurationRecord.configurationVersion = configurationVersion;
    }
    
    public void setLengthSizeMinusOne(final int lengthSizeMinusOne) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_11, this, this, Conversions.intObject(lengthSizeMinusOne)));
        this.avcDecoderConfigurationRecord.lengthSizeMinusOne = lengthSizeMinusOne;
    }
    
    public void setPictureParameterSets(final List<byte[]> pictureParameterSets) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_13, this, this, pictureParameterSets));
        this.avcDecoderConfigurationRecord.pictureParameterSets = pictureParameterSets;
    }
    
    public void setProfileCompatibility(final int profileCompatibility) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_9, this, this, Conversions.intObject(profileCompatibility)));
        this.avcDecoderConfigurationRecord.profileCompatibility = profileCompatibility;
    }
    
    public void setSequenceParameterSets(final List<byte[]> sequenceParameterSets) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_12, this, this, sequenceParameterSets));
        this.avcDecoderConfigurationRecord.sequenceParameterSets = sequenceParameterSets;
    }
    
    @Override
    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AvcConfigurationBox.ajc$tjp_29, this, this));
        final StringBuilder sb = new StringBuilder("AvcConfigurationBox{avcDecoderConfigurationRecord=");
        sb.append(this.avcDecoderConfigurationRecord);
        sb.append('}');
        return sb.toString();
    }
}
