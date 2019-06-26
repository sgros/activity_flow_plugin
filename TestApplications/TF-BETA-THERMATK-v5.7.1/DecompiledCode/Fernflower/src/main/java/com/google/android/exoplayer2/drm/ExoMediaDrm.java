package com.google.android.exoplayer2.drm;

import android.media.DeniedByServerException;
import android.media.MediaCryptoException;
import android.media.MediaDrmException;
import android.media.NotProvisionedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ExoMediaDrm {
   void closeSession(byte[] var1);

   ExoMediaCrypto createMediaCrypto(byte[] var1) throws MediaCryptoException;

   ExoMediaDrm.KeyRequest getKeyRequest(byte[] var1, List var2, int var3, HashMap var4) throws NotProvisionedException;

   ExoMediaDrm.ProvisionRequest getProvisionRequest();

   byte[] openSession() throws MediaDrmException;

   byte[] provideKeyResponse(byte[] var1, byte[] var2) throws NotProvisionedException, DeniedByServerException;

   void provideProvisionResponse(byte[] var1) throws DeniedByServerException;

   Map queryKeyStatus(byte[] var1);

   void restoreKeys(byte[] var1, byte[] var2);

   public static final class KeyRequest {
   }

   public static final class ProvisionRequest {
   }
}
