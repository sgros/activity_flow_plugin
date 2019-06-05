// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

class ViewGroupOverlayApi14 extends ViewOverlayApi14 implements ViewGroupOverlayImpl
{
    ViewGroupOverlayApi14(final Context context, final ViewGroup viewGroup, final View view) {
        super(context, viewGroup, view);
    }
    
    static ViewGroupOverlayApi14 createFrom(final ViewGroup viewGroup) {
        return (ViewGroupOverlayApi14)ViewOverlayApi14.createFrom((View)viewGroup);
    }
    
    @Override
    public void add(final View view) {
        this.mOverlayViewGroup.add(view);
    }
    
    @Override
    public void remove(final View view) {
        this.mOverlayViewGroup.remove(view);
    }
}
