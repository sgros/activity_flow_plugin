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

public final class InitializationChunk extends Chunk
{
    private static final PositionHolder DUMMY_POSITION_HOLDER;
    private final ChunkExtractorWrapper extractorWrapper;
    private volatile boolean loadCanceled;
    private long nextLoadPosition;
    
    static {
        DUMMY_POSITION_HOLDER = new PositionHolder();
    }
    
    public InitializationChunk(final DataSource dataSource, final DataSpec dataSpec, final Format format, final int n, final Object o, final ChunkExtractorWrapper extractorWrapper) {
        super(dataSource, dataSpec, 2, format, n, o, -9223372036854775807L, -9223372036854775807L);
        this.extractorWrapper = extractorWrapper;
    }
    
    @Override
    public void cancelLoad() {
        this.loadCanceled = true;
    }
    
    @Override
    public void load() throws IOException, InterruptedException {
        final DataSpec subrange = super.dataSpec.subrange(this.nextLoadPosition);
        try {
            final DefaultExtractorInput defaultExtractorInput = new DefaultExtractorInput(super.dataSource, subrange.absoluteStreamPosition, super.dataSource.open(subrange));
            if (this.nextLoadPosition == 0L) {
                this.extractorWrapper.init(null, -9223372036854775807L, -9223372036854775807L);
            }
            try {
                Extractor extractor;
                int read;
                for (extractor = this.extractorWrapper.extractor, read = 0; read == 0 && !this.loadCanceled; read = extractor.read(defaultExtractorInput, InitializationChunk.DUMMY_POSITION_HOLDER)) {}
                boolean b = true;
                if (read == 1) {
                    b = false;
                }
                Assertions.checkState(b);
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
