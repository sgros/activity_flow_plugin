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

public class CompositionTimeToSample extends AbstractFullBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    List<Entry> entries;
    
    static {
        ajc$preClinit();
    }
    
    public CompositionTimeToSample() {
        super("ctts");
        this.entries = Collections.emptyList();
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("CompositionTimeToSample.java", CompositionTimeToSample.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.CompositionTimeToSample", "", "", "", "java.util.List"), 57);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.CompositionTimeToSample", "java.util.List", "entries", "", "void"), 61);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.parseVersionAndFlags(byteBuffer);
        final int l2i = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        this.entries = new ArrayList<Entry>(l2i);
        for (int i = 0; i < l2i; ++i) {
            this.entries.add(new Entry(CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer)), byteBuffer.getInt()));
        }
    }
    
    @Override
    protected void getContent(final ByteBuffer byteBuffer) {
        this.writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.entries.size());
        for (final Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getCount());
            byteBuffer.putInt(entry.getOffset());
        }
    }
    
    @Override
    protected long getContentSize() {
        return this.entries.size() * 8 + 8;
    }
    
    public void setEntries(final List<Entry> entries) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(CompositionTimeToSample.ajc$tjp_1, this, this, entries));
        this.entries = entries;
    }
    
    public static class Entry
    {
        int count;
        int offset;
        
        public Entry(final int count, final int offset) {
            this.count = count;
            this.offset = offset;
        }
        
        public int getCount() {
            return this.count;
        }
        
        public int getOffset() {
            return this.offset;
        }
        
        public void setCount(final int count) {
            this.count = count;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Entry{count=");
            sb.append(this.count);
            sb.append(", offset=");
            sb.append(this.offset);
            sb.append('}');
            return sb.toString();
        }
    }
}
