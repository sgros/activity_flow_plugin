// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import android.util.SparseArray;
import com.google.android.exoplayer2.util.ParsableNalUnitBitArray;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.util.Arrays;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.TrackOutput;

public final class H264Reader implements ElementaryStreamReader
{
    private final boolean allowNonIdrKeyframes;
    private final boolean detectAccessUnits;
    private String formatId;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private final NalUnitTargetBuffer pps;
    private final boolean[] prefixFlags;
    private boolean randomAccessIndicator;
    private SampleReader sampleReader;
    private final NalUnitTargetBuffer sei;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper;
    private final NalUnitTargetBuffer sps;
    private long totalBytesWritten;
    
    public H264Reader(final SeiReader seiReader, final boolean allowNonIdrKeyframes, final boolean detectAccessUnits) {
        this.seiReader = seiReader;
        this.allowNonIdrKeyframes = allowNonIdrKeyframes;
        this.detectAccessUnits = detectAccessUnits;
        this.prefixFlags = new boolean[3];
        this.sps = new NalUnitTargetBuffer(7, 128);
        this.pps = new NalUnitTargetBuffer(8, 128);
        this.sei = new NalUnitTargetBuffer(6, 128);
        this.seiWrapper = new ParsableByteArray();
    }
    
    private void endNalUnit(final long n, final int n2, int unescapeStream, final long n3) {
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.endNalUnit(unescapeStream);
            this.pps.endNalUnit(unescapeStream);
            if (!this.hasOutputFormat) {
                if (this.sps.isCompleted() && this.pps.isCompleted()) {
                    final ArrayList<byte[]> list = new ArrayList<byte[]>();
                    final NalUnitTargetBuffer sps = this.sps;
                    list.add(Arrays.copyOf(sps.nalData, sps.nalLength));
                    final NalUnitTargetBuffer pps = this.pps;
                    list.add(Arrays.copyOf(pps.nalData, pps.nalLength));
                    final NalUnitTargetBuffer sps2 = this.sps;
                    final NalUnitUtil.SpsData spsNalUnit = NalUnitUtil.parseSpsNalUnit(sps2.nalData, 3, sps2.nalLength);
                    final NalUnitTargetBuffer pps2 = this.pps;
                    final NalUnitUtil.PpsData ppsNalUnit = NalUnitUtil.parsePpsNalUnit(pps2.nalData, 3, pps2.nalLength);
                    this.output.format(Format.createVideoSampleFormat(this.formatId, "video/avc", CodecSpecificDataUtil.buildAvcCodecString(spsNalUnit.profileIdc, spsNalUnit.constraintsFlagsAndReservedZero2Bits, spsNalUnit.levelIdc), -1, -1, spsNalUnit.width, spsNalUnit.height, -1.0f, list, -1, spsNalUnit.pixelWidthAspectRatio, null));
                    this.hasOutputFormat = true;
                    this.sampleReader.putSps(spsNalUnit);
                    this.sampleReader.putPps(ppsNalUnit);
                    this.sps.reset();
                    this.pps.reset();
                }
            }
            else if (this.sps.isCompleted()) {
                final NalUnitTargetBuffer sps3 = this.sps;
                this.sampleReader.putSps(NalUnitUtil.parseSpsNalUnit(sps3.nalData, 3, sps3.nalLength));
                this.sps.reset();
            }
            else if (this.pps.isCompleted()) {
                final NalUnitTargetBuffer pps3 = this.pps;
                this.sampleReader.putPps(NalUnitUtil.parsePpsNalUnit(pps3.nalData, 3, pps3.nalLength));
                this.pps.reset();
            }
        }
        if (this.sei.endNalUnit(unescapeStream)) {
            final NalUnitTargetBuffer sei = this.sei;
            unescapeStream = NalUnitUtil.unescapeStream(sei.nalData, sei.nalLength);
            this.seiWrapper.reset(this.sei.nalData, unescapeStream);
            this.seiWrapper.setPosition(4);
            this.seiReader.consume(n3, this.seiWrapper);
        }
        if (this.sampleReader.endNalUnit(n, n2, this.hasOutputFormat, this.randomAccessIndicator)) {
            this.randomAccessIndicator = false;
        }
    }
    
    private void nalUnitData(final byte[] array, final int n, final int n2) {
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.appendToNalUnit(array, n, n2);
            this.pps.appendToNalUnit(array, n, n2);
        }
        this.sei.appendToNalUnit(array, n, n2);
        this.sampleReader.appendToNalUnit(array, n, n2);
    }
    
    private void startNalUnit(final long n, final int n2, final long n3) {
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.startNalUnit(n2);
            this.pps.startNalUnit(n2);
        }
        this.sei.startNalUnit(n2);
        this.sampleReader.startNalUnit(n, n2, n3);
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
            final int nalUnitType = NalUnitUtil.getNalUnitType(data, nalUnit);
            final int n = nalUnit - position;
            if (n > 0) {
                this.nalUnitData(data, position, nalUnit);
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
            this.startNalUnit(n3, nalUnitType, this.pesTimeUs);
            position = nalUnit + 3;
        }
        this.nalUnitData(data, position, limit);
    }
    
    @Override
    public void createTracks(final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 2);
        this.sampleReader = new SampleReader(this.output, this.allowNonIdrKeyframes, this.detectAccessUnits);
        this.seiReader.createTracks(extractorOutput, trackIdGenerator);
    }
    
    @Override
    public void packetFinished() {
    }
    
    @Override
    public void packetStarted(final long pesTimeUs, int n) {
        this.pesTimeUs = pesTimeUs;
        final boolean randomAccessIndicator = this.randomAccessIndicator;
        if ((n & 0x2) != 0x0) {
            n = 1;
        }
        else {
            n = 0;
        }
        this.randomAccessIndicator = (((randomAccessIndicator ? 1 : 0) | n) != 0x0);
    }
    
    @Override
    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.sps.reset();
        this.pps.reset();
        this.sei.reset();
        this.sampleReader.reset();
        this.totalBytesWritten = 0L;
        this.randomAccessIndicator = false;
    }
    
    private static final class SampleReader
    {
        private final boolean allowNonIdrKeyframes;
        private final ParsableNalUnitBitArray bitArray;
        private byte[] buffer;
        private int bufferLength;
        private final boolean detectAccessUnits;
        private boolean isFilling;
        private long nalUnitStartPosition;
        private long nalUnitTimeUs;
        private int nalUnitType;
        private final TrackOutput output;
        private final SparseArray<NalUnitUtil.PpsData> pps;
        private SliceHeaderData previousSliceHeader;
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        private SliceHeaderData sliceHeader;
        private final SparseArray<NalUnitUtil.SpsData> sps;
        
        public SampleReader(final TrackOutput output, final boolean allowNonIdrKeyframes, final boolean detectAccessUnits) {
            this.output = output;
            this.allowNonIdrKeyframes = allowNonIdrKeyframes;
            this.detectAccessUnits = detectAccessUnits;
            this.sps = (SparseArray<NalUnitUtil.SpsData>)new SparseArray();
            this.pps = (SparseArray<NalUnitUtil.PpsData>)new SparseArray();
            this.previousSliceHeader = new SliceHeaderData();
            this.sliceHeader = new SliceHeaderData();
            this.buffer = new byte[128];
            this.bitArray = new ParsableNalUnitBitArray(this.buffer, 0, 0);
            this.reset();
        }
        
        private void outputSample(final int n) {
            this.output.sampleMetadata(this.sampleTimeUs, this.sampleIsKeyframe ? 1 : 0, (int)(this.nalUnitStartPosition - this.samplePosition), n, null);
        }
        
        public void appendToNalUnit(final byte[] array, int n, int n2) {
            if (!this.isFilling) {
                return;
            }
            final int n3 = n2 - n;
            final byte[] buffer = this.buffer;
            final int length = buffer.length;
            n2 = this.bufferLength;
            if (length < n2 + n3) {
                this.buffer = Arrays.copyOf(buffer, (n2 + n3) * 2);
            }
            System.arraycopy(array, n, this.buffer, this.bufferLength, n3);
            this.bufferLength += n3;
            this.bitArray.reset(this.buffer, 0, this.bufferLength);
            if (!this.bitArray.canReadBits(8)) {
                return;
            }
            this.bitArray.skipBit();
            final int bits = this.bitArray.readBits(2);
            this.bitArray.skipBits(5);
            if (!this.bitArray.canReadExpGolombCodedNum()) {
                return;
            }
            this.bitArray.readUnsignedExpGolombCodedInt();
            if (!this.bitArray.canReadExpGolombCodedNum()) {
                return;
            }
            final int unsignedExpGolombCodedInt = this.bitArray.readUnsignedExpGolombCodedInt();
            if (!this.detectAccessUnits) {
                this.isFilling = false;
                this.sliceHeader.setSliceType(unsignedExpGolombCodedInt);
                return;
            }
            if (!this.bitArray.canReadExpGolombCodedNum()) {
                return;
            }
            final int unsignedExpGolombCodedInt2 = this.bitArray.readUnsignedExpGolombCodedInt();
            if (this.pps.indexOfKey(unsignedExpGolombCodedInt2) < 0) {
                this.isFilling = false;
                return;
            }
            final NalUnitUtil.PpsData ppsData = (NalUnitUtil.PpsData)this.pps.get(unsignedExpGolombCodedInt2);
            final NalUnitUtil.SpsData spsData = (NalUnitUtil.SpsData)this.sps.get(ppsData.seqParameterSetId);
            if (spsData.separateColorPlaneFlag) {
                if (!this.bitArray.canReadBits(2)) {
                    return;
                }
                this.bitArray.skipBits(2);
            }
            if (!this.bitArray.canReadBits(spsData.frameNumLength)) {
                return;
            }
            final int bits2 = this.bitArray.readBits(spsData.frameNumLength);
            boolean bit = false;
            boolean bit2 = false;
            boolean b = false;
            Label_0389: {
                if (!spsData.frameMbsOnlyFlag) {
                    if (!this.bitArray.canReadBits(1)) {
                        return;
                    }
                    bit = this.bitArray.readBit();
                    if (bit) {
                        if (!this.bitArray.canReadBits(1)) {
                            return;
                        }
                        bit2 = this.bitArray.readBit();
                        b = true;
                        break Label_0389;
                    }
                }
                else {
                    bit = false;
                }
                b = false;
                bit2 = false;
            }
            final boolean b2 = this.nalUnitType == 5;
            int unsignedExpGolombCodedInt3;
            if (b2) {
                if (!this.bitArray.canReadExpGolombCodedNum()) {
                    return;
                }
                unsignedExpGolombCodedInt3 = this.bitArray.readUnsignedExpGolombCodedInt();
            }
            else {
                unsignedExpGolombCodedInt3 = 0;
            }
            n = spsData.picOrderCountType;
            int signedExpGolombCodedInt = 0;
            int signedExpGolombCodedInt2 = 0;
            Label_0600: {
                Label_0597: {
                    Label_0594: {
                        if (n == 0) {
                            if (!this.bitArray.canReadBits(spsData.picOrderCntLsbLength)) {
                                return;
                            }
                            n = this.bitArray.readBits(spsData.picOrderCntLsbLength);
                            if (ppsData.bottomFieldPicOrderInFramePresentFlag && !bit) {
                                if (!this.bitArray.canReadExpGolombCodedNum()) {
                                    return;
                                }
                                n2 = this.bitArray.readSignedExpGolombCodedInt();
                                break Label_0594;
                            }
                        }
                        else if (n == 1 && !spsData.deltaPicOrderAlwaysZeroFlag) {
                            if (!this.bitArray.canReadExpGolombCodedNum()) {
                                return;
                            }
                            signedExpGolombCodedInt = this.bitArray.readSignedExpGolombCodedInt();
                            if (!ppsData.bottomFieldPicOrderInFramePresentFlag || bit) {
                                n = 0;
                                n2 = 0;
                                break Label_0597;
                            }
                            if (!this.bitArray.canReadExpGolombCodedNum()) {
                                return;
                            }
                            signedExpGolombCodedInt2 = this.bitArray.readSignedExpGolombCodedInt();
                            n = 0;
                            n2 = 0;
                            break Label_0600;
                        }
                        else {
                            n = 0;
                        }
                        n2 = 0;
                    }
                    signedExpGolombCodedInt = 0;
                }
                signedExpGolombCodedInt2 = 0;
            }
            this.sliceHeader.setAll(spsData, bits, unsignedExpGolombCodedInt, bits2, unsignedExpGolombCodedInt2, bit, b, bit2, b2, unsignedExpGolombCodedInt3, n, n2, signedExpGolombCodedInt, signedExpGolombCodedInt2);
            this.isFilling = false;
        }
        
        public boolean endNalUnit(final long n, int n2, final boolean b, boolean iSlice) {
            final int nalUnitType = this.nalUnitType;
            final int n3 = 0;
            if (nalUnitType == 9 || (this.detectAccessUnits && this.sliceHeader.isFirstVclNalUnitOfPicture(this.previousSliceHeader))) {
                if (b && this.readingSample) {
                    this.outputSample(n2 + (int)(n - this.nalUnitStartPosition));
                }
                this.samplePosition = this.nalUnitStartPosition;
                this.sampleTimeUs = this.nalUnitTimeUs;
                this.sampleIsKeyframe = false;
                this.readingSample = true;
            }
            if (this.allowNonIdrKeyframes) {
                iSlice = this.sliceHeader.isISlice();
            }
            final boolean sampleIsKeyframe = this.sampleIsKeyframe;
            final int nalUnitType2 = this.nalUnitType;
            if (nalUnitType2 != 5) {
                n2 = n3;
                if (!iSlice) {
                    return this.sampleIsKeyframe = (((sampleIsKeyframe ? 1 : 0) | n2) != 0x0);
                }
                n2 = n3;
                if (nalUnitType2 != 1) {
                    return this.sampleIsKeyframe = (((sampleIsKeyframe ? 1 : 0) | n2) != 0x0);
                }
            }
            n2 = 1;
            return this.sampleIsKeyframe = (((sampleIsKeyframe ? 1 : 0) | n2) != 0x0);
        }
        
        public boolean needsSpsPps() {
            return this.detectAccessUnits;
        }
        
        public void putPps(final NalUnitUtil.PpsData ppsData) {
            this.pps.append(ppsData.picParameterSetId, (Object)ppsData);
        }
        
        public void putSps(final NalUnitUtil.SpsData spsData) {
            this.sps.append(spsData.seqParameterSetId, (Object)spsData);
        }
        
        public void reset() {
            this.isFilling = false;
            this.readingSample = false;
            this.sliceHeader.clear();
        }
        
        public void startNalUnit(final long nalUnitStartPosition, int nalUnitType, final long nalUnitTimeUs) {
            this.nalUnitType = nalUnitType;
            this.nalUnitTimeUs = nalUnitTimeUs;
            this.nalUnitStartPosition = nalUnitStartPosition;
            if (!this.allowNonIdrKeyframes || this.nalUnitType != 1) {
                if (!this.detectAccessUnits) {
                    return;
                }
                nalUnitType = this.nalUnitType;
                if (nalUnitType != 5 && nalUnitType != 1 && nalUnitType != 2) {
                    return;
                }
            }
            final SliceHeaderData previousSliceHeader = this.previousSliceHeader;
            this.previousSliceHeader = this.sliceHeader;
            (this.sliceHeader = previousSliceHeader).clear();
            this.bufferLength = 0;
            this.isFilling = true;
        }
        
        private static final class SliceHeaderData
        {
            private boolean bottomFieldFlag;
            private boolean bottomFieldFlagPresent;
            private int deltaPicOrderCnt0;
            private int deltaPicOrderCnt1;
            private int deltaPicOrderCntBottom;
            private boolean fieldPicFlag;
            private int frameNum;
            private boolean hasSliceType;
            private boolean idrPicFlag;
            private int idrPicId;
            private boolean isComplete;
            private int nalRefIdc;
            private int picOrderCntLsb;
            private int picParameterSetId;
            private int sliceType;
            private NalUnitUtil.SpsData spsData;
            
            private boolean isFirstVclNalUnitOfPicture(final SliceHeaderData sliceHeaderData) {
                final boolean isComplete = this.isComplete;
                final boolean b = true;
                if (isComplete) {
                    boolean b2 = b;
                    if (!sliceHeaderData.isComplete) {
                        return b2;
                    }
                    b2 = b;
                    if (this.frameNum != sliceHeaderData.frameNum) {
                        return b2;
                    }
                    b2 = b;
                    if (this.picParameterSetId != sliceHeaderData.picParameterSetId) {
                        return b2;
                    }
                    b2 = b;
                    if (this.fieldPicFlag != sliceHeaderData.fieldPicFlag) {
                        return b2;
                    }
                    if (this.bottomFieldFlagPresent && sliceHeaderData.bottomFieldFlagPresent) {
                        b2 = b;
                        if (this.bottomFieldFlag != sliceHeaderData.bottomFieldFlag) {
                            return b2;
                        }
                    }
                    final int nalRefIdc = this.nalRefIdc;
                    final int nalRefIdc2 = sliceHeaderData.nalRefIdc;
                    if (nalRefIdc != nalRefIdc2) {
                        b2 = b;
                        if (nalRefIdc == 0) {
                            return b2;
                        }
                        b2 = b;
                        if (nalRefIdc2 == 0) {
                            return b2;
                        }
                    }
                    if (this.spsData.picOrderCountType == 0 && sliceHeaderData.spsData.picOrderCountType == 0) {
                        b2 = b;
                        if (this.picOrderCntLsb != sliceHeaderData.picOrderCntLsb) {
                            return b2;
                        }
                        b2 = b;
                        if (this.deltaPicOrderCntBottom != sliceHeaderData.deltaPicOrderCntBottom) {
                            return b2;
                        }
                    }
                    if (this.spsData.picOrderCountType == 1 && sliceHeaderData.spsData.picOrderCountType == 1) {
                        b2 = b;
                        if (this.deltaPicOrderCnt0 != sliceHeaderData.deltaPicOrderCnt0) {
                            return b2;
                        }
                        b2 = b;
                        if (this.deltaPicOrderCnt1 != sliceHeaderData.deltaPicOrderCnt1) {
                            return b2;
                        }
                    }
                    final boolean idrPicFlag = this.idrPicFlag;
                    final boolean idrPicFlag2 = sliceHeaderData.idrPicFlag;
                    b2 = b;
                    if (idrPicFlag != idrPicFlag2) {
                        return b2;
                    }
                    if (idrPicFlag && idrPicFlag2 && this.idrPicId != sliceHeaderData.idrPicId) {
                        b2 = b;
                        return b2;
                    }
                }
                return false;
            }
            
            public void clear() {
                this.hasSliceType = false;
                this.isComplete = false;
            }
            
            public boolean isISlice() {
                if (this.hasSliceType) {
                    final int sliceType = this.sliceType;
                    if (sliceType == 7 || sliceType == 2) {
                        return true;
                    }
                }
                return false;
            }
            
            public void setAll(final NalUnitUtil.SpsData spsData, final int nalRefIdc, final int sliceType, final int frameNum, final int picParameterSetId, final boolean fieldPicFlag, final boolean bottomFieldFlagPresent, final boolean bottomFieldFlag, final boolean idrPicFlag, final int idrPicId, final int picOrderCntLsb, final int deltaPicOrderCntBottom, final int deltaPicOrderCnt0, final int deltaPicOrderCnt2) {
                this.spsData = spsData;
                this.nalRefIdc = nalRefIdc;
                this.sliceType = sliceType;
                this.frameNum = frameNum;
                this.picParameterSetId = picParameterSetId;
                this.fieldPicFlag = fieldPicFlag;
                this.bottomFieldFlagPresent = bottomFieldFlagPresent;
                this.bottomFieldFlag = bottomFieldFlag;
                this.idrPicFlag = idrPicFlag;
                this.idrPicId = idrPicId;
                this.picOrderCntLsb = picOrderCntLsb;
                this.deltaPicOrderCntBottom = deltaPicOrderCntBottom;
                this.deltaPicOrderCnt0 = deltaPicOrderCnt0;
                this.deltaPicOrderCnt1 = deltaPicOrderCnt2;
                this.isComplete = true;
                this.hasSliceType = true;
            }
            
            public void setSliceType(final int sliceType) {
                this.sliceType = sliceType;
                this.hasSliceType = true;
            }
        }
    }
}
