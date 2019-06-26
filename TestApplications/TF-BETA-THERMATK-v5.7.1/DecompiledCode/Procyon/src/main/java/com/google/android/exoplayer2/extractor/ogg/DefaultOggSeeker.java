// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.SeekPoint;
import java.io.EOFException;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.Assertions;

final class DefaultOggSeeker implements OggSeeker
{
    private long end;
    private long endGranule;
    private final long endPosition;
    private final OggPageHeader pageHeader;
    private long positionBeforeSeekToEnd;
    private long start;
    private long startGranule;
    private final long startPosition;
    private int state;
    private final StreamReader streamReader;
    private long targetGranule;
    private long totalGranules;
    
    public DefaultOggSeeker(final long startPosition, final long endPosition, final StreamReader streamReader, final long n, final long totalGranules, final boolean b) {
        this.pageHeader = new OggPageHeader();
        Assertions.checkArgument(startPosition >= 0L && endPosition > startPosition);
        this.streamReader = streamReader;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        if (n != endPosition - startPosition && !b) {
            this.state = 0;
        }
        else {
            this.totalGranules = totalGranules;
            this.state = 3;
        }
    }
    
    private long getEstimatedPosition(long n, long n2, long endPosition) {
        final long endPosition2 = this.endPosition;
        final long startPosition = this.startPosition;
        n2 = (n += n2 * (endPosition2 - startPosition) / this.totalGranules - endPosition);
        if (n2 < startPosition) {
            n = startPosition;
        }
        endPosition = this.endPosition;
        n2 = n;
        if (n >= endPosition) {
            n2 = endPosition - 1L;
        }
        return n2;
    }
    
    @Override
    public OggSeekMap createSeekMap() {
        OggSeekMap oggSeekMap;
        if (this.totalGranules != 0L) {
            oggSeekMap = new OggSeekMap();
        }
        else {
            oggSeekMap = null;
        }
        return oggSeekMap;
    }
    
    public long getNextSeekPosition(long end, final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final long start = this.start;
        final long end2 = this.end;
        final long n = 2L;
        if (start == end2) {
            return -(this.startGranule + 2L);
        }
        final long position = extractorInput.getPosition();
        if (!this.skipToNextPage(extractorInput, this.end)) {
            end = this.start;
            if (end != position) {
                return end;
            }
            throw new IOException("No ogg page can be found.");
        }
        else {
            this.pageHeader.populate(extractorInput, false);
            extractorInput.resetPeekPosition();
            final OggPageHeader pageHeader = this.pageHeader;
            final long n2 = end - pageHeader.granulePosition;
            final int n3 = pageHeader.headerSize + pageHeader.bodySize;
            if (n2 >= 0L && n2 <= 72000L) {
                extractorInput.skipFully(n3);
                return -(this.pageHeader.granulePosition + 2L);
            }
            if (n2 < 0L) {
                this.end = position;
                this.endGranule = this.pageHeader.granulePosition;
            }
            else {
                final long position2 = extractorInput.getPosition();
                end = n3;
                this.start = position2 + end;
                this.startGranule = this.pageHeader.granulePosition;
                if (this.end - this.start + end < 100000L) {
                    extractorInput.skipFully(n3);
                    return -(this.startGranule + 2L);
                }
            }
            final long end3 = this.end;
            end = this.start;
            if (end3 - end < 100000L) {
                return this.end = end;
            }
            final long n4 = n3;
            if (n2 <= 0L) {
                end = n;
            }
            else {
                end = 1L;
            }
            final long position3 = extractorInput.getPosition();
            final long end4 = this.end;
            final long start2 = this.start;
            return Math.min(Math.max(position3 - n4 * end + n2 * (end4 - start2) / (this.endGranule - this.startGranule), start2), this.end - 1L);
        }
    }
    
    @Override
    public long read(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final int state = this.state;
        if (state != 0) {
            if (state != 1) {
                if (state == 2) {
                    final long targetGranule = this.targetGranule;
                    long skipToPageOfGranule = 0L;
                    if (targetGranule != 0L) {
                        final long nextSeekPosition = this.getNextSeekPosition(targetGranule, extractorInput);
                        if (nextSeekPosition >= 0L) {
                            return nextSeekPosition;
                        }
                        skipToPageOfGranule = this.skipToPageOfGranule(extractorInput, this.targetGranule, -(nextSeekPosition + 2L));
                    }
                    this.state = 3;
                    return -(skipToPageOfGranule + 2L);
                }
                if (state == 3) {
                    return -1L;
                }
                throw new IllegalStateException();
            }
        }
        else {
            this.positionBeforeSeekToEnd = extractorInput.getPosition();
            this.state = 1;
            final long n = this.endPosition - 65307L;
            if (n > this.positionBeforeSeekToEnd) {
                return n;
            }
        }
        this.totalGranules = this.readGranuleOfLastPage(extractorInput);
        this.state = 3;
        return this.positionBeforeSeekToEnd;
    }
    
