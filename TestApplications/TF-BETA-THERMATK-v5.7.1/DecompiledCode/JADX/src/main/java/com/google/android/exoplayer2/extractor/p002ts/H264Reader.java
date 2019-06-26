package com.google.android.exoplayer2.extractor.p002ts;

import android.util.SparseArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.NalUnitUtil.PpsData;
import com.google.android.exoplayer2.util.NalUnitUtil.SpsData;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.ParsableNalUnitBitArray;
import java.util.ArrayList;
import java.util.Arrays;

/* renamed from: com.google.android.exoplayer2.extractor.ts.H264Reader */
public final class H264Reader implements ElementaryStreamReader {
    private final boolean allowNonIdrKeyframes;
    private final boolean detectAccessUnits;
    private String formatId;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private final NalUnitTargetBuffer pps = new NalUnitTargetBuffer(8, 128);
    private final boolean[] prefixFlags = new boolean[3];
    private boolean randomAccessIndicator;
    private SampleReader sampleReader;
    private final NalUnitTargetBuffer sei = new NalUnitTargetBuffer(6, 128);
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper = new ParsableByteArray();
    private final NalUnitTargetBuffer sps = new NalUnitTargetBuffer(7, 128);
    private long totalBytesWritten;

    /* renamed from: com.google.android.exoplayer2.extractor.ts.H264Reader$SampleReader */
    private static final class SampleReader {
        private final boolean allowNonIdrKeyframes;
        private final ParsableNalUnitBitArray bitArray = new ParsableNalUnitBitArray(this.buffer, 0, 0);
        private byte[] buffer = new byte[128];
        private int bufferLength;
        private final boolean detectAccessUnits;
        private boolean isFilling;
        private long nalUnitStartPosition;
        private long nalUnitTimeUs;
        private int nalUnitType;
        private final TrackOutput output;
        private final SparseArray<PpsData> pps = new SparseArray();
        private SliceHeaderData previousSliceHeader = new SliceHeaderData();
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        private SliceHeaderData sliceHeader = new SliceHeaderData();
        private final SparseArray<SpsData> sps = new SparseArray();

        /* renamed from: com.google.android.exoplayer2.extractor.ts.H264Reader$SampleReader$SliceHeaderData */
        private static final class SliceHeaderData {
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
            private SpsData spsData;

            private SliceHeaderData() {
            }

            public void clear() {
                this.hasSliceType = false;
                this.isComplete = false;
            }

            public void setSliceType(int i) {
                this.sliceType = i;
                this.hasSliceType = true;
            }

            public void setAll(SpsData spsData, int i, int i2, int i3, int i4, boolean z, boolean z2, boolean z3, boolean z4, int i5, int i6, int i7, int i8, int i9) {
                this.spsData = spsData;
                this.nalRefIdc = i;
                this.sliceType = i2;
                this.frameNum = i3;
                this.picParameterSetId = i4;
                this.fieldPicFlag = z;
                this.bottomFieldFlagPresent = z2;
                this.bottomFieldFlag = z3;
                this.idrPicFlag = z4;
                this.idrPicId = i5;
                this.picOrderCntLsb = i6;
                this.deltaPicOrderCntBottom = i7;
                this.deltaPicOrderCnt0 = i8;
                this.deltaPicOrderCnt1 = i9;
                this.isComplete = true;
                this.hasSliceType = true;
            }

            public boolean isISlice() {
                if (this.hasSliceType) {
                    int i = this.sliceType;
                    if (i == 7 || i == 2) {
                        return true;
                    }
                }
                return false;
            }

            private boolean isFirstVclNalUnitOfPicture(SliceHeaderData sliceHeaderData) {
                if (this.isComplete) {
                    if (!sliceHeaderData.isComplete || this.frameNum != sliceHeaderData.frameNum || this.picParameterSetId != sliceHeaderData.picParameterSetId || this.fieldPicFlag != sliceHeaderData.fieldPicFlag) {
                        return true;
                    }
                    if (this.bottomFieldFlagPresent && sliceHeaderData.bottomFieldFlagPresent && this.bottomFieldFlag != sliceHeaderData.bottomFieldFlag) {
                        return true;
                    }
                    int i = this.nalRefIdc;
                    int i2 = sliceHeaderData.nalRefIdc;
                    if (i != i2 && (i == 0 || i2 == 0)) {
                        return true;
                    }
                    if (this.spsData.picOrderCountType == 0 && sliceHeaderData.spsData.picOrderCountType == 0 && (this.picOrderCntLsb != sliceHeaderData.picOrderCntLsb || this.deltaPicOrderCntBottom != sliceHeaderData.deltaPicOrderCntBottom)) {
                        return true;
                    }
                    if (this.spsData.picOrderCountType == 1 && sliceHeaderData.spsData.picOrderCountType == 1 && (this.deltaPicOrderCnt0 != sliceHeaderData.deltaPicOrderCnt0 || this.deltaPicOrderCnt1 != sliceHeaderData.deltaPicOrderCnt1)) {
                        return true;
                    }
                    boolean z = this.idrPicFlag;
                    boolean z2 = sliceHeaderData.idrPicFlag;
                    if (z != z2) {
                        return true;
                    }
                    if (z && z2 && this.idrPicId != sliceHeaderData.idrPicId) {
                        return true;
                    }
                }
                return false;
            }
        }

