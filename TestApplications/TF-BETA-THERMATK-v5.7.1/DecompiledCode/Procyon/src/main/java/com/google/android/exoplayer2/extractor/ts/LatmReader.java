// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.Collections;
import android.util.Pair;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.Format;

public final class LatmReader implements ElementaryStreamReader
{
    private int audioMuxVersionA;
    private int bytesRead;
    private int channelCount;
    private Format format;
    private String formatId;
    private int frameLengthType;
    private final String language;
    private int numSubframes;
    private long otherDataLenBits;
    private boolean otherDataPresent;
    private TrackOutput output;
    private final ParsableBitArray sampleBitArray;
    private final ParsableByteArray sampleDataBuffer;
    private long sampleDurationUs;
    private int sampleRateHz;
    private int sampleSize;
    private int secondHeaderByte;
    private int state;
    private boolean streamMuxRead;
    private long timeUs;
    
    public LatmReader(final String language) {
        this.language = language;
        this.sampleDataBuffer = new ParsableByteArray(1024);
        this.sampleBitArray = new ParsableBitArray(this.sampleDataBuffer.data);
    }
    
    private static long latmGetValue(final ParsableBitArray parsableBitArray) {
        return parsableBitArray.readBits((parsableBitArray.readBits(2) + 1) * 8);
    }
    
    private void parseAudioMuxElement(final ParsableBitArray parsableBitArray) throws ParserException {
        if (!parsableBitArray.readBit()) {
            this.streamMuxRead = true;
            this.parseStreamMuxConfig(parsableBitArray);
        }
        else if (!this.streamMuxRead) {
            return;
        }
        if (this.audioMuxVersionA != 0) {
            throw new ParserException();
        }
        if (this.numSubframes == 0) {
            this.parsePayloadMux(parsableBitArray, this.parsePayloadLengthInfo(parsableBitArray));
            if (this.otherDataPresent) {
                parsableBitArray.skipBits((int)this.otherDataLenBits);
            }
            return;
        }
        throw new ParserException();
    }
    
    private int parseAudioSpecificConfig(final ParsableBitArray parsableBitArray) throws ParserException {
        final int bitsLeft = parsableBitArray.bitsLeft();
        final Pair<Integer, Integer> aacAudioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(parsableBitArray, true);
        this.sampleRateHz = (int)aacAudioSpecificConfig.first;
        this.channelCount = (int)aacAudioSpecificConfig.second;
        return bitsLeft - parsableBitArray.bitsLeft();
    }
    
    private void parseFrameLength(final ParsableBitArray parsableBitArray) {
        this.frameLengthType = parsableBitArray.readBits(3);
        final int frameLengthType = this.frameLengthType;
        if (frameLengthType != 0) {
            if (frameLengthType != 1) {
                if (frameLengthType != 3 && frameLengthType != 4 && frameLengthType != 5) {
                    if (frameLengthType != 6 && frameLengthType != 7) {
                        throw new IllegalStateException();
                    }
                    parsableBitArray.skipBits(1);
                }
                else {
                    parsableBitArray.skipBits(6);
                }
            }
            else {
                parsableBitArray.skipBits(9);
            }
        }
        else {
            parsableBitArray.skipBits(8);
        }
    }
    
    private int parsePayloadLengthInfo(final ParsableBitArray parsableBitArray) throws ParserException {
        if (this.frameLengthType == 0) {
            int n = 0;
            int i;
            do {
                i = parsableBitArray.readBits(8);
                n += i;
            } while (i == 255);
            return n;
        }
        throw new ParserException();
    }
    
    private void parsePayloadMux(final ParsableBitArray parsableBitArray, final int n) {
        final int position = parsableBitArray.getPosition();
        if ((position & 0x7) == 0x0) {
            this.sampleDataBuffer.setPosition(position >> 3);
        }
        else {
            parsableBitArray.readBits(this.sampleDataBuffer.data, 0, n * 8);
            this.sampleDataBuffer.setPosition(0);
        }
        this.output.sampleData(this.sampleDataBuffer, n);
        this.output.sampleMetadata(this.timeUs, 1, n, 0, null);
        this.timeUs += this.sampleDurationUs;
    }
    
