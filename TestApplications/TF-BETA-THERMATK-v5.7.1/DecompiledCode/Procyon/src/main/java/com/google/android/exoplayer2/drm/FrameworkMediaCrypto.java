// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

import java.util.UUID;
import android.annotation.TargetApi;

@TargetApi(16)
public final class FrameworkMediaCrypto implements ExoMediaCrypto
{
    public final boolean forceAllowInsecureDecoderComponents;
    public final byte[] sessionId;
    public final UUID uuid;
}
