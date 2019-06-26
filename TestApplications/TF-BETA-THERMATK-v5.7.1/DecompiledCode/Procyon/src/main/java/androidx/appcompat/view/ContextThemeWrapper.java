// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.view;

import androidx.appcompat.R$style;
import android.content.res.AssetManager;
import android.os.Build$VERSION;
import android.content.Context;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.content.ContextWrapper;

public class ContextThemeWrapper extends ContextWrapper
{
    private LayoutInflater mInflater;
    private Configuration mOverrideConfiguration;
    private Resources mResources;
    private Resources$Theme mTheme;
    private int mThemeResource;
    
    public ContextThemeWrapper(final Context context, final int mThemeResource) {
        super(context);
        this.mThemeResource = mThemeResource;
    }
    
    public ContextThemeWrapper(final Context context, final Resources$Theme mTheme) {
        super(context);
        this.mTheme = mTheme;
    }
    
    private Resources getResourcesInternal() {
        if (this.mResources == null) {
            final Configuration mOverrideConfiguration = this.mOverrideConfiguration;
            if (mOverrideConfiguration == null) {
                this.mResources = super.getResources();
            }
            else if (Build$VERSION.SDK_INT >= 17) {
                this.mResources = this.createConfigurationContext(mOverrideConfiguration).getResources();
            }
        }
        return this.mResources;
    }
    
    private void initializeTheme() {
        final boolean b = this.mTheme == null;
        if (b) {
            this.mTheme = this.getResources().newTheme();
            final Resources$Theme theme = this.getBaseContext().getTheme();
            if (theme != null) {
                this.mTheme.setTo(theme);
            }
        }
        this.onApplyThemeResource(this.mTheme, this.mThemeResource, b);
    }
    
    protected void attachBaseContext(final Context context) {
        super.attachBaseContext(context);
    }
    
    public AssetManager getAssets() {
        return this.getResources().getAssets();
    }
    
    public Resources getResources() {
        return this.getResourcesInternal();
    }
    
    public Object getSystemService(final String anObject) {
        if ("layout_inflater".equals(anObject)) {
            if (this.mInflater == null) {
                this.mInflater = LayoutInflater.from(this.getBaseContext()).cloneInContext((Context)this);
            }
            return this.mInflater;
        }
        return this.getBaseContext().getSystemService(anObject);
    }
    
    public Resources$Theme getTheme() {
        final Resources$Theme mTheme = this.mTheme;
        if (mTheme != null) {
            return mTheme;
        }
        if (this.mThemeResource == 0) {
            this.mThemeResource = R$style.Theme_AppCompat_Light;
        }
        this.initializeTheme();
        return this.mTheme;
    }
    
    protected void onApplyThemeResource(final Resources$Theme resources$Theme, final int n, final boolean b) {
        resources$Theme.applyStyle(n, true);
    }
    
    public void setTheme(final int mThemeResource) {
        if (this.mThemeResource != mThemeResource) {
            this.mThemeResource = mThemeResource;
            this.initializeTheme();
        }
    }
}
