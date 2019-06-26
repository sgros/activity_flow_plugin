// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.Collections;
import java.util.Arrays;
import com.google.android.exoplayer2.Format;
import android.util.Pair;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.TrackOutput;

public final class H262Reader implements ElementaryStreamReader
{
    private static final double[] FRAME_RATE_VALUES;
    private final CsdBuffer csdBuffer;
    private String formatId;
    private long frameDurationUs;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private final boolean[] prefixFlags;
    private boolean sampleHasPicture;
    private boolean sampleIsKeyframe;
    private long samplePosition;
    private long sampleTimeUs;
    private boolean startedFirstSample;
    private long totalBytesWritten;
    private final NalUnitTargetBuffer userData;
    private final ParsableByteArray userDataParsable;
    private final UserDataReader userDataReader;
    
    static {
        FRAME_RATE_VALUES = new double[] { 23.976023976023978, 24.0, 25.0, 29.97002997002997, 30.0, 50.0, 59.94005994005994, 60.0 };
    }
    
    public H262Reader() {
        this(null);
    }
    
    public H262Reader(final UserDataReader userDataReader) {
        this.userDataReader = userDataReader;
        this.prefixFlags = new boolean[4];
        this.csdBuffer = new CsdBuffer(128);
        if (userDataReader != null) {
            this.userData = new NalUnitTargetBuffer(178, 128);
            this.userDataParsable = new ParsableByteArray();
        }
        else {
            this.userData = null;
            this.userDataParsable = null;
        }
    }
    
    private static Pair<Format, Long> parseCsdBuffer(final CsdBuffer csdBuffer, final String s) {
        final byte[] copy = Arrays.copyOf(csdBuffer.data, csdBuffer.length);
        final byte b = copy[4];
        final int n = copy[5] & 0xFF;
        final byte b2 = copy[6];
        final int n2 = (b & 0xFF) << 4 | n >> 4;
        final int n3 = (n & 0xF) << 8 | (b2 & 0xFF);
        final int n4 = (copy[7] & 0xF0) >> 4;
        float n5 = 0.0f;
        Label_0151: {
            float n6;
            int n7;
            if (n4 != 2) {
                if (n4 != 3) {
                    if (n4 != 4) {
                        n5 = 1.0f;
                        break Label_0151;
                    }
                    n6 = (float)(n3 * 121);
                    n7 = n2 * 100;
                }
                else {
                    n6 = (float)(n3 * 16);
                    n7 = n2 * 9;
                }
            }
            else {
                n6 = (float)(n3 * 4);
                n7 = n2 * 3;
            }
            n5 = n6 / n7;
        }
        final Format videoSampleFormat = Format.createVideoSampleFormat(s, "video/mpeg2", null, -1, -1, n2, n3, -1.0f, Collections.singletonList(copy), -1, n5, null);
        final long n8 = 0L;
        final int n9 = (copy[7] & 0xF) - 1;
        long l = n8;
        if (n9 >= 0) {
            final double[] frame_RATE_VALUES = H262Reader.FRAME_RATE_VALUES;
            l = n8;
            if (n9 < frame_RATE_VALUES.length) {
                final double n10 = frame_RATE_VALUES[n9];
                final int n11 = csdBuffer.sequenceExtensionPosition + 9;
                final int n12 = (copy[n11] & 0x60) >> 5;
                final int n13 = copy[n11] & 0x1F;
                double n14 = n10;
                if (n12 != n13) {
                    final double v = n12;
                    Double.isNaN(v);
                    final double v2 = n13 + 1;
                    Double.isNaN(v2);
                    n14 = n10 * ((v + 1.0) / v2);
                }
                l = (long)(1000000.0 / n14);
            }
        }
        return (Pair<Format, Long>)Pair.create((Object)videoSampleFormat, (Object)l);
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray) {
        int position = parsableByteArray.getPosition();
        final int limit = parsableByteArray.limit();
        final byte[] data = parsableByteArray.data;
        this.totalBytesWritten += parsableByteArray.bytesLeft();
        this.output.sampleData(parsableByteArray, parsableByteArray.bytesLeft());
        while (true) {
            final int nalUnit = NalUnitUtil.findNalUnit(data, position, limit, this.prefixFlags);
            if (nalUnit == limit) {
                break;
            }
            final byte[] data2 = parsableByteArray.data;
            final int n = nalUnit + 3;
            final int n2 = data2[n] & 0xFF;
            final int n3 = nalUnit - position;
            final boolean hasOutputFormat = this.hasOutputFormat;
            boolean sampleHasPicture = false;
            if (!hasOutputFormat) {
                if (n3 > 0) {
                    this.csdBuffer.onData(data, position, nalUnit);
                }
                int n4;
                if (n3 < 0) {
                    n4 = -n3;
                }
                else {
                    n4 = 0;
                }
                if (this.csdBuffer.onStartCode(n2, n4)) {
                    final Pair<Format, Long> csdBuffer = parseCsdBuffer(this.csdBuffer, this.formatId);
                    this.output.format((Format)csdBuffer.first);
                    this.frameDurationUs = (long)csdBuffer.second;
                    this.hasOutputFormat = true;
                }
            }
            if (this.userDataReader != null) {
                int n5;
                if (n3 > 0) {
                    this.userData.appendToNalUnit(data, position, nalUnit);
                    n5 = 0;
                }
                else {
                    n5 = -n3;
                }
                if (this.userData.endNalUnit(n5)) {
                    final NalUnitTargetBuffer userData = this.userData;
                    this.userDataParsable.reset(this.userData.nalData, NalUnitUtil.unescapeStream(userData.nalData, userData.nalLength));
                    this.userDataReader.consume(this.sampleTimeUs, this.userDataParsable);
                }
                if (n2 == 178 && parsableByteArray.data[nalUnit + 2] == 1) {
                    this.userData.startNalUnit(n2);
                }
            }
            if (n2 != 0 && n2 != 179) {
                if (n2 == 184) {
                    this.sampleIsKeyframe = true;
                }
            }
            else {
                final int n6 = limit - nalUnit;
                if (this.startedFirstSample && this.sampleHasPicture && this.hasOutputFormat) {
                    this.output.sampleMetadata(this.sampleTimeUs, this.sampleIsKeyframe ? 1 : 0, (int)(this.totalBytesWritten - this.samplePosition) - n6, n6, null);
                }
                if (!this.startedFirstSample || this.sampleHasPicture) {
                    this.samplePosition = this.totalBytesWritten - n6;
                    long pesTimeUs = this.pesTimeUs;
                    if (pesTimeUs == -9223372036854775807L) {
                        if (this.startedFirstSample) {
                            pesTimeUs = this.sampleTimeUs + this.frameDurationUs;
                        }
                        else {
                            pesTimeUs = 0L;
                        }
                    }
                    this.sampleTimeUs = pesTimeUs;
                    this.sampleIsKeyframe = false;
                    this.pesTimeUs = -9223372036854775807L;
                    this.startedFirstSample = true;
                }
                if (n2 == 0) {
                    sampleHasPicture = true;
                }
                this.sampleHasPicture = sampleHasPicture;
            }
            position = n;
        }
        if (!this.hasOutputFormat) {
            this.csdBuffer.onData(data, position, limit);
        }
        if (this.userDataReader != null) {
            this.userData.appendToNalUnit(data, position, limit);
        }
    }
    
