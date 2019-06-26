package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.source.dash.manifest.RangedUri;

public interface DashSegmentIndex {
   long getDurationUs(long var1, long var3);

   long getFirstSegmentNum();

   int getSegmentCount(long var1);

   long getSegmentNum(long var1, long var3);

   RangedUri getSegmentUrl(long var1);

   long getTimeUs(long var1);

   boolean isExplicit();
}
