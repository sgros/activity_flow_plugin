// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.Options;
import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import com.bumptech.glide.load.model.GlideUrl;
import java.util.Set;
import java.io.InputStream;
import android.net.Uri;
import com.bumptech.glide.load.model.ModelLoader;

public class HttpUriLoader implements ModelLoader<Uri, InputStream>
{
    private static final Set<String> SCHEMES;
    private final ModelLoader<GlideUrl, InputStream> urlLoader;
    
    static {
        SCHEMES = Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(Arrays.asList("http", "https")));
    }
    
    public HttpUriLoader(final ModelLoader<GlideUrl, InputStream> urlLoader) {
        this.urlLoader = urlLoader;
    }
    
    public LoadData<InputStream> buildLoadData(final Uri uri, final int n, final int n2, final Options options) {
        return this.urlLoader.buildLoadData(new GlideUrl(uri.toString()), n, n2, options);
    }
    
    @Override
    public boolean handles(final Uri uri) {
        return HttpUriLoader.SCHEMES.contains(uri.getScheme());
    }
    
    public static class Factory implements ModelLoaderFactory<Uri, InputStream>
    {
        @Override
        public ModelLoader<Uri, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new HttpUriLoader(multiModelLoaderFactory.build(GlideUrl.class, InputStream.class));
        }
    }
}
