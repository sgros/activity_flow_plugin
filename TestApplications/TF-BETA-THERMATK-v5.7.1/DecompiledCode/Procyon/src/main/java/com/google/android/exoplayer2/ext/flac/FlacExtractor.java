// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.flac;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import java.util.Arrays;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.Id3Peeker;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public final class FlacExtractor implements Extractor
{
    public static final ExtractorsFactory FACTORY;
    private static final byte[] FLAC_SIGNATURE;
    public static final int FLAG_DISABLE_ID3_METADATA = 1;
    private FlacDecoderJni decoderJni;
    private ExtractorOutput extractorOutput;
    private FlacBinarySearchSeeker flacBinarySearchSeeker;
    private Metadata id3Metadata;
    private final Id3Peeker id3Peeker;
    private final boolean isId3MetadataDisabled;
    private ParsableByteArray outputBuffer;
    private ByteBuffer outputByteBuffer;
    private BinarySearchSeeker.OutputFrameHolder outputFrameHolder;
    private boolean readPastStreamInfo;
    private FlacStreamInfo streamInfo;
    private TrackOutput trackOutput;
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$FlacExtractor$hclvvK8AqHrca9y8kXj1zx0IKB4.INSTANCE;
        FLAC_SIGNATURE = new byte[] { 102, 76, 97, 67, 0, 0, 0, 34 };
    }
    
    public FlacExtractor() {
        this(0);
    }
    
    public FlacExtractor(final int n) {
        this.id3Peeker = new Id3Peeker();
        boolean isId3MetadataDisabled = true;
        if ((n & 0x1) == 0x0) {
            isId3MetadataDisabled = false;
        }
        this.isId3MetadataDisabled = isId3MetadataDisabled;
    }
    
    private FlacStreamInfo decodeStreamInfo(final ExtractorInput extractorInput) throws InterruptedException, IOException {
        try {
            final FlacStreamInfo decodeMetadata = this.decoderJni.decodeMetadata();
            if (decodeMetadata != null) {
                return decodeMetadata;
            }
            throw new IOException("Metadata decoding failed");
        }
        catch (IOException ex) {
            this.decoderJni.reset(0L);
            extractorInput.setRetryPosition(0L, ex);
            throw null;
        }
    }
    
    private SeekMap getSeekMapForNonSeekTableFlac(final ExtractorInput extractorInput, final FlacStreamInfo flacStreamInfo) {
        final long length = extractorInput.getLength();
        if (length != -1L) {
            this.flacBinarySearchSeeker = new FlacBinarySearchSeeker(flacStreamInfo, this.decoderJni.getDecodePosition(), length, this.decoderJni);
            return this.flacBinarySearchSeeker.getSeekMap();
        }
        return new SeekMap.Unseekable(flacStreamInfo.durationUs());
    }
    
    private int handlePendingSeek(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws InterruptedException, IOException {
        final int handlePendingSeek = this.flacBinarySearchSeeker.handlePendingSeek(extractorInput, positionHolder, this.outputFrameHolder);
        final ByteBuffer byteBuffer = this.outputFrameHolder.byteBuffer;
        if (handlePendingSeek == 0 && byteBuffer.limit() > 0) {
            this.writeLastSampleToOutput(byteBuffer.limit(), this.outputFrameHolder.timeUs);
        }
        return handlePendingSeek;
    }
    
    private void outputFormat(final FlacStreamInfo flacStreamInfo) {
        final int bitRate = flacStreamInfo.bitRate();
        final int maxDecodedFrameSize = flacStreamInfo.maxDecodedFrameSize();
        final int channels = flacStreamInfo.channels;
        final int sampleRate = flacStreamInfo.sampleRate;
        final int pcmEncoding = Util.getPcmEncoding(flacStreamInfo.bitsPerSample);
        Metadata id3Metadata;
        if (this.isId3MetadataDisabled) {
            id3Metadata = null;
        }
        else {
            id3Metadata = this.id3Metadata;
        }
        this.trackOutput.format(Format.createAudioSampleFormat(null, "audio/raw", null, bitRate, maxDecodedFrameSize, channels, sampleRate, pcmEncoding, 0, 0, null, null, 0, null, id3Metadata));
    }
    
    private void outputSeekMap(final ExtractorInput extractorInput, final FlacStreamInfo flacStreamInfo) {
        SeekMap seekMapForNonSeekTableFlac;
        if (this.decoderJni.getSeekPosition(0L) != -1L) {
            seekMapForNonSeekTableFlac = new FlacSeekMap(flacStreamInfo.durationUs(), this.decoderJni);
        }
        else {
            seekMapForNonSeekTableFlac = this.getSeekMapForNonSeekTableFlac(extractorInput, flacStreamInfo);
        }
        this.extractorOutput.seekMap(seekMapForNonSeekTableFlac);
    }
    
    private boolean peekFlacSignature(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final byte[] flac_SIGNATURE = FlacExtractor.FLAC_SIGNATURE;
        final byte[] a = new byte[flac_SIGNATURE.length];
        extractorInput.peekFully(a, 0, flac_SIGNATURE.length);
        return Arrays.equals(a, FlacExtractor.FLAC_SIGNATURE);
    }
    
    private Metadata peekId3Data(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.resetPeekPosition();
        Object no_FRAMES_PREDICATE;
        if (this.isId3MetadataDisabled) {
            no_FRAMES_PREDICATE = Id3Decoder.NO_FRAMES_PREDICATE;
        }
        else {
            no_FRAMES_PREDICATE = null;
        }
        return this.id3Peeker.peekId3Data(extractorInput, (Id3Decoder.FramePredicate)no_FRAMES_PREDICATE);
    }
    
    private void readPastStreamInfo(final ExtractorInput extractorInput) throws InterruptedException, IOException {
        if (this.readPastStreamInfo) {
            return;
        }
        final FlacStreamInfo decodeStreamInfo = this.decodeStreamInfo(extractorInput);
        this.readPastStreamInfo = true;
        if (this.streamInfo == null) {
            this.updateFlacStreamInfo(extractorInput, decodeStreamInfo);
        }
    }
    
    private void updateFlacStreamInfo(final ExtractorInput extractorInput, final FlacStreamInfo streamInfo) {
        this.outputSeekMap(extractorInput, this.streamInfo = streamInfo);
        this.outputFormat(streamInfo);
        this.outputBuffer = new ParsableByteArray(streamInfo.maxDecodedFrameSize());
        this.outputByteBuffer = ByteBuffer.wrap(this.outputBuffer.data);
        this.outputFrameHolder = new BinarySearchSeeker.OutputFrameHolder(this.outputByteBuffer);
    }
    
    private void writeLastSampleToOutput(final int n, final long n2) {
        this.outputBuffer.setPosition(0);
        this.trackOutput.sampleData(this.outputBuffer, n);
        this.trackOutput.sampleMetadata(n2, 1, n, 0, null);
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = this.extractorOutput.track(0, 1);
        this.extractorOutput.endTracks();
        try {
            this.decoderJni = new FlacDecoderJni();
        }
        catch (FlacDecoderException cause) {
            throw new RuntimeException(cause);
        }
    }
    
    @Override
    public int read(final ExtractorInput data, final PositionHolder positionHolder) throws IOException, InterruptedException {
        if (data.getPosition() == 0L && !this.isId3MetadataDisabled && this.id3Metadata == null) {
            this.id3Metadata = this.peekId3Data(data);
        }
        this.decoderJni.setData(data);
        this.readPastStreamInfo(data);
        final FlacBinarySearchSeeker flacBinarySearchSeeker = this.flacBinarySearchSeeker;
        if (flacBinarySearchSeeker != null && flacBinarySearchSeeker.isSeeking()) {
            return this.handlePendingSeek(data, positionHolder);
        }
        final long decodePosition = this.decoderJni.getDecodePosition();
        try {
            this.decoderJni.decodeSampleWithBacktrackPosition(this.outputByteBuffer, decodePosition);
            final int limit = this.outputByteBuffer.limit();
            int n = -1;
            if (limit == 0) {
                return -1;
            }
            this.writeLastSampleToOutput(limit, this.decoderJni.getLastFrameTimestamp());
            if (!this.decoderJni.isEndOfData()) {
                n = 0;
            }
            return n;
        }
        catch (FlacDecoderJni.FlacFrameDecodeException cause) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot read frame at position ");
            sb.append(decodePosition);
            throw new IOException(sb.toString(), cause);
        }
    }
    
    @Override
    public void release() {
        this.flacBinarySearchSeeker = null;
        final FlacDecoderJni decoderJni = this.decoderJni;
        if (decoderJni != null) {
            decoderJni.release();
            this.decoderJni = null;
        }
    }
    
    @Override
    public void seek(final long n, final long seekTargetUs) {
        if (n == 0L) {
            this.readPastStreamInfo = false;
        }
        final FlacDecoderJni decoderJni = this.decoderJni;
        if (decoderJni != null) {
            decoderJni.reset(n);
        }
        final FlacBinarySearchSeeker flacBinarySearchSeeker = this.flacBinarySearchSeeker;
        if (flacBinarySearchSeeker != null) {
            flacBinarySearchSeeker.setSeekTargetUs(seekTargetUs);
        }
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (extractorInput.getPosition() == 0L) {
            this.id3Metadata = this.peekId3Data(extractorInput);
        }
        return this.peekFlacSignature(extractorInput);
    }
    
    private static final class FlacSeekMap implements SeekMap
    {
        private final FlacDecoderJni decoderJni;
        private final long durationUs;
        
        public FlacSeekMap(final long durationUs, final FlacDecoderJni decoderJni) {
            this.durationUs = durationUs;
            this.decoderJni = decoderJni;
        }
        
        @Override
        public long getDurationUs() {
            return this.durationUs;
        }
        
        @Override
        public SeekPoints getSeekPoints(final long n) {
            return new SeekPoints(new SeekPoint(n, this.decoderJni.getSeekPosition(n)));
        }
        
        @Override
        public boolean isSeekable() {
            return true;
        }
    }
    
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }
}
