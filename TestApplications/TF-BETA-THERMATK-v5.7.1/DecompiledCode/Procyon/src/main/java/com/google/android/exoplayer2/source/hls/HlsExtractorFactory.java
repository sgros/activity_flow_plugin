// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import java.io.IOException;
import android.util.Pair;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import java.util.Map;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.Format;
import android.net.Uri;
import com.google.android.exoplayer2.extractor.Extractor;

public interface HlsExtractorFactory
{
    public static final HlsExtractorFactory DEFAULT = new DefaultHlsExtractorFactory();
    
    Pair<Extractor, Boolean> createExtractor(final Extractor p0, final Uri p1, final Format p2, final List<Format> p3, final DrmInitData p4, final TimestampAdjuster p5, final Map<String, List<String>> p6, final ExtractorInput p7) throws InterruptedException, IOException;
}
