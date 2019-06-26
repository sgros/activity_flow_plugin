// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes;

import org.aspectj.runtime.internal.Conversions;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.DateHelper;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import com.googlecode.mp4parser.util.Matrix;
import java.util.Date;
import org.aspectj.lang.JoinPoint;
import com.googlecode.mp4parser.AbstractFullBox;

public class TrackHeaderBox extends AbstractFullBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_10;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_11;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_12;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_13;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_14;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_15;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_16;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_17;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_18;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_19;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_20;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_21;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_26;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_27;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_28;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_4;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_6;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_8;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_9;
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
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("TrackHeaderBox.java", TrackHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getCreationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.util.Date"), 60);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getModificationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.util.Date"), 64);
        ajc$tjp_10 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getContent", "com.coremedia.iso.boxes.TrackHeaderBox", "java.nio.ByteBuffer", "byteBuffer", "", "void"), 142);
        ajc$tjp_11 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.lang.String"), 170);
        ajc$tjp_12 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setCreationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "java.util.Date", "creationTime", "", "void"), 196);
        ajc$tjp_13 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setModificationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "java.util.Date", "modificationTime", "", "void"), 203);
        ajc$tjp_14 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setTrackId", "com.coremedia.iso.boxes.TrackHeaderBox", "long", "trackId", "", "void"), 211);
        ajc$tjp_15 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setDuration", "com.coremedia.iso.boxes.TrackHeaderBox", "long", "duration", "", "void"), 215);
        ajc$tjp_16 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setLayer", "com.coremedia.iso.boxes.TrackHeaderBox", "int", "layer", "", "void"), 222);
        ajc$tjp_17 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setAlternateGroup", "com.coremedia.iso.boxes.TrackHeaderBox", "int", "alternateGroup", "", "void"), 226);
        ajc$tjp_18 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setVolume", "com.coremedia.iso.boxes.TrackHeaderBox", "float", "volume", "", "void"), 230);
        ajc$tjp_19 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setMatrix", "com.coremedia.iso.boxes.TrackHeaderBox", "com.googlecode.mp4parser.util.Matrix", "matrix", "", "void"), 234);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getTrackId", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "long"), 68);
        ajc$tjp_20 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setWidth", "com.coremedia.iso.boxes.TrackHeaderBox", "double", "width", "", "void"), 238);
        ajc$tjp_21 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setHeight", "com.coremedia.iso.boxes.TrackHeaderBox", "double", "height", "", "void"), 242);
        ajc$tjp_22 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "isEnabled", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 247);
        ajc$tjp_23 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "isInMovie", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 251);
        ajc$tjp_24 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "isInPreview", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 255);
        ajc$tjp_25 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "isInPoster", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 259);
        ajc$tjp_26 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setEnabled", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "enabled", "", "void"), 263);
        ajc$tjp_27 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setInMovie", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inMovie", "", "void"), 271);
        ajc$tjp_28 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setInPreview", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inPreview", "", "void"), 279);
        ajc$tjp_29 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setInPoster", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inPoster", "", "void"), 287);
        ajc$tjp_3 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getDuration", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "long"), 72);
        ajc$tjp_4 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getLayer", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "int"), 76);
        ajc$tjp_5 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getAlternateGroup", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "int"), 80);
        ajc$tjp_6 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getVolume", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "float"), 84);
        ajc$tjp_7 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getMatrix", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "com.googlecode.mp4parser.util.Matrix"), 88);
        ajc$tjp_8 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getWidth", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "double"), 92);
        ajc$tjp_9 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getHeight", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "double"), 96);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.parseVersionAndFlags(byteBuffer);
        if (this.getVersion() == 1) {
            this.creationTime = DateHelper.convert(IsoTypeReader.readUInt64(byteBuffer));
            this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt64(byteBuffer));
            this.trackId = IsoTypeReader.readUInt32(byteBuffer);
            IsoTypeReader.readUInt32(byteBuffer);
            this.duration = byteBuffer.getLong();
            if (this.duration < -1L) {
                throw new RuntimeException("The tracks duration is bigger than Long.MAX_VALUE");
            }
        }
        else {
            this.creationTime = DateHelper.convert(IsoTypeReader.readUInt32(byteBuffer));
            this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt32(byteBuffer));
            this.trackId = IsoTypeReader.readUInt32(byteBuffer);
            IsoTypeReader.readUInt32(byteBuffer);
            this.duration = IsoTypeReader.readUInt32(byteBuffer);
        }
        IsoTypeReader.readUInt32(byteBuffer);
        IsoTypeReader.readUInt32(byteBuffer);
        this.layer = IsoTypeReader.readUInt16(byteBuffer);
        this.alternateGroup = IsoTypeReader.readUInt16(byteBuffer);
        this.volume = IsoTypeReader.readFixedPoint88(byteBuffer);
        IsoTypeReader.readUInt16(byteBuffer);
        this.matrix = Matrix.fromByteBuffer(byteBuffer);
        this.width = IsoTypeReader.readFixedPoint1616(byteBuffer);
        this.height = IsoTypeReader.readFixedPoint1616(byteBuffer);
    }
    
    public int getAlternateGroup() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_5, this, this));
        return this.alternateGroup;
    }
    
    public void getContent(final ByteBuffer byteBuffer) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_10, this, this, byteBuffer));
        this.writeVersionAndFlags(byteBuffer);
        if (this.getVersion() == 1) {
            IsoTypeWriter.writeUInt64(byteBuffer, DateHelper.convert(this.creationTime));
            IsoTypeWriter.writeUInt64(byteBuffer, DateHelper.convert(this.modificationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, this.trackId);
            IsoTypeWriter.writeUInt32(byteBuffer, 0L);
            IsoTypeWriter.writeUInt64(byteBuffer, this.duration);
        }
        else {
            IsoTypeWriter.writeUInt32(byteBuffer, DateHelper.convert(this.creationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, DateHelper.convert(this.modificationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, this.trackId);
            IsoTypeWriter.writeUInt32(byteBuffer, 0L);
            IsoTypeWriter.writeUInt32(byteBuffer, this.duration);
        }
        IsoTypeWriter.writeUInt32(byteBuffer, 0L);
        IsoTypeWriter.writeUInt32(byteBuffer, 0L);
        IsoTypeWriter.writeUInt16(byteBuffer, this.layer);
        IsoTypeWriter.writeUInt16(byteBuffer, this.alternateGroup);
        IsoTypeWriter.writeFixedPoint88(byteBuffer, this.volume);
        IsoTypeWriter.writeUInt16(byteBuffer, 0);
        this.matrix.getContent(byteBuffer);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.width);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.height);
    }
    
    @Override
    protected long getContentSize() {
        long n;
        if (this.getVersion() == 1) {
            n = 36L;
        }
        else {
            n = 24L;
        }
        return n + 60L;
    }
    
    public Date getCreationTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_0, this, this));
        return this.creationTime;
    }
    
    public long getDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_3, this, this));
        return this.duration;
    }
    
    public double getHeight() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_9, this, this));
        return this.height;
    }
    
    public int getLayer() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_4, this, this));
        return this.layer;
    }
    
    public Date getModificationTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_1, this, this));
        return this.modificationTime;
    }
    
    public long getTrackId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_2, this, this));
        return this.trackId;
    }
    
    public float getVolume() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_6, this, this));
        return this.volume;
    }
    
    public double getWidth() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_8, this, this));
        return this.width;
    }
    
    public void setAlternateGroup(final int alternateGroup) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_17, this, this, Conversions.intObject(alternateGroup)));
        this.alternateGroup = alternateGroup;
    }
    
    public void setCreationTime(final Date creationTime) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_12, this, this, creationTime));
        this.creationTime = creationTime;
        if (DateHelper.convert(creationTime) >= 4294967296L) {
            this.setVersion(1);
        }
    }
    
    public void setDuration(final long duration) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_15, this, this, Conversions.longObject(duration)));
        this.duration = duration;
        if (duration >= 4294967296L) {
            this.setFlags(1);
        }
    }
    
    public void setEnabled(final boolean b) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_26, this, this, Conversions.booleanObject(b)));
        if (b) {
            this.setFlags(this.getFlags() | 0x1);
        }
        else {
            this.setFlags(this.getFlags() & 0xFFFFFFFE);
        }
    }
    
    public void setHeight(final double height) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_21, this, this, Conversions.doubleObject(height)));
        this.height = height;
    }
    
    public void setInMovie(final boolean b) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_27, this, this, Conversions.booleanObject(b)));
        if (b) {
            this.setFlags(this.getFlags() | 0x2);
        }
        else {
            this.setFlags(this.getFlags() & 0xFFFFFFFD);
        }
    }
    
    public void setInPreview(final boolean b) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_28, this, this, Conversions.booleanObject(b)));
        if (b) {
            this.setFlags(this.getFlags() | 0x4);
        }
        else {
            this.setFlags(this.getFlags() & 0xFFFFFFFB);
        }
    }
    
    public void setLayer(final int layer) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_16, this, this, Conversions.intObject(layer)));
        this.layer = layer;
    }
    
    public void setMatrix(final Matrix matrix) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_19, this, this, matrix));
        this.matrix = matrix;
    }
    
    public void setModificationTime(final Date modificationTime) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_13, this, this, modificationTime));
        this.modificationTime = modificationTime;
        if (DateHelper.convert(modificationTime) >= 4294967296L) {
            this.setVersion(1);
        }
    }
    
    public void setTrackId(final long trackId) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_14, this, this, Conversions.longObject(trackId)));
        this.trackId = trackId;
    }
    
    public void setVolume(final float volume) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_18, this, this, Conversions.floatObject(volume)));
        this.volume = volume;
    }
    
    public void setWidth(final double width) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_20, this, this, Conversions.doubleObject(width)));
        this.width = width;
    }
    
    @Override
    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TrackHeaderBox.ajc$tjp_11, this, this));
        final StringBuilder sb = new StringBuilder();
        sb.append("TrackHeaderBox[");
        sb.append("creationTime=");
        sb.append(this.getCreationTime());
        sb.append(";");
        sb.append("modificationTime=");
        sb.append(this.getModificationTime());
        sb.append(";");
        sb.append("trackId=");
        sb.append(this.getTrackId());
        sb.append(";");
        sb.append("duration=");
        sb.append(this.getDuration());
        sb.append(";");
        sb.append("layer=");
        sb.append(this.getLayer());
        sb.append(";");
        sb.append("alternateGroup=");
        sb.append(this.getAlternateGroup());
        sb.append(";");
        sb.append("volume=");
        sb.append(this.getVolume());
        sb.append(";");
        sb.append("matrix=");
        sb.append(this.matrix);
        sb.append(";");
        sb.append("width=");
        sb.append(this.getWidth());
        sb.append(";");
        sb.append("height=");
        sb.append(this.getHeight());
        sb.append("]");
        return sb.toString();
    }
}
