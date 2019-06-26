// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import java.nio.ByteBuffer;

public class Resampler
{
    public static native int convert44to48(final ByteBuffer p0, final ByteBuffer p1);
    
    public static native int convert48to44(final ByteBuffer p0, final ByteBuffer p1);
}
