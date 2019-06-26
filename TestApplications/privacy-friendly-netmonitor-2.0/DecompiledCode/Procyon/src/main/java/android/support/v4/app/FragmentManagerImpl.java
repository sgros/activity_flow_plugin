// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.support.annotation.CallSuper;
import android.support.v4.util.DebugUtils;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.animation.AnimatorInflater;
import android.content.res.Resources$NotFoundException;
import android.view.animation.AnimationUtils;
import java.util.Collections;
import java.util.Arrays;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Configuration;
import java.io.FileDescriptor;
import java.io.Writer;
import java.io.PrintWriter;
import android.support.v4.util.LogWriter;
import android.support.v4.view.ViewCompat;
import android.os.Build$VERSION;
import java.util.Iterator;
import android.graphics.Paint;
import java.util.List;
import android.animation.PropertyValuesHolder;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.ScaleAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AlphaAnimation;
import android.content.Context;
import android.util.Log;
import java.util.Collection;
import android.os.Looper;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.view.ViewGroup;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import android.util.SparseArray;
import java.lang.reflect.Field;
import android.view.animation.Interpolator;
import android.view.LayoutInflater$Factory2;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflater$Factory2
{
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    SparseArray<Fragment> mActive;
    final ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private final CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks;
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    SparseArray<Parcelable> mStateArray;
    Bundle mStateBundle;
    boolean mStateSaved;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;
    
    static {
        DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = (Interpolator)new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = (Interpolator)new AccelerateInterpolator(1.5f);
    }
    
    FragmentManagerImpl() {
        this.mNextFragmentIndex = 0;
        this.mAdded = new ArrayList<Fragment>();
        this.mLifecycleCallbacks = new CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>>();
        this.mCurState = 0;
        this.mStateBundle = null;
        this.mStateArray = null;
        this.mExecCommit = new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.execPendingActions();
            }
        };
    }
    
    private void addAddedFragments(final ArraySet<Fragment> set) {
        if (this.mCurState < 1) {
            return;
        }
        final int min = Math.min(this.mCurState, 4);
        for (int size = this.mAdded.size(), i = 0; i < size; ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment.mState < min) {
                this.moveToState(fragment, min, fragment.getNextAnim(), fragment.getNextTransition(), false);
                if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                    set.add(fragment);
                }
            }
        }
    }
    
    private void animateRemoveFragment(@NonNull final Fragment fragment, @NonNull final AnimationOrAnimator animationOrAnimator, final int stateAfterAnimating) {
        final View mView = fragment.mView;
        fragment.setStateAfterAnimating(stateAfterAnimating);
        if (animationOrAnimator.animation != null) {
            final Animation animation = animationOrAnimator.animation;
            fragment.setAnimatingAway(fragment.mView);
            animation.setAnimationListener((Animation$AnimationListener)new AnimationListenerWrapper(getAnimationListener(animation)) {
                @Override
                public void onAnimationEnd(final Animation animation) {
                    super.onAnimationEnd(animation);
                    if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway(null);
                        FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                    }
                }
            });
            setHWLayerAnimListenerIfAlpha(mView, animationOrAnimator);
            fragment.mView.startAnimation(animation);
        }
        else {
            final Animator animator = animationOrAnimator.animator;
            fragment.setAnimator(animationOrAnimator.animator);
            final ViewGroup mContainer = fragment.mContainer;
            if (mContainer != null) {
                mContainer.startViewTransition(mView);
            }
            animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    if (mContainer != null) {
                        mContainer.endViewTransition(mView);
                    }
                    if (fragment.getAnimator() != null) {
                        fragment.setAnimator(null);
                        FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                    }
                }
            });
            animator.setTarget((Object)fragment.mView);
            setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
            animator.start();
        }
    }
    
    private void burpActive() {
        if (this.mActive != null) {
            for (int i = this.mActive.size() - 1; i >= 0; --i) {
                if (this.mActive.valueAt(i) == null) {
                    this.mActive.delete(this.mActive.keyAt(i));
                }
            }
        }
    }
    
    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
        if (this.mNoTransactionsBecause != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can not perform this action inside of ");
            sb.append(this.mNoTransactionsBecause);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }
    
    private void completeExecute(final BackStackRecord e, final boolean b, final boolean b2, final boolean b3) {
        if (b) {
            e.executePopOps(b3);
        }
        else {
            e.executeOps();
        }
        final ArrayList<BackStackRecord> list = new ArrayList<BackStackRecord>(1);
        final ArrayList<Boolean> list2 = new ArrayList<Boolean>(1);
        list.add(e);
        list2.add(b);
        if (b2) {
            FragmentTransition.startTransitions(this, list, list2, 0, 1, true);
        }
        if (b3) {
            this.moveToState(this.mCurState, true);
        }
        if (this.mActive != null) {
            for (int size = this.mActive.size(), i = 0; i < size; ++i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && e.interactsWith(fragment.mContainerId)) {
                    if (fragment.mPostponedAlpha > 0.0f) {
                        fragment.mView.setAlpha(fragment.mPostponedAlpha);
                    }
                    if (b3) {
                        fragment.mPostponedAlpha = 0.0f;
                    }
                    else {
                        fragment.mPostponedAlpha = -1.0f;
                        fragment.mIsNewlyAdded = false;
                    }
                }
            }
        }
    }
    
    private void dispatchStateChange(final int n) {
        try {
            this.mExecutingActions = true;
            this.moveToState(n, false);
            this.mExecutingActions = false;
            this.execPendingActions();
        }
        finally {
            this.mExecutingActions = false;
        }
    }
    
    private void endAnimatingAwayFragments() {
        final SparseArray<Fragment> mActive = this.mActive;
        int i = 0;
        int size;
        if (mActive == null) {
            size = 0;
        }
        else {
            size = this.mActive.size();
        }
        while (i < size) {
            final Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment != null) {
                if (fragment.getAnimatingAway() != null) {
                    final int stateAfterAnimating = fragment.getStateAfterAnimating();
                    final View animatingAway = fragment.getAnimatingAway();
                    fragment.setAnimatingAway(null);
                    final Animation animation = animatingAway.getAnimation();
                    if (animation != null) {
                        animation.cancel();
                        animatingAway.clearAnimation();
                    }
                    this.moveToState(fragment, stateAfterAnimating, 0, 0, false);
                }
                else if (fragment.getAnimator() != null) {
                    fragment.getAnimator().end();
                }
            }
            ++i;
        }
    }
    
    private void ensureExecReady(final boolean b) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        }
        if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
        if (!b) {
            this.checkStateLoss();
        }
        if (this.mTmpRecords == null) {
            this.mTmpRecords = new ArrayList<BackStackRecord>();
            this.mTmpIsPop = new ArrayList<Boolean>();
        }
        this.mExecutingActions = true;
        try {
            this.executePostponedTransaction(null, null);
        }
        finally {
            this.mExecutingActions = false;
        }
    }
    
    private static void executeOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, int i, final int n) {
        while (i < n) {
            final BackStackRecord backStackRecord = list.get(i);
            final boolean booleanValue = list2.get(i);
            boolean b = true;
            if (booleanValue) {
                backStackRecord.bumpBackStackNesting(-1);
                if (i != n - 1) {
                    b = false;
                }
                backStackRecord.executePopOps(b);
            }
            else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
            ++i;
        }
    }
    
    private void executeOpsTogether(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, int i, final int n) {
        final int index = i;
        final boolean mReorderingAllowed = list.get(index).mReorderingAllowed;
        if (this.mTmpAddedFragments == null) {
            this.mTmpAddedFragments = new ArrayList<Fragment>();
        }
        else {
            this.mTmpAddedFragments.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        Fragment fragment = this.getPrimaryNavigationFragment();
        int n2 = 0;
        for (int j = index; j < n; ++j) {
            final BackStackRecord backStackRecord = list.get(j);
            if (!list2.get(j)) {
                fragment = backStackRecord.expandOps(this.mTmpAddedFragments, fragment);
            }
            else {
                fragment = backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, fragment);
            }
            if (n2 == 0 && !backStackRecord.mAddToBackStack) {
                n2 = 0;
            }
            else {
                n2 = 1;
            }
        }
        this.mTmpAddedFragments.clear();
        if (!mReorderingAllowed) {
            FragmentTransition.startTransitions(this, list, list2, index, n, false);
        }
        executeOps(list, list2, i, n);
        int postponePostponableTransactions;
        if (mReorderingAllowed) {
            final ArraySet<Fragment> set = new ArraySet<Fragment>();
            this.addAddedFragments(set);
            postponePostponableTransactions = this.postponePostponableTransactions(list, list2, index, n, set);
            this.makeRemovedFragmentsInvisible(set);
        }
        else {
            postponePostponableTransactions = n;
        }
        i = index;
        if (postponePostponableTransactions != index) {
            i = index;
            if (mReorderingAllowed) {
                FragmentTransition.startTransitions(this, list, list2, index, postponePostponableTransactions, true);
                this.moveToState(this.mCurState, true);
                i = index;
            }
        }
        while (i < n) {
            final BackStackRecord backStackRecord2 = list.get(i);
            if (list2.get(i) && backStackRecord2.mIndex >= 0) {
                this.freeBackStackIndex(backStackRecord2.mIndex);
                backStackRecord2.mIndex = -1;
            }
            backStackRecord2.runOnCommitRunnables();
            ++i;
        }
        if (n2 != 0) {
            this.reportBackStackChanged();
        }
    }
    
    private void executePostponedTransaction(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        int size;
        if (this.mPostponedTransactions == null) {
            size = 0;
        }
        else {
            size = this.mPostponedTransactions.size();
        }
        final int n = 0;
        int n3;
        int n4;
        for (int n2 = size, i = n; i < n2; i = n3 + 1, n2 = n4) {
            final StartEnterTransitionListener startEnterTransitionListener = this.mPostponedTransactions.get(i);
            if (list != null && !startEnterTransitionListener.mIsBack) {
                final int index = list.indexOf(startEnterTransitionListener.mRecord);
                if (index != -1 && list2.get(index)) {
                    startEnterTransitionListener.cancelTransaction();
                    n3 = i;
                    n4 = n2;
                    continue;
                }
            }
            if (!startEnterTransitionListener.isReady()) {
                n3 = i;
                n4 = n2;
                if (list == null) {
                    continue;
                }
                n3 = i;
                n4 = n2;
                if (!startEnterTransitionListener.mRecord.interactsWith(list, 0, list.size())) {
                    continue;
                }
            }
            this.mPostponedTransactions.remove(i);
            n3 = i - 1;
            n4 = n2 - 1;
            if (list != null && !startEnterTransitionListener.mIsBack) {
                final int index2 = list.indexOf(startEnterTransitionListener.mRecord);
                if (index2 != -1 && list2.get(index2)) {
                    startEnterTransitionListener.cancelTransaction();
                    continue;
                }
            }
            startEnterTransitionListener.completeTransaction();
        }
    }
    
    private Fragment findFragmentUnder(Fragment o) {
        final ViewGroup mContainer = o.mContainer;
        final View mView = o.mView;
        if (mContainer != null && mView != null) {
            for (int i = this.mAdded.indexOf(o) - 1; i >= 0; --i) {
                o = this.mAdded.get(i);
                if (o.mContainer == mContainer && o.mView != null) {
                    return o;
                }
            }
            return null;
        }
        return null;
    }
    
    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }
    
    private boolean generateOpsForPendingActions(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        synchronized (this) {
            final ArrayList<OpGenerator> mPendingActions = this.mPendingActions;
            int i = 0;
            if (mPendingActions != null && this.mPendingActions.size() != 0) {
                final int size = this.mPendingActions.size();
                boolean b = false;
                while (i < size) {
                    b |= this.mPendingActions.get(i).generateOps(list, list2);
                    ++i;
                }
                this.mPendingActions.clear();
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                return b;
            }
            return false;
        }
    }
    
    private static Animation$AnimationListener getAnimationListener(final Animation obj) {
        try {
            if (FragmentManagerImpl.sAnimationListenerField == null) {
                (FragmentManagerImpl.sAnimationListenerField = Animation.class.getDeclaredField("mListener")).setAccessible(true);
            }
            return (Animation$AnimationListener)FragmentManagerImpl.sAnimationListenerField.get(obj);
        }
        catch (IllegalAccessException ex) {
            Log.e("FragmentManager", "Cannot access Animation's mListener field", (Throwable)ex);
        }
        catch (NoSuchFieldException ex2) {
            Log.e("FragmentManager", "No field with the name mListener is found in Animation class", (Throwable)ex2);
        }
        return null;
    }
    
    static AnimationOrAnimator makeFadeAnimation(final Context context, final float n, final float n2) {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n, n2);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        return new AnimationOrAnimator((Animation)alphaAnimation);
    }
    
    static AnimationOrAnimator makeOpenCloseAnimation(final Context context, final float n, final float n2, final float n3, final float n4) {
        final AnimationSet set = new AnimationSet(false);
        final ScaleAnimation scaleAnimation = new ScaleAnimation(n, n2, n, n2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_QUINT);
        scaleAnimation.setDuration(220L);
        set.addAnimation((Animation)scaleAnimation);
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n3, n4);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        set.addAnimation((Animation)alphaAnimation);
        return new AnimationOrAnimator((Animation)set);
    }
    
    private void makeRemovedFragmentsInvisible(final ArraySet<Fragment> set) {
        for (int size = set.size(), i = 0; i < size; ++i) {
            final Fragment fragment = set.valueAt(i);
            if (!fragment.mAdded) {
                final View view = fragment.getView();
                fragment.mPostponedAlpha = view.getAlpha();
                view.setAlpha(0.0f);
            }
        }
    }
    
    static boolean modifiesAlpha(final Animator animator) {
        if (animator == null) {
            return false;
        }
        if (animator instanceof ValueAnimator) {
            final PropertyValuesHolder[] values = ((ValueAnimator)animator).getValues();
            for (int i = 0; i < values.length; ++i) {
                if ("alpha".equals(values[i].getPropertyName())) {
                    return true;
                }
            }
        }
        else if (animator instanceof AnimatorSet) {
            final ArrayList childAnimations = ((AnimatorSet)animator).getChildAnimations();
            for (int j = 0; j < childAnimations.size(); ++j) {
                if (modifiesAlpha((Animator)childAnimations.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static boolean modifiesAlpha(final AnimationOrAnimator animationOrAnimator) {
        if (animationOrAnimator.animation instanceof AlphaAnimation) {
            return true;
        }
        if (animationOrAnimator.animation instanceof AnimationSet) {
            final List animations = ((AnimationSet)animationOrAnimator.animation).getAnimations();
            for (int i = 0; i < animations.size(); ++i) {
                if (animations.get(i) instanceof AlphaAnimation) {
                    return true;
                }
            }
            return false;
        }
        return modifiesAlpha(animationOrAnimator.animator);
    }
    
    private boolean popBackStackImmediate(final String s, final int n, final int n2) {
        this.execPendingActions();
        this.ensureExecReady(true);
        if (this.mPrimaryNav != null && n < 0 && s == null) {
            final FragmentManager peekChildFragmentManager = this.mPrimaryNav.peekChildFragmentManager();
            if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                return true;
            }
        }
        final boolean popBackStackState = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, s, n, n2);
        if (popBackStackState) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.doPendingDeferredStart();
        this.burpActive();
        return popBackStackState;
    }
    
    private int postponePostponableTransactions(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final int n, final int n2, final ArraySet<Fragment> set) {
        int i = n2 - 1;
        int n3 = n2;
        while (i >= n) {
            final BackStackRecord element = list.get(i);
            final boolean booleanValue = list2.get(i);
            final boolean b = element.isPostponed() && !element.interactsWith(list, i + 1, n2);
            int index = n3;
            if (b) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<StartEnterTransitionListener>();
                }
                final StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(element, booleanValue);
                this.mPostponedTransactions.add(startEnterTransitionListener);
                element.setOnStartPostponedListener(startEnterTransitionListener);
                if (booleanValue) {
                    element.executeOps();
                }
                else {
                    element.executePopOps(false);
                }
                index = n3 - 1;
                if (i != index) {
                    list.remove(i);
                    list.add(index, element);
                }
                this.addAddedFragments(set);
            }
            --i;
            n3 = index;
        }
        return n3;
    }
    
    private void removeRedundantOperationsAndExecute(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (list2 != null && list.size() == list2.size()) {
            this.executePostponedTransaction(list, list2);
            final int size = list.size();
            int i = 0;
            int n = 0;
            while (i < size) {
                int n2 = i;
                int n3 = n;
                if (!list.get(i).mReorderingAllowed) {
                    if (n != i) {
                        this.executeOpsTogether(list, list2, n, i);
                    }
                    int n4 = n3 = i + 1;
                    if (list2.get(i)) {
                        while ((n3 = n4) < size) {
                            n3 = n4;
                            if (!list2.get(n4)) {
                                break;
                            }
                            n3 = n4;
                            if (list.get(n4).mReorderingAllowed) {
                                break;
                            }
                            ++n4;
                        }
                    }
                    this.executeOpsTogether(list, list2, i, n3);
                    n2 = n3 - 1;
                }
                i = n2 + 1;
                n = n3;
            }
            if (n != size) {
                this.executeOpsTogether(list, list2, n, size);
            }
            return;
        }
        throw new IllegalStateException("Internal error with the back stack records");
    }
    
    public static int reverseTransit(final int n) {
        int n2 = 8194;
        if (n != 4097) {
            if (n != 4099) {
                if (n != 8194) {
                    n2 = 0;
                }
                else {
                    n2 = 4097;
                }
            }
            else {
                n2 = 4099;
            }
        }
        return n2;
    }
    
    private void scheduleCommit() {
        synchronized (this) {
            final ArrayList<StartEnterTransitionListener> mPostponedTransactions = this.mPostponedTransactions;
            final int n = 0;
            final boolean b = mPostponedTransactions != null && !this.mPostponedTransactions.isEmpty();
            int n2 = n;
            if (this.mPendingActions != null) {
                n2 = n;
                if (this.mPendingActions.size() == 1) {
                    n2 = 1;
                }
            }
            if (b || n2 != 0) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
        }
    }
    
    private static void setHWLayerAnimListenerIfAlpha(final View view, final AnimationOrAnimator animationOrAnimator) {
        if (view != null && animationOrAnimator != null) {
            if (shouldRunOnHWLayer(view, animationOrAnimator)) {
                if (animationOrAnimator.animator != null) {
                    animationOrAnimator.animator.addListener((Animator$AnimatorListener)new AnimatorOnHWLayerIfNeededListener(view));
                }
                else {
                    final Animation$AnimationListener animationListener = getAnimationListener(animationOrAnimator.animation);
                    view.setLayerType(2, (Paint)null);
                    animationOrAnimator.animation.setAnimationListener((Animation$AnimationListener)new AnimateOnHWLayerIfNeededListener(view, animationListener));
                }
            }
        }
    }
    
    private static void setRetaining(final FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (fragmentManagerNonConfig == null) {
            return;
        }
        final List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
        if (fragments != null) {
            final Iterator<Fragment> iterator = fragments.iterator();
            while (iterator.hasNext()) {
                iterator.next().mRetaining = true;
            }
        }
        final List<FragmentManagerNonConfig> childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
        if (childNonConfigs != null) {
            final Iterator<FragmentManagerNonConfig> iterator2 = childNonConfigs.iterator();
            while (iterator2.hasNext()) {
                setRetaining(iterator2.next());
            }
        }
    }
    
    static boolean shouldRunOnHWLayer(final View view, final AnimationOrAnimator animationOrAnimator) {
        final boolean b = false;
        if (view != null && animationOrAnimator != null) {
            boolean b2 = b;
            if (Build$VERSION.SDK_INT >= 19) {
                b2 = b;
                if (view.getLayerType() == 0) {
                    b2 = b;
                    if (ViewCompat.hasOverlappingRendering(view)) {
                        b2 = b;
                        if (modifiesAlpha(animationOrAnimator)) {
                            b2 = true;
                        }
                    }
                }
            }
            return b2;
        }
        return false;
    }
    
    private void throwException(final RuntimeException ex) {
        Log.e("FragmentManager", ex.getMessage());
        Log.e("FragmentManager", "Activity state:");
        final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
        if (this.mHost != null) {
            try {
                this.mHost.onDump("  ", null, printWriter, new String[0]);
            }
            catch (Exception ex2) {
                Log.e("FragmentManager", "Failed dumping state", (Throwable)ex2);
            }
        }
        else {
            try {
                this.dump("  ", null, printWriter, new String[0]);
            }
            catch (Exception ex3) {
                Log.e("FragmentManager", "Failed dumping state", (Throwable)ex3);
            }
        }
        throw ex;
    }
    
    public static int transitToStyleIndex(int n, final boolean b) {
        if (n != 4097) {
            if (n != 4099) {
                if (n != 8194) {
                    n = -1;
                }
                else if (b) {
                    n = 3;
                }
                else {
                    n = 4;
                }
            }
            else if (b) {
                n = 5;
            }
            else {
                n = 6;
            }
        }
        else if (b) {
            n = 1;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    void addBackStackState(final BackStackRecord e) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<BackStackRecord>();
        }
        this.mBackStack.add(e);
    }
    
    public void addFragment(final Fragment fragment, final boolean b) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("add: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        this.makeActive(fragment);
        if (!fragment.mDetached) {
            if (this.mAdded.contains(fragment)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Fragment already added: ");
                sb2.append(fragment);
                throw new IllegalStateException(sb2.toString());
            }
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
                // monitorexit(this.mAdded)
                fragment.mAdded = true;
                fragment.mRemoving = false;
                if (fragment.mView == null) {
                    fragment.mHiddenChanged = false;
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                if (b) {
                    this.moveToState(fragment);
                }
            }
        }
    }
    
    @Override
    public void addOnBackStackChangedListener(final OnBackStackChangedListener e) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<OnBackStackChangedListener>();
        }
        this.mBackStackChangeListeners.add(e);
    }
    
    public int allocBackStackIndex(final BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                final int intValue = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Adding back stack index ");
                    sb.append(intValue);
                    sb.append(" with ");
                    sb.append(backStackRecord);
                    Log.v("FragmentManager", sb.toString());
                }
                this.mBackStackIndices.set(intValue, backStackRecord);
                return intValue;
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<BackStackRecord>();
            }
            final int size = this.mBackStackIndices.size();
            if (FragmentManagerImpl.DEBUG) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Setting back stack index ");
                sb2.append(size);
                sb2.append(" to ");
                sb2.append(backStackRecord);
                Log.v("FragmentManager", sb2.toString());
            }
            this.mBackStackIndices.add(backStackRecord);
            return size;
        }
    }
    
    public void attachController(final FragmentHostCallback mHost, final FragmentContainer mContainer, final Fragment mParent) {
        if (this.mHost != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mHost = mHost;
        this.mContainer = mContainer;
        this.mParent = mParent;
    }
    
    public void attachFragment(final Fragment e) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("attach: ");
            sb.append(e);
            Log.v("FragmentManager", sb.toString());
        }
        if (e.mDetached) {
            e.mDetached = false;
            if (!e.mAdded) {
                if (this.mAdded.contains(e)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Fragment already added: ");
                    sb2.append(e);
                    throw new IllegalStateException(sb2.toString());
                }
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("add from attach: ");
                    sb3.append(e);
                    Log.v("FragmentManager", sb3.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.add(e);
                    // monitorexit(this.mAdded)
                    e.mAdded = true;
                    if (e.mHasMenu && e.mMenuVisible) {
                        this.mNeedMenuInvalidate = true;
                    }
                }
            }
        }
    }
    
    @Override
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }
    
    void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            final AnimationOrAnimator loadAnimation = this.loadAnimation(fragment, fragment.getNextTransition(), fragment.mHidden ^ true, fragment.getNextTransitionStyle());
            if (loadAnimation != null && loadAnimation.animator != null) {
                loadAnimation.animator.setTarget((Object)fragment.mView);
                if (fragment.mHidden) {
                    if (fragment.isHideReplaced()) {
                        fragment.setHideReplaced(false);
                    }
                    else {
                        final ViewGroup mContainer = fragment.mContainer;
                        final View mView = fragment.mView;
                        mContainer.startViewTransition(mView);
                        loadAnimation.animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                mContainer.endViewTransition(mView);
                                animator.removeListener((Animator$AnimatorListener)this);
                                if (fragment.mView != null) {
                                    fragment.mView.setVisibility(8);
                                }
                            }
                        });
                    }
                }
                else {
                    fragment.mView.setVisibility(0);
                }
                setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                loadAnimation.animator.start();
            }
            else {
                if (loadAnimation != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                    fragment.mView.startAnimation(loadAnimation.animation);
                    loadAnimation.animation.start();
                }
                int visibility;
                if (fragment.mHidden && !fragment.isHideReplaced()) {
                    visibility = 8;
                }
                else {
                    visibility = 0;
                }
                fragment.mView.setVisibility(visibility);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }
    
    public void detachFragment(final Fragment o) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("detach: ");
            sb.append(o);
            Log.v("FragmentManager", sb.toString());
        }
        if (!o.mDetached) {
            o.mDetached = true;
            if (o.mAdded) {
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("remove from detach: ");
                    sb2.append(o);
                    Log.v("FragmentManager", sb2.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.remove(o);
                    // monitorexit(this.mAdded)
                    if (o.mHasMenu && o.mMenuVisible) {
                        this.mNeedMenuInvalidate = true;
                    }
                    o.mAdded = false;
                }
            }
        }
    }
    
    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.dispatchStateChange(2);
    }
    
    public void dispatchConfigurationChanged(final Configuration configuration) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performConfigurationChanged(configuration);
            }
        }
    }
    
    public boolean dispatchContextItemSelected(final MenuItem menuItem) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
    
    public void dispatchCreate() {
        this.mStateSaved = false;
        this.dispatchStateChange(1);
    }
    
    public boolean dispatchCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        final int n = 0;
        int n2 = 0;
        ArrayList<Fragment> mCreatedMenus = null;
        ArrayList<Fragment> list;
        int n3;
        for (int i = n2; i < this.mAdded.size(); ++i, mCreatedMenus = list, n2 = n3) {
            final Fragment e = this.mAdded.get(i);
            list = mCreatedMenus;
            n3 = n2;
            if (e != null) {
                list = mCreatedMenus;
                n3 = n2;
                if (e.performCreateOptionsMenu(menu, menuInflater)) {
                    if ((list = mCreatedMenus) == null) {
                        list = new ArrayList<Fragment>();
                    }
                    list.add(e);
                    n3 = 1;
                }
            }
        }
        if (this.mCreatedMenus != null) {
            for (int j = n; j < this.mCreatedMenus.size(); ++j) {
                final Fragment o = this.mCreatedMenus.get(j);
                if (mCreatedMenus == null || !mCreatedMenus.contains(o)) {
                    o.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = mCreatedMenus;
        return n2 != 0;
    }
    
    public void dispatchDestroy() {
        this.mDestroyed = true;
        this.execPendingActions();
        this.dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }
    
    public void dispatchDestroyView() {
        this.dispatchStateChange(1);
    }
    
    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performLowMemory();
            }
        }
    }
    
    public void dispatchMultiWindowModeChanged(final boolean b) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performMultiWindowModeChanged(b);
            }
        }
    }
    
    void dispatchOnFragmentActivityCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentActivityCreated(fragment, bundle, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentActivityCreated(this, fragment, bundle);
            }
        }
    }
    
    void dispatchOnFragmentAttached(final Fragment fragment, final Context context, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentAttached(fragment, context, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentAttached(this, fragment, context);
            }
        }
    }
    
    void dispatchOnFragmentCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentCreated(fragment, bundle, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentCreated(this, fragment, bundle);
            }
        }
    }
    
    void dispatchOnFragmentDestroyed(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDestroyed(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentDestroyed(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentDetached(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDetached(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentDetached(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentPaused(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPaused(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentPaused(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentPreAttached(final Fragment fragment, final Context context, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreAttached(fragment, context, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentPreAttached(this, fragment, context);
            }
        }
    }
    
    void dispatchOnFragmentPreCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreCreated(fragment, bundle, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentPreCreated(this, fragment, bundle);
            }
        }
    }
    
    void dispatchOnFragmentResumed(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentResumed(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentResumed(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentSaveInstanceState(final Fragment fragment, final Bundle bundle, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentSaveInstanceState(this, fragment, bundle);
            }
        }
    }
    
    void dispatchOnFragmentStarted(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStarted(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentStarted(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentStopped(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStopped(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentStopped(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentViewCreated(final Fragment fragment, final View view, final Bundle bundle, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewCreated(fragment, view, bundle, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentViewCreated(this, fragment, view, bundle);
            }
        }
    }
    
    void dispatchOnFragmentViewDestroyed(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewDestroyed(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (!b || pair.second) {
                pair.first.onFragmentViewDestroyed(this, fragment);
            }
        }
    }
    
    public boolean dispatchOptionsItemSelected(final MenuItem menuItem) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
    
    public void dispatchOptionsMenuClosed(final Menu menu) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performOptionsMenuClosed(menu);
            }
        }
    }
    
    public void dispatchPause() {
        this.dispatchStateChange(4);
    }
    
    public void dispatchPictureInPictureModeChanged(final boolean b) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performPictureInPictureModeChanged(b);
            }
        }
    }
    
    public boolean dispatchPrepareOptionsMenu(final Menu menu) {
        int i = 0;
        boolean b = false;
        while (i < this.mAdded.size()) {
            final Fragment fragment = this.mAdded.get(i);
            boolean b2 = b;
            if (fragment != null) {
                b2 = b;
                if (fragment.performPrepareOptionsMenu(menu)) {
                    b2 = true;
                }
            }
            ++i;
            b = b2;
        }
        return b;
    }
    
    public void dispatchReallyStop() {
        this.dispatchStateChange(2);
    }
    
    public void dispatchResume() {
        this.mStateSaved = false;
        this.dispatchStateChange(5);
    }
    
    public void dispatchStart() {
        this.mStateSaved = false;
        this.dispatchStateChange(4);
    }
    
    public void dispatchStop() {
        this.mStateSaved = true;
        this.dispatchStateChange(3);
    }
    
    void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            boolean b;
            boolean b2;
            for (int i = (b = false) ? 1 : 0; i < this.mActive.size(); ++i, b = b2) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                b2 = b;
                if (fragment != null) {
                    b2 = b;
                    if (fragment.mLoaderManager != null) {
                        b2 = (b | fragment.mLoaderManager.hasRunningLoaders());
                    }
                }
            }
            if (!b) {
                this.mHavePendingDeferredStart = false;
                this.startPendingDeferredFragments();
            }
        }
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("    ");
        final String string = sb.toString();
        final SparseArray<Fragment> mActive = this.mActive;
        final int n = 0;
        if (mActive != null) {
            final int size = this.mActive.size();
            if (size > 0) {
                printWriter.print(s);
                printWriter.print("Active Fragments in ");
                printWriter.print(Integer.toHexString(System.identityHashCode(this)));
                printWriter.println(":");
                for (int i = 0; i < size; ++i) {
                    final Fragment x = (Fragment)this.mActive.valueAt(i);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(i);
                    printWriter.print(": ");
                    printWriter.println(x);
                    if (x != null) {
                        x.dump(string, fileDescriptor, printWriter, array);
                    }
                }
            }
        }
        final int size2 = this.mAdded.size();
        if (size2 > 0) {
            printWriter.print(s);
            printWriter.println("Added Fragments:");
            for (int j = 0; j < size2; ++j) {
                final Fragment fragment = this.mAdded.get(j);
                printWriter.print(s);
                printWriter.print("  #");
                printWriter.print(j);
                printWriter.print(": ");
                printWriter.println(fragment.toString());
            }
        }
        if (this.mCreatedMenus != null) {
            final int size3 = this.mCreatedMenus.size();
            if (size3 > 0) {
                printWriter.print(s);
                printWriter.println("Fragments Created Menus:");
                for (int k = 0; k < size3; ++k) {
                    final Fragment fragment2 = this.mCreatedMenus.get(k);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(k);
                    printWriter.print(": ");
                    printWriter.println(fragment2.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            final int size4 = this.mBackStack.size();
            if (size4 > 0) {
                printWriter.print(s);
                printWriter.println("Back Stack:");
                for (int l = 0; l < size4; ++l) {
                    final BackStackRecord backStackRecord = this.mBackStack.get(l);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(l);
                    printWriter.print(": ");
                    printWriter.println(backStackRecord.toString());
                    backStackRecord.dump(string, fileDescriptor, printWriter, array);
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                final int size5 = this.mBackStackIndices.size();
                if (size5 > 0) {
                    printWriter.print(s);
                    printWriter.println("Back Stack Indices:");
                    for (int n2 = 0; n2 < size5; ++n2) {
                        final BackStackRecord x2 = this.mBackStackIndices.get(n2);
                        printWriter.print(s);
                        printWriter.print("  #");
                        printWriter.print(n2);
                        printWriter.print(": ");
                        printWriter.println(x2);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(s);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
            // monitorexit(this)
            if (this.mPendingActions != null) {
                final int size6 = this.mPendingActions.size();
                if (size6 > 0) {
                    printWriter.print(s);
                    printWriter.println("Pending Actions:");
                    for (int n3 = n; n3 < size6; ++n3) {
                        final OpGenerator x3 = this.mPendingActions.get(n3);
                        printWriter.print(s);
                        printWriter.print("  #");
                        printWriter.print(n3);
                        printWriter.print(": ");
                        printWriter.println(x3);
                    }
                }
            }
            printWriter.print(s);
            printWriter.println("FragmentManager misc state:");
            printWriter.print(s);
            printWriter.print("  mHost=");
            printWriter.println(this.mHost);
            printWriter.print(s);
            printWriter.print("  mContainer=");
            printWriter.println(this.mContainer);
            if (this.mParent != null) {
                printWriter.print(s);
                printWriter.print("  mParent=");
                printWriter.println(this.mParent);
            }
            printWriter.print(s);
            printWriter.print("  mCurState=");
            printWriter.print(this.mCurState);
            printWriter.print(" mStateSaved=");
            printWriter.print(this.mStateSaved);
            printWriter.print(" mDestroyed=");
            printWriter.println(this.mDestroyed);
            if (this.mNeedMenuInvalidate) {
                printWriter.print(s);
                printWriter.print("  mNeedMenuInvalidate=");
                printWriter.println(this.mNeedMenuInvalidate);
            }
            if (this.mNoTransactionsBecause != null) {
                printWriter.print(s);
                printWriter.print("  mNoTransactionsBecause=");
                printWriter.println(this.mNoTransactionsBecause);
            }
        }
    }
    
    public void enqueueAction(final OpGenerator e, final boolean b) {
        if (!b) {
            this.checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed && this.mHost != null) {
                if (this.mPendingActions == null) {
                    this.mPendingActions = new ArrayList<OpGenerator>();
                }
                this.mPendingActions.add(e);
                this.scheduleCommit();
                return;
            }
            if (b) {
                return;
            }
            throw new IllegalStateException("Activity has been destroyed");
        }
    }
    
    void ensureInflatedFragmentView(final Fragment fragment) {
        if (fragment.mFromLayout && !fragment.mPerformedCreateView) {
            fragment.mView = fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), null, fragment.mSavedFragmentState);
            if (fragment.mView != null) {
                fragment.mInnerView = fragment.mView;
                fragment.mView.setSaveFromParentEnabled(false);
                if (fragment.mHidden) {
                    fragment.mView.setVisibility(8);
                }
                fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                this.dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
            }
            else {
                fragment.mInnerView = null;
            }
        }
    }
    
    public boolean execPendingActions() {
        this.ensureExecReady(true);
        boolean b = false;
        while (this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                this.cleanupExec();
                b = true;
                continue;
            }
            finally {
                this.cleanupExec();
            }
            break;
        }
        this.doPendingDeferredStart();
        this.burpActive();
        return b;
    }
    
    public void execSingleAction(final OpGenerator opGenerator, final boolean b) {
        if (b && (this.mHost == null || this.mDestroyed)) {
            return;
        }
        this.ensureExecReady(b);
        if (opGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.doPendingDeferredStart();
        this.burpActive();
    }
    
    @Override
    public boolean executePendingTransactions() {
        final boolean execPendingActions = this.execPendingActions();
        this.forcePostponedTransactions();
        return execPendingActions;
    }
    
    @Override
    public Fragment findFragmentById(final int n) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.mFragmentId == n) {
                return fragment;
            }
        }
        if (this.mActive != null) {
            for (int j = this.mActive.size() - 1; j >= 0; --j) {
                final Fragment fragment2 = (Fragment)this.mActive.valueAt(j);
                if (fragment2 != null && fragment2.mFragmentId == n) {
                    return fragment2;
                }
            }
        }
        return null;
    }
    
    @Override
    public Fragment findFragmentByTag(final String s) {
        if (s != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && s.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        if (this.mActive != null && s != null) {
            for (int j = this.mActive.size() - 1; j >= 0; --j) {
                final Fragment fragment2 = (Fragment)this.mActive.valueAt(j);
                if (fragment2 != null && s.equals(fragment2.mTag)) {
                    return fragment2;
                }
            }
        }
        return null;
    }
    
    public Fragment findFragmentByWho(final String s) {
        if (this.mActive != null && s != null) {
            for (int i = this.mActive.size() - 1; i >= 0; --i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null) {
                    final Fragment fragmentByWho = fragment.findFragmentByWho(s);
                    if (fragmentByWho != null) {
                        return fragmentByWho;
                    }
                }
            }
        }
        return null;
    }
    
    public void freeBackStackIndex(final int i) {
        synchronized (this) {
            this.mBackStackIndices.set(i, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<Integer>();
            }
            if (FragmentManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Freeing back stack index ");
                sb.append(i);
                Log.v("FragmentManager", sb.toString());
            }
            this.mAvailBackStackIndices.add(i);
        }
    }
    
    int getActiveFragmentCount() {
        if (this.mActive == null) {
            return 0;
        }
        return this.mActive.size();
    }
    
    List<Fragment> getActiveFragments() {
        if (this.mActive == null) {
            return null;
        }
        final int size = this.mActive.size();
        final ArrayList list = new ArrayList<Fragment>(size);
        for (int i = 0; i < size; ++i) {
            list.add((Fragment)this.mActive.valueAt(i));
        }
        return (List<Fragment>)list;
    }
    
    @Override
    public BackStackEntry getBackStackEntryAt(final int index) {
        return this.mBackStack.get(index);
    }
    
    @Override
    public int getBackStackEntryCount() {
        int size;
        if (this.mBackStack != null) {
            size = this.mBackStack.size();
        }
        else {
            size = 0;
        }
        return size;
    }
    
    @Override
    public Fragment getFragment(final Bundle bundle, final String str) {
        final int int1 = bundle.getInt(str, -1);
        if (int1 == -1) {
            return null;
        }
        final Fragment fragment = (Fragment)this.mActive.get(int1);
        if (fragment == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment no longer exists for key ");
            sb.append(str);
            sb.append(": index ");
            sb.append(int1);
            this.throwException(new IllegalStateException(sb.toString()));
        }
        return fragment;
    }
    
    @Override
    public List<Fragment> getFragments() {
        if (this.mAdded.isEmpty()) {
            return (List<Fragment>)Collections.EMPTY_LIST;
        }
        synchronized (this.mAdded) {
            return (List<Fragment>)this.mAdded.clone();
        }
    }
    
    LayoutInflater$Factory2 getLayoutInflaterFactory() {
        return (LayoutInflater$Factory2)this;
    }
    
    @Override
    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }
    
    public void hideFragment(final Fragment obj) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("hide: ");
            sb.append(obj);
            Log.v("FragmentManager", sb.toString());
        }
        if (!obj.mHidden) {
            obj.mHidden = true;
            obj.mHiddenChanged ^= true;
        }
    }
    
    @Override
    public boolean isDestroyed() {
        return this.mDestroyed;
    }
    
    boolean isStateAtLeast(final int n) {
        return this.mCurState >= n;
    }
    
    @Override
    public boolean isStateSaved() {
        return this.mStateSaved;
    }
    
    AnimationOrAnimator loadAnimation(final Fragment fragment, final int n, final boolean b, final int n2) {
        final int nextAnim = fragment.getNextAnim();
        final Animation onCreateAnimation = fragment.onCreateAnimation(n, b, nextAnim);
        if (onCreateAnimation != null) {
            return new AnimationOrAnimator(onCreateAnimation);
        }
        final Animator onCreateAnimator = fragment.onCreateAnimator(n, b, nextAnim);
        if (onCreateAnimator != null) {
            return new AnimationOrAnimator(onCreateAnimator);
        }
        if (nextAnim == 0) {
            goto Label_0202;
        }
        if (!"anim".equals(this.mHost.getContext().getResources().getResourceTypeName(nextAnim))) {
            goto Label_0133;
        }
        try {
            final Animation loadAnimation = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
            if (loadAnimation != null) {
                return new AnimationOrAnimator(loadAnimation);
            }
            goto Label_0133;
        }
        catch (Resources$NotFoundException ex) {
            throw ex;
        }
        catch (RuntimeException ex2) {
            goto Label_0133;
        }
        try {
            final Animator loadAnimator = AnimatorInflater.loadAnimator(this.mHost.getContext(), nextAnim);
            if (loadAnimator != null) {
                return new AnimationOrAnimator(loadAnimator);
            }
            goto Label_0202;
        }
        catch (RuntimeException ex3) {}
    }
    
    void makeActive(final Fragment obj) {
        if (obj.mIndex >= 0) {
            return;
        }
        obj.setIndex(this.mNextFragmentIndex++, this.mParent);
        if (this.mActive == null) {
            this.mActive = (SparseArray<Fragment>)new SparseArray();
        }
        this.mActive.put(obj.mIndex, (Object)obj);
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Allocated fragment index ");
            sb.append(obj);
            Log.v("FragmentManager", sb.toString());
        }
    }
    
    void makeInactive(final Fragment obj) {
        if (obj.mIndex < 0) {
            return;
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Freeing fragment index ");
            sb.append(obj);
            Log.v("FragmentManager", sb.toString());
        }
        this.mActive.put(obj.mIndex, (Object)null);
        this.mHost.inactivateFragment(obj.mWho);
        obj.initState();
    }
    
    void moveFragmentToExpectedState(final Fragment fragment) {
        if (fragment == null) {
            return;
        }
        int n2;
        final int n = n2 = this.mCurState;
        if (fragment.mRemoving) {
            if (fragment.isInBackStack()) {
                n2 = Math.min(n, 1);
            }
            else {
                n2 = Math.min(n, 0);
            }
        }
        this.moveToState(fragment, n2, fragment.getNextTransition(), fragment.getNextTransitionStyle(), false);
        if (fragment.mView != null) {
            final Fragment fragmentUnder = this.findFragmentUnder(fragment);
            if (fragmentUnder != null) {
                final View mView = fragmentUnder.mView;
                final ViewGroup mContainer = fragment.mContainer;
                final int indexOfChild = mContainer.indexOfChild(mView);
                final int indexOfChild2 = mContainer.indexOfChild(fragment.mView);
                if (indexOfChild2 < indexOfChild) {
                    mContainer.removeViewAt(indexOfChild2);
                    mContainer.addView(fragment.mView, indexOfChild);
                }
            }
            if (fragment.mIsNewlyAdded && fragment.mContainer != null) {
                if (fragment.mPostponedAlpha > 0.0f) {
                    fragment.mView.setAlpha(fragment.mPostponedAlpha);
                }
                fragment.mPostponedAlpha = 0.0f;
                fragment.mIsNewlyAdded = false;
                final AnimationOrAnimator loadAnimation = this.loadAnimation(fragment, fragment.getNextTransition(), true, fragment.getNextTransitionStyle());
                if (loadAnimation != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                    if (loadAnimation.animation != null) {
                        fragment.mView.startAnimation(loadAnimation.animation);
                    }
                    else {
                        loadAnimation.animator.setTarget((Object)fragment.mView);
                        loadAnimation.animator.start();
                    }
                }
            }
        }
        if (fragment.mHiddenChanged) {
            this.completeShowHideFragment(fragment);
        }
    }
    
    void moveToState(int mCurState, final boolean b) {
        if (this.mHost == null && mCurState != 0) {
            throw new IllegalStateException("No activity");
        }
        if (!b && mCurState == this.mCurState) {
            return;
        }
        this.mCurState = mCurState;
        if (this.mActive != null) {
            int n;
            for (int size = this.mAdded.size(), i = mCurState = 0; i < size; ++i, mCurState = n) {
                final Fragment fragment = this.mAdded.get(i);
                this.moveFragmentToExpectedState(fragment);
                n = mCurState;
                if (fragment.mLoaderManager != null) {
                    n = (mCurState | (fragment.mLoaderManager.hasRunningLoaders() ? 1 : 0));
                }
            }
            int n2;
            for (int size2 = this.mActive.size(), j = 0; j < size2; ++j, mCurState = n2) {
                final Fragment fragment2 = (Fragment)this.mActive.valueAt(j);
                n2 = mCurState;
                if (fragment2 != null) {
                    if (!fragment2.mRemoving) {
                        n2 = mCurState;
                        if (!fragment2.mDetached) {
                            continue;
                        }
                    }
                    n2 = mCurState;
                    if (!fragment2.mIsNewlyAdded) {
                        this.moveFragmentToExpectedState(fragment2);
                        n2 = mCurState;
                        if (fragment2.mLoaderManager != null) {
                            n2 = (mCurState | (fragment2.mLoaderManager.hasRunningLoaders() ? 1 : 0));
                        }
                    }
                }
            }
            if (mCurState == 0) {
                this.startPendingDeferredFragments();
            }
            if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
                this.mHost.onSupportInvalidateOptionsMenu();
                this.mNeedMenuInvalidate = false;
            }
        }
    }
    
    void moveToState(final Fragment fragment) {
        this.moveToState(fragment, this.mCurState, 0, 0, false);
    }
    
    void moveToState(final Fragment fragment, int stateAfterAnimating, int n, int n2, final boolean b) {
        final boolean mAdded = fragment.mAdded;
        final int n3 = 1;
        final boolean b2 = true;
        if (!mAdded || fragment.mDetached) {
            if ((stateAfterAnimating = stateAfterAnimating) > 1) {
                stateAfterAnimating = 1;
            }
        }
        int mState = stateAfterAnimating;
        if (fragment.mRemoving && (mState = stateAfterAnimating) > fragment.mState) {
            if (fragment.mState == 0 && fragment.isInBackStack()) {
                mState = 1;
            }
            else {
                mState = fragment.mState;
            }
        }
        if (fragment.mDeferStart && fragment.mState < 4 && mState > 3) {
            stateAfterAnimating = 3;
        }
        else {
            stateAfterAnimating = mState;
        }
        int n6 = 0;
        if (fragment.mState <= stateAfterAnimating) {
            if (fragment.mFromLayout && !fragment.mInLayout) {
                return;
            }
            if (fragment.getAnimatingAway() != null || fragment.getAnimator() != null) {
                fragment.setAnimatingAway(null);
                fragment.setAnimator(null);
                this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, true);
            }
            int n4 = stateAfterAnimating;
            n2 = stateAfterAnimating;
            int n5 = stateAfterAnimating;
            n = stateAfterAnimating;
            Label_0743: {
                switch (fragment.mState) {
                    default: {
                        n6 = stateAfterAnimating;
                        break;
                    }
                    case 0: {
                        n4 = stateAfterAnimating;
                        if (stateAfterAnimating <= 0) {
                            break Label_0743;
                        }
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("moveto CREATED: ");
                            sb.append(fragment);
                            Log.v("FragmentManager", sb.toString());
                        }
                        n4 = stateAfterAnimating;
                        if (fragment.mSavedFragmentState != null) {
                            fragment.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            fragment.mSavedViewState = (SparseArray<Parcelable>)fragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                            fragment.mTarget = this.getFragment(fragment.mSavedFragmentState, "android:target_state");
                            if (fragment.mTarget != null) {
                                fragment.mTargetRequestCode = fragment.mSavedFragmentState.getInt("android:target_req_state", 0);
                            }
                            fragment.mUserVisibleHint = fragment.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
                            n4 = stateAfterAnimating;
                            if (!fragment.mUserVisibleHint) {
                                fragment.mDeferStart = true;
                                if ((n4 = stateAfterAnimating) > 3) {
                                    n4 = 3;
                                }
                            }
                        }
                        fragment.mHost = this.mHost;
                        fragment.mParentFragment = this.mParent;
                        FragmentManagerImpl mFragmentManager;
                        if (this.mParent != null) {
                            mFragmentManager = this.mParent.mChildFragmentManager;
                        }
                        else {
                            mFragmentManager = this.mHost.getFragmentManagerImpl();
                        }
                        fragment.mFragmentManager = mFragmentManager;
                        if (fragment.mTarget != null) {
                            if (this.mActive.get(fragment.mTarget.mIndex) != fragment.mTarget) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Fragment ");
                                sb2.append(fragment);
                                sb2.append(" declared target fragment ");
                                sb2.append(fragment.mTarget);
                                sb2.append(" that does not belong to this FragmentManager!");
                                throw new IllegalStateException(sb2.toString());
                            }
                            if (fragment.mTarget.mState < 1) {
                                this.moveToState(fragment.mTarget, 1, 0, 0, true);
                            }
                        }
                        this.dispatchOnFragmentPreAttached(fragment, this.mHost.getContext(), false);
                        fragment.mCalled = false;
                        fragment.onAttach(this.mHost.getContext());
                        if (!fragment.mCalled) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Fragment ");
                            sb3.append(fragment);
                            sb3.append(" did not call through to super.onAttach()");
                            throw new SuperNotCalledException(sb3.toString());
                        }
                        if (fragment.mParentFragment == null) {
                            this.mHost.onAttachFragment(fragment);
                        }
                        else {
                            fragment.mParentFragment.onAttachFragment(fragment);
                        }
                        this.dispatchOnFragmentAttached(fragment, this.mHost.getContext(), false);
                        if (!fragment.mIsCreated) {
                            this.dispatchOnFragmentPreCreated(fragment, fragment.mSavedFragmentState, false);
                            fragment.performCreate(fragment.mSavedFragmentState);
                            this.dispatchOnFragmentCreated(fragment, fragment.mSavedFragmentState, false);
                        }
                        else {
                            fragment.restoreChildFragmentState(fragment.mSavedFragmentState);
                            fragment.mState = 1;
                        }
                        fragment.mRetaining = false;
                        break Label_0743;
                    }
                    case 1: {
                        this.ensureInflatedFragmentView(fragment);
                        if ((n2 = n4) > 1) {
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("moveto ACTIVITY_CREATED: ");
                                sb4.append(fragment);
                                Log.v("FragmentManager", sb4.toString());
                            }
                            if (!fragment.mFromLayout) {
                                ViewGroup mContainer;
                                if (fragment.mContainerId != 0) {
                                    if (fragment.mContainerId == -1) {
                                        final StringBuilder sb5 = new StringBuilder();
                                        sb5.append("Cannot create fragment ");
                                        sb5.append(fragment);
                                        sb5.append(" for a container view with no id");
                                        this.throwException(new IllegalArgumentException(sb5.toString()));
                                    }
                                    final ViewGroup viewGroup = (ViewGroup)this.mContainer.onFindViewById(fragment.mContainerId);
                                    if ((mContainer = viewGroup) == null) {
                                        mContainer = viewGroup;
                                        if (!fragment.mRestored) {
                                            String resourceName;
                                            try {
                                                resourceName = fragment.getResources().getResourceName(fragment.mContainerId);
                                            }
                                            catch (Resources$NotFoundException ex) {
                                                resourceName = "unknown";
                                            }
                                            final StringBuilder sb6 = new StringBuilder();
                                            sb6.append("No view found for id 0x");
                                            sb6.append(Integer.toHexString(fragment.mContainerId));
                                            sb6.append(" (");
                                            sb6.append(resourceName);
                                            sb6.append(") for fragment ");
                                            sb6.append(fragment);
                                            this.throwException(new IllegalArgumentException(sb6.toString()));
                                            mContainer = viewGroup;
                                        }
                                    }
                                }
                                else {
                                    mContainer = null;
                                }
                                fragment.mContainer = mContainer;
                                fragment.mView = fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), mContainer, fragment.mSavedFragmentState);
                                if (fragment.mView != null) {
                                    fragment.mInnerView = fragment.mView;
                                    fragment.mView.setSaveFromParentEnabled(false);
                                    if (mContainer != null) {
                                        mContainer.addView(fragment.mView);
                                    }
                                    if (fragment.mHidden) {
                                        fragment.mView.setVisibility(8);
                                    }
                                    fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                                    this.dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
                                    fragment.mIsNewlyAdded = (fragment.mView.getVisibility() == 0 && fragment.mContainer != null && b2);
                                }
                                else {
                                    fragment.mInnerView = null;
                                }
                            }
                            fragment.performActivityCreated(fragment.mSavedFragmentState);
                            this.dispatchOnFragmentActivityCreated(fragment, fragment.mSavedFragmentState, false);
                            if (fragment.mView != null) {
                                fragment.restoreViewState(fragment.mSavedFragmentState);
                            }
                            fragment.mSavedFragmentState = null;
                            n2 = n4;
                        }
                    }
                    case 2: {
                        if ((n5 = n2) > 2) {
                            fragment.mState = 3;
                            n5 = n2;
                        }
                    }
                    case 3: {
                        if ((n = n5) > 3) {
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb7 = new StringBuilder();
                                sb7.append("moveto STARTED: ");
                                sb7.append(fragment);
                                Log.v("FragmentManager", sb7.toString());
                            }
                            fragment.performStart();
                            this.dispatchOnFragmentStarted(fragment, false);
                            n = n5;
                        }
                    }
                    case 4: {
                        if ((n6 = n) > 4) {
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb8 = new StringBuilder();
                                sb8.append("moveto RESUMED: ");
                                sb8.append(fragment);
                                Log.v("FragmentManager", sb8.toString());
                            }
                            fragment.performResume();
                            this.dispatchOnFragmentResumed(fragment, false);
                            fragment.mSavedFragmentState = null;
                            fragment.mSavedViewState = null;
                            n6 = n;
                            break;
                        }
                        break;
                    }
                }
            }
        }
        else if (fragment.mState > (n6 = stateAfterAnimating)) {
            switch (fragment.mState) {
                default: {
                    n6 = stateAfterAnimating;
                    break;
                }
                case 5: {
                    if (stateAfterAnimating < 5) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb9 = new StringBuilder();
                            sb9.append("movefrom RESUMED: ");
                            sb9.append(fragment);
                            Log.v("FragmentManager", sb9.toString());
                        }
                        fragment.performPause();
                        this.dispatchOnFragmentPaused(fragment, false);
                    }
                }
                case 4: {
                    if (stateAfterAnimating < 4) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb10 = new StringBuilder();
                            sb10.append("movefrom STARTED: ");
                            sb10.append(fragment);
                            Log.v("FragmentManager", sb10.toString());
                        }
                        fragment.performStop();
                        this.dispatchOnFragmentStopped(fragment, false);
                    }
                }
                case 3: {
                    if (stateAfterAnimating < 3) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb11 = new StringBuilder();
                            sb11.append("movefrom STOPPED: ");
                            sb11.append(fragment);
                            Log.v("FragmentManager", sb11.toString());
                        }
                        fragment.performReallyStop();
                    }
                }
                case 2: {
                    if (stateAfterAnimating < 2) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb12 = new StringBuilder();
                            sb12.append("movefrom ACTIVITY_CREATED: ");
                            sb12.append(fragment);
                            Log.v("FragmentManager", sb12.toString());
                        }
                        if (fragment.mView != null && this.mHost.onShouldSaveFragmentState(fragment) && fragment.mSavedViewState == null) {
                            this.saveFragmentViewState(fragment);
                        }
                        fragment.performDestroyView();
                        this.dispatchOnFragmentViewDestroyed(fragment, false);
                        if (fragment.mView != null && fragment.mContainer != null) {
                            fragment.mView.clearAnimation();
                            fragment.mContainer.endViewTransition(fragment.mView);
                            AnimationOrAnimator loadAnimation;
                            if (this.mCurState > 0 && !this.mDestroyed && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f) {
                                loadAnimation = this.loadAnimation(fragment, n, false, n2);
                            }
                            else {
                                loadAnimation = null;
                            }
                            fragment.mPostponedAlpha = 0.0f;
                            if (loadAnimation != null) {
                                this.animateRemoveFragment(fragment, loadAnimation, stateAfterAnimating);
                            }
                            fragment.mContainer.removeView(fragment.mView);
                        }
                        fragment.mContainer = null;
                        fragment.mView = null;
                        fragment.mInnerView = null;
                        fragment.mInLayout = false;
                    }
                }
                case 1: {
                    if ((n6 = stateAfterAnimating) >= 1) {
                        break;
                    }
                    if (this.mDestroyed) {
                        if (fragment.getAnimatingAway() != null) {
                            final View animatingAway = fragment.getAnimatingAway();
                            fragment.setAnimatingAway(null);
                            animatingAway.clearAnimation();
                        }
                        else if (fragment.getAnimator() != null) {
                            final Animator animator = fragment.getAnimator();
                            fragment.setAnimator(null);
                            animator.cancel();
                        }
                    }
                    if (fragment.getAnimatingAway() != null || fragment.getAnimator() != null) {
                        fragment.setStateAfterAnimating(stateAfterAnimating);
                        n6 = n3;
                        break;
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb13 = new StringBuilder();
                        sb13.append("movefrom CREATED: ");
                        sb13.append(fragment);
                        Log.v("FragmentManager", sb13.toString());
                    }
                    if (!fragment.mRetaining) {
                        fragment.performDestroy();
                        this.dispatchOnFragmentDestroyed(fragment, false);
                    }
                    else {
                        fragment.mState = 0;
                    }
                    fragment.performDetach();
                    this.dispatchOnFragmentDetached(fragment, false);
                    n6 = stateAfterAnimating;
                    if (b) {
                        break;
                    }
                    if (!fragment.mRetaining) {
                        this.makeInactive(fragment);
                        n6 = stateAfterAnimating;
                        break;
                    }
                    fragment.mHost = null;
                    fragment.mParentFragment = null;
                    fragment.mFragmentManager = null;
                    n6 = stateAfterAnimating;
                    break;
                }
            }
        }
        if (fragment.mState != n6) {
            final StringBuilder sb14 = new StringBuilder();
            sb14.append("moveToState: Fragment state for ");
            sb14.append(fragment);
            sb14.append(" not updated inline; ");
            sb14.append("expected state ");
            sb14.append(n6);
            sb14.append(" found ");
            sb14.append(fragment.mState);
            Log.w("FragmentManager", sb14.toString());
            fragment.mState = n6;
        }
    }
    
    public void noteStateNotSaved() {
        this.mSavedNonConfig = null;
        int i = 0;
        this.mStateSaved = false;
        while (i < this.mAdded.size()) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
            ++i;
        }
    }
    
    public View onCreateView(final View view, String attributeValue, final Context context, final AttributeSet set) {
        if (!"fragment".equals(attributeValue)) {
            return null;
        }
        attributeValue = set.getAttributeValue((String)null, "class");
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, FragmentTag.Fragment);
        int id = 0;
        String string;
        if ((string = attributeValue) == null) {
            string = obtainStyledAttributes.getString(0);
        }
        final int resourceId = obtainStyledAttributes.getResourceId(1, -1);
        final String string2 = obtainStyledAttributes.getString(2);
        obtainStyledAttributes.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), string)) {
            return null;
        }
        if (view != null) {
            id = view.getId();
        }
        if (id == -1 && resourceId == -1 && string2 == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(set.getPositionDescription());
            sb.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
            sb.append(string);
            throw new IllegalArgumentException(sb.toString());
        }
        Fragment fragmentById;
        if (resourceId != -1) {
            fragmentById = this.findFragmentById(resourceId);
        }
        else {
            fragmentById = null;
        }
        Fragment fragmentByTag = fragmentById;
        if (fragmentById == null) {
            fragmentByTag = fragmentById;
            if (string2 != null) {
                fragmentByTag = this.findFragmentByTag(string2);
            }
        }
        Fragment fragmentById2;
        if ((fragmentById2 = fragmentByTag) == null) {
            fragmentById2 = fragmentByTag;
            if (id != -1) {
                fragmentById2 = this.findFragmentById(id);
            }
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("onCreateView: id=0x");
            sb2.append(Integer.toHexString(resourceId));
            sb2.append(" fname=");
            sb2.append(string);
            sb2.append(" existing=");
            sb2.append(fragmentById2);
            Log.v("FragmentManager", sb2.toString());
        }
        Fragment instantiate;
        if (fragmentById2 == null) {
            instantiate = this.mContainer.instantiate(context, string, null);
            instantiate.mFromLayout = true;
            int mFragmentId;
            if (resourceId != 0) {
                mFragmentId = resourceId;
            }
            else {
                mFragmentId = id;
            }
            instantiate.mFragmentId = mFragmentId;
            instantiate.mContainerId = id;
            instantiate.mTag = string2;
            instantiate.mInLayout = true;
            instantiate.mFragmentManager = this;
            instantiate.mHost = this.mHost;
            instantiate.onInflate(this.mHost.getContext(), set, instantiate.mSavedFragmentState);
            this.addFragment(instantiate, true);
        }
        else {
            if (fragmentById2.mInLayout) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(set.getPositionDescription());
                sb3.append(": Duplicate id 0x");
                sb3.append(Integer.toHexString(resourceId));
                sb3.append(", tag ");
                sb3.append(string2);
                sb3.append(", or parent id 0x");
                sb3.append(Integer.toHexString(id));
                sb3.append(" with another fragment for ");
                sb3.append(string);
                throw new IllegalArgumentException(sb3.toString());
            }
            fragmentById2.mInLayout = true;
            fragmentById2.mHost = this.mHost;
            if (!fragmentById2.mRetaining) {
                fragmentById2.onInflate(this.mHost.getContext(), set, fragmentById2.mSavedFragmentState);
            }
            instantiate = fragmentById2;
        }
        if (this.mCurState < 1 && instantiate.mFromLayout) {
            this.moveToState(instantiate, 1, 0, 0, false);
        }
        else {
            this.moveToState(instantiate);
        }
        if (instantiate.mView == null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Fragment ");
            sb4.append(string);
            sb4.append(" did not create a view.");
            throw new IllegalStateException(sb4.toString());
        }
        if (resourceId != 0) {
            instantiate.mView.setId(resourceId);
        }
        if (instantiate.mView.getTag() == null) {
            instantiate.mView.setTag((Object)string2);
        }
        return instantiate.mView;
    }
    
    public View onCreateView(final String s, final Context context, final AttributeSet set) {
        return this.onCreateView(null, s, context, set);
    }
    
    public void performPendingDeferredStart(final Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            fragment.mDeferStart = false;
            this.moveToState(fragment, this.mCurState, 0, 0, false);
        }
    }
    
    @Override
    public void popBackStack() {
        this.enqueueAction((OpGenerator)new PopBackStackState(null, -1, 0), false);
    }
    
    @Override
    public void popBackStack(final int i, final int n) {
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad id: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        this.enqueueAction((OpGenerator)new PopBackStackState(null, i, n), false);
    }
    
    @Override
    public void popBackStack(final String s, final int n) {
        this.enqueueAction((OpGenerator)new PopBackStackState(s, -1, n), false);
    }
    
    @Override
    public boolean popBackStackImmediate() {
        this.checkStateLoss();
        return this.popBackStackImmediate(null, -1, 0);
    }
    
    @Override
    public boolean popBackStackImmediate(final int i, final int n) {
        this.checkStateLoss();
        this.execPendingActions();
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad id: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        return this.popBackStackImmediate(null, i, n);
    }
    
    @Override
    public boolean popBackStackImmediate(final String s, final int n) {
        this.checkStateLoss();
        return this.popBackStackImmediate(s, -1, n);
    }
    
    boolean popBackStackState(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final String s, int i, int index) {
        if (this.mBackStack == null) {
            return false;
        }
        if (s == null && i < 0 && (index & 0x1) == 0x0) {
            i = this.mBackStack.size() - 1;
            if (i < 0) {
                return false;
            }
            list.add(this.mBackStack.remove(i));
            list2.add(true);
        }
        else {
            int n;
            if (s == null && i < 0) {
                n = -1;
            }
            else {
                int j;
                for (j = this.mBackStack.size() - 1; j >= 0; --j) {
                    final BackStackRecord backStackRecord = this.mBackStack.get(j);
                    if (s != null && s.equals(backStackRecord.getName())) {
                        break;
                    }
                    if (i >= 0 && i == backStackRecord.mIndex) {
                        break;
                    }
                }
                if (j < 0) {
                    return false;
                }
                n = j;
                if ((index & 0x1) != 0x0) {
                    index = j - 1;
                    while (true) {
                        n = index;
                        if (index < 0) {
                            break;
                        }
                        final BackStackRecord backStackRecord2 = this.mBackStack.get(index);
                        if (s == null || !s.equals(backStackRecord2.getName())) {
                            n = index;
                            if (i < 0) {
                                break;
                            }
                            n = index;
                            if (i != backStackRecord2.mIndex) {
                                break;
                            }
                        }
                        --index;
                    }
                }
            }
            if (n == this.mBackStack.size() - 1) {
                return false;
            }
            for (i = this.mBackStack.size() - 1; i > n; --i) {
                list.add(this.mBackStack.remove(i));
                list2.add(true);
            }
        }
        return true;
    }
    
    @Override
    public void putFragment(final Bundle bundle, final String s, final Fragment obj) {
        if (obj.mIndex < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(obj);
            sb.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(sb.toString()));
        }
        bundle.putInt(s, obj.mIndex);
    }
    
    @Override
    public void registerFragmentLifecycleCallbacks(final FragmentLifecycleCallbacks fragmentLifecycleCallbacks, final boolean b) {
        this.mLifecycleCallbacks.add(new Pair<FragmentLifecycleCallbacks, Boolean>(fragmentLifecycleCallbacks, b));
    }
    
    public void removeFragment(final Fragment fragment) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("remove: ");
            sb.append(fragment);
            sb.append(" nesting=");
            sb.append(fragment.mBackStackNesting);
            Log.v("FragmentManager", sb.toString());
        }
        final boolean inBackStack = fragment.isInBackStack();
        if (fragment.mDetached && !(inBackStack ^ true)) {
            return;
        }
        synchronized (this.mAdded) {
            this.mAdded.remove(fragment);
            // monitorexit(this.mAdded)
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
        }
    }
    
    @Override
    public void removeOnBackStackChangedListener(final OnBackStackChangedListener o) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(o);
        }
    }
    
    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); ++i) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }
    
    void restoreAllState(final Parcelable parcelable, final FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (parcelable == null) {
            return;
        }
        final FragmentManagerState fragmentManagerState = (FragmentManagerState)parcelable;
        if (fragmentManagerState.mActive == null) {
            return;
        }
        List<FragmentManagerNonConfig> list;
        if (fragmentManagerNonConfig != null) {
            final List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
            final List<FragmentManagerNonConfig> childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
            int size;
            if (fragments != null) {
                size = fragments.size();
            }
            else {
                size = 0;
            }
            int n = 0;
            while (true) {
                list = childNonConfigs;
                if (n >= size) {
                    break;
                }
                final Fragment fragment = fragments.get(n);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("restoreAllState: re-attaching retained ");
                    sb.append(fragment);
                    Log.v("FragmentManager", sb.toString());
                }
                int n2;
                for (n2 = 0; n2 < fragmentManagerState.mActive.length && fragmentManagerState.mActive[n2].mIndex != fragment.mIndex; ++n2) {}
                if (n2 == fragmentManagerState.mActive.length) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Could not find active fragment with index ");
                    sb2.append(fragment.mIndex);
                    this.throwException(new IllegalStateException(sb2.toString()));
                }
                final FragmentState fragmentState = fragmentManagerState.mActive[n2];
                fragmentState.mInstance = fragment;
                fragment.mSavedViewState = null;
                fragment.mBackStackNesting = 0;
                fragment.mInLayout = false;
                fragment.mAdded = false;
                fragment.mTarget = null;
                if (fragmentState.mSavedFragmentState != null) {
                    fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                    fragment.mSavedViewState = (SparseArray<Parcelable>)fragmentState.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                    fragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
                }
                ++n;
            }
        }
        else {
            list = null;
        }
        this.mActive = (SparseArray<Fragment>)new SparseArray(fragmentManagerState.mActive.length);
        for (int i = 0; i < fragmentManagerState.mActive.length; ++i) {
            final FragmentState fragmentState2 = fragmentManagerState.mActive[i];
            if (fragmentState2 != null) {
                FragmentManagerNonConfig fragmentManagerNonConfig2;
                if (list != null && i < list.size()) {
                    fragmentManagerNonConfig2 = list.get(i);
                }
                else {
                    fragmentManagerNonConfig2 = null;
                }
                final Fragment instantiate = fragmentState2.instantiate(this.mHost, this.mContainer, this.mParent, fragmentManagerNonConfig2);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("restoreAllState: active #");
                    sb3.append(i);
                    sb3.append(": ");
                    sb3.append(instantiate);
                    Log.v("FragmentManager", sb3.toString());
                }
                this.mActive.put(instantiate.mIndex, (Object)instantiate);
                fragmentState2.mInstance = null;
            }
        }
        if (fragmentManagerNonConfig != null) {
            final List<Fragment> fragments2 = fragmentManagerNonConfig.getFragments();
            int size2;
            if (fragments2 != null) {
                size2 = fragments2.size();
            }
            else {
                size2 = 0;
            }
            for (int j = 0; j < size2; ++j) {
                final Fragment obj = fragments2.get(j);
                if (obj.mTargetIndex >= 0) {
                    obj.mTarget = (Fragment)this.mActive.get(obj.mTargetIndex);
                    if (obj.mTarget == null) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("Re-attaching retained fragment ");
                        sb4.append(obj);
                        sb4.append(" target no longer exists: ");
                        sb4.append(obj.mTargetIndex);
                        Log.w("FragmentManager", sb4.toString());
                    }
                }
            }
        }
        this.mAdded.clear();
        if (fragmentManagerState.mAdded != null) {
            int k = 0;
            while (k < fragmentManagerState.mAdded.length) {
                final Fragment e = (Fragment)this.mActive.get(fragmentManagerState.mAdded[k]);
                if (e == null) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("No instantiated fragment for index #");
                    sb5.append(fragmentManagerState.mAdded[k]);
                    this.throwException(new IllegalStateException(sb5.toString()));
                }
                e.mAdded = true;
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("restoreAllState: added #");
                    sb6.append(k);
                    sb6.append(": ");
                    sb6.append(e);
                    Log.v("FragmentManager", sb6.toString());
                }
                if (this.mAdded.contains(e)) {
                    throw new IllegalStateException("Already added!");
                }
                synchronized (this.mAdded) {
                    this.mAdded.add(e);
                    // monitorexit(this.mAdded)
                    ++k;
                    continue;
                }
                break;
            }
        }
        if (fragmentManagerState.mBackStack != null) {
            this.mBackStack = new ArrayList<BackStackRecord>(fragmentManagerState.mBackStack.length);
            for (int l = 0; l < fragmentManagerState.mBackStack.length; ++l) {
                final BackStackRecord instantiate2 = fragmentManagerState.mBackStack[l].instantiate(this);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append("restoreAllState: back stack #");
                    sb7.append(l);
                    sb7.append(" (index ");
                    sb7.append(instantiate2.mIndex);
                    sb7.append("): ");
                    sb7.append(instantiate2);
                    Log.v("FragmentManager", sb7.toString());
                    final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
                    instantiate2.dump("  ", printWriter, false);
                    printWriter.close();
                }
                this.mBackStack.add(instantiate2);
                if (instantiate2.mIndex >= 0) {
                    this.setBackStackIndex(instantiate2.mIndex, instantiate2);
                }
            }
        }
        else {
            this.mBackStack = null;
        }
        if (fragmentManagerState.mPrimaryNavActiveIndex >= 0) {
            this.mPrimaryNav = (Fragment)this.mActive.get(fragmentManagerState.mPrimaryNavActiveIndex);
        }
        this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
    }
    
    FragmentManagerNonConfig retainNonConfig() {
        setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }
    
    Parcelable saveAllState() {
        this.forcePostponedTransactions();
        this.endAnimatingAwayFragments();
        this.execPendingActions();
        this.mStateSaved = true;
        final BackStackState[] array = null;
        this.mSavedNonConfig = null;
        if (this.mActive == null || this.mActive.size() <= 0) {
            return null;
        }
        final int size = this.mActive.size();
        final FragmentState[] mActive = new FragmentState[size];
        final int n = 0;
        int n2;
        for (int i = n2 = 0; i < size; ++i) {
            final Fragment obj = (Fragment)this.mActive.valueAt(i);
            if (obj != null) {
                if (obj.mIndex < 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failure saving state: active ");
                    sb.append(obj);
                    sb.append(" has cleared index: ");
                    sb.append(obj.mIndex);
                    this.throwException(new IllegalStateException(sb.toString()));
                }
                final FragmentState fragmentState = new FragmentState(obj);
                mActive[i] = fragmentState;
                if (obj.mState > 0 && fragmentState.mSavedFragmentState == null) {
                    fragmentState.mSavedFragmentState = this.saveFragmentBasicState(obj);
                    if (obj.mTarget != null) {
                        if (obj.mTarget.mIndex < 0) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Failure saving state: ");
                            sb2.append(obj);
                            sb2.append(" has target not in fragment manager: ");
                            sb2.append(obj.mTarget);
                            this.throwException(new IllegalStateException(sb2.toString()));
                        }
                        if (fragmentState.mSavedFragmentState == null) {
                            fragmentState.mSavedFragmentState = new Bundle();
                        }
                        this.putFragment(fragmentState.mSavedFragmentState, "android:target_state", obj.mTarget);
                        if (obj.mTargetRequestCode != 0) {
                            fragmentState.mSavedFragmentState.putInt("android:target_req_state", obj.mTargetRequestCode);
                        }
                    }
                }
                else {
                    fragmentState.mSavedFragmentState = obj.mSavedFragmentState;
                }
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Saved state of ");
                    sb3.append(obj);
                    sb3.append(": ");
                    sb3.append(fragmentState.mSavedFragmentState);
                    Log.v("FragmentManager", sb3.toString());
                }
                n2 = 1;
            }
        }
        if (n2 == 0) {
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "saveAllState: no fragments!");
            }
            return null;
        }
        final int size2 = this.mAdded.size();
        int[] mAdded;
        if (size2 > 0) {
            final int[] array2 = new int[size2];
            int n3 = 0;
            while (true) {
                mAdded = array2;
                if (n3 >= size2) {
                    break;
                }
                array2[n3] = this.mAdded.get(n3).mIndex;
                if (array2[n3] < 0) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Failure saving state: active ");
                    sb4.append(this.mAdded.get(n3));
                    sb4.append(" has cleared index: ");
                    sb4.append(array2[n3]);
                    this.throwException(new IllegalStateException(sb4.toString()));
                }
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("saveAllState: adding fragment #");
                    sb5.append(n3);
                    sb5.append(": ");
                    sb5.append(this.mAdded.get(n3));
                    Log.v("FragmentManager", sb5.toString());
                }
                ++n3;
            }
        }
        else {
            mAdded = null;
        }
        BackStackState[] mBackStack = array;
        if (this.mBackStack != null) {
            final int size3 = this.mBackStack.size();
            mBackStack = array;
            if (size3 > 0) {
                final BackStackState[] array3 = new BackStackState[size3];
                int index = n;
                while (true) {
                    mBackStack = array3;
                    if (index >= size3) {
                        break;
                    }
                    array3[index] = new BackStackState(this.mBackStack.get(index));
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("saveAllState: adding back stack #");
                        sb6.append(index);
                        sb6.append(": ");
                        sb6.append(this.mBackStack.get(index));
                        Log.v("FragmentManager", sb6.toString());
                    }
                    ++index;
                }
            }
        }
        final FragmentManagerState fragmentManagerState = new FragmentManagerState();
        fragmentManagerState.mActive = mActive;
        fragmentManagerState.mAdded = mAdded;
        fragmentManagerState.mBackStack = mBackStack;
        if (this.mPrimaryNav != null) {
            fragmentManagerState.mPrimaryNavActiveIndex = this.mPrimaryNav.mIndex;
        }
        fragmentManagerState.mNextFragmentIndex = this.mNextFragmentIndex;
        this.saveNonConfig();
        return (Parcelable)fragmentManagerState;
    }
    
    Bundle saveFragmentBasicState(final Fragment fragment) {
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        this.dispatchOnFragmentSaveInstanceState(fragment, this.mStateBundle, false);
        Bundle mStateBundle;
        if (!this.mStateBundle.isEmpty()) {
            mStateBundle = this.mStateBundle;
            this.mStateBundle = null;
        }
        else {
            mStateBundle = null;
        }
        if (fragment.mView != null) {
            this.saveFragmentViewState(fragment);
        }
        Bundle bundle = mStateBundle;
        if (fragment.mSavedViewState != null) {
            if ((bundle = mStateBundle) == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray("android:view_state", (SparseArray)fragment.mSavedViewState);
        }
        Bundle bundle2 = bundle;
        if (!fragment.mUserVisibleHint) {
            if ((bundle2 = bundle) == null) {
                bundle2 = new Bundle();
            }
            bundle2.putBoolean("android:user_visible_hint", fragment.mUserVisibleHint);
        }
        return bundle2;
    }
    
    @Override
    public Fragment.SavedState saveFragmentInstanceState(final Fragment obj) {
        if (obj.mIndex < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(obj);
            sb.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(sb.toString()));
        }
        final int mState = obj.mState;
        final Fragment.SavedState savedState = null;
        if (mState > 0) {
            final Bundle saveFragmentBasicState = this.saveFragmentBasicState(obj);
            Object o = savedState;
            if (saveFragmentBasicState != null) {
                o = new Fragment.SavedState(saveFragmentBasicState);
            }
            return (Fragment.SavedState)o;
        }
        return null;
    }
    
    void saveFragmentViewState(final Fragment fragment) {
        if (fragment.mInnerView == null) {
            return;
        }
        if (this.mStateArray == null) {
            this.mStateArray = (SparseArray<Parcelable>)new SparseArray();
        }
        else {
            this.mStateArray.clear();
        }
        fragment.mInnerView.saveHierarchyState((SparseArray)this.mStateArray);
        if (this.mStateArray.size() > 0) {
            fragment.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
        }
    }
    
    void saveNonConfig() {
        Object o;
        ArrayList<FragmentManagerNonConfig> list3;
        if (this.mActive != null) {
            int n = 0;
            ArrayList<FragmentManagerNonConfig> list2;
            ArrayList<FragmentManagerNonConfig> list = list2 = null;
            while (true) {
                o = list;
                list3 = list2;
                if (n >= this.mActive.size()) {
                    break;
                }
                final Fragment fragment = (Fragment)this.mActive.valueAt(n);
                ArrayList<FragmentManagerNonConfig> list4 = list;
                ArrayList<FragmentManagerNonConfig> list5 = list2;
                if (fragment != null) {
                    ArrayList<FragmentManagerNonConfig> list6 = list;
                    if (fragment.mRetainInstance) {
                        ArrayList<FragmentManagerNonConfig> list7;
                        if ((list7 = list) == null) {
                            list7 = new ArrayList<FragmentManagerNonConfig>();
                        }
                        list7.add((FragmentManagerNonConfig)fragment);
                        int mIndex;
                        if (fragment.mTarget != null) {
                            mIndex = fragment.mTarget.mIndex;
                        }
                        else {
                            mIndex = -1;
                        }
                        fragment.mTargetIndex = mIndex;
                        list6 = list7;
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("retainNonConfig: keeping retained ");
                            sb.append(fragment);
                            Log.v("FragmentManager", sb.toString());
                            list6 = list7;
                        }
                    }
                    FragmentManagerNonConfig e;
                    if (fragment.mChildFragmentManager != null) {
                        fragment.mChildFragmentManager.saveNonConfig();
                        e = fragment.mChildFragmentManager.mSavedNonConfig;
                    }
                    else {
                        e = fragment.mChildNonConfig;
                    }
                    ArrayList<FragmentManagerNonConfig> list8 = list2;
                    if (list2 == null) {
                        list8 = list2;
                        if (e != null) {
                            final ArrayList<FragmentManagerNonConfig> list9 = new ArrayList<FragmentManagerNonConfig>(this.mActive.size());
                            int n2 = 0;
                            while (true) {
                                list8 = list9;
                                if (n2 >= n) {
                                    break;
                                }
                                list9.add(null);
                                ++n2;
                            }
                        }
                    }
                    list4 = list6;
                    if ((list5 = list8) != null) {
                        list8.add(e);
                        list5 = list8;
                        list4 = list6;
                    }
                }
                ++n;
                list = list4;
                list2 = list5;
            }
        }
        else {
            o = (list3 = null);
        }
        if (o == null && list3 == null) {
            this.mSavedNonConfig = null;
        }
        else {
            this.mSavedNonConfig = new FragmentManagerNonConfig((List<Fragment>)o, list3);
        }
    }
    
    public void setBackStackIndex(final int i, final BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<BackStackRecord>();
            }
            int j;
            if (i < (j = this.mBackStackIndices.size())) {
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Setting back stack index ");
                    sb.append(i);
                    sb.append(" to ");
                    sb.append(backStackRecord);
                    Log.v("FragmentManager", sb.toString());
                }
                this.mBackStackIndices.set(i, backStackRecord);
            }
            else {
                while (j < i) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList<Integer>();
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Adding available back stack index ");
                        sb2.append(j);
                        Log.v("FragmentManager", sb2.toString());
                    }
                    this.mAvailBackStackIndices.add(j);
                    ++j;
                }
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Adding back stack index ");
                    sb3.append(i);
                    sb3.append(" with ");
                    sb3.append(backStackRecord);
                    Log.v("FragmentManager", sb3.toString());
                }
                this.mBackStackIndices.add(backStackRecord);
            }
        }
    }
    
    public void setPrimaryNavigationFragment(final Fragment fragment) {
        if (fragment != null && (this.mActive.get(fragment.mIndex) != fragment || (fragment.mHost != null && fragment.getFragmentManager() != this))) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(fragment);
            sb.append(" is not an active fragment of FragmentManager ");
            sb.append(this);
            throw new IllegalArgumentException(sb.toString());
        }
        this.mPrimaryNav = fragment;
    }
    
    public void showFragment(final Fragment obj) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("show: ");
            sb.append(obj);
            Log.v("FragmentManager", sb.toString());
        }
        if (obj.mHidden) {
            obj.mHidden = false;
            obj.mHiddenChanged ^= true;
        }
    }
    
    void startPendingDeferredFragments() {
        if (this.mActive == null) {
            return;
        }
        for (int i = 0; i < this.mActive.size(); ++i) {
            final Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment != null) {
                this.performPendingDeferredStart(fragment);
            }
        }
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, sb);
        }
        else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }
    
    @Override
    public void unregisterFragmentLifecycleCallbacks(final FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        final CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks = this.mLifecycleCallbacks;
        // monitorenter(mLifecycleCallbacks)
        int i = 0;
        try {
            while (i < this.mLifecycleCallbacks.size()) {
                if (this.mLifecycleCallbacks.get(i).first == fragmentLifecycleCallbacks) {
                    this.mLifecycleCallbacks.remove(i);
                    break;
                }
                ++i;
            }
        }
        finally {
        }
        // monitorexit(mLifecycleCallbacks)
    }
    
    private static class AnimateOnHWLayerIfNeededListener extends AnimationListenerWrapper
    {
        View mView;
        
        AnimateOnHWLayerIfNeededListener(final View mView, final Animation$AnimationListener animation$AnimationListener) {
            super(animation$AnimationListener);
            this.mView = mView;
        }
        
        @CallSuper
        @Override
        public void onAnimationEnd(final Animation animation) {
            if (!ViewCompat.isAttachedToWindow(this.mView) && Build$VERSION.SDK_INT < 24) {
                this.mView.setLayerType(0, (Paint)null);
            }
            else {
                this.mView.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, (Paint)null);
                    }
                });
            }
            super.onAnimationEnd(animation);
        }
    }
    
    private static class AnimationListenerWrapper implements Animation$AnimationListener
    {
        private final Animation$AnimationListener mWrapped;
        
        private AnimationListenerWrapper(final Animation$AnimationListener mWrapped) {
            this.mWrapped = mWrapped;
        }
        
        @CallSuper
        public void onAnimationEnd(final Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationEnd(animation);
            }
        }
        
        @CallSuper
        public void onAnimationRepeat(final Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationRepeat(animation);
            }
        }
        
        @CallSuper
        public void onAnimationStart(final Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationStart(animation);
            }
        }
    }
    
    private static class AnimationOrAnimator
    {
        public final Animation animation;
        public final Animator animator;
        
        private AnimationOrAnimator(final Animator animator) {
            this.animation = null;
            this.animator = animator;
            if (animator == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }
        
        private AnimationOrAnimator(final Animation animation) {
            this.animation = animation;
            this.animator = null;
            if (animation == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }
    }
    
    private static class AnimatorOnHWLayerIfNeededListener extends AnimatorListenerAdapter
    {
        View mView;
        
        AnimatorOnHWLayerIfNeededListener(final View mView) {
            this.mView = mView;
        }
        
        public void onAnimationEnd(final Animator animator) {
            this.mView.setLayerType(0, (Paint)null);
            animator.removeListener((Animator$AnimatorListener)this);
        }
        
        public void onAnimationStart(final Animator animator) {
            this.mView.setLayerType(2, (Paint)null);
        }
    }
    
    static class FragmentTag
    {
        public static final int[] Fragment;
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;
        
        static {
            Fragment = new int[] { 16842755, 16842960, 16842961 };
        }
    }
    
    interface OpGenerator
    {
        boolean generateOps(final ArrayList<BackStackRecord> p0, final ArrayList<Boolean> p1);
    }
    
    private class PopBackStackState implements OpGenerator
    {
        final int mFlags;
        final int mId;
        final String mName;
        
        PopBackStackState(final String mName, final int mId, final int mFlags) {
            this.mName = mName;
            this.mId = mId;
            this.mFlags = mFlags;
        }
        
        @Override
        public boolean generateOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
                final FragmentManager peekChildFragmentManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager();
                if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                    return false;
                }
            }
            return FragmentManagerImpl.this.popBackStackState(list, list2, this.mName, this.mId, this.mFlags);
        }
    }
    
    static class StartEnterTransitionListener implements OnStartEnterTransitionListener
    {
        private final boolean mIsBack;
        private int mNumPostponed;
        private final BackStackRecord mRecord;
        
        StartEnterTransitionListener(final BackStackRecord mRecord, final boolean mIsBack) {
            this.mIsBack = mIsBack;
            this.mRecord = mRecord;
        }
        
        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }
        
        public void completeTransaction() {
            final int mNumPostponed = this.mNumPostponed;
            int i = 0;
            final boolean b = mNumPostponed > 0;
            for (FragmentManagerImpl mManager = this.mRecord.mManager; i < mManager.mAdded.size(); ++i) {
                final Fragment fragment = mManager.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener(null);
                if (b && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, b ^ true, true);
        }
        
        public boolean isReady() {
            return this.mNumPostponed == 0;
        }
        
        @Override
        public void onStartEnterTransition() {
            --this.mNumPostponed;
            if (this.mNumPostponed != 0) {
                return;
            }
            this.mRecord.mManager.scheduleCommit();
        }
        
        @Override
        public void startListening() {
            ++this.mNumPostponed;
        }
    }
}
