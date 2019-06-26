// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import org.aspectj.lang.JoinPoint;

public class StaticChunkOffsetBox extends ChunkOffsetBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    private long[] chunkOffsets;
    
    static {
        ajc$preClinit();
    }
    
    public StaticChunkOffsetBox() {
        super("stco");
        this.chunkOffsets = new long[0];
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("StaticChunkOffsetBox.java", StaticChunkOffsetBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getChunkOffsets", "com.coremedia.iso.boxes.StaticChunkOffsetBox", "", "", "", "[J"), 39);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setChunkOffsets", "com.coremedia.iso.boxes.StaticChunkOffsetBox", "[J", "chunkOffsets", "", "void"), 48);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.parseVersionAndFlags(byteBuffer);
        final int l2i = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        this.chunkOffsets = new long[l2i];
        for (int i = 0; i < l2i; ++i) {
            this.chunkOffsets[i] = IsoTypeReader.readUInt32(byteBuffer);
        }
    }
    
    @Override
    public long[] getChunkOffsets() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(StaticChunkOffsetBox.ajc$tjp_0, this, this));
        return this.chunkOffsets;
    }
    
    @Override
    protected void getContent(final ByteBuffer byteBuffer) {
        this.writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.chunkOffsets.length);
        final long[] chunkOffsets = this.chunkOffsets;
        for (int length = chunkOffsets.length, i = 0; i < length; ++i) {
            IsoTypeWriter.writeUInt32(byteBuffer, chunkOffsets[i]);
        }
    }
    
    @Override
    protected long getContentSize() {
        return this.chunkOffsets.length * 4 + 8;
    }
    
    public void setChunkOffsets(final long[] chunkOffsets) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(StaticChunkOffsetBox.ajc$tjp_1, this, this, chunkOffsets));
        this.chunkOffsets = chunkOffsets;
    }
}
