package androidx.versionedparcelable;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SuppressLint({"BanParcelableUsage"})
public class ParcelImpl implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public ParcelImpl createFromParcel(Parcel var1) {
         return new ParcelImpl(var1);
      }

      public ParcelImpl[] newArray(int var1) {
         return new ParcelImpl[var1];
      }
   };
   private final VersionedParcelable mParcel;

   protected ParcelImpl(Parcel var1) {
      this.mParcel = (new VersionedParcelParcel(var1)).readVersionedParcelable();
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      (new VersionedParcelParcel(var1)).writeVersionedParcelable(this.mParcel);
   }
}
