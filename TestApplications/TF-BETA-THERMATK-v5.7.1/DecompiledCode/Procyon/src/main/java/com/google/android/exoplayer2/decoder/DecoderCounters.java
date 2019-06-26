// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.decoder;

public final class DecoderCounters
{
    public int decoderInitCount;
    public int decoderReleaseCount;
    public int droppedBufferCount;
    public int droppedToKeyframeCount;
    public int inputBufferCount;
    public int maxConsecutiveDroppedBufferCount;
    public int renderedOutputBufferCount;
    public int skippedInputBufferCount;
    public int skippedOutputBufferCount;
    
    public void ensureUpdated() {
    }
    // monitorenter(this)
    // monitorexit(this)
}
