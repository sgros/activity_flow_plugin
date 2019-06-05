// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.io.InputStream;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Options;
import java.io.File;
import android.text.TextUtils;
import android.net.Uri;

public class StringLoader<Data> implements ModelLoader<String, Data>
{
    private final ModelLoader<Uri, Data> uriLoader;
    
    public StringLoader(final ModelLoader<Uri, Data> uriLoader) {
        this.uriLoader = uriLoader;
    }
    
    private static Uri parseUri(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return null;
        }
        Uri uri;
        if (s.startsWith("/")) {
            uri = toFileUri(s);
        }
        else {
            final Uri parse = Uri.parse(s);
            if (parse.getScheme() == null) {
                uri = toFileUri(s);
            }
            else {
                uri = parse;
            }
        }
        return uri;
    }
    
    private static Uri toFileUri(final String pathname) {
        return Uri.fromFile(new File(pathname));
    }
    
    public LoadData<Data> buildLoadData(final String s, final int n, final int n2, final Options options) {
        final Uri uri = parseUri(s);
        Object buildLoadData;
        if (uri == null) {
            buildLoadData = null;
        }
        else {
            buildLoadData = this.uriLoader.buildLoadData(uri, n, n2, options);
        }
        return (LoadData<Data>)buildLoadData;
    }
    
    @Override
    public boolean handles(final String s) {
        return true;
    }
    
    public static class FileDescriptorFactory implements ModelLoaderFactory<String, ParcelFileDescriptor>
    {
        @Override
        public ModelLoader<String, ParcelFileDescriptor> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new StringLoader<ParcelFileDescriptor>((ModelLoader<Uri, ParcelFileDescriptor>)multiModelLoaderFactory.build(Uri.class, (Class<Data>)ParcelFileDescriptor.class));
        }
    }
    
    public static class StreamFactory implements ModelLoaderFactory<String, InputStream>
    {
        @Override
        public ModelLoader<String, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new StringLoader<InputStream>((ModelLoader<Uri, InputStream>)multiModelLoaderFactory.build(Uri.class, (Class<Data>)InputStream.class));
        }
    }
}
