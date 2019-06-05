package com.bumptech.glide.load.model;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorAssetPathFetcher;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;

public class AssetUriLoader<Data> implements ModelLoader<Uri, Data> {
    private static final int ASSET_PREFIX_LENGTH = "file:///android_asset/".length();
    private final AssetManager assetManager;
    private final AssetFetcherFactory<Data> factory;

    public interface AssetFetcherFactory<Data> {
        DataFetcher<Data> buildFetcher(AssetManager assetManager, String str);
    }

    public static class FileDescriptorFactory implements AssetFetcherFactory<ParcelFileDescriptor>, ModelLoaderFactory<Uri, ParcelFileDescriptor> {
        private final AssetManager assetManager;

        public FileDescriptorFactory(AssetManager assetManager) {
            this.assetManager = assetManager;
        }

        public ModelLoader<Uri, ParcelFileDescriptor> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new AssetUriLoader(this.assetManager, this);
        }

        public DataFetcher<ParcelFileDescriptor> buildFetcher(AssetManager assetManager, String str) {
            return new FileDescriptorAssetPathFetcher(assetManager, str);
        }
    }

    public static class StreamFactory implements AssetFetcherFactory<InputStream>, ModelLoaderFactory<Uri, InputStream> {
        private final AssetManager assetManager;

        public StreamFactory(AssetManager assetManager) {
            this.assetManager = assetManager;
        }

        public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new AssetUriLoader(this.assetManager, this);
        }

        public DataFetcher<InputStream> buildFetcher(AssetManager assetManager, String str) {
            return new StreamAssetPathFetcher(assetManager, str);
        }
    }

    public AssetUriLoader(AssetManager assetManager, AssetFetcherFactory<Data> assetFetcherFactory) {
        this.assetManager = assetManager;
        this.factory = assetFetcherFactory;
    }

    public LoadData<Data> buildLoadData(Uri uri, int i, int i2, Options options) {
        return new LoadData(new ObjectKey(uri), this.factory.buildFetcher(this.assetManager, uri.toString().substring(ASSET_PREFIX_LENGTH)));
    }

    public boolean handles(Uri uri) {
        if ("file".equals(uri.getScheme()) && !uri.getPathSegments().isEmpty() && "android_asset".equals(uri.getPathSegments().get(0))) {
            return true;
        }
        return false;
    }
}
