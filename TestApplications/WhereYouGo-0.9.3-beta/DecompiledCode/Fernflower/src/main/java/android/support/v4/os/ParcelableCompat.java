package android.support.v4.os;

import android.os.Parcel;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;

public final class ParcelableCompat {
   private ParcelableCompat() {
   }

   public static Creator newCreator(ParcelableCompatCreatorCallbacks var0) {
      Object var1;
      if (VERSION.SDK_INT >= 13) {
         var1 = ParcelableCompatCreatorHoneycombMR2Stub.instantiate(var0);
      } else {
         var1 = new ParcelableCompat.CompatCreator(var0);
      }

      return (Creator)var1;
   }

   static class CompatCreator implements Creator {
      final ParcelableCompatCreatorCallbacks mCallbacks;

      public CompatCreator(ParcelableCompatCreatorCallbacks var1) {
         this.mCallbacks = var1;
      }

      public Object createFromParcel(Parcel var1) {
         return this.mCallbacks.createFromParcel(var1, (ClassLoader)null);
      }

      public Object[] newArray(int var1) {
         return this.mCallbacks.newArray(var1);
      }
   }
}
