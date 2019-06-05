package com.bumptech.glide.load.data;

import com.bumptech.glide.load.data.DataRewinder.Factory;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Map;

public class DataRewinderRegistry {
    private static final Factory<?> DEFAULT_FACTORY = new C03901();
    private final Map<Class<?>, Factory<?>> rewinders = new HashMap();

    /* renamed from: com.bumptech.glide.load.data.DataRewinderRegistry$1 */
    static class C03901 implements Factory<Object> {
        C03901() {
        }

        public DataRewinder<Object> build(Object obj) {
            return new DefaultRewinder(obj);
        }

        public Class<Object> getDataClass() {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    private static class DefaultRewinder implements DataRewinder<Object> {
        private final Object data;

        public void cleanup() {
        }

        public DefaultRewinder(Object obj) {
            this.data = obj;
        }

        public Object rewindAndGet() {
            return this.data;
        }
    }

    public synchronized void register(Factory<?> factory) {
        this.rewinders.put(factory.getDataClass(), factory);
    }

    public synchronized <T> DataRewinder<T> build(T t) {
        Factory factory;
        Preconditions.checkNotNull(t);
        factory = (Factory) this.rewinders.get(t.getClass());
        if (factory == null) {
            for (Factory factory2 : this.rewinders.values()) {
                if (factory2.getDataClass().isAssignableFrom(t.getClass())) {
                    factory = factory2;
                    break;
                }
            }
        }
        if (factory == null) {
            factory = DEFAULT_FACTORY;
        }
        return factory.build(t);
    }
}
