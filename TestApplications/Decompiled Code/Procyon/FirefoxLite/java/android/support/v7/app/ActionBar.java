// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.app;

import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.ViewGroup$MarginLayoutParams;
import android.support.v7.view.ActionMode;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.content.res.Configuration;
import android.content.Context;

public abstract class ActionBar
{
    public boolean closeOptionsMenu() {
        return false;
    }
    
    public boolean collapseActionView() {
        return false;
    }
    
    public void dispatchMenuVisibilityChanged(final boolean b) {
    }
    
    public abstract int getDisplayOptions();
    
    public Context getThemedContext() {
        return null;
    }
    
    public boolean invalidateOptionsMenu() {
        return false;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
    }
    
    void onDestroy() {
    }
    
    public boolean onKeyShortcut(final int n, final KeyEvent keyEvent) {
        return false;
    }
    
    public boolean onMenuKeyEvent(final KeyEvent keyEvent) {
        return false;
    }
    
    public boolean openOptionsMenu() {
        return false;
    }
    
    public void setDefaultDisplayHomeAsUpEnabled(final boolean b) {
    }
    
    public abstract void setDisplayHomeAsUpEnabled(final boolean p0);
    
    public abstract void setDisplayShowHomeEnabled(final boolean p0);
    
    public void setElevation(final float n) {
        if (n == 0.0f) {
            return;
        }
        throw new UnsupportedOperationException("Setting a non-zero elevation is not supported in this action bar configuration.");
    }
    
    public void setHideOnContentScrollEnabled(final boolean b) {
        if (!b) {
            return;
        }
        throw new UnsupportedOperationException("Hide on content scroll is not supported in this action bar configuration.");
    }
    
    public void setHomeAsUpIndicator(final Drawable drawable) {
    }
    
    public void setHomeButtonEnabled(final boolean b) {
    }
    
    public void setShowHideAnimationEnabled(final boolean b) {
    }
    
    public void setWindowTitle(final CharSequence charSequence) {
    }
    
    public ActionMode startActionMode(final ActionMode.Callback callback) {
        return null;
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public int gravity;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.gravity = 0;
            this.gravity = 8388627;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.gravity = 0;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ActionBarLayout);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.ActionBarLayout_android_layout_gravity, 0);
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.gravity = 0;
            this.gravity = layoutParams.gravity;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.gravity = 0;
        }
    }
    
    public interface OnMenuVisibilityListener
    {
        void onMenuVisibilityChanged(final boolean p0);
    }
    
    @Deprecated
    public abstract static class Tab
    {
        public abstract CharSequence getContentDescription();
        
        public abstract View getCustomView();
        
        public abstract Drawable getIcon();
        
        public abstract CharSequence getText();
        
        public abstract void select();
    }
}
