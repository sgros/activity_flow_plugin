// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

public interface SequenceableLoader
{
    boolean continueLoading(final long p0);
    
    long getBufferedPositionUs();
    
    long getNextLoadPositionUs();
    
    void reevaluateBuffer(final long p0);
    
    public interface Callback<T extends SequenceableLoader>
    {
        void onContinueLoadingRequested(final T p0);
    }
}
