// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser;

import com.coremedia.iso.IsoTypeWriter;
import org.aspectj.runtime.internal.Conversions;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import org.aspectj.lang.JoinPoint;
import com.coremedia.iso.boxes.FullBox;

public abstract class AbstractFullBox extends AbstractBox implements FullBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    private int flags;
    private int version;
    
    static {
        ajc$preClinit();
    }
    
    protected AbstractFullBox(final String s) {
        super(s);
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("AbstractFullBox.java", AbstractFullBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setVersion", "com.googlecode.mp4parser.AbstractFullBox", "int", "version", "", "void"), 51);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setFlags", "com.googlecode.mp4parser.AbstractFullBox", "int", "flags", "", "void"), 64);
    }
    
    public int getFlags() {
        if (!super.isParsed) {
            this.parseDetails();
        }
        return this.flags;
    }
    
    public int getVersion() {
        if (!super.isParsed) {
            this.parseDetails();
        }
        return this.version;
    }
    
    protected final long parseVersionAndFlags(final ByteBuffer byteBuffer) {
        this.version = IsoTypeReader.readUInt8(byteBuffer);
        this.flags = IsoTypeReader.readUInt24(byteBuffer);
        return 4L;
    }
    
    public void setFlags(final int flags) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AbstractFullBox.ajc$tjp_1, this, this, Conversions.intObject(flags)));
        this.flags = flags;
    }
    
    public void setVersion(final int version) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(AbstractFullBox.ajc$tjp_0, this, this, Conversions.intObject(version)));
        this.version = version;
    }
    
    protected final void writeVersionAndFlags(final ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt8(byteBuffer, this.version);
        IsoTypeWriter.writeUInt24(byteBuffer, this.flags);
    }
}