        public SampleReader(TrackOutput trackOutput, boolean z, boolean z2) {
            this.output = trackOutput;
            this.allowNonIdrKeyframes = z;
            this.detectAccessUnits = z2;
            reset();
        }

        public boolean needsSpsPps() {
            return this.detectAccessUnits;
        }

        public void putSps(SpsData spsData) {
            this.sps.append(spsData.seqParameterSetId, spsData);
        }

        public void putPps(PpsData ppsData) {
            this.pps.append(ppsData.picParameterSetId, ppsData);
        }

        public void reset() {
            this.isFilling = false;
            this.readingSample = false;
            this.sliceHeader.clear();
        }

        public void startNalUnit(long j, int i, long j2) {
            this.nalUnitType = i;
            this.nalUnitTimeUs = j2;
            this.nalUnitStartPosition = j;
            if (!(this.allowNonIdrKeyframes && this.nalUnitType == 1)) {
                if (this.detectAccessUnits) {
                    int i2 = this.nalUnitType;
                    if (!(i2 == 5 || i2 == 1 || i2 == 2)) {
                        return;
                    }
                }
                return;
            }
            SliceHeaderData sliceHeaderData = this.previousSliceHeader;
            this.previousSliceHeader = this.sliceHeader;
            this.sliceHeader = sliceHeaderData;
            this.sliceHeader.clear();
            this.bufferLength = 0;
            this.isFilling = true;
        }

