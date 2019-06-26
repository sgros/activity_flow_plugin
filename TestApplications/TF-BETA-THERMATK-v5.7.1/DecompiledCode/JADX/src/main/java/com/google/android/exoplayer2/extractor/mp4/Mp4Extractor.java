package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekMap.SeekPoints;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public final class Mp4Extractor implements Extractor, SeekMap {
    private static final int BRAND_QUICKTIME = Util.getIntegerCodeForString("qt  ");
    public static final ExtractorsFactory FACTORY = C3337-$$Lambda$Mp4Extractor$quy71uYOGsneho91FZy1d2UGE1Q.INSTANCE;
    private long[][] accumulatedSampleSizes;
    private ParsableByteArray atomData;
    private final ParsableByteArray atomHeader;
    private int atomHeaderBytesRead;
    private long atomSize;
    private int atomType;
    private final ArrayDeque<ContainerAtom> containerAtoms;
    private long durationUs;
    private ExtractorOutput extractorOutput;
    private int firstVideoTrackIndex;
    private final int flags;
    private boolean isQuickTime;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private int parserState;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private int sampleTrackIndex;
    private Mp4Track[] tracks;

    private static final class Mp4Track {
        public int sampleIndex;
        public final TrackSampleTable sampleTable;
        public final Track track;
        public final TrackOutput trackOutput;

        public Mp4Track(Track track, TrackSampleTable trackSampleTable, TrackOutput trackOutput) {
            this.track = track;
            this.sampleTable = trackSampleTable;
            this.trackOutput = trackOutput;
        }
    }

    public boolean isSeekable() {
        return true;
    }

    public void release() {
    }

    public Mp4Extractor() {
        this(0);
    }

    public Mp4Extractor(int i) {
        this.flags = i;
        this.atomHeader = new ParsableByteArray(16);
        this.containerAtoms = new ArrayDeque();
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
        this.sampleTrackIndex = -1;
    }

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        return Sniffer.sniffUnfragmented(extractorInput);
    }

    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
    }

    public void seek(long j, long j2) {
        this.containerAtoms.clear();
        this.atomHeaderBytesRead = 0;
        this.sampleTrackIndex = -1;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        if (j == 0) {
            enterReadingAtomHeaderState();
        } else if (this.tracks != null) {
            updateSampleIndices(j2);
        }
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        while (true) {
            int i = this.parserState;
            if (i != 0) {
                if (i != 1) {
                    if (i == 2) {
                        return readSample(extractorInput, positionHolder);
                    }
                    throw new IllegalStateException();
                } else if (readAtomPayload(extractorInput, positionHolder)) {
                    return 1;
                }
            } else if (!readAtomHeader(extractorInput)) {
                return -1;
            }
        }
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    public SeekPoints getSeekPoints(long j) {
        Mp4Track[] mp4TrackArr = this.tracks;
        if (mp4TrackArr.length == 0) {
            return new SeekPoints(SeekPoint.START);
        }
        long j2;
        long j3;
        long j4;
        long j5;
        int i = this.firstVideoTrackIndex;
        if (i != -1) {
            TrackSampleTable trackSampleTable = mp4TrackArr[i].sampleTable;
            i = getSynchronizationSampleIndex(trackSampleTable, j);
            if (i == -1) {
                return new SeekPoints(SeekPoint.START);
            }
            j2 = trackSampleTable.timestampsUs[i];
            j3 = trackSampleTable.offsets[i];
            if (j2 < j && i < trackSampleTable.sampleCount - 1) {
                int indexOfLaterOrEqualSynchronizationSample = trackSampleTable.getIndexOfLaterOrEqualSynchronizationSample(j);
                if (!(indexOfLaterOrEqualSynchronizationSample == -1 || indexOfLaterOrEqualSynchronizationSample == i)) {
                    j4 = trackSampleTable.timestampsUs[indexOfLaterOrEqualSynchronizationSample];
                    j = trackSampleTable.offsets[indexOfLaterOrEqualSynchronizationSample];
                    j5 = j;
                    j = j2;
                }
            }
            j = -1;
            j4 = -9223372036854775807L;
            j5 = j;
            j = j2;
        } else {
            j3 = TimestampAdjuster.DO_NOT_OFFSET;
            j5 = -1;
            j4 = -9223372036854775807L;
        }
        int i2 = 0;
        while (true) {
            Mp4Track[] mp4TrackArr2 = this.tracks;
            if (i2 >= mp4TrackArr2.length) {
                break;
            }
            if (i2 != this.firstVideoTrackIndex) {
                TrackSampleTable trackSampleTable2 = mp4TrackArr2[i2].sampleTable;
                j2 = maybeAdjustSeekOffset(trackSampleTable2, j, j3);
                if (j4 != -9223372036854775807L) {
                    j5 = maybeAdjustSeekOffset(trackSampleTable2, j4, j5);
                }
                j3 = j2;
            }
            i2++;
        }
        SeekPoint seekPoint = new SeekPoint(j, j3);
        if (j4 == -9223372036854775807L) {
            return new SeekPoints(seekPoint);
        }
        return new SeekPoints(seekPoint, new SeekPoint(j4, j5));
    }

    private void enterReadingAtomHeaderState() {
        this.parserState = 0;
        this.atomHeaderBytesRead = 0;
    }

    private boolean readAtomHeader(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.atomHeaderBytesRead == 0) {
            if (!extractorInput.readFully(this.atomHeader.data, 0, 8, true)) {
                return false;
            }
            this.atomHeaderBytesRead = 8;
            this.atomHeader.setPosition(0);
            this.atomSize = this.atomHeader.readUnsignedInt();
            this.atomType = this.atomHeader.readInt();
        }
        long j = this.atomSize;
        if (j == 1) {
            extractorInput.readFully(this.atomHeader.data, 8, 8);
            this.atomHeaderBytesRead += 8;
            this.atomSize = this.atomHeader.readUnsignedLongToLong();
        } else if (j == 0) {
            j = extractorInput.getLength();
            if (j == -1 && !this.containerAtoms.isEmpty()) {
                j = ((ContainerAtom) this.containerAtoms.peek()).endPosition;
            }
            if (j != -1) {
                this.atomSize = (j - extractorInput.getPosition()) + ((long) this.atomHeaderBytesRead);
            }
        }
        if (this.atomSize >= ((long) this.atomHeaderBytesRead)) {
            if (shouldParseContainerAtom(this.atomType)) {
                long position = (extractorInput.getPosition() + this.atomSize) - ((long) this.atomHeaderBytesRead);
                this.containerAtoms.push(new ContainerAtom(this.atomType, position));
                if (this.atomSize == ((long) this.atomHeaderBytesRead)) {
                    processAtomEnded(position);
                } else {
                    enterReadingAtomHeaderState();
                }
            } else if (shouldParseLeafAtom(this.atomType)) {
                Assertions.checkState(this.atomHeaderBytesRead == 8);
                Assertions.checkState(this.atomSize <= 2147483647L);
                this.atomData = new ParsableByteArray((int) this.atomSize);
                System.arraycopy(this.atomHeader.data, 0, this.atomData.data, 0, 8);
                this.parserState = 1;
            } else {
                this.atomData = null;
                this.parserState = 1;
            }
            return true;
        }
        throw new ParserException("Atom size less than header length (unsupported).");
    }

    private boolean readAtomPayload(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        Object obj;
        long j = this.atomSize - ((long) this.atomHeaderBytesRead);
        long position = extractorInput.getPosition() + j;
        ParsableByteArray parsableByteArray = this.atomData;
        if (parsableByteArray != null) {
            extractorInput.readFully(parsableByteArray.data, this.atomHeaderBytesRead, (int) j);
            if (this.atomType == Atom.TYPE_ftyp) {
                this.isQuickTime = processFtypAtom(this.atomData);
            } else if (!this.containerAtoms.isEmpty()) {
                ((ContainerAtom) this.containerAtoms.peek()).add(new LeafAtom(this.atomType, this.atomData));
            }
        } else if (j < 262144) {
            extractorInput.skipFully((int) j);
        } else {
            positionHolder.position = extractorInput.getPosition() + j;
            obj = 1;
            processAtomEnded(position);
            if (obj != null || this.parserState == 2) {
                return false;
            }
            return true;
        }
        obj = null;
        processAtomEnded(position);
        if (obj != null) {
        }
        return false;
    }

    private void processAtomEnded(long j) throws ParserException {
        while (!this.containerAtoms.isEmpty() && ((ContainerAtom) this.containerAtoms.peek()).endPosition == j) {
            ContainerAtom containerAtom = (ContainerAtom) this.containerAtoms.pop();
            if (containerAtom.type == Atom.TYPE_moov) {
                processMoovAtom(containerAtom);
                this.containerAtoms.clear();
                this.parserState = 2;
            } else if (!this.containerAtoms.isEmpty()) {
                ((ContainerAtom) this.containerAtoms.peek()).add(containerAtom);
            }
        }
        if (this.parserState != 2) {
            enterReadingAtomHeaderState();
        }
    }

    private void processMoovAtom(ContainerAtom containerAtom) throws ParserException {
        Metadata parseUdta;
        ContainerAtom containerAtom2 = containerAtom;
        ArrayList arrayList = new ArrayList();
        GaplessInfoHolder gaplessInfoHolder = new GaplessInfoHolder();
        LeafAtom leafAtomOfType = containerAtom2.getLeafAtomOfType(Atom.TYPE_udta);
        Metadata metadata = null;
        if (leafAtomOfType != null) {
            parseUdta = AtomParsers.parseUdta(leafAtomOfType, this.isQuickTime);
            if (parseUdta != null) {
                gaplessInfoHolder.setFromMetadata(parseUdta);
            }
        } else {
            parseUdta = null;
        }
        ContainerAtom containerAtomOfType = containerAtom2.getContainerAtomOfType(Atom.TYPE_meta);
        if (containerAtomOfType != null) {
            metadata = AtomParsers.parseMdtaFromMeta(containerAtomOfType);
        }
        boolean z = true;
        if ((this.flags & 1) == 0) {
            z = false;
        }
        ArrayList trackSampleTables = getTrackSampleTables(containerAtom2, gaplessInfoHolder, z);
        int size = trackSampleTables.size();
        int i = -1;
        long j = -9223372036854775807L;
        for (int i2 = 0; i2 < size; i2++) {
            TrackSampleTable trackSampleTable = (TrackSampleTable) trackSampleTables.get(i2);
            Track track = trackSampleTable.track;
            Mp4Track mp4Track = new Mp4Track(track, trackSampleTable, this.extractorOutput.track(i2, track.type));
            mp4Track.trackOutput.format(MetadataUtil.getFormatWithMetadata(track.type, track.format.copyWithMaxInputSize(trackSampleTable.maximumSize + 30), parseUdta, metadata, gaplessInfoHolder));
            long j2 = track.durationUs;
            if (j2 == -9223372036854775807L) {
                j2 = trackSampleTable.durationUs;
            }
            j = Math.max(j, j2);
            if (track.type == 2) {
                if (i == -1) {
                    i = arrayList.size();
                }
            }
            arrayList.add(mp4Track);
        }
        this.firstVideoTrackIndex = i;
        this.durationUs = j;
        this.tracks = (Mp4Track[]) arrayList.toArray(new Mp4Track[0]);
        this.accumulatedSampleSizes = calculateAccumulatedSampleSizes(this.tracks);
        this.extractorOutput.endTracks();
        this.extractorOutput.seekMap(this);
    }

    private ArrayList<TrackSampleTable> getTrackSampleTables(ContainerAtom containerAtom, GaplessInfoHolder gaplessInfoHolder, boolean z) throws ParserException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < containerAtom.containerChildren.size(); i++) {
            ContainerAtom containerAtom2 = (ContainerAtom) containerAtom.containerChildren.get(i);
            if (containerAtom2.type == Atom.TYPE_trak) {
                Track parseTrak = AtomParsers.parseTrak(containerAtom2, containerAtom.getLeafAtomOfType(Atom.TYPE_mvhd), -9223372036854775807L, null, z, this.isQuickTime);
                if (parseTrak != null) {
                    TrackSampleTable parseStbl = AtomParsers.parseStbl(parseTrak, containerAtom2.getContainerAtomOfType(Atom.TYPE_mdia).getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl), gaplessInfoHolder);
                    if (parseStbl.sampleCount != 0) {
                        arrayList.add(parseStbl);
                    }
                }
            }
        }
        return arrayList;
    }

    private int readSample(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        long position = extractorInput.getPosition();
        if (this.sampleTrackIndex == -1) {
            this.sampleTrackIndex = getTrackIndexOfNextReadSample(position);
            if (this.sampleTrackIndex == -1) {
                return -1;
            }
        }
        Mp4Track mp4Track = this.tracks[this.sampleTrackIndex];
        TrackOutput trackOutput = mp4Track.trackOutput;
        int i = mp4Track.sampleIndex;
        TrackSampleTable trackSampleTable = mp4Track.sampleTable;
        long j = trackSampleTable.offsets[i];
        int i2 = trackSampleTable.sizes[i];
        position = (j - position) + ((long) this.sampleBytesWritten);
        if (position < 0 || position >= 262144) {
            positionHolder.position = j;
            return 1;
        }
        int i3;
        if (mp4Track.track.sampleTransformation == 1) {
            position += 8;
            i2 -= 8;
        }
        extractorInput.skipFully((int) position);
        int i4 = mp4Track.track.nalUnitLengthFieldLength;
        if (i4 == 0) {
            while (true) {
                i4 = this.sampleBytesWritten;
                if (i4 >= i2) {
                    break;
                }
                i4 = trackOutput.sampleData(extractorInput, i2 - i4, false);
                this.sampleBytesWritten += i4;
                this.sampleCurrentNalBytesRemaining -= i4;
            }
        } else {
            byte[] bArr = this.nalLength.data;
            bArr[0] = (byte) 0;
            bArr[1] = (byte) 0;
            bArr[2] = (byte) 0;
            int i5 = 4 - i4;
            while (this.sampleBytesWritten < i2) {
                i3 = this.sampleCurrentNalBytesRemaining;
                if (i3 == 0) {
                    extractorInput.readFully(this.nalLength.data, i5, i4);
                    this.nalLength.setPosition(0);
                    this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                    this.nalStartCode.setPosition(0);
                    trackOutput.sampleData(this.nalStartCode, 4);
                    this.sampleBytesWritten += 4;
                    i2 += i5;
                } else {
                    i3 = trackOutput.sampleData(extractorInput, i3, false);
                    this.sampleBytesWritten += i3;
                    this.sampleCurrentNalBytesRemaining -= i3;
                }
            }
        }
        i3 = i2;
        TrackSampleTable trackSampleTable2 = mp4Track.sampleTable;
        trackOutput.sampleMetadata(trackSampleTable2.timestampsUs[i], trackSampleTable2.flags[i], i3, 0, null);
        mp4Track.sampleIndex++;
        this.sampleTrackIndex = -1;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        return 0;
    }

    private int getTrackIndexOfNextReadSample(long j) {
        int i = 0;
        long j2 = TimestampAdjuster.DO_NOT_OFFSET;
        Object obj = 1;
        long j3 = TimestampAdjuster.DO_NOT_OFFSET;
        int i2 = -1;
        int i3 = -1;
        Object obj2 = 1;
        long j4 = TimestampAdjuster.DO_NOT_OFFSET;
        while (true) {
            Mp4Track[] mp4TrackArr = this.tracks;
            if (i >= mp4TrackArr.length) {
                break;
            }
            Mp4Track mp4Track = mp4TrackArr[i];
            int i4 = mp4Track.sampleIndex;
            TrackSampleTable trackSampleTable = mp4Track.sampleTable;
            if (i4 != trackSampleTable.sampleCount) {
                long j5 = trackSampleTable.offsets[i4];
                long j6 = this.accumulatedSampleSizes[i][i4];
                j5 -= j;
                Object obj3 = (j5 < 0 || j5 >= 262144) ? 1 : null;
                if ((obj3 == null && obj2 != null) || (obj3 == obj2 && j5 < j4)) {
                    obj2 = obj3;
                    i3 = i;
                    j4 = j5;
                    j3 = j6;
                }
                if (j6 < j2) {
                    obj = obj3;
                    i2 = i;
                    j2 = j6;
                }
            }
            i++;
        }
        return (j2 == TimestampAdjuster.DO_NOT_OFFSET || obj == null || j3 < j2 + 1048576) ? i3 : i2;
    }

    private void updateSampleIndices(long j) {
        for (Mp4Track mp4Track : this.tracks) {
            TrackSampleTable trackSampleTable = mp4Track.sampleTable;
            int indexOfEarlierOrEqualSynchronizationSample = trackSampleTable.getIndexOfEarlierOrEqualSynchronizationSample(j);
            if (indexOfEarlierOrEqualSynchronizationSample == -1) {
                indexOfEarlierOrEqualSynchronizationSample = trackSampleTable.getIndexOfLaterOrEqualSynchronizationSample(j);
            }
            mp4Track.sampleIndex = indexOfEarlierOrEqualSynchronizationSample;
        }
    }

    private static long[][] calculateAccumulatedSampleSizes(Mp4Track[] mp4TrackArr) {
        int i;
        long[][] jArr = new long[mp4TrackArr.length][];
        int[] iArr = new int[mp4TrackArr.length];
        long[] jArr2 = new long[mp4TrackArr.length];
        boolean[] zArr = new boolean[mp4TrackArr.length];
        for (i = 0; i < mp4TrackArr.length; i++) {
            jArr[i] = new long[mp4TrackArr[i].sampleTable.sampleCount];
            jArr2[i] = mp4TrackArr[i].sampleTable.timestampsUs[0];
        }
        long j = 0;
        i = 0;
        while (i < mp4TrackArr.length) {
            int i2 = -1;
            long j2 = TimestampAdjuster.DO_NOT_OFFSET;
            int i3 = 0;
            while (i3 < mp4TrackArr.length) {
                if (!zArr[i3] && jArr2[i3] <= j2) {
                    j2 = jArr2[i3];
                    i2 = i3;
                }
                i3++;
            }
            i3 = iArr[i2];
            jArr[i2][i3] = j;
            j += (long) mp4TrackArr[i2].sampleTable.sizes[i3];
            i3++;
            iArr[i2] = i3;
            if (i3 < jArr[i2].length) {
                jArr2[i2] = mp4TrackArr[i2].sampleTable.timestampsUs[i3];
            } else {
                zArr[i2] = true;
                i++;
            }
        }
        return jArr;
    }

    private static long maybeAdjustSeekOffset(TrackSampleTable trackSampleTable, long j, long j2) {
        int synchronizationSampleIndex = getSynchronizationSampleIndex(trackSampleTable, j);
        if (synchronizationSampleIndex == -1) {
            return j2;
        }
        return Math.min(trackSampleTable.offsets[synchronizationSampleIndex], j2);
    }

    private static int getSynchronizationSampleIndex(TrackSampleTable trackSampleTable, long j) {
        int indexOfEarlierOrEqualSynchronizationSample = trackSampleTable.getIndexOfEarlierOrEqualSynchronizationSample(j);
        return indexOfEarlierOrEqualSynchronizationSample == -1 ? trackSampleTable.getIndexOfLaterOrEqualSynchronizationSample(j) : indexOfEarlierOrEqualSynchronizationSample;
    }

    private static boolean processFtypAtom(ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(8);
        if (parsableByteArray.readInt() == BRAND_QUICKTIME) {
            return true;
        }
        parsableByteArray.skipBytes(4);
        while (parsableByteArray.bytesLeft() > 0) {
            if (parsableByteArray.readInt() == BRAND_QUICKTIME) {
                return true;
            }
        }
        return false;
    }

    private static boolean shouldParseLeafAtom(int i) {
        return i == Atom.TYPE_mdhd || i == Atom.TYPE_mvhd || i == Atom.TYPE_hdlr || i == Atom.TYPE_stsd || i == Atom.TYPE_stts || i == Atom.TYPE_stss || i == Atom.TYPE_ctts || i == Atom.TYPE_elst || i == Atom.TYPE_stsc || i == Atom.TYPE_stsz || i == Atom.TYPE_stz2 || i == Atom.TYPE_stco || i == Atom.TYPE_co64 || i == Atom.TYPE_tkhd || i == Atom.TYPE_ftyp || i == Atom.TYPE_udta || i == Atom.TYPE_keys || i == Atom.TYPE_ilst;
    }

    private static boolean shouldParseContainerAtom(int i) {
        return i == Atom.TYPE_moov || i == Atom.TYPE_trak || i == Atom.TYPE_mdia || i == Atom.TYPE_minf || i == Atom.TYPE_stbl || i == Atom.TYPE_edts || i == Atom.TYPE_meta;
    }
}
