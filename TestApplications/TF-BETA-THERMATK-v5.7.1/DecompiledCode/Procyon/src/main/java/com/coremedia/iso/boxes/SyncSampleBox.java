// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.util.CastUtils;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import org.aspectj.lang.JoinPoint;
import com.googlecode.mp4parser.AbstractFullBox;

public class SyncSampleBox extends AbstractFullBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2;
    private long[] sampleNumber;
    
    static {
        ajc$preClinit();
    }
    
    public SyncSampleBox() {
        super("stss");
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("SyncSampleBox.java", SyncSampleBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getSampleNumber", "com.coremedia.iso.boxes.SyncSampleBox", "", "", "", "[J"), 46);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SyncSampleBox", "", "", "", "java.lang.String"), 77);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setSampleNumber", "com.coremedia.iso.boxes.SyncSampleBox", "[J", "sampleNumber", "", "void"), 81);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.parseVersionAndFlags(byteBuffer);
        final int l2i = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        this.sampleNumber = new long[l2i];
        for (int i = 0; i < l2i; ++i) {
            this.sampleNumber[i] = IsoTypeReader.readUInt32(byteBuffer);
        }
    }
    
    @Override
    protected void getContent(final ByteBuffer byteBuffer) {
        this.writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.sampleNumber.length);
        final long[] sampleNumber = this.sampleNumber;
        for (int length = sampleNumber.length, i = 0; i < length; ++i) {
            IsoTypeWriter.writeUInt32(byteBuffer, sampleNumber[i]);
        }
    }
    
    @Override
    protected long getContentSize() {
        return this.sampleNumber.length * 4 + 8;
    }
    
    public void setSampleNumber(final long[] sampleNumber) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(SyncSampleBox.ajc$tjp_2, this, this, sampleNumber));
        this.sampleNumber = sampleNumber;
    }
    
    @Override
    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(SyncSampleBox.ajc$tjp_1, this, this));
        final StringBuilder sb = new StringBuilder("SyncSampleBox[entryCount=");
        sb.append(this.sampleNumber.length);
        sb.append("]");
        return sb.toString();
    }
}
