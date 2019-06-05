// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public interface TransitionFactory<R>
{
    Transition<R> build(final DataSource p0, final boolean p1);
}
