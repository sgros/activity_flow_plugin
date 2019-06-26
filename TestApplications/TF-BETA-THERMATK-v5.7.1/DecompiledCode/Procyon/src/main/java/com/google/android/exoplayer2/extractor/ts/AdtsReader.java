// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.ParserException;
import android.util.Pair;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.Format;
import java.util.Collections;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.Log;
import java.util.Arrays;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableBitArray;

public final class AdtsReader implements ElementaryStreamReader
{
    private static final byte[] ID3_IDENTIFIER;
    private final ParsableBitArray adtsScratch;
    private int bytesRead;
    private int currentFrameVersion;
    private TrackOutput currentOutput;
    private long currentSampleDuration;
    private final boolean exposeId3;
    private int firstFrameSampleRateIndex;
    private int firstFrameVersion;
    private String formatId;
    private boolean foundFirstFrame;
    private boolean hasCrc;
    private boolean hasOutputFormat;
    private final ParsableByteArray id3HeaderBuffer;
    private TrackOutput id3Output;
    private final String language;
    private int matchState;
    private TrackOutput output;
    private long sampleDurationUs;
    private int sampleSize;
    private int state;
    private long timeUs;
    
    static {
        ID3_IDENTIFIER = new byte[] { 73, 68, 51 };
    }
    
    public AdtsReader(final boolean b) {
        this(b, null);
    }
    
    public AdtsReader(final boolean exposeId3, final String language) {
        this.adtsScratch = new ParsableBitArray(new byte[7]);
        this.id3HeaderBuffer = new ParsableByteArray(Arrays.copyOf(AdtsReader.ID3_IDENTIFIER, 10));
        this.setFindingSampleState();
        this.firstFrameVersion = -1;
        this.firstFrameSampleRateIndex = -1;
        this.sampleDurationUs = -9223372036854775807L;
        this.exposeId3 = exposeId3;
        this.language = language;
    }
    
    private void checkAdtsHeader(final ParsableByteArray parsableByteArray) {
        if (parsableByteArray.bytesLeft() == 0) {
            return;
        }
        this.adtsScratch.data[0] = parsableByteArray.data[parsableByteArray.getPosition()];
        this.adtsScratch.setPosition(2);
        final int bits = this.adtsScratch.readBits(4);
        final int firstFrameSampleRateIndex = this.firstFrameSampleRateIndex;
        if (firstFrameSampleRateIndex != -1 && bits != firstFrameSampleRateIndex) {
            this.resetSync();
            return;
        }
        if (!this.foundFirstFrame) {
            this.foundFirstFrame = true;
            this.firstFrameVersion = this.currentFrameVersion;
            this.firstFrameSampleRateIndex = bits;
        }
        this.setReadingAdtsHeaderState();
    }
    
    private boolean checkSyncPositionValid(final ParsableByteArray parsableByteArray, int n) {
        parsableByteArray.setPosition(n + 1);
        final byte[] data = this.adtsScratch.data;
        final boolean b = true;
        if (!this.tryRead(parsableByteArray, data, 1)) {
            return false;
        }
        this.adtsScratch.setPosition(4);
        final int bits = this.adtsScratch.readBits(1);
        final int firstFrameVersion = this.firstFrameVersion;
        if (firstFrameVersion != -1 && bits != firstFrameVersion) {
            return false;
        }
        if (this.firstFrameSampleRateIndex != -1) {
            if (!this.tryRead(parsableByteArray, this.adtsScratch.data, 1)) {
                return true;
            }
            this.adtsScratch.setPosition(2);
            if (this.adtsScratch.readBits(4) != this.firstFrameSampleRateIndex) {
                return false;
            }
            parsableByteArray.setPosition(n + 2);
        }
        if (!this.tryRead(parsableByteArray, this.adtsScratch.data, 4)) {
            return true;
        }
        this.adtsScratch.setPosition(14);
        final int bits2 = this.adtsScratch.readBits(13);
        if (bits2 <= 6) {
            return false;
        }
        n += bits2;
        final int n2 = n + 1;
        if (n2 >= parsableByteArray.limit()) {
            return true;
        }
        final byte[] data2 = parsableByteArray.data;
        if (this.isAdtsSyncBytes(data2[n], data2[n2])) {
            boolean b2 = b;
            if (this.firstFrameVersion == -1) {
                return b2;
            }
            if ((parsableByteArray.data[n2] & 0x8) >> 3 == bits) {
                b2 = b;
                return b2;
            }
        }
        return false;
    }
    
