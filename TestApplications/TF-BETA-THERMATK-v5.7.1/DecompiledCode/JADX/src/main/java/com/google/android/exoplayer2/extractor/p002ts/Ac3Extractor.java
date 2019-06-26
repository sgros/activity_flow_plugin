package com.google.android.exoplayer2.extractor.p002ts;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap.Unseekable;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

/* renamed from: com.google.android.exoplayer2.extractor.ts.Ac3Extractor */
public final class Ac3Extractor implements Extractor {
    public static final ExtractorsFactory FACTORY = C3339-$$Lambda$Ac3Extractor$c2Fqr1pF6vjFNOhLk9sPPtkNnGE.INSTANCE;
    private static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    private final long firstSampleTimestampUs;
    private final Ac3Reader reader;
    private final ParsableByteArray sampleData;
    private boolean startedPacket;

    public void release() {
    }

    public Ac3Extractor() {
        this(0);
    }

    public Ac3Extractor(long j) {
        this.firstSampleTimestampUs = j;
        this.reader = new Ac3Reader();
        this.sampleData = new ParsableByteArray(2786);
    }

    /* JADX WARNING: Missing block: B:7:0x0032, code skipped:
            r8.resetPeekPosition();
            r4 = r4 + 1;
     */
    /* JADX WARNING: Missing block: B:8:0x003b, code skipped:
            if ((r4 - r3) < org.telegram.messenger.MessagesController.UPDATE_MASK_CHAT) goto L_0x003e;
     */
    /* JADX WARNING: Missing block: B:9:0x003d, code skipped:
            return false;
     */
    public boolean sniff(com.google.android.exoplayer2.extractor.ExtractorInput r8) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r7 = this;
        r0 = new com.google.android.exoplayer2.util.ParsableByteArray;
        r1 = 10;
        r0.<init>(r1);
        r2 = 0;
        r3 = 0;
    L_0x0009:
        r4 = r0.data;
        r8.peekFully(r4, r2, r1);
        r0.setPosition(r2);
        r4 = r0.readUnsignedInt24();
        r5 = ID3_TAG;
        if (r4 == r5) goto L_0x0058;
    L_0x0019:
        r8.resetPeekPosition();
        r8.advancePeekPosition(r3);
        r4 = r3;
    L_0x0020:
        r1 = 0;
    L_0x0021:
        r5 = r0.data;
        r6 = 6;
        r8.peekFully(r5, r2, r6);
        r0.setPosition(r2);
        r5 = r0.readUnsignedShort();
        r6 = 2935; // 0xb77 float:4.113E-42 double:1.45E-320;
        if (r5 == r6) goto L_0x0042;
    L_0x0032:
        r8.resetPeekPosition();
        r4 = r4 + 1;
        r1 = r4 - r3;
        r5 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        if (r1 < r5) goto L_0x003e;
    L_0x003d:
        return r2;
    L_0x003e:
        r8.advancePeekPosition(r4);
        goto L_0x0020;
    L_0x0042:
        r5 = 1;
        r1 = r1 + r5;
        r6 = 4;
        if (r1 < r6) goto L_0x0048;
    L_0x0047:
        return r5;
    L_0x0048:
        r5 = r0.data;
        r5 = com.google.android.exoplayer2.audio.Ac3Util.parseAc3SyncframeSize(r5);
        r6 = -1;
        if (r5 != r6) goto L_0x0052;
    L_0x0051:
        return r2;
    L_0x0052:
        r5 = r5 + -6;
        r8.advancePeekPosition(r5);
        goto L_0x0021;
    L_0x0058:
        r4 = 3;
        r0.skipBytes(r4);
        r4 = r0.readSynchSafeInt();
        r5 = r4 + 10;
        r3 = r3 + r5;
        r8.advancePeekPosition(r4);
        goto L_0x0009;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.p002ts.Ac3Extractor.sniff(com.google.android.exoplayer2.extractor.ExtractorInput):boolean");
    }

    public void init(ExtractorOutput extractorOutput) {
        this.reader.createTracks(extractorOutput, new TrackIdGenerator(0, 1));
        extractorOutput.endTracks();
        extractorOutput.seekMap(new Unseekable(-9223372036854775807L));
    }

    public void seek(long j, long j2) {
        this.startedPacket = false;
        this.reader.seek();
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        int read = extractorInput.read(this.sampleData.data, 0, 2786);
        if (read == -1) {
            return -1;
        }
        this.sampleData.setPosition(0);
        this.sampleData.setLimit(read);
        if (!this.startedPacket) {
            this.reader.packetStarted(this.firstSampleTimestampUs, 4);
            this.startedPacket = true;
        }
        this.reader.consume(this.sampleData);
        return 0;
    }
}
