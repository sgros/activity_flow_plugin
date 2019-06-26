// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.flac;

import java.io.IOException;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker$TimestampSeeker$_CC;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;

final class FlacBinarySearchSeeker extends BinarySearchSeeker
{
    private final FlacDecoderJni decoderJni;
    
    public FlacBinarySearchSeeker(final FlacStreamInfo flacStreamInfo, final long n, final long n2, final FlacDecoderJni flacDecoderJni) {
        super((SeekTimestampConverter)new FlacSeekTimestampConverter(flacStreamInfo), (TimestampSeeker)new FlacTimestampSeeker(flacDecoderJni), flacStreamInfo.durationUs(), 0L, flacStreamInfo.totalSamples, n, n2, flacStreamInfo.getApproxBytesPerFrame(), Math.max(1, flacStreamInfo.minFrameSize));
        Assertions.checkNotNull(flacDecoderJni);
        this.decoderJni = flacDecoderJni;
    }
    
    @Override
    protected void onSeekOperationFinished(final boolean b, final long n) {
        if (!b) {
            this.decoderJni.reset(n);
        }
    }
    
    private static final class FlacSeekTimestampConverter implements SeekTimestampConverter
    {
        private final FlacStreamInfo streamInfo;
        
        public FlacSeekTimestampConverter(final FlacStreamInfo streamInfo) {
            this.streamInfo = streamInfo;
        }
        
        @Override
        public long timeUsToTargetTime(final long n) {
            final FlacStreamInfo streamInfo = this.streamInfo;
            Assertions.checkNotNull(streamInfo);
            return streamInfo.getSampleIndex(n);
        }
    }
    
    private static final class FlacTimestampSeeker implements TimestampSeeker
    {
        private final FlacDecoderJni decoderJni;
        
        private FlacTimestampSeeker(final FlacDecoderJni decoderJni) {
            this.decoderJni = decoderJni;
        }
        
        @Override
        public TimestampSearchResult searchForTimestamp(final ExtractorInput extractorInput, final long n, final OutputFrameHolder outputFrameHolder) throws IOException, InterruptedException {
            final ByteBuffer byteBuffer = outputFrameHolder.byteBuffer;
            final long position = extractorInput.getPosition();
            this.decoderJni.reset(position);
            try {
                this.decoderJni.decodeSampleWithBacktrackPosition(byteBuffer, position);
                if (byteBuffer.limit() == 0) {
                    return TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
                }
                final long lastFrameFirstSampleIndex = this.decoderJni.getLastFrameFirstSampleIndex();
                final long nextFrameFirstSampleIndex = this.decoderJni.getNextFrameFirstSampleIndex();
                final long decodePosition = this.decoderJni.getDecodePosition();
                if (lastFrameFirstSampleIndex <= n && nextFrameFirstSampleIndex > n) {
                    outputFrameHolder.timeUs = this.decoderJni.getLastFrameTimestamp();
                    return TimestampSearchResult.targetFoundResult(extractorInput.getPosition());
                }
                if (nextFrameFirstSampleIndex <= n) {
                    return TimestampSearchResult.underestimatedResult(nextFrameFirstSampleIndex, decodePosition);
                }
                return TimestampSearchResult.overestimatedResult(lastFrameFirstSampleIndex, position);
            }
            catch (FlacDecoderJni.FlacFrameDecodeException ex) {
                return TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
            }
        }
    }
}
