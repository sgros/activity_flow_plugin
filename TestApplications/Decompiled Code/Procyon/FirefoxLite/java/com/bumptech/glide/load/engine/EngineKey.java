// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import java.security.MessageDigest;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.load.Transformation;
import java.util.Map;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Key;

class EngineKey implements Key
{
    private int hashCode;
    private final int height;
    private final Object model;
    private final Options options;
    private final Class<?> resourceClass;
    private final Key signature;
    private final Class<?> transcodeClass;
    private final Map<Class<?>, Transformation<?>> transformations;
    private final int width;
    
    public EngineKey(final Object o, final Key key, final int width, final int height, final Map<Class<?>, Transformation<?>> map, final Class<?> clazz, final Class<?> clazz2, final Options options) {
        this.model = Preconditions.checkNotNull(o);
        this.signature = Preconditions.checkNotNull(key, "Signature must not be null");
        this.width = width;
        this.height = height;
        this.transformations = Preconditions.checkNotNull(map);
        this.resourceClass = Preconditions.checkNotNull(clazz, "Resource class must not be null");
        this.transcodeClass = Preconditions.checkNotNull(clazz2, "Transcode class must not be null");
        this.options = Preconditions.checkNotNull(options);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof EngineKey;
        final boolean b2 = false;
        if (b) {
            final EngineKey engineKey = (EngineKey)o;
            boolean b3 = b2;
            if (this.model.equals(engineKey.model)) {
                b3 = b2;
                if (this.signature.equals(engineKey.signature)) {
                    b3 = b2;
                    if (this.height == engineKey.height) {
                        b3 = b2;
                        if (this.width == engineKey.width) {
                            b3 = b2;
                            if (this.transformations.equals(engineKey.transformations)) {
                                b3 = b2;
                                if (this.resourceClass.equals(engineKey.resourceClass)) {
                                    b3 = b2;
                                    if (this.transcodeClass.equals(engineKey.transcodeClass)) {
                                        b3 = b2;
                                        if (this.options.equals(engineKey.options)) {
                                            b3 = true;
                                        }
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
        if (this.hashCode == 0) {
            this.hashCode = this.model.hashCode();
            this.hashCode = this.hashCode * 31 + this.signature.hashCode();
            this.hashCode = this.hashCode * 31 + this.width;
            this.hashCode = this.hashCode * 31 + this.height;
            this.hashCode = this.hashCode * 31 + this.transformations.hashCode();
            this.hashCode = this.hashCode * 31 + this.resourceClass.hashCode();
            this.hashCode = this.hashCode * 31 + this.transcodeClass.hashCode();
            this.hashCode = this.hashCode * 31 + this.options.hashCode();
        }
        return this.hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("EngineKey{model=");
        sb.append(this.model);
        sb.append(", width=");
        sb.append(this.width);
        sb.append(", height=");
        sb.append(this.height);
        sb.append(", resourceClass=");
        sb.append(this.resourceClass);
        sb.append(", transcodeClass=");
        sb.append(this.transcodeClass);
        sb.append(", signature=");
        sb.append(this.signature);
        sb.append(", hashCode=");
        sb.append(this.hashCode);
        sb.append(", transformations=");
        sb.append(this.transformations);
        sb.append(", options=");
        sb.append(this.options);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        throw new UnsupportedOperationException();
    }
}
