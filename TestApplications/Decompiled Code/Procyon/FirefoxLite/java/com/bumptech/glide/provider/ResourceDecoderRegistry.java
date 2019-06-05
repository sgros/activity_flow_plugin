// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.provider;

import java.util.Collection;
import java.util.Iterator;
import com.bumptech.glide.load.ResourceDecoder;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class ResourceDecoderRegistry
{
    private final List<String> bucketPriorityList;
    private final Map<String, List<Entry<?, ?>>> decoders;
    
    public ResourceDecoderRegistry() {
        this.bucketPriorityList = new ArrayList<String>();
        this.decoders = new HashMap<String, List<Entry<?, ?>>>();
    }
    
    private List<Entry<?, ?>> getOrAddEntryList(final String s) {
        synchronized (this) {
            if (!this.bucketPriorityList.contains(s)) {
                this.bucketPriorityList.add(s);
            }
            List<Entry<?, ?>> list;
            if ((list = this.decoders.get(s)) == null) {
                list = new ArrayList<Entry<?, ?>>();
                this.decoders.put(s, list);
            }
            return list;
        }
    }
    
    public <T, R> void append(final String s, final ResourceDecoder<T, R> resourceDecoder, final Class<T> clazz, final Class<R> clazz2) {
        synchronized (this) {
            this.getOrAddEntryList(s).add(new Entry<Object, Object>((Class<Object>)clazz, (Class<Object>)clazz2, (ResourceDecoder<Object, Object>)resourceDecoder));
        }
    }
    
    public <T, R> List<ResourceDecoder<T, R>> getDecoders(final Class<T> clazz, final Class<R> clazz2) {
        synchronized (this) {
            final ArrayList<ResourceDecoder<T, R>> list = new ArrayList<ResourceDecoder<T, R>>();
            final Iterator<String> iterator = this.bucketPriorityList.iterator();
            while (iterator.hasNext()) {
                final List<Entry<?, ?>> list2 = this.decoders.get(iterator.next());
                if (list2 == null) {
                    continue;
                }
                for (final Entry<?, ?> entry : list2) {
                    if (entry.handles(clazz, clazz2)) {
                        list.add((ResourceDecoder<T, R>)entry.decoder);
                    }
                }
            }
            return (List<ResourceDecoder<T, R>>)list;
        }
    }
    
    public <T, R> List<Class<R>> getResourceClasses(final Class<T> clazz, final Class<R> clazz2) {
        synchronized (this) {
            final ArrayList<Class<R>> list = new ArrayList<Class<R>>();
            final Iterator<String> iterator = this.bucketPriorityList.iterator();
            while (iterator.hasNext()) {
                final List<Entry<?, ?>> list2 = this.decoders.get(iterator.next());
                if (list2 == null) {
                    continue;
                }
                for (final Entry<?, ?> entry : list2) {
                    if (entry.handles(clazz, clazz2)) {
                        list.add((Class<R>)entry.resourceClass);
                    }
                }
            }
            return (List<Class<R>>)list;
        }
    }
    
    public <T, R> void prepend(final String s, final ResourceDecoder<T, R> resourceDecoder, final Class<T> clazz, final Class<R> clazz2) {
        synchronized (this) {
            this.getOrAddEntryList(s).add(0, new Entry<Object, Object>((Class<Object>)clazz, (Class<Object>)clazz2, (ResourceDecoder<Object, Object>)resourceDecoder));
        }
    }
    
    public void setBucketPriorityList(final List<String> list) {
        synchronized (this) {
            final ArrayList<String> list2 = new ArrayList<String>(this.bucketPriorityList);
            this.bucketPriorityList.clear();
            this.bucketPriorityList.addAll(list);
            for (final String s : list2) {
                if (!list.contains(s)) {
                    this.bucketPriorityList.add(s);
                }
            }
        }
    }
    
    private static class Entry<T, R>
    {
        private final Class<T> dataClass;
        final ResourceDecoder<T, R> decoder;
        final Class<R> resourceClass;
        
        public Entry(final Class<T> dataClass, final Class<R> resourceClass, final ResourceDecoder<T, R> decoder) {
            this.dataClass = dataClass;
            this.resourceClass = resourceClass;
            this.decoder = decoder;
        }
        
        public boolean handles(final Class<?> clazz, final Class<?> clazz2) {
            return this.dataClass.isAssignableFrom(clazz) && clazz2.isAssignableFrom(this.resourceClass);
        }
    }
}
