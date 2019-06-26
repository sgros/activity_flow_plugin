package com.google.android.exoplayer2.extractor.p002ts;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;

/* renamed from: com.google.android.exoplayer2.extractor.ts.MpegAudioReader */
public final class MpegAudioReader implements ElementaryStreamReader {
    private String formatId;
    private int frameBytesRead;
    private long frameDurationUs;
    private int frameSize;
    private boolean hasOutputFormat;
    private final MpegAudioHeader header;
    private final ParsableByteArray headerScratch;
    private final String language;
    private boolean lastByteWasFF;
    private TrackOutput output;
    private int state;
    private long timeUs;

    public void packetFinished() {
    }

    public MpegAudioReader() {
        this(null);
    }

    public MpegAudioReader(String str) {
        this.state = 0;
        this.headerScratch = new ParsableByteArray(4);
        this.headerScratch.data[0] = (byte) -1;
        this.header = new MpegAudioHeader();
        this.language = str;
    }

    public void seek() {
        this.state = 0;
        this.frameBytesRead = 0;
        this.lastByteWasFF = false;
    }

    public void createTracks(ExtractorOutput extractorOutput, TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 1);
    }

    public void packetStarted(long j, int i) {
        this.timeUs = j;
    }

    public void consume(ParsableByteArray parsableByteArray) {
        while (parsableByteArray.bytesLeft() > 0) {
            int i = this.state;
            if (i == 0) {
                findHeader(parsableByteArray);
            } else if (i == 1) {
                readHeaderRemainder(parsableByteArray);
            } else if (i == 2) {
                readFrameRemainder(parsableByteArray);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private void findHeader(ParsableByteArray parsableByteArray) {
        byte[] bArr = parsableByteArray.data;
        int limit = parsableByteArray.limit();
        for (int position = parsableByteArray.getPosition(); position < limit; position++) {
            boolean z = (bArr[position] & NalUnitUtil.EXTENDED_SAR) == NalUnitUtil.EXTENDED_SAR;
            Object obj = (this.lastByteWasFF && (bArr[position] & 224) == 224) ? 1 : null;
            this.lastByteWasFF = z;
            if (obj != null) {
                parsableByteArray.setPosition(position + 1);
                this.lastByteWasFF = false;
                this.headerScratch.data[1] = bArr[position];
                this.frameBytesRead = 2;
                this.state = 1;
                return;
            }
        }
        parsableByteArray.setPosition(limit);
    }

    private void readHeaderRemainder(ParsableByteArray parsableByteArray) {
        int min = Math.min(parsableByteArray.bytesLeft(), 4 - this.frameBytesRead);
        parsableByteArray.readBytes(this.headerScratch.data, this.frameBytesRead, min);
        this.frameBytesRead += min;
        if (this.frameBytesRead >= 4) {
            this.headerScratch.setPosition(0);
            if (MpegAudioHeader.populateHeader(this.headerScratch.readInt(), this.header)) {
                MpegAudioHeader mpegAudioHeader = this.header;
                this.frameSize = mpegAudioHeader.frameSize;
                if (!this.hasOutputFormat) {
                    long j = ((long) mpegAudioHeader.samplesPerFrame) * 1000000;
                    int i = mpegAudioHeader.sampleRate;
                    this.frameDurationUs = j / ((long) i);
                    this.output.format(Format.createAudioSampleFormat(this.formatId, mpegAudioHeader.mimeType, null, -1, 4096, mpegAudioHeader.channels, i, null, null, 0, this.language));
                    this.hasOutputFormat = true;
                }
                this.headerScratch.setPosition(0);
                this.output.sampleData(this.headerScratch, 4);
                this.state = 2;
                return;
            }
            this.frameBytesRead = 0;
            this.state = 1;
        }
    }

    private void readFrameRemainder(ParsableByteArray parsableByteArray) {
        int min = Math.min(parsableByteArray.bytesLeft(), this.frameSize - this.frameBytesRead);
        this.output.sampleData(parsableByteArray, min);
        this.frameBytesRead += min;
        int i = this.frameBytesRead;
        int i2 = this.frameSize;
        if (i >= i2) {
            this.output.sampleMetadata(this.timeUs, 1, i2, 0, null);
            this.timeUs += this.frameDurationUs;
            this.frameBytesRead = 0;
            this.state = 0;
        }
    }
}
