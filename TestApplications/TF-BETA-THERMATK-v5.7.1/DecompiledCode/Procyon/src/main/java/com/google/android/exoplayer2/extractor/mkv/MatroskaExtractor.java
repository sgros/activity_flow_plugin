// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.video.ColorInfo;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.video.AvcConfig;
import com.google.android.exoplayer2.video.HevcConfig;
import java.nio.ByteOrder;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.Log;
import java.util.Collections;
import java.util.List;
import android.util.Pair;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Locale;
import com.google.android.exoplayer2.ParserException;
import java.io.IOException;
import java.util.Arrays;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.Util;
import android.util.SparseArray;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.LongArray;
import java.util.UUID;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public final class MatroskaExtractor implements Extractor
{
    public static final ExtractorsFactory FACTORY;
    private static final byte[] SSA_DIALOGUE_FORMAT;
    private static final byte[] SSA_PREFIX;
    private static final byte[] SSA_TIMECODE_EMPTY;
    private static final byte[] SUBRIP_PREFIX;
    private static final byte[] SUBRIP_TIMECODE_EMPTY;
    private static final UUID WAVE_SUBFORMAT_PCM;
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
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$MatroskaExtractor$jNXW0tyYIOPE6N2jicocV6rRvBs.INSTANCE;
        SUBRIP_PREFIX = new byte[] { 49, 10, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 32, 45, 45, 62, 32, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 10 };
        SUBRIP_TIMECODE_EMPTY = new byte[] { 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32 };
        SSA_DIALOGUE_FORMAT = Util.getUtf8Bytes("Format: Start, End, ReadOrder, Layer, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
        SSA_PREFIX = new byte[] { 68, 105, 97, 108, 111, 103, 117, 101, 58, 32, 48, 58, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 58, 48, 48, 58, 48, 48, 58, 48, 48, 44 };
        SSA_TIMECODE_EMPTY = new byte[] { 32, 32, 32, 32, 32, 32, 32, 32, 32, 32 };
        WAVE_SUBFORMAT_PCM = new UUID(72057594037932032L, -9223371306706625679L);
    }
    
    public MatroskaExtractor() {
        this(0);
    }
    
    public MatroskaExtractor(final int n) {
        this(new DefaultEbmlReader(), n);
    }
    
    MatroskaExtractor(final EbmlReader reader, final int n) {
        this.segmentContentPosition = -1L;
        this.timecodeScale = -9223372036854775807L;
        this.durationTimecode = -9223372036854775807L;
        this.durationUs = -9223372036854775807L;
        this.cuesContentPosition = -1L;
        this.seekPositionAfterBuildingCues = -1L;
        this.clusterTimecodeUs = -9223372036854775807L;
        (this.reader = reader).init(new InnerEbmlReaderOutput());
        boolean seekForCuesEnabled = true;
        if ((n & 0x1) != 0x0) {
            seekForCuesEnabled = false;
        }
        this.seekForCuesEnabled = seekForCuesEnabled;
        this.varintReader = new VarintReader();
        this.tracks = (SparseArray<Track>)new SparseArray();
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
    
    private SeekMap buildSeekMap() {
        if (this.segmentContentPosition != -1L && this.durationUs != -9223372036854775807L) {
            final LongArray cueTimesUs = this.cueTimesUs;
            if (cueTimesUs != null && cueTimesUs.size() != 0) {
                final LongArray cueClusterPositions = this.cueClusterPositions;
                if (cueClusterPositions != null) {
                    if (cueClusterPositions.size() == this.cueTimesUs.size()) {
                        final int size = this.cueTimesUs.size();
                        final int[] array = new int[size];
                        final long[] array2 = new long[size];
                        final long[] array3 = new long[size];
                        final long[] array4 = new long[size];
                        final int n = 0;
                        int n2 = 0;
                        int n3;
                        while (true) {
                            n3 = n;
                            if (n2 >= size) {
                                break;
                            }
                            array4[n2] = this.cueTimesUs.get(n2);
                            array2[n2] = this.segmentContentPosition + this.cueClusterPositions.get(n2);
                            ++n2;
                        }
                        int n4;
                        while (true) {
                            n4 = size - 1;
                            if (n3 >= n4) {
                                break;
                            }
                            final int n5 = n3 + 1;
                            array[n3] = (int)(array2[n5] - array2[n3]);
                            array3[n3] = array4[n5] - array4[n3];
                            n3 = n5;
                        }
                        array[n4] = (int)(this.segmentContentPosition + this.segmentContentSize - array2[n4]);
                        array3[n4] = this.durationUs - array4[n4];
                        this.cueTimesUs = null;
                        this.cueClusterPositions = null;
                        return new ChunkIndex(array, array2, array3, array4);
                    }
                }
            }
        }
        this.cueTimesUs = null;
        this.cueClusterPositions = null;
        return new SeekMap.Unseekable(this.durationUs);
    }
    
    private void commitSampleToOutput(final Track track, final long n) {
        final TrueHdSampleRechunker trueHdSampleRechunker = track.trueHdSampleRechunker;
        if (trueHdSampleRechunker != null) {
            trueHdSampleRechunker.sampleMetadata(track, n);
        }
        else {
            if ("S_TEXT/UTF8".equals(track.codecId)) {
                this.commitSubtitleSample(track, "%02d:%02d:%02d,%03d", 19, 1000L, MatroskaExtractor.SUBRIP_TIMECODE_EMPTY);
            }
            else if ("S_TEXT/ASS".equals(track.codecId)) {
                this.commitSubtitleSample(track, "%01d:%02d:%02d:%02d", 21, 10000L, MatroskaExtractor.SSA_TIMECODE_EMPTY);
            }
            track.output.sampleMetadata(n, this.blockFlags, this.sampleBytesWritten, 0, track.cryptoData);
        }
        this.sampleRead = true;
        this.resetSample();
    }
    
    private void commitSubtitleSample(final Track track, final String s, final int n, final long n2, final byte[] array) {
        setSampleDuration(this.subtitleSample.data, this.blockDurationUs, s, n, n2, array);
        final TrackOutput output = track.output;
        final ParsableByteArray subtitleSample = this.subtitleSample;
        output.sampleData(subtitleSample, subtitleSample.limit());
        this.sampleBytesWritten += this.subtitleSample.limit();
    }
    
    private static int[] ensureArrayCapacity(final int[] array, final int b) {
        if (array == null) {
            return new int[b];
        }
        if (array.length >= b) {
            return array;
        }
        return new int[Math.max(array.length * 2, b)];
    }
    
    private static boolean isCodecSupported(final String anObject) {
        return "V_VP8".equals(anObject) || "V_VP9".equals(anObject) || "V_MPEG2".equals(anObject) || "V_MPEG4/ISO/SP".equals(anObject) || "V_MPEG4/ISO/ASP".equals(anObject) || "V_MPEG4/ISO/AP".equals(anObject) || "V_MPEG4/ISO/AVC".equals(anObject) || "V_MPEGH/ISO/HEVC".equals(anObject) || "V_MS/VFW/FOURCC".equals(anObject) || "V_THEORA".equals(anObject) || "A_OPUS".equals(anObject) || "A_VORBIS".equals(anObject) || "A_AAC".equals(anObject) || "A_MPEG/L2".equals(anObject) || "A_MPEG/L3".equals(anObject) || "A_AC3".equals(anObject) || "A_EAC3".equals(anObject) || "A_TRUEHD".equals(anObject) || "A_DTS".equals(anObject) || "A_DTS/EXPRESS".equals(anObject) || "A_DTS/LOSSLESS".equals(anObject) || "A_FLAC".equals(anObject) || "A_MS/ACM".equals(anObject) || "A_PCM/INT/LIT".equals(anObject) || "S_TEXT/UTF8".equals(anObject) || "S_TEXT/ASS".equals(anObject) || "S_VOBSUB".equals(anObject) || "S_HDMV/PGS".equals(anObject) || "S_DVBSUB".equals(anObject);
    }
    
    private boolean maybeSeekForCues(final PositionHolder positionHolder, long seekPositionAfterBuildingCues) {
        if (this.seekForCues) {
            this.seekPositionAfterBuildingCues = seekPositionAfterBuildingCues;
            positionHolder.position = this.cuesContentPosition;
            this.seekForCues = false;
            return true;
        }
        if (this.sentSeekMap) {
            seekPositionAfterBuildingCues = this.seekPositionAfterBuildingCues;
            if (seekPositionAfterBuildingCues != -1L) {
                positionHolder.position = seekPositionAfterBuildingCues;
                this.seekPositionAfterBuildingCues = -1L;
                return true;
            }
        }
        return false;
    }
    
    private void readScratch(final ExtractorInput extractorInput, final int n) throws IOException, InterruptedException {
        if (this.scratch.limit() >= n) {
            return;
        }
        if (this.scratch.capacity() < n) {
            final ParsableByteArray scratch = this.scratch;
            final byte[] data = scratch.data;
            scratch.reset(Arrays.copyOf(data, Math.max(data.length * 2, n)), this.scratch.limit());
        }
        final ParsableByteArray scratch2 = this.scratch;
        extractorInput.readFully(scratch2.data, scratch2.limit(), n - this.scratch.limit());
        this.scratch.setLimit(n);
    }
    
    private int readToOutput(final ExtractorInput extractorInput, final TrackOutput trackOutput, int a) throws IOException, InterruptedException {
        final int bytesLeft = this.sampleStrippedBytes.bytesLeft();
        if (bytesLeft > 0) {
            a = Math.min(a, bytesLeft);
            trackOutput.sampleData(this.sampleStrippedBytes, a);
        }
        else {
            a = trackOutput.sampleData(extractorInput, a, false);
        }
        this.sampleBytesRead += a;
        this.sampleBytesWritten += a;
        return a;
    }
    
    private void readToTarget(final ExtractorInput extractorInput, final byte[] array, final int n, final int a) throws IOException, InterruptedException {
        final int min = Math.min(a, this.sampleStrippedBytes.bytesLeft());
        extractorInput.readFully(array, n + min, a - min);
        if (min > 0) {
            this.sampleStrippedBytes.readBytes(array, n, min);
        }
        this.sampleBytesRead += a;
    }
    
    private void resetSample() {
        this.sampleBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        this.sampleEncodingHandled = false;
        this.sampleSignalByteRead = false;
        this.samplePartitionCountRead = false;
        this.samplePartitionCount = 0;
        this.sampleSignalByte = 0;
        this.sampleInitializationVectorRead = false;
        this.sampleStrippedBytes.reset();
    }
    
    private long scaleTimecodeToUs(final long n) throws ParserException {
        final long timecodeScale = this.timecodeScale;
        if (timecodeScale != -9223372036854775807L) {
            return Util.scaleLargeTimestamp(n, timecodeScale, 1000L);
        }
        throw new ParserException("Can't scale timecode prior to timecodeScale being set.");
    }
    
    private static void setSampleDuration(final byte[] array, long n, final String format, final int n2, final long n3, final byte[] array2) {
        byte[] utf8Bytes;
        if (n == -9223372036854775807L) {
            utf8Bytes = array2;
        }
        else {
            final int i = (int)(n / 3600000000L);
            n -= i * 3600 * 1000000L;
            final int j = (int)(n / 60000000L);
            n -= j * 60 * 1000000L;
            final int k = (int)(n / 1000000L);
            utf8Bytes = Util.getUtf8Bytes(String.format(Locale.US, format, i, j, k, (int)((n - k * 1000000L) / n3)));
        }
        System.arraycopy(utf8Bytes, 0, array, n2, array2.length);
    }
    
    private void writeSampleData(final ExtractorInput extractorInput, final Track track, int n) throws IOException, InterruptedException {
        if ("S_TEXT/UTF8".equals(track.codecId)) {
            this.writeSubtitleSampleData(extractorInput, MatroskaExtractor.SUBRIP_PREFIX, n);
            return;
        }
        if ("S_TEXT/ASS".equals(track.codecId)) {
            this.writeSubtitleSampleData(extractorInput, MatroskaExtractor.SSA_PREFIX, n);
            return;
        }
        final TrackOutput output = track.output;
        final boolean sampleEncodingHandled = this.sampleEncodingHandled;
        boolean b = true;
        if (!sampleEncodingHandled) {
            if (track.hasContentEncryption) {
                this.blockFlags &= 0xBFFFFFFF;
                final boolean sampleSignalByteRead = this.sampleSignalByteRead;
                int n2 = 128;
                if (!sampleSignalByteRead) {
                    extractorInput.readFully(this.scratch.data, 0, 1);
                    ++this.sampleBytesRead;
                    final byte[] data = this.scratch.data;
                    if ((data[0] & 0x80) == 0x80) {
                        throw new ParserException("Extension bit is set in signal byte");
                    }
                    this.sampleSignalByte = data[0];
                    this.sampleSignalByteRead = true;
                }
                if ((this.sampleSignalByte & 0x1) == 0x1) {
                    final boolean b2 = (this.sampleSignalByte & 0x2) == 0x2;
                    this.blockFlags |= 0x40000000;
                    if (!this.sampleInitializationVectorRead) {
                        extractorInput.readFully(this.encryptionInitializationVector.data, 0, 8);
                        this.sampleBytesRead += 8;
                        this.sampleInitializationVectorRead = true;
                        final byte[] data2 = this.scratch.data;
                        if (!b2) {
                            n2 = 0;
                        }
                        data2[0] = (byte)(n2 | 0x8);
                        this.scratch.setPosition(0);
                        output.sampleData(this.scratch, 1);
                        ++this.sampleBytesWritten;
                        this.encryptionInitializationVector.setPosition(0);
                        output.sampleData(this.encryptionInitializationVector, 8);
                        this.sampleBytesWritten += 8;
                    }
                    if (b2) {
                        if (!this.samplePartitionCountRead) {
                            extractorInput.readFully(this.scratch.data, 0, 1);
                            ++this.sampleBytesRead;
                            this.scratch.setPosition(0);
                            this.samplePartitionCount = this.scratch.readUnsignedByte();
                            this.samplePartitionCountRead = true;
                        }
                        final int n3 = this.samplePartitionCount * 4;
                        this.scratch.reset(n3);
                        extractorInput.readFully(this.scratch.data, 0, n3);
                        this.sampleBytesRead += n3;
                        final short n4 = (short)(this.samplePartitionCount / 2 + 1);
                        final int capacity = n4 * 6 + 2;
                        final ByteBuffer encryptionSubsampleDataBuffer = this.encryptionSubsampleDataBuffer;
                        if (encryptionSubsampleDataBuffer == null || encryptionSubsampleDataBuffer.capacity() < capacity) {
                            this.encryptionSubsampleDataBuffer = ByteBuffer.allocate(capacity);
                        }
                        this.encryptionSubsampleDataBuffer.position(0);
                        this.encryptionSubsampleDataBuffer.putShort(n4);
                        int n5 = 0;
                        int n6 = 0;
                        int samplePartitionCount;
                        while (true) {
                            samplePartitionCount = this.samplePartitionCount;
                            if (n5 >= samplePartitionCount) {
                                break;
                            }
                            final int unsignedIntToInt = this.scratch.readUnsignedIntToInt();
                            if (n5 % 2 == 0) {
                                this.encryptionSubsampleDataBuffer.putShort((short)(unsignedIntToInt - n6));
                            }
                            else {
                                this.encryptionSubsampleDataBuffer.putInt(unsignedIntToInt - n6);
                            }
                            ++n5;
                            n6 = unsignedIntToInt;
                        }
                        final int n7 = n - this.sampleBytesRead - n6;
                        if (samplePartitionCount % 2 == 1) {
                            this.encryptionSubsampleDataBuffer.putInt(n7);
                        }
                        else {
                            this.encryptionSubsampleDataBuffer.putShort((short)n7);
                            this.encryptionSubsampleDataBuffer.putInt(0);
                        }
                        this.encryptionSubsampleData.reset(this.encryptionSubsampleDataBuffer.array(), capacity);
                        output.sampleData(this.encryptionSubsampleData, capacity);
                        this.sampleBytesWritten += capacity;
                    }
                }
            }
            else {
                final byte[] sampleStrippedBytes = track.sampleStrippedBytes;
                if (sampleStrippedBytes != null) {
                    this.sampleStrippedBytes.reset(sampleStrippedBytes, sampleStrippedBytes.length);
                }
            }
            this.sampleEncodingHandled = true;
        }
        n += this.sampleStrippedBytes.limit();
        if (!"V_MPEG4/ISO/AVC".equals(track.codecId) && !"V_MPEGH/ISO/HEVC".equals(track.codecId)) {
            if (track.trueHdSampleRechunker != null) {
                if (this.sampleStrippedBytes.limit() != 0) {
                    b = false;
                }
                Assertions.checkState(b);
                track.trueHdSampleRechunker.startSample(extractorInput, this.blockFlags, n);
            }
            while (true) {
                final int sampleBytesRead = this.sampleBytesRead;
                if (sampleBytesRead >= n) {
                    break;
                }
                this.readToOutput(extractorInput, output, n - sampleBytesRead);
            }
        }
        else {
            final byte[] data3 = this.nalLength.data;
            data3[0] = 0;
            data3[1] = 0;
            data3[2] = 0;
            final int nalUnitLengthFieldLength = track.nalUnitLengthFieldLength;
            while (this.sampleBytesRead < n) {
                final int sampleCurrentNalBytesRemaining = this.sampleCurrentNalBytesRemaining;
                if (sampleCurrentNalBytesRemaining == 0) {
                    this.readToTarget(extractorInput, data3, 4 - nalUnitLengthFieldLength, nalUnitLengthFieldLength);
                    this.nalLength.setPosition(0);
                    this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                    this.nalStartCode.setPosition(0);
                    output.sampleData(this.nalStartCode, 4);
                    this.sampleBytesWritten += 4;
                }
                else {
                    this.sampleCurrentNalBytesRemaining = sampleCurrentNalBytesRemaining - this.readToOutput(extractorInput, output, sampleCurrentNalBytesRemaining);
                }
            }
        }
        if ("A_VORBIS".equals(track.codecId)) {
            this.vorbisNumPageSamples.setPosition(0);
            output.sampleData(this.vorbisNumPageSamples, 4);
            this.sampleBytesWritten += 4;
        }
    }
    
    private void writeSubtitleSampleData(final ExtractorInput extractorInput, final byte[] original, final int n) throws IOException, InterruptedException {
        final int n2 = original.length + n;
        if (this.subtitleSample.capacity() < n2) {
            this.subtitleSample.data = Arrays.copyOf(original, n2 + n);
        }
        else {
            System.arraycopy(original, 0, this.subtitleSample.data, 0, original.length);
        }
        extractorInput.readFully(this.subtitleSample.data, original.length, n);
        this.subtitleSample.reset(n2);
    }
    
    void binaryElement(int blockLacingSampleIndex, int val, final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (blockLacingSampleIndex != 161 && blockLacingSampleIndex != 163) {
            if (blockLacingSampleIndex != 16981) {
                if (blockLacingSampleIndex != 18402) {
                    if (blockLacingSampleIndex != 21419) {
                        if (blockLacingSampleIndex != 25506) {
                            if (blockLacingSampleIndex != 30322) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unexpected id: ");
                                sb.append(blockLacingSampleIndex);
                                throw new ParserException(sb.toString());
                            }
                            extractorInput.readFully(this.currentTrack.projectionData = new byte[val], 0, val);
                        }
                        else {
                            extractorInput.readFully(this.currentTrack.codecPrivate = new byte[val], 0, val);
                        }
                    }
                    else {
                        Arrays.fill(this.seekEntryIdBytes.data, (byte)0);
                        extractorInput.readFully(this.seekEntryIdBytes.data, 4 - val, val);
                        this.seekEntryIdBytes.setPosition(0);
                        this.seekEntryId = (int)this.seekEntryIdBytes.readUnsignedInt();
                    }
                }
                else {
                    final byte[] array = new byte[val];
                    extractorInput.readFully(array, 0, val);
                    this.currentTrack.cryptoData = new TrackOutput.CryptoData(1, array, 0, 0);
                }
            }
            else {
                extractorInput.readFully(this.currentTrack.sampleStrippedBytes = new byte[val], 0, val);
            }
        }
        else {
            if (this.blockState == 0) {
                this.blockTrackNumber = (int)this.varintReader.readUnsignedVarint(extractorInput, false, true, 8);
                this.blockTrackNumberLength = this.varintReader.getLastLength();
                this.blockDurationUs = -9223372036854775807L;
                this.blockState = 1;
                this.scratch.reset();
            }
            final Track track = (Track)this.tracks.get(this.blockTrackNumber);
            if (track == null) {
                extractorInput.skipFully(val - this.blockTrackNumberLength);
                this.blockState = 0;
                return;
            }
            if (this.blockState == 1) {
                this.readScratch(extractorInput, 3);
                final int i = (this.scratch.data[2] & 0x6) >> 1;
                if (i == 0) {
                    this.blockLacingSampleCount = 1;
                    (this.blockLacingSampleSizes = ensureArrayCapacity(this.blockLacingSampleSizes, 1))[0] = val - this.blockTrackNumberLength - 3;
                }
                else {
                    if (blockLacingSampleIndex != 163) {
                        throw new ParserException("Lacing only supported in SimpleBlocks.");
                    }
                    this.readScratch(extractorInput, 4);
                    this.blockLacingSampleCount = (this.scratch.data[3] & 0xFF) + 1;
                    this.blockLacingSampleSizes = ensureArrayCapacity(this.blockLacingSampleSizes, this.blockLacingSampleCount);
                    if (i == 2) {
                        final int blockTrackNumberLength = this.blockTrackNumberLength;
                        final int blockLacingSampleCount = this.blockLacingSampleCount;
                        val = (val - blockTrackNumberLength - 4) / blockLacingSampleCount;
                        Arrays.fill(this.blockLacingSampleSizes, 0, blockLacingSampleCount, val);
                    }
                    else if (i == 1) {
                        int n = 0;
                        int n2 = 4;
                        int n3 = 0;
                        int blockLacingSampleCount2;
                        while (true) {
                            blockLacingSampleCount2 = this.blockLacingSampleCount;
                            if (n >= blockLacingSampleCount2 - 1) {
                                break;
                            }
                            this.blockLacingSampleSizes[n] = 0;
                            int n4 = n2;
                            int j;
                            int[] blockLacingSampleSizes;
                            do {
                                n2 = n4 + 1;
                                this.readScratch(extractorInput, n2);
                                j = (this.scratch.data[n2 - 1] & 0xFF);
                                blockLacingSampleSizes = this.blockLacingSampleSizes;
                                blockLacingSampleSizes[n] += j;
                                n4 = n2;
                            } while (j == 255);
                            n3 += blockLacingSampleSizes[n];
                            ++n;
                        }
                        this.blockLacingSampleSizes[blockLacingSampleCount2 - 1] = val - this.blockTrackNumberLength - n2 - n3;
                    }
                    else {
                        if (i != 3) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Unexpected lacing value: ");
                            sb2.append(i);
                            throw new ParserException(sb2.toString());
                        }
                        int n5 = 0;
                        int n6 = 4;
                        int n7 = 0;
                        while (true) {
                            final int blockLacingSampleCount3 = this.blockLacingSampleCount;
                            if (n5 >= blockLacingSampleCount3 - 1) {
                                this.blockLacingSampleSizes[blockLacingSampleCount3 - 1] = val - this.blockTrackNumberLength - n6 - n7;
                                break;
                            }
                            this.blockLacingSampleSizes[n5] = 0;
                            final int n8 = n6 + 1;
                            this.readScratch(extractorInput, n8);
                            final byte[] data = this.scratch.data;
                            final int n9 = n8 - 1;
                            if (data[n9] == 0) {
                                throw new ParserException("No valid varint length mask found");
                            }
                            final long n10 = 0L;
                            int n11 = 0;
                            long n12;
                            while (true) {
                                n6 = n8;
                                n12 = n10;
                                if (n11 >= 8) {
                                    break;
                                }
                                final int n13 = 1 << 7 - n11;
                                if ((this.scratch.data[n9] & n13) != 0x0) {
                                    final int n14 = n8 + n11;
                                    this.readScratch(extractorInput, n14);
                                    long n15 = this.scratch.data[n9] & 0xFF & ~n13;
                                    int n16 = n9 + 1;
                                    long n17;
                                    while (true) {
                                        n17 = n15;
                                        if (n16 >= n14) {
                                            break;
                                        }
                                        n15 = (n17 << 8 | (long)(this.scratch.data[n16] & 0xFF));
                                        ++n16;
                                    }
                                    n6 = n14;
                                    n12 = n17;
                                    if (n5 > 0) {
                                        n12 = n17 - ((1L << n11 * 7 + 6) - 1L);
                                        n6 = n14;
                                        break;
                                    }
                                    break;
                                }
                                else {
                                    ++n11;
                                }
                            }
                            if (n12 < -2147483648L || n12 > 2147483647L) {
                                throw new ParserException("EBML lacing sample size out of range.");
                            }
                            int n18 = (int)n12;
                            final int[] blockLacingSampleSizes2 = this.blockLacingSampleSizes;
                            if (n5 != 0) {
                                n18 += blockLacingSampleSizes2[n5 - 1];
                            }
                            blockLacingSampleSizes2[n5] = n18;
                            n7 += this.blockLacingSampleSizes[n5];
                            ++n5;
                        }
                    }
                }
                final byte[] data2 = this.scratch.data;
                val = data2[0];
                this.blockTimeUs = this.clusterTimecodeUs + this.scaleTimecodeToUs((data2[1] & 0xFF) | val << 8);
                final boolean b = (this.scratch.data[2] & 0x8) == 0x8;
                if (track.type != 2 && (blockLacingSampleIndex != 163 || (this.scratch.data[2] & 0x80) != 0x80)) {
                    val = 0;
                }
                else {
                    val = 1;
                }
                int n19;
                if (b) {
                    n19 = Integer.MIN_VALUE;
                }
                else {
                    n19 = 0;
                }
                this.blockFlags = (val | n19);
                this.blockState = 2;
                this.blockLacingSampleIndex = 0;
            }
            if (blockLacingSampleIndex == 163) {
                while (true) {
                    blockLacingSampleIndex = this.blockLacingSampleIndex;
                    if (blockLacingSampleIndex >= this.blockLacingSampleCount) {
                        break;
                    }
                    this.writeSampleData(extractorInput, track, this.blockLacingSampleSizes[blockLacingSampleIndex]);
                    this.commitSampleToOutput(track, this.blockTimeUs + this.blockLacingSampleIndex * track.defaultSampleDurationNs / 1000);
                    ++this.blockLacingSampleIndex;
                }
                this.blockState = 0;
            }
            else {
                this.writeSampleData(extractorInput, track, this.blockLacingSampleSizes[0]);
            }
        }
    }
    
    void endMasterElement(int seekEntryId) throws ParserException {
        if (seekEntryId != 160) {
            if (seekEntryId != 174) {
                if (seekEntryId == 19899) {
                    seekEntryId = this.seekEntryId;
                    if (seekEntryId != -1) {
                        final long seekEntryPosition = this.seekEntryPosition;
                        if (seekEntryPosition != -1L) {
                            if (seekEntryId == 475249515) {
                                this.cuesContentPosition = seekEntryPosition;
                            }
                            return;
                        }
                    }
                    throw new ParserException("Mandatory element SeekID or SeekPosition not found");
                }
                if (seekEntryId != 25152) {
                    if (seekEntryId != 28032) {
                        if (seekEntryId != 357149030) {
                            if (seekEntryId != 374648427) {
                                if (seekEntryId == 475249515) {
                                    if (!this.sentSeekMap) {
                                        this.extractorOutput.seekMap(this.buildSeekMap());
                                        this.sentSeekMap = true;
                                    }
                                }
                            }
                            else {
                                if (this.tracks.size() == 0) {
                                    throw new ParserException("No valid tracks were found");
                                }
                                this.extractorOutput.endTracks();
                            }
                        }
                        else {
                            if (this.timecodeScale == -9223372036854775807L) {
                                this.timecodeScale = 1000000L;
                            }
                            final long durationTimecode = this.durationTimecode;
                            if (durationTimecode != -9223372036854775807L) {
                                this.durationUs = this.scaleTimecodeToUs(durationTimecode);
                            }
                        }
                    }
                    else {
                        final Track currentTrack = this.currentTrack;
                        if (currentTrack.hasContentEncryption) {
                            if (currentTrack.sampleStrippedBytes != null) {
                                throw new ParserException("Combining encryption and compression is not supported");
                            }
                        }
                    }
                }
                else {
                    final Track currentTrack2 = this.currentTrack;
                    if (currentTrack2.hasContentEncryption) {
                        final TrackOutput.CryptoData cryptoData = currentTrack2.cryptoData;
                        if (cryptoData == null) {
                            throw new ParserException("Encrypted Track found but ContentEncKeyID was not found");
                        }
                        currentTrack2.drmInitData = new DrmInitData(new DrmInitData.SchemeData[] { new DrmInitData.SchemeData(C.UUID_NIL, "video/webm", cryptoData.encryptionKey) });
                    }
                }
            }
            else {
                if (isCodecSupported(this.currentTrack.codecId)) {
                    final Track currentTrack3 = this.currentTrack;
                    currentTrack3.initializeOutput(this.extractorOutput, currentTrack3.number);
                    final SparseArray<Track> tracks = this.tracks;
                    final Track currentTrack4 = this.currentTrack;
                    tracks.put(currentTrack4.number, (Object)currentTrack4);
                }
                this.currentTrack = null;
            }
        }
        else {
            if (this.blockState != 2) {
                return;
            }
            if (!this.sampleSeenReferenceBlock) {
                this.blockFlags |= 0x1;
            }
            this.commitSampleToOutput((Track)this.tracks.get(this.blockTrackNumber), this.blockTimeUs);
            this.blockState = 0;
        }
    }
    
    void floatElement(final int n, final double n2) {
        Label_0273: {
            if (n != 181) {
                if (n != 17545) {
                    switch (n) {
                        default: {
                            switch (n) {
                                default: {
                                    break Label_0273;
                                }
                                case 30325: {
                                    this.currentTrack.projectionPoseRoll = (float)n2;
                                    break Label_0273;
                                }
                                case 30324: {
                                    this.currentTrack.projectionPosePitch = (float)n2;
                                    break Label_0273;
                                }
                                case 30323: {
                                    this.currentTrack.projectionPoseYaw = (float)n2;
                                    break Label_0273;
                                }
                            }
                            break;
                        }
                        case 21978: {
                            this.currentTrack.minMasteringLuminance = (float)n2;
                            break;
                        }
                        case 21977: {
                            this.currentTrack.maxMasteringLuminance = (float)n2;
                            break;
                        }
                        case 21976: {
                            this.currentTrack.whitePointChromaticityY = (float)n2;
                            break;
                        }
                        case 21975: {
                            this.currentTrack.whitePointChromaticityX = (float)n2;
                            break;
                        }
                        case 21974: {
                            this.currentTrack.primaryBChromaticityY = (float)n2;
                            break;
                        }
                        case 21973: {
                            this.currentTrack.primaryBChromaticityX = (float)n2;
                            break;
                        }
                        case 21972: {
                            this.currentTrack.primaryGChromaticityY = (float)n2;
                            break;
                        }
                        case 21971: {
                            this.currentTrack.primaryGChromaticityX = (float)n2;
                            break;
                        }
                        case 21970: {
                            this.currentTrack.primaryRChromaticityY = (float)n2;
                            break;
                        }
                        case 21969: {
                            this.currentTrack.primaryRChromaticityX = (float)n2;
                            break;
                        }
                    }
                }
                else {
                    this.durationTimecode = (long)n2;
                }
            }
            else {
                this.currentTrack.sampleRate = (int)n2;
            }
        }
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
    }
    
    void integerElement(int n, final long n2) throws ParserException {
        Label_1288: {
            if (n != 20529) {
                if (n != 20530) {
                    final boolean b = false;
                    boolean flagForced = false;
                    switch (n) {
                        default: {
                            switch (n) {
                                default: {
                                    break Label_1288;
                                }
                                case 21949: {
                                    this.currentTrack.maxFrameAverageLuminance = (int)n2;
                                    break Label_1288;
                                }
                                case 21948: {
                                    this.currentTrack.maxContentLuminance = (int)n2;
                                    break Label_1288;
                                }
                                case 21947: {
                                    final Track currentTrack = this.currentTrack;
                                    currentTrack.hasColorInfo = true;
                                    n = (int)n2;
                                    if (n == 1) {
                                        currentTrack.colorSpace = 1;
                                        break Label_1288;
                                    }
                                    if (n == 9) {
                                        currentTrack.colorSpace = 6;
                                        break Label_1288;
                                    }
                                    if (n != 4 && n != 5 && n != 6 && n != 7) {
                                        break Label_1288;
                                    }
                                    this.currentTrack.colorSpace = 2;
                                    break Label_1288;
                                }
                                case 21946: {
                                    n = (int)n2;
                                    if (n != 1) {
                                        if (n == 16) {
                                            this.currentTrack.colorTransfer = 6;
                                            break Label_1288;
                                        }
                                        if (n == 18) {
                                            this.currentTrack.colorTransfer = 7;
                                            break Label_1288;
                                        }
                                        if (n != 6 && n != 7) {
                                            break Label_1288;
                                        }
                                    }
                                    this.currentTrack.colorTransfer = 3;
                                    break Label_1288;
                                }
                                case 21945: {
                                    n = (int)n2;
                                    if (n == 1) {
                                        this.currentTrack.colorRange = 2;
                                        break Label_1288;
                                    }
                                    if (n != 2) {
                                        break Label_1288;
                                    }
                                    this.currentTrack.colorRange = 1;
                                    break Label_1288;
                                }
                            }
                            break;
                        }
                        case 2807729: {
                            this.timecodeScale = n2;
                            break;
                        }
                        case 2352003: {
                            this.currentTrack.defaultSampleDurationNs = (int)n2;
                            break;
                        }
                        case 30321: {
                            n = (int)n2;
                            if (n == 0) {
                                this.currentTrack.projectionType = 0;
                                break;
                            }
                            if (n == 1) {
                                this.currentTrack.projectionType = 1;
                                break;
                            }
                            if (n == 2) {
                                this.currentTrack.projectionType = 2;
                                break;
                            }
                            if (n != 3) {
                                break;
                            }
                            this.currentTrack.projectionType = 3;
                            break;
                        }
                        case 25188: {
                            this.currentTrack.audioBitDepth = (int)n2;
                            break;
                        }
                        case 22203: {
                            this.currentTrack.seekPreRollNs = n2;
                            break;
                        }
                        case 22186: {
                            this.currentTrack.codecDelayNs = n2;
                            break;
                        }
                        case 21930: {
                            final Track currentTrack2 = this.currentTrack;
                            if (n2 == 1L) {
                                flagForced = true;
                            }
                            currentTrack2.flagForced = flagForced;
                            break;
                        }
                        case 21690: {
                            this.currentTrack.displayHeight = (int)n2;
                            break;
                        }
                        case 21682: {
                            this.currentTrack.displayUnit = (int)n2;
                            break;
                        }
                        case 21680: {
                            this.currentTrack.displayWidth = (int)n2;
                            break;
                        }
                        case 21432: {
                            n = (int)n2;
                            if (n == 0) {
                                this.currentTrack.stereoMode = 0;
                                break;
                            }
                            if (n == 1) {
                                this.currentTrack.stereoMode = 2;
                                break;
                            }
                            if (n == 3) {
                                this.currentTrack.stereoMode = 1;
                                break;
                            }
                            if (n != 15) {
                                break;
                            }
                            this.currentTrack.stereoMode = 3;
                            break;
                        }
                        case 21420: {
                            this.seekEntryPosition = n2 + this.segmentContentPosition;
                            break;
                        }
                        case 18408: {
                            if (n2 == 1L) {
                                break;
                            }
                            final StringBuilder sb = new StringBuilder();
                            sb.append("AESSettingsCipherMode ");
                            sb.append(n2);
                            sb.append(" not supported");
                            throw new ParserException(sb.toString());
                        }
                        case 18401: {
                            if (n2 == 5L) {
                                break;
                            }
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("ContentEncAlgo ");
                            sb2.append(n2);
                            sb2.append(" not supported");
                            throw new ParserException(sb2.toString());
                        }
                        case 17143: {
                            if (n2 == 1L) {
                                break;
                            }
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("EBMLReadVersion ");
                            sb3.append(n2);
                            sb3.append(" not supported");
                            throw new ParserException(sb3.toString());
                        }
                        case 17029: {
                            if (n2 >= 1L && n2 <= 2L) {
                                break;
                            }
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("DocTypeReadVersion ");
                            sb4.append(n2);
                            sb4.append(" not supported");
                            throw new ParserException(sb4.toString());
                        }
                        case 16980: {
                            if (n2 == 3L) {
                                break;
                            }
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append("ContentCompAlgo ");
                            sb5.append(n2);
                            sb5.append(" not supported");
                            throw new ParserException(sb5.toString());
                        }
                        case 251: {
                            this.sampleSeenReferenceBlock = true;
                            break;
                        }
                        case 241: {
                            if (!this.seenClusterPositionForCurrentCuePoint) {
                                this.cueClusterPositions.add(n2);
                                this.seenClusterPositionForCurrentCuePoint = true;
                                break;
                            }
                            break;
                        }
                        case 231: {
                            this.clusterTimecodeUs = this.scaleTimecodeToUs(n2);
                            break;
                        }
                        case 215: {
                            this.currentTrack.number = (int)n2;
                            break;
                        }
                        case 186: {
                            this.currentTrack.height = (int)n2;
                            break;
                        }
                        case 179: {
                            this.cueTimesUs.add(this.scaleTimecodeToUs(n2));
                            break;
                        }
                        case 176: {
                            this.currentTrack.width = (int)n2;
                            break;
                        }
                        case 159: {
                            this.currentTrack.channelCount = (int)n2;
                            break;
                        }
                        case 155: {
                            this.blockDurationUs = this.scaleTimecodeToUs(n2);
                            break;
                        }
                        case 136: {
                            final Track currentTrack3 = this.currentTrack;
                            boolean flagDefault = b;
                            if (n2 == 1L) {
                                flagDefault = true;
                            }
                            currentTrack3.flagDefault = flagDefault;
                            break;
                        }
                        case 131: {
                            this.currentTrack.type = (int)n2;
                            break;
                        }
                    }
                }
                else if (n2 != 1L) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("ContentEncodingScope ");
                    sb6.append(n2);
                    sb6.append(" not supported");
                    throw new ParserException(sb6.toString());
                }
            }
            else if (n2 != 0L) {
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("ContentEncodingOrder ");
                sb7.append(n2);
                sb7.append(" not supported");
                throw new ParserException(sb7.toString());
            }
        }
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        int i = 0;
        this.sampleRead = false;
        boolean read = true;
        while (read && !this.sampleRead) {
            final boolean b = read = this.reader.read(extractorInput);
            if (b) {
                read = b;
                if (this.maybeSeekForCues(positionHolder, extractorInput.getPosition())) {
                    return 1;
                }
                continue;
            }
        }
        if (!read) {
            while (i < this.tracks.size()) {
                ((Track)this.tracks.valueAt(i)).outputPendingSampleMetadata();
                ++i;
            }
            return -1;
        }
        return 0;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        this.clusterTimecodeUs = -9223372036854775807L;
        int i = 0;
        this.blockState = 0;
        this.reader.reset();
        this.varintReader.reset();
        this.resetSample();
        while (i < this.tracks.size()) {
            ((Track)this.tracks.valueAt(i)).reset();
            ++i;
        }
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        return new Sniffer().sniff(extractorInput);
    }
    
    void startMasterElement(final int n, final long segmentContentPosition, final long segmentContentSize) throws ParserException {
        if (n != 160) {
            if (n != 174) {
                if (n != 187) {
                    if (n != 19899) {
                        if (n != 20533) {
                            if (n != 21968) {
                                if (n != 25152) {
                                    if (n != 408125543) {
                                        if (n != 475249515) {
                                            if (n == 524531317) {
                                                if (!this.sentSeekMap) {
                                                    if (this.seekForCuesEnabled && this.cuesContentPosition != -1L) {
                                                        this.seekForCues = true;
                                                    }
                                                    else {
                                                        this.extractorOutput.seekMap(new SeekMap.Unseekable(this.durationUs));
                                                        this.sentSeekMap = true;
                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            this.cueTimesUs = new LongArray();
                                            this.cueClusterPositions = new LongArray();
                                        }
                                    }
                                    else {
                                        final long segmentContentPosition2 = this.segmentContentPosition;
                                        if (segmentContentPosition2 != -1L && segmentContentPosition2 != segmentContentPosition) {
                                            throw new ParserException("Multiple Segment elements not supported");
                                        }
                                        this.segmentContentPosition = segmentContentPosition;
                                        this.segmentContentSize = segmentContentSize;
                                    }
                                }
                            }
                            else {
                                this.currentTrack.hasColorInfo = true;
                            }
                        }
                        else {
                            this.currentTrack.hasContentEncryption = true;
                        }
                    }
                    else {
                        this.seekEntryId = -1;
                        this.seekEntryPosition = -1L;
                    }
                }
                else {
                    this.seenClusterPositionForCurrentCuePoint = false;
                }
            }
            else {
                this.currentTrack = new Track();
            }
        }
        else {
            this.sampleSeenReferenceBlock = false;
        }
    }
    
    void stringElement(final int n, final String codecId) throws ParserException {
        if (n != 134) {
            if (n != 17026) {
                if (n != 21358) {
                    if (n == 2274716) {
                        this.currentTrack.language = codecId;
                    }
                }
                else {
                    this.currentTrack.name = codecId;
                }
            }
            else if (!"webm".equals(codecId)) {
                if (!"matroska".equals(codecId)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("DocType ");
                    sb.append(codecId);
                    sb.append(" not supported");
                    throw new ParserException(sb.toString());
                }
            }
        }
        else {
            this.currentTrack.codecId = codecId;
        }
    }
    
    private final class InnerEbmlReaderOutput implements EbmlReaderOutput
    {
        @Override
        public void binaryElement(final int n, final int n2, final ExtractorInput extractorInput) throws IOException, InterruptedException {
            MatroskaExtractor.this.binaryElement(n, n2, extractorInput);
        }
        
        @Override
        public void endMasterElement(final int n) throws ParserException {
            MatroskaExtractor.this.endMasterElement(n);
        }
        
        @Override
        public void floatElement(final int n, final double n2) throws ParserException {
            MatroskaExtractor.this.floatElement(n, n2);
        }
        
        @Override
        public int getElementType(final int n) {
            switch (n) {
                default: {
                    return 0;
                }
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
                case 30325: {
                    return 5;
                }
                case 161:
                case 163:
                case 16981:
                case 18402:
                case 21419:
                case 25506:
                case 30322: {
                    return 4;
                }
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
                case 524531317: {
                    return 1;
                }
                case 134:
                case 17026:
                case 21358:
                case 2274716: {
                    return 3;
                }
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
                case 2807729: {
                    return 2;
                }
            }
        }
        
        @Override
        public void integerElement(final int n, final long n2) throws ParserException {
            MatroskaExtractor.this.integerElement(n, n2);
        }
        
        @Override
        public boolean isLevel1Element(final int n) {
            return n == 357149030 || n == 524531317 || n == 475249515 || n == 374648427;
        }
        
        @Override
        public void startMasterElement(final int n, final long n2, final long n3) throws ParserException {
            MatroskaExtractor.this.startMasterElement(n, n2, n3);
        }
        
        @Override
        public void stringElement(final int n, final String s) throws ParserException {
            MatroskaExtractor.this.stringElement(n, s);
        }
    }
    
    private static final class Track
    {
        public int audioBitDepth;
        public int channelCount;
        public long codecDelayNs;
        public String codecId;
        public byte[] codecPrivate;
        public int colorRange;
        public int colorSpace;
        public int colorTransfer;
        public TrackOutput.CryptoData cryptoData;
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
            this.maxFrameAverageLuminance = 200;
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
            this.codecDelayNs = 0L;
            this.seekPreRollNs = 0L;
            this.flagDefault = true;
            this.language = "eng";
        }
        
        private byte[] getHdrStaticInfo() {
            if (this.primaryRChromaticityX != -1.0f && this.primaryRChromaticityY != -1.0f && this.primaryGChromaticityX != -1.0f && this.primaryGChromaticityY != -1.0f && this.primaryBChromaticityX != -1.0f && this.primaryBChromaticityY != -1.0f && this.whitePointChromaticityX != -1.0f && this.whitePointChromaticityY != -1.0f && this.maxMasteringLuminance != -1.0f && this.minMasteringLuminance != -1.0f) {
                final byte[] array = new byte[25];
                final ByteBuffer wrap = ByteBuffer.wrap(array);
                wrap.put((byte)0);
                wrap.putShort((short)(this.primaryRChromaticityX * 50000.0f + 0.5f));
                wrap.putShort((short)(this.primaryRChromaticityY * 50000.0f + 0.5f));
                wrap.putShort((short)(this.primaryGChromaticityX * 50000.0f + 0.5f));
                wrap.putShort((short)(this.primaryGChromaticityY * 50000.0f + 0.5f));
                wrap.putShort((short)(this.primaryBChromaticityX * 50000.0f + 0.5f));
                wrap.putShort((short)(this.primaryBChromaticityY * 50000.0f + 0.5f));
                wrap.putShort((short)(this.whitePointChromaticityX * 50000.0f + 0.5f));
                wrap.putShort((short)(this.whitePointChromaticityY * 50000.0f + 0.5f));
                wrap.putShort((short)(this.maxMasteringLuminance + 0.5f));
                wrap.putShort((short)(this.minMasteringLuminance + 0.5f));
                wrap.putShort((short)this.maxContentLuminance);
                wrap.putShort((short)this.maxFrameAverageLuminance);
                return array;
            }
            return null;
        }
        
        private static Pair<String, List<byte[]>> parseFourCcPrivate(final ParsableByteArray parsableByteArray) throws ParserException {
            try {
                parsableByteArray.skipBytes(16);
                final long littleEndianUnsignedInt = parsableByteArray.readLittleEndianUnsignedInt();
                if (littleEndianUnsignedInt == 1482049860L) {
                    return (Pair<String, List<byte[]>>)new Pair((Object)"video/3gpp", (Object)null);
                }
                if (littleEndianUnsignedInt == 826496599L) {
                    int i = parsableByteArray.getPosition() + 20;
                    for (byte[] data = parsableByteArray.data; i < data.length - 4; ++i) {
                        if (data[i] == 0 && data[i + 1] == 0 && data[i + 2] == 1 && data[i + 3] == 15) {
                            return (Pair<String, List<byte[]>>)new Pair((Object)"video/wvc1", (Object)Collections.singletonList(Arrays.copyOfRange(data, i, data.length)));
                        }
                    }
                    throw new ParserException("Failed to find FourCC VC1 initialization data");
                }
                Log.w("MatroskaExtractor", "Unknown FourCC. Setting mimeType to video/x-unknown");
                return (Pair<String, List<byte[]>>)new Pair((Object)"video/x-unknown", (Object)null);
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                throw new ParserException("Error parsing FourCC private data");
            }
        }
        
        private static boolean parseMsAcmCodecPrivate(final ParsableByteArray parsableByteArray) throws ParserException {
            try {
                final int littleEndianUnsignedShort = parsableByteArray.readLittleEndianUnsignedShort();
                boolean b = true;
                if (littleEndianUnsignedShort == 1) {
                    return true;
                }
                if (littleEndianUnsignedShort == 65534) {
                    parsableByteArray.setPosition(24);
                    if (parsableByteArray.readLong() != MatroskaExtractor.WAVE_SUBFORMAT_PCM.getMostSignificantBits() || parsableByteArray.readLong() != MatroskaExtractor.WAVE_SUBFORMAT_PCM.getLeastSignificantBits()) {
                        b = false;
                    }
                    return b;
                }
                return false;
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                throw new ParserException("Error parsing MS/ACM codec private");
            }
        }
        
        private static List<byte[]> parseVorbisCodecPrivate(final byte[] array) throws ParserException {
            Label_0208: {
                if (array[0] != 2) {
                    break Label_0208;
                }
                int n = 1;
                int n2 = 0;
                while (array[n] == -1) {
                    n2 += 255;
                    ++n;
                }
                final int n3 = n + 1;
                final int n4 = n2 + array[n];
                int n5 = 0;
                int n6;
                for (n6 = n3; array[n6] == -1; ++n6) {
                    n5 += 255;
                }
                final int n7 = n6 + 1;
                final byte b = array[n6];
                Label_0195: {
                    if (array[n7] != 1) {
                        break Label_0195;
                    }
                    try {
                        final byte[] array2 = new byte[n4];
                        System.arraycopy(array, n7, array2, 0, n4);
                        final int n8 = n7 + n4;
                        if (array[n8] != 3) {
                            throw new ParserException("Error parsing vorbis codec private");
                        }
                        final int n9 = n8 + (n5 + b);
                        if (array[n9] == 5) {
                            final byte[] array3 = new byte[array.length - n9];
                            System.arraycopy(array, n9, array3, 0, array.length - n9);
                            final ArrayList<byte[]> list = new ArrayList<byte[]>(2);
                            list.add(array2);
                            list.add(array3);
                            return list;
                        }
                        throw new ParserException("Error parsing vorbis codec private");
                        throw new ParserException("Error parsing vorbis codec private");
                        throw new ParserException("Error parsing vorbis codec private");
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        throw new ParserException("Error parsing vorbis codec private");
                    }
                }
            }
        }
        
        public void initializeOutput(final ExtractorOutput extractorOutput, int i) throws ParserException {
            final String codecId = this.codecId;
            final int hashCode = codecId.hashCode();
            final int n = 3;
            int n2 = 0;
            Label_0753: {
                switch (hashCode) {
                    case 1951062397: {
                        if (codecId.equals("A_OPUS")) {
                            n2 = 11;
                            break Label_0753;
                        }
                        break;
                    }
                    case 1950789798: {
                        if (codecId.equals("A_FLAC")) {
                            n2 = 21;
                            break Label_0753;
                        }
                        break;
                    }
                    case 1950749482: {
                        if (codecId.equals("A_EAC3")) {
                            n2 = 16;
                            break Label_0753;
                        }
                        break;
                    }
                    case 1809237540: {
                        if (codecId.equals("V_MPEG2")) {
                            n2 = 2;
                            break Label_0753;
                        }
                        break;
                    }
                    case 1422270023: {
                        if (codecId.equals("S_TEXT/UTF8")) {
                            n2 = 24;
                            break Label_0753;
                        }
                        break;
                    }
                    case 855502857: {
                        if (codecId.equals("V_MPEGH/ISO/HEVC")) {
                            n2 = 7;
                            break Label_0753;
                        }
                        break;
                    }
                    case 738597099: {
                        if (codecId.equals("S_TEXT/ASS")) {
                            n2 = 25;
                            break Label_0753;
                        }
                        break;
                    }
                    case 725957860: {
                        if (codecId.equals("A_PCM/INT/LIT")) {
                            n2 = 23;
                            break Label_0753;
                        }
                        break;
                    }
                    case 542569478: {
                        if (codecId.equals("A_DTS/EXPRESS")) {
                            n2 = 19;
                            break Label_0753;
                        }
                        break;
                    }
                    case 444813526: {
                        if (codecId.equals("V_THEORA")) {
                            n2 = 9;
                            break Label_0753;
                        }
                        break;
                    }
                    case 99146302: {
                        if (codecId.equals("S_HDMV/PGS")) {
                            n2 = 27;
                            break Label_0753;
                        }
                        break;
                    }
                    case 82338134: {
                        if (codecId.equals("V_VP9")) {
                            n2 = 1;
                            break Label_0753;
                        }
                        break;
                    }
                    case 82338133: {
                        if (codecId.equals("V_VP8")) {
                            n2 = 0;
                            break Label_0753;
                        }
                        break;
                    }
                    case 62927045: {
                        if (codecId.equals("A_DTS")) {
                            n2 = 18;
                            break Label_0753;
                        }
                        break;
                    }
                    case 62923603: {
                        if (codecId.equals("A_AC3")) {
                            n2 = 15;
                            break Label_0753;
                        }
                        break;
                    }
                    case 62923557: {
                        if (codecId.equals("A_AAC")) {
                            n2 = 12;
                            break Label_0753;
                        }
                        break;
                    }
                    case -356037306: {
                        if (codecId.equals("A_DTS/LOSSLESS")) {
                            n2 = 20;
                            break Label_0753;
                        }
                        break;
                    }
                    case -425012669: {
                        if (codecId.equals("S_VOBSUB")) {
                            n2 = 26;
                            break Label_0753;
                        }
                        break;
                    }
                    case -538363109: {
                        if (codecId.equals("V_MPEG4/ISO/AVC")) {
                            n2 = 6;
                            break Label_0753;
                        }
                        break;
                    }
                    case -538363189: {
                        if (codecId.equals("V_MPEG4/ISO/ASP")) {
                            n2 = 4;
                            break Label_0753;
                        }
                        break;
                    }
                    case -933872740: {
                        if (codecId.equals("S_DVBSUB")) {
                            n2 = 28;
                            break Label_0753;
                        }
                        break;
                    }
                    case -1373388978: {
                        if (codecId.equals("V_MS/VFW/FOURCC")) {
                            n2 = 8;
                            break Label_0753;
                        }
                        break;
                    }
                    case -1482641357: {
                        if (codecId.equals("A_MPEG/L3")) {
                            n2 = 14;
                            break Label_0753;
                        }
                        break;
                    }
                    case -1482641358: {
                        if (codecId.equals("A_MPEG/L2")) {
                            n2 = 13;
                            break Label_0753;
                        }
                        break;
                    }
                    case -1730367663: {
                        if (codecId.equals("A_VORBIS")) {
                            n2 = 10;
                            break Label_0753;
                        }
                        break;
                    }
                    case -1784763192: {
                        if (codecId.equals("A_TRUEHD")) {
                            n2 = 17;
                            break Label_0753;
                        }
                        break;
                    }
                    case -1985379776: {
                        if (codecId.equals("A_MS/ACM")) {
                            n2 = 22;
                            break Label_0753;
                        }
                        break;
                    }
                    case -2095575984: {
                        if (codecId.equals("V_MPEG4/ISO/SP")) {
                            n2 = 3;
                            break Label_0753;
                        }
                        break;
                    }
                    case -2095576542: {
                        if (codecId.equals("V_MPEG4/ISO/AP")) {
                            n2 = 5;
                            break Label_0753;
                        }
                        break;
                    }
                }
                n2 = -1;
            }
            final ColorInfo colorInfo = null;
            String anObject = null;
            List<byte[]> list2 = null;
            int n5 = 0;
            int n6 = 0;
            Label_1596: {
                int n4 = 0;
                Label_1589: {
                    Label_1586: {
                        Label_1583: {
                            List<byte[]> list = null;
                            String s = null;
                            Label_1447: {
                                Label_1269: {
                                    Label_1189: {
                                        int n3 = 0;
                                        switch (n2) {
                                            default: {
                                                throw new ParserException("Unrecognized codec identifier.");
                                            }
                                            case 28: {
                                                final byte[] codecPrivate = this.codecPrivate;
                                                list = Collections.singletonList(new byte[] { codecPrivate[0], codecPrivate[1], codecPrivate[2], codecPrivate[3] });
                                                s = "application/dvbsubs";
                                                break Label_1447;
                                            }
                                            case 27: {
                                                anObject = "application/pgs";
                                                break Label_1583;
                                            }
                                            case 26: {
                                                list2 = Collections.singletonList(this.codecPrivate);
                                                anObject = "application/vobsub";
                                                break Label_1586;
                                            }
                                            case 25: {
                                                anObject = "text/x-ssa";
                                                break Label_1583;
                                            }
                                            case 24: {
                                                anObject = "application/x-subrip";
                                                break Label_1583;
                                            }
                                            case 23: {
                                                if ((n3 = Util.getPcmEncoding(this.audioBitDepth)) == 0) {
                                                    final StringBuilder sb = new StringBuilder();
                                                    sb.append("Unsupported PCM bit depth: ");
                                                    sb.append(this.audioBitDepth);
                                                    sb.append(". Setting mimeType to ");
                                                    sb.append("audio/x-unknown");
                                                    Log.w("MatroskaExtractor", sb.toString());
                                                    break Label_1189;
                                                }
                                                break;
                                            }
                                            case 22: {
                                                if (!parseMsAcmCodecPrivate(new ParsableByteArray(this.codecPrivate))) {
                                                    final StringBuilder sb2 = new StringBuilder();
                                                    sb2.append("Non-PCM MS/ACM is unsupported. Setting mimeType to ");
                                                    sb2.append("audio/x-unknown");
                                                    Log.w("MatroskaExtractor", sb2.toString());
                                                    break Label_1189;
                                                }
                                                if ((n3 = Util.getPcmEncoding(this.audioBitDepth)) == 0) {
                                                    final StringBuilder sb3 = new StringBuilder();
                                                    sb3.append("Unsupported PCM bit depth: ");
                                                    sb3.append(this.audioBitDepth);
                                                    sb3.append(". Setting mimeType to ");
                                                    sb3.append("audio/x-unknown");
                                                    Log.w("MatroskaExtractor", sb3.toString());
                                                    break Label_1189;
                                                }
                                                break;
                                            }
                                            case 21: {
                                                list = Collections.singletonList(this.codecPrivate);
                                                s = "audio/flac";
                                                break Label_1447;
                                            }
                                            case 20: {
                                                anObject = "audio/vnd.dts.hd";
                                                break Label_1583;
                                            }
                                            case 18:
                                            case 19: {
                                                anObject = "audio/vnd.dts";
                                                break Label_1583;
                                            }
                                            case 17: {
                                                this.trueHdSampleRechunker = new TrueHdSampleRechunker();
                                                anObject = "audio/true-hd";
                                                break Label_1583;
                                            }
                                            case 16: {
                                                anObject = "audio/eac3";
                                                break Label_1583;
                                            }
                                            case 15: {
                                                anObject = "audio/ac3";
                                                break Label_1583;
                                            }
                                            case 14: {
                                                anObject = "audio/mpeg";
                                                break Label_1269;
                                            }
                                            case 13: {
                                                anObject = "audio/mpeg-L2";
                                                break Label_1269;
                                            }
                                            case 12: {
                                                list = Collections.singletonList(this.codecPrivate);
                                                s = "audio/mp4a-latm";
                                                break Label_1447;
                                            }
                                            case 11: {
                                                list2 = new ArrayList<byte[]>(3);
                                                list2.add(this.codecPrivate);
                                                list2.add(ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(this.codecDelayNs).array());
                                                list2.add(ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(this.seekPreRollNs).array());
                                                anObject = "audio/opus";
                                                n4 = 5760;
                                                break Label_1589;
                                            }
                                            case 10: {
                                                list2 = parseVorbisCodecPrivate(this.codecPrivate);
                                                anObject = "audio/vorbis";
                                                n4 = 8192;
                                                break Label_1589;
                                            }
                                            case 9: {
                                                anObject = "video/x-unknown";
                                                break Label_1583;
                                            }
                                            case 8: {
                                                final Pair<String, List<byte[]>> fourCcPrivate = parseFourCcPrivate(new ParsableByteArray(this.codecPrivate));
                                                s = (String)fourCcPrivate.first;
                                                list = (List<byte[]>)fourCcPrivate.second;
                                                break Label_1447;
                                            }
                                            case 7: {
                                                final HevcConfig parse = HevcConfig.parse(new ParsableByteArray(this.codecPrivate));
                                                list2 = parse.initializationData;
                                                this.nalUnitLengthFieldLength = parse.nalUnitLengthFieldLength;
                                                anObject = "video/hevc";
                                                break Label_1586;
                                            }
                                            case 6: {
                                                final AvcConfig parse2 = AvcConfig.parse(new ParsableByteArray(this.codecPrivate));
                                                list2 = parse2.initializationData;
                                                this.nalUnitLengthFieldLength = parse2.nalUnitLengthFieldLength;
                                                anObject = "video/avc";
                                                break Label_1586;
                                            }
                                            case 3:
                                            case 4:
                                            case 5: {
                                                final byte[] codecPrivate2 = this.codecPrivate;
                                                if (codecPrivate2 == null) {
                                                    list = null;
                                                }
                                                else {
                                                    list = Collections.singletonList(codecPrivate2);
                                                }
                                                s = "video/mp4v-es";
                                                break Label_1447;
                                            }
                                            case 2: {
                                                anObject = "video/mpeg2";
                                                break Label_1583;
                                            }
                                            case 1: {
                                                anObject = "video/x-vnd.on2.vp9";
                                                break Label_1583;
                                            }
                                            case 0: {
                                                anObject = "video/x-vnd.on2.vp8";
                                                break Label_1583;
                                            }
                                        }
                                        anObject = "audio/raw";
                                        list2 = null;
                                        n5 = -1;
                                        n6 = n3;
                                        break Label_1596;
                                    }
                                    anObject = "audio/x-unknown";
                                    break Label_1583;
                                }
                                list2 = null;
                                n4 = 4096;
                                break Label_1589;
                            }
                            n5 = -1;
                            n6 = -1;
                            final List<byte[]> list3 = list;
                            anObject = s;
                            list2 = list3;
                            break Label_1596;
                        }
                        list2 = null;
                    }
                    n4 = -1;
                }
                n6 = -1;
                n5 = n4;
            }
            final boolean flagDefault = this.flagDefault;
            int n7;
            if (this.flagForced) {
                n7 = 2;
            }
            else {
                n7 = 0;
            }
            final int n8 = ((flagDefault | false) ? 1 : 0) | n7;
            Format format;
            if (MimeTypes.isAudio(anObject)) {
                format = Format.createAudioSampleFormat(Integer.toString(i), anObject, null, -1, n5, this.channelCount, this.sampleRate, n6, list2, this.drmInitData, n8, this.language);
                i = 1;
            }
            else if (MimeTypes.isVideo(anObject)) {
                if (this.displayUnit == 0) {
                    int displayWidth;
                    if ((displayWidth = this.displayWidth) == -1) {
                        displayWidth = this.width;
                    }
                    this.displayWidth = displayWidth;
                    int displayHeight;
                    if ((displayHeight = this.displayHeight) == -1) {
                        displayHeight = this.height;
                    }
                    this.displayHeight = displayHeight;
                }
                final int displayWidth2 = this.displayWidth;
                float n9 = 0.0f;
                Label_1794: {
                    if (displayWidth2 != -1) {
                        final int displayHeight2 = this.displayHeight;
                        if (displayHeight2 != -1) {
                            n9 = this.height * displayWidth2 / (float)(this.width * displayHeight2);
                            break Label_1794;
                        }
                    }
                    n9 = -1.0f;
                }
                ColorInfo colorInfo2 = colorInfo;
                if (this.hasColorInfo) {
                    colorInfo2 = new ColorInfo(this.colorSpace, this.colorRange, this.colorTransfer, this.getHdrStaticInfo());
                }
                int n10;
                if ("htc_video_rotA-000".equals(this.name)) {
                    n10 = 0;
                }
                else if ("htc_video_rotA-090".equals(this.name)) {
                    n10 = 90;
                }
                else if ("htc_video_rotA-180".equals(this.name)) {
                    n10 = 180;
                }
                else if ("htc_video_rotA-270".equals(this.name)) {
                    n10 = 270;
                }
                else {
                    n10 = -1;
                }
                if (this.projectionType == 0 && Float.compare(this.projectionPoseYaw, 0.0f) == 0 && Float.compare(this.projectionPosePitch, 0.0f) == 0) {
                    if (Float.compare(this.projectionPoseRoll, 0.0f) == 0) {
                        n10 = 0;
                    }
                    else if (Float.compare(this.projectionPosePitch, 90.0f) == 0) {
                        n10 = 90;
                    }
                    else if (Float.compare(this.projectionPosePitch, -180.0f) != 0 && Float.compare(this.projectionPosePitch, 180.0f) != 0) {
                        if (Float.compare(this.projectionPosePitch, -90.0f) == 0) {
                            n10 = 270;
                        }
                    }
                    else {
                        n10 = 180;
                    }
                }
                format = Format.createVideoSampleFormat(Integer.toString(i), anObject, null, -1, n5, this.width, this.height, -1.0f, list2, n10, n9, this.projectionData, this.stereoMode, colorInfo2, this.drmInitData);
                i = 2;
            }
            else if ("application/x-subrip".equals(anObject)) {
                format = Format.createTextSampleFormat(Integer.toString(i), anObject, n8, this.language, this.drmInitData);
                i = n;
            }
            else if ("text/x-ssa".equals(anObject)) {
                final ArrayList<byte[]> list4 = new ArrayList<byte[]>(2);
                list4.add(MatroskaExtractor.SSA_DIALOGUE_FORMAT);
                list4.add(this.codecPrivate);
                format = Format.createTextSampleFormat(Integer.toString(i), anObject, null, -1, n8, this.language, -1, this.drmInitData, Long.MAX_VALUE, list4);
                i = n;
            }
            else {
                if (!"application/vobsub".equals(anObject) && !"application/pgs".equals(anObject) && !"application/dvbsubs".equals(anObject)) {
                    throw new ParserException("Unexpected MIME type.");
                }
                format = Format.createImageSampleFormat(Integer.toString(i), anObject, null, -1, n8, list2, this.language, this.drmInitData);
                i = n;
            }
            (this.output = extractorOutput.track(this.number, i)).format(format);
        }
        
        public void outputPendingSampleMetadata() {
            final TrueHdSampleRechunker trueHdSampleRechunker = this.trueHdSampleRechunker;
            if (trueHdSampleRechunker != null) {
                trueHdSampleRechunker.outputPendingSampleMetadata(this);
            }
        }
        
        public void reset() {
            final TrueHdSampleRechunker trueHdSampleRechunker = this.trueHdSampleRechunker;
            if (trueHdSampleRechunker != null) {
                trueHdSampleRechunker.reset();
            }
        }
    }
    
    private static final class TrueHdSampleRechunker
    {
        private int blockFlags;
        private int chunkSize;
        private boolean foundSyncframe;
        private int sampleCount;
        private final byte[] syncframePrefix;
        private long timeUs;
        
        public TrueHdSampleRechunker() {
            this.syncframePrefix = new byte[10];
        }
        
        public void outputPendingSampleMetadata(final Track track) {
            if (this.foundSyncframe && this.sampleCount > 0) {
                track.output.sampleMetadata(this.timeUs, this.blockFlags, this.chunkSize, 0, track.cryptoData);
                this.sampleCount = 0;
            }
        }
        
        public void reset() {
            this.foundSyncframe = false;
        }
        
        public void sampleMetadata(final Track track, final long timeUs) {
            if (!this.foundSyncframe) {
                return;
            }
            if (this.sampleCount++ == 0) {
                this.timeUs = timeUs;
            }
            if (this.sampleCount < 16) {
                return;
            }
            track.output.sampleMetadata(this.timeUs, this.blockFlags, this.chunkSize, 0, track.cryptoData);
            this.sampleCount = 0;
        }
        
        public void startSample(final ExtractorInput extractorInput, final int blockFlags, final int n) throws IOException, InterruptedException {
            if (!this.foundSyncframe) {
                extractorInput.peekFully(this.syncframePrefix, 0, 10);
                extractorInput.resetPeekPosition();
                if (Ac3Util.parseTrueHdSyncframeAudioSampleCount(this.syncframePrefix) == 0) {
                    return;
                }
                this.foundSyncframe = true;
                this.sampleCount = 0;
            }
            if (this.sampleCount == 0) {
                this.blockFlags = blockFlags;
                this.chunkSize = 0;
            }
            this.chunkSize += n;
        }
    }
}
