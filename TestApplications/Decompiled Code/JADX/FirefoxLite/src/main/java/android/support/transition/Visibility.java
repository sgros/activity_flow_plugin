package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.transition.Transition.TransitionListener;
import android.view.View;
import android.view.ViewGroup;

public abstract class Visibility extends Transition {
    private static final String[] sTransitionProperties = new String[]{"android:visibility:visibility", "android:visibility:parent"};
    private int mMode = 3;

    private static class VisibilityInfo {
        ViewGroup mEndParent;
        int mEndVisibility;
        boolean mFadeIn;
        ViewGroup mStartParent;
        int mStartVisibility;
        boolean mVisibilityChange;

        VisibilityInfo() {
        }
    }

    private static class DisappearListener extends AnimatorListenerAdapter implements AnimatorPauseListenerCompat, TransitionListener {
        boolean mCanceled = false;
        private final int mFinalVisibility;
        private boolean mLayoutSuppressed;
        private final ViewGroup mParent;
        private final boolean mSuppressLayout;
        private final View mView;

        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
        }

        public void onTransitionStart(Transition transition) {
        }

        DisappearListener(View view, int i, boolean z) {
            this.mView = view;
            this.mFinalVisibility = i;
            this.mParent = (ViewGroup) view.getParent();
            this.mSuppressLayout = z;
            suppressLayout(true);
        }

