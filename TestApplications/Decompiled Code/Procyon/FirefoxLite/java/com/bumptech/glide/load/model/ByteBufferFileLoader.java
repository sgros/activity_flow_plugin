// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.io.IOException;
import android.util.Log;
import com.bumptech.glide.util.ByteBufferUtil;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.load.Options;
import java.nio.ByteBuffer;
import java.io.File;

public class ByteBufferFileLoader implements ModelLoader<File, ByteBuffer>
{
    public LoadData<ByteBuffer> buildLoadData(final File file, final int n, final int n2, final Options options) {
        return (LoadData<ByteBuffer>)new LoadData(new ObjectKey(file), (DataFetcher<Object>)new ByteBufferFetcher(file));
    }
    
    @Override
    public boolean handles(final File file) {
        return true;
    }
    
    private static class ByteBufferFetcher implements DataFetcher<ByteBuffer>
    {
        private final File file;
        
        public ByteBufferFetcher(final File file) {
            this.file = file;
        }
        
        @Override
        public void cancel() {
        }
        
        @Override
        public void cleanup() {
        }
        
        @Override
        public Class<ByteBuffer> getDataClass() {
            return ByteBuffer.class;
        }
        
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
        
        @Override
        public void loadData(final Priority priority, final DataCallback<? super ByteBuffer> dataCallback) {
            try {
                dataCallback.onDataReady(ByteBufferUtil.fromFile(this.file));
            }
            catch (IOException ex) {
                if (Log.isLoggable("ByteBufferFileLoader", 3)) {
                    Log.d("ByteBufferFileLoader", "Failed to obtain ByteBuffer for file", (Throwable)ex);
                }
                dataCallback.onLoadFailed(ex);
            }
        }
    }
    
    public static class Factory implements ModelLoaderFactory<File, ByteBuffer>
    {
        @Override
        public ModelLoader<File, ByteBuffer> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteBufferFileLoader();
        }
    }
}
