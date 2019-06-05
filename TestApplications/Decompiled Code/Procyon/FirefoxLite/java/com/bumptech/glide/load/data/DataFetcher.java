// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;

public interface DataFetcher<T>
{
    void cancel();
    
    void cleanup();
    
    Class<T> getDataClass();
    
    DataSource getDataSource();
    
    void loadData(final Priority p0, final DataCallback<? super T> p1);
    
    public interface DataCallback<T>
    {
        void onDataReady(final T p0);
        
        void onLoadFailed(final Exception p0);
    }
}
