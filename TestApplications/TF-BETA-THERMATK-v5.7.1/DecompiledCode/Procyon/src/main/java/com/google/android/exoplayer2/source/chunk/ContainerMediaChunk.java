// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

import java.io.IOException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.extractor.PositionHolder;

public class ContainerMediaChunk extends BaseMediaChunk
{
    private static final PositionHolder DUMMY_POSITION_HOLDER;
    private final int chunkCount;
    private final ChunkExtractorWrapper extractorWrapper;
    private volatile boolean loadCanceled;
    private boolean loadCompleted;
    private long nextLoadPosition;
    private final long sampleOffsetUs;
    
    static {
        DUMMY_POSITION_HOLDER = new PositionHolder();
    }
    
    public ContainerMediaChunk(final DataSource dataSource, final DataSpec dataSpec, final Format format, final int n, final Object o, final long n2, final long n3, final long n4, final long n5, final long n6, final int chunkCount, final long sampleOffsetUs, final ChunkExtractorWrapper extractorWrapper) {
        super(dataSource, dataSpec, format, n, o, n2, n3, n4, n5, n6);
        this.chunkCount = chunkCount;
        this.sampleOffsetUs = sampleOffsetUs;
        this.extractorWrapper = extractorWrapper;
    }
    
    @Override
    public final void cancelLoad() {
        this.loadCanceled = true;
    }
    
    @Override
    public long getNextChunkIndex() {
        return super.chunkIndex + this.chunkCount;
    }
    
    @Override
    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }
    
    @Override
    public final void load() throws IOException, InterruptedException {
        final DataSpec subrange = super.dataSpec.subrange(this.nextLoadPosition);
        try {
            final DefaultExtractorInput defaultExtractorInput = new DefaultExtractorInput(super.dataSource, subrange.absoluteStreamPosition, super.dataSource.open(subrange));
            if (this.nextLoadPosition == 0L) {
                final BaseMediaChunkOutput output = this.getOutput();
                output.setSampleOffsetUs(this.sampleOffsetUs);
                final ChunkExtractorWrapper extractorWrapper = this.extractorWrapper;
                long n;
                if (super.clippedStartTimeUs == -9223372036854775807L) {
                    n = -9223372036854775807L;
                }
                else {
                    n = super.clippedStartTimeUs - this.sampleOffsetUs;
                }
                long n2;
                if (super.clippedEndTimeUs == -9223372036854775807L) {
                    n2 = -9223372036854775807L;
                }
                else {
                    n2 = super.clippedEndTimeUs - this.sampleOffsetUs;
                }
                extractorWrapper.init((ChunkExtractorWrapper.TrackOutputProvider)output, n, n2);
            }
            try {
                final Extractor extractor = this.extractorWrapper.extractor;
                boolean b = false;
                int read;
                for (read = 0; read == 0 && !this.loadCanceled; read = extractor.read(defaultExtractorInput, ContainerMediaChunk.DUMMY_POSITION_HOLDER)) {}
                if (read != 1) {
                    b = true;
                }
                Assertions.checkState(b);
                this.nextLoadPosition = defaultExtractorInput.getPosition() - super.dataSpec.absoluteStreamPosition;
                Util.closeQuietly(super.dataSource);
                this.loadCompleted = true;
            }
            finally {
                this.nextLoadPosition = defaultExtractorInput.getPosition() - super.dataSpec.absoluteStreamPosition;
            }
        }
        finally {
            Util.closeQuietly(super.dataSource);
        }
    }
}