        public void onAnimationPause(Animator animator) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
            }
        }

        public void onAnimationResume(Animator animator) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, 0);
            }
        }

        public void onAnimationCancel(Animator animator) {
            this.mCanceled = true;
        }

        public void onAnimationEnd(Animator animator) {
            hideViewWhenNotCanceled();
        }

        public void onTransitionEnd(Transition transition) {
            hideViewWhenNotCanceled();
            transition.removeListener(this);
        }

        public void onTransitionPause(Transition transition) {
            suppressLayout(false);
        }

        public void onTransitionResume(Transition transition) {
            suppressLayout(true);
        }

        private void hideViewWhenNotCanceled() {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
                if (this.mParent != null) {
                    this.mParent.invalidate();
                }
            }
            suppressLayout(false);
        }

        private void suppressLayout(boolean z) {
            if (this.mSuppressLayout && this.mLayoutSuppressed != z && this.mParent != null) {
                this.mLayoutSuppressed = z;
                ViewGroupUtils.suppressLayout(this.mParent, z);
            }
        }
    }

    public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    public void setMode(int i) {
        if ((i & -4) == 0) {
            this.mMode = i;
            return;
        }
        throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put("android:visibility:visibility", Integer.valueOf(transitionValues.view.getVisibility()));
        transitionValues.values.put("android:visibility:parent", transitionValues.view.getParent());
        int[] iArr = new int[2];
        transitionValues.view.getLocationOnScreen(iArr);
        transitionValues.values.put("android:visibility:screenLocation", iArr);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private VisibilityInfo getVisibilityChangeInfo(TransitionValues transitionValues, TransitionValues transitionValues2) {
        VisibilityInfo visibilityInfo = new VisibilityInfo();
        visibilityInfo.mVisibilityChange = false;
        visibilityInfo.mFadeIn = false;
        if (transitionValues == null || !transitionValues.values.containsKey("android:visibility:visibility")) {
            visibilityInfo.mStartVisibility = -1;
            visibilityInfo.mStartParent = null;
        } else {
            visibilityInfo.mStartVisibility = ((Integer) transitionValues.values.get("android:visibility:visibility")).intValue();
            visibilityInfo.mStartParent = (ViewGroup) transitionValues.values.get("android:visibility:parent");
        }
        if (transitionValues2 == null || !transitionValues2.values.containsKey("android:visibility:visibility")) {
            visibilityInfo.mEndVisibility = -1;
            visibilityInfo.mEndParent = null;
        } else {
            visibilityInfo.mEndVisibility = ((Integer) transitionValues2.values.get("android:visibility:visibility")).intValue();
            visibilityInfo.mEndParent = (ViewGroup) transitionValues2.values.get("android:visibility:parent");
        }
        if (transitionValues == null || transitionValues2 == null) {
            if (transitionValues == null && visibilityInfo.mEndVisibility == 0) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
            } else if (transitionValues2 == null && visibilityInfo.mStartVisibility == 0) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
            }
        } else if (visibilityInfo.mStartVisibility == visibilityInfo.mEndVisibility && visibilityInfo.mStartParent == visibilityInfo.mEndParent) {
            return visibilityInfo;
        } else {
            if (visibilityInfo.mStartVisibility != visibilityInfo.mEndVisibility) {
                if (visibilityInfo.mStartVisibility == 0) {
                    visibilityInfo.mFadeIn = false;
                    visibilityInfo.mVisibilityChange = true;
                } else if (visibilityInfo.mEndVisibility == 0) {
                    visibilityInfo.mFadeIn = true;
                    visibilityInfo.mVisibilityChange = true;
                }
            } else if (visibilityInfo.mEndParent == null) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
            } else if (visibilityInfo.mStartParent == null) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
            }
        }
        return visibilityInfo;
    }

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        VisibilityInfo visibilityChangeInfo = getVisibilityChangeInfo(transitionValues, transitionValues2);
        if (!visibilityChangeInfo.mVisibilityChange || (visibilityChangeInfo.mStartParent == null && visibilityChangeInfo.mEndParent == null)) {
            return null;
        }
        if (visibilityChangeInfo.mFadeIn) {
            return onAppear(viewGroup, transitionValues, visibilityChangeInfo.mStartVisibility, transitionValues2, visibilityChangeInfo.mEndVisibility);
        }
        return onDisappear(viewGroup, transitionValues, visibilityChangeInfo.mStartVisibility, transitionValues2, visibilityChangeInfo.mEndVisibility);
    }

    public Animator onAppear(ViewGroup viewGroup, TransitionValues transitionValues, int i, TransitionValues transitionValues2, int i2) {
        if ((this.mMode & 1) != 1 || transitionValues2 == null) {
            return null;
        }
        if (transitionValues == null) {
            View view = (View) transitionValues2.view.getParent();
            if (getVisibilityChangeInfo(getMatchedTransitionValues(view, false), getTransitionValues(view, false)).mVisibilityChange) {
                return null;
            }
        }
        return onAppear(viewGroup, transitionValues2.view, transitionValues, transitionValues2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ee A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0087 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ee A:{RETURN} */
    /* JADX WARNING: Missing block: B:38:0x007d, code skipped:
            if (r6.mCanRemoveViews != false) goto L_0x003a;
     */
    public android.animation.Animator onDisappear(android.view.ViewGroup r7, android.support.transition.TransitionValues r8, int r9, android.support.transition.TransitionValues r10, int r11) {
        /*
        r6 = this;
        r9 = r6.mMode;
        r0 = 2;
        r9 = r9 & r0;
        r1 = 0;
        if (r9 == r0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        if (r8 == 0) goto L_0x000d;
    L_0x000a:
        r9 = r8.view;
        goto L_0x000e;
    L_0x000d:
        r9 = r1;
    L_0x000e:
        if (r10 == 0) goto L_0x0013;
    L_0x0010:
        r2 = r10.view;
        goto L_0x0014;
    L_0x0013:
        r2 = r1;
    L_0x0014:
        r3 = 1;
        if (r2 == 0) goto L_0x0037;
    L_0x0017:
        r4 = r2.getParent();
        if (r4 != 0) goto L_0x001e;
    L_0x001d:
        goto L_0x0037;
    L_0x001e:
        r4 = 4;
        if (r11 != r4) goto L_0x0022;
    L_0x0021:
        goto L_0x0024;
    L_0x0022:
        if (r9 != r2) goto L_0x0027;
    L_0x0024:
        r9 = r1;
        goto L_0x0084;
    L_0x0027:
        r2 = r6.mCanRemoveViews;
        if (r2 == 0) goto L_0x002c;
    L_0x002b:
        goto L_0x0044;
    L_0x002c:
        r2 = r9.getParent();
        r2 = (android.view.View) r2;
        r9 = android.support.transition.TransitionUtils.copyViewImage(r7, r9, r2);
        goto L_0x003a;
    L_0x0037:
        if (r2 == 0) goto L_0x003c;
    L_0x0039:
        r9 = r2;
    L_0x003a:
        r2 = r1;
        goto L_0x0084;
    L_0x003c:
        if (r9 == 0) goto L_0x0082;
    L_0x003e:
        r2 = r9.getParent();
        if (r2 != 0) goto L_0x0045;
    L_0x0044:
        goto L_0x003a;
    L_0x0045:
        r2 = r9.getParent();
        r2 = r2 instanceof android.view.View;
        if (r2 == 0) goto L_0x0082;
    L_0x004d:
        r2 = r9.getParent();
        r2 = (android.view.View) r2;
        r4 = r6.getTransitionValues(r2, r3);
        r5 = r6.getMatchedTransitionValues(r2, r3);
        r4 = r6.getVisibilityChangeInfo(r4, r5);
        r4 = r4.mVisibilityChange;
        if (r4 != 0) goto L_0x0068;
    L_0x0063:
        r9 = android.support.transition.TransitionUtils.copyViewImage(r7, r9, r2);
        goto L_0x003a;
    L_0x0068:
        r4 = r2.getParent();
        if (r4 != 0) goto L_0x0080;
    L_0x006e:
        r2 = r2.getId();
        r4 = -1;
        if (r2 == r4) goto L_0x0080;
    L_0x0075:
        r2 = r7.findViewById(r2);
        if (r2 == 0) goto L_0x0080;
    L_0x007b:
        r2 = r6.mCanRemoveViews;
        if (r2 == 0) goto L_0x0080;
    L_0x007f:
        goto L_0x003a;
    L_0x0080:
        r9 = r1;
        goto L_0x003a;
    L_0x0082:
        r9 = r1;
        r2 = r9;
    L_0x0084:
        r4 = 0;
        if (r9 == 0) goto L_0x00cc;
    L_0x0087:
        if (r8 == 0) goto L_0x00cc;
    L_0x0089:
        r11 = r8.values;
        r1 = "android:visibility:screenLocation";
        r11 = r11.get(r1);
        r11 = (int[]) r11;
        r1 = r11[r4];
        r11 = r11[r3];
        r0 = new int[r0];
        r7.getLocationOnScreen(r0);
        r2 = r0[r4];
        r1 = r1 - r2;
        r2 = r9.getLeft();
        r1 = r1 - r2;
        r9.offsetLeftAndRight(r1);
        r0 = r0[r3];
        r11 = r11 - r0;
        r0 = r9.getTop();
        r11 = r11 - r0;
        r9.offsetTopAndBottom(r11);
        r11 = android.support.transition.ViewGroupUtils.getOverlay(r7);
        r11.add(r9);
        r7 = r6.onDisappear(r7, r9, r8, r10);
        if (r7 != 0) goto L_0x00c3;
    L_0x00bf:
        r11.remove(r9);
        goto L_0x00cb;
    L_0x00c3:
        r8 = new android.support.transition.Visibility$1;
        r8.<init>(r11, r9);
        r7.addListener(r8);
    L_0x00cb:
        return r7;
    L_0x00cc:
        if (r2 == 0) goto L_0x00ee;
    L_0x00ce:
        r9 = r2.getVisibility();
        android.support.transition.ViewUtils.setTransitionVisibility(r2, r4);
        r7 = r6.onDisappear(r7, r2, r8, r10);
        if (r7 == 0) goto L_0x00ea;
    L_0x00db:
        r8 = new android.support.transition.Visibility$DisappearListener;
        r8.<init>(r2, r11, r3);
        r7.addListener(r8);
        android.support.transition.AnimatorUtils.addPauseListener(r7, r8);
        r6.addListener(r8);
        goto L_0x00ed;
    L_0x00ea:
        android.support.transition.ViewUtils.setTransitionVisibility(r2, r9);
    L_0x00ed:
        return r7;
    L_0x00ee:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.Visibility.onDisappear(android.view.ViewGroup, android.support.transition.TransitionValues, int, android.support.transition.TransitionValues, int):android.animation.Animator");
    }

    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        boolean z = false;
        if (transitionValues == null && transitionValues2 == null) {
            return false;
        }
        if (transitionValues != null && transitionValues2 != null && transitionValues2.values.containsKey("android:visibility:visibility") != transitionValues.values.containsKey("android:visibility:visibility")) {
            return false;
        }
        VisibilityInfo visibilityChangeInfo = getVisibilityChangeInfo(transitionValues, transitionValues2);
        if (visibilityChangeInfo.mVisibilityChange && (visibilityChangeInfo.mStartVisibility == 0 || visibilityChangeInfo.mEndVisibility == 0)) {
            z = true;
        }
        return z;
    }
}