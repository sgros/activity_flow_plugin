package com.bumptech.glide.util.pool;

import android.support.p001v4.util.Pools.Pool;
import android.support.p001v4.util.Pools.SimplePool;
import android.support.p001v4.util.Pools.SynchronizedPool;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class FactoryPools {
    private static final Resetter<Object> EMPTY_RESETTER = new C04121();

    public interface Factory<T> {
        T create();
    }

    public interface Poolable {
        StateVerifier getVerifier();
    }

    public interface Resetter<T> {
        void reset(T t);
    }

    /* renamed from: com.bumptech.glide.util.pool.FactoryPools$1 */
    static class C04121 implements Resetter<Object> {
        public void reset(Object obj) {
        }

        C04121() {
        }
    }

    /* renamed from: com.bumptech.glide.util.pool.FactoryPools$2 */
    static class C04132 implements Factory<List<T>> {
        C04132() {
        }

        public List<T> create() {
            return new ArrayList();
        }
    }

    /* renamed from: com.bumptech.glide.util.pool.FactoryPools$3 */
    static class C04143 implements Resetter<List<T>> {
        C04143() {
        }

        public void reset(List<T> list) {
            list.clear();
        }
    }

    private static final class FactoryPool<T> implements Pool<T> {
        private final Factory<T> factory;
        private final Pool<T> pool;
        private final Resetter<T> resetter;

        FactoryPool(Pool<T> pool, Factory<T> factory, Resetter<T> resetter) {
            this.pool = pool;
            this.factory = factory;
            this.resetter = resetter;
        }

        public T acquire() {
            T acquire = this.pool.acquire();
            if (acquire == null) {
                acquire = this.factory.create();
                if (Log.isLoggable("FactoryPools", 2)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Created new ");
                    stringBuilder.append(acquire.getClass());
                    Log.v("FactoryPools", stringBuilder.toString());
                }
            }
            if (acquire instanceof Poolable) {
                ((Poolable) acquire).getVerifier().setRecycled(false);
            }
            return acquire;
        }

        public boolean release(T t) {
            if (t instanceof Poolable) {
                ((Poolable) t).getVerifier().setRecycled(true);
            }
            this.resetter.reset(t);
            return this.pool.release(t);
        }
    }

    public static <T extends Poolable> Pool<T> simple(int i, Factory<T> factory) {
        return build(new SimplePool(i), factory);
    }

    public static <T extends Poolable> Pool<T> threadSafe(int i, Factory<T> factory) {
        return build(new SynchronizedPool(i), factory);
    }

    public static <T> Pool<List<T>> threadSafeList() {
        return threadSafeList(20);
    }

    public static <T> Pool<List<T>> threadSafeList(int i) {
        return build(new SynchronizedPool(i), new C04132(), new C04143());
    }

    private static <T extends Poolable> Pool<T> build(Pool<T> pool, Factory<T> factory) {
        return build(pool, factory, emptyResetter());
    }

    private static <T> Pool<T> build(Pool<T> pool, Factory<T> factory, Resetter<T> resetter) {
        return new FactoryPool(pool, factory, resetter);
    }

    private static <T> Resetter<T> emptyResetter() {
        return EMPTY_RESETTER;
    }
}
