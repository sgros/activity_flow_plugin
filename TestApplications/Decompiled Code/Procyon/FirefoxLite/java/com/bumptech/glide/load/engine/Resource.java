// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

public interface Resource<Z>
{
    Z get();
    
    Class<Z> getResourceClass();
    
    int getSize();
    
    void recycle();
}
