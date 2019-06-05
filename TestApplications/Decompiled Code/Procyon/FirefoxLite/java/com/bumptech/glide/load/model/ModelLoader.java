// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import com.bumptech.glide.util.Preconditions;
import java.util.Collections;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import java.util.List;
import com.bumptech.glide.load.Options;

public interface ModelLoader<Model, Data>
{
    LoadData<Data> buildLoadData(final Model p0, final int p1, final int p2, final Options p3);
    
    boolean handles(final Model p0);
    
    public static class LoadData<Data>
    {
        public final List<Key> alternateKeys;
        public final DataFetcher<Data> fetcher;
        public final Key sourceKey;
        
        public LoadData(final Key key, final DataFetcher<Data> dataFetcher) {
            this(key, Collections.emptyList(), dataFetcher);
        }
        
        public LoadData(final Key key, final List<Key> list, final DataFetcher<Data> dataFetcher) {
            this.sourceKey = Preconditions.checkNotNull(key);
            this.alternateKeys = Preconditions.checkNotNull(list);
            this.fetcher = Preconditions.checkNotNull(dataFetcher);
        }
    }
}
