package android.support.v4.app;

import android.os.Build.VERSION;
import android.support.v4.util.LogWriter;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

final class BackStackRecord extends FragmentTransaction implements FragmentManager.BackStackEntry, FragmentManagerImpl.OpGenerator {
   static final int OP_ADD = 1;
   static final int OP_ATTACH = 7;
   static final int OP_DETACH = 6;
   static final int OP_HIDE = 4;
   static final int OP_NULL = 0;
   static final int OP_REMOVE = 3;
   static final int OP_REPLACE = 2;
   static final int OP_SHOW = 5;
   static final boolean SUPPORTS_TRANSITIONS;
   static final String TAG = "FragmentManager";
   boolean mAddToBackStack;
   boolean mAllowAddToBackStack = true;
   boolean mAllowOptimization = false;
   int mBreadCrumbShortTitleRes;
   CharSequence mBreadCrumbShortTitleText;
   int mBreadCrumbTitleRes;
   CharSequence mBreadCrumbTitleText;
   boolean mCommitted;
   int mEnterAnim;
   int mExitAnim;
   int mIndex = -1;
   final FragmentManagerImpl mManager;
   String mName;
   ArrayList mOps = new ArrayList();
   int mPopEnterAnim;
   int mPopExitAnim;
   ArrayList mSharedElementSourceNames;
   ArrayList mSharedElementTargetNames;
   int mTransition;
   int mTransitionStyle;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      SUPPORTS_TRANSITIONS = var0;
   }

   public BackStackRecord(FragmentManagerImpl var1) {
      this.mManager = var1;
   }

   private void doAddOp(int var1, Fragment var2, String var3, int var4) {
      Class var5 = var2.getClass();
      int var6 = var5.getModifiers();
      if (var5.isAnonymousClass() || !Modifier.isPublic(var6) || var5.isMemberClass() && !Modifier.isStatic(var6)) {
         throw new IllegalStateException("Fragment " + var5.getCanonicalName() + " must be a public static class to be  properly recreated from" + " instance state.");
      } else {
         var2.mFragmentManager = this.mManager;
         if (var3 != null) {
            if (var2.mTag != null && !var3.equals(var2.mTag)) {
               throw new IllegalStateException("Can't change tag of fragment " + var2 + ": was " + var2.mTag + " now " + var3);
            }

            var2.mTag = var3;
         }

         if (var1 != 0) {
            if (var1 == -1) {
               throw new IllegalArgumentException("Can't add fragment " + var2 + " with tag " + var3 + " to container view with no id");
            }

            if (var2.mFragmentId != 0 && var2.mFragmentId != var1) {
               throw new IllegalStateException("Can't change container ID of fragment " + var2 + ": was " + var2.mFragmentId + " now " + var1);
            }

            var2.mFragmentId = var1;
            var2.mContainerId = var1;
         }

         BackStackRecord.Op var7 = new BackStackRecord.Op();
         var7.cmd = var4;
         var7.fragment = var2;
         this.addOp(var7);
      }
   }

   private static boolean isFragmentPostponed(BackStackRecord.Op var0) {
      Fragment var2 = var0.fragment;
      boolean var1;
      if (var2.mAdded && var2.mView != null && !var2.mDetached && !var2.mHidden && var2.isPostponed()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public FragmentTransaction add(int var1, Fragment var2) {
      this.doAddOp(var1, var2, (String)null, 1);
      return this;
   }

   public FragmentTransaction add(int var1, Fragment var2, String var3) {
      this.doAddOp(var1, var2, var3, 1);
      return this;
   }

   public FragmentTransaction add(Fragment var1, String var2) {
      this.doAddOp(0, var1, var2, 1);
      return this;
   }

   void addOp(BackStackRecord.Op var1) {
      this.mOps.add(var1);
      var1.enterAnim = this.mEnterAnim;
      var1.exitAnim = this.mExitAnim;
      var1.popEnterAnim = this.mPopEnterAnim;
      var1.popExitAnim = this.mPopExitAnim;
   }

   public FragmentTransaction addSharedElement(View var1, String var2) {
      if (SUPPORTS_TRANSITIONS) {
         String var3 = ViewCompat.getTransitionName(var1);
         if (var3 == null) {
            throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
         }

         if (this.mSharedElementSourceNames == null) {
            this.mSharedElementSourceNames = new ArrayList();
            this.mSharedElementTargetNames = new ArrayList();
         } else {
            if (this.mSharedElementTargetNames.contains(var2)) {
               throw new IllegalArgumentException("A shared element with the target name '" + var2 + "' has already been added to the transaction.");
            }

            if (this.mSharedElementSourceNames.contains(var3)) {
               throw new IllegalArgumentException("A shared element with the source name '" + var3 + " has already been added to the transaction.");
            }
         }

         this.mSharedElementSourceNames.add(var3);
         this.mSharedElementTargetNames.add(var2);
      }

      return this;
   }

   public FragmentTransaction addToBackStack(String var1) {
      if (!this.mAllowAddToBackStack) {
         throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
      } else {
         this.mAddToBackStack = true;
         this.mName = var1;
         return this;
      }
   }

   public FragmentTransaction attach(Fragment var1) {
      BackStackRecord.Op var2 = new BackStackRecord.Op();
      var2.cmd = 7;
      var2.fragment = var1;
      this.addOp(var2);
      return this;
   }

   void bumpBackStackNesting(int var1) {
      if (this.mAddToBackStack) {
         if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "Bump nesting in " + this + " by " + var1);
         }

         int var2 = this.mOps.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            BackStackRecord.Op var4 = (BackStackRecord.Op)this.mOps.get(var3);
            if (var4.fragment != null) {
               Fragment var5 = var4.fragment;
               var5.mBackStackNesting += var1;
               if (FragmentManagerImpl.DEBUG) {
                  Log.v("FragmentManager", "Bump nesting of " + var4.fragment + " to " + var4.fragment.mBackStackNesting);
               }
            }
         }
      }

   }

   public int commit() {
      return this.commitInternal(false);
   }

   public int commitAllowingStateLoss() {
      return this.commitInternal(true);
   }

   int commitInternal(boolean var1) {
      if (this.mCommitted) {
         throw new IllegalStateException("commit already called");
      } else {
         if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "Commit: " + this);
            PrintWriter var2 = new PrintWriter(new LogWriter("FragmentManager"));
            this.dump("  ", (FileDescriptor)null, var2, (String[])null);
            var2.close();
         }

         this.mCommitted = true;
         if (this.mAddToBackStack) {
            this.mIndex = this.mManager.allocBackStackIndex(this);
         } else {
            this.mIndex = -1;
         }

         this.mManager.enqueueAction(this, var1);
         return this.mIndex;
      }
   }

   public void commitNow() {
      this.disallowAddToBackStack();
      this.mManager.execSingleAction(this, false);
   }

   public void commitNowAllowingStateLoss() {
      this.disallowAddToBackStack();
      this.mManager.execSingleAction(this, true);
   }

   public FragmentTransaction detach(Fragment var1) {
      BackStackRecord.Op var2 = new BackStackRecord.Op();
      var2.cmd = 6;
      var2.fragment = var1;
      this.addOp(var2);
      return this;
   }

   public FragmentTransaction disallowAddToBackStack() {
      if (this.mAddToBackStack) {
         throw new IllegalStateException("This transaction is already being added to the back stack");
      } else {
         this.mAllowAddToBackStack = false;
         return this;
      }
   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      this.dump(var1, var3, true);
   }

   public void dump(String var1, PrintWriter var2, boolean var3) {
      if (var3) {
         var2.print(var1);
         var2.print("mName=");
         var2.print(this.mName);
         var2.print(" mIndex=");
         var2.print(this.mIndex);
         var2.print(" mCommitted=");
         var2.println(this.mCommitted);
         if (this.mTransition != 0) {
            var2.print(var1);
            var2.print("mTransition=#");
            var2.print(Integer.toHexString(this.mTransition));
            var2.print(" mTransitionStyle=#");
            var2.println(Integer.toHexString(this.mTransitionStyle));
         }

         if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
            var2.print(var1);
            var2.print("mEnterAnim=#");
            var2.print(Integer.toHexString(this.mEnterAnim));
            var2.print(" mExitAnim=#");
            var2.println(Integer.toHexString(this.mExitAnim));
         }

         if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
            var2.print(var1);
            var2.print("mPopEnterAnim=#");
            var2.print(Integer.toHexString(this.mPopEnterAnim));
            var2.print(" mPopExitAnim=#");
            var2.println(Integer.toHexString(this.mPopExitAnim));
         }

         if (this.mBreadCrumbTitleRes != 0 || this.mBreadCrumbTitleText != null) {
            var2.print(var1);
            var2.print("mBreadCrumbTitleRes=#");
            var2.print(Integer.toHexString(this.mBreadCrumbTitleRes));
            var2.print(" mBreadCrumbTitleText=");
            var2.println(this.mBreadCrumbTitleText);
         }

         if (this.mBreadCrumbShortTitleRes != 0 || this.mBreadCrumbShortTitleText != null) {
            var2.print(var1);
            var2.print("mBreadCrumbShortTitleRes=#");
            var2.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
            var2.print(" mBreadCrumbShortTitleText=");
            var2.println(this.mBreadCrumbShortTitleText);
         }
      }

      if (!this.mOps.isEmpty()) {
         var2.print(var1);
         var2.println("Operations:");
         (new StringBuilder()).append(var1).append("    ").toString();
         int var4 = this.mOps.size();

         for(int var5 = 0; var5 < var4; ++var5) {
            BackStackRecord.Op var6 = (BackStackRecord.Op)this.mOps.get(var5);
            String var7;
            switch(var6.cmd) {
            case 0:
               var7 = "NULL";
               break;
            case 1:
               var7 = "ADD";
               break;
            case 2:
               var7 = "REPLACE";
               break;
            case 3:
               var7 = "REMOVE";
               break;
            case 4:
               var7 = "HIDE";
               break;
            case 5:
               var7 = "SHOW";
               break;
            case 6:
               var7 = "DETACH";
               break;
            case 7:
               var7 = "ATTACH";
               break;
            default:
               var7 = "cmd=" + var6.cmd;
            }

            var2.print(var1);
            var2.print("  Op #");
            var2.print(var5);
            var2.print(": ");
            var2.print(var7);
            var2.print(" ");
            var2.println(var6.fragment);
            if (var3) {
               if (var6.enterAnim != 0 || var6.exitAnim != 0) {
                  var2.print(var1);
                  var2.print("enterAnim=#");
                  var2.print(Integer.toHexString(var6.enterAnim));
                  var2.print(" exitAnim=#");
                  var2.println(Integer.toHexString(var6.exitAnim));
               }

               if (var6.popEnterAnim != 0 || var6.popExitAnim != 0) {
                  var2.print(var1);
                  var2.print("popEnterAnim=#");
                  var2.print(Integer.toHexString(var6.popEnterAnim));
                  var2.print(" popExitAnim=#");
                  var2.println(Integer.toHexString(var6.popExitAnim));
               }
            }
         }
      }

   }

   void executeOps() {
      int var1 = this.mOps.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         BackStackRecord.Op var3 = (BackStackRecord.Op)this.mOps.get(var2);
         Fragment var4 = var3.fragment;
         var4.setNextTransition(this.mTransition, this.mTransitionStyle);
         switch(var3.cmd) {
         case 1:
            var4.setNextAnim(var3.enterAnim);
            this.mManager.addFragment(var4, false);
            break;
         case 2:
         default:
            throw new IllegalArgumentException("Unknown cmd: " + var3.cmd);
         case 3:
            var4.setNextAnim(var3.exitAnim);
            this.mManager.removeFragment(var4);
            break;
         case 4:
            var4.setNextAnim(var3.exitAnim);
            this.mManager.hideFragment(var4);
            break;
         case 5:
            var4.setNextAnim(var3.enterAnim);
            this.mManager.showFragment(var4);
            break;
         case 6:
            var4.setNextAnim(var3.exitAnim);
            this.mManager.detachFragment(var4);
            break;
         case 7:
            var4.setNextAnim(var3.enterAnim);
            this.mManager.attachFragment(var4);
         }

         if (!this.mAllowOptimization && var3.cmd != 1) {
            this.mManager.moveFragmentToExpectedState(var4);
         }
      }

      if (!this.mAllowOptimization) {
         this.mManager.moveToState(this.mManager.mCurState, true);
      }

   }

   void executePopOps(boolean var1) {
      for(int var2 = this.mOps.size() - 1; var2 >= 0; --var2) {
         BackStackRecord.Op var3 = (BackStackRecord.Op)this.mOps.get(var2);
         Fragment var4 = var3.fragment;
         var4.setNextTransition(FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
         switch(var3.cmd) {
         case 1:
            var4.setNextAnim(var3.popExitAnim);
            this.mManager.removeFragment(var4);
            break;
         case 2:
         default:
            throw new IllegalArgumentException("Unknown cmd: " + var3.cmd);
         case 3:
            var4.setNextAnim(var3.popEnterAnim);
            this.mManager.addFragment(var4, false);
            break;
         case 4:
            var4.setNextAnim(var3.popEnterAnim);
            this.mManager.showFragment(var4);
            break;
         case 5:
            var4.setNextAnim(var3.popExitAnim);
            this.mManager.hideFragment(var4);
            break;
         case 6:
            var4.setNextAnim(var3.popEnterAnim);
            this.mManager.attachFragment(var4);
            break;
         case 7:
            var4.setNextAnim(var3.popExitAnim);
            this.mManager.detachFragment(var4);
         }

         if (!this.mAllowOptimization && var3.cmd != 3) {
            this.mManager.moveFragmentToExpectedState(var4);
         }
      }

      if (!this.mAllowOptimization && var1) {
         this.mManager.moveToState(this.mManager.mCurState, true);
      }

   }

   void expandReplaceOps(ArrayList var1) {
      int var4;
      for(int var2 = 0; var2 < this.mOps.size(); var2 = var4 + 1) {
         BackStackRecord.Op var3 = (BackStackRecord.Op)this.mOps.get(var2);
         var4 = var2;
         switch(var3.cmd) {
         case 1:
         case 7:
            var1.add(var3.fragment);
            var4 = var2;
            break;
         case 2:
            Fragment var5 = var3.fragment;
            int var6 = var5.mContainerId;
            boolean var7 = false;

            int var10;
            for(var4 = var1.size() - 1; var4 >= 0; var2 = var10) {
               Fragment var8 = (Fragment)var1.get(var4);
               boolean var9 = var7;
               var10 = var2;
               if (var8.mContainerId == var6) {
                  if (var8 == var5) {
                     var9 = true;
                     var10 = var2;
                  } else {
                     BackStackRecord.Op var11 = new BackStackRecord.Op();
                     var11.cmd = 3;
                     var11.fragment = var8;
                     var11.enterAnim = var3.enterAnim;
                     var11.popEnterAnim = var3.popEnterAnim;
                     var11.exitAnim = var3.exitAnim;
                     var11.popExitAnim = var3.popExitAnim;
                     this.mOps.add(var2, var11);
                     var1.remove(var8);
                     var10 = var2 + 1;
                     var9 = var7;
                  }
               }

               --var4;
               var7 = var9;
            }

            if (var7) {
               this.mOps.remove(var2);
               var4 = var2 - 1;
            } else {
               var3.cmd = 1;
               var1.add(var5);
               var4 = var2;
            }
            break;
         case 3:
         case 6:
            var1.remove(var3.fragment);
            var4 = var2;
         case 4:
         case 5:
            break;
         default:
            var4 = var2;
         }
      }

   }

   public boolean generateOps(ArrayList var1, ArrayList var2) {
      if (FragmentManagerImpl.DEBUG) {
         Log.v("FragmentManager", "Run: " + this);
      }

      var1.add(this);
      var2.add(false);
      if (this.mAddToBackStack) {
         this.mManager.addBackStackState(this);
      }

      return true;
   }

   public CharSequence getBreadCrumbShortTitle() {
      CharSequence var1;
      if (this.mBreadCrumbShortTitleRes != 0) {
         var1 = this.mManager.mHost.getContext().getText(this.mBreadCrumbShortTitleRes);
      } else {
         var1 = this.mBreadCrumbShortTitleText;
      }

      return var1;
   }

   public int getBreadCrumbShortTitleRes() {
      return this.mBreadCrumbShortTitleRes;
   }

   public CharSequence getBreadCrumbTitle() {
      CharSequence var1;
      if (this.mBreadCrumbTitleRes != 0) {
         var1 = this.mManager.mHost.getContext().getText(this.mBreadCrumbTitleRes);
      } else {
         var1 = this.mBreadCrumbTitleText;
      }

      return var1;
   }

   public int getBreadCrumbTitleRes() {
      return this.mBreadCrumbTitleRes;
   }

   public int getId() {
      return this.mIndex;
   }

   public String getName() {
      return this.mName;
   }

   public int getTransition() {
      return this.mTransition;
   }

   public int getTransitionStyle() {
      return this.mTransitionStyle;
   }

   public FragmentTransaction hide(Fragment var1) {
      BackStackRecord.Op var2 = new BackStackRecord.Op();
      var2.cmd = 4;
      var2.fragment = var1;
      this.addOp(var2);
      return this;
   }

   boolean interactsWith(int var1) {
      int var2 = this.mOps.size();
      int var3 = 0;

      boolean var4;
      while(true) {
         if (var3 >= var2) {
            var4 = false;
            break;
         }

         if (((BackStackRecord.Op)this.mOps.get(var3)).fragment.mContainerId == var1) {
            var4 = true;
            break;
         }

         ++var3;
      }

      return var4;
   }

   boolean interactsWith(ArrayList var1, int var2, int var3) {
      boolean var4 = false;
      boolean var5;
      if (var3 == var2) {
         var5 = var4;
      } else {
         int var6 = this.mOps.size();
         int var7 = -1;
         int var8 = 0;

         while(true) {
            var5 = var4;
            if (var8 >= var6) {
               break;
            }

            int var9 = ((BackStackRecord.Op)this.mOps.get(var8)).fragment.mContainerId;
            int var10 = var7;
            if (var9 != 0) {
               var10 = var7;
               if (var9 != var7) {
                  var7 = var9;
                  int var11 = var2;

                  while(true) {
                     var10 = var7;
                     if (var11 >= var3) {
                        break;
                     }

                     BackStackRecord var12 = (BackStackRecord)var1.get(var11);
                     int var13 = var12.mOps.size();

                     for(var10 = 0; var10 < var13; ++var10) {
                        if (((BackStackRecord.Op)var12.mOps.get(var10)).fragment.mContainerId == var9) {
                           var5 = true;
                           return var5;
                        }
                     }

                     ++var11;
                  }
               }
            }

            ++var8;
            var7 = var10;
         }
      }

      return var5;
   }

   public boolean isAddToBackStackAllowed() {
      return this.mAllowAddToBackStack;
   }

   public boolean isEmpty() {
      return this.mOps.isEmpty();
   }

   boolean isPostponed() {
      int var1 = 0;

      boolean var2;
      while(true) {
         if (var1 >= this.mOps.size()) {
            var2 = false;
            break;
         }

         if (isFragmentPostponed((BackStackRecord.Op)this.mOps.get(var1))) {
            var2 = true;
            break;
         }

         ++var1;
      }

      return var2;
   }

   public FragmentTransaction remove(Fragment var1) {
      BackStackRecord.Op var2 = new BackStackRecord.Op();
      var2.cmd = 3;
      var2.fragment = var1;
      this.addOp(var2);
      return this;
   }

   public FragmentTransaction replace(int var1, Fragment var2) {
      return this.replace(var1, var2, (String)null);
   }

   public FragmentTransaction replace(int var1, Fragment var2, String var3) {
      if (var1 == 0) {
         throw new IllegalArgumentException("Must use non-zero containerViewId");
      } else {
         this.doAddOp(var1, var2, var3, 2);
         return this;
      }
   }

   public FragmentTransaction setAllowOptimization(boolean var1) {
      this.mAllowOptimization = var1;
      return this;
   }

   public FragmentTransaction setBreadCrumbShortTitle(int var1) {
      this.mBreadCrumbShortTitleRes = var1;
      this.mBreadCrumbShortTitleText = null;
      return this;
   }

   public FragmentTransaction setBreadCrumbShortTitle(CharSequence var1) {
      this.mBreadCrumbShortTitleRes = 0;
      this.mBreadCrumbShortTitleText = var1;
      return this;
   }

   public FragmentTransaction setBreadCrumbTitle(int var1) {
      this.mBreadCrumbTitleRes = var1;
      this.mBreadCrumbTitleText = null;
      return this;
   }

   public FragmentTransaction setBreadCrumbTitle(CharSequence var1) {
      this.mBreadCrumbTitleRes = 0;
      this.mBreadCrumbTitleText = var1;
      return this;
   }

   public FragmentTransaction setCustomAnimations(int var1, int var2) {
      return this.setCustomAnimations(var1, var2, 0, 0);
   }

   public FragmentTransaction setCustomAnimations(int var1, int var2, int var3, int var4) {
      this.mEnterAnim = var1;
      this.mExitAnim = var2;
      this.mPopEnterAnim = var3;
      this.mPopExitAnim = var4;
      return this;
   }

   void setOnStartPostponedListener(Fragment.OnStartEnterTransitionListener var1) {
      for(int var2 = 0; var2 < this.mOps.size(); ++var2) {
         BackStackRecord.Op var3 = (BackStackRecord.Op)this.mOps.get(var2);
         if (isFragmentPostponed(var3)) {
            var3.fragment.setOnStartEnterTransitionListener(var1);
         }
      }

   }

   public FragmentTransaction setTransition(int var1) {
      this.mTransition = var1;
      return this;
   }

   public FragmentTransaction setTransitionStyle(int var1) {
      this.mTransitionStyle = var1;
      return this;
   }

   public FragmentTransaction show(Fragment var1) {
      BackStackRecord.Op var2 = new BackStackRecord.Op();
      var2.cmd = 5;
      var2.fragment = var1;
      this.addOp(var2);
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(128);
      var1.append("BackStackEntry{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      if (this.mIndex >= 0) {
         var1.append(" #");
         var1.append(this.mIndex);
      }

      if (this.mName != null) {
         var1.append(" ");
         var1.append(this.mName);
      }

      var1.append("}");
      return var1.toString();
   }

   void trackAddedFragmentsInPop(ArrayList var1) {
      for(int var2 = 0; var2 < this.mOps.size(); ++var2) {
         BackStackRecord.Op var3 = (BackStackRecord.Op)this.mOps.get(var2);
         switch(var3.cmd) {
         case 1:
         case 7:
            var1.remove(var3.fragment);
         case 2:
         case 4:
         case 5:
         default:
            break;
         case 3:
         case 6:
            var1.add(var3.fragment);
         }
      }

   }

   static final class Op {
      int cmd;
      int enterAnim;
      int exitAnim;
      Fragment fragment;
      int popEnterAnim;
      int popExitAnim;
   }
}
