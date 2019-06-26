// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.text.cea.CeaUtil;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import android.util.Pair;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.ParserException;
import java.util.Arrays;
import java.util.UUID;
import com.google.android.exoplayer2.util.Log;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.util.Collections;
import com.google.android.exoplayer2.util.Util;
import android.util.SparseArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import java.util.ArrayDeque;
import java.util.List;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.Extractor;

public class FragmentedMp4Extractor implements Extractor
{
    private static final Format EMSG_FORMAT;
    public static final ExtractorsFactory FACTORY;
    private static final byte[] PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE;
    private static final int SAMPLE_GROUP_TYPE_seig;
    private final TrackOutput additionalEmsgTrackOutput;
    private ParsableByteArray atomData;
    private final ParsableByteArray atomHeader;
    private int atomHeaderBytesRead;
    private long atomSize;
    private int atomType;
    private TrackOutput[] cea608TrackOutputs;
    private final List<Format> closedCaptionFormats;
    private final ArrayDeque<Atom.ContainerAtom> containerAtoms;
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
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$FragmentedMp4Extractor$i0zfpH_PcF0vytkdatCL0xeWFhQ.INSTANCE;
        SAMPLE_GROUP_TYPE_seig = Util.getIntegerCodeForString("seig");
        PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE = new byte[] { -94, 57, 79, 82, 90, -101, 79, 20, -94, 68, 108, 66, 124, 100, -115, -12 };
        EMSG_FORMAT = Format.createSampleFormat(null, "application/x-emsg", Long.MAX_VALUE);
    }
    
    public FragmentedMp4Extractor() {
        this(0);
    }
    
    public FragmentedMp4Extractor(final int n) {
        this(n, null);
    }
    
    public FragmentedMp4Extractor(final int n, final TimestampAdjuster timestampAdjuster) {
        this(n, timestampAdjuster, null, null);
    }
    
    public FragmentedMp4Extractor(final int n, final TimestampAdjuster timestampAdjuster, final Track track, final DrmInitData drmInitData) {
        this(n, timestampAdjuster, track, drmInitData, Collections.emptyList());
    }
    
    public FragmentedMp4Extractor(final int n, final TimestampAdjuster timestampAdjuster, final Track track, final DrmInitData drmInitData, final List<Format> list) {
        this(n, timestampAdjuster, track, drmInitData, list, null);
    }
    
    public FragmentedMp4Extractor(final int n, final TimestampAdjuster timestampAdjuster, final Track sideloadedTrack, final DrmInitData sideloadedDrmInitData, final List<Format> list, final TrackOutput additionalEmsgTrackOutput) {
        int n2;
        if (sideloadedTrack != null) {
            n2 = 8;
        }
        else {
            n2 = 0;
        }
        this.flags = (n | n2);
        this.timestampAdjuster = timestampAdjuster;
        this.sideloadedTrack = sideloadedTrack;
        this.sideloadedDrmInitData = sideloadedDrmInitData;
        this.closedCaptionFormats = Collections.unmodifiableList((List<? extends Format>)list);
        this.additionalEmsgTrackOutput = additionalEmsgTrackOutput;
        this.atomHeader = new ParsableByteArray(16);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalPrefix = new ParsableByteArray(5);
        this.nalBuffer = new ParsableByteArray();
        this.extendedTypeScratch = new byte[16];
        this.containerAtoms = new ArrayDeque<Atom.ContainerAtom>();
        this.pendingMetadataSampleInfos = new ArrayDeque<MetadataSampleInfo>();
        this.trackBundles = (SparseArray<TrackBundle>)new SparseArray();
        this.durationUs = -9223372036854775807L;
        this.pendingSeekTimeUs = -9223372036854775807L;
        this.segmentIndexEarliestPresentationTimeUs = -9223372036854775807L;
        this.enterReadingAtomHeaderState();
    }
    
    private void enterReadingAtomHeaderState() {
        this.parserState = 0;
        this.atomHeaderBytesRead = 0;
    }
    
    private DefaultSampleValues getDefaultSampleValues(final SparseArray<DefaultSampleValues> sparseArray, final int n) {
        if (sparseArray.size() == 1) {
            return (DefaultSampleValues)sparseArray.valueAt(0);
        }
        final Object value = sparseArray.get(n);
        Assertions.checkNotNull(value);
        return (DefaultSampleValues)value;
    }
    
    private static DrmInitData getDrmInitDataFromAtoms(final List<Atom.LeafAtom> list) {
        final int size = list.size();
        final DrmInitData drmInitData = null;
        int i = 0;
        List<DrmInitData.SchemeData> list2 = null;
        while (i < size) {
            final Atom.LeafAtom leafAtom = list.get(i);
            List<DrmInitData.SchemeData> list3 = list2;
            if (leafAtom.type == Atom.TYPE_pssh) {
                if ((list3 = list2) == null) {
                    list3 = new ArrayList<DrmInitData.SchemeData>();
                }
                final byte[] data = leafAtom.data.data;
                final UUID uuid = PsshAtomUtil.parseUuid(data);
                if (uuid == null) {
                    Log.w("FragmentedMp4Extractor", "Skipped pssh atom (failed to extract uuid)");
                }
                else {
                    ((ArrayList<DrmInitData.SchemeData>)list3).add(new DrmInitData.SchemeData(uuid, "video/mp4", data));
                }
            }
            ++i;
            list2 = list3;
        }
        DrmInitData drmInitData2;
        if (list2 == null) {
            drmInitData2 = drmInitData;
        }
        else {
            drmInitData2 = new DrmInitData(list2);
        }
        return drmInitData2;
    }
    
    private static TrackBundle getNextFragmentRun(final SparseArray<TrackBundle> sparseArray) {
        final int size = sparseArray.size();
        TrackBundle trackBundle = null;
        long n = Long.MAX_VALUE;
        long n2;
        for (int i = 0; i < size; ++i, n = n2) {
            final TrackBundle trackBundle2 = (TrackBundle)sparseArray.valueAt(i);
            final int currentTrackRunIndex = trackBundle2.currentTrackRunIndex;
            final TrackFragment fragment = trackBundle2.fragment;
            if (currentTrackRunIndex == fragment.trunCount) {
                n2 = n;
            }
            else {
                final long n3 = fragment.trunDataPosition[currentTrackRunIndex];
                n2 = n;
                if (n3 < n) {
                    trackBundle = trackBundle2;
                    n2 = n3;
                }
            }
        }
        return trackBundle;
    }
    
    private static TrackBundle getTrackBundle(final SparseArray<TrackBundle> sparseArray, final int n) {
        if (sparseArray.size() == 1) {
            return (TrackBundle)sparseArray.valueAt(0);
        }
        return (TrackBundle)sparseArray.get(n);
    }
    
    private void maybeInitExtraTracks() {
        final TrackOutput[] emsgTrackOutputs = this.emsgTrackOutputs;
        final int n = 0;
        if (emsgTrackOutputs == null) {
            this.emsgTrackOutputs = new TrackOutput[2];
            final TrackOutput additionalEmsgTrackOutput = this.additionalEmsgTrackOutput;
            int n2;
            if (additionalEmsgTrackOutput != null) {
                this.emsgTrackOutputs[0] = additionalEmsgTrackOutput;
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            int newLength = n2;
            if ((this.flags & 0x4) != 0x0) {
                this.emsgTrackOutputs[n2] = this.extractorOutput.track(this.trackBundles.size(), 4);
                newLength = n2 + 1;
            }
            this.emsgTrackOutputs = Arrays.copyOf(this.emsgTrackOutputs, newLength);
            final TrackOutput[] emsgTrackOutputs2 = this.emsgTrackOutputs;
            for (int length = emsgTrackOutputs2.length, i = 0; i < length; ++i) {
                emsgTrackOutputs2[i].format(FragmentedMp4Extractor.EMSG_FORMAT);
            }
        }
        if (this.cea608TrackOutputs == null) {
            this.cea608TrackOutputs = new TrackOutput[this.closedCaptionFormats.size()];
            for (int j = n; j < this.cea608TrackOutputs.length; ++j) {
                final TrackOutput track = this.extractorOutput.track(this.trackBundles.size() + 1 + j, 3);
                track.format(this.closedCaptionFormats.get(j));
                this.cea608TrackOutputs[j] = track;
            }
        }
    }
    
    private void onContainerAtomRead(final Atom.ContainerAtom containerAtom) throws ParserException {
        final int type = containerAtom.type;
        if (type == Atom.TYPE_moov) {
            this.onMoovContainerAtomRead(containerAtom);
        }
        else if (type == Atom.TYPE_moof) {
            this.onMoofContainerAtomRead(containerAtom);
        }
        else if (!this.containerAtoms.isEmpty()) {
            this.containerAtoms.peek().add(containerAtom);
        }
    }
    
    private void onEmsgLeafAtomRead(final ParsableByteArray parsableByteArray) {
        final TrackOutput[] emsgTrackOutputs = this.emsgTrackOutputs;
        if (emsgTrackOutputs != null) {
            if (emsgTrackOutputs.length != 0) {
                parsableByteArray.setPosition(12);
                final int bytesLeft = parsableByteArray.bytesLeft();
                parsableByteArray.readNullTerminatedString();
                parsableByteArray.readNullTerminatedString();
                final long scaleLargeTimestamp = Util.scaleLargeTimestamp(parsableByteArray.readUnsignedInt(), 1000000L, parsableByteArray.readUnsignedInt());
                for (final TrackOutput trackOutput : this.emsgTrackOutputs) {
                    parsableByteArray.setPosition(12);
                    trackOutput.sampleData(parsableByteArray, bytesLeft);
                }
                final long segmentIndexEarliestPresentationTimeUs = this.segmentIndexEarliestPresentationTimeUs;
                if (segmentIndexEarliestPresentationTimeUs != -9223372036854775807L) {
                    long adjustSampleTimestamp = segmentIndexEarliestPresentationTimeUs + scaleLargeTimestamp;
                    final TimestampAdjuster timestampAdjuster = this.timestampAdjuster;
                    if (timestampAdjuster != null) {
                        adjustSampleTimestamp = timestampAdjuster.adjustSampleTimestamp(adjustSampleTimestamp);
                    }
                    final TrackOutput[] emsgTrackOutputs3 = this.emsgTrackOutputs;
                    for (int length2 = emsgTrackOutputs3.length, j = 0; j < length2; ++j) {
                        emsgTrackOutputs3[j].sampleMetadata(adjustSampleTimestamp, 1, bytesLeft, 0, null);
                    }
                }
                else {
                    this.pendingMetadataSampleInfos.addLast(new MetadataSampleInfo(scaleLargeTimestamp, bytesLeft));
                    this.pendingMetadataSampleBytes += bytesLeft;
                }
            }
        }
    }
    
    private void onLeafAtomRead(final Atom.LeafAtom leafAtom, final long n) throws ParserException {
        if (!this.containerAtoms.isEmpty()) {
            this.containerAtoms.peek().add(leafAtom);
        }
        else {
            final int type = leafAtom.type;
            if (type == Atom.TYPE_sidx) {
                final Pair<Long, ChunkIndex> sidx = parseSidx(leafAtom.data, n);
                this.segmentIndexEarliestPresentationTimeUs = (long)sidx.first;
                this.extractorOutput.seekMap((SeekMap)sidx.second);
                this.haveOutputSeekMap = true;
            }
            else if (type == Atom.TYPE_emsg) {
                this.onEmsgLeafAtomRead(leafAtom.data);
            }
        }
    }
    
    private void onMoofContainerAtomRead(final Atom.ContainerAtom containerAtom) throws ParserException {
        parseMoof(containerAtom, this.trackBundles, this.flags, this.extendedTypeScratch);
        DrmInitData drmInitDataFromAtoms;
        if (this.sideloadedDrmInitData != null) {
            drmInitDataFromAtoms = null;
        }
        else {
            drmInitDataFromAtoms = getDrmInitDataFromAtoms(containerAtom.leafChildren);
        }
        final int n = 0;
        if (drmInitDataFromAtoms != null) {
            for (int size = this.trackBundles.size(), i = 0; i < size; ++i) {
                ((TrackBundle)this.trackBundles.valueAt(i)).updateDrmInitData(drmInitDataFromAtoms);
            }
        }
        if (this.pendingSeekTimeUs != -9223372036854775807L) {
            for (int size2 = this.trackBundles.size(), j = n; j < size2; ++j) {
                ((TrackBundle)this.trackBundles.valueAt(j)).seek(this.pendingSeekTimeUs);
            }
            this.pendingSeekTimeUs = -9223372036854775807L;
        }
    }
    
    private void onMoovContainerAtomRead(final Atom.ContainerAtom containerAtom) throws ParserException {
        final Track sideloadedTrack = this.sideloadedTrack;
        final boolean b = true;
        final int n = 0;
        final int n2 = 0;
        Assertions.checkState(sideloadedTrack == null, "Unexpected moov box.");
        DrmInitData drmInitData = this.sideloadedDrmInitData;
        if (drmInitData == null) {
            drmInitData = getDrmInitDataFromAtoms(containerAtom.leafChildren);
        }
        final Atom.ContainerAtom containerAtomOfType = containerAtom.getContainerAtomOfType(Atom.TYPE_mvex);
        final SparseArray sparseArray = new SparseArray();
        final int size = containerAtomOfType.leafChildren.size();
        long mehd = -9223372036854775807L;
        for (int i = 0; i < size; ++i) {
            final Atom.LeafAtom leafAtom = containerAtomOfType.leafChildren.get(i);
            final int type = leafAtom.type;
            if (type == Atom.TYPE_trex) {
                final Pair<Integer, DefaultSampleValues> trex = parseTrex(leafAtom.data);
                sparseArray.put((int)trex.first, trex.second);
            }
            else if (type == Atom.TYPE_mehd) {
                mehd = parseMehd(leafAtom.data);
            }
        }
        final SparseArray sparseArray2 = new SparseArray();
        for (int size2 = containerAtom.containerChildren.size(), j = 0; j < size2; ++j) {
            final Atom.ContainerAtom containerAtom2 = containerAtom.containerChildren.get(j);
            if (containerAtom2.type == Atom.TYPE_trak) {
                final Track trak = AtomParsers.parseTrak(containerAtom2, containerAtom.getLeafAtomOfType(Atom.TYPE_mvhd), mehd, drmInitData, (this.flags & 0x10) != 0x0, false);
                this.modifyTrack(trak);
                if (trak != null) {
                    sparseArray2.put(trak.id, (Object)trak);
                }
            }
        }
        final int size3 = sparseArray2.size();
        if (this.trackBundles.size() == 0) {
            for (int k = n2; k < size3; ++k) {
                final Track track = (Track)sparseArray2.valueAt(k);
                final TrackBundle trackBundle = new TrackBundle(this.extractorOutput.track(k, track.type));
                trackBundle.init(track, this.getDefaultSampleValues((SparseArray<DefaultSampleValues>)sparseArray, track.id));
                this.trackBundles.put(track.id, (Object)trackBundle);
                this.durationUs = Math.max(this.durationUs, track.durationUs);
            }
            this.maybeInitExtraTracks();
            this.extractorOutput.endTracks();
        }
        else {
            Assertions.checkState(this.trackBundles.size() == size3 && b);
            for (int l = n; l < size3; ++l) {
                final Track track2 = (Track)sparseArray2.valueAt(l);
                ((TrackBundle)this.trackBundles.get(track2.id)).init(track2, this.getDefaultSampleValues((SparseArray<DefaultSampleValues>)sparseArray, track2.id));
            }
        }
    }
    
    private void outputPendingMetadataSamples(final long n) {
        while (!this.pendingMetadataSampleInfos.isEmpty()) {
            final MetadataSampleInfo metadataSampleInfo = this.pendingMetadataSampleInfos.removeFirst();
            this.pendingMetadataSampleBytes -= metadataSampleInfo.size;
            final long n2 = metadataSampleInfo.presentationTimeDeltaUs + n;
            final TimestampAdjuster timestampAdjuster = this.timestampAdjuster;
            long adjustSampleTimestamp = n2;
            if (timestampAdjuster != null) {
                adjustSampleTimestamp = timestampAdjuster.adjustSampleTimestamp(n2);
            }
            final TrackOutput[] emsgTrackOutputs = this.emsgTrackOutputs;
            for (int length = emsgTrackOutputs.length, i = 0; i < length; ++i) {
                emsgTrackOutputs[i].sampleMetadata(adjustSampleTimestamp, 1, metadataSampleInfo.size, this.pendingMetadataSampleBytes, null);
            }
        }
    }
    
    private static long parseMehd(final ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(8);
        long n;
        if (Atom.parseFullAtomVersion(parsableByteArray.readInt()) == 0) {
            n = parsableByteArray.readUnsignedInt();
        }
        else {
            n = parsableByteArray.readUnsignedLongToLong();
        }
        return n;
    }
    
    private static void parseMoof(final Atom.ContainerAtom containerAtom, final SparseArray<TrackBundle> sparseArray, final int n, final byte[] array) throws ParserException {
        for (int size = containerAtom.containerChildren.size(), i = 0; i < size; ++i) {
            final Atom.ContainerAtom containerAtom2 = containerAtom.containerChildren.get(i);
            if (containerAtom2.type == Atom.TYPE_traf) {
                parseTraf(containerAtom2, sparseArray, n, array);
            }
        }
    }
    
    private static void parseSaio(final ParsableByteArray parsableByteArray, final TrackFragment trackFragment) throws ParserException {
        parsableByteArray.setPosition(8);
        final int int1 = parsableByteArray.readInt();
        if ((Atom.parseFullAtomFlags(int1) & 0x1) == 0x1) {
            parsableByteArray.skipBytes(8);
        }
        final int unsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        if (unsignedIntToInt == 1) {
            final int fullAtomVersion = Atom.parseFullAtomVersion(int1);
            final long auxiliaryDataPosition = trackFragment.auxiliaryDataPosition;
            long n;
            if (fullAtomVersion == 0) {
                n = parsableByteArray.readUnsignedInt();
            }
            else {
                n = parsableByteArray.readUnsignedLongToLong();
            }
            trackFragment.auxiliaryDataPosition = auxiliaryDataPosition + n;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unexpected saio entry count: ");
        sb.append(unsignedIntToInt);
        throw new ParserException(sb.toString());
    }
    
    private static void parseSaiz(final TrackEncryptionBox trackEncryptionBox, final ParsableByteArray parsableByteArray, final TrackFragment trackFragment) throws ParserException {
        final int perSampleIvSize = trackEncryptionBox.perSampleIvSize;
        parsableByteArray.setPosition(8);
        final int fullAtomFlags = Atom.parseFullAtomFlags(parsableByteArray.readInt());
        boolean val = true;
        if ((fullAtomFlags & 0x1) == 0x1) {
            parsableByteArray.skipBytes(8);
        }
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final int unsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        if (unsignedIntToInt == trackFragment.sampleCount) {
            int n3;
            if (unsignedByte == 0) {
                final boolean[] sampleHasSubsampleEncryptionTable = trackFragment.sampleHasSubsampleEncryptionTable;
                int n = 0;
                int n2 = 0;
                while (true) {
                    n3 = n2;
                    if (n >= unsignedIntToInt) {
                        break;
                    }
                    final int unsignedByte2 = parsableByteArray.readUnsignedByte();
                    n2 += unsignedByte2;
                    sampleHasSubsampleEncryptionTable[n] = (unsignedByte2 > perSampleIvSize);
                    ++n;
                }
            }
            else {
                if (unsignedByte <= perSampleIvSize) {
                    val = false;
                }
                n3 = unsignedByte * unsignedIntToInt + 0;
                Arrays.fill(trackFragment.sampleHasSubsampleEncryptionTable, 0, unsignedIntToInt, val);
            }
            trackFragment.initEncryptionData(n3);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Length mismatch: ");
        sb.append(unsignedIntToInt);
        sb.append(", ");
        sb.append(trackFragment.sampleCount);
        throw new ParserException(sb.toString());
    }
    
    private static void parseSenc(final ParsableByteArray parsableByteArray, int n, final TrackFragment trackFragment) throws ParserException {
        parsableByteArray.setPosition(n + 8);
        n = Atom.parseFullAtomFlags(parsableByteArray.readInt());
        if ((n & 0x1) != 0x0) {
            throw new ParserException("Overriding TrackEncryptionBox parameters is unsupported.");
        }
        final boolean val = (n & 0x2) != 0x0;
        n = parsableByteArray.readUnsignedIntToInt();
        if (n == trackFragment.sampleCount) {
            Arrays.fill(trackFragment.sampleHasSubsampleEncryptionTable, 0, n, val);
            trackFragment.initEncryptionData(parsableByteArray.bytesLeft());
            trackFragment.fillEncryptionData(parsableByteArray);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Length mismatch: ");
        sb.append(n);
        sb.append(", ");
        sb.append(trackFragment.sampleCount);
        throw new ParserException(sb.toString());
    }
    
    private static void parseSenc(final ParsableByteArray parsableByteArray, final TrackFragment trackFragment) throws ParserException {
        parseSenc(parsableByteArray, 0, trackFragment);
    }
    
    private static void parseSgpd(final ParsableByteArray parsableByteArray, final ParsableByteArray parsableByteArray2, final String s, final TrackFragment trackFragment) throws ParserException {
        parsableByteArray.setPosition(8);
        final int int1 = parsableByteArray.readInt();
        if (parsableByteArray.readInt() != FragmentedMp4Extractor.SAMPLE_GROUP_TYPE_seig) {
            return;
        }
        if (Atom.parseFullAtomVersion(int1) == 1) {
            parsableByteArray.skipBytes(4);
        }
        if (parsableByteArray.readInt() != 1) {
            throw new ParserException("Entry count in sbgp != 1 (unsupported).");
        }
        parsableByteArray2.setPosition(8);
        final int int2 = parsableByteArray2.readInt();
        if (parsableByteArray2.readInt() != FragmentedMp4Extractor.SAMPLE_GROUP_TYPE_seig) {
            return;
        }
        final int fullAtomVersion = Atom.parseFullAtomVersion(int2);
        if (fullAtomVersion == 1) {
            if (parsableByteArray2.readUnsignedInt() == 0L) {
                throw new ParserException("Variable length description in sgpd found (unsupported)");
            }
        }
        else if (fullAtomVersion >= 2) {
            parsableByteArray2.skipBytes(4);
        }
        if (parsableByteArray2.readUnsignedInt() != 1L) {
            throw new ParserException("Entry count in sgpd != 1 (unsupported).");
        }
        parsableByteArray2.skipBytes(1);
        final int unsignedByte = parsableByteArray2.readUnsignedByte();
        final boolean b = parsableByteArray2.readUnsignedByte() == 1;
        if (!b) {
            return;
        }
        final int unsignedByte2 = parsableByteArray2.readUnsignedByte();
        final byte[] array = new byte[16];
        parsableByteArray2.readBytes(array, 0, array.length);
        byte[] array2;
        if (b && unsignedByte2 == 0) {
            final int unsignedByte3 = parsableByteArray2.readUnsignedByte();
            array2 = new byte[unsignedByte3];
            parsableByteArray2.readBytes(array2, 0, unsignedByte3);
        }
        else {
            array2 = null;
        }
        trackFragment.definesEncryptionData = true;
        trackFragment.trackEncryptionBox = new TrackEncryptionBox(b, s, unsignedByte2, array, (unsignedByte & 0xF0) >> 4, unsignedByte & 0xF, array2);
    }
    
    private static Pair<Long, ChunkIndex> parseSidx(final ParsableByteArray parsableByteArray, long n) throws ParserException {
        parsableByteArray.setPosition(8);
        final int fullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        parsableByteArray.skipBytes(4);
        final long unsignedInt = parsableByteArray.readUnsignedInt();
        long n2;
        long n3;
        if (fullAtomVersion == 0) {
            n2 = parsableByteArray.readUnsignedInt();
            n3 = parsableByteArray.readUnsignedInt();
        }
        else {
            n2 = parsableByteArray.readUnsignedLongToLong();
            n3 = parsableByteArray.readUnsignedLongToLong();
        }
        n += n3;
        final long scaleLargeTimestamp = Util.scaleLargeTimestamp(n2, 1000000L, unsignedInt);
        parsableByteArray.skipBytes(2);
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        final int[] array = new int[unsignedShort];
        final long[] array2 = new long[unsignedShort];
        final long[] array3 = new long[unsignedShort];
        final long[] array4 = new long[unsignedShort];
        long n4 = n2;
        long scaleLargeTimestamp2 = scaleLargeTimestamp;
        for (int i = 0; i < unsignedShort; ++i) {
            final int int1 = parsableByteArray.readInt();
            if ((int1 & Integer.MIN_VALUE) != 0x0) {
                throw new ParserException("Unhandled indirect reference");
            }
            final long unsignedInt2 = parsableByteArray.readUnsignedInt();
            array[i] = (int1 & Integer.MAX_VALUE);
            array2[i] = n;
            array4[i] = scaleLargeTimestamp2;
            n4 += unsignedInt2;
            scaleLargeTimestamp2 = Util.scaleLargeTimestamp(n4, 1000000L, unsignedInt);
            array3[i] = scaleLargeTimestamp2 - array4[i];
            parsableByteArray.skipBytes(4);
            n += array[i];
        }
        return (Pair<Long, ChunkIndex>)Pair.create((Object)scaleLargeTimestamp, (Object)new ChunkIndex(array, array2, array3, array4));
    }
    
    private static long parseTfdt(final ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(8);
        long n;
        if (Atom.parseFullAtomVersion(parsableByteArray.readInt()) == 1) {
            n = parsableByteArray.readUnsignedLongToLong();
        }
        else {
            n = parsableByteArray.readUnsignedInt();
        }
        return n;
    }
    
    private static TrackBundle parseTfhd(final ParsableByteArray parsableByteArray, final SparseArray<TrackBundle> sparseArray) {
        parsableByteArray.setPosition(8);
        final int fullAtomFlags = Atom.parseFullAtomFlags(parsableByteArray.readInt());
        final TrackBundle trackBundle = getTrackBundle(sparseArray, parsableByteArray.readInt());
        if (trackBundle == null) {
            return null;
        }
        if ((fullAtomFlags & 0x1) != 0x0) {
            final long unsignedLongToLong = parsableByteArray.readUnsignedLongToLong();
            final TrackFragment fragment = trackBundle.fragment;
            fragment.dataPosition = unsignedLongToLong;
            fragment.auxiliaryDataPosition = unsignedLongToLong;
        }
        final DefaultSampleValues defaultSampleValues = trackBundle.defaultSampleValues;
        int sampleDescriptionIndex;
        if ((fullAtomFlags & 0x2) != 0x0) {
            sampleDescriptionIndex = parsableByteArray.readUnsignedIntToInt() - 1;
        }
        else {
            sampleDescriptionIndex = defaultSampleValues.sampleDescriptionIndex;
        }
        int n;
        if ((fullAtomFlags & 0x8) != 0x0) {
            n = parsableByteArray.readUnsignedIntToInt();
        }
        else {
            n = defaultSampleValues.duration;
        }
        int n2;
        if ((fullAtomFlags & 0x10) != 0x0) {
            n2 = parsableByteArray.readUnsignedIntToInt();
        }
        else {
            n2 = defaultSampleValues.size;
        }
        int n3;
        if ((fullAtomFlags & 0x20) != 0x0) {
            n3 = parsableByteArray.readUnsignedIntToInt();
        }
        else {
            n3 = defaultSampleValues.flags;
        }
        trackBundle.fragment.header = new DefaultSampleValues(sampleDescriptionIndex, n, n2, n3);
        return trackBundle;
    }
    
    private static void parseTraf(final Atom.ContainerAtom containerAtom, final SparseArray<TrackBundle> sparseArray, int i, final byte[] array) throws ParserException {
        final TrackBundle tfhd = parseTfhd(containerAtom.getLeafAtomOfType(Atom.TYPE_tfhd).data, sparseArray);
        if (tfhd == null) {
            return;
        }
        final TrackFragment fragment = tfhd.fragment;
        final long nextFragmentDecodeTime = fragment.nextFragmentDecodeTime;
        tfhd.reset();
        long tfdt = nextFragmentDecodeTime;
        if (containerAtom.getLeafAtomOfType(Atom.TYPE_tfdt) != null) {
            tfdt = nextFragmentDecodeTime;
            if ((i & 0x2) == 0x0) {
                tfdt = parseTfdt(containerAtom.getLeafAtomOfType(Atom.TYPE_tfdt).data);
            }
        }
        parseTruns(containerAtom, tfhd, tfdt, i);
        final TrackEncryptionBox sampleDescriptionEncryptionBox = tfhd.track.getSampleDescriptionEncryptionBox(fragment.header.sampleDescriptionIndex);
        final Atom.LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_saiz);
        if (leafAtomOfType != null) {
            parseSaiz(sampleDescriptionEncryptionBox, leafAtomOfType.data, fragment);
        }
        final Atom.LeafAtom leafAtomOfType2 = containerAtom.getLeafAtomOfType(Atom.TYPE_saio);
        if (leafAtomOfType2 != null) {
            parseSaio(leafAtomOfType2.data, fragment);
        }
        final Atom.LeafAtom leafAtomOfType3 = containerAtom.getLeafAtomOfType(Atom.TYPE_senc);
        if (leafAtomOfType3 != null) {
            parseSenc(leafAtomOfType3.data, fragment);
        }
        final Atom.LeafAtom leafAtomOfType4 = containerAtom.getLeafAtomOfType(Atom.TYPE_sbgp);
        final Atom.LeafAtom leafAtomOfType5 = containerAtom.getLeafAtomOfType(Atom.TYPE_sgpd);
        if (leafAtomOfType4 != null && leafAtomOfType5 != null) {
            final ParsableByteArray data = leafAtomOfType4.data;
            final ParsableByteArray data2 = leafAtomOfType5.data;
            String schemeType;
            if (sampleDescriptionEncryptionBox != null) {
                schemeType = sampleDescriptionEncryptionBox.schemeType;
            }
            else {
                schemeType = null;
            }
            parseSgpd(data, data2, schemeType, fragment);
        }
        int size;
        Atom.LeafAtom leafAtom;
        for (size = containerAtom.leafChildren.size(), i = 0; i < size; ++i) {
            leafAtom = containerAtom.leafChildren.get(i);
            if (leafAtom.type == Atom.TYPE_uuid) {
                parseUuid(leafAtom.data, fragment, array);
            }
        }
    }
    
    private static Pair<Integer, DefaultSampleValues> parseTrex(final ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(12);
        return (Pair<Integer, DefaultSampleValues>)Pair.create((Object)parsableByteArray.readInt(), (Object)new DefaultSampleValues(parsableByteArray.readUnsignedIntToInt() - 1, parsableByteArray.readUnsignedIntToInt(), parsableByteArray.readUnsignedIntToInt(), parsableByteArray.readInt()));
    }
    
    private static int parseTrun(final TrackBundle trackBundle, int n, long nextFragmentDecodeTime, int n2, final ParsableByteArray parsableByteArray, int n3) {
        parsableByteArray.setPosition(8);
        final int fullAtomFlags = Atom.parseFullAtomFlags(parsableByteArray.readInt());
        final Track track = trackBundle.track;
        final TrackFragment fragment = trackBundle.fragment;
        final DefaultSampleValues header = fragment.header;
        fragment.trunLength[n] = parsableByteArray.readUnsignedIntToInt();
        final long[] trunDataPosition = fragment.trunDataPosition;
        trunDataPosition[n] = fragment.dataPosition;
        if ((fullAtomFlags & 0x1) != 0x0) {
            trunDataPosition[n] += parsableByteArray.readInt();
        }
        final boolean b = (fullAtomFlags & 0x4) != 0x0;
        int n4 = header.flags;
        if (b) {
            n4 = parsableByteArray.readUnsignedIntToInt();
        }
        final boolean b2 = (fullAtomFlags & 0x100) != 0x0;
        final boolean b3 = (fullAtomFlags & 0x200) != 0x0;
        final boolean b4 = (fullAtomFlags & 0x400) != 0x0;
        final boolean b5 = (fullAtomFlags & 0x800) != 0x0;
        final long[] editListDurations = track.editListDurations;
        long scaleLargeTimestamp;
        final long n5 = scaleLargeTimestamp = 0L;
        if (editListDurations != null) {
            scaleLargeTimestamp = n5;
            if (editListDurations.length == 1) {
                scaleLargeTimestamp = n5;
                if (editListDurations[0] == 0L) {
                    scaleLargeTimestamp = Util.scaleLargeTimestamp(track.editListMediaTimes[0], 1000L, track.timescale);
                }
            }
        }
        final int[] sampleSizeTable = fragment.sampleSizeTable;
        final int[] sampleCompositionTimeOffsetTable = fragment.sampleCompositionTimeOffsetTable;
        final long[] sampleDecodingTimeTable = fragment.sampleDecodingTimeTable;
        final boolean[] sampleIsSyncFrameTable = fragment.sampleIsSyncFrameTable;
        if (track.type == 2 && (n2 & 0x1) != 0x0) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        final int n6 = n3 + fragment.trunLength[n];
        final long timescale = track.timescale;
        if (n > 0) {
            nextFragmentDecodeTime = fragment.nextFragmentDecodeTime;
        }
        int i;
        int n7;
        int n8;
        long n9;
        for (i = n3, n3 = n6; i < n3; ++i, nextFragmentDecodeTime += n9) {
            if (b2) {
                n7 = parsableByteArray.readUnsignedIntToInt();
            }
            else {
                n7 = header.duration;
            }
            if (b3) {
                n8 = parsableByteArray.readUnsignedIntToInt();
            }
            else {
                n8 = header.size;
            }
            if (i == 0 && b) {
                n = n4;
            }
            else if (b4) {
                n = parsableByteArray.readInt();
            }
            else {
                n = header.flags;
            }
            if (b5) {
                sampleCompositionTimeOffsetTable[i] = (int)(parsableByteArray.readInt() * 1000L / timescale);
            }
            else {
                sampleCompositionTimeOffsetTable[i] = 0;
            }
            sampleDecodingTimeTable[i] = Util.scaleLargeTimestamp(nextFragmentDecodeTime, 1000L, timescale) - scaleLargeTimestamp;
            sampleSizeTable[i] = n8;
            sampleIsSyncFrameTable[i] = ((n >> 16 & 0x1) == 0x0 && (n2 == 0 || i == 0));
            n9 = n7;
        }
        fragment.nextFragmentDecodeTime = nextFragmentDecodeTime;
        return n3;
    }
    
    private static void parseTruns(final Atom.ContainerAtom containerAtom, final TrackBundle trackBundle, final long n, final int n2) {
        final List<Atom.LeafAtom> leafChildren = containerAtom.leafChildren;
        final int size = leafChildren.size();
        final int n3 = 0;
        int i = 0;
        int n4 = 0;
        int n5 = 0;
        while (i < size) {
            final Atom.LeafAtom leafAtom = leafChildren.get(i);
            int n6 = n4;
            int n7 = n5;
            if (leafAtom.type == Atom.TYPE_trun) {
                final ParsableByteArray data = leafAtom.data;
                data.setPosition(12);
                final int unsignedIntToInt = data.readUnsignedIntToInt();
                n6 = n4;
                n7 = n5;
                if (unsignedIntToInt > 0) {
                    n7 = n5 + unsignedIntToInt;
                    n6 = n4 + 1;
                }
            }
            ++i;
            n4 = n6;
            n5 = n7;
        }
        trackBundle.currentTrackRunIndex = 0;
        trackBundle.currentSampleInTrackRun = 0;
        trackBundle.currentSampleIndex = 0;
        trackBundle.fragment.initTables(n4, n5);
        int n8 = 0;
        int n9 = 0;
        int n10;
        int trun;
        for (int j = n3; j < size; ++j, n8 = n10, n9 = trun) {
            final Atom.LeafAtom leafAtom2 = leafChildren.get(j);
            n10 = n8;
            trun = n9;
            if (leafAtom2.type == Atom.TYPE_trun) {
                trun = parseTrun(trackBundle, n8, n, n2, leafAtom2.data, n9);
                n10 = n8 + 1;
            }
        }
    }
    
    private static void parseUuid(final ParsableByteArray parsableByteArray, final TrackFragment trackFragment, final byte[] a) throws ParserException {
        parsableByteArray.setPosition(8);
        parsableByteArray.readBytes(a, 0, 16);
        if (!Arrays.equals(a, FragmentedMp4Extractor.PIFF_SAMPLE_ENCRYPTION_BOX_EXTENDED_TYPE)) {
            return;
        }
        parseSenc(parsableByteArray, 16, trackFragment);
    }
    
    private void processAtomEnded(final long n) throws ParserException {
        while (!this.containerAtoms.isEmpty() && this.containerAtoms.peek().endPosition == n) {
            this.onContainerAtomRead(this.containerAtoms.pop());
        }
        this.enterReadingAtomHeaderState();
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
        if (this.atomSize < this.atomHeaderBytesRead) {
            throw new ParserException("Atom size less than header length (unsupported).");
        }
        final long dataPosition = extractorInput.getPosition() - this.atomHeaderBytesRead;
        if (this.atomType == Atom.TYPE_moof) {
            for (int size = this.trackBundles.size(), i = 0; i < size; ++i) {
                final TrackFragment fragment = ((TrackBundle)this.trackBundles.valueAt(i)).fragment;
                fragment.atomPosition = dataPosition;
                fragment.auxiliaryDataPosition = dataPosition;
                fragment.dataPosition = dataPosition;
            }
        }
        final int atomType = this.atomType;
        if (atomType == Atom.TYPE_mdat) {
            this.currentTrackBundle = null;
            this.endOfMdatPosition = this.atomSize + dataPosition;
            if (!this.haveOutputSeekMap) {
                this.extractorOutput.seekMap(new SeekMap.Unseekable(this.durationUs, dataPosition));
                this.haveOutputSeekMap = true;
            }
            this.parserState = 2;
            return true;
        }
        if (shouldParseContainerAtom(atomType)) {
            final long n3 = extractorInput.getPosition() + this.atomSize - 8L;
            this.containerAtoms.push(new Atom.ContainerAtom(this.atomType, n3));
            if (this.atomSize == this.atomHeaderBytesRead) {
                this.processAtomEnded(n3);
            }
            else {
                this.enterReadingAtomHeaderState();
            }
        }
        else if (shouldParseLeafAtom(this.atomType)) {
            if (this.atomHeaderBytesRead != 8) {
                throw new ParserException("Leaf atom defines extended atom size (unsupported).");
            }
            final long atomSize2 = this.atomSize;
            if (atomSize2 > 2147483647L) {
                throw new ParserException("Leaf atom with length > 2147483647 (unsupported).");
            }
            this.atomData = new ParsableByteArray((int)atomSize2);
            System.arraycopy(this.atomHeader.data, 0, this.atomData.data, 0, 8);
            this.parserState = 1;
        }
        else {
            if (this.atomSize > 2147483647L) {
                throw new ParserException("Skipping atom with length > 2147483647 (unsupported).");
            }
            this.atomData = null;
            this.parserState = 1;
        }
        return true;
    }
    
    private void readAtomPayload(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final int n = (int)this.atomSize - this.atomHeaderBytesRead;
        final ParsableByteArray atomData = this.atomData;
        if (atomData != null) {
            extractorInput.readFully(atomData.data, 8, n);
            this.onLeafAtomRead(new Atom.LeafAtom(this.atomType, this.atomData), extractorInput.getPosition());
        }
        else {
            extractorInput.skipFully(n);
        }
        this.processAtomEnded(extractorInput.getPosition());
    }
    
    private void readEncryptionData(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final int size = this.trackBundles.size();
        TrackBundle trackBundle = null;
        long n = Long.MAX_VALUE;
        TrackBundle trackBundle2;
        long n2;
        for (int i = 0; i < size; ++i, trackBundle = trackBundle2, n = n2) {
            final TrackFragment fragment = ((TrackBundle)this.trackBundles.valueAt(i)).fragment;
            trackBundle2 = trackBundle;
            n2 = n;
            if (fragment.sampleEncryptionDataNeedsFill) {
                final long auxiliaryDataPosition = fragment.auxiliaryDataPosition;
                trackBundle2 = trackBundle;
                n2 = n;
                if (auxiliaryDataPosition < n) {
                    trackBundle2 = (TrackBundle)this.trackBundles.valueAt(i);
                    n2 = auxiliaryDataPosition;
                }
            }
        }
        if (trackBundle == null) {
            this.parserState = 3;
            return;
        }
        final int n3 = (int)(n - extractorInput.getPosition());
        if (n3 >= 0) {
            extractorInput.skipFully(n3);
            trackBundle.fragment.fillEncryptionData(extractorInput);
            return;
        }
        throw new ParserException("Offset to encryption data was negative.");
    }
    
    private boolean readSample(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.parserState == 3) {
            if (this.currentTrackBundle == null) {
                final TrackBundle nextFragmentRun = getNextFragmentRun(this.trackBundles);
                if (nextFragmentRun == null) {
                    final int n = (int)(this.endOfMdatPosition - extractorInput.getPosition());
                    if (n >= 0) {
                        extractorInput.skipFully(n);
                        this.enterReadingAtomHeaderState();
                        return false;
                    }
                    throw new ParserException("Offset to end of mdat was negative.");
                }
                else {
                    int n2;
                    if ((n2 = (int)(nextFragmentRun.fragment.trunDataPosition[nextFragmentRun.currentTrackRunIndex] - extractorInput.getPosition())) < 0) {
                        Log.w("FragmentedMp4Extractor", "Ignoring negative offset to sample data.");
                        n2 = 0;
                    }
                    extractorInput.skipFully(n2);
                    this.currentTrackBundle = nextFragmentRun;
                }
            }
            final TrackBundle currentTrackBundle = this.currentTrackBundle;
            final int[] sampleSizeTable = currentTrackBundle.fragment.sampleSizeTable;
            final int currentSampleIndex = currentTrackBundle.currentSampleIndex;
            this.sampleSize = sampleSizeTable[currentSampleIndex];
            if (currentSampleIndex < currentTrackBundle.firstSampleToOutputIndex) {
                extractorInput.skipFully(this.sampleSize);
                this.currentTrackBundle.skipSampleEncryptionData();
                if (!this.currentTrackBundle.next()) {
                    this.currentTrackBundle = null;
                }
                this.parserState = 3;
                return true;
            }
            if (currentTrackBundle.track.sampleTransformation == 1) {
                this.sampleSize -= 8;
                extractorInput.skipFully(8);
            }
            this.sampleBytesWritten = this.currentTrackBundle.outputSampleEncryptionData();
            this.sampleSize += this.sampleBytesWritten;
            this.parserState = 4;
            this.sampleCurrentNalBytesRemaining = 0;
        }
        final TrackBundle currentTrackBundle2 = this.currentTrackBundle;
        final TrackFragment fragment = currentTrackBundle2.fragment;
        final Track track = currentTrackBundle2.track;
        final TrackOutput output = currentTrackBundle2.output;
        final int currentSampleIndex2 = currentTrackBundle2.currentSampleIndex;
        final long n3 = fragment.getSamplePresentationTime(currentSampleIndex2) * 1000L;
        final TimestampAdjuster timestampAdjuster = this.timestampAdjuster;
        long adjustSampleTimestamp = n3;
        if (timestampAdjuster != null) {
            adjustSampleTimestamp = timestampAdjuster.adjustSampleTimestamp(n3);
        }
        final int nalUnitLengthFieldLength = track.nalUnitLengthFieldLength;
        if (nalUnitLengthFieldLength != 0) {
            final byte[] data = this.nalPrefix.data;
            data[0] = 0;
            data[1] = 0;
            data[2] = 0;
            final int n4 = 4 - nalUnitLengthFieldLength;
            while (this.sampleBytesWritten < this.sampleSize) {
                final int sampleCurrentNalBytesRemaining = this.sampleCurrentNalBytesRemaining;
                if (sampleCurrentNalBytesRemaining == 0) {
                    extractorInput.readFully(data, n4, nalUnitLengthFieldLength + 1);
                    this.nalPrefix.setPosition(0);
                    this.sampleCurrentNalBytesRemaining = this.nalPrefix.readUnsignedIntToInt() - 1;
                    this.nalStartCode.setPosition(0);
                    output.sampleData(this.nalStartCode, 4);
                    output.sampleData(this.nalPrefix, 1);
                    this.processSeiNalUnitPayload = (this.cea608TrackOutputs.length > 0 && NalUnitUtil.isNalUnitSei(track.format.sampleMimeType, data[4]));
                    this.sampleBytesWritten += 5;
                    this.sampleSize += n4;
                }
                else {
                    int n5;
                    if (this.processSeiNalUnitPayload) {
                        this.nalBuffer.reset(sampleCurrentNalBytesRemaining);
                        extractorInput.readFully(this.nalBuffer.data, 0, this.sampleCurrentNalBytesRemaining);
                        output.sampleData(this.nalBuffer, this.sampleCurrentNalBytesRemaining);
                        n5 = this.sampleCurrentNalBytesRemaining;
                        final ParsableByteArray nalBuffer = this.nalBuffer;
                        final int unescapeStream = NalUnitUtil.unescapeStream(nalBuffer.data, nalBuffer.limit());
                        this.nalBuffer.setPosition("video/hevc".equals(track.format.sampleMimeType) ? 1 : 0);
                        this.nalBuffer.setLimit(unescapeStream);
                        CeaUtil.consume(adjustSampleTimestamp, this.nalBuffer, this.cea608TrackOutputs);
                    }
                    else {
                        n5 = output.sampleData(extractorInput, sampleCurrentNalBytesRemaining, false);
                    }
                    this.sampleBytesWritten += n5;
                    this.sampleCurrentNalBytesRemaining -= n5;
                }
            }
        }
        else {
            while (true) {
                final int sampleBytesWritten = this.sampleBytesWritten;
                final int sampleSize = this.sampleSize;
                if (sampleBytesWritten >= sampleSize) {
                    break;
                }
                this.sampleBytesWritten += output.sampleData(extractorInput, sampleSize - sampleBytesWritten, false);
            }
        }
        int n6 = fragment.sampleIsSyncFrameTable[currentSampleIndex2] ? 1 : 0;
        final TrackEncryptionBox access$100 = this.currentTrackBundle.getEncryptionBoxIfEncrypted();
        Object cryptoData;
        if (access$100 != null) {
            cryptoData = access$100.cryptoData;
            n6 |= 0x40000000;
        }
        else {
            cryptoData = null;
        }
        output.sampleMetadata(adjustSampleTimestamp, n6, this.sampleSize, 0, (TrackOutput.CryptoData)cryptoData);
        this.outputPendingMetadataSamples(adjustSampleTimestamp);
        if (!this.currentTrackBundle.next()) {
            this.currentTrackBundle = null;
        }
        this.parserState = 3;
        return true;
    }
    
    private static boolean shouldParseContainerAtom(final int n) {
        return n == Atom.TYPE_moov || n == Atom.TYPE_trak || n == Atom.TYPE_mdia || n == Atom.TYPE_minf || n == Atom.TYPE_stbl || n == Atom.TYPE_moof || n == Atom.TYPE_traf || n == Atom.TYPE_mvex || n == Atom.TYPE_edts;
    }
    
    private static boolean shouldParseLeafAtom(final int n) {
        return n == Atom.TYPE_hdlr || n == Atom.TYPE_mdhd || n == Atom.TYPE_mvhd || n == Atom.TYPE_sidx || n == Atom.TYPE_stsd || n == Atom.TYPE_tfdt || n == Atom.TYPE_tfhd || n == Atom.TYPE_tkhd || n == Atom.TYPE_trex || n == Atom.TYPE_trun || n == Atom.TYPE_pssh || n == Atom.TYPE_saiz || n == Atom.TYPE_saio || n == Atom.TYPE_senc || n == Atom.TYPE_uuid || n == Atom.TYPE_sbgp || n == Atom.TYPE_sgpd || n == Atom.TYPE_elst || n == Atom.TYPE_mehd || n == Atom.TYPE_emsg;
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        final Track sideloadedTrack = this.sideloadedTrack;
        if (sideloadedTrack != null) {
            final TrackBundle trackBundle = new TrackBundle(extractorOutput.track(0, sideloadedTrack.type));
            trackBundle.init(this.sideloadedTrack, new DefaultSampleValues(0, 0, 0, 0));
            this.trackBundles.put(0, (Object)trackBundle);
            this.maybeInitExtraTracks();
            this.extractorOutput.endTracks();
        }
    }
    
    protected Track modifyTrack(final Track track) {
        return track;
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        while (true) {
            final int parserState = this.parserState;
            if (parserState != 0) {
                if (parserState != 1) {
                    if (parserState != 2) {
                        if (this.readSample(extractorInput)) {
                            return 0;
                        }
                        continue;
                    }
                    else {
                        this.readEncryptionData(extractorInput);
                    }
                }
                else {
                    this.readAtomPayload(extractorInput);
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
    public void seek(final long n, final long pendingSeekTimeUs) {
        for (int size = this.trackBundles.size(), i = 0; i < size; ++i) {
            ((TrackBundle)this.trackBundles.valueAt(i)).reset();
        }
        this.pendingMetadataSampleInfos.clear();
        this.pendingMetadataSampleBytes = 0;
        this.pendingSeekTimeUs = pendingSeekTimeUs;
        this.containerAtoms.clear();
        this.enterReadingAtomHeaderState();
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        return Sniffer.sniffFragmented(extractorInput);
    }
    
    private static final class MetadataSampleInfo
    {
        public final long presentationTimeDeltaUs;
        public final int size;
        
        public MetadataSampleInfo(final long presentationTimeDeltaUs, final int size) {
            this.presentationTimeDeltaUs = presentationTimeDeltaUs;
            this.size = size;
        }
    }
    
    private static final class TrackBundle
    {
        public int currentSampleInTrackRun;
        public int currentSampleIndex;
        public int currentTrackRunIndex;
        private final ParsableByteArray defaultInitializationVector;
        public DefaultSampleValues defaultSampleValues;
        private final ParsableByteArray encryptionSignalByte;
        public int firstSampleToOutputIndex;
        public final TrackFragment fragment;
        public final TrackOutput output;
        public Track track;
        
        public TrackBundle(final TrackOutput output) {
            this.output = output;
            this.fragment = new TrackFragment();
            this.encryptionSignalByte = new ParsableByteArray(1);
            this.defaultInitializationVector = new ParsableByteArray();
        }
        
        private TrackEncryptionBox getEncryptionBoxIfEncrypted() {
            final TrackFragment fragment = this.fragment;
            final int sampleDescriptionIndex = fragment.header.sampleDescriptionIndex;
            TrackEncryptionBox trackEncryptionBox = fragment.trackEncryptionBox;
            if (trackEncryptionBox == null) {
                trackEncryptionBox = this.track.getSampleDescriptionEncryptionBox(sampleDescriptionIndex);
            }
            if (trackEncryptionBox == null || !trackEncryptionBox.isEncrypted) {
                trackEncryptionBox = null;
            }
            return trackEncryptionBox;
        }
        
        private void skipSampleEncryptionData() {
            final TrackEncryptionBox encryptionBoxIfEncrypted = this.getEncryptionBoxIfEncrypted();
            if (encryptionBoxIfEncrypted == null) {
                return;
            }
            final ParsableByteArray sampleEncryptionData = this.fragment.sampleEncryptionData;
            final int perSampleIvSize = encryptionBoxIfEncrypted.perSampleIvSize;
            if (perSampleIvSize != 0) {
                sampleEncryptionData.skipBytes(perSampleIvSize);
            }
            if (this.fragment.sampleHasSubsampleEncryptionTable(this.currentSampleIndex)) {
                sampleEncryptionData.skipBytes(sampleEncryptionData.readUnsignedShort() * 6);
            }
        }
        
        public void init(final Track track, final DefaultSampleValues defaultSampleValues) {
            Assertions.checkNotNull(track);
            this.track = track;
            Assertions.checkNotNull(defaultSampleValues);
            this.defaultSampleValues = defaultSampleValues;
            this.output.format(track.format);
            this.reset();
        }
        
        public boolean next() {
            ++this.currentSampleIndex;
            ++this.currentSampleInTrackRun;
            final int currentSampleInTrackRun = this.currentSampleInTrackRun;
            final int[] trunLength = this.fragment.trunLength;
            final int currentTrackRunIndex = this.currentTrackRunIndex;
            if (currentSampleInTrackRun == trunLength[currentTrackRunIndex]) {
                this.currentTrackRunIndex = currentTrackRunIndex + 1;
                this.currentSampleInTrackRun = 0;
                return false;
            }
            return true;
        }
        
        public int outputSampleEncryptionData() {
            final TrackEncryptionBox encryptionBoxIfEncrypted = this.getEncryptionBoxIfEncrypted();
            if (encryptionBoxIfEncrypted == null) {
                return 0;
            }
            int n = encryptionBoxIfEncrypted.perSampleIvSize;
            ParsableByteArray parsableByteArray;
            if (n != 0) {
                parsableByteArray = this.fragment.sampleEncryptionData;
            }
            else {
                final byte[] defaultInitializationVector = encryptionBoxIfEncrypted.defaultInitializationVector;
                this.defaultInitializationVector.reset(defaultInitializationVector, defaultInitializationVector.length);
                parsableByteArray = this.defaultInitializationVector;
                n = defaultInitializationVector.length;
            }
            final boolean sampleHasSubsampleEncryptionTable = this.fragment.sampleHasSubsampleEncryptionTable(this.currentSampleIndex);
            final byte[] data = this.encryptionSignalByte.data;
            int n2;
            if (sampleHasSubsampleEncryptionTable) {
                n2 = 128;
            }
            else {
                n2 = 0;
            }
            data[0] = (byte)(n2 | n);
            this.encryptionSignalByte.setPosition(0);
            this.output.sampleData(this.encryptionSignalByte, 1);
            this.output.sampleData(parsableByteArray, n);
            if (!sampleHasSubsampleEncryptionTable) {
                return n + 1;
            }
            final ParsableByteArray sampleEncryptionData = this.fragment.sampleEncryptionData;
            final int unsignedShort = sampleEncryptionData.readUnsignedShort();
            sampleEncryptionData.skipBytes(-2);
            final int n3 = unsignedShort * 6 + 2;
            this.output.sampleData(sampleEncryptionData, n3);
            return n + 1 + n3;
        }
        
        public void reset() {
            this.fragment.reset();
            this.currentSampleIndex = 0;
            this.currentTrackRunIndex = 0;
            this.currentSampleInTrackRun = 0;
            this.firstSampleToOutputIndex = 0;
        }
        
        public void seek(long usToMs) {
            usToMs = C.usToMs(usToMs);
            int currentSampleIndex = this.currentSampleIndex;
            while (true) {
                final TrackFragment fragment = this.fragment;
                if (currentSampleIndex >= fragment.sampleCount || fragment.getSamplePresentationTime(currentSampleIndex) >= usToMs) {
                    break;
                }
                if (this.fragment.sampleIsSyncFrameTable[currentSampleIndex]) {
                    this.firstSampleToOutputIndex = currentSampleIndex;
                }
                ++currentSampleIndex;
            }
        }
        
        public void updateDrmInitData(final DrmInitData drmInitData) {
            final TrackEncryptionBox sampleDescriptionEncryptionBox = this.track.getSampleDescriptionEncryptionBox(this.fragment.header.sampleDescriptionIndex);
            String schemeType;
            if (sampleDescriptionEncryptionBox != null) {
                schemeType = sampleDescriptionEncryptionBox.schemeType;
            }
            else {
                schemeType = null;
            }
            this.output.format(this.track.format.copyWithDrmInitData(drmInitData.copyWithSchemeType(schemeType)));
        }
    }
}
