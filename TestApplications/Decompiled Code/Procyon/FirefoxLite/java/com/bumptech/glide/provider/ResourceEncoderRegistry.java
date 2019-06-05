// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResourceEncoderRegistry
{
    final List<Entry<?>> encoders;
    
    public ResourceEncoderRegistry() {
        this.encoders = new ArrayList<Entry<?>>();
    }
    
    public <Z> void append(final Class<Z> clazz, final ResourceEncoder<Z> resourceEncoder) {
        synchronized (this) {
            this.encoders.add(new Entry<Object>((Class<Object>)clazz, (ResourceEncoder<Object>)resourceEncoder));
        }
    }
    
    public <Z> ResourceEncoder<Z> get(final Class<Z> clazz) {
        synchronized (this) {
            for (int size = this.encoders.size(), i = 0; i < size; ++i) {
                final Entry<?> entry = this.encoders.get(i);
                if (entry.handles(clazz)) {
                    return (ResourceEncoder<Z>)entry.encoder;
                }
            }
            return null;
        }
    }
    
    private static final class Entry<T>
    {
        final ResourceEncoder<T> encoder;
        private final Class<T> resourceClass;
        
        Entry(final Class<T> resourceClass, final ResourceEncoder<T> encoder) {
            this.resourceClass = resourceClass;
            this.encoder = encoder;
        }
        
        boolean handles(final Class<?> clazz) {
            return this.resourceClass.isAssignableFrom(clazz);
        }
    }
}
