// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import java.util.Map;
import java.io.IOException;
import java.io.EOFException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import android.text.TextUtils;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import java.util.Collections;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.Format;
import android.net.Uri;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.extractor.ts.Ac3Extractor;
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import android.util.Pair;
import com.google.android.exoplayer2.extractor.Extractor;

public final class DefaultHlsExtractorFactory implements HlsExtractorFactory
{
    private final int payloadReaderFactoryFlags;
    
    public DefaultHlsExtractorFactory() {
        this(0);
    }
    
    public DefaultHlsExtractorFactory(final int payloadReaderFactoryFlags) {
        this.payloadReaderFactoryFlags = payloadReaderFactoryFlags;
    }
    
    private static Pair<Extractor, Boolean> buildResult(final Extractor extractor) {
        return (Pair<Extractor, Boolean>)new Pair((Object)extractor, (Object)(extractor instanceof AdtsExtractor || extractor instanceof Ac3Extractor || extractor instanceof Mp3Extractor));
    }
    
    private Extractor createExtractorByFileExtension(final Uri uri, final Format format, List<Format> emptyList, final DrmInitData drmInitData, final TimestampAdjuster timestampAdjuster) {
        String lastPathSegment;
        if ((lastPathSegment = uri.getLastPathSegment()) == null) {
            lastPathSegment = "";
        }
        if ("text/vtt".equals(format.sampleMimeType) || lastPathSegment.endsWith(".webvtt") || lastPathSegment.endsWith(".vtt")) {
            return new WebvttExtractor(format.language, timestampAdjuster);
        }
        if (lastPathSegment.endsWith(".aac")) {
            return new AdtsExtractor();
        }
        if (lastPathSegment.endsWith(".ac3") || lastPathSegment.endsWith(".ec3")) {
            return new Ac3Extractor();
        }
        if (lastPathSegment.endsWith(".mp3")) {
            return new Mp3Extractor(0, 0L);
        }
        if (!lastPathSegment.endsWith(".mp4") && !lastPathSegment.startsWith(".m4", lastPathSegment.length() - 4) && !lastPathSegment.startsWith(".mp4", lastPathSegment.length() - 5) && !lastPathSegment.startsWith(".cmf", lastPathSegment.length() - 5)) {
            return createTsExtractor(this.payloadReaderFactoryFlags, format, emptyList, timestampAdjuster);
        }
        if (emptyList == null) {
            emptyList = Collections.emptyList();
        }
        return new FragmentedMp4Extractor(0, timestampAdjuster, null, drmInitData, emptyList);
    }
    
    private static TsExtractor createTsExtractor(int n, final Format format, List<Format> singletonList, final TimestampAdjuster timestampAdjuster) {
        n |= 0x10;
        if (singletonList != null) {
            n |= 0x20;
        }
        else {
            singletonList = Collections.singletonList(Format.createTextSampleFormat(null, "application/cea-608", 0, null));
        }
        final String codecs = format.codecs;
        int n2 = n;
        if (!TextUtils.isEmpty((CharSequence)codecs)) {
            int n3 = n;
            if (!"audio/mp4a-latm".equals(MimeTypes.getAudioMediaMimeType(codecs))) {
                n3 = (n | 0x2);
            }
            n2 = n3;
            if (!"video/avc".equals(MimeTypes.getVideoMediaMimeType(codecs))) {
                n2 = (n3 | 0x4);
            }
        }
        return new TsExtractor(2, timestampAdjuster, new DefaultTsPayloadReaderFactory(n2, singletonList));
    }
    
    private static boolean sniffQuietly(final Extractor extractor, final ExtractorInput extractorInput) throws InterruptedException, IOException {
        boolean sniff;
        try {
            sniff = extractor.sniff(extractorInput);
        }
        catch (EOFException ex) {
            extractorInput.resetPeekPosition();
            sniff = false;
        }
        finally {
            extractorInput.resetPeekPosition();
        }
        return sniff;
    }
    
    @Override
    public Pair<Extractor, Boolean> createExtractor(final Extractor extractor, final Uri uri, final Format format, final List<Format> list, final DrmInitData drmInitData, final TimestampAdjuster timestampAdjuster, final Map<String, List<String>> map, final ExtractorInput extractorInput) throws InterruptedException, IOException {
        if (extractor != null) {
            if (extractor instanceof TsExtractor || extractor instanceof FragmentedMp4Extractor) {
                return buildResult(extractor);
            }
            if (extractor instanceof WebvttExtractor) {
                return buildResult(new WebvttExtractor(format.language, timestampAdjuster));
            }
            if (extractor instanceof AdtsExtractor) {
                return buildResult(new AdtsExtractor());
            }
            if (extractor instanceof Ac3Extractor) {
                return buildResult(new Ac3Extractor());
            }
            if (extractor instanceof Mp3Extractor) {
                return buildResult(new Mp3Extractor());
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected previousExtractor type: ");
            sb.append(extractor.getClass().getSimpleName());
            throw new IllegalArgumentException(sb.toString());
        }
        else {
            final Extractor extractorByFileExtension = this.createExtractorByFileExtension(uri, format, list, drmInitData, timestampAdjuster);
            extractorInput.resetPeekPosition();
            if (sniffQuietly(extractorByFileExtension, extractorInput)) {
                return buildResult(extractorByFileExtension);
            }
            if (!(extractorByFileExtension instanceof WebvttExtractor)) {
                final WebvttExtractor webvttExtractor = new WebvttExtractor(format.language, timestampAdjuster);
                if (sniffQuietly(webvttExtractor, extractorInput)) {
                    return buildResult(webvttExtractor);
                }
            }
            if (!(extractorByFileExtension instanceof AdtsExtractor)) {
                final AdtsExtractor adtsExtractor = new AdtsExtractor();
                if (sniffQuietly(adtsExtractor, extractorInput)) {
                    return buildResult(adtsExtractor);
                }
            }
            if (!(extractorByFileExtension instanceof Ac3Extractor)) {
                final Ac3Extractor ac3Extractor = new Ac3Extractor();
                if (sniffQuietly(ac3Extractor, extractorInput)) {
                    return buildResult(ac3Extractor);
                }
            }
            if (!(extractorByFileExtension instanceof Mp3Extractor)) {
                final Mp3Extractor mp3Extractor = new Mp3Extractor(0, 0L);
                if (sniffQuietly(mp3Extractor, extractorInput)) {
                    return buildResult(mp3Extractor);
                }
            }
            if (!(extractorByFileExtension instanceof FragmentedMp4Extractor)) {
                List<Format> emptyList;
                if (list != null) {
                    emptyList = list;
                }
                else {
                    emptyList = Collections.emptyList();
                }
                final FragmentedMp4Extractor fragmentedMp4Extractor = new FragmentedMp4Extractor(0, timestampAdjuster, null, drmInitData, emptyList);
                if (sniffQuietly(fragmentedMp4Extractor, extractorInput)) {
                    return buildResult(fragmentedMp4Extractor);
                }
            }
            if (!(extractorByFileExtension instanceof TsExtractor)) {
                final TsExtractor tsExtractor = createTsExtractor(this.payloadReaderFactoryFlags, format, list, timestampAdjuster);
                if (sniffQuietly(tsExtractor, extractorInput)) {
                    return buildResult(tsExtractor);
                }
            }
            return buildResult(extractorByFileExtension);
        }
    }
}
