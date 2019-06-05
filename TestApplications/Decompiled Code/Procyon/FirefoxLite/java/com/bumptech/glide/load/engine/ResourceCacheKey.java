// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.load.Key;

final class ResourceCacheKey implements Key
{
    private static final LruCache<Class<?>, byte[]> RESOURCE_CLASS_BYTES;
    private final Class<?> decodedResourceClass;
    private final int height;
    private final Options options;
    private final Key signature;
    private final Key sourceKey;
    private final Transformation<?> transformation;
    private final int width;
    
    static {
        RESOURCE_CLASS_BYTES = new LruCache<Class<?>, byte[]>(50);
    }
    
    public ResourceCacheKey(final Key sourceKey, final Key signature, final int width, final int height, final Transformation<?> transformation, final Class<?> decodedResourceClass, final Options options) {
        this.sourceKey = sourceKey;
        this.signature = signature;
        this.width = width;
        this.height = height;
        this.transformation = transformation;
        this.decodedResourceClass = decodedResourceClass;
        this.options = options;
    }
    
    private byte[] getResourceClassBytes() {
        byte[] bytes;
        if ((bytes = ResourceCacheKey.RESOURCE_CLASS_BYTES.get(this.decodedResourceClass)) == null) {
            bytes = this.decodedResourceClass.getName().getBytes(ResourceCacheKey.CHARSET);
            ResourceCacheKey.RESOURCE_CLASS_BYTES.put(this.decodedResourceClass, bytes);
        }
        return bytes;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof ResourceCacheKey;
        final boolean b2 = false;
        if (b) {
            final ResourceCacheKey resourceCacheKey = (ResourceCacheKey)o;
            boolean b3 = b2;
            if (this.height == resourceCacheKey.height) {
                b3 = b2;
                if (this.width == resourceCacheKey.width) {
                    b3 = b2;
                    if (Util.bothNullOrEqual(this.transformation, resourceCacheKey.transformation)) {
                        b3 = b2;
                        if (this.decodedResourceClass.equals(resourceCacheKey.decodedResourceClass)) {
                            b3 = b2;
                            if (this.sourceKey.equals(resourceCacheKey.sourceKey)) {
                                b3 = b2;
                                if (this.signature.equals(resourceCacheKey.signature)) {
                                    b3 = b2;
                                    if (this.options.equals(resourceCacheKey.options)) {
                                        b3 = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return b3;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int n = ((this.sourceKey.hashCode() * 31 + this.signature.hashCode()) * 31 + this.width) * 31 + this.height;
        if (this.transformation != null) {
            n = n * 31 + this.transformation.hashCode();
        }
        return (n * 31 + this.decodedResourceClass.hashCode()) * 31 + this.options.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ResourceCacheKey{sourceKey=");
        sb.append(this.sourceKey);
        sb.append(", signature=");
        sb.append(this.signature);
        sb.append(", width=");
        sb.append(this.width);
        sb.append(", height=");
        sb.append(this.height);
        sb.append(", decodedResourceClass=");
        sb.append(this.decodedResourceClass);
        sb.append(", transformation='");
        sb.append(this.transformation);
        sb.append('\'');
        sb.append(", options=");
        sb.append(this.options);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        final byte[] array = ByteBuffer.allocate(8).putInt(this.width).putInt(this.height).array();
        this.signature.updateDiskCacheKey(messageDigest);
        this.sourceKey.updateDiskCacheKey(messageDigest);
        messageDigest.update(array);
        if (this.transformation != null) {
            this.transformation.updateDiskCacheKey(messageDigest);
        }
        this.options.updateDiskCacheKey(messageDigest);
        messageDigest.update(this.getResourceClassBytes());
    }
}
