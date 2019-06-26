package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.Id3Peeker;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder.FramePredicate;
import com.google.android.exoplayer2.metadata.id3.MlltFrame;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import org.telegram.messenger.MessagesController;

public final class Mp3Extractor implements Extractor {
    public static final ExtractorsFactory FACTORY = C3334-$$Lambda$Mp3Extractor$6eyGfoogMVGFHZKg1gVp93FAKZA.INSTANCE;
    private static final FramePredicate REQUIRED_ID3_FRAME_PREDICATE = C3335-$$Lambda$Mp3Extractor$bb754AZIAMUosKBF4SefP1vYq88.INSTANCE;
    private static final int SEEK_HEADER_INFO = Util.getIntegerCodeForString("Info");
    private static final int SEEK_HEADER_VBRI = Util.getIntegerCodeForString("VBRI");
    private static final int SEEK_HEADER_XING = Util.getIntegerCodeForString("Xing");
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

    interface Seeker extends SeekMap {
        long getDataEndPosition();

        long getTimeUs(long j);
    }

    private static boolean headersMatch(int i, long j) {
        return ((long) (i & -128000)) == (j & -128000);
    }

    static /* synthetic */ boolean lambda$static$1(int i, int i2, int i3, int i4, int i5) {
        return (i2 == 67 && i3 == 79 && i4 == 77 && (i5 == 77 || i == 2)) || (i2 == 77 && i3 == 76 && i4 == 76 && (i5 == 84 || i == 2));
    }

    public void release() {
    }

    public Mp3Extractor() {
        this(0);
    }

    public Mp3Extractor(int i) {
        this(i, -9223372036854775807L);
    }

