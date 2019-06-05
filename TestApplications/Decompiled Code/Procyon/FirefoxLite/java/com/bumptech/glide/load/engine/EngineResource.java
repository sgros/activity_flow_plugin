// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import android.os.Looper;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.load.Key;

class EngineResource<Z> implements Resource<Z>
{
    private int acquired;
    private final boolean isCacheable;
    private boolean isRecycled;
    private Key key;
    private ResourceListener listener;
    private final Resource<Z> resource;
    
    EngineResource(final Resource<Z> resource, final boolean isCacheable) {
        this.resource = Preconditions.checkNotNull(resource);
        this.isCacheable = isCacheable;
    }
    
    void acquire() {
        if (this.isRecycled) {
            throw new IllegalStateException("Cannot acquire a recycled resource");
        }
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            ++this.acquired;
            return;
        }
        throw new IllegalThreadStateException("Must call acquire on the main thread");
    }
    
    @Override
    public Z get() {
        return this.resource.get();
    }
    
    @Override
    public Class<Z> getResourceClass() {
        return this.resource.getResourceClass();
    }
    
    @Override
    public int getSize() {
        return this.resource.getSize();
    }
    
    boolean isCacheable() {
        return this.isCacheable;
    }
    
    @Override
    public void recycle() {
        if (this.acquired > 0) {
            throw new IllegalStateException("Cannot recycle a resource while it is still acquired");
        }
        if (!this.isRecycled) {
            this.isRecycled = true;
            this.resource.recycle();
            return;
        }
        throw new IllegalStateException("Cannot recycle a resource that has already been recycled");
    }
    
    void release() {
        if (this.acquired <= 0) {
            throw new IllegalStateException("Cannot release a recycled or not yet acquired resource");
        }
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            if (--this.acquired == 0) {
                this.listener.onResourceReleased(this.key, this);
            }
            return;
        }
        throw new IllegalThreadStateException("Must call release on the main thread");
    }
    
    void setResourceListener(final Key key, final ResourceListener listener) {
        this.key = key;
        this.listener = listener;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("EngineResource{isCacheable=");
        sb.append(this.isCacheable);
        sb.append(", listener=");
        sb.append(this.listener);
        sb.append(", key=");
        sb.append(this.key);
        sb.append(", acquired=");
        sb.append(this.acquired);
        sb.append(", isRecycled=");
        sb.append(this.isRecycled);
        sb.append(", resource=");
        sb.append(this.resource);
        sb.append('}');
        return sb.toString();
    }
    
    interface ResourceListener
    {
        void onResourceReleased(final Key p0, final EngineResource<?> p1);
    }
}
