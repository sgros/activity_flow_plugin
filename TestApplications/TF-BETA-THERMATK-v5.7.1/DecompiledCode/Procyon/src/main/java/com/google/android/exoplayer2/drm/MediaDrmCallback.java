// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

import java.util.UUID;

public interface MediaDrmCallback
{
    byte[] executeKeyRequest(final UUID p0, final ExoMediaDrm.KeyRequest p1) throws Exception;
    
    byte[] executeProvisionRequest(final UUID p0, final ExoMediaDrm.ProvisionRequest p1) throws Exception;
}
