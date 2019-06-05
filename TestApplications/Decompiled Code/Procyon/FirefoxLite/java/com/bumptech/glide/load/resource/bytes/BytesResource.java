// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bytes;

import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.load.engine.Resource;

public class BytesResource implements Resource<byte[]>
{
    private final byte[] bytes;
    
    public BytesResource(final byte[] array) {
        this.bytes = Preconditions.checkNotNull(array);
    }
    
    @Override
    public byte[] get() {
        return this.bytes;
    }
    
    @Override
    public Class<byte[]> getResourceClass() {
        return byte[].class;
    }
    
    @Override
    public int getSize() {
        return this.bytes.length;
    }
    
    @Override
    public void recycle() {
    }
}
