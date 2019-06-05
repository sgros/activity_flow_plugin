// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.support.design.ripple.RippleUtils;
import android.graphics.PorterDuff$Mode;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.content.res.ColorStateList;
import java.util.Iterator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Paint;
import android.os.Build$VERSION;
import android.support.v4.view.ViewCompat;
import android.support.design.R;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.ValueAnimator;
import java.util.List;
import android.support.design.animation.AnimatorSetCompat;
import android.animation.TypeEvaluator;
import android.util.Property;
import android.support.design.animation.MatrixEvaluator;
import android.support.design.animation.ImageMatrixProperty;
import android.animation.ObjectAnimator;
import android.view.View;
import android.animation.AnimatorSet;
import android.graphics.Matrix$ScaleToFit;
import android.support.design.animation.AnimationUtils;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.Matrix;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.animation.Animator$AnimatorListener;
import java.util.ArrayList;
import android.support.design.animation.MotionSpec;
import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.animation.TimeInterpolator;

class FloatingActionButtonImpl
{
    static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR;
    static final int[] EMPTY_STATE_SET;
    static final int[] ENABLED_STATE_SET;
    static final int[] FOCUSED_ENABLED_STATE_SET;
    static final int[] HOVERED_ENABLED_STATE_SET;
    static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET;
    static final int[] PRESSED_ENABLED_STATE_SET;
    int animState;
    CircularBorderDrawable borderDrawable;
    Drawable contentBackground;
    Animator currentAnimator;
    private MotionSpec defaultHideMotionSpec;
    private MotionSpec defaultShowMotionSpec;
    float elevation;
    private ArrayList<Animator$AnimatorListener> hideListeners;
    MotionSpec hideMotionSpec;
    float hoveredFocusedTranslationZ;
    float imageMatrixScale;
    int maxImageSize;
    private ViewTreeObserver$OnPreDrawListener preDrawListener;
    float pressedTranslationZ;
    Drawable rippleDrawable;
    private float rotation;
    ShadowDrawableWrapper shadowDrawable;
    final ShadowViewDelegate shadowViewDelegate;
    Drawable shapeDrawable;
    private ArrayList<Animator$AnimatorListener> showListeners;
    MotionSpec showMotionSpec;
    private final StateListAnimator stateListAnimator;
    private final Matrix tmpMatrix;
    private final Rect tmpRect;
    private final RectF tmpRectF1;
    private final RectF tmpRectF2;
    final VisibilityAwareImageButton view;
    
    static {
        ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
        PRESSED_ENABLED_STATE_SET = new int[] { 16842919, 16842910 };
        HOVERED_FOCUSED_ENABLED_STATE_SET = new int[] { 16843623, 16842908, 16842910 };
        FOCUSED_ENABLED_STATE_SET = new int[] { 16842908, 16842910 };
        HOVERED_ENABLED_STATE_SET = new int[] { 16843623, 16842910 };
        ENABLED_STATE_SET = new int[] { 16842910 };
        EMPTY_STATE_SET = new int[0];
    }
    
