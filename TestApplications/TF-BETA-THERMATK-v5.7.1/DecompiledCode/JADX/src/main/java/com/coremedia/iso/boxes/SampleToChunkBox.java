package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.runtime.reflect.Factory;

public class SampleToChunkBox extends AbstractFullBox {
    private static final /* synthetic */ StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ StaticPart ajc$tjp_3 = null;
    List<Entry> entries = Collections.emptyList();

    public static class Entry {
        long firstChunk;
        long sampleDescriptionIndex;
        long samplesPerChunk;

        public Entry(long j, long j2, long j3) {
            this.firstChunk = j;
            this.samplesPerChunk = j2;
            this.sampleDescriptionIndex = j3;
        }

        public long getFirstChunk() {
            return this.firstChunk;
        }

        public long getSamplesPerChunk() {
            return this.samplesPerChunk;
        }

        public long getSampleDescriptionIndex() {
            return this.sampleDescriptionIndex;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("Entry{firstChunk=");
            stringBuilder.append(this.firstChunk);
            stringBuilder.append(", samplesPerChunk=");
            stringBuilder.append(this.samplesPerChunk);
            stringBuilder.append(", sampleDescriptionIndex=");
            stringBuilder.append(this.sampleDescriptionIndex);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || Entry.class != obj.getClass()) {
                return false;
            }
            Entry entry = (Entry) obj;
            return this.firstChunk == entry.firstChunk && this.sampleDescriptionIndex == entry.sampleDescriptionIndex && this.samplesPerChunk == entry.samplesPerChunk;
        }

        public int hashCode() {
            long j = this.firstChunk;
            int i = ((int) (j ^ (j >>> 32))) * 31;
            long j2 = this.samplesPerChunk;
            i = (i + ((int) (j2 ^ (j2 >>> 32)))) * 31;
            j2 = this.sampleDescriptionIndex;
            return i + ((int) (j2 ^ (j2 >>> 32)));
        }
    }

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("SampleToChunkBox.java", SampleToChunkBox.class);
        String str = "method-execution";
        ajc$tjp_0 = factory.makeSJP(str, factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.SampleToChunkBox", "", "", "", "java.util.List"), 47);
        ajc$tjp_1 = factory.makeSJP(str, factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.SampleToChunkBox", "java.util.List", "entries", "", "void"), 51);
        ajc$tjp_2 = factory.makeSJP(str, factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SampleToChunkBox", "", "", "", "java.lang.String"), 84);
        ajc$tjp_3 = factory.makeSJP(str, factory.makeMethodSig("1", "blowup", "com.coremedia.iso.boxes.SampleToChunkBox", "int", "chunkCount", "", "[J"), 95);
    }

    public SampleToChunkBox() {
        super("stsc");
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this, list));
        this.entries = list;
    }

    /* Access modifiers changed, original: protected */
    public long getContentSize() {
        return (long) ((this.entries.size() * 12) + 8);
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        int l2i = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        this.entries = new ArrayList(l2i);
        for (int i = 0; i < l2i; i++) {
            this.entries.add(new Entry(IsoTypeReader.readUInt32(byteBuffer), IsoTypeReader.readUInt32(byteBuffer), IsoTypeReader.readUInt32(byteBuffer)));
        }
    }

    /* Access modifiers changed, original: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getFirstChunk());
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getSamplesPerChunk());
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getSampleDescriptionIndex());
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        StringBuilder stringBuilder = new StringBuilder("SampleToChunkBox[entryCount=");
        stringBuilder.append(this.entries.size());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
