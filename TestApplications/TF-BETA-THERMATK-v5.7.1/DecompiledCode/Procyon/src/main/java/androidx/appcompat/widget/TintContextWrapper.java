// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.widget;

import android.content.res.AssetManager;
import android.os.Build$VERSION;
import android.content.Context;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.content.ContextWrapper;

public class TintContextWrapper extends ContextWrapper
{
    private static final Object CACHE_LOCK;
    private static ArrayList<WeakReference<TintContextWrapper>> sCache;
    private final Resources mResources;
    private final Resources$Theme mTheme;
    
    static {
        CACHE_LOCK = new Object();
    }
    
    private TintContextWrapper(final Context context) {
        super(context);
        if (VectorEnabledTintResources.shouldBeUsed()) {
            this.mResources = new VectorEnabledTintResources((Context)this, context.getResources());
            (this.mTheme = this.mResources.newTheme()).setTo(context.getTheme());
        }
        else {
            this.mResources = new TintResources((Context)this, context.getResources());
            this.mTheme = null;
        }
    }
    
    private static boolean shouldWrap(final Context context) {
        final boolean b = context instanceof TintContextWrapper;
        boolean b3;
        final boolean b2 = b3 = false;
        if (!b) {
            b3 = b2;
            if (!(context.getResources() instanceof TintResources)) {
                if (context.getResources() instanceof VectorEnabledTintResources) {
                    b3 = b2;
                }
                else {
                    if (Build$VERSION.SDK_INT >= 21) {
                        b3 = b2;
                        if (!VectorEnabledTintResources.shouldBeUsed()) {
                            return b3;
                        }
                    }
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    public static Context wrap(final Context context) {
        if (shouldWrap(context)) {
            synchronized (TintContextWrapper.CACHE_LOCK) {
                if (TintContextWrapper.sCache == null) {
                    TintContextWrapper.sCache = new ArrayList<WeakReference<TintContextWrapper>>();
                }
                else {
                    for (int i = TintContextWrapper.sCache.size() - 1; i >= 0; --i) {
                        final WeakReference<TintContextWrapper> weakReference = TintContextWrapper.sCache.get(i);
                        if (weakReference == null || weakReference.get() == null) {
                            TintContextWrapper.sCache.remove(i);
                        }
                    }
                    for (int j = TintContextWrapper.sCache.size() - 1; j >= 0; --j) {
                        final WeakReference<TintContextWrapper> weakReference2 = TintContextWrapper.sCache.get(j);
                        ContextWrapper contextWrapper;
                        if (weakReference2 != null) {
                            contextWrapper = weakReference2.get();
                        }
                        else {
                            contextWrapper = null;
                        }
                        if (contextWrapper != null && contextWrapper.getBaseContext() == context) {
                            return (Context)contextWrapper;
                        }
                    }
                }
                final TintContextWrapper referent = new TintContextWrapper(context);
                TintContextWrapper.sCache.add(new WeakReference<TintContextWrapper>(referent));
                return (Context)referent;
            }
        }
        return context;
    }
    
    public AssetManager getAssets() {
        return this.mResources.getAssets();
    }
    
    public Resources getResources() {
        return this.mResources;
    }
    
    public Resources$Theme getTheme() {
        Resources$Theme resources$Theme;
        if ((resources$Theme = this.mTheme) == null) {
            resources$Theme = super.getTheme();
        }
        return resources$Theme;
    }
    
    public void setTheme(final int theme) {
        final Resources$Theme mTheme = this.mTheme;
        if (mTheme == null) {
            super.setTheme(theme);
        }
        else {
            mTheme.applyStyle(theme, true);
        }
    }
}
