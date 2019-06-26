// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.io.IOException;
import com.google.android.exoplayer2.util.Assertions;

public abstract class BinarySearchSeeker
{
    private static final long MAX_SKIP_BYTES = 262144L;
    private final int minimumSearchRange;
    protected final BinarySearchSeekMap seekMap;
    protected SeekOperationParams seekOperationParams;
    protected final TimestampSeeker timestampSeeker;
    
    protected BinarySearchSeeker(final SeekTimestampConverter seekTimestampConverter, final TimestampSeeker timestampSeeker, final long n, final long n2, final long n3, final long n4, final long n5, final long n6, final int minimumSearchRange) {
        this.timestampSeeker = timestampSeeker;
        this.minimumSearchRange = minimumSearchRange;
        this.seekMap = new BinarySearchSeekMap(seekTimestampConverter, n, n2, n3, n4, n5, n6);
    }
    
    protected SeekOperationParams createSeekParamsForTargetTimeUs(final long n) {
        return new SeekOperationParams(n, this.seekMap.timeUsToTargetTime(n), this.seekMap.floorTimePosition, this.seekMap.ceilingTimePosition, this.seekMap.floorBytePosition, this.seekMap.ceilingBytePosition, this.seekMap.approxBytesPerFrame);
    }
    
    public final SeekMap getSeekMap() {
        return this.seekMap;
    }
    
    public int handlePendingSeek(final ExtractorInput extractorInput, final PositionHolder positionHolder, final OutputFrameHolder outputFrameHolder) throws InterruptedException, IOException {
        final TimestampSeeker timestampSeeker = this.timestampSeeker;
        Assertions.checkNotNull(timestampSeeker);
        final TimestampSeeker timestampSeeker2 = timestampSeeker;
        while (true) {
            final SeekOperationParams seekOperationParams = this.seekOperationParams;
            Assertions.checkNotNull(seekOperationParams);
            final SeekOperationParams seekOperationParams2 = seekOperationParams;
            final long access$100 = seekOperationParams2.getFloorBytePosition();
            final long access$101 = seekOperationParams2.getCeilingBytePosition();
            final long access$102 = seekOperationParams2.getNextSearchBytePosition();
            if (access$101 - access$100 <= this.minimumSearchRange) {
                this.markSeekOperationFinished(false, access$100);
                return this.seekToPosition(extractorInput, access$100, positionHolder);
            }
            if (!this.skipInputUntilPosition(extractorInput, access$102)) {
                return this.seekToPosition(extractorInput, access$102, positionHolder);
            }
            extractorInput.resetPeekPosition();
            final TimestampSearchResult searchForTimestamp = timestampSeeker2.searchForTimestamp(extractorInput, seekOperationParams2.getTargetTimePosition(), outputFrameHolder);
            final int access$103 = searchForTimestamp.type;
            if (access$103 == -3) {
                this.markSeekOperationFinished(false, access$102);
                return this.seekToPosition(extractorInput, access$102, positionHolder);
            }
            if (access$103 != -2) {
                if (access$103 != -1) {
                    if (access$103 == 0) {
                        this.markSeekOperationFinished(true, searchForTimestamp.bytePositionToUpdate);
                        this.skipInputUntilPosition(extractorInput, searchForTimestamp.bytePositionToUpdate);
                        return this.seekToPosition(extractorInput, searchForTimestamp.bytePositionToUpdate, positionHolder);
                    }
                    throw new IllegalStateException("Invalid case");
                }
                else {
                    seekOperationParams2.updateSeekCeiling(searchForTimestamp.timestampToUpdate, searchForTimestamp.bytePositionToUpdate);
                }
            }
            else {
                seekOperationParams2.updateSeekFloor(searchForTimestamp.timestampToUpdate, searchForTimestamp.bytePositionToUpdate);
            }
        }
    }
    
    public final boolean isSeeking() {
        return this.seekOperationParams != null;
    }
    
    protected final void markSeekOperationFinished(final boolean b, final long n) {
        this.seekOperationParams = null;
        this.timestampSeeker.onSeekFinished();
        this.onSeekOperationFinished(b, n);
    }
    
    protected void onSeekOperationFinished(final boolean b, final long n) {
    }
    
    protected final int seekToPosition(final ExtractorInput extractorInput, final long position, final PositionHolder positionHolder) {
        if (position == extractorInput.getPosition()) {
            return 0;
        }
        positionHolder.position = position;
        return 1;
    }
    
    public final void setSeekTargetUs(final long n) {
        final SeekOperationParams seekOperationParams = this.seekOperationParams;
        if (seekOperationParams != null && seekOperationParams.getSeekTimeUs() == n) {
            return;
        }
        this.seekOperationParams = this.createSeekParamsForTargetTimeUs(n);
    }
    
    protected final boolean skipInputUntilPosition(final ExtractorInput extractorInput, long n) throws IOException, InterruptedException {
        n -= extractorInput.getPosition();
        if (n >= 0L && n <= 262144L) {
            extractorInput.skipFully((int)n);
            return true;
        }
        return false;
    }
    
    public static class BinarySearchSeekMap implements SeekMap
    {
        private final long approxBytesPerFrame;
        private final long ceilingBytePosition;
        private final long ceilingTimePosition;
        private final long durationUs;
        private final long floorBytePosition;
        private final long floorTimePosition;
        private final SeekTimestampConverter seekTimestampConverter;
        
        public BinarySearchSeekMap(final SeekTimestampConverter seekTimestampConverter, final long durationUs, final long floorTimePosition, final long ceilingTimePosition, final long floorBytePosition, final long ceilingBytePosition, final long approxBytesPerFrame) {
            this.seekTimestampConverter = seekTimestampConverter;
            this.durationUs = durationUs;
            this.floorTimePosition = floorTimePosition;
            this.ceilingTimePosition = ceilingTimePosition;
            this.floorBytePosition = floorBytePosition;
            this.ceilingBytePosition = ceilingBytePosition;
            this.approxBytesPerFrame = approxBytesPerFrame;
        }
        
