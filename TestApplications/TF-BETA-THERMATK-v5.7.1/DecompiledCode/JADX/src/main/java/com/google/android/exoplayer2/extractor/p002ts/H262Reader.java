package com.google.android.exoplayer2.extractor.p002ts;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Arrays;

/* renamed from: com.google.android.exoplayer2.extractor.ts.H262Reader */
public final class H262Reader implements ElementaryStreamReader {
    private static final double[] FRAME_RATE_VALUES = new double[]{23.976023976023978d, 24.0d, 25.0d, 29.97002997002997d, 30.0d, 50.0d, 59.94005994005994d, 60.0d};
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

    /* renamed from: com.google.android.exoplayer2.extractor.ts.H262Reader$CsdBuffer */
    private static final class CsdBuffer {
        private static final byte[] START_CODE = new byte[]{(byte) 0, (byte) 0, (byte) 1};
        public byte[] data;
        private boolean isFilling;
        public int length;
        public int sequenceExtensionPosition;

        public CsdBuffer(int i) {
            this.data = new byte[i];
        }

        public void reset() {
            this.isFilling = false;
            this.length = 0;
            this.sequenceExtensionPosition = 0;
        }

        public boolean onStartCode(int i, int i2) {
            if (this.isFilling) {
                this.length -= i2;
                if (this.sequenceExtensionPosition == 0 && i == 181) {
                    this.sequenceExtensionPosition = this.length;
                } else {
                    this.isFilling = false;
                    return true;
                }
            } else if (i == 179) {
                this.isFilling = true;
            }
            byte[] bArr = START_CODE;
            onData(bArr, 0, bArr.length);
            return false;
        }

        public void onData(byte[] bArr, int i, int i2) {
            if (this.isFilling) {
                i2 -= i;
                byte[] bArr2 = this.data;
                int length = bArr2.length;
                int i3 = this.length;
                if (length < i3 + i2) {
                    this.data = Arrays.copyOf(bArr2, (i3 + i2) * 2);
                }
                System.arraycopy(bArr, i, this.data, this.length, i2);
                this.length += i2;
            }
        }
    }

    public void packetFinished() {
    }

    public H262Reader() {
        this(null);
    }

