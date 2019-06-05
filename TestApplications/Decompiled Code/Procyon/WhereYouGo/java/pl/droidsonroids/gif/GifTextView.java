// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.os.Parcelable;
import android.view.View;
import android.os.Build$VERSION;
import android.content.res.Resources;
import android.content.res.Resources$NotFoundException;
import java.io.IOException;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;

public class GifTextView extends TextView
{
    private boolean mFreezesAnimation;
    
    public GifTextView(final Context context) {
        super(context);
    }
    
    public GifTextView(final Context context, final AttributeSet set) {
        super(context, set);
        this.init(set, 0, 0);
    }
    
    public GifTextView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init(set, n, 0);
    }
    
    @RequiresApi(21)
    public GifTextView(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.init(set, n, n2);
    }
    
    private Drawable getGifOrDefaultDrawable(final int n) {
        Drawable drawable = null;
        if (n == 0) {
            drawable = null;
        }
        else {
            final Resources resources = this.getResources();
            if (this.isInEditMode() || !"drawable".equals(resources.getResourceTypeName(n))) {
                goto Label_0047;
            }
            try {
                drawable = new GifDrawable(resources, n);
            }
            catch (IOException ex) {}
            catch (Resources$NotFoundException ex2) {
                goto Label_0047;
            }
        }
        return drawable;
    }
    
    private void hideCompoundDrawables(final Drawable[] array) {
        for (final Drawable drawable : array) {
            if (drawable != null) {
                drawable.setVisible(false, false);
            }
        }
    }
    
    private void init(final AttributeSet set, final int n, final int n2) {
        if (set != null) {
            final Drawable gifOrDefaultDrawable = this.getGifOrDefaultDrawable(set.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableLeft", 0));
            final Drawable gifOrDefaultDrawable2 = this.getGifOrDefaultDrawable(set.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableTop", 0));
            final Drawable gifOrDefaultDrawable3 = this.getGifOrDefaultDrawable(set.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableRight", 0));
            final Drawable gifOrDefaultDrawable4 = this.getGifOrDefaultDrawable(set.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableBottom", 0));
            final Drawable gifOrDefaultDrawable5 = this.getGifOrDefaultDrawable(set.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableStart", 0));
            final Drawable gifOrDefaultDrawable6 = this.getGifOrDefaultDrawable(set.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableEnd", 0));
            if (Build$VERSION.SDK_INT >= 17) {
                Drawable drawable2;
                Drawable drawable3;
                if (this.getLayoutDirection() == 0) {
                    Drawable drawable;
                    if ((drawable = gifOrDefaultDrawable5) == null) {
                        drawable = gifOrDefaultDrawable;
                    }
                    drawable2 = gifOrDefaultDrawable6;
                    drawable3 = drawable;
                    if (gifOrDefaultDrawable6 == null) {
                        drawable2 = gifOrDefaultDrawable3;
                        drawable3 = drawable;
                    }
                }
                else {
                    Drawable drawable4;
                    if ((drawable4 = gifOrDefaultDrawable5) == null) {
                        drawable4 = gifOrDefaultDrawable3;
                    }
                    drawable2 = gifOrDefaultDrawable6;
                    drawable3 = drawable4;
                    if (gifOrDefaultDrawable6 == null) {
                        drawable2 = gifOrDefaultDrawable;
                        drawable3 = drawable4;
                    }
                }
                this.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable3, gifOrDefaultDrawable2, drawable2, gifOrDefaultDrawable4);
                this.setCompoundDrawablesWithIntrinsicBounds(gifOrDefaultDrawable, gifOrDefaultDrawable2, gifOrDefaultDrawable3, gifOrDefaultDrawable4);
            }
            else {
                this.setCompoundDrawablesWithIntrinsicBounds(gifOrDefaultDrawable, gifOrDefaultDrawable2, gifOrDefaultDrawable3, gifOrDefaultDrawable4);
            }
            this.setBackgroundInternal(this.getGifOrDefaultDrawable(set.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", 0)));
        }
        this.mFreezesAnimation = GifViewUtils.isFreezingAnimation((View)this, set, n, n2);
    }
    
    private void setBackgroundInternal(final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 16) {
            this.setBackground(drawable);
        }
        else {
            this.setBackgroundDrawable(drawable);
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.hideCompoundDrawables(this.getCompoundDrawables());
        if (Build$VERSION.SDK_INT >= 17) {
            this.hideCompoundDrawables(this.getCompoundDrawablesRelative());
        }
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof GifViewSavedState)) {
            super.onRestoreInstanceState(parcelable);
        }
        else {
            final GifViewSavedState gifViewSavedState = (GifViewSavedState)parcelable;
            super.onRestoreInstanceState(gifViewSavedState.getSuperState());
            final Drawable[] compoundDrawables = this.getCompoundDrawables();
            gifViewSavedState.restoreState(compoundDrawables[0], 0);
            gifViewSavedState.restoreState(compoundDrawables[1], 1);
            gifViewSavedState.restoreState(compoundDrawables[2], 2);
            gifViewSavedState.restoreState(compoundDrawables[3], 3);
            if (Build$VERSION.SDK_INT >= 17) {
                final Drawable[] compoundDrawablesRelative = this.getCompoundDrawablesRelative();
                gifViewSavedState.restoreState(compoundDrawablesRelative[0], 4);
                gifViewSavedState.restoreState(compoundDrawablesRelative[2], 5);
            }
            gifViewSavedState.restoreState(this.getBackground(), 6);
        }
    }
    
    public Parcelable onSaveInstanceState() {
        final Drawable[] array = new Drawable[7];
        if (this.mFreezesAnimation) {
            final Drawable[] compoundDrawables = this.getCompoundDrawables();
            System.arraycopy(compoundDrawables, 0, array, 0, compoundDrawables.length);
            if (Build$VERSION.SDK_INT >= 17) {
                final Drawable[] compoundDrawablesRelative = this.getCompoundDrawablesRelative();
                array[4] = compoundDrawablesRelative[0];
                array[5] = compoundDrawablesRelative[2];
            }
            array[6] = this.getBackground();
        }
        return (Parcelable)new GifViewSavedState(super.onSaveInstanceState(), array);
    }
    
    public void setBackgroundResource(final int n) {
        this.setBackgroundInternal(this.getGifOrDefaultDrawable(n));
    }
    
    @RequiresApi(17)
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(final int n, final int n2, final int n3, final int n4) {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(this.getGifOrDefaultDrawable(n), this.getGifOrDefaultDrawable(n2), this.getGifOrDefaultDrawable(n3), this.getGifOrDefaultDrawable(n4));
    }
    
    public void setCompoundDrawablesWithIntrinsicBounds(final int n, final int n2, final int n3, final int n4) {
        this.setCompoundDrawablesWithIntrinsicBounds(this.getGifOrDefaultDrawable(n), this.getGifOrDefaultDrawable(n2), this.getGifOrDefaultDrawable(n3), this.getGifOrDefaultDrawable(n4));
    }
    
    public void setFreezesAnimation(final boolean mFreezesAnimation) {
        this.mFreezesAnimation = mFreezesAnimation;
    }
}
