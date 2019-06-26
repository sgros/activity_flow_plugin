// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.opus;

import com.google.android.exoplayer2.audio.AudioDecoderException;

public final class OpusDecoderException extends AudioDecoderException
{
    OpusDecoderException(final String s) {
        super(s);
    }
    
    OpusDecoderException(final String s, final Throwable t) {
        super(s, t);
    }
}