    @Override
    public void createTracks(final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 2);
        final UserDataReader userDataReader = this.userDataReader;
        if (userDataReader != null) {
            userDataReader.createTracks(extractorOutput, trackIdGenerator);
        }
    }
    
    @Override
    public void packetFinished() {
    }
    
    @Override
    public void packetStarted(final long pesTimeUs, final int n) {
        this.pesTimeUs = pesTimeUs;
    }
    
    @Override
    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.csdBuffer.reset();
        if (this.userDataReader != null) {
            this.userData.reset();
        }
        this.totalBytesWritten = 0L;
        this.startedFirstSample = false;
    }
    
    private static final class CsdBuffer
    {
        private static final byte[] START_CODE;
        public byte[] data;
        private boolean isFilling;
        public int length;
        public int sequenceExtensionPosition;
        
        static {
            START_CODE = new byte[] { 0, 0, 1 };
        }
        
        public CsdBuffer(final int n) {
            this.data = new byte[n];
        }
        
        public void onData(final byte[] array, final int n, int length) {
            if (!this.isFilling) {
                return;
            }
            final int n2 = length - n;
            final byte[] data = this.data;
            final int length2 = data.length;
            length = this.length;
            if (length2 < length + n2) {
                this.data = Arrays.copyOf(data, (length + n2) * 2);
            }
            System.arraycopy(array, n, this.data, this.length, n2);
            this.length += n2;
        }
        
        public boolean onStartCode(final int n, final int n2) {
            if (this.isFilling) {
                this.length -= n2;
                if (this.sequenceExtensionPosition != 0 || n != 181) {
                    this.isFilling = false;
                    return true;
                }
                this.sequenceExtensionPosition = this.length;
            }
            else if (n == 179) {
                this.isFilling = true;
            }
            final byte[] start_CODE = CsdBuffer.START_CODE;
            this.onData(start_CODE, 0, start_CODE.length);
            return false;
        }
        
        public void reset() {
            this.isFilling = false;
            this.length = 0;
            this.sequenceExtensionPosition = 0;
        }
    }
}
