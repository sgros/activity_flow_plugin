package android.support.v4.app;

import android.animation.Animator;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
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
import java.lang.reflect.InvocationTargetException;

public class Fragment implements LifecycleOwner, ViewModelStoreOwner, ComponentCallbacks, OnCreateContextMenuListener {
   static final Object USE_DEFAULT_TRANSITION = new Object();
   private static final SimpleArrayMap sClassMap = new SimpleArrayMap();
   boolean mAdded;
   Fragment.AnimationInfo mAnimationInfo;
   Bundle mArguments;
   int mBackStackNesting;
   boolean mCalled;
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
   boolean mIsCreated;
   boolean mIsNewlyAdded;
   LayoutInflater mLayoutInflater;
   LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
   boolean mMenuVisible = true;
   Fragment mParentFragment;
   boolean mPerformedCreateView;
   float mPostponedAlpha;
   boolean mRemoving;
   boolean mRestored;
   boolean mRetainInstance;
   boolean mRetaining;
   Bundle mSavedFragmentState;
   Boolean mSavedUserVisibleHint;
   SparseArray mSavedViewState;
   int mState = 0;
   String mTag;
   Fragment mTarget;
   int mTargetIndex = -1;
   int mTargetRequestCode;
   boolean mUserVisibleHint = true;
   View mView;
   LifecycleOwner mViewLifecycleOwner;
   MutableLiveData mViewLifecycleOwnerLiveData = new MutableLiveData();
   LifecycleRegistry mViewLifecycleRegistry;
   ViewModelStore mViewModelStore;
   String mWho;

   private Fragment.AnimationInfo ensureAnimationInfo() {
      if (this.mAnimationInfo == null) {
         this.mAnimationInfo = new Fragment.AnimationInfo();
      }

      return this.mAnimationInfo;
   }

