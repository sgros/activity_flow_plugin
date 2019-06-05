// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import java.io.InputStream;
import com.bumptech.glide.load.data.FileDescriptorAssetPathFetcher;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.load.Options;
import android.content.res.AssetManager;
import android.net.Uri;

public class AssetUriLoader<Data> implements ModelLoader<Uri, Data>
{
    private static final int ASSET_PREFIX_LENGTH;
    private final AssetManager assetManager;
    private final AssetFetcherFactory<Data> factory;
    
    static {
        ASSET_PREFIX_LENGTH = "file:///android_asset/".length();
    }
    
    public AssetUriLoader(final AssetManager assetManager, final AssetFetcherFactory<Data> factory) {
        this.assetManager = assetManager;
        this.factory = factory;
    }
    
    public LoadData<Data> buildLoadData(final Uri uri, final int n, final int n2, final Options options) {
        return (LoadData<Data>)new LoadData(new ObjectKey(uri), (DataFetcher<Object>)this.factory.buildFetcher(this.assetManager, uri.toString().substring(AssetUriLoader.ASSET_PREFIX_LENGTH)));
    }
    
    @Override
    public boolean handles(final Uri uri) {
        final boolean equals = "file".equals(uri.getScheme());
        boolean b2;
        final boolean b = b2 = false;
        if (equals) {
            b2 = b;
            if (!uri.getPathSegments().isEmpty()) {
                b2 = b;
                if ("android_asset".equals(uri.getPathSegments().get(0))) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public interface AssetFetcherFactory<Data>
    {
        DataFetcher<Data> buildFetcher(final AssetManager p0, final String p1);
    }
    
    public static class FileDescriptorFactory implements AssetFetcherFactory<ParcelFileDescriptor>, ModelLoaderFactory<Uri, ParcelFileDescriptor>
    {
        private final AssetManager assetManager;
        
        public FileDescriptorFactory(final AssetManager assetManager) {
            this.assetManager = assetManager;
        }
        
        @Override
        public ModelLoader<Uri, ParcelFileDescriptor> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new AssetUriLoader<ParcelFileDescriptor>(this.assetManager, (AssetFetcherFactory<ParcelFileDescriptor>)this);
        }
        
        @Override
        public DataFetcher<ParcelFileDescriptor> buildFetcher(final AssetManager assetManager, final String s) {
            return new FileDescriptorAssetPathFetcher(assetManager, s);
        }
    }
    
    public static class StreamFactory implements AssetFetcherFactory<InputStream>, ModelLoaderFactory<Uri, InputStream>
    {
        private final AssetManager assetManager;
        
        public StreamFactory(final AssetManager assetManager) {
            this.assetManager = assetManager;
        }
        
        @Override
        public ModelLoader<Uri, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new AssetUriLoader<InputStream>(this.assetManager, (AssetFetcherFactory<InputStream>)this);
        }
        
        @Override
        public DataFetcher<InputStream> buildFetcher(final AssetManager assetManager, final String s) {
            return new StreamAssetPathFetcher(assetManager, s);
        }
    }
}
