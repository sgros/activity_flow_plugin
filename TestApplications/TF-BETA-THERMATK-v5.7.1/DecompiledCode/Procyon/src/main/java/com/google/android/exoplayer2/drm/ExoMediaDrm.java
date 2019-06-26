// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

import java.util.Map;
import android.media.DeniedByServerException;
import android.media.MediaDrmException;
import android.media.NotProvisionedException;
import java.util.HashMap;
import java.util.List;
import android.media.MediaCryptoException;

public interface ExoMediaDrm<T extends ExoMediaCrypto>
{
    void closeSession(final byte[] p0);
    
    T createMediaCrypto(final byte[] p0) throws MediaCryptoException;
    
    KeyRequest getKeyRequest(final byte[] p0, final List<DrmInitData.SchemeData> p1, final int p2, final HashMap<String, String> p3) throws NotProvisionedException;
    
    ProvisionRequest getProvisionRequest();
    
    byte[] openSession() throws MediaDrmException;
    
    byte[] provideKeyResponse(final byte[] p0, final byte[] p1) throws NotProvisionedException, DeniedByServerException;
    
    void provideProvisionResponse(final byte[] p0) throws DeniedByServerException;
    
    Map<String, String> queryKeyStatus(final byte[] p0);
    
    void restoreKeys(final byte[] p0, final byte[] p1);
    
    public static final class KeyRequest
    {
    }
    
    public static final class ProvisionRequest
    {
    }
}
