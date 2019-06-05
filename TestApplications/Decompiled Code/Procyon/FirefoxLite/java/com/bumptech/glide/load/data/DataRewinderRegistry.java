// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import java.util.Iterator;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Map;

public class DataRewinderRegistry
{
    private static final DataRewinder.Factory<?> DEFAULT_FACTORY;
    private final Map<Class<?>, DataRewinder.Factory<?>> rewinders;
    
    static {
        DEFAULT_FACTORY = new DataRewinder.Factory<Object>() {
            @Override
            public DataRewinder<Object> build(final Object o) {
                return new DefaultRewinder(o);
            }
            
            @Override
            public Class<Object> getDataClass() {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
    }
    
    public DataRewinderRegistry() {
        this.rewinders = new HashMap<Class<?>, DataRewinder.Factory<?>>();
    }
    
    public <T> DataRewinder<T> build(final T t) {
        synchronized (this) {
            Preconditions.checkNotNull(t);
            DataRewinder.Factory<T> factory2;
            final DataRewinder.Factory factory = (DataRewinder.Factory)(factory2 = (DataRewinder.Factory<T>)this.rewinders.get(t.getClass()));
            if (factory == null) {
                final Iterator<DataRewinder.Factory<?>> iterator = this.rewinders.values().iterator();
                do {
                    factory2 = (DataRewinder.Factory<T>)factory;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    factory2 = iterator.next();
                } while (!factory2.getDataClass().isAssignableFrom(t.getClass()));
            }
            Object default_FACTORY;
            if ((default_FACTORY = factory2) == null) {
                default_FACTORY = DataRewinderRegistry.DEFAULT_FACTORY;
            }
            return ((DataRewinder.Factory<T>)default_FACTORY).build(t);
        }
    }
    
    public void register(final DataRewinder.Factory<?> factory) {
        synchronized (this) {
            this.rewinders.put(factory.getDataClass(), factory);
        }
    }
    
    private static class DefaultRewinder implements DataRewinder<Object>
    {
        private final Object data;
        
        public DefaultRewinder(final Object data) {
            this.data = data;
        }
        
        @Override
        public void cleanup() {
        }
        
        @Override
        public Object rewindAndGet() {
            return this.data;
        }
    }
}
