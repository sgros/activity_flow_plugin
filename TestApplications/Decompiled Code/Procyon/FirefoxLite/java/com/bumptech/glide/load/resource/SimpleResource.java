// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource;

import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.load.engine.Resource;

public class SimpleResource<T> implements Resource<T>
{
    protected final T data;
    
    public SimpleResource(final T t) {
        this.data = Preconditions.checkNotNull(t);
    }
    
    @Override
    public final T get() {
        return this.data;
    }
    
    @Override
    public Class<T> getResourceClass() {
        return (Class<T>)this.data.getClass();
    }
    
    @Override
    public final int getSize() {
        return 1;
    }
    
    @Override
    public void recycle() {
    }
}
