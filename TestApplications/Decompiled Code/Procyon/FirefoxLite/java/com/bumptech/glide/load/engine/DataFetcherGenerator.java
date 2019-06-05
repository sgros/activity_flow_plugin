// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;

interface DataFetcherGenerator
{
    void cancel();
    
    boolean startNext();
    
    public interface FetcherReadyCallback
    {
        void onDataFetcherFailed(final Key p0, final Exception p1, final DataFetcher<?> p2, final DataSource p3);
        
        void onDataFetcherReady(final Key p0, final Object p1, final DataFetcher<?> p2, final DataSource p3, final Key p4);
        
        void reschedule();
    }
}
