// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.Resource;

public interface ResourceEncoder<T> extends Encoder<Resource<T>>
{
    EncodeStrategy getEncodeStrategy(final Options p0);
}