    public Mp3Extractor(int i, long j) {
        this.flags = i;
        this.forcedFirstSampleTimestampUs = j;
        this.scratch = new ParsableByteArray(10);
        this.synchronizedHeader = new MpegAudioHeader();
        this.gaplessInfoHolder = new GaplessInfoHolder();
        this.basisTimeUs = -9223372036854775807L;
        this.id3Peeker = new Id3Peeker();
    }

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        return synchronize(extractorInput, true);
    }

    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = this.extractorOutput.track(0, 1);
        this.extractorOutput.endTracks();
    }

    public void seek(long j, long j2) {
        this.synchronizedHeaderData = 0;
        this.basisTimeUs = -9223372036854775807L;
        this.samplesRead = 0;
        this.sampleBytesRemaining = 0;
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        if (this.synchronizedHeaderData == 0) {
            try {
                synchronize(extractorInput, false);
            } catch (EOFException unused) {
                return -1;
            }
        }
        ExtractorInput extractorInput2 = extractorInput;
        if (this.seeker == null) {
            Seeker maybeReadSeekFrame = maybeReadSeekFrame(extractorInput);
            MlltSeeker maybeHandleSeekMetadata = maybeHandleSeekMetadata(this.metadata, extractorInput.getPosition());
            if (maybeHandleSeekMetadata != null) {
                this.seeker = maybeHandleSeekMetadata;
            } else if (maybeReadSeekFrame != null) {
                this.seeker = maybeReadSeekFrame;
            }
            maybeReadSeekFrame = this.seeker;
            if (maybeReadSeekFrame == null || !(maybeReadSeekFrame.isSeekable() || (this.flags & 1) == 0)) {
                this.seeker = getConstantBitrateSeeker(extractorInput);
            }
            this.extractorOutput.seekMap(this.seeker);
            TrackOutput trackOutput = this.trackOutput;
            MpegAudioHeader mpegAudioHeader = this.synchronizedHeader;
            String str = mpegAudioHeader.mimeType;
            int i = mpegAudioHeader.channels;
            int i2 = mpegAudioHeader.sampleRate;
            GaplessInfoHolder gaplessInfoHolder = this.gaplessInfoHolder;
            trackOutput.format(Format.createAudioSampleFormat(null, str, null, -1, 4096, i, i2, -1, gaplessInfoHolder.encoderDelay, gaplessInfoHolder.encoderPadding, null, null, 0, null, (this.flags & 2) != 0 ? null : this.metadata));
        }
        return readSample(extractorInput);
    }

    private int readSample(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.sampleBytesRemaining == 0) {
            extractorInput.resetPeekPosition();
            if (peekEndOfStreamOrHeader(extractorInput)) {
                return -1;
            }
            this.scratch.setPosition(0);
            int readInt = this.scratch.readInt();
            if (!headersMatch(readInt, (long) this.synchronizedHeaderData) || MpegAudioHeader.getFrameSize(readInt) == -1) {
                extractorInput.skipFully(1);
                this.synchronizedHeaderData = 0;
                return 0;
            }
            MpegAudioHeader.populateHeader(readInt, this.synchronizedHeader);
            if (this.basisTimeUs == -9223372036854775807L) {
                this.basisTimeUs = this.seeker.getTimeUs(extractorInput.getPosition());
                if (this.forcedFirstSampleTimestampUs != -9223372036854775807L) {
                    this.basisTimeUs += this.forcedFirstSampleTimestampUs - this.seeker.getTimeUs(0);
                }
            }
            this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
        }
        int sampleData = this.trackOutput.sampleData(extractorInput, this.sampleBytesRemaining, true);
        if (sampleData == -1) {
            return -1;
        }
        this.sampleBytesRemaining -= sampleData;
        if (this.sampleBytesRemaining > 0) {
            return 0;
        }
        long j = this.basisTimeUs;
        long j2 = this.samplesRead * 1000000;
        MpegAudioHeader mpegAudioHeader = this.synchronizedHeader;
        this.trackOutput.sampleMetadata(j + (j2 / ((long) mpegAudioHeader.sampleRate)), 1, mpegAudioHeader.frameSize, 0, null);
        this.samplesRead += (long) this.synchronizedHeader.samplesPerFrame;
        this.sampleBytesRemaining = 0;
        return 0;
    }

    private boolean synchronize(ExtractorInput extractorInput, boolean z) throws IOException, InterruptedException {
        int peekPosition;
        int i;
        int i2;
        int i3;
        int i4 = z ? 16384 : MessagesController.UPDATE_MASK_REORDER;
        extractorInput.resetPeekPosition();
        if (extractorInput.getPosition() == 0) {
            FramePredicate framePredicate;
            if (((this.flags & 2) == 0 ? 1 : null) != null) {
                framePredicate = null;
            } else {
                framePredicate = REQUIRED_ID3_FRAME_PREDICATE;
            }
            this.metadata = this.id3Peeker.peekId3Data(extractorInput, framePredicate);
            Metadata metadata = this.metadata;
            if (metadata != null) {
                this.gaplessInfoHolder.setFromMetadata(metadata);
            }
            peekPosition = (int) extractorInput.getPeekPosition();
            if (!z) {
                extractorInput.skipFully(peekPosition);
            }
            i = peekPosition;
            i2 = 0;
            peekPosition = 0;
            i3 = 0;
        } else {
            i2 = 0;
            peekPosition = 0;
            i3 = 0;
            i = 0;
        }
        while (!peekEndOfStreamOrHeader(extractorInput)) {
            this.scratch.setPosition(0);
            int readInt = this.scratch.readInt();
            if (i2 == 0 || headersMatch(readInt, (long) i2)) {
                int frameSize = MpegAudioHeader.getFrameSize(readInt);
                if (frameSize != -1) {
                    peekPosition++;
                    if (peekPosition != 1) {
                        if (peekPosition == 4) {
                            break;
                        }
                    }
                    MpegAudioHeader.populateHeader(readInt, this.synchronizedHeader);
                    i2 = readInt;
                    extractorInput.advancePeekPosition(frameSize - 4);
                }
            }
            i2 = i3 + 1;
            if (i3 != i4) {
                if (z) {
                    extractorInput.resetPeekPosition();
                    extractorInput.advancePeekPosition(i + i2);
                } else {
                    extractorInput.skipFully(1);
                }
                i3 = i2;
                i2 = 0;
                peekPosition = 0;
            } else if (z) {
                return false;
            } else {
                throw new ParserException("Searched too many bytes.");
            }
        }
        if (peekPosition <= 0) {
            throw new EOFException();
        }
        if (z) {
            extractorInput.skipFully(i + i3);
        } else {
            extractorInput.resetPeekPosition();
        }
        this.synchronizedHeaderData = i2;
        return true;
    }

    private boolean peekEndOfStreamOrHeader(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if ((this.seeker == null || extractorInput.getPeekPosition() != this.seeker.getDataEndPosition()) && extractorInput.peekFully(this.scratch.data, 0, 4, true)) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0039  */
    private com.google.android.exoplayer2.extractor.mp3.Mp3Extractor.Seeker maybeReadSeekFrame(com.google.android.exoplayer2.extractor.ExtractorInput r10) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r9 = this;
        r5 = new com.google.android.exoplayer2.util.ParsableByteArray;
        r0 = r9.synchronizedHeader;
        r0 = r0.frameSize;
        r5.<init>(r0);
        r0 = r5.data;
        r1 = r9.synchronizedHeader;
        r1 = r1.frameSize;
        r6 = 0;
        r10.peekFully(r0, r6, r1);
        r0 = r9.synchronizedHeader;
        r1 = r0.version;
        r2 = 1;
        r1 = r1 & r2;
        r3 = 21;
        if (r1 == 0) goto L_0x0026;
    L_0x001d:
        r0 = r0.channels;
        if (r0 == r2) goto L_0x002a;
    L_0x0021:
        r3 = 36;
        r7 = 36;
        goto L_0x0031;
    L_0x0026:
        r0 = r0.channels;
        if (r0 == r2) goto L_0x002d;
    L_0x002a:
        r7 = 21;
        goto L_0x0031;
    L_0x002d:
        r3 = 13;
        r7 = 13;
    L_0x0031:
        r8 = getSeekFrameHeader(r5, r7);
        r0 = SEEK_HEADER_XING;
        if (r8 == r0) goto L_0x005d;
    L_0x0039:
        r0 = SEEK_HEADER_INFO;
        if (r8 != r0) goto L_0x003e;
    L_0x003d:
        goto L_0x005d;
    L_0x003e:
        r0 = SEEK_HEADER_VBRI;
        if (r8 != r0) goto L_0x0058;
    L_0x0042:
        r0 = r10.getLength();
        r2 = r10.getPosition();
        r4 = r9.synchronizedHeader;
        r0 = com.google.android.exoplayer2.extractor.mp3.VbriSeeker.create(r0, r2, r4, r5);
        r1 = r9.synchronizedHeader;
        r1 = r1.frameSize;
        r10.skipFully(r1);
        goto L_0x00ad;
    L_0x0058:
        r0 = 0;
        r10.resetPeekPosition();
        goto L_0x00ad;
    L_0x005d:
        r0 = r10.getLength();
        r2 = r10.getPosition();
        r4 = r9.synchronizedHeader;
        r0 = com.google.android.exoplayer2.extractor.mp3.XingSeeker.create(r0, r2, r4, r5);
        if (r0 == 0) goto L_0x0095;
    L_0x006d:
        r1 = r9.gaplessInfoHolder;
        r1 = r1.hasGaplessInfo();
        if (r1 != 0) goto L_0x0095;
    L_0x0075:
        r10.resetPeekPosition();
        r7 = r7 + 141;
        r10.advancePeekPosition(r7);
        r1 = r9.scratch;
        r1 = r1.data;
        r2 = 3;
        r10.peekFully(r1, r6, r2);
        r1 = r9.scratch;
        r1.setPosition(r6);
        r1 = r9.gaplessInfoHolder;
        r2 = r9.scratch;
        r2 = r2.readUnsignedInt24();
        r1.setFromXingHeaderValue(r2);
    L_0x0095:
        r1 = r9.synchronizedHeader;
        r1 = r1.frameSize;
        r10.skipFully(r1);
        if (r0 == 0) goto L_0x00ad;
    L_0x009e:
        r1 = r0.isSeekable();
        if (r1 != 0) goto L_0x00ad;
    L_0x00a4:
        r1 = SEEK_HEADER_INFO;
        if (r8 != r1) goto L_0x00ad;
    L_0x00a8:
        r10 = r9.getConstantBitrateSeeker(r10);
        return r10;
    L_0x00ad:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp3.Mp3Extractor.maybeReadSeekFrame(com.google.android.exoplayer2.extractor.ExtractorInput):com.google.android.exoplayer2.extractor.mp3.Mp3Extractor$Seeker");
    }

    private Seeker getConstantBitrateSeeker(ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
        return new ConstantBitrateSeeker(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader);
    }

    private static int getSeekFrameHeader(ParsableByteArray parsableByteArray, int i) {
        if (parsableByteArray.limit() >= i + 4) {
            parsableByteArray.setPosition(i);
            i = parsableByteArray.readInt();
            if (i == SEEK_HEADER_XING || i == SEEK_HEADER_INFO) {
                return i;
            }
        }
        if (parsableByteArray.limit() >= 40) {
            parsableByteArray.setPosition(36);
            int readInt = parsableByteArray.readInt();
            i = SEEK_HEADER_VBRI;
            if (readInt == i) {
                return i;
            }
        }
        return 0;
    }

    private static MlltSeeker maybeHandleSeekMetadata(Metadata metadata, long j) {
        if (metadata != null) {
            int length = metadata.length();
            for (int i = 0; i < length; i++) {
                Entry entry = metadata.get(i);
                if (entry instanceof MlltFrame) {
                    return MlltSeeker.create(j, (MlltFrame) entry);
                }
            }
        }
        return null;
    }
}
