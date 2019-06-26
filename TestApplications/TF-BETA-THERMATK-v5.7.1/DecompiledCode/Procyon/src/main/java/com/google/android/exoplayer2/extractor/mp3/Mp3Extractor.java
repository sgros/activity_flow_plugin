// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.PositionHolder;
import java.io.EOFException;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.metadata.id3.MlltFrame;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.extractor.Id3Peeker;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public final class Mp3Extractor implements Extractor
{
    public static final ExtractorsFactory FACTORY;
    private static final Id3Decoder.FramePredicate REQUIRED_ID3_FRAME_PREDICATE;
    private static final int SEEK_HEADER_INFO;
    private static final int SEEK_HEADER_VBRI;
    private static final int SEEK_HEADER_XING;
    private long basisTimeUs;
    private ExtractorOutput extractorOutput;
    private final int flags;
    private final long forcedFirstSampleTimestampUs;
    private final GaplessInfoHolder gaplessInfoHolder;
    private final Id3Peeker id3Peeker;
    private Metadata metadata;
    private int sampleBytesRemaining;
    private long samplesRead;
    private final ParsableByteArray scratch;
    private Seeker seeker;
    private final MpegAudioHeader synchronizedHeader;
    private int synchronizedHeaderData;
    private TrackOutput trackOutput;
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$Mp3Extractor$6eyGfoogMVGFHZKg1gVp93FAKZA.INSTANCE;
        REQUIRED_ID3_FRAME_PREDICATE = (Id3Decoder.FramePredicate)_$$Lambda$Mp3Extractor$bb754AZIAMUosKBF4SefP1vYq88.INSTANCE;
        SEEK_HEADER_XING = Util.getIntegerCodeForString("Xing");
        SEEK_HEADER_INFO = Util.getIntegerCodeForString("Info");
        SEEK_HEADER_VBRI = Util.getIntegerCodeForString("VBRI");
    }
    
    public Mp3Extractor() {
        this(0);
    }
    
    public Mp3Extractor(final int n) {
        this(n, -9223372036854775807L);
    }
    
    public Mp3Extractor(final int flags, final long forcedFirstSampleTimestampUs) {
        this.flags = flags;
        this.forcedFirstSampleTimestampUs = forcedFirstSampleTimestampUs;
        this.scratch = new ParsableByteArray(10);
        this.synchronizedHeader = new MpegAudioHeader();
        this.gaplessInfoHolder = new GaplessInfoHolder();
        this.basisTimeUs = -9223372036854775807L;
        this.id3Peeker = new Id3Peeker();
    }
    
    private Seeker getConstantBitrateSeeker(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
        return (Seeker)new ConstantBitrateSeeker(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader);
    }
    
    private static int getSeekFrameHeader(final ParsableByteArray parsableByteArray, int position) {
        if (parsableByteArray.limit() >= position + 4) {
            parsableByteArray.setPosition(position);
            position = parsableByteArray.readInt();
            if (position == Mp3Extractor.SEEK_HEADER_XING || position == Mp3Extractor.SEEK_HEADER_INFO) {
                return position;
            }
        }
        if (parsableByteArray.limit() >= 40) {
            parsableByteArray.setPosition(36);
            final int int1 = parsableByteArray.readInt();
            position = Mp3Extractor.SEEK_HEADER_VBRI;
            if (int1 == position) {
                return position;
            }
        }
        return 0;
    }
    
    private static boolean headersMatch(final int n, final long n2) {
        return (n & 0xFFFE0C00) == (n2 & 0xFFFFFFFFFFFE0C00L);
    }
    
    private static MlltSeeker maybeHandleSeekMetadata(final Metadata metadata, final long n) {
        if (metadata != null) {
            for (int length = metadata.length(), i = 0; i < length; ++i) {
                final Metadata.Entry value = metadata.get(i);
                if (value instanceof MlltFrame) {
                    return MlltSeeker.create(n, (MlltFrame)value);
                }
            }
        }
        return null;
    }
    
    private Seeker maybeReadSeekFrame(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final ParsableByteArray parsableByteArray = new ParsableByteArray(this.synchronizedHeader.frameSize);
        extractorInput.peekFully(parsableByteArray.data, 0, this.synchronizedHeader.frameSize);
        final MpegAudioHeader synchronizedHeader = this.synchronizedHeader;
        int n = 0;
        Label_0081: {
            if ((synchronizedHeader.version & 0x1) != 0x0) {
                if (synchronizedHeader.channels != 1) {
                    n = 36;
                    break Label_0081;
                }
            }
            else if (synchronizedHeader.channels == 1) {
                n = 13;
                break Label_0081;
            }
            n = 21;
        }
        final int seekFrameHeader = getSeekFrameHeader(parsableByteArray, n);
        Object create;
        if (seekFrameHeader != Mp3Extractor.SEEK_HEADER_XING && seekFrameHeader != Mp3Extractor.SEEK_HEADER_INFO) {
            if (seekFrameHeader == Mp3Extractor.SEEK_HEADER_VBRI) {
                create = VbriSeeker.create(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader, parsableByteArray);
                extractorInput.skipFully(this.synchronizedHeader.frameSize);
            }
            else {
                create = null;
                extractorInput.resetPeekPosition();
            }
        }
        else {
            final XingSeeker create2 = XingSeeker.create(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader, parsableByteArray);
            if (create2 != null && !this.gaplessInfoHolder.hasGaplessInfo()) {
                extractorInput.resetPeekPosition();
                extractorInput.advancePeekPosition(n + 141);
                extractorInput.peekFully(this.scratch.data, 0, 3);
                this.scratch.setPosition(0);
                this.gaplessInfoHolder.setFromXingHeaderValue(this.scratch.readUnsignedInt24());
            }
            extractorInput.skipFully(this.synchronizedHeader.frameSize);
            if ((create = create2) != null) {
                create = create2;
                if (!create2.isSeekable()) {
                    create = create2;
                    if (seekFrameHeader == Mp3Extractor.SEEK_HEADER_INFO) {
                        return this.getConstantBitrateSeeker(extractorInput);
                    }
                }
            }
        }
        return (Seeker)create;
    }
    
    private boolean peekEndOfStreamOrHeader(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final Seeker seeker = this.seeker;
        final boolean b = true;
        if (seeker != null) {
            final boolean b2 = b;
            if (extractorInput.getPeekPosition() == this.seeker.getDataEndPosition()) {
                return b2;
            }
        }
        return !extractorInput.peekFully(this.scratch.data, 0, 4, true) && b;
    }
    
    private int readSample(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.sampleBytesRemaining == 0) {
            extractorInput.resetPeekPosition();
            if (this.peekEndOfStreamOrHeader(extractorInput)) {
                return -1;
            }
            this.scratch.setPosition(0);
            final int int1 = this.scratch.readInt();
            if (!headersMatch(int1, this.synchronizedHeaderData) || MpegAudioHeader.getFrameSize(int1) == -1) {
                extractorInput.skipFully(1);
                return this.synchronizedHeaderData = 0;
            }
            MpegAudioHeader.populateHeader(int1, this.synchronizedHeader);
            if (this.basisTimeUs == -9223372036854775807L) {
                this.basisTimeUs = this.seeker.getTimeUs(extractorInput.getPosition());
                if (this.forcedFirstSampleTimestampUs != -9223372036854775807L) {
                    this.basisTimeUs += this.forcedFirstSampleTimestampUs - this.seeker.getTimeUs(0L);
                }
            }
            this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
        }
        final int sampleData = this.trackOutput.sampleData(extractorInput, this.sampleBytesRemaining, true);
        if (sampleData == -1) {
            return -1;
        }
        this.sampleBytesRemaining -= sampleData;
        if (this.sampleBytesRemaining > 0) {
            return 0;
        }
        final long basisTimeUs = this.basisTimeUs;
        final long samplesRead = this.samplesRead;
        final MpegAudioHeader synchronizedHeader = this.synchronizedHeader;
        this.trackOutput.sampleMetadata(basisTimeUs + samplesRead * 1000000L / synchronizedHeader.sampleRate, 1, synchronizedHeader.frameSize, 0, null);
        this.samplesRead += this.synchronizedHeader.samplesPerFrame;
        return this.sampleBytesRemaining = 0;
    }
    
    private boolean synchronize(final ExtractorInput extractorInput, final boolean b) throws IOException, InterruptedException {
        int n;
        if (b) {
            n = 16384;
        }
        else {
            n = 131072;
        }
        extractorInput.resetPeekPosition();
        int n2;
        int synchronizedHeaderData;
        int n3;
        int n4;
        if (extractorInput.getPosition() == 0L) {
            Id3Decoder.FramePredicate required_ID3_FRAME_PREDICATE;
            if ((this.flags & 0x2) == 0x0) {
                required_ID3_FRAME_PREDICATE = null;
            }
            else {
                required_ID3_FRAME_PREDICATE = Mp3Extractor.REQUIRED_ID3_FRAME_PREDICATE;
            }
            this.metadata = this.id3Peeker.peekId3Data(extractorInput, required_ID3_FRAME_PREDICATE);
            final Metadata metadata = this.metadata;
            if (metadata != null) {
                this.gaplessInfoHolder.setFromMetadata(metadata);
            }
            n2 = (int)extractorInput.getPeekPosition();
            if (!b) {
                extractorInput.skipFully(n2);
            }
            synchronizedHeaderData = 0;
            n3 = 0;
            n4 = 0;
        }
        else {
            synchronizedHeaderData = 0;
            n3 = 0;
            n4 = 0;
            n2 = 0;
        }
        while (true) {
            while (!this.peekEndOfStreamOrHeader(extractorInput)) {
                this.scratch.setPosition(0);
                final int int1 = this.scratch.readInt();
                if (synchronizedHeaderData == 0 || headersMatch(int1, synchronizedHeaderData)) {
                    final int frameSize = MpegAudioHeader.getFrameSize(int1);
                    if (frameSize != -1) {
                        final int n5 = n3 + 1;
                        int n6;
                        if (n5 == 1) {
                            MpegAudioHeader.populateHeader(int1, this.synchronizedHeader);
                            n6 = int1;
                        }
                        else {
                            n6 = synchronizedHeaderData;
                            if (n5 == 4) {
                                if (b) {
                                    extractorInput.skipFully(n2 + n4);
                                }
                                else {
                                    extractorInput.resetPeekPosition();
                                }
                                this.synchronizedHeaderData = synchronizedHeaderData;
                                return true;
                            }
                        }
                        extractorInput.advancePeekPosition(frameSize - 4);
                        synchronizedHeaderData = n6;
                        n3 = n5;
                        continue;
                    }
                }
                final int n7 = n4 + 1;
                if (n4 == n) {
                    if (b) {
                        return false;
                    }
                    throw new ParserException("Searched too many bytes.");
                }
                else {
                    if (b) {
                        extractorInput.resetPeekPosition();
                        extractorInput.advancePeekPosition(n2 + n7);
                    }
                    else {
                        extractorInput.skipFully(1);
                    }
                    n4 = n7;
                    synchronizedHeaderData = 0;
                    n3 = 0;
                }
            }
            if (n3 > 0) {
                continue;
            }
            break;
        }
        throw new EOFException();
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = this.extractorOutput.track(0, 1);
        this.extractorOutput.endTracks();
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        if (this.synchronizedHeaderData == 0) {
            try {
                this.synchronize(extractorInput, false);
            }
            catch (EOFException ex) {
                return -1;
            }
        }
        if (this.seeker == null) {
            final Seeker maybeReadSeekFrame = this.maybeReadSeekFrame(extractorInput);
            final MlltSeeker maybeHandleSeekMetadata = maybeHandleSeekMetadata(this.metadata, extractorInput.getPosition());
            if (maybeHandleSeekMetadata != null) {
                this.seeker = (Seeker)maybeHandleSeekMetadata;
            }
            else if (maybeReadSeekFrame != null) {
                this.seeker = maybeReadSeekFrame;
            }
            final Seeker seeker = this.seeker;
            if (seeker == null || (!seeker.isSeekable() && (this.flags & 0x1) != 0x0)) {
                this.seeker = this.getConstantBitrateSeeker(extractorInput);
            }
            this.extractorOutput.seekMap(this.seeker);
            final TrackOutput trackOutput = this.trackOutput;
            final MpegAudioHeader synchronizedHeader = this.synchronizedHeader;
            final String mimeType = synchronizedHeader.mimeType;
            final int channels = synchronizedHeader.channels;
            final int sampleRate = synchronizedHeader.sampleRate;
            final GaplessInfoHolder gaplessInfoHolder = this.gaplessInfoHolder;
            final int encoderDelay = gaplessInfoHolder.encoderDelay;
            final int encoderPadding = gaplessInfoHolder.encoderPadding;
            Metadata metadata;
            if ((this.flags & 0x2) != 0x0) {
                metadata = null;
            }
            else {
                metadata = this.metadata;
            }
            trackOutput.format(Format.createAudioSampleFormat(null, mimeType, null, -1, 4096, channels, sampleRate, -1, encoderDelay, encoderPadding, null, null, 0, null, metadata));
        }
        return this.readSample(extractorInput);
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        this.synchronizedHeaderData = 0;
        this.basisTimeUs = -9223372036854775807L;
        this.samplesRead = 0L;
        this.sampleBytesRemaining = 0;
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        return this.synchronize(extractorInput, true);
    }
    
    interface Seeker extends SeekMap
    {
        long getDataEndPosition();
        
        long getTimeUs(final long p0);
    }
}