        /* JADX WARNING: Removed duplicated region for block: B:52:0x0104  */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x0101  */
        /* JADX WARNING: Removed duplicated region for block: B:58:0x011a  */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x0108  */
        /* JADX WARNING: Removed duplicated region for block: B:72:0x0150  */
        /* JADX WARNING: Removed duplicated region for block: B:61:0x0120  */
        public void appendToNalUnit(byte[] r24, int r25, int r26) {
            /*
            r23 = this;
            r0 = r23;
            r1 = r25;
            r2 = r0.isFilling;
            if (r2 != 0) goto L_0x0009;
        L_0x0008:
            return;
        L_0x0009:
            r2 = r26 - r1;
            r3 = r0.buffer;
            r4 = r3.length;
            r5 = r0.bufferLength;
            r6 = r5 + r2;
            r7 = 2;
            if (r4 >= r6) goto L_0x001e;
        L_0x0015:
            r5 = r5 + r2;
            r5 = r5 * 2;
            r3 = java.util.Arrays.copyOf(r3, r5);
            r0.buffer = r3;
        L_0x001e:
            r3 = r0.buffer;
            r4 = r0.bufferLength;
            r5 = r24;
            java.lang.System.arraycopy(r5, r1, r3, r4, r2);
            r1 = r0.bufferLength;
            r1 = r1 + r2;
            r0.bufferLength = r1;
            r1 = r0.bitArray;
            r2 = r0.buffer;
            r3 = r0.bufferLength;
            r4 = 0;
            r1.reset(r2, r4, r3);
            r1 = r0.bitArray;
            r2 = 8;
            r1 = r1.canReadBits(r2);
            if (r1 != 0) goto L_0x0041;
        L_0x0040:
            return;
        L_0x0041:
            r1 = r0.bitArray;
            r1.skipBit();
            r1 = r0.bitArray;
            r10 = r1.readBits(r7);
            r1 = r0.bitArray;
            r2 = 5;
            r1.skipBits(r2);
            r1 = r0.bitArray;
            r1 = r1.canReadExpGolombCodedNum();
            if (r1 != 0) goto L_0x005b;
        L_0x005a:
            return;
        L_0x005b:
            r1 = r0.bitArray;
            r1.readUnsignedExpGolombCodedInt();
            r1 = r0.bitArray;
            r1 = r1.canReadExpGolombCodedNum();
            if (r1 != 0) goto L_0x0069;
        L_0x0068:
            return;
        L_0x0069:
            r1 = r0.bitArray;
            r11 = r1.readUnsignedExpGolombCodedInt();
            r1 = r0.detectAccessUnits;
            if (r1 != 0) goto L_0x007b;
        L_0x0073:
            r0.isFilling = r4;
            r1 = r0.sliceHeader;
            r1.setSliceType(r11);
            return;
        L_0x007b:
            r1 = r0.bitArray;
            r1 = r1.canReadExpGolombCodedNum();
            if (r1 != 0) goto L_0x0084;
        L_0x0083:
            return;
        L_0x0084:
            r1 = r0.bitArray;
            r13 = r1.readUnsignedExpGolombCodedInt();
            r1 = r0.pps;
            r1 = r1.indexOfKey(r13);
            if (r1 >= 0) goto L_0x0095;
        L_0x0092:
            r0.isFilling = r4;
            return;
        L_0x0095:
            r1 = r0.pps;
            r1 = r1.get(r13);
            r1 = (com.google.android.exoplayer2.util.NalUnitUtil.PpsData) r1;
            r3 = r0.sps;
            r5 = r1.seqParameterSetId;
            r3 = r3.get(r5);
            r9 = r3;
            r9 = (com.google.android.exoplayer2.util.NalUnitUtil.SpsData) r9;
            r3 = r9.separateColorPlaneFlag;
            if (r3 == 0) goto L_0x00ba;
        L_0x00ac:
            r3 = r0.bitArray;
            r3 = r3.canReadBits(r7);
            if (r3 != 0) goto L_0x00b5;
        L_0x00b4:
            return;
        L_0x00b5:
            r3 = r0.bitArray;
            r3.skipBits(r7);
        L_0x00ba:
            r3 = r0.bitArray;
            r5 = r9.frameNumLength;
            r3 = r3.canReadBits(r5);
            if (r3 != 0) goto L_0x00c5;
        L_0x00c4:
            return;
        L_0x00c5:
            r3 = r0.bitArray;
            r5 = r9.frameNumLength;
            r12 = r3.readBits(r5);
            r3 = r9.frameMbsOnlyFlag;
            r5 = 1;
            if (r3 != 0) goto L_0x00f9;
        L_0x00d2:
            r3 = r0.bitArray;
            r3 = r3.canReadBits(r5);
            if (r3 != 0) goto L_0x00db;
        L_0x00da:
            return;
        L_0x00db:
            r3 = r0.bitArray;
            r3 = r3.readBit();
            if (r3 == 0) goto L_0x00f7;
        L_0x00e3:
            r6 = r0.bitArray;
            r6 = r6.canReadBits(r5);
            if (r6 != 0) goto L_0x00ec;
        L_0x00eb:
            return;
        L_0x00ec:
            r6 = r0.bitArray;
            r6 = r6.readBit();
            r14 = r3;
            r16 = r6;
            r15 = 1;
            goto L_0x00fd;
        L_0x00f7:
            r14 = r3;
            goto L_0x00fa;
        L_0x00f9:
            r14 = 0;
        L_0x00fa:
            r15 = 0;
            r16 = 0;
        L_0x00fd:
            r3 = r0.nalUnitType;
            if (r3 != r2) goto L_0x0104;
        L_0x0101:
            r17 = 1;
            goto L_0x0106;
        L_0x0104:
            r17 = 0;
        L_0x0106:
            if (r17 == 0) goto L_0x011a;
        L_0x0108:
            r2 = r0.bitArray;
            r2 = r2.canReadExpGolombCodedNum();
            if (r2 != 0) goto L_0x0111;
        L_0x0110:
            return;
        L_0x0111:
            r2 = r0.bitArray;
            r2 = r2.readUnsignedExpGolombCodedInt();
            r18 = r2;
            goto L_0x011c;
        L_0x011a:
            r18 = 0;
        L_0x011c:
            r2 = r9.picOrderCountType;
            if (r2 != 0) goto L_0x0150;
        L_0x0120:
            r2 = r0.bitArray;
            r3 = r9.picOrderCntLsbLength;
            r2 = r2.canReadBits(r3);
            if (r2 != 0) goto L_0x012b;
        L_0x012a:
            return;
        L_0x012b:
            r2 = r0.bitArray;
            r3 = r9.picOrderCntLsbLength;
            r2 = r2.readBits(r3);
            r1 = r1.bottomFieldPicOrderInFramePresentFlag;
            if (r1 == 0) goto L_0x014d;
        L_0x0137:
            if (r14 != 0) goto L_0x014d;
        L_0x0139:
            r1 = r0.bitArray;
            r1 = r1.canReadExpGolombCodedNum();
            if (r1 != 0) goto L_0x0142;
        L_0x0141:
            return;
        L_0x0142:
            r1 = r0.bitArray;
            r1 = r1.readSignedExpGolombCodedInt();
            r20 = r1;
            r19 = r2;
            goto L_0x018e;
        L_0x014d:
            r19 = r2;
            goto L_0x018c;
        L_0x0150:
            if (r2 != r5) goto L_0x018a;
        L_0x0152:
            r2 = r9.deltaPicOrderAlwaysZeroFlag;
            if (r2 != 0) goto L_0x018a;
        L_0x0156:
            r2 = r0.bitArray;
            r2 = r2.canReadExpGolombCodedNum();
            if (r2 != 0) goto L_0x015f;
        L_0x015e:
            return;
        L_0x015f:
            r2 = r0.bitArray;
            r2 = r2.readSignedExpGolombCodedInt();
            r1 = r1.bottomFieldPicOrderInFramePresentFlag;
            if (r1 == 0) goto L_0x0183;
        L_0x0169:
            if (r14 != 0) goto L_0x0183;
        L_0x016b:
            r1 = r0.bitArray;
            r1 = r1.canReadExpGolombCodedNum();
            if (r1 != 0) goto L_0x0174;
        L_0x0173:
            return;
        L_0x0174:
            r1 = r0.bitArray;
            r1 = r1.readSignedExpGolombCodedInt();
            r22 = r1;
            r21 = r2;
            r19 = 0;
            r20 = 0;
            goto L_0x0192;
        L_0x0183:
            r21 = r2;
            r19 = 0;
            r20 = 0;
            goto L_0x0190;
        L_0x018a:
            r19 = 0;
        L_0x018c:
            r20 = 0;
        L_0x018e:
            r21 = 0;
        L_0x0190:
            r22 = 0;
        L_0x0192:
            r8 = r0.sliceHeader;
            r8.setAll(r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22);
            r0.isFilling = r4;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.p002ts.H264Reader$SampleReader.appendToNalUnit(byte[], int, int):void");
        }

        public boolean endNalUnit(long j, int i, boolean z, boolean z2) {
            int i2 = 0;
            if (this.nalUnitType == 9 || (this.detectAccessUnits && this.sliceHeader.isFirstVclNalUnitOfPicture(this.previousSliceHeader))) {
                if (z && this.readingSample) {
                    outputSample(i + ((int) (j - this.nalUnitStartPosition)));
                }
                this.samplePosition = this.nalUnitStartPosition;
                this.sampleTimeUs = this.nalUnitTimeUs;
                this.sampleIsKeyframe = false;
                this.readingSample = true;
            }
            if (this.allowNonIdrKeyframes) {
                z2 = this.sliceHeader.isISlice();
            }
            boolean z3 = this.sampleIsKeyframe;
            int i3 = this.nalUnitType;
            if (i3 == 5 || (z2 && i3 == 1)) {
                i2 = 1;
            }
            this.sampleIsKeyframe = z3 | i2;
            return this.sampleIsKeyframe;
        }

        private void outputSample(int i) {
            this.output.sampleMetadata(this.sampleTimeUs, this.sampleIsKeyframe, (int) (this.nalUnitStartPosition - this.samplePosition), i, null);
        }
    }

    public void packetFinished() {
    }

    public H264Reader(SeiReader seiReader, boolean z, boolean z2) {
        this.seiReader = seiReader;
        this.allowNonIdrKeyframes = z;
        this.detectAccessUnits = z2;
    }

    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.sps.reset();
        this.pps.reset();
        this.sei.reset();
        this.sampleReader.reset();
        this.totalBytesWritten = 0;
        this.randomAccessIndicator = false;
    }

    public void createTracks(ExtractorOutput extractorOutput, TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 2);
        this.sampleReader = new SampleReader(this.output, this.allowNonIdrKeyframes, this.detectAccessUnits);
        this.seiReader.createTracks(extractorOutput, trackIdGenerator);
    }

    public void packetStarted(long j, int i) {
        this.pesTimeUs = j;
        this.randomAccessIndicator |= (i & 2) != 0 ? 1 : 0;
    }

    public void consume(ParsableByteArray parsableByteArray) {
        int position = parsableByteArray.getPosition();
        int limit = parsableByteArray.limit();
        byte[] bArr = parsableByteArray.data;
        this.totalBytesWritten += (long) parsableByteArray.bytesLeft();
        this.output.sampleData(parsableByteArray, parsableByteArray.bytesLeft());
        while (true) {
            int findNalUnit = NalUnitUtil.findNalUnit(bArr, position, limit, this.prefixFlags);
            if (findNalUnit == limit) {
                nalUnitData(bArr, position, limit);
                return;
            }
            int nalUnitType = NalUnitUtil.getNalUnitType(bArr, findNalUnit);
            int i = findNalUnit - position;
            if (i > 0) {
                nalUnitData(bArr, position, findNalUnit);
            }
            int i2 = limit - findNalUnit;
            long j = this.totalBytesWritten - ((long) i2);
            endNalUnit(j, i2, i < 0 ? -i : 0, this.pesTimeUs);
            startNalUnit(j, nalUnitType, this.pesTimeUs);
            position = findNalUnit + 3;
        }
    }

    private void startNalUnit(long j, int i, long j2) {
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.startNalUnit(i);
            this.pps.startNalUnit(i);
        }
        this.sei.startNalUnit(i);
        this.sampleReader.startNalUnit(j, i, j2);
    }

    private void nalUnitData(byte[] bArr, int i, int i2) {
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.appendToNalUnit(bArr, i, i2);
            this.pps.appendToNalUnit(bArr, i, i2);
        }
        this.sei.appendToNalUnit(bArr, i, i2);
        this.sampleReader.appendToNalUnit(bArr, i, i2);
    }

    private void endNalUnit(long j, int i, int i2, long j2) {
        NalUnitTargetBuffer nalUnitTargetBuffer;
        int i3 = i2;
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.endNalUnit(i3);
            this.pps.endNalUnit(i3);
            if (this.hasOutputFormat) {
                if (this.sps.isCompleted()) {
                    nalUnitTargetBuffer = this.sps;
                    this.sampleReader.putSps(NalUnitUtil.parseSpsNalUnit(nalUnitTargetBuffer.nalData, 3, nalUnitTargetBuffer.nalLength));
                    this.sps.reset();
                } else if (this.pps.isCompleted()) {
                    nalUnitTargetBuffer = this.pps;
                    this.sampleReader.putPps(NalUnitUtil.parsePpsNalUnit(nalUnitTargetBuffer.nalData, 3, nalUnitTargetBuffer.nalLength));
                    this.pps.reset();
                }
            } else if (this.sps.isCompleted() && this.pps.isCompleted()) {
                ArrayList arrayList = new ArrayList();
                NalUnitTargetBuffer nalUnitTargetBuffer2 = this.sps;
                arrayList.add(Arrays.copyOf(nalUnitTargetBuffer2.nalData, nalUnitTargetBuffer2.nalLength));
                nalUnitTargetBuffer2 = this.pps;
                arrayList.add(Arrays.copyOf(nalUnitTargetBuffer2.nalData, nalUnitTargetBuffer2.nalLength));
                nalUnitTargetBuffer2 = this.sps;
                SpsData parseSpsNalUnit = NalUnitUtil.parseSpsNalUnit(nalUnitTargetBuffer2.nalData, 3, nalUnitTargetBuffer2.nalLength);
                NalUnitTargetBuffer nalUnitTargetBuffer3 = this.pps;
                PpsData parsePpsNalUnit = NalUnitUtil.parsePpsNalUnit(nalUnitTargetBuffer3.nalData, 3, nalUnitTargetBuffer3.nalLength);
                this.output.format(Format.createVideoSampleFormat(this.formatId, "video/avc", CodecSpecificDataUtil.buildAvcCodecString(parseSpsNalUnit.profileIdc, parseSpsNalUnit.constraintsFlagsAndReservedZero2Bits, parseSpsNalUnit.levelIdc), -1, -1, parseSpsNalUnit.width, parseSpsNalUnit.height, -1.0f, arrayList, -1, parseSpsNalUnit.pixelWidthAspectRatio, null));
                this.hasOutputFormat = true;
                this.sampleReader.putSps(parseSpsNalUnit);
                this.sampleReader.putPps(parsePpsNalUnit);
                this.sps.reset();
                this.pps.reset();
            }
        }
        if (this.sei.endNalUnit(i2)) {
            nalUnitTargetBuffer = this.sei;
            this.seiWrapper.reset(this.sei.nalData, NalUnitUtil.unescapeStream(nalUnitTargetBuffer.nalData, nalUnitTargetBuffer.nalLength));
            this.seiWrapper.setPosition(4);
            this.seiReader.consume(j2, this.seiWrapper);
        }
        if (this.sampleReader.endNalUnit(j, i, this.hasOutputFormat, this.randomAccessIndicator)) {
            this.randomAccessIndicator = false;
        }
    }
}
