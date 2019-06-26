// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import java.io.IOException;
import java.util.Arrays;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import java.util.regex.Matcher;
import com.google.android.exoplayer2.ParserException;
import android.text.TextUtils;
import com.google.android.exoplayer2.text.webvtt.WebvttParserUtil;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import java.util.regex.Pattern;
import com.google.android.exoplayer2.extractor.Extractor;

public final class WebvttExtractor implements Extractor
{
    private static final Pattern LOCAL_TIMESTAMP;
    private static final Pattern MEDIA_TIMESTAMP;
    private final String language;
    private ExtractorOutput output;
    private byte[] sampleData;
    private final ParsableByteArray sampleDataWrapper;
    private int sampleSize;
    private final TimestampAdjuster timestampAdjuster;
    
    static {
        LOCAL_TIMESTAMP = Pattern.compile("LOCAL:([^,]+)");
        MEDIA_TIMESTAMP = Pattern.compile("MPEGTS:(\\d+)");
    }
    
    public WebvttExtractor(final String language, final TimestampAdjuster timestampAdjuster) {
        this.language = language;
        this.timestampAdjuster = timestampAdjuster;
        this.sampleDataWrapper = new ParsableByteArray();
        this.sampleData = new byte[1024];
    }
    
    private TrackOutput buildTrackOutput(final long n) {
        final TrackOutput track = this.output.track(0, 3);
        track.format(Format.createTextSampleFormat(null, "text/vtt", null, -1, 0, this.language, null, n));
        this.output.endTracks();
        return track;
    }
    
    private void processSample() throws ParserException {
        final ParsableByteArray parsableByteArray = new ParsableByteArray(this.sampleData);
        WebvttParserUtil.validateWebvttHeaderLine(parsableByteArray);
        long timestampUs;
        long ptsToUs = timestampUs = 0L;
        while (true) {
            final String line = parsableByteArray.readLine();
            if (!TextUtils.isEmpty((CharSequence)line)) {
                if (!line.startsWith("X-TIMESTAMP-MAP")) {
                    continue;
                }
                final Matcher matcher = WebvttExtractor.LOCAL_TIMESTAMP.matcher(line);
                if (!matcher.find()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("X-TIMESTAMP-MAP doesn't contain local timestamp: ");
                    sb.append(line);
                    throw new ParserException(sb.toString());
                }
                final Matcher matcher2 = WebvttExtractor.MEDIA_TIMESTAMP.matcher(line);
                if (!matcher2.find()) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("X-TIMESTAMP-MAP doesn't contain media timestamp: ");
                    sb2.append(line);
                    throw new ParserException(sb2.toString());
                }
                timestampUs = WebvttParserUtil.parseTimestampUs(matcher.group(1));
                ptsToUs = TimestampAdjuster.ptsToUs(Long.parseLong(matcher2.group(1)));
            }
            else {
                final Matcher nextCueHeader = WebvttParserUtil.findNextCueHeader(parsableByteArray);
                if (nextCueHeader == null) {
                    this.buildTrackOutput(0L);
                    return;
                }
                final long timestampUs2 = WebvttParserUtil.parseTimestampUs(nextCueHeader.group(1));
                final long adjustTsTimestamp = this.timestampAdjuster.adjustTsTimestamp(TimestampAdjuster.usToPts(ptsToUs + timestampUs2 - timestampUs));
                final TrackOutput buildTrackOutput = this.buildTrackOutput(adjustTsTimestamp - timestampUs2);
                this.sampleDataWrapper.reset(this.sampleData, this.sampleSize);
                buildTrackOutput.sampleData(this.sampleDataWrapper, this.sampleSize);
                buildTrackOutput.sampleMetadata(adjustTsTimestamp, 1, this.sampleSize, 0, null);
            }
        }
    }
    
    @Override
    public void init(final ExtractorOutput output) {
        (this.output = output).seekMap(new SeekMap.Unseekable(-9223372036854775807L));
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final int n = (int)extractorInput.getLength();
        final int sampleSize = this.sampleSize;
        final byte[] sampleData = this.sampleData;
        if (sampleSize == sampleData.length) {
            int length;
            if (n != -1) {
                length = n;
            }
            else {
                length = sampleData.length;
            }
            this.sampleData = Arrays.copyOf(sampleData, length * 3 / 2);
        }
        final byte[] sampleData2 = this.sampleData;
        final int sampleSize2 = this.sampleSize;
        final int read = extractorInput.read(sampleData2, sampleSize2, sampleData2.length - sampleSize2);
        if (read != -1) {
            this.sampleSize += read;
            if (n == -1 || this.sampleSize != n) {
                return 0;
            }
        }
        this.processSample();
        return -1;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        throw new IllegalStateException();
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.peekFully(this.sampleData, 0, 6, false);
        this.sampleDataWrapper.reset(this.sampleData, 6);
        if (WebvttParserUtil.isWebvttHeaderLine(this.sampleDataWrapper)) {
            return true;
        }
        extractorInput.peekFully(this.sampleData, 6, 3, false);
        this.sampleDataWrapper.reset(this.sampleData, 9);
        return WebvttParserUtil.isWebvttHeaderLine(this.sampleDataWrapper);
    }
}
