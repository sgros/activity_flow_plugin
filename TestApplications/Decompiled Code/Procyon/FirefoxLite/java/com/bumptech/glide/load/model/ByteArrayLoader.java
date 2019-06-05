// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.nio.ByteBuffer;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.load.Options;

public class ByteArrayLoader<Data> implements ModelLoader<byte[], Data>
{
    private final Converter<Data> converter;
    
    public ByteArrayLoader(final Converter<Data> converter) {
        this.converter = converter;
    }
    
    public LoadData<Data> buildLoadData(final byte[] array, final int n, final int n2, final Options options) {
        return (LoadData<Data>)new LoadData(EmptySignature.obtain(), new Fetcher<Object>(array, (Converter<Object>)this.converter));
    }
    
    @Override
    public boolean handles(final byte[] array) {
        return true;
    }
    
    public static class ByteBufferFactory implements ModelLoaderFactory<byte[], ByteBuffer>
    {
        @Override
        public ModelLoader<byte[], ByteBuffer> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteArrayLoader<ByteBuffer>((Converter<ByteBuffer>)new Converter<ByteBuffer>() {
                public ByteBuffer convert(final byte[] array) {
                    return ByteBuffer.wrap(array);
                }
                
                @Override
                public Class<ByteBuffer> getDataClass() {
                    return ByteBuffer.class;
                }
            });
        }
    }
    
    public interface Converter<Data>
    {
        Data convert(final byte[] p0);
        
        Class<Data> getDataClass();
    }
    
    private static class Fetcher<Data> implements DataFetcher<Data>
    {
        private final Converter<Data> converter;
        private final byte[] model;
        
        public Fetcher(final byte[] model, final Converter<Data> converter) {
            this.model = model;
            this.converter = converter;
        }
        
        @Override
        public void cancel() {
        }
        
        @Override
        public void cleanup() {
        }
        
        @Override
        public Class<Data> getDataClass() {
            return this.converter.getDataClass();
        }
        
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
        
        @Override
        public void loadData(final Priority priority, final DataCallback<? super Data> dataCallback) {
            dataCallback.onDataReady(this.converter.convert(this.model));
        }
    }
    
    public static class StreamFactory implements ModelLoaderFactory<byte[], InputStream>
    {
        @Override
        public ModelLoader<byte[], InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteArrayLoader<InputStream>((Converter<InputStream>)new Converter<InputStream>() {
                public InputStream convert(final byte[] buf) {
                    return new ByteArrayInputStream(buf);
                }
                
                @Override
                public Class<InputStream> getDataClass() {
                    return InputStream.class;
                }
            });
        }
    }
}
