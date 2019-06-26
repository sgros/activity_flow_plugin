package com.google.android.exoplayer2.extractor.mkv;

import android.util.SparseArray;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import com.google.android.exoplayer2.C0131C;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekMap.Unseekable;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.TrackOutput.CryptoData;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public final class MatroskaExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = C3333-$$Lambda$MatroskaExtractor$jNXW0tyYIOPE6N2jicocV6rRvBs.INSTANCE;
    private static final byte[] SSA_DIALOGUE_FORMAT = Util.getUtf8Bytes("Format: Start, End, ReadOrder, Layer, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
    private static final byte[] SSA_PREFIX = new byte[]{(byte) 68, (byte) 105, (byte) 97, (byte) 108, (byte) 111, (byte) 103, (byte) 117, (byte) 101, (byte) 58, (byte) 32, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44};
    private static final byte[] SSA_TIMECODE_EMPTY = new byte[]{(byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32};
    private static final byte[] SUBRIP_PREFIX = new byte[]{(byte) 49, (byte) 10, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44, (byte) 48, (byte) 48, (byte) 48, (byte) 32, (byte) 45, (byte) 45, (byte) 62, (byte) 32, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44, (byte) 48, (byte) 48, (byte) 48, (byte) 10};
    private static final byte[] SUBRIP_TIMECODE_EMPTY = new byte[]{(byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32};
    private static final UUID WAVE_SUBFORMAT_PCM = new UUID(72057594037932032L, -9223371306706625679L);
    private long blockDurationUs;
    private int blockFlags;
    private int blockLacingSampleCount;
    private int blockLacingSampleIndex;
    private int[] blockLacingSampleSizes;
    private int blockState;
    private long blockTimeUs;
    private int blockTrackNumber;
    private int blockTrackNumberLength;
    private long clusterTimecodeUs;
    private LongArray cueClusterPositions;
    private LongArray cueTimesUs;
    private long cuesContentPosition;
    private Track currentTrack;
    private long durationTimecode;
    private long durationUs;
    private final ParsableByteArray encryptionInitializationVector;
    private final ParsableByteArray encryptionSubsampleData;
    private ByteBuffer encryptionSubsampleDataBuffer;
    private ExtractorOutput extractorOutput;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private final EbmlReader reader;
    private int sampleBytesRead;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private boolean sampleEncodingHandled;
    private boolean sampleInitializationVectorRead;
    private int samplePartitionCount;
    private boolean samplePartitionCountRead;
    private boolean sampleRead;
    private boolean sampleSeenReferenceBlock;
    private byte sampleSignalByte;
    private boolean sampleSignalByteRead;
    private final ParsableByteArray sampleStrippedBytes;
    private final ParsableByteArray scratch;
    private int seekEntryId;
    private final ParsableByteArray seekEntryIdBytes;
    private long seekEntryPosition;
    private boolean seekForCues;
    private final boolean seekForCuesEnabled;
    private long seekPositionAfterBuildingCues;
    private boolean seenClusterPositionForCurrentCuePoint;
    private long segmentContentPosition;
    private long segmentContentSize;
    private boolean sentSeekMap;
    private final ParsableByteArray subtitleSample;
    private long timecodeScale;
    private final SparseArray<Track> tracks;
    private final VarintReader varintReader;
    private final ParsableByteArray vorbisNumPageSamples;

    private static final class Track {
        public int audioBitDepth;
        public int channelCount;
        public long codecDelayNs;
        public String codecId;
        public byte[] codecPrivate;
        public int colorRange;
        public int colorSpace;
        public int colorTransfer;
        public CryptoData cryptoData;
        public int defaultSampleDurationNs;
        public int displayHeight;
        public int displayUnit;
        public int displayWidth;
        public DrmInitData drmInitData;
        public boolean flagDefault;
        public boolean flagForced;
        public boolean hasColorInfo;
        public boolean hasContentEncryption;
        public int height;
        private String language;
        public int maxContentLuminance;
        public int maxFrameAverageLuminance;
        public float maxMasteringLuminance;
        public float minMasteringLuminance;
        public int nalUnitLengthFieldLength;
        public String name;
        public int number;
        public TrackOutput output;
        public float primaryBChromaticityX;
        public float primaryBChromaticityY;
        public float primaryGChromaticityX;
        public float primaryGChromaticityY;
        public float primaryRChromaticityX;
        public float primaryRChromaticityY;
        public byte[] projectionData;
        public float projectionPosePitch;
        public float projectionPoseRoll;
        public float projectionPoseYaw;
        public int projectionType;
        public int sampleRate;
        public byte[] sampleStrippedBytes;
        public long seekPreRollNs;
        public int stereoMode;
        public TrueHdSampleRechunker trueHdSampleRechunker;
        public int type;
        public float whitePointChromaticityX;
        public float whitePointChromaticityY;
        public int width;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:29:0x0079 in {5, 20, 21, 23, 25, 28} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        private static android.util.Pair<java.lang.String, java.util.List<byte[]>> parseFourCcPrivate(com.google.android.exoplayer2.util.ParsableByteArray r6) throws com.google.android.exoplayer2.ParserException {
            /*
            r0 = 16;
            r6.skipBytes(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = r6.readLittleEndianUnsignedInt();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r2 = 1482049860; // 0x58564944 float:9.4244065E14 double:7.322299212E-315;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r4 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r5 != 0) goto L_0x0019;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r6 = new android.util.Pair;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = "video/3gpp";	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r6.<init>(r0, r4);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            return r6;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r2 = 826496599; // 0x31435657 float:2.8425313E-9 double:4.08343576E-315;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r5 != 0) goto L_0x0062;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = r6.getPosition();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = r0 + 20;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r6 = r6.data;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = r6.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = r1 + -4;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r0 >= r1) goto L_0x005a;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = r6[r0];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r1 != 0) goto L_0x0057;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = r0 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = r6[r1];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r1 != 0) goto L_0x0057;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = r0 + 2;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = r6[r1];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r2 = 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r1 != r2) goto L_0x0057;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = r0 + 3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = r6[r1];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r2 = 15;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r1 != r2) goto L_0x0057;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = r6.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r6 = java.util.Arrays.copyOfRange(r6, r0, r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = new android.util.Pair;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r1 = "video/wvc1";	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r6 = java.util.Collections.singletonList(r6);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0.<init>(r1, r6);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            return r0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = r0 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            goto L_0x0028;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r6 = new com.google.android.exoplayer2.ParserException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = "Failed to find FourCC VC1 initialization data";	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r6.<init>(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            throw r6;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r6 = "MatroskaExtractor";
            r0 = "Unknown FourCC. Setting mimeType to video/x-unknown";
            com.google.android.exoplayer2.util.Log.m18w(r6, r0);
            r6 = new android.util.Pair;
            r0 = "video/x-unknown";
            r6.<init>(r0, r4);
            return r6;
            r6 = new com.google.android.exoplayer2.ParserException;
            r0 = "Error parsing FourCC private data";
            r6.<init>(r0);
            throw r6;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor$Track.parseFourCcPrivate(com.google.android.exoplayer2.util.ParsableByteArray):android.util.Pair");
        }

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:31:0x0071 in {7, 11, 19, 21, 23, 25, 27, 30} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        private static java.util.List<byte[]> parseVorbisCodecPrivate(byte[] r9) throws com.google.android.exoplayer2.ParserException {
            /*
            r0 = "Error parsing vorbis codec private";
            r1 = 0;
            r2 = r9[r1];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r3 = 2;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            if (r2 != r3) goto L_0x0065;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r2 = 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r4 = 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r5 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r6 = r9[r4];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r7 = -1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            if (r6 != r7) goto L_0x0015;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r5 = r5 + 255;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r4 = r4 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            goto L_0x000b;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r6 = r4 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r4 = r9[r4];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r5 = r5 + r4;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r4 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r8 = r9[r6];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            if (r8 != r7) goto L_0x0024;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r4 = r4 + 255;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r6 = r6 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            goto L_0x001b;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r7 = r6 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r6 = r9[r6];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r4 = r4 + r6;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r6 = r9[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            if (r6 != r2) goto L_0x005f;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r2 = new byte[r5];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            java.lang.System.arraycopy(r9, r7, r2, r1, r5);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r7 = r7 + r5;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r5 = r9[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r6 = 3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            if (r5 != r6) goto L_0x0059;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r7 = r7 + r4;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r4 = r9[r7];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r5 = 5;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            if (r4 != r5) goto L_0x0053;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r4 = r9.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r4 = r4 - r7;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r4 = new byte[r4];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r5 = r9.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r5 = r5 - r7;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            java.lang.System.arraycopy(r9, r7, r4, r1, r5);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9 = new java.util.ArrayList;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9.<init>(r3);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9.add(r2);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9.add(r4);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            return r9;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9 = new com.google.android.exoplayer2.ParserException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9.<init>(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            throw r9;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9 = new com.google.android.exoplayer2.ParserException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9.<init>(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            throw r9;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9 = new com.google.android.exoplayer2.ParserException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9.<init>(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            throw r9;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9 = new com.google.android.exoplayer2.ParserException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9.<init>(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            throw r9;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x006b }
            r9 = new com.google.android.exoplayer2.ParserException;
            r9.<init>(r0);
            throw r9;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor$Track.parseVorbisCodecPrivate(byte[]):java.util.List");
        }

        private Track() {
            this.width = -1;
            this.height = -1;
            this.displayWidth = -1;
            this.displayHeight = -1;
            this.displayUnit = 0;
            this.projectionType = -1;
            this.projectionPoseYaw = 0.0f;
            this.projectionPosePitch = 0.0f;
            this.projectionPoseRoll = 0.0f;
            this.projectionData = null;
            this.stereoMode = -1;
            this.hasColorInfo = false;
            this.colorSpace = -1;
            this.colorTransfer = -1;
            this.colorRange = -1;
            this.maxContentLuminance = 1000;
            this.maxFrameAverageLuminance = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
            this.primaryRChromaticityX = -1.0f;
            this.primaryRChromaticityY = -1.0f;
            this.primaryGChromaticityX = -1.0f;
            this.primaryGChromaticityY = -1.0f;
            this.primaryBChromaticityX = -1.0f;
            this.primaryBChromaticityY = -1.0f;
            this.whitePointChromaticityX = -1.0f;
            this.whitePointChromaticityY = -1.0f;
            this.maxMasteringLuminance = -1.0f;
            this.minMasteringLuminance = -1.0f;
            this.channelCount = 1;
            this.audioBitDepth = -1;
            this.sampleRate = 8000;
            this.codecDelayNs = 0;
            this.seekPreRollNs = 0;
            this.flagDefault = true;
            this.language = "eng";
        }

        /* JADX WARNING: Missing block: B:102:0x01d2, code skipped:
            r23 = r1;
            r1 = r11;
            r3 = null;
            r20 = -1;
     */
        /* JADX WARNING: Missing block: B:109:0x0222, code skipped:
            r1 = r14;
     */
        /* JADX WARNING: Missing block: B:118:0x024f, code skipped:
            r3 = null;
            r20 = 4096;
     */
        /* JADX WARNING: Missing block: B:124:0x02c7, code skipped:
            r20 = -1;
            r23 = -1;
            r31 = r3;
            r3 = r1;
            r1 = r31;
     */
        /* JADX WARNING: Missing block: B:135:0x030e, code skipped:
            r3 = null;
     */
        /* JADX WARNING: Missing block: B:136:0x030f, code skipped:
            r20 = -1;
     */
        /* JADX WARNING: Missing block: B:137:0x0311, code skipped:
            r23 = -1;
     */
        /* JADX WARNING: Missing block: B:138:0x0313, code skipped:
            r11 = r0.flagDefault | 0;
     */
        /* JADX WARNING: Missing block: B:139:0x0318, code skipped:
            if (r0.flagForced == false) goto L_0x031c;
     */
        /* JADX WARNING: Missing block: B:140:0x031a, code skipped:
            r12 = 2;
     */
        /* JADX WARNING: Missing block: B:141:0x031c, code skipped:
            r12 = 0;
     */
        /* JADX WARNING: Missing block: B:142:0x031d, code skipped:
            r11 = r11 | r12;
     */
        /* JADX WARNING: Missing block: B:143:0x0322, code skipped:
            if (com.google.android.exoplayer2.util.MimeTypes.isAudio(r1) == false) goto L_0x0349;
     */
        /* JADX WARNING: Missing block: B:144:0x0324, code skipped:
            r1 = com.google.android.exoplayer2.Format.createAudioSampleFormat(java.lang.Integer.toString(r34), r1, null, -1, r20, r0.channelCount, r0.sampleRate, r23, r3, r0.drmInitData, r11, r0.language);
            r7 = 1;
     */
        /* JADX WARNING: Missing block: B:146:0x034d, code skipped:
            if (com.google.android.exoplayer2.util.MimeTypes.isVideo(r1) == false) goto L_0x0449;
     */
        /* JADX WARNING: Missing block: B:148:0x0351, code skipped:
            if (r0.displayUnit != 0) goto L_0x0363;
     */
        /* JADX WARNING: Missing block: B:149:0x0353, code skipped:
            r2 = r0.displayWidth;
     */
        /* JADX WARNING: Missing block: B:150:0x0355, code skipped:
            if (r2 != -1) goto L_0x0359;
     */
        /* JADX WARNING: Missing block: B:151:0x0357, code skipped:
            r2 = r0.width;
     */
        /* JADX WARNING: Missing block: B:152:0x0359, code skipped:
            r0.displayWidth = r2;
            r2 = r0.displayHeight;
     */
        /* JADX WARNING: Missing block: B:153:0x035d, code skipped:
            if (r2 != -1) goto L_0x0361;
     */
        /* JADX WARNING: Missing block: B:154:0x035f, code skipped:
            r2 = r0.height;
     */
        /* JADX WARNING: Missing block: B:155:0x0361, code skipped:
            r0.displayHeight = r2;
     */
        /* JADX WARNING: Missing block: B:156:0x0363, code skipped:
            r4 = r0.displayWidth;
     */
        /* JADX WARNING: Missing block: B:157:0x0367, code skipped:
            if (r4 == -1) goto L_0x037b;
     */
        /* JADX WARNING: Missing block: B:158:0x0369, code skipped:
            r7 = r0.displayHeight;
     */
        /* JADX WARNING: Missing block: B:159:0x036b, code skipped:
            if (r7 == -1) goto L_0x037b;
     */
        /* JADX WARNING: Missing block: B:160:0x036d, code skipped:
            r26 = ((float) (r0.height * r4)) / ((float) (r0.width * r7));
     */
        /* JADX WARNING: Missing block: B:161:0x037b, code skipped:
            r26 = -1.0f;
     */
        /* JADX WARNING: Missing block: B:163:0x037f, code skipped:
            if (r0.hasColorInfo == false) goto L_0x0390;
     */
        /* JADX WARNING: Missing block: B:164:0x0381, code skipped:
            r15 = new com.google.android.exoplayer2.video.ColorInfo(r0.colorSpace, r0.colorRange, r0.colorTransfer, getHdrStaticInfo());
     */
        /* JADX WARNING: Missing block: B:165:0x0390, code skipped:
            r29 = r15;
     */
        /* JADX WARNING: Missing block: B:166:0x039a, code skipped:
            if ("htc_video_rotA-000".equals(r0.name) == false) goto L_0x039e;
     */
        /* JADX WARNING: Missing block: B:167:0x039c, code skipped:
            r2 = 0;
     */
        /* JADX WARNING: Missing block: B:169:0x03a6, code skipped:
            if ("htc_video_rotA-090".equals(r0.name) == false) goto L_0x03ab;
     */
        /* JADX WARNING: Missing block: B:170:0x03a8, code skipped:
            r2 = 90;
     */
        /* JADX WARNING: Missing block: B:172:0x03b3, code skipped:
            if ("htc_video_rotA-180".equals(r0.name) == false) goto L_0x03b8;
     */
        /* JADX WARNING: Missing block: B:173:0x03b5, code skipped:
            r2 = 180;
     */
        /* JADX WARNING: Missing block: B:175:0x03c0, code skipped:
            if ("htc_video_rotA-270".equals(r0.name) == false) goto L_0x03c5;
     */
        /* JADX WARNING: Missing block: B:176:0x03c2, code skipped:
            r2 = 270;
     */
        /* JADX WARNING: Missing block: B:177:0x03c5, code skipped:
            r2 = -1;
     */
        /* JADX WARNING: Missing block: B:179:0x03c8, code skipped:
            if (r0.projectionType != 0) goto L_0x041e;
     */
        /* JADX WARNING: Missing block: B:181:0x03d1, code skipped:
            if (java.lang.Float.compare(r0.projectionPoseYaw, 0.0f) != 0) goto L_0x041e;
     */
        /* JADX WARNING: Missing block: B:183:0x03d9, code skipped:
            if (java.lang.Float.compare(r0.projectionPosePitch, 0.0f) != 0) goto L_0x041e;
     */
        /* JADX WARNING: Missing block: B:185:0x03e1, code skipped:
            if (java.lang.Float.compare(r0.projectionPoseRoll, 0.0f) != 0) goto L_0x03e6;
     */
        /* JADX WARNING: Missing block: B:186:0x03e3, code skipped:
            r25 = 0;
     */
        /* JADX WARNING: Missing block: B:188:0x03ee, code skipped:
            if (java.lang.Float.compare(r0.projectionPosePitch, 90.0f) != 0) goto L_0x03f5;
     */
        /* JADX WARNING: Missing block: B:189:0x03f0, code skipped:
            r25 = 90;
     */
        /* JADX WARNING: Missing block: B:191:0x03fd, code skipped:
            if (java.lang.Float.compare(r0.projectionPosePitch, -180.0f) == 0) goto L_0x0419;
     */
        /* JADX WARNING: Missing block: B:193:0x0407, code skipped:
            if (java.lang.Float.compare(r0.projectionPosePitch, 180.0f) != 0) goto L_0x040a;
     */
        /* JADX WARNING: Missing block: B:195:0x0412, code skipped:
            if (java.lang.Float.compare(r0.projectionPosePitch, -90.0f) != 0) goto L_0x041e;
     */
        /* JADX WARNING: Missing block: B:196:0x0414, code skipped:
            r25 = 270;
     */
        /* JADX WARNING: Missing block: B:197:0x0419, code skipped:
            r25 = 180;
     */
        /* JADX WARNING: Missing block: B:198:0x041e, code skipped:
            r25 = r2;
     */
        /* JADX WARNING: Missing block: B:199:0x0420, code skipped:
            r1 = com.google.android.exoplayer2.Format.createVideoSampleFormat(java.lang.Integer.toString(r34), r1, null, -1, r20, r0.width, r0.height, -1.0f, r3, r25, r26, r0.projectionData, r0.stereoMode, r29, r0.drmInitData);
            r7 = 2;
     */
        /* JADX WARNING: Missing block: B:201:0x044d, code skipped:
            if (r10.equals(r1) == false) goto L_0x045d;
     */
        /* JADX WARNING: Missing block: B:202:0x044f, code skipped:
            r1 = com.google.android.exoplayer2.Format.createTextSampleFormat(java.lang.Integer.toString(r34), r1, r11, r0.language, r0.drmInitData);
     */
        /* JADX WARNING: Missing block: B:204:0x0461, code skipped:
            if (r9.equals(r1) == false) goto L_0x0496;
     */
        /* JADX WARNING: Missing block: B:205:0x0463, code skipped:
            r2 = new java.util.ArrayList(2);
            r2.add(com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.access$300());
            r2.add(r0.codecPrivate);
            r1 = com.google.android.exoplayer2.Format.createTextSampleFormat(java.lang.Integer.toString(r34), r1, null, -1, r11, r0.language, -1, r0.drmInitData, com.google.android.exoplayer2.util.TimestampAdjuster.DO_NOT_OFFSET, r2);
     */
        /* JADX WARNING: Missing block: B:207:0x049a, code skipped:
            if (r2.equals(r1) != false) goto L_0x04b5;
     */
        /* JADX WARNING: Missing block: B:209:0x04a2, code skipped:
            if (com.google.android.exoplayer2.util.MimeTypes.APPLICATION_PGS.equals(r1) != false) goto L_0x04b5;
     */
        /* JADX WARNING: Missing block: B:211:0x04aa, code skipped:
            if (com.google.android.exoplayer2.util.MimeTypes.APPLICATION_DVBSUBS.equals(r1) == false) goto L_0x04ad;
     */
        /* JADX WARNING: Missing block: B:213:0x04b4, code skipped:
            throw new com.google.android.exoplayer2.ParserException("Unexpected MIME type.");
     */
        /* JADX WARNING: Missing block: B:214:0x04b5, code skipped:
            r1 = com.google.android.exoplayer2.Format.createImageSampleFormat(java.lang.Integer.toString(r34), r1, null, -1, r11, r3, r0.language, r0.drmInitData);
     */
        /* JADX WARNING: Missing block: B:215:0x04cf, code skipped:
            r0.output = r33.track(r0.number, r7);
            r0.output.format(r1);
     */
        /* JADX WARNING: Missing block: B:216:0x04de, code skipped:
            return;
     */
        public void initializeOutput(com.google.android.exoplayer2.extractor.ExtractorOutput r33, int r34) throws com.google.android.exoplayer2.ParserException {
            /*
            r32 = this;
            r0 = r32;
            r1 = r0.codecId;
            r2 = r1.hashCode();
            r3 = 8;
            r4 = 1;
            r5 = 2;
            r6 = 0;
            r7 = 3;
            r8 = -1;
            switch(r2) {
                case -2095576542: goto L_0x0155;
                case -2095575984: goto L_0x014b;
                case -1985379776: goto L_0x0140;
                case -1784763192: goto L_0x0135;
                case -1730367663: goto L_0x012a;
                case -1482641358: goto L_0x011f;
                case -1482641357: goto L_0x0114;
                case -1373388978: goto L_0x0109;
                case -933872740: goto L_0x00fe;
                case -538363189: goto L_0x00f3;
                case -538363109: goto L_0x00e8;
                case -425012669: goto L_0x00dc;
                case -356037306: goto L_0x00d0;
                case 62923557: goto L_0x00c4;
                case 62923603: goto L_0x00b8;
                case 62927045: goto L_0x00ac;
                case 82338133: goto L_0x00a1;
                case 82338134: goto L_0x0096;
                case 99146302: goto L_0x008a;
                case 444813526: goto L_0x007e;
                case 542569478: goto L_0x0072;
                case 725957860: goto L_0x0066;
                case 738597099: goto L_0x005a;
                case 855502857: goto L_0x004f;
                case 1422270023: goto L_0x0043;
                case 1809237540: goto L_0x0038;
                case 1950749482: goto L_0x002c;
                case 1950789798: goto L_0x0020;
                case 1951062397: goto L_0x0014;
                default: goto L_0x0012;
            };
        L_0x0012:
            goto L_0x015f;
        L_0x0014:
            r2 = "A_OPUS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x001c:
            r1 = 11;
            goto L_0x0160;
        L_0x0020:
            r2 = "A_FLAC";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0028:
            r1 = 21;
            goto L_0x0160;
        L_0x002c:
            r2 = "A_EAC3";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0034:
            r1 = 16;
            goto L_0x0160;
        L_0x0038:
            r2 = "V_MPEG2";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0040:
            r1 = 2;
            goto L_0x0160;
        L_0x0043:
            r2 = "S_TEXT/UTF8";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x004b:
            r1 = 24;
            goto L_0x0160;
        L_0x004f:
            r2 = "V_MPEGH/ISO/HEVC";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0057:
            r1 = 7;
            goto L_0x0160;
        L_0x005a:
            r2 = "S_TEXT/ASS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0062:
            r1 = 25;
            goto L_0x0160;
        L_0x0066:
            r2 = "A_PCM/INT/LIT";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x006e:
            r1 = 23;
            goto L_0x0160;
        L_0x0072:
            r2 = "A_DTS/EXPRESS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x007a:
            r1 = 19;
            goto L_0x0160;
        L_0x007e:
            r2 = "V_THEORA";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0086:
            r1 = 9;
            goto L_0x0160;
        L_0x008a:
            r2 = "S_HDMV/PGS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0092:
            r1 = 27;
            goto L_0x0160;
        L_0x0096:
            r2 = "V_VP9";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x009e:
            r1 = 1;
            goto L_0x0160;
        L_0x00a1:
            r2 = "V_VP8";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x00a9:
            r1 = 0;
            goto L_0x0160;
        L_0x00ac:
            r2 = "A_DTS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x00b4:
            r1 = 18;
            goto L_0x0160;
        L_0x00b8:
            r2 = "A_AC3";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x00c0:
            r1 = 15;
            goto L_0x0160;
        L_0x00c4:
            r2 = "A_AAC";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x00cc:
            r1 = 12;
            goto L_0x0160;
        L_0x00d0:
            r2 = "A_DTS/LOSSLESS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x00d8:
            r1 = 20;
            goto L_0x0160;
        L_0x00dc:
            r2 = "S_VOBSUB";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x00e4:
            r1 = 26;
            goto L_0x0160;
        L_0x00e8:
            r2 = "V_MPEG4/ISO/AVC";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x00f0:
            r1 = 6;
            goto L_0x0160;
        L_0x00f3:
            r2 = "V_MPEG4/ISO/ASP";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x00fb:
            r1 = 4;
            goto L_0x0160;
        L_0x00fe:
            r2 = "S_DVBSUB";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0106:
            r1 = 28;
            goto L_0x0160;
        L_0x0109:
            r2 = "V_MS/VFW/FOURCC";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0111:
            r1 = 8;
            goto L_0x0160;
        L_0x0114:
            r2 = "A_MPEG/L3";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x011c:
            r1 = 14;
            goto L_0x0160;
        L_0x011f:
            r2 = "A_MPEG/L2";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0127:
            r1 = 13;
            goto L_0x0160;
        L_0x012a:
            r2 = "A_VORBIS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0132:
            r1 = 10;
            goto L_0x0160;
        L_0x0135:
            r2 = "A_TRUEHD";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x013d:
            r1 = 17;
            goto L_0x0160;
        L_0x0140:
            r2 = "A_MS/ACM";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0148:
            r1 = 22;
            goto L_0x0160;
        L_0x014b:
            r2 = "V_MPEG4/ISO/SP";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x0153:
            r1 = 3;
            goto L_0x0160;
        L_0x0155:
            r2 = "V_MPEG4/ISO/AP";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x015f;
        L_0x015d:
            r1 = 5;
            goto L_0x0160;
        L_0x015f:
            r1 = -1;
        L_0x0160:
            r2 = "application/vobsub";
            r9 = "text/x-ssa";
            r10 = "application/x-subrip";
            r11 = "audio/raw";
            r12 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            r13 = "MatroskaExtractor";
            r14 = "audio/x-unknown";
            r15 = 0;
            switch(r1) {
                case 0: goto L_0x030c;
                case 1: goto L_0x0309;
                case 2: goto L_0x0306;
                case 3: goto L_0x02f9;
                case 4: goto L_0x02f9;
                case 5: goto L_0x02f9;
                case 6: goto L_0x02e5;
                case 7: goto L_0x02d1;
                case 8: goto L_0x02b4;
                case 9: goto L_0x02b1;
                case 10: goto L_0x02a3;
                case 11: goto L_0x025d;
                case 12: goto L_0x0254;
                case 13: goto L_0x024d;
                case 14: goto L_0x024a;
                case 15: goto L_0x0246;
                case 16: goto L_0x0242;
                case 17: goto L_0x0237;
                case 18: goto L_0x0233;
                case 19: goto L_0x0233;
                case 20: goto L_0x022f;
                case 21: goto L_0x0225;
                case 22: goto L_0x01da;
                case 23: goto L_0x01ab;
                case 24: goto L_0x01a8;
                case 25: goto L_0x01a5;
                case 26: goto L_0x019b;
                case 27: goto L_0x0197;
                case 28: goto L_0x017a;
                default: goto L_0x0172;
            };
        L_0x0172:
            r1 = new com.google.android.exoplayer2.ParserException;
            r2 = "Unrecognized codec identifier.";
            r1.<init>(r2);
            throw r1;
        L_0x017a:
            r1 = 4;
            r1 = new byte[r1];
            r3 = r0.codecPrivate;
            r11 = r3[r6];
            r1[r6] = r11;
            r11 = r3[r4];
            r1[r4] = r11;
            r11 = r3[r5];
            r1[r5] = r11;
            r3 = r3[r7];
            r1[r7] = r3;
            r1 = java.util.Collections.singletonList(r1);
            r3 = "application/dvbsubs";
            goto L_0x02c7;
        L_0x0197:
            r1 = "application/pgs";
            goto L_0x030e;
        L_0x019b:
            r1 = r0.codecPrivate;
            r1 = java.util.Collections.singletonList(r1);
            r3 = r1;
            r1 = r2;
            goto L_0x030f;
        L_0x01a5:
            r1 = r9;
            goto L_0x030e;
        L_0x01a8:
            r1 = r10;
            goto L_0x030e;
        L_0x01ab:
            r1 = r0.audioBitDepth;
            r1 = com.google.android.exoplayer2.util.Util.getPcmEncoding(r1);
            if (r1 != 0) goto L_0x01d2;
        L_0x01b3:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r3 = "Unsupported PCM bit depth: ";
            r1.append(r3);
            r3 = r0.audioBitDepth;
            r1.append(r3);
            r3 = ". Setting mimeType to ";
            r1.append(r3);
            r1.append(r14);
            r1 = r1.toString();
            com.google.android.exoplayer2.util.Log.m18w(r13, r1);
            goto L_0x0222;
        L_0x01d2:
            r23 = r1;
            r1 = r11;
            r3 = r15;
            r20 = -1;
            goto L_0x0313;
        L_0x01da:
            r1 = new com.google.android.exoplayer2.util.ParsableByteArray;
            r3 = r0.codecPrivate;
            r1.<init>(r3);
            r1 = parseMsAcmCodecPrivate(r1);
            if (r1 == 0) goto L_0x020e;
        L_0x01e7:
            r1 = r0.audioBitDepth;
            r1 = com.google.android.exoplayer2.util.Util.getPcmEncoding(r1);
            if (r1 != 0) goto L_0x01d2;
        L_0x01ef:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r3 = "Unsupported PCM bit depth: ";
            r1.append(r3);
            r3 = r0.audioBitDepth;
            r1.append(r3);
            r3 = ". Setting mimeType to ";
            r1.append(r3);
            r1.append(r14);
            r1 = r1.toString();
            com.google.android.exoplayer2.util.Log.m18w(r13, r1);
            goto L_0x0222;
        L_0x020e:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r3 = "Non-PCM MS/ACM is unsupported. Setting mimeType to ";
            r1.append(r3);
            r1.append(r14);
            r1 = r1.toString();
            com.google.android.exoplayer2.util.Log.m18w(r13, r1);
        L_0x0222:
            r1 = r14;
            goto L_0x030e;
        L_0x0225:
            r1 = r0.codecPrivate;
            r1 = java.util.Collections.singletonList(r1);
            r3 = "audio/flac";
            goto L_0x02c7;
        L_0x022f:
            r1 = "audio/vnd.dts.hd";
            goto L_0x030e;
        L_0x0233:
            r1 = "audio/vnd.dts";
            goto L_0x030e;
        L_0x0237:
            r1 = new com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor$TrueHdSampleRechunker;
            r1.<init>();
            r0.trueHdSampleRechunker = r1;
            r1 = "audio/true-hd";
            goto L_0x030e;
        L_0x0242:
            r1 = "audio/eac3";
            goto L_0x030e;
        L_0x0246:
            r1 = "audio/ac3";
            goto L_0x030e;
        L_0x024a:
            r1 = "audio/mpeg";
            goto L_0x024f;
        L_0x024d:
            r1 = "audio/mpeg-L2";
        L_0x024f:
            r3 = r15;
            r20 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            goto L_0x0311;
        L_0x0254:
            r1 = r0.codecPrivate;
            r1 = java.util.Collections.singletonList(r1);
            r3 = "audio/mp4a-latm";
            goto L_0x02c7;
        L_0x025d:
            r1 = 5760; // 0x1680 float:8.071E-42 double:2.846E-320;
            r11 = new java.util.ArrayList;
            r11.<init>(r7);
            r12 = r0.codecPrivate;
            r11.add(r12);
            r12 = java.nio.ByteBuffer.allocate(r3);
            r13 = java.nio.ByteOrder.nativeOrder();
            r12 = r12.order(r13);
            r13 = r0.codecDelayNs;
            r12 = r12.putLong(r13);
            r12 = r12.array();
            r11.add(r12);
            r3 = java.nio.ByteBuffer.allocate(r3);
            r12 = java.nio.ByteOrder.nativeOrder();
            r3 = r3.order(r12);
            r12 = r0.seekPreRollNs;
            r3 = r3.putLong(r12);
            r3 = r3.array();
            r11.add(r3);
            r3 = "audio/opus";
            r1 = r3;
            r3 = r11;
            r20 = 5760; // 0x1680 float:8.071E-42 double:2.846E-320;
            goto L_0x0311;
        L_0x02a3:
            r1 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r3 = r0.codecPrivate;
            r3 = parseVorbisCodecPrivate(r3);
            r11 = "audio/vorbis";
            r1 = r11;
            r20 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            goto L_0x0311;
        L_0x02b1:
            r1 = "video/x-unknown";
            goto L_0x030e;
        L_0x02b4:
            r1 = new com.google.android.exoplayer2.util.ParsableByteArray;
            r3 = r0.codecPrivate;
            r1.<init>(r3);
            r1 = parseFourCcPrivate(r1);
            r3 = r1.first;
            r3 = (java.lang.String) r3;
            r1 = r1.second;
            r1 = (java.util.List) r1;
        L_0x02c7:
            r20 = -1;
            r23 = -1;
            r31 = r3;
            r3 = r1;
            r1 = r31;
            goto L_0x0313;
        L_0x02d1:
            r1 = new com.google.android.exoplayer2.util.ParsableByteArray;
            r3 = r0.codecPrivate;
            r1.<init>(r3);
            r1 = com.google.android.exoplayer2.video.HevcConfig.parse(r1);
            r3 = r1.initializationData;
            r1 = r1.nalUnitLengthFieldLength;
            r0.nalUnitLengthFieldLength = r1;
            r1 = "video/hevc";
            goto L_0x030f;
        L_0x02e5:
            r1 = new com.google.android.exoplayer2.util.ParsableByteArray;
            r3 = r0.codecPrivate;
            r1.<init>(r3);
            r1 = com.google.android.exoplayer2.video.AvcConfig.parse(r1);
            r3 = r1.initializationData;
            r1 = r1.nalUnitLengthFieldLength;
            r0.nalUnitLengthFieldLength = r1;
            r1 = "video/avc";
            goto L_0x030f;
        L_0x02f9:
            r1 = r0.codecPrivate;
            if (r1 != 0) goto L_0x02ff;
        L_0x02fd:
            r1 = r15;
            goto L_0x0303;
        L_0x02ff:
            r1 = java.util.Collections.singletonList(r1);
        L_0x0303:
            r3 = "video/mp4v-es";
            goto L_0x02c7;
        L_0x0306:
            r1 = "video/mpeg2";
            goto L_0x030e;
        L_0x0309:
            r1 = "video/x-vnd.on2.vp9";
            goto L_0x030e;
        L_0x030c:
            r1 = "video/x-vnd.on2.vp8";
        L_0x030e:
            r3 = r15;
        L_0x030f:
            r20 = -1;
        L_0x0311:
            r23 = -1;
        L_0x0313:
            r11 = r0.flagDefault;
            r11 = r11 | r6;
            r12 = r0.flagForced;
            if (r12 == 0) goto L_0x031c;
        L_0x031a:
            r12 = 2;
            goto L_0x031d;
        L_0x031c:
            r12 = 0;
        L_0x031d:
            r11 = r11 | r12;
            r12 = com.google.android.exoplayer2.util.MimeTypes.isAudio(r1);
            if (r12 == 0) goto L_0x0349;
        L_0x0324:
            r16 = java.lang.Integer.toString(r34);
            r18 = 0;
            r19 = -1;
            r2 = r0.channelCount;
            r5 = r0.sampleRate;
            r6 = r0.drmInitData;
            r7 = r0.language;
            r17 = r1;
            r21 = r2;
            r22 = r5;
            r24 = r3;
            r25 = r6;
            r26 = r11;
            r27 = r7;
            r1 = com.google.android.exoplayer2.Format.createAudioSampleFormat(r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27);
            r7 = 1;
            goto L_0x04cf;
        L_0x0349:
            r4 = com.google.android.exoplayer2.util.MimeTypes.isVideo(r1);
            if (r4 == 0) goto L_0x0449;
        L_0x034f:
            r2 = r0.displayUnit;
            if (r2 != 0) goto L_0x0363;
        L_0x0353:
            r2 = r0.displayWidth;
            if (r2 != r8) goto L_0x0359;
        L_0x0357:
            r2 = r0.width;
        L_0x0359:
            r0.displayWidth = r2;
            r2 = r0.displayHeight;
            if (r2 != r8) goto L_0x0361;
        L_0x035f:
            r2 = r0.height;
        L_0x0361:
            r0.displayHeight = r2;
        L_0x0363:
            r2 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
            r4 = r0.displayWidth;
            if (r4 == r8) goto L_0x037b;
        L_0x0369:
            r7 = r0.displayHeight;
            if (r7 == r8) goto L_0x037b;
        L_0x036d:
            r2 = r0.height;
            r2 = r2 * r4;
            r2 = (float) r2;
            r4 = r0.width;
            r4 = r4 * r7;
            r4 = (float) r4;
            r2 = r2 / r4;
            r26 = r2;
            goto L_0x037d;
        L_0x037b:
            r26 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        L_0x037d:
            r2 = r0.hasColorInfo;
            if (r2 == 0) goto L_0x0390;
        L_0x0381:
            r2 = r32.getHdrStaticInfo();
            r15 = new com.google.android.exoplayer2.video.ColorInfo;
            r4 = r0.colorSpace;
            r7 = r0.colorRange;
            r9 = r0.colorTransfer;
            r15.<init>(r4, r7, r9, r2);
        L_0x0390:
            r29 = r15;
            r2 = r0.name;
            r4 = "htc_video_rotA-000";
            r2 = r4.equals(r2);
            if (r2 == 0) goto L_0x039e;
        L_0x039c:
            r2 = 0;
            goto L_0x03c6;
        L_0x039e:
            r2 = r0.name;
            r4 = "htc_video_rotA-090";
            r2 = r4.equals(r2);
            if (r2 == 0) goto L_0x03ab;
        L_0x03a8:
            r2 = 90;
            goto L_0x03c6;
        L_0x03ab:
            r2 = r0.name;
            r4 = "htc_video_rotA-180";
            r2 = r4.equals(r2);
            if (r2 == 0) goto L_0x03b8;
        L_0x03b5:
            r2 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
            goto L_0x03c6;
        L_0x03b8:
            r2 = r0.name;
            r4 = "htc_video_rotA-270";
            r2 = r4.equals(r2);
            if (r2 == 0) goto L_0x03c5;
        L_0x03c2:
            r2 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
            goto L_0x03c6;
        L_0x03c5:
            r2 = -1;
        L_0x03c6:
            r4 = r0.projectionType;
            if (r4 != 0) goto L_0x041e;
        L_0x03ca:
            r4 = r0.projectionPoseYaw;
            r7 = 0;
            r4 = java.lang.Float.compare(r4, r7);
            if (r4 != 0) goto L_0x041e;
        L_0x03d3:
            r4 = r0.projectionPosePitch;
            r4 = java.lang.Float.compare(r4, r7);
            if (r4 != 0) goto L_0x041e;
        L_0x03db:
            r4 = r0.projectionPoseRoll;
            r4 = java.lang.Float.compare(r4, r7);
            if (r4 != 0) goto L_0x03e6;
        L_0x03e3:
            r25 = 0;
            goto L_0x0420;
        L_0x03e6:
            r4 = r0.projectionPosePitch;
            r6 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
            r4 = java.lang.Float.compare(r4, r6);
            if (r4 != 0) goto L_0x03f5;
        L_0x03f0:
            r6 = 90;
            r25 = 90;
            goto L_0x0420;
        L_0x03f5:
            r4 = r0.projectionPosePitch;
            r6 = -1020002304; // 0xffffffffc3340000 float:-180.0 double:NaN;
            r4 = java.lang.Float.compare(r4, r6);
            if (r4 == 0) goto L_0x0419;
        L_0x03ff:
            r4 = r0.projectionPosePitch;
            r6 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
            r4 = java.lang.Float.compare(r4, r6);
            if (r4 != 0) goto L_0x040a;
        L_0x0409:
            goto L_0x0419;
        L_0x040a:
            r4 = r0.projectionPosePitch;
            r6 = -1028390912; // 0xffffffffc2b40000 float:-90.0 double:NaN;
            r4 = java.lang.Float.compare(r4, r6);
            if (r4 != 0) goto L_0x041e;
        L_0x0414:
            r6 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
            r25 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
            goto L_0x0420;
        L_0x0419:
            r6 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
            r25 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
            goto L_0x0420;
        L_0x041e:
            r25 = r2;
        L_0x0420:
            r16 = java.lang.Integer.toString(r34);
            r18 = 0;
            r19 = -1;
            r2 = r0.width;
            r4 = r0.height;
            r23 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
            r6 = r0.projectionData;
            r7 = r0.stereoMode;
            r8 = r0.drmInitData;
            r17 = r1;
            r21 = r2;
            r22 = r4;
            r24 = r3;
            r27 = r6;
            r28 = r7;
            r30 = r8;
            r1 = com.google.android.exoplayer2.Format.createVideoSampleFormat(r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30);
            r7 = 2;
            goto L_0x04cf;
        L_0x0449:
            r4 = r10.equals(r1);
            if (r4 == 0) goto L_0x045d;
        L_0x044f:
            r2 = java.lang.Integer.toString(r34);
            r3 = r0.language;
            r4 = r0.drmInitData;
            r1 = com.google.android.exoplayer2.Format.createTextSampleFormat(r2, r1, r11, r3, r4);
            goto L_0x04cf;
        L_0x045d:
            r4 = r9.equals(r1);
            if (r4 == 0) goto L_0x0496;
        L_0x0463:
            r2 = new java.util.ArrayList;
            r2.<init>(r5);
            r3 = com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.SSA_DIALOGUE_FORMAT;
            r2.add(r3);
            r3 = r0.codecPrivate;
            r2.add(r3);
            r16 = java.lang.Integer.toString(r34);
            r18 = 0;
            r19 = -1;
            r3 = r0.language;
            r22 = -1;
            r4 = r0.drmInitData;
            r24 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r17 = r1;
            r20 = r11;
            r21 = r3;
            r23 = r4;
            r26 = r2;
            r1 = com.google.android.exoplayer2.Format.createTextSampleFormat(r16, r17, r18, r19, r20, r21, r22, r23, r24, r26);
            goto L_0x04cf;
        L_0x0496:
            r2 = r2.equals(r1);
            if (r2 != 0) goto L_0x04b5;
        L_0x049c:
            r2 = "application/pgs";
            r2 = r2.equals(r1);
            if (r2 != 0) goto L_0x04b5;
        L_0x04a4:
            r2 = "application/dvbsubs";
            r2 = r2.equals(r1);
            if (r2 == 0) goto L_0x04ad;
        L_0x04ac:
            goto L_0x04b5;
        L_0x04ad:
            r1 = new com.google.android.exoplayer2.ParserException;
            r2 = "Unexpected MIME type.";
            r1.<init>(r2);
            throw r1;
        L_0x04b5:
            r16 = java.lang.Integer.toString(r34);
            r18 = 0;
            r19 = -1;
            r2 = r0.language;
            r4 = r0.drmInitData;
            r17 = r1;
            r20 = r11;
            r21 = r3;
            r22 = r2;
            r23 = r4;
            r1 = com.google.android.exoplayer2.Format.createImageSampleFormat(r16, r17, r18, r19, r20, r21, r22, r23);
        L_0x04cf:
            r2 = r0.number;
            r3 = r33;
            r2 = r3.track(r2, r7);
            r0.output = r2;
            r2 = r0.output;
            r2.format(r1);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor$Track.initializeOutput(com.google.android.exoplayer2.extractor.ExtractorOutput, int):void");
        }

        public void outputPendingSampleMetadata() {
            TrueHdSampleRechunker trueHdSampleRechunker = this.trueHdSampleRechunker;
            if (trueHdSampleRechunker != null) {
                trueHdSampleRechunker.outputPendingSampleMetadata(this);
            }
        }

        public void reset() {
            TrueHdSampleRechunker trueHdSampleRechunker = this.trueHdSampleRechunker;
            if (trueHdSampleRechunker != null) {
                trueHdSampleRechunker.reset();
            }
        }

        private byte[] getHdrStaticInfo() {
            if (this.primaryRChromaticityX == -1.0f || this.primaryRChromaticityY == -1.0f || this.primaryGChromaticityX == -1.0f || this.primaryGChromaticityY == -1.0f || this.primaryBChromaticityX == -1.0f || this.primaryBChromaticityY == -1.0f || this.whitePointChromaticityX == -1.0f || this.whitePointChromaticityY == -1.0f || this.maxMasteringLuminance == -1.0f || this.minMasteringLuminance == -1.0f) {
                return null;
            }
            byte[] bArr = new byte[25];
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            wrap.put((byte) 0);
            wrap.putShort((short) ((int) ((this.primaryRChromaticityX * 50000.0f) + 0.5f)));
            wrap.putShort((short) ((int) ((this.primaryRChromaticityY * 50000.0f) + 0.5f)));
            wrap.putShort((short) ((int) ((this.primaryGChromaticityX * 50000.0f) + 0.5f)));
            wrap.putShort((short) ((int) ((this.primaryGChromaticityY * 50000.0f) + 0.5f)));
            wrap.putShort((short) ((int) ((this.primaryBChromaticityX * 50000.0f) + 0.5f)));
            wrap.putShort((short) ((int) ((this.primaryBChromaticityY * 50000.0f) + 0.5f)));
            wrap.putShort((short) ((int) ((this.whitePointChromaticityX * 50000.0f) + 0.5f)));
            wrap.putShort((short) ((int) ((this.whitePointChromaticityY * 50000.0f) + 0.5f)));
            wrap.putShort((short) ((int) (this.maxMasteringLuminance + 0.5f)));
            wrap.putShort((short) ((int) (this.minMasteringLuminance + 0.5f)));
            wrap.putShort((short) this.maxContentLuminance);
            wrap.putShort((short) this.maxFrameAverageLuminance);
            return bArr;
        }

        private static boolean parseMsAcmCodecPrivate(ParsableByteArray parsableByteArray) throws ParserException {
            try {
                int readLittleEndianUnsignedShort = parsableByteArray.readLittleEndianUnsignedShort();
                boolean z = true;
                if (readLittleEndianUnsignedShort == 1) {
                    return true;
                }
                if (readLittleEndianUnsignedShort != 65534) {
                    return false;
                }
                parsableByteArray.setPosition(24);
                if (!(parsableByteArray.readLong() == MatroskaExtractor.WAVE_SUBFORMAT_PCM.getMostSignificantBits() && parsableByteArray.readLong() == MatroskaExtractor.WAVE_SUBFORMAT_PCM.getLeastSignificantBits())) {
                    z = false;
                }
                return z;
            } catch (ArrayIndexOutOfBoundsException unused) {
                throw new ParserException("Error parsing MS/ACM codec private");
            }
        }
    }

    private static final class TrueHdSampleRechunker {
        private int blockFlags;
        private int chunkSize;
        private boolean foundSyncframe;
        private int sampleCount;
        private final byte[] syncframePrefix = new byte[10];
        private long timeUs;

        public void reset() {
            this.foundSyncframe = false;
        }

        public void startSample(ExtractorInput extractorInput, int i, int i2) throws IOException, InterruptedException {
            if (!this.foundSyncframe) {
                extractorInput.peekFully(this.syncframePrefix, 0, 10);
                extractorInput.resetPeekPosition();
                if (Ac3Util.parseTrueHdSyncframeAudioSampleCount(this.syncframePrefix) != 0) {
                    this.foundSyncframe = true;
                    this.sampleCount = 0;
                } else {
                    return;
                }
            }
            if (this.sampleCount == 0) {
                this.blockFlags = i;
                this.chunkSize = 0;
            }
            this.chunkSize += i2;
        }

        public void sampleMetadata(Track track, long j) {
            if (this.foundSyncframe) {
                int i = this.sampleCount;
                this.sampleCount = i + 1;
                if (i == 0) {
                    this.timeUs = j;
                }
                if (this.sampleCount >= 16) {
                    track.output.sampleMetadata(this.timeUs, this.blockFlags, this.chunkSize, 0, track.cryptoData);
                    this.sampleCount = 0;
                }
            }
        }

        public void outputPendingSampleMetadata(Track track) {
            if (this.foundSyncframe && this.sampleCount > 0) {
                track.output.sampleMetadata(this.timeUs, this.blockFlags, this.chunkSize, 0, track.cryptoData);
                this.sampleCount = 0;
            }
        }
    }

    private final class InnerEbmlReaderOutput implements EbmlReaderOutput {
        public int getElementType(int i) {
            switch (i) {
                case 131:
                case 136:
                case 155:
                case 159:
                case 176:
                case 179:
                case 186:
                case 215:
                case 231:
                case 241:
                case 251:
                case 16980:
                case 17029:
                case 17143:
                case 18401:
                case 18408:
                case 20529:
                case 20530:
                case 21420:
                case 21432:
                case 21680:
                case 21682:
                case 21690:
                case 21930:
                case 21945:
                case 21946:
                case 21947:
                case 21948:
                case 21949:
                case 22186:
                case 22203:
                case 25188:
                case 30321:
                case 2352003:
                case 2807729:
                    return 2;
                case 134:
                case 17026:
                case 21358:
                case 2274716:
                    return 3;
                case 160:
                case 174:
                case 183:
                case 187:
                case 224:
                case 225:
                case 18407:
                case 19899:
                case 20532:
                case 20533:
                case 21936:
                case 21968:
                case 25152:
                case 28032:
                case 30320:
                case 290298740:
                case 357149030:
                case 374648427:
                case 408125543:
                case 440786851:
                case 475249515:
                case 524531317:
                    return 1;
                case 161:
                case 163:
                case 16981:
                case 18402:
                case 21419:
                case 25506:
                case 30322:
                    return 4;
                case 181:
                case 17545:
                case 21969:
                case 21970:
                case 21971:
                case 21972:
                case 21973:
                case 21974:
                case 21975:
                case 21976:
                case 21977:
                case 21978:
                case 30323:
                case 30324:
                case 30325:
                    return 5;
                default:
                    return 0;
            }
        }

        public boolean isLevel1Element(int i) {
            return i == 357149030 || i == 524531317 || i == 475249515 || i == 374648427;
        }

        private InnerEbmlReaderOutput() {
        }

        public void startMasterElement(int i, long j, long j2) throws ParserException {
            MatroskaExtractor.this.startMasterElement(i, j, j2);
        }

        public void endMasterElement(int i) throws ParserException {
            MatroskaExtractor.this.endMasterElement(i);
        }

        public void integerElement(int i, long j) throws ParserException {
            MatroskaExtractor.this.integerElement(i, j);
        }

        public void floatElement(int i, double d) throws ParserException {
            MatroskaExtractor.this.floatElement(i, d);
        }

        public void stringElement(int i, String str) throws ParserException {
            MatroskaExtractor.this.stringElement(i, str);
        }

        public void binaryElement(int i, int i2, ExtractorInput extractorInput) throws IOException, InterruptedException {
            MatroskaExtractor.this.binaryElement(i, i2, extractorInput);
        }
    }

    public void release() {
    }

    public MatroskaExtractor() {
        this(0);
    }

    public MatroskaExtractor(int i) {
        this(new DefaultEbmlReader(), i);
    }

    MatroskaExtractor(EbmlReader ebmlReader, int i) {
        this.segmentContentPosition = -1;
        this.timecodeScale = -9223372036854775807L;
        this.durationTimecode = -9223372036854775807L;
        this.durationUs = -9223372036854775807L;
        this.cuesContentPosition = -1;
        this.seekPositionAfterBuildingCues = -1;
        this.clusterTimecodeUs = -9223372036854775807L;
        this.reader = ebmlReader;
        this.reader.init(new InnerEbmlReaderOutput());
        boolean z = true;
        if ((i & 1) != 0) {
            z = false;
        }
        this.seekForCuesEnabled = z;
        this.varintReader = new VarintReader();
        this.tracks = new SparseArray();
        this.scratch = new ParsableByteArray(4);
        this.vorbisNumPageSamples = new ParsableByteArray(ByteBuffer.allocate(4).putInt(-1).array());
        this.seekEntryIdBytes = new ParsableByteArray(4);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
        this.sampleStrippedBytes = new ParsableByteArray();
        this.subtitleSample = new ParsableByteArray();
        this.encryptionInitializationVector = new ParsableByteArray(8);
        this.encryptionSubsampleData = new ParsableByteArray();
    }

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        return new Sniffer().sniff(extractorInput);
    }

    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
    }

    public void seek(long j, long j2) {
        this.clusterTimecodeUs = -9223372036854775807L;
        int i = 0;
        this.blockState = 0;
        this.reader.reset();
        this.varintReader.reset();
        resetSample();
        while (i < this.tracks.size()) {
            ((Track) this.tracks.valueAt(i)).reset();
            i++;
        }
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        int i = 0;
        this.sampleRead = false;
        boolean z = true;
        while (z && !this.sampleRead) {
            z = this.reader.read(extractorInput);
            if (z && maybeSeekForCues(positionHolder, extractorInput.getPosition())) {
                return 1;
            }
        }
        if (z) {
            return 0;
        }
        while (i < this.tracks.size()) {
            ((Track) this.tracks.valueAt(i)).outputPendingSampleMetadata();
            i++;
        }
        return -1;
    }

    /* Access modifiers changed, original: 0000 */
    public void startMasterElement(int i, long j, long j2) throws ParserException {
        if (i == 160) {
            this.sampleSeenReferenceBlock = false;
        } else if (i == 174) {
            this.currentTrack = new Track();
        } else if (i == 187) {
            this.seenClusterPositionForCurrentCuePoint = false;
        } else if (i == 19899) {
            this.seekEntryId = -1;
            this.seekEntryPosition = -1;
        } else if (i == 20533) {
            this.currentTrack.hasContentEncryption = true;
        } else if (i == 21968) {
            this.currentTrack.hasColorInfo = true;
        } else if (i == 25152) {
        } else {
            if (i == 408125543) {
                long j3 = this.segmentContentPosition;
                if (j3 == -1 || j3 == j) {
                    this.segmentContentPosition = j;
                    this.segmentContentSize = j2;
                    return;
                }
                throw new ParserException("Multiple Segment elements not supported");
            } else if (i == 475249515) {
                this.cueTimesUs = new LongArray();
                this.cueClusterPositions = new LongArray();
            } else if (i != 524531317 || this.sentSeekMap) {
            } else {
                if (!this.seekForCuesEnabled || this.cuesContentPosition == -1) {
                    this.extractorOutput.seekMap(new Unseekable(this.durationUs));
                    this.sentSeekMap = true;
                    return;
                }
                this.seekForCues = true;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void endMasterElement(int i) throws ParserException {
        if (i != 160) {
            Track track;
            long j;
            if (i == 174) {
                if (isCodecSupported(this.currentTrack.codecId)) {
                    track = this.currentTrack;
                    track.initializeOutput(this.extractorOutput, track.number);
                    SparseArray sparseArray = this.tracks;
                    Track track2 = this.currentTrack;
                    sparseArray.put(track2.number, track2);
                }
                this.currentTrack = null;
            } else if (i == 19899) {
                i = this.seekEntryId;
                if (i != -1) {
                    j = this.seekEntryPosition;
                    if (j != -1) {
                        if (i == 475249515) {
                            this.cuesContentPosition = j;
                        }
                    }
                }
                throw new ParserException("Mandatory element SeekID or SeekPosition not found");
            } else if (i == 25152) {
                track = this.currentTrack;
                if (track.hasContentEncryption) {
                    CryptoData cryptoData = track.cryptoData;
                    if (cryptoData != null) {
                        SchemeData[] schemeDataArr = new SchemeData[1];
                        schemeDataArr[0] = new SchemeData(C0131C.UUID_NIL, MimeTypes.VIDEO_WEBM, cryptoData.encryptionKey);
                        track.drmInitData = new DrmInitData(schemeDataArr);
                    } else {
                        throw new ParserException("Encrypted Track found but ContentEncKeyID was not found");
                    }
                }
            } else if (i == 28032) {
                track = this.currentTrack;
                if (track.hasContentEncryption && track.sampleStrippedBytes != null) {
                    throw new ParserException("Combining encryption and compression is not supported");
                }
            } else if (i == 357149030) {
                if (this.timecodeScale == -9223372036854775807L) {
                    this.timecodeScale = 1000000;
                }
                j = this.durationTimecode;
                if (j != -9223372036854775807L) {
                    this.durationUs = scaleTimecodeToUs(j);
                }
            } else if (i != 374648427) {
                if (i == 475249515 && !this.sentSeekMap) {
                    this.extractorOutput.seekMap(buildSeekMap());
                    this.sentSeekMap = true;
                }
            } else if (this.tracks.size() != 0) {
                this.extractorOutput.endTracks();
            } else {
                throw new ParserException("No valid tracks were found");
            }
        } else if (this.blockState == 2) {
            if (!this.sampleSeenReferenceBlock) {
                this.blockFlags |= 1;
            }
            commitSampleToOutput((Track) this.tracks.get(this.blockTrackNumber), this.blockTimeUs);
            this.blockState = 0;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void integerElement(int i, long j) throws ParserException {
        String str = " not supported";
        StringBuilder stringBuilder;
        if (i != 20529) {
            if (i != 20530) {
                boolean z = false;
                Track track;
                switch (i) {
                    case 131:
                        this.currentTrack.type = (int) j;
                        return;
                    case 136:
                        track = this.currentTrack;
                        if (j == 1) {
                            z = true;
                        }
                        track.flagDefault = z;
                        return;
                    case 155:
                        this.blockDurationUs = scaleTimecodeToUs(j);
                        return;
                    case 159:
                        this.currentTrack.channelCount = (int) j;
                        return;
                    case 176:
                        this.currentTrack.width = (int) j;
                        return;
                    case 179:
                        this.cueTimesUs.add(scaleTimecodeToUs(j));
                        return;
                    case 186:
                        this.currentTrack.height = (int) j;
                        return;
                    case 215:
                        this.currentTrack.number = (int) j;
                        return;
                    case 231:
                        this.clusterTimecodeUs = scaleTimecodeToUs(j);
                        return;
                    case 241:
                        if (!this.seenClusterPositionForCurrentCuePoint) {
                            this.cueClusterPositions.add(j);
                            this.seenClusterPositionForCurrentCuePoint = true;
                            return;
                        }
                        return;
                    case 251:
                        this.sampleSeenReferenceBlock = true;
                        return;
                    case 16980:
                        if (j != 3) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("ContentCompAlgo ");
                            stringBuilder.append(j);
                            stringBuilder.append(str);
                            throw new ParserException(stringBuilder.toString());
                        }
                        return;
                    case 17029:
                        if (j < 1 || j > 2) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("DocTypeReadVersion ");
                            stringBuilder.append(j);
                            stringBuilder.append(str);
                            throw new ParserException(stringBuilder.toString());
                        }
                        return;
                    case 17143:
                        if (j != 1) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("EBMLReadVersion ");
                            stringBuilder.append(j);
                            stringBuilder.append(str);
                            throw new ParserException(stringBuilder.toString());
                        }
                        return;
                    case 18401:
                        if (j != 5) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("ContentEncAlgo ");
                            stringBuilder.append(j);
                            stringBuilder.append(str);
                            throw new ParserException(stringBuilder.toString());
                        }
                        return;
                    case 18408:
                        if (j != 1) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("AESSettingsCipherMode ");
                            stringBuilder.append(j);
                            stringBuilder.append(str);
                            throw new ParserException(stringBuilder.toString());
                        }
                        return;
                    case 21420:
                        this.seekEntryPosition = j + this.segmentContentPosition;
                        return;
                    case 21432:
                        i = (int) j;
                        if (i == 0) {
                            this.currentTrack.stereoMode = 0;
                            return;
                        } else if (i == 1) {
                            this.currentTrack.stereoMode = 2;
                            return;
                        } else if (i == 3) {
                            this.currentTrack.stereoMode = 1;
                            return;
                        } else if (i == 15) {
                            this.currentTrack.stereoMode = 3;
                            return;
                        } else {
                            return;
                        }
                    case 21680:
                        this.currentTrack.displayWidth = (int) j;
                        return;
                    case 21682:
                        this.currentTrack.displayUnit = (int) j;
                        return;
                    case 21690:
                        this.currentTrack.displayHeight = (int) j;
                        return;
                    case 21930:
                        track = this.currentTrack;
                        if (j == 1) {
                            z = true;
                        }
                        track.flagForced = z;
                        return;
                    case 22186:
                        this.currentTrack.codecDelayNs = j;
                        return;
                    case 22203:
                        this.currentTrack.seekPreRollNs = j;
                        return;
                    case 25188:
                        this.currentTrack.audioBitDepth = (int) j;
                        return;
                    case 30321:
                        i = (int) j;
                        if (i == 0) {
                            this.currentTrack.projectionType = 0;
                            return;
                        } else if (i == 1) {
                            this.currentTrack.projectionType = 1;
                            return;
                        } else if (i == 2) {
                            this.currentTrack.projectionType = 2;
                            return;
                        } else if (i == 3) {
                            this.currentTrack.projectionType = 3;
                            return;
                        } else {
                            return;
                        }
                    case 2352003:
                        this.currentTrack.defaultSampleDurationNs = (int) j;
                        return;
                    case 2807729:
                        this.timecodeScale = j;
                        return;
                    default:
                        switch (i) {
                            case 21945:
                                i = (int) j;
                                if (i == 1) {
                                    this.currentTrack.colorRange = 2;
                                    return;
                                } else if (i == 2) {
                                    this.currentTrack.colorRange = 1;
                                    return;
                                } else {
                                    return;
                                }
                            case 21946:
                                i = (int) j;
                                if (i != 1) {
                                    if (i == 16) {
                                        this.currentTrack.colorTransfer = 6;
                                        return;
                                    } else if (i == 18) {
                                        this.currentTrack.colorTransfer = 7;
                                        return;
                                    } else if (!(i == 6 || i == 7)) {
                                        return;
                                    }
                                }
                                this.currentTrack.colorTransfer = 3;
                                return;
                            case 21947:
                                track = this.currentTrack;
                                track.hasColorInfo = true;
                                int i2 = (int) j;
                                if (i2 == 1) {
                                    track.colorSpace = 1;
                                    return;
                                } else if (i2 == 9) {
                                    track.colorSpace = 6;
                                    return;
                                } else if (i2 == 4 || i2 == 5 || i2 == 6 || i2 == 7) {
                                    this.currentTrack.colorSpace = 2;
                                    return;
                                } else {
                                    return;
                                }
                            case 21948:
                                this.currentTrack.maxContentLuminance = (int) j;
                                return;
                            case 21949:
                                this.currentTrack.maxFrameAverageLuminance = (int) j;
                                return;
                            default:
                                return;
                        }
                }
            } else if (j != 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("ContentEncodingScope ");
                stringBuilder.append(j);
                stringBuilder.append(str);
                throw new ParserException(stringBuilder.toString());
            }
        } else if (j != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("ContentEncodingOrder ");
            stringBuilder.append(j);
            stringBuilder.append(str);
            throw new ParserException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void floatElement(int i, double d) {
        if (i == 181) {
            this.currentTrack.sampleRate = (int) d;
        } else if (i != 17545) {
            switch (i) {
                case 21969:
                    this.currentTrack.primaryRChromaticityX = (float) d;
                    return;
                case 21970:
                    this.currentTrack.primaryRChromaticityY = (float) d;
                    return;
                case 21971:
                    this.currentTrack.primaryGChromaticityX = (float) d;
                    return;
                case 21972:
                    this.currentTrack.primaryGChromaticityY = (float) d;
                    return;
                case 21973:
                    this.currentTrack.primaryBChromaticityX = (float) d;
                    return;
                case 21974:
                    this.currentTrack.primaryBChromaticityY = (float) d;
                    return;
                case 21975:
                    this.currentTrack.whitePointChromaticityX = (float) d;
                    return;
                case 21976:
                    this.currentTrack.whitePointChromaticityY = (float) d;
                    return;
                case 21977:
                    this.currentTrack.maxMasteringLuminance = (float) d;
                    return;
                case 21978:
                    this.currentTrack.minMasteringLuminance = (float) d;
                    return;
                default:
                    switch (i) {
                        case 30323:
                            this.currentTrack.projectionPoseYaw = (float) d;
                            return;
                        case 30324:
                            this.currentTrack.projectionPosePitch = (float) d;
                            return;
                        case 30325:
                            this.currentTrack.projectionPoseRoll = (float) d;
                            return;
                        default:
                            return;
                    }
            }
        } else {
            this.durationTimecode = (long) d;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void stringElement(int i, String str) throws ParserException {
        if (i == 134) {
            this.currentTrack.codecId = str;
        } else if (i != 17026) {
            if (i == 21358) {
                this.currentTrack.name = str;
            } else if (i == 2274716) {
                this.currentTrack.language = str;
            }
        } else if (!"webm".equals(str) && !"matroska".equals(str)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DocType ");
            stringBuilder.append(str);
            stringBuilder.append(" not supported");
            throw new ParserException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0239  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0237  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0256  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0253  */
    /* JADX WARNING: Missing block: B:72:0x0200, code skipped:
            throw new com.google.android.exoplayer2.ParserException("EBML lacing sample size out of range.");
     */
    public void binaryElement(int r20, int r21, com.google.android.exoplayer2.extractor.ExtractorInput r22) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r19 = this;
        r0 = r19;
        r1 = r20;
        r2 = r21;
        r3 = r22;
        r4 = 161; // 0xa1 float:2.26E-43 double:7.95E-322;
        r5 = 163; // 0xa3 float:2.28E-43 double:8.05E-322;
        r6 = 4;
        r7 = 0;
        r8 = 1;
        if (r1 == r4) goto L_0x0094;
    L_0x0011:
        if (r1 == r5) goto L_0x0094;
    L_0x0013:
        r4 = 16981; // 0x4255 float:2.3795E-41 double:8.3897E-320;
        if (r1 == r4) goto L_0x0087;
    L_0x0017:
        r4 = 18402; // 0x47e2 float:2.5787E-41 double:9.092E-320;
        if (r1 == r4) goto L_0x0077;
    L_0x001b:
        r4 = 21419; // 0x53ab float:3.0014E-41 double:1.05824E-319;
        if (r1 == r4) goto L_0x0058;
    L_0x001f:
        r4 = 25506; // 0x63a2 float:3.5742E-41 double:1.26016E-319;
        if (r1 == r4) goto L_0x004b;
    L_0x0023:
        r4 = 30322; // 0x7672 float:4.249E-41 double:1.4981E-319;
        if (r1 != r4) goto L_0x0034;
    L_0x0027:
        r1 = r0.currentTrack;
        r4 = new byte[r2];
        r1.projectionData = r4;
        r1 = r1.projectionData;
        r3.readFully(r1, r7, r2);
        goto L_0x02b4;
    L_0x0034:
        r2 = new com.google.android.exoplayer2.ParserException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Unexpected id: ";
        r3.append(r4);
        r3.append(r1);
        r1 = r3.toString();
        r2.<init>(r1);
        throw r2;
    L_0x004b:
        r1 = r0.currentTrack;
        r4 = new byte[r2];
        r1.codecPrivate = r4;
        r1 = r1.codecPrivate;
        r3.readFully(r1, r7, r2);
        goto L_0x02b4;
    L_0x0058:
        r1 = r0.seekEntryIdBytes;
        r1 = r1.data;
        java.util.Arrays.fill(r1, r7);
        r1 = r0.seekEntryIdBytes;
        r1 = r1.data;
        r6 = r6 - r2;
        r3.readFully(r1, r6, r2);
        r1 = r0.seekEntryIdBytes;
        r1.setPosition(r7);
        r1 = r0.seekEntryIdBytes;
        r1 = r1.readUnsignedInt();
        r2 = (int) r1;
        r0.seekEntryId = r2;
        goto L_0x02b4;
    L_0x0077:
        r1 = new byte[r2];
        r3.readFully(r1, r7, r2);
        r2 = r0.currentTrack;
        r3 = new com.google.android.exoplayer2.extractor.TrackOutput$CryptoData;
        r3.<init>(r8, r1, r7, r7);
        r2.cryptoData = r3;
        goto L_0x02b4;
    L_0x0087:
        r1 = r0.currentTrack;
        r4 = new byte[r2];
        r1.sampleStrippedBytes = r4;
        r1 = r1.sampleStrippedBytes;
        r3.readFully(r1, r7, r2);
        goto L_0x02b4;
    L_0x0094:
        r4 = r0.blockState;
        r9 = 8;
        if (r4 != 0) goto L_0x00b9;
    L_0x009a:
        r4 = r0.varintReader;
        r10 = r4.readUnsignedVarint(r3, r7, r8, r9);
        r4 = (int) r10;
        r0.blockTrackNumber = r4;
        r4 = r0.varintReader;
        r4 = r4.getLastLength();
        r0.blockTrackNumberLength = r4;
        r10 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r0.blockDurationUs = r10;
        r0.blockState = r8;
        r4 = r0.scratch;
        r4.reset();
    L_0x00b9:
        r4 = r0.tracks;
        r10 = r0.blockTrackNumber;
        r4 = r4.get(r10);
        r4 = (com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.Track) r4;
        if (r4 != 0) goto L_0x00cf;
    L_0x00c5:
        r1 = r0.blockTrackNumberLength;
        r1 = r2 - r1;
        r3.skipFully(r1);
        r0.blockState = r7;
        return;
    L_0x00cf:
        r10 = r0.blockState;
        if (r10 != r8) goto L_0x0281;
    L_0x00d3:
        r10 = 3;
        r0.readScratch(r3, r10);
        r11 = r0.scratch;
        r11 = r11.data;
        r12 = 2;
        r11 = r11[r12];
        r11 = r11 & 6;
        r11 = r11 >> r8;
        r13 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r11 != 0) goto L_0x00fa;
    L_0x00e5:
        r0.blockLacingSampleCount = r8;
        r6 = r0.blockLacingSampleSizes;
        r6 = ensureArrayCapacity(r6, r8);
        r0.blockLacingSampleSizes = r6;
        r6 = r0.blockLacingSampleSizes;
        r11 = r0.blockTrackNumberLength;
        r2 = r2 - r11;
        r2 = r2 - r10;
        r6[r7] = r2;
    L_0x00f7:
        r6 = 1;
        goto L_0x0214;
    L_0x00fa:
        if (r1 != r5) goto L_0x0279;
    L_0x00fc:
        r0.readScratch(r3, r6);
        r14 = r0.scratch;
        r14 = r14.data;
        r14 = r14[r10];
        r14 = r14 & r13;
        r14 = r14 + r8;
        r0.blockLacingSampleCount = r14;
        r14 = r0.blockLacingSampleSizes;
        r15 = r0.blockLacingSampleCount;
        r14 = ensureArrayCapacity(r14, r15);
        r0.blockLacingSampleSizes = r14;
        if (r11 != r12) goto L_0x0122;
    L_0x0115:
        r10 = r0.blockTrackNumberLength;
        r2 = r2 - r10;
        r2 = r2 - r6;
        r6 = r0.blockLacingSampleCount;
        r2 = r2 / r6;
        r10 = r0.blockLacingSampleSizes;
        java.util.Arrays.fill(r10, r7, r6, r2);
        goto L_0x00f7;
    L_0x0122:
        if (r11 != r8) goto L_0x0159;
    L_0x0124:
        r6 = 0;
        r10 = 4;
        r11 = 0;
    L_0x0127:
        r14 = r0.blockLacingSampleCount;
        r15 = r14 + -1;
        if (r6 >= r15) goto L_0x014e;
    L_0x012d:
        r14 = r0.blockLacingSampleSizes;
        r14[r6] = r7;
    L_0x0131:
        r10 = r10 + r8;
        r0.readScratch(r3, r10);
        r14 = r0.scratch;
        r14 = r14.data;
        r15 = r10 + -1;
        r14 = r14[r15];
        r14 = r14 & r13;
        r15 = r0.blockLacingSampleSizes;
        r16 = r15[r6];
        r16 = r16 + r14;
        r15[r6] = r16;
        if (r14 == r13) goto L_0x0131;
    L_0x0148:
        r14 = r15[r6];
        r11 = r11 + r14;
        r6 = r6 + 1;
        goto L_0x0127;
    L_0x014e:
        r6 = r0.blockLacingSampleSizes;
        r14 = r14 - r8;
        r15 = r0.blockTrackNumberLength;
        r2 = r2 - r15;
        r2 = r2 - r10;
        r2 = r2 - r11;
        r6[r14] = r2;
        goto L_0x00f7;
    L_0x0159:
        if (r11 != r10) goto L_0x0262;
    L_0x015b:
        r6 = 0;
        r10 = 4;
        r11 = 0;
    L_0x015e:
        r14 = r0.blockLacingSampleCount;
        r15 = r14 + -1;
        if (r6 >= r15) goto L_0x0209;
    L_0x0164:
        r14 = r0.blockLacingSampleSizes;
        r14[r6] = r7;
        r10 = r10 + 1;
        r0.readScratch(r3, r10);
        r14 = r0.scratch;
        r14 = r14.data;
        r15 = r10 + -1;
        r14 = r14[r15];
        if (r14 == 0) goto L_0x0201;
    L_0x0177:
        r16 = 0;
        r14 = 0;
    L_0x017a:
        if (r14 >= r9) goto L_0x01cc;
    L_0x017c:
        r18 = 7 - r14;
        r18 = r8 << r18;
        r5 = r0.scratch;
        r5 = r5.data;
        r5 = r5[r15];
        r5 = r5 & r18;
        if (r5 == 0) goto L_0x01c2;
    L_0x018a:
        r10 = r10 + r14;
        r0.readScratch(r3, r10);
        r5 = r0.scratch;
        r5 = r5.data;
        r16 = r15 + 1;
        r5 = r5[r15];
        r5 = r5 & r13;
        r15 = r18 ^ -1;
        r5 = r5 & r15;
        r7 = (long) r5;
        r5 = r16;
    L_0x019d:
        r16 = r7;
        if (r5 >= r10) goto L_0x01b4;
    L_0x01a1:
        r7 = r16 << r9;
        r15 = r0.scratch;
        r15 = r15.data;
        r16 = r5 + 1;
        r5 = r15[r5];
        r5 = r5 & r13;
        r12 = (long) r5;
        r7 = r7 | r12;
        r5 = r16;
        r12 = 2;
        r13 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        goto L_0x019d;
    L_0x01b4:
        if (r6 <= 0) goto L_0x01cc;
    L_0x01b6:
        r14 = r14 * 7;
        r14 = r14 + 6;
        r7 = 1;
        r12 = r7 << r14;
        r12 = r12 - r7;
        r16 = r16 - r12;
        goto L_0x01cc;
    L_0x01c2:
        r14 = r14 + 1;
        r5 = 163; // 0xa3 float:2.28E-43 double:8.05E-322;
        r7 = 0;
        r8 = 1;
        r12 = 2;
        r13 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        goto L_0x017a;
    L_0x01cc:
        r7 = r16;
        r12 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r5 = (r7 > r12 ? 1 : (r7 == r12 ? 0 : -1));
        if (r5 < 0) goto L_0x01f9;
    L_0x01d5:
        r12 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r5 = (r7 > r12 ? 1 : (r7 == r12 ? 0 : -1));
        if (r5 > 0) goto L_0x01f9;
    L_0x01dc:
        r5 = (int) r7;
        r7 = r0.blockLacingSampleSizes;
        if (r6 != 0) goto L_0x01e2;
    L_0x01e1:
        goto L_0x01e7;
    L_0x01e2:
        r8 = r6 + -1;
        r8 = r7[r8];
        r5 = r5 + r8;
    L_0x01e7:
        r7[r6] = r5;
        r5 = r0.blockLacingSampleSizes;
        r5 = r5[r6];
        r11 = r11 + r5;
        r6 = r6 + 1;
        r5 = 163; // 0xa3 float:2.28E-43 double:8.05E-322;
        r7 = 0;
        r8 = 1;
        r12 = 2;
        r13 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        goto L_0x015e;
    L_0x01f9:
        r1 = new com.google.android.exoplayer2.ParserException;
        r2 = "EBML lacing sample size out of range.";
        r1.<init>(r2);
        throw r1;
    L_0x0201:
        r1 = new com.google.android.exoplayer2.ParserException;
        r2 = "No valid varint length mask found";
        r1.<init>(r2);
        throw r1;
    L_0x0209:
        r5 = r0.blockLacingSampleSizes;
        r6 = 1;
        r14 = r14 - r6;
        r7 = r0.blockTrackNumberLength;
        r2 = r2 - r7;
        r2 = r2 - r10;
        r2 = r2 - r11;
        r5[r14] = r2;
    L_0x0214:
        r2 = r0.scratch;
        r2 = r2.data;
        r5 = 0;
        r7 = r2[r5];
        r5 = r7 << 8;
        r2 = r2[r6];
        r6 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r2 = r2 & r6;
        r2 = r2 | r5;
        r5 = r0.clusterTimecodeUs;
        r7 = (long) r2;
        r7 = r0.scaleTimecodeToUs(r7);
        r5 = r5 + r7;
        r0.blockTimeUs = r5;
        r2 = r0.scratch;
        r2 = r2.data;
        r5 = 2;
        r2 = r2[r5];
        r2 = r2 & r9;
        if (r2 != r9) goto L_0x0239;
    L_0x0237:
        r2 = 1;
        goto L_0x023a;
    L_0x0239:
        r2 = 0;
    L_0x023a:
        r6 = r4.type;
        if (r6 == r5) goto L_0x0250;
    L_0x023e:
        r6 = 163; // 0xa3 float:2.28E-43 double:8.05E-322;
        if (r1 != r6) goto L_0x024e;
    L_0x0242:
        r6 = r0.scratch;
        r6 = r6.data;
        r6 = r6[r5];
        r5 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r6 = r6 & r5;
        if (r6 != r5) goto L_0x024e;
    L_0x024d:
        goto L_0x0250;
    L_0x024e:
        r5 = 0;
        goto L_0x0251;
    L_0x0250:
        r5 = 1;
    L_0x0251:
        if (r2 == 0) goto L_0x0256;
    L_0x0253:
        r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        goto L_0x0257;
    L_0x0256:
        r7 = 0;
    L_0x0257:
        r2 = r5 | r7;
        r0.blockFlags = r2;
        r2 = 2;
        r0.blockState = r2;
        r2 = 0;
        r0.blockLacingSampleIndex = r2;
        goto L_0x0281;
    L_0x0262:
        r1 = new com.google.android.exoplayer2.ParserException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Unexpected lacing value: ";
        r2.append(r3);
        r2.append(r11);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x0279:
        r1 = new com.google.android.exoplayer2.ParserException;
        r2 = "Lacing only supported in SimpleBlocks.";
        r1.<init>(r2);
        throw r1;
    L_0x0281:
        r2 = 163; // 0xa3 float:2.28E-43 double:8.05E-322;
        if (r1 != r2) goto L_0x02ac;
    L_0x0285:
        r1 = r0.blockLacingSampleIndex;
        r2 = r0.blockLacingSampleCount;
        if (r1 >= r2) goto L_0x02a8;
    L_0x028b:
        r2 = r0.blockLacingSampleSizes;
        r1 = r2[r1];
        r0.writeSampleData(r3, r4, r1);
        r1 = r0.blockTimeUs;
        r5 = r0.blockLacingSampleIndex;
        r6 = r4.defaultSampleDurationNs;
        r5 = r5 * r6;
        r5 = r5 / 1000;
        r5 = (long) r5;
        r1 = r1 + r5;
        r0.commitSampleToOutput(r4, r1);
        r1 = r0.blockLacingSampleIndex;
        r2 = 1;
        r1 = r1 + r2;
        r0.blockLacingSampleIndex = r1;
        goto L_0x0285;
    L_0x02a8:
        r1 = 0;
        r0.blockState = r1;
        goto L_0x02b4;
    L_0x02ac:
        r1 = 0;
        r2 = r0.blockLacingSampleSizes;
        r1 = r2[r1];
        r0.writeSampleData(r3, r4, r1);
    L_0x02b4:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.binaryElement(int, int, com.google.android.exoplayer2.extractor.ExtractorInput):void");
    }

    private void commitSampleToOutput(Track track, long j) {
        Track track2 = track;
        TrueHdSampleRechunker trueHdSampleRechunker = track2.trueHdSampleRechunker;
        if (trueHdSampleRechunker != null) {
            trueHdSampleRechunker.sampleMetadata(track2, j);
        } else {
            long j2 = j;
            Track track3;
            if ("S_TEXT/UTF8".equals(track2.codecId)) {
                track3 = track;
                commitSubtitleSample(track3, "%02d:%02d:%02d,%03d", 19, 1000, SUBRIP_TIMECODE_EMPTY);
            } else {
                if ("S_TEXT/ASS".equals(track2.codecId)) {
                    track3 = track;
                    commitSubtitleSample(track3, "%01d:%02d:%02d:%02d", 21, 10000, SSA_TIMECODE_EMPTY);
                }
            }
            track2.output.sampleMetadata(j, this.blockFlags, this.sampleBytesWritten, 0, track2.cryptoData);
        }
        this.sampleRead = true;
        resetSample();
    }

    private void resetSample() {
        this.sampleBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        this.sampleEncodingHandled = false;
        this.sampleSignalByteRead = false;
        this.samplePartitionCountRead = false;
        this.samplePartitionCount = 0;
        this.sampleSignalByte = (byte) 0;
        this.sampleInitializationVectorRead = false;
        this.sampleStrippedBytes.reset();
    }

    private void readScratch(ExtractorInput extractorInput, int i) throws IOException, InterruptedException {
        if (this.scratch.limit() < i) {
            ParsableByteArray parsableByteArray;
            if (this.scratch.capacity() < i) {
                parsableByteArray = this.scratch;
                byte[] bArr = parsableByteArray.data;
                parsableByteArray.reset(Arrays.copyOf(bArr, Math.max(bArr.length * 2, i)), this.scratch.limit());
            }
            parsableByteArray = this.scratch;
            extractorInput.readFully(parsableByteArray.data, parsableByteArray.limit(), i - this.scratch.limit());
            this.scratch.setLimit(i);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:82:0x01f4  */
    private void writeSampleData(com.google.android.exoplayer2.extractor.ExtractorInput r11, com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.Track r12, int r13) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r10 = this;
        r0 = r12.codecId;
        r1 = "S_TEXT/UTF8";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0010;
    L_0x000a:
        r12 = SUBRIP_PREFIX;
        r10.writeSubtitleSampleData(r11, r12, r13);
        return;
    L_0x0010:
        r0 = r12.codecId;
        r1 = "S_TEXT/ASS";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0020;
    L_0x001a:
        r12 = SSA_PREFIX;
        r10.writeSubtitleSampleData(r11, r12, r13);
        return;
    L_0x0020:
        r0 = r12.output;
        r1 = r10.sampleEncodingHandled;
        r2 = 4;
        r3 = 2;
        r4 = 1;
        r5 = 0;
        if (r1 != 0) goto L_0x016e;
    L_0x002a:
        r1 = r12.hasContentEncryption;
        if (r1 == 0) goto L_0x0162;
    L_0x002e:
        r1 = r10.blockFlags;
        r6 = -1073741825; // 0xffffffffbfffffff float:-1.9999999 double:NaN;
        r1 = r1 & r6;
        r10.blockFlags = r1;
        r1 = r10.sampleSignalByteRead;
        r6 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r1 != 0) goto L_0x0060;
    L_0x003c:
        r1 = r10.scratch;
        r1 = r1.data;
        r11.readFully(r1, r5, r4);
        r1 = r10.sampleBytesRead;
        r1 = r1 + r4;
        r10.sampleBytesRead = r1;
        r1 = r10.scratch;
        r1 = r1.data;
        r7 = r1[r5];
        r7 = r7 & r6;
        if (r7 == r6) goto L_0x0058;
    L_0x0051:
        r1 = r1[r5];
        r10.sampleSignalByte = r1;
        r10.sampleSignalByteRead = r4;
        goto L_0x0060;
    L_0x0058:
        r11 = new com.google.android.exoplayer2.ParserException;
        r12 = "Extension bit is set in signal byte";
        r11.<init>(r12);
        throw r11;
    L_0x0060:
        r1 = r10.sampleSignalByte;
        r1 = r1 & r4;
        if (r1 != r4) goto L_0x0067;
    L_0x0065:
        r1 = 1;
        goto L_0x0068;
    L_0x0067:
        r1 = 0;
    L_0x0068:
        if (r1 == 0) goto L_0x016c;
    L_0x006a:
        r1 = r10.sampleSignalByte;
        r1 = r1 & r3;
        if (r1 != r3) goto L_0x0071;
    L_0x006f:
        r1 = 1;
        goto L_0x0072;
    L_0x0071:
        r1 = 0;
    L_0x0072:
        r7 = r10.blockFlags;
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = r7 | r8;
        r10.blockFlags = r7;
        r7 = r10.sampleInitializationVectorRead;
        if (r7 != 0) goto L_0x00b7;
    L_0x007d:
        r7 = r10.encryptionInitializationVector;
        r7 = r7.data;
        r8 = 8;
        r11.readFully(r7, r5, r8);
        r7 = r10.sampleBytesRead;
        r7 = r7 + r8;
        r10.sampleBytesRead = r7;
        r10.sampleInitializationVectorRead = r4;
        r7 = r10.scratch;
        r7 = r7.data;
        if (r1 == 0) goto L_0x0094;
    L_0x0093:
        goto L_0x0095;
    L_0x0094:
        r6 = 0;
    L_0x0095:
        r6 = r6 | r8;
        r6 = (byte) r6;
        r7[r5] = r6;
        r6 = r10.scratch;
        r6.setPosition(r5);
        r6 = r10.scratch;
        r0.sampleData(r6, r4);
        r6 = r10.sampleBytesWritten;
        r6 = r6 + r4;
        r10.sampleBytesWritten = r6;
        r6 = r10.encryptionInitializationVector;
        r6.setPosition(r5);
        r6 = r10.encryptionInitializationVector;
        r0.sampleData(r6, r8);
        r6 = r10.sampleBytesWritten;
        r6 = r6 + r8;
        r10.sampleBytesWritten = r6;
    L_0x00b7:
        if (r1 == 0) goto L_0x016c;
    L_0x00b9:
        r1 = r10.samplePartitionCountRead;
        if (r1 != 0) goto L_0x00d8;
    L_0x00bd:
        r1 = r10.scratch;
        r1 = r1.data;
        r11.readFully(r1, r5, r4);
        r1 = r10.sampleBytesRead;
        r1 = r1 + r4;
        r10.sampleBytesRead = r1;
        r1 = r10.scratch;
        r1.setPosition(r5);
        r1 = r10.scratch;
        r1 = r1.readUnsignedByte();
        r10.samplePartitionCount = r1;
        r10.samplePartitionCountRead = r4;
    L_0x00d8:
        r1 = r10.samplePartitionCount;
        r1 = r1 * 4;
        r6 = r10.scratch;
        r6.reset(r1);
        r6 = r10.scratch;
        r6 = r6.data;
        r11.readFully(r6, r5, r1);
        r6 = r10.sampleBytesRead;
        r6 = r6 + r1;
        r10.sampleBytesRead = r6;
        r1 = r10.samplePartitionCount;
        r1 = r1 / r3;
        r1 = r1 + r4;
        r1 = (short) r1;
        r6 = r1 * 6;
        r6 = r6 + r3;
        r7 = r10.encryptionSubsampleDataBuffer;
        if (r7 == 0) goto L_0x00ff;
    L_0x00f9:
        r7 = r7.capacity();
        if (r7 >= r6) goto L_0x0105;
    L_0x00ff:
        r7 = java.nio.ByteBuffer.allocate(r6);
        r10.encryptionSubsampleDataBuffer = r7;
    L_0x0105:
        r7 = r10.encryptionSubsampleDataBuffer;
        r7.position(r5);
        r7 = r10.encryptionSubsampleDataBuffer;
        r7.putShort(r1);
        r1 = 0;
        r7 = 0;
    L_0x0111:
        r8 = r10.samplePartitionCount;
        if (r1 >= r8) goto L_0x0133;
    L_0x0115:
        r8 = r10.scratch;
        r8 = r8.readUnsignedIntToInt();
        r9 = r1 % 2;
        if (r9 != 0) goto L_0x0128;
    L_0x011f:
        r9 = r10.encryptionSubsampleDataBuffer;
        r7 = r8 - r7;
        r7 = (short) r7;
        r9.putShort(r7);
        goto L_0x012f;
    L_0x0128:
        r9 = r10.encryptionSubsampleDataBuffer;
        r7 = r8 - r7;
        r9.putInt(r7);
    L_0x012f:
        r1 = r1 + 1;
        r7 = r8;
        goto L_0x0111;
    L_0x0133:
        r1 = r10.sampleBytesRead;
        r1 = r13 - r1;
        r1 = r1 - r7;
        r8 = r8 % r3;
        if (r8 != r4) goto L_0x0141;
    L_0x013b:
        r7 = r10.encryptionSubsampleDataBuffer;
        r7.putInt(r1);
        goto L_0x014c;
    L_0x0141:
        r7 = r10.encryptionSubsampleDataBuffer;
        r1 = (short) r1;
        r7.putShort(r1);
        r1 = r10.encryptionSubsampleDataBuffer;
        r1.putInt(r5);
    L_0x014c:
        r1 = r10.encryptionSubsampleData;
        r7 = r10.encryptionSubsampleDataBuffer;
        r7 = r7.array();
        r1.reset(r7, r6);
        r1 = r10.encryptionSubsampleData;
        r0.sampleData(r1, r6);
        r1 = r10.sampleBytesWritten;
        r1 = r1 + r6;
        r10.sampleBytesWritten = r1;
        goto L_0x016c;
    L_0x0162:
        r1 = r12.sampleStrippedBytes;
        if (r1 == 0) goto L_0x016c;
    L_0x0166:
        r6 = r10.sampleStrippedBytes;
        r7 = r1.length;
        r6.reset(r1, r7);
    L_0x016c:
        r10.sampleEncodingHandled = r4;
    L_0x016e:
        r1 = r10.sampleStrippedBytes;
        r1 = r1.limit();
        r13 = r13 + r1;
        r1 = r12.codecId;
        r6 = "V_MPEG4/ISO/AVC";
        r1 = r6.equals(r1);
        if (r1 != 0) goto L_0x01ac;
    L_0x017f:
        r1 = r12.codecId;
        r6 = "V_MPEGH/ISO/HEVC";
        r1 = r6.equals(r1);
        if (r1 == 0) goto L_0x018a;
    L_0x0189:
        goto L_0x01ac;
    L_0x018a:
        r1 = r12.trueHdSampleRechunker;
        if (r1 == 0) goto L_0x01a2;
    L_0x018e:
        r1 = r10.sampleStrippedBytes;
        r1 = r1.limit();
        if (r1 != 0) goto L_0x0197;
    L_0x0196:
        goto L_0x0198;
    L_0x0197:
        r4 = 0;
    L_0x0198:
        com.google.android.exoplayer2.util.Assertions.checkState(r4);
        r1 = r12.trueHdSampleRechunker;
        r3 = r10.blockFlags;
        r1.startSample(r11, r3, r13);
    L_0x01a2:
        r1 = r10.sampleBytesRead;
        if (r1 >= r13) goto L_0x01ea;
    L_0x01a6:
        r1 = r13 - r1;
        r10.readToOutput(r11, r0, r1);
        goto L_0x01a2;
    L_0x01ac:
        r1 = r10.nalLength;
        r1 = r1.data;
        r1[r5] = r5;
        r1[r4] = r5;
        r1[r3] = r5;
        r3 = r12.nalUnitLengthFieldLength;
        r4 = 4 - r3;
    L_0x01ba:
        r6 = r10.sampleBytesRead;
        if (r6 >= r13) goto L_0x01ea;
    L_0x01be:
        r6 = r10.sampleCurrentNalBytesRemaining;
        if (r6 != 0) goto L_0x01e2;
    L_0x01c2:
        r10.readToTarget(r11, r1, r4, r3);
        r6 = r10.nalLength;
        r6.setPosition(r5);
        r6 = r10.nalLength;
        r6 = r6.readUnsignedIntToInt();
        r10.sampleCurrentNalBytesRemaining = r6;
        r6 = r10.nalStartCode;
        r6.setPosition(r5);
        r6 = r10.nalStartCode;
        r0.sampleData(r6, r2);
        r6 = r10.sampleBytesWritten;
        r6 = r6 + r2;
        r10.sampleBytesWritten = r6;
        goto L_0x01ba;
    L_0x01e2:
        r7 = r10.readToOutput(r11, r0, r6);
        r6 = r6 - r7;
        r10.sampleCurrentNalBytesRemaining = r6;
        goto L_0x01ba;
    L_0x01ea:
        r11 = r12.codecId;
        r12 = "A_VORBIS";
        r11 = r12.equals(r11);
        if (r11 == 0) goto L_0x0203;
    L_0x01f4:
        r11 = r10.vorbisNumPageSamples;
        r11.setPosition(r5);
        r11 = r10.vorbisNumPageSamples;
        r0.sampleData(r11, r2);
        r11 = r10.sampleBytesWritten;
        r11 = r11 + r2;
        r10.sampleBytesWritten = r11;
    L_0x0203:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.writeSampleData(com.google.android.exoplayer2.extractor.ExtractorInput, com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor$Track, int):void");
    }

    private void writeSubtitleSampleData(ExtractorInput extractorInput, byte[] bArr, int i) throws IOException, InterruptedException {
        int length = bArr.length + i;
        if (this.subtitleSample.capacity() < length) {
            this.subtitleSample.data = Arrays.copyOf(bArr, length + i);
        } else {
            System.arraycopy(bArr, 0, this.subtitleSample.data, 0, bArr.length);
        }
        extractorInput.readFully(this.subtitleSample.data, bArr.length, i);
        this.subtitleSample.reset(length);
    }

    private void commitSubtitleSample(Track track, String str, int i, long j, byte[] bArr) {
        setSampleDuration(this.subtitleSample.data, this.blockDurationUs, str, i, j, bArr);
        TrackOutput trackOutput = track.output;
        ParsableByteArray parsableByteArray = this.subtitleSample;
        trackOutput.sampleData(parsableByteArray, parsableByteArray.limit());
        this.sampleBytesWritten += this.subtitleSample.limit();
    }

    private static void setSampleDuration(byte[] bArr, long j, String str, int i, long j2, byte[] bArr2) {
        Object obj;
        Object obj2;
        int i2;
        if (j == -9223372036854775807L) {
            obj = bArr2;
            obj2 = obj;
        } else {
            long j3 = j - (((long) (((int) (j / 3600000000L)) * 3600)) * 1000000);
            j3 -= ((long) (((int) (j3 / 60000000)) * 60)) * 1000000;
            i2 = (int) ((j3 - (((long) ((int) (j3 / 1000000))) * 1000000)) / j2);
            Object[] objArr = new Object[]{Integer.valueOf(r2), Integer.valueOf(r1), Integer.valueOf(r8), Integer.valueOf(i2)};
            String str2 = str;
            obj2 = Util.getUtf8Bytes(String.format(Locale.US, str, objArr));
            obj = bArr2;
        }
        byte[] bArr3 = bArr;
        i2 = i;
        System.arraycopy(obj2, 0, bArr, i, obj.length);
    }

    private void readToTarget(ExtractorInput extractorInput, byte[] bArr, int i, int i2) throws IOException, InterruptedException {
        int min = Math.min(i2, this.sampleStrippedBytes.bytesLeft());
        extractorInput.readFully(bArr, i + min, i2 - min);
        if (min > 0) {
            this.sampleStrippedBytes.readBytes(bArr, i, min);
        }
        this.sampleBytesRead += i2;
    }

    private int readToOutput(ExtractorInput extractorInput, TrackOutput trackOutput, int i) throws IOException, InterruptedException {
        int min;
        int bytesLeft = this.sampleStrippedBytes.bytesLeft();
        if (bytesLeft > 0) {
            min = Math.min(i, bytesLeft);
            trackOutput.sampleData(this.sampleStrippedBytes, min);
        } else {
            min = trackOutput.sampleData(extractorInput, i, false);
        }
        this.sampleBytesRead += min;
        this.sampleBytesWritten += min;
        return min;
    }

    private SeekMap buildSeekMap() {
        if (!(this.segmentContentPosition == -1 || this.durationUs == -9223372036854775807L)) {
            LongArray longArray = this.cueTimesUs;
            if (!(longArray == null || longArray.size() == 0)) {
                longArray = this.cueClusterPositions;
                if (longArray != null && longArray.size() == this.cueTimesUs.size()) {
                    int i;
                    int size = this.cueTimesUs.size();
                    int[] iArr = new int[size];
                    long[] jArr = new long[size];
                    long[] jArr2 = new long[size];
                    long[] jArr3 = new long[size];
                    int i2 = 0;
                    for (i = 0; i < size; i++) {
                        jArr3[i] = this.cueTimesUs.get(i);
                        jArr[i] = this.segmentContentPosition + this.cueClusterPositions.get(i);
                    }
                    while (true) {
                        i = size - 1;
                        if (i2 < i) {
                            i = i2 + 1;
                            iArr[i2] = (int) (jArr[i] - jArr[i2]);
                            jArr2[i2] = jArr3[i] - jArr3[i2];
                            i2 = i;
                        } else {
                            iArr[i] = (int) ((this.segmentContentPosition + this.segmentContentSize) - jArr[i]);
                            jArr2[i] = this.durationUs - jArr3[i];
                            this.cueTimesUs = null;
                            this.cueClusterPositions = null;
                            return new ChunkIndex(iArr, jArr, jArr2, jArr3);
                        }
                    }
                }
            }
        }
        this.cueTimesUs = null;
        this.cueClusterPositions = null;
        return new Unseekable(this.durationUs);
    }

    private boolean maybeSeekForCues(PositionHolder positionHolder, long j) {
        if (this.seekForCues) {
            this.seekPositionAfterBuildingCues = j;
            positionHolder.position = this.cuesContentPosition;
            this.seekForCues = false;
            return true;
        }
        if (this.sentSeekMap) {
            j = this.seekPositionAfterBuildingCues;
            if (j != -1) {
                positionHolder.position = j;
                this.seekPositionAfterBuildingCues = -1;
                return true;
            }
        }
        return false;
    }

    private long scaleTimecodeToUs(long j) throws ParserException {
        long j2 = this.timecodeScale;
        if (j2 != -9223372036854775807L) {
            return Util.scaleLargeTimestamp(j, j2, 1000);
        }
        throw new ParserException("Can't scale timecode prior to timecodeScale being set.");
    }

    private static boolean isCodecSupported(String str) {
        return "V_VP8".equals(str) || "V_VP9".equals(str) || "V_MPEG2".equals(str) || "V_MPEG4/ISO/SP".equals(str) || "V_MPEG4/ISO/ASP".equals(str) || "V_MPEG4/ISO/AP".equals(str) || "V_MPEG4/ISO/AVC".equals(str) || "V_MPEGH/ISO/HEVC".equals(str) || "V_MS/VFW/FOURCC".equals(str) || "V_THEORA".equals(str) || "A_OPUS".equals(str) || "A_VORBIS".equals(str) || "A_AAC".equals(str) || "A_MPEG/L2".equals(str) || "A_MPEG/L3".equals(str) || "A_AC3".equals(str) || "A_EAC3".equals(str) || "A_TRUEHD".equals(str) || "A_DTS".equals(str) || "A_DTS/EXPRESS".equals(str) || "A_DTS/LOSSLESS".equals(str) || "A_FLAC".equals(str) || "A_MS/ACM".equals(str) || "A_PCM/INT/LIT".equals(str) || "S_TEXT/UTF8".equals(str) || "S_TEXT/ASS".equals(str) || "S_VOBSUB".equals(str) || "S_HDMV/PGS".equals(str) || "S_DVBSUB".equals(str);
    }

    private static int[] ensureArrayCapacity(int[] iArr, int i) {
        if (iArr == null) {
            return new int[i];
        }
        if (iArr.length >= i) {
            return iArr;
        }
        return new int[Math.max(iArr.length * 2, i)];
    }
}
