// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.animation.Animator;
import android.view.ViewGroup;

public abstract class Visibility extends Transition
{
    private static final String[] sTransitionProperties;
    private int mMode;
    
    static {
        sTransitionProperties = new String[] { "android:visibility:visibility", "android:visibility:parent" };
    }
    
    public Visibility() {
        this.mMode = 3;
    }
    
    private void captureValues(final TransitionValues transitionValues) {
        transitionValues.values.put("android:visibility:visibility", transitionValues.view.getVisibility());
        transitionValues.values.put("android:visibility:parent", transitionValues.view.getParent());
        final int[] array = new int[2];
        transitionValues.view.getLocationOnScreen(array);
        transitionValues.values.put("android:visibility:screenLocation", array);
    }
    
    private VisibilityInfo getVisibilityChangeInfo(final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        final VisibilityInfo visibilityInfo = new VisibilityInfo();
        visibilityInfo.mVisibilityChange = false;
        visibilityInfo.mFadeIn = false;
        if (transitionValues != null && transitionValues.values.containsKey("android:visibility:visibility")) {
            visibilityInfo.mStartVisibility = transitionValues.values.get("android:visibility:visibility");
            visibilityInfo.mStartParent = transitionValues.values.get("android:visibility:parent");
        }
        else {
            visibilityInfo.mStartVisibility = -1;
            visibilityInfo.mStartParent = null;
        }
        if (transitionValues2 != null && transitionValues2.values.containsKey("android:visibility:visibility")) {
            visibilityInfo.mEndVisibility = transitionValues2.values.get("android:visibility:visibility");
            visibilityInfo.mEndParent = transitionValues2.values.get("android:visibility:parent");
        }
        else {
            visibilityInfo.mEndVisibility = -1;
            visibilityInfo.mEndParent = null;
        }
        if (transitionValues != null && transitionValues2 != null) {
            if (visibilityInfo.mStartVisibility == visibilityInfo.mEndVisibility && visibilityInfo.mStartParent == visibilityInfo.mEndParent) {
                return visibilityInfo;
            }
            if (visibilityInfo.mStartVisibility != visibilityInfo.mEndVisibility) {
                if (visibilityInfo.mStartVisibility == 0) {
                    visibilityInfo.mFadeIn = false;
                    visibilityInfo.mVisibilityChange = true;
                }
                else if (visibilityInfo.mEndVisibility == 0) {
                    visibilityInfo.mFadeIn = true;
                    visibilityInfo.mVisibilityChange = true;
                }
            }
            else if (visibilityInfo.mEndParent == null) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
            }
            else if (visibilityInfo.mStartParent == null) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
            }
        }
        else if (transitionValues == null && visibilityInfo.mEndVisibility == 0) {
            visibilityInfo.mFadeIn = true;
            visibilityInfo.mVisibilityChange = true;
        }
        else if (transitionValues2 == null && visibilityInfo.mStartVisibility == 0) {
            visibilityInfo.mFadeIn = false;
            visibilityInfo.mVisibilityChange = true;
        }
        return visibilityInfo;
    }
    
    @Override
    public void captureEndValues(final TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }
    
    @Override
    public void captureStartValues(final TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }
    
    @Override
    public Animator createAnimator(final ViewGroup viewGroup, final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        final VisibilityInfo visibilityChangeInfo = this.getVisibilityChangeInfo(transitionValues, transitionValues2);
        if (!visibilityChangeInfo.mVisibilityChange || (visibilityChangeInfo.mStartParent == null && visibilityChangeInfo.mEndParent == null)) {
            return null;
        }
        if (visibilityChangeInfo.mFadeIn) {
            return this.onAppear(viewGroup, transitionValues, visibilityChangeInfo.mStartVisibility, transitionValues2, visibilityChangeInfo.mEndVisibility);
        }
        return this.onDisappear(viewGroup, transitionValues, visibilityChangeInfo.mStartVisibility, transitionValues2, visibilityChangeInfo.mEndVisibility);
    }
    
    @Override
    public String[] getTransitionProperties() {
        return Visibility.sTransitionProperties;
    }
    
    @Override
    public boolean isTransitionRequired(final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        final boolean b = false;
        if (transitionValues == null && transitionValues2 == null) {
            return false;
        }
        if (transitionValues != null && transitionValues2 != null && transitionValues2.values.containsKey("android:visibility:visibility") != transitionValues.values.containsKey("android:visibility:visibility")) {
            return false;
        }
        final VisibilityInfo visibilityChangeInfo = this.getVisibilityChangeInfo(transitionValues, transitionValues2);
        boolean b2 = b;
        if (visibilityChangeInfo.mVisibilityChange) {
            if (visibilityChangeInfo.mStartVisibility != 0) {
                b2 = b;
                if (visibilityChangeInfo.mEndVisibility != 0) {
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    public Animator onAppear(final ViewGroup viewGroup, final TransitionValues transitionValues, final int n, final TransitionValues transitionValues2, final int n2) {
        if ((this.mMode & 0x1) == 0x1 && transitionValues2 != null) {
            if (transitionValues == null) {
                final View view = (View)transitionValues2.view.getParent();
                if (this.getVisibilityChangeInfo(this.getMatchedTransitionValues(view, false), this.getTransitionValues(view, false)).mVisibilityChange) {
                    return null;
                }
            }
            return this.onAppear(viewGroup, transitionValues2.view, transitionValues, transitionValues2);
        }
        return null;
    }
    
    public Animator onAppear(final ViewGroup viewGroup, final View view, final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        return null;
    }
    
    public Animator onDisappear(final ViewGroup viewGroup, final TransitionValues transitionValues, int n, final TransitionValues transitionValues2, int n2) {
        if ((this.mMode & 0x2) != 0x2) {
            return null;
        }
        View view;
        if (transitionValues != null) {
            view = transitionValues.view;
        }
        else {
            view = null;
        }
        View view2;
        if (transitionValues2 != null) {
            view2 = transitionValues2.view;
        }
        else {
            view2 = null;
        }
        View view4 = null;
    Label_0264:
        while (true) {
            Label_0145: {
                if (view2 != null && view2.getParent() != null) {
                    if (n2 != 4) {
                        if (view != view2) {
                            if (this.mCanRemoveViews) {
                                break Label_0145;
                            }
                            view2 = TransitionUtils.copyViewImage(viewGroup, view, (View)view.getParent());
                            break Label_0126;
                        }
                    }
                    final View view3 = null;
                    view4 = view2;
                    view2 = view3;
                    break Label_0264;
                }
                if (view2 == null) {
                    if (view != null) {
                        if (view.getParent() == null) {
                            break Label_0145;
                        }
                        if (view.getParent() instanceof View) {
                            final View view5 = (View)view.getParent();
                            if (!this.getVisibilityChangeInfo(this.getTransitionValues(view5, true), this.getMatchedTransitionValues(view5, true)).mVisibilityChange) {
                                view2 = TransitionUtils.copyViewImage(viewGroup, view, view5);
                                break Label_0126;
                            }
                            if (view5.getParent() == null) {
                                n = view5.getId();
                                if (n != -1 && viewGroup.findViewById(n) != null && this.mCanRemoveViews) {
                                    view2 = view;
                                    break Label_0126;
                                }
                            }
                            view2 = null;
                            break Label_0126;
                        }
                    }
                    view2 = (view4 = null);
                    break Label_0264;
                }
                view4 = null;
                break Label_0264;
            }
            view2 = view;
            continue;
        }
        if (view2 != null && transitionValues != null) {
            final int[] array = transitionValues.values.get("android:visibility:screenLocation");
            n = array[0];
            n2 = array[1];
            final int[] array2 = new int[2];
            viewGroup.getLocationOnScreen(array2);
            view2.offsetLeftAndRight(n - array2[0] - view2.getLeft());
            view2.offsetTopAndBottom(n2 - array2[1] - view2.getTop());
            final ViewGroupOverlayImpl overlay = ViewGroupUtils.getOverlay(viewGroup);
            overlay.add(view2);
            final Animator onDisappear = this.onDisappear(viewGroup, view2, transitionValues, transitionValues2);
            if (onDisappear == null) {
                overlay.remove(view2);
            }
            else {
                onDisappear.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        overlay.remove(view2);
                    }
                });
            }
            return onDisappear;
        }
        if (view4 != null) {
            n = view4.getVisibility();
            ViewUtils.setTransitionVisibility(view4, 0);
            final Animator onDisappear2 = this.onDisappear(viewGroup, view4, transitionValues, transitionValues2);
            if (onDisappear2 != null) {
                final DisappearListener disappearListener = new DisappearListener(view4, n2, true);
                onDisappear2.addListener((Animator$AnimatorListener)disappearListener);
                AnimatorUtils.addPauseListener(onDisappear2, disappearListener);
                this.addListener((TransitionListener)disappearListener);
            }
            else {
                ViewUtils.setTransitionVisibility(view4, n);
            }
            return onDisappear2;
        }
        return null;
    }
    
    public Animator onDisappear(final ViewGroup viewGroup, final View view, final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        return null;
    }
    
    public void setMode(final int mMode) {
        if ((mMode & 0xFFFFFFFC) == 0x0) {
            this.mMode = mMode;
            return;
        }
        throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
    }
    
    private static class DisappearListener extends AnimatorListenerAdapter implements AnimatorPauseListenerCompat, TransitionListener
    {
        boolean mCanceled;
        private final int mFinalVisibility;
        private boolean mLayoutSuppressed;
        private final ViewGroup mParent;
        private final boolean mSuppressLayout;
        private final View mView;
        
        DisappearListener(final View mView, final int mFinalVisibility, final boolean mSuppressLayout) {
            this.mCanceled = false;
            this.mView = mView;
            this.mFinalVisibility = mFinalVisibility;
            this.mParent = (ViewGroup)mView.getParent();
            this.mSuppressLayout = mSuppressLayout;
            this.suppressLayout(true);
        }
        
        private void hideViewWhenNotCanceled() {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
                if (this.mParent != null) {
                    this.mParent.invalidate();
                }
            }
            this.suppressLayout(false);
        }
        
        private void suppressLayout(final boolean mLayoutSuppressed) {
            if (this.mSuppressLayout && this.mLayoutSuppressed != mLayoutSuppressed && this.mParent != null) {
                this.mLayoutSuppressed = mLayoutSuppressed;
                ViewGroupUtils.suppressLayout(this.mParent, mLayoutSuppressed);
            }
        }
        
        public void onAnimationCancel(final Animator animator) {
            this.mCanceled = true;
        }
        
        public void onAnimationEnd(final Animator animator) {
            this.hideViewWhenNotCanceled();
        }
        
        public void onAnimationPause(final Animator animator) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
            }
        }
        
        public void onAnimationRepeat(final Animator animator) {
        }
        
        public void onAnimationResume(final Animator animator) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, 0);
            }
        }
        
        public void onAnimationStart(final Animator animator) {
        }
        
        public void onTransitionEnd(final Transition transition) {
            this.hideViewWhenNotCanceled();
            transition.removeListener((TransitionListener)this);
        }
        
        public void onTransitionPause(final Transition transition) {
            this.suppressLayout(false);
        }
        
        public void onTransitionResume(final Transition transition) {
            this.suppressLayout(true);
        }
        
        public void onTransitionStart(final Transition transition) {
        }
    }
    
    private static class VisibilityInfo
    {
        ViewGroup mEndParent;
        int mEndVisibility;
        boolean mFadeIn;
        ViewGroup mStartParent;
        int mStartVisibility;
        boolean mVisibilityChange;
        
        VisibilityInfo() {
        }
    }
}
