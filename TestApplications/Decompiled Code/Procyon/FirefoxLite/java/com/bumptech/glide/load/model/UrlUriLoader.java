// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.io.InputStream;
import com.bumptech.glide.load.Options;
import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;
import android.net.Uri;

public class UrlUriLoader<Data> implements ModelLoader<Uri, Data>
{
    private static final Set<String> SCHEMES;
    private final ModelLoader<GlideUrl, Data> urlLoader;
    
    static {
        SCHEMES = Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(Arrays.asList("http", "https")));
    }
    
    public UrlUriLoader(final ModelLoader<GlideUrl, Data> urlLoader) {
        this.urlLoader = urlLoader;
    }
    
    public LoadData<Data> buildLoadData(final Uri uri, final int n, final int n2, final Options options) {
        return this.urlLoader.buildLoadData(new GlideUrl(uri.toString()), n, n2, options);
    }
    
    @Override
    public boolean handles(final Uri uri) {
        return UrlUriLoader.SCHEMES.contains(uri.getScheme());
    }
    
    public static class StreamFactory implements ModelLoaderFactory<Uri, InputStream>
    {
        @Override
        public ModelLoader<Uri, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UrlUriLoader<InputStream>((ModelLoader<GlideUrl, InputStream>)multiModelLoaderFactory.build(GlideUrl.class, (Class<Data>)InputStream.class));
        }
    }
}
