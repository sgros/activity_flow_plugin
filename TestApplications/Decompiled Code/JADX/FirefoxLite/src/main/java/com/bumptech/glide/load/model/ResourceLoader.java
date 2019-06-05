package com.bumptech.glide.load.model;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import java.io.InputStream;

public class ResourceLoader<Data> implements ModelLoader<Integer, Data> {
    private final Resources resources;
    private final ModelLoader<Uri, Data> uriLoader;

    public static class FileDescriptorFactory implements ModelLoaderFactory<Integer, ParcelFileDescriptor> {
        private final Resources resources;

        public FileDescriptorFactory(Resources resources) {
            this.resources = resources;
        }

        public ModelLoader<Integer, ParcelFileDescriptor> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader(this.resources, multiModelLoaderFactory.build(Uri.class, ParcelFileDescriptor.class));
        }
    }

    public static class StreamFactory implements ModelLoaderFactory<Integer, InputStream> {
        private final Resources resources;

        public StreamFactory(Resources resources) {
            this.resources = resources;
        }

        public ModelLoader<Integer, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader(this.resources, multiModelLoaderFactory.build(Uri.class, InputStream.class));
        }
    }

    public boolean handles(Integer num) {
        return true;
    }

    public ResourceLoader(Resources resources, ModelLoader<Uri, Data> modelLoader) {
        this.resources = resources;
        this.uriLoader = modelLoader;
    }

    public LoadData<Data> buildLoadData(Integer num, int i, int i2, Options options) {
        Uri resourceUri = getResourceUri(num);
        if (resourceUri == null) {
            return null;
        }
        return this.uriLoader.buildLoadData(resourceUri, i, i2, options);
    }

    private Uri getResourceUri(Integer num) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("android.resource://");
            stringBuilder.append(this.resources.getResourcePackageName(num.intValue()));
            stringBuilder.append('/');
            stringBuilder.append(this.resources.getResourceTypeName(num.intValue()));
            stringBuilder.append('/');
            stringBuilder.append(this.resources.getResourceEntryName(num.intValue()));
            return Uri.parse(stringBuilder.toString());
        } catch (NotFoundException e) {
            if (Log.isLoggable("ResourceLoader", 5)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Received invalid resource id: ");
                stringBuilder2.append(num);
                Log.w("ResourceLoader", stringBuilder2.toString(), e);
            }
            return null;
        }
    }
}
