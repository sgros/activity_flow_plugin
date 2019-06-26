// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.Collections;
import com.google.android.exoplayer2.util.ParsableNalUnitBitArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.TrackOutput;

public final class H265Reader implements ElementaryStreamReader
{
    private String formatId;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private final NalUnitTargetBuffer pps;
    private final boolean[] prefixFlags;
    private final NalUnitTargetBuffer prefixSei;
    private SampleReader sampleReader;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper;
    private final NalUnitTargetBuffer sps;
    private final NalUnitTargetBuffer suffixSei;
    private long totalBytesWritten;
    private final NalUnitTargetBuffer vps;
    
    public H265Reader(final SeiReader seiReader) {
        this.seiReader = seiReader;
        this.prefixFlags = new boolean[3];
        this.vps = new NalUnitTargetBuffer(32, 128);
        this.sps = new NalUnitTargetBuffer(33, 128);
        this.pps = new NalUnitTargetBuffer(34, 128);
        this.prefixSei = new NalUnitTargetBuffer(39, 128);
        this.suffixSei = new NalUnitTargetBuffer(40, 128);
        this.seiWrapper = new ParsableByteArray();
    }
    
    private void endNalUnit(final long n, int n2, final int n3, final long n4) {
        if (this.hasOutputFormat) {
            this.sampleReader.endNalUnit(n, n2);
        }
        else {
            this.vps.endNalUnit(n3);
            this.sps.endNalUnit(n3);
            this.pps.endNalUnit(n3);
            if (this.vps.isCompleted() && this.sps.isCompleted() && this.pps.isCompleted()) {
                this.output.format(parseMediaFormat(this.formatId, this.vps, this.sps, this.pps));
                this.hasOutputFormat = true;
            }
        }
        if (this.prefixSei.endNalUnit(n3)) {
            final NalUnitTargetBuffer prefixSei = this.prefixSei;
            n2 = NalUnitUtil.unescapeStream(prefixSei.nalData, prefixSei.nalLength);
            this.seiWrapper.reset(this.prefixSei.nalData, n2);
            this.seiWrapper.skipBytes(5);
            this.seiReader.consume(n4, this.seiWrapper);
        }
        if (this.suffixSei.endNalUnit(n3)) {
            final NalUnitTargetBuffer suffixSei = this.suffixSei;
            n2 = NalUnitUtil.unescapeStream(suffixSei.nalData, suffixSei.nalLength);
            this.seiWrapper.reset(this.suffixSei.nalData, n2);
            this.seiWrapper.skipBytes(5);
            this.seiReader.consume(n4, this.seiWrapper);
        }
    }
    
    private void nalUnitData(final byte[] array, final int n, final int n2) {
        if (this.hasOutputFormat) {
            this.sampleReader.readNalUnitData(array, n, n2);
        }
        else {
            this.vps.appendToNalUnit(array, n, n2);
            this.sps.appendToNalUnit(array, n, n2);
            this.pps.appendToNalUnit(array, n, n2);
        }
        this.prefixSei.appendToNalUnit(array, n, n2);
        this.suffixSei.appendToNalUnit(array, n, n2);
    }
    
