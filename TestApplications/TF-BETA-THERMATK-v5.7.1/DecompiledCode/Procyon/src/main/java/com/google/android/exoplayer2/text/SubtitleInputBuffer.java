// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;

public class SubtitleInputBuffer extends DecoderInputBuffer
{
    public long subsampleOffsetUs;
    
    public SubtitleInputBuffer() {
        super(1);
    }
}