    long readGranuleOfLastPage(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        this.skipToNextPage(extractorInput);
        this.pageHeader.reset();
        while ((this.pageHeader.type & 0x4) != 0x4 && extractorInput.getPosition() < this.endPosition) {
            this.pageHeader.populate(extractorInput, false);
            final OggPageHeader pageHeader = this.pageHeader;
            extractorInput.skipFully(pageHeader.headerSize + pageHeader.bodySize);
        }
        return this.pageHeader.granulePosition;
    }
    
    public void resetSeeking() {
        this.start = this.startPosition;
        this.end = this.endPosition;
        this.startGranule = 0L;
        this.endGranule = this.totalGranules;
    }
    
    void skipToNextPage(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.skipToNextPage(extractorInput, this.endPosition)) {
            return;
        }
        throw new EOFException();
    }
    
    boolean skipToNextPage(final ExtractorInput extractorInput, long position) throws IOException, InterruptedException {
        final long min = Math.min(position + 3L, this.endPosition);
        final byte[] array = new byte[2048];
        int length = array.length;
        while (true) {
            position = extractorInput.getPosition();
            final long n = length;
            int n2 = 0;
            if (position + n > min) {
                length = (int)(min - extractorInput.getPosition());
                if (length < 4) {
                    return false;
                }
            }
            extractorInput.peekFully(array, 0, length, false);
            while (true) {
                final int n3 = length - 3;
                if (n2 >= n3) {
                    extractorInput.skipFully(n3);
                    break;
                }
                if (array[n2] == 79 && array[n2 + 1] == 103 && array[n2 + 2] == 103 && array[n2 + 3] == 83) {
                    extractorInput.skipFully(n2);
                    return true;
                }
                ++n2;
            }
        }
    }
    
    long skipToPageOfGranule(final ExtractorInput extractorInput, final long n, long granulePosition) throws IOException, InterruptedException {
        this.pageHeader.populate(extractorInput, false);
        while (true) {
            final OggPageHeader pageHeader = this.pageHeader;
            if (pageHeader.granulePosition >= n) {
                break;
            }
            extractorInput.skipFully(pageHeader.headerSize + pageHeader.bodySize);
            final OggPageHeader pageHeader2 = this.pageHeader;
            granulePosition = pageHeader2.granulePosition;
            pageHeader2.populate(extractorInput, false);
        }
        extractorInput.resetPeekPosition();
        return granulePosition;
    }
    
    @Override
    public long startSeek(long convertTimeToGranule) {
        final int state = this.state;
        Assertions.checkArgument(state == 3 || state == 2);
        final long n = 0L;
        if (convertTimeToGranule == 0L) {
            convertTimeToGranule = n;
        }
        else {
            convertTimeToGranule = this.streamReader.convertTimeToGranule(convertTimeToGranule);
        }
        this.targetGranule = convertTimeToGranule;
        this.state = 2;
        this.resetSeeking();
        return this.targetGranule;
    }
    
    private class OggSeekMap implements SeekMap
    {
        @Override
        public long getDurationUs() {
            return DefaultOggSeeker.this.streamReader.convertGranuleToTime(DefaultOggSeeker.this.totalGranules);
        }
        
        @Override
        public SeekPoints getSeekPoints(final long n) {
            if (n == 0L) {
                return new SeekPoints(new SeekPoint(0L, DefaultOggSeeker.this.startPosition));
            }
            final long convertTimeToGranule = DefaultOggSeeker.this.streamReader.convertTimeToGranule(n);
            final DefaultOggSeeker this$0 = DefaultOggSeeker.this;
            return new SeekPoints(new SeekPoint(n, this$0.getEstimatedPosition(this$0.startPosition, convertTimeToGranule, 30000L)));
        }
        
        @Override
        public boolean isSeekable() {
            return true;
        }
    }
}
