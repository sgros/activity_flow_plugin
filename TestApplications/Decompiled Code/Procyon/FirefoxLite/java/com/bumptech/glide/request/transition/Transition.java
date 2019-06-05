// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request.transition;

public interface Transition<R>
{
    boolean transition(final R p0, final ViewAdapter p1);
    
    public interface ViewAdapter
    {
    }
}
