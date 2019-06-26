// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.ParsableBitArray;

public final class PesReader implements TsPayloadReader
{
    private int bytesRead;
    private boolean dataAlignmentIndicator;
    private boolean dtsFlag;
    private int extendedHeaderLength;
    private int payloadSize;
    private final ParsableBitArray pesScratch;
    private boolean ptsFlag;
    private final ElementaryStreamReader reader;
    private boolean seenFirstDts;
    private int state;
    private long timeUs;
    private TimestampAdjuster timestampAdjuster;
    
    public PesReader(final ElementaryStreamReader reader) {
        this.reader = reader;
        this.pesScratch = new ParsableBitArray(new byte[10]);
        this.state = 0;
    }
    
    private boolean continueRead(final ParsableByteArray parsableByteArray, final byte[] array, final int n) {
        final int min = Math.min(parsableByteArray.bytesLeft(), n - this.bytesRead);
        boolean b = true;
        if (min <= 0) {
            return true;
        }
        if (array == null) {
            parsableByteArray.skipBytes(min);
        }
        else {
            parsableByteArray.readBytes(array, this.bytesRead, min);
        }
        this.bytesRead += min;
        if (this.bytesRead != n) {
            b = false;
        }
        return b;
    }
    
    private boolean parseHeader() {
        this.pesScratch.setPosition(0);
        final int bits = this.pesScratch.readBits(24);
        if (bits != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected start code prefix: ");
            sb.append(bits);
            Log.w("PesReader", sb.toString());
            this.payloadSize = -1;
            return false;
        }
        this.pesScratch.skipBits(8);
        final int bits2 = this.pesScratch.readBits(16);
        this.pesScratch.skipBits(5);
        this.dataAlignmentIndicator = this.pesScratch.readBit();
        this.pesScratch.skipBits(2);
        this.ptsFlag = this.pesScratch.readBit();
        this.dtsFlag = this.pesScratch.readBit();
        this.pesScratch.skipBits(6);
        this.extendedHeaderLength = this.pesScratch.readBits(8);
        if (bits2 == 0) {
            this.payloadSize = -1;
        }
        else {
            this.payloadSize = bits2 + 6 - 9 - this.extendedHeaderLength;
        }
        return true;
    }
    
    private void parseHeaderExtension() {
        this.pesScratch.setPosition(0);
        this.timeUs = -9223372036854775807L;
        if (this.ptsFlag) {
            this.pesScratch.skipBits(4);
            final long n = this.pesScratch.readBits(3);
            this.pesScratch.skipBits(1);
            final long n2 = this.pesScratch.readBits(15) << 15;
            this.pesScratch.skipBits(1);
            final long n3 = this.pesScratch.readBits(15);
            this.pesScratch.skipBits(1);
            if (!this.seenFirstDts && this.dtsFlag) {
                this.pesScratch.skipBits(4);
                final long n4 = this.pesScratch.readBits(3);
                this.pesScratch.skipBits(1);
                final long n5 = this.pesScratch.readBits(15) << 15;
                this.pesScratch.skipBits(1);
                final long n6 = this.pesScratch.readBits(15);
                this.pesScratch.skipBits(1);
                this.timestampAdjuster.adjustTsTimestamp(n4 << 30 | n5 | n6);
                this.seenFirstDts = true;
            }
            this.timeUs = this.timestampAdjuster.adjustTsTimestamp(n << 30 | n2 | n3);
        }
    }
    
    private void setState(final int state) {
        this.state = state;
        this.bytesRead = 0;
    }
    
    @Override
    public final void consume(final ParsableByteArray parsableByteArray, int payloadSize) throws ParserException {
        int n = payloadSize;
        if ((payloadSize & 0x1) != 0x0) {
            final int state = this.state;
            if (state != 0 && state != 1) {
                if (state != 2) {
                    if (state != 3) {
                        throw new IllegalStateException();
                    }
                    if (this.payloadSize != -1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unexpected start indicator: expected ");
                        sb.append(this.payloadSize);
                        sb.append(" more bytes");
                        Log.w("PesReader", sb.toString());
                    }
                    this.reader.packetFinished();
                }
                else {
                    Log.w("PesReader", "Unexpected start indicator reading extended header");
                }
            }
            this.setState(1);
            n = payloadSize;
        }
        while (parsableByteArray.bytesLeft() > 0) {
            final int state2 = this.state;
            if (state2 != 0) {
                payloadSize = 0;
                final int n2 = 0;
                int n3 = 0;
                if (state2 != 1) {
                    if (state2 != 2) {
                        if (state2 != 3) {
                            throw new IllegalStateException();
                        }
                        final int bytesLeft = parsableByteArray.bytesLeft();
                        payloadSize = this.payloadSize;
                        if (payloadSize != -1) {
                            n3 = bytesLeft - payloadSize;
                        }
                        payloadSize = bytesLeft;
                        if (n3 > 0) {
                            payloadSize = bytesLeft - n3;
                            parsableByteArray.setLimit(parsableByteArray.getPosition() + payloadSize);
                        }
                        this.reader.consume(parsableByteArray);
                        final int payloadSize2 = this.payloadSize;
                        if (payloadSize2 == -1) {
                            continue;
                        }
                        this.payloadSize = payloadSize2 - payloadSize;
                        if (this.payloadSize != 0) {
                            continue;
                        }
                        this.reader.packetFinished();
                        this.setState(1);
                    }
                    else {
                        if (!this.continueRead(parsableByteArray, this.pesScratch.data, Math.min(10, this.extendedHeaderLength)) || !this.continueRead(parsableByteArray, null, this.extendedHeaderLength)) {
                            continue;
                        }
                        this.parseHeaderExtension();
                        if (this.dataAlignmentIndicator) {
                            payloadSize = 4;
                        }
                        n |= payloadSize;
                        this.reader.packetStarted(this.timeUs, n);
                        this.setState(3);
                    }
                }
                else {
                    if (!this.continueRead(parsableByteArray, this.pesScratch.data, 9)) {
                        continue;
                    }
                    payloadSize = n2;
                    if (this.parseHeader()) {
                        payloadSize = 2;
                    }
                    this.setState(payloadSize);
                }
            }
            else {
                parsableByteArray.skipBytes(parsableByteArray.bytesLeft());
            }
        }
    }
    
    @Override
    public void init(final TimestampAdjuster timestampAdjuster, final ExtractorOutput extractorOutput, final TrackIdGenerator trackIdGenerator) {
        this.timestampAdjuster = timestampAdjuster;
        this.reader.createTracks(extractorOutput, trackIdGenerator);
    }
    
    @Override
    public final void seek() {
        this.state = 0;
        this.bytesRead = 0;
        this.seenFirstDts = false;
        this.reader.seek();
    }
}