   public static Fragment instantiate(Context var0, String var1, Bundle var2) {
      NoSuchMethodException var33;
      label79: {
         InvocationTargetException var10000;
         StringBuilder var26;
         label78: {
            ClassNotFoundException var36;
            label77: {
               java.lang.InstantiationException var35;
               label76: {
                  IllegalAccessException var34;
                  label82: {
                     Class var3;
                     boolean var10001;
                     try {
                        var3 = (Class)sClassMap.get(var1);
                     } catch (ClassNotFoundException var20) {
                        var36 = var20;
                        var10001 = false;
                        break label77;
                     } catch (java.lang.InstantiationException var21) {
                        var35 = var21;
                        var10001 = false;
                        break label76;
                     } catch (IllegalAccessException var22) {
                        var34 = var22;
                        var10001 = false;
                        break label82;
                     } catch (NoSuchMethodException var23) {
                        var33 = var23;
                        var10001 = false;
                        break label79;
                     } catch (InvocationTargetException var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label78;
                     }

                     Class var4 = var3;
                     if (var3 == null) {
                        try {
                           var4 = var0.getClassLoader().loadClass(var1);
                           sClassMap.put(var1, var4);
                        } catch (ClassNotFoundException var15) {
                           var36 = var15;
                           var10001 = false;
                           break label77;
                        } catch (java.lang.InstantiationException var16) {
                           var35 = var16;
                           var10001 = false;
                           break label76;
                        } catch (IllegalAccessException var17) {
                           var34 = var17;
                           var10001 = false;
                           break label82;
                        } catch (NoSuchMethodException var18) {
                           var33 = var18;
                           var10001 = false;
                           break label79;
                        } catch (InvocationTargetException var19) {
                           var10000 = var19;
                           var10001 = false;
                           break label78;
                        }
                     }

                     Fragment var25;
                     try {
                        var25 = (Fragment)var4.getConstructor().newInstance();
                     } catch (ClassNotFoundException var10) {
                        var36 = var10;
                        var10001 = false;
                        break label77;
                     } catch (java.lang.InstantiationException var11) {
                        var35 = var11;
                        var10001 = false;
                        break label76;
                     } catch (IllegalAccessException var12) {
                        var34 = var12;
                        var10001 = false;
                        break label82;
                     } catch (NoSuchMethodException var13) {
                        var33 = var13;
                        var10001 = false;
                        break label79;
                     } catch (InvocationTargetException var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label78;
                     }

                     if (var2 == null) {
                        return var25;
                     }

                     try {
                        var2.setClassLoader(var25.getClass().getClassLoader());
                        var25.setArguments(var2);
                        return var25;
                     } catch (ClassNotFoundException var5) {
                        var36 = var5;
                        var10001 = false;
                        break label77;
                     } catch (java.lang.InstantiationException var6) {
                        var35 = var6;
                        var10001 = false;
                        break label76;
                     } catch (IllegalAccessException var7) {
                        var34 = var7;
                        var10001 = false;
                     } catch (NoSuchMethodException var8) {
                        var33 = var8;
                        var10001 = false;
                        break label79;
                     } catch (InvocationTargetException var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label78;
                     }
                  }

                  IllegalAccessException var30 = var34;
                  var26 = new StringBuilder();
                  var26.append("Unable to instantiate fragment ");
                  var26.append(var1);
                  var26.append(": make sure class name exists, is public, and has an");
                  var26.append(" empty constructor that is public");
                  throw new Fragment.InstantiationException(var26.toString(), var30);
               }

               java.lang.InstantiationException var31 = var35;
               var26 = new StringBuilder();
               var26.append("Unable to instantiate fragment ");
               var26.append(var1);
               var26.append(": make sure class name exists, is public, and has an");
               var26.append(" empty constructor that is public");
               throw new Fragment.InstantiationException(var26.toString(), var31);
            }

            ClassNotFoundException var32 = var36;
            var26 = new StringBuilder();
            var26.append("Unable to instantiate fragment ");
            var26.append(var1);
            var26.append(": make sure class name exists, is public, and has an");
            var26.append(" empty constructor that is public");
            throw new Fragment.InstantiationException(var26.toString(), var32);
         }

         InvocationTargetException var28 = var10000;
         var26 = new StringBuilder();
         var26.append("Unable to instantiate fragment ");
         var26.append(var1);
         var26.append(": calling Fragment constructor caused an exception");
         throw new Fragment.InstantiationException(var26.toString(), var28);
      }

      NoSuchMethodException var27 = var33;
      StringBuilder var29 = new StringBuilder();
      var29.append("Unable to instantiate fragment ");
      var29.append(var1);
      var29.append(": could not find Fragment constructor");
      throw new Fragment.InstantiationException(var29.toString(), var27);
   }

   static boolean isSupportFragmentClass(Context var0, String var1) {
      boolean var10001;
      Class var2;
      try {
         var2 = (Class)sClassMap.get(var1);
      } catch (ClassNotFoundException var7) {
         var10001 = false;
         return false;
      }

      Class var3 = var2;
      if (var2 == null) {
         try {
            var3 = var0.getClassLoader().loadClass(var1);
            sClassMap.put(var1, var3);
         } catch (ClassNotFoundException var6) {
            var10001 = false;
            return false;
         }
      }

      try {
         boolean var4 = Fragment.class.isAssignableFrom(var3);
         return var4;
      } catch (ClassNotFoundException var5) {
         var10001 = false;
         return false;
      }
   }

   void callStartTransitionListener() {
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

      if (this.getContext() != null) {
         LoaderManager.getInstance(this).dump(var1, var2, var3, var4);
      }

      if (this.mChildFragmentManager != null) {
         var3.print(var1);
         StringBuilder var5 = new StringBuilder();
         var5.append("Child ");
         var5.append(this.mChildFragmentManager);
         var5.append(":");
         var3.println(var5.toString());
         FragmentManagerImpl var7 = this.mChildFragmentManager;
         StringBuilder var6 = new StringBuilder();
         var6.append(var1);
         var6.append("  ");
         var7.dump(var6.toString(), var2, var3, var4);
      }

   }

