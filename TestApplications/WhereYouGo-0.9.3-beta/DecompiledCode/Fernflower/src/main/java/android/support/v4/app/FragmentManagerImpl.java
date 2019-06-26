package android.support.v4.app;

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
import android.support.v4.os.BuildCompat;
import android.support.v4.util.ArraySet;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.util.Pair;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

final class FragmentManagerImpl extends FragmentManager implements LayoutInflaterFactory {
   static final Interpolator ACCELERATE_CUBIC;
   static final Interpolator ACCELERATE_QUINT;
   static final int ANIM_DUR = 220;
   public static final int ANIM_STYLE_CLOSE_ENTER = 3;
   public static final int ANIM_STYLE_CLOSE_EXIT = 4;
   public static final int ANIM_STYLE_FADE_ENTER = 5;
   public static final int ANIM_STYLE_FADE_EXIT = 6;
   public static final int ANIM_STYLE_OPEN_ENTER = 1;
   public static final int ANIM_STYLE_OPEN_EXIT = 2;
   static boolean DEBUG;
   static final Interpolator DECELERATE_CUBIC;
   static final Interpolator DECELERATE_QUINT;
   static final boolean HONEYCOMB;
   static final String TAG = "FragmentManager";
   static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
   static final String TARGET_STATE_TAG = "android:target_state";
   static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
   static final String VIEW_STATE_TAG = "android:view_state";
   static Field sAnimationListenerField;
   ArrayList mActive;
   ArrayList mAdded;
   ArrayList mAvailBackStackIndices;
   ArrayList mAvailIndices;
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
   private CopyOnWriteArrayList mLifecycleCallbacks;
   boolean mNeedMenuInvalidate;
   String mNoTransactionsBecause;
   Fragment mParent;
   ArrayList mPendingActions;
   ArrayList mPostponedTransactions;
   SparseArray mStateArray = null;
   Bundle mStateBundle = null;
   boolean mStateSaved;
   Runnable[] mTmpActions;
   ArrayList mTmpAddedFragments;
   ArrayList mTmpIsPop;
   ArrayList mTmpRecords;

   static {
      boolean var0 = false;
      DEBUG = false;
      if (VERSION.SDK_INT >= 11) {
         var0 = true;
      }

      HONEYCOMB = var0;
      sAnimationListenerField = null;
      DECELERATE_QUINT = new DecelerateInterpolator(2.5F);
      DECELERATE_CUBIC = new DecelerateInterpolator(1.5F);
      ACCELERATE_QUINT = new AccelerateInterpolator(2.5F);
      ACCELERATE_CUBIC = new AccelerateInterpolator(1.5F);
   }

