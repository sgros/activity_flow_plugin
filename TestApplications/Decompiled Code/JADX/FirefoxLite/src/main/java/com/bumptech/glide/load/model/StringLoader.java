package com.bumptech.glide.load.model;

import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import java.io.File;
import java.io.InputStream;

public class StringLoader<Data> implements ModelLoader<String, Data> {
    private final ModelLoader<Uri, Data> uriLoader;

    public static class FileDescriptorFactory implements ModelLoaderFactory<String, ParcelFileDescriptor> {
        public ModelLoader<String, ParcelFileDescriptor> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new StringLoader(multiModelLoaderFactory.build(Uri.class, ParcelFileDescriptor.class));
        }
    }

    public static class StreamFactory implements ModelLoaderFactory<String, InputStream> {
        public ModelLoader<String, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new StringLoader(multiModelLoaderFactory.build(Uri.class, InputStream.class));
        }
    }

    public boolean handles(String str) {
        return true;
    }

    public StringLoader(ModelLoader<Uri, Data> modelLoader) {
        this.uriLoader = modelLoader;
    }

    public LoadData<Data> buildLoadData(String str, int i, int i2, Options options) {
        Uri parseUri = parseUri(str);
        if (parseUri == null) {
            return null;
        }
        return this.uriLoader.buildLoadData(parseUri, i, i2, options);
    }

    private static Uri parseUri(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Uri toFileUri;
        if (str.startsWith("/")) {
            toFileUri = toFileUri(str);
        } else {
            Uri parse = Uri.parse(str);
            toFileUri = parse.getScheme() == null ? toFileUri(str) : parse;
        }
        return toFileUri;
    }

    private static Uri toFileUri(String str) {
        return Uri.fromFile(new File(str));
    }
}
