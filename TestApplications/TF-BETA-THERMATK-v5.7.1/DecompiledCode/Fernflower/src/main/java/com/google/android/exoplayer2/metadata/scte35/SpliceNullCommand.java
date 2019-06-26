package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class SpliceNullCommand extends SpliceCommand {
   public static final Creator CREATOR = new Creator() {
      public SpliceNullCommand createFromParcel(Parcel var1) {
         return new SpliceNullCommand();
      }

      public SpliceNullCommand[] newArray(int var1) {
         return new SpliceNullCommand[var1];
      }
   };

   public void writeToParcel(Parcel var1, int var2) {
   }
}
