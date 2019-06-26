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
import java.util.WeakHashMap;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import com.googlecode.mp4parser.AbstractFullBox;

public class TimeToSampleBox extends AbstractFullBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2;
    static Map<List<Entry>, SoftReference<long[]>> cache;
    List<Entry> entries;
    
    static {
        ajc$preClinit();
        TimeToSampleBox.cache = new WeakHashMap<List<Entry>, SoftReference<long[]>>();
    }
    
    public TimeToSampleBox() {
        super("stts");
        this.entries = Collections.emptyList();
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("TimeToSampleBox.java", TimeToSampleBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.TimeToSampleBox", "", "", "", "java.util.List"), 79);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.TimeToSampleBox", "java.util.List", "entries", "", "void"), 83);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.TimeToSampleBox", "", "", "", "java.lang.String"), 87);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.parseVersionAndFlags(byteBuffer);
        final int l2i = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        this.entries = new ArrayList<Entry>(l2i);
        for (int i = 0; i < l2i; ++i) {
            this.entries.add(new Entry(IsoTypeReader.readUInt32(byteBuffer), IsoTypeReader.readUInt32(byteBuffer)));
        }
    }
    
    @Override
    protected void getContent(final ByteBuffer byteBuffer) {
        this.writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.entries.size());
        for (final Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getCount());
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getDelta());
        }
    }
    
    @Override
    protected long getContentSize() {
        return this.entries.size() * 8 + 8;
    }
    
    public void setEntries(final List<Entry> entries) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TimeToSampleBox.ajc$tjp_1, this, this, entries));
        this.entries = entries;
    }
    
    @Override
    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(TimeToSampleBox.ajc$tjp_2, this, this));
        final StringBuilder sb = new StringBuilder("TimeToSampleBox[entryCount=");
        sb.append(this.entries.size());
        sb.append("]");
        return sb.toString();
    }
    
    public static class Entry
    {
        long count;
        long delta;
        
        public Entry(final long count, final long delta) {
            this.count = count;
            this.delta = delta;
        }
        
        public long getCount() {
            return this.count;
        }
        
        public long getDelta() {
            return this.delta;
        }
        
        public void setCount(final long count) {
            this.count = count;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Entry{count=");
            sb.append(this.count);
            sb.append(", delta=");
            sb.append(this.delta);
            sb.append('}');
            return sb.toString();
        }
    }
}
