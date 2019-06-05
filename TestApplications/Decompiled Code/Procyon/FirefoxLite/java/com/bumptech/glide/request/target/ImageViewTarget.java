// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.graphics.drawable.Animatable;
import com.bumptech.glide.request.transition.Transition;
import android.widget.ImageView;

public abstract class ImageViewTarget<Z> extends ViewTarget<ImageView, Z> implements ViewAdapter
{
    private Animatable animatable;
    
    public ImageViewTarget(final ImageView imageView) {
        super((View)imageView);
    }
    
    private void maybeUpdateAnimatable(final Z b) {
        if (b instanceof Animatable) {
            (this.animatable = (Animatable)b).start();
        }
        else {
            this.animatable = null;
        }
    }
    
    private void setResourceInternal(final Z resource) {
        this.maybeUpdateAnimatable(resource);
        this.setResource(resource);
    }
    
    @Override
    public void onLoadCleared(final Drawable drawable) {
        super.onLoadCleared(drawable);
        this.setResourceInternal(null);
        this.setDrawable(drawable);
    }
    
    @Override
    public void onLoadFailed(final Drawable drawable) {
        super.onLoadFailed(drawable);
        this.setResourceInternal(null);
        this.setDrawable(drawable);
    }
    
    @Override
    public void onLoadStarted(final Drawable drawable) {
        super.onLoadStarted(drawable);
        this.setResourceInternal(null);
        this.setDrawable(drawable);
    }
    
    @Override
    public void onResourceReady(final Z resourceInternal, final Transition<? super Z> transition) {
        if (transition != null && transition.transition(resourceInternal, (Transition.ViewAdapter)this)) {
            this.maybeUpdateAnimatable(resourceInternal);
        }
        else {
            this.setResourceInternal(resourceInternal);
        }
    }
    
    @Override
    public void onStart() {
        if (this.animatable != null) {
            this.animatable.start();
        }
    }
    
    @Override
    public void onStop() {
        if (this.animatable != null) {
            this.animatable.stop();
        }
    }
    
    public void setDrawable(final Drawable imageDrawable) {
        ((ImageView)this.view).setImageDrawable(imageDrawable);
    }
    
    protected abstract void setResource(final Z p0);
}
