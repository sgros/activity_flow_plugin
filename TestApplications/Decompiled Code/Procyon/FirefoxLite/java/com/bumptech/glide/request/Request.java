// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request;

public interface Request
{
    void begin();
    
    void clear();
    
    boolean isCancelled();
    
    boolean isComplete();
    
    boolean isEquivalentTo(final Request p0);
    
    boolean isResourceSet();
    
    boolean isRunning();
    
    void pause();
    
    void recycle();
}
