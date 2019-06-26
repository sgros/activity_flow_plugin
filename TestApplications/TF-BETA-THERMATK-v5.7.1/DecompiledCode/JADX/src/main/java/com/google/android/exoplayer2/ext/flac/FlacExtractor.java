package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ext.flac.FlacDecoderJni.FlacFrameDecodeException;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker.OutputFrameHolder;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Id3Peeker;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekMap.SeekPoints;
import com.google.android.exoplayer2.extractor.SeekMap.Unseekable;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class FlacExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = C3330-$$Lambda$FlacExtractor$hclvvK8AqHrca9y8kXj1zx0IKB4.INSTANCE;
    private static final byte[] FLAC_SIGNATURE = new byte[]{(byte) 102, (byte) 76, (byte) 97, (byte) 67, (byte) 0, (byte) 0, (byte) 0, (byte) 34};
    public static final int FLAG_DISABLE_ID3_METADATA = 1;
    private FlacDecoderJni decoderJni;
    private ExtractorOutput extractorOutput;
    private FlacBinarySearchSeeker flacBinarySearchSeeker;
    private Metadata id3Metadata;
    private final Id3Peeker id3Peeker;
    private final boolean isId3MetadataDisabled;
    private ParsableByteArray outputBuffer;
    private ByteBuffer outputByteBuffer;
    private OutputFrameHolder outputFrameHolder;
    private boolean readPastStreamInfo;
    private FlacStreamInfo streamInfo;
    private TrackOutput trackOutput;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    private static final class FlacSeekMap implements SeekMap {
        private final FlacDecoderJni decoderJni;
        private final long durationUs;

        public boolean isSeekable() {
            return true;
        }

        public FlacSeekMap(long j, FlacDecoderJni flacDecoderJni) {
            this.durationUs = j;
            this.decoderJni = flacDecoderJni;
        }

        public SeekPoints getSeekPoints(long j) {
            return new SeekPoints(new SeekPoint(j, this.decoderJni.getSeekPosition(j)));
        }

        public long getDurationUs() {
            return this.durationUs;
        }
    }

    public FlacExtractor() {
        this(0);
    }

    public FlacExtractor(int i) {
        this.id3Peeker = new Id3Peeker();
        boolean z = true;
        if ((i & 1) == 0) {
            z = false;
        }
        this.isId3MetadataDisabled = z;
    }

    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = this.extractorOutput.track(0, 1);
        this.extractorOutput.endTracks();
        try {
            this.decoderJni = new FlacDecoderJni();
        } catch (FlacDecoderException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (extractorInput.getPosition() == 0) {
            this.id3Metadata = peekId3Data(extractorInput);
        }
        return peekFlacSignature(extractorInput);
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        if (extractorInput.getPosition() == 0 && !this.isId3MetadataDisabled && this.id3Metadata == null) {
            this.id3Metadata = peekId3Data(extractorInput);
        }
        this.decoderJni.setData(extractorInput);
        readPastStreamInfo(extractorInput);
        FlacBinarySearchSeeker flacBinarySearchSeeker = this.flacBinarySearchSeeker;
        if (flacBinarySearchSeeker != null && flacBinarySearchSeeker.isSeeking()) {
            return handlePendingSeek(extractorInput, positionHolder);
        }
        long decodePosition = this.decoderJni.getDecodePosition();
        try {
            this.decoderJni.decodeSampleWithBacktrackPosition(this.outputByteBuffer, decodePosition);
            int limit = this.outputByteBuffer.limit();
            int i = -1;
            if (limit == 0) {
                return -1;
            }
            writeLastSampleToOutput(limit, this.decoderJni.getLastFrameTimestamp());
            if (!this.decoderJni.isEndOfData()) {
                i = 0;
            }
            return i;
        } catch (FlacFrameDecodeException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot read frame at position ");
            stringBuilder.append(decodePosition);
            throw new IOException(stringBuilder.toString(), e);
        }
    }

    public void seek(long j, long j2) {
        if (j == 0) {
            this.readPastStreamInfo = false;
        }
        FlacDecoderJni flacDecoderJni = this.decoderJni;
        if (flacDecoderJni != null) {
            flacDecoderJni.reset(j);
        }
        FlacBinarySearchSeeker flacBinarySearchSeeker = this.flacBinarySearchSeeker;
        if (flacBinarySearchSeeker != null) {
            flacBinarySearchSeeker.setSeekTargetUs(j2);
        }
    }

    public void release() {
        this.flacBinarySearchSeeker = null;
        FlacDecoderJni flacDecoderJni = this.decoderJni;
        if (flacDecoderJni != null) {
            flacDecoderJni.release();
            this.decoderJni = null;
        }
    }

    private Metadata peekId3Data(ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.resetPeekPosition();
        return this.id3Peeker.peekId3Data(extractorInput, this.isId3MetadataDisabled ? Id3Decoder.NO_FRAMES_PREDICATE : null);
    }

    private boolean peekFlacSignature(ExtractorInput extractorInput) throws IOException, InterruptedException {
        byte[] bArr = FLAC_SIGNATURE;
        byte[] bArr2 = new byte[bArr.length];
        extractorInput.peekFully(bArr2, 0, bArr.length);
        return Arrays.equals(bArr2, FLAC_SIGNATURE);
    }

    private void readPastStreamInfo(ExtractorInput extractorInput) throws InterruptedException, IOException {
        if (!this.readPastStreamInfo) {
            FlacStreamInfo decodeStreamInfo = decodeStreamInfo(extractorInput);
            this.readPastStreamInfo = true;
            if (this.streamInfo == null) {
                updateFlacStreamInfo(extractorInput, decodeStreamInfo);
            }
        }
    }

    private void updateFlacStreamInfo(ExtractorInput extractorInput, FlacStreamInfo flacStreamInfo) {
        this.streamInfo = flacStreamInfo;
        outputSeekMap(extractorInput, flacStreamInfo);
        outputFormat(flacStreamInfo);
        this.outputBuffer = new ParsableByteArray(flacStreamInfo.maxDecodedFrameSize());
        this.outputByteBuffer = ByteBuffer.wrap(this.outputBuffer.data);
        this.outputFrameHolder = new OutputFrameHolder(this.outputByteBuffer);
    }

    private FlacStreamInfo decodeStreamInfo(ExtractorInput extractorInput) throws InterruptedException, IOException {
        try {
            FlacStreamInfo decodeMetadata = this.decoderJni.decodeMetadata();
            if (decodeMetadata != null) {
                return decodeMetadata;
            }
            throw new IOException("Metadata decoding failed");
        } catch (IOException e) {
            this.decoderJni.reset(0);
            extractorInput.setRetryPosition(0, e);
            throw null;
        }
    }

    private void outputSeekMap(ExtractorInput extractorInput, FlacStreamInfo flacStreamInfo) {
        SeekMap flacSeekMap;
        if ((this.decoderJni.getSeekPosition(0) != -1 ? 1 : null) != null) {
            flacSeekMap = new FlacSeekMap(flacStreamInfo.durationUs(), this.decoderJni);
        } else {
            flacSeekMap = getSeekMapForNonSeekTableFlac(extractorInput, flacStreamInfo);
        }
        this.extractorOutput.seekMap(flacSeekMap);
    }

    private SeekMap getSeekMapForNonSeekTableFlac(ExtractorInput extractorInput, FlacStreamInfo flacStreamInfo) {
        long length = extractorInput.getLength();
        if (length == -1) {
            return new Unseekable(flacStreamInfo.durationUs());
        }
        this.flacBinarySearchSeeker = new FlacBinarySearchSeeker(flacStreamInfo, this.decoderJni.getDecodePosition(), length, this.decoderJni);
        return this.flacBinarySearchSeeker.getSeekMap();
    }

    private void outputFormat(FlacStreamInfo flacStreamInfo) {
        FlacStreamInfo flacStreamInfo2 = flacStreamInfo;
        this.trackOutput.format(Format.createAudioSampleFormat(null, MimeTypes.AUDIO_RAW, null, flacStreamInfo.bitRate(), flacStreamInfo.maxDecodedFrameSize(), flacStreamInfo2.channels, flacStreamInfo2.sampleRate, Util.getPcmEncoding(flacStreamInfo2.bitsPerSample), 0, 0, null, null, 0, null, this.isId3MetadataDisabled ? null : this.id3Metadata));
    }

    private int handlePendingSeek(ExtractorInput extractorInput, PositionHolder positionHolder) throws InterruptedException, IOException {
        int handlePendingSeek = this.flacBinarySearchSeeker.handlePendingSeek(extractorInput, positionHolder, this.outputFrameHolder);
        ByteBuffer byteBuffer = this.outputFrameHolder.byteBuffer;
        if (handlePendingSeek == 0 && byteBuffer.limit() > 0) {
            writeLastSampleToOutput(byteBuffer.limit(), this.outputFrameHolder.timeUs);
        }
        return handlePendingSeek;
    }

    private void writeLastSampleToOutput(int i, long j) {
        this.outputBuffer.setPosition(0);
        this.trackOutput.sampleData(this.outputBuffer, i);
        this.trackOutput.sampleMetadata(j, 1, i, 0, null);
    }
}
