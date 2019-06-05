package android.support.design.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.design.C0028R;
import android.support.design.animation.AnimationUtils;
import android.support.design.animation.AnimatorSetCompat;
import android.support.design.animation.ImageMatrixProperty;
import android.support.design.animation.MatrixEvaluator;
import android.support.design.animation.MotionSpec;
import android.support.design.ripple.RippleUtils;
import android.support.p001v4.graphics.drawable.DrawableCompat;
import android.support.p001v4.view.ViewCompat;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.util.ArrayList;
import java.util.Iterator;

class FloatingActionButtonImpl {
    static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
    static final int[] EMPTY_STATE_SET = new int[0];
    static final int[] ENABLED_STATE_SET = new int[]{16842910};
    static final int[] FOCUSED_ENABLED_STATE_SET = new int[]{16842908, 16842910};
    static final int[] HOVERED_ENABLED_STATE_SET = new int[]{16843623, 16842910};
    static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET = new int[]{16843623, 16842908, 16842910};
    static final int[] PRESSED_ENABLED_STATE_SET = new int[]{16842919, 16842910};
    int animState = 0;
    CircularBorderDrawable borderDrawable;
    Drawable contentBackground;
    Animator currentAnimator;
    private MotionSpec defaultHideMotionSpec;
    private MotionSpec defaultShowMotionSpec;
    float elevation;
    private ArrayList<AnimatorListener> hideListeners;
    MotionSpec hideMotionSpec;
    float hoveredFocusedTranslationZ;
    float imageMatrixScale = 1.0f;
    int maxImageSize;
    private OnPreDrawListener preDrawListener;
    float pressedTranslationZ;
    Drawable rippleDrawable;
    private float rotation;
    ShadowDrawableWrapper shadowDrawable;
    final ShadowViewDelegate shadowViewDelegate;
    Drawable shapeDrawable;
    private ArrayList<AnimatorListener> showListeners;
    MotionSpec showMotionSpec;
    private final StateListAnimator stateListAnimator;
    private final Matrix tmpMatrix = new Matrix();
    private final Rect tmpRect = new Rect();
    private final RectF tmpRectF1 = new RectF();
    private final RectF tmpRectF2 = new RectF();
    final VisibilityAwareImageButton view;

    /* renamed from: android.support.design.widget.FloatingActionButtonImpl$3 */
    class C00713 implements OnPreDrawListener {
        C00713() {
        }

        public boolean onPreDraw() {
            FloatingActionButtonImpl.this.onPreDraw();
            return true;
        }
    }

    interface InternalVisibilityChangedListener {
        void onHidden();

        void onShown();
    }

    private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements AnimatorUpdateListener {
        private float shadowSizeEnd;
        private float shadowSizeStart;
        private boolean validValues;

        public abstract float getTargetShadowSize();

        private ShadowAnimatorImpl() {
        }

        /* synthetic */ ShadowAnimatorImpl(FloatingActionButtonImpl floatingActionButtonImpl, C00691 c00691) {
            this();
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (!this.validValues) {
                this.shadowSizeStart = FloatingActionButtonImpl.this.shadowDrawable.getShadowSize();
                this.shadowSizeEnd = getTargetShadowSize();
                this.validValues = true;
            }
            FloatingActionButtonImpl.this.shadowDrawable.setShadowSize(this.shadowSizeStart + ((this.shadowSizeEnd - this.shadowSizeStart) * valueAnimator.getAnimatedFraction()));
        }

        public void onAnimationEnd(Animator animator) {
            FloatingActionButtonImpl.this.shadowDrawable.setShadowSize(this.shadowSizeEnd);
            this.validValues = false;
        }
    }

    private class DisabledElevationAnimation extends ShadowAnimatorImpl {
        /* Access modifiers changed, original: protected */
        public float getTargetShadowSize() {
            return 0.0f;
        }

        DisabledElevationAnimation() {
            super(FloatingActionButtonImpl.this, null);
        }
    }

    private class ElevateToHoveredFocusedTranslationZAnimation extends ShadowAnimatorImpl {
        ElevateToHoveredFocusedTranslationZAnimation() {
            super(FloatingActionButtonImpl.this, null);
        }

        /* Access modifiers changed, original: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.hoveredFocusedTranslationZ;
        }
    }

    private class ElevateToPressedTranslationZAnimation extends ShadowAnimatorImpl {
        ElevateToPressedTranslationZAnimation() {
            super(FloatingActionButtonImpl.this, null);
        }

        /* Access modifiers changed, original: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.pressedTranslationZ;
        }
    }

    private class ResetElevationAnimation extends ShadowAnimatorImpl {
        ResetElevationAnimation() {
            super(FloatingActionButtonImpl.this, null);
        }

        /* Access modifiers changed, original: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onCompatShadowChanged() {
    }

    /* Access modifiers changed, original: 0000 */
    public void onPaddingUpdated(Rect rect) {
    }

