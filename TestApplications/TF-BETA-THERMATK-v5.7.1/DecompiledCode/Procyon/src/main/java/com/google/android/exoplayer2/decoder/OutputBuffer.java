// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.decoder;

public abstract class OutputBuffer extends Buffer
{
    public int skippedOutputBufferCount;
    public long timeUs;
    
    public abstract void release();
}
