package com.bumptech.glide.load.engine;

import android.support.p001v4.util.Pools.Pool;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.FactoryPools.Factory;
import com.bumptech.glide.util.pool.FactoryPools.Poolable;
import com.bumptech.glide.util.pool.StateVerifier;

final class LockedResource<Z> implements Resource<Z>, Poolable {
    private static final Pool<LockedResource<?>> POOL = FactoryPools.threadSafe(20, new C06711());
    private boolean isLocked;
    private boolean isRecycled;
    private final StateVerifier stateVerifier = StateVerifier.newInstance();
    private Resource<Z> toWrap;

    /* renamed from: com.bumptech.glide.load.engine.LockedResource$1 */
    static class C06711 implements Factory<LockedResource<?>> {
        C06711() {
        }

        public LockedResource<?> create() {
            return new LockedResource();
        }
    }

    static <Z> LockedResource<Z> obtain(Resource<Z> resource) {
        LockedResource lockedResource = (LockedResource) POOL.acquire();
        lockedResource.init(resource);
        return lockedResource;
    }

    LockedResource() {
    }

    private void init(Resource<Z> resource) {
        this.isRecycled = false;
        this.isLocked = true;
        this.toWrap = resource;
    }

    private void release() {
        this.toWrap = null;
        POOL.release(this);
    }

    public synchronized void unlock() {
        this.stateVerifier.throwIfRecycled();
        if (this.isLocked) {
            this.isLocked = false;
            if (this.isRecycled) {
                recycle();
            }
        } else {
            throw new IllegalStateException("Already unlocked");
        }
    }

    public Class<Z> getResourceClass() {
        return this.toWrap.getResourceClass();
    }

    public Z get() {
        return this.toWrap.get();
    }

    public int getSize() {
        return this.toWrap.getSize();
    }

    public synchronized void recycle() {
        this.stateVerifier.throwIfRecycled();
        this.isRecycled = true;
        if (!this.isLocked) {
            this.toWrap.recycle();
            release();
        }
    }

    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }
}
