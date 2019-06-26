package com.google.android.exoplayer2.extractor.p002ts;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap.Unseekable;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.DvbSubtitleInfo;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.EsInfo;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.Factory;
import com.google.android.exoplayer2.extractor.p002ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.MessagesController;

/* renamed from: com.google.android.exoplayer2.extractor.ts.TsExtractor */
public final class TsExtractor implements Extractor {
    private static final long AC3_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("AC-3"));
    private static final long E_AC3_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("EAC3"));
    public static final ExtractorsFactory FACTORY = C3342-$$Lambda$TsExtractor$f-UE6PC86cqq4V-qVoFQnPhfFZ8.INSTANCE;
    private static final long HEVC_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("HEVC"));
    private int bytesSinceLastSync;
    private final SparseIntArray continuityCounters;
    private final TsDurationReader durationReader;
    private boolean hasOutputSeekMap;
    private TsPayloadReader id3Reader;
    private final int mode;
    private ExtractorOutput output;
    private final Factory payloadReaderFactory;
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

    /* renamed from: com.google.android.exoplayer2.extractor.ts.TsExtractor$PatReader */
    private class PatReader implements SectionPayloadReader {
        private final ParsableBitArray patScratch = new ParsableBitArray(new byte[4]);

        public void init(TimestampAdjuster timestampAdjuster, ExtractorOutput extractorOutput, TrackIdGenerator trackIdGenerator) {
        }

        public void consume(ParsableByteArray parsableByteArray) {
            if (parsableByteArray.readUnsignedByte() == 0) {
                parsableByteArray.skipBytes(7);
                int bytesLeft = parsableByteArray.bytesLeft() / 4;
                for (int i = 0; i < bytesLeft; i++) {
                    parsableByteArray.readBytes(this.patScratch, 4);
                    int readBits = this.patScratch.readBits(16);
                    this.patScratch.skipBits(3);
                    if (readBits == 0) {
                        this.patScratch.skipBits(13);
                    } else {
                        readBits = this.patScratch.readBits(13);
                        TsExtractor.this.tsPayloadReaders.put(readBits, new SectionReader(new PmtReader(readBits)));
                        TsExtractor.this.remainingPmts = TsExtractor.this.remainingPmts + 1;
                    }
                }
                if (TsExtractor.this.mode != 2) {
                    TsExtractor.this.tsPayloadReaders.remove(0);
                }
            }
        }
    }

    /* renamed from: com.google.android.exoplayer2.extractor.ts.TsExtractor$PmtReader */
    private class PmtReader implements SectionPayloadReader {
        private final int pid;
        private final ParsableBitArray pmtScratch = new ParsableBitArray(new byte[5]);
        private final SparseIntArray trackIdToPidScratch = new SparseIntArray();
        private final SparseArray<TsPayloadReader> trackIdToReaderScratch = new SparseArray();

        public void init(TimestampAdjuster timestampAdjuster, ExtractorOutput extractorOutput, TrackIdGenerator trackIdGenerator) {
        }

        public PmtReader(int i) {
            this.pid = i;
        }

        public void consume(ParsableByteArray parsableByteArray) {
            ParsableByteArray parsableByteArray2 = parsableByteArray;
            if (parsableByteArray.readUnsignedByte() == 2) {
                TimestampAdjuster timestampAdjuster;
                int i = 0;
                if (TsExtractor.this.mode == 1 || TsExtractor.this.mode == 2 || TsExtractor.this.remainingPmts == 1) {
                    timestampAdjuster = (TimestampAdjuster) TsExtractor.this.timestampAdjusters.get(0);
                } else {
                    timestampAdjuster = new TimestampAdjuster(((TimestampAdjuster) TsExtractor.this.timestampAdjusters.get(0)).getFirstSampleTimestampUs());
                    TsExtractor.this.timestampAdjusters.add(timestampAdjuster);
                }
                parsableByteArray2.skipBytes(2);
                int readUnsignedShort = parsableByteArray.readUnsignedShort();
                int i2 = 3;
                parsableByteArray2.skipBytes(3);
                parsableByteArray2.readBytes(this.pmtScratch, 2);
                this.pmtScratch.skipBits(3);
                int i3 = 13;
                TsExtractor.this.pcrPid = this.pmtScratch.readBits(13);
                parsableByteArray2.readBytes(this.pmtScratch, 2);
                int i4 = 4;
                this.pmtScratch.skipBits(4);
                parsableByteArray2.skipBytes(this.pmtScratch.readBits(12));
                if (TsExtractor.this.mode == 2 && TsExtractor.this.id3Reader == null) {
                    EsInfo esInfo = new EsInfo(21, null, null, Util.EMPTY_BYTE_ARRAY);
                    TsExtractor tsExtractor = TsExtractor.this;
                    tsExtractor.id3Reader = tsExtractor.payloadReaderFactory.createPayloadReader(21, esInfo);
                    TsExtractor.this.id3Reader.init(timestampAdjuster, TsExtractor.this.output, new TrackIdGenerator(readUnsignedShort, 21, MessagesController.UPDATE_MASK_CHAT));
                }
                this.trackIdToReaderScratch.clear();
                this.trackIdToPidScratch.clear();
                int bytesLeft = parsableByteArray.bytesLeft();
                while (bytesLeft > 0) {
                    parsableByteArray2.readBytes(this.pmtScratch, 5);
                    int readBits = this.pmtScratch.readBits(8);
                    this.pmtScratch.skipBits(i2);
                    int readBits2 = this.pmtScratch.readBits(i3);
                    this.pmtScratch.skipBits(i4);
                    i2 = this.pmtScratch.readBits(12);
                    EsInfo readEsInfo = readEsInfo(parsableByteArray2, i2);
                    if (readBits == 6) {
                        readBits = readEsInfo.streamType;
                    }
                    bytesLeft -= i2 + 5;
                    i2 = TsExtractor.this.mode == 2 ? readBits : readBits2;
                    if (!TsExtractor.this.trackIds.get(i2)) {
                        Object access$500;
                        if (TsExtractor.this.mode == 2 && readBits == 21) {
                            access$500 = TsExtractor.this.id3Reader;
                        } else {
                            access$500 = TsExtractor.this.payloadReaderFactory.createPayloadReader(readBits, readEsInfo);
                        }
                        if (TsExtractor.this.mode != 2 || readBits2 < this.trackIdToPidScratch.get(i2, MessagesController.UPDATE_MASK_CHAT)) {
                            this.trackIdToPidScratch.put(i2, readBits2);
                            this.trackIdToReaderScratch.put(i2, access$500);
                        }
                    }
                    i2 = 3;
                    i4 = 4;
                    i3 = 13;
                }
                int size = this.trackIdToPidScratch.size();
                for (i2 = 0; i2 < size; i2++) {
                    bytesLeft = this.trackIdToPidScratch.keyAt(i2);
                    i4 = this.trackIdToPidScratch.valueAt(i2);
                    TsExtractor.this.trackIds.put(bytesLeft, true);
                    TsExtractor.this.trackPids.put(i4, true);
                    TsPayloadReader tsPayloadReader = (TsPayloadReader) this.trackIdToReaderScratch.valueAt(i2);
                    if (tsPayloadReader != null) {
                        if (tsPayloadReader != TsExtractor.this.id3Reader) {
                            tsPayloadReader.init(timestampAdjuster, TsExtractor.this.output, new TrackIdGenerator(readUnsignedShort, bytesLeft, MessagesController.UPDATE_MASK_CHAT));
                        }
                        TsExtractor.this.tsPayloadReaders.put(i4, tsPayloadReader);
                    }
                }
                if (TsExtractor.this.mode != 2) {
                    TsExtractor.this.tsPayloadReaders.remove(this.pid);
                    TsExtractor tsExtractor2 = TsExtractor.this;
                    if (tsExtractor2.mode != 1) {
                        i = TsExtractor.this.remainingPmts - 1;
                    }
                    tsExtractor2.remainingPmts = i;
                    if (TsExtractor.this.remainingPmts == 0) {
                        TsExtractor.this.output.endTracks();
                        TsExtractor.this.tracksEnded = true;
                    }
                } else if (!TsExtractor.this.tracksEnded) {
                    TsExtractor.this.output.endTracks();
                    TsExtractor.this.remainingPmts = 0;
                    TsExtractor.this.tracksEnded = true;
                }
            }
        }

        private EsInfo readEsInfo(ParsableByteArray parsableByteArray, int i) {
            int position = parsableByteArray.getPosition();
            i += position;
            String str = null;
            int i2 = -1;
            List list = null;
            while (parsableByteArray.getPosition() < i) {
                int readUnsignedByte = parsableByteArray.readUnsignedByte();
                int position2 = parsableByteArray.getPosition() + parsableByteArray.readUnsignedByte();
                if (readUnsignedByte == 5) {
                    long readUnsignedInt = parsableByteArray.readUnsignedInt();
                    if (readUnsignedInt != TsExtractor.AC3_FORMAT_IDENTIFIER) {
                        if (readUnsignedInt != TsExtractor.E_AC3_FORMAT_IDENTIFIER) {
                            if (readUnsignedInt == TsExtractor.HEVC_FORMAT_IDENTIFIER) {
                                i2 = 36;
                            }
                            parsableByteArray.skipBytes(position2 - parsableByteArray.getPosition());
                        }
                        i2 = 135;
                        parsableByteArray.skipBytes(position2 - parsableByteArray.getPosition());
                    }
                } else if (readUnsignedByte != 106) {
                    if (readUnsignedByte != 122) {
                        if (readUnsignedByte == 123) {
                            i2 = 138;
                        } else if (readUnsignedByte == 10) {
                            str = parsableByteArray.readString(3).trim();
                        } else if (readUnsignedByte == 89) {
                            ArrayList arrayList = new ArrayList();
                            while (parsableByteArray.getPosition() < position2) {
                                String trim = parsableByteArray.readString(3).trim();
                                readUnsignedByte = parsableByteArray.readUnsignedByte();
                                byte[] bArr = new byte[4];
                                parsableByteArray.readBytes(bArr, 0, 4);
                                arrayList.add(new DvbSubtitleInfo(trim, readUnsignedByte, bArr));
                            }
                            list = arrayList;
                            i2 = 89;
                        }
                        parsableByteArray.skipBytes(position2 - parsableByteArray.getPosition());
                    }
                    i2 = 135;
                    parsableByteArray.skipBytes(position2 - parsableByteArray.getPosition());
                }
                i2 = 129;
                parsableByteArray.skipBytes(position2 - parsableByteArray.getPosition());
            }
            parsableByteArray.setPosition(i);
            return new EsInfo(i2, str, list, Arrays.copyOfRange(parsableByteArray.data, position, i));
        }
    }

    public void release() {
    }

    public TsExtractor() {
        this(0);
    }

    public TsExtractor(int i) {
        this(1, i);
    }

    public TsExtractor(int i, int i2) {
        this(i, new TimestampAdjuster(0), new DefaultTsPayloadReaderFactory(i2));
    }

    public TsExtractor(int i, TimestampAdjuster timestampAdjuster, Factory factory) {
        Assertions.checkNotNull(factory);
        this.payloadReaderFactory = factory;
        this.mode = i;
        if (i == 1 || i == 2) {
            this.timestampAdjusters = Collections.singletonList(timestampAdjuster);
        } else {
            this.timestampAdjusters = new ArrayList();
            this.timestampAdjusters.add(timestampAdjuster);
        }
        this.tsPacketBuffer = new ParsableByteArray(new byte[9400], 0);
        this.trackIds = new SparseBooleanArray();
        this.trackPids = new SparseBooleanArray();
        this.tsPayloadReaders = new SparseArray();
        this.continuityCounters = new SparseIntArray();
        this.durationReader = new TsDurationReader();
        this.pcrPid = -1;
        resetPayloadReaders();
    }

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        byte[] bArr = this.tsPacketBuffer.data;
        extractorInput.peekFully(bArr, 0, 940);
        for (int i = 0; i < 188; i++) {
            Object obj;
            for (int i2 = 0; i2 < 5; i2++) {
                if (bArr[(i2 * 188) + i] != (byte) 71) {
                    obj = null;
                    break;
                }
            }
            obj = 1;
            if (obj != null) {
                extractorInput.skipFully(i);
                return true;
            }
        }
        return false;
    }

    public void init(ExtractorOutput extractorOutput) {
        this.output = extractorOutput;
    }

    public void seek(long j, long j2) {
        Assertions.checkState(this.mode != 2);
        int size = this.timestampAdjusters.size();
        for (int i = 0; i < size; i++) {
            TimestampAdjuster timestampAdjuster = (TimestampAdjuster) this.timestampAdjusters.get(i);
            if (!((timestampAdjuster.getTimestampOffsetUs() == -9223372036854775807L ? 1 : null) == null && (timestampAdjuster.getTimestampOffsetUs() == 0 || timestampAdjuster.getFirstSampleTimestampUs() == j2))) {
                timestampAdjuster.reset();
                timestampAdjuster.setFirstSampleTimestampUs(j2);
            }
        }
        if (j2 != 0) {
            TsBinarySearchSeeker tsBinarySearchSeeker = this.tsBinarySearchSeeker;
            if (tsBinarySearchSeeker != null) {
                tsBinarySearchSeeker.setSeekTargetUs(j2);
            }
        }
        this.tsPacketBuffer.reset();
        this.continuityCounters.clear();
        for (size = 0; size < this.tsPayloadReaders.size(); size++) {
            ((TsPayloadReader) this.tsPayloadReaders.valueAt(size)).seek();
        }
        this.bytesSinceLastSync = 0;
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        ExtractorInput extractorInput2 = extractorInput;
        PositionHolder positionHolder2 = positionHolder;
        long length = extractorInput.getLength();
        TsPayloadReader tsPayloadReader = null;
        if (this.tracksEnded) {
            Object obj = (length == -1 || this.mode == 2) ? null : 1;
            if (obj != null && !this.durationReader.isDurationReadFinished()) {
                return this.durationReader.readDuration(extractorInput2, positionHolder2, this.pcrPid);
            }
            maybeOutputSeekMap(length);
            if (this.pendingSeekToStart) {
                this.pendingSeekToStart = false;
                seek(0, 0);
                if (extractorInput.getPosition() != 0) {
                    positionHolder2.position = 0;
                    return 1;
                }
            }
            TsBinarySearchSeeker tsBinarySearchSeeker = this.tsBinarySearchSeeker;
            if (tsBinarySearchSeeker != null && tsBinarySearchSeeker.isSeeking()) {
                return this.tsBinarySearchSeeker.handlePendingSeek(extractorInput2, positionHolder2, null);
            }
        }
        if (!fillBufferWithAtLeastOnePacket(extractorInput)) {
            return -1;
        }
        int findEndOfFirstTsPacketInBuffer = findEndOfFirstTsPacketInBuffer();
        int limit = this.tsPacketBuffer.limit();
        if (findEndOfFirstTsPacketInBuffer > limit) {
            return 0;
        }
        int readInt = this.tsPacketBuffer.readInt();
        if ((8388608 & readInt) != 0) {
            this.tsPacketBuffer.setPosition(findEndOfFirstTsPacketInBuffer);
            return 0;
        }
        int i = ((4194304 & readInt) != 0 ? 1 : 0) | 0;
        int i2 = (2096896 & readInt) >> 8;
        Object obj2 = (readInt & 32) != 0 ? 1 : null;
        if (((readInt & 16) != 0 ? 1 : null) != null) {
            tsPayloadReader = (TsPayloadReader) this.tsPayloadReaders.get(i2);
        }
        if (tsPayloadReader == null) {
            this.tsPacketBuffer.setPosition(findEndOfFirstTsPacketInBuffer);
            return 0;
        }
        if (this.mode != 2) {
            readInt &= 15;
            int i3 = this.continuityCounters.get(i2, readInt - 1);
            this.continuityCounters.put(i2, readInt);
            if (i3 == readInt) {
                this.tsPacketBuffer.setPosition(findEndOfFirstTsPacketInBuffer);
                return 0;
            } else if (readInt != ((i3 + 1) & 15)) {
                tsPayloadReader.seek();
            }
        }
        if (obj2 != null) {
            i |= (this.tsPacketBuffer.readUnsignedByte() & 64) != 0 ? 2 : 0;
            this.tsPacketBuffer.skipBytes(this.tsPacketBuffer.readUnsignedByte() - 1);
        }
        boolean z = this.tracksEnded;
        if (shouldConsumePacketPayload(i2)) {
            this.tsPacketBuffer.setLimit(findEndOfFirstTsPacketInBuffer);
            tsPayloadReader.consume(this.tsPacketBuffer, i);
            this.tsPacketBuffer.setLimit(limit);
        }
        if (!(this.mode == 2 || z || !this.tracksEnded || length == -1)) {
            this.pendingSeekToStart = true;
        }
        this.tsPacketBuffer.setPosition(findEndOfFirstTsPacketInBuffer);
        return 0;
    }

    private void maybeOutputSeekMap(long j) {
        if (!this.hasOutputSeekMap) {
            this.hasOutputSeekMap = true;
            if (this.durationReader.getDurationUs() != -9223372036854775807L) {
                this.tsBinarySearchSeeker = new TsBinarySearchSeeker(this.durationReader.getPcrTimestampAdjuster(), this.durationReader.getDurationUs(), j, this.pcrPid);
                this.output.seekMap(this.tsBinarySearchSeeker.getSeekMap());
                return;
            }
            this.output.seekMap(new Unseekable(this.durationReader.getDurationUs()));
        }
    }

    private boolean fillBufferWithAtLeastOnePacket(ExtractorInput extractorInput) throws IOException, InterruptedException {
        int bytesLeft;
        ParsableByteArray parsableByteArray = this.tsPacketBuffer;
        byte[] bArr = parsableByteArray.data;
        if (9400 - parsableByteArray.getPosition() < 188) {
            bytesLeft = this.tsPacketBuffer.bytesLeft();
            if (bytesLeft > 0) {
                System.arraycopy(bArr, this.tsPacketBuffer.getPosition(), bArr, 0, bytesLeft);
            }
            this.tsPacketBuffer.reset(bArr, bytesLeft);
        }
        while (this.tsPacketBuffer.bytesLeft() < 188) {
            bytesLeft = this.tsPacketBuffer.limit();
            int read = extractorInput.read(bArr, bytesLeft, 9400 - bytesLeft);
            if (read == -1) {
                return false;
            }
            this.tsPacketBuffer.setLimit(bytesLeft + read);
        }
        return true;
    }

    private int findEndOfFirstTsPacketInBuffer() throws ParserException {
        int position = this.tsPacketBuffer.getPosition();
        int limit = this.tsPacketBuffer.limit();
        int findSyncBytePosition = TsUtil.findSyncBytePosition(this.tsPacketBuffer.data, position, limit);
        this.tsPacketBuffer.setPosition(findSyncBytePosition);
        int i = findSyncBytePosition + 188;
        if (i > limit) {
            this.bytesSinceLastSync += findSyncBytePosition - position;
            if (this.mode == 2 && this.bytesSinceLastSync > 376) {
                throw new ParserException("Cannot find sync byte. Most likely not a Transport Stream.");
            }
        }
        this.bytesSinceLastSync = 0;
        return i;
    }

    private boolean shouldConsumePacketPayload(int i) {
        if (this.mode == 2 || this.tracksEnded || !this.trackPids.get(i, false)) {
            return true;
        }
        return false;
    }

    private void resetPayloadReaders() {
        this.trackIds.clear();
        this.tsPayloadReaders.clear();
        SparseArray createInitialPayloadReaders = this.payloadReaderFactory.createInitialPayloadReaders();
        int size = createInitialPayloadReaders.size();
        for (int i = 0; i < size; i++) {
            this.tsPayloadReaders.put(createInitialPayloadReaders.keyAt(i), createInitialPayloadReaders.valueAt(i));
        }
        this.tsPayloadReaders.put(0, new SectionReader(new PatReader()));
        this.id3Reader = null;
    }
}
