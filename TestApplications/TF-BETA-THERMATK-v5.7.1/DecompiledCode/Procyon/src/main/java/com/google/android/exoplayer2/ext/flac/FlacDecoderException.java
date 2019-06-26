// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.audio.AudioDecoderException;

public final class FlacDecoderException extends AudioDecoderException
{
    FlacDecoderException(final String s) {
        super(s);
    }
    
    FlacDecoderException(final String s, final Throwable t) {
        super(s, t);
    }
}
