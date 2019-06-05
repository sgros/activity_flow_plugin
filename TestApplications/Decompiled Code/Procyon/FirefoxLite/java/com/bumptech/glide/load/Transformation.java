// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.Resource;
import android.content.Context;

public interface Transformation<T> extends Key
{
    boolean equals(final Object p0);
    
    int hashCode();
    
    Resource<T> transform(final Context p0, final Resource<T> p1, final int p2, final int p3);
}
