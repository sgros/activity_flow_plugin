// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import java.util.Arrays;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.ParserException;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import java.util.Collections;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import android.util.SparseArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import android.util.SparseBooleanArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.util.List;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import android.util.SparseIntArray;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public final class TsExtractor implements Extractor
{
    private static final long AC3_FORMAT_IDENTIFIER;
    private static final long E_AC3_FORMAT_IDENTIFIER;
    public static final ExtractorsFactory FACTORY;
    private static final long HEVC_FORMAT_IDENTIFIER;
    private int bytesSinceLastSync;
    private final SparseIntArray continuityCounters;
    private final TsDurationReader durationReader;
    private boolean hasOutputSeekMap;
    private TsPayloadReader id3Reader;
    private final int mode;
    private ExtractorOutput output;
    private final TsPayloadReader.Factory payloadReaderFactory;
    private int pcrPid;
    private boolean pendingSeekToStart;
    private int remainingPmts;
    private final List<TimestampAdjuster> timestampAdjusters;
    private final SparseBooleanArray trackIds;
    private final SparseBooleanArray trackPids;
    private boolean tracksEnded;
    private TsBinarySearchSeeker tsBinarySearchSeeker;
    private final ParsableByteArray tsPacketBuffer;
    private final SparseArray<TsPayloadReader> tsPayloadReaders;
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$TsExtractor$f_UE6PC86cqq4V_qVoFQnPhfFZ8.INSTANCE;
        AC3_FORMAT_IDENTIFIER = Util.getIntegerCodeForString("AC-3");
        E_AC3_FORMAT_IDENTIFIER = Util.getIntegerCodeForString("EAC3");
        HEVC_FORMAT_IDENTIFIER = Util.getIntegerCodeForString("HEVC");
    }
    
    public TsExtractor() {
        this(0);
    }
    
    public TsExtractor(final int n) {
        this(1, n);
    }
    
    public TsExtractor(final int n, final int n2) {
        this(n, new TimestampAdjuster(0L), new DefaultTsPayloadReaderFactory(n2));
    }
    
    public TsExtractor(final int mode, final TimestampAdjuster o, final TsPayloadReader.Factory factory) {
        Assertions.checkNotNull(factory);
        this.payloadReaderFactory = factory;
        this.mode = mode;
        if (mode != 1 && mode != 2) {
            (this.timestampAdjusters = new ArrayList<TimestampAdjuster>()).add(o);
        }
        else {
            this.timestampAdjusters = Collections.singletonList(o);
        }
        this.tsPacketBuffer = new ParsableByteArray(new byte[9400], 0);
        this.trackIds = new SparseBooleanArray();
        this.trackPids = new SparseBooleanArray();
        this.tsPayloadReaders = (SparseArray<TsPayloadReader>)new SparseArray();
        this.continuityCounters = new SparseIntArray();
        this.durationReader = new TsDurationReader();
        this.pcrPid = -1;
        this.resetPayloadReaders();
    }
    
    private boolean fillBufferWithAtLeastOnePacket(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final ParsableByteArray tsPacketBuffer = this.tsPacketBuffer;
        final byte[] data = tsPacketBuffer.data;
        if (9400 - tsPacketBuffer.getPosition() < 188) {
            final int bytesLeft = this.tsPacketBuffer.bytesLeft();
            if (bytesLeft > 0) {
                System.arraycopy(data, this.tsPacketBuffer.getPosition(), data, 0, bytesLeft);
            }
            this.tsPacketBuffer.reset(data, bytesLeft);
        }
        while (this.tsPacketBuffer.bytesLeft() < 188) {
            final int limit = this.tsPacketBuffer.limit();
            final int read = extractorInput.read(data, limit, 9400 - limit);
            if (read == -1) {
                return false;
            }
            this.tsPacketBuffer.setLimit(limit + read);
        }
        return true;
    }
    
    private int findEndOfFirstTsPacketInBuffer() throws ParserException {
        final int position = this.tsPacketBuffer.getPosition();
        final int limit = this.tsPacketBuffer.limit();
        final int syncBytePosition = TsUtil.findSyncBytePosition(this.tsPacketBuffer.data, position, limit);
        this.tsPacketBuffer.setPosition(syncBytePosition);
        final int n = syncBytePosition + 188;
        if (n > limit) {
            this.bytesSinceLastSync += syncBytePosition - position;
            if (this.mode == 2) {
                if (this.bytesSinceLastSync > 376) {
                    throw new ParserException("Cannot find sync byte. Most likely not a Transport Stream.");
                }
            }
        }
        else {
            this.bytesSinceLastSync = 0;
        }
        return n;
    }
    
    private void maybeOutputSeekMap(final long n) {
        if (!this.hasOutputSeekMap) {
            this.hasOutputSeekMap = true;
            if (this.durationReader.getDurationUs() != -9223372036854775807L) {
                this.tsBinarySearchSeeker = new TsBinarySearchSeeker(this.durationReader.getPcrTimestampAdjuster(), this.durationReader.getDurationUs(), n, this.pcrPid);
                this.output.seekMap(this.tsBinarySearchSeeker.getSeekMap());
            }
            else {
                this.output.seekMap(new SeekMap.Unseekable(this.durationReader.getDurationUs()));
            }
        }
    }
    
    private void resetPayloadReaders() {
        this.trackIds.clear();
        this.tsPayloadReaders.clear();
        final SparseArray<TsPayloadReader> initialPayloadReaders = this.payloadReaderFactory.createInitialPayloadReaders();
        for (int size = initialPayloadReaders.size(), i = 0; i < size; ++i) {
            this.tsPayloadReaders.put(initialPayloadReaders.keyAt(i), initialPayloadReaders.valueAt(i));
        }
        this.tsPayloadReaders.put(0, (Object)new SectionReader(new PatReader()));
        this.id3Reader = null;
    }
    
    private boolean shouldConsumePacketPayload(final int n) {
        final int mode = this.mode;
        boolean b = false;
        if (mode == 2 || this.tracksEnded || !this.trackPids.get(n, false)) {
            b = true;
        }
        return b;
    }
    
    @Override
    public void init(final ExtractorOutput output) {
        this.output = output;
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final long length = extractorInput.getLength();
        final boolean tracksEnded = this.tracksEnded;
        final TsPayloadReader tsPayloadReader = null;
        if (tracksEnded) {
            if (length != -1L && this.mode != 2 && !this.durationReader.isDurationReadFinished()) {
                return this.durationReader.readDuration(extractorInput, positionHolder, this.pcrPid);
            }
            this.maybeOutputSeekMap(length);
            if (this.pendingSeekToStart) {
                this.pendingSeekToStart = false;
                this.seek(0L, 0L);
                if (extractorInput.getPosition() != 0L) {
                    positionHolder.position = 0L;
                    return 1;
                }
            }
            final TsBinarySearchSeeker tsBinarySearchSeeker = this.tsBinarySearchSeeker;
            if (tsBinarySearchSeeker != null && tsBinarySearchSeeker.isSeeking()) {
                return this.tsBinarySearchSeeker.handlePendingSeek(extractorInput, positionHolder, null);
            }
        }
        if (!this.fillBufferWithAtLeastOnePacket(extractorInput)) {
            return -1;
        }
        final int endOfFirstTsPacketInBuffer = this.findEndOfFirstTsPacketInBuffer();
        final int limit = this.tsPacketBuffer.limit();
        if (endOfFirstTsPacketInBuffer > limit) {
            return 0;
        }
        final int int1 = this.tsPacketBuffer.readInt();
        if ((0x800000 & int1) != 0x0) {
            this.tsPacketBuffer.setPosition(endOfFirstTsPacketInBuffer);
            return 0;
        }
        final int n = ((0x400000 & int1) != 0x0 | false) ? 1 : 0;
        final int n2 = (0x1FFF00 & int1) >> 8;
        final boolean b = (int1 & 0x20) != 0x0;
        final boolean b2 = (int1 & 0x10) != 0x0;
        TsPayloadReader tsPayloadReader2 = tsPayloadReader;
        if (b2) {
            tsPayloadReader2 = (TsPayloadReader)this.tsPayloadReaders.get(n2);
        }
        if (tsPayloadReader2 == null) {
            this.tsPacketBuffer.setPosition(endOfFirstTsPacketInBuffer);
            return 0;
        }
        if (this.mode != 2) {
            final int n3 = int1 & 0xF;
            final int value = this.continuityCounters.get(n2, n3 - 1);
            this.continuityCounters.put(n2, n3);
            if (value == n3) {
                this.tsPacketBuffer.setPosition(endOfFirstTsPacketInBuffer);
                return 0;
            }
            if (n3 != (value + 1 & 0xF)) {
                tsPayloadReader2.seek();
            }
        }
        int n4 = n;
        if (b) {
            final int unsignedByte = this.tsPacketBuffer.readUnsignedByte();
            int n5;
            if ((this.tsPacketBuffer.readUnsignedByte() & 0x40) != 0x0) {
                n5 = 2;
            }
            else {
                n5 = 0;
            }
            n4 = (n | n5);
            this.tsPacketBuffer.skipBytes(unsignedByte - 1);
        }
        final boolean tracksEnded2 = this.tracksEnded;
        if (this.shouldConsumePacketPayload(n2)) {
            this.tsPacketBuffer.setLimit(endOfFirstTsPacketInBuffer);
            tsPayloadReader2.consume(this.tsPacketBuffer, n4);
            this.tsPacketBuffer.setLimit(limit);
        }
        if (this.mode != 2 && !tracksEnded2 && this.tracksEnded && length != -1L) {
            this.pendingSeekToStart = true;
        }
        this.tsPacketBuffer.setPosition(endOfFirstTsPacketInBuffer);
        return 0;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        Assertions.checkState(this.mode != 2);
        for (int size = this.timestampAdjusters.size(), i = 0; i < size; ++i) {
            final TimestampAdjuster timestampAdjuster = this.timestampAdjusters.get(i);
            if (timestampAdjuster.getTimestampOffsetUs() == -9223372036854775807L || (timestampAdjuster.getTimestampOffsetUs() != 0L && timestampAdjuster.getFirstSampleTimestampUs() != n2)) {
                timestampAdjuster.reset();
                timestampAdjuster.setFirstSampleTimestampUs(n2);
            }
        }
        if (n2 != 0L) {
            final TsBinarySearchSeeker tsBinarySearchSeeker = this.tsBinarySearchSeeker;
            if (tsBinarySearchSeeker != null) {
                tsBinarySearchSeeker.setSeekTargetUs(n2);
            }
        }
        this.tsPacketBuffer.reset();
        this.continuityCounters.clear();
        for (int j = 0; j < this.tsPayloadReaders.size(); ++j) {
            ((TsPayloadReader)this.tsPayloadReaders.valueAt(j)).seek();
        }
        this.bytesSinceLastSync = 0;
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final byte[] data = this.tsPacketBuffer.data;
        extractorInput.peekFully(data, 0, 940);
        int i = 0;
    Label_0021:
        while (i < 188) {
            int j = 0;
            while (true) {
                while (j < 5) {
                    if (data[j * 188 + i] != 71) {
                        final boolean b = false;
                        if (b) {
                            extractorInput.skipFully(i);
                            return true;
                        }
                        ++i;
                        continue Label_0021;
                    }
                    else {
                        ++j;
                    }
                }
                final boolean b = true;
                continue;
            }
        }
        return false;
    }
    
    private class PatReader implements SectionPayloadReader
    {
        private final ParsableBitArray patScratch;
        
        public PatReader() {
            this.patScratch = new ParsableBitArray(new byte[4]);
        }
        
        @Override
        public void consume(final ParsableByteArray parsableByteArray) {
            if (parsableByteArray.readUnsignedByte() != 0) {
                return;
            }
            parsableByteArray.skipBytes(7);
            for (int n = parsableByteArray.bytesLeft() / 4, i = 0; i < n; ++i) {
                parsableByteArray.readBytes(this.patScratch, 4);
                final int bits = this.patScratch.readBits(16);
                this.patScratch.skipBits(3);
                if (bits == 0) {
                    this.patScratch.skipBits(13);
                }
                else {
                    final int bits2 = this.patScratch.readBits(13);
                    TsExtractor.this.tsPayloadReaders.put(bits2, (Object)new SectionReader(new PmtReader(bits2)));
                    TsExtractor.this.remainingPmts++;
                }
            }
            if (TsExtractor.this.mode != 2) {
                TsExtractor.this.tsPayloadReaders.remove(0);
            }
        }
        
        @Override
        public void init(final TimestampAdjuster timestampAdjuster, final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        }
    }
    
    private class PmtReader implements SectionPayloadReader
    {
        private final int pid;
        private final ParsableBitArray pmtScratch;
        private final SparseIntArray trackIdToPidScratch;
        private final SparseArray<TsPayloadReader> trackIdToReaderScratch;
        
        public PmtReader(final int pid) {
            this.pmtScratch = new ParsableBitArray(new byte[5]);
            this.trackIdToReaderScratch = (SparseArray<TsPayloadReader>)new SparseArray();
            this.trackIdToPidScratch = new SparseIntArray();
            this.pid = pid;
        }
        
        private TsPayloadReader.EsInfo readEsInfo(final ParsableByteArray parsableByteArray, int unsignedByte) {
            final int position = parsableByteArray.getPosition();
            final int n = unsignedByte + position;
            String s = null;
            unsignedByte = -1;
            List<TsPayloadReader.DvbSubtitleInfo> list = null;
            while (parsableByteArray.getPosition() < n) {
                final int unsignedByte2 = parsableByteArray.readUnsignedByte();
                final int n2 = parsableByteArray.getPosition() + parsableByteArray.readUnsignedByte();
                String trim = null;
                List<TsPayloadReader.DvbSubtitleInfo> list2 = null;
                Label_0296: {
                    Label_0144: {
                        if (unsignedByte2 == 5) {
                            final long unsignedInt = parsableByteArray.readUnsignedInt();
                            if (unsignedInt != TsExtractor.AC3_FORMAT_IDENTIFIER) {
                                if (unsignedInt == TsExtractor.E_AC3_FORMAT_IDENTIFIER) {
                                    break Label_0144;
                                }
                                trim = s;
                                list2 = list;
                                if (unsignedInt == TsExtractor.HEVC_FORMAT_IDENTIFIER) {
                                    unsignedByte = 36;
                                    trim = s;
                                    list2 = list;
                                }
                                break Label_0296;
                            }
                        }
                        else if (unsignedByte2 != 106) {
                            if (unsignedByte2 == 122) {
                                break Label_0144;
                            }
                            if (unsignedByte2 == 123) {
                                unsignedByte = 138;
                                trim = s;
                                list2 = list;
                                break Label_0296;
                            }
                            if (unsignedByte2 == 10) {
                                trim = parsableByteArray.readString(3).trim();
                                list2 = list;
                                break Label_0296;
                            }
                            trim = s;
                            list2 = list;
                            if (unsignedByte2 == 89) {
                                list2 = new ArrayList<TsPayloadReader.DvbSubtitleInfo>();
                                while (parsableByteArray.getPosition() < n2) {
                                    final String trim2 = parsableByteArray.readString(3).trim();
                                    unsignedByte = parsableByteArray.readUnsignedByte();
                                    final byte[] array = new byte[4];
                                    parsableByteArray.readBytes(array, 0, 4);
                                    list2.add(new TsPayloadReader.DvbSubtitleInfo(trim2, unsignedByte, array));
                                }
                                unsignedByte = 89;
                                trim = s;
                            }
                            break Label_0296;
                        }
                        unsignedByte = 129;
                        trim = s;
                        list2 = list;
                        break Label_0296;
                    }
                    unsignedByte = 135;
                    trim = s;
                    list2 = list;
                }
                parsableByteArray.skipBytes(n2 - parsableByteArray.getPosition());
                s = trim;
                list = list2;
            }
            parsableByteArray.setPosition(n);
            return new TsPayloadReader.EsInfo(unsignedByte, s, list, Arrays.copyOfRange(parsableByteArray.data, position, n));
        }
        
        @Override
        public void consume(final ParsableByteArray parsableByteArray) {
            if (parsableByteArray.readUnsignedByte() != 2) {
                return;
            }
            final int access$200 = TsExtractor.this.mode;
            final int n = 0;
            TimestampAdjuster timestampAdjuster;
            if (access$200 != 1 && TsExtractor.this.mode != 2 && TsExtractor.this.remainingPmts != 1) {
                timestampAdjuster = new TimestampAdjuster(TsExtractor.this.timestampAdjusters.get(0).getFirstSampleTimestampUs());
                TsExtractor.this.timestampAdjusters.add(timestampAdjuster);
            }
            else {
                timestampAdjuster = TsExtractor.this.timestampAdjusters.get(0);
            }
            parsableByteArray.skipBytes(2);
            final int unsignedShort = parsableByteArray.readUnsignedShort();
            parsableByteArray.skipBytes(3);
            parsableByteArray.readBytes(this.pmtScratch, 2);
            this.pmtScratch.skipBits(3);
            TsExtractor.this.pcrPid = this.pmtScratch.readBits(13);
            parsableByteArray.readBytes(this.pmtScratch, 2);
            this.pmtScratch.skipBits(4);
            parsableByteArray.skipBytes(this.pmtScratch.readBits(12));
            if (TsExtractor.this.mode == 2 && TsExtractor.this.id3Reader == null) {
                final TsPayloadReader.EsInfo esInfo = new TsPayloadReader.EsInfo(21, null, null, Util.EMPTY_BYTE_ARRAY);
                final TsExtractor this$0 = TsExtractor.this;
                this$0.id3Reader = this$0.payloadReaderFactory.createPayloadReader(21, esInfo);
                TsExtractor.this.id3Reader.init(timestampAdjuster, TsExtractor.this.output, new TsPayloadReader.TrackIdGenerator(unsignedShort, 21, 8192));
            }
            this.trackIdToReaderScratch.clear();
            this.trackIdToPidScratch.clear();
            int n2;
            for (int i = parsableByteArray.bytesLeft(); i > 0; i = n2) {
                parsableByteArray.readBytes(this.pmtScratch, 5);
                final int bits = this.pmtScratch.readBits(8);
                this.pmtScratch.skipBits(3);
                final int bits2 = this.pmtScratch.readBits(13);
                this.pmtScratch.skipBits(4);
                final int bits3 = this.pmtScratch.readBits(12);
                final TsPayloadReader.EsInfo esInfo2 = this.readEsInfo(parsableByteArray, bits3);
                int streamType;
                if ((streamType = bits) == 6) {
                    streamType = esInfo2.streamType;
                }
                n2 = i - (bits3 + 5);
                int n3;
                if (TsExtractor.this.mode == 2) {
                    n3 = streamType;
                }
                else {
                    n3 = bits2;
                }
                if (!TsExtractor.this.trackIds.get(n3)) {
                    TsPayloadReader tsPayloadReader;
                    if (TsExtractor.this.mode == 2 && streamType == 21) {
                        tsPayloadReader = TsExtractor.this.id3Reader;
                    }
                    else {
                        tsPayloadReader = TsExtractor.this.payloadReaderFactory.createPayloadReader(streamType, esInfo2);
                    }
                    if (TsExtractor.this.mode != 2 || bits2 < this.trackIdToPidScratch.get(n3, 8192)) {
                        this.trackIdToPidScratch.put(n3, bits2);
                        this.trackIdToReaderScratch.put(n3, (Object)tsPayloadReader);
                    }
                }
            }
            for (int size = this.trackIdToPidScratch.size(), j = 0; j < size; ++j) {
                final int key = this.trackIdToPidScratch.keyAt(j);
                final int value = this.trackIdToPidScratch.valueAt(j);
                TsExtractor.this.trackIds.put(key, true);
                TsExtractor.this.trackPids.put(value, true);
                final TsPayloadReader tsPayloadReader2 = (TsPayloadReader)this.trackIdToReaderScratch.valueAt(j);
                if (tsPayloadReader2 != null) {
                    if (tsPayloadReader2 != TsExtractor.this.id3Reader) {
                        tsPayloadReader2.init(timestampAdjuster, TsExtractor.this.output, new TsPayloadReader.TrackIdGenerator(unsignedShort, key, 8192));
                    }
                    TsExtractor.this.tsPayloadReaders.put(value, (Object)tsPayloadReader2);
                }
            }
            if (TsExtractor.this.mode == 2) {
                if (!TsExtractor.this.tracksEnded) {
                    TsExtractor.this.output.endTracks();
                    TsExtractor.this.remainingPmts = 0;
                    TsExtractor.this.tracksEnded = true;
                }
            }
            else {
                TsExtractor.this.tsPayloadReaders.remove(this.pid);
                final TsExtractor this$2 = TsExtractor.this;
                int n4;
                if (this$2.mode == 1) {
                    n4 = n;
                }
                else {
                    n4 = TsExtractor.this.remainingPmts - 1;
                }
                this$2.remainingPmts = n4;
                if (TsExtractor.this.remainingPmts == 0) {
                    TsExtractor.this.output.endTracks();
                    TsExtractor.this.tracksEnded = true;
                }
            }
        }
        
        @Override
        public void init(final TimestampAdjuster timestampAdjuster, final ExtractorOutput extractorOutput, final TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        }
    }
}
