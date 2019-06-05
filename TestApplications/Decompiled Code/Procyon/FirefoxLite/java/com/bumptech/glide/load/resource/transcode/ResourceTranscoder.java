// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;

public interface ResourceTranscoder<Z, R>
{
    Resource<R> transcode(final Resource<Z> p0, final Options p1);
}
