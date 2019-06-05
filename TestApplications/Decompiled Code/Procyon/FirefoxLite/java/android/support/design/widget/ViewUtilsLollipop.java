// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.content.res.TypedArray;
import android.content.Context;
import android.animation.AnimatorInflater;
import android.support.design.internal.ThemeEnforcement;
import android.util.AttributeSet;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.support.design.R;
import android.view.ViewOutlineProvider;
import android.view.View;

class ViewUtilsLollipop
{
    private static final int[] STATE_LIST_ANIM_ATTRS;
    
    static {
        STATE_LIST_ANIM_ATTRS = new int[] { 16843848 };
    }
    
    static void setBoundsViewOutlineProvider(final View view) {
        view.setOutlineProvider(ViewOutlineProvider.BOUNDS);
    }
    
    static void setDefaultAppBarLayoutStateListAnimator(final View view, final float n) {
        final int integer = view.getResources().getInteger(R.integer.app_bar_elevation_anim_duration);
        final StateListAnimator stateListAnimator = new StateListAnimator();
        final int state_liftable = R.attr.state_liftable;
        final int n2 = -R.attr.state_lifted;
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)view, "elevation", new float[] { 0.0f });
        final long n3 = integer;
        stateListAnimator.addState(new int[] { 16842766, state_liftable, n2 }, (Animator)ofFloat.setDuration(n3));
        stateListAnimator.addState(new int[] { 16842766 }, (Animator)ObjectAnimator.ofFloat((Object)view, "elevation", new float[] { n }).setDuration(n3));
        stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)view, "elevation", new float[] { 0.0f }).setDuration(0L));
        view.setStateListAnimator(stateListAnimator);
    }
    
    static void setStateListAnimatorFromAttrs(final View view, AttributeSet obtainStyledAttributes, final int n, final int n2) {
        final Context context = view.getContext();
        obtainStyledAttributes = (AttributeSet)ThemeEnforcement.obtainStyledAttributes(context, obtainStyledAttributes, ViewUtilsLollipop.STATE_LIST_ANIM_ATTRS, n, n2, new int[0]);
        try {
            if (((TypedArray)obtainStyledAttributes).hasValue(0)) {
                view.setStateListAnimator(AnimatorInflater.loadStateListAnimator(context, ((TypedArray)obtainStyledAttributes).getResourceId(0, 0)));
            }
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
}
