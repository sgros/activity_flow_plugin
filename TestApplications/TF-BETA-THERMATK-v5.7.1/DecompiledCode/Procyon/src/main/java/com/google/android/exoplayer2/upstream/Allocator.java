// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

public interface Allocator
{
    Allocation allocate();
    
    int getIndividualAllocationLength();
    
    void release(final Allocation p0);
    
    void release(final Allocation[] p0);
    
    void trim();
}
