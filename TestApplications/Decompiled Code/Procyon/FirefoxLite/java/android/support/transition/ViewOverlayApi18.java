// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewOverlay;

class ViewOverlayApi18 implements ViewOverlayImpl
{
    private final ViewOverlay mViewOverlay;
    
    ViewOverlayApi18(final View view) {
        this.mViewOverlay = view.getOverlay();
    }
    
    @Override
    public void add(final Drawable drawable) {
        this.mViewOverlay.add(drawable);
    }
    
    @Override
    public void remove(final Drawable drawable) {
        this.mViewOverlay.remove(drawable);
    }
}