    FloatingActionButtonImpl(final VisibilityAwareImageButton view, final ShadowViewDelegate shadowViewDelegate) {
        this.animState = 0;
        this.imageMatrixScale = 1.0f;
        this.tmpRect = new Rect();
        this.tmpRectF1 = new RectF();
        this.tmpRectF2 = new RectF();
        this.tmpMatrix = new Matrix();
        this.view = view;
        this.shadowViewDelegate = shadowViewDelegate;
        (this.stateListAnimator = new StateListAnimator()).addState(FloatingActionButtonImpl.PRESSED_ENABLED_STATE_SET, this.createElevationAnimator((ShadowAnimatorImpl)new ElevateToPressedTranslationZAnimation()));
        this.stateListAnimator.addState(FloatingActionButtonImpl.HOVERED_FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator((ShadowAnimatorImpl)new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(FloatingActionButtonImpl.FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator((ShadowAnimatorImpl)new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(FloatingActionButtonImpl.HOVERED_ENABLED_STATE_SET, this.createElevationAnimator((ShadowAnimatorImpl)new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(FloatingActionButtonImpl.ENABLED_STATE_SET, this.createElevationAnimator((ShadowAnimatorImpl)new ResetElevationAnimation()));
        this.stateListAnimator.addState(FloatingActionButtonImpl.EMPTY_STATE_SET, this.createElevationAnimator((ShadowAnimatorImpl)new DisabledElevationAnimation()));
        this.rotation = this.view.getRotation();
    }
    
    private void calculateImageMatrixFromScale(final float n, final Matrix matrix) {
        matrix.reset();
        final Drawable drawable = this.view.getDrawable();
        if (drawable != null && this.maxImageSize != 0) {
            final RectF tmpRectF1 = this.tmpRectF1;
            final RectF tmpRectF2 = this.tmpRectF2;
            tmpRectF1.set(0.0f, 0.0f, (float)drawable.getIntrinsicWidth(), (float)drawable.getIntrinsicHeight());
            tmpRectF2.set(0.0f, 0.0f, (float)this.maxImageSize, (float)this.maxImageSize);
            matrix.setRectToRect(tmpRectF1, tmpRectF2, Matrix$ScaleToFit.CENTER);
            matrix.postScale(n, n, this.maxImageSize / 2.0f, this.maxImageSize / 2.0f);
        }
    }
    
    private AnimatorSet createAnimator(final MotionSpec motionSpec, final float n, final float n2, final float n3) {
        final ArrayList<Animator> list = new ArrayList<Animator>();
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.view, View.ALPHA, new float[] { n });
        motionSpec.getTiming("opacity").apply((Animator)ofFloat);
        list.add((Animator)ofFloat);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.view, View.SCALE_X, new float[] { n2 });
        motionSpec.getTiming("scale").apply((Animator)ofFloat2);
        list.add((Animator)ofFloat2);
        final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.view, View.SCALE_Y, new float[] { n2 });
        motionSpec.getTiming("scale").apply((Animator)ofFloat3);
        list.add((Animator)ofFloat3);
        this.calculateImageMatrixFromScale(n3, this.tmpMatrix);
        final ObjectAnimator ofObject = ObjectAnimator.ofObject((Object)this.view, (Property)new ImageMatrixProperty(), (TypeEvaluator)new MatrixEvaluator(), (Object[])new Matrix[] { new Matrix(this.tmpMatrix) });
        motionSpec.getTiming("iconScale").apply((Animator)ofObject);
        list.add((Animator)ofObject);
        final AnimatorSet set = new AnimatorSet();
        AnimatorSetCompat.playTogether(set, list);
        return set;
    }
    
    private ValueAnimator createElevationAnimator(final ShadowAnimatorImpl shadowAnimatorImpl) {
        final ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(FloatingActionButtonImpl.ELEVATION_ANIM_INTERPOLATOR);
        valueAnimator.setDuration(100L);
        valueAnimator.addListener((Animator$AnimatorListener)shadowAnimatorImpl);
        valueAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)shadowAnimatorImpl);
        valueAnimator.setFloatValues(new float[] { 0.0f, 1.0f });
        return valueAnimator;
    }
    
    private void ensurePreDrawListener() {
        if (this.preDrawListener == null) {
            this.preDrawListener = (ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                public boolean onPreDraw() {
                    FloatingActionButtonImpl.this.onPreDraw();
                    return true;
                }
            };
        }
    }
    
    private MotionSpec getDefaultHideMotionSpec() {
        if (this.defaultHideMotionSpec == null) {
            this.defaultHideMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_hide_motion_spec);
        }
        return this.defaultHideMotionSpec;
    }
    
