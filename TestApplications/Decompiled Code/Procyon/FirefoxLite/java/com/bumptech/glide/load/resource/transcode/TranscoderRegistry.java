// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.transcode;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class TranscoderRegistry
{
    private final List<Entry<?, ?>> transcoders;
    
    public TranscoderRegistry() {
        this.transcoders = new ArrayList<Entry<?, ?>>();
    }
    
    public <Z, R> ResourceTranscoder<Z, R> get(final Class<Z> obj, final Class<R> obj2) {
        synchronized (this) {
            if (obj2.isAssignableFrom(obj)) {
                return (ResourceTranscoder<Z, R>)UnitTranscoder.get();
            }
            for (final Entry<?, ?> entry : this.transcoders) {
                if (entry.handles(obj, obj2)) {
                    return (ResourceTranscoder<Z, R>)entry.transcoder;
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("No transcoder registered to transcode from ");
            sb.append(obj);
            sb.append(" to ");
            sb.append(obj2);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public <Z, R> List<Class<R>> getTranscodeClasses(final Class<Z> clazz, final Class<R> clazz2) {
        synchronized (this) {
            final ArrayList<Class<R>> list = new ArrayList<Class<R>>();
            if (clazz2.isAssignableFrom(clazz)) {
                list.add(clazz2);
                return list;
            }
            final Iterator<Entry<?, ?>> iterator = this.transcoders.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().handles(clazz, clazz2)) {
                    list.add(clazz2);
                }
            }
            return list;
        }
    }
    
    public <Z, R> void register(final Class<Z> clazz, final Class<R> clazz2, final ResourceTranscoder<Z, R> resourceTranscoder) {
        synchronized (this) {
            this.transcoders.add(new Entry<Object, Object>((Class<Object>)clazz, (Class<Object>)clazz2, (ResourceTranscoder<Object, Object>)resourceTranscoder));
        }
    }
    
    private static final class Entry<Z, R>
    {
        private final Class<Z> fromClass;
        private final Class<R> toClass;
        final ResourceTranscoder<Z, R> transcoder;
        
        Entry(final Class<Z> fromClass, final Class<R> toClass, final ResourceTranscoder<Z, R> transcoder) {
            this.fromClass = fromClass;
            this.toClass = toClass;
            this.transcoder = transcoder;
        }
        
        public boolean handles(final Class<?> clazz, final Class<?> clazz2) {
            return this.fromClass.isAssignableFrom(clazz) && clazz2.isAssignableFrom(this.toClass);
        }
    }
}
