package com.bumptech.glide.request.target;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.Transition.ViewAdapter;

public abstract class ImageViewTarget<Z> extends ViewTarget<ImageView, Z> implements ViewAdapter {
    private Animatable animatable;

    public abstract void setResource(Z z);

    public ImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    public void setDrawable(Drawable drawable) {
        ((ImageView) this.view).setImageDrawable(drawable);
    }

    public void onLoadStarted(Drawable drawable) {
        super.onLoadStarted(drawable);
        setResourceInternal(null);
        setDrawable(drawable);
    }

    public void onLoadFailed(Drawable drawable) {
        super.onLoadFailed(drawable);
        setResourceInternal(null);
        setDrawable(drawable);
    }

    public void onLoadCleared(Drawable drawable) {
        super.onLoadCleared(drawable);
        setResourceInternal(null);
        setDrawable(drawable);
    }

    public void onResourceReady(Z z, Transition<? super Z> transition) {
        if (transition == null || !transition.transition(z, this)) {
            setResourceInternal(z);
        } else {
            maybeUpdateAnimatable(z);
        }
    }

    public void onStart() {
        if (this.animatable != null) {
            this.animatable.start();
        }
    }

    public void onStop() {
        if (this.animatable != null) {
            this.animatable.stop();
        }
    }

    private void setResourceInternal(Z z) {
        maybeUpdateAnimatable(z);
        setResource(z);
    }

    private void maybeUpdateAnimatable(Z z) {
        if (z instanceof Animatable) {
            this.animatable = (Animatable) z;
            this.animatable.start();
            return;
        }
        this.animatable = null;
    }
}
