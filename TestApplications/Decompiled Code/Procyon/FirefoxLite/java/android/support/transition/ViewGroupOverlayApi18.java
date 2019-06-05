// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.view.View;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

class ViewGroupOverlayApi18 implements ViewGroupOverlayImpl
{
    private final ViewGroupOverlay mViewGroupOverlay;
    
    ViewGroupOverlayApi18(final ViewGroup viewGroup) {
        this.mViewGroupOverlay = viewGroup.getOverlay();
    }
    
    @Override
    public void add(final Drawable drawable) {
        this.mViewGroupOverlay.add(drawable);
    }
    
    @Override
    public void add(final View view) {
        this.mViewGroupOverlay.add(view);
    }
    
    @Override
    public void remove(final Drawable drawable) {
        this.mViewGroupOverlay.remove(drawable);
    }
    
    @Override
    public void remove(final View view) {
        this.mViewGroupOverlay.remove(view);
    }
}
