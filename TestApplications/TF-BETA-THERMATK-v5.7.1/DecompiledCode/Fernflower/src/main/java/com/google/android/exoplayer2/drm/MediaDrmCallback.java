package com.google.android.exoplayer2.drm;

import java.util.UUID;

public interface MediaDrmCallback {
   byte[] executeKeyRequest(UUID var1, ExoMediaDrm.KeyRequest var2) throws Exception;

   byte[] executeProvisionRequest(UUID var1, ExoMediaDrm.ProvisionRequest var2) throws Exception;
}