    private void parseStreamMuxConfig(final ParsableBitArray parsableBitArray) throws ParserException {
        final int bits = parsableBitArray.readBits(1);
        int bits2;
        if (bits == 1) {
            bits2 = parsableBitArray.readBits(1);
        }
        else {
            bits2 = 0;
        }
        this.audioMuxVersionA = bits2;
        if (this.audioMuxVersionA != 0) {
            throw new ParserException();
        }
        if (bits == 1) {
            latmGetValue(parsableBitArray);
        }
        if (!parsableBitArray.readBit()) {
            throw new ParserException();
        }
        this.numSubframes = parsableBitArray.readBits(6);
        final int bits3 = parsableBitArray.readBits(4);
        final int bits4 = parsableBitArray.readBits(3);
        if (bits3 == 0 && bits4 == 0) {
            if (bits == 0) {
                final int position = parsableBitArray.getPosition();
                final int audioSpecificConfig = this.parseAudioSpecificConfig(parsableBitArray);
                parsableBitArray.setPosition(position);
                final byte[] o = new byte[(audioSpecificConfig + 7) / 8];
                parsableBitArray.readBits(o, 0, audioSpecificConfig);
                final Format audioSampleFormat = Format.createAudioSampleFormat(this.formatId, "audio/mp4a-latm", null, -1, -1, this.channelCount, this.sampleRateHz, Collections.singletonList(o), null, 0, this.language);
                if (!audioSampleFormat.equals(this.format)) {
                    this.format = audioSampleFormat;
                    this.sampleDurationUs = 1024000000L / audioSampleFormat.sampleRate;
                    this.output.format(audioSampleFormat);
                }
            }
            else {
                parsableBitArray.skipBits((int)latmGetValue(parsableBitArray) - this.parseAudioSpecificConfig(parsableBitArray));
            }
            this.parseFrameLength(parsableBitArray);
            this.otherDataPresent = parsableBitArray.readBit();
            this.otherDataLenBits = 0L;
            if (this.otherDataPresent) {
                if (bits == 1) {
                    this.otherDataLenBits = latmGetValue(parsableBitArray);
                }
                else {
                    boolean bit;
                    do {
                        bit = parsableBitArray.readBit();
                        this.otherDataLenBits = (this.otherDataLenBits << 8) + parsableBitArray.readBits(8);
                    } while (bit);
                }
            }
            if (parsableBitArray.readBit()) {
                parsableBitArray.skipBits(8);
            }
            return;
        }
        throw new ParserException();
    }
    
    private void resetBufferForSize(final int n) {
        this.sampleDataBuffer.reset(n);
        this.sampleBitArray.reset(this.sampleDataBuffer.data);
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray) throws ParserException {
        while (parsableByteArray.bytesLeft() > 0) {
            final int state = this.state;
            if (state != 0) {
                if (state != 1) {
                    if (state != 2) {
                        if (state != 3) {
                            throw new IllegalStateException();
                        }
                        final int min = Math.min(parsableByteArray.bytesLeft(), this.sampleSize - this.bytesRead);
                        parsableByteArray.readBytes(this.sampleBitArray.data, this.bytesRead, min);
                        this.bytesRead += min;
                        if (this.bytesRead != this.sampleSize) {
                            continue;
                        }
                        this.sampleBitArray.setPosition(0);
                        this.parseAudioMuxElement(this.sampleBitArray);
                        this.state = 0;
                    }
                    else {
                        this.sampleSize = ((this.secondHeaderByte & 0xFFFFFF1F) << 8 | parsableByteArray.readUnsignedByte());
                        final int sampleSize = this.sampleSize;
                        if (sampleSize > this.sampleDataBuffer.data.length) {
                            this.resetBufferForSize(sampleSize);
                        }
                        this.bytesRead = 0;
                        this.state = 3;
                    }
                }
                else {
                    final int unsignedByte = parsableByteArray.readUnsignedByte();
                    if ((unsignedByte & 0xE0) == 0xE0) {
                        this.secondHeaderByte = unsignedByte;
                        this.state = 2;
                    }
                    else {
                        if (unsignedByte == 86) {
                            continue;
                        }
                        this.state = 0;
                    }
                }
            }
            else {
                if (parsableByteArray.readUnsignedByte() != 86) {
                    continue;
                }
                this.state = 1;
            }
        }
    }
    
    @Override
    public void createTracks(final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 1);
        this.formatId = trackIdGenerator.getFormatId();
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
        this.state = 0;
        this.streamMuxRead = false;
    }
}
