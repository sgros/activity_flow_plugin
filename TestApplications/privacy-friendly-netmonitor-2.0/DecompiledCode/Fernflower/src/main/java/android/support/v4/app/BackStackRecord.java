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
   static final int OP_SET_PRIMARY_NAV = 8;
   static final int OP_SHOW = 5;
   static final int OP_UNSET_PRIMARY_NAV = 9;
   static final boolean SUPPORTS_TRANSITIONS;
   static final String TAG = "FragmentManager";
   boolean mAddToBackStack;
   boolean mAllowAddToBackStack = true;
   int mBreadCrumbShortTitleRes;
   CharSequence mBreadCrumbShortTitleText;
   int mBreadCrumbTitleRes;
   CharSequence mBreadCrumbTitleText;
   ArrayList mCommitRunnables;
   boolean mCommitted;
   int mEnterAnim;
   int mExitAnim;
   int mIndex = -1;
   final FragmentManagerImpl mManager;
   String mName;
   ArrayList mOps = new ArrayList();
   int mPopEnterAnim;
   int mPopExitAnim;
   boolean mReorderingAllowed = false;
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
         StringBuilder var7 = new StringBuilder();
         var7.append("Fragment ");
         var7.append(var5.getCanonicalName());
         var7.append(" must be a public static class to be  properly recreated from");
         var7.append(" instance state.");
         throw new IllegalStateException(var7.toString());
      } else {
         var2.mFragmentManager = this.mManager;
         StringBuilder var9;
         if (var3 != null) {
            if (var2.mTag != null && !var3.equals(var2.mTag)) {
               var9 = new StringBuilder();
               var9.append("Can't change tag of fragment ");
               var9.append(var2);
               var9.append(": was ");
               var9.append(var2.mTag);
               var9.append(" now ");
               var9.append(var3);
               throw new IllegalStateException(var9.toString());
            }

            var2.mTag = var3;
         }

         if (var1 != 0) {
            if (var1 == -1) {
               var9 = new StringBuilder();
               var9.append("Can't add fragment ");
               var9.append(var2);
               var9.append(" with tag ");
               var9.append(var3);
               var9.append(" to container view with no id");
               throw new IllegalArgumentException(var9.toString());
            }

            if (var2.mFragmentId != 0 && var2.mFragmentId != var1) {
               StringBuilder var8 = new StringBuilder();
               var8.append("Can't change container ID of fragment ");
               var8.append(var2);
               var8.append(": was ");
               var8.append(var2.mFragmentId);
               var8.append(" now ");
               var8.append(var1);
               throw new IllegalStateException(var8.toString());
            }

            var2.mFragmentId = var1;
            var2.mContainerId = var1;
         }

         this.addOp(new BackStackRecord.Op(var4, var2));
      }
   }

   private static boolean isFragmentPostponed(BackStackRecord.Op var0) {
      Fragment var2 = var0.fragment;
      boolean var1;
      if (var2 != null && var2.mAdded && var2.mView != null && !var2.mDetached && !var2.mHidden && var2.isPostponed()) {
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
               StringBuilder var4 = new StringBuilder();
               var4.append("A shared element with the target name '");
               var4.append(var2);
               var4.append("' has already been added to the transaction.");
               throw new IllegalArgumentException(var4.toString());
            }

            if (this.mSharedElementSourceNames.contains(var3)) {
               StringBuilder var5 = new StringBuilder();
               var5.append("A shared element with the source name '");
               var5.append(var3);
               var5.append(" has already been added to the transaction.");
               throw new IllegalArgumentException(var5.toString());
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
      this.addOp(new BackStackRecord.Op(7, var1));
      return this;
   }

   void bumpBackStackNesting(int var1) {
      if (this.mAddToBackStack) {
         if (FragmentManagerImpl.DEBUG) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Bump nesting in ");
            var2.append(this);
            var2.append(" by ");
            var2.append(var1);
            Log.v("FragmentManager", var2.toString());
         }

         int var3 = this.mOps.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            BackStackRecord.Op var6 = (BackStackRecord.Op)this.mOps.get(var4);
            if (var6.fragment != null) {
               Fragment var5 = var6.fragment;
               var5.mBackStackNesting += var1;
               if (FragmentManagerImpl.DEBUG) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("Bump nesting of ");
                  var7.append(var6.fragment);
                  var7.append(" to ");
                  var7.append(var6.fragment.mBackStackNesting);
                  Log.v("FragmentManager", var7.toString());
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
            StringBuilder var2 = new StringBuilder();
            var2.append("Commit: ");
            var2.append(this);
            Log.v("FragmentManager", var2.toString());
            PrintWriter var3 = new PrintWriter(new LogWriter("FragmentManager"));
            this.dump("  ", (FileDescriptor)null, var3, (String[])null);
            var3.close();
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
      this.addOp(new BackStackRecord.Op(6, var1));
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
         StringBuilder var4 = new StringBuilder();
         var4.append(var1);
         var4.append("    ");
         var4.toString();
         int var5 = this.mOps.size();

         for(int var6 = 0; var6 < var5; ++var6) {
            BackStackRecord.Op var7 = (BackStackRecord.Op)this.mOps.get(var6);
            String var8;
            switch(var7.cmd) {
            case 0:
               var8 = "NULL";
               break;
            case 1:
               var8 = "ADD";
               break;
            case 2:
               var8 = "REPLACE";
               break;
            case 3:
               var8 = "REMOVE";
               break;
            case 4:
               var8 = "HIDE";
               break;
            case 5:
               var8 = "SHOW";
               break;
            case 6:
               var8 = "DETACH";
               break;
            case 7:
               var8 = "ATTACH";
               break;
            case 8:
               var8 = "SET_PRIMARY_NAV";
               break;
            case 9:
               var8 = "UNSET_PRIMARY_NAV";
               break;
            default:
               var4 = new StringBuilder();
               var4.append("cmd=");
               var4.append(var7.cmd);
               var8 = var4.toString();
            }

            var2.print(var1);
            var2.print("  Op #");
            var2.print(var6);
            var2.print(": ");
            var2.print(var8);
            var2.print(" ");
            var2.println(var7.fragment);
            if (var3) {
               if (var7.enterAnim != 0 || var7.exitAnim != 0) {
                  var2.print(var1);
                  var2.print("enterAnim=#");
                  var2.print(Integer.toHexString(var7.enterAnim));
                  var2.print(" exitAnim=#");
                  var2.println(Integer.toHexString(var7.exitAnim));
               }

               if (var7.popEnterAnim != 0 || var7.popExitAnim != 0) {
                  var2.print(var1);
                  var2.print("popEnterAnim=#");
                  var2.print(Integer.toHexString(var7.popEnterAnim));
                  var2.print(" popExitAnim=#");
                  var2.println(Integer.toHexString(var7.popExitAnim));
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
         if (var4 != null) {
            var4.setNextTransition(this.mTransition, this.mTransitionStyle);
         }

         int var5 = var3.cmd;
         if (var5 != 1) {
            switch(var5) {
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
               break;
            case 8:
               this.mManager.setPrimaryNavigationFragment(var4);
               break;
            case 9:
               this.mManager.setPrimaryNavigationFragment((Fragment)null);
               break;
            default:
               StringBuilder var6 = new StringBuilder();
               var6.append("Unknown cmd: ");
               var6.append(var3.cmd);
               throw new IllegalArgumentException(var6.toString());
            }
         } else {
            var4.setNextAnim(var3.enterAnim);
            this.mManager.addFragment(var4, false);
         }

         if (!this.mReorderingAllowed && var3.cmd != 1 && var4 != null) {
            this.mManager.moveFragmentToExpectedState(var4);
         }
      }

      if (!this.mReorderingAllowed) {
         this.mManager.moveToState(this.mManager.mCurState, true);
      }

   }

   void executePopOps(boolean var1) {
      for(int var2 = this.mOps.size() - 1; var2 >= 0; --var2) {
         BackStackRecord.Op var3 = (BackStackRecord.Op)this.mOps.get(var2);
         Fragment var4 = var3.fragment;
         if (var4 != null) {
            var4.setNextTransition(FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
         }

         int var5 = var3.cmd;
         if (var5 != 1) {
            switch(var5) {
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
               break;
            case 8:
               this.mManager.setPrimaryNavigationFragment((Fragment)null);
               break;
            case 9:
               this.mManager.setPrimaryNavigationFragment(var4);
               break;
            default:
               StringBuilder var6 = new StringBuilder();
               var6.append("Unknown cmd: ");
               var6.append(var3.cmd);
               throw new IllegalArgumentException(var6.toString());
            }
         } else {
            var4.setNextAnim(var3.popExitAnim);
            this.mManager.removeFragment(var4);
         }

         if (!this.mReorderingAllowed && var3.cmd != 3 && var4 != null) {
            this.mManager.moveFragmentToExpectedState(var4);
         }
      }

      if (!this.mReorderingAllowed && var1) {
         this.mManager.moveToState(this.mManager.mCurState, true);
      }

   }

   Fragment expandOps(ArrayList var1, Fragment var2) {
      int var3 = 0;

      Fragment var4;
      for(var4 = var2; var3 < this.mOps.size(); var4 = var2) {
         BackStackRecord.Op var5 = (BackStackRecord.Op)this.mOps.get(var3);
         int var6;
         switch(var5.cmd) {
         case 1:
         case 7:
            var1.add(var5.fragment);
            var6 = var3;
            var2 = var4;
            break;
         case 2:
            Fragment var7 = var5.fragment;
            int var8 = var7.mContainerId;
            int var9 = var1.size() - 1;
            var2 = var4;

            boolean var12;
            boolean var14;
            for(var14 = false; var9 >= 0; var14 = var12) {
               Fragment var10 = (Fragment)var1.get(var9);
               int var11 = var3;
               var4 = var2;
               var12 = var14;
               if (var10.mContainerId == var8) {
                  if (var10 == var7) {
                     var12 = true;
                     var11 = var3;
                     var4 = var2;
                  } else {
                     var11 = var3;
                     var4 = var2;
                     if (var10 == var2) {
                        this.mOps.add(var3, new BackStackRecord.Op(9, var10));
                        var11 = var3 + 1;
                        var4 = null;
                     }

                     BackStackRecord.Op var13 = new BackStackRecord.Op(3, var10);
                     var13.enterAnim = var5.enterAnim;
                     var13.popEnterAnim = var5.popEnterAnim;
                     var13.exitAnim = var5.exitAnim;
                     var13.popExitAnim = var5.popExitAnim;
                     this.mOps.add(var11, var13);
                     var1.remove(var10);
                     ++var11;
                     var12 = var14;
                  }
               }

               --var9;
               var3 = var11;
               var2 = var4;
            }

            if (var14) {
               this.mOps.remove(var3);
               --var3;
            } else {
               var5.cmd = 1;
               var1.add(var7);
            }

            var6 = var3;
            break;
         case 3:
         case 6:
            var1.remove(var5.fragment);
            var2 = var4;
            var6 = var3;
            if (var5.fragment == var4) {
               this.mOps.add(var3, new BackStackRecord.Op(9, var5.fragment));
               var6 = var3 + 1;
               var2 = null;
            }
            break;
         case 4:
         case 5:
         default:
            var2 = var4;
            var6 = var3;
            break;
         case 8:
            this.mOps.add(var3, new BackStackRecord.Op(9, var4));
            var6 = var3 + 1;
            var2 = var5.fragment;
         }

         var3 = var6 + 1;
      }

      return var4;
   }

   public boolean generateOps(ArrayList var1, ArrayList var2) {
      if (FragmentManagerImpl.DEBUG) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Run: ");
         var3.append(this);
         Log.v("FragmentManager", var3.toString());
      }

      var1.add(this);
      var2.add(false);
      if (this.mAddToBackStack) {
         this.mManager.addBackStackState(this);
      }

      return true;
   }

   public CharSequence getBreadCrumbShortTitle() {
      return this.mBreadCrumbShortTitleRes != 0 ? this.mManager.mHost.getContext().getText(this.mBreadCrumbShortTitleRes) : this.mBreadCrumbShortTitleText;
   }

   public int getBreadCrumbShortTitleRes() {
      return this.mBreadCrumbShortTitleRes;
   }

   public CharSequence getBreadCrumbTitle() {
      return this.mBreadCrumbTitleRes != 0 ? this.mManager.mHost.getContext().getText(this.mBreadCrumbTitleRes) : this.mBreadCrumbTitleText;
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
      this.addOp(new BackStackRecord.Op(4, var1));
      return this;
   }

   boolean interactsWith(int var1) {
      int var2 = this.mOps.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         BackStackRecord.Op var4 = (BackStackRecord.Op)this.mOps.get(var3);
         int var5;
         if (var4.fragment != null) {
            var5 = var4.fragment.mContainerId;
         } else {
            var5 = 0;
         }

         if (var5 != 0 && var5 == var1) {
            return true;
         }
      }

      return false;
   }

   boolean interactsWith(ArrayList var1, int var2, int var3) {
      if (var3 == var2) {
         return false;
      } else {
         int var4 = this.mOps.size();
         int var5 = -1;

         int var9;
         for(int var6 = 0; var6 < var4; var5 = var9) {
            BackStackRecord.Op var7 = (BackStackRecord.Op)this.mOps.get(var6);
            int var8;
            if (var7.fragment != null) {
               var8 = var7.fragment.mContainerId;
            } else {
               var8 = 0;
            }

            var9 = var5;
            if (var8 != 0) {
               var9 = var5;
               if (var8 != var5) {
                  var5 = var2;

                  while(true) {
                     if (var5 >= var3) {
                        var9 = var8;
                        break;
                     }

                     BackStackRecord var10 = (BackStackRecord)var1.get(var5);
                     int var11 = var10.mOps.size();

                     for(var9 = 0; var9 < var11; ++var9) {
                        var7 = (BackStackRecord.Op)var10.mOps.get(var9);
                        int var12;
                        if (var7.fragment != null) {
                           var12 = var7.fragment.mContainerId;
                        } else {
                           var12 = 0;
                        }

                        if (var12 == var8) {
                           return true;
                        }
                     }

                     ++var5;
                  }
               }
            }

            ++var6;
         }

         return false;
      }
   }

   public boolean isAddToBackStackAllowed() {
      return this.mAllowAddToBackStack;
   }

   public boolean isEmpty() {
      return this.mOps.isEmpty();
   }

   boolean isPostponed() {
      for(int var1 = 0; var1 < this.mOps.size(); ++var1) {
         if (isFragmentPostponed((BackStackRecord.Op)this.mOps.get(var1))) {
            return true;
         }
      }

      return false;
   }

   public FragmentTransaction remove(Fragment var1) {
      this.addOp(new BackStackRecord.Op(3, var1));
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

   public FragmentTransaction runOnCommit(Runnable var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("runnable cannot be null");
      } else {
         this.disallowAddToBackStack();
         if (this.mCommitRunnables == null) {
            this.mCommitRunnables = new ArrayList();
         }

         this.mCommitRunnables.add(var1);
         return this;
      }
   }

   public void runOnCommitRunnables() {
      if (this.mCommitRunnables != null) {
         int var1 = 0;

         for(int var2 = this.mCommitRunnables.size(); var1 < var2; ++var1) {
            ((Runnable)this.mCommitRunnables.get(var1)).run();
         }

         this.mCommitRunnables = null;
      }

   }

   public FragmentTransaction setAllowOptimization(boolean var1) {
      return this.setReorderingAllowed(var1);
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

   public FragmentTransaction setPrimaryNavigationFragment(Fragment var1) {
      this.addOp(new BackStackRecord.Op(8, var1));
      return this;
   }

   public FragmentTransaction setReorderingAllowed(boolean var1) {
      this.mReorderingAllowed = var1;
      return this;
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
      this.addOp(new BackStackRecord.Op(5, var1));
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

   Fragment trackAddedFragmentsInPop(ArrayList var1, Fragment var2) {
      for(int var3 = 0; var3 < this.mOps.size(); ++var3) {
         BackStackRecord.Op var4;
         label36: {
            var4 = (BackStackRecord.Op)this.mOps.get(var3);
            int var5 = var4.cmd;
            if (var5 != 1) {
               if (var5 == 3) {
                  break label36;
               }

               switch(var5) {
               case 6:
                  break label36;
               case 7:
                  break;
               case 8:
                  var2 = null;
                  continue;
               case 9:
                  var2 = var4.fragment;
               default:
                  continue;
               }
            }

            var1.remove(var4.fragment);
            continue;
         }

         var1.add(var4.fragment);
      }

      return var2;
   }

   static final class Op {
      int cmd;
      int enterAnim;
      int exitAnim;
      Fragment fragment;
      int popEnterAnim;
      int popExitAnim;

      Op() {
      }

      Op(int var1, Fragment var2) {
         this.cmd = var1;
         this.fragment = var2;
      }
   }
}
