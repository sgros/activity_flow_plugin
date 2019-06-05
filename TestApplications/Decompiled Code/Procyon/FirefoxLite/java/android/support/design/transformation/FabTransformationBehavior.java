// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.transformation;

import android.support.design.animation.MotionSpec;
import android.support.design.animation.AnimatorSetCompat;
import java.util.ArrayList;
import android.animation.AnimatorSet;
import android.support.design.widget.CoordinatorLayout;
import android.content.res.ColorStateList;
import android.view.ViewAnimationUtils;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.support.design.animation.DrawableAlphaProperty;
import android.widget.ImageView;
import android.animation.AnimatorListenerAdapter;
import android.support.design.circularreveal.CircularRevealCompat;
import android.support.design.widget.MathUtils;
import android.support.design.widget.FloatingActionButton;
import android.annotation.TargetApi;
import android.support.v4.view.ViewCompat;
import android.animation.TypeEvaluator;
import android.support.design.animation.ArgbEvaluatorCompat;
import android.util.Property;
import android.animation.ObjectAnimator;
import android.support.design.animation.ChildrenAlphaProperty;
import android.support.design.circularreveal.CircularRevealHelper;
import android.support.design.circularreveal.CircularRevealWidget;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import java.util.List;
import android.support.design.animation.AnimationUtils;
import android.support.design.animation.Positioning;
import android.support.design.animation.MotionTiming;
import android.support.design.R;
import android.view.ViewGroup;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.Rect;

public abstract class FabTransformationBehavior extends ExpandableTransformationBehavior
{
    private final int[] tmpArray;
    private final Rect tmpRect;
    private final RectF tmpRectF1;
    private final RectF tmpRectF2;
    
    public FabTransformationBehavior() {
        this.tmpRect = new Rect();
        this.tmpRectF1 = new RectF();
        this.tmpRectF2 = new RectF();
        this.tmpArray = new int[2];
    }
    