    private static Format parseMediaFormat(final String s, final NalUnitTargetBuffer nalUnitTargetBuffer, final NalUnitTargetBuffer nalUnitTargetBuffer2, final NalUnitTargetBuffer nalUnitTargetBuffer3) {
        final int nalLength = nalUnitTargetBuffer.nalLength;
        final byte[] o = new byte[nalUnitTargetBuffer2.nalLength + nalLength + nalUnitTargetBuffer3.nalLength];
        final byte[] nalData = nalUnitTargetBuffer.nalData;
        final int n = 0;
        System.arraycopy(nalData, 0, o, 0, nalLength);
        System.arraycopy(nalUnitTargetBuffer2.nalData, 0, o, nalUnitTargetBuffer.nalLength, nalUnitTargetBuffer2.nalLength);
        System.arraycopy(nalUnitTargetBuffer3.nalData, 0, o, nalUnitTargetBuffer.nalLength + nalUnitTargetBuffer2.nalLength, nalUnitTargetBuffer3.nalLength);
        final ParsableNalUnitBitArray parsableNalUnitBitArray = new ParsableNalUnitBitArray(nalUnitTargetBuffer2.nalData, 0, nalUnitTargetBuffer2.nalLength);
        parsableNalUnitBitArray.skipBits(44);
        final int bits = parsableNalUnitBitArray.readBits(3);
        parsableNalUnitBitArray.skipBit();
        parsableNalUnitBitArray.skipBits(88);
        parsableNalUnitBitArray.skipBits(8);
        int i = 0;
        int n2 = 0;
        while (i < bits) {
            int n3 = n2;
            if (parsableNalUnitBitArray.readBit()) {
                n3 = n2 + 89;
            }
            n2 = n3;
            if (parsableNalUnitBitArray.readBit()) {
                n2 = n3 + 8;
            }
            ++i;
        }
        parsableNalUnitBitArray.skipBits(n2);
        if (bits > 0) {
            parsableNalUnitBitArray.skipBits((8 - bits) * 2);
        }
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        final int unsignedExpGolombCodedInt = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        if (unsignedExpGolombCodedInt == 3) {
            parsableNalUnitBitArray.skipBit();
        }
        final int unsignedExpGolombCodedInt2 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        final int unsignedExpGolombCodedInt3 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        int n4 = unsignedExpGolombCodedInt2;
        int n5 = unsignedExpGolombCodedInt3;
        if (parsableNalUnitBitArray.readBit()) {
            final int unsignedExpGolombCodedInt4 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt5 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt6 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt7 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            int n6;
            if (unsignedExpGolombCodedInt != 1 && unsignedExpGolombCodedInt != 2) {
                n6 = 1;
            }
            else {
                n6 = 2;
            }
            int n7;
            if (unsignedExpGolombCodedInt == 1) {
                n7 = 2;
            }
            else {
                n7 = 1;
            }
            n4 = unsignedExpGolombCodedInt2 - n6 * (unsignedExpGolombCodedInt4 + unsignedExpGolombCodedInt5);
            n5 = unsignedExpGolombCodedInt3 - n7 * (unsignedExpGolombCodedInt6 + unsignedExpGolombCodedInt7);
        }
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        final int unsignedExpGolombCodedInt8 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        int j;
        if (parsableNalUnitBitArray.readBit()) {
            j = 0;
        }
        else {
            j = bits;
        }
        while (j <= bits) {
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            ++j;
        }
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        if (parsableNalUnitBitArray.readBit() && parsableNalUnitBitArray.readBit()) {
            skipScalingList(parsableNalUnitBitArray);
        }
        parsableNalUnitBitArray.skipBits(2);
        if (parsableNalUnitBitArray.readBit()) {
            parsableNalUnitBitArray.skipBits(8);
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            parsableNalUnitBitArray.skipBit();
        }
        skipShortTermRefPicSets(parsableNalUnitBitArray);
        if (parsableNalUnitBitArray.readBit()) {
            for (int k = n; k < parsableNalUnitBitArray.readUnsignedExpGolombCodedInt(); ++k) {
                parsableNalUnitBitArray.skipBits(unsignedExpGolombCodedInt8 + 4 + 1);
            }
        }
        parsableNalUnitBitArray.skipBits(2);
        final float n8 = 1.0f;
        if (parsableNalUnitBitArray.readBit() && parsableNalUnitBitArray.readBit()) {
            final int bits2 = parsableNalUnitBitArray.readBits(8);
            if (bits2 == 255) {
                final int bits3 = parsableNalUnitBitArray.readBits(16);
                final int bits4 = parsableNalUnitBitArray.readBits(16);
                float n9 = n8;
                if (bits3 != 0) {
                    n9 = n8;
                    if (bits4 != 0) {
                        n9 = bits3 / (float)bits4;
                    }
                }
                return Format.createVideoSampleFormat(s, "video/hevc", null, -1, -1, n4, n5, -1.0f, Collections.singletonList(o), -1, n9, null);
            }
            final float[] aspect_RATIO_IDC_VALUES = NalUnitUtil.ASPECT_RATIO_IDC_VALUES;
            if (bits2 < aspect_RATIO_IDC_VALUES.length) {
                final float n9 = aspect_RATIO_IDC_VALUES[bits2];
                return Format.createVideoSampleFormat(s, "video/hevc", null, -1, -1, n4, n5, -1.0f, Collections.singletonList(o), -1, n9, null);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected aspect_ratio_idc value: ");
            sb.append(bits2);
            Log.w("H265Reader", sb.toString());
        }
        float n9 = 1.0f;
        return Format.createVideoSampleFormat(s, "video/hevc", null, -1, -1, n4, n5, -1.0f, Collections.singletonList(o), -1, n9, null);
    }
    
    private static void skipScalingList(final ParsableNalUnitBitArray parsableNalUnitBitArray) {
        for (int i = 0; i < 4; ++i) {
            int n;
            for (int j = 0; j < 6; j += n) {
                if (!parsableNalUnitBitArray.readBit()) {
                    parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                }
                else {
                    final int min = Math.min(64, 1 << (i << 1) + 4);
                    if (i > 1) {
                        parsableNalUnitBitArray.readSignedExpGolombCodedInt();
                    }
                    for (int k = 0; k < min; ++k) {
                        parsableNalUnitBitArray.readSignedExpGolombCodedInt();
                    }
                }
                n = 3;
                if (i != 3) {
                    n = 1;
                }
            }
        }
    }
    
    private static void skipShortTermRefPicSets(final ParsableNalUnitBitArray parsableNalUnitBitArray) {
        final int unsignedExpGolombCodedInt = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        int i = 0;
        boolean bit = false;
        int n = 0;
        while (i < unsignedExpGolombCodedInt) {
            if (i != 0) {
                bit = parsableNalUnitBitArray.readBit();
            }
            int n3;
            if (bit) {
                parsableNalUnitBitArray.skipBit();
                parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                int n2 = 0;
                while (true) {
                    n3 = n;
                    if (n2 > n) {
                        break;
                    }
                    if (parsableNalUnitBitArray.readBit()) {
                        parsableNalUnitBitArray.skipBit();
                    }
                    ++n2;
                }
            }
            else {
                final int unsignedExpGolombCodedInt2 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                final int unsignedExpGolombCodedInt3 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                for (int j = 0; j < unsignedExpGolombCodedInt2; ++j) {
                    parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                    parsableNalUnitBitArray.skipBit();
                }
                for (int k = 0; k < unsignedExpGolombCodedInt3; ++k) {
                    parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                    parsableNalUnitBitArray.skipBit();
                }
                n3 = unsignedExpGolombCodedInt2 + unsignedExpGolombCodedInt3;
            }
            ++i;
            n = n3;
        }
    }
    
    private void startNalUnit(final long n, final int n2, final int n3, final long n4) {
        if (this.hasOutputFormat) {
            this.sampleReader.startNalUnit(n, n2, n3, n4);
        }
        else {
            this.vps.startNalUnit(n3);
            this.sps.startNalUnit(n3);
            this.pps.startNalUnit(n3);
        }
        this.prefixSei.startNalUnit(n3);
        this.suffixSei.startNalUnit(n3);
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray) {
        while (parsableByteArray.bytesLeft() > 0) {
            int i = parsableByteArray.getPosition();
            final int limit = parsableByteArray.limit();
            final byte[] data = parsableByteArray.data;
            this.totalBytesWritten += parsableByteArray.bytesLeft();
            this.output.sampleData(parsableByteArray, parsableByteArray.bytesLeft());
            while (i < limit) {
                final int nalUnit = NalUnitUtil.findNalUnit(data, i, limit, this.prefixFlags);
                if (nalUnit == limit) {
                    this.nalUnitData(data, i, limit);
                    return;
                }
                final int h265NalUnitType = NalUnitUtil.getH265NalUnitType(data, nalUnit);
                final int n = nalUnit - i;
                if (n > 0) {
                    this.nalUnitData(data, i, nalUnit);
                }
                final int n2 = limit - nalUnit;
                final long n3 = this.totalBytesWritten - n2;
                int n4;
                if (n < 0) {
                    n4 = -n;
                }
                else {
                    n4 = 0;
                }
                this.endNalUnit(n3, n2, n4, this.pesTimeUs);
                this.startNalUnit(n3, n2, h265NalUnitType, this.pesTimeUs);
                i = nalUnit + 3;
            }
        }
    }
    
    @Override
    public void createTracks(final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 2);
        this.sampleReader = new SampleReader(this.output);
        this.seiReader.createTracks(extractorOutput, trackIdGenerator);
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
        this.vps.reset();
        this.sps.reset();
        this.pps.reset();
        this.prefixSei.reset();
        this.suffixSei.reset();
        this.sampleReader.reset();
        this.totalBytesWritten = 0L;
    }
    
