package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

final class FragmentState implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public FragmentState createFromParcel(Parcel var1) {
         return new FragmentState(var1);
      }

      public FragmentState[] newArray(int var1) {
         return new FragmentState[var1];
      }
   };
   final Bundle mArguments;
   final String mClassName;
   final int mContainerId;
   final boolean mDetached;
   final int mFragmentId;
   final boolean mFromLayout;
   final boolean mHidden;
   final int mIndex;
   Fragment mInstance;
   final boolean mRetainInstance;
   Bundle mSavedFragmentState;
   final String mTag;

   public FragmentState(Parcel var1) {
      this.mClassName = var1.readString();
      this.mIndex = var1.readInt();
      int var2 = var1.readInt();
      boolean var3 = false;
      boolean var4;
      if (var2 != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.mFromLayout = var4;
      this.mFragmentId = var1.readInt();
      this.mContainerId = var1.readInt();
      this.mTag = var1.readString();
      if (var1.readInt() != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.mRetainInstance = var4;
      if (var1.readInt() != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.mDetached = var4;
      this.mArguments = var1.readBundle();
      var4 = var3;
      if (var1.readInt() != 0) {
         var4 = true;
      }

      this.mHidden = var4;
      this.mSavedFragmentState = var1.readBundle();
   }

   public FragmentState(Fragment var1) {
      this.mClassName = var1.getClass().getName();
      this.mIndex = var1.mIndex;
      this.mFromLayout = var1.mFromLayout;
      this.mFragmentId = var1.mFragmentId;
      this.mContainerId = var1.mContainerId;
      this.mTag = var1.mTag;
      this.mRetainInstance = var1.mRetainInstance;
      this.mDetached = var1.mDetached;
      this.mArguments = var1.mArguments;
      this.mHidden = var1.mHidden;
   }

   public int describeContents() {
      return 0;
   }

   public Fragment instantiate(FragmentHostCallback var1, FragmentContainer var2, Fragment var3, FragmentManagerNonConfig var4) {
      if (this.mInstance == null) {
         Context var5 = var1.getContext();
         if (this.mArguments != null) {
            this.mArguments.setClassLoader(var5.getClassLoader());
         }

         if (var2 != null) {
            this.mInstance = var2.instantiate(var5, this.mClassName, this.mArguments);
         } else {
            this.mInstance = Fragment.instantiate(var5, this.mClassName, this.mArguments);
         }

         if (this.mSavedFragmentState != null) {
            this.mSavedFragmentState.setClassLoader(var5.getClassLoader());
            this.mInstance.mSavedFragmentState = this.mSavedFragmentState;
         }

         this.mInstance.setIndex(this.mIndex, var3);
         this.mInstance.mFromLayout = this.mFromLayout;
         this.mInstance.mRestored = true;
         this.mInstance.mFragmentId = this.mFragmentId;
         this.mInstance.mContainerId = this.mContainerId;
         this.mInstance.mTag = this.mTag;
         this.mInstance.mRetainInstance = this.mRetainInstance;
         this.mInstance.mDetached = this.mDetached;
         this.mInstance.mHidden = this.mHidden;
         this.mInstance.mFragmentManager = var1.mFragmentManager;
         if (FragmentManagerImpl.DEBUG) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Instantiated fragment ");
            var6.append(this.mInstance);
            Log.v("FragmentManager", var6.toString());
         }
      }

      this.mInstance.mChildNonConfig = var4;
      return this.mInstance;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.mClassName);
      var1.writeInt(this.mIndex);
      var1.writeInt(this.mFromLayout);
      var1.writeInt(this.mFragmentId);
      var1.writeInt(this.mContainerId);
      var1.writeString(this.mTag);
      var1.writeInt(this.mRetainInstance);
      var1.writeInt(this.mDetached);
      var1.writeBundle(this.mArguments);
      var1.writeInt(this.mHidden);
      var1.writeBundle(this.mSavedFragmentState);
   }
}
