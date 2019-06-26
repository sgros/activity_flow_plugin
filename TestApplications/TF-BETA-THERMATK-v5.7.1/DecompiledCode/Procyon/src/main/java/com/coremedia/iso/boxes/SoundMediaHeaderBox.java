// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import org.aspectj.lang.JoinPoint;

public class SoundMediaHeaderBox extends AbstractMediaHeaderBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    private float balance;
    
    static {
        ajc$preClinit();
    }
    
    public SoundMediaHeaderBox() {
        super("smhd");
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("SoundMediaHeaderBox.java", SoundMediaHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getBalance", "com.coremedia.iso.boxes.SoundMediaHeaderBox", "", "", "", "float"), 36);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SoundMediaHeaderBox", "", "", "", "java.lang.String"), 58);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.parseVersionAndFlags(byteBuffer);
        this.balance = IsoTypeReader.readFixedPoint88(byteBuffer);
        IsoTypeReader.readUInt16(byteBuffer);
    }
    
    public float getBalance() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(SoundMediaHeaderBox.ajc$tjp_0, this, this));
        return this.balance;
    }
    
    @Override
    protected void getContent(final ByteBuffer byteBuffer) {
        this.writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeFixedPoint88(byteBuffer, this.balance);
        IsoTypeWriter.writeUInt16(byteBuffer, 0);
    }
    
    @Override
    protected long getContentSize() {
        return 8L;
    }
    
    @Override
    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(SoundMediaHeaderBox.ajc$tjp_1, this, this));
        final StringBuilder sb = new StringBuilder("SoundMediaHeaderBox[balance=");
        sb.append(this.getBalance());
        sb.append("]");
        return sb.toString();
    }
}
