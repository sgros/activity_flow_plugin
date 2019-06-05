// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import java.security.MessageDigest;
import com.bumptech.glide.load.Key;

final class DataCacheKey implements Key
{
    private final Key signature;
    private final Key sourceKey;
    
    public DataCacheKey(final Key sourceKey, final Key signature) {
        this.sourceKey = sourceKey;
        this.signature = signature;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DataCacheKey;
        final boolean b2 = false;
        if (b) {
            final DataCacheKey dataCacheKey = (DataCacheKey)o;
            boolean b3 = b2;
            if (this.sourceKey.equals(dataCacheKey.sourceKey)) {
                b3 = b2;
                if (this.signature.equals(dataCacheKey.signature)) {
                    b3 = true;
                }
            }
            return b3;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.sourceKey.hashCode() * 31 + this.signature.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DataCacheKey{sourceKey=");
        sb.append(this.sourceKey);
        sb.append(", signature=");
        sb.append(this.signature);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        this.sourceKey.updateDiskCacheKey(messageDigest);
        this.signature.updateDiskCacheKey(messageDigest);
    }
}
