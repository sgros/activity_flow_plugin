// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request.target;

import com.bumptech.glide.request.transition.Transition;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.manager.LifecycleListener;

public interface Target<R> extends LifecycleListener
{
    Request getRequest();
    
    void getSize(final SizeReadyCallback p0);
    
    void onLoadCleared(final Drawable p0);
    
    void onLoadFailed(final Drawable p0);
    
    void onLoadStarted(final Drawable p0);
    
    void onResourceReady(final R p0, final Transition<? super R> p1);
    
    void removeCallback(final SizeReadyCallback p0);
    
    void setRequest(final Request p0);
}
