package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekMap.Unseekable;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

abstract class StreamReader {
    private long currentGranule;
    private ExtractorOutput extractorOutput;
    private boolean formatSet;
    private long lengthOfReadPacket;
    private final OggPacket oggPacket = new OggPacket();
    private OggSeeker oggSeeker;
    private long payloadStartPosition;
    private int sampleRate;
    private boolean seekMapSet;
    private SetupData setupData;
    private int state;
    private long targetGranule;
    private TrackOutput trackOutput;

    static class SetupData {
        Format format;
        OggSeeker oggSeeker;

        SetupData() {
        }
    }

    private static final class UnseekableOggSeeker implements OggSeeker {
        public long read(ExtractorInput extractorInput) throws IOException, InterruptedException {
            return -1;
        }

        public long startSeek(long j) {
            return 0;
        }

        private UnseekableOggSeeker() {
        }

        public SeekMap createSeekMap() {
            return new Unseekable(-9223372036854775807L);
        }
    }

    public abstract long preparePayload(ParsableByteArray parsableByteArray);

    public abstract boolean readHeaders(ParsableByteArray parsableByteArray, long j, SetupData setupData) throws IOException, InterruptedException;

    /* Access modifiers changed, original: 0000 */
    public void init(ExtractorOutput extractorOutput, TrackOutput trackOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = trackOutput;
        reset(true);
    }

    /* Access modifiers changed, original: protected */
    public void reset(boolean z) {
        if (z) {
            this.setupData = new SetupData();
            this.payloadStartPosition = 0;
            this.state = 0;
        } else {
            this.state = 1;
        }
        this.targetGranule = -1;
        this.currentGranule = 0;
    }

    /* Access modifiers changed, original: final */
    public final void seek(long j, long j2) {
        this.oggPacket.reset();
        if (j == 0) {
            reset(this.seekMapSet ^ 1);
        } else if (this.state != 0) {
            this.targetGranule = this.oggSeeker.startSeek(j2);
            this.state = 2;
        }
    }

    /* Access modifiers changed, original: final */
    public final int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        int i = this.state;
        if (i == 0) {
            return readHeaders(extractorInput);
        }
        if (i == 1) {
            extractorInput.skipFully((int) this.payloadStartPosition);
            this.state = 2;
            return 0;
        } else if (i == 2) {
            return readPayload(extractorInput, positionHolder);
        } else {
            throw new IllegalStateException();
        }
    }

    private int readHeaders(ExtractorInput extractorInput) throws IOException, InterruptedException {
        boolean z = true;
        while (z) {
            if (this.oggPacket.populate(extractorInput)) {
                this.lengthOfReadPacket = extractorInput.getPosition() - this.payloadStartPosition;
                z = readHeaders(this.oggPacket.getPayload(), this.payloadStartPosition, this.setupData);
                if (z) {
                    this.payloadStartPosition = extractorInput.getPosition();
                }
            } else {
                this.state = 3;
                return -1;
            }
        }
        ExtractorInput extractorInput2 = extractorInput;
        Format format = this.setupData.format;
        this.sampleRate = format.sampleRate;
        if (!this.formatSet) {
            this.trackOutput.format(format);
            this.formatSet = true;
        }
        OggSeeker oggSeeker = this.setupData.oggSeeker;
        if (oggSeeker != null) {
            this.oggSeeker = oggSeeker;
        } else if (extractorInput.getLength() == -1) {
            this.oggSeeker = new UnseekableOggSeeker();
        } else {
            OggPageHeader pageHeader = this.oggPacket.getPageHeader();
            this.oggSeeker = new DefaultOggSeeker(this.payloadStartPosition, extractorInput.getLength(), this, (long) (pageHeader.headerSize + pageHeader.bodySize), pageHeader.granulePosition, (pageHeader.type & 4) != 0);
        }
        this.setupData = null;
        this.state = 2;
        this.oggPacket.trimPayload();
        return 0;
    }

    private int readPayload(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        ExtractorInput extractorInput2 = extractorInput;
        long read = this.oggSeeker.read(extractorInput2);
        if (read >= 0) {
            positionHolder.position = read;
            return 1;
        }
        if (read < -1) {
            onSeekEnd(-(read + 2));
        }
        if (!this.seekMapSet) {
            this.extractorOutput.seekMap(this.oggSeeker.createSeekMap());
            this.seekMapSet = true;
        }
        if (this.lengthOfReadPacket > 0 || this.oggPacket.populate(extractorInput2)) {
            this.lengthOfReadPacket = 0;
            ParsableByteArray payload = this.oggPacket.getPayload();
            read = preparePayload(payload);
            if (read >= 0) {
                long j = this.currentGranule;
                if (j + read >= this.targetGranule) {
                    long convertGranuleToTime = convertGranuleToTime(j);
                    this.trackOutput.sampleData(payload, payload.limit());
                    this.trackOutput.sampleMetadata(convertGranuleToTime, 1, payload.limit(), 0, null);
                    this.targetGranule = -1;
                }
            }
            this.currentGranule += read;
            return 0;
        }
        this.state = 3;
        return -1;
    }

    /* Access modifiers changed, original: protected */
    public long convertGranuleToTime(long j) {
        return (j * 1000000) / ((long) this.sampleRate);
    }

    /* Access modifiers changed, original: protected */
    public long convertTimeToGranule(long j) {
        return (((long) this.sampleRate) * j) / 1000000;
    }

    /* Access modifiers changed, original: protected */
    public void onSeekEnd(long j) {
        this.currentGranule = j;
    }
}
