package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HlsExtractorFactory {
   HlsExtractorFactory DEFAULT = new DefaultHlsExtractorFactory();

   Pair createExtractor(Extractor var1, Uri var2, Format var3, List var4, DrmInitData var5, TimestampAdjuster var6, Map var7, ExtractorInput var8) throws InterruptedException, IOException;
}
