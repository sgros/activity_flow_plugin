// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import java.io.IOException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.ArrayList;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import java.util.ArrayDeque;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.Extractor;

public final class Mp4Extractor implements Extractor, SeekMap
{
    private static final int BRAND_QUICKTIME;
    public static final ExtractorsFactory FACTORY;
    private long[][] accumulatedSampleSizes;
    private ParsableByteArray atomData;
    private final ParsableByteArray atomHeader;
    private int atomHeaderBytesRead;
    private long atomSize;
    private int atomType;
    private final ArrayDeque<Atom.ContainerAtom> containerAtoms;
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
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$Mp4Extractor$quy71uYOGsneho91FZy1d2UGE1Q.INSTANCE;
        BRAND_QUICKTIME = Util.getIntegerCodeForString("qt  ");
    }
    
    public Mp4Extractor() {
        this(0);
    }
    
    public Mp4Extractor(final int flags) {
        this.flags = flags;
        this.atomHeader = new ParsableByteArray(16);
        this.containerAtoms = new ArrayDeque<Atom.ContainerAtom>();
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
        this.sampleTrackIndex = -1;
    }
    
    private static long[][] calculateAccumulatedSampleSizes(final Mp4Track[] array) {
        final long[][] array2 = new long[array.length][];
        final int[] array3 = new int[array.length];
        final long[] array4 = new long[array.length];
        final boolean[] array5 = new boolean[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = new long[array[i].sampleTable.sampleCount];
            array4[i] = array[i].sampleTable.timestampsUs[0];
        }
        long n = 0L;
        int j = 0;
        while (j < array.length) {
            int n2 = -1;
            long n3 = Long.MAX_VALUE;
            int n4;
            long n5;
            for (int k = 0; k < array.length; ++k, n2 = n4, n3 = n5) {
                n4 = n2;
                n5 = n3;
                if (!array5[k]) {
                    n4 = n2;
                    n5 = n3;
                    if (array4[k] <= n3) {
                        n5 = array4[k];
                        n4 = k;
                    }
                }
            }
            int n6 = array3[n2];
            array2[n2][n6] = n;
            n += array[n2].sampleTable.sizes[n6];
            ++n6;
            if ((array3[n2] = n6) < array2[n2].length) {
                array4[n2] = array[n2].sampleTable.timestampsUs[n6];
            }
            else {
                array5[n2] = true;
                ++j;
            }
        }
        return array2;
    }
    
    private void enterReadingAtomHeaderState() {
        this.parserState = 0;
        this.atomHeaderBytesRead = 0;
    }
    
    private static int getSynchronizationSampleIndex(final TrackSampleTable trackSampleTable, final long n) {
        int n2;
        if ((n2 = trackSampleTable.getIndexOfEarlierOrEqualSynchronizationSample(n)) == -1) {
            n2 = trackSampleTable.getIndexOfLaterOrEqualSynchronizationSample(n);
        }
        return n2;
    }
    
    private int getTrackIndexOfNextReadSample(final long n) {
        int n2 = 0;
        long n3 = Long.MAX_VALUE;
        int n4 = 1;
        long n5 = Long.MAX_VALUE;
        int n6 = -1;
        int n7 = -1;
        int n8 = 1;
        long n9 = Long.MAX_VALUE;
        while (true) {
            final Mp4Track[] tracks = this.tracks;
            if (n2 >= tracks.length) {
                break;
            }
            final Mp4Track mp4Track = tracks[n2];
            final int sampleIndex = mp4Track.sampleIndex;
            final TrackSampleTable sampleTable = mp4Track.sampleTable;
            long n10;
            if (sampleIndex == sampleTable.sampleCount) {
                n10 = n3;
            }
            else {
                final long n11 = sampleTable.offsets[sampleIndex];
                final long n12 = this.accumulatedSampleSizes[n2][sampleIndex];
                final long n13 = n11 - n;
                final boolean b = n13 < 0L || n13 >= 262144L;
                long n14 = 0L;
                int n15 = 0;
                int n16 = 0;
                long n17 = 0L;
                Label_0206: {
                    if (b || n8 == 0) {
                        n14 = n5;
                        n15 = n7;
                        n16 = n8;
                        n17 = n9;
                        if ((b ? 1 : 0) != n8) {
                            break Label_0206;
                        }
                        n14 = n5;
                        n15 = n7;
                        n16 = n8;
                        n17 = n9;
                        if (n13 >= n9) {
                            break Label_0206;
                        }
                    }
                    n16 = (b ? 1 : 0);
                    n15 = n2;
                    n17 = n13;
                    n14 = n12;
                }
                n10 = n3;
                n5 = n14;
                n7 = n15;
                n8 = n16;
                n9 = n17;
                if (n12 < n3) {
                    n6 = n2;
                    n9 = n17;
                    n8 = n16;
                    n7 = n15;
                    n5 = n14;
                    n4 = (b ? 1 : 0);
                    n10 = n12;
                }
            }
            ++n2;
            n3 = n10;
        }
        if (n3 == Long.MAX_VALUE || n4 == 0 || n5 < n3 + 1048576L) {
            n6 = n7;
        }
        return n6;
    }
    
    private ArrayList<TrackSampleTable> getTrackSampleTables(final Atom.ContainerAtom containerAtom, final GaplessInfoHolder gaplessInfoHolder, final boolean b) throws ParserException {
        final ArrayList<TrackSampleTable> list = new ArrayList<TrackSampleTable>();
        for (int i = 0; i < containerAtom.containerChildren.size(); ++i) {
            final Atom.ContainerAtom containerAtom2 = containerAtom.containerChildren.get(i);
            if (containerAtom2.type == Atom.TYPE_trak) {
                final Track trak = AtomParsers.parseTrak(containerAtom2, containerAtom.getLeafAtomOfType(Atom.TYPE_mvhd), -9223372036854775807L, null, b, this.isQuickTime);
                if (trak != null) {
                    final TrackSampleTable stbl = AtomParsers.parseStbl(trak, containerAtom2.getContainerAtomOfType(Atom.TYPE_mdia).getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl), gaplessInfoHolder);
                    if (stbl.sampleCount != 0) {
                        list.add(stbl);
                    }
                }
            }
        }
        return list;
    }
    
    private static long maybeAdjustSeekOffset(final TrackSampleTable trackSampleTable, final long n, final long b) {
        final int synchronizationSampleIndex = getSynchronizationSampleIndex(trackSampleTable, n);
        if (synchronizationSampleIndex == -1) {
            return b;
        }
        return Math.min(trackSampleTable.offsets[synchronizationSampleIndex], b);
    }
    
    private void processAtomEnded(final long n) throws ParserException {
        while (!this.containerAtoms.isEmpty() && this.containerAtoms.peek().endPosition == n) {
            final Atom.ContainerAtom containerAtom = this.containerAtoms.pop();
            if (containerAtom.type == Atom.TYPE_moov) {
                this.processMoovAtom(containerAtom);
                this.containerAtoms.clear();
                this.parserState = 2;
            }
            else {
                if (this.containerAtoms.isEmpty()) {
                    continue;
                }
                this.containerAtoms.peek().add(containerAtom);
            }
        }
        if (this.parserState != 2) {
            this.enterReadingAtomHeaderState();
        }
    }
    
    private static boolean processFtypAtom(final ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(8);
        if (parsableByteArray.readInt() == Mp4Extractor.BRAND_QUICKTIME) {
            return true;
        }
        parsableByteArray.skipBytes(4);
        while (parsableByteArray.bytesLeft() > 0) {
            if (parsableByteArray.readInt() == Mp4Extractor.BRAND_QUICKTIME) {
                return true;
            }
        }
        return false;
    }
    
    private void processMoovAtom(final Atom.ContainerAtom containerAtom) throws ParserException {
        final ArrayList<Mp4Track> list = new ArrayList<Mp4Track>();
        final GaplessInfoHolder gaplessInfoHolder = new GaplessInfoHolder();
        final Atom.LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_udta);
        Metadata mdtaFromMeta = null;
        Metadata metadata;
        if (leafAtomOfType != null) {
            final Metadata udta = AtomParsers.parseUdta(leafAtomOfType, this.isQuickTime);
            if ((metadata = udta) != null) {
                gaplessInfoHolder.setFromMetadata(udta);
                metadata = udta;
            }
        }
        else {
            metadata = null;
        }
        final Atom.ContainerAtom containerAtomOfType = containerAtom.getContainerAtomOfType(Atom.TYPE_meta);
        if (containerAtomOfType != null) {
            mdtaFromMeta = AtomParsers.parseMdtaFromMeta(containerAtomOfType);
        }
        final int flags = this.flags;
        boolean b = true;
        if ((flags & 0x1) == 0x0) {
            b = false;
        }
        final ArrayList<TrackSampleTable> trackSampleTables = this.getTrackSampleTables(containerAtom, gaplessInfoHolder, b);
        final int size = trackSampleTables.size();
        int i = 0;
        int firstVideoTrackIndex = -1;
        long max = -9223372036854775807L;
        while (i < size) {
            final TrackSampleTable trackSampleTable = trackSampleTables.get(i);
            final Track track = trackSampleTable.track;
            final Mp4Track mp4Track = new Mp4Track(track, trackSampleTable, this.extractorOutput.track(i, track.type));
            mp4Track.trackOutput.format(MetadataUtil.getFormatWithMetadata(track.type, track.format.copyWithMaxInputSize(trackSampleTable.maximumSize + 30), metadata, mdtaFromMeta, gaplessInfoHolder));
            long b2 = track.durationUs;
            if (b2 == -9223372036854775807L) {
                b2 = trackSampleTable.durationUs;
            }
            max = Math.max(max, b2);
            int size2;
            if (track.type == 2) {
                if ((size2 = firstVideoTrackIndex) == -1) {
                    size2 = list.size();
                }
            }
            else {
                size2 = firstVideoTrackIndex;
            }
            list.add(mp4Track);
            ++i;
            firstVideoTrackIndex = size2;
        }
        this.firstVideoTrackIndex = firstVideoTrackIndex;
        this.durationUs = max;
        this.tracks = list.toArray(new Mp4Track[0]);
        this.accumulatedSampleSizes = calculateAccumulatedSampleSizes(this.tracks);
        this.extractorOutput.endTracks();
        this.extractorOutput.seekMap(this);
    }
    
    private boolean readAtomHeader(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.atomHeaderBytesRead == 0) {
            if (!extractorInput.readFully(this.atomHeader.data, 0, 8, true)) {
                return false;
            }
            this.atomHeaderBytesRead = 8;
            this.atomHeader.setPosition(0);
            this.atomSize = this.atomHeader.readUnsignedInt();
            this.atomType = this.atomHeader.readInt();
        }
        final long atomSize = this.atomSize;
        if (atomSize == 1L) {
            extractorInput.readFully(this.atomHeader.data, 8, 8);
            this.atomHeaderBytesRead += 8;
            this.atomSize = this.atomHeader.readUnsignedLongToLong();
        }
        else if (atomSize == 0L) {
            long n2;
            final long n = n2 = extractorInput.getLength();
            if (n == -1L) {
                n2 = n;
                if (!this.containerAtoms.isEmpty()) {
                    n2 = this.containerAtoms.peek().endPosition;
                }
            }
            if (n2 != -1L) {
                this.atomSize = n2 - extractorInput.getPosition() + this.atomHeaderBytesRead;
            }
        }
        if (this.atomSize >= this.atomHeaderBytesRead) {
            if (shouldParseContainerAtom(this.atomType)) {
                final long n3 = extractorInput.getPosition() + this.atomSize - this.atomHeaderBytesRead;
                this.containerAtoms.push(new Atom.ContainerAtom(this.atomType, n3));
                if (this.atomSize == this.atomHeaderBytesRead) {
                    this.processAtomEnded(n3);
                }
                else {
                    this.enterReadingAtomHeaderState();
                }
            }
            else if (shouldParseLeafAtom(this.atomType)) {
                Assertions.checkState(this.atomHeaderBytesRead == 8);
                Assertions.checkState(this.atomSize <= 2147483647L);
                this.atomData = new ParsableByteArray((int)this.atomSize);
                System.arraycopy(this.atomHeader.data, 0, this.atomData.data, 0, 8);
                this.parserState = 1;
            }
            else {
                this.atomData = null;
                this.parserState = 1;
            }
            return true;
        }
        throw new ParserException("Atom size less than header length (unsupported).");
    }
    
    private boolean readAtomPayload(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final long n = this.atomSize - this.atomHeaderBytesRead;
        final long position = extractorInput.getPosition();
        final ParsableByteArray atomData = this.atomData;
        boolean b = true;
        boolean b2 = false;
        Label_0152: {
            if (atomData != null) {
                extractorInput.readFully(atomData.data, this.atomHeaderBytesRead, (int)n);
                if (this.atomType == Atom.TYPE_ftyp) {
                    this.isQuickTime = processFtypAtom(this.atomData);
                }
                else if (!this.containerAtoms.isEmpty()) {
                    this.containerAtoms.peek().add(new Atom.LeafAtom(this.atomType, this.atomData));
                }
            }
            else {
                if (n >= 262144L) {
                    positionHolder.position = extractorInput.getPosition() + n;
                    b2 = true;
                    break Label_0152;
                }
                extractorInput.skipFully((int)n);
            }
            b2 = false;
        }
        this.processAtomEnded(position + n);
        if (!b2 || this.parserState == 2) {
            b = false;
        }
        return b;
    }
    
    private int readSample(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final long position = extractorInput.getPosition();
        if (this.sampleTrackIndex == -1) {
            this.sampleTrackIndex = this.getTrackIndexOfNextReadSample(position);
            if (this.sampleTrackIndex == -1) {
                return -1;
            }
        }
        final Mp4Track mp4Track = this.tracks[this.sampleTrackIndex];
        final TrackOutput trackOutput = mp4Track.trackOutput;
        final int sampleIndex = mp4Track.sampleIndex;
        final TrackSampleTable sampleTable = mp4Track.sampleTable;
        final long position2 = sampleTable.offsets[sampleIndex];
        final int n = sampleTable.sizes[sampleIndex];
        final long n2 = position2 - position + this.sampleBytesWritten;
        if (n2 >= 0L && n2 < 262144L) {
            long n3 = n2;
            int n4 = n;
            if (mp4Track.track.sampleTransformation == 1) {
                n3 = n2 + 8L;
                n4 = n - 8;
            }
            extractorInput.skipFully((int)n3);
            final int nalUnitLengthFieldLength = mp4Track.track.nalUnitLengthFieldLength;
            int n6;
            if (nalUnitLengthFieldLength != 0) {
                final byte[] data = this.nalLength.data;
                data[0] = 0;
                data[1] = 0;
                data[2] = 0;
                final int n5 = 4 - nalUnitLengthFieldLength;
                while (true) {
                    n6 = n4;
                    if (this.sampleBytesWritten >= n4) {
                        break;
                    }
                    final int sampleCurrentNalBytesRemaining = this.sampleCurrentNalBytesRemaining;
                    if (sampleCurrentNalBytesRemaining == 0) {
                        extractorInput.readFully(this.nalLength.data, n5, nalUnitLengthFieldLength);
                        this.nalLength.setPosition(0);
                        this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                        this.nalStartCode.setPosition(0);
                        trackOutput.sampleData(this.nalStartCode, 4);
                        this.sampleBytesWritten += 4;
                        n4 += n5;
                    }
                    else {
                        final int sampleData = trackOutput.sampleData(extractorInput, sampleCurrentNalBytesRemaining, false);
                        this.sampleBytesWritten += sampleData;
                        this.sampleCurrentNalBytesRemaining -= sampleData;
                    }
                }
            }
            else {
                while (true) {
                    final int sampleBytesWritten = this.sampleBytesWritten;
                    if (sampleBytesWritten >= (n6 = n4)) {
                        break;
                    }
                    final int sampleData2 = trackOutput.sampleData(extractorInput, n4 - sampleBytesWritten, false);
                    this.sampleBytesWritten += sampleData2;
                    this.sampleCurrentNalBytesRemaining -= sampleData2;
                }
            }
            final TrackSampleTable sampleTable2 = mp4Track.sampleTable;
            trackOutput.sampleMetadata(sampleTable2.timestampsUs[sampleIndex], sampleTable2.flags[sampleIndex], n6, 0, null);
            ++mp4Track.sampleIndex;
            this.sampleTrackIndex = -1;
            this.sampleBytesWritten = 0;
            return this.sampleCurrentNalBytesRemaining = 0;
        }
        positionHolder.position = position2;
        return 1;
    }
    
    private static boolean shouldParseContainerAtom(final int n) {
        return n == Atom.TYPE_moov || n == Atom.TYPE_trak || n == Atom.TYPE_mdia || n == Atom.TYPE_minf || n == Atom.TYPE_stbl || n == Atom.TYPE_edts || n == Atom.TYPE_meta;
    }
    
    private static boolean shouldParseLeafAtom(final int n) {
        return n == Atom.TYPE_mdhd || n == Atom.TYPE_mvhd || n == Atom.TYPE_hdlr || n == Atom.TYPE_stsd || n == Atom.TYPE_stts || n == Atom.TYPE_stss || n == Atom.TYPE_ctts || n == Atom.TYPE_elst || n == Atom.TYPE_stsc || n == Atom.TYPE_stsz || n == Atom.TYPE_stz2 || n == Atom.TYPE_stco || n == Atom.TYPE_co64 || n == Atom.TYPE_tkhd || n == Atom.TYPE_ftyp || n == Atom.TYPE_udta || n == Atom.TYPE_keys || n == Atom.TYPE_ilst;
    }
    
    private void updateSampleIndices(final long n) {
        for (final Mp4Track mp4Track : this.tracks) {
            final TrackSampleTable sampleTable = mp4Track.sampleTable;
            int sampleIndex;
            if ((sampleIndex = sampleTable.getIndexOfEarlierOrEqualSynchronizationSample(n)) == -1) {
                sampleIndex = sampleTable.getIndexOfLaterOrEqualSynchronizationSample(n);
            }
            mp4Track.sampleIndex = sampleIndex;
        }
    }
    
    @Override
    public long getDurationUs() {
        return this.durationUs;
    }
    
    @Override
    public SeekPoints getSeekPoints(long n) {
        final Mp4Track[] tracks = this.tracks;
        if (tracks.length == 0) {
            return new SeekPoints(SeekPoint.START);
        }
        final int firstVideoTrackIndex = this.firstVideoTrackIndex;
        long n2;
        long n5;
        long n6;
        if (firstVideoTrackIndex != -1) {
            final TrackSampleTable sampleTable = tracks[firstVideoTrackIndex].sampleTable;
            final int synchronizationSampleIndex = getSynchronizationSampleIndex(sampleTable, n);
            if (synchronizationSampleIndex == -1) {
                return new SeekPoints(SeekPoint.START);
            }
            n2 = sampleTable.timestampsUs[synchronizationSampleIndex];
            final long n3 = sampleTable.offsets[synchronizationSampleIndex];
            long n4 = 0L;
            Label_0150: {
                if (n2 < n && synchronizationSampleIndex < sampleTable.sampleCount - 1) {
                    final int indexOfLaterOrEqualSynchronizationSample = sampleTable.getIndexOfLaterOrEqualSynchronizationSample(n);
                    if (indexOfLaterOrEqualSynchronizationSample != -1 && indexOfLaterOrEqualSynchronizationSample != synchronizationSampleIndex) {
                        n = sampleTable.timestampsUs[indexOfLaterOrEqualSynchronizationSample];
                        n4 = sampleTable.offsets[indexOfLaterOrEqualSynchronizationSample];
                        break Label_0150;
                    }
                }
                n4 = -1L;
                n = -9223372036854775807L;
            }
            n5 = n;
            n = n4;
            n6 = n3;
        }
        else {
            n6 = Long.MAX_VALUE;
            final long n7 = -1L;
            n5 = -9223372036854775807L;
            n2 = n;
            n = n7;
        }
        int n8 = 0;
        while (true) {
            final Mp4Track[] tracks2 = this.tracks;
            if (n8 >= tracks2.length) {
                break;
            }
            long n9 = n;
            long maybeAdjustSeekOffset = n6;
            if (n8 != this.firstVideoTrackIndex) {
                final TrackSampleTable sampleTable2 = tracks2[n8].sampleTable;
                maybeAdjustSeekOffset = maybeAdjustSeekOffset(sampleTable2, n2, n6);
                long maybeAdjustSeekOffset2 = n;
                if (n5 != -9223372036854775807L) {
                    maybeAdjustSeekOffset2 = maybeAdjustSeekOffset(sampleTable2, n5, n);
                }
                n9 = maybeAdjustSeekOffset2;
            }
            ++n8;
            n = n9;
            n6 = maybeAdjustSeekOffset;
        }
        final SeekPoint seekPoint = new SeekPoint(n2, n6);
        if (n5 == -9223372036854775807L) {
            return new SeekPoints(seekPoint);
        }
        return new SeekPoints(seekPoint, new SeekPoint(n5, n));
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
    }
    
    @Override
    public boolean isSeekable() {
        return true;
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        while (true) {
            final int parserState = this.parserState;
            if (parserState != 0) {
                if (parserState != 1) {
                    if (parserState == 2) {
                        return this.readSample(extractorInput, positionHolder);
                    }
                    throw new IllegalStateException();
                }
                else {
                    if (this.readAtomPayload(extractorInput, positionHolder)) {
                        return 1;
                    }
                    continue;
                }
            }
            else {
                if (!this.readAtomHeader(extractorInput)) {
                    return -1;
                }
                continue;
            }
        }
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        this.containerAtoms.clear();
        this.atomHeaderBytesRead = 0;
        this.sampleTrackIndex = -1;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        if (n == 0L) {
            this.enterReadingAtomHeaderState();
        }
        else if (this.tracks != null) {
            this.updateSampleIndices(n2);
        }
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        return Sniffer.sniffUnfragmented(extractorInput);
    }
    
    private static final class Mp4Track
    {
        public int sampleIndex;
        public final TrackSampleTable sampleTable;
        public final Track track;
        public final TrackOutput trackOutput;
        
        public Mp4Track(final Track track, final TrackSampleTable sampleTable, final TrackOutput trackOutput) {
            this.track = track;
            this.sampleTable = sampleTable;
            this.trackOutput = trackOutput;
        }
    }
}