   public final boolean equals(Object var1) {
      return super.equals(var1);
   }

   Fragment findFragmentByWho(String var1) {
      if (var1.equals(this.mWho)) {
         return this;
      } else {
         return this.mChildFragmentManager != null ? this.mChildFragmentManager.findFragmentByWho(var1) : null;
      }
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
      return this.mAnimationInfo == null ? null : this.mAnimationInfo.mAnimatingAway;
   }

   Animator getAnimator() {
      return this.mAnimationInfo == null ? null : this.mAnimationInfo.mAnimator;
   }

   public final Bundle getArguments() {
      return this.mArguments;
   }

   public final FragmentManager getChildFragmentManager() {
      if (this.mChildFragmentManager == null) {
         this.instantiateChildFragmentManager();
         if (this.mState >= 4) {
            this.mChildFragmentManager.dispatchResume();
         } else if (this.mState >= 3) {
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
      return this.mAnimationInfo == null ? null : this.mAnimationInfo.mEnterTransition;
   }

   SharedElementCallback getEnterTransitionCallback() {
      return this.mAnimationInfo == null ? null : this.mAnimationInfo.mEnterTransitionCallback;
   }

   public Object getExitTransition() {
      return this.mAnimationInfo == null ? null : this.mAnimationInfo.mExitTransition;
   }

   SharedElementCallback getExitTransitionCallback() {
      return this.mAnimationInfo == null ? null : this.mAnimationInfo.mExitTransitionCallback;
   }

   public final FragmentManager getFragmentManager() {
      return this.mFragmentManager;
   }

   @Deprecated
   public LayoutInflater getLayoutInflater(Bundle var1) {
      if (this.mHost != null) {
         LayoutInflater var2 = this.mHost.onGetLayoutInflater();
         this.getChildFragmentManager();
         LayoutInflaterCompat.setFactory2(var2, this.mChildFragmentManager.getLayoutInflaterFactory());
         return var2;
      } else {
         throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
      }
   }

   public Lifecycle getLifecycle() {
      return this.mLifecycleRegistry;
   }

   int getNextAnim() {
      return this.mAnimationInfo == null ? 0 : this.mAnimationInfo.mNextAnim;
   }

   int getNextTransition() {
      return this.mAnimationInfo == null ? 0 : this.mAnimationInfo.mNextTransition;
   }

   int getNextTransitionStyle() {
      return this.mAnimationInfo == null ? 0 : this.mAnimationInfo.mNextTransitionStyle;
   }

   public final Fragment getParentFragment() {
      return this.mParentFragment;
   }

   public Object getReenterTransition() {
      if (this.mAnimationInfo == null) {
         return null;
      } else {
         Object var1;
         if (this.mAnimationInfo.mReenterTransition == USE_DEFAULT_TRANSITION) {
            var1 = this.getExitTransition();
         } else {
            var1 = this.mAnimationInfo.mReenterTransition;
         }

         return var1;
      }
   }

   public final Resources getResources() {
      return this.requireContext().getResources();
   }

   public Object getReturnTransition() {
      if (this.mAnimationInfo == null) {
         return null;
      } else {
         Object var1;
         if (this.mAnimationInfo.mReturnTransition == USE_DEFAULT_TRANSITION) {
            var1 = this.getEnterTransition();
         } else {
            var1 = this.mAnimationInfo.mReturnTransition;
         }

         return var1;
      }
   }

   public Object getSharedElementEnterTransition() {
      return this.mAnimationInfo == null ? null : this.mAnimationInfo.mSharedElementEnterTransition;
   }

   public Object getSharedElementReturnTransition() {
      if (this.mAnimationInfo == null) {
         return null;
      } else {
         Object var1;
         if (this.mAnimationInfo.mSharedElementReturnTransition == USE_DEFAULT_TRANSITION) {
            var1 = this.getSharedElementEnterTransition();
         } else {
            var1 = this.mAnimationInfo.mSharedElementReturnTransition;
         }

         return var1;
      }
   }

   int getStateAfterAnimating() {
      return this.mAnimationInfo == null ? 0 : this.mAnimationInfo.mStateAfterAnimating;
   }

   public final String getString(int var1) {
      return this.getResources().getString(var1);
   }

   public final String getString(int var1, Object... var2) {
      return this.getResources().getString(var1, var2);
   }

   public View getView() {
      return this.mView;
   }

   public LifecycleOwner getViewLifecycleOwner() {
      if (this.mViewLifecycleOwner != null) {
         return this.mViewLifecycleOwner;
      } else {
         throw new IllegalStateException("Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()");
      }
   }

   public ViewModelStore getViewModelStore() {
      if (this.getContext() != null) {
         if (this.mViewModelStore == null) {
            this.mViewModelStore = new ViewModelStore();
         }

         return this.mViewModelStore;
      } else {
         throw new IllegalStateException("Can't access ViewModels from detached fragment");
      }
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
   }

   void instantiateChildFragmentManager() {
      if (this.mHost != null) {
         this.mChildFragmentManager = new FragmentManagerImpl();
         this.mChildFragmentManager.attachController(this.mHost, new FragmentContainer() {
            public Fragment instantiate(Context var1, String var2, Bundle var3) {
               return Fragment.this.mHost.instantiate(var1, var2, var3);
            }

            public View onFindViewById(int var1) {
               if (Fragment.this.mView != null) {
                  return Fragment.this.mView.findViewById(var1);
               } else {
                  throw new IllegalStateException("Fragment does not have a view");
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
      } else {
         throw new IllegalStateException("Fragment has not been attached yet.");
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

   public final boolean isHidden() {
      return this.mHidden;
   }

   boolean isHideReplaced() {
      return this.mAnimationInfo == null ? false : this.mAnimationInfo.mIsHideReplaced;
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

   boolean isPostponed() {
      return this.mAnimationInfo == null ? false : this.mAnimationInfo.mEnterTransitionPostponed;
   }

   public final boolean isRemoving() {
      return this.mRemoving;
   }

   public final boolean isResumed() {
      boolean var1;
      if (this.mState >= 4) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final boolean isStateSaved() {
      return this.mFragmentManager == null ? false : this.mFragmentManager.isStateSaved();
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

   void noteStateNotSaved() {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.noteStateNotSaved();
      }

   }

   public void onActivityCreated(Bundle var1) {
      this.mCalled = true;
   }

   public void onActivityResult(int var1, int var2, Intent var3) {
   }

   @Deprecated
   public void onAttach(Activity var1) {
      this.mCalled = true;
   }

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

   public void onConfigurationChanged(Configuration var1) {
      this.mCalled = true;
   }

   public boolean onContextItemSelected(MenuItem var1) {
      return false;
   }

   public void onCreate(Bundle var1) {
      this.mCalled = true;
      this.restoreChildFragmentState(var1);
      if (this.mChildFragmentManager != null && !this.mChildFragmentManager.isStateAtLeast(1)) {
         this.mChildFragmentManager.dispatchCreate();
      }

   }

   public Animation onCreateAnimation(int var1, boolean var2, int var3) {
      return null;
   }

   public Animator onCreateAnimator(int var1, boolean var2, int var3) {
      return null;
   }

   public void onCreateContextMenu(ContextMenu var1, View var2, ContextMenuInfo var3) {
      this.getActivity().onCreateContextMenu(var1, var2, var3);
   }

   public void onCreateOptionsMenu(Menu var1, MenuInflater var2) {
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      return null;
   }

   public void onDestroy() {
      boolean var1 = true;
      this.mCalled = true;
      FragmentActivity var2 = this.getActivity();
      if (var2 == null || !var2.isChangingConfigurations()) {
         var1 = false;
      }

      if (this.mViewModelStore != null && !var1) {
         this.mViewModelStore.clear();
      }

   }

   public void onDestroyOptionsMenu() {
   }

   public void onDestroyView() {
      this.mCalled = true;
   }

   public void onDetach() {
      this.mCalled = true;
   }

   public LayoutInflater onGetLayoutInflater(Bundle var1) {
      return this.getLayoutInflater(var1);
   }

   public void onHiddenChanged(boolean var1) {
   }

   @Deprecated
   public void onInflate(Activity var1, AttributeSet var2, Bundle var3) {
      this.mCalled = true;
   }

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

   public void onPause() {
      this.mCalled = true;
   }

   public void onPictureInPictureModeChanged(boolean var1) {
   }

   public void onPrepareOptionsMenu(Menu var1) {
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
   }

   public void onResume() {
      this.mCalled = true;
   }

   public void onSaveInstanceState(Bundle var1) {
   }

   public void onStart() {
      this.mCalled = true;
   }

   public void onStop() {
      this.mCalled = true;
   }

   public void onViewCreated(View var1, Bundle var2) {
   }

   public void onViewStateRestored(Bundle var1) {
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
      if (this.mCalled) {
         if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchActivityCreated();
         }

      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" did not call through to super.onActivityCreated()");
         throw new SuperNotCalledException(var2.toString());
      }
   }

   void performConfigurationChanged(Configuration var1) {
      this.onConfigurationChanged(var1);
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchConfigurationChanged(var1);
      }

   }

   boolean performContextItemSelected(MenuItem var1) {
      if (!this.mHidden) {
         if (this.onContextItemSelected(var1)) {
            return true;
         }

         if (this.mChildFragmentManager != null && this.mChildFragmentManager.dispatchContextItemSelected(var1)) {
            return true;
         }
      }

      return false;
   }

   void performCreate(Bundle var1) {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.noteStateNotSaved();
      }

      this.mState = 1;
      this.mCalled = false;
      this.onCreate(var1);
      this.mIsCreated = true;
      if (this.mCalled) {
         this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" did not call through to super.onCreate()");
         throw new SuperNotCalledException(var2.toString());
      }
   }

   boolean performCreateOptionsMenu(Menu var1, MenuInflater var2) {
      boolean var3 = this.mHidden;
      boolean var4 = false;
      boolean var5 = false;
      if (!var3) {
         var3 = var5;
         if (this.mHasMenu) {
            var3 = var5;
            if (this.mMenuVisible) {
               var3 = true;
               this.onCreateOptionsMenu(var1, var2);
            }
         }

         var4 = var3;
         if (this.mChildFragmentManager != null) {
            var4 = var3 | this.mChildFragmentManager.dispatchCreateOptionsMenu(var1, var2);
         }
      }

      return var4;
   }

   void performCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.noteStateNotSaved();
      }

      this.mPerformedCreateView = true;
      this.mViewLifecycleOwner = new LifecycleOwner() {
         public Lifecycle getLifecycle() {
            if (Fragment.this.mViewLifecycleRegistry == null) {
               Fragment.this.mViewLifecycleRegistry = new LifecycleRegistry(Fragment.this.mViewLifecycleOwner);
            }

            return Fragment.this.mViewLifecycleRegistry;
         }
      };
      this.mViewLifecycleRegistry = null;
      this.mView = this.onCreateView(var1, var2, var3);
      if (this.mView != null) {
         this.mViewLifecycleOwner.getLifecycle();
         this.mViewLifecycleOwnerLiveData.setValue(this.mViewLifecycleOwner);
      } else {
         if (this.mViewLifecycleRegistry != null) {
            throw new IllegalStateException("Called getViewLifecycleOwner() but onCreateView() returned null");
         }

         this.mViewLifecycleOwner = null;
      }

   }

   void performDestroy() {
      this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchDestroy();
      }

      this.mState = 0;
      this.mCalled = false;
      this.mIsCreated = false;
      this.onDestroy();
      if (this.mCalled) {
         this.mChildFragmentManager = null;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onDestroy()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   void performDestroyView() {
      if (this.mView != null) {
         this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
      }

      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchDestroyView();
      }

      this.mState = 1;
      this.mCalled = false;
      this.onDestroyView();
      if (this.mCalled) {
         LoaderManager.getInstance(this).markForRedelivery();
         this.mPerformedCreateView = false;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onDestroyView()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   void performDetach() {
      this.mCalled = false;
      this.onDetach();
      this.mLayoutInflater = null;
      StringBuilder var1;
      if (this.mCalled) {
         if (this.mChildFragmentManager != null) {
            if (!this.mRetaining) {
               var1 = new StringBuilder();
               var1.append("Child FragmentManager of ");
               var1.append(this);
               var1.append(" was not ");
               var1.append(" destroyed and this fragment is not retaining instance");
               throw new IllegalStateException(var1.toString());
            }

            this.mChildFragmentManager.dispatchDestroy();
            this.mChildFragmentManager = null;
         }

      } else {
         var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onDetach()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   LayoutInflater performGetLayoutInflater(Bundle var1) {
      this.mLayoutInflater = this.onGetLayoutInflater(var1);
      return this.mLayoutInflater;
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
      if (!this.mHidden) {
         if (this.mHasMenu && this.mMenuVisible && this.onOptionsItemSelected(var1)) {
            return true;
         }

         if (this.mChildFragmentManager != null && this.mChildFragmentManager.dispatchOptionsItemSelected(var1)) {
            return true;
         }
      }

      return false;
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
      if (this.mView != null) {
         this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
      }

      this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchPause();
      }

      this.mState = 3;
      this.mCalled = false;
      this.onPause();
      if (!this.mCalled) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onPause()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   void performPictureInPictureModeChanged(boolean var1) {
      this.onPictureInPictureModeChanged(var1);
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchPictureInPictureModeChanged(var1);
      }

   }

   boolean performPrepareOptionsMenu(Menu var1) {
      boolean var2 = this.mHidden;
      boolean var3 = false;
      boolean var4 = false;
      if (!var2) {
         var2 = var4;
         if (this.mHasMenu) {
            var2 = var4;
            if (this.mMenuVisible) {
               var2 = true;
               this.onPrepareOptionsMenu(var1);
            }
         }

         var3 = var2;
         if (this.mChildFragmentManager != null) {
            var3 = var2 | this.mChildFragmentManager.dispatchPrepareOptionsMenu(var1);
         }
      }

      return var3;
   }

   void performResume() {
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.noteStateNotSaved();
         this.mChildFragmentManager.execPendingActions();
      }

      this.mState = 4;
      this.mCalled = false;
      this.onResume();
      if (this.mCalled) {
         if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchResume();
            this.mChildFragmentManager.execPendingActions();
         }

         this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
         if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
         }

      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onResume()");
         throw new SuperNotCalledException(var1.toString());
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

      this.mState = 3;
      this.mCalled = false;
      this.onStart();
      if (this.mCalled) {
         if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchStart();
         }

         this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
         if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
         }

      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onStart()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   void performStop() {
      if (this.mView != null) {
         this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
      }

      this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
      if (this.mChildFragmentManager != null) {
         this.mChildFragmentManager.dispatchStop();
      }

      this.mState = 2;
      this.mCalled = false;
      this.onStop();
      if (!this.mCalled) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onStop()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   public final void requestPermissions(String[] var1, int var2) {
      if (this.mHost != null) {
         this.mHost.onRequestPermissionsFromFragment(this, var1, var2);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Fragment ");
         var3.append(this);
         var3.append(" not attached to Activity");
         throw new IllegalStateException(var3.toString());
      }
   }

   public final Context requireContext() {
      Context var1 = this.getContext();
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" not attached to a context.");
         throw new IllegalStateException(var2.toString());
      }
   }

   void restoreChildFragmentState(Bundle var1) {
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
      if (this.mCalled) {
         if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
         }

      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" did not call through to super.onViewStateRestored()");
         throw new SuperNotCalledException(var2.toString());
      }
   }

   void setAnimatingAway(View var1) {
      this.ensureAnimationInfo().mAnimatingAway = var1;
   }

   void setAnimator(Animator var1) {
      this.ensureAnimationInfo().mAnimator = var1;
   }

   public void setArguments(Bundle var1) {
      if (this.mIndex >= 0 && this.isStateSaved()) {
         throw new IllegalStateException("Fragment already active and state has been saved");
      } else {
         this.mArguments = var1;
      }
   }

   public void setExitTransition(Object var1) {
      this.ensureAnimationInfo().mExitTransition = var1;
   }

   void setHideReplaced(boolean var1) {
      this.ensureAnimationInfo().mIsHideReplaced = var1;
   }

   final void setIndex(int var1, Fragment var2) {
      this.mIndex = var1;
      if (var2 != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var2.mWho);
         var3.append(":");
         var3.append(this.mIndex);
         this.mWho = var3.toString();
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("android:fragment:");
         var4.append(this.mIndex);
         this.mWho = var4.toString();
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
            StringBuilder var2 = new StringBuilder();
            var2.append("Trying to set a replacement startPostponedEnterTransition on ");
            var2.append(this);
            throw new IllegalStateException(var2.toString());
         } else {
            if (this.mAnimationInfo.mEnterTransitionPostponed) {
               this.mAnimationInfo.mStartEnterTransitionListener = var1;
            }

            if (var1 != null) {
               var1.startListening();
            }

         }
      }
   }

   public void setRetainInstance(boolean var1) {
      this.mRetainInstance = var1;
   }

   void setStateAfterAnimating(int var1) {
      this.ensureAnimationInfo().mStateAfterAnimating = var1;
   }

   public boolean shouldShowRequestPermissionRationale(String var1) {
      return this.mHost != null ? this.mHost.onShouldShowRequestPermissionRationale(var1) : false;
   }

   public void startActivity(Intent var1) {
      this.startActivity(var1, (Bundle)null);
   }

   public void startActivity(Intent var1, Bundle var2) {
      if (this.mHost != null) {
         this.mHost.onStartActivityFromFragment(this, var1, -1, var2);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Fragment ");
         var3.append(this);
         var3.append(" not attached to Activity");
         throw new IllegalStateException(var3.toString());
      }
   }

   public void startActivityForResult(Intent var1, int var2) {
      this.startActivityForResult(var1, var2, (Bundle)null);
   }

   public void startActivityForResult(Intent var1, int var2, Bundle var3) {
      if (this.mHost != null) {
         this.mHost.onStartActivityFromFragment(this, var1, var2, var3);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Fragment ");
         var4.append(this);
         var4.append(" not attached to Activity");
         throw new IllegalStateException(var4.toString());
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

   static class AnimationInfo {
      Boolean mAllowEnterTransitionOverlap;
      Boolean mAllowReturnTransitionOverlap;
      View mAnimatingAway;
      Animator mAnimator;
      Object mEnterTransition = null;
      SharedElementCallback mEnterTransitionCallback;
      boolean mEnterTransitionPostponed;
      Object mExitTransition;
      SharedElementCallback mExitTransitionCallback;
      boolean mIsHideReplaced;
      int mNextAnim;
      int mNextTransition;
      int mNextTransitionStyle;
      Object mReenterTransition;
      Object mReturnTransition;
      Object mSharedElementEnterTransition;
      Object mSharedElementReturnTransition;
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
}