    public FabTransformationBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.tmpRect = new Rect();
        this.tmpRectF1 = new RectF();
        this.tmpRectF2 = new RectF();
        this.tmpArray = new int[2];
    }
    
    private ViewGroup calculateChildContentContainer(final View view) {
        final View viewById = view.findViewById(R.id.mtrl_child_content_container);
        if (viewById != null) {
            return this.toViewGroupOrNull(viewById);
        }
        if (!(view instanceof TransformationChildLayout) && !(view instanceof TransformationChildCard)) {
            return this.toViewGroupOrNull(view);
        }
        return this.toViewGroupOrNull(((ViewGroup)view).getChildAt(0));
    }
    
    private void calculateChildVisibleBoundsAtEndOfExpansion(final View view, final FabTransformationSpec fabTransformationSpec, final MotionTiming motionTiming, final MotionTiming motionTiming2, float calculateValueOfAnimationAtEndOfExpansion, float calculateValueOfAnimationAtEndOfExpansion2, final float n, final float n2, final RectF rectF) {
        calculateValueOfAnimationAtEndOfExpansion = this.calculateValueOfAnimationAtEndOfExpansion(fabTransformationSpec, motionTiming, calculateValueOfAnimationAtEndOfExpansion, n);
        calculateValueOfAnimationAtEndOfExpansion2 = this.calculateValueOfAnimationAtEndOfExpansion(fabTransformationSpec, motionTiming2, calculateValueOfAnimationAtEndOfExpansion2, n2);
        final Rect tmpRect = this.tmpRect;
        view.getWindowVisibleDisplayFrame(tmpRect);
        final RectF tmpRectF1 = this.tmpRectF1;
        tmpRectF1.set(tmpRect);
        final RectF tmpRectF2 = this.tmpRectF2;
        this.calculateWindowBounds(view, tmpRectF2);
        tmpRectF2.offset(calculateValueOfAnimationAtEndOfExpansion, calculateValueOfAnimationAtEndOfExpansion2);
        tmpRectF2.intersect(tmpRectF1);
        rectF.set(tmpRectF2);
    }
    
    private float calculateRevealCenterX(final View view, final View view2, final Positioning positioning) {
        final RectF tmpRectF1 = this.tmpRectF1;
        final RectF tmpRectF2 = this.tmpRectF2;
        this.calculateWindowBounds(view, tmpRectF1);
        this.calculateWindowBounds(view2, tmpRectF2);
        tmpRectF2.offset(-this.calculateTranslationX(view, view2, positioning), 0.0f);
        return tmpRectF1.centerX() - tmpRectF2.left;
    }
    
    private float calculateRevealCenterY(final View view, final View view2, final Positioning positioning) {
        final RectF tmpRectF1 = this.tmpRectF1;
        final RectF tmpRectF2 = this.tmpRectF2;
        this.calculateWindowBounds(view, tmpRectF1);
        this.calculateWindowBounds(view2, tmpRectF2);
        tmpRectF2.offset(0.0f, -this.calculateTranslationY(view, view2, positioning));
        return tmpRectF1.centerY() - tmpRectF2.top;
    }
    
    private float calculateTranslationX(final View view, final View view2, final Positioning positioning) {
        final RectF tmpRectF1 = this.tmpRectF1;
        final RectF tmpRectF2 = this.tmpRectF2;
        this.calculateWindowBounds(view, tmpRectF1);
        this.calculateWindowBounds(view2, tmpRectF2);
        final int n = positioning.gravity & 0x7;
        float n2;
        if (n != 1) {
            if (n != 3) {
                if (n != 5) {
                    n2 = 0.0f;
                }
                else {
                    n2 = tmpRectF2.right - tmpRectF1.right;
                }
            }
            else {
                n2 = tmpRectF2.left - tmpRectF1.left;
            }
        }
        else {
            n2 = tmpRectF2.centerX() - tmpRectF1.centerX();
        }
        return n2 + positioning.xAdjustment;
    }
    
    private float calculateTranslationY(final View view, final View view2, final Positioning positioning) {
        final RectF tmpRectF1 = this.tmpRectF1;
        final RectF tmpRectF2 = this.tmpRectF2;
        this.calculateWindowBounds(view, tmpRectF1);
        this.calculateWindowBounds(view2, tmpRectF2);
        final int n = positioning.gravity & 0x70;
        float n2;
        if (n != 16) {
            if (n != 48) {
                if (n != 80) {
                    n2 = 0.0f;
                }
                else {
                    n2 = tmpRectF2.bottom - tmpRectF1.bottom;
                }
            }
            else {
                n2 = tmpRectF2.top - tmpRectF1.top;
            }
        }
        else {
            n2 = tmpRectF2.centerY() - tmpRectF1.centerY();
        }
        return n2 + positioning.yAdjustment;
    }
    
    private float calculateValueOfAnimationAtEndOfExpansion(final FabTransformationSpec fabTransformationSpec, final MotionTiming motionTiming, final float n, final float n2) {
        final long delay = motionTiming.getDelay();
        final long duration = motionTiming.getDuration();
        final MotionTiming timing = fabTransformationSpec.timings.getTiming("expansion");
        return AnimationUtils.lerp(n, n2, motionTiming.getInterpolator().getInterpolation((timing.getDelay() + timing.getDuration() + 17L - delay) / (float)duration));
    }
    
    private void calculateWindowBounds(final View view, final RectF rectF) {
        rectF.set(0.0f, 0.0f, (float)view.getWidth(), (float)view.getHeight());
        final int[] tmpArray = this.tmpArray;
        view.getLocationInWindow(tmpArray);
        rectF.offsetTo((float)tmpArray[0], (float)tmpArray[1]);
        rectF.offset((float)(int)(-view.getTranslationX()), (float)(int)(-view.getTranslationY()));
    }
    
    private void createChildrenFadeAnimation(final View view, final View view2, final boolean b, final boolean b2, final FabTransformationSpec fabTransformationSpec, final List<Animator> list, final List<Animator$AnimatorListener> list2) {
        if (!(view2 instanceof ViewGroup)) {
            return;
        }
        if (view2 instanceof CircularRevealWidget && CircularRevealHelper.STRATEGY == 0) {
            return;
        }
        final ViewGroup calculateChildContentContainer = this.calculateChildContentContainer(view2);
        if (calculateChildContentContainer == null) {
            return;
        }
        ObjectAnimator objectAnimator;
        if (b) {
            if (!b2) {
                ChildrenAlphaProperty.CHILDREN_ALPHA.set((Object)calculateChildContentContainer, (Object)0.0f);
            }
            objectAnimator = ObjectAnimator.ofFloat((Object)calculateChildContentContainer, (Property)ChildrenAlphaProperty.CHILDREN_ALPHA, new float[] { 1.0f });
        }
        else {
            objectAnimator = ObjectAnimator.ofFloat((Object)calculateChildContentContainer, (Property)ChildrenAlphaProperty.CHILDREN_ALPHA, new float[] { 0.0f });
        }
        fabTransformationSpec.timings.getTiming("contentFade").apply((Animator)objectAnimator);
        list.add((Animator)objectAnimator);
    }
    
    private void createColorAnimation(final View view, final View view2, final boolean b, final boolean b2, final FabTransformationSpec fabTransformationSpec, final List<Animator> list, final List<Animator$AnimatorListener> list2) {
        if (!(view2 instanceof CircularRevealWidget)) {
            return;
        }
        final CircularRevealWidget circularRevealWidget = (CircularRevealWidget)view2;
        final int backgroundTint = this.getBackgroundTint(view);
        ObjectAnimator objectAnimator;
        if (b) {
            if (!b2) {
                circularRevealWidget.setCircularRevealScrimColor(backgroundTint);
            }
            objectAnimator = ObjectAnimator.ofInt((Object)circularRevealWidget, (Property)CircularRevealWidget.CircularRevealScrimColorProperty.CIRCULAR_REVEAL_SCRIM_COLOR, new int[] { 0xFFFFFF & backgroundTint });
        }
        else {
            objectAnimator = ObjectAnimator.ofInt((Object)circularRevealWidget, (Property)CircularRevealWidget.CircularRevealScrimColorProperty.CIRCULAR_REVEAL_SCRIM_COLOR, new int[] { backgroundTint });
        }
        objectAnimator.setEvaluator((TypeEvaluator)ArgbEvaluatorCompat.getInstance());
        fabTransformationSpec.timings.getTiming("color").apply((Animator)objectAnimator);
        list.add((Animator)objectAnimator);
    }
    
    @TargetApi(21)
    private void createElevationAnimation(final View view, final View view2, final boolean b, final boolean b2, final FabTransformationSpec fabTransformationSpec, final List<Animator> list, final List<Animator$AnimatorListener> list2) {
        final float n = ViewCompat.getElevation(view2) - ViewCompat.getElevation(view);
        ObjectAnimator objectAnimator;
        if (b) {
            if (!b2) {
                view2.setTranslationZ(-n);
            }
            objectAnimator = ObjectAnimator.ofFloat((Object)view2, View.TRANSLATION_Z, new float[] { 0.0f });
        }
        else {
            objectAnimator = ObjectAnimator.ofFloat((Object)view2, View.TRANSLATION_Z, new float[] { -n });
        }
        fabTransformationSpec.timings.getTiming("elevation").apply((Animator)objectAnimator);
        list.add((Animator)objectAnimator);
    }
    
    private void createExpansionAnimation(final View view, final View view2, final boolean b, final boolean b2, final FabTransformationSpec fabTransformationSpec, float radius, final float n, final List<Animator> list, final List<Animator$AnimatorListener> list2) {
        if (!(view2 instanceof CircularRevealWidget)) {
            return;
        }
        final CircularRevealWidget circularRevealWidget = (CircularRevealWidget)view2;
        final float calculateRevealCenterX = this.calculateRevealCenterX(view, view2, fabTransformationSpec.positioning);
        final float calculateRevealCenterY = this.calculateRevealCenterY(view, view2, fabTransformationSpec.positioning);
        ((FloatingActionButton)view).getContentRect(this.tmpRect);
        float radius2 = this.tmpRect.width() / 2.0f;
        final MotionTiming timing = fabTransformationSpec.timings.getTiming("expansion");
        Animator animator;
        if (b) {
            if (!b2) {
                circularRevealWidget.setRevealInfo(new CircularRevealWidget.RevealInfo(calculateRevealCenterX, calculateRevealCenterY, radius2));
            }
            if (b2) {
                radius2 = circularRevealWidget.getRevealInfo().radius;
            }
            animator = CircularRevealCompat.createCircularReveal(circularRevealWidget, calculateRevealCenterX, calculateRevealCenterY, MathUtils.distanceToFurthestCorner(calculateRevealCenterX, calculateRevealCenterY, 0.0f, 0.0f, radius, n));
            animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    final CircularRevealWidget.RevealInfo revealInfo = circularRevealWidget.getRevealInfo();
                    revealInfo.radius = Float.MAX_VALUE;
                    circularRevealWidget.setRevealInfo(revealInfo);
                }
            });
            this.createPreFillRadialExpansion(view2, timing.getDelay(), (int)calculateRevealCenterX, (int)calculateRevealCenterY, radius2, list);
        }
        else {
            radius = circularRevealWidget.getRevealInfo().radius;
            animator = CircularRevealCompat.createCircularReveal(circularRevealWidget, calculateRevealCenterX, calculateRevealCenterY, radius2);
            final long delay = timing.getDelay();
            final int n2 = (int)calculateRevealCenterX;
            final int n3 = (int)calculateRevealCenterY;
            this.createPreFillRadialExpansion(view2, delay, n2, n3, radius, list);
            this.createPostFillRadialExpansion(view2, timing.getDelay(), timing.getDuration(), fabTransformationSpec.timings.getTotalDuration(), n2, n3, radius2, list);
        }
        timing.apply(animator);
        list.add(animator);
        list2.add(CircularRevealCompat.createCircularRevealListener(circularRevealWidget));
    }
    
    private void createIconFadeAnimation(final View view, final View view2, final boolean b, final boolean b2, final FabTransformationSpec fabTransformationSpec, final List<Animator> list, final List<Animator$AnimatorListener> list2) {
        if (!(view2 instanceof CircularRevealWidget) || !(view instanceof ImageView)) {
            return;
        }
        final CircularRevealWidget circularRevealWidget = (CircularRevealWidget)view2;
        final Drawable drawable = ((ImageView)view).getDrawable();
        if (drawable == null) {
            return;
        }
        drawable.mutate();
        ObjectAnimator objectAnimator;
        if (b) {
            if (!b2) {
                drawable.setAlpha(255);
            }
            objectAnimator = ObjectAnimator.ofInt((Object)drawable, (Property)DrawableAlphaProperty.DRAWABLE_ALPHA_COMPAT, new int[] { 0 });
        }
        else {
            objectAnimator = ObjectAnimator.ofInt((Object)drawable, (Property)DrawableAlphaProperty.DRAWABLE_ALPHA_COMPAT, new int[] { 255 });
        }
        objectAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                view2.invalidate();
            }
        });
        fabTransformationSpec.timings.getTiming("iconFade").apply((Animator)objectAnimator);
        list.add((Animator)objectAnimator);
        list2.add((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                circularRevealWidget.setCircularRevealOverlayDrawable(null);
            }
            
            public void onAnimationStart(final Animator animator) {
                circularRevealWidget.setCircularRevealOverlayDrawable(drawable);
            }
        });
    }
    
    private void createPostFillRadialExpansion(final View view, long startDelay, final long n, final long n2, final int n3, final int n4, final float n5, final List<Animator> list) {
        if (Build$VERSION.SDK_INT >= 21) {
            startDelay += n;
            if (startDelay < n2) {
                final Animator circularReveal = ViewAnimationUtils.createCircularReveal(view, n3, n4, n5, n5);
                circularReveal.setStartDelay(startDelay);
                circularReveal.setDuration(n2 - startDelay);
                list.add(circularReveal);
            }
        }
    }
    
    private void createPreFillRadialExpansion(final View view, final long duration, final int n, final int n2, final float n3, final List<Animator> list) {
        if (Build$VERSION.SDK_INT >= 21 && duration > 0L) {
            final Animator circularReveal = ViewAnimationUtils.createCircularReveal(view, n, n2, n3, n3);
            circularReveal.setStartDelay(0L);
            circularReveal.setDuration(duration);
            list.add(circularReveal);
        }
    }
    
    private void createTranslationAnimation(final View view, final View view2, final boolean b, final boolean b2, final FabTransformationSpec fabTransformationSpec, final List<Animator> list, final List<Animator$AnimatorListener> list2, final RectF rectF) {
        final float calculateTranslationX = this.calculateTranslationX(view, view2, fabTransformationSpec.positioning);
        final float calculateTranslationY = this.calculateTranslationY(view, view2, fabTransformationSpec.positioning);
        MotionTiming motionTiming = null;
        MotionTiming motionTiming2 = null;
        Label_0148: {
            if (calculateTranslationX != 0.0f) {
                final float n = fcmpl(calculateTranslationY, 0.0f);
                if (n != 0) {
                    if ((b && calculateTranslationY < 0.0f) || (!b && n > 0)) {
                        motionTiming = fabTransformationSpec.timings.getTiming("translationXCurveUpwards");
                        motionTiming2 = fabTransformationSpec.timings.getTiming("translationYCurveUpwards");
                        break Label_0148;
                    }
                    motionTiming = fabTransformationSpec.timings.getTiming("translationXCurveDownwards");
                    motionTiming2 = fabTransformationSpec.timings.getTiming("translationYCurveDownwards");
                    break Label_0148;
                }
            }
            motionTiming = fabTransformationSpec.timings.getTiming("translationXLinear");
            motionTiming2 = fabTransformationSpec.timings.getTiming("translationYLinear");
        }
        ObjectAnimator ofFloat3;
        ObjectAnimator ofFloat4;
        if (b) {
            if (!b2) {
                view2.setTranslationX(-calculateTranslationX);
                view2.setTranslationY(-calculateTranslationY);
            }
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)view2, View.TRANSLATION_X, new float[] { 0.0f });
            final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)view2, View.TRANSLATION_Y, new float[] { 0.0f });
            this.calculateChildVisibleBoundsAtEndOfExpansion(view2, fabTransformationSpec, motionTiming, motionTiming2, -calculateTranslationX, -calculateTranslationY, 0.0f, 0.0f, rectF);
            ofFloat3 = ofFloat;
            ofFloat4 = ofFloat2;
        }
        else {
            ofFloat3 = ObjectAnimator.ofFloat((Object)view2, View.TRANSLATION_X, new float[] { -calculateTranslationX });
            ofFloat4 = ObjectAnimator.ofFloat((Object)view2, View.TRANSLATION_Y, new float[] { -calculateTranslationY });
        }
        motionTiming.apply((Animator)ofFloat3);
        motionTiming2.apply((Animator)ofFloat4);
        list.add((Animator)ofFloat3);
        list.add((Animator)ofFloat4);
    }
    
    private int getBackgroundTint(final View view) {
        final ColorStateList backgroundTintList = ViewCompat.getBackgroundTintList(view);
        if (backgroundTintList != null) {
            return backgroundTintList.getColorForState(view.getDrawableState(), backgroundTintList.getDefaultColor());
        }
        return 0;
    }
    
    private ViewGroup toViewGroupOrNull(final View view) {
        if (view instanceof ViewGroup) {
            return (ViewGroup)view;
        }
        return null;
    }
    
    @Override
    public boolean layoutDependsOn(final CoordinatorLayout coordinatorLayout, final View view, final View view2) {
        if (view.getVisibility() == 8) {
            throw new IllegalStateException("This behavior cannot be attached to a GONE view. Set the view to INVISIBLE instead.");
        }
        final boolean b = view2 instanceof FloatingActionButton;
        boolean b2 = false;
        if (b) {
            final int expandedComponentIdHint = ((FloatingActionButton)view2).getExpandedComponentIdHint();
            if (expandedComponentIdHint == 0 || expandedComponentIdHint == view.getId()) {
                b2 = true;
            }
            return b2;
        }
        return false;
    }
    
    @Override
    public void onAttachedToLayoutParams(final LayoutParams layoutParams) {
        if (layoutParams.dodgeInsetEdges == 0) {
            layoutParams.dodgeInsetEdges = 80;
        }
    }
    
    @Override
    protected AnimatorSet onCreateExpandedStateChangeAnimation(final View view, final View view2, final boolean b, final boolean b2) {
        final FabTransformationSpec onCreateMotionSpec = this.onCreateMotionSpec(view2.getContext(), b);
        final ArrayList<Animator> list = new ArrayList<Animator>();
        final ArrayList list2 = new ArrayList<Object>();
        if (Build$VERSION.SDK_INT >= 21) {
            this.createElevationAnimation(view, view2, b, b2, onCreateMotionSpec, list, list2);
        }
        final RectF tmpRectF1 = this.tmpRectF1;
        this.createTranslationAnimation(view, view2, b, b2, onCreateMotionSpec, list, list2, tmpRectF1);
        final float width = tmpRectF1.width();
        final float height = tmpRectF1.height();
        this.createIconFadeAnimation(view, view2, b, b2, onCreateMotionSpec, list, list2);
        this.createExpansionAnimation(view, view2, b, b2, onCreateMotionSpec, width, height, list, list2);
        this.createColorAnimation(view, view2, b, b2, onCreateMotionSpec, list, list2);
        this.createChildrenFadeAnimation(view, view2, b, b2, onCreateMotionSpec, list, list2);
        final AnimatorSet set = new AnimatorSet();
        AnimatorSetCompat.playTogether(set, list);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (!b) {
                    view2.setVisibility(4);
                    view.setAlpha(1.0f);
                    view.setVisibility(0);
                }
            }
            
            public void onAnimationStart(final Animator animator) {
                if (b) {
                    view2.setVisibility(0);
                    view.setAlpha(0.0f);
                    view.setVisibility(4);
                }
            }
        });
        for (int i = 0; i < list2.size(); ++i) {
            set.addListener((Animator$AnimatorListener)list2.get(i));
        }
        return set;
    }
    
    protected abstract FabTransformationSpec onCreateMotionSpec(final Context p0, final boolean p1);
    
    protected static class FabTransformationSpec
    {
        public Positioning positioning;
        public MotionSpec timings;
    }
}
