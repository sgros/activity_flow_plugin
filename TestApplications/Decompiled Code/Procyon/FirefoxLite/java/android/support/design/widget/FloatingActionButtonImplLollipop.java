// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.support.design.ripple.RippleUtils;
import android.graphics.drawable.RippleDrawable;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import android.animation.StateListAnimator;
import android.os.Build$VERSION;
import android.graphics.Rect;
import android.view.View;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.Animator;
import android.graphics.drawable.InsetDrawable;

class FloatingActionButtonImplLollipop extends FloatingActionButtonImpl
{
    private InsetDrawable insetDrawable;
    
    FloatingActionButtonImplLollipop(final VisibilityAwareImageButton visibilityAwareImageButton, final ShadowViewDelegate shadowViewDelegate) {
        super(visibilityAwareImageButton, shadowViewDelegate);
    }
    
    private Animator createElevationAnimator(final float n, final float n2) {
        final AnimatorSet set = new AnimatorSet();
        set.play((Animator)ObjectAnimator.ofFloat((Object)this.view, "elevation", new float[] { n }).setDuration(0L)).with((Animator)ObjectAnimator.ofFloat((Object)this.view, View.TRANSLATION_Z, new float[] { n2 }).setDuration(100L));
        set.setInterpolator(FloatingActionButtonImplLollipop.ELEVATION_ANIM_INTERPOLATOR);
        return (Animator)set;
    }
    
    public float getElevation() {
        return this.view.getElevation();
    }
    
    @Override
    void getPadding(final Rect rect) {
        if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
            final float radius = this.shadowViewDelegate.getRadius();
            final float n = this.getElevation() + this.pressedTranslationZ;
            final int n2 = (int)Math.ceil(ShadowDrawableWrapper.calculateHorizontalPadding(n, radius, false));
            final int n3 = (int)Math.ceil(ShadowDrawableWrapper.calculateVerticalPadding(n, radius, false));
            rect.set(n2, n3, n2, n3);
        }
        else {
            rect.set(0, 0, 0, 0);
        }
    }
    
    @Override
    void jumpDrawableToCurrentState() {
    }
    
    @Override
    void onCompatShadowChanged() {
        this.updatePadding();
    }
    
    @Override
    void onDrawableStateChanged(final int[] array) {
        if (Build$VERSION.SDK_INT == 21) {
            if (this.view.isEnabled()) {
                this.view.setElevation(this.elevation);
                if (this.view.isPressed()) {
                    this.view.setTranslationZ(this.pressedTranslationZ);
                }
                else if (!this.view.isFocused() && !this.view.isHovered()) {
                    this.view.setTranslationZ(0.0f);
                }
                else {
                    this.view.setTranslationZ(this.hoveredFocusedTranslationZ);
                }
            }
            else {
                this.view.setElevation(0.0f);
                this.view.setTranslationZ(0.0f);
            }
        }
    }
    
    @Override
    void onElevationsChanged(final float n, final float n2, final float n3) {
        if (Build$VERSION.SDK_INT == 21) {
            this.view.refreshDrawableState();
        }
        else {
            final StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(FloatingActionButtonImplLollipop.PRESSED_ENABLED_STATE_SET, this.createElevationAnimator(n, n3));
            stateListAnimator.addState(FloatingActionButtonImplLollipop.HOVERED_FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(n, n2));
            stateListAnimator.addState(FloatingActionButtonImplLollipop.FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(n, n2));
            stateListAnimator.addState(FloatingActionButtonImplLollipop.HOVERED_ENABLED_STATE_SET, this.createElevationAnimator(n, n2));
            final AnimatorSet set = new AnimatorSet();
            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
            list.add(ObjectAnimator.ofFloat((Object)this.view, "elevation", new float[] { n }).setDuration(0L));
            if (Build$VERSION.SDK_INT >= 22 && Build$VERSION.SDK_INT <= 24) {
                list.add(ObjectAnimator.ofFloat((Object)this.view, View.TRANSLATION_Z, new float[] { this.view.getTranslationZ() }).setDuration(100L));
            }
            list.add(ObjectAnimator.ofFloat((Object)this.view, View.TRANSLATION_Z, new float[] { 0.0f }).setDuration(100L));
            set.playSequentially((Animator[])list.toArray(new Animator[0]));
            set.setInterpolator(FloatingActionButtonImplLollipop.ELEVATION_ANIM_INTERPOLATOR);
            stateListAnimator.addState(FloatingActionButtonImplLollipop.ENABLED_STATE_SET, (Animator)set);
            stateListAnimator.addState(FloatingActionButtonImplLollipop.EMPTY_STATE_SET, this.createElevationAnimator(0.0f, 0.0f));
            this.view.setStateListAnimator(stateListAnimator);
        }
        if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
            this.updatePadding();
        }
    }
    
    @Override
    void onPaddingUpdated(final Rect rect) {
        if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
            this.insetDrawable = new InsetDrawable(this.rippleDrawable, rect.left, rect.top, rect.right, rect.bottom);
            this.shadowViewDelegate.setBackgroundDrawable((Drawable)this.insetDrawable);
        }
        else {
            this.shadowViewDelegate.setBackgroundDrawable(this.rippleDrawable);
        }
    }
    
    @Override
    boolean requirePreDrawListener() {
        return false;
    }
    
    @Override
    void setRippleColor(final ColorStateList rippleColor) {
        if (this.rippleDrawable instanceof RippleDrawable) {
            ((RippleDrawable)this.rippleDrawable).setColor(RippleUtils.convertToRippleDrawableColor(rippleColor));
        }
        else {
            super.setRippleColor(rippleColor);
        }
    }
}