    public H262Reader(UserDataReader userDataReader) {
        this.userDataReader = userDataReader;
        this.prefixFlags = new boolean[4];
        this.csdBuffer = new CsdBuffer(128);
        if (userDataReader != null) {
            this.userData = new NalUnitTargetBuffer(178, 128);
            this.userDataParsable = new ParsableByteArray();
            return;
        }
        this.userData = null;
        this.userDataParsable = null;
    }

    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.csdBuffer.reset();
        if (this.userDataReader != null) {
            this.userData.reset();
        }
        this.totalBytesWritten = 0;
        this.startedFirstSample = false;
    }

    public void createTracks(ExtractorOutput extractorOutput, TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 2);
        UserDataReader userDataReader = this.userDataReader;
        if (userDataReader != null) {
            userDataReader.createTracks(extractorOutput, trackIdGenerator);
        }
    }

    public void packetStarted(long j, int i) {
        this.pesTimeUs = j;
    }

    public void consume(ParsableByteArray parsableByteArray) {
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        int position = parsableByteArray.getPosition();
        int limit = parsableByteArray.limit();
        byte[] bArr = parsableByteArray2.data;
        this.totalBytesWritten += (long) parsableByteArray.bytesLeft();
        this.output.sampleData(parsableByteArray2, parsableByteArray.bytesLeft());
        while (true) {
            int findNalUnit = NalUnitUtil.findNalUnit(bArr, position, limit, this.prefixFlags);
            if (findNalUnit == limit) {
                break;
            }
            int i = findNalUnit + 3;
            int i2 = parsableByteArray2.data[i] & NalUnitUtil.EXTENDED_SAR;
            int i3 = findNalUnit - position;
            boolean z = false;
            if (!this.hasOutputFormat) {
                if (i3 > 0) {
                    this.csdBuffer.onData(bArr, position, findNalUnit);
                }
                if (this.csdBuffer.onStartCode(i2, i3 < 0 ? -i3 : 0)) {
                    Pair parseCsdBuffer = H262Reader.parseCsdBuffer(this.csdBuffer, this.formatId);
                    this.output.format((Format) parseCsdBuffer.first);
                    this.frameDurationUs = ((Long) parseCsdBuffer.second).longValue();
                    this.hasOutputFormat = true;
                }
            }
            if (this.userDataReader != null) {
                if (i3 > 0) {
                    this.userData.appendToNalUnit(bArr, position, findNalUnit);
                    position = 0;
                } else {
                    position = -i3;
                }
                if (this.userData.endNalUnit(position)) {
                    NalUnitTargetBuffer nalUnitTargetBuffer = this.userData;
                    this.userDataParsable.reset(this.userData.nalData, NalUnitUtil.unescapeStream(nalUnitTargetBuffer.nalData, nalUnitTargetBuffer.nalLength));
                    this.userDataReader.consume(this.sampleTimeUs, this.userDataParsable);
                }
                if (i2 == 178 && parsableByteArray2.data[findNalUnit + 2] == (byte) 1) {
                    this.userData.startNalUnit(i2);
                }
            }
            if (i2 == 0 || i2 == 179) {
                position = limit - findNalUnit;
                if (this.startedFirstSample && this.sampleHasPicture && this.hasOutputFormat) {
                    this.output.sampleMetadata(this.sampleTimeUs, this.sampleIsKeyframe, ((int) (this.totalBytesWritten - this.samplePosition)) - position, position, null);
                }
                if (!this.startedFirstSample || this.sampleHasPicture) {
                    this.samplePosition = this.totalBytesWritten - ((long) position);
                    long j = this.pesTimeUs;
                    if (j == -9223372036854775807L) {
                        j = this.startedFirstSample ? this.sampleTimeUs + this.frameDurationUs : 0;
                    }
                    this.sampleTimeUs = j;
                    this.sampleIsKeyframe = false;
                    this.pesTimeUs = -9223372036854775807L;
                    this.startedFirstSample = true;
                }
                if (i2 == 0) {
                    z = true;
                }
                this.sampleHasPicture = z;
            } else if (i2 == 184) {
                this.sampleIsKeyframe = true;
            }
            position = i;
        }
        if (!this.hasOutputFormat) {
            this.csdBuffer.onData(bArr, position, limit);
        }
        if (this.userDataReader != null) {
            this.userData.appendToNalUnit(bArr, position, limit);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x006b  */
    private static android.util.Pair<com.google.android.exoplayer2.Format, java.lang.Long> parseCsdBuffer(com.google.android.exoplayer2.extractor.p002ts.H262Reader.CsdBuffer r20, java.lang.String r21) {
        /*
        r0 = r20;
        r1 = r0.data;
        r2 = r0.length;
        r1 = java.util.Arrays.copyOf(r1, r2);
        r2 = 4;
        r3 = r1[r2];
        r3 = r3 & 255;
        r4 = 5;
        r5 = r1[r4];
        r5 = r5 & 255;
        r6 = 6;
        r6 = r1[r6];
        r6 = r6 & 255;
        r3 = r3 << r2;
        r7 = r5 >> 4;
        r13 = r3 | r7;
        r3 = r5 & 15;
        r3 = r3 << 8;
        r14 = r3 | r6;
        r3 = 7;
        r5 = r1[r3];
        r5 = r5 & 240;
        r5 = r5 >> r2;
        r6 = 2;
        if (r5 == r6) goto L_0x0043;
    L_0x002d:
        r6 = 3;
        if (r5 == r6) goto L_0x003d;
    L_0x0030:
        if (r5 == r2) goto L_0x0037;
    L_0x0032:
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r18 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x004c;
    L_0x0037:
        r2 = r14 * 121;
        r2 = (float) r2;
        r5 = r13 * 100;
        goto L_0x0048;
    L_0x003d:
        r2 = r14 * 16;
        r2 = (float) r2;
        r5 = r13 * 9;
        goto L_0x0048;
    L_0x0043:
        r2 = r14 * 4;
        r2 = (float) r2;
        r5 = r13 * 3;
    L_0x0048:
        r5 = (float) r5;
        r2 = r2 / r5;
        r18 = r2;
    L_0x004c:
        r10 = 0;
        r11 = -1;
        r12 = -1;
        r15 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r16 = java.util.Collections.singletonList(r1);
        r17 = -1;
        r19 = 0;
        r9 = "video/mpeg2";
        r8 = r21;
        r2 = com.google.android.exoplayer2.Format.createVideoSampleFormat(r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19);
        r5 = 0;
        r3 = r1[r3];
        r3 = r3 & 15;
        r3 = r3 + -1;
        if (r3 < 0) goto L_0x0098;
    L_0x006b:
        r7 = FRAME_RATE_VALUES;
        r8 = r7.length;
        if (r3 >= r8) goto L_0x0098;
    L_0x0070:
        r5 = r7[r3];
        r0 = r0.sequenceExtensionPosition;
        r0 = r0 + 9;
        r3 = r1[r0];
        r3 = r3 & 96;
        r3 = r3 >> r4;
        r0 = r1[r0];
        r0 = r0 & 31;
        if (r3 == r0) goto L_0x0091;
    L_0x0081:
        r3 = (double) r3;
        r7 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        java.lang.Double.isNaN(r3);
        r3 = r3 + r7;
        r0 = r0 + 1;
        r0 = (double) r0;
        java.lang.Double.isNaN(r0);
        r3 = r3 / r0;
        r5 = r5 * r3;
    L_0x0091:
        r0 = 4696837146684686336; // 0x412e848000000000 float:0.0 double:1000000.0;
        r0 = r0 / r5;
        r5 = (long) r0;
    L_0x0098:
        r0 = java.lang.Long.valueOf(r5);
        r0 = android.util.Pair.create(r2, r0);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.p002ts.H262Reader.parseCsdBuffer(com.google.android.exoplayer2.extractor.ts.H262Reader$CsdBuffer, java.lang.String):android.util.Pair");
    }
}
