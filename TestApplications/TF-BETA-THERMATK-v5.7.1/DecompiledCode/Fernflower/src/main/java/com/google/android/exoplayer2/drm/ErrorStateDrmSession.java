package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.Assertions;
import java.util.Map;

public final class ErrorStateDrmSession implements DrmSession {
   private final DrmSession.DrmSessionException error;

   public ErrorStateDrmSession(DrmSession.DrmSessionException var1) {
      Assertions.checkNotNull(var1);
      this.error = (DrmSession.DrmSessionException)var1;
   }

   public DrmSession.DrmSessionException getError() {
      return this.error;
   }

   public ExoMediaCrypto getMediaCrypto() {
      return null;
   }

   public int getState() {
      return 1;
   }

   public Map queryKeyStatus() {
      return null;
   }
}
