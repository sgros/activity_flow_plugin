package com.google.android.exoplayer2.drm;

import android.annotation.TargetApi;
import java.util.Map;

@TargetApi(16)
public interface DrmSession {
   DrmSession.DrmSessionException getError();

   ExoMediaCrypto getMediaCrypto();

   int getState();

   Map queryKeyStatus();

   public static class DrmSessionException extends Exception {
      public DrmSessionException(Throwable var1) {
         super(var1);
      }
   }
}
