// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.Extractor;
import android.util.SparseArray;
import com.google.android.exoplayer2.extractor.ExtractorOutput;

public final class ChunkExtractorWrapper implements ExtractorOutput
{
    private final SparseArray<BindingTrackOutput> bindingTrackOutputs;
    private long endTimeUs;
    public final Extractor extractor;
    private boolean extractorInitialized;
    private final Format primaryTrackManifestFormat;
    private final int primaryTrackType;
    private Format[] sampleFormats;
    private SeekMap seekMap;
    private TrackOutputProvider trackOutputProvider;
    
    public ChunkExtractorWrapper(final Extractor extractor, final int primaryTrackType, final Format primaryTrackManifestFormat) {
        this.extractor = extractor;
        this.primaryTrackType = primaryTrackType;
        this.primaryTrackManifestFormat = primaryTrackManifestFormat;
        this.bindingTrackOutputs = (SparseArray<BindingTrackOutput>)new SparseArray();
    }
    
    @Override
    public void endTracks() {
        final Format[] sampleFormats = new Format[this.bindingTrackOutputs.size()];
        for (int i = 0; i < this.bindingTrackOutputs.size(); ++i) {
            sampleFormats[i] = ((BindingTrackOutput)this.bindingTrackOutputs.valueAt(i)).sampleFormat;
        }
        this.sampleFormats = sampleFormats;
    }
    
    public Format[] getSampleFormats() {
        return this.sampleFormats;
    }
    
    public SeekMap getSeekMap() {
        return this.seekMap;
    }
    
    public void init(final TrackOutputProvider trackOutputProvider, final long n, final long endTimeUs) {
        this.trackOutputProvider = trackOutputProvider;
        this.endTimeUs = endTimeUs;
        if (!this.extractorInitialized) {
            this.extractor.init(this);
            if (n != -9223372036854775807L) {
                this.extractor.seek(0L, n);
            }
            this.extractorInitialized = true;
        }
        else {
            final Extractor extractor = this.extractor;
            long n2 = n;
            if (n == -9223372036854775807L) {
                n2 = 0L;
            }
            extractor.seek(0L, n2);
            for (int i = 0; i < this.bindingTrackOutputs.size(); ++i) {
                ((BindingTrackOutput)this.bindingTrackOutputs.valueAt(i)).bind(trackOutputProvider, endTimeUs);
            }
        }
    }
    
    @Override
    public void seekMap(final SeekMap seekMap) {
        this.seekMap = seekMap;
    }
    
    @Override
    public TrackOutput track(final int n, final int n2) {
        BindingTrackOutput bindingTrackOutput;
        if ((bindingTrackOutput = (BindingTrackOutput)this.bindingTrackOutputs.get(n)) == null) {
            Assertions.checkState(this.sampleFormats == null);
            Format primaryTrackManifestFormat;
            if (n2 == this.primaryTrackType) {
                primaryTrackManifestFormat = this.primaryTrackManifestFormat;
            }
            else {
                primaryTrackManifestFormat = null;
            }
            bindingTrackOutput = new BindingTrackOutput(n, n2, primaryTrackManifestFormat);
            bindingTrackOutput.bind(this.trackOutputProvider, this.endTimeUs);
            this.bindingTrackOutputs.put(n, (Object)bindingTrackOutput);
        }
        return bindingTrackOutput;
    }
    
    private static final class BindingTrackOutput implements TrackOutput
    {
        private final DummyTrackOutput dummyTrackOutput;
        private long endTimeUs;
        private final int id;
        private final Format manifestFormat;
        public Format sampleFormat;
        private TrackOutput trackOutput;
        private final int type;
        
        public BindingTrackOutput(final int id, final int type, final Format manifestFormat) {
            this.id = id;
            this.type = type;
            this.manifestFormat = manifestFormat;
            this.dummyTrackOutput = new DummyTrackOutput();
        }
        
        public void bind(final TrackOutputProvider trackOutputProvider, final long endTimeUs) {
            if (trackOutputProvider == null) {
                this.trackOutput = this.dummyTrackOutput;
                return;
            }
            this.endTimeUs = endTimeUs;
            this.trackOutput = trackOutputProvider.track(this.id, this.type);
            final Format sampleFormat = this.sampleFormat;
            if (sampleFormat != null) {
                this.trackOutput.format(sampleFormat);
            }
        }
        
        @Override
        public void format(final Format format) {
            final Format manifestFormat = this.manifestFormat;
            Format copyWithManifestFormatInfo = format;
            if (manifestFormat != null) {
                copyWithManifestFormatInfo = format.copyWithManifestFormatInfo(manifestFormat);
            }
            this.sampleFormat = copyWithManifestFormatInfo;
            this.trackOutput.format(this.sampleFormat);
        }
        
        @Override
        public int sampleData(final ExtractorInput extractorInput, final int n, final boolean b) throws IOException, InterruptedException {
            return this.trackOutput.sampleData(extractorInput, n, b);
        }
        
        @Override
        public void sampleData(final ParsableByteArray parsableByteArray, final int n) {
            this.trackOutput.sampleData(parsableByteArray, n);
        }
        
        @Override
        public void sampleMetadata(final long n, final int n2, final int n3, final int n4, final CryptoData cryptoData) {
            final long endTimeUs = this.endTimeUs;
            if (endTimeUs != -9223372036854775807L && n >= endTimeUs) {
                this.trackOutput = this.dummyTrackOutput;
            }
            this.trackOutput.sampleMetadata(n, n2, n3, n4, cryptoData);
        }
    }
    
    public interface TrackOutputProvider
    {
        TrackOutput track(final int p0, final int p1);
    }
}
