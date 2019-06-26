// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import java.io.IOException;

public interface ExtractorInput
{
    void advancePeekPosition(final int p0) throws IOException, InterruptedException;
    
    boolean advancePeekPosition(final int p0, final boolean p1) throws IOException, InterruptedException;
    
    long getLength();
    
    long getPeekPosition();
    
    long getPosition();
    
    void peekFully(final byte[] p0, final int p1, final int p2) throws IOException, InterruptedException;
    
    boolean peekFully(final byte[] p0, final int p1, final int p2, final boolean p3) throws IOException, InterruptedException;
    
    int read(final byte[] p0, final int p1, final int p2) throws IOException, InterruptedException;
    
    void readFully(final byte[] p0, final int p1, final int p2) throws IOException, InterruptedException;
    
    boolean readFully(final byte[] p0, final int p1, final int p2, final boolean p3) throws IOException, InterruptedException;
    
    void resetPeekPosition();
    
     <E extends Throwable> void setRetryPosition(final long p0, final E p1) throws E, Throwable;
    
    int skip(final int p0) throws IOException, InterruptedException;
    
    void skipFully(final int p0) throws IOException, InterruptedException;
}
