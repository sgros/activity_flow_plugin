// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.view.ViewPropertyAnimator;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.CoordinatorLayout;

public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> implements OnOffsetChangedListener
{
    private FloatingActionButton button;
    private AppBarLayout layout;
    private boolean visible;
    
    public FloatingActionButtonBehavior(final Context context, final AttributeSet set) {
        this.visible = true;
    }
    
    private void animate(final View view, final boolean b) {
        final ViewPropertyAnimator animate = view.animate();
        final float n = 1.0f;
        float n2;
        if (b) {
            n2 = 0.0f;
        }
        else {
            n2 = 1.0f;
        }
        final ViewPropertyAnimator scaleX = animate.scaleX(n2);
        float n3 = n;
        if (b) {
            n3 = 0.0f;
        }
        scaleX.scaleY(n3).setDuration(300L).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                FloatingActionButtonBehavior.this.visible = (b ^ true);
                if (b) {
                    view.setVisibility(8);
                }
            }
            
            public void onAnimationStart(final Animator animator) {
                if (!b) {
                    view.setVisibility(0);
                }
            }
        }).start();
    }
    
    private void hideButton() {
        this.animate((View)this.button, true);
    }
    
    private void showButton() {
        this.animate((View)this.button, false);
    }
    
    public boolean layoutDependsOn(final CoordinatorLayout coordinatorLayout, final FloatingActionButton button, final View view) {
        if (this.button != button) {
            this.button = button;
        }
        if (view instanceof AppBarLayout && this.layout != view) {
            (this.layout = (AppBarLayout)view).addOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener)this);
            return true;
        }
        return super.layoutDependsOn(coordinatorLayout, button, view);
    }
    
    public void onDependentViewRemoved(final CoordinatorLayout coordinatorLayout, final FloatingActionButton floatingActionButton, final View view) {
        super.onDependentViewRemoved(coordinatorLayout, floatingActionButton, view);
        this.layout.removeOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener)this);
        this.layout = null;
    }
    
    @Override
    public void onOffsetChanged(final AppBarLayout appBarLayout, final int a) {
        if (a == 0 && !this.visible) {
            this.showButton();
        }
        else if (Math.abs(a) >= appBarLayout.getTotalScrollRange() && this.visible) {
            this.hideButton();
        }
    }
}
