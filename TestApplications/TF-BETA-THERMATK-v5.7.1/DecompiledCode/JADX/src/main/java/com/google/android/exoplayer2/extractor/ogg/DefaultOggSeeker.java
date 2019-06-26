package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekMap.SeekPoints;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.Assertions;
import java.io.EOFException;
import java.io.IOException;

final class DefaultOggSeeker implements OggSeeker {
    private long end;
    private long endGranule;
    private final long endPosition;
    private final OggPageHeader pageHeader = new OggPageHeader();
    private long positionBeforeSeekToEnd;
    private long start;
    private long startGranule;
    private final long startPosition;
    private int state;
    private final StreamReader streamReader;
    private long targetGranule;
    private long totalGranules;

    private class OggSeekMap implements SeekMap {
        public boolean isSeekable() {
            return true;
        }

        private OggSeekMap() {
        }

        public SeekPoints getSeekPoints(long j) {
            if (j == 0) {
                return new SeekPoints(new SeekPoint(0, DefaultOggSeeker.this.startPosition));
            }
            long convertTimeToGranule = DefaultOggSeeker.this.streamReader.convertTimeToGranule(j);
            DefaultOggSeeker defaultOggSeeker = DefaultOggSeeker.this;
            return new SeekPoints(new SeekPoint(j, defaultOggSeeker.getEstimatedPosition(defaultOggSeeker.startPosition, convertTimeToGranule, 30000)));
        }

        public long getDurationUs() {
            return DefaultOggSeeker.this.streamReader.convertGranuleToTime(DefaultOggSeeker.this.totalGranules);
        }
    }

    public DefaultOggSeeker(long j, long j2, StreamReader streamReader, long j3, long j4, boolean z) {
        boolean z2 = j >= 0 && j2 > j;
        Assertions.checkArgument(z2);
        this.streamReader = streamReader;
        this.startPosition = j;
        this.endPosition = j2;
        if (j3 == j2 - j || z) {
            this.totalGranules = j4;
            this.state = 3;
            return;
        }
        this.state = 0;
    }

    public long read(ExtractorInput extractorInput) throws IOException, InterruptedException {
        int i = this.state;
        long j;
        if (i == 0) {
            this.positionBeforeSeekToEnd = extractorInput.getPosition();
            this.state = 1;
            j = this.endPosition - 65307;
            if (j > this.positionBeforeSeekToEnd) {
                return j;
            }
        } else if (i != 1) {
            if (i == 2) {
                j = this.targetGranule;
                long j2 = 0;
                if (j != 0) {
                    j = getNextSeekPosition(j, extractorInput);
                    if (j >= 0) {
                        return j;
                    }
                    j2 = skipToPageOfGranule(extractorInput, this.targetGranule, -(j + 2));
                }
                this.state = 3;
                return -(j2 + 2);
            } else if (i == 3) {
                return -1;
            } else {
                throw new IllegalStateException();
            }
        }
        this.totalGranules = readGranuleOfLastPage(extractorInput);
        this.state = 3;
        return this.positionBeforeSeekToEnd;
    }

    public long startSeek(long j) {
        int i = this.state;
        boolean z = i == 3 || i == 2;
        Assertions.checkArgument(z);
        long j2 = 0;
        if (j != 0) {
            j2 = this.streamReader.convertTimeToGranule(j);
        }
        this.targetGranule = j2;
        this.state = 2;
        resetSeeking();
        return this.targetGranule;
    }

    public OggSeekMap createSeekMap() {
        return this.totalGranules != 0 ? new OggSeekMap() : null;
    }

    public void resetSeeking() {
        this.start = this.startPosition;
        this.end = this.endPosition;
        this.startGranule = 0;
        this.endGranule = this.totalGranules;
    }

