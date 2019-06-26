// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.cea;

import java.util.Collections;
import java.util.List;

public final class Cea708InitializationData
{
    public static List<byte[]> buildData(final boolean b) {
        return Collections.singletonList(new byte[] { (byte)(b ? 1 : 0) });
    }
}
