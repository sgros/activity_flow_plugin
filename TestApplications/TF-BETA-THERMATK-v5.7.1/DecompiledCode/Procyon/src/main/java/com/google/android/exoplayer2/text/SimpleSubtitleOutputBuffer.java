// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text;

final class SimpleSubtitleOutputBuffer extends SubtitleOutputBuffer
{
    private final SimpleSubtitleDecoder owner;
    
    public SimpleSubtitleOutputBuffer(final SimpleSubtitleDecoder owner) {
        this.owner = owner;
    }
    
    @Override
    public final void release() {
        this.owner.releaseOutputBuffer(this);
    }
}
