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
      boolean var2 = true;
      super();
      this.mClassName = var1.readString();
      this.mIndex = var1.readInt();
      boolean var3;
      if (var1.readInt() != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.mFromLayout = var3;
      this.mFragmentId = var1.readInt();
      this.mContainerId = var1.readInt();
      this.mTag = var1.readString();
      if (var1.readInt() != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.mRetainInstance = var3;
      if (var1.readInt() != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.mDetached = var3;
      this.mArguments = var1.readBundle();
      if (var1.readInt() != 0) {
         var3 = var2;
      } else {
         var3 = false;
      }

      this.mHidden = var3;
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

   public Fragment instantiate(FragmentHostCallback var1, Fragment var2, FragmentManagerNonConfig var3) {
      if (this.mInstance == null) {
         Context var4 = var1.getContext();
         if (this.mArguments != null) {
            this.mArguments.setClassLoader(var4.getClassLoader());
         }

         this.mInstance = Fragment.instantiate(var4, this.mClassName, this.mArguments);
         if (this.mSavedFragmentState != null) {
            this.mSavedFragmentState.setClassLoader(var4.getClassLoader());
            this.mInstance.mSavedFragmentState = this.mSavedFragmentState;
         }

         this.mInstance.setIndex(this.mIndex, var2);
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
            Log.v("FragmentManager", "Instantiated fragment " + this.mInstance);
         }
      }

      this.mInstance.mChildNonConfig = var3;
      return this.mInstance;
   }

   public void writeToParcel(Parcel var1, int var2) {
      byte var3 = 1;
      var1.writeString(this.mClassName);
      var1.writeInt(this.mIndex);
      byte var4;
      if (this.mFromLayout) {
         var4 = 1;
      } else {
         var4 = 0;
      }

      var1.writeInt(var4);
      var1.writeInt(this.mFragmentId);
      var1.writeInt(this.mContainerId);
      var1.writeString(this.mTag);
      if (this.mRetainInstance) {
         var4 = 1;
      } else {
         var4 = 0;
      }

      var1.writeInt(var4);
      if (this.mDetached) {
         var4 = 1;
      } else {
         var4 = 0;
      }

      var1.writeInt(var4);
      var1.writeBundle(this.mArguments);
      if (this.mHidden) {
         var4 = var3;
      } else {
         var4 = 0;
      }

      var1.writeInt(var4);
      var1.writeBundle(this.mSavedFragmentState);
   }
}