    private boolean continueRead(final ParsableByteArray parsableByteArray, final byte[] array, final int n) {
        final int min = Math.min(parsableByteArray.bytesLeft(), n - this.bytesRead);
        parsableByteArray.readBytes(array, this.bytesRead, min);
        this.bytesRead += min;
        return this.bytesRead == n;
    }
    
    private void findNextSample(final ParsableByteArray parsableByteArray) {
        final byte[] data = parsableByteArray.data;
        int i = parsableByteArray.getPosition();
        while (i < parsableByteArray.limit()) {
            final int n = i + 1;
            final int n2 = data[i] & 0xFF;
            if (this.matchState == 512 && this.isAdtsSyncBytes((byte)(-1), (byte)n2) && (this.foundFirstFrame || this.checkSyncPositionValid(parsableByteArray, n - 2))) {
                this.currentFrameVersion = (n2 & 0x8) >> 3;
                boolean hasCrc = true;
                if ((n2 & 0x1) != 0x0) {
                    hasCrc = false;
                }
                this.hasCrc = hasCrc;
                if (!this.foundFirstFrame) {
                    this.setCheckingAdtsHeaderState();
                }
                else {
                    this.setReadingAdtsHeaderState();
                }
                parsableByteArray.setPosition(n);
                return;
            }
            final int matchState = this.matchState;
            final int n3 = n2 | matchState;
            if (n3 != 329) {
                if (n3 != 511) {
                    if (n3 != 836) {
                        if (n3 == 1075) {
                            this.setReadingId3HeaderState();
                            parsableByteArray.setPosition(n);
                            return;
                        }
                        i = n;
                        if (matchState == 256) {
                            continue;
                        }
                        this.matchState = 256;
                        i = n - 1;
                    }
                    else {
                        this.matchState = 1024;
                        i = n;
                    }
                }
                else {
                    this.matchState = 512;
                    i = n;
                }
            }
            else {
                this.matchState = 768;
                i = n;
            }
        }
        parsableByteArray.setPosition(i);
    }
    
    private boolean isAdtsSyncBytes(final byte b, final byte b2) {
        return isAdtsSyncWord((b & 0xFF) << 8 | (b2 & 0xFF));
    }
    
    public static boolean isAdtsSyncWord(final int n) {
        return (n & 0xFFF6) == 0xFFF0;
    }
    
    private void parseAdtsHeader() throws ParserException {
        this.adtsScratch.setPosition(0);
        if (!this.hasOutputFormat) {
            final int i = this.adtsScratch.readBits(2) + 1;
            int n;
            if ((n = i) != 2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Detected audio object type: ");
                sb.append(i);
                sb.append(", but assuming AAC LC.");
                Log.w("AdtsReader", sb.toString());
                n = 2;
            }
            this.adtsScratch.skipBits(5);
            final byte[] buildAacAudioSpecificConfig = CodecSpecificDataUtil.buildAacAudioSpecificConfig(n, this.firstFrameSampleRateIndex, this.adtsScratch.readBits(3));
            final Pair<Integer, Integer> aacAudioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(buildAacAudioSpecificConfig);
            final Format audioSampleFormat = Format.createAudioSampleFormat(this.formatId, "audio/mp4a-latm", null, -1, -1, (int)aacAudioSpecificConfig.second, (int)aacAudioSpecificConfig.first, Collections.singletonList(buildAacAudioSpecificConfig), null, 0, this.language);
            this.sampleDurationUs = 1024000000L / audioSampleFormat.sampleRate;
            this.output.format(audioSampleFormat);
            this.hasOutputFormat = true;
        }
        else {
            this.adtsScratch.skipBits(10);
        }
        this.adtsScratch.skipBits(4);
        int n2 = this.adtsScratch.readBits(13) - 2 - 5;
        if (this.hasCrc) {
            n2 -= 2;
        }
        this.setReadingSampleState(this.output, this.sampleDurationUs, 0, n2);
    }
    
