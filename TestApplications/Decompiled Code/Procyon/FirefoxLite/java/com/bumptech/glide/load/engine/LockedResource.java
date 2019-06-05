// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import com.bumptech.glide.util.pool.StateVerifier;
import android.support.v4.util.Pools;
import com.bumptech.glide.util.pool.FactoryPools;

final class LockedResource<Z> implements Resource<Z>, Poolable
{
    private static final Pools.Pool<LockedResource<?>> POOL;
    private boolean isLocked;
    private boolean isRecycled;
    private final StateVerifier stateVerifier;
    private Resource<Z> toWrap;
    
    static {
        POOL = FactoryPools.threadSafe(20, (FactoryPools.Factory<LockedResource<?>>)new Factory<LockedResource<?>>() {
            public LockedResource<?> create() {
                return new LockedResource<Object>();
            }
        });
    }
    
    LockedResource() {
        this.stateVerifier = StateVerifier.newInstance();
    }
    
    private void init(final Resource<Z> toWrap) {
        this.isRecycled = false;
        this.isLocked = true;
        this.toWrap = toWrap;
    }
    
    static <Z> LockedResource<Z> obtain(final Resource<Z> resource) {
        final LockedResource<?> lockedResource = LockedResource.POOL.acquire();
        lockedResource.init(resource);
        return (LockedResource<Z>)lockedResource;
    }
    
    private void release() {
        this.toWrap = null;
        LockedResource.POOL.release(this);
    }
    
    @Override
    public Z get() {
        return this.toWrap.get();
    }
    
    @Override
    public Class<Z> getResourceClass() {
        return this.toWrap.getResourceClass();
    }
    
    @Override
    public int getSize() {
        return this.toWrap.getSize();
    }
    
    @Override
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }
    
    @Override
    public void recycle() {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            this.isRecycled = true;
            if (!this.isLocked) {
                this.toWrap.recycle();
                this.release();
            }
        }
    }
    
    public void unlock() {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            if (this.isLocked) {
                this.isLocked = false;
                if (this.isRecycled) {
                    this.recycle();
                }
                return;
            }
            throw new IllegalStateException("Already unlocked");
        }
    }
}
