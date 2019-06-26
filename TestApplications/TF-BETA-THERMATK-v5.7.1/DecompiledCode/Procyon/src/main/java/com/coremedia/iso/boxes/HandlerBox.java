// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.coremedia.iso.Utf8;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import com.googlecode.mp4parser.AbstractFullBox;

public class HandlerBox extends AbstractFullBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5;
    public static final Map<String, String> readableTypes;
    private long a;
    private long b;
    private long c;
    private String handlerType;
    private String name;
    private long shouldBeZeroButAppleWritesHereSomeValue;
    private boolean zeroTerm;
    
    static {
        ajc$preClinit();
        final HashMap<String, String> m = new HashMap<String, String>();
        m.put("odsm", "ObjectDescriptorStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
        m.put("crsm", "ClockReferenceStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
        m.put("sdsm", "SceneDescriptionStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
        m.put("m7sm", "MPEG7Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
        m.put("ocsm", "ObjectContentInfoStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
        m.put("ipsm", "IPMP Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
        m.put("mjsm", "MPEG-J Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
        m.put("mdir", "Apple Meta Data iTunes Reader");
        m.put("mp7b", "MPEG-7 binary XML");
        m.put("mp7t", "MPEG-7 XML");
        m.put("vide", "Video Track");
        m.put("soun", "Sound Track");
        m.put("hint", "Hint Track");
        m.put("appl", "Apple specific");
        m.put("meta", "Timed Metadata track - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO");
        readableTypes = Collections.unmodifiableMap((Map<?, ?>)m);
    }
    
    public HandlerBox() {
        super("hdlr");
        this.name = null;
        this.zeroTerm = true;
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("HandlerBox.java", HandlerBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getHandlerType", "com.coremedia.iso.boxes.HandlerBox", "", "", "", "java.lang.String"), 78);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setName", "com.coremedia.iso.boxes.HandlerBox", "java.lang.String", "name", "", "void"), 87);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setHandlerType", "com.coremedia.iso.boxes.HandlerBox", "java.lang.String", "handlerType", "", "void"), 91);
        ajc$tjp_3 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getName", "com.coremedia.iso.boxes.HandlerBox", "", "", "", "java.lang.String"), 95);
        ajc$tjp_4 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getHumanReadableTrackType", "com.coremedia.iso.boxes.HandlerBox", "", "", "", "java.lang.String"), 99);
        ajc$tjp_5 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.HandlerBox", "", "", "", "java.lang.String"), 149);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.parseVersionAndFlags(byteBuffer);
        this.shouldBeZeroButAppleWritesHereSomeValue = IsoTypeReader.readUInt32(byteBuffer);
        this.handlerType = IsoTypeReader.read4cc(byteBuffer);
        this.a = IsoTypeReader.readUInt32(byteBuffer);
        this.b = IsoTypeReader.readUInt32(byteBuffer);
        this.c = IsoTypeReader.readUInt32(byteBuffer);
        if (byteBuffer.remaining() > 0) {
            this.name = IsoTypeReader.readString(byteBuffer, byteBuffer.remaining());
            if (this.name.endsWith("\u0000")) {
                final String name = this.name;
                this.name = name.substring(0, name.length() - 1);
                this.zeroTerm = true;
            }
            else {
                this.zeroTerm = false;
            }
        }
        else {
            this.zeroTerm = false;
        }
    }
    
    @Override
    protected void getContent(final ByteBuffer byteBuffer) {
        this.writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.shouldBeZeroButAppleWritesHereSomeValue);
        byteBuffer.put(IsoFile.fourCCtoBytes(this.handlerType));
        IsoTypeWriter.writeUInt32(byteBuffer, this.a);
        IsoTypeWriter.writeUInt32(byteBuffer, this.b);
        IsoTypeWriter.writeUInt32(byteBuffer, this.c);
        final String name = this.name;
        if (name != null) {
            byteBuffer.put(Utf8.convert(name));
        }
        if (this.zeroTerm) {
            byteBuffer.put((byte)0);
        }
    }
    
    @Override
    protected long getContentSize() {
        int n;
        if (this.zeroTerm) {
            n = Utf8.utf8StringLengthInBytes(this.name) + 25;
        }
        else {
            n = Utf8.utf8StringLengthInBytes(this.name) + 24;
        }
        return n;
    }
    
    public String getHandlerType() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(HandlerBox.ajc$tjp_0, this, this));
        return this.handlerType;
    }
    
    public String getName() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(HandlerBox.ajc$tjp_3, this, this));
        return this.name;
    }
    
    public void setHandlerType(final String handlerType) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(HandlerBox.ajc$tjp_2, this, this, handlerType));
        this.handlerType = handlerType;
    }
    
    public void setName(final String name) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(HandlerBox.ajc$tjp_1, this, this, name));
        this.name = name;
    }
    
    @Override
    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(HandlerBox.ajc$tjp_5, this, this));
        final StringBuilder sb = new StringBuilder("HandlerBox[handlerType=");
        sb.append(this.getHandlerType());
        sb.append(";name=");
        sb.append(this.getName());
        sb.append("]");
        return sb.toString();
    }
}
