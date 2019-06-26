package pl.droidsonroids.gif;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.view.View.BaseSavedState;

class GifViewSavedState extends BaseSavedState {
   public static final Creator CREATOR = new Creator() {
      public GifViewSavedState createFromParcel(Parcel var1) {
         return new GifViewSavedState(var1);
      }

      public GifViewSavedState[] newArray(int var1) {
         return new GifViewSavedState[var1];
      }
   };
   final long[][] mStates;

   private GifViewSavedState(Parcel var1) {
      super(var1);
      this.mStates = new long[var1.readInt()][];

      for(int var2 = 0; var2 < this.mStates.length; ++var2) {
         this.mStates[var2] = var1.createLongArray();
      }

   }

   // $FF: synthetic method
   GifViewSavedState(Parcel var1, Object var2) {
      this(var1);
   }

   GifViewSavedState(Parcelable var1, long[] var2) {
      super(var1);
      this.mStates = new long[1][];
      this.mStates[0] = var2;
   }

   GifViewSavedState(Parcelable var1, Drawable... var2) {
      super(var1);
      this.mStates = new long[var2.length][];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Drawable var4 = var2[var3];
         if (var4 instanceof GifDrawable) {
            this.mStates[var3] = ((GifDrawable)var4).mNativeInfoHandle.getSavedState();
         } else {
            this.mStates[var3] = null;
         }
      }

   }

   void restoreState(Drawable var1, int var2) {
      if (this.mStates[var2] != null && var1 instanceof GifDrawable) {
         GifDrawable var3 = (GifDrawable)var1;
         var3.startAnimation((long)var3.mNativeInfoHandle.restoreSavedState(this.mStates[var2], var3.mBuffer));
      }

   }

   public void writeToParcel(@NonNull Parcel var1, int var2) {
      super.writeToParcel(var1, var2);
      var1.writeInt(this.mStates.length);
      long[][] var3 = this.mStates;
      int var4 = var3.length;

      for(var2 = 0; var2 < var4; ++var2) {
         var1.writeLongArray(var3[var2]);
      }

   }
}
