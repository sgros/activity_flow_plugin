// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.signature;

import java.security.MessageDigest;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.load.Key;

public final class ObjectKey implements Key
{
    private final Object object;
    
    public ObjectKey(final Object o) {
        this.object = Preconditions.checkNotNull(o);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof ObjectKey && this.object.equals(((ObjectKey)o).object);
    }
    
    @Override
    public int hashCode() {
        return this.object.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ObjectKey{object=");
        sb.append(this.object);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        messageDigest.update(this.object.toString().getBytes(ObjectKey.CHARSET));
    }
}
