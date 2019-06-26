package locus.api.android.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ParcelableContainer implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public ParcelableContainer createFromParcel(Parcel var1) {
         return new ParcelableContainer(var1);
      }

      public ParcelableContainer[] newArray(int var1) {
         return new ParcelableContainer[var1];
      }
   };
   private byte[] mData;

   private ParcelableContainer(Parcel var1) {
      this.readFromParcel(var1);
   }

   // $FF: synthetic method
   ParcelableContainer(Parcel var1, Object var2) {
      this(var1);
   }

   public ParcelableContainer(byte[] var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("'data' cannot 'null'");
      } else {
         this.mData = var1;
      }
   }

   private void readFromParcel(Parcel var1) {
      this.mData = new byte[var1.readInt()];
      var1.readByteArray(this.mData);
   }

   public int describeContents() {
      return 0;
   }

   public byte[] getData() {
      return this.mData;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mData.length);
      var1.writeByteArray(this.mData);
   }
}