    private static final class SampleReader
    {
        private boolean isFirstParameterSet;
        private boolean isFirstSlice;
        private boolean lookingForFirstSliceFlag;
        private int nalUnitBytesRead;
        private boolean nalUnitHasKeyframeData;
        private long nalUnitStartPosition;
        private long nalUnitTimeUs;
        private final TrackOutput output;
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        private boolean writingParameterSets;
        
        public SampleReader(final TrackOutput output) {
            this.output = output;
        }
        
        private void outputSample(final int n) {
            this.output.sampleMetadata(this.sampleTimeUs, this.sampleIsKeyframe ? 1 : 0, (int)(this.nalUnitStartPosition - this.samplePosition), n, null);
        }
        
        public void endNalUnit(final long n, final int n2) {
            if (this.writingParameterSets && this.isFirstSlice) {
                this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
                this.writingParameterSets = false;
            }
            else if (this.isFirstParameterSet || this.isFirstSlice) {
                if (this.readingSample) {
                    this.outputSample(n2 + (int)(n - this.nalUnitStartPosition));
                }
                this.samplePosition = this.nalUnitStartPosition;
                this.sampleTimeUs = this.nalUnitTimeUs;
                this.readingSample = true;
                this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
            }
        }
        
        public void readNalUnitData(final byte[] array, final int n, final int n2) {
            if (this.lookingForFirstSliceFlag) {
                final int nalUnitBytesRead = this.nalUnitBytesRead;
                final int n3 = n + 2 - nalUnitBytesRead;
                if (n3 < n2) {
                    this.isFirstSlice = ((array[n3] & 0x80) != 0x0);
                    this.lookingForFirstSliceFlag = false;
                }
                else {
                    this.nalUnitBytesRead = nalUnitBytesRead + (n2 - n);
                }
            }
        }
        