    public long getNextSeekPosition(long j, ExtractorInput extractorInput) throws IOException, InterruptedException {
        ExtractorInput extractorInput2 = extractorInput;
        long j2 = 2;
        if (this.start == this.end) {
            return -(this.startGranule + 2);
        }
        long position = extractorInput.getPosition();
        if (skipToNextPage(extractorInput2, this.end)) {
            this.pageHeader.populate(extractorInput2, false);
            extractorInput.resetPeekPosition();
            OggPageHeader oggPageHeader = this.pageHeader;
            long j3 = j - oggPageHeader.granulePosition;
            int i = oggPageHeader.headerSize + oggPageHeader.bodySize;
            if (j3 < 0 || j3 > 72000) {
                if (j3 < 0) {
                    this.end = position;
                    this.endGranule = this.pageHeader.granulePosition;
                } else {
                    long j4 = (long) i;
                    this.start = extractorInput.getPosition() + j4;
                    this.startGranule = this.pageHeader.granulePosition;
                    if ((this.end - this.start) + j4 < 100000) {
                        extractorInput2.skipFully(i);
                        return -(this.startGranule + 2);
                    }
                }
                position = this.end;
                long j5 = this.start;
                if (position - j5 < 100000) {
                    this.end = j5;
                    return j5;
                }
                position = (long) i;
                if (j3 > 0) {
                    j2 = 1;
                }
                j2 = extractorInput.getPosition() - (position * j2);
                long j6 = this.end;
                j5 = this.start;
                return Math.min(Math.max(j2 + ((j3 * (j6 - j5)) / (this.endGranule - this.startGranule)), j5), this.end - 1);
            }
            extractorInput2.skipFully(i);
            return -(this.pageHeader.granulePosition + 2);
        }
        long j7 = this.start;
        if (j7 != position) {
            return j7;
        }
        throw new IOException("No ogg page can be found.");
    }

    private long getEstimatedPosition(long j, long j2, long j3) {
        long j4 = this.endPosition;
        long j5 = this.startPosition;
        j += ((j2 * (j4 - j5)) / this.totalGranules) - j3;
        if (j < j5) {
            j = j5;
        }
        j2 = this.endPosition;
        return j >= j2 ? j2 - 1 : j;
    }

    /* Access modifiers changed, original: 0000 */
    public void skipToNextPage(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (!skipToNextPage(extractorInput, this.endPosition)) {
            throw new EOFException();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean skipToNextPage(ExtractorInput extractorInput, long j) throws IOException, InterruptedException {
        j = Math.min(j + 3, this.endPosition);
        byte[] bArr = new byte[2048];
        int length = bArr.length;
        while (true) {
            int position;
            int i = 0;
            if (extractorInput.getPosition() + ((long) length) > j) {
                position = (int) (j - extractorInput.getPosition());
                if (position < 4) {
                    return false;
                }
                length = position;
            }
            extractorInput.peekFully(bArr, 0, length, false);
            while (true) {
                position = length - 3;
                if (i >= position) {
                    break;
                } else if (bArr[i] == (byte) 79 && bArr[i + 1] == (byte) 103 && bArr[i + 2] == (byte) 103 && bArr[i + 3] == (byte) 83) {
                    extractorInput.skipFully(i);
                    return true;
                } else {
                    i++;
                }
            }
            extractorInput.skipFully(position);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public long readGranuleOfLastPage(ExtractorInput extractorInput) throws IOException, InterruptedException {
        skipToNextPage(extractorInput);
        this.pageHeader.reset();
        while ((this.pageHeader.type & 4) != 4 && extractorInput.getPosition() < this.endPosition) {
            this.pageHeader.populate(extractorInput, false);
            OggPageHeader oggPageHeader = this.pageHeader;
            extractorInput.skipFully(oggPageHeader.headerSize + oggPageHeader.bodySize);
        }
        return this.pageHeader.granulePosition;
    }

    /* Access modifiers changed, original: 0000 */
    public long skipToPageOfGranule(ExtractorInput extractorInput, long j, long j2) throws IOException, InterruptedException {
        this.pageHeader.populate(extractorInput, false);
        while (true) {
            OggPageHeader oggPageHeader = this.pageHeader;
            if (oggPageHeader.granulePosition < j) {
                extractorInput.skipFully(oggPageHeader.headerSize + oggPageHeader.bodySize);
                OggPageHeader oggPageHeader2 = this.pageHeader;
                long j3 = oggPageHeader2.granulePosition;
                oggPageHeader2.populate(extractorInput, false);
                j2 = j3;
            } else {
                extractorInput.resetPeekPosition();
                return j2;
            }
        }
    }
}
