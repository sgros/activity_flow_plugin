// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.io.ByteArrayInputStream;
import android.util.Base64;
import java.io.InputStream;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.io.IOException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.load.Options;

public final class DataUrlLoader<Data> implements ModelLoader<String, Data>
{
    private final DataDecoder<Data> dataDecoder;
    
    public DataUrlLoader(final DataDecoder<Data> dataDecoder) {
        this.dataDecoder = dataDecoder;
    }
    
    public LoadData<Data> buildLoadData(final String s, final int n, final int n2, final Options options) {
        return (LoadData<Data>)new LoadData(new ObjectKey(s), new DataUriFetcher<Object>(s, (DataDecoder<Object>)this.dataDecoder));
    }
    
    @Override
    public boolean handles(final String s) {
        return s.startsWith("data:image");
    }
    
    public interface DataDecoder<Data>
    {
        void close(final Data p0) throws IOException;
        
        Data decode(final String p0) throws IllegalArgumentException;
        
        Class<Data> getDataClass();
    }
    
    private static final class DataUriFetcher<Data> implements DataFetcher<Data>
    {
        private Data data;
        private final String dataUri;
        private final DataDecoder<Data> reader;
        
        public DataUriFetcher(final String dataUri, final DataDecoder<Data> reader) {
            this.dataUri = dataUri;
            this.reader = reader;
        }
        
        @Override
        public void cancel() {
        }
        
        @Override
        public void cleanup() {
            try {
                this.reader.close(this.data);
            }
            catch (IOException ex) {}
        }
        
        @Override
        public Class<Data> getDataClass() {
            return this.reader.getDataClass();
        }
        
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
        
        @Override
        public void loadData(final Priority priority, final DataCallback<? super Data> dataCallback) {
            try {
                dataCallback.onDataReady(this.data = this.reader.decode(this.dataUri));
            }
            catch (IllegalArgumentException ex) {
                dataCallback.onLoadFailed(ex);
            }
        }
    }
    
    public static final class StreamFactory implements ModelLoaderFactory<String, InputStream>
    {
        private final DataDecoder<InputStream> opener;
        
        public StreamFactory() {
            this.opener = new DataDecoder<InputStream>() {
                public void close(final InputStream inputStream) throws IOException {
                    inputStream.close();
                }
                
                public InputStream decode(final String s) {
                    if (!s.startsWith("data:image")) {
                        throw new IllegalArgumentException("Not a valid image data URL.");
                    }
                    final int index = s.indexOf(44);
                    if (index == -1) {
                        throw new IllegalArgumentException("Missing comma in data URL.");
                    }
                    if (s.substring(0, index).endsWith(";base64")) {
                        return new ByteArrayInputStream(Base64.decode(s.substring(index + 1), 0));
                    }
                    throw new IllegalArgumentException("Not a base64 image data URL.");
                }
                
                @Override
                public Class<InputStream> getDataClass() {
                    return InputStream.class;
                }
            };
        }
        
        @Override
        public final ModelLoader<String, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new DataUrlLoader<InputStream>(this.opener);
        }
    }
}
