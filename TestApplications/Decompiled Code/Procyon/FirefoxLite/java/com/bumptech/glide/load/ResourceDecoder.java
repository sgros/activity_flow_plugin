// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import java.io.IOException;
import com.bumptech.glide.load.engine.Resource;

public interface ResourceDecoder<T, Z>
{
    Resource<Z> decode(final T p0, final int p1, final int p2, final Options p3) throws IOException;
    
    boolean handles(final T p0, final Options p1) throws IOException;
}
