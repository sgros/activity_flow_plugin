// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.net.Uri;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;

public class GifImageView extends ImageView
{
    private boolean mFreezesAnimation;
    
    public GifImageView(final Context context) {
        super(context);
    }
    
    public GifImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.postInit(GifViewUtils.initImageView(this, set, 0, 0));
    }
    
    public GifImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.postInit(GifViewUtils.initImageView(this, set, n, 0));
    }
    
    @RequiresApi(21)
    public GifImageView(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.postInit(GifViewUtils.initImageView(this, set, n, n2));
    }
    
    private void postInit(final GifViewUtils.InitResult initResult) {
        this.mFreezesAnimation = initResult.mFreezesAnimation;
        if (initResult.mSourceResId > 0) {
            super.setImageResource(initResult.mSourceResId);
        }
        if (initResult.mBackgroundResId > 0) {
            super.setBackgroundResource(initResult.mBackgroundResId);
        }
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof GifViewSavedState)) {
            super.onRestoreInstanceState(parcelable);
        }
        else {
            final GifViewSavedState gifViewSavedState = (GifViewSavedState)parcelable;
            super.onRestoreInstanceState(gifViewSavedState.getSuperState());
            gifViewSavedState.restoreState(this.getDrawable(), 0);
            gifViewSavedState.restoreState(this.getBackground(), 1);
        }
    }
    
    public Parcelable onSaveInstanceState() {
        Drawable drawable;
        if (this.mFreezesAnimation) {
            drawable = this.getDrawable();
        }
        else {
            drawable = null;
        }
        Drawable background;
        if (this.mFreezesAnimation) {
            background = this.getBackground();
        }
        else {
            background = null;
        }
        return (Parcelable)new GifViewSavedState(super.onSaveInstanceState(), new Drawable[] { drawable, background });
    }
    
    public void setBackgroundResource(final int backgroundResource) {
        if (!GifViewUtils.setResource(this, false, backgroundResource)) {
            super.setBackgroundResource(backgroundResource);
        }
    }
    
    public void setFreezesAnimation(final boolean mFreezesAnimation) {
        this.mFreezesAnimation = mFreezesAnimation;
    }
    
    public void setImageResource(final int imageResource) {
        if (!GifViewUtils.setResource(this, true, imageResource)) {
            super.setImageResource(imageResource);
        }
    }
    
    public void setImageURI(final Uri imageURI) {
        if (!GifViewUtils.setGifImageUri(this, imageURI)) {
            super.setImageURI(imageURI);
        }
    }
}
