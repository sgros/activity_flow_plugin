package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import java.util.List;

public interface TrackBitrateEstimator {
   TrackBitrateEstimator DEFAULT = _$$Lambda$TrackBitrateEstimator$2lQ5lBvmOkJuNPw2qehuzXBInmI.INSTANCE;

   int[] getBitrates(Format[] var1, List var2, MediaChunkIterator[] var3, int[] var4);
}