    private void parseId3Header() {
        this.id3Output.sampleData(this.id3HeaderBuffer, 10);
        this.id3HeaderBuffer.setPosition(6);
        this.setReadingSampleState(this.id3Output, 0L, 10, this.id3HeaderBuffer.readSynchSafeInt() + 10);
    }
    
    private void readSample(final ParsableByteArray parsableByteArray) {
        final int min = Math.min(parsableByteArray.bytesLeft(), this.sampleSize - this.bytesRead);
        this.currentOutput.sampleData(parsableByteArray, min);
        this.bytesRead += min;
        final int bytesRead = this.bytesRead;
        final int sampleSize = this.sampleSize;
        if (bytesRead == sampleSize) {
            this.currentOutput.sampleMetadata(this.timeUs, 1, sampleSize, 0, null);
            this.timeUs += this.currentSampleDuration;
            this.setFindingSampleState();
        }
    }
    
    private void resetSync() {
        this.foundFirstFrame = false;
        this.setFindingSampleState();
    }
    
    private void setCheckingAdtsHeaderState() {
        this.state = 1;
        this.bytesRead = 0;
    }
    
    private void setFindingSampleState() {
        this.state = 0;
        this.bytesRead = 0;
        this.matchState = 256;
    }
    
    private void setReadingAdtsHeaderState() {
        this.state = 3;
        this.bytesRead = 0;
    }
    
    private void setReadingId3HeaderState() {
        this.state = 2;
        this.bytesRead = AdtsReader.ID3_IDENTIFIER.length;
        this.sampleSize = 0;
        this.id3HeaderBuffer.setPosition(0);
    }
    
    private void setReadingSampleState(final TrackOutput currentOutput, final long currentSampleDuration, final int bytesRead, final int sampleSize) {
        this.state = 4;
        this.bytesRead = bytesRead;
        this.currentOutput = currentOutput;
        this.currentSampleDuration = currentSampleDuration;
        this.sampleSize = sampleSize;
    }
    
    private boolean tryRead(final ParsableByteArray parsableByteArray, final byte[] array, final int n) {
        if (parsableByteArray.bytesLeft() < n) {
            return false;
        }
        parsableByteArray.readBytes(array, 0, n);
        return true;
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray) throws ParserException {
        while (parsableByteArray.bytesLeft() > 0) {
            final int state = this.state;
            if (state != 0) {
                if (state != 1) {
                    if (state != 2) {
                        if (state != 3) {
                            if (state != 4) {
                                throw new IllegalStateException();
                            }
                            this.readSample(parsableByteArray);
                        }
                        else {
                            int n;
                            if (this.hasCrc) {
                                n = 7;
                            }
                            else {
                                n = 5;
                            }
                            if (!this.continueRead(parsableByteArray, this.adtsScratch.data, n)) {
                                continue;
                            }
                            this.parseAdtsHeader();
                        }
                    }
                    else {
                        if (!this.continueRead(parsableByteArray, this.id3HeaderBuffer.data, 10)) {
                            continue;
                        }
                        this.parseId3Header();
                    }
                }
                else {
                    this.checkAdtsHeader(parsableByteArray);
                }
            }
            else {
                this.findNextSample(parsableByteArray);
            }
        }
    }
    
    @Override
    public void createTracks(final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 1);
        if (this.exposeId3) {
            trackIdGenerator.generateNewId();
            (this.id3Output = extractorOutput.track(trackIdGenerator.getTrackId(), 4)).format(Format.createSampleFormat(trackIdGenerator.getFormatId(), "application/id3", null, -1, null));
        }
        else {
            this.id3Output = new DummyTrackOutput();
        }
    }
    
    public long getSampleDurationUs() {
        return this.sampleDurationUs;
    }
    
    @Override
    public void packetFinished() {
    }
    
    @Override
    public void packetStarted(final long timeUs, final int n) {
        this.timeUs = timeUs;
    }
    
    @Override
    public void seek() {
        this.resetSync();
    }
}
