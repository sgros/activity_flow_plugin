// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.io.IOException;
import android.content.res.AssetManager;

public abstract class AssetPathFetcher<T> implements DataFetcher<T>
{
    private final AssetManager assetManager;
    private final String assetPath;
    private T data;
    
    public AssetPathFetcher(final AssetManager assetManager, final String assetPath) {
        this.assetManager = assetManager;
        this.assetPath = assetPath;
    }
    
    @Override
    public void cancel() {
    }
    
    @Override
    public void cleanup() {
        if (this.data == null) {
            return;
        }
        try {
            this.close(this.data);
        }
        catch (IOException ex) {}
    }
    
    protected abstract void close(final T p0) throws IOException;
    
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
    
    @Override
    public void loadData(final Priority priority, final DataCallback<? super T> dataCallback) {
        try {
            dataCallback.onDataReady(this.data = this.loadResource(this.assetManager, this.assetPath));
        }
        catch (IOException ex) {
            if (Log.isLoggable("AssetPathFetcher", 3)) {
                Log.d("AssetPathFetcher", "Failed to load data from asset manager", (Throwable)ex);
            }
            dataCallback.onLoadFailed(ex);
        }
    }
    
    protected abstract T loadResource(final AssetManager p0, final String p1) throws IOException;
}
