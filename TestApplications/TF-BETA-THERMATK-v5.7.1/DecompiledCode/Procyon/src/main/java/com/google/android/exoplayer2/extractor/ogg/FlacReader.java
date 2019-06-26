// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.extractor.SeekMap;
import java.io.IOException;
import java.util.List;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.Format;
import java.util.Collections;
import java.util.Arrays;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.FlacStreamInfo;

final class FlacReader extends StreamReader
{
    private FlacOggSeeker flacOggSeeker;
    private FlacStreamInfo streamInfo;
    
    private int getFlacFrameBlockSize(final ParsableByteArray parsableByteArray) {
        int n = (parsableByteArray.data[2] & 0xFF) >> 4;
        int n2 = 0;
        switch (n) {
            default: {
                return -1;
            }
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15: {
                n2 = 256;
                n -= 8;
                break;
            }
            case 6:
            case 7: {
                parsableByteArray.skipBytes(4);
                parsableByteArray.readUtf8EncodedLong();
                int n3;
                if (n == 6) {
                    n3 = parsableByteArray.readUnsignedByte();
                }
                else {
                    n3 = parsableByteArray.readUnsignedShort();
                }
                parsableByteArray.setPosition(0);
                return n3 + 1;
            }
            case 2:
            case 3:
            case 4:
            case 5: {
                n2 = 576;
                n -= 2;
                break;
            }
            case 1: {
                return 192;
            }
        }
        return n2 << n;
    }
    
    private static boolean isAudioPacket(final byte[] array) {
        boolean b = false;
        if (array[0] == -1) {
            b = true;
        }
        return b;
    }
    
    public static boolean verifyBitstreamType(final ParsableByteArray parsableByteArray) {
        return parsableByteArray.bytesLeft() >= 5 && parsableByteArray.readUnsignedByte() == 127 && parsableByteArray.readUnsignedInt() == 1179402563L;
    }
    
    @Override
    protected long preparePayload(final ParsableByteArray parsableByteArray) {
        if (!isAudioPacket(parsableByteArray.data)) {
            return -1L;
        }
        return this.getFlacFrameBlockSize(parsableByteArray);
    }
    
    @Override
    protected boolean readHeaders(final ParsableByteArray parsableByteArray, final long firstFrameOffset, final SetupData setupData) throws IOException, InterruptedException {
        final byte[] data = parsableByteArray.data;
        if (this.streamInfo == null) {
            this.streamInfo = new FlacStreamInfo(data, 17);
            final byte[] copyOfRange = Arrays.copyOfRange(data, 9, parsableByteArray.limit());
            copyOfRange[4] = -128;
            final List<byte[]> singletonList = Collections.singletonList(copyOfRange);
            final int bitRate = this.streamInfo.bitRate();
            final FlacStreamInfo streamInfo = this.streamInfo;
            setupData.format = Format.createAudioSampleFormat(null, "audio/flac", null, -1, bitRate, streamInfo.channels, streamInfo.sampleRate, singletonList, null, 0, null);
        }
        else if ((data[0] & 0x7F) == 0x3) {
            (this.flacOggSeeker = new FlacOggSeeker()).parseSeekTable(parsableByteArray);
        }
        else if (isAudioPacket(data)) {
            final FlacOggSeeker flacOggSeeker = this.flacOggSeeker;
            if (flacOggSeeker != null) {
                flacOggSeeker.setFirstFrameOffset(firstFrameOffset);
                setupData.oggSeeker = this.flacOggSeeker;
            }
            return false;
        }
        return true;
    }
    
    @Override
    protected void reset(final boolean b) {
        super.reset(b);
        if (b) {
            this.streamInfo = null;
            this.flacOggSeeker = null;
        }
    }
    
    private class FlacOggSeeker implements OggSeeker, SeekMap
    {
        private long firstFrameOffset;
        private long pendingSeekGranule;
        private long[] seekPointGranules;
        private long[] seekPointOffsets;
        
        public FlacOggSeeker() {
            this.firstFrameOffset = -1L;
            this.pendingSeekGranule = -1L;
        }
        
        @Override
        public SeekMap createSeekMap() {
            return this;
        }
        
        @Override
        public long getDurationUs() {
            return FlacReader.this.streamInfo.durationUs();
        }
        
        @Override
        public SeekPoints getSeekPoints(final long n) {
            int binarySearchFloor = Util.binarySearchFloor(this.seekPointGranules, FlacReader.this.convertTimeToGranule(n), true, true);
            final long convertGranuleToTime = FlacReader.this.convertGranuleToTime(this.seekPointGranules[binarySearchFloor]);
            final SeekPoint seekPoint = new SeekPoint(convertGranuleToTime, this.firstFrameOffset + this.seekPointOffsets[binarySearchFloor]);
            if (convertGranuleToTime < n) {
                final long[] seekPointGranules = this.seekPointGranules;
                if (binarySearchFloor != seekPointGranules.length - 1) {
                    final FlacReader this$0 = FlacReader.this;
                    ++binarySearchFloor;
                    return new SeekPoints(seekPoint, new SeekPoint(this$0.convertGranuleToTime(seekPointGranules[binarySearchFloor]), this.firstFrameOffset + this.seekPointOffsets[binarySearchFloor]));
                }
            }
            return new SeekPoints(seekPoint);
        }
        
        @Override
        public boolean isSeekable() {
            return true;
        }
        
        public void parseSeekTable(final ParsableByteArray parsableByteArray) {
            parsableByteArray.skipBytes(1);
            final int n = parsableByteArray.readUnsignedInt24() / 18;
            this.seekPointGranules = new long[n];
            this.seekPointOffsets = new long[n];
            for (int i = 0; i < n; ++i) {
                this.seekPointGranules[i] = parsableByteArray.readLong();
                this.seekPointOffsets[i] = parsableByteArray.readLong();
                parsableByteArray.skipBytes(2);
            }
        }
        
        @Override
        public long read(final ExtractorInput extractorInput) throws IOException, InterruptedException {
            final long pendingSeekGranule = this.pendingSeekGranule;
            if (pendingSeekGranule >= 0L) {
                final long n = -(pendingSeekGranule + 2L);
                this.pendingSeekGranule = -1L;
                return n;
            }
            return -1L;
        }
        
        public void setFirstFrameOffset(final long firstFrameOffset) {
            this.firstFrameOffset = firstFrameOffset;
        }
        
        @Override
        public long startSeek(long convertTimeToGranule) {
            convertTimeToGranule = FlacReader.this.convertTimeToGranule(convertTimeToGranule);
            this.pendingSeekGranule = this.seekPointGranules[Util.binarySearchFloor(this.seekPointGranules, convertTimeToGranule, true, true)];
            return convertTimeToGranule;
        }
    }
}
