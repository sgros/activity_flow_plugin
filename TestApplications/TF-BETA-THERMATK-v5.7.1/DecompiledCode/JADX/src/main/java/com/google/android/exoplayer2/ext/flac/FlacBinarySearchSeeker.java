package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.ext.flac.FlacDecoderJni.FlacFrameDecodeException;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker.OutputFrameHolder;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker.TimestampSearchResult;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker.TimestampSeeker.C0159-CC;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import java.io.IOException;
import java.nio.ByteBuffer;

final class FlacBinarySearchSeeker extends BinarySearchSeeker {
    private final FlacDecoderJni decoderJni;

    private static final class FlacSeekTimestampConverter implements SeekTimestampConverter {
        private final FlacStreamInfo streamInfo;

        public FlacSeekTimestampConverter(FlacStreamInfo flacStreamInfo) {
            this.streamInfo = flacStreamInfo;
        }

        public long timeUsToTargetTime(long j) {
            FlacStreamInfo flacStreamInfo = this.streamInfo;
            Assertions.checkNotNull(flacStreamInfo);
            return flacStreamInfo.getSampleIndex(j);
        }
    }

    private static final class FlacTimestampSeeker implements TimestampSeeker {
        private final FlacDecoderJni decoderJni;

        public /* synthetic */ void onSeekFinished() {
            C0159-CC.$default$onSeekFinished(this);
        }

        private FlacTimestampSeeker(FlacDecoderJni flacDecoderJni) {
            this.decoderJni = flacDecoderJni;
        }

        public TimestampSearchResult searchForTimestamp(ExtractorInput extractorInput, long j, OutputFrameHolder outputFrameHolder) throws IOException, InterruptedException {
            ByteBuffer byteBuffer = outputFrameHolder.byteBuffer;
            long position = extractorInput.getPosition();
            this.decoderJni.reset(position);
            try {
                this.decoderJni.decodeSampleWithBacktrackPosition(byteBuffer, position);
                if (byteBuffer.limit() == 0) {
                    return TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
                }
                long lastFrameFirstSampleIndex = this.decoderJni.getLastFrameFirstSampleIndex();
                long nextFrameFirstSampleIndex = this.decoderJni.getNextFrameFirstSampleIndex();
                long decodePosition = this.decoderJni.getDecodePosition();
                Object obj = (lastFrameFirstSampleIndex > j || nextFrameFirstSampleIndex <= j) ? null : 1;
                if (obj != null) {
                    outputFrameHolder.timeUs = this.decoderJni.getLastFrameTimestamp();
                    return TimestampSearchResult.targetFoundResult(extractorInput.getPosition());
                } else if (nextFrameFirstSampleIndex <= j) {
                    return TimestampSearchResult.underestimatedResult(nextFrameFirstSampleIndex, decodePosition);
                } else {
                    return TimestampSearchResult.overestimatedResult(lastFrameFirstSampleIndex, position);
                }
            } catch (FlacFrameDecodeException unused) {
                return TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
            }
        }
    }

    public FlacBinarySearchSeeker(FlacStreamInfo flacStreamInfo, long j, long j2, FlacDecoderJni flacDecoderJni) {
        FlacStreamInfo flacStreamInfo2 = flacStreamInfo;
        FlacDecoderJni flacDecoderJni2 = flacDecoderJni;
        super(new FlacSeekTimestampConverter(flacStreamInfo2), new FlacTimestampSeeker(flacDecoderJni2), flacStreamInfo.durationUs(), 0, flacStreamInfo2.totalSamples, j, j2, flacStreamInfo.getApproxBytesPerFrame(), Math.max(1, flacStreamInfo2.minFrameSize));
        Assertions.checkNotNull(flacDecoderJni);
        this.decoderJni = flacDecoderJni2;
    }

    /* Access modifiers changed, original: protected */
    public void onSeekOperationFinished(boolean z, long j) {
        if (!z) {
            this.decoderJni.reset(j);
        }
    }
}
