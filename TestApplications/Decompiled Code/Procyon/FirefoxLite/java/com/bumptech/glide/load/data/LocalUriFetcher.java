// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import java.io.FileNotFoundException;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.io.IOException;
import android.net.Uri;
import android.content.ContentResolver;

public abstract class LocalUriFetcher<T> implements DataFetcher<T>
{
    private final ContentResolver contentResolver;
    private T data;
    private final Uri uri;
    
    public LocalUriFetcher(final ContentResolver contentResolver, final Uri uri) {
        this.contentResolver = contentResolver;
        this.uri = uri;
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
    public final void loadData(final Priority priority, final DataCallback<? super T> dataCallback) {
        try {
            dataCallback.onDataReady(this.data = this.loadResource(this.uri, this.contentResolver));
        }
        catch (FileNotFoundException ex) {
            if (Log.isLoggable("LocalUriFetcher", 3)) {
                Log.d("LocalUriFetcher", "Failed to open Uri", (Throwable)ex);
            }
            dataCallback.onLoadFailed(ex);
        }
    }
    
    protected abstract T loadResource(final Uri p0, final ContentResolver p1) throws FileNotFoundException;
}
