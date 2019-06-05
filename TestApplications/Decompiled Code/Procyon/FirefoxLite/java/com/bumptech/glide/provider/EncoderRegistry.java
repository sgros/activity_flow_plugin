// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.provider;

import java.util.Iterator;
import com.bumptech.glide.load.Encoder;
import java.util.ArrayList;
import java.util.List;

public class EncoderRegistry
{
    private final List<Entry<?>> encoders;
    
    public EncoderRegistry() {
        this.encoders = new ArrayList<Entry<?>>();
    }
    
    public <T> void append(final Class<T> clazz, final Encoder<T> encoder) {
        synchronized (this) {
            this.encoders.add(new Entry<Object>((Class<Object>)clazz, (Encoder<Object>)encoder));
        }
    }
    
    public <T> Encoder<T> getEncoder(final Class<T> clazz) {
        synchronized (this) {
            for (final Entry<?> entry : this.encoders) {
                if (entry.handles(clazz)) {
                    return (Encoder<T>)entry.encoder;
                }
            }
            return null;
        }
    }
    
    private static final class Entry<T>
    {
        private final Class<T> dataClass;
        final Encoder<T> encoder;
        
        public Entry(final Class<T> dataClass, final Encoder<T> encoder) {
            this.dataClass = dataClass;
            this.encoder = encoder;
        }
        
        public boolean handles(final Class<?> clazz) {
            return this.dataClass.isAssignableFrom(clazz);
        }
    }
}
