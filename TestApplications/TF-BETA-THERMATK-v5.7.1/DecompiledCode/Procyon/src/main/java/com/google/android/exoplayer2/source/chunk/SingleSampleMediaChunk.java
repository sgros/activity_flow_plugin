// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

import java.io.IOException;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.Format;

public final class SingleSampleMediaChunk extends BaseMediaChunk
{
    private boolean loadCompleted;
    private long nextLoadPosition;
    private final Format sampleFormat;
    private final int trackType;
    
    public SingleSampleMediaChunk(final DataSource dataSource, final DataSpec dataSpec, final Format format, final int n, final Object o, final long n2, final long n3, final long n4, final int trackType, final Format sampleFormat) {
        super(dataSource, dataSpec, format, n, o, n2, n3, -9223372036854775807L, -9223372036854775807L, n4);
        this.trackType = trackType;
        this.sampleFormat = sampleFormat;
    }
    
    @Override
    public void cancelLoad() {
    }
    
    @Override
    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }
    
    @Override
    public void load() throws IOException, InterruptedException {
        final DataSpec subrange = super.dataSpec.subrange(this.nextLoadPosition);
        try {
            long open;
            final long n = open = super.dataSource.open(subrange);
            if (n != -1L) {
                open = n + this.nextLoadPosition;
            }
            final DefaultExtractorInput defaultExtractorInput = new DefaultExtractorInput(super.dataSource, this.nextLoadPosition, open);
            final BaseMediaChunkOutput output = this.getOutput();
            output.setSampleOffsetUs(0L);
            final int trackType = this.trackType;
            int i = 0;
            final TrackOutput track = output.track(0, trackType);
            track.format(this.sampleFormat);
            while (i != -1) {
                this.nextLoadPosition += i;
                i = track.sampleData(defaultExtractorInput, Integer.MAX_VALUE, true);
            }
            track.sampleMetadata(super.startTimeUs, 1, (int)this.nextLoadPosition, 0, null);
            Util.closeQuietly(super.dataSource);
            this.loadCompleted = true;
        }
        finally {
            Util.closeQuietly(super.dataSource);
        }
    }
}
