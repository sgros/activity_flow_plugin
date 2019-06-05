// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import java.security.MessageDigest;
import java.util.Iterator;
import com.bumptech.glide.load.engine.Resource;
import android.content.Context;
import java.util.Arrays;
import java.util.Collection;

public class MultiTransformation<T> implements Transformation<T>
{
    private final Collection<? extends Transformation<T>> transformations;
    
    @SafeVarargs
    public MultiTransformation(final Transformation<T>... a) {
        if (a.length >= 1) {
            this.transformations = Arrays.asList(a);
            return;
        }
        throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof MultiTransformation && this.transformations.equals(((MultiTransformation)o).transformations);
    }
    
    @Override
    public int hashCode() {
        return this.transformations.hashCode();
    }
    
    @Override
    public Resource<T> transform(final Context context, final Resource<T> obj, final int n, final int n2) {
        final Iterator<? extends Transformation<T>> iterator = this.transformations.iterator();
        Resource<T> resource = obj;
        while (iterator.hasNext()) {
            final Resource<T> transform = ((Transformation<T>)iterator.next()).transform(context, resource, n, n2);
            if (resource != null && !resource.equals(obj) && !resource.equals(transform)) {
                resource.recycle();
            }
            resource = transform;
        }
        return resource;
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        final Iterator<? extends Transformation<T>> iterator = this.transformations.iterator();
        while (iterator.hasNext()) {
            ((Transformation<?>)iterator.next()).updateDiskCacheKey(messageDigest);
        }
    }
}
