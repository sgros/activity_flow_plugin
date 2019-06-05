package androidx.versionedparcelable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

class VersionedParcelParcel extends VersionedParcel {
   private int mCurrentField;
   private final int mEnd;
   private int mNextRead;
   private final int mOffset;
   private final Parcel mParcel;
   private final SparseIntArray mPositionLookup;
   private final String mPrefix;

   VersionedParcelParcel(Parcel var1) {
      this(var1, var1.dataPosition(), var1.dataSize(), "");
   }

   VersionedParcelParcel(Parcel var1, int var2, int var3, String var4) {
      this.mPositionLookup = new SparseIntArray();
      this.mCurrentField = -1;
      this.mNextRead = 0;
      this.mParcel = var1;
      this.mOffset = var2;
      this.mEnd = var3;
      this.mNextRead = this.mOffset;
      this.mPrefix = var4;
   }

   private int readUntilField(int var1) {
      while(true) {
         if (this.mNextRead < this.mEnd) {
            this.mParcel.setDataPosition(this.mNextRead);
            int var2 = this.mParcel.readInt();
            int var3 = this.mParcel.readInt();
            this.mNextRead += var2;
            if (var3 != var1) {
               continue;
            }

            return this.mParcel.dataPosition();
         }

         return -1;
      }
   }

   public void closeField() {
      if (this.mCurrentField >= 0) {
         int var1 = this.mPositionLookup.get(this.mCurrentField);
         int var2 = this.mParcel.dataPosition();
         this.mParcel.setDataPosition(var1);
         this.mParcel.writeInt(var2 - var1);
         this.mParcel.setDataPosition(var2);
      }

   }

   protected VersionedParcel createSubParcel() {
      Parcel var1 = this.mParcel;
      int var2 = this.mParcel.dataPosition();
      int var3;
      if (this.mNextRead == this.mOffset) {
         var3 = this.mEnd;
      } else {
         var3 = this.mNextRead;
      }

      StringBuilder var4 = new StringBuilder();
      var4.append(this.mPrefix);
      var4.append("  ");
      return new VersionedParcelParcel(var1, var2, var3, var4.toString());
   }

   public byte[] readByteArray() {
      int var1 = this.mParcel.readInt();
      if (var1 < 0) {
         return null;
      } else {
         byte[] var2 = new byte[var1];
         this.mParcel.readByteArray(var2);
         return var2;
      }
   }

   public boolean readField(int var1) {
      var1 = this.readUntilField(var1);
      if (var1 == -1) {
         return false;
      } else {
         this.mParcel.setDataPosition(var1);
         return true;
      }
   }

   public int readInt() {
      return this.mParcel.readInt();
   }

   public Parcelable readParcelable() {
      return this.mParcel.readParcelable(this.getClass().getClassLoader());
   }

   public String readString() {
      return this.mParcel.readString();
   }

   public void setOutputField(int var1) {
      this.closeField();
      this.mCurrentField = var1;
      this.mPositionLookup.put(var1, this.mParcel.dataPosition());
      this.writeInt(0);
      this.writeInt(var1);
   }

   public void writeByteArray(byte[] var1) {
      if (var1 != null) {
         this.mParcel.writeInt(var1.length);
         this.mParcel.writeByteArray(var1);
      } else {
         this.mParcel.writeInt(-1);
      }

   }

   public void writeInt(int var1) {
      this.mParcel.writeInt(var1);
   }

   public void writeParcelable(Parcelable var1) {
      this.mParcel.writeParcelable(var1, 0);
   }

   public void writeString(String var1) {
      this.mParcel.writeString(var1);
   }
}
