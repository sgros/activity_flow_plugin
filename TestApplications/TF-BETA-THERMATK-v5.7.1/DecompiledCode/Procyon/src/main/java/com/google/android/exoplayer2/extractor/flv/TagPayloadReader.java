// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.TrackOutput;

abstract class TagPayloadReader
{
    protected final TrackOutput output;
    
    protected TagPayloadReader(final TrackOutput output) {
        this.output = output;
    }
    
    public final void consume(final ParsableByteArray parsableByteArray, final long n) throws ParserException {
        if (this.parseHeader(parsableByteArray)) {
            this.parsePayload(parsableByteArray, n);
        }
    }
    
    protected abstract boolean parseHeader(final ParsableByteArray p0) throws ParserException;
    
    protected abstract void parsePayload(final ParsableByteArray p0, final long p1) throws ParserException;
    
    public static final class UnsupportedFormatException extends ParserException
    {
        public UnsupportedFormatException(final String s) {
            super(s);
        }
    }
}
