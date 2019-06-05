package p008pl.droidsonroids.gif;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.p000v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/* renamed from: pl.droidsonroids.gif.GifViewUtils */
final class GifViewUtils {
    static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    static final List<String> SUPPORTED_RESOURCE_TYPE_NAMES = Arrays.asList(new String[]{"raw", "drawable", "mipmap"});

    /* renamed from: pl.droidsonroids.gif.GifViewUtils$InitResult */
    static class InitResult {
        final int mBackgroundResId;
        final boolean mFreezesAnimation;
        final int mSourceResId;

        InitResult(int sourceResId, int backgroundResId, boolean freezesAnimation) {
            this.mSourceResId = sourceResId;
            this.mBackgroundResId = backgroundResId;
            this.mFreezesAnimation = freezesAnimation;
        }
    }

    private GifViewUtils() {
    }

    static InitResult initImageView(ImageView view, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null || view.isInEditMode()) {
            return new InitResult(0, 0, false);
        }
        return new InitResult(GifViewUtils.getResourceId(view, attrs, true), GifViewUtils.getResourceId(view, attrs, false), GifViewUtils.isFreezingAnimation(view, attrs, defStyleAttr, defStyleRes));
    }

    private static int getResourceId(ImageView view, AttributeSet attrs, boolean isSrc) {
        int resId = attrs.getAttributeResourceValue(ANDROID_NS, isSrc ? "src" : "background", 0);
        if (resId > 0) {
            if (SUPPORTED_RESOURCE_TYPE_NAMES.contains(view.getResources().getResourceTypeName(resId)) && !GifViewUtils.setResource(view, isSrc, resId)) {
                return resId;
            }
        }
        return 0;
    }

    static boolean setResource(ImageView view, boolean isSrc, int resId) {
        Resources res = view.getResources();
        if (res != null) {
            try {
                GifDrawable d = new GifDrawable(res, resId);
                if (isSrc) {
                    view.setImageDrawable(d);
                } else if (VERSION.SDK_INT >= 16) {
                    view.setBackground(d);
                } else {
                    view.setBackgroundDrawable(d);
                }
                return true;
            } catch (NotFoundException | IOException e) {
            }
        }
        return false;
    }

    static boolean isFreezingAnimation(View view, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray gifViewAttributes = view.getContext().obtainStyledAttributes(attrs, C0386R.styleable.GifView, defStyleAttr, defStyleRes);
        boolean freezesAnimation = gifViewAttributes.getBoolean(C0386R.styleable.GifView_freezesAnimation, false);
        gifViewAttributes.recycle();
        return freezesAnimation;
    }

    static boolean setGifImageUri(ImageView imageView, Uri uri) {
        if (uri != null) {
            try {
                imageView.setImageDrawable(new GifDrawable(imageView.getContext().getContentResolver(), uri));
                return true;
            } catch (IOException e) {
            }
        }
        return false;
    }

    static float getDensityScale(@NonNull Resources res, @RawRes @DrawableRes int id) {
        int density;
        TypedValue value = new TypedValue();
        res.getValue(id, value, true);
        int resourceDensity = value.density;
        if (resourceDensity == 0) {
            density = 160;
        } else if (resourceDensity != SupportMenu.USER_MASK) {
            density = resourceDensity;
        } else {
            density = 0;
        }
        int targetDensity = res.getDisplayMetrics().densityDpi;
        if (density <= 0 || targetDensity <= 0) {
            return 1.0f;
        }
        return ((float) targetDensity) / ((float) density);
    }
}
