package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

final class BackStackState implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public BackStackState createFromParcel(Parcel var1) {
         return new BackStackState(var1);
      }

      public BackStackState[] newArray(int var1) {
         return new BackStackState[var1];
      }
   };
   final int mBreadCrumbShortTitleRes;
   final CharSequence mBreadCrumbShortTitleText;
   final int mBreadCrumbTitleRes;
   final CharSequence mBreadCrumbTitleText;
   final int mIndex;
   final String mName;
   final int[] mOps;
   final boolean mReorderingAllowed;
   final ArrayList mSharedElementSourceNames;
   final ArrayList mSharedElementTargetNames;
   final int mTransition;
   final int mTransitionStyle;

   public BackStackState(Parcel var1) {
      this.mOps = var1.createIntArray();
      this.mTransition = var1.readInt();
      this.mTransitionStyle = var1.readInt();
      this.mName = var1.readString();
      this.mIndex = var1.readInt();
      this.mBreadCrumbTitleRes = var1.readInt();
      this.mBreadCrumbTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
      this.mBreadCrumbShortTitleRes = var1.readInt();
      this.mBreadCrumbShortTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
      this.mSharedElementSourceNames = var1.createStringArrayList();
      this.mSharedElementTargetNames = var1.createStringArrayList();
      boolean var2;
      if (var1.readInt() != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mReorderingAllowed = var2;
   }

   public BackStackState(BackStackRecord var1) {
      int var2 = var1.mOps.size();
      this.mOps = new int[var2 * 6];
      if (var1.mAddToBackStack) {
         int var3 = 0;

         int var7;
         for(int var4 = 0; var3 < var2; var4 = var7 + 1) {
            BackStackRecord.Op var5 = (BackStackRecord.Op)var1.mOps.get(var3);
            int[] var6 = this.mOps;
            var7 = var4 + 1;
            var6[var4] = var5.cmd;
            var6 = this.mOps;
            int var8 = var7 + 1;
            if (var5.fragment != null) {
               var4 = var5.fragment.mIndex;
            } else {
               var4 = -1;
            }

            var6[var7] = var4;
            var6 = this.mOps;
            var7 = var8 + 1;
            var6[var8] = var5.enterAnim;
            var6 = this.mOps;
            var4 = var7 + 1;
            var6[var7] = var5.exitAnim;
            var6 = this.mOps;
            var7 = var4 + 1;
            var6[var4] = var5.popEnterAnim;
            this.mOps[var7] = var5.popExitAnim;
            ++var3;
         }

         this.mTransition = var1.mTransition;
         this.mTransitionStyle = var1.mTransitionStyle;
         this.mName = var1.mName;
         this.mIndex = var1.mIndex;
         this.mBreadCrumbTitleRes = var1.mBreadCrumbTitleRes;
         this.mBreadCrumbTitleText = var1.mBreadCrumbTitleText;
         this.mBreadCrumbShortTitleRes = var1.mBreadCrumbShortTitleRes;
         this.mBreadCrumbShortTitleText = var1.mBreadCrumbShortTitleText;
         this.mSharedElementSourceNames = var1.mSharedElementSourceNames;
         this.mSharedElementTargetNames = var1.mSharedElementTargetNames;
         this.mReorderingAllowed = var1.mReorderingAllowed;
      } else {
         throw new IllegalStateException("Not on back stack");
      }
   }

   public int describeContents() {
      return 0;
   }

   public BackStackRecord instantiate(FragmentManagerImpl var1) {
      BackStackRecord var2 = new BackStackRecord(var1);
      int var3 = 0;

      int var7;
      for(int var4 = 0; var3 < this.mOps.length; var3 = var7 + 1) {
         BackStackRecord.Op var5 = new BackStackRecord.Op();
         int[] var6 = this.mOps;
         var7 = var3 + 1;
         var5.cmd = var6[var3];
         if (FragmentManagerImpl.DEBUG) {
            StringBuilder var8 = new StringBuilder();
            var8.append("Instantiate ");
            var8.append(var2);
            var8.append(" op #");
            var8.append(var4);
            var8.append(" base fragment #");
            var8.append(this.mOps[var7]);
            Log.v("FragmentManager", var8.toString());
         }

         var6 = this.mOps;
         var3 = var7 + 1;
         var7 = var6[var7];
         if (var7 >= 0) {
            var5.fragment = (Fragment)var1.mActive.get(var7);
         } else {
            var5.fragment = null;
         }

         var6 = this.mOps;
         var7 = var3 + 1;
         var5.enterAnim = var6[var3];
         var6 = this.mOps;
         var3 = var7 + 1;
         var5.exitAnim = var6[var7];
         var6 = this.mOps;
         var7 = var3 + 1;
         var5.popEnterAnim = var6[var3];
         var5.popExitAnim = this.mOps[var7];
         var2.mEnterAnim = var5.enterAnim;
         var2.mExitAnim = var5.exitAnim;
         var2.mPopEnterAnim = var5.popEnterAnim;
         var2.mPopExitAnim = var5.popExitAnim;
         var2.addOp(var5);
         ++var4;
      }

      var2.mTransition = this.mTransition;
      var2.mTransitionStyle = this.mTransitionStyle;
      var2.mName = this.mName;
      var2.mIndex = this.mIndex;
      var2.mAddToBackStack = true;
      var2.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
      var2.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
      var2.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
      var2.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
      var2.mSharedElementSourceNames = this.mSharedElementSourceNames;
      var2.mSharedElementTargetNames = this.mSharedElementTargetNames;
      var2.mReorderingAllowed = this.mReorderingAllowed;
      var2.bumpBackStackNesting(1);
      return var2;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeIntArray(this.mOps);
      var1.writeInt(this.mTransition);
      var1.writeInt(this.mTransitionStyle);
      var1.writeString(this.mName);
      var1.writeInt(this.mIndex);
      var1.writeInt(this.mBreadCrumbTitleRes);
      TextUtils.writeToParcel(this.mBreadCrumbTitleText, var1, 0);
      var1.writeInt(this.mBreadCrumbShortTitleRes);
      TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, var1, 0);
      var1.writeStringList(this.mSharedElementSourceNames);
      var1.writeStringList(this.mSharedElementTargetNames);
      var1.writeInt(this.mReorderingAllowed);
   }
}
