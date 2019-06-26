// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.ffmpeg;

import com.google.android.exoplayer2.audio.AudioDecoderException;

public final class FfmpegDecoderException extends AudioDecoderException
{
    FfmpegDecoderException(final String s) {
        super(s);
    }
    
    FfmpegDecoderException(final String s, final Throwable t) {
        super(s, t);
    }
}
