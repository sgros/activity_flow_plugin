// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.Option;
import java.io.InputStream;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;

public class HttpGlideUrlLoader implements ModelLoader<GlideUrl, InputStream>
{
    public static final Option<Integer> TIMEOUT;
    private final ModelCache<GlideUrl, GlideUrl> modelCache;
    
    static {
        TIMEOUT = Option.memory("com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.Timeout", 2500);
    }
    
    public HttpGlideUrlLoader() {
        this(null);
    }
    
    public HttpGlideUrlLoader(final ModelCache<GlideUrl, GlideUrl> modelCache) {
        this.modelCache = modelCache;
    }
    
    public LoadData<InputStream> buildLoadData(final GlideUrl glideUrl, final int n, final int n2, final Options options) {
        GlideUrl glideUrl2 = glideUrl;
        if (this.modelCache != null) {
            glideUrl2 = this.modelCache.get(glideUrl, 0, 0);
            if (glideUrl2 == null) {
                this.modelCache.put(glideUrl, 0, 0, glideUrl);
                glideUrl2 = glideUrl;
            }
        }
        return (LoadData<InputStream>)new LoadData(glideUrl2, (DataFetcher<Object>)new HttpUrlFetcher(glideUrl2, options.get(HttpGlideUrlLoader.TIMEOUT)));
    }
    
    @Override
    public boolean handles(final GlideUrl glideUrl) {
        return true;
    }
    
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream>
    {
        private final ModelCache<GlideUrl, GlideUrl> modelCache;
        
        public Factory() {
            this.modelCache = new ModelCache<GlideUrl, GlideUrl>(500);
        }
        
        @Override
        public ModelLoader<GlideUrl, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new HttpGlideUrlLoader(this.modelCache);
        }
    }
}
