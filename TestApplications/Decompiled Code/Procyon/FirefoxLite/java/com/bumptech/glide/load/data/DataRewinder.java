// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import java.io.IOException;

public interface DataRewinder<T>
{
    void cleanup();
    
    T rewindAndGet() throws IOException;
    
    public interface Factory<T>
    {
        DataRewinder<T> build(final T p0);
        
        Class<T> getDataClass();
    }
}
