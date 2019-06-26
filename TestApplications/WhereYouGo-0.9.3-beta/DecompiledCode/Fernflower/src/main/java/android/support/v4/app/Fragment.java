package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.Animation;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Fragment implements ComponentCallbacks, OnCreateContextMenuListener {
   static final int ACTIVITY_CREATED = 2;
   static final int CREATED = 1;
   static final int INITIALIZING = 0;
   static final int RESUMED = 5;
   static final int STARTED = 4;
   static final int STOPPED = 3;
   static final Object USE_DEFAULT_TRANSITION = new Object();
   private static final SimpleArrayMap sClassMap = new SimpleArrayMap();
   boolean mAdded;
   Fragment.AnimationInfo mAnimationInfo;
   Bundle mArguments;
   int mBackStackNesting;
   boolean mCalled;
   boolean mCheckedForLoaderManager;
   FragmentManagerImpl mChildFragmentManager;
   FragmentManagerNonConfig mChildNonConfig;
   ViewGroup mContainer;
   int mContainerId;
   boolean mDeferStart;
   boolean mDetached;
   int mFragmentId;
   FragmentManagerImpl mFragmentManager;
   boolean mFromLayout;
   boolean mHasMenu;
   boolean mHidden;
   boolean mHiddenChanged;
   FragmentHostCallback mHost;
   boolean mInLayout;
   int mIndex = -1;
   View mInnerView;
   boolean mIsNewlyAdded;
   LoaderManagerImpl mLoaderManager;
   boolean mLoadersStarted;
   boolean mMenuVisible = true;
   Fragment mParentFragment;
   float mPostponedAlpha;
   boolean mRemoving;
   boolean mRestored;
   boolean mRetainInstance;
   boolean mRetaining;
   Bundle mSavedFragmentState;
   SparseArray mSavedViewState;
   int mState = 0;
   String mTag;
   Fragment mTarget;
   int mTargetIndex = -1;
   int mTargetRequestCode;
   boolean mUserVisibleHint = true;
   View mView;
   String mWho;

   private void callStartTransitionListener() {
      Fragment.OnStartEnterTransitionListener var1;
      if (this.mAnimationInfo == null) {
         var1 = null;
      } else {
         this.mAnimationInfo.mEnterTransitionPostponed = false;
         var1 = this.mAnimationInfo.mStartEnterTransitionListener;
         this.mAnimationInfo.mStartEnterTransitionListener = null;
      }

      if (var1 != null) {
         var1.onStartEnterTransition();
      }

   }

   private Fragment.AnimationInfo ensureAnimationInfo() {
      if (this.mAnimationInfo == null) {
         this.mAnimationInfo = new Fragment.AnimationInfo();
      }

      return this.mAnimationInfo;
   }

   public static Fragment instantiate(Context var0, String var1) {
      return instantiate(var0, var1, (Bundle)null);
   }

   public static Fragment instantiate(Context var0, String var1, @Nullable Bundle var2) {
      ClassNotFoundException var22;
      label57: {
         java.lang.InstantiationException var21;
         label56: {
            IllegalAccessException var10000;
            label60: {
               Class var3;
               boolean var10001;
               try {
                  var3 = (Class)sClassMap.get(var1);
               } catch (ClassNotFoundException var14) {
                  var22 = var14;
                  var10001 = false;
                  break label57;
               } catch (java.lang.InstantiationException var15) {
                  var21 = var15;
                  var10001 = false;
                  break label56;
               } catch (IllegalAccessException var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label60;
               }

               Class var4 = var3;
               if (var3 == null) {
                  try {
                     var4 = var0.getClassLoader().loadClass(var1);
                     sClassMap.put(var1, var4);
                  } catch (ClassNotFoundException var11) {
                     var22 = var11;
                     var10001 = false;
                     break label57;
                  } catch (java.lang.InstantiationException var12) {
                     var21 = var12;
                     var10001 = false;
                     break label56;
                  } catch (IllegalAccessException var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label60;
                  }
               }

               Fragment var17;
               try {
                  var17 = (Fragment)var4.newInstance();
               } catch (ClassNotFoundException var8) {
                  var22 = var8;
                  var10001 = false;
                  break label57;
               } catch (java.lang.InstantiationException var9) {
                  var21 = var9;
                  var10001 = false;
                  break label56;
               } catch (IllegalAccessException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label60;
               }

               if (var2 == null) {
                  return var17;
               }

               try {
                  var2.setClassLoader(var17.getClass().getClassLoader());
                  var17.mArguments = var2;
                  return var17;
               } catch (ClassNotFoundException var5) {
                  var22 = var5;
                  var10001 = false;
                  break label57;
               } catch (java.lang.InstantiationException var6) {
                  var21 = var6;
                  var10001 = false;
                  break label56;
               } catch (IllegalAccessException var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }

            IllegalAccessException var18 = var10000;
            throw new Fragment.InstantiationException("Unable to instantiate fragment " + var1 + ": make sure class name exists, is public, and has an" + " empty constructor that is public", var18);
         }

         java.lang.InstantiationException var19 = var21;
         throw new Fragment.InstantiationException("Unable to instantiate fragment " + var1 + ": make sure class name exists, is public, and has an" + " empty constructor that is public", var19);
      }

      ClassNotFoundException var20 = var22;
      throw new Fragment.InstantiationException("Unable to instantiate fragment " + var1 + ": make sure class name exists, is public, and has an" + " empty constructor that is public", var20);
   }

   static boolean isSupportFragmentClass(Context var0, String var1) {
      boolean var4;
      label29: {
         boolean var10001;
         Class var2;
         try {
            var2 = (Class)sClassMap.get(var1);
         } catch (ClassNotFoundException var7) {
            var10001 = false;
            break label29;
         }

         Class var3 = var2;
         if (var2 == null) {
            try {
               var3 = var0.getClassLoader().loadClass(var1);
               sClassMap.put(var1, var3);
            } catch (ClassNotFoundException var6) {
               var10001 = false;
               break label29;
            }
         }

         try {
            var4 = Fragment.class.isAssignableFrom(var3);
            return var4;
         } catch (ClassNotFoundException var5) {
            var10001 = false;
         }
      }

      var4 = false;
      return var4;
   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      var3.print(var1);
      var3.print("mFragmentId=#");
      var3.print(Integer.toHexString(this.mFragmentId));
      var3.print(" mContainerId=#");
      var3.print(Integer.toHexString(this.mContainerId));
      var3.print(" mTag=");
      var3.println(this.mTag);
      var3.print(var1);
      var3.print("mState=");
      var3.print(this.mState);
      var3.print(" mIndex=");
      var3.print(this.mIndex);
      var3.print(" mWho=");
      var3.print(this.mWho);
      var3.print(" mBackStackNesting=");
      var3.println(this.mBackStackNesting);
      var3.print(var1);
      var3.print("mAdded=");
      var3.print(this.mAdded);
      var3.print(" mRemoving=");
      var3.print(this.mRemoving);
      var3.print(" mFromLayout=");
      var3.print(this.mFromLayout);
      var3.print(" mInLayout=");
      var3.println(this.mInLayout);
      var3.print(var1);
      var3.print("mHidden=");
      var3.print(this.mHidden);
      var3.print(" mDetached=");
      var3.print(this.mDetached);
      var3.print(" mMenuVisible=");
      var3.print(this.mMenuVisible);
      var3.print(" mHasMenu=");
      var3.println(this.mHasMenu);
      var3.print(var1);
      var3.print("mRetainInstance=");
      var3.print(this.mRetainInstance);
      var3.print(" mRetaining=");
      var3.print(this.mRetaining);
      var3.print(" mUserVisibleHint=");
      var3.println(this.mUserVisibleHint);
      if (this.mFragmentManager != null) {
         var3.print(var1);
         var3.print("mFragmentManager=");
         var3.println(this.mFragmentManager);
      }

      if (this.mHost != null) {
         var3.print(var1);
         var3.print("mHost=");
         var3.println(this.mHost);
      }

      if (this.mParentFragment != null) {
         var3.print(var1);
         var3.print("mParentFragment=");
         var3.println(this.mParentFragment);
      }

      if (this.mArguments != null) {
         var3.print(var1);
         var3.print("mArguments=");
         var3.println(this.mArguments);
      }

      if (this.mSavedFragmentState != null) {
         var3.print(var1);
         var3.print("mSavedFragmentState=");
         var3.println(this.mSavedFragmentState);
      }

      if (this.mSavedViewState != null) {
         var3.print(var1);
         var3.print("mSavedViewState=");
         var3.println(this.mSavedViewState);
      }

      if (this.mTarget != null) {
         var3.print(var1);
         var3.print("mTarget=");
         var3.print(this.mTarget);
         var3.print(" mTargetRequestCode=");
         var3.println(this.mTargetRequestCode);
      }

      if (this.getNextAnim() != 0) {
         var3.print(var1);
         var3.print("mNextAnim=");
         var3.println(this.getNextAnim());
      }

      if (this.mContainer != null) {
         var3.print(var1);
         var3.print("mContainer=");
         var3.println(this.mContainer);
      }

      if (this.mView != null) {
         var3.print(var1);
         var3.print("mView=");
         var3.println(this.mView);
      }

      if (this.mInnerView != null) {
         var3.print(var1);
         var3.print("mInnerView=");
         var3.println(this.mView);
      }

      if (this.getAnimatingAway() != null) {
         var3.print(var1);
         var3.print("mAnimatingAway=");
         var3.println(this.getAnimatingAway());
         var3.print(var1);
         var3.print("mStateAfterAnimating=");
         var3.println(this.getStateAfterAnimating());
      }

      if (this.mLoaderManager != null) {
         var3.print(var1);
         var3.println("Loader Manager:");
         this.mLoaderManager.dump(var1 + "  ", var2, var3, var4);
      }

      if (this.mChildFragmentManager != null) {
         var3.print(var1);
         var3.println("Child " + this.mChildFragmentManager + ":");
         this.mChildFragmentManager.dump(var1 + "  ", var2, var3, var4);
      }

   }

   public final boolean equals(Object var1) {
      return super.equals(var1);
   }

   Fragment findFragmentByWho(String var1) {
      Fragment var2;
      if (var1.equals(this.mWho)) {
         var2 = this;
      } else if (this.mChildFragmentManager != null) {
         var2 = this.mChildFragmentManager.findFragmentByWho(var1);
      } else {
         var2 = null;
      }

      return var2;
   }

   public final FragmentActivity getActivity() {
      FragmentActivity var1;
      if (this.mHost == null) {
         var1 = null;
      } else {
         var1 = (FragmentActivity)this.mHost.getActivity();
      }

      return var1;
   }

   public boolean getAllowEnterTransitionOverlap() {
      boolean var1;
      if (this.mAnimationInfo != null && this.mAnimationInfo.mAllowEnterTransitionOverlap != null) {
         var1 = this.mAnimationInfo.mAllowEnterTransitionOverlap;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean getAllowReturnTransitionOverlap() {
      boolean var1;
      if (this.mAnimationInfo != null && this.mAnimationInfo.mAllowReturnTransitionOverlap != null) {
         var1 = this.mAnimationInfo.mAllowReturnTransitionOverlap;
      } else {
         var1 = true;
      }

      return var1;
   }

   View getAnimatingAway() {
      View var1;
      if (this.mAnimationInfo == null) {
         var1 = null;
      } else {
         var1 = this.mAnimationInfo.mAnimatingAway;
      }

      return var1;
   }

   public final Bundle getArguments() {
      return this.mArguments;
   }

   public final FragmentManager getChildFragmentManager() {
      if (this.mChildFragmentManager == null) {
         this.instantiateChildFragmentManager();
         if (this.mState >= 5) {
            this.mChildFragmentManager.dispatchResume();
         } else if (this.mState >= 4) {
            this.mChildFragmentManager.dispatchStart();
         } else if (this.mState >= 2) {
            this.mChildFragmentManager.dispatchActivityCreated();
         } else if (this.mState >= 1) {
            this.mChildFragmentManager.dispatchCreate();
         }
      }

      return this.mChildFragmentManager;
   }

   public Context getContext() {
      Context var1;
      if (this.mHost == null) {
         var1 = null;
      } else {
         var1 = this.mHost.getContext();
      }

      return var1;
   }

   public Object getEnterTransition() {
      Object var1;
      if (this.mAnimationInfo == null) {
         var1 = null;
      } else {
         var1 = this.mAnimationInfo.mEnterTransition;
      }

      return var1;
   }

   SharedElementCallback getEnterTransitionCallback() {
      SharedElementCallback var1;
      if (this.mAnimationInfo == null) {
         var1 = null;
      } else {
         var1 = this.mAnimationInfo.mEnterTransitionCallback;
      }

      return var1;
   }

   public Object getExitTransition() {
      Object var1;
      if (this.mAnimationInfo == null) {
         var1 = null;
      } else {
         var1 = this.mAnimationInfo.mExitTransition;
      }

      return var1;
   }

   SharedElementCallback getExitTransitionCallback() {
      SharedElementCallback var1;
      if (this.mAnimationInfo == null) {
         var1 = null;
      } else {
         var1 = this.mAnimationInfo.mExitTransitionCallback;
      }

      return var1;
   }

   public final FragmentManager getFragmentManager() {
      return this.mFragmentManager;
   }

   public final Object getHost() {
      Object var1;
      if (this.mHost == null) {
         var1 = null;
      } else {
         var1 = this.mHost.onGetHost();
      }

      return var1;
   }

   public final int getId() {
      return this.mFragmentId;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public LayoutInflater getLayoutInflater(Bundle var1) {
      LayoutInflater var2 = this.mHost.onGetLayoutInflater();
      this.getChildFragmentManager();
      LayoutInflaterCompat.setFactory(var2, this.mChildFragmentManager.getLayoutInflaterFactory());
      return var2;
   }

   public LoaderManager getLoaderManager() {
      LoaderManagerImpl var1;
      if (this.mLoaderManager != null) {
         var1 = this.mLoaderManager;
      } else {
         if (this.mHost == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
         }

         this.mCheckedForLoaderManager = true;
         this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, true);
         var1 = this.mLoaderManager;
      }

      return var1;
   }

   int getNextAnim() {
      int var1;
      if (this.mAnimationInfo == null) {
         var1 = 0;
      } else {
         var1 = this.mAnimationInfo.mNextAnim;
      }

      return var1;
   }

   int getNextTransition() {
      int var1;
      if (this.mAnimationInfo == null) {
         var1 = 0;
      } else {
         var1 = this.mAnimationInfo.mNextTransition;
      }

      return var1;
   }

   int getNextTransitionStyle() {
      int var1;
      if (this.mAnimationInfo == null) {
         var1 = 0;
      } else {
         var1 = this.mAnimationInfo.mNextTransitionStyle;
      }

      return var1;
   }

   public final Fragment getParentFragment() {
      return this.mParentFragment;
   }

   public Object getReenterTransition() {
      Object var1;
      if (this.mAnimationInfo == null) {
         var1 = null;
      } else if (this.mAnimationInfo.mReenterTransition == USE_DEFAULT_TRANSITION) {
         var1 = this.getExitTransition();
      } else {
         var1 = this.mAnimationInfo.mReenterTransition;
      }

      return var1;
   }

   public final Resources getResources() {
      if (this.mHost == null) {
         throw new IllegalStateException("Fragment " + this + " not attached to Activity");
      } else {
         return this.mHost.getContext().getResources();
      }
   }

   public final boolean getRetainInstance() {
      return this.mRetainInstance;
   }

   public Object getReturnTransition() {
      Object var1;
      if (this.mAnimationInfo == null) {
         var1 = null;
      } else if (this.mAnimationInfo.mReturnTransition == USE_DEFAULT_TRANSITION) {
         var1 = this.getEnterTransition();
      } else {
         var1 = this.mAnimationInfo.mReturnTransition;
      }

      return var1;
   }

   public Object getSharedElementEnterTransition() {
      Object var1;
      if (this.mAnimationInfo == null) {
         var1 = null;
      } else {
         var1 = this.mAnimationInfo.mSharedElementEnterTransition;
      }

      return var1;
   }

   public Object getSharedElementReturnTransition() {
      Object var1;
      if (this.mAnimationInfo == null) {
         var1 = null;
      } else if (this.mAnimationInfo.mSharedElementReturnTransition == USE_DEFAULT_TRANSITION) {
         var1 = this.getSharedElementEnterTransition();
      } else {
         var1 = this.mAnimationInfo.mSharedElementReturnTransition;
      }

      return var1;
   }

   int getStateAfterAnimating() {
      int var1;
      if (this.mAnimationInfo == null) {
         var1 = 0;
      } else {
         var1 = this.mAnimationInfo.mStateAfterAnimating;
      }

      return var1;
   }

   public final String getString(@StringRes int var1) {
      return this.getResources().getString(var1);
   }

   public final String getString(@StringRes int var1, Object... var2) {
      return this.getResources().getString(var1, var2);
   }

   public final String getTag() {
      return this.mTag;
   }

   public final Fragment getTargetFragment() {
      return this.mTarget;
   }

   public final int getTargetRequestCode() {
      return this.mTargetRequestCode;
   }

   public final CharSequence getText(@StringRes int var1) {
      return this.getResources().getText(var1);
   }

   public boolean getUserVisibleHint() {
      return this.mUserVisibleHint;
   }

   @Nullable
   public View getView() {
      return this.mView;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public final boolean hasOptionsMenu() {
      return this.mHasMenu;
   }

   public final int hashCode() {
      return super.hashCode();
   }

   void initState() {
      this.mIndex = -1;
      this.mWho = null;
      this.mAdded = false;
      this.mRemoving = false;
      this.mFromLayout = false;
      this.mInLayout = false;
      this.mRestored = false;
      this.mBackStackNesting = 0;
      this.mFragmentManager = null;
      this.mChildFragmentManager = null;
      this.mHost = null;
      this.mFragmentId = 0;
      this.mContainerId = 0;
      this.mTag = null;
      this.mHidden = false;
      this.mDetached = false;
      this.mRetaining = false;
      this.mLoaderManager = null;
      this.mLoadersStarted = false;
      this.mCheckedForLoaderManager = false;
   }

   void instantiateChildFragmentManager() {
      if (this.mHost == null) {
         throw new IllegalStateException("Fragment has not been attached yet.");
      } else {
         this.mChildFragmentManager = new FragmentManagerImpl();
         this.mChildFragmentManager.attachController(this.mHost, new FragmentContainer() {
            @Nullable
            public View onFindViewById(int var1) {
               if (Fragment.this.mView == null) {
                  throw new IllegalStateException("Fragment does not have a view");
               } else {
                  return Fragment.this.mView.findViewById(var1);
               }
            }

            public boolean onHasView() {
               boolean var1;
               if (Fragment.this.mView != null) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               return var1;
            }
         }, this);
      }
   }

   public final boolean isAdded() {
      boolean var1;
      if (this.mHost != null && this.mAdded) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final boolean isDetached() {
      return this.mDetached;
   }

   public final boolean isHidden() {
      return this.mHidden;
   }

   boolean isHideReplaced() {
      boolean var1;
      if (this.mAnimationInfo == null) {
         var1 = false;
      } else {
         var1 = this.mAnimationInfo.mIsHideReplaced;
      }

      return var1;
   }

   final boolean isInBackStack() {
      boolean var1;
      if (this.mBackStackNesting > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final boolean isInLayout() {
      return this.mInLayout;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public final boolean isMenuVisible() {
      return this.mMenuVisible;
   }

   boolean isPostponed() {
      boolean var1;
      if (this.mAnimationInfo == null) {
         var1 = false;
      } else {
         var1 = this.mAnimationInfo.mEnterTransitionPostponed;
      }

      return var1;
   }

   public final boolean isRemoving() {
      return this.mRemoving;
   }

   public final boolean isResumed() {
      boolean var1;
      if (this.mState >= 5) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final boolean isVisible() {
      boolean var1;
      if (this.isAdded() && !this.isHidden() && this.mView != null && this.mView.getWindowToken() != null && this.mView.getVisibility() == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @CallSuper
   public void onActivityCreated(@Nullable Bundle var1) {
      this.mCalled = true;
   }

   public void onActivityResult(int var1, int var2, Intent var3) {
   }

   @Deprecated
   @CallSuper
   public void onAttach(Activity var1) {
      this.mCalled = true;
   }

   @CallSuper
   public void onAttach(Context var1) {
      this.mCalled = true;
      Activity var2;
      if (this.mHost == null) {
         var2 = null;
      } else {
         var2 = this.mHost.getActivity();
      }

      if (var2 != null) {
         this.mCalled = false;
         this.onAttach(var2);
      }

   }

   public void onAttachFragment(Fragment var1) {
   }

   @CallSuper
   public void onConfigurationChanged(Configuration var1) {
      this.mCalled = true;
   }

   public boolean onContextItemSelected(MenuItem var1) {
      return false;
   }

   @CallSuper
   public void onCreate(@Nullable Bundle var1) {
      this.mCalled = true;
      this.restoreChildFragmentState(var1);
      if (this.mChildFragmentManager != null && !this.mChildFragmentManager.isStateAtLeast(1)) {
         this.mChildFragmentManager.dispatchCreate();
      }

   }

   public Animation onCreateAnimation(int var1, boolean var2, int var3) {
      return null;
   }

   public void onCreateContextMenu(ContextMenu var1, View var2, ContextMenuInfo var3) {
      this.getActivity().onCreateContextMenu(var1, var2, var3);
   }

   public void onCreateOptionsMenu(Menu var1, MenuInflater var2) {
   }

   @Nullable
   public View onCreateView(LayoutInflater var1, @Nullable ViewGroup var2, @Nullable Bundle var3) {
      return null;
   }

   @CallSuper
   public void onDestroy() {
      this.mCalled = true;
      if (!this.mCheckedForLoaderManager) {
         this.mCheckedForLoaderManager = true;
         this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
      }

      if (this.mLoaderManager != null) {
         this.mLoaderManager.doDestroy();
      }

   }

   public void onDestroyOptionsMenu() {
   }

   @CallSuper
   public void onDestroyView() {
      this.mCalled = true;
   }

   @CallSuper
   public void onDetach() {
      this.mCalled = true;
   }

   public void onHiddenChanged(boolean var1) {
   }

   @Deprecated
   @CallSuper
   public void onInflate(Activity var1, AttributeSet var2, Bundle var3) {
      this.mCalled = true;
   }

   @CallSuper
   public void onInflate(Context var1, AttributeSet var2, Bundle var3) {
      this.mCalled = true;
      Activity var4;
      if (this.mHost == null) {
         var4 = null;
      } else {
         var4 = this.mHost.getActivity();
      }

      if (var4 != null) {
         this.mCalled = false;
         this.onInflate(var4, var2, var3);
      }

   }

   @CallSuper
   public void onLowMemory() {
      this.mCalled = true;
   }

   public void onMultiWindowModeChanged(boolean var1) {
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      return false;
   }

   public void onOptionsMenuClosed(Menu var1) {
   }

   @CallSuper
   public void onPause() {
      this.mCalled = true;
   }

   public void onPictureInPictureModeChanged(boolean var1) {
   }

   public void onPrepareOptionsMenu(Menu var1) {
   }

   public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
   }

   @CallSuper
   public void onResume() {
      this.mCalled = true;
   }

   public void onSaveInstanceState(Bundle var1) {
   }

   @CallSuper
   public void onStart() {
      this.mCalled = true;
      if (!this.mLoadersStarted) {
         this.mLoadersStarted = true;
         if (!this.mCheckedForLoaderManager) {
            this.mCheckedForLoaderManager = true;
            this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
         }

         if (this.mLoaderManager != null) {
            this.mLoaderManager.doStart();
         }
      }

   }

   @CallSuper
   public void onStop() {
      this.mCalled = true;
   }

   public void onViewCreated(View var1, @Nullable Bundle var2) {
   }

   @CallSuper
   public void onViewStateRestored(@Nullable Bundle var1) {
      this.mCalled = true;
   }

   FragmentManager peekChildFragmentManager() {
      return this.mChildFragmentManager;
   }

   void performActivityCreated(Bundle var1) {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.noteStateNotSaved();
      }

      this.mState = 2;
      this.mCalled = false;
      this.onActivityCreated(var1);
      if (!this.mCalled) {
         throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onActivityCreated()");
      } else {
         if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchActivityCreated();
         }

      }
   }

   void performConfigurationChanged(Configuration var1) {
      this.onConfigurationChanged(var1);
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchConfigurationChanged(var1);
      }

   }

   boolean performContextItemSelected(MenuItem var1) {
      boolean var2 = true;
      if (this.mHidden || !this.onContextItemSelected(var1) && (this.mChildFragmentManager == null || !this.mChildFragmentManager.dispatchContextItemSelected(var1))) {
         var2 = false;
      }

      return var2;
   }

   void performCreate(Bundle var1) {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.noteStateNotSaved();
      }

      this.mState = 1;
      this.mCalled = false;
      this.onCreate(var1);
      if (!this.mCalled) {
         throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onCreate()");
      }
   }

   boolean performCreateOptionsMenu(Menu var1, MenuInflater var2) {
      boolean var3 = false;
      boolean var4 = false;
      if (!this.mHidden) {
         boolean var5 = var4;
         if (this.mHasMenu) {
            var5 = var4;
            if (this.mMenuVisible) {
               var5 = true;
               this.onCreateOptionsMenu(var1, var2);
            }
         }

         var3 = var5;
         if (this.mChildFragmentManager != null) {
            var3 = var5 | this.mChildFragmentManager.dispatchCreateOptionsMenu(var1, var2);
         }
      }

      return var3;
   }

   View performCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.noteStateNotSaved();
      }

      return this.onCreateView(var1, var2, var3);
   }

   void performDestroy() {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchDestroy();
      }

      this.mState = 0;
      this.mCalled = false;
      this.onDestroy();
      if (!this.mCalled) {
         throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDestroy()");
      } else {
         this.mChildFragmentManager = null;
      }
   }

   void performDestroyView() {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchDestroyView();
      }

      this.mState = 1;
      this.mCalled = false;
      this.onDestroyView();
      if (!this.mCalled) {
         throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDestroyView()");
      } else {
         if (this.mLoaderManager != null) {
            this.mLoaderManager.doReportNextStart();
         }

      }
   }

   void performDetach() {
      this.mCalled = false;
      this.onDetach();
      if (!this.mCalled) {
         throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDetach()");
      } else {
         if (this.mChildFragmentManager != null) {
            if (!this.mRetaining) {
               throw new IllegalStateException("Child FragmentManager of " + this + " was not " + " destroyed and this fragment is not retaining instance");
            }

            this.mChildFragmentManager.dispatchDestroy();
            this.mChildFragmentManager = null;
         }

      }
   }

   void performLowMemory() {
      this.onLowMemory();
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchLowMemory();
      }

   }

   void performMultiWindowModeChanged(boolean var1) {
      this.onMultiWindowModeChanged(var1);
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchMultiWindowModeChanged(var1);
      }

   }

   boolean performOptionsItemSelected(MenuItem var1) {
      boolean var2 = true;
      if (this.mHidden || (!this.mHasMenu || !this.mMenuVisible || !this.onOptionsItemSelected(var1)) && (this.mChildFragmentManager == null || !this.mChildFragmentManager.dispatchOptionsItemSelected(var1))) {
         var2 = false;
      }

      return var2;
   }

   void performOptionsMenuClosed(Menu var1) {
      if (!this.mHidden) {
         if (this.mHasMenu && this.mMenuVisible) {
            this.onOptionsMenuClosed(var1);
         }

         if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchOptionsMenuClosed(var1);
         }
      }

   }

   void performPause() {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchPause();
      }

      this.mState = 4;
      this.mCalled = false;
      this.onPause();
      if (!this.mCalled) {
         throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onPause()");
      }
   }

   void performPictureInPictureModeChanged(boolean var1) {
      this.onPictureInPictureModeChanged(var1);
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchPictureInPictureModeChanged(var1);
      }

   }

   boolean performPrepareOptionsMenu(Menu var1) {
      boolean var2 = false;
      boolean var3 = false;
      if (!this.mHidden) {
         boolean var4 = var3;
         if (this.mHasMenu) {
            var4 = var3;
            if (this.mMenuVisible) {
               var4 = true;
               this.onPrepareOptionsMenu(var1);
            }
         }

         var2 = var4;
         if (this.mChildFragmentManager != null) {
            var2 = var4 | this.mChildFragmentManager.dispatchPrepareOptionsMenu(var1);
         }
      }

      return var2;
   }

   void performReallyStop() {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchReallyStop();
      }

      this.mState = 2;
      if (this.mLoadersStarted) {
         this.mLoadersStarted = false;
         if (!this.mCheckedForLoaderManager) {
            this.mCheckedForLoaderManager = true;
            this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
         }

         if (this.mLoaderManager != null) {
            if (this.mHost.getRetainLoaders()) {
               this.mLoaderManager.doRetain();
            } else {
               this.mLoaderManager.doStop();
            }
         }
      }

   }

   void performResume() {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.noteStateNotSaved();
         this.mChildFragmentManager.execPendingActions();
      }

      this.mState = 5;
      this.mCalled = false;
      this.onResume();
      if (!this.mCalled) {
         throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onResume()");
      } else {
         if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchResume();
            this.mChildFragmentManager.execPendingActions();
         }

      }
   }

   void performSaveInstanceState(Bundle var1) {
      this.onSaveInstanceState(var1);
      if (this.mChildFragmentManager != null) {
         Parcelable var2 = this.mChildFragmentManager.saveAllState();
         if (var2 != null) {
            var1.putParcelable("android:support:fragments", var2);
         }
      }

   }

   void performStart() {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.noteStateNotSaved();
         this.mChildFragmentManager.execPendingActions();
      }

      this.mState = 4;
      this.mCalled = false;
      this.onStart();
      if (!this.mCalled) {
         throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onStart()");
      } else {
         if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchStart();
         }

         if (this.mLoaderManager != null) {
            this.mLoaderManager.doReportStart();
         }

      }
   }

   void performStop() {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchStop();
      }

      this.mState = 3;
      this.mCalled = false;
      this.onStop();
      if (!this.mCalled) {
         throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onStop()");
      }
   }

   public void postponeEnterTransition() {
      this.ensureAnimationInfo().mEnterTransitionPostponed = true;
   }

   public void registerForContextMenu(View var1) {
      var1.setOnCreateContextMenuListener(this);
   }

   public final void requestPermissions(@NonNull String[] var1, int var2) {
      if (this.mHost == null) {
         throw new IllegalStateException("Fragment " + this + " not attached to Activity");
      } else {
         this.mHost.onRequestPermissionsFromFragment(this, var1, var2);
      }
   }

   void restoreChildFragmentState(@Nullable Bundle var1) {
      if (var1 != null) {
         Parcelable var2 = var1.getParcelable("android:support:fragments");
         if (var2 != null) {
            if (this.mChildFragmentManager == null) {
               this.instantiateChildFragmentManager();
            }

            this.mChildFragmentManager.restoreAllState(var2, this.mChildNonConfig);
            this.mChildNonConfig = null;
            this.mChildFragmentManager.dispatchCreate();
         }
      }

   }

   final void restoreViewState(Bundle var1) {
      if (this.mSavedViewState != null) {
         this.mInnerView.restoreHierarchyState(this.mSavedViewState);
         this.mSavedViewState = null;
      }

      this.mCalled = false;
      this.onViewStateRestored(var1);
      if (!this.mCalled) {
         throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onViewStateRestored()");
      }
   }

   public void setAllowEnterTransitionOverlap(boolean var1) {
      this.ensureAnimationInfo().mAllowEnterTransitionOverlap = var1;
   }

   public void setAllowReturnTransitionOverlap(boolean var1) {
      this.ensureAnimationInfo().mAllowReturnTransitionOverlap = var1;
   }

   void setAnimatingAway(View var1) {
      this.ensureAnimationInfo().mAnimatingAway = var1;
   }

   public void setArguments(Bundle var1) {
      if (this.mIndex >= 0) {
         throw new IllegalStateException("Fragment already active");
      } else {
         this.mArguments = var1;
      }
   }

   public void setEnterSharedElementCallback(SharedElementCallback var1) {
      this.ensureAnimationInfo().mEnterTransitionCallback = var1;
   }

   public void setEnterTransition(Object var1) {
      this.ensureAnimationInfo().mEnterTransition = var1;
   }

   public void setExitSharedElementCallback(SharedElementCallback var1) {
      this.ensureAnimationInfo().mExitTransitionCallback = var1;
   }

   public void setExitTransition(Object var1) {
      this.ensureAnimationInfo().mExitTransition = var1;
   }

   public void setHasOptionsMenu(boolean var1) {
      if (this.mHasMenu != var1) {
         this.mHasMenu = var1;
         if (this.isAdded() && !this.isHidden()) {
            this.mHost.onSupportInvalidateOptionsMenu();
         }
      }

   }

   void setHideReplaced(boolean var1) {
      this.ensureAnimationInfo().mIsHideReplaced = var1;
   }

   final void setIndex(int var1, Fragment var2) {
      this.mIndex = var1;
      if (var2 != null) {
         this.mWho = var2.mWho + ":" + this.mIndex;
      } else {
         this.mWho = "android:fragment:" + this.mIndex;
      }

   }

   public void setInitialSavedState(Fragment.SavedState var1) {
      if (this.mIndex >= 0) {
         throw new IllegalStateException("Fragment already active");
      } else {
         Bundle var2;
         if (var1 != null && var1.mState != null) {
            var2 = var1.mState;
         } else {
            var2 = null;
         }

         this.mSavedFragmentState = var2;
      }
   }

   public void setMenuVisibility(boolean var1) {
      if (this.mMenuVisible != var1) {
         this.mMenuVisible = var1;
         if (this.mHasMenu && this.isAdded() && !this.isHidden()) {
            this.mHost.onSupportInvalidateOptionsMenu();
         }
      }

   }

   void setNextAnim(int var1) {
      if (this.mAnimationInfo != null || var1 != 0) {
         this.ensureAnimationInfo().mNextAnim = var1;
      }

   }

   void setNextTransition(int var1, int var2) {
      if (this.mAnimationInfo != null || var1 != 0 || var2 != 0) {
         this.ensureAnimationInfo();
         this.mAnimationInfo.mNextTransition = var1;
         this.mAnimationInfo.mNextTransitionStyle = var2;
      }

   }

   void setOnStartEnterTransitionListener(Fragment.OnStartEnterTransitionListener var1) {
      this.ensureAnimationInfo();
      if (var1 != this.mAnimationInfo.mStartEnterTransitionListener) {
         if (var1 != null && this.mAnimationInfo.mStartEnterTransitionListener != null) {
            throw new IllegalStateException("Trying to set a replacement startPostponedEnterTransition on " + this);
         }

         if (this.mAnimationInfo.mEnterTransitionPostponed) {
            this.mAnimationInfo.mStartEnterTransitionListener = var1;
         }

         if (var1 != null) {
            var1.startListening();
         }
      }

   }

   public void setReenterTransition(Object var1) {
      this.ensureAnimationInfo().mReenterTransition = var1;
   }

   public void setRetainInstance(boolean var1) {
      this.mRetainInstance = var1;
   }

   public void setReturnTransition(Object var1) {
      this.ensureAnimationInfo().mReturnTransition = var1;
   }

   public void setSharedElementEnterTransition(Object var1) {
      this.ensureAnimationInfo().mSharedElementEnterTransition = var1;
   }

   public void setSharedElementReturnTransition(Object var1) {
      this.ensureAnimationInfo().mSharedElementReturnTransition = var1;
   }

   void setStateAfterAnimating(int var1) {
      this.ensureAnimationInfo().mStateAfterAnimating = var1;
   }

   public void setTargetFragment(Fragment var1, int var2) {
      this.mTarget = var1;
      this.mTargetRequestCode = var2;
   }

   public void setUserVisibleHint(boolean var1) {
      if (!this.mUserVisibleHint && var1 && this.mState < 4 && this.mFragmentManager != null && this.isAdded()) {
         this.mFragmentManager.performPendingDeferredStart(this);
      }

      this.mUserVisibleHint = var1;
      if (this.mState < 4 && !var1) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.mDeferStart = var1;
   }

   public boolean shouldShowRequestPermissionRationale(@NonNull String var1) {
      boolean var2;
      if (this.mHost != null) {
         var2 = this.mHost.onShouldShowRequestPermissionRationale(var1);
      } else {
         var2 = false;
      }

      return var2;
   }

   public void startActivity(Intent var1) {
      this.startActivity(var1, (Bundle)null);
   }

   public void startActivity(Intent var1, @Nullable Bundle var2) {
      if (this.mHost == null) {
         throw new IllegalStateException("Fragment " + this + " not attached to Activity");
      } else {
         this.mHost.onStartActivityFromFragment(this, var1, -1, var2);
      }
   }

   public void startActivityForResult(Intent var1, int var2) {
      this.startActivityForResult(var1, var2, (Bundle)null);
   }

   public void startActivityForResult(Intent var1, int var2, @Nullable Bundle var3) {
      if (this.mHost == null) {
         throw new IllegalStateException("Fragment " + this + " not attached to Activity");
      } else {
         this.mHost.onStartActivityFromFragment(this, var1, var2, var3);
      }
   }

   public void startIntentSenderForResult(IntentSender var1, int var2, @Nullable Intent var3, int var4, int var5, int var6, Bundle var7) throws SendIntentException {
      if (this.mHost == null) {
         throw new IllegalStateException("Fragment " + this + " not attached to Activity");
      } else {
         this.mHost.onStartIntentSenderFromFragment(this, var1, var2, var3, var4, var5, var6, var7);
      }
   }

   public void startPostponedEnterTransition() {
      if (this.mFragmentManager != null && this.mFragmentManager.mHost != null) {
         if (Looper.myLooper() != this.mFragmentManager.mHost.getHandler().getLooper()) {
            this.mFragmentManager.mHost.getHandler().postAtFrontOfQueue(new Runnable() {
               public void run() {
                  Fragment.this.callStartTransitionListener();
               }
            });
         } else {
            this.callStartTransitionListener();
         }
      } else {
         this.ensureAnimationInfo().mEnterTransitionPostponed = false;
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(128);
      DebugUtils.buildShortClassTag(this, var1);
      if (this.mIndex >= 0) {
         var1.append(" #");
         var1.append(this.mIndex);
      }

      if (this.mFragmentId != 0) {
         var1.append(" id=0x");
         var1.append(Integer.toHexString(this.mFragmentId));
      }

      if (this.mTag != null) {
         var1.append(" ");
         var1.append(this.mTag);
      }

      var1.append('}');
      return var1.toString();
   }

   public void unregisterForContextMenu(View var1) {
      var1.setOnCreateContextMenuListener((OnCreateContextMenuListener)null);
   }

   static class AnimationInfo {
      private Boolean mAllowEnterTransitionOverlap;
      private Boolean mAllowReturnTransitionOverlap;
      View mAnimatingAway;
      private Object mEnterTransition = null;
      SharedElementCallback mEnterTransitionCallback;
      boolean mEnterTransitionPostponed;
      private Object mExitTransition;
      SharedElementCallback mExitTransitionCallback;
      boolean mIsHideReplaced;
      int mNextAnim;
      int mNextTransition;
      int mNextTransitionStyle;
      private Object mReenterTransition;
      private Object mReturnTransition;
      private Object mSharedElementEnterTransition;
      private Object mSharedElementReturnTransition;
      Fragment.OnStartEnterTransitionListener mStartEnterTransitionListener;
      int mStateAfterAnimating;

      AnimationInfo() {
         this.mReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
         this.mExitTransition = null;
         this.mReenterTransition = Fragment.USE_DEFAULT_TRANSITION;
         this.mSharedElementEnterTransition = null;
         this.mSharedElementReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
         this.mEnterTransitionCallback = null;
         this.mExitTransitionCallback = null;
      }
   }

   public static class InstantiationException extends RuntimeException {
      public InstantiationException(String var1, Exception var2) {
         super(var1, var2);
      }
   }

   interface OnStartEnterTransitionListener {
      void onStartEnterTransition();

      void startListening();
   }

   public static class SavedState implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public Fragment.SavedState createFromParcel(Parcel var1) {
            return new Fragment.SavedState(var1, (ClassLoader)null);
         }

         public Fragment.SavedState[] newArray(int var1) {
            return new Fragment.SavedState[var1];
         }
      };
      final Bundle mState;

      SavedState(Bundle var1) {
         this.mState = var1;
      }

      SavedState(Parcel var1, ClassLoader var2) {
         this.mState = var1.readBundle();
         if (var2 != null && this.mState != null) {
            this.mState.setClassLoader(var2);
         }

      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeBundle(this.mState);
      }
   }
}
