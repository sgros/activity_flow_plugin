// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.amr;

import com.google.android.exoplayer2.extractor.PositionHolder;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ConstantBitrateSeekMap;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public final class AmrExtractor implements Extractor
{
    public static final ExtractorsFactory FACTORY;
    private static final int MAX_FRAME_SIZE_BYTES;
    private static final byte[] amrSignatureNb;
    private static final byte[] amrSignatureWb;
    private static final int[] frameSizeBytesByTypeNb;
    private static final int[] frameSizeBytesByTypeWb;
    private int currentSampleBytesRemaining;
    private int currentSampleSize;
    private long currentSampleTimeUs;
    private ExtractorOutput extractorOutput;
    private long firstSamplePosition;
    private int firstSampleSize;
    private final int flags;
    private boolean hasOutputFormat;
    private boolean hasOutputSeekMap;
    private boolean isWideBand;
    private int numSamplesWithSameSize;
    private final byte[] scratch;
    private SeekMap seekMap;
    private long timeOffsetUs;
    private TrackOutput trackOutput;
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$AmrExtractor$lVuGuaAcylUV__XE4_hSR1hBylI.INSTANCE;
        frameSizeBytesByTypeNb = new int[] { 13, 14, 16, 18, 20, 21, 27, 32, 6, 7, 6, 6, 1, 1, 1, 1 };
        frameSizeBytesByTypeWb = new int[] { 18, 24, 33, 37, 41, 47, 51, 59, 61, 6, 1, 1, 1, 1, 1, 1 };
        amrSignatureNb = Util.getUtf8Bytes("#!AMR\n");
        amrSignatureWb = Util.getUtf8Bytes("#!AMR-WB\n");
        MAX_FRAME_SIZE_BYTES = AmrExtractor.frameSizeBytesByTypeWb[8];
    }
    
    public AmrExtractor() {
        this(0);
    }
    
    public AmrExtractor(final int flags) {
        this.flags = flags;
        this.scratch = new byte[1];
        this.firstSampleSize = -1;
    }
    
    private static int getBitrateFromFrameSize(final int n, final long n2) {
        return (int)(n * 8 * 1000000L / n2);
    }
    
    private SeekMap getConstantBitrateSeekMap(final long n) {
        return new ConstantBitrateSeekMap(n, this.firstSamplePosition, getBitrateFromFrameSize(this.firstSampleSize, 20000L), this.firstSampleSize);
    }
    
    private int getFrameSizeInBytes(int i) throws ParserException {
        if (!this.isValidFrameType(i)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Illegal AMR ");
            String str;
            if (this.isWideBand) {
                str = "WB";
            }
            else {
                str = "NB";
            }
            sb.append(str);
            sb.append(" frame type ");
            sb.append(i);
            throw new ParserException(sb.toString());
        }
        if (this.isWideBand) {
            i = AmrExtractor.frameSizeBytesByTypeWb[i];
        }
        else {
            i = AmrExtractor.frameSizeBytesByTypeNb[i];
        }
        return i;
    }
    
    private boolean isNarrowBandValidFrameType(final int n) {
        return !this.isWideBand && (n < 12 || n > 14);
    }
    
    private boolean isValidFrameType(final int n) {
        return n >= 0 && n <= 15 && (this.isWideBandValidFrameType(n) || this.isNarrowBandValidFrameType(n));
    }
    
    private boolean isWideBandValidFrameType(final int n) {
        return this.isWideBand && (n < 10 || n > 13);
    }
    
    private void maybeOutputFormat() {
        if (!this.hasOutputFormat) {
            this.hasOutputFormat = true;
            String s;
            if (this.isWideBand) {
                s = "audio/amr-wb";
            }
            else {
                s = "audio/3gpp";
            }
            int n;
            if (this.isWideBand) {
                n = 16000;
            }
            else {
                n = 8000;
            }
            this.trackOutput.format(Format.createAudioSampleFormat(null, s, null, -1, AmrExtractor.MAX_FRAME_SIZE_BYTES, 1, n, -1, null, null, 0, null));
        }
    }
    
    private void maybeOutputSeekMap(final long n, final int n2) {
        if (this.hasOutputSeekMap) {
            return;
        }
        if ((this.flags & 0x1) != 0x0 && n != -1L) {
            final int firstSampleSize = this.firstSampleSize;
            if (firstSampleSize == -1 || firstSampleSize == this.currentSampleSize) {
                if (this.numSamplesWithSameSize >= 20 || n2 == -1) {
                    this.seekMap = this.getConstantBitrateSeekMap(n);
                    this.extractorOutput.seekMap(this.seekMap);
                    this.hasOutputSeekMap = true;
                }
                return;
            }
        }
        this.seekMap = new SeekMap.Unseekable(-9223372036854775807L);
        this.extractorOutput.seekMap(this.seekMap);
        this.hasOutputSeekMap = true;
    }
    
    private boolean peekAmrSignature(final ExtractorInput extractorInput, final byte[] a2) throws IOException, InterruptedException {
        extractorInput.resetPeekPosition();
        final byte[] a3 = new byte[a2.length];
        extractorInput.peekFully(a3, 0, a2.length);
        return Arrays.equals(a3, a2);
    }
    
    private int peekNextSampleSize(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.resetPeekPosition();
        extractorInput.peekFully(this.scratch, 0, 1);
        final byte i = this.scratch[0];
        if ((i & 0x83) <= 0) {
            return this.getFrameSizeInBytes(i >> 3 & 0xF);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid padding bits for frame header ");
        sb.append(i);
        throw new ParserException(sb.toString());
    }
    
    private boolean readAmrHeader(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.peekAmrSignature(extractorInput, AmrExtractor.amrSignatureNb)) {
            this.isWideBand = false;
            extractorInput.skipFully(AmrExtractor.amrSignatureNb.length);
            return true;
        }
        if (this.peekAmrSignature(extractorInput, AmrExtractor.amrSignatureWb)) {
            this.isWideBand = true;
            extractorInput.skipFully(AmrExtractor.amrSignatureWb.length);
            return true;
        }
        return false;
    }
    
    private int readSample(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.currentSampleBytesRemaining == 0) {
            try {
                this.currentSampleSize = this.peekNextSampleSize(extractorInput);
                this.currentSampleBytesRemaining = this.currentSampleSize;
                if (this.firstSampleSize == -1) {
                    this.firstSamplePosition = extractorInput.getPosition();
                    this.firstSampleSize = this.currentSampleSize;
                }
                if (this.firstSampleSize == this.currentSampleSize) {
                    ++this.numSamplesWithSameSize;
                }
            }
            catch (EOFException ex) {
                return -1;
            }
        }
        final int sampleData = this.trackOutput.sampleData(extractorInput, this.currentSampleBytesRemaining, true);
        if (sampleData == -1) {
            return -1;
        }
        this.currentSampleBytesRemaining -= sampleData;
        if (this.currentSampleBytesRemaining > 0) {
            return 0;
        }
        this.trackOutput.sampleMetadata(this.timeOffsetUs + this.currentSampleTimeUs, 1, this.currentSampleSize, 0, null);
        this.currentSampleTimeUs += 20000L;
        return 0;
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = extractorOutput.track(0, 1);
        extractorOutput.endTracks();
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        if (extractorInput.getPosition() == 0L && !this.readAmrHeader(extractorInput)) {
            throw new ParserException("Could not find AMR header.");
        }
        this.maybeOutputFormat();
        final int sample = this.readSample(extractorInput);
        this.maybeOutputSeekMap(extractorInput.getLength(), sample);
        return sample;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        this.currentSampleTimeUs = 0L;
        this.currentSampleSize = 0;
        this.currentSampleBytesRemaining = 0;
        if (n != 0L) {
            final SeekMap seekMap = this.seekMap;
            if (seekMap instanceof ConstantBitrateSeekMap) {
                this.timeOffsetUs = ((ConstantBitrateSeekMap)seekMap).getTimeUsAtPosition(n);
                return;
            }
        }
        this.timeOffsetUs = 0L;
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        return this.readAmrHeader(extractorInput);
    }
}