    private MotionSpec getDefaultShowMotionSpec() {
        if (this.defaultShowMotionSpec == null) {
            this.defaultShowMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_show_motion_spec);
        }
        return this.defaultShowMotionSpec;
    }
    
    private boolean shouldAnimateVisibilityChange() {
        return ViewCompat.isLaidOut((View)this.view) && !this.view.isInEditMode();
    }
    
    private void updateFromViewRotation() {
        if (Build$VERSION.SDK_INT == 19) {
            if (this.rotation % 90.0f != 0.0f) {
                if (this.view.getLayerType() != 1) {
                    this.view.setLayerType(1, (Paint)null);
                }
            }
            else if (this.view.getLayerType() != 0) {
                this.view.setLayerType(0, (Paint)null);
            }
        }
        if (this.shadowDrawable != null) {
            this.shadowDrawable.setRotation(-this.rotation);
        }
        if (this.borderDrawable != null) {
            this.borderDrawable.setRotation(-this.rotation);
        }
    }
    
    public void addOnHideAnimationListener(final Animator$AnimatorListener e) {
        if (this.hideListeners == null) {
            this.hideListeners = new ArrayList<Animator$AnimatorListener>();
        }
        this.hideListeners.add(e);
    }
    
    void addOnShowAnimationListener(final Animator$AnimatorListener e) {
        if (this.showListeners == null) {
            this.showListeners = new ArrayList<Animator$AnimatorListener>();
        }
        this.showListeners.add(e);
    }
    
    final Drawable getContentBackground() {
        return this.contentBackground;
    }
    
    float getElevation() {
        return this.elevation;
    }
    
    final MotionSpec getHideMotionSpec() {
        return this.hideMotionSpec;
    }
    
    float getHoveredFocusedTranslationZ() {
        return this.hoveredFocusedTranslationZ;
    }
    
    void getPadding(final Rect rect) {
        this.shadowDrawable.getPadding(rect);
    }
    
    float getPressedTranslationZ() {
        return this.pressedTranslationZ;
    }
    
    final MotionSpec getShowMotionSpec() {
        return this.showMotionSpec;
    }
    
    void hide(final InternalVisibilityChangedListener internalVisibilityChangedListener, final boolean b) {
        if (this.isOrWillBeHidden()) {
            return;
        }
        if (this.currentAnimator != null) {
            this.currentAnimator.cancel();
        }
        if (this.shouldAnimateVisibilityChange()) {
            MotionSpec motionSpec;
            if (this.hideMotionSpec != null) {
                motionSpec = this.hideMotionSpec;
            }
            else {
                motionSpec = this.getDefaultHideMotionSpec();
            }
            final AnimatorSet animator = this.createAnimator(motionSpec, 0.0f, 0.0f, 0.0f);
            animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                private boolean cancelled;
                
                public void onAnimationCancel(final Animator animator) {
                    this.cancelled = true;
                }
                
                public void onAnimationEnd(final Animator animator) {
                    FloatingActionButtonImpl.this.animState = 0;
                    FloatingActionButtonImpl.this.currentAnimator = null;
                    if (!this.cancelled) {
                        final VisibilityAwareImageButton view = FloatingActionButtonImpl.this.view;
                        int n;
                        if (b) {
                            n = 8;
                        }
                        else {
                            n = 4;
                        }
                        view.internalSetVisibility(n, b);
                        if (internalVisibilityChangedListener != null) {
                            internalVisibilityChangedListener.onHidden();
                        }
                    }
                }
                
                public void onAnimationStart(final Animator currentAnimator) {
                    FloatingActionButtonImpl.this.view.internalSetVisibility(0, b);
                    FloatingActionButtonImpl.this.animState = 1;
                    FloatingActionButtonImpl.this.currentAnimator = currentAnimator;
                    this.cancelled = false;
                }
            });
            if (this.hideListeners != null) {
                final Iterator<Animator$AnimatorListener> iterator = this.hideListeners.iterator();
                while (iterator.hasNext()) {
                    animator.addListener((Animator$AnimatorListener)iterator.next());
                }
            }
            animator.start();
        }
        else {
            final VisibilityAwareImageButton view = this.view;
            int n;
            if (b) {
                n = 8;
            }
            else {
                n = 4;
            }
            view.internalSetVisibility(n, b);
            if (internalVisibilityChangedListener != null) {
                internalVisibilityChangedListener.onHidden();
            }
        }
    }
    
    boolean isOrWillBeHidden() {
        final int visibility = this.view.getVisibility();
        final boolean b = false;
        boolean b2 = false;
        if (visibility == 0) {
            if (this.animState == 1) {
                b2 = true;
            }
            return b2;
        }
        boolean b3 = b;
        if (this.animState != 2) {
            b3 = true;
        }
        return b3;
    }
    
    boolean isOrWillBeShown() {
        final int visibility = this.view.getVisibility();
        final boolean b = false;
        boolean b2 = false;
        if (visibility != 0) {
            if (this.animState == 2) {
                b2 = true;
            }
            return b2;
        }
        boolean b3 = b;
        if (this.animState != 1) {
            b3 = true;
        }
        return b3;
    }
    
    void jumpDrawableToCurrentState() {
        this.stateListAnimator.jumpToCurrentState();
    }
    
    void onAttachedToWindow() {
        if (this.requirePreDrawListener()) {
            this.ensurePreDrawListener();
            this.view.getViewTreeObserver().addOnPreDrawListener(this.preDrawListener);
        }
    }
    
    void onCompatShadowChanged() {
    }
    
    void onDetachedFromWindow() {
        if (this.preDrawListener != null) {
            this.view.getViewTreeObserver().removeOnPreDrawListener(this.preDrawListener);
            this.preDrawListener = null;
        }
    }
    
    void onDrawableStateChanged(final int[] state) {
        this.stateListAnimator.setState(state);
    }
    
    void onElevationsChanged(final float n, final float n2, final float n3) {
        if (this.shadowDrawable != null) {
            this.shadowDrawable.setShadowSize(n, this.pressedTranslationZ + n);
            this.updatePadding();
        }
    }
    
    void onPaddingUpdated(final Rect rect) {
    }
    
    void onPreDraw() {
        final float rotation = this.view.getRotation();
        if (this.rotation != rotation) {
            this.rotation = rotation;
            this.updateFromViewRotation();
        }
    }
    
    public void removeOnHideAnimationListener(final Animator$AnimatorListener o) {
        if (this.hideListeners == null) {
            return;
        }
        this.hideListeners.remove(o);
    }
    
    void removeOnShowAnimationListener(final Animator$AnimatorListener o) {
        if (this.showListeners == null) {
            return;
        }
        this.showListeners.remove(o);
    }
    
    boolean requirePreDrawListener() {
        return true;
    }
    
    void setBackgroundTintList(final ColorStateList borderTint) {
        if (this.shapeDrawable != null) {
            DrawableCompat.setTintList(this.shapeDrawable, borderTint);
        }
        if (this.borderDrawable != null) {
            this.borderDrawable.setBorderTint(borderTint);
        }
    }
    
    void setBackgroundTintMode(final PorterDuff$Mode porterDuff$Mode) {
        if (this.shapeDrawable != null) {
            DrawableCompat.setTintMode(this.shapeDrawable, porterDuff$Mode);
        }
    }
    
    final void setElevation(final float elevation) {
        if (this.elevation != elevation) {
            this.onElevationsChanged(this.elevation = elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }
    
    final void setHideMotionSpec(final MotionSpec hideMotionSpec) {
        this.hideMotionSpec = hideMotionSpec;
    }
    
    final void setHoveredFocusedTranslationZ(final float hoveredFocusedTranslationZ) {
        if (this.hoveredFocusedTranslationZ != hoveredFocusedTranslationZ) {
            this.hoveredFocusedTranslationZ = hoveredFocusedTranslationZ;
            this.onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }
    
    final void setImageMatrixScale(final float imageMatrixScale) {
        this.imageMatrixScale = imageMatrixScale;
        final Matrix tmpMatrix = this.tmpMatrix;
        this.calculateImageMatrixFromScale(imageMatrixScale, tmpMatrix);
        this.view.setImageMatrix(tmpMatrix);
    }
    
    final void setPressedTranslationZ(final float pressedTranslationZ) {
        if (this.pressedTranslationZ != pressedTranslationZ) {
            this.pressedTranslationZ = pressedTranslationZ;
            this.onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }
    
    void setRippleColor(final ColorStateList list) {
        if (this.rippleDrawable != null) {
            DrawableCompat.setTintList(this.rippleDrawable, RippleUtils.convertToRippleDrawableColor(list));
        }
    }
    
    final void setShowMotionSpec(final MotionSpec showMotionSpec) {
        this.showMotionSpec = showMotionSpec;
    }
    
    void show(final InternalVisibilityChangedListener internalVisibilityChangedListener, final boolean b) {
        if (this.isOrWillBeShown()) {
            return;
        }
        if (this.currentAnimator != null) {
            this.currentAnimator.cancel();
        }
        if (this.shouldAnimateVisibilityChange()) {
            if (this.view.getVisibility() != 0) {
                this.view.setAlpha(0.0f);
                this.view.setScaleY(0.0f);
                this.view.setScaleX(0.0f);
                this.setImageMatrixScale(0.0f);
            }
            MotionSpec motionSpec;
            if (this.showMotionSpec != null) {
                motionSpec = this.showMotionSpec;
            }
            else {
                motionSpec = this.getDefaultShowMotionSpec();
            }
            final AnimatorSet animator = this.createAnimator(motionSpec, 1.0f, 1.0f, 1.0f);
            animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    FloatingActionButtonImpl.this.animState = 0;
                    FloatingActionButtonImpl.this.currentAnimator = null;
                    if (internalVisibilityChangedListener != null) {
                        internalVisibilityChangedListener.onShown();
                    }
                }
                
                public void onAnimationStart(final Animator currentAnimator) {
                    FloatingActionButtonImpl.this.view.internalSetVisibility(0, b);
                    FloatingActionButtonImpl.this.animState = 2;
                    FloatingActionButtonImpl.this.currentAnimator = currentAnimator;
                }
            });
            if (this.showListeners != null) {
                final Iterator<Animator$AnimatorListener> iterator = this.showListeners.iterator();
                while (iterator.hasNext()) {
                    animator.addListener((Animator$AnimatorListener)iterator.next());
                }
            }
            animator.start();
        }
        else {
            this.view.internalSetVisibility(0, b);
            this.view.setAlpha(1.0f);
            this.view.setScaleY(1.0f);
            this.view.setScaleX(1.0f);
            this.setImageMatrixScale(1.0f);
            if (internalVisibilityChangedListener != null) {
                internalVisibilityChangedListener.onShown();
            }
        }
    }
    
    final void updateImageMatrixScale() {
        this.setImageMatrixScale(this.imageMatrixScale);
    }
    
    final void updatePadding() {
        final Rect tmpRect = this.tmpRect;
        this.getPadding(tmpRect);
        this.onPaddingUpdated(tmpRect);
        this.shadowViewDelegate.setShadowPadding(tmpRect.left, tmpRect.top, tmpRect.right, tmpRect.bottom);
    }
    
    private class DisabledElevationAnimation extends ShadowAnimatorImpl
    {
        DisabledElevationAnimation() {
        }
        
        @Override
        protected float getTargetShadowSize() {
            return 0.0f;
        }
    }
    
    private class ElevateToHoveredFocusedTranslationZAnimation extends ShadowAnimatorImpl
    {
        ElevateToHoveredFocusedTranslationZAnimation() {
        }
        
        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.hoveredFocusedTranslationZ;
        }
    }
    
    private class ElevateToPressedTranslationZAnimation extends ShadowAnimatorImpl
    {
        ElevateToPressedTranslationZAnimation() {
        }
        
        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.pressedTranslationZ;
        }
    }
    
    interface InternalVisibilityChangedListener
    {
        void onHidden();
        
        void onShown();
    }
    
    private class ResetElevationAnimation extends ShadowAnimatorImpl
    {
        ResetElevationAnimation() {
        }
        
        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation;
        }
    }
    
    private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements ValueAnimator$AnimatorUpdateListener
    {
        private float shadowSizeEnd;
        private float shadowSizeStart;
        private boolean validValues;
        
        protected abstract float getTargetShadowSize();
        
        public void onAnimationEnd(final Animator animator) {
            FloatingActionButtonImpl.this.shadowDrawable.setShadowSize(this.shadowSizeEnd);
            this.validValues = false;
        }
        
        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
            if (!this.validValues) {
                this.shadowSizeStart = FloatingActionButtonImpl.this.shadowDrawable.getShadowSize();
                this.shadowSizeEnd = this.getTargetShadowSize();
                this.validValues = true;
            }
            FloatingActionButtonImpl.this.shadowDrawable.setShadowSize(this.shadowSizeStart + (this.shadowSizeEnd - this.shadowSizeStart) * valueAnimator.getAnimatedFraction());
        }
    }
}
