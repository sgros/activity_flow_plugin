// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.view.menu;

import android.view.View;
import android.view.MenuItem;
import android.view.ActionProvider$VisibilityListener;
import android.view.ActionProvider;
import androidx.core.internal.view.SupportMenuItem;
import android.content.Context;

class MenuItemWrapperJB extends MenuItemWrapperICS
{
    MenuItemWrapperJB(final Context context, final SupportMenuItem supportMenuItem) {
        super(context, supportMenuItem);
    }
    
    @Override
    ActionProviderWrapper createActionProviderWrapper(final ActionProvider actionProvider) {
        return new ActionProviderWrapperJB(super.mContext, actionProvider);
    }
    
    class ActionProviderWrapperJB extends ActionProviderWrapper implements ActionProvider$VisibilityListener
    {
        VisibilityListener mListener;
        
        public ActionProviderWrapperJB(final Context context, final android.view.ActionProvider actionProvider) {
            super(context, actionProvider);
        }
        
        public boolean isVisible() {
            return super.mInner.isVisible();
        }
        
        public void onActionProviderVisibilityChanged(final boolean b) {
            final VisibilityListener mListener = this.mListener;
            if (mListener != null) {
                mListener.onActionProviderVisibilityChanged(b);
            }
        }
        
        public View onCreateActionView(final MenuItem menuItem) {
            return super.mInner.onCreateActionView(menuItem);
        }
        
        public boolean overridesItemVisibility() {
            return super.mInner.overridesItemVisibility();
        }
        
        public void setVisibilityListener(final VisibilityListener mListener) {
            this.mListener = mListener;
            final android.view.ActionProvider mInner = super.mInner;
            Object visibilityListener;
            if (mListener != null) {
                visibilityListener = this;
            }
            else {
                visibilityListener = null;
            }
            mInner.setVisibilityListener((ActionProvider$VisibilityListener)visibilityListener);
        }
    }
}
