package com.google.android.exoplayer2.drm;

import android.annotation.TargetApi;
import java.util.UUID;

@TargetApi(16)
public final class FrameworkMediaCrypto implements ExoMediaCrypto {
   public final boolean forceAllowInsecureDecoderComponents;
   public final byte[] sessionId;
   public final UUID uuid;
}
