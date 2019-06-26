// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.util.Iterator;
import com.coremedia.iso.IsoTypeWriter;
import java.util.ArrayList;
import com.googlecode.mp4parser.util.CastUtils;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import java.util.Collections;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import com.googlecode.mp4parser.AbstractFullBox;

public class SampleToChunkBox extends AbstractFullBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2;
    List<Entry> entries;
    
    static {
        ajc$preClinit();
    }
    
    public SampleToChunkBox() {
        super("stsc");
        this.entries = Collections.emptyList();
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("SampleToChunkBox.java", SampleToChunkBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.SampleToChunkBox", "", "", "", "java.util.List"), 47);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.SampleToChunkBox", "java.util.List", "entries", "", "void"), 51);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SampleToChunkBox", "", "", "", "java.lang.String"), 84);
        ajc$tjp_3 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "blowup", "com.coremedia.iso.boxes.SampleToChunkBox", "int", "chunkCount", "", "[J"), 95);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.parseVersionAndFlags(byteBuffer);
        final int l2i = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        this.entries = new ArrayList<Entry>(l2i);
        for (int i = 0; i < l2i; ++i) {
            this.entries.add(new Entry(IsoTypeReader.readUInt32(byteBuffer), IsoTypeReader.readUInt32(byteBuffer), IsoTypeReader.readUInt32(byteBuffer)));
        }
    }
    
    @Override
    protected void getContent(final ByteBuffer byteBuffer) {
        this.writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.entries.size());
        for (final Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getFirstChunk());
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getSamplesPerChunk());
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getSampleDescriptionIndex());
        }
    }
    
    @Override
    protected long getContentSize() {
        return this.entries.size() * 12 + 8;
    }
    
    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(SampleToChunkBox.ajc$tjp_0, this, this));
        return this.entries;
    }
    
    public void setEntries(final List<Entry> entries) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(SampleToChunkBox.ajc$tjp_1, this, this, entries));
        this.entries = entries;
    }
    
    @Override
    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(SampleToChunkBox.ajc$tjp_2, this, this));
        final StringBuilder sb = new StringBuilder("SampleToChunkBox[entryCount=");
        sb.append(this.entries.size());
        sb.append("]");
        return sb.toString();
    }
    
    public static class Entry
    {
        long firstChunk;
        long sampleDescriptionIndex;
        long samplesPerChunk;
        
        public Entry(final long firstChunk, final long samplesPerChunk, final long sampleDescriptionIndex) {
            this.firstChunk = firstChunk;
            this.samplesPerChunk = samplesPerChunk;
            this.sampleDescriptionIndex = sampleDescriptionIndex;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o != null && Entry.class == o.getClass()) {
                final Entry entry = (Entry)o;
                return this.firstChunk == entry.firstChunk && this.sampleDescriptionIndex == entry.sampleDescriptionIndex && this.samplesPerChunk == entry.samplesPerChunk;
            }
            return false;
        }
        
        public long getFirstChunk() {
            return this.firstChunk;
        }
        
        public long getSampleDescriptionIndex() {
            return this.sampleDescriptionIndex;
        }
        
        public long getSamplesPerChunk() {
            return this.samplesPerChunk;
        }
        
        @Override
        public int hashCode() {
            final long firstChunk = this.firstChunk;
            final int n = (int)(firstChunk ^ firstChunk >>> 32);
            final long samplesPerChunk = this.samplesPerChunk;
            final int n2 = (int)(samplesPerChunk ^ samplesPerChunk >>> 32);
            final long sampleDescriptionIndex = this.sampleDescriptionIndex;
            return (n * 31 + n2) * 31 + (int)(sampleDescriptionIndex ^ sampleDescriptionIndex >>> 32);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Entry{firstChunk=");
            sb.append(this.firstChunk);
            sb.append(", samplesPerChunk=");
            sb.append(this.samplesPerChunk);
            sb.append(", sampleDescriptionIndex=");
            sb.append(this.sampleDescriptionIndex);
            sb.append('}');
            return sb.toString();
        }
    }
}
