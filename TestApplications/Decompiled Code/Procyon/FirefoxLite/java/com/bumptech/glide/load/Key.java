// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import java.security.MessageDigest;
import java.nio.charset.Charset;

public interface Key
{
    public static final Charset CHARSET = Charset.forName("UTF-8");
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    void updateDiskCacheKey(final MessageDigest p0);
}
