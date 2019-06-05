// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import java.io.File;

public interface Encoder<T>
{
    boolean encode(final T p0, final File p1, final Options p2);
}
