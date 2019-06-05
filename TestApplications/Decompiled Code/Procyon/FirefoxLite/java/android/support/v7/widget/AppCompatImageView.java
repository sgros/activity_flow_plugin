// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v4.view.TintableBackgroundView;
import android.widget.ImageView;

public class AppCompatImageView extends ImageView implements TintableBackgroundView, TintableImageSourceView
{
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    private final AppCompatImageHelper mImageHelper;
    
    public AppCompatImageView(final Context context) {
        this(context, null);
    }
    
    public AppCompatImageView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public AppCompatImageView(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mBackgroundTintHelper = new AppCompatBackgroundHelper((View)this)).loadFromAttributes(set, n);
        (this.mImageHelper = new AppCompatImageHelper(this)).loadFromAttributes(set, n);
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.applySupportBackgroundTint();
        }
        if (this.mImageHelper != null) {
            this.mImageHelper.applySupportImageTint();
        }
    }
    
    public ColorStateList getSupportBackgroundTintList() {
        ColorStateList supportBackgroundTintList;
        if (this.mBackgroundTintHelper != null) {
            supportBackgroundTintList = this.mBackgroundTintHelper.getSupportBackgroundTintList();
        }
        else {
            supportBackgroundTintList = null;
        }
        return supportBackgroundTintList;
    }
    
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        PorterDuff$Mode supportBackgroundTintMode;
        if (this.mBackgroundTintHelper != null) {
            supportBackgroundTintMode = this.mBackgroundTintHelper.getSupportBackgroundTintMode();
        }
        else {
            supportBackgroundTintMode = null;
        }
        return supportBackgroundTintMode;
    }
    
    public ColorStateList getSupportImageTintList() {
        ColorStateList supportImageTintList;
        if (this.mImageHelper != null) {
            supportImageTintList = this.mImageHelper.getSupportImageTintList();
        }
        else {
            supportImageTintList = null;
        }
        return supportImageTintList;
    }
    
    public PorterDuff$Mode getSupportImageTintMode() {
        PorterDuff$Mode supportImageTintMode;
        if (this.mImageHelper != null) {
            supportImageTintMode = this.mImageHelper.getSupportImageTintMode();
        }
        else {
            supportImageTintMode = null;
        }
        return supportImageTintMode;
    }
    
    public boolean hasOverlappingRendering() {
        return this.mImageHelper.hasOverlappingRendering() && super.hasOverlappingRendering();
    }
    
    public void setBackgroundDrawable(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundDrawable(backgroundDrawable);
        }
    }
    
    public void setBackgroundResource(final int backgroundResource) {
        super.setBackgroundResource(backgroundResource);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundResource(backgroundResource);
        }
    }
    
    public void setImageBitmap(final Bitmap imageBitmap) {
        super.setImageBitmap(imageBitmap);
        if (this.mImageHelper != null) {
            this.mImageHelper.applySupportImageTint();
        }
    }
    
    public void setImageDrawable(final Drawable imageDrawable) {
        super.setImageDrawable(imageDrawable);
        if (this.mImageHelper != null) {
            this.mImageHelper.applySupportImageTint();
        }
    }
    
    public void setImageResource(final int imageResource) {
        if (this.mImageHelper != null) {
            this.mImageHelper.setImageResource(imageResource);
        }
    }
    
    public void setImageURI(final Uri imageURI) {
        super.setImageURI(imageURI);
        if (this.mImageHelper != null) {
            this.mImageHelper.applySupportImageTint();
        }
    }
    
    public void setSupportBackgroundTintList(final ColorStateList supportBackgroundTintList) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintList(supportBackgroundTintList);
        }
    }
    
    public void setSupportBackgroundTintMode(final PorterDuff$Mode supportBackgroundTintMode) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintMode(supportBackgroundTintMode);
        }
    }
    
    public void setSupportImageTintList(final ColorStateList supportImageTintList) {
        if (this.mImageHelper != null) {
            this.mImageHelper.setSupportImageTintList(supportImageTintList);
        }
    }
    
    public void setSupportImageTintMode(final PorterDuff$Mode supportImageTintMode) {
        if (this.mImageHelper != null) {
            this.mImageHelper.setSupportImageTintMode(supportImageTintMode);
        }
    }
}
