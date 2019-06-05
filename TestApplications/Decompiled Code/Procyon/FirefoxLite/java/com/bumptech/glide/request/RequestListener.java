// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.load.engine.GlideException;

public interface RequestListener<R>
{
    boolean onLoadFailed(final GlideException p0, final Object p1, final Target<R> p2, final boolean p3);
    
    boolean onResourceReady(final R p0, final Object p1, final Target<R> p2, final DataSource p3, final boolean p4);
}
