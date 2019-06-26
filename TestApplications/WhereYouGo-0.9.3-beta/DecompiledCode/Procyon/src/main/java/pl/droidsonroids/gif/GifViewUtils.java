// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.content.res.Resources$NotFoundException;
import android.os.Build$VERSION;
import java.io.IOException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.content.res.TypedArray;
import android.view.View;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.util.TypedValue;
import android.support.annotation.RawRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.content.res.Resources;
import java.util.Arrays;
import java.util.List;

final class GifViewUtils
{
    static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    static final List<String> SUPPORTED_RESOURCE_TYPE_NAMES;
    
    static {
        SUPPORTED_RESOURCE_TYPE_NAMES = Arrays.asList("raw", "drawable", "mipmap");
    }
    
    private GifViewUtils() {
    }
    
    static float getDensityScale(@NonNull final Resources resources, @DrawableRes @RawRes int density) {
        final TypedValue typedValue = new TypedValue();
        resources.getValue(density, typedValue, true);
        density = typedValue.density;
        if (density == 0) {
            density = 160;
        }
        else if (density == 65535) {
            density = 0;
        }
        final int densityDpi = resources.getDisplayMetrics().densityDpi;
        float n;
        if (density > 0 && densityDpi > 0) {
            n = densityDpi / (float)density;
        }
        else {
            n = 1.0f;
        }
        return n;
    }
    
    private static int getResourceId(final ImageView imageView, final AttributeSet set, final boolean b) {
        String s;
        if (b) {
            s = "src";
        }
        else {
            s = "background";
        }
        int attributeResourceValue = set.getAttributeResourceValue("http://schemas.android.com/apk/res/android", s, 0);
        if (attributeResourceValue <= 0 || !GifViewUtils.SUPPORTED_RESOURCE_TYPE_NAMES.contains(imageView.getResources().getResourceTypeName(attributeResourceValue)) || setResource(imageView, b, attributeResourceValue)) {
            attributeResourceValue = 0;
        }
        return attributeResourceValue;
    }
    
    static InitResult initImageView(final ImageView imageView, final AttributeSet set, final int n, final int n2) {
        InitResult initResult;
        if (set != null && !imageView.isInEditMode()) {
            initResult = new InitResult(getResourceId(imageView, set, true), getResourceId(imageView, set, false), isFreezingAnimation((View)imageView, set, n, n2));
        }
        else {
            initResult = new InitResult(0, 0, false);
        }
        return initResult;
    }
    
    static boolean isFreezingAnimation(final View view, final AttributeSet set, final int n, final int n2) {
        final TypedArray obtainStyledAttributes = view.getContext().obtainStyledAttributes(set, R.styleable.GifView, n, n2);
        final boolean boolean1 = obtainStyledAttributes.getBoolean(R.styleable.GifView_freezesAnimation, false);
        obtainStyledAttributes.recycle();
        return boolean1;
    }
    
    static boolean setGifImageUri(final ImageView imageView, final Uri uri) {
        if (uri == null) {
            return false;
        }
        try {
            imageView.setImageDrawable((Drawable)new GifDrawable(imageView.getContext().getContentResolver(), uri));
            return true;
        }
        catch (IOException ex) {}
        return false;
    }
    
    static boolean setResource(final ImageView imageView, final boolean b, final int n) {
        final Resources resources = imageView.getResources();
        if (resources == null) {
            goto Label_0053;
        }
        try {
            final GifDrawable gifDrawable = new GifDrawable(resources, n);
            if (b) {
                imageView.setImageDrawable((Drawable)gifDrawable);
            }
            else {
                if (Build$VERSION.SDK_INT < 16) {
                    goto Label_0058;
                }
                imageView.setBackground((Drawable)gifDrawable);
            }
            return true;
        }
        catch (IOException ex) {}
        catch (Resources$NotFoundException ex2) {
            goto Label_0053;
        }
    }
    
    static class InitResult
    {
        final int mBackgroundResId;
        final boolean mFreezesAnimation;
        final int mSourceResId;
        
        InitResult(final int mSourceResId, final int mBackgroundResId, final boolean mFreezesAnimation) {
            this.mSourceResId = mSourceResId;
            this.mBackgroundResId = mBackgroundResId;
            this.mFreezesAnimation = mFreezesAnimation;
        }
    }
}
