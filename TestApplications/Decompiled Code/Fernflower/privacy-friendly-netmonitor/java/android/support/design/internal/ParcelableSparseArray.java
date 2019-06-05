package android.support.design.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.RestrictTo;
import android.util.SparseArray;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ParcelableSparseArray extends SparseArray implements Parcelable {
   public static final Creator CREATOR = new ClassLoaderCreator() {
      public ParcelableSparseArray createFromParcel(Parcel var1) {
         return new ParcelableSparseArray(var1, (ClassLoader)null);
      }

      public ParcelableSparseArray createFromParcel(Parcel var1, ClassLoader var2) {
         return new ParcelableSparseArray(var1, var2);
      }

      public ParcelableSparseArray[] newArray(int var1) {
         return new ParcelableSparseArray[var1];
      }
   };

   public ParcelableSparseArray() {
   }

   public ParcelableSparseArray(Parcel var1, ClassLoader var2) {
      int var3 = var1.readInt();
      int[] var4 = new int[var3];
      var1.readIntArray(var4);
      Parcelable[] var6 = var1.readParcelableArray(var2);

      for(int var5 = 0; var5 < var3; ++var5) {
         this.put(var4[var5], var6[var5]);
      }

   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      int var3 = this.size();
      int[] var4 = new int[var3];
      Parcelable[] var5 = new Parcelable[var3];

      for(int var6 = 0; var6 < var3; ++var6) {
         var4[var6] = this.keyAt(var6);
         var5[var6] = (Parcelable)this.valueAt(var6);
      }

      var1.writeInt(var3);
      var1.writeIntArray(var4);
      var1.writeParcelableArray(var5, var2);
   }
}