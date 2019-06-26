package android.support.v4.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater.Factory2;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl extends FragmentManager implements Factory2 {
   static final Interpolator ACCELERATE_CUBIC = new AccelerateInterpolator(1.5F);
   static final Interpolator ACCELERATE_QUINT = new AccelerateInterpolator(2.5F);
   static final int ANIM_DUR = 220;
   public static final int ANIM_STYLE_CLOSE_ENTER = 3;
   public static final int ANIM_STYLE_CLOSE_EXIT = 4;
   public static final int ANIM_STYLE_FADE_ENTER = 5;
   public static final int ANIM_STYLE_FADE_EXIT = 6;
   public static final int ANIM_STYLE_OPEN_ENTER = 1;
   public static final int ANIM_STYLE_OPEN_EXIT = 2;
   static boolean DEBUG;
   static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5F);
   static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5F);
   static final String TAG = "FragmentManager";
   static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
   static final String TARGET_STATE_TAG = "android:target_state";
   static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
   static final String VIEW_STATE_TAG = "android:view_state";
   static Field sAnimationListenerField;
   SparseArray mActive;
   final ArrayList mAdded = new ArrayList();
   ArrayList mAvailBackStackIndices;
   ArrayList mBackStack;
   ArrayList mBackStackChangeListeners;
   ArrayList mBackStackIndices;
   FragmentContainer mContainer;
   ArrayList mCreatedMenus;
   int mCurState = 0;
   boolean mDestroyed;
   Runnable mExecCommit = new Runnable() {
      public void run() {
         FragmentManagerImpl.this.execPendingActions();
      }
   };
   boolean mExecutingActions;
   boolean mHavePendingDeferredStart;
   FragmentHostCallback mHost;
   private final CopyOnWriteArrayList mLifecycleCallbacks = new CopyOnWriteArrayList();
   boolean mNeedMenuInvalidate;
   int mNextFragmentIndex = 0;
   String mNoTransactionsBecause;
   Fragment mParent;
   ArrayList mPendingActions;
   ArrayList mPostponedTransactions;
   Fragment mPrimaryNav;
   FragmentManagerNonConfig mSavedNonConfig;
   SparseArray mStateArray = null;
   Bundle mStateBundle = null;
   boolean mStateSaved;
   ArrayList mTmpAddedFragments;
   ArrayList mTmpIsPop;
   ArrayList mTmpRecords;

   private void addAddedFragments(ArraySet var1) {
      if (this.mCurState >= 1) {
         int var2 = Math.min(this.mCurState, 4);
         int var3 = this.mAdded.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            Fragment var5 = (Fragment)this.mAdded.get(var4);
            if (var5.mState < var2) {
               this.moveToState(var5, var2, var5.getNextAnim(), var5.getNextTransition(), false);
               if (var5.mView != null && !var5.mHidden && var5.mIsNewlyAdded) {
                  var1.add(var5);
               }
            }
         }

      }
   }

   private void animateRemoveFragment(@NonNull final Fragment var1, @NonNull FragmentManagerImpl.AnimationOrAnimator var2, int var3) {
      final View var4 = var1.mView;
      var1.setStateAfterAnimating(var3);
      if (var2.animation != null) {
         Animation var5 = var2.animation;
         var1.setAnimatingAway(var1.mView);
         var5.setAnimationListener(new FragmentManagerImpl.AnimationListenerWrapper(getAnimationListener(var5)) {
            public void onAnimationEnd(Animation var1x) {
               super.onAnimationEnd(var1x);
               if (var1.getAnimatingAway() != null) {
                  var1.setAnimatingAway((View)null);
                  FragmentManagerImpl.this.moveToState(var1, var1.getStateAfterAnimating(), 0, 0, false);
               }

            }
         });
         setHWLayerAnimListenerIfAlpha(var4, var2);
         var1.mView.startAnimation(var5);
      } else {
         Animator var6 = var2.animator;
         var1.setAnimator(var2.animator);
         final ViewGroup var7 = var1.mContainer;
         if (var7 != null) {
            var7.startViewTransition(var4);
         }

         var6.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
               if (var7 != null) {
                  var7.endViewTransition(var4);
               }

               if (var1.getAnimator() != null) {
                  var1.setAnimator((Animator)null);
                  FragmentManagerImpl.this.moveToState(var1, var1.getStateAfterAnimating(), 0, 0, false);
               }

            }
         });
         var6.setTarget(var1.mView);
         setHWLayerAnimListenerIfAlpha(var1.mView, var2);
         var6.start();
      }

   }

   private void burpActive() {
      if (this.mActive != null) {
         for(int var1 = this.mActive.size() - 1; var1 >= 0; --var1) {
            if (this.mActive.valueAt(var1) == null) {
               this.mActive.delete(this.mActive.keyAt(var1));
            }
         }
      }

   }

   private void checkStateLoss() {
      if (this.mStateSaved) {
         throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
      } else if (this.mNoTransactionsBecause != null) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Can not perform this action inside of ");
         var1.append(this.mNoTransactionsBecause);
         throw new IllegalStateException(var1.toString());
      }
   }

   private void cleanupExec() {
      this.mExecutingActions = false;
      this.mTmpIsPop.clear();
      this.mTmpRecords.clear();
   }

   private void completeExecute(BackStackRecord var1, boolean var2, boolean var3, boolean var4) {
      if (var2) {
         var1.executePopOps(var4);
      } else {
         var1.executeOps();
      }

      ArrayList var5 = new ArrayList(1);
      ArrayList var6 = new ArrayList(1);
      var5.add(var1);
      var6.add(var2);
      if (var3) {
         FragmentTransition.startTransitions(this, var5, var6, 0, 1, true);
      }

      if (var4) {
         this.moveToState(this.mCurState, true);
      }

      if (this.mActive != null) {
         int var7 = this.mActive.size();

         for(int var8 = 0; var8 < var7; ++var8) {
            Fragment var9 = (Fragment)this.mActive.valueAt(var8);
            if (var9 != null && var9.mView != null && var9.mIsNewlyAdded && var1.interactsWith(var9.mContainerId)) {
               if (var9.mPostponedAlpha > 0.0F) {
                  var9.mView.setAlpha(var9.mPostponedAlpha);
               }

               if (var4) {
                  var9.mPostponedAlpha = 0.0F;
               } else {
                  var9.mPostponedAlpha = -1.0F;
                  var9.mIsNewlyAdded = false;
               }
            }
         }
      }

   }

   private void dispatchStateChange(int var1) {
      try {
         this.mExecutingActions = true;
         this.moveToState(var1, false);
      } finally {
         this.mExecutingActions = false;
      }

      this.execPendingActions();
   }

   private void endAnimatingAwayFragments() {
      SparseArray var1 = this.mActive;
      int var2 = 0;
      int var3;
      if (var1 == null) {
         var3 = 0;
      } else {
         var3 = this.mActive.size();
      }

      for(; var2 < var3; ++var2) {
         Fragment var7 = (Fragment)this.mActive.valueAt(var2);
         if (var7 != null) {
            if (var7.getAnimatingAway() != null) {
               int var4 = var7.getStateAfterAnimating();
               View var5 = var7.getAnimatingAway();
               var7.setAnimatingAway((View)null);
               Animation var6 = var5.getAnimation();
               if (var6 != null) {
                  var6.cancel();
                  var5.clearAnimation();
               }

               this.moveToState(var7, var4, 0, 0, false);
            } else if (var7.getAnimator() != null) {
               var7.getAnimator().end();
            }
         }
      }

   }

   private void ensureExecReady(boolean var1) {
      if (this.mExecutingActions) {
         throw new IllegalStateException("FragmentManager is already executing transactions");
      } else if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
         throw new IllegalStateException("Must be called from main thread of fragment host");
      } else {
         if (!var1) {
            this.checkStateLoss();
         }

         if (this.mTmpRecords == null) {
            this.mTmpRecords = new ArrayList();
            this.mTmpIsPop = new ArrayList();
         }

         this.mExecutingActions = true;

         try {
            this.executePostponedTransaction((ArrayList)null, (ArrayList)null);
         } finally {
            this.mExecutingActions = false;
         }

      }
   }

   private static void executeOps(ArrayList var0, ArrayList var1, int var2, int var3) {
      for(; var2 < var3; ++var2) {
         BackStackRecord var4 = (BackStackRecord)var0.get(var2);
         boolean var5 = (Boolean)var1.get(var2);
         boolean var6 = true;
         if (var5) {
            var4.bumpBackStackNesting(-1);
            if (var2 != var3 - 1) {
               var6 = false;
            }

            var4.executePopOps(var6);
         } else {
            var4.bumpBackStackNesting(1);
            var4.executeOps();
         }
      }

   }

   private void executeOpsTogether(ArrayList var1, ArrayList var2, int var3, int var4) {
      int var5 = var3;
      boolean var6 = ((BackStackRecord)var1.get(var3)).mReorderingAllowed;
      if (this.mTmpAddedFragments == null) {
         this.mTmpAddedFragments = new ArrayList();
      } else {
         this.mTmpAddedFragments.clear();
      }

      this.mTmpAddedFragments.addAll(this.mAdded);
      Fragment var7 = this.getPrimaryNavigationFragment();
      boolean var8 = false;

      int var9;
      for(var9 = var3; var9 < var4; ++var9) {
         BackStackRecord var10 = (BackStackRecord)var1.get(var9);
         if (!(Boolean)var2.get(var9)) {
            var7 = var10.expandOps(this.mTmpAddedFragments, var7);
         } else {
            var7 = var10.trackAddedFragmentsInPop(this.mTmpAddedFragments, var7);
         }

         if (!var8 && !var10.mAddToBackStack) {
            var8 = false;
         } else {
            var8 = true;
         }
      }

      this.mTmpAddedFragments.clear();
      if (!var6) {
         FragmentTransition.startTransitions(this, var1, var2, var3, var4, false);
      }

      executeOps(var1, var2, var3, var4);
      if (var6) {
         ArraySet var11 = new ArraySet();
         this.addAddedFragments(var11);
         var9 = this.postponePostponableTransactions(var1, var2, var3, var4, var11);
         this.makeRemovedFragmentsInvisible(var11);
      } else {
         var9 = var4;
      }

      var3 = var3;
      if (var9 != var5) {
         var3 = var5;
         if (var6) {
            FragmentTransition.startTransitions(this, var1, var2, var5, var9, true);
            this.moveToState(this.mCurState, true);
            var3 = var5;
         }
      }

      while(var3 < var4) {
         BackStackRecord var12 = (BackStackRecord)var1.get(var3);
         if ((Boolean)var2.get(var3) && var12.mIndex >= 0) {
            this.freeBackStackIndex(var12.mIndex);
            var12.mIndex = -1;
         }

         var12.runOnCommitRunnables();
         ++var3;
      }

      if (var8) {
         this.reportBackStackChanged();
      }

   }

   private void executePostponedTransaction(ArrayList var1, ArrayList var2) {
      int var3;
      if (this.mPostponedTransactions == null) {
         var3 = 0;
      } else {
         var3 = this.mPostponedTransactions.size();
      }

      byte var4 = 0;
      int var5 = var3;

      int var8;
      for(var3 = var4; var3 < var5; var5 = var8) {
         int var7;
         label50: {
            FragmentManagerImpl.StartEnterTransitionListener var6 = (FragmentManagerImpl.StartEnterTransitionListener)this.mPostponedTransactions.get(var3);
            if (var1 != null && !var6.mIsBack) {
               var8 = var1.indexOf(var6.mRecord);
               if (var8 != -1 && (Boolean)var2.get(var8)) {
                  var6.cancelTransaction();
                  var7 = var3;
                  var8 = var5;
                  break label50;
               }
            }

            if (!var6.isReady()) {
               var7 = var3;
               var8 = var5;
               if (var1 == null) {
                  break label50;
               }

               var7 = var3;
               var8 = var5;
               if (!var6.mRecord.interactsWith(var1, 0, var1.size())) {
                  break label50;
               }
            }

            this.mPostponedTransactions.remove(var3);
            var7 = var3 - 1;
            var8 = var5 - 1;
            if (var1 != null && !var6.mIsBack) {
               var3 = var1.indexOf(var6.mRecord);
               if (var3 != -1 && (Boolean)var2.get(var3)) {
                  var6.cancelTransaction();
                  break label50;
               }
            }

            var6.completeTransaction();
         }

         var3 = var7 + 1;
      }

   }

   private Fragment findFragmentUnder(Fragment var1) {
      ViewGroup var2 = var1.mContainer;
      View var3 = var1.mView;
      if (var2 != null && var3 != null) {
         for(int var4 = this.mAdded.indexOf(var1) - 1; var4 >= 0; --var4) {
            var1 = (Fragment)this.mAdded.get(var4);
            if (var1.mContainer == var2 && var1.mView != null) {
               return var1;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private void forcePostponedTransactions() {
      if (this.mPostponedTransactions != null) {
         while(!this.mPostponedTransactions.isEmpty()) {
            ((FragmentManagerImpl.StartEnterTransitionListener)this.mPostponedTransactions.remove(0)).completeTransaction();
         }
      }

   }

   private boolean generateOpsForPendingActions(ArrayList var1, ArrayList var2) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label513: {
         ArrayList var3;
         try {
            var3 = this.mPendingActions;
         } catch (Throwable var62) {
            var10000 = var62;
            var10001 = false;
            break label513;
         }

         int var4;
         label505: {
            var4 = 0;
            if (var3 != null) {
               try {
                  if (this.mPendingActions.size() != 0) {
                     break label505;
                  }
               } catch (Throwable var61) {
                  var10000 = var61;
                  var10001 = false;
                  break label513;
               }
            }

            try {
               return false;
            } catch (Throwable var60) {
               var10000 = var60;
               var10001 = false;
               break label513;
            }
         }

         int var5;
         try {
            var5 = this.mPendingActions.size();
         } catch (Throwable var59) {
            var10000 = var59;
            var10001 = false;
            break label513;
         }

         boolean var6;
         for(var6 = false; var4 < var5; ++var4) {
            try {
               var6 |= ((FragmentManagerImpl.OpGenerator)this.mPendingActions.get(var4)).generateOps(var1, var2);
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label513;
            }
         }

         label484:
         try {
            this.mPendingActions.clear();
            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
            return var6;
         } catch (Throwable var57) {
            var10000 = var57;
            var10001 = false;
            break label484;
         }
      }

      while(true) {
         Throwable var63 = var10000;

         try {
            throw var63;
         } catch (Throwable var56) {
            var10000 = var56;
            var10001 = false;
            continue;
         }
      }
   }

   private static AnimationListener getAnimationListener(Animation var0) {
      AnimationListener var3;
      try {
         if (sAnimationListenerField == null) {
            sAnimationListenerField = Animation.class.getDeclaredField("mListener");
            sAnimationListenerField.setAccessible(true);
         }

         var3 = (AnimationListener)sAnimationListenerField.get(var0);
         return var3;
      } catch (NoSuchFieldException var1) {
         Log.e("FragmentManager", "No field with the name mListener is found in Animation class", var1);
      } catch (IllegalAccessException var2) {
         Log.e("FragmentManager", "Cannot access Animation's mListener field", var2);
      }

      var3 = null;
      return var3;
   }

   static FragmentManagerImpl.AnimationOrAnimator makeFadeAnimation(Context var0, float var1, float var2) {
      AlphaAnimation var3 = new AlphaAnimation(var1, var2);
      var3.setInterpolator(DECELERATE_CUBIC);
      var3.setDuration(220L);
      return new FragmentManagerImpl.AnimationOrAnimator(var3);
   }

   static FragmentManagerImpl.AnimationOrAnimator makeOpenCloseAnimation(Context var0, float var1, float var2, float var3, float var4) {
      AnimationSet var7 = new AnimationSet(false);
      ScaleAnimation var5 = new ScaleAnimation(var1, var2, var1, var2, 1, 0.5F, 1, 0.5F);
      var5.setInterpolator(DECELERATE_QUINT);
      var5.setDuration(220L);
      var7.addAnimation(var5);
      AlphaAnimation var6 = new AlphaAnimation(var3, var4);
      var6.setInterpolator(DECELERATE_CUBIC);
      var6.setDuration(220L);
      var7.addAnimation(var6);
      return new FragmentManagerImpl.AnimationOrAnimator(var7);
   }

   private void makeRemovedFragmentsInvisible(ArraySet var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         Fragment var4 = (Fragment)var1.valueAt(var3);
         if (!var4.mAdded) {
            View var5 = var4.getView();
            var4.mPostponedAlpha = var5.getAlpha();
            var5.setAlpha(0.0F);
         }
      }

   }

   static boolean modifiesAlpha(Animator var0) {
      if (var0 == null) {
         return false;
      } else {
         int var1;
         if (var0 instanceof ValueAnimator) {
            PropertyValuesHolder[] var2 = ((ValueAnimator)var0).getValues();

            for(var1 = 0; var1 < var2.length; ++var1) {
               if ("alpha".equals(var2[var1].getPropertyName())) {
                  return true;
               }
            }
         } else if (var0 instanceof AnimatorSet) {
            ArrayList var3 = ((AnimatorSet)var0).getChildAnimations();

            for(var1 = 0; var1 < var3.size(); ++var1) {
               if (modifiesAlpha((Animator)var3.get(var1))) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   static boolean modifiesAlpha(FragmentManagerImpl.AnimationOrAnimator var0) {
      if (var0.animation instanceof AlphaAnimation) {
         return true;
      } else if (var0.animation instanceof AnimationSet) {
         List var2 = ((AnimationSet)var0.animation).getAnimations();

         for(int var1 = 0; var1 < var2.size(); ++var1) {
            if (var2.get(var1) instanceof AlphaAnimation) {
               return true;
            }
         }

         return false;
      } else {
         return modifiesAlpha(var0.animator);
      }
   }

   private boolean popBackStackImmediate(String var1, int var2, int var3) {
      this.execPendingActions();
      this.ensureExecReady(true);
      if (this.mPrimaryNav != null && var2 < 0 && var1 == null) {
         FragmentManager var4 = this.mPrimaryNav.peekChildFragmentManager();
         if (var4 != null && var4.popBackStackImmediate()) {
            return true;
         }
      }

      boolean var5 = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, var1, var2, var3);
      if (var5) {
         this.mExecutingActions = true;

         try {
            this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
         } finally {
            this.cleanupExec();
         }
      }

      this.doPendingDeferredStart();
      this.burpActive();
      return var5;
   }

   private int postponePostponableTransactions(ArrayList var1, ArrayList var2, int var3, int var4, ArraySet var5) {
      int var6 = var4 - 1;

      int var7;
      int var11;
      for(var7 = var4; var6 >= var3; var7 = var11) {
         BackStackRecord var8 = (BackStackRecord)var1.get(var6);
         boolean var9 = (Boolean)var2.get(var6);
         boolean var10;
         if (var8.isPostponed() && !var8.interactsWith(var1, var6 + 1, var4)) {
            var10 = true;
         } else {
            var10 = false;
         }

         var11 = var7;
         if (var10) {
            if (this.mPostponedTransactions == null) {
               this.mPostponedTransactions = new ArrayList();
            }

            FragmentManagerImpl.StartEnterTransitionListener var12 = new FragmentManagerImpl.StartEnterTransitionListener(var8, var9);
            this.mPostponedTransactions.add(var12);
            var8.setOnStartPostponedListener(var12);
            if (var9) {
               var8.executeOps();
            } else {
               var8.executePopOps(false);
            }

            var11 = var7 - 1;
            if (var6 != var11) {
               var1.remove(var6);
               var1.add(var11, var8);
            }

            this.addAddedFragments(var5);
         }

         --var6;
      }

      return var7;
   }

   private void removeRedundantOperationsAndExecute(ArrayList var1, ArrayList var2) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 != null && var1.size() == var2.size()) {
            this.executePostponedTransaction(var1, var2);
            int var3 = var1.size();
            int var4 = 0;

            int var5;
            int var7;
            for(var5 = 0; var4 < var3; var5 = var7) {
               int var6 = var4;
               var7 = var5;
               if (!((BackStackRecord)var1.get(var4)).mReorderingAllowed) {
                  if (var5 != var4) {
                     this.executeOpsTogether(var1, var2, var5, var4);
                  }

                  var5 = var4 + 1;
                  var7 = var5;
                  if ((Boolean)var2.get(var4)) {
                     while(true) {
                        var7 = var5;
                        if (var5 >= var3) {
                           break;
                        }

                        var7 = var5;
                        if (!(Boolean)var2.get(var5)) {
                           break;
                        }

                        var7 = var5;
                        if (((BackStackRecord)var1.get(var5)).mReorderingAllowed) {
                           break;
                        }

                        ++var5;
                     }
                  }

                  this.executeOpsTogether(var1, var2, var4, var7);
                  var6 = var7 - 1;
               }

               var4 = var6 + 1;
            }

            if (var5 != var3) {
               this.executeOpsTogether(var1, var2, var5, var3);
            }

         } else {
            throw new IllegalStateException("Internal error with the back stack records");
         }
      }
   }

   public static int reverseTransit(int var0) {
      short var1 = 8194;
      if (var0 != 4097) {
         if (var0 != 4099) {
            if (var0 != 8194) {
               var1 = 0;
            } else {
               var1 = 4097;
            }
         } else {
            var1 = 4099;
         }
      }

      return var1;
   }

   private void scheduleCommit() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label586: {
         ArrayList var1;
         try {
            var1 = this.mPostponedTransactions;
         } catch (Throwable var60) {
            var10000 = var60;
            var10001 = false;
            break label586;
         }

         boolean var2;
         boolean var3;
         label576: {
            label575: {
               var2 = false;
               if (var1 != null) {
                  try {
                     if (!this.mPostponedTransactions.isEmpty()) {
                        break label575;
                     }
                  } catch (Throwable var59) {
                     var10000 = var59;
                     var10001 = false;
                     break label586;
                  }
               }

               var3 = false;
               break label576;
            }

            var3 = true;
         }

         boolean var4 = var2;

         label585: {
            try {
               if (this.mPendingActions == null) {
                  break label585;
               }
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label586;
            }

            var4 = var2;

            try {
               if (this.mPendingActions.size() != 1) {
                  break label585;
               }
            } catch (Throwable var57) {
               var10000 = var57;
               var10001 = false;
               break label586;
            }

            var4 = true;
         }

         if (var3 || var4) {
            try {
               this.mHost.getHandler().removeCallbacks(this.mExecCommit);
               this.mHost.getHandler().post(this.mExecCommit);
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label586;
            }
         }

         label549:
         try {
            return;
         } catch (Throwable var55) {
            var10000 = var55;
            var10001 = false;
            break label549;
         }
      }

      while(true) {
         Throwable var61 = var10000;

         try {
            throw var61;
         } catch (Throwable var54) {
            var10000 = var54;
            var10001 = false;
            continue;
         }
      }
   }

   private static void setHWLayerAnimListenerIfAlpha(View var0, FragmentManagerImpl.AnimationOrAnimator var1) {
      if (var0 != null && var1 != null) {
         if (shouldRunOnHWLayer(var0, var1)) {
            if (var1.animator != null) {
               var1.animator.addListener(new FragmentManagerImpl.AnimatorOnHWLayerIfNeededListener(var0));
            } else {
               AnimationListener var2 = getAnimationListener(var1.animation);
               var0.setLayerType(2, (Paint)null);
               var1.animation.setAnimationListener(new FragmentManagerImpl.AnimateOnHWLayerIfNeededListener(var0, var2));
            }
         }

      }
   }

   private static void setRetaining(FragmentManagerNonConfig var0) {
      if (var0 != null) {
         List var1 = var0.getFragments();
         if (var1 != null) {
            for(Iterator var4 = var1.iterator(); var4.hasNext(); ((Fragment)var4.next()).mRetaining = true) {
            }
         }

         List var2 = var0.getChildNonConfigs();
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               setRetaining((FragmentManagerNonConfig)var3.next());
            }
         }

      }
   }

   static boolean shouldRunOnHWLayer(View var0, FragmentManagerImpl.AnimationOrAnimator var1) {
      boolean var2 = false;
      if (var0 != null && var1 != null) {
         boolean var3 = var2;
         if (VERSION.SDK_INT >= 19) {
            var3 = var2;
            if (var0.getLayerType() == 0) {
               var3 = var2;
               if (ViewCompat.hasOverlappingRendering(var0)) {
                  var3 = var2;
                  if (modifiesAlpha(var1)) {
                     var3 = true;
                  }
               }
            }
         }

         return var3;
      } else {
         return false;
      }
   }

   private void throwException(RuntimeException var1) {
      Log.e("FragmentManager", var1.getMessage());
      Log.e("FragmentManager", "Activity state:");
      PrintWriter var2 = new PrintWriter(new LogWriter("FragmentManager"));
      if (this.mHost != null) {
         try {
            this.mHost.onDump("  ", (FileDescriptor)null, var2, new String[0]);
         } catch (Exception var4) {
            Log.e("FragmentManager", "Failed dumping state", var4);
         }
      } else {
         try {
            this.dump("  ", (FileDescriptor)null, var2, new String[0]);
         } catch (Exception var3) {
            Log.e("FragmentManager", "Failed dumping state", var3);
         }
      }

      throw var1;
   }

   public static int transitToStyleIndex(int var0, boolean var1) {
      byte var2;
      if (var0 != 4097) {
         if (var0 != 4099) {
            if (var0 != 8194) {
               var2 = -1;
            } else if (var1) {
               var2 = 3;
            } else {
               var2 = 4;
            }
         } else if (var1) {
            var2 = 5;
         } else {
            var2 = 6;
         }
      } else if (var1) {
         var2 = 1;
      } else {
         var2 = 2;
      }

      return var2;
   }

   void addBackStackState(BackStackRecord var1) {
      if (this.mBackStack == null) {
         this.mBackStack = new ArrayList();
      }

      this.mBackStack.add(var1);
   }

   public void addFragment(Fragment param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1) {
      if (this.mBackStackChangeListeners == null) {
         this.mBackStackChangeListeners = new ArrayList();
      }

      this.mBackStackChangeListeners.add(var1);
   }

   public int allocBackStackIndex(BackStackRecord var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label537: {
         int var2;
         StringBuilder var61;
         label539: {
            try {
               if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                  break label539;
               }
            } catch (Throwable var59) {
               var10000 = var59;
               var10001 = false;
               break label537;
            }

            try {
               if (this.mBackStackIndices == null) {
                  ArrayList var3 = new ArrayList();
                  this.mBackStackIndices = var3;
               }
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label537;
            }

            try {
               var2 = this.mBackStackIndices.size();
               if (DEBUG) {
                  var61 = new StringBuilder();
                  var61.append("Setting back stack index ");
                  var61.append(var2);
                  var61.append(" to ");
                  var61.append(var1);
                  Log.v("FragmentManager", var61.toString());
               }
            } catch (Throwable var57) {
               var10000 = var57;
               var10001 = false;
               break label537;
            }

            try {
               this.mBackStackIndices.add(var1);
               return var2;
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label537;
            }
         }

         try {
            var2 = (Integer)this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
            if (DEBUG) {
               var61 = new StringBuilder();
               var61.append("Adding back stack index ");
               var61.append(var2);
               var61.append(" with ");
               var61.append(var1);
               Log.v("FragmentManager", var61.toString());
            }
         } catch (Throwable var55) {
            var10000 = var55;
            var10001 = false;
            break label537;
         }

         label512:
         try {
            this.mBackStackIndices.set(var2, var1);
            return var2;
         } catch (Throwable var54) {
            var10000 = var54;
            var10001 = false;
            break label512;
         }
      }

      while(true) {
         Throwable var60 = var10000;

         try {
            throw var60;
         } catch (Throwable var53) {
            var10000 = var53;
            var10001 = false;
            continue;
         }
      }
   }

   public void attachController(FragmentHostCallback var1, FragmentContainer var2, Fragment var3) {
      if (this.mHost != null) {
         throw new IllegalStateException("Already attached");
      } else {
         this.mHost = var1;
         this.mContainer = var2;
         this.mParent = var3;
      }
   }

   public void attachFragment(Fragment param1) {
      // $FF: Couldn't be decompiled
   }

   public FragmentTransaction beginTransaction() {
      return new BackStackRecord(this);
   }

   void completeShowHideFragment(final Fragment var1) {
      if (var1.mView != null) {
         FragmentManagerImpl.AnimationOrAnimator var2 = this.loadAnimation(var1, var1.getNextTransition(), var1.mHidden ^ true, var1.getNextTransitionStyle());
         if (var2 != null && var2.animator != null) {
            var2.animator.setTarget(var1.mView);
            if (var1.mHidden) {
               if (var1.isHideReplaced()) {
                  var1.setHideReplaced(false);
               } else {
                  final ViewGroup var3 = var1.mContainer;
                  final View var4 = var1.mView;
                  var3.startViewTransition(var4);
                  var2.animator.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1x) {
                        var3.endViewTransition(var4);
                        var1x.removeListener(this);
                        if (var1.mView != null) {
                           var1.mView.setVisibility(8);
                        }

                     }
                  });
               }
            } else {
               var1.mView.setVisibility(0);
            }

            setHWLayerAnimListenerIfAlpha(var1.mView, var2);
            var2.animator.start();
         } else {
            if (var2 != null) {
               setHWLayerAnimListenerIfAlpha(var1.mView, var2);
               var1.mView.startAnimation(var2.animation);
               var2.animation.start();
            }

            byte var5;
            if (var1.mHidden && !var1.isHideReplaced()) {
               var5 = 8;
            } else {
               var5 = 0;
            }

            var1.mView.setVisibility(var5);
            if (var1.isHideReplaced()) {
               var1.setHideReplaced(false);
            }
         }
      }

      if (var1.mAdded && var1.mHasMenu && var1.mMenuVisible) {
         this.mNeedMenuInvalidate = true;
      }

      var1.mHiddenChanged = false;
      var1.onHiddenChanged(var1.mHidden);
   }

   public void detachFragment(Fragment param1) {
      // $FF: Couldn't be decompiled
   }

   public void dispatchActivityCreated() {
      this.mStateSaved = false;
      this.dispatchStateChange(2);
   }

   public void dispatchConfigurationChanged(Configuration var1) {
      for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
         Fragment var3 = (Fragment)this.mAdded.get(var2);
         if (var3 != null) {
            var3.performConfigurationChanged(var1);
         }
      }

   }

   public boolean dispatchContextItemSelected(MenuItem var1) {
      for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
         Fragment var3 = (Fragment)this.mAdded.get(var2);
         if (var3 != null && var3.performContextItemSelected(var1)) {
            return true;
         }
      }

      return false;
   }

   public void dispatchCreate() {
      this.mStateSaved = false;
      this.dispatchStateChange(1);
   }

   public boolean dispatchCreateOptionsMenu(Menu var1, MenuInflater var2) {
      byte var3 = 0;
      byte var4 = 0;
      ArrayList var5 = null;

      byte var9;
      for(int var6 = var4; var6 < this.mAdded.size(); var4 = var9) {
         Fragment var7 = (Fragment)this.mAdded.get(var6);
         ArrayList var8 = var5;
         var9 = var4;
         if (var7 != null) {
            var8 = var5;
            var9 = var4;
            if (var7.performCreateOptionsMenu(var1, var2)) {
               var8 = var5;
               if (var5 == null) {
                  var8 = new ArrayList();
               }

               var8.add(var7);
               var9 = 1;
            }
         }

         ++var6;
         var5 = var8;
      }

      if (this.mCreatedMenus != null) {
         for(int var10 = var3; var10 < this.mCreatedMenus.size(); ++var10) {
            Fragment var11 = (Fragment)this.mCreatedMenus.get(var10);
            if (var5 == null || !var5.contains(var11)) {
               var11.onDestroyOptionsMenu();
            }
         }
      }

      this.mCreatedMenus = var5;
      return (boolean)var4;
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
      for(int var1 = 0; var1 < this.mAdded.size(); ++var1) {
         Fragment var2 = (Fragment)this.mAdded.get(var1);
         if (var2 != null) {
            var2.performLowMemory();
         }
      }

   }

   public void dispatchMultiWindowModeChanged(boolean var1) {
      for(int var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
         Fragment var3 = (Fragment)this.mAdded.get(var2);
         if (var3 != null) {
            var3.performMultiWindowModeChanged(var1);
         }
      }

   }

   void dispatchOnFragmentActivityCreated(Fragment var1, Bundle var2, boolean var3) {
      if (this.mParent != null) {
         FragmentManager var4 = this.mParent.getFragmentManager();
         if (var4 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var4).dispatchOnFragmentActivityCreated(var1, var2, true);
         }
      }

      Iterator var6 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var5;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var5 = (Pair)var6.next();
         } while(var3 && !(Boolean)var5.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var5.first).onFragmentActivityCreated(this, var1, var2);
      }
   }

   void dispatchOnFragmentAttached(Fragment var1, Context var2, boolean var3) {
      if (this.mParent != null) {
         FragmentManager var4 = this.mParent.getFragmentManager();
         if (var4 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var4).dispatchOnFragmentAttached(var1, var2, true);
         }
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var6;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var6 = (Pair)var5.next();
         } while(var3 && !(Boolean)var6.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var6.first).onFragmentAttached(this, var1, var2);
      }
   }

   void dispatchOnFragmentCreated(Fragment var1, Bundle var2, boolean var3) {
      if (this.mParent != null) {
         FragmentManager var4 = this.mParent.getFragmentManager();
         if (var4 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var4).dispatchOnFragmentCreated(var1, var2, true);
         }
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var6;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var6 = (Pair)var5.next();
         } while(var3 && !(Boolean)var6.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var6.first).onFragmentCreated(this, var1, var2);
      }
   }

   void dispatchOnFragmentDestroyed(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentDestroyed(var1, true);
         }
      }

      Iterator var4 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var5;
         do {
            if (!var4.hasNext()) {
               return;
            }

            var5 = (Pair)var4.next();
         } while(var2 && !(Boolean)var5.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var5.first).onFragmentDestroyed(this, var1);
      }
   }

   void dispatchOnFragmentDetached(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentDetached(var1, true);
         }
      }

      Iterator var4 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var5;
         do {
            if (!var4.hasNext()) {
               return;
            }

            var5 = (Pair)var4.next();
         } while(var2 && !(Boolean)var5.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var5.first).onFragmentDetached(this, var1);
      }
   }

   void dispatchOnFragmentPaused(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentPaused(var1, true);
         }
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (Pair)var5.next();
         } while(var2 && !(Boolean)var4.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var4.first).onFragmentPaused(this, var1);
      }
   }

   void dispatchOnFragmentPreAttached(Fragment var1, Context var2, boolean var3) {
      if (this.mParent != null) {
         FragmentManager var4 = this.mParent.getFragmentManager();
         if (var4 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var4).dispatchOnFragmentPreAttached(var1, var2, true);
         }
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var6;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var6 = (Pair)var5.next();
         } while(var3 && !(Boolean)var6.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var6.first).onFragmentPreAttached(this, var1, var2);
      }
   }

   void dispatchOnFragmentPreCreated(Fragment var1, Bundle var2, boolean var3) {
      if (this.mParent != null) {
         FragmentManager var4 = this.mParent.getFragmentManager();
         if (var4 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var4).dispatchOnFragmentPreCreated(var1, var2, true);
         }
      }

      Iterator var6 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var5;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var5 = (Pair)var6.next();
         } while(var3 && !(Boolean)var5.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var5.first).onFragmentPreCreated(this, var1, var2);
      }
   }

   void dispatchOnFragmentResumed(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentResumed(var1, true);
         }
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (Pair)var5.next();
         } while(var2 && !(Boolean)var4.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var4.first).onFragmentResumed(this, var1);
      }
   }

   void dispatchOnFragmentSaveInstanceState(Fragment var1, Bundle var2, boolean var3) {
      if (this.mParent != null) {
         FragmentManager var4 = this.mParent.getFragmentManager();
         if (var4 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var4).dispatchOnFragmentSaveInstanceState(var1, var2, true);
         }
      }

      Iterator var6 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var5;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var5 = (Pair)var6.next();
         } while(var3 && !(Boolean)var5.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var5.first).onFragmentSaveInstanceState(this, var1, var2);
      }
   }

   void dispatchOnFragmentStarted(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentStarted(var1, true);
         }
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (Pair)var5.next();
         } while(var2 && !(Boolean)var4.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var4.first).onFragmentStarted(this, var1);
      }
   }

   void dispatchOnFragmentStopped(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentStopped(var1, true);
         }
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (Pair)var5.next();
         } while(var2 && !(Boolean)var4.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var4.first).onFragmentStopped(this, var1);
      }
   }

   void dispatchOnFragmentViewCreated(Fragment var1, View var2, Bundle var3, boolean var4) {
      if (this.mParent != null) {
         FragmentManager var5 = this.mParent.getFragmentManager();
         if (var5 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var5).dispatchOnFragmentViewCreated(var1, var2, var3, true);
         }
      }

      Iterator var6 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var7;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var7 = (Pair)var6.next();
         } while(var4 && !(Boolean)var7.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var7.first).onFragmentViewCreated(this, var1, var2, var3);
      }
   }

   void dispatchOnFragmentViewDestroyed(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentViewDestroyed(var1, true);
         }
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         Pair var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (Pair)var5.next();
         } while(var2 && !(Boolean)var4.second);

         ((FragmentManager.FragmentLifecycleCallbacks)var4.first).onFragmentViewDestroyed(this, var1);
      }
   }

   public boolean dispatchOptionsItemSelected(MenuItem var1) {
      for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
         Fragment var3 = (Fragment)this.mAdded.get(var2);
         if (var3 != null && var3.performOptionsItemSelected(var1)) {
            return true;
         }
      }

      return false;
   }

   public void dispatchOptionsMenuClosed(Menu var1) {
      for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
         Fragment var3 = (Fragment)this.mAdded.get(var2);
         if (var3 != null) {
            var3.performOptionsMenuClosed(var1);
         }
      }

   }

   public void dispatchPause() {
      this.dispatchStateChange(4);
   }

   public void dispatchPictureInPictureModeChanged(boolean var1) {
      for(int var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
         Fragment var3 = (Fragment)this.mAdded.get(var2);
         if (var3 != null) {
            var3.performPictureInPictureModeChanged(var1);
         }
      }

   }

   public boolean dispatchPrepareOptionsMenu(Menu var1) {
      int var2 = 0;

      boolean var3;
      boolean var5;
      for(var3 = false; var2 < this.mAdded.size(); var3 = var5) {
         Fragment var4 = (Fragment)this.mAdded.get(var2);
         var5 = var3;
         if (var4 != null) {
            var5 = var3;
            if (var4.performPrepareOptionsMenu(var1)) {
               var5 = true;
            }
         }

         ++var2;
      }

      return var3;
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
         int var1 = 0;

         int var2;
         int var4;
         for(var2 = var1; var1 < this.mActive.size(); var2 = var4) {
            Fragment var3 = (Fragment)this.mActive.valueAt(var1);
            var4 = var2;
            if (var3 != null) {
               var4 = var2;
               if (var3.mLoaderManager != null) {
                  var4 = var2 | var3.mLoaderManager.hasRunningLoaders();
               }
            }

            ++var1;
         }

         if (var2 == 0) {
            this.mHavePendingDeferredStart = false;
            this.startPendingDeferredFragments();
         }
      }

   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append("    ");
      String var55 = var5.toString();
      SparseArray var6 = this.mActive;
      byte var7 = 0;
      int var8;
      int var9;
      Fragment var56;
      if (var6 != null) {
         var8 = this.mActive.size();
         if (var8 > 0) {
            var3.print(var1);
            var3.print("Active Fragments in ");
            var3.print(Integer.toHexString(System.identityHashCode(this)));
            var3.println(":");

            for(var9 = 0; var9 < var8; ++var9) {
               var56 = (Fragment)this.mActive.valueAt(var9);
               var3.print(var1);
               var3.print("  #");
               var3.print(var9);
               var3.print(": ");
               var3.println(var56);
               if (var56 != null) {
                  var56.dump(var55, var2, var3, var4);
               }
            }
         }
      }

      var8 = this.mAdded.size();
      if (var8 > 0) {
         var3.print(var1);
         var3.println("Added Fragments:");

         for(var9 = 0; var9 < var8; ++var9) {
            var56 = (Fragment)this.mAdded.get(var9);
            var3.print(var1);
            var3.print("  #");
            var3.print(var9);
            var3.print(": ");
            var3.println(var56.toString());
         }
      }

      if (this.mCreatedMenus != null) {
         var8 = this.mCreatedMenus.size();
         if (var8 > 0) {
            var3.print(var1);
            var3.println("Fragments Created Menus:");

            for(var9 = 0; var9 < var8; ++var9) {
               var56 = (Fragment)this.mCreatedMenus.get(var9);
               var3.print(var1);
               var3.print("  #");
               var3.print(var9);
               var3.print(": ");
               var3.println(var56.toString());
            }
         }
      }

      if (this.mBackStack != null) {
         var8 = this.mBackStack.size();
         if (var8 > 0) {
            var3.print(var1);
            var3.println("Back Stack:");

            for(var9 = 0; var9 < var8; ++var9) {
               BackStackRecord var57 = (BackStackRecord)this.mBackStack.get(var9);
               var3.print(var1);
               var3.print("  #");
               var3.print(var9);
               var3.print(": ");
               var3.println(var57.toString());
               var57.dump(var55, var2, var3, var4);
            }
         }
      }

      synchronized(this){}

      label1026: {
         Throwable var10000;
         boolean var10001;
         label1027: {
            label1028: {
               try {
                  if (this.mBackStackIndices == null) {
                     break label1028;
                  }

                  var8 = this.mBackStackIndices.size();
               } catch (Throwable var51) {
                  var10000 = var51;
                  var10001 = false;
                  break label1027;
               }

               if (var8 > 0) {
                  try {
                     var3.print(var1);
                     var3.println("Back Stack Indices:");
                  } catch (Throwable var50) {
                     var10000 = var50;
                     var10001 = false;
                     break label1027;
                  }

                  for(var9 = 0; var9 < var8; ++var9) {
                     try {
                        BackStackRecord var53 = (BackStackRecord)this.mBackStackIndices.get(var9);
                        var3.print(var1);
                        var3.print("  #");
                        var3.print(var9);
                        var3.print(": ");
                        var3.println(var53);
                     } catch (Throwable var49) {
                        var10000 = var49;
                        var10001 = false;
                        break label1027;
                     }
                  }
               }
            }

            try {
               if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                  var3.print(var1);
                  var3.print("mAvailBackStackIndices: ");
                  var3.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
               }
            } catch (Throwable var48) {
               var10000 = var48;
               var10001 = false;
               break label1027;
            }

            label969:
            try {
               break label1026;
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label969;
            }
         }

         while(true) {
            Throwable var52 = var10000;

            try {
               throw var52;
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               continue;
            }
         }
      }

      if (this.mPendingActions != null) {
         var8 = this.mPendingActions.size();
         if (var8 > 0) {
            var3.print(var1);
            var3.println("Pending Actions:");

            for(var9 = var7; var9 < var8; ++var9) {
               FragmentManagerImpl.OpGenerator var54 = (FragmentManagerImpl.OpGenerator)this.mPendingActions.get(var9);
               var3.print(var1);
               var3.print("  #");
               var3.print(var9);
               var3.print(": ");
               var3.println(var54);
            }
         }
      }

      var3.print(var1);
      var3.println("FragmentManager misc state:");
      var3.print(var1);
      var3.print("  mHost=");
      var3.println(this.mHost);
      var3.print(var1);
      var3.print("  mContainer=");
      var3.println(this.mContainer);
      if (this.mParent != null) {
         var3.print(var1);
         var3.print("  mParent=");
         var3.println(this.mParent);
      }

      var3.print(var1);
      var3.print("  mCurState=");
      var3.print(this.mCurState);
      var3.print(" mStateSaved=");
      var3.print(this.mStateSaved);
      var3.print(" mDestroyed=");
      var3.println(this.mDestroyed);
      if (this.mNeedMenuInvalidate) {
         var3.print(var1);
         var3.print("  mNeedMenuInvalidate=");
         var3.println(this.mNeedMenuInvalidate);
      }

      if (this.mNoTransactionsBecause != null) {
         var3.print(var1);
         var3.print("  mNoTransactionsBecause=");
         var3.println(this.mNoTransactionsBecause);
      }

   }

   public void enqueueAction(FragmentManagerImpl.OpGenerator var1, boolean var2) {
      if (!var2) {
         this.checkStateLoss();
      }

      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label407: {
         label400: {
            try {
               if (!this.mDestroyed && this.mHost != null) {
                  break label400;
               }
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label407;
            }

            if (var2) {
               try {
                  return;
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label407;
               }
            } else {
               try {
                  IllegalStateException var46 = new IllegalStateException("Activity has been destroyed");
                  throw var46;
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label407;
               }
            }
         }

         try {
            if (this.mPendingActions == null) {
               ArrayList var3 = new ArrayList();
               this.mPendingActions = var3;
            }
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label407;
         }

         label384:
         try {
            this.mPendingActions.add(var1);
            this.scheduleCommit();
            return;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label384;
         }
      }

      while(true) {
         Throwable var47 = var10000;

         try {
            throw var47;
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            continue;
         }
      }
   }

   void ensureInflatedFragmentView(Fragment var1) {
      if (var1.mFromLayout && !var1.mPerformedCreateView) {
         var1.mView = var1.performCreateView(var1.performGetLayoutInflater(var1.mSavedFragmentState), (ViewGroup)null, var1.mSavedFragmentState);
         if (var1.mView != null) {
            var1.mInnerView = var1.mView;
            var1.mView.setSaveFromParentEnabled(false);
            if (var1.mHidden) {
               var1.mView.setVisibility(8);
            }

            var1.onViewCreated(var1.mView, var1.mSavedFragmentState);
            this.dispatchOnFragmentViewCreated(var1, var1.mView, var1.mSavedFragmentState, false);
         } else {
            var1.mInnerView = null;
         }
      }

   }

   public boolean execPendingActions() {
      this.ensureExecReady(true);

      boolean var1;
      for(var1 = false; this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop); var1 = true) {
         this.mExecutingActions = true;

         try {
            this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
         } finally {
            this.cleanupExec();
         }
      }

      this.doPendingDeferredStart();
      this.burpActive();
      return var1;
   }

   public void execSingleAction(FragmentManagerImpl.OpGenerator var1, boolean var2) {
      if (!var2 || this.mHost != null && !this.mDestroyed) {
         this.ensureExecReady(var2);
         if (var1.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;

            try {
               this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
               this.cleanupExec();
            }
         }

         this.doPendingDeferredStart();
         this.burpActive();
      }
   }

   public boolean executePendingTransactions() {
      boolean var1 = this.execPendingActions();
      this.forcePostponedTransactions();
      return var1;
   }

   public Fragment findFragmentById(int var1) {
      int var2;
      Fragment var3;
      for(var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
         var3 = (Fragment)this.mAdded.get(var2);
         if (var3 != null && var3.mFragmentId == var1) {
            return var3;
         }
      }

      if (this.mActive != null) {
         for(var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            var3 = (Fragment)this.mActive.valueAt(var2);
            if (var3 != null && var3.mFragmentId == var1) {
               return var3;
            }
         }
      }

      return null;
   }

   public Fragment findFragmentByTag(String var1) {
      int var2;
      Fragment var3;
      if (var1 != null) {
         for(var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null && var1.equals(var3.mTag)) {
               return var3;
            }
         }
      }

      if (this.mActive != null && var1 != null) {
         for(var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            var3 = (Fragment)this.mActive.valueAt(var2);
            if (var3 != null && var1.equals(var3.mTag)) {
               return var3;
            }
         }
      }

      return null;
   }

   public Fragment findFragmentByWho(String var1) {
      if (this.mActive != null && var1 != null) {
         for(int var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            Fragment var3 = (Fragment)this.mActive.valueAt(var2);
            if (var3 != null) {
               var3 = var3.findFragmentByWho(var1);
               if (var3 != null) {
                  return var3;
               }
            }
         }
      }

      return null;
   }

   public void freeBackStackIndex(int var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label196: {
         try {
            this.mBackStackIndices.set(var1, (Object)null);
            if (this.mAvailBackStackIndices == null) {
               ArrayList var2 = new ArrayList();
               this.mAvailBackStackIndices = var2;
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label196;
         }

         try {
            if (DEBUG) {
               StringBuilder var23 = new StringBuilder();
               var23.append("Freeing back stack index ");
               var23.append(var1);
               Log.v("FragmentManager", var23.toString());
            }
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label196;
         }

         label186:
         try {
            this.mAvailBackStackIndices.add(var1);
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label186;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   int getActiveFragmentCount() {
      return this.mActive == null ? 0 : this.mActive.size();
   }

   List getActiveFragments() {
      if (this.mActive == null) {
         return null;
      } else {
         int var1 = this.mActive.size();
         ArrayList var2 = new ArrayList(var1);

         for(int var3 = 0; var3 < var1; ++var3) {
            var2.add(this.mActive.valueAt(var3));
         }

         return var2;
      }
   }

   public FragmentManager.BackStackEntry getBackStackEntryAt(int var1) {
      return (FragmentManager.BackStackEntry)this.mBackStack.get(var1);
   }

   public int getBackStackEntryCount() {
      int var1;
      if (this.mBackStack != null) {
         var1 = this.mBackStack.size();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public Fragment getFragment(Bundle var1, String var2) {
      int var3 = var1.getInt(var2, -1);
      if (var3 == -1) {
         return null;
      } else {
         Fragment var4 = (Fragment)this.mActive.get(var3);
         if (var4 == null) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Fragment no longer exists for key ");
            var5.append(var2);
            var5.append(": index ");
            var5.append(var3);
            this.throwException(new IllegalStateException(var5.toString()));
         }

         return var4;
      }
   }

   public List getFragments() {
      // $FF: Couldn't be decompiled
   }

   Factory2 getLayoutInflaterFactory() {
      return this;
   }

   public Fragment getPrimaryNavigationFragment() {
      return this.mPrimaryNav;
   }

   public void hideFragment(Fragment var1) {
      if (DEBUG) {
         StringBuilder var2 = new StringBuilder();
         var2.append("hide: ");
         var2.append(var1);
         Log.v("FragmentManager", var2.toString());
      }

      if (!var1.mHidden) {
         var1.mHidden = true;
         var1.mHiddenChanged ^= true;
      }

   }

   public boolean isDestroyed() {
      return this.mDestroyed;
   }

   boolean isStateAtLeast(int var1) {
      boolean var2;
      if (this.mCurState >= var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isStateSaved() {
      return this.mStateSaved;
   }

   FragmentManagerImpl.AnimationOrAnimator loadAnimation(Fragment var1, int var2, boolean var3, int var4) {
      int var5 = var1.getNextAnim();
      Animation var6 = var1.onCreateAnimation(var2, var3, var5);
      if (var6 != null) {
         return new FragmentManagerImpl.AnimationOrAnimator(var6);
      } else {
         Animator var16 = var1.onCreateAnimator(var2, var3, var5);
         if (var16 != null) {
            return new FragmentManagerImpl.AnimationOrAnimator(var16);
         } else {
            if (var5 != 0) {
               boolean var7 = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(var5));
               boolean var8 = false;
               boolean var9 = var8;
               Animation var17;
               boolean var10001;
               FragmentManagerImpl.AnimationOrAnimator var19;
               if (var7) {
                  label116: {
                     NotFoundException var10000;
                     label97: {
                        label120: {
                           try {
                              var17 = AnimationUtils.loadAnimation(this.mHost.getContext(), var5);
                           } catch (NotFoundException var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label97;
                           } catch (RuntimeException var15) {
                              var10001 = false;
                              break label120;
                           }

                           if (var17 == null) {
                              var9 = true;
                              break label116;
                           }

                           try {
                              var19 = new FragmentManagerImpl.AnimationOrAnimator(var17);
                              return var19;
                           } catch (NotFoundException var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label97;
                           } catch (RuntimeException var13) {
                              var10001 = false;
                           }
                        }

                        var9 = var8;
                        break label116;
                     }

                     NotFoundException var18 = var10000;
                     throw var18;
                  }
               }

               if (!var9) {
                  label110: {
                     RuntimeException var21;
                     label111: {
                        try {
                           var16 = AnimatorInflater.loadAnimator(this.mHost.getContext(), var5);
                        } catch (RuntimeException var11) {
                           var21 = var11;
                           var10001 = false;
                           break label111;
                        }

                        if (var16 == null) {
                           break label110;
                        }

                        try {
                           var19 = new FragmentManagerImpl.AnimationOrAnimator(var16);
                           return var19;
                        } catch (RuntimeException var10) {
                           var21 = var10;
                           var10001 = false;
                        }
                     }

                     RuntimeException var20 = var21;
                     if (var7) {
                        throw var20;
                     }

                     var17 = AnimationUtils.loadAnimation(this.mHost.getContext(), var5);
                     if (var17 != null) {
                        return new FragmentManagerImpl.AnimationOrAnimator(var17);
                     }
                  }
               }
            }

            if (var2 == 0) {
               return null;
            } else {
               var2 = transitToStyleIndex(var2, var3);
               if (var2 < 0) {
                  return null;
               } else {
                  switch(var2) {
                  case 1:
                     return makeOpenCloseAnimation(this.mHost.getContext(), 1.125F, 1.0F, 0.0F, 1.0F);
                  case 2:
                     return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 0.975F, 1.0F, 0.0F);
                  case 3:
                     return makeOpenCloseAnimation(this.mHost.getContext(), 0.975F, 1.0F, 0.0F, 1.0F);
                  case 4:
                     return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 1.075F, 1.0F, 0.0F);
                  case 5:
                     return makeFadeAnimation(this.mHost.getContext(), 0.0F, 1.0F);
                  case 6:
                     return makeFadeAnimation(this.mHost.getContext(), 1.0F, 0.0F);
                  default:
                     var2 = var4;
                     if (var4 == 0) {
                        var2 = var4;
                        if (this.mHost.onHasWindowAnimations()) {
                           var2 = this.mHost.onGetWindowAnimations();
                        }
                     }

                     return var2 == 0 ? null : null;
                  }
               }
            }
         }
      }
   }

   void makeActive(Fragment var1) {
      if (var1.mIndex < 0) {
         int var2 = this.mNextFragmentIndex++;
         var1.setIndex(var2, this.mParent);
         if (this.mActive == null) {
            this.mActive = new SparseArray();
         }

         this.mActive.put(var1.mIndex, var1);
         if (DEBUG) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Allocated fragment index ");
            var3.append(var1);
            Log.v("FragmentManager", var3.toString());
         }

      }
   }

   void makeInactive(Fragment var1) {
      if (var1.mIndex >= 0) {
         if (DEBUG) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Freeing fragment index ");
            var2.append(var1);
            Log.v("FragmentManager", var2.toString());
         }

         this.mActive.put(var1.mIndex, (Object)null);
         this.mHost.inactivateFragment(var1.mWho);
         var1.initState();
      }
   }

   void moveFragmentToExpectedState(Fragment var1) {
      if (var1 != null) {
         int var2 = this.mCurState;
         int var3 = var2;
         if (var1.mRemoving) {
            if (var1.isInBackStack()) {
               var3 = Math.min(var2, 1);
            } else {
               var3 = Math.min(var2, 0);
            }
         }

         this.moveToState(var1, var3, var1.getNextTransition(), var1.getNextTransitionStyle(), false);
         if (var1.mView != null) {
            Fragment var4 = this.findFragmentUnder(var1);
            if (var4 != null) {
               View var6 = var4.mView;
               ViewGroup var5 = var1.mContainer;
               var3 = var5.indexOfChild(var6);
               var2 = var5.indexOfChild(var1.mView);
               if (var2 < var3) {
                  var5.removeViewAt(var2);
                  var5.addView(var1.mView, var3);
               }
            }

            if (var1.mIsNewlyAdded && var1.mContainer != null) {
               if (var1.mPostponedAlpha > 0.0F) {
                  var1.mView.setAlpha(var1.mPostponedAlpha);
               }

               var1.mPostponedAlpha = 0.0F;
               var1.mIsNewlyAdded = false;
               FragmentManagerImpl.AnimationOrAnimator var7 = this.loadAnimation(var1, var1.getNextTransition(), true, var1.getNextTransitionStyle());
               if (var7 != null) {
                  setHWLayerAnimListenerIfAlpha(var1.mView, var7);
                  if (var7.animation != null) {
                     var1.mView.startAnimation(var7.animation);
                  } else {
                     var7.animator.setTarget(var1.mView);
                     var7.animator.start();
                  }
               }
            }
         }

         if (var1.mHiddenChanged) {
            this.completeShowHideFragment(var1);
         }

      }
   }

   void moveToState(int var1, boolean var2) {
      if (this.mHost == null && var1 != 0) {
         throw new IllegalStateException("No activity");
      } else if (var2 || var1 != this.mCurState) {
         this.mCurState = var1;
         if (this.mActive != null) {
            int var3 = this.mAdded.size();
            int var4 = 0;

            Fragment var5;
            int var6;
            for(var1 = var4; var4 < var3; var1 = var6) {
               var5 = (Fragment)this.mAdded.get(var4);
               this.moveFragmentToExpectedState(var5);
               var6 = var1;
               if (var5.mLoaderManager != null) {
                  var6 = var1 | var5.mLoaderManager.hasRunningLoaders();
               }

               ++var4;
            }

            var3 = this.mActive.size();

            for(var6 = 0; var6 < var3; var1 = var4) {
               var5 = (Fragment)this.mActive.valueAt(var6);
               var4 = var1;
               if (var5 != null) {
                  label73: {
                     if (!var5.mRemoving) {
                        var4 = var1;
                        if (!var5.mDetached) {
                           break label73;
                        }
                     }

                     var4 = var1;
                     if (!var5.mIsNewlyAdded) {
                        this.moveFragmentToExpectedState(var5);
                        var4 = var1;
                        if (var5.mLoaderManager != null) {
                           var4 = var1 | var5.mLoaderManager.hasRunningLoaders();
                        }
                     }
                  }
               }

               ++var6;
            }

            if (var1 == 0) {
               this.startPendingDeferredFragments();
            }

            if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
               this.mHost.onSupportInvalidateOptionsMenu();
               this.mNeedMenuInvalidate = false;
            }
         }

      }
   }

   void moveToState(Fragment var1) {
      this.moveToState(var1, this.mCurState, 0, 0, false);
   }

   void moveToState(Fragment var1, int var2, int var3, int var4, boolean var5) {
      boolean var6 = var1.mAdded;
      byte var7 = 1;
      boolean var8 = true;
      int var9;
      if (!var6 || var1.mDetached) {
         var9 = var2;
         var2 = var2;
         if (var9 > 1) {
            var2 = 1;
         }
      }

      var9 = var2;
      if (var1.mRemoving) {
         var9 = var2;
         if (var2 > var1.mState) {
            if (var1.mState == 0 && var1.isInBackStack()) {
               var9 = 1;
            } else {
               var9 = var1.mState;
            }
         }
      }

      if (var1.mDeferStart && var1.mState < 4 && var9 > 3) {
         var2 = 3;
      } else {
         var2 = var9;
      }

      StringBuilder var10;
      if (var1.mState <= var2) {
         label323: {
            if (var1.mFromLayout && !var1.mInLayout) {
               return;
            }

            if (var1.getAnimatingAway() != null || var1.getAnimator() != null) {
               var1.setAnimatingAway((View)null);
               var1.setAnimator((Animator)null);
               this.moveToState(var1, var1.getStateAfterAnimating(), 0, 0, true);
            }

            label274: {
               var9 = var2;
               var4 = var2;
               int var14 = var2;
               var3 = var2;
               switch(var1.mState) {
               case 0:
                  var9 = var2;
                  if (var2 > 0) {
                     if (DEBUG) {
                        var10 = new StringBuilder();
                        var10.append("moveto CREATED: ");
                        var10.append(var1);
                        Log.v("FragmentManager", var10.toString());
                     }

                     var9 = var2;
                     if (var1.mSavedFragmentState != null) {
                        var1.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                        var1.mSavedViewState = var1.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                        var1.mTarget = this.getFragment(var1.mSavedFragmentState, "android:target_state");
                        if (var1.mTarget != null) {
                           var1.mTargetRequestCode = var1.mSavedFragmentState.getInt("android:target_req_state", 0);
                        }

                        var1.mUserVisibleHint = var1.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
                        var9 = var2;
                        if (!var1.mUserVisibleHint) {
                           var1.mDeferStart = true;
                           var9 = var2;
                           if (var2 > 3) {
                              var9 = 3;
                           }
                        }
                     }

                     var1.mHost = this.mHost;
                     var1.mParentFragment = this.mParent;
                     FragmentManagerImpl var15;
                     if (this.mParent != null) {
                        var15 = this.mParent.mChildFragmentManager;
                     } else {
                        var15 = this.mHost.getFragmentManagerImpl();
                     }

                     var1.mFragmentManager = var15;
                     if (var1.mTarget != null) {
                        if (this.mActive.get(var1.mTarget.mIndex) != var1.mTarget) {
                           var10 = new StringBuilder();
                           var10.append("Fragment ");
                           var10.append(var1);
                           var10.append(" declared target fragment ");
                           var10.append(var1.mTarget);
                           var10.append(" that does not belong to this FragmentManager!");
                           throw new IllegalStateException(var10.toString());
                        }

                        if (var1.mTarget.mState < 1) {
                           this.moveToState(var1.mTarget, 1, 0, 0, true);
                        }
                     }

                     this.dispatchOnFragmentPreAttached(var1, this.mHost.getContext(), false);
                     var1.mCalled = false;
                     var1.onAttach(this.mHost.getContext());
                     if (!var1.mCalled) {
                        var10 = new StringBuilder();
                        var10.append("Fragment ");
                        var10.append(var1);
                        var10.append(" did not call through to super.onAttach()");
                        throw new SuperNotCalledException(var10.toString());
                     }

                     if (var1.mParentFragment == null) {
                        this.mHost.onAttachFragment(var1);
                     } else {
                        var1.mParentFragment.onAttachFragment(var1);
                     }

                     this.dispatchOnFragmentAttached(var1, this.mHost.getContext(), false);
                     if (!var1.mIsCreated) {
                        this.dispatchOnFragmentPreCreated(var1, var1.mSavedFragmentState, false);
                        var1.performCreate(var1.mSavedFragmentState);
                        this.dispatchOnFragmentCreated(var1, var1.mSavedFragmentState, false);
                     } else {
                        var1.restoreChildFragmentState(var1.mSavedFragmentState);
                        var1.mState = 1;
                     }

                     var1.mRetaining = false;
                  }
               case 1:
                  this.ensureInflatedFragmentView(var1);
                  var4 = var9;
                  if (var9 > 1) {
                     if (DEBUG) {
                        var10 = new StringBuilder();
                        var10.append("moveto ACTIVITY_CREATED: ");
                        var10.append(var1);
                        Log.v("FragmentManager", var10.toString());
                     }

                     if (!var1.mFromLayout) {
                        ViewGroup var16;
                        if (var1.mContainerId != 0) {
                           if (var1.mContainerId == -1) {
                              var10 = new StringBuilder();
                              var10.append("Cannot create fragment ");
                              var10.append(var1);
                              var10.append(" for a container view with no id");
                              this.throwException(new IllegalArgumentException(var10.toString()));
                           }

                           ViewGroup var11 = (ViewGroup)this.mContainer.onFindViewById(var1.mContainerId);
                           var16 = var11;
                           if (var11 == null) {
                              var16 = var11;
                              if (!var1.mRestored) {
                                 String var17;
                                 try {
                                    var17 = var1.getResources().getResourceName(var1.mContainerId);
                                 } catch (NotFoundException var13) {
                                    var17 = "unknown";
                                 }

                                 StringBuilder var12 = new StringBuilder();
                                 var12.append("No view found for id 0x");
                                 var12.append(Integer.toHexString(var1.mContainerId));
                                 var12.append(" (");
                                 var12.append(var17);
                                 var12.append(") for fragment ");
                                 var12.append(var1);
                                 this.throwException(new IllegalArgumentException(var12.toString()));
                                 var16 = var11;
                              }
                           }
                        } else {
                           var16 = null;
                        }

                        var1.mContainer = var16;
                        var1.mView = var1.performCreateView(var1.performGetLayoutInflater(var1.mSavedFragmentState), var16, var1.mSavedFragmentState);
                        if (var1.mView == null) {
                           var1.mInnerView = null;
                        } else {
                           var1.mInnerView = var1.mView;
                           var1.mView.setSaveFromParentEnabled(false);
                           if (var16 != null) {
                              var16.addView(var1.mView);
                           }

                           if (var1.mHidden) {
                              var1.mView.setVisibility(8);
                           }

                           var1.onViewCreated(var1.mView, var1.mSavedFragmentState);
                           this.dispatchOnFragmentViewCreated(var1, var1.mView, var1.mSavedFragmentState, false);
                           if (var1.mView.getVisibility() == 0 && var1.mContainer != null) {
                              var5 = var8;
                           } else {
                              var5 = false;
                           }

                           var1.mIsNewlyAdded = var5;
                        }
                     }

                     var1.performActivityCreated(var1.mSavedFragmentState);
                     this.dispatchOnFragmentActivityCreated(var1, var1.mSavedFragmentState, false);
                     if (var1.mView != null) {
                        var1.restoreViewState(var1.mSavedFragmentState);
                     }

                     var1.mSavedFragmentState = null;
                     var4 = var9;
                  }
               case 2:
                  var14 = var4;
                  if (var4 > 2) {
                     var1.mState = 3;
                     var14 = var4;
                  }
               case 3:
                  break;
               case 4:
                  break label274;
               default:
                  var9 = var2;
                  break label323;
               }

               var3 = var14;
               if (var14 > 3) {
                  if (DEBUG) {
                     var10 = new StringBuilder();
                     var10.append("moveto STARTED: ");
                     var10.append(var1);
                     Log.v("FragmentManager", var10.toString());
                  }

                  var1.performStart();
                  this.dispatchOnFragmentStarted(var1, false);
                  var3 = var14;
               }
            }

            var9 = var3;
            if (var3 > 4) {
               if (DEBUG) {
                  var10 = new StringBuilder();
                  var10.append("moveto RESUMED: ");
                  var10.append(var1);
                  Log.v("FragmentManager", var10.toString());
               }

               var1.performResume();
               this.dispatchOnFragmentResumed(var1, false);
               var1.mSavedFragmentState = null;
               var1.mSavedViewState = null;
               var9 = var3;
            }
         }
      } else {
         var9 = var2;
         if (var1.mState > var2) {
            switch(var1.mState) {
            case 5:
               if (var2 < 5) {
                  if (DEBUG) {
                     var10 = new StringBuilder();
                     var10.append("movefrom RESUMED: ");
                     var10.append(var1);
                     Log.v("FragmentManager", var10.toString());
                  }

                  var1.performPause();
                  this.dispatchOnFragmentPaused(var1, false);
               }
            case 4:
               if (var2 < 4) {
                  if (DEBUG) {
                     var10 = new StringBuilder();
                     var10.append("movefrom STARTED: ");
                     var10.append(var1);
                     Log.v("FragmentManager", var10.toString());
                  }

                  var1.performStop();
                  this.dispatchOnFragmentStopped(var1, false);
               }
            case 3:
               if (var2 < 3) {
                  if (DEBUG) {
                     var10 = new StringBuilder();
                     var10.append("movefrom STOPPED: ");
                     var10.append(var1);
                     Log.v("FragmentManager", var10.toString());
                  }

                  var1.performReallyStop();
               }
            case 2:
               if (var2 < 2) {
                  if (DEBUG) {
                     var10 = new StringBuilder();
                     var10.append("movefrom ACTIVITY_CREATED: ");
                     var10.append(var1);
                     Log.v("FragmentManager", var10.toString());
                  }

                  if (var1.mView != null && this.mHost.onShouldSaveFragmentState(var1) && var1.mSavedViewState == null) {
                     this.saveFragmentViewState(var1);
                  }

                  var1.performDestroyView();
                  this.dispatchOnFragmentViewDestroyed(var1, false);
                  if (var1.mView != null && var1.mContainer != null) {
                     var1.mView.clearAnimation();
                     var1.mContainer.endViewTransition(var1.mView);
                     FragmentManagerImpl.AnimationOrAnimator var18;
                     if (this.mCurState > 0 && !this.mDestroyed && var1.mView.getVisibility() == 0 && var1.mPostponedAlpha >= 0.0F) {
                        var18 = this.loadAnimation(var1, var3, false, var4);
                     } else {
                        var18 = null;
                     }

                     var1.mPostponedAlpha = 0.0F;
                     if (var18 != null) {
                        this.animateRemoveFragment(var1, var18, var2);
                     }

                     var1.mContainer.removeView(var1.mView);
                  }

                  var1.mContainer = null;
                  var1.mView = null;
                  var1.mInnerView = null;
                  var1.mInLayout = false;
               }
            case 1:
               var9 = var2;
               if (var2 < 1) {
                  if (this.mDestroyed) {
                     if (var1.getAnimatingAway() != null) {
                        View var19 = var1.getAnimatingAway();
                        var1.setAnimatingAway((View)null);
                        var19.clearAnimation();
                     } else if (var1.getAnimator() != null) {
                        Animator var20 = var1.getAnimator();
                        var1.setAnimator((Animator)null);
                        var20.cancel();
                     }
                  }

                  if (var1.getAnimatingAway() == null && var1.getAnimator() == null) {
                     if (DEBUG) {
                        var10 = new StringBuilder();
                        var10.append("movefrom CREATED: ");
                        var10.append(var1);
                        Log.v("FragmentManager", var10.toString());
                     }

                     if (!var1.mRetaining) {
                        var1.performDestroy();
                        this.dispatchOnFragmentDestroyed(var1, false);
                     } else {
                        var1.mState = 0;
                     }

                     var1.performDetach();
                     this.dispatchOnFragmentDetached(var1, false);
                     var9 = var2;
                     if (!var5) {
                        if (!var1.mRetaining) {
                           this.makeInactive(var1);
                           var9 = var2;
                        } else {
                           var1.mHost = null;
                           var1.mParentFragment = null;
                           var1.mFragmentManager = null;
                           var9 = var2;
                        }
                     }
                  } else {
                     var1.setStateAfterAnimating(var2);
                     var9 = var7;
                  }
               }
               break;
            default:
               var9 = var2;
            }
         }
      }

      if (var1.mState != var9) {
         var10 = new StringBuilder();
         var10.append("moveToState: Fragment state for ");
         var10.append(var1);
         var10.append(" not updated inline; ");
         var10.append("expected state ");
         var10.append(var9);
         var10.append(" found ");
         var10.append(var1.mState);
         Log.w("FragmentManager", var10.toString());
         var1.mState = var9;
      }

   }

   public void noteStateNotSaved() {
      this.mSavedNonConfig = null;
      int var1 = 0;
      this.mStateSaved = false;

      for(int var2 = this.mAdded.size(); var1 < var2; ++var1) {
         Fragment var3 = (Fragment)this.mAdded.get(var1);
         if (var3 != null) {
            var3.noteStateNotSaved();
         }
      }

   }

   public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      if (!"fragment".equals(var2)) {
         return null;
      } else {
         var2 = var4.getAttributeValue((String)null, "class");
         TypedArray var5 = var3.obtainStyledAttributes(var4, FragmentManagerImpl.FragmentTag.Fragment);
         int var6 = 0;
         String var7 = var2;
         if (var2 == null) {
            var7 = var5.getString(0);
         }

         int var8 = var5.getResourceId(1, -1);
         String var9 = var5.getString(2);
         var5.recycle();
         if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), var7)) {
            return null;
         } else {
            if (var1 != null) {
               var6 = var1.getId();
            }

            StringBuilder var13;
            if (var6 == -1 && var8 == -1 && var9 == null) {
               var13 = new StringBuilder();
               var13.append(var4.getPositionDescription());
               var13.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
               var13.append(var7);
               throw new IllegalArgumentException(var13.toString());
            } else {
               Fragment var12;
               if (var8 != -1) {
                  var12 = this.findFragmentById(var8);
               } else {
                  var12 = null;
               }

               Fragment var11 = var12;
               if (var12 == null) {
                  var11 = var12;
                  if (var9 != null) {
                     var11 = this.findFragmentByTag(var9);
                  }
               }

               var12 = var11;
               if (var11 == null) {
                  var12 = var11;
                  if (var6 != -1) {
                     var12 = this.findFragmentById(var6);
                  }
               }

               if (DEBUG) {
                  var13 = new StringBuilder();
                  var13.append("onCreateView: id=0x");
                  var13.append(Integer.toHexString(var8));
                  var13.append(" fname=");
                  var13.append(var7);
                  var13.append(" existing=");
                  var13.append(var12);
                  Log.v("FragmentManager", var13.toString());
               }

               if (var12 == null) {
                  var11 = this.mContainer.instantiate(var3, var7, (Bundle)null);
                  var11.mFromLayout = true;
                  int var10;
                  if (var8 != 0) {
                     var10 = var8;
                  } else {
                     var10 = var6;
                  }

                  var11.mFragmentId = var10;
                  var11.mContainerId = var6;
                  var11.mTag = var9;
                  var11.mInLayout = true;
                  var11.mFragmentManager = this;
                  var11.mHost = this.mHost;
                  var11.onInflate(this.mHost.getContext(), var4, var11.mSavedFragmentState);
                  this.addFragment(var11, true);
               } else {
                  if (var12.mInLayout) {
                     var13 = new StringBuilder();
                     var13.append(var4.getPositionDescription());
                     var13.append(": Duplicate id 0x");
                     var13.append(Integer.toHexString(var8));
                     var13.append(", tag ");
                     var13.append(var9);
                     var13.append(", or parent id 0x");
                     var13.append(Integer.toHexString(var6));
                     var13.append(" with another fragment for ");
                     var13.append(var7);
                     throw new IllegalArgumentException(var13.toString());
                  }

                  var12.mInLayout = true;
                  var12.mHost = this.mHost;
                  if (!var12.mRetaining) {
                     var12.onInflate(this.mHost.getContext(), var4, var12.mSavedFragmentState);
                  }

                  var11 = var12;
               }

               if (this.mCurState < 1 && var11.mFromLayout) {
                  this.moveToState(var11, 1, 0, 0, false);
               } else {
                  this.moveToState(var11);
               }

               if (var11.mView == null) {
                  var13 = new StringBuilder();
                  var13.append("Fragment ");
                  var13.append(var7);
                  var13.append(" did not create a view.");
                  throw new IllegalStateException(var13.toString());
               } else {
                  if (var8 != 0) {
                     var11.mView.setId(var8);
                  }

                  if (var11.mView.getTag() == null) {
                     var11.mView.setTag(var9);
                  }

                  return var11.mView;
               }
            }
         }
      }
   }

   public View onCreateView(String var1, Context var2, AttributeSet var3) {
      return this.onCreateView((View)null, var1, var2, var3);
   }

   public void performPendingDeferredStart(Fragment var1) {
      if (var1.mDeferStart) {
         if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
         }

         var1.mDeferStart = false;
         this.moveToState(var1, this.mCurState, 0, 0, false);
      }

   }

   public void popBackStack() {
      this.enqueueAction(new FragmentManagerImpl.PopBackStackState((String)null, -1, 0), false);
   }

   public void popBackStack(int var1, int var2) {
      if (var1 < 0) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Bad id: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      } else {
         this.enqueueAction(new FragmentManagerImpl.PopBackStackState((String)null, var1, var2), false);
      }
   }

   public void popBackStack(String var1, int var2) {
      this.enqueueAction(new FragmentManagerImpl.PopBackStackState(var1, -1, var2), false);
   }

   public boolean popBackStackImmediate() {
      this.checkStateLoss();
      return this.popBackStackImmediate((String)null, -1, 0);
   }

   public boolean popBackStackImmediate(int var1, int var2) {
      this.checkStateLoss();
      this.execPendingActions();
      if (var1 < 0) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Bad id: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      } else {
         return this.popBackStackImmediate((String)null, var1, var2);
      }
   }

   public boolean popBackStackImmediate(String var1, int var2) {
      this.checkStateLoss();
      return this.popBackStackImmediate(var1, -1, var2);
   }

   boolean popBackStackState(ArrayList var1, ArrayList var2, String var3, int var4, int var5) {
      if (this.mBackStack == null) {
         return false;
      } else {
         if (var3 == null && var4 < 0 && (var5 & 1) == 0) {
            var4 = this.mBackStack.size() - 1;
            if (var4 < 0) {
               return false;
            }

            var1.add(this.mBackStack.remove(var4));
            var2.add(true);
         } else {
            int var6;
            if (var3 == null && var4 < 0) {
               var6 = -1;
            } else {
               int var7;
               BackStackRecord var8;
               for(var7 = this.mBackStack.size() - 1; var7 >= 0; --var7) {
                  var8 = (BackStackRecord)this.mBackStack.get(var7);
                  if (var3 != null && var3.equals(var8.getName()) || var4 >= 0 && var4 == var8.mIndex) {
                     break;
                  }
               }

               if (var7 < 0) {
                  return false;
               }

               var6 = var7;
               if ((var5 & 1) != 0) {
                  var5 = var7 - 1;

                  while(true) {
                     var6 = var5;
                     if (var5 < 0) {
                        break;
                     }

                     var8 = (BackStackRecord)this.mBackStack.get(var5);
                     if (var3 == null || !var3.equals(var8.getName())) {
                        var6 = var5;
                        if (var4 < 0) {
                           break;
                        }

                        var6 = var5;
                        if (var4 != var8.mIndex) {
                           break;
                        }
                     }

                     --var5;
                  }
               }
            }

            if (var6 == this.mBackStack.size() - 1) {
               return false;
            }

            for(var4 = this.mBackStack.size() - 1; var4 > var6; --var4) {
               var1.add(this.mBackStack.remove(var4));
               var2.add(true);
            }
         }

         return true;
      }
   }

   public void putFragment(Bundle var1, String var2, Fragment var3) {
      if (var3.mIndex < 0) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Fragment ");
         var4.append(var3);
         var4.append(" is not currently in the FragmentManager");
         this.throwException(new IllegalStateException(var4.toString()));
      }

      var1.putInt(var2, var3.mIndex);
   }

   public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks var1, boolean var2) {
      this.mLifecycleCallbacks.add(new Pair(var1, var2));
   }

   public void removeFragment(Fragment param1) {
      // $FF: Couldn't be decompiled
   }

   public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1) {
      if (this.mBackStackChangeListeners != null) {
         this.mBackStackChangeListeners.remove(var1);
      }

   }

   void reportBackStackChanged() {
      if (this.mBackStackChangeListeners != null) {
         for(int var1 = 0; var1 < this.mBackStackChangeListeners.size(); ++var1) {
            ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(var1)).onBackStackChanged();
         }
      }

   }

   void restoreAllState(Parcelable param1, FragmentManagerNonConfig param2) {
      // $FF: Couldn't be decompiled
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
      BackStackState[] var1 = null;
      this.mSavedNonConfig = null;
      if (this.mActive != null && this.mActive.size() > 0) {
         int var2 = this.mActive.size();
         FragmentState[] var3 = new FragmentState[var2];
         byte var4 = 0;
         int var5 = 0;

         int var6;
         StringBuilder var8;
         for(var6 = var5; var5 < var2; ++var5) {
            Fragment var7 = (Fragment)this.mActive.valueAt(var5);
            if (var7 != null) {
               if (var7.mIndex < 0) {
                  var8 = new StringBuilder();
                  var8.append("Failure saving state: active ");
                  var8.append(var7);
                  var8.append(" has cleared index: ");
                  var8.append(var7.mIndex);
                  this.throwException(new IllegalStateException(var8.toString()));
               }

               FragmentState var13 = new FragmentState(var7);
               var3[var5] = var13;
               StringBuilder var9;
               if (var7.mState > 0 && var13.mSavedFragmentState == null) {
                  var13.mSavedFragmentState = this.saveFragmentBasicState(var7);
                  if (var7.mTarget != null) {
                     if (var7.mTarget.mIndex < 0) {
                        var9 = new StringBuilder();
                        var9.append("Failure saving state: ");
                        var9.append(var7);
                        var9.append(" has target not in fragment manager: ");
                        var9.append(var7.mTarget);
                        this.throwException(new IllegalStateException(var9.toString()));
                     }

                     if (var13.mSavedFragmentState == null) {
                        var13.mSavedFragmentState = new Bundle();
                     }

                     this.putFragment(var13.mSavedFragmentState, "android:target_state", var7.mTarget);
                     if (var7.mTargetRequestCode != 0) {
                        var13.mSavedFragmentState.putInt("android:target_req_state", var7.mTargetRequestCode);
                     }
                  }
               } else {
                  var13.mSavedFragmentState = var7.mSavedFragmentState;
               }

               if (DEBUG) {
                  var9 = new StringBuilder();
                  var9.append("Saved state of ");
                  var9.append(var7);
                  var9.append(": ");
                  var9.append(var13.mSavedFragmentState);
                  Log.v("FragmentManager", var9.toString());
               }

               var6 = 1;
            }
         }

         if (var6 == 0) {
            if (DEBUG) {
               Log.v("FragmentManager", "saveAllState: no fragments!");
            }

            return null;
         } else {
            var6 = this.mAdded.size();
            int[] var11;
            if (var6 > 0) {
               int[] var14 = new int[var6];
               var5 = 0;

               while(true) {
                  var11 = var14;
                  if (var5 >= var6) {
                     break;
                  }

                  var14[var5] = ((Fragment)this.mAdded.get(var5)).mIndex;
                  StringBuilder var12;
                  if (var14[var5] < 0) {
                     var12 = new StringBuilder();
                     var12.append("Failure saving state: active ");
                     var12.append(this.mAdded.get(var5));
                     var12.append(" has cleared index: ");
                     var12.append(var14[var5]);
                     this.throwException(new IllegalStateException(var12.toString()));
                  }

                  if (DEBUG) {
                     var12 = new StringBuilder();
                     var12.append("saveAllState: adding fragment #");
                     var12.append(var5);
                     var12.append(": ");
                     var12.append(this.mAdded.get(var5));
                     Log.v("FragmentManager", var12.toString());
                  }

                  ++var5;
               }
            } else {
               var11 = null;
            }

            BackStackState[] var15 = var1;
            if (this.mBackStack != null) {
               var6 = this.mBackStack.size();
               var15 = var1;
               if (var6 > 0) {
                  var1 = new BackStackState[var6];
                  var5 = var4;

                  while(true) {
                     var15 = var1;
                     if (var5 >= var6) {
                        break;
                     }

                     var1[var5] = new BackStackState((BackStackRecord)this.mBackStack.get(var5));
                     if (DEBUG) {
                        var8 = new StringBuilder();
                        var8.append("saveAllState: adding back stack #");
                        var8.append(var5);
                        var8.append(": ");
                        var8.append(this.mBackStack.get(var5));
                        Log.v("FragmentManager", var8.toString());
                     }

                     ++var5;
                  }
               }
            }

            FragmentManagerState var10 = new FragmentManagerState();
            var10.mActive = var3;
            var10.mAdded = var11;
            var10.mBackStack = var15;
            if (this.mPrimaryNav != null) {
               var10.mPrimaryNavActiveIndex = this.mPrimaryNav.mIndex;
            }

            var10.mNextFragmentIndex = this.mNextFragmentIndex;
            this.saveNonConfig();
            return var10;
         }
      } else {
         return null;
      }
   }

   Bundle saveFragmentBasicState(Fragment var1) {
      if (this.mStateBundle == null) {
         this.mStateBundle = new Bundle();
      }

      var1.performSaveInstanceState(this.mStateBundle);
      this.dispatchOnFragmentSaveInstanceState(var1, this.mStateBundle, false);
      Bundle var2;
      if (!this.mStateBundle.isEmpty()) {
         var2 = this.mStateBundle;
         this.mStateBundle = null;
      } else {
         var2 = null;
      }

      if (var1.mView != null) {
         this.saveFragmentViewState(var1);
      }

      Bundle var3 = var2;
      if (var1.mSavedViewState != null) {
         var3 = var2;
         if (var2 == null) {
            var3 = new Bundle();
         }

         var3.putSparseParcelableArray("android:view_state", var1.mSavedViewState);
      }

      var2 = var3;
      if (!var1.mUserVisibleHint) {
         var2 = var3;
         if (var3 == null) {
            var2 = new Bundle();
         }

         var2.putBoolean("android:user_visible_hint", var1.mUserVisibleHint);
      }

      return var2;
   }

   public Fragment.SavedState saveFragmentInstanceState(Fragment var1) {
      StringBuilder var2;
      if (var1.mIndex < 0) {
         var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(var1);
         var2.append(" is not currently in the FragmentManager");
         this.throwException(new IllegalStateException(var2.toString()));
      }

      int var3 = var1.mState;
      var2 = null;
      if (var3 > 0) {
         Bundle var4 = this.saveFragmentBasicState(var1);
         Fragment.SavedState var5 = var2;
         if (var4 != null) {
            var5 = new Fragment.SavedState(var4);
         }

         return var5;
      } else {
         return null;
      }
   }

   void saveFragmentViewState(Fragment var1) {
      if (var1.mInnerView != null) {
         if (this.mStateArray == null) {
            this.mStateArray = new SparseArray();
         } else {
            this.mStateArray.clear();
         }

         var1.mInnerView.saveHierarchyState(this.mStateArray);
         if (this.mStateArray.size() > 0) {
            var1.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
         }

      }
   }

   void saveNonConfig() {
      ArrayList var4;
      ArrayList var5;
      if (this.mActive != null) {
         int var1 = 0;
         ArrayList var2 = null;
         ArrayList var3 = var2;

         while(true) {
            var4 = var2;
            var5 = var3;
            if (var1 >= this.mActive.size()) {
               break;
            }

            Fragment var6 = (Fragment)this.mActive.valueAt(var1);
            var4 = var2;
            ArrayList var7 = var3;
            if (var6 != null) {
               var5 = var2;
               int var8;
               if (var6.mRetainInstance) {
                  var4 = var2;
                  if (var2 == null) {
                     var4 = new ArrayList();
                  }

                  var4.add(var6);
                  if (var6.mTarget != null) {
                     var8 = var6.mTarget.mIndex;
                  } else {
                     var8 = -1;
                  }

                  var6.mTargetIndex = var8;
                  var5 = var4;
                  if (DEBUG) {
                     StringBuilder var9 = new StringBuilder();
                     var9.append("retainNonConfig: keeping retained ");
                     var9.append(var6);
                     Log.v("FragmentManager", var9.toString());
                     var5 = var4;
                  }
               }

               FragmentManagerNonConfig var10;
               if (var6.mChildFragmentManager != null) {
                  var6.mChildFragmentManager.saveNonConfig();
                  var10 = var6.mChildFragmentManager.mSavedNonConfig;
               } else {
                  var10 = var6.mChildNonConfig;
               }

               var2 = var3;
               if (var3 == null) {
                  var2 = var3;
                  if (var10 != null) {
                     var3 = new ArrayList(this.mActive.size());
                     var8 = 0;

                     while(true) {
                        var2 = var3;
                        if (var8 >= var1) {
                           break;
                        }

                        var3.add((Object)null);
                        ++var8;
                     }
                  }
               }

               var4 = var5;
               var7 = var2;
               if (var2 != null) {
                  var2.add(var10);
                  var7 = var2;
                  var4 = var5;
               }
            }

            ++var1;
            var2 = var4;
            var3 = var7;
         }
      } else {
         var4 = null;
         var5 = var4;
      }

      if (var4 == null && var5 == null) {
         this.mSavedNonConfig = null;
      } else {
         this.mSavedNonConfig = new FragmentManagerNonConfig(var4, var5);
      }

   }

   public void setBackStackIndex(int var1, BackStackRecord var2) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label1131: {
         ArrayList var3;
         try {
            if (this.mBackStackIndices == null) {
               var3 = new ArrayList();
               this.mBackStackIndices = var3;
            }
         } catch (Throwable var137) {
            var10000 = var137;
            var10001 = false;
            break label1131;
         }

         int var4;
         try {
            var4 = this.mBackStackIndices.size();
         } catch (Throwable var136) {
            var10000 = var136;
            var10001 = false;
            break label1131;
         }

         int var5 = var4;
         StringBuilder var139;
         if (var1 < var4) {
            try {
               if (DEBUG) {
                  var139 = new StringBuilder();
                  var139.append("Setting back stack index ");
                  var139.append(var1);
                  var139.append(" to ");
                  var139.append(var2);
                  Log.v("FragmentManager", var139.toString());
               }
            } catch (Throwable var133) {
               var10000 = var133;
               var10001 = false;
               break label1131;
            }

            try {
               this.mBackStackIndices.set(var1, var2);
            } catch (Throwable var132) {
               var10000 = var132;
               var10001 = false;
               break label1131;
            }
         } else {
            while(true) {
               if (var5 >= var1) {
                  try {
                     if (DEBUG) {
                        var139 = new StringBuilder();
                        var139.append("Adding back stack index ");
                        var139.append(var1);
                        var139.append(" with ");
                        var139.append(var2);
                        Log.v("FragmentManager", var139.toString());
                     }
                  } catch (Throwable var134) {
                     var10000 = var134;
                     var10001 = false;
                     break label1131;
                  }

                  try {
                     this.mBackStackIndices.add(var2);
                     break;
                  } catch (Throwable var129) {
                     var10000 = var129;
                     var10001 = false;
                     break label1131;
                  }
               }

               try {
                  this.mBackStackIndices.add((Object)null);
                  if (this.mAvailBackStackIndices == null) {
                     var3 = new ArrayList();
                     this.mAvailBackStackIndices = var3;
                  }
               } catch (Throwable var131) {
                  var10000 = var131;
                  var10001 = false;
                  break label1131;
               }

               try {
                  if (DEBUG) {
                     var139 = new StringBuilder();
                     var139.append("Adding available back stack index ");
                     var139.append(var5);
                     Log.v("FragmentManager", var139.toString());
                  }
               } catch (Throwable var135) {
                  var10000 = var135;
                  var10001 = false;
                  break label1131;
               }

               try {
                  this.mAvailBackStackIndices.add(var5);
               } catch (Throwable var130) {
                  var10000 = var130;
                  var10001 = false;
                  break label1131;
               }

               ++var5;
            }
         }

         label1085:
         try {
            return;
         } catch (Throwable var128) {
            var10000 = var128;
            var10001 = false;
            break label1085;
         }
      }

      while(true) {
         Throwable var138 = var10000;

         try {
            throw var138;
         } catch (Throwable var127) {
            var10000 = var127;
            var10001 = false;
            continue;
         }
      }
   }

   public void setPrimaryNavigationFragment(Fragment var1) {
      if (var1 == null || this.mActive.get(var1.mIndex) == var1 && (var1.mHost == null || var1.getFragmentManager() == this)) {
         this.mPrimaryNav = var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(var1);
         var2.append(" is not an active fragment of FragmentManager ");
         var2.append(this);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public void showFragment(Fragment var1) {
      if (DEBUG) {
         StringBuilder var2 = new StringBuilder();
         var2.append("show: ");
         var2.append(var1);
         Log.v("FragmentManager", var2.toString());
      }

      if (var1.mHidden) {
         var1.mHidden = false;
         var1.mHiddenChanged ^= true;
      }

   }

   void startPendingDeferredFragments() {
      if (this.mActive != null) {
         for(int var1 = 0; var1 < this.mActive.size(); ++var1) {
            Fragment var2 = (Fragment)this.mActive.valueAt(var1);
            if (var2 != null) {
               this.performPendingDeferredStart(var2);
            }
         }

      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(128);
      var1.append("FragmentManager{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(" in ");
      if (this.mParent != null) {
         DebugUtils.buildShortClassTag(this.mParent, var1);
      } else {
         DebugUtils.buildShortClassTag(this.mHost, var1);
      }

      var1.append("}}");
      return var1.toString();
   }

   public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks var1) {
      CopyOnWriteArrayList var2 = this.mLifecycleCallbacks;
      synchronized(var2){}
      int var3 = 0;

      Throwable var10000;
      boolean var10001;
      label245: {
         int var4;
         try {
            var4 = this.mLifecycleCallbacks.size();
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label245;
         }

         for(; var3 < var4; ++var3) {
            try {
               if (((Pair)this.mLifecycleCallbacks.get(var3)).first == var1) {
                  this.mLifecycleCallbacks.remove(var3);
                  break;
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label245;
            }
         }

         label224:
         try {
            return;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label224;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   private static class AnimateOnHWLayerIfNeededListener extends FragmentManagerImpl.AnimationListenerWrapper {
      View mView;

      AnimateOnHWLayerIfNeededListener(View var1, AnimationListener var2) {
         super(var2, null);
         this.mView = var1;
      }

      @CallSuper
      public void onAnimationEnd(Animation var1) {
         if (!ViewCompat.isAttachedToWindow(this.mView) && VERSION.SDK_INT < 24) {
            this.mView.setLayerType(0, (Paint)null);
         } else {
            this.mView.post(new Runnable() {
               public void run() {
                  AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, (Paint)null);
               }
            });
         }

         super.onAnimationEnd(var1);
      }
   }

   private static class AnimationListenerWrapper implements AnimationListener {
      private final AnimationListener mWrapped;

      private AnimationListenerWrapper(AnimationListener var1) {
         this.mWrapped = var1;
      }

      // $FF: synthetic method
      AnimationListenerWrapper(AnimationListener var1, Object var2) {
         this(var1);
      }

      @CallSuper
      public void onAnimationEnd(Animation var1) {
         if (this.mWrapped != null) {
            this.mWrapped.onAnimationEnd(var1);
         }

      }

      @CallSuper
      public void onAnimationRepeat(Animation var1) {
         if (this.mWrapped != null) {
            this.mWrapped.onAnimationRepeat(var1);
         }

      }

      @CallSuper
      public void onAnimationStart(Animation var1) {
         if (this.mWrapped != null) {
            this.mWrapped.onAnimationStart(var1);
         }

      }
   }

   private static class AnimationOrAnimator {
      public final Animation animation;
      public final Animator animator;

      private AnimationOrAnimator(Animator var1) {
         this.animation = null;
         this.animator = var1;
         if (var1 == null) {
            throw new IllegalStateException("Animator cannot be null");
         }
      }

      // $FF: synthetic method
      AnimationOrAnimator(Animator var1, Object var2) {
         this(var1);
      }

      private AnimationOrAnimator(Animation var1) {
         this.animation = var1;
         this.animator = null;
         if (var1 == null) {
            throw new IllegalStateException("Animation cannot be null");
         }
      }

      // $FF: synthetic method
      AnimationOrAnimator(Animation var1, Object var2) {
         this(var1);
      }
   }

   private static class AnimatorOnHWLayerIfNeededListener extends AnimatorListenerAdapter {
      View mView;

      AnimatorOnHWLayerIfNeededListener(View var1) {
         this.mView = var1;
      }

      public void onAnimationEnd(Animator var1) {
         this.mView.setLayerType(0, (Paint)null);
         var1.removeListener(this);
      }

      public void onAnimationStart(Animator var1) {
         this.mView.setLayerType(2, (Paint)null);
      }
   }

   static class FragmentTag {
      public static final int[] Fragment = new int[]{16842755, 16842960, 16842961};
      public static final int Fragment_id = 1;
      public static final int Fragment_name = 0;
      public static final int Fragment_tag = 2;
   }

   interface OpGenerator {
      boolean generateOps(ArrayList var1, ArrayList var2);
   }

   private class PopBackStackState implements FragmentManagerImpl.OpGenerator {
      final int mFlags;
      final int mId;
      final String mName;

      PopBackStackState(String var2, int var3, int var4) {
         this.mName = var2;
         this.mId = var3;
         this.mFlags = var4;
      }

      public boolean generateOps(ArrayList var1, ArrayList var2) {
         if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
            FragmentManager var3 = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager();
            if (var3 != null && var3.popBackStackImmediate()) {
               return false;
            }
         }

         return FragmentManagerImpl.this.popBackStackState(var1, var2, this.mName, this.mId, this.mFlags);
      }
   }

   static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
      private final boolean mIsBack;
      private int mNumPostponed;
      private final BackStackRecord mRecord;

      StartEnterTransitionListener(BackStackRecord var1, boolean var2) {
         this.mIsBack = var2;
         this.mRecord = var1;
      }

      public void cancelTransaction() {
         this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
      }

      public void completeTransaction() {
         int var1 = this.mNumPostponed;
         int var2 = 0;
         boolean var6;
         if (var1 > 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         FragmentManagerImpl var3 = this.mRecord.mManager;

         for(int var4 = var3.mAdded.size(); var2 < var4; ++var2) {
            Fragment var5 = (Fragment)var3.mAdded.get(var2);
            var5.setOnStartEnterTransitionListener((Fragment.OnStartEnterTransitionListener)null);
            if (var6 && var5.isPostponed()) {
               var5.startPostponedEnterTransition();
            }
         }

         this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, var6 ^ true, true);
      }

      public boolean isReady() {
         boolean var1;
         if (this.mNumPostponed == 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void onStartEnterTransition() {
         --this.mNumPostponed;
         if (this.mNumPostponed == 0) {
            this.mRecord.mManager.scheduleCommit();
         }
      }

      public void startListening() {
         ++this.mNumPostponed;
      }
   }
}
