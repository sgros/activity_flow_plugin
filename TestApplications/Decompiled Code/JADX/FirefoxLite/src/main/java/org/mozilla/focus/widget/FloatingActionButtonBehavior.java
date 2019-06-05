package org.mozilla.focus.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

public class FloatingActionButtonBehavior extends Behavior<FloatingActionButton> implements OnOffsetChangedListener {
    private FloatingActionButton button;
    private AppBarLayout layout;
    private boolean visible = true;

    public FloatingActionButtonBehavior(Context context, AttributeSet attributeSet) {
    }

    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
        if (this.button != floatingActionButton) {
            this.button = floatingActionButton;
        }
        if (!(view instanceof AppBarLayout) || this.layout == view) {
            return super.layoutDependsOn(coordinatorLayout, floatingActionButton, view);
        }
        this.layout = (AppBarLayout) view;
        this.layout.addOnOffsetChangedListener((OnOffsetChangedListener) this);
        return true;
    }

    public void onDependentViewRemoved(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
        super.onDependentViewRemoved(coordinatorLayout, floatingActionButton, view);
        this.layout.removeOnOffsetChangedListener((OnOffsetChangedListener) this);
        this.layout = null;
    }

    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0 && !this.visible) {
            showButton();
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange() && this.visible) {
            hideButton();
        }
    }

    private void showButton() {
        animate(this.button, false);
    }

    private void hideButton() {
        animate(this.button, true);
    }

    private void animate(final View view, final boolean z) {
        float f = 1.0f;
        ViewPropertyAnimator scaleX = view.animate().scaleX(z ? 0.0f : 1.0f);
        if (z) {
            f = 0.0f;
        }
        scaleX.scaleY(f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                if (!z) {
                    view.setVisibility(0);
                }
            }

            public void onAnimationEnd(Animator animator) {
                FloatingActionButtonBehavior.this.visible = z ^ 1;
                if (z) {
                    view.setVisibility(8);
                }
            }
        }).start();
    }
}
