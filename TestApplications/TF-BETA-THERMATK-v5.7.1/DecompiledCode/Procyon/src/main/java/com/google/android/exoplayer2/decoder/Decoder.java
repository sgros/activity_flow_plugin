// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.decoder;

public interface Decoder<I, O, E extends Exception>
{
    I dequeueInputBuffer() throws E, Exception;
    
    O dequeueOutputBuffer() throws E, Exception;
    
    void flush();
    
    String getName();
    
    void queueInputBuffer(final I p0) throws E, Exception;
    
    void release();
}