   private void addAddedFragments(ArraySet var1) {
      if (this.mCurState >= 1) {
         int var2 = Math.min(this.mCurState, 4);
         int var3;
         if (this.mAdded == null) {
            var3 = 0;
         } else {
            var3 = this.mAdded.size();
         }

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

   private void checkStateLoss() {
      if (this.mStateSaved) {
         throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
      } else if (this.mNoTransactionsBecause != null) {
         throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
      }
   }

   private void cleanupExec() {
      this.mExecutingActions = false;
      this.mTmpIsPop.clear();
      this.mTmpRecords.clear();
   }

   private void completeExecute(BackStackRecord var1, boolean var2, boolean var3, boolean var4) {
      ArrayList var5 = new ArrayList(1);
      ArrayList var6 = new ArrayList(1);
      var5.add(var1);
      var6.add(var2);
      executeOps(var5, var6, 0, 1);
      if (var3) {
         FragmentTransition.startTransitions(this, var5, var6, 0, 1, true);
      }

      if (var4) {
         this.moveToState(this.mCurState, true);
      }

      if (this.mActive != null) {
         int var7 = this.mActive.size();

         for(int var8 = 0; var8 < var7; ++var8) {
            Fragment var9 = (Fragment)this.mActive.get(var8);
            if (var9 != null && var9.mView != null && var9.mIsNewlyAdded && var1.interactsWith(var9.mContainerId)) {
               if (VERSION.SDK_INT >= 11 && var9.mPostponedAlpha > 0.0F) {
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

   private void endAnimatingAwayFragments() {
      int var1;
      if (this.mActive == null) {
         var1 = 0;
      } else {
         var1 = this.mActive.size();
      }

      for(int var2 = 0; var2 < var1; ++var2) {
         Fragment var3 = (Fragment)this.mActive.get(var2);
         if (var3 != null && var3.getAnimatingAway() != null) {
            int var4 = var3.getStateAfterAnimating();
            View var5 = var3.getAnimatingAway();
            var3.setAnimatingAway((View)null);
            Animation var6 = var5.getAnimation();
            if (var6 != null) {
               var6.cancel();
            }

            this.moveToState(var3, var4, 0, 0, false);
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
         if ((Boolean)var1.get(var2)) {
            var4.bumpBackStackNesting(-1);
            boolean var5;
            if (var2 == var3 - 1) {
               var5 = true;
            } else {
               var5 = false;
            }

            var4.executePopOps(var5);
         } else {
            var4.bumpBackStackNesting(1);
            var4.executeOps();
         }
      }

   }

   private void executeOpsTogether(ArrayList var1, ArrayList var2, int var3, int var4) {
      boolean var5 = ((BackStackRecord)var1.get(var3)).mAllowOptimization;
      boolean var6 = false;
      if (this.mTmpAddedFragments == null) {
         this.mTmpAddedFragments = new ArrayList();
      } else {
         this.mTmpAddedFragments.clear();
      }

      if (this.mAdded != null) {
         this.mTmpAddedFragments.addAll(this.mAdded);
      }

      int var7;
      BackStackRecord var8;
      for(var7 = var3; var7 < var4; ++var7) {
         var8 = (BackStackRecord)var1.get(var7);
         if (!(Boolean)var2.get(var7)) {
            var8.expandReplaceOps(this.mTmpAddedFragments);
         } else {
            var8.trackAddedFragmentsInPop(this.mTmpAddedFragments);
         }

         if (!var6 && !var8.mAddToBackStack) {
            var6 = false;
         } else {
            var6 = true;
         }
      }

      this.mTmpAddedFragments.clear();
      if (!var5) {
         FragmentTransition.startTransitions(this, var1, var2, var3, var4, false);
      }

      executeOps(var1, var2, var3, var4);
      var7 = var4;
      if (var5) {
         ArraySet var9 = new ArraySet();
         this.addAddedFragments(var9);
         var7 = this.postponePostponableTransactions(var1, var2, var3, var4, var9);
         this.makeRemovedFragmentsInvisible(var9);
      }

      if (var7 != var3 && var5) {
         FragmentTransition.startTransitions(this, var1, var2, var3, var7, true);
         this.moveToState(this.mCurState, true);
      }

      for(; var3 < var4; ++var3) {
         var8 = (BackStackRecord)var1.get(var3);
         if ((Boolean)var2.get(var3) && var8.mIndex >= 0) {
            this.freeBackStackIndex(var8.mIndex);
            var8.mIndex = -1;
         }
      }

      if (var6) {
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
                  var8 = var5;
                  var7 = var3;
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
         int var4 = this.mAdded.indexOf(var1) - 1;

         while(true) {
            if (var4 < 0) {
               var1 = null;
               break;
            }

            Fragment var5 = (Fragment)this.mAdded.get(var4);
            if (var5.mContainer == var2) {
               var1 = var5;
               if (var5.mView != null) {
                  break;
               }
            }

            --var4;
         }
      } else {
         var1 = null;
      }

      return var1;
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
      label499: {
         boolean var3;
         label500: {
            try {
               if (this.mPendingActions != null && this.mPendingActions.size() != 0) {
                  break label500;
               }
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label499;
            }

            try {
               ;
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label499;
            }

            var3 = false;
            return var3;
         }

         int var4;
         try {
            var4 = this.mPendingActions.size();
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label499;
         }

         for(int var5 = 0; var5 < var4; ++var5) {
            try {
               ((FragmentManagerImpl.OpGenerator)this.mPendingActions.get(var5)).generateOps(var1, var2);
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label499;
            }
         }

         try {
            this.mPendingActions.clear();
            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label499;
         }

         if (var4 > 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      while(true) {
         Throwable var48 = var10000;

         try {
            throw var48;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            continue;
         }
      }
   }

   static Animation makeFadeAnimation(Context var0, float var1, float var2) {
      AlphaAnimation var3 = new AlphaAnimation(var1, var2);
      var3.setInterpolator(DECELERATE_CUBIC);
      var3.setDuration(220L);
      return var3;
   }

   static Animation makeOpenCloseAnimation(Context var0, float var1, float var2, float var3, float var4) {
      AnimationSet var7 = new AnimationSet(false);
      ScaleAnimation var5 = new ScaleAnimation(var1, var2, var1, var2, 1, 0.5F, 1, 0.5F);
      var5.setInterpolator(DECELERATE_QUINT);
      var5.setDuration(220L);
      var7.addAnimation(var5);
      AlphaAnimation var6 = new AlphaAnimation(var3, var4);
      var6.setInterpolator(DECELERATE_CUBIC);
      var6.setDuration(220L);
      var7.addAnimation(var6);
      return var7;
   }

   private void makeRemovedFragmentsInvisible(ArraySet var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         Fragment var4 = (Fragment)var1.valueAt(var3);
         if (!var4.mAdded) {
            View var5 = var4.getView();
            if (VERSION.SDK_INT < 11) {
               var4.getView().setVisibility(4);
            } else {
               var4.mPostponedAlpha = var5.getAlpha();
               var5.setAlpha(0.0F);
            }
         }
      }

   }

   static boolean modifiesAlpha(Animation var0) {
      boolean var1 = true;
      boolean var2;
      if (var0 instanceof AlphaAnimation) {
         var2 = var1;
      } else {
         if (var0 instanceof AnimationSet) {
            List var4 = ((AnimationSet)var0).getAnimations();

            for(int var3 = 0; var3 < var4.size(); ++var3) {
               var2 = var1;
               if (var4.get(var3) instanceof AlphaAnimation) {
                  return var2;
               }
            }
         }

         var2 = false;
      }

      return var2;
   }

   private void optimizeAndExecuteOps(ArrayList var1, ArrayList var2) {
      if (var1 != null && !var1.isEmpty()) {
         if (var2 == null || var1.size() != var2.size()) {
            throw new IllegalStateException("Internal error with the back stack records");
         }

         this.executePostponedTransaction(var1, var2);
         int var3 = var1.size();
         int var4 = 0;

         int var7;
         for(int var5 = 0; var5 < var3; var4 = var7) {
            int var6 = var5;
            var7 = var4;
            if (!((BackStackRecord)var1.get(var5)).mAllowOptimization) {
               if (var4 != var5) {
                  this.executeOpsTogether(var1, var2, var4, var5);
               }

               var4 = var5 + 1;
               var7 = var4;
               if ((Boolean)var2.get(var5)) {
                  while(true) {
                     var7 = var4;
                     if (var4 >= var3) {
                        break;
                     }

                     var7 = var4;
                     if (!(Boolean)var2.get(var4)) {
                        break;
                     }

                     var7 = var4;
                     if (((BackStackRecord)var1.get(var4)).mAllowOptimization) {
                        break;
                     }

                     ++var4;
                  }
               }

               this.executeOpsTogether(var1, var2, var5, var7);
               var6 = var7 - 1;
               var7 = var7;
            }

            var5 = var6 + 1;
         }

         if (var4 != var3) {
            this.executeOpsTogether(var1, var2, var4, var3);
         }
      }

   }

   private boolean popBackStackImmediate(String var1, int var2, int var3) {
      this.execPendingActions();
      this.ensureExecReady(true);
      boolean var4 = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, var1, var2, var3);
      if (var4) {
         this.mExecutingActions = true;

         try {
            this.optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
         } finally {
            this.cleanupExec();
         }
      }

      this.doPendingDeferredStart();
      return var4;
   }

   private int postponePostponableTransactions(ArrayList var1, ArrayList var2, int var3, int var4, ArraySet var5) {
      int var6 = var4;

      int var11;
      for(int var7 = var4 - 1; var7 >= var3; var6 = var11) {
         BackStackRecord var8 = (BackStackRecord)var1.get(var7);
         boolean var9 = (Boolean)var2.get(var7);
         boolean var10;
         if (var8.isPostponed() && !var8.interactsWith(var1, var7 + 1, var4)) {
            var10 = true;
         } else {
            var10 = false;
         }

         var11 = var6;
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

            var11 = var6 - 1;
            if (var7 != var11) {
               var1.remove(var7);
               var1.add(var11, var8);
            }

            this.addAddedFragments(var5);
         }

         --var7;
      }

      return var6;
   }

   public static int reverseTransit(int var0) {
      byte var1 = 0;
      short var2;
      switch(var0) {
      case 4097:
         var2 = 8194;
         break;
      case 4099:
         var2 = 4099;
         break;
      case 8194:
         var2 = 4097;
         break;
      default:
         var2 = var1;
      }

      return var2;
   }

   private void scheduleCommit() {
      boolean var1 = true;
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label380: {
         boolean var2;
         label374: {
            label373: {
               try {
                  if (this.mPostponedTransactions != null && !this.mPostponedTransactions.isEmpty()) {
                     break label373;
                  }
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label380;
               }

               var2 = false;
               break label374;
            }

            var2 = true;
         }

         label363: {
            try {
               if (this.mPendingActions != null && this.mPendingActions.size() == 1) {
                  break label363;
               }
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label380;
            }

            var1 = false;
         }

         if (var2 || var1) {
            try {
               this.mHost.getHandler().removeCallbacks(this.mExecCommit);
               this.mHost.getHandler().post(this.mExecCommit);
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label380;
            }
         }

         label348:
         try {
            return;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label348;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            continue;
         }
      }
   }

   private void setHWLayerAnimListenerIfAlpha(View var1, Animation var2) {
      if (var1 != null && var2 != null && shouldRunOnHWLayer(var1, var2)) {
         AnimationListener var3 = null;

         label27: {
            AnimationListener var4;
            try {
               if (sAnimationListenerField == null) {
                  sAnimationListenerField = Animation.class.getDeclaredField("mListener");
                  sAnimationListenerField.setAccessible(true);
               }

               var4 = (AnimationListener)sAnimationListenerField.get(var2);
            } catch (NoSuchFieldException var5) {
               Log.e("FragmentManager", "No field with the name mListener is found in Animation class", var5);
               break label27;
            } catch (IllegalAccessException var6) {
               Log.e("FragmentManager", "Cannot access Animation's mListener field", var6);
               break label27;
            }

            var3 = var4;
         }

         ViewCompat.setLayerType(var1, 2, (Paint)null);
         var2.setAnimationListener(new FragmentManagerImpl.AnimateOnHWLayerIfNeededListener(var1, var2, var3));
      }

   }

   static boolean shouldRunOnHWLayer(View var0, Animation var1) {
      boolean var2;
      if (VERSION.SDK_INT >= 19 && ViewCompat.getLayerType(var0) == 0 && ViewCompat.hasOverlappingRendering(var0) && modifiesAlpha(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
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
      byte var2 = -1;
      byte var3;
      switch(var0) {
      case 4097:
         if (var1) {
            var3 = 1;
         } else {
            var3 = 2;
         }
         break;
      case 4099:
         if (var1) {
            var3 = 5;
         } else {
            var3 = 6;
         }
         break;
      case 8194:
         if (var1) {
            var3 = 3;
         } else {
            var3 = 4;
         }
         break;
      default:
         var3 = var2;
      }

      return var3;
   }

   void addBackStackState(BackStackRecord var1) {
      if (this.mBackStack == null) {
         this.mBackStack = new ArrayList();
      }

      this.mBackStack.add(var1);
      this.reportBackStackChanged();
   }

   public void addFragment(Fragment var1, boolean var2) {
      if (this.mAdded == null) {
         this.mAdded = new ArrayList();
      }

      if (DEBUG) {
         Log.v("FragmentManager", "add: " + var1);
      }

      this.makeActive(var1);
      if (!var1.mDetached) {
         if (this.mAdded.contains(var1)) {
            throw new IllegalStateException("Fragment already added: " + var1);
         }

         this.mAdded.add(var1);
         var1.mAdded = true;
         var1.mRemoving = false;
         if (var1.mView == null) {
            var1.mHiddenChanged = false;
         }

         if (var1.mHasMenu && var1.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
         }

         if (var2) {
            this.moveToState(var1);
         }
      }

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
      label593: {
         int var3;
         label597: {
            StringBuilder var77;
            label598: {
               try {
                  if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                     break label598;
                  }
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label593;
               }

               try {
                  if (this.mBackStackIndices == null) {
                     ArrayList var2 = new ArrayList();
                     this.mBackStackIndices = var2;
                  }
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  break label593;
               }

               try {
                  var3 = this.mBackStackIndices.size();
                  if (DEBUG) {
                     var77 = new StringBuilder();
                     Log.v("FragmentManager", var77.append("Setting back stack index ").append(var3).append(" to ").append(var1).toString());
                  }
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label593;
               }

               try {
                  this.mBackStackIndices.add(var1);
                  break label597;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label593;
               }
            }

            try {
               var3 = (Integer)this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
               if (DEBUG) {
                  var77 = new StringBuilder();
                  Log.v("FragmentManager", var77.append("Adding back stack index ").append(var3).append(" with ").append(var1).toString());
               }
            } catch (Throwable var73) {
               var10000 = var73;
               var10001 = false;
               break label593;
            }

            try {
               this.mBackStackIndices.set(var3, var1);
            } catch (Throwable var70) {
               var10000 = var70;
               var10001 = false;
               break label593;
            }
         }

         label567:
         try {
            return var3;
         } catch (Throwable var69) {
            var10000 = var69;
            var10001 = false;
            break label567;
         }
      }

      while(true) {
         Throwable var76 = var10000;

         try {
            throw var76;
         } catch (Throwable var68) {
            var10000 = var68;
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

   public void attachFragment(Fragment var1) {
      if (DEBUG) {
         Log.v("FragmentManager", "attach: " + var1);
      }

      if (var1.mDetached) {
         var1.mDetached = false;
         if (!var1.mAdded) {
            if (this.mAdded == null) {
               this.mAdded = new ArrayList();
            }

            if (this.mAdded.contains(var1)) {
               throw new IllegalStateException("Fragment already added: " + var1);
            }

            if (DEBUG) {
               Log.v("FragmentManager", "add from attach: " + var1);
            }

            this.mAdded.add(var1);
            var1.mAdded = true;
            if (var1.mHasMenu && var1.mMenuVisible) {
               this.mNeedMenuInvalidate = true;
            }
         }
      }

   }

   public FragmentTransaction beginTransaction() {
      return new BackStackRecord(this);
   }

   void completeShowHideFragment(Fragment var1) {
      if (var1.mView != null) {
         int var2 = var1.getNextTransition();
         boolean var3;
         if (!var1.mHidden) {
            var3 = true;
         } else {
            var3 = false;
         }

         Animation var4 = this.loadAnimation(var1, var2, var3, var1.getNextTransitionStyle());
         if (var4 != null) {
            this.setHWLayerAnimListenerIfAlpha(var1.mView, var4);
            var1.mView.startAnimation(var4);
            this.setHWLayerAnimListenerIfAlpha(var1.mView, var4);
            var4.start();
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

      if (var1.mAdded && var1.mHasMenu && var1.mMenuVisible) {
         this.mNeedMenuInvalidate = true;
      }

      var1.mHiddenChanged = false;
      var1.onHiddenChanged(var1.mHidden);
   }

   public void detachFragment(Fragment var1) {
      if (DEBUG) {
         Log.v("FragmentManager", "detach: " + var1);
      }

      if (!var1.mDetached) {
         var1.mDetached = true;
         if (var1.mAdded) {
            if (this.mAdded != null) {
               if (DEBUG) {
                  Log.v("FragmentManager", "remove from detach: " + var1);
               }

               this.mAdded.remove(var1);
            }

            if (var1.mHasMenu && var1.mMenuVisible) {
               this.mNeedMenuInvalidate = true;
            }

            var1.mAdded = false;
         }
      }

   }

   public void dispatchActivityCreated() {
      this.mStateSaved = false;
      this.mExecutingActions = true;
      this.moveToState(2, false);
      this.mExecutingActions = false;
   }

   public void dispatchConfigurationChanged(Configuration var1) {
      if (this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null) {
               var3.performConfigurationChanged(var1);
            }
         }
      }

   }

   public boolean dispatchContextItemSelected(MenuItem var1) {
      boolean var4;
      if (this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null && var3.performContextItemSelected(var1)) {
               var4 = true;
               return var4;
            }
         }
      }

      var4 = false;
      return var4;
   }

   public void dispatchCreate() {
      this.mStateSaved = false;
      this.mExecutingActions = true;
      this.moveToState(1, false);
      this.mExecutingActions = false;
   }

   public boolean dispatchCreateOptionsMenu(Menu var1, MenuInflater var2) {
      boolean var3 = false;
      boolean var4 = false;
      ArrayList var5 = null;
      ArrayList var6 = null;
      int var7;
      if (this.mAdded != null) {
         var7 = 0;

         while(true) {
            var5 = var6;
            var3 = var4;
            if (var7 >= this.mAdded.size()) {
               break;
            }

            Fragment var8 = (Fragment)this.mAdded.get(var7);
            var5 = var6;
            var3 = var4;
            if (var8 != null) {
               var5 = var6;
               var3 = var4;
               if (var8.performCreateOptionsMenu(var1, var2)) {
                  var3 = true;
                  var5 = var6;
                  if (var6 == null) {
                     var5 = new ArrayList();
                  }

                  var5.add(var8);
               }
            }

            ++var7;
            var6 = var5;
            var4 = var3;
         }
      }

      if (this.mCreatedMenus != null) {
         for(var7 = 0; var7 < this.mCreatedMenus.size(); ++var7) {
            Fragment var9 = (Fragment)this.mCreatedMenus.get(var7);
            if (var5 == null || !var5.contains(var9)) {
               var9.onDestroyOptionsMenu();
            }
         }
      }

      this.mCreatedMenus = var5;
      return var3;
   }

   public void dispatchDestroy() {
      this.mDestroyed = true;
      this.execPendingActions();
      this.mExecutingActions = true;
      this.moveToState(0, false);
      this.mExecutingActions = false;
      this.mHost = null;
      this.mContainer = null;
      this.mParent = null;
   }

   public void dispatchDestroyView() {
      this.mExecutingActions = true;
      this.moveToState(1, false);
      this.mExecutingActions = false;
   }

   public void dispatchLowMemory() {
      if (this.mAdded != null) {
         for(int var1 = 0; var1 < this.mAdded.size(); ++var1) {
            Fragment var2 = (Fragment)this.mAdded.get(var1);
            if (var2 != null) {
               var2.performLowMemory();
            }
         }
      }

   }

   public void dispatchMultiWindowModeChanged(boolean var1) {
      if (this.mAdded != null) {
         for(int var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null) {
               var3.performMultiWindowModeChanged(var1);
            }
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

      if (this.mLifecycleCallbacks != null) {
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
   }

   void dispatchOnFragmentAttached(Fragment var1, Context var2, boolean var3) {
      if (this.mParent != null) {
         FragmentManager var4 = this.mParent.getFragmentManager();
         if (var4 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var4).dispatchOnFragmentAttached(var1, var2, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
         Iterator var6 = this.mLifecycleCallbacks.iterator();

         while(true) {
            Pair var5;
            do {
               if (!var6.hasNext()) {
                  return;
               }

               var5 = (Pair)var6.next();
            } while(var3 && !(Boolean)var5.second);

            ((FragmentManager.FragmentLifecycleCallbacks)var5.first).onFragmentAttached(this, var1, var2);
         }
      }
   }

   void dispatchOnFragmentCreated(Fragment var1, Bundle var2, boolean var3) {
      if (this.mParent != null) {
         FragmentManager var4 = this.mParent.getFragmentManager();
         if (var4 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var4).dispatchOnFragmentCreated(var1, var2, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
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
   }

   void dispatchOnFragmentDestroyed(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentDestroyed(var1, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
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
   }

   void dispatchOnFragmentDetached(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentDetached(var1, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
         Iterator var5 = this.mLifecycleCallbacks.iterator();

         while(true) {
            Pair var4;
            do {
               if (!var5.hasNext()) {
                  return;
               }

               var4 = (Pair)var5.next();
            } while(var2 && !(Boolean)var4.second);

            ((FragmentManager.FragmentLifecycleCallbacks)var4.first).onFragmentDetached(this, var1);
         }
      }
   }

   void dispatchOnFragmentPaused(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentPaused(var1, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
         Iterator var4 = this.mLifecycleCallbacks.iterator();

         while(true) {
            Pair var5;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               var5 = (Pair)var4.next();
            } while(var2 && !(Boolean)var5.second);

            ((FragmentManager.FragmentLifecycleCallbacks)var5.first).onFragmentPaused(this, var1);
         }
      }
   }

   void dispatchOnFragmentPreAttached(Fragment var1, Context var2, boolean var3) {
      if (this.mParent != null) {
         FragmentManager var4 = this.mParent.getFragmentManager();
         if (var4 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var4).dispatchOnFragmentPreAttached(var1, var2, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
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
   }

   void dispatchOnFragmentResumed(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentResumed(var1, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
         Iterator var4 = this.mLifecycleCallbacks.iterator();

         while(true) {
            Pair var5;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               var5 = (Pair)var4.next();
            } while(var2 && !(Boolean)var5.second);

            ((FragmentManager.FragmentLifecycleCallbacks)var5.first).onFragmentResumed(this, var1);
         }
      }
   }

   void dispatchOnFragmentSaveInstanceState(Fragment var1, Bundle var2, boolean var3) {
      if (this.mParent != null) {
         FragmentManager var4 = this.mParent.getFragmentManager();
         if (var4 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var4).dispatchOnFragmentSaveInstanceState(var1, var2, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
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
   }

   void dispatchOnFragmentStarted(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentStarted(var1, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
         Iterator var4 = this.mLifecycleCallbacks.iterator();

         while(true) {
            Pair var5;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               var5 = (Pair)var4.next();
            } while(var2 && !(Boolean)var5.second);

            ((FragmentManager.FragmentLifecycleCallbacks)var5.first).onFragmentStarted(this, var1);
         }
      }
   }

   void dispatchOnFragmentStopped(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentStopped(var1, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
         Iterator var4 = this.mLifecycleCallbacks.iterator();

         while(true) {
            Pair var5;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               var5 = (Pair)var4.next();
            } while(var2 && !(Boolean)var5.second);

            ((FragmentManager.FragmentLifecycleCallbacks)var5.first).onFragmentStopped(this, var1);
         }
      }
   }

   void dispatchOnFragmentViewCreated(Fragment var1, View var2, Bundle var3, boolean var4) {
      if (this.mParent != null) {
         FragmentManager var5 = this.mParent.getFragmentManager();
         if (var5 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var5).dispatchOnFragmentViewCreated(var1, var2, var3, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
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
   }

   void dispatchOnFragmentViewDestroyed(Fragment var1, boolean var2) {
      if (this.mParent != null) {
         FragmentManager var3 = this.mParent.getFragmentManager();
         if (var3 instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)var3).dispatchOnFragmentViewDestroyed(var1, true);
         }
      }

      if (this.mLifecycleCallbacks != null) {
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
   }

   public boolean dispatchOptionsItemSelected(MenuItem var1) {
      boolean var4;
      if (this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null && var3.performOptionsItemSelected(var1)) {
               var4 = true;
               return var4;
            }
         }
      }

      var4 = false;
      return var4;
   }

   public void dispatchOptionsMenuClosed(Menu var1) {
      if (this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null) {
               var3.performOptionsMenuClosed(var1);
            }
         }
      }

   }

   public void dispatchPause() {
      this.mExecutingActions = true;
      this.moveToState(4, false);
      this.mExecutingActions = false;
   }

   public void dispatchPictureInPictureModeChanged(boolean var1) {
      if (this.mAdded != null) {
         for(int var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null) {
               var3.performPictureInPictureModeChanged(var1);
            }
         }
      }

   }

   public boolean dispatchPrepareOptionsMenu(Menu var1) {
      boolean var2 = false;
      boolean var3 = false;
      if (this.mAdded != null) {
         int var4 = 0;

         while(true) {
            var2 = var3;
            if (var4 >= this.mAdded.size()) {
               break;
            }

            Fragment var5 = (Fragment)this.mAdded.get(var4);
            var2 = var3;
            if (var5 != null) {
               var2 = var3;
               if (var5.performPrepareOptionsMenu(var1)) {
                  var2 = true;
               }
            }

            ++var4;
            var3 = var2;
         }
      }

      return var2;
   }

   public void dispatchReallyStop() {
      this.mExecutingActions = true;
      this.moveToState(2, false);
      this.mExecutingActions = false;
   }

   public void dispatchResume() {
      this.mStateSaved = false;
      this.mExecutingActions = true;
      this.moveToState(5, false);
      this.mExecutingActions = false;
   }

   public void dispatchStart() {
      this.mStateSaved = false;
      this.mExecutingActions = true;
      this.moveToState(4, false);
      this.mExecutingActions = false;
   }

   public void dispatchStop() {
      this.mStateSaved = true;
      this.mExecutingActions = true;
      this.moveToState(3, false);
      this.mExecutingActions = false;
   }

   void doPendingDeferredStart() {
      if (this.mHavePendingDeferredStart) {
         boolean var1 = false;

         boolean var4;
         for(int var2 = 0; var2 < this.mActive.size(); var1 = var4) {
            Fragment var3 = (Fragment)this.mActive.get(var2);
            var4 = var1;
            if (var3 != null) {
               var4 = var1;
               if (var3.mLoaderManager != null) {
                  var4 = var1 | var3.mLoaderManager.hasRunningLoaders();
               }
            }

            ++var2;
         }

         if (!var1) {
            this.mHavePendingDeferredStart = false;
            this.startPendingDeferredFragments();
         }
      }

   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      String var5 = var1 + "    ";
      int var6;
      int var7;
      Fragment var8;
      if (this.mActive != null) {
         var6 = this.mActive.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.print("Active Fragments in ");
            var3.print(Integer.toHexString(System.identityHashCode(this)));
            var3.println(":");

            for(var7 = 0; var7 < var6; ++var7) {
               var8 = (Fragment)this.mActive.get(var7);
               var3.print(var1);
               var3.print("  #");
               var3.print(var7);
               var3.print(": ");
               var3.println(var8);
               if (var8 != null) {
                  var8.dump(var5, var2, var3, var4);
               }
            }
         }
      }

      if (this.mAdded != null) {
         var6 = this.mAdded.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.println("Added Fragments:");

            for(var7 = 0; var7 < var6; ++var7) {
               var8 = (Fragment)this.mAdded.get(var7);
               var3.print(var1);
               var3.print("  #");
               var3.print(var7);
               var3.print(": ");
               var3.println(var8.toString());
            }
         }
      }

      if (this.mCreatedMenus != null) {
         var6 = this.mCreatedMenus.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.println("Fragments Created Menus:");

            for(var7 = 0; var7 < var6; ++var7) {
               var8 = (Fragment)this.mCreatedMenus.get(var7);
               var3.print(var1);
               var3.print("  #");
               var3.print(var7);
               var3.print(": ");
               var3.println(var8.toString());
            }
         }
      }

      if (this.mBackStack != null) {
         var6 = this.mBackStack.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.println("Back Stack:");

            for(var7 = 0; var7 < var6; ++var7) {
               BackStackRecord var54 = (BackStackRecord)this.mBackStack.get(var7);
               var3.print(var1);
               var3.print("  #");
               var3.print(var7);
               var3.print(": ");
               var3.println(var54.toString());
               var54.dump(var5, var2, var3, var4);
            }
         }
      }

      synchronized(this){}

      label1082: {
         Throwable var10000;
         boolean var10001;
         label1083: {
            label1084: {
               try {
                  if (this.mBackStackIndices == null) {
                     break label1084;
                  }

                  var6 = this.mBackStackIndices.size();
               } catch (Throwable var50) {
                  var10000 = var50;
                  var10001 = false;
                  break label1083;
               }

               if (var6 > 0) {
                  try {
                     var3.print(var1);
                     var3.println("Back Stack Indices:");
                  } catch (Throwable var49) {
                     var10000 = var49;
                     var10001 = false;
                     break label1083;
                  }

                  for(var7 = 0; var7 < var6; ++var7) {
                     try {
                        BackStackRecord var52 = (BackStackRecord)this.mBackStackIndices.get(var7);
                        var3.print(var1);
                        var3.print("  #");
                        var3.print(var7);
                        var3.print(": ");
                        var3.println(var52);
                     } catch (Throwable var48) {
                        var10000 = var48;
                        var10001 = false;
                        break label1083;
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
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label1083;
            }

            label1024:
            try {
               break label1082;
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label1024;
            }
         }

         while(true) {
            Throwable var51 = var10000;

            try {
               throw var51;
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               continue;
            }
         }
      }

      if (this.mPendingActions != null) {
         var6 = this.mPendingActions.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.println("Pending Actions:");

            for(var7 = 0; var7 < var6; ++var7) {
               FragmentManagerImpl.OpGenerator var53 = (FragmentManagerImpl.OpGenerator)this.mPendingActions.get(var7);
               var3.print(var1);
               var3.print("  #");
               var3.print(var7);
               var3.print(": ");
               var3.println(var53);
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

      if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
         var3.print(var1);
         var3.print("  mAvailIndices: ");
         var3.println(Arrays.toString(this.mAvailIndices.toArray()));
      }

   }

   public void enqueueAction(FragmentManagerImpl.OpGenerator var1, boolean var2) {
      if (!var2) {
         this.checkStateLoss();
      }

      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label312: {
         label305: {
            try {
               if (!this.mDestroyed && this.mHost != null) {
                  break label305;
               }
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label312;
            }

            try {
               IllegalStateException var34 = new IllegalStateException("Activity has been destroyed");
               throw var34;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label312;
            }
         }

         try {
            if (this.mPendingActions == null) {
               ArrayList var3 = new ArrayList();
               this.mPendingActions = var3;
            }
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label312;
         }

         label292:
         try {
            this.mPendingActions.add(var1);
            this.scheduleCommit();
            return;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label292;
         }
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean execPendingActions() {
      this.ensureExecReady(true);

      boolean var1;
      for(var1 = false; this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop); var1 = true) {
         this.mExecutingActions = true;

         try {
            this.optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
         } finally {
            this.cleanupExec();
         }
      }

      this.doPendingDeferredStart();
      return var1;
   }

   public void execSingleAction(FragmentManagerImpl.OpGenerator var1, boolean var2) {
      this.ensureExecReady(var2);
      if (var1.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
         this.mExecutingActions = true;

         try {
            this.optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
         } finally {
            this.cleanupExec();
         }
      }

      this.doPendingDeferredStart();
   }

   public boolean executePendingTransactions() {
      boolean var1 = this.execPendingActions();
      this.forcePostponedTransactions();
      return var1;
   }

   public Fragment findFragmentById(int var1) {
      int var2;
      Fragment var3;
      if (this.mAdded != null) {
         for(var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null && var3.mFragmentId == var1) {
               return var3;
            }
         }
      }

      if (this.mActive != null) {
         for(var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            Fragment var4 = (Fragment)this.mActive.get(var2);
            if (var4 != null) {
               var3 = var4;
               if (var4.mFragmentId == var1) {
                  return var3;
               }
            }
         }
      }

      var3 = null;
      return var3;
   }

   public Fragment findFragmentByTag(String var1) {
      int var2;
      Fragment var3;
      if (this.mAdded != null && var1 != null) {
         for(var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null && var1.equals(var3.mTag)) {
               return var3;
            }
         }
      }

      if (this.mActive != null && var1 != null) {
         for(var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            Fragment var4 = (Fragment)this.mActive.get(var2);
            if (var4 != null) {
               var3 = var4;
               if (var1.equals(var4.mTag)) {
                  return var3;
               }
            }
         }
      }

      var3 = null;
      return var3;
   }

   public Fragment findFragmentByWho(String var1) {
      Fragment var4;
      if (this.mActive != null && var1 != null) {
         for(int var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            Fragment var3 = (Fragment)this.mActive.get(var2);
            if (var3 != null) {
               var3 = var3.findFragmentByWho(var1);
               if (var3 != null) {
                  var4 = var3;
                  return var4;
               }
            }
         }
      }

      var4 = null;
      return var4;
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
               Log.v("FragmentManager", var23.append("Freeing back stack index ").append(var1).toString());
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
      Fragment var5;
      if (var3 == -1) {
         var5 = null;
      } else {
         if (var3 >= this.mActive.size()) {
            this.throwException(new IllegalStateException("Fragment no longer exists for key " + var2 + ": index " + var3));
         }

         Fragment var4 = (Fragment)this.mActive.get(var3);
         var5 = var4;
         if (var4 == null) {
            this.throwException(new IllegalStateException("Fragment no longer exists for key " + var2 + ": index " + var3));
            var5 = var4;
         }
      }

      return var5;
   }

   public List getFragments() {
      return this.mActive;
   }

   LayoutInflaterFactory getLayoutInflaterFactory() {
      return this;
   }

   public void hideFragment(Fragment var1) {
      boolean var2 = true;
      if (DEBUG) {
         Log.v("FragmentManager", "hide: " + var1);
      }

      if (!var1.mHidden) {
         var1.mHidden = true;
         if (var1.mHiddenChanged) {
            var2 = false;
         }

         var1.mHiddenChanged = var2;
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

   Animation loadAnimation(Fragment var1, int var2, boolean var3, int var4) {
      Animation var5 = var1.onCreateAnimation(var2, var3, var1.getNextAnim());
      Animation var6;
      if (var5 != null) {
         var6 = var5;
      } else {
         if (var1.getNextAnim() != 0) {
            var6 = AnimationUtils.loadAnimation(this.mHost.getContext(), var1.getNextAnim());
            if (var6 != null) {
               return var6;
            }
         }

         if (var2 == 0) {
            var6 = null;
         } else {
            var2 = transitToStyleIndex(var2, var3);
            if (var2 < 0) {
               var6 = null;
            } else {
               switch(var2) {
               case 1:
                  var6 = makeOpenCloseAnimation(this.mHost.getContext(), 1.125F, 1.0F, 0.0F, 1.0F);
                  break;
               case 2:
                  var6 = makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 0.975F, 1.0F, 0.0F);
                  break;
               case 3:
                  var6 = makeOpenCloseAnimation(this.mHost.getContext(), 0.975F, 1.0F, 0.0F, 1.0F);
                  break;
               case 4:
                  var6 = makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 1.075F, 1.0F, 0.0F);
                  break;
               case 5:
                  var6 = makeFadeAnimation(this.mHost.getContext(), 0.0F, 1.0F);
                  break;
               case 6:
                  var6 = makeFadeAnimation(this.mHost.getContext(), 1.0F, 0.0F);
                  break;
               default:
                  var2 = var4;
                  if (var4 == 0) {
                     var2 = var4;
                     if (this.mHost.onHasWindowAnimations()) {
                        var2 = this.mHost.onGetWindowAnimations();
                     }
                  }

                  if (var2 == 0) {
                     var6 = null;
                  } else {
                     var6 = null;
                  }
               }
            }
         }
      }

      return var6;
   }

   void makeActive(Fragment var1) {
      if (var1.mIndex < 0) {
         if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
            var1.setIndex((Integer)this.mAvailIndices.remove(this.mAvailIndices.size() - 1), this.mParent);
            this.mActive.set(var1.mIndex, var1);
         } else {
            if (this.mActive == null) {
               this.mActive = new ArrayList();
            }

            var1.setIndex(this.mActive.size(), this.mParent);
            this.mActive.add(var1);
         }

         if (DEBUG) {
            Log.v("FragmentManager", "Allocated fragment index " + var1);
         }
      }

   }

   void makeInactive(Fragment var1) {
      if (var1.mIndex >= 0) {
         if (DEBUG) {
            Log.v("FragmentManager", "Freeing fragment index " + var1);
         }

         this.mActive.set(var1.mIndex, (Object)null);
         if (this.mAvailIndices == null) {
            this.mAvailIndices = new ArrayList();
         }

         this.mAvailIndices.add(var1.mIndex);
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
               if (VERSION.SDK_INT < 11) {
                  var1.mView.setVisibility(0);
               } else if (var1.mPostponedAlpha > 0.0F) {
                  var1.mView.setAlpha(var1.mPostponedAlpha);
               }

               var1.mPostponedAlpha = 0.0F;
               var1.mIsNewlyAdded = false;
               Animation var7 = this.loadAnimation(var1, var1.getNextTransition(), true, var1.getNextTransitionStyle());
               if (var7 != null) {
                  this.setHWLayerAnimListenerIfAlpha(var1.mView, var7);
                  var1.mView.startAnimation(var7);
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
      } else {
         if (var2 || var1 != this.mCurState) {
            this.mCurState = var1;
            if (this.mActive != null) {
               boolean var7 = false;
               boolean var3 = false;
               int var4;
               int var5;
               Fragment var6;
               if (this.mAdded != null) {
                  var4 = this.mAdded.size();
                  var5 = 0;

                  while(true) {
                     var7 = var3;
                     if (var5 >= var4) {
                        break;
                     }

                     var6 = (Fragment)this.mAdded.get(var5);
                     this.moveFragmentToExpectedState(var6);
                     var7 = var3;
                     if (var6.mLoaderManager != null) {
                        var7 = var3 | var6.mLoaderManager.hasRunningLoaders();
                     }

                     ++var5;
                     var3 = var7;
                  }
               }

               var4 = this.mActive.size();

               for(var5 = 0; var5 < var4; var7 = var3) {
                  var6 = (Fragment)this.mActive.get(var5);
                  var3 = var7;
                  if (var6 != null) {
                     label78: {
                        if (!var6.mRemoving) {
                           var3 = var7;
                           if (!var6.mDetached) {
                              break label78;
                           }
                        }

                        var3 = var7;
                        if (!var6.mIsNewlyAdded) {
                           this.moveFragmentToExpectedState(var6);
                           var3 = var7;
                           if (var6.mLoaderManager != null) {
                              var3 = var7 | var6.mLoaderManager.hasRunningLoaders();
                           }
                        }
                     }
                  }

                  ++var5;
               }

               if (!var7) {
                  this.startPendingDeferredFragments();
               }

               if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
                  this.mHost.onSupportInvalidateOptionsMenu();
                  this.mNeedMenuInvalidate = false;
               }
            }
         }

      }
   }

   void moveToState(Fragment var1) {
      this.moveToState(var1, this.mCurState, 0, 0, false);
   }

   void moveToState(final Fragment var1, int var2, int var3, int var4, boolean var5) {
      int var6;
      label275: {
         if (var1.mAdded) {
            var6 = var2;
            if (!var1.mDetached) {
               break label275;
            }
         }

         var6 = var2;
         if (var2 > 1) {
            var6 = 1;
         }
      }

      int var7 = var6;
      if (var1.mRemoving) {
         var7 = var6;
         if (var6 > var1.mState) {
            var7 = var1.mState;
         }
      }

      var2 = var7;
      if (var1.mDeferStart) {
         var2 = var7;
         if (var1.mState < 4) {
            var2 = var7;
            if (var7 > 3) {
               var2 = 3;
            }
         }
      }

      ViewGroup var9;
      if (var1.mState < var2) {
         label291: {
            if (var1.mFromLayout && !var1.mInLayout) {
               return;
            }

            if (var1.getAnimatingAway() != null) {
               var1.setAnimatingAway((View)null);
               this.moveToState(var1, var1.getStateAfterAnimating(), 0, 0, true);
            }

            label260: {
               var4 = var2;
               var6 = var2;
               var7 = var2;
               var3 = var2;
               switch(var1.mState) {
               case 0:
                  if (DEBUG) {
                     Log.v("FragmentManager", "moveto CREATED: " + var1);
                  }

                  var3 = var2;
                  if (var1.mSavedFragmentState != null) {
                     var1.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                     var1.mSavedViewState = var1.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                     var1.mTarget = this.getFragment(var1.mSavedFragmentState, "android:target_state");
                     if (var1.mTarget != null) {
                        var1.mTargetRequestCode = var1.mSavedFragmentState.getInt("android:target_req_state", 0);
                     }

                     var1.mUserVisibleHint = var1.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
                     var3 = var2;
                     if (!var1.mUserVisibleHint) {
                        var1.mDeferStart = true;
                        var3 = var2;
                        if (var2 > 3) {
                           var3 = 3;
                        }
                     }
                  }

                  var1.mHost = this.mHost;
                  var1.mParentFragment = this.mParent;
                  FragmentManagerImpl var8;
                  if (this.mParent != null) {
                     var8 = this.mParent.mChildFragmentManager;
                  } else {
                     var8 = this.mHost.getFragmentManagerImpl();
                  }

                  var1.mFragmentManager = var8;
                  this.dispatchOnFragmentPreAttached(var1, this.mHost.getContext(), false);
                  var1.mCalled = false;
                  var1.onAttach(this.mHost.getContext());
                  if (!var1.mCalled) {
                     throw new SuperNotCalledException("Fragment " + var1 + " did not call through to super.onAttach()");
                  }

                  if (var1.mParentFragment == null) {
                     this.mHost.onAttachFragment(var1);
                  } else {
                     var1.mParentFragment.onAttachFragment(var1);
                  }

                  this.dispatchOnFragmentAttached(var1, this.mHost.getContext(), false);
                  if (!var1.mRetaining) {
                     var1.performCreate(var1.mSavedFragmentState);
                     this.dispatchOnFragmentCreated(var1, var1.mSavedFragmentState, false);
                  } else {
                     var1.restoreChildFragmentState(var1.mSavedFragmentState);
                     var1.mState = 1;
                  }

                  var1.mRetaining = false;
                  var4 = var3;
                  if (var1.mFromLayout) {
                     var1.mView = var1.performCreateView(var1.getLayoutInflater(var1.mSavedFragmentState), (ViewGroup)null, var1.mSavedFragmentState);
                     if (var1.mView != null) {
                        var1.mInnerView = var1.mView;
                        if (VERSION.SDK_INT >= 11) {
                           ViewCompat.setSaveFromParentEnabled(var1.mView, false);
                        } else {
                           var1.mView = NoSaveStateFrameLayout.wrap(var1.mView);
                        }

                        if (var1.mHidden) {
                           var1.mView.setVisibility(8);
                        }

                        var1.onViewCreated(var1.mView, var1.mSavedFragmentState);
                        this.dispatchOnFragmentViewCreated(var1, var1.mView, var1.mSavedFragmentState, false);
                        var4 = var3;
                     } else {
                        var1.mInnerView = null;
                        var4 = var3;
                     }
                  }
               case 1:
                  var6 = var4;
                  if (var4 > 1) {
                     if (DEBUG) {
                        Log.v("FragmentManager", "moveto ACTIVITY_CREATED: " + var1);
                     }

                     if (!var1.mFromLayout) {
                        ViewGroup var11 = null;
                        if (var1.mContainerId != 0) {
                           if (var1.mContainerId == -1) {
                              this.throwException(new IllegalArgumentException("Cannot create fragment " + var1 + " for a container view with no id"));
                           }

                           var9 = (ViewGroup)this.mContainer.onFindViewById(var1.mContainerId);
                           var11 = var9;
                           if (var9 == null) {
                              var11 = var9;
                              if (!var1.mRestored) {
                                 String var12;
                                 try {
                                    var12 = var1.getResources().getResourceName(var1.mContainerId);
                                 } catch (NotFoundException var10) {
                                    var12 = "unknown";
                                 }

                                 this.throwException(new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(var1.mContainerId) + " (" + var12 + ") for fragment " + var1));
                                 var11 = var9;
                              }
                           }
                        }

                        var1.mContainer = var11;
                        var1.mView = var1.performCreateView(var1.getLayoutInflater(var1.mSavedFragmentState), var11, var1.mSavedFragmentState);
                        if (var1.mView != null) {
                           var1.mInnerView = var1.mView;
                           if (VERSION.SDK_INT >= 11) {
                              ViewCompat.setSaveFromParentEnabled(var1.mView, false);
                           } else {
                              var1.mView = NoSaveStateFrameLayout.wrap(var1.mView);
                           }

                           if (var11 != null) {
                              var11.addView(var1.mView);
                           }

                           if (var1.mHidden) {
                              var1.mView.setVisibility(8);
                           }

                           var1.onViewCreated(var1.mView, var1.mSavedFragmentState);
                           this.dispatchOnFragmentViewCreated(var1, var1.mView, var1.mSavedFragmentState, false);
                           if (var1.mView.getVisibility() == 0 && var1.mContainer != null) {
                              var5 = true;
                           } else {
                              var5 = false;
                           }

                           var1.mIsNewlyAdded = var5;
                        } else {
                           var1.mInnerView = null;
                        }
                     }

                     var1.performActivityCreated(var1.mSavedFragmentState);
                     this.dispatchOnFragmentActivityCreated(var1, var1.mSavedFragmentState, false);
                     if (var1.mView != null) {
                        var1.restoreViewState(var1.mSavedFragmentState);
                     }

                     var1.mSavedFragmentState = null;
                     var6 = var4;
                  }
               case 2:
                  var7 = var6;
                  if (var6 > 2) {
                     var1.mState = 3;
                     var7 = var6;
                  }
               case 3:
                  break;
               case 4:
                  break label260;
               default:
                  var7 = var2;
                  break label291;
               }

               var3 = var7;
               if (var7 > 3) {
                  if (DEBUG) {
                     Log.v("FragmentManager", "moveto STARTED: " + var1);
                  }

                  var1.performStart();
                  this.dispatchOnFragmentStarted(var1, false);
                  var3 = var7;
               }
            }

            var7 = var3;
            if (var3 > 4) {
               if (DEBUG) {
                  Log.v("FragmentManager", "moveto RESUMED: " + var1);
               }

               var1.performResume();
               this.dispatchOnFragmentResumed(var1, false);
               var1.mSavedFragmentState = null;
               var1.mSavedViewState = null;
               var7 = var3;
            }
         }
      } else {
         var7 = var2;
         if (var1.mState > var2) {
            switch(var1.mState) {
            case 5:
               if (var2 < 5) {
                  if (DEBUG) {
                     Log.v("FragmentManager", "movefrom RESUMED: " + var1);
                  }

                  var1.performPause();
                  this.dispatchOnFragmentPaused(var1, false);
               }
            case 4:
               if (var2 < 4) {
                  if (DEBUG) {
                     Log.v("FragmentManager", "movefrom STARTED: " + var1);
                  }

                  var1.performStop();
                  this.dispatchOnFragmentStopped(var1, false);
               }
            case 3:
               if (var2 < 3) {
                  if (DEBUG) {
                     Log.v("FragmentManager", "movefrom STOPPED: " + var1);
                  }

                  var1.performReallyStop();
               }
            case 2:
               if (var2 < 2) {
                  if (DEBUG) {
                     Log.v("FragmentManager", "movefrom ACTIVITY_CREATED: " + var1);
                  }

                  if (var1.mView != null && this.mHost.onShouldSaveFragmentState(var1) && var1.mSavedViewState == null) {
                     this.saveFragmentViewState(var1);
                  }

                  var1.performDestroyView();
                  this.dispatchOnFragmentViewDestroyed(var1, false);
                  if (var1.mView != null && var1.mContainer != null) {
                     var9 = null;
                     Animation var13 = var9;
                     if (this.mCurState > 0) {
                        var13 = var9;
                        if (!this.mDestroyed) {
                           var13 = var9;
                           if (var1.mView.getVisibility() == 0) {
                              var13 = var9;
                              if (var1.mPostponedAlpha >= 0.0F) {
                                 var13 = this.loadAnimation(var1, var3, false, var4);
                              }
                           }
                        }
                     }

                     var1.mPostponedAlpha = 0.0F;
                     if (var13 != null) {
                        var1.setAnimatingAway(var1.mView);
                        var1.setStateAfterAnimating(var2);
                        var13.setAnimationListener(new FragmentManagerImpl.AnimateOnHWLayerIfNeededListener(var1.mView, var13) {
                           public void onAnimationEnd(Animation var1x) {
                              super.onAnimationEnd(var1x);
                              if (var1.getAnimatingAway() != null) {
                                 var1.setAnimatingAway((View)null);
                                 FragmentManagerImpl.this.moveToState(var1, var1.getStateAfterAnimating(), 0, 0, false);
                              }

                           }
                        });
                        var1.mView.startAnimation(var13);
                     }

                     var1.mContainer.removeView(var1.mView);
                  }

                  var1.mContainer = null;
                  var1.mView = null;
                  var1.mInnerView = null;
               }
            case 1:
               var7 = var2;
               if (var2 < 1) {
                  if (this.mDestroyed && var1.getAnimatingAway() != null) {
                     View var14 = var1.getAnimatingAway();
                     var1.setAnimatingAway((View)null);
                     var14.clearAnimation();
                  }

                  if (var1.getAnimatingAway() != null) {
                     var1.setStateAfterAnimating(var2);
                     var7 = 1;
                  } else {
                     if (DEBUG) {
                        Log.v("FragmentManager", "movefrom CREATED: " + var1);
                     }

                     if (!var1.mRetaining) {
                        var1.performDestroy();
                        this.dispatchOnFragmentDestroyed(var1, false);
                     } else {
                        var1.mState = 0;
                     }

                     var1.performDetach();
                     this.dispatchOnFragmentDetached(var1, false);
                     var7 = var2;
                     if (!var5) {
                        if (!var1.mRetaining) {
                           this.makeInactive(var1);
                           var7 = var2;
                        } else {
                           var1.mHost = null;
                           var1.mParentFragment = null;
                           var1.mFragmentManager = null;
                           var7 = var2;
                        }
                     }
                  }
               }
               break;
            default:
               var7 = var2;
            }
         }
      }

      if (var1.mState != var7) {
         Log.w("FragmentManager", "moveToState: Fragment state for " + var1 + " not updated inline; " + "expected state " + var7 + " found " + var1.mState);
         var1.mState = var7;
      }

   }

   public void noteStateNotSaved() {
      this.mStateSaved = false;
   }

   public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      Object var5 = null;
      View var13;
      if (!"fragment".equals(var2)) {
         var13 = (View)var5;
      } else {
         var2 = var4.getAttributeValue((String)null, "class");
         TypedArray var6 = var3.obtainStyledAttributes(var4, FragmentManagerImpl.FragmentTag.Fragment);
         String var7 = var2;
         if (var2 == null) {
            var7 = var6.getString(0);
         }

         int var8 = var6.getResourceId(1, -1);
         String var9 = var6.getString(2);
         var6.recycle();
         var13 = (View)var5;
         if (Fragment.isSupportFragmentClass(this.mHost.getContext(), var7)) {
            int var10;
            if (var1 != null) {
               var10 = var1.getId();
            } else {
               var10 = 0;
            }

            if (var10 == -1 && var8 == -1 && var9 == null) {
               throw new IllegalArgumentException(var4.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + var7);
            }

            Fragment var14;
            if (var8 != -1) {
               var14 = this.findFragmentById(var8);
            } else {
               var14 = null;
            }

            Fragment var12 = var14;
            if (var14 == null) {
               var12 = var14;
               if (var9 != null) {
                  var12 = this.findFragmentByTag(var9);
               }
            }

            var14 = var12;
            if (var12 == null) {
               var14 = var12;
               if (var10 != -1) {
                  var14 = this.findFragmentById(var10);
               }
            }

            if (DEBUG) {
               Log.v("FragmentManager", "onCreateView: id=0x" + Integer.toHexString(var8) + " fname=" + var7 + " existing=" + var14);
            }

            if (var14 == null) {
               var12 = Fragment.instantiate(var3, var7);
               var12.mFromLayout = true;
               int var11;
               if (var8 != 0) {
                  var11 = var8;
               } else {
                  var11 = var10;
               }

               var12.mFragmentId = var11;
               var12.mContainerId = var10;
               var12.mTag = var9;
               var12.mInLayout = true;
               var12.mFragmentManager = this;
               var12.mHost = this.mHost;
               var12.onInflate(this.mHost.getContext(), var4, var12.mSavedFragmentState);
               this.addFragment(var12, true);
            } else {
               if (var14.mInLayout) {
                  throw new IllegalArgumentException(var4.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(var8) + ", tag " + var9 + ", or parent id 0x" + Integer.toHexString(var10) + " with another fragment for " + var7);
               }

               var14.mInLayout = true;
               var14.mHost = this.mHost;
               var12 = var14;
               if (!var14.mRetaining) {
                  var14.onInflate(this.mHost.getContext(), var4, var14.mSavedFragmentState);
                  var12 = var14;
               }
            }

            if (this.mCurState < 1 && var12.mFromLayout) {
               this.moveToState(var12, 1, 0, 0, false);
            } else {
               this.moveToState(var12);
            }

            if (var12.mView == null) {
               throw new IllegalStateException("Fragment " + var7 + " did not create a view.");
            }

            if (var8 != 0) {
               var12.mView.setId(var8);
            }

            if (var12.mView.getTag() == null) {
               var12.mView.setTag(var9);
            }

            var13 = var12.mView;
         }
      }

      return var13;
   }

   public void performPendingDeferredStart(Fragment var1) {
      if (var1.mDeferStart) {
         if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
         } else {
            var1.mDeferStart = false;
            this.moveToState(var1, this.mCurState, 0, 0, false);
         }
      }

   }

   public void popBackStack() {
      this.enqueueAction(new FragmentManagerImpl.PopBackStackState((String)null, -1, 0), false);
   }

   public void popBackStack(int var1, int var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Bad id: " + var1);
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
         throw new IllegalArgumentException("Bad id: " + var1);
      } else {
         return this.popBackStackImmediate((String)null, var1, var2);
      }
   }

   public boolean popBackStackImmediate(String var1, int var2) {
      this.checkStateLoss();
      return this.popBackStackImmediate(var1, -1, var2);
   }

   boolean popBackStackState(ArrayList var1, ArrayList var2, String var3, int var4, int var5) {
      boolean var6 = false;
      boolean var7;
      if (this.mBackStack == null) {
         var7 = var6;
      } else {
         if (var3 == null && var4 < 0 && (var5 & 1) == 0) {
            var4 = this.mBackStack.size() - 1;
            var7 = var6;
            if (var4 < 0) {
               return var7;
            }

            var1.add(this.mBackStack.remove(var4));
            var2.add(true);
         } else {
            int var8 = -1;
            if (var3 != null || var4 >= 0) {
               int var9;
               BackStackRecord var10;
               for(var9 = this.mBackStack.size() - 1; var9 >= 0; --var9) {
                  var10 = (BackStackRecord)this.mBackStack.get(var9);
                  if (var3 != null && var3.equals(var10.getName()) || var4 >= 0 && var4 == var10.mIndex) {
                     break;
                  }
               }

               var7 = var6;
               if (var9 < 0) {
                  return var7;
               }

               var8 = var9;
               if ((var5 & 1) != 0) {
                  var5 = var9 - 1;

                  while(true) {
                     var8 = var5;
                     if (var5 < 0) {
                        break;
                     }

                     var10 = (BackStackRecord)this.mBackStack.get(var5);
                     if (var3 == null || !var3.equals(var10.getName())) {
                        var8 = var5;
                        if (var4 < 0) {
                           break;
                        }

                        var8 = var5;
                        if (var4 != var10.mIndex) {
                           break;
                        }
                     }

                     --var5;
                  }
               }
            }

            var7 = var6;
            if (var8 == this.mBackStack.size() - 1) {
               return var7;
            }

            for(var4 = this.mBackStack.size() - 1; var4 > var8; --var4) {
               var1.add(this.mBackStack.remove(var4));
               var2.add(true);
            }
         }

         var7 = true;
      }

      return var7;
   }

   public void putFragment(Bundle var1, String var2, Fragment var3) {
      if (var3.mIndex < 0) {
         this.throwException(new IllegalStateException("Fragment " + var3 + " is not currently in the FragmentManager"));
      }

      var1.putInt(var2, var3.mIndex);
   }

   public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks var1, boolean var2) {
      if (this.mLifecycleCallbacks == null) {
         this.mLifecycleCallbacks = new CopyOnWriteArrayList();
      }

      this.mLifecycleCallbacks.add(new Pair(var1, var2));
   }

   public void removeFragment(Fragment var1) {
      if (DEBUG) {
         Log.v("FragmentManager", "remove: " + var1 + " nesting=" + var1.mBackStackNesting);
      }

      boolean var2;
      if (!var1.isInBackStack()) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (!var1.mDetached || var2) {
         if (this.mAdded != null) {
            this.mAdded.remove(var1);
         }

         if (var1.mHasMenu && var1.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
         }

         var1.mAdded = false;
         var1.mRemoving = true;
      }

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

   void restoreAllState(Parcelable var1, FragmentManagerNonConfig var2) {
      if (var1 != null) {
         FragmentManagerState var3 = (FragmentManagerState)var1;
         if (var3.mActive != null) {
            List var9 = null;
            List var4;
            int var6;
            int var7;
            FragmentState var8;
            Fragment var10;
            if (var2 != null) {
               var4 = var2.getFragments();
               List var5 = var2.getChildNonConfigs();
               if (var4 != null) {
                  var6 = var4.size();
               } else {
                  var6 = 0;
               }

               var7 = 0;

               while(true) {
                  var9 = var5;
                  if (var7 >= var6) {
                     break;
                  }

                  var10 = (Fragment)var4.get(var7);
                  if (DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: re-attaching retained " + var10);
                  }

                  var8 = var3.mActive[var10.mIndex];
                  var8.mInstance = var10;
                  var10.mSavedViewState = null;
                  var10.mBackStackNesting = 0;
                  var10.mInLayout = false;
                  var10.mAdded = false;
                  var10.mTarget = null;
                  if (var8.mSavedFragmentState != null) {
                     var8.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                     var10.mSavedViewState = var8.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                     var10.mSavedFragmentState = var8.mSavedFragmentState;
                  }

                  ++var7;
               }
            }

            this.mActive = new ArrayList(var3.mActive.length);
            if (this.mAvailIndices != null) {
               this.mAvailIndices.clear();
            }

            for(var6 = 0; var6 < var3.mActive.length; ++var6) {
               var8 = var3.mActive[var6];
               if (var8 != null) {
                  var4 = null;
                  FragmentManagerNonConfig var14 = var4;
                  if (var9 != null) {
                     var14 = var4;
                     if (var6 < var9.size()) {
                        var14 = (FragmentManagerNonConfig)var9.get(var6);
                     }
                  }

                  Fragment var15 = var8.instantiate(this.mHost, this.mParent, var14);
                  if (DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: active #" + var6 + ": " + var15);
                  }

                  this.mActive.add(var15);
                  var8.mInstance = null;
               } else {
                  this.mActive.add((Object)null);
                  if (this.mAvailIndices == null) {
                     this.mAvailIndices = new ArrayList();
                  }

                  if (DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: avail #" + var6);
                  }

                  this.mAvailIndices.add(var6);
               }
            }

            if (var2 != null) {
               List var11 = var2.getFragments();
               if (var11 != null) {
                  var6 = var11.size();
               } else {
                  var6 = 0;
               }

               for(var7 = 0; var7 < var6; ++var7) {
                  var10 = (Fragment)var11.get(var7);
                  if (var10.mTargetIndex >= 0) {
                     if (var10.mTargetIndex < this.mActive.size()) {
                        var10.mTarget = (Fragment)this.mActive.get(var10.mTargetIndex);
                     } else {
                        Log.w("FragmentManager", "Re-attaching retained fragment " + var10 + " target no longer exists: " + var10.mTargetIndex);
                        var10.mTarget = null;
                     }
                  }
               }
            }

            if (var3.mAdded != null) {
               this.mAdded = new ArrayList(var3.mAdded.length);

               for(var6 = 0; var6 < var3.mAdded.length; ++var6) {
                  var10 = (Fragment)this.mActive.get(var3.mAdded[var6]);
                  if (var10 == null) {
                     this.throwException(new IllegalStateException("No instantiated fragment for index #" + var3.mAdded[var6]));
                  }

                  var10.mAdded = true;
                  if (DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: added #" + var6 + ": " + var10);
                  }

                  if (this.mAdded.contains(var10)) {
                     throw new IllegalStateException("Already added!");
                  }

                  this.mAdded.add(var10);
               }
            } else {
               this.mAdded = null;
            }

            if (var3.mBackStack != null) {
               this.mBackStack = new ArrayList(var3.mBackStack.length);

               for(var6 = 0; var6 < var3.mBackStack.length; ++var6) {
                  BackStackRecord var13 = var3.mBackStack[var6].instantiate(this);
                  if (DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: back stack #" + var6 + " (index " + var13.mIndex + "): " + var13);
                     PrintWriter var12 = new PrintWriter(new LogWriter("FragmentManager"));
                     var13.dump("  ", var12, false);
                     var12.close();
                  }

                  this.mBackStack.add(var13);
                  if (var13.mIndex >= 0) {
                     this.setBackStackIndex(var13.mIndex, var13);
                  }
               }
            } else {
               this.mBackStack = null;
            }
         }
      }

   }

   FragmentManagerNonConfig retainNonConfig() {
      ArrayList var1 = null;
      ArrayList var2 = null;
      ArrayList var3 = null;
      ArrayList var4 = null;
      if (this.mActive != null) {
         int var5 = 0;

         while(true) {
            var3 = var4;
            var1 = var2;
            if (var5 >= this.mActive.size()) {
               break;
            }

            Fragment var6 = (Fragment)this.mActive.get(var5);
            var1 = var4;
            ArrayList var7 = var2;
            if (var6 != null) {
               var3 = var2;
               int var8;
               if (var6.mRetainInstance) {
                  var1 = var2;
                  if (var2 == null) {
                     var1 = new ArrayList();
                  }

                  var1.add(var6);
                  var6.mRetaining = true;
                  if (var6.mTarget != null) {
                     var8 = var6.mTarget.mIndex;
                  } else {
                     var8 = -1;
                  }

                  var6.mTargetIndex = var8;
                  var3 = var1;
                  if (DEBUG) {
                     Log.v("FragmentManager", "retainNonConfig: keeping retained " + var6);
                     var3 = var1;
                  }
               }

               boolean var9 = false;
               boolean var12 = var9;
               var2 = var4;
               if (var6.mChildFragmentManager != null) {
                  FragmentManagerNonConfig var10 = var6.mChildFragmentManager.retainNonConfig();
                  var12 = var9;
                  var2 = var4;
                  if (var10 != null) {
                     var2 = var4;
                     if (var4 == null) {
                        var4 = new ArrayList();
                        var8 = 0;

                        while(true) {
                           var2 = var4;
                           if (var8 >= var5) {
                              break;
                           }

                           var4.add((Object)null);
                           ++var8;
                        }
                     }

                     var2.add(var10);
                     var12 = true;
                  }
               }

               var1 = var2;
               var7 = var3;
               if (var2 != null) {
                  var1 = var2;
                  var7 = var3;
                  if (!var12) {
                     var2.add((Object)null);
                     var7 = var3;
                     var1 = var2;
                  }
               }
            }

            ++var5;
            var4 = var1;
            var2 = var7;
         }
      }

      FragmentManagerNonConfig var11;
      if (var1 == null && var3 == null) {
         var11 = null;
      } else {
         var11 = new FragmentManagerNonConfig(var1, var3);
      }

      return var11;
   }

   Parcelable saveAllState() {
      BackStackState[] var1 = null;
      this.forcePostponedTransactions();
      this.endAnimatingAwayFragments();
      this.execPendingActions();
      if (HONEYCOMB) {
         this.mStateSaved = true;
      }

      FragmentManagerState var2 = var1;
      if (this.mActive != null) {
         if (this.mActive.size() <= 0) {
            var2 = var1;
         } else {
            int var3 = this.mActive.size();
            FragmentState[] var4 = new FragmentState[var3];
            boolean var5 = false;

            int var6;
            FragmentState var8;
            for(var6 = 0; var6 < var3; ++var6) {
               Fragment var10 = (Fragment)this.mActive.get(var6);
               if (var10 != null) {
                  if (var10.mIndex < 0) {
                     this.throwException(new IllegalStateException("Failure saving state: active " + var10 + " has cleared index: " + var10.mIndex));
                  }

                  boolean var7 = true;
                  var8 = new FragmentState(var10);
                  var4[var6] = var8;
                  if (var10.mState > 0 && var8.mSavedFragmentState == null) {
                     var8.mSavedFragmentState = this.saveFragmentBasicState(var10);
                     if (var10.mTarget != null) {
                        if (var10.mTarget.mIndex < 0) {
                           this.throwException(new IllegalStateException("Failure saving state: " + var10 + " has target not in fragment manager: " + var10.mTarget));
                        }

                        if (var8.mSavedFragmentState == null) {
                           var8.mSavedFragmentState = new Bundle();
                        }

                        this.putFragment(var8.mSavedFragmentState, "android:target_state", var10.mTarget);
                        if (var10.mTargetRequestCode != 0) {
                           var8.mSavedFragmentState.putInt("android:target_req_state", var10.mTargetRequestCode);
                        }
                     }
                  } else {
                     var8.mSavedFragmentState = var10.mSavedFragmentState;
                  }

                  var5 = var7;
                  if (DEBUG) {
                     Log.v("FragmentManager", "Saved state of " + var10 + ": " + var8.mSavedFragmentState);
                     var5 = var7;
                  }
               }
            }

            if (!var5) {
               var2 = var1;
               if (DEBUG) {
                  Log.v("FragmentManager", "saveAllState: no fragments!");
                  var2 = var1;
               }
            } else {
               var1 = null;
               var8 = null;
               int[] var11 = (int[])var1;
               int var12;
               if (this.mAdded != null) {
                  var12 = this.mAdded.size();
                  var11 = (int[])var1;
                  if (var12 > 0) {
                     int[] var9 = new int[var12];
                     var6 = 0;

                     while(true) {
                        var11 = var9;
                        if (var6 >= var12) {
                           break;
                        }

                        var9[var6] = ((Fragment)this.mAdded.get(var6)).mIndex;
                        if (var9[var6] < 0) {
                           this.throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(var6) + " has cleared index: " + var9[var6]));
                        }

                        if (DEBUG) {
                           Log.v("FragmentManager", "saveAllState: adding fragment #" + var6 + ": " + this.mAdded.get(var6));
                        }

                        ++var6;
                     }
                  }
               }

               var1 = var8;
               if (this.mBackStack != null) {
                  var12 = this.mBackStack.size();
                  var1 = var8;
                  if (var12 > 0) {
                     BackStackState[] var13 = new BackStackState[var12];
                     var6 = 0;

                     while(true) {
                        var1 = var13;
                        if (var6 >= var12) {
                           break;
                        }

                        var13[var6] = new BackStackState((BackStackRecord)this.mBackStack.get(var6));
                        if (DEBUG) {
                           Log.v("FragmentManager", "saveAllState: adding back stack #" + var6 + ": " + this.mBackStack.get(var6));
                        }

                        ++var6;
                     }
                  }
               }

               FragmentManagerState var14 = new FragmentManagerState();
               var14.mActive = var4;
               var14.mAdded = var11;
               var14.mBackStack = var1;
               var2 = var14;
            }
         }
      }

      return var2;
   }

   Bundle saveFragmentBasicState(Fragment var1) {
      Bundle var2 = null;
      if (this.mStateBundle == null) {
         this.mStateBundle = new Bundle();
      }

      var1.performSaveInstanceState(this.mStateBundle);
      this.dispatchOnFragmentSaveInstanceState(var1, this.mStateBundle, false);
      if (!this.mStateBundle.isEmpty()) {
         var2 = this.mStateBundle;
         this.mStateBundle = null;
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
      Object var2 = null;
      if (var1.mIndex < 0) {
         this.throwException(new IllegalStateException("Fragment " + var1 + " is not currently in the FragmentManager"));
      }

      Fragment.SavedState var3 = (Fragment.SavedState)var2;
      if (var1.mState > 0) {
         Bundle var4 = this.saveFragmentBasicState(var1);
         var3 = (Fragment.SavedState)var2;
         if (var4 != null) {
            var3 = new Fragment.SavedState(var4);
         }
      }

      return var3;
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

   public void setBackStackIndex(int var1, BackStackRecord var2) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label1119: {
         ArrayList var3;
         try {
            if (this.mBackStackIndices == null) {
               var3 = new ArrayList();
               this.mBackStackIndices = var3;
            }
         } catch (Throwable var137) {
            var10000 = var137;
            var10001 = false;
            break label1119;
         }

         int var4;
         try {
            var4 = this.mBackStackIndices.size();
         } catch (Throwable var136) {
            var10000 = var136;
            var10001 = false;
            break label1119;
         }

         int var5 = var4;
         StringBuilder var139;
         if (var1 < var4) {
            try {
               if (DEBUG) {
                  var139 = new StringBuilder();
                  Log.v("FragmentManager", var139.append("Setting back stack index ").append(var1).append(" to ").append(var2).toString());
               }
            } catch (Throwable var133) {
               var10000 = var133;
               var10001 = false;
               break label1119;
            }

            try {
               this.mBackStackIndices.set(var1, var2);
            } catch (Throwable var132) {
               var10000 = var132;
               var10001 = false;
               break label1119;
            }
         } else {
            while(true) {
               if (var5 >= var1) {
                  try {
                     if (DEBUG) {
                        var139 = new StringBuilder();
                        Log.v("FragmentManager", var139.append("Adding back stack index ").append(var1).append(" with ").append(var2).toString());
                     }
                  } catch (Throwable var134) {
                     var10000 = var134;
                     var10001 = false;
                     break label1119;
                  }

                  try {
                     this.mBackStackIndices.add(var2);
                     break;
                  } catch (Throwable var129) {
                     var10000 = var129;
                     var10001 = false;
                     break label1119;
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
                  break label1119;
               }

               try {
                  if (DEBUG) {
                     var139 = new StringBuilder();
                     Log.v("FragmentManager", var139.append("Adding available back stack index ").append(var5).toString());
                  }
               } catch (Throwable var135) {
                  var10000 = var135;
                  var10001 = false;
                  break label1119;
               }

               try {
                  this.mAvailBackStackIndices.add(var5);
               } catch (Throwable var130) {
                  var10000 = var130;
                  var10001 = false;
                  break label1119;
               }

               ++var5;
            }
         }

         label1074:
         try {
            return;
         } catch (Throwable var128) {
            var10000 = var128;
            var10001 = false;
            break label1074;
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

   public void showFragment(Fragment var1) {
      boolean var2 = false;
      if (DEBUG) {
         Log.v("FragmentManager", "show: " + var1);
      }

      if (var1.mHidden) {
         var1.mHidden = false;
         if (!var1.mHiddenChanged) {
            var2 = true;
         }

         var1.mHiddenChanged = var2;
      }

   }

   void startPendingDeferredFragments() {
      if (this.mActive != null) {
         for(int var1 = 0; var1 < this.mActive.size(); ++var1) {
            Fragment var2 = (Fragment)this.mActive.get(var1);
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
      if (this.mLifecycleCallbacks != null) {
         CopyOnWriteArrayList var2 = this.mLifecycleCallbacks;
         synchronized(var2){}
         int var3 = 0;

         Throwable var10000;
         boolean var10001;
         label255: {
            int var4;
            try {
               var4 = this.mLifecycleCallbacks.size();
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label255;
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
                  break label255;
               }
            }

            label235:
            try {
               return;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label235;
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
   }

   static class AnimateOnHWLayerIfNeededListener implements AnimationListener {
      private AnimationListener mOriginalListener;
      private boolean mShouldRunOnHWLayer;
      View mView;

      public AnimateOnHWLayerIfNeededListener(View var1, Animation var2) {
         if (var1 != null && var2 != null) {
            this.mView = var1;
         }

      }

      public AnimateOnHWLayerIfNeededListener(View var1, Animation var2, AnimationListener var3) {
         if (var1 != null && var2 != null) {
            this.mOriginalListener = var3;
            this.mView = var1;
            this.mShouldRunOnHWLayer = true;
         }

      }

      @CallSuper
      public void onAnimationEnd(Animation var1) {
         if (this.mView != null && this.mShouldRunOnHWLayer) {
            if (!ViewCompat.isAttachedToWindow(this.mView) && !BuildCompat.isAtLeastN()) {
               ViewCompat.setLayerType(this.mView, 0, (Paint)null);
            } else {
               this.mView.post(new Runnable() {
                  public void run() {
                     ViewCompat.setLayerType(AnimateOnHWLayerIfNeededListener.this.mView, 0, (Paint)null);
                  }
               });
            }
         }

         if (this.mOriginalListener != null) {
            this.mOriginalListener.onAnimationEnd(var1);
         }

      }

      public void onAnimationRepeat(Animation var1) {
         if (this.mOriginalListener != null) {
            this.mOriginalListener.onAnimationRepeat(var1);
         }

      }

      @CallSuper
      public void onAnimationStart(Animation var1) {
         if (this.mOriginalListener != null) {
            this.mOriginalListener.onAnimationStart(var1);
         }

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
         boolean var1 = false;
         boolean var2;
         if (this.mNumPostponed > 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         FragmentManagerImpl var3 = this.mRecord.mManager;
         int var4 = var3.mAdded.size();

         for(int var5 = 0; var5 < var4; ++var5) {
            Fragment var6 = (Fragment)var3.mAdded.get(var5);
            var6.setOnStartEnterTransitionListener((Fragment.OnStartEnterTransitionListener)null);
            if (var2 && var6.isPostponed()) {
               var6.startPostponedEnterTransition();
            }
         }

         FragmentManagerImpl var9 = this.mRecord.mManager;
         BackStackRecord var8 = this.mRecord;
         boolean var7 = this.mIsBack;
         if (!var2) {
            var1 = true;
         }

         var9.completeExecute(var8, var7, var1, true);
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