    /* Access modifiers changed, original: 0000 */
    public boolean requirePreDrawListener() {
        return true;
    }

    FloatingActionButtonImpl(VisibilityAwareImageButton visibilityAwareImageButton, ShadowViewDelegate shadowViewDelegate) {
        this.view = visibilityAwareImageButton;
        this.shadowViewDelegate = shadowViewDelegate;
        this.stateListAnimator = new StateListAnimator();
        this.stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToPressedTranslationZAnimation()));
        this.stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(ENABLED_STATE_SET, createElevationAnimator(new ResetElevationAnimation()));
        this.stateListAnimator.addState(EMPTY_STATE_SET, createElevationAnimator(new DisabledElevationAnimation()));
        this.rotation = this.view.getRotation();
    }

    /* Access modifiers changed, original: 0000 */
    public void setBackgroundTintList(ColorStateList colorStateList) {
        if (this.shapeDrawable != null) {
            DrawableCompat.setTintList(this.shapeDrawable, colorStateList);
        }
        if (this.borderDrawable != null) {
            this.borderDrawable.setBorderTint(colorStateList);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setBackgroundTintMode(Mode mode) {
        if (this.shapeDrawable != null) {
            DrawableCompat.setTintMode(this.shapeDrawable, mode);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setRippleColor(ColorStateList colorStateList) {
        if (this.rippleDrawable != null) {
            DrawableCompat.setTintList(this.rippleDrawable, RippleUtils.convertToRippleDrawableColor(colorStateList));
        }
    }

    /* Access modifiers changed, original: final */
    public final void setElevation(float f) {
        if (this.elevation != f) {
            this.elevation = f;
            onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public float getElevation() {
        return this.elevation;
    }

    /* Access modifiers changed, original: 0000 */
    public float getHoveredFocusedTranslationZ() {
        return this.hoveredFocusedTranslationZ;
    }

    /* Access modifiers changed, original: 0000 */
    public float getPressedTranslationZ() {
        return this.pressedTranslationZ;
    }

    /* Access modifiers changed, original: final */
    public final void setHoveredFocusedTranslationZ(float f) {
        if (this.hoveredFocusedTranslationZ != f) {
            this.hoveredFocusedTranslationZ = f;
            onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }

    /* Access modifiers changed, original: final */
    public final void setPressedTranslationZ(float f) {
        if (this.pressedTranslationZ != f) {
            this.pressedTranslationZ = f;
            onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }

    /* Access modifiers changed, original: final */
    public final void updateImageMatrixScale() {
        setImageMatrixScale(this.imageMatrixScale);
    }

    /* Access modifiers changed, original: final */
    public final void setImageMatrixScale(float f) {
        this.imageMatrixScale = f;
        Matrix matrix = this.tmpMatrix;
        calculateImageMatrixFromScale(f, matrix);
        this.view.setImageMatrix(matrix);
    }

    private void calculateImageMatrixFromScale(float f, Matrix matrix) {
        matrix.reset();
        Drawable drawable = this.view.getDrawable();
        if (drawable != null && this.maxImageSize != 0) {
            RectF rectF = this.tmpRectF1;
            RectF rectF2 = this.tmpRectF2;
            rectF.set(0.0f, 0.0f, (float) drawable.getIntrinsicWidth(), (float) drawable.getIntrinsicHeight());
            rectF2.set(0.0f, 0.0f, (float) this.maxImageSize, (float) this.maxImageSize);
            matrix.setRectToRect(rectF, rectF2, ScaleToFit.CENTER);
            matrix.postScale(f, f, ((float) this.maxImageSize) / 2.0f, ((float) this.maxImageSize) / 2.0f);
        }
    }

    /* Access modifiers changed, original: final */
    public final MotionSpec getShowMotionSpec() {
        return this.showMotionSpec;
    }

    /* Access modifiers changed, original: final */
    public final void setShowMotionSpec(MotionSpec motionSpec) {
        this.showMotionSpec = motionSpec;
    }

    /* Access modifiers changed, original: final */
    public final MotionSpec getHideMotionSpec() {
        return this.hideMotionSpec;
    }

    /* Access modifiers changed, original: final */
    public final void setHideMotionSpec(MotionSpec motionSpec) {
        this.hideMotionSpec = motionSpec;
    }

    /* Access modifiers changed, original: 0000 */
    public void onElevationsChanged(float f, float f2, float f3) {
        if (this.shadowDrawable != null) {
            this.shadowDrawable.setShadowSize(f, this.pressedTranslationZ + f);
            updatePadding();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onDrawableStateChanged(int[] iArr) {
        this.stateListAnimator.setState(iArr);
    }

    /* Access modifiers changed, original: 0000 */
    public void jumpDrawableToCurrentState() {
        this.stateListAnimator.jumpToCurrentState();
    }

    /* Access modifiers changed, original: 0000 */
    public void addOnShowAnimationListener(AnimatorListener animatorListener) {
        if (this.showListeners == null) {
            this.showListeners = new ArrayList();
        }
        this.showListeners.add(animatorListener);
    }

    /* Access modifiers changed, original: 0000 */
    public void removeOnShowAnimationListener(AnimatorListener animatorListener) {
        if (this.showListeners != null) {
            this.showListeners.remove(animatorListener);
        }
    }

    public void addOnHideAnimationListener(AnimatorListener animatorListener) {
        if (this.hideListeners == null) {
            this.hideListeners = new ArrayList();
        }
        this.hideListeners.add(animatorListener);
    }

    public void removeOnHideAnimationListener(AnimatorListener animatorListener) {
        if (this.hideListeners != null) {
            this.hideListeners.remove(animatorListener);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void hide(final InternalVisibilityChangedListener internalVisibilityChangedListener, final boolean z) {
        if (!isOrWillBeHidden()) {
            if (this.currentAnimator != null) {
                this.currentAnimator.cancel();
            }
            if (shouldAnimateVisibilityChange()) {
                MotionSpec motionSpec;
                if (this.hideMotionSpec != null) {
                    motionSpec = this.hideMotionSpec;
                } else {
                    motionSpec = getDefaultHideMotionSpec();
                }
                AnimatorSet createAnimator = createAnimator(motionSpec, 0.0f, 0.0f, 0.0f);
                createAnimator.addListener(new AnimatorListenerAdapter() {
                    private boolean cancelled;

                    public void onAnimationStart(Animator animator) {
                        FloatingActionButtonImpl.this.view.internalSetVisibility(0, z);
                        FloatingActionButtonImpl.this.animState = 1;
                        FloatingActionButtonImpl.this.currentAnimator = animator;
                        this.cancelled = false;
                    }

                    public void onAnimationCancel(Animator animator) {
                        this.cancelled = true;
                    }

                    public void onAnimationEnd(Animator animator) {
                        FloatingActionButtonImpl.this.animState = 0;
                        FloatingActionButtonImpl.this.currentAnimator = null;
                        if (!this.cancelled) {
                            FloatingActionButtonImpl.this.view.internalSetVisibility(z ? 8 : 4, z);
                            if (internalVisibilityChangedListener != null) {
                                internalVisibilityChangedListener.onHidden();
                            }
                        }
                    }
                });
                if (this.hideListeners != null) {
                    Iterator it = this.hideListeners.iterator();
                    while (it.hasNext()) {
                        createAnimator.addListener((AnimatorListener) it.next());
                    }
                }
                createAnimator.start();
            } else {
                this.view.internalSetVisibility(z ? 8 : 4, z);
                if (internalVisibilityChangedListener != null) {
                    internalVisibilityChangedListener.onHidden();
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void show(final InternalVisibilityChangedListener internalVisibilityChangedListener, final boolean z) {
        if (!isOrWillBeShown()) {
            if (this.currentAnimator != null) {
                this.currentAnimator.cancel();
            }
            if (shouldAnimateVisibilityChange()) {
                MotionSpec motionSpec;
                if (this.view.getVisibility() != 0) {
                    this.view.setAlpha(0.0f);
                    this.view.setScaleY(0.0f);
                    this.view.setScaleX(0.0f);
                    setImageMatrixScale(0.0f);
                }
                if (this.showMotionSpec != null) {
                    motionSpec = this.showMotionSpec;
                } else {
                    motionSpec = getDefaultShowMotionSpec();
                }
                AnimatorSet createAnimator = createAnimator(motionSpec, 1.0f, 1.0f, 1.0f);
                createAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animator) {
                        FloatingActionButtonImpl.this.view.internalSetVisibility(0, z);
                        FloatingActionButtonImpl.this.animState = 2;
                        FloatingActionButtonImpl.this.currentAnimator = animator;
                    }

                    public void onAnimationEnd(Animator animator) {
                        FloatingActionButtonImpl.this.animState = 0;
                        FloatingActionButtonImpl.this.currentAnimator = null;
                        if (internalVisibilityChangedListener != null) {
                            internalVisibilityChangedListener.onShown();
                        }
                    }
                });
                if (this.showListeners != null) {
                    Iterator it = this.showListeners.iterator();
                    while (it.hasNext()) {
                        createAnimator.addListener((AnimatorListener) it.next());
                    }
                }
                createAnimator.start();
            } else {
                this.view.internalSetVisibility(0, z);
                this.view.setAlpha(1.0f);
                this.view.setScaleY(1.0f);
                this.view.setScaleX(1.0f);
                setImageMatrixScale(1.0f);
                if (internalVisibilityChangedListener != null) {
                    internalVisibilityChangedListener.onShown();
                }
            }
        }
    }

    private MotionSpec getDefaultShowMotionSpec() {
        if (this.defaultShowMotionSpec == null) {
            this.defaultShowMotionSpec = MotionSpec.createFromResource(this.view.getContext(), C0028R.animator.design_fab_show_motion_spec);
        }
        return this.defaultShowMotionSpec;
    }

    private MotionSpec getDefaultHideMotionSpec() {
        if (this.defaultHideMotionSpec == null) {
            this.defaultHideMotionSpec = MotionSpec.createFromResource(this.view.getContext(), C0028R.animator.design_fab_hide_motion_spec);
        }
        return this.defaultHideMotionSpec;
    }

    private AnimatorSet createAnimator(MotionSpec motionSpec, float f, float f2, float f3) {
        ArrayList arrayList = new ArrayList();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.view, View.ALPHA, new float[]{f});
        motionSpec.getTiming("opacity").apply(ofFloat);
        arrayList.add(ofFloat);
        ofFloat = ObjectAnimator.ofFloat(this.view, View.SCALE_X, new float[]{f2});
        motionSpec.getTiming("scale").apply(ofFloat);
        arrayList.add(ofFloat);
        ofFloat = ObjectAnimator.ofFloat(this.view, View.SCALE_Y, new float[]{f2});
        motionSpec.getTiming("scale").apply(ofFloat);
        arrayList.add(ofFloat);
        calculateImageMatrixFromScale(f3, this.tmpMatrix);
        ofFloat = ObjectAnimator.ofObject(this.view, new ImageMatrixProperty(), new MatrixEvaluator(), new Matrix[]{new Matrix(this.tmpMatrix)});
        motionSpec.getTiming("iconScale").apply(ofFloat);
        arrayList.add(ofFloat);
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSetCompat.playTogether(animatorSet, arrayList);
        return animatorSet;
    }

    /* Access modifiers changed, original: final */
    public final Drawable getContentBackground() {
        return this.contentBackground;
    }

    /* Access modifiers changed, original: final */
    public final void updatePadding() {
        Rect rect = this.tmpRect;
        getPadding(rect);
        onPaddingUpdated(rect);
        this.shadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    /* Access modifiers changed, original: 0000 */
    public void getPadding(Rect rect) {
        this.shadowDrawable.getPadding(rect);
    }

    /* Access modifiers changed, original: 0000 */
    public void onAttachedToWindow() {
        if (requirePreDrawListener()) {
            ensurePreDrawListener();
            this.view.getViewTreeObserver().addOnPreDrawListener(this.preDrawListener);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onDetachedFromWindow() {
        if (this.preDrawListener != null) {
            this.view.getViewTreeObserver().removeOnPreDrawListener(this.preDrawListener);
            this.preDrawListener = null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onPreDraw() {
        float rotation = this.view.getRotation();
        if (this.rotation != rotation) {
            this.rotation = rotation;
            updateFromViewRotation();
        }
    }

    private void ensurePreDrawListener() {
        if (this.preDrawListener == null) {
            this.preDrawListener = new C00713();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOrWillBeShown() {
        boolean z = false;
        if (this.view.getVisibility() != 0) {
            if (this.animState == 2) {
                z = true;
            }
            return z;
        }
        if (this.animState != 1) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOrWillBeHidden() {
        boolean z = false;
        if (this.view.getVisibility() == 0) {
            if (this.animState == 1) {
                z = true;
            }
            return z;
        }
        if (this.animState != 2) {
            z = true;
        }
        return z;
    }

    private ValueAnimator createElevationAnimator(ShadowAnimatorImpl shadowAnimatorImpl) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
        valueAnimator.setDuration(100);
        valueAnimator.addListener(shadowAnimatorImpl);
        valueAnimator.addUpdateListener(shadowAnimatorImpl);
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        return valueAnimator;
    }

    private boolean shouldAnimateVisibilityChange() {
        return ViewCompat.isLaidOut(this.view) && !this.view.isInEditMode();
    }

    private void updateFromViewRotation() {
        if (VERSION.SDK_INT == 19) {
            if (this.rotation % 90.0f != 0.0f) {
                if (this.view.getLayerType() != 1) {
                    this.view.setLayerType(1, null);
                }
            } else if (this.view.getLayerType() != 0) {
                this.view.setLayerType(0, null);
            }
        }
        if (this.shadowDrawable != null) {
            this.shadowDrawable.setRotation(-this.rotation);
        }
        if (this.borderDrawable != null) {
            this.borderDrawable.setRotation(-this.rotation);
        }
    }
}