        @Override
        public long getDurationUs() {
            return this.durationUs;
        }
        
        @Override
        public SeekPoints getSeekPoints(final long n) {
            return new SeekPoints(new SeekPoint(n, SeekOperationParams.calculateNextSearchBytePosition(this.seekTimestampConverter.timeUsToTargetTime(n), this.floorTimePosition, this.ceilingTimePosition, this.floorBytePosition, this.ceilingBytePosition, this.approxBytesPerFrame)));
        }
        
        @Override
        public boolean isSeekable() {
            return true;
        }
        
        public long timeUsToTargetTime(final long n) {
            return this.seekTimestampConverter.timeUsToTargetTime(n);
        }
    }
    
    public static final class DefaultSeekTimestampConverter implements SeekTimestampConverter
    {
        @Override
        public long timeUsToTargetTime(final long n) {
            return n;
        }
    }
    
    public static final class OutputFrameHolder
    {
        public final ByteBuffer byteBuffer;
        public long timeUs;
        
        public OutputFrameHolder(final ByteBuffer byteBuffer) {
            this.timeUs = 0L;
            this.byteBuffer = byteBuffer;
        }
    }
    
    protected static class SeekOperationParams
    {
        private final long approxBytesPerFrame;
        private long ceilingBytePosition;
        private long ceilingTimePosition;
        private long floorBytePosition;
        private long floorTimePosition;
        private long nextSearchBytePosition;
        private final long seekTimeUs;
        private final long targetTimePosition;
        
        protected SeekOperationParams(final long seekTimeUs, final long targetTimePosition, final long floorTimePosition, final long ceilingTimePosition, final long floorBytePosition, final long ceilingBytePosition, final long approxBytesPerFrame) {
            this.seekTimeUs = seekTimeUs;
            this.targetTimePosition = targetTimePosition;
            this.floorTimePosition = floorTimePosition;
            this.ceilingTimePosition = ceilingTimePosition;
            this.floorBytePosition = floorBytePosition;
            this.ceilingBytePosition = ceilingBytePosition;
            this.approxBytesPerFrame = approxBytesPerFrame;
            this.nextSearchBytePosition = calculateNextSearchBytePosition(targetTimePosition, floorTimePosition, ceilingTimePosition, floorBytePosition, ceilingBytePosition, approxBytesPerFrame);
        }
        
        protected static long calculateNextSearchBytePosition(long n, final long n2, final long n3, final long n4, final long n5, final long n6) {
            if (n4 + 1L < n5 && n2 + 1L < n3) {
                n = (long)((n - n2) * ((n5 - n4) / (float)(n3 - n2)));
                return Util.constrainValue(n + n4 - n6 - n / 20L, n4, n5 - 1L);
            }
            return n4;
        }
        
        private long getCeilingBytePosition() {
            return this.ceilingBytePosition;
        }
        
        private long getFloorBytePosition() {
            return this.floorBytePosition;
        }
        
        private long getNextSearchBytePosition() {
            return this.nextSearchBytePosition;
        }
        
        private long getSeekTimeUs() {
            return this.seekTimeUs;
        }
        
        private long getTargetTimePosition() {
            return this.targetTimePosition;
        }
        
        private void updateNextSearchBytePosition() {
            this.nextSearchBytePosition = calculateNextSearchBytePosition(this.targetTimePosition, this.floorTimePosition, this.ceilingTimePosition, this.floorBytePosition, this.ceilingBytePosition, this.approxBytesPerFrame);
        }
        
        private void updateSeekCeiling(final long ceilingTimePosition, final long ceilingBytePosition) {
            this.ceilingTimePosition = ceilingTimePosition;
            this.ceilingBytePosition = ceilingBytePosition;
            this.updateNextSearchBytePosition();
        }
        
        private void updateSeekFloor(final long floorTimePosition, final long floorBytePosition) {
            this.floorTimePosition = floorTimePosition;
            this.floorBytePosition = floorBytePosition;
            this.updateNextSearchBytePosition();
        }
    }
    
    protected interface SeekTimestampConverter
    {
        long timeUsToTargetTime(final long p0);
    }
    
    public static final class TimestampSearchResult
    {
        public static final TimestampSearchResult NO_TIMESTAMP_IN_RANGE_RESULT;
        private final long bytePositionToUpdate;
        private final long timestampToUpdate;
        private final int type;
        
        static {
            NO_TIMESTAMP_IN_RANGE_RESULT = new TimestampSearchResult(-3, -9223372036854775807L, -1L);
        }
        
        private TimestampSearchResult(final int type, final long timestampToUpdate, final long bytePositionToUpdate) {
            this.type = type;
            this.timestampToUpdate = timestampToUpdate;
            this.bytePositionToUpdate = bytePositionToUpdate;
        }
        
        public static TimestampSearchResult overestimatedResult(final long n, final long n2) {
            return new TimestampSearchResult(-1, n, n2);
        }
        
        public static TimestampSearchResult targetFoundResult(final long n) {
            return new TimestampSearchResult(0, -9223372036854775807L, n);
        }
        
        public static TimestampSearchResult underestimatedResult(final long n, final long n2) {
            return new TimestampSearchResult(-2, n, n2);
        }
    }
    
    protected interface TimestampSeeker
    {
        void onSeekFinished();
        
        TimestampSearchResult searchForTimestamp(final ExtractorInput p0, final long p1, final OutputFrameHolder p2) throws IOException, InterruptedException;
    }
}
