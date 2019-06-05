// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import com.bumptech.glide.load.data.StreamLocalUriFetcher;
import java.io.InputStream;
import com.bumptech.glide.load.data.FileDescriptorLocalUriFetcher;
import android.content.ContentResolver;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.load.Options;
import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;
import android.net.Uri;

public class UriLoader<Data> implements ModelLoader<Uri, Data>
{
    private static final Set<String> SCHEMES;
    private final LocalUriFetcherFactory<Data> factory;
    
    static {
        SCHEMES = Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(Arrays.asList("file", "android.resource", "content")));
    }
    
    public UriLoader(final LocalUriFetcherFactory<Data> factory) {
        this.factory = factory;
    }
    
    public LoadData<Data> buildLoadData(final Uri uri, final int n, final int n2, final Options options) {
        return (LoadData<Data>)new LoadData(new ObjectKey(uri), (DataFetcher<Object>)this.factory.build(uri));
    }
    
    @Override
    public boolean handles(final Uri uri) {
        return UriLoader.SCHEMES.contains(uri.getScheme());
    }
    
    public static class FileDescriptorFactory implements ModelLoaderFactory<Uri, ParcelFileDescriptor>, LocalUriFetcherFactory<ParcelFileDescriptor>
    {
        private final ContentResolver contentResolver;
        
        public FileDescriptorFactory(final ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }
        
        @Override
        public DataFetcher<ParcelFileDescriptor> build(final Uri uri) {
            return new FileDescriptorLocalUriFetcher(this.contentResolver, uri);
        }
        
        @Override
        public ModelLoader<Uri, ParcelFileDescriptor> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UriLoader<ParcelFileDescriptor>((LocalUriFetcherFactory<ParcelFileDescriptor>)this);
        }
    }
    
    public interface LocalUriFetcherFactory<Data>
    {
        DataFetcher<Data> build(final Uri p0);
    }
    
    public static class StreamFactory implements ModelLoaderFactory<Uri, InputStream>, LocalUriFetcherFactory<InputStream>
    {
        private final ContentResolver contentResolver;
        
        public StreamFactory(final ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }
        
        @Override
        public DataFetcher<InputStream> build(final Uri uri) {
            return new StreamLocalUriFetcher(this.contentResolver, uri);
        }
        
        @Override
        public ModelLoader<Uri, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UriLoader<InputStream>((LocalUriFetcherFactory<InputStream>)this);
        }
    }
}
