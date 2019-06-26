package com.google.android.exoplayer2.extractor.mp4;

import android.util.Pair;
import android.util.SparseArray;
import com.google.android.exoplayer2.C0131C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.TrackOutput.CryptoData;
import com.google.android.exoplayer2.text.cea.CeaUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FragmentedMp4Extractor implements Extractor {
    private static final Format EMSG_FORMAT = Format.createSampleFormat(null, MimeTypes.APPLICATION_EMSG, TimestampAdjuster.DO_NOT_OFFSET);
    public static final ExtractorsFactory FACTORY = C3336-$$Lambda$FragmentedMp4Extractor$i0zfpH_PcF0vytkdatCL0xeWFhQ.INSTANCE;
    private static final byte[] PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE = new byte[]{(byte) -94, (byte) 57, (byte) 79, (byte) 82, (byte) 90, (byte) -101, (byte) 79, (byte) 20, (byte) -94, (byte) 68, (byte) 108, (byte) 66, (byte) 124, (byte) 100, (byte) -115, (byte) -12};
    private static final int SAMPLE_GROUP_TYPE_seig = Util.getIntegerCodeForString("seig");
    private final TrackOutput additionalEmsgTrackOutput;
    private ParsableByteArray atomData;
    private final ParsableByteArray atomHeader;
    private int atomHeaderBytesRead;
    private long atomSize;
    private int atomType;
    private TrackOutput[] cea608TrackOutputs;
    private final List<Format> closedCaptionFormats;
    private final ArrayDeque<ContainerAtom> containerAtoms;
    private TrackBundle currentTrackBundle;
    private long durationUs;
    private TrackOutput[] emsgTrackOutputs;
    private long endOfMdatPosition;
    private final byte[] extendedTypeScratch;
    private ExtractorOutput extractorOutput;
    private final int flags;
    private boolean haveOutputSeekMap;
    private final ParsableByteArray nalBuffer;
    private final ParsableByteArray nalPrefix;
    private final ParsableByteArray nalStartCode;
    private int parserState;
    private int pendingMetadataSampleBytes;
    private final ArrayDeque<MetadataSampleInfo> pendingMetadataSampleInfos;
    private long pendingSeekTimeUs;
    private boolean processSeiNalUnitPayload;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private int sampleSize;
    private long segmentIndexEarliestPresentationTimeUs;
    private final DrmInitData sideloadedDrmInitData;
    private final Track sideloadedTrack;
    private final TimestampAdjuster timestampAdjuster;
    private final SparseArray<TrackBundle> trackBundles;

    private static final class MetadataSampleInfo {
        public final long presentationTimeDeltaUs;
        public final int size;

        public MetadataSampleInfo(long j, int i) {
            this.presentationTimeDeltaUs = j;
            this.size = i;
        }
    }

    private static final class TrackBundle {
        public int currentSampleInTrackRun;
        public int currentSampleIndex;
        public int currentTrackRunIndex;
        private final ParsableByteArray defaultInitializationVector = new ParsableByteArray();
        public DefaultSampleValues defaultSampleValues;
        private final ParsableByteArray encryptionSignalByte = new ParsableByteArray(1);
        public int firstSampleToOutputIndex;
        public final TrackFragment fragment = new TrackFragment();
        public final TrackOutput output;
        public Track track;

        public TrackBundle(TrackOutput trackOutput) {
            this.output = trackOutput;
        }

        public void init(Track track, DefaultSampleValues defaultSampleValues) {
            Assertions.checkNotNull(track);
            this.track = track;
            Assertions.checkNotNull(defaultSampleValues);
            this.defaultSampleValues = defaultSampleValues;
            this.output.format(track.format);
            reset();
        }

        public void updateDrmInitData(DrmInitData drmInitData) {
            TrackEncryptionBox sampleDescriptionEncryptionBox = this.track.getSampleDescriptionEncryptionBox(this.fragment.header.sampleDescriptionIndex);
            this.output.format(this.track.format.copyWithDrmInitData(drmInitData.copyWithSchemeType(sampleDescriptionEncryptionBox != null ? sampleDescriptionEncryptionBox.schemeType : null)));
        }

        public void reset() {
            this.fragment.reset();
            this.currentSampleIndex = 0;
            this.currentTrackRunIndex = 0;
            this.currentSampleInTrackRun = 0;
            this.firstSampleToOutputIndex = 0;
        }

        public void seek(long j) {
            j = C0131C.usToMs(j);
            int i = this.currentSampleIndex;
            while (true) {
                TrackFragment trackFragment = this.fragment;
                if (i < trackFragment.sampleCount && trackFragment.getSamplePresentationTime(i) < j) {
                    if (this.fragment.sampleIsSyncFrameTable[i]) {
                        this.firstSampleToOutputIndex = i;
                    }
                    i++;
                } else {
                    return;
                }
            }
        }

        public boolean next() {
            this.currentSampleIndex++;
            this.currentSampleInTrackRun++;
            int i = this.currentSampleInTrackRun;
            int[] iArr = this.fragment.trunLength;
            int i2 = this.currentTrackRunIndex;
            if (i != iArr[i2]) {
                return true;
            }
            this.currentTrackRunIndex = i2 + 1;
            this.currentSampleInTrackRun = 0;
            return false;
        }

        public int outputSampleEncryptionData() {
            TrackEncryptionBox encryptionBoxIfEncrypted = getEncryptionBoxIfEncrypted();
            if (encryptionBoxIfEncrypted == null) {
                return 0;
            }
            ParsableByteArray parsableByteArray;
            int i;
            int i2 = encryptionBoxIfEncrypted.perSampleIvSize;
            if (i2 != 0) {
                int i3 = i2;
                parsableByteArray = this.fragment.sampleEncryptionData;
                i = i3;
            } else {
                byte[] bArr = encryptionBoxIfEncrypted.defaultInitializationVector;
                this.defaultInitializationVector.reset(bArr, bArr.length);
                parsableByteArray = this.defaultInitializationVector;
                i = bArr.length;
            }
            boolean sampleHasSubsampleEncryptionTable = this.fragment.sampleHasSubsampleEncryptionTable(this.currentSampleIndex);
            this.encryptionSignalByte.data[0] = (byte) ((sampleHasSubsampleEncryptionTable ? 128 : 0) | i);
            this.encryptionSignalByte.setPosition(0);
            this.output.sampleData(this.encryptionSignalByte, 1);
            this.output.sampleData(parsableByteArray, i);
            if (!sampleHasSubsampleEncryptionTable) {
                return i + 1;
            }
            ParsableByteArray parsableByteArray2 = this.fragment.sampleEncryptionData;
            i2 = parsableByteArray2.readUnsignedShort();
            parsableByteArray2.skipBytes(-2);
            i2 = (i2 * 6) + 2;
            this.output.sampleData(parsableByteArray2, i2);
            return (i + 1) + i2;
        }

        private void skipSampleEncryptionData() {
            TrackEncryptionBox encryptionBoxIfEncrypted = getEncryptionBoxIfEncrypted();
            if (encryptionBoxIfEncrypted != null) {
                ParsableByteArray parsableByteArray = this.fragment.sampleEncryptionData;
                int i = encryptionBoxIfEncrypted.perSampleIvSize;
                if (i != 0) {
                    parsableByteArray.skipBytes(i);
                }
                if (this.fragment.sampleHasSubsampleEncryptionTable(this.currentSampleIndex)) {
                    parsableByteArray.skipBytes(parsableByteArray.readUnsignedShort() * 6);
                }
            }
        }

        private TrackEncryptionBox getEncryptionBoxIfEncrypted() {
            TrackFragment trackFragment = this.fragment;
            int i = trackFragment.header.sampleDescriptionIndex;
            TrackEncryptionBox trackEncryptionBox = trackFragment.trackEncryptionBox;
            if (trackEncryptionBox == null) {
                trackEncryptionBox = this.track.getSampleDescriptionEncryptionBox(i);
            }
            return (trackEncryptionBox == null || !trackEncryptionBox.isEncrypted) ? null : trackEncryptionBox;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:22:0x006c in {2, 11, 12, 13, 15, 16, 17, 19, 21} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private static void parseSaiz(com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox r7, com.google.android.exoplayer2.util.ParsableByteArray r8, com.google.android.exoplayer2.extractor.mp4.TrackFragment r9) throws com.google.android.exoplayer2.ParserException {
        /*
        r7 = r7.perSampleIvSize;
        r0 = 8;
        r8.setPosition(r0);
        r1 = r8.readInt();
        r1 = com.google.android.exoplayer2.extractor.mp4.Atom.parseFullAtomFlags(r1);
        r2 = 1;
        r1 = r1 & r2;
        if (r1 != r2) goto L_0x0016;
        r8.skipBytes(r0);
        r0 = r8.readUnsignedByte();
        r1 = r8.readUnsignedIntToInt();
        r3 = r9.sampleCount;
        if (r1 != r3) goto L_0x004b;
        r3 = 0;
        if (r0 != 0) goto L_0x003a;
        r0 = r9.sampleHasSubsampleEncryptionTable;
        r4 = 0;
        r5 = 0;
        if (r4 >= r1) goto L_0x0047;
        r6 = r8.readUnsignedByte();
        r5 = r5 + r6;
        if (r6 <= r7) goto L_0x0034;
        r6 = 1;
        goto L_0x0035;
        r6 = 0;
        r0[r4] = r6;
        r4 = r4 + 1;
        goto L_0x0029;
        if (r0 <= r7) goto L_0x003d;
        goto L_0x003e;
        r2 = 0;
        r0 = r0 * r1;
        r5 = r0 + 0;
        r7 = r9.sampleHasSubsampleEncryptionTable;
        java.util.Arrays.fill(r7, r3, r1, r2);
        r9.initEncryptionData(r5);
        return;
        r7 = new com.google.android.exoplayer2.ParserException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r0 = "Length mismatch: ";
        r8.append(r0);
        r8.append(r1);
        r0 = ", ";
        r8.append(r0);
        r9 = r9.sampleCount;
        r8.append(r9);
        r8 = r8.toString();
        r7.<init>(r8);
        throw r7;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor.parseSaiz(com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox, com.google.android.exoplayer2.util.ParsableByteArray, com.google.android.exoplayer2.extractor.mp4.TrackFragment):void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:58:0x0152 in {4, 5, 8, 15, 18, 25, 30, 32, 37, 38, 45, 47, 49, 52, 53, 55, 57} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private boolean readAtomHeader(com.google.android.exoplayer2.extractor.ExtractorInput r9) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r8 = this;
        r0 = r8.atomHeaderBytesRead;
        r1 = 8;
        r2 = 0;
        r3 = 1;
        if (r0 != 0) goto L_0x002a;
        r0 = r8.atomHeader;
        r0 = r0.data;
        r0 = r9.readFully(r0, r2, r1, r3);
        if (r0 != 0) goto L_0x0013;
        return r2;
        r8.atomHeaderBytesRead = r1;
        r0 = r8.atomHeader;
        r0.setPosition(r2);
        r0 = r8.atomHeader;
        r4 = r0.readUnsignedInt();
        r8.atomSize = r4;
        r0 = r8.atomHeader;
        r0 = r0.readInt();
        r8.atomType = r0;
        r4 = r8.atomSize;
        r6 = 1;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 != 0) goto L_0x0047;
        r0 = r8.atomHeader;
        r0 = r0.data;
        r9.readFully(r0, r1, r1);
        r0 = r8.atomHeaderBytesRead;
        r0 = r0 + r1;
        r8.atomHeaderBytesRead = r0;
        r0 = r8.atomHeader;
        r4 = r0.readUnsignedLongToLong();
        r8.atomSize = r4;
        goto L_0x0078;
        r6 = 0;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 != 0) goto L_0x0078;
        r4 = r9.getLength();
        r6 = -1;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 != 0) goto L_0x0069;
        r0 = r8.containerAtoms;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0069;
        r0 = r8.containerAtoms;
        r0 = r0.peek();
        r0 = (com.google.android.exoplayer2.extractor.mp4.Atom.ContainerAtom) r0;
        r4 = r0.endPosition;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 == 0) goto L_0x0078;
        r6 = r9.getPosition();
        r4 = r4 - r6;
        r0 = r8.atomHeaderBytesRead;
        r6 = (long) r0;
        r4 = r4 + r6;
        r8.atomSize = r4;
        r4 = r8.atomSize;
        r0 = r8.atomHeaderBytesRead;
        r6 = (long) r0;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 < 0) goto L_0x014a;
        r4 = r9.getPosition();
        r0 = r8.atomHeaderBytesRead;
        r6 = (long) r0;
        r4 = r4 - r6;
        r0 = r8.atomType;
        r6 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_moof;
        if (r0 != r6) goto L_0x00ab;
        r0 = r8.trackBundles;
        r0 = r0.size();
        r6 = 0;
        if (r6 >= r0) goto L_0x00ab;
        r7 = r8.trackBundles;
        r7 = r7.valueAt(r6);
        r7 = (com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor.TrackBundle) r7;
        r7 = r7.fragment;
        r7.atomPosition = r4;
        r7.auxiliaryDataPosition = r4;
        r7.dataPosition = r4;
        r6 = r6 + 1;
        goto L_0x0096;
        r0 = r8.atomType;
        r6 = com.google.android.exoplayer2.extractor.mp4.Atom.TYPE_mdat;
        r7 = 0;
        if (r0 != r6) goto L_0x00cf;
        r8.currentTrackBundle = r7;
        r0 = r8.atomSize;
        r0 = r0 + r4;
        r8.endOfMdatPosition = r0;
        r9 = r8.haveOutputSeekMap;
        if (r9 != 0) goto L_0x00cb;
        r9 = r8.extractorOutput;
        r0 = new com.google.android.exoplayer2.extractor.SeekMap$Unseekable;
        r1 = r8.durationUs;
        r0.<init>(r1, r4);
        r9.seekMap(r0);
        r8.haveOutputSeekMap = r3;
        r9 = 2;
        r8.parserState = r9;
        return r3;
        r0 = shouldParseContainerAtom(r0);
        if (r0 == 0) goto L_0x00fc;
        r0 = r9.getPosition();
        r4 = r8.atomSize;
        r0 = r0 + r4;
        r4 = 8;
        r0 = r0 - r4;
        r9 = r8.containerAtoms;
        r2 = new com.google.android.exoplayer2.extractor.mp4.Atom$ContainerAtom;
        r4 = r8.atomType;
        r2.<init>(r4, r0);
        r9.push(r2);
        r4 = r8.atomSize;
        r9 = r8.atomHeaderBytesRead;
        r6 = (long) r9;
        r9 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r9 != 0) goto L_0x00f8;
        r8.processAtomEnded(r0);
        goto L_0x0141;
        r8.enterReadingAtomHeaderState();
        goto L_0x0141;
        r9 = r8.atomType;
        r9 = shouldParseLeafAtom(r9);
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r9 == 0) goto L_0x0137;
        r9 = r8.atomHeaderBytesRead;
        if (r9 != r1) goto L_0x012f;
        r6 = r8.atomSize;
        r9 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r9 > 0) goto L_0x0127;
        r9 = new com.google.android.exoplayer2.util.ParsableByteArray;
        r0 = (int) r6;
        r9.<init>(r0);
        r8.atomData = r9;
        r9 = r8.atomHeader;
        r9 = r9.data;
        r0 = r8.atomData;
        r0 = r0.data;
        java.lang.System.arraycopy(r9, r2, r0, r2, r1);
        r8.parserState = r3;
        goto L_0x0141;
        r9 = new com.google.android.exoplayer2.ParserException;
        r0 = "Leaf atom with length > 2147483647 (unsupported).";
        r9.<init>(r0);
        throw r9;
        r9 = new com.google.android.exoplayer2.ParserException;
        r0 = "Leaf atom defines extended atom size (unsupported).";
        r9.<init>(r0);
        throw r9;
        r0 = r8.atomSize;
        r9 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r9 > 0) goto L_0x0142;
        r8.atomData = r7;
        r8.parserState = r3;
        return r3;
        r9 = new com.google.android.exoplayer2.ParserException;
        r0 = "Skipping atom with length > 2147483647 (unsupported).";
        r9.<init>(r0);
        throw r9;
        r9 = new com.google.android.exoplayer2.ParserException;
        r0 = "Atom size less than header length (unsupported).";
        r9.<init>(r0);
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor.readAtomHeader(com.google.android.exoplayer2.extractor.ExtractorInput):boolean");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:17:0x004e in {6, 7, 10, 14, 16} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private void readEncryptionData(com.google.android.exoplayer2.extractor.ExtractorInput r9) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r8 = this;
        r0 = r8.trackBundles;
        r0 = r0.size();
        r1 = 0;
        r2 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r4 = 0;
        if (r4 >= r0) goto L_0x002f;
        r5 = r8.trackBundles;
        r5 = r5.valueAt(r4);
        r5 = (com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor.TrackBundle) r5;
        r5 = r5.fragment;
        r6 = r5.sampleEncryptionDataNeedsFill;
        if (r6 == 0) goto L_0x002c;
        r5 = r5.auxiliaryDataPosition;
        r7 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1));
        if (r7 >= 0) goto L_0x002c;
        r1 = r8.trackBundles;
        r1 = r1.valueAt(r4);
        r1 = (com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor.TrackBundle) r1;
        r2 = r5;
        r4 = r4 + 1;
        goto L_0x000d;
        if (r1 != 0) goto L_0x0035;
        r9 = 3;
        r8.parserState = r9;
        return;
        r4 = r9.getPosition();
        r2 = r2 - r4;
        r0 = (int) r2;
        if (r0 < 0) goto L_0x0046;
        r9.skipFully(r0);
        r0 = r1.fragment;
        r0.fillEncryptionData(r9);
        return;
        r9 = new com.google.android.exoplayer2.ParserException;
        r0 = "Offset to encryption data was negative.";
        r9.<init>(r0);
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor.readEncryptionData(com.google.android.exoplayer2.extractor.ExtractorInput):void");
    }

    /* Access modifiers changed, original: protected */
    public Track modifyTrack(Track track) {
        return track;
    }

    public void release() {
    }

    public FragmentedMp4Extractor() {
        this(0);
    }

    public FragmentedMp4Extractor(int i) {
        this(i, null);
    }

    public FragmentedMp4Extractor(int i, TimestampAdjuster timestampAdjuster) {
        this(i, timestampAdjuster, null, null);
    }

    public FragmentedMp4Extractor(int i, TimestampAdjuster timestampAdjuster, Track track, DrmInitData drmInitData) {
        this(i, timestampAdjuster, track, drmInitData, Collections.emptyList());
    }

    public FragmentedMp4Extractor(int i, TimestampAdjuster timestampAdjuster, Track track, DrmInitData drmInitData, List<Format> list) {
        this(i, timestampAdjuster, track, drmInitData, list, null);
    }

    public FragmentedMp4Extractor(int i, TimestampAdjuster timestampAdjuster, Track track, DrmInitData drmInitData, List<Format> list, TrackOutput trackOutput) {
        this.flags = i | (track != null ? 8 : 0);
        this.timestampAdjuster = timestampAdjuster;
        this.sideloadedTrack = track;
        this.sideloadedDrmInitData = drmInitData;
        this.closedCaptionFormats = Collections.unmodifiableList(list);
        this.additionalEmsgTrackOutput = trackOutput;
        this.atomHeader = new ParsableByteArray(16);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalPrefix = new ParsableByteArray(5);
        this.nalBuffer = new ParsableByteArray();
        this.extendedTypeScratch = new byte[16];
        this.containerAtoms = new ArrayDeque();
        this.pendingMetadataSampleInfos = new ArrayDeque();
        this.trackBundles = new SparseArray();
        this.durationUs = -9223372036854775807L;
        this.pendingSeekTimeUs = -9223372036854775807L;
        this.segmentIndexEarliestPresentationTimeUs = -9223372036854775807L;
        enterReadingAtomHeaderState();
    }

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        return Sniffer.sniffFragmented(extractorInput);
    }

    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        Track track = this.sideloadedTrack;
        if (track != null) {
            TrackBundle trackBundle = new TrackBundle(extractorOutput.track(0, track.type));
            trackBundle.init(this.sideloadedTrack, new DefaultSampleValues(0, 0, 0, 0));
            this.trackBundles.put(0, trackBundle);
            maybeInitExtraTracks();
            this.extractorOutput.endTracks();
        }
    }

    public void seek(long j, long j2) {
        int size = this.trackBundles.size();
        for (int i = 0; i < size; i++) {
            ((TrackBundle) this.trackBundles.valueAt(i)).reset();
        }
        this.pendingMetadataSampleInfos.clear();
        this.pendingMetadataSampleBytes = 0;
        this.pendingSeekTimeUs = j2;
        this.containerAtoms.clear();
        enterReadingAtomHeaderState();
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        while (true) {
            int i = this.parserState;
            if (i != 0) {
                if (i == 1) {
                    readAtomPayload(extractorInput);
                } else if (i == 2) {
                    readEncryptionData(extractorInput);
                } else if (readSample(extractorInput)) {
                    return 0;
                }
            } else if (!readAtomHeader(extractorInput)) {
                return -1;
            }
        }
    }

    private void enterReadingAtomHeaderState() {
        this.parserState = 0;
        this.atomHeaderBytesRead = 0;
    }

    private void readAtomPayload(ExtractorInput extractorInput) throws IOException, InterruptedException {
        int i = ((int) this.atomSize) - this.atomHeaderBytesRead;
        ParsableByteArray parsableByteArray = this.atomData;
        if (parsableByteArray != null) {
            extractorInput.readFully(parsableByteArray.data, 8, i);
            onLeafAtomRead(new LeafAtom(this.atomType, this.atomData), extractorInput.getPosition());
        } else {
            extractorInput.skipFully(i);
        }
        processAtomEnded(extractorInput.getPosition());
    }

    private void processAtomEnded(long j) throws ParserException {
        while (!this.containerAtoms.isEmpty() && ((ContainerAtom) this.containerAtoms.peek()).endPosition == j) {
            onContainerAtomRead((ContainerAtom) this.containerAtoms.pop());
        }
        enterReadingAtomHeaderState();
    }

    private void onLeafAtomRead(LeafAtom leafAtom, long j) throws ParserException {
        if (this.containerAtoms.isEmpty()) {
            int i = leafAtom.type;
            if (i == Atom.TYPE_sidx) {
                Pair parseSidx = parseSidx(leafAtom.data, j);
                this.segmentIndexEarliestPresentationTimeUs = ((Long) parseSidx.first).longValue();
                this.extractorOutput.seekMap((SeekMap) parseSidx.second);
                this.haveOutputSeekMap = true;
                return;
            } else if (i == Atom.TYPE_emsg) {
                onEmsgLeafAtomRead(leafAtom.data);
                return;
            } else {
                return;
            }
        }
        ((ContainerAtom) this.containerAtoms.peek()).add(leafAtom);
    }

    private void onContainerAtomRead(ContainerAtom containerAtom) throws ParserException {
        int i = containerAtom.type;
        if (i == Atom.TYPE_moov) {
            onMoovContainerAtomRead(containerAtom);
        } else if (i == Atom.TYPE_moof) {
            onMoofContainerAtomRead(containerAtom);
        } else if (!this.containerAtoms.isEmpty()) {
            ((ContainerAtom) this.containerAtoms.peek()).add(containerAtom);
        }
    }

    private void onMoovContainerAtomRead(ContainerAtom containerAtom) throws ParserException {
        ContainerAtom containerAtom2 = containerAtom;
        boolean z = true;
        int i = 0;
        Assertions.checkState(this.sideloadedTrack == null, "Unexpected moov box.");
        DrmInitData drmInitData = this.sideloadedDrmInitData;
        if (drmInitData == null) {
            drmInitData = getDrmInitDataFromAtoms(containerAtom2.leafChildren);
        }
        ContainerAtom containerAtomOfType = containerAtom2.getContainerAtomOfType(Atom.TYPE_mvex);
        SparseArray sparseArray = new SparseArray();
        int size = containerAtomOfType.leafChildren.size();
        long j = -9223372036854775807L;
        for (int i2 = 0; i2 < size; i2++) {
            LeafAtom leafAtom = (LeafAtom) containerAtomOfType.leafChildren.get(i2);
            int i3 = leafAtom.type;
            if (i3 == Atom.TYPE_trex) {
                Pair parseTrex = parseTrex(leafAtom.data);
                sparseArray.put(((Integer) parseTrex.first).intValue(), parseTrex.second);
            } else if (i3 == Atom.TYPE_mehd) {
                j = parseMehd(leafAtom.data);
            }
        }
        SparseArray sparseArray2 = new SparseArray();
        int size2 = containerAtom2.containerChildren.size();
        int i4 = 0;
        while (i4 < size2) {
            int i5;
            int i6;
            containerAtomOfType = (ContainerAtom) containerAtom2.containerChildren.get(i4);
            if (containerAtomOfType.type == Atom.TYPE_trak) {
                i5 = i4;
                i6 = size2;
                Track parseTrak = AtomParsers.parseTrak(containerAtomOfType, containerAtom2.getLeafAtomOfType(Atom.TYPE_mvhd), j, drmInitData, (this.flags & 16) != 0, false);
                modifyTrack(parseTrak);
                if (parseTrak != null) {
                    sparseArray2.put(parseTrak.f19id, parseTrak);
                }
            } else {
                i5 = i4;
                i6 = size2;
            }
            i4 = i5 + 1;
            size2 = i6;
        }
        int size3 = sparseArray2.size();
        Track track;
        if (this.trackBundles.size() == 0) {
            while (i < size3) {
                track = (Track) sparseArray2.valueAt(i);
                TrackBundle trackBundle = new TrackBundle(this.extractorOutput.track(i, track.type));
                trackBundle.init(track, getDefaultSampleValues(sparseArray, track.f19id));
                this.trackBundles.put(track.f19id, trackBundle);
                this.durationUs = Math.max(this.durationUs, track.durationUs);
                i++;
            }
            maybeInitExtraTracks();
            this.extractorOutput.endTracks();
            return;
        }
        if (this.trackBundles.size() != size3) {
            z = false;
        }
        Assertions.checkState(z);
        while (i < size3) {
            track = (Track) sparseArray2.valueAt(i);
            ((TrackBundle) this.trackBundles.get(track.f19id)).init(track, getDefaultSampleValues(sparseArray, track.f19id));
            i++;
        }
    }

    private DefaultSampleValues getDefaultSampleValues(SparseArray<DefaultSampleValues> sparseArray, int i) {
        if (sparseArray.size() == 1) {
            return (DefaultSampleValues) sparseArray.valueAt(0);
        }
        Object obj = sparseArray.get(i);
        Assertions.checkNotNull(obj);
        return (DefaultSampleValues) obj;
    }

    private void onMoofContainerAtomRead(ContainerAtom containerAtom) throws ParserException {
        DrmInitData drmInitData;
        parseMoof(containerAtom, this.trackBundles, this.flags, this.extendedTypeScratch);
        if (this.sideloadedDrmInitData != null) {
            drmInitData = null;
        } else {
            drmInitData = getDrmInitDataFromAtoms(containerAtom.leafChildren);
        }
        if (drmInitData != null) {
            int size = this.trackBundles.size();
            for (int i = 0; i < size; i++) {
                ((TrackBundle) this.trackBundles.valueAt(i)).updateDrmInitData(drmInitData);
            }
        }
        if (this.pendingSeekTimeUs != -9223372036854775807L) {
            int size2 = this.trackBundles.size();
            for (int i2 = 0; i2 < size2; i2++) {
                ((TrackBundle) this.trackBundles.valueAt(i2)).seek(this.pendingSeekTimeUs);
            }
            this.pendingSeekTimeUs = -9223372036854775807L;
        }
    }

    private void maybeInitExtraTracks() {
        TrackOutput trackOutput;
        int i = 0;
        if (this.emsgTrackOutputs == null) {
            int i2;
            this.emsgTrackOutputs = new TrackOutput[2];
            trackOutput = this.additionalEmsgTrackOutput;
            if (trackOutput != null) {
                this.emsgTrackOutputs[0] = trackOutput;
                i2 = 1;
            } else {
                i2 = 0;
            }
            if ((this.flags & 4) != 0) {
                int i3 = i2 + 1;
                this.emsgTrackOutputs[i2] = this.extractorOutput.track(this.trackBundles.size(), 4);
                i2 = i3;
            }
            this.emsgTrackOutputs = (TrackOutput[]) Arrays.copyOf(this.emsgTrackOutputs, i2);
            for (TrackOutput format : this.emsgTrackOutputs) {
                format.format(EMSG_FORMAT);
            }
        }
        if (this.cea608TrackOutputs == null) {
            this.cea608TrackOutputs = new TrackOutput[this.closedCaptionFormats.size()];
            while (i < this.cea608TrackOutputs.length) {
                trackOutput = this.extractorOutput.track((this.trackBundles.size() + 1) + i, 3);
                trackOutput.format((Format) this.closedCaptionFormats.get(i));
                this.cea608TrackOutputs[i] = trackOutput;
                i++;
            }
        }
    }

    private void onEmsgLeafAtomRead(ParsableByteArray parsableByteArray) {
        TrackOutput[] trackOutputArr = this.emsgTrackOutputs;
        if (trackOutputArr != null && trackOutputArr.length != 0) {
            parsableByteArray.setPosition(12);
            int bytesLeft = parsableByteArray.bytesLeft();
            parsableByteArray.readNullTerminatedString();
            parsableByteArray.readNullTerminatedString();
            long scaleLargeTimestamp = Util.scaleLargeTimestamp(parsableByteArray.readUnsignedInt(), 1000000, parsableByteArray.readUnsignedInt());
            for (TrackOutput trackOutput : this.emsgTrackOutputs) {
                parsableByteArray.setPosition(12);
                trackOutput.sampleData(parsableByteArray, bytesLeft);
            }
            long j = this.segmentIndexEarliestPresentationTimeUs;
            if (j != -9223372036854775807L) {
                j += scaleLargeTimestamp;
                TimestampAdjuster timestampAdjuster = this.timestampAdjuster;
                long adjustSampleTimestamp = timestampAdjuster != null ? timestampAdjuster.adjustSampleTimestamp(j) : j;
                for (TrackOutput sampleMetadata : this.emsgTrackOutputs) {
                    sampleMetadata.sampleMetadata(adjustSampleTimestamp, 1, bytesLeft, 0, null);
                }
                return;
            }
            this.pendingMetadataSampleInfos.addLast(new MetadataSampleInfo(scaleLargeTimestamp, bytesLeft));
            this.pendingMetadataSampleBytes += bytesLeft;
        }
    }

    private static Pair<Integer, DefaultSampleValues> parseTrex(ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(12);
        return Pair.create(Integer.valueOf(parsableByteArray.readInt()), new DefaultSampleValues(parsableByteArray.readUnsignedIntToInt() - 1, parsableByteArray.readUnsignedIntToInt(), parsableByteArray.readUnsignedIntToInt(), parsableByteArray.readInt()));
    }

    private static long parseMehd(ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(8);
        return Atom.parseFullAtomVersion(parsableByteArray.readInt()) == 0 ? parsableByteArray.readUnsignedInt() : parsableByteArray.readUnsignedLongToLong();
    }

    private static void parseMoof(ContainerAtom containerAtom, SparseArray<TrackBundle> sparseArray, int i, byte[] bArr) throws ParserException {
        int size = containerAtom.containerChildren.size();
        for (int i2 = 0; i2 < size; i2++) {
            ContainerAtom containerAtom2 = (ContainerAtom) containerAtom.containerChildren.get(i2);
            if (containerAtom2.type == Atom.TYPE_traf) {
                parseTraf(containerAtom2, sparseArray, i, bArr);
            }
        }
    }

    private static void parseTraf(ContainerAtom containerAtom, SparseArray<TrackBundle> sparseArray, int i, byte[] bArr) throws ParserException {
        TrackBundle parseTfhd = parseTfhd(containerAtom.getLeafAtomOfType(Atom.TYPE_tfhd).data, sparseArray);
        if (parseTfhd != null) {
            TrackFragment trackFragment = parseTfhd.fragment;
            long j = trackFragment.nextFragmentDecodeTime;
            parseTfhd.reset();
            if (containerAtom.getLeafAtomOfType(Atom.TYPE_tfdt) != null && (i & 2) == 0) {
                j = parseTfdt(containerAtom.getLeafAtomOfType(Atom.TYPE_tfdt).data);
            }
            parseTruns(containerAtom, parseTfhd, j, i);
            TrackEncryptionBox sampleDescriptionEncryptionBox = parseTfhd.track.getSampleDescriptionEncryptionBox(trackFragment.header.sampleDescriptionIndex);
            LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_saiz);
            if (leafAtomOfType != null) {
                parseSaiz(sampleDescriptionEncryptionBox, leafAtomOfType.data, trackFragment);
            }
            leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_saio);
            if (leafAtomOfType != null) {
                parseSaio(leafAtomOfType.data, trackFragment);
            }
            leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_senc);
            if (leafAtomOfType != null) {
                parseSenc(leafAtomOfType.data, trackFragment);
            }
            leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_sbgp);
            LeafAtom leafAtomOfType2 = containerAtom.getLeafAtomOfType(Atom.TYPE_sgpd);
            if (!(leafAtomOfType == null || leafAtomOfType2 == null)) {
                parseSgpd(leafAtomOfType.data, leafAtomOfType2.data, sampleDescriptionEncryptionBox != null ? sampleDescriptionEncryptionBox.schemeType : null, trackFragment);
            }
            int size = containerAtom.leafChildren.size();
            for (i = 0; i < size; i++) {
                leafAtomOfType2 = (LeafAtom) containerAtom.leafChildren.get(i);
                if (leafAtomOfType2.type == Atom.TYPE_uuid) {
                    parseUuid(leafAtomOfType2.data, trackFragment, bArr);
                }
            }
        }
    }

    private static void parseTruns(ContainerAtom containerAtom, TrackBundle trackBundle, long j, int i) {
        List list = containerAtom.leafChildren;
        int size = list.size();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < size; i5++) {
            LeafAtom leafAtom = (LeafAtom) list.get(i5);
            if (leafAtom.type == Atom.TYPE_trun) {
                ParsableByteArray parsableByteArray = leafAtom.data;
                parsableByteArray.setPosition(12);
                int readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
                if (readUnsignedIntToInt > 0) {
                    i4 += readUnsignedIntToInt;
                    i3++;
                }
            }
        }
        trackBundle.currentTrackRunIndex = 0;
        trackBundle.currentSampleInTrackRun = 0;
        trackBundle.currentSampleIndex = 0;
        trackBundle.fragment.initTables(i3, i4);
        i3 = 0;
        int i6 = 0;
        while (i2 < size) {
            LeafAtom leafAtom2 = (LeafAtom) list.get(i2);
            if (leafAtom2.type == Atom.TYPE_trun) {
                int i7 = i3 + 1;
                i6 = parseTrun(trackBundle, i3, j, i, leafAtom2.data, i6);
                i3 = i7;
            }
            i2++;
        }
    }

    private static void parseSaio(ParsableByteArray parsableByteArray, TrackFragment trackFragment) throws ParserException {
        parsableByteArray.setPosition(8);
        int readInt = parsableByteArray.readInt();
        if ((Atom.parseFullAtomFlags(readInt) & 1) == 1) {
            parsableByteArray.skipBytes(8);
        }
        int readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        if (readUnsignedIntToInt == 1) {
            long readUnsignedInt;
            readUnsignedIntToInt = Atom.parseFullAtomVersion(readInt);
            long j = trackFragment.auxiliaryDataPosition;
            if (readUnsignedIntToInt == 0) {
                readUnsignedInt = parsableByteArray.readUnsignedInt();
            } else {
                readUnsignedInt = parsableByteArray.readUnsignedLongToLong();
            }
            trackFragment.auxiliaryDataPosition = j + readUnsignedInt;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected saio entry count: ");
        stringBuilder.append(readUnsignedIntToInt);
        throw new ParserException(stringBuilder.toString());
    }

    private static TrackBundle parseTfhd(ParsableByteArray parsableByteArray, SparseArray<TrackBundle> sparseArray) {
        parsableByteArray.setPosition(8);
        int parseFullAtomFlags = Atom.parseFullAtomFlags(parsableByteArray.readInt());
        TrackBundle trackBundle = getTrackBundle(sparseArray, parsableByteArray.readInt());
        if (trackBundle == null) {
            return null;
        }
        if ((parseFullAtomFlags & 1) != 0) {
            long readUnsignedLongToLong = parsableByteArray.readUnsignedLongToLong();
            TrackFragment trackFragment = trackBundle.fragment;
            trackFragment.dataPosition = readUnsignedLongToLong;
            trackFragment.auxiliaryDataPosition = readUnsignedLongToLong;
        }
        DefaultSampleValues defaultSampleValues = trackBundle.defaultSampleValues;
        trackBundle.fragment.header = new DefaultSampleValues((parseFullAtomFlags & 2) != 0 ? parsableByteArray.readUnsignedIntToInt() - 1 : defaultSampleValues.sampleDescriptionIndex, (parseFullAtomFlags & 8) != 0 ? parsableByteArray.readUnsignedIntToInt() : defaultSampleValues.duration, (parseFullAtomFlags & 16) != 0 ? parsableByteArray.readUnsignedIntToInt() : defaultSampleValues.size, (parseFullAtomFlags & 32) != 0 ? parsableByteArray.readUnsignedIntToInt() : defaultSampleValues.flags);
        return trackBundle;
    }

    private static TrackBundle getTrackBundle(SparseArray<TrackBundle> sparseArray, int i) {
        if (sparseArray.size() == 1) {
            return (TrackBundle) sparseArray.valueAt(0);
        }
        return (TrackBundle) sparseArray.get(i);
    }

    private static long parseTfdt(ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(8);
        return Atom.parseFullAtomVersion(parsableByteArray.readInt()) == 1 ? parsableByteArray.readUnsignedLongToLong() : parsableByteArray.readUnsignedInt();
    }

    private static int parseTrun(TrackBundle trackBundle, int i, long j, int i2, ParsableByteArray parsableByteArray, int i3) {
        boolean[] zArr;
        long[] jArr;
        long j2;
        TrackBundle trackBundle2 = trackBundle;
        parsableByteArray.setPosition(8);
        int parseFullAtomFlags = Atom.parseFullAtomFlags(parsableByteArray.readInt());
        Track track = trackBundle2.track;
        TrackFragment trackFragment = trackBundle2.fragment;
        DefaultSampleValues defaultSampleValues = trackFragment.header;
        trackFragment.trunLength[i] = parsableByteArray.readUnsignedIntToInt();
        long[] jArr2 = trackFragment.trunDataPosition;
        jArr2[i] = trackFragment.dataPosition;
        if ((parseFullAtomFlags & 1) != 0) {
            jArr2[i] = jArr2[i] + ((long) parsableByteArray.readInt());
        }
        Object obj = (parseFullAtomFlags & 4) != 0 ? 1 : null;
        int i4 = defaultSampleValues.flags;
        if (obj != null) {
            i4 = parsableByteArray.readUnsignedIntToInt();
        }
        Object obj2 = (parseFullAtomFlags & 256) != 0 ? 1 : null;
        Object obj3 = (parseFullAtomFlags & 512) != 0 ? 1 : null;
        Object obj4 = (parseFullAtomFlags & 1024) != 0 ? 1 : null;
        Object obj5 = (parseFullAtomFlags & 2048) != 0 ? 1 : null;
        long[] jArr3 = track.editListDurations;
        long j3 = 0;
        if (jArr3 != null && jArr3.length == 1 && jArr3[0] == 0) {
            j3 = Util.scaleLargeTimestamp(track.editListMediaTimes[0], 1000, track.timescale);
        }
        int[] iArr = trackFragment.sampleSizeTable;
        int[] iArr2 = trackFragment.sampleCompositionTimeOffsetTable;
        long[] jArr4 = trackFragment.sampleDecodingTimeTable;
        boolean[] zArr2 = trackFragment.sampleIsSyncFrameTable;
        int i5 = i4;
        Object obj6 = (track.type != 2 || (i2 & 1) == 0) ? null : 1;
        int i6 = i3 + trackFragment.trunLength[i];
        long j4 = j3;
        boolean[] zArr3 = zArr2;
        long j5 = track.timescale;
        if (i > 0) {
            zArr = zArr3;
            jArr = jArr4;
            j2 = trackFragment.nextFragmentDecodeTime;
        } else {
            zArr = zArr3;
            jArr = jArr4;
            j2 = j;
        }
        long j6 = j2;
        int i7 = i3;
        while (i7 < i6) {
            Object obj7;
            int readUnsignedIntToInt;
            Object obj8;
            int i8;
            Object obj9;
            Object obj10;
            Object obj11;
            int readUnsignedIntToInt2 = obj2 != null ? parsableByteArray.readUnsignedIntToInt() : defaultSampleValues.duration;
            if (obj3 != null) {
                obj7 = obj2;
                readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
            } else {
                obj7 = obj2;
                readUnsignedIntToInt = defaultSampleValues.size;
            }
            if (i7 == 0 && obj != null) {
                obj8 = obj;
                i8 = i5;
            } else if (obj4 != null) {
                obj8 = obj;
                i8 = parsableByteArray.readInt();
            } else {
                obj8 = obj;
                i8 = defaultSampleValues.flags;
            }
            if (obj5 != null) {
                obj9 = obj5;
                obj10 = obj3;
                obj11 = obj4;
                iArr2[i7] = (int) ((((long) parsableByteArray.readInt()) * 1000) / j5);
            } else {
                obj9 = obj5;
                obj10 = obj3;
                obj11 = obj4;
                iArr2[i7] = 0;
            }
            jArr[i7] = Util.scaleLargeTimestamp(j6, 1000, j5) - j4;
            iArr[i7] = readUnsignedIntToInt;
            boolean z = ((i8 >> 16) & 1) == 0 && (obj6 == null || i7 == 0);
            zArr[i7] = z;
            i7++;
            j6 += (long) readUnsignedIntToInt2;
            obj2 = obj7;
            obj = obj8;
            obj5 = obj9;
            obj3 = obj10;
            obj4 = obj11;
            i6 = i6;
        }
        int i9 = i6;
        trackFragment.nextFragmentDecodeTime = j6;
        return i9;
    }

    private static void parseUuid(ParsableByteArray parsableByteArray, TrackFragment trackFragment, byte[] bArr) throws ParserException {
        parsableByteArray.setPosition(8);
        parsableByteArray.readBytes(bArr, 0, 16);
        if (Arrays.equals(bArr, PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE)) {
            parseSenc(parsableByteArray, 16, trackFragment);
        }
    }

    private static void parseSenc(ParsableByteArray parsableByteArray, TrackFragment trackFragment) throws ParserException {
        parseSenc(parsableByteArray, 0, trackFragment);
    }

    private static void parseSenc(ParsableByteArray parsableByteArray, int i, TrackFragment trackFragment) throws ParserException {
        parsableByteArray.setPosition(i + 8);
        i = Atom.parseFullAtomFlags(parsableByteArray.readInt());
        if ((i & 1) == 0) {
            boolean z = (i & 2) != 0;
            int readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
            if (readUnsignedIntToInt == trackFragment.sampleCount) {
                Arrays.fill(trackFragment.sampleHasSubsampleEncryptionTable, 0, readUnsignedIntToInt, z);
                trackFragment.initEncryptionData(parsableByteArray.bytesLeft());
                trackFragment.fillEncryptionData(parsableByteArray);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Length mismatch: ");
            stringBuilder.append(readUnsignedIntToInt);
            stringBuilder.append(", ");
            stringBuilder.append(trackFragment.sampleCount);
            throw new ParserException(stringBuilder.toString());
        }
        throw new ParserException("Overriding TrackEncryptionBox parameters is unsupported.");
    }

    private static void parseSgpd(ParsableByteArray parsableByteArray, ParsableByteArray parsableByteArray2, String str, TrackFragment trackFragment) throws ParserException {
        parsableByteArray.setPosition(8);
        int readInt = parsableByteArray.readInt();
        if (parsableByteArray.readInt() == SAMPLE_GROUP_TYPE_seig) {
            if (Atom.parseFullAtomVersion(readInt) == 1) {
                parsableByteArray.skipBytes(4);
            }
            if (parsableByteArray.readInt() == 1) {
                parsableByteArray2.setPosition(8);
                int readInt2 = parsableByteArray2.readInt();
                if (parsableByteArray2.readInt() == SAMPLE_GROUP_TYPE_seig) {
                    readInt2 = Atom.parseFullAtomVersion(readInt2);
                    if (readInt2 == 1) {
                        if (parsableByteArray2.readUnsignedInt() == 0) {
                            throw new ParserException("Variable length description in sgpd found (unsupported)");
                        }
                    } else if (readInt2 >= 2) {
                        parsableByteArray2.skipBytes(4);
                    }
                    if (parsableByteArray2.readUnsignedInt() == 1) {
                        parsableByteArray2.skipBytes(1);
                        readInt2 = parsableByteArray2.readUnsignedByte();
                        int i = (readInt2 & 240) >> 4;
                        int i2 = readInt2 & 15;
                        boolean z = parsableByteArray2.readUnsignedByte() == 1;
                        if (z) {
                            byte[] bArr;
                            int readUnsignedByte = parsableByteArray2.readUnsignedByte();
                            byte[] bArr2 = new byte[16];
                            parsableByteArray2.readBytes(bArr2, 0, bArr2.length);
                            if (z && readUnsignedByte == 0) {
                                readInt2 = parsableByteArray2.readUnsignedByte();
                                byte[] bArr3 = new byte[readInt2];
                                parsableByteArray2.readBytes(bArr3, 0, readInt2);
                                bArr = bArr3;
                            } else {
                                bArr = null;
                            }
                            trackFragment.definesEncryptionData = true;
                            trackFragment.trackEncryptionBox = new TrackEncryptionBox(z, str, readUnsignedByte, bArr2, i, i2, bArr);
                            return;
                        }
                        return;
                    }
                    throw new ParserException("Entry count in sgpd != 1 (unsupported).");
                }
                return;
            }
            throw new ParserException("Entry count in sbgp != 1 (unsupported).");
        }
    }

    private static Pair<Long, ChunkIndex> parseSidx(ParsableByteArray parsableByteArray, long j) throws ParserException {
        long readUnsignedInt;
        long readUnsignedInt2;
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        parsableByteArray2.setPosition(8);
        int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        parsableByteArray2.skipBytes(4);
        long readUnsignedInt3 = parsableByteArray.readUnsignedInt();
        if (parseFullAtomVersion == 0) {
            readUnsignedInt = parsableByteArray.readUnsignedInt();
            readUnsignedInt2 = parsableByteArray.readUnsignedInt();
        } else {
            readUnsignedInt = parsableByteArray.readUnsignedLongToLong();
            readUnsignedInt2 = parsableByteArray.readUnsignedLongToLong();
        }
        long j2 = readUnsignedInt;
        long j3 = j + readUnsignedInt2;
        long scaleLargeTimestamp = Util.scaleLargeTimestamp(j2, 1000000, readUnsignedInt3);
        parsableByteArray2.skipBytes(2);
        parseFullAtomVersion = parsableByteArray.readUnsignedShort();
        int[] iArr = new int[parseFullAtomVersion];
        long[] jArr = new long[parseFullAtomVersion];
        long[] jArr2 = new long[parseFullAtomVersion];
        long[] jArr3 = new long[parseFullAtomVersion];
        long j4 = j2;
        readUnsignedInt = scaleLargeTimestamp;
        int i = 0;
        while (i < parseFullAtomVersion) {
            int readInt = parsableByteArray.readInt();
            if ((readInt & Integer.MIN_VALUE) == 0) {
                long readUnsignedInt4 = parsableByteArray.readUnsignedInt();
                iArr[i] = readInt & Integer.MAX_VALUE;
                jArr[i] = j3;
                jArr3[i] = readUnsignedInt;
                j4 += readUnsignedInt4;
                long[] jArr4 = jArr2;
                long[] jArr5 = jArr3;
                int i2 = parseFullAtomVersion;
                int[] iArr2 = iArr;
                long[] jArr6 = jArr;
                readUnsignedInt = Util.scaleLargeTimestamp(j4, 1000000, readUnsignedInt3);
                jArr4[i] = readUnsignedInt - jArr5[i];
                parsableByteArray2.skipBytes(4);
                j3 += (long) iArr2[i];
                i++;
                iArr = iArr2;
                jArr3 = jArr5;
                jArr2 = jArr4;
                jArr = jArr6;
                parseFullAtomVersion = i2;
            } else {
                throw new ParserException("Unhandled indirect reference");
            }
        }
        return Pair.create(Long.valueOf(scaleLargeTimestamp), new ChunkIndex(iArr, jArr, jArr2, jArr3));
    }

    private boolean readSample(ExtractorInput extractorInput) throws IOException, InterruptedException {
        TrackBundle nextFragmentRun;
        int position;
        int i;
        CryptoData cryptoData;
        ExtractorInput extractorInput2 = extractorInput;
        int i2 = 4;
        int i3 = 1;
        int i4 = 0;
        if (this.parserState == 3) {
            if (this.currentTrackBundle == null) {
                nextFragmentRun = getNextFragmentRun(this.trackBundles);
                if (nextFragmentRun == null) {
                    position = (int) (this.endOfMdatPosition - extractorInput.getPosition());
                    if (position >= 0) {
                        extractorInput2.skipFully(position);
                        enterReadingAtomHeaderState();
                        return false;
                    }
                    throw new ParserException("Offset to end of mdat was negative.");
                }
                int position2 = (int) (nextFragmentRun.fragment.trunDataPosition[nextFragmentRun.currentTrackRunIndex] - extractorInput.getPosition());
                if (position2 < 0) {
                    Log.m18w("FragmentedMp4Extractor", "Ignoring negative offset to sample data.");
                    position2 = 0;
                }
                extractorInput2.skipFully(position2);
                this.currentTrackBundle = nextFragmentRun;
            }
            nextFragmentRun = this.currentTrackBundle;
            int[] iArr = nextFragmentRun.fragment.sampleSizeTable;
            int i5 = nextFragmentRun.currentSampleIndex;
            this.sampleSize = iArr[i5];
            if (i5 < nextFragmentRun.firstSampleToOutputIndex) {
                extractorInput2.skipFully(this.sampleSize);
                this.currentTrackBundle.skipSampleEncryptionData();
                if (!this.currentTrackBundle.next()) {
                    this.currentTrackBundle = null;
                }
                this.parserState = 3;
                return true;
            }
            if (nextFragmentRun.track.sampleTransformation == 1) {
                this.sampleSize -= 8;
                extractorInput2.skipFully(8);
            }
            this.sampleBytesWritten = this.currentTrackBundle.outputSampleEncryptionData();
            this.sampleSize += this.sampleBytesWritten;
            this.parserState = 4;
            this.sampleCurrentNalBytesRemaining = 0;
        }
        nextFragmentRun = this.currentTrackBundle;
        TrackFragment trackFragment = nextFragmentRun.fragment;
        Track track = nextFragmentRun.track;
        TrackOutput trackOutput = nextFragmentRun.output;
        int i6 = nextFragmentRun.currentSampleIndex;
        long samplePresentationTime = trackFragment.getSamplePresentationTime(i6) * 1000;
        TimestampAdjuster timestampAdjuster = this.timestampAdjuster;
        if (timestampAdjuster != null) {
            samplePresentationTime = timestampAdjuster.adjustSampleTimestamp(samplePresentationTime);
        }
        long j = samplePresentationTime;
        int i7 = track.nalUnitLengthFieldLength;
        int i8;
        if (i7 == 0) {
            while (true) {
                position = this.sampleBytesWritten;
                i8 = this.sampleSize;
                if (position >= i8) {
                    break;
                }
                this.sampleBytesWritten += trackOutput.sampleData(extractorInput2, i8 - position, false);
            }
        } else {
            byte[] bArr = this.nalPrefix.data;
            bArr[0] = (byte) 0;
            bArr[1] = (byte) 0;
            bArr[2] = (byte) 0;
            i = i7 + 1;
            i7 = 4 - i7;
            while (this.sampleBytesWritten < this.sampleSize) {
                position = this.sampleCurrentNalBytesRemaining;
                if (position == 0) {
                    extractorInput2.readFully(bArr, i7, i);
                    this.nalPrefix.setPosition(i4);
                    this.sampleCurrentNalBytesRemaining = this.nalPrefix.readUnsignedIntToInt() - i3;
                    this.nalStartCode.setPosition(i4);
                    trackOutput.sampleData(this.nalStartCode, i2);
                    trackOutput.sampleData(this.nalPrefix, i3);
                    boolean z = this.cea608TrackOutputs.length > 0 && NalUnitUtil.isNalUnitSei(track.format.sampleMimeType, bArr[i2]);
                    this.processSeiNalUnitPayload = z;
                    this.sampleBytesWritten += 5;
                    this.sampleSize += i7;
                } else {
                    if (this.processSeiNalUnitPayload) {
                        this.nalBuffer.reset(position);
                        extractorInput2.readFully(this.nalBuffer.data, i4, this.sampleCurrentNalBytesRemaining);
                        trackOutput.sampleData(this.nalBuffer, this.sampleCurrentNalBytesRemaining);
                        position = this.sampleCurrentNalBytesRemaining;
                        ParsableByteArray parsableByteArray = this.nalBuffer;
                        i8 = NalUnitUtil.unescapeStream(parsableByteArray.data, parsableByteArray.limit());
                        this.nalBuffer.setPosition(MimeTypes.VIDEO_H265.equals(track.format.sampleMimeType));
                        this.nalBuffer.setLimit(i8);
                        CeaUtil.consume(j, this.nalBuffer, this.cea608TrackOutputs);
                    } else {
                        position = trackOutput.sampleData(extractorInput2, position, false);
                    }
                    this.sampleBytesWritten += position;
                    this.sampleCurrentNalBytesRemaining -= position;
                    i2 = 4;
                    i3 = 1;
                    i4 = 0;
                }
            }
        }
        boolean z2 = trackFragment.sampleIsSyncFrameTable[i6];
        TrackEncryptionBox access$100 = this.currentTrackBundle.getEncryptionBoxIfEncrypted();
        if (access$100 != null) {
            i = z2 | 1073741824;
            cryptoData = access$100.cryptoData;
        } else {
            i = z2;
            cryptoData = null;
        }
        long j2 = j;
        trackOutput.sampleMetadata(j, i, this.sampleSize, 0, cryptoData);
        outputPendingMetadataSamples(j2);
        if (!this.currentTrackBundle.next()) {
            this.currentTrackBundle = null;
        }
        this.parserState = 3;
        return true;
    }

    private void outputPendingMetadataSamples(long j) {
        while (!this.pendingMetadataSampleInfos.isEmpty()) {
            MetadataSampleInfo metadataSampleInfo = (MetadataSampleInfo) this.pendingMetadataSampleInfos.removeFirst();
            this.pendingMetadataSampleBytes -= metadataSampleInfo.size;
            long j2 = metadataSampleInfo.presentationTimeDeltaUs + j;
            TimestampAdjuster timestampAdjuster = this.timestampAdjuster;
            if (timestampAdjuster != null) {
                j2 = timestampAdjuster.adjustSampleTimestamp(j2);
            }
            for (TrackOutput sampleMetadata : this.emsgTrackOutputs) {
                sampleMetadata.sampleMetadata(j2, 1, metadataSampleInfo.size, this.pendingMetadataSampleBytes, null);
            }
        }
    }

    private static TrackBundle getNextFragmentRun(SparseArray<TrackBundle> sparseArray) {
        int size = sparseArray.size();
        TrackBundle trackBundle = null;
        long j = TimestampAdjuster.DO_NOT_OFFSET;
        for (int i = 0; i < size; i++) {
            TrackBundle trackBundle2 = (TrackBundle) sparseArray.valueAt(i);
            int i2 = trackBundle2.currentTrackRunIndex;
            TrackFragment trackFragment = trackBundle2.fragment;
            if (i2 != trackFragment.trunCount) {
                long j2 = trackFragment.trunDataPosition[i2];
                if (j2 < j) {
                    trackBundle = trackBundle2;
                    j = j2;
                }
            }
        }
        return trackBundle;
    }

    private static DrmInitData getDrmInitDataFromAtoms(List<LeafAtom> list) {
        int size = list.size();
        List list2 = null;
        for (int i = 0; i < size; i++) {
            LeafAtom leafAtom = (LeafAtom) list.get(i);
            if (leafAtom.type == Atom.TYPE_pssh) {
                if (list2 == null) {
                    list2 = new ArrayList();
                }
                byte[] bArr = leafAtom.data.data;
                UUID parseUuid = PsshAtomUtil.parseUuid(bArr);
                if (parseUuid == null) {
                    Log.m18w("FragmentedMp4Extractor", "Skipped pssh atom (failed to extract uuid)");
                } else {
                    list2.add(new SchemeData(parseUuid, MimeTypes.VIDEO_MP4, bArr));
                }
            }
        }
        if (list2 == null) {
            return null;
        }
        return new DrmInitData(list2);
    }

    private static boolean shouldParseLeafAtom(int i) {
        return i == Atom.TYPE_hdlr || i == Atom.TYPE_mdhd || i == Atom.TYPE_mvhd || i == Atom.TYPE_sidx || i == Atom.TYPE_stsd || i == Atom.TYPE_tfdt || i == Atom.TYPE_tfhd || i == Atom.TYPE_tkhd || i == Atom.TYPE_trex || i == Atom.TYPE_trun || i == Atom.TYPE_pssh || i == Atom.TYPE_saiz || i == Atom.TYPE_saio || i == Atom.TYPE_senc || i == Atom.TYPE_uuid || i == Atom.TYPE_sbgp || i == Atom.TYPE_sgpd || i == Atom.TYPE_elst || i == Atom.TYPE_mehd || i == Atom.TYPE_emsg;
    }

    private static boolean shouldParseContainerAtom(int i) {
        return i == Atom.TYPE_moov || i == Atom.TYPE_trak || i == Atom.TYPE_mdia || i == Atom.TYPE_minf || i == Atom.TYPE_stbl || i == Atom.TYPE_moof || i == Atom.TYPE_traf || i == Atom.TYPE_mvex || i == Atom.TYPE_edts;
    }
}
