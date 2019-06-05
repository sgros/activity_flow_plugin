// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.util.pool;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import android.support.v4.util.Pools;

public final class FactoryPools
{
    private static final Resetter<Object> EMPTY_RESETTER;
    
    static {
        EMPTY_RESETTER = (Resetter)new Resetter<Object>() {
            @Override
            public void reset(final Object o) {
            }
        };
    }
    
    private static <T extends Poolable> Pools.Pool<T> build(final Pools.Pool<T> pool, final Factory<T> factory) {
        return build(pool, factory, emptyResetter());
    }
    
    private static <T> Pools.Pool<T> build(final Pools.Pool<T> pool, final Factory<T> factory, final Resetter<T> resetter) {
        return new FactoryPool<T>(pool, factory, resetter);
    }
    
    private static <T> Resetter<T> emptyResetter() {
        return (Resetter<T>)FactoryPools.EMPTY_RESETTER;
    }
    
    public static <T extends Poolable> Pools.Pool<T> simple(final int n, final Factory<T> factory) {
        return build(new Pools.SimplePool<T>(n), factory);
    }
    
    public static <T extends Poolable> Pools.Pool<T> threadSafe(final int n, final Factory<T> factory) {
        return build(new Pools.SynchronizedPool<T>(n), factory);
    }
    
    public static <T> Pools.Pool<List<T>> threadSafeList() {
        return threadSafeList(20);
    }
    
    public static <T> Pools.Pool<List<T>> threadSafeList(final int n) {
        return build(new Pools.SynchronizedPool<List<T>>(n), (Factory<List<T>>)new Factory<List<T>>() {
            public List<T> create() {
                return new ArrayList<T>();
            }
        }, (Resetter<List<T>>)new Resetter<List<T>>() {
            public void reset(final List<T> list) {
                list.clear();
            }
        });
    }
    
    public interface Factory<T>
    {
        T create();
    }
    
    private static final class FactoryPool<T> implements Pool<T>
    {
        private final Factory<T> factory;
        private final Pool<T> pool;
        private final Resetter<T> resetter;
        
        FactoryPool(final Pool<T> pool, final Factory<T> factory, final Resetter<T> resetter) {
            this.pool = pool;
            this.factory = factory;
            this.resetter = resetter;
        }
        
        @Override
        public T acquire() {
            Object o;
            if ((o = this.pool.acquire()) == null) {
                final T t = (T)(o = this.factory.create());
                if (Log.isLoggable("FactoryPools", 2)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Created new ");
                    sb.append(((Poolable)t).getClass());
                    Log.v("FactoryPools", sb.toString());
                    o = t;
                }
            }
            if (o instanceof Poolable) {
                ((Poolable)o).getVerifier().setRecycled(false);
            }
            return (T)o;
        }
        
        @Override
        public boolean release(final T t) {
            if (t instanceof Poolable) {
                ((Poolable)t).getVerifier().setRecycled(true);
            }
            this.resetter.reset(t);
            return this.pool.release(t);
        }
    }
    
    public interface Poolable
    {
        StateVerifier getVerifier();
    }
    
    public interface Resetter<T>
    {
        void reset(final T p0);
    }
}
