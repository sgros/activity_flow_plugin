// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.video;

import com.coremedia.iso.BoxParser;
import com.googlecode.mp4parser.DataSource;
import java.io.IOException;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Container;
import android.media.MediaCodec$BufferInfo;
import com.coremedia.iso.boxes.DataEntryUrlBox;
import com.coremedia.iso.boxes.DataReferenceBox;
import com.coremedia.iso.boxes.DataInformationBox;
import com.coremedia.iso.boxes.MediaInformationBox;
import com.coremedia.iso.boxes.HandlerBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SyncSampleBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import java.util.Iterator;
import com.googlecode.mp4parser.util.Matrix;
import java.util.Date;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.MovieBox;
import java.util.LinkedList;
import com.coremedia.iso.boxes.FileTypeBox;
import com.coremedia.iso.boxes.Box;
import java.util.List;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import java.util.ArrayList;
import com.coremedia.iso.boxes.SampleTableBox;
import android.media.MediaFormat;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class MP4Builder
{
    private Mp4Movie currentMp4Movie;
    private long dataOffset;
    private FileChannel fc;
    private FileOutputStream fos;
    private InterleaveChunkMdat mdat;
    private ByteBuffer sizeBuffer;
    private boolean splitMdat;
    private HashMap<Track, long[]> track2SampleSizes;
    private boolean wasFirstVideoFrame;
    private boolean writeNewMdat;
    private long wroteSinceLastMdat;
    
    public MP4Builder() {
        this.mdat = null;
        this.currentMp4Movie = null;
        this.fos = null;
        this.fc = null;
        this.dataOffset = 0L;
        this.wroteSinceLastMdat = 0L;
        this.writeNewMdat = true;
        this.track2SampleSizes = new HashMap<Track, long[]>();
        this.sizeBuffer = null;
    }
    
    private void flushCurrentMdat() throws Exception {
        final long position = this.fc.position();
        this.fc.position(this.mdat.getOffset());
        this.mdat.getBox(this.fc);
        this.fc.position(position);
        this.mdat.setDataOffset(0L);
        this.mdat.setContentSize(0L);
        this.fos.flush();
        this.fos.getFD().sync();
    }
    
    public static long gcd(final long n, final long n2) {
        if (n2 == 0L) {
            return n;
        }
        return gcd(n2, n % n2);
    }
    
    public int addTrack(final MediaFormat mediaFormat, final boolean b) {
        return this.currentMp4Movie.addTrack(mediaFormat, b);
    }
    
    protected void createCtts(final Track track, final SampleTableBox sampleTableBox) {
        final int[] sampleCompositions = track.getSampleCompositions();
        if (sampleCompositions == null) {
            return;
        }
        CompositionTimeToSample.Entry entry = null;
        final ArrayList<CompositionTimeToSample.Entry> entries = new ArrayList<CompositionTimeToSample.Entry>();
        for (int i = 0; i < sampleCompositions.length; ++i) {
            final int n = sampleCompositions[i];
            if (entry != null && entry.getOffset() == n) {
                entry.setCount(entry.getCount() + 1);
            }
            else {
                entry = new CompositionTimeToSample.Entry(1, n);
                entries.add(entry);
            }
        }
        final CompositionTimeToSample compositionTimeToSample = new CompositionTimeToSample();
        compositionTimeToSample.setEntries(entries);
        sampleTableBox.addBox(compositionTimeToSample);
    }
    
    protected FileTypeBox createFileTypeBox() {
        final LinkedList<String> list = new LinkedList<String>();
        list.add("isom");
        list.add("iso2");
        list.add("avc1");
        list.add("mp41");
        return new FileTypeBox("isom", 512L, list);
    }
    
    public MP4Builder createMovie(final Mp4Movie currentMp4Movie, final boolean splitMdat) throws Exception {
        this.currentMp4Movie = currentMp4Movie;
        this.fos = new FileOutputStream(currentMp4Movie.getCacheFile());
        this.fc = this.fos.getChannel();
        final FileTypeBox fileTypeBox = this.createFileTypeBox();
        fileTypeBox.getBox(this.fc);
        this.dataOffset += fileTypeBox.getSize();
        this.wroteSinceLastMdat += this.dataOffset;
        this.splitMdat = splitMdat;
        this.mdat = new InterleaveChunkMdat();
        this.sizeBuffer = ByteBuffer.allocateDirect(4);
        return this;
    }
    
    protected MovieBox createMovieBox(final Mp4Movie mp4Movie) {
        final MovieBox movieBox = new MovieBox();
        final MovieHeaderBox movieHeaderBox = new MovieHeaderBox();
        movieHeaderBox.setCreationTime(new Date());
        movieHeaderBox.setModificationTime(new Date());
        movieHeaderBox.setMatrix(Matrix.ROTATE_0);
        final long timescale = this.getTimescale(mp4Movie);
        final Iterator<Track> iterator = mp4Movie.getTracks().iterator();
        long duration = 0L;
        while (iterator.hasNext()) {
            final Track track = iterator.next();
            track.prepare();
            final long n = track.getDuration() * timescale / track.getTimeScale();
            if (n > duration) {
                duration = n;
            }
        }
        movieHeaderBox.setDuration(duration);
        movieHeaderBox.setTimescale(timescale);
        movieHeaderBox.setNextTrackId(mp4Movie.getTracks().size() + 1);
        movieBox.addBox(movieHeaderBox);
        final Iterator<Track> iterator2 = mp4Movie.getTracks().iterator();
        while (iterator2.hasNext()) {
            movieBox.addBox(this.createTrackBox(iterator2.next(), mp4Movie));
        }
        return movieBox;
    }
    
    protected void createSidx(final Track track, final SampleTableBox sampleTableBox) {
    }
    
    protected Box createStbl(final Track track) {
        final SampleTableBox sampleTableBox = new SampleTableBox();
        this.createStsd(track, sampleTableBox);
        this.createStts(track, sampleTableBox);
        this.createCtts(track, sampleTableBox);
        this.createStss(track, sampleTableBox);
        this.createStsc(track, sampleTableBox);
        this.createStsz(track, sampleTableBox);
        this.createStco(track, sampleTableBox);
        return sampleTableBox;
    }
    
    protected void createStco(final Track track, final SampleTableBox sampleTableBox) {
        final ArrayList<Long> list = new ArrayList<Long>();
        final Iterator<Sample> iterator = track.getSamples().iterator();
        long n = -1L;
        while (iterator.hasNext()) {
            final Sample sample = iterator.next();
            final long offset = sample.getOffset();
            long n2 = n;
            if (n != -1L) {
                n2 = n;
                if (n != offset) {
                    n2 = -1L;
                }
            }
            if (n2 == -1L) {
                list.add(offset);
            }
            n = sample.getSize() + offset;
        }
        final long[] chunkOffsets = new long[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            chunkOffsets[i] = list.get(i);
        }
        final StaticChunkOffsetBox staticChunkOffsetBox = new StaticChunkOffsetBox();
        staticChunkOffsetBox.setChunkOffsets(chunkOffsets);
        sampleTableBox.addBox(staticChunkOffsetBox);
    }
    
    protected void createStsc(final Track track, final SampleTableBox sampleTableBox) {
        final SampleToChunkBox sampleToChunkBox = new SampleToChunkBox();
        sampleToChunkBox.setEntries(new LinkedList<SampleToChunkBox.Entry>());
        final int size = track.getSamples().size();
        int i = 0;
        int n = 0;
        int n2 = -1;
        int n3 = 1;
        while (i < size) {
            final Sample sample = track.getSamples().get(i);
            final long offset = sample.getOffset();
            final long size2 = sample.getSize();
            final int n4 = n + 1;
            final boolean b = i == size - 1 || offset + size2 != track.getSamples().get(i + 1).getOffset();
            n = n4;
            int n5 = n2;
            int n6 = n3;
            if (b) {
                if (n2 != n4) {
                    sampleToChunkBox.getEntries().add(new SampleToChunkBox.Entry(n3, n4, 1L));
                    n2 = n4;
                }
                n6 = n3 + 1;
                n = 0;
                n5 = n2;
            }
            ++i;
            n2 = n5;
            n3 = n6;
        }
        sampleTableBox.addBox(sampleToChunkBox);
    }
    
    protected void createStsd(final Track track, final SampleTableBox sampleTableBox) {
        sampleTableBox.addBox(track.getSampleDescriptionBox());
    }
    
    protected void createStss(final Track track, final SampleTableBox sampleTableBox) {
        final long[] syncSamples = track.getSyncSamples();
        if (syncSamples != null && syncSamples.length > 0) {
            final SyncSampleBox syncSampleBox = new SyncSampleBox();
            syncSampleBox.setSampleNumber(syncSamples);
            sampleTableBox.addBox(syncSampleBox);
        }
    }
    
    protected void createStsz(final Track key, final SampleTableBox sampleTableBox) {
        final SampleSizeBox sampleSizeBox = new SampleSizeBox();
        sampleSizeBox.setSampleSizes(this.track2SampleSizes.get(key));
        sampleTableBox.addBox(sampleSizeBox);
    }
    
    protected void createStts(final Track track, final SampleTableBox sampleTableBox) {
        final ArrayList<TimeToSampleBox.Entry> entries = new ArrayList<TimeToSampleBox.Entry>();
        final long[] sampleDurations = track.getSampleDurations();
        TimeToSampleBox.Entry entry = null;
        for (int i = 0; i < sampleDurations.length; ++i) {
            final long n = sampleDurations[i];
            if (entry != null && entry.getDelta() == n) {
                entry.setCount(entry.getCount() + 1L);
            }
            else {
                entry = new TimeToSampleBox.Entry(1L, n);
                entries.add(entry);
            }
        }
        final TimeToSampleBox timeToSampleBox = new TimeToSampleBox();
        timeToSampleBox.setEntries(entries);
        sampleTableBox.addBox(timeToSampleBox);
    }
    
    protected TrackBox createTrackBox(final Track track, final Mp4Movie mp4Movie) {
        final TrackBox trackBox = new TrackBox();
        final TrackHeaderBox trackHeaderBox = new TrackHeaderBox();
        trackHeaderBox.setEnabled(true);
        trackHeaderBox.setInMovie(true);
        trackHeaderBox.setInPreview(true);
        if (track.isAudio()) {
            trackHeaderBox.setMatrix(Matrix.ROTATE_0);
        }
        else {
            trackHeaderBox.setMatrix(mp4Movie.getMatrix());
        }
        trackHeaderBox.setAlternateGroup(0);
        trackHeaderBox.setCreationTime(track.getCreationTime());
        trackHeaderBox.setDuration(track.getDuration() * this.getTimescale(mp4Movie) / track.getTimeScale());
        trackHeaderBox.setHeight(track.getHeight());
        trackHeaderBox.setWidth(track.getWidth());
        trackHeaderBox.setLayer(0);
        trackHeaderBox.setModificationTime(new Date());
        trackHeaderBox.setTrackId(track.getTrackId() + 1L);
        trackHeaderBox.setVolume(track.getVolume());
        trackBox.addBox(trackHeaderBox);
        final MediaBox mediaBox = new MediaBox();
        trackBox.addBox(mediaBox);
        final MediaHeaderBox mediaHeaderBox = new MediaHeaderBox();
        mediaHeaderBox.setCreationTime(track.getCreationTime());
        mediaHeaderBox.setDuration(track.getDuration());
        mediaHeaderBox.setTimescale(track.getTimeScale());
        mediaHeaderBox.setLanguage("eng");
        mediaBox.addBox(mediaHeaderBox);
        final HandlerBox handlerBox = new HandlerBox();
        String name;
        if (track.isAudio()) {
            name = "SoundHandle";
        }
        else {
            name = "VideoHandle";
        }
        handlerBox.setName(name);
        handlerBox.setHandlerType(track.getHandler());
        mediaBox.addBox(handlerBox);
        final MediaInformationBox mediaInformationBox = new MediaInformationBox();
        mediaInformationBox.addBox(track.getMediaHeaderBox());
        final DataInformationBox dataInformationBox = new DataInformationBox();
        final DataReferenceBox dataReferenceBox = new DataReferenceBox();
        dataInformationBox.addBox(dataReferenceBox);
        final DataEntryUrlBox dataEntryUrlBox = new DataEntryUrlBox();
        dataEntryUrlBox.setFlags(1);
        dataReferenceBox.addBox(dataEntryUrlBox);
        mediaInformationBox.addBox(dataInformationBox);
        mediaInformationBox.addBox(this.createStbl(track));
        mediaBox.addBox(mediaInformationBox);
        return trackBox;
    }
    
    public void finishMovie() throws Exception {
        if (this.mdat.getContentSize() != 0L) {
            this.flushCurrentMdat();
        }
        for (final Track key : this.currentMp4Movie.getTracks()) {
            final ArrayList<Sample> samples = key.getSamples();
            final long[] value = new long[samples.size()];
            for (int i = 0; i < value.length; ++i) {
                value[i] = ((Sample)samples.get(i)).getSize();
            }
            this.track2SampleSizes.put(key, value);
        }
        this.createMovieBox(this.currentMp4Movie).getBox(this.fc);
        this.fos.flush();
        this.fos.getFD().sync();
        this.fc.close();
        this.fos.close();
    }
    
    public long getTimescale(final Mp4Movie mp4Movie) {
        long gcd;
        if (!mp4Movie.getTracks().isEmpty()) {
            gcd = mp4Movie.getTracks().iterator().next().getTimeScale();
        }
        else {
            gcd = 0L;
        }
        final Iterator<Track> iterator = mp4Movie.getTracks().iterator();
        while (iterator.hasNext()) {
            gcd = gcd(iterator.next().getTimeScale(), gcd);
        }
        return gcd;
    }
    
    public long writeSampleData(final int n, final ByteBuffer byteBuffer, final MediaCodec$BufferInfo mediaCodec$BufferInfo, final boolean b) throws Exception {
        if (this.writeNewMdat) {
            this.mdat.setContentSize(0L);
            this.mdat.getBox(this.fc);
            this.mdat.setDataOffset(this.dataOffset);
            this.dataOffset += 16L;
            this.wroteSinceLastMdat += 16L;
            this.writeNewMdat = false;
        }
        final InterleaveChunkMdat mdat = this.mdat;
        mdat.setContentSize(mdat.getContentSize() + mediaCodec$BufferInfo.size);
        this.wroteSinceLastMdat += mediaCodec$BufferInfo.size;
        final long wroteSinceLastMdat = this.wroteSinceLastMdat;
        boolean b2 = true;
        if (wroteSinceLastMdat >= 32768L) {
            if (this.splitMdat) {
                this.flushCurrentMdat();
                this.writeNewMdat = true;
            }
            this.wroteSinceLastMdat = 0L;
        }
        else {
            b2 = false;
        }
        this.currentMp4Movie.addSample(n, this.dataOffset, mediaCodec$BufferInfo);
        if (b) {
            this.sizeBuffer.position(0);
            this.sizeBuffer.putInt(mediaCodec$BufferInfo.size - 4);
            this.sizeBuffer.position(0);
            this.fc.write(this.sizeBuffer);
            byteBuffer.position(mediaCodec$BufferInfo.offset + 4);
        }
        else {
            byteBuffer.position(mediaCodec$BufferInfo.offset);
        }
        byteBuffer.limit(mediaCodec$BufferInfo.offset + mediaCodec$BufferInfo.size);
        this.fc.write(byteBuffer);
        this.dataOffset += mediaCodec$BufferInfo.size;
        if (b2) {
            this.fos.flush();
            this.fos.getFD().sync();
            return this.fc.position();
        }
        return 0L;
    }
    
    private class InterleaveChunkMdat implements Box
    {
        private long contentSize;
        private long dataOffset;
        private Container parent;
        
        private InterleaveChunkMdat() {
            this.contentSize = 1073741824L;
            this.dataOffset = 0L;
        }
        
        private boolean isSmallBox(final long n) {
            return n + 8L < 4294967296L;
        }
        
        @Override
        public void getBox(final WritableByteChannel writableByteChannel) throws IOException {
            final ByteBuffer allocate = ByteBuffer.allocate(16);
            final long size = this.getSize();
            if (this.isSmallBox(size)) {
                IsoTypeWriter.writeUInt32(allocate, size);
            }
            else {
                IsoTypeWriter.writeUInt32(allocate, 1L);
            }
            allocate.put(IsoFile.fourCCtoBytes("mdat"));
            if (this.isSmallBox(size)) {
                allocate.put(new byte[8]);
            }
            else {
                IsoTypeWriter.writeUInt64(allocate, size);
            }
            allocate.rewind();
            writableByteChannel.write(allocate);
        }
        
        public long getContentSize() {
            return this.contentSize;
        }
        
        public long getOffset() {
            return this.dataOffset;
        }
        
        public Container getParent() {
            return this.parent;
        }
        
        @Override
        public long getSize() {
            return this.contentSize + 16L;
        }
        
        public String getType() {
            return "mdat";
        }
        
        public void parse(final DataSource dataSource, final ByteBuffer byteBuffer, final long n, final BoxParser boxParser) {
        }
        
        public void setContentSize(final long contentSize) {
            this.contentSize = contentSize;
        }
        
        public void setDataOffset(final long dataOffset) {
            this.dataOffset = dataOffset;
        }
        
        @Override
        public void setParent(final Container parent) {
            this.parent = parent;
        }
    }
}
