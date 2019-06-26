package com.google.android.exoplayer2.drm;

import android.annotation.TargetApi;
import android.os.Looper;

@TargetApi(16)
public interface DrmSessionManager {
   DrmSession acquireSession(Looper var1, DrmInitData var2);

   boolean canAcquireSession(DrmInitData var1);

   void releaseSession(DrmSession var1);
}