        public void reset() {
            this.lookingForFirstSliceFlag = false;
            this.isFirstSlice = false;
            this.isFirstParameterSet = false;
            this.readingSample = false;
            this.writingParameterSets = false;
        }
        
        public void startNalUnit(final long nalUnitStartPosition, final int n, final int n2, final long nalUnitTimeUs) {
            this.isFirstSlice = false;
            this.isFirstParameterSet = false;
            this.nalUnitTimeUs = nalUnitTimeUs;
            this.nalUnitBytesRead = 0;
            this.nalUnitStartPosition = nalUnitStartPosition;
            final boolean b = true;
            if (n2 >= 32) {
                if (!this.writingParameterSets && this.readingSample) {
                    this.outputSample(n);
                    this.readingSample = false;
                }
                if (n2 <= 34) {
                    this.isFirstParameterSet = (this.writingParameterSets ^ true);
                    this.writingParameterSets = true;
                }
            }
            this.nalUnitHasKeyframeData = (n2 >= 16 && n2 <= 21);
            boolean lookingForFirstSliceFlag = b;
            if (!this.nalUnitHasKeyframeData) {
                lookingForFirstSliceFlag = (n2 <= 9 && b);
            }
            this.lookingForFirstSliceFlag = lookingForFirstSliceFlag;
        }
    }
}
