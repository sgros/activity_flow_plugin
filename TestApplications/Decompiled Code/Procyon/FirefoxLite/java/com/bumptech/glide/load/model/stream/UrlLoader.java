// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import java.io.InputStream;
import java.net.URL;
import com.bumptech.glide.load.model.ModelLoader;

public class UrlLoader implements ModelLoader<URL, InputStream>
{
    private final ModelLoader<GlideUrl, InputStream> glideUrlLoader;
    
    public UrlLoader(final ModelLoader<GlideUrl, InputStream> glideUrlLoader) {
        this.glideUrlLoader = glideUrlLoader;
    }
    
    public LoadData<InputStream> buildLoadData(final URL url, final int n, final int n2, final Options options) {
        return this.glideUrlLoader.buildLoadData(new GlideUrl(url), n, n2, options);
    }
    
    @Override
    public boolean handles(final URL url) {
        return true;
    }
    
    public static class StreamFactory implements ModelLoaderFactory<URL, InputStream>
    {
        @Override
        public ModelLoader<URL, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UrlLoader(multiModelLoaderFactory.build(GlideUrl.class, InputStream.class));
        }
    }
}
