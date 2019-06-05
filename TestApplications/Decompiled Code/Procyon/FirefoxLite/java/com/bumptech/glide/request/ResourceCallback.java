// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.GlideException;

public interface ResourceCallback
{
    void onLoadFailed(final GlideException p0);
    
    void onResourceReady(final Resource<?> p0, final DataSource p1);
}
