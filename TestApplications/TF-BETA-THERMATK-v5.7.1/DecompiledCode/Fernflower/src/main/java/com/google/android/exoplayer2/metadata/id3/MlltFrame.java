package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class MlltFrame extends Id3Frame {
   public static final Creator CREATOR = new Creator() {
      public MlltFrame createFromParcel(Parcel var1) {
         return new MlltFrame(var1);
      }

      public MlltFrame[] newArray(int var1) {
         return new MlltFrame[var1];
      }
   };
   public final int bytesBetweenReference;
   public final int[] bytesDeviations;
   public final int millisecondsBetweenReference;
   public final int[] millisecondsDeviations;
   public final int mpegFramesBetweenReference;

   public MlltFrame(int var1, int var2, int var3, int[] var4, int[] var5) {
      super("MLLT");
      this.mpegFramesBetweenReference = var1;
      this.bytesBetweenReference = var2;
      this.millisecondsBetweenReference = var3;
      this.bytesDeviations = var4;
      this.millisecondsDeviations = var5;
   }

   MlltFrame(Parcel var1) {
      super("MLLT");
      this.mpegFramesBetweenReference = var1.readInt();
      this.bytesBetweenReference = var1.readInt();
      this.millisecondsBetweenReference = var1.readInt();
      this.bytesDeviations = var1.createIntArray();
      this.millisecondsDeviations = var1.createIntArray();
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && MlltFrame.class == var1.getClass()) {
         MlltFrame var3 = (MlltFrame)var1;
         if (this.mpegFramesBetweenReference != var3.mpegFramesBetweenReference || this.bytesBetweenReference != var3.bytesBetweenReference || this.millisecondsBetweenReference != var3.millisecondsBetweenReference || !Arrays.equals(this.bytesDeviations, var3.bytesDeviations) || !Arrays.equals(this.millisecondsDeviations, var3.millisecondsDeviations)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return ((((527 + this.mpegFramesBetweenReference) * 31 + this.bytesBetweenReference) * 31 + this.millisecondsBetweenReference) * 31 + Arrays.hashCode(this.bytesDeviations)) * 31 + Arrays.hashCode(this.millisecondsDeviations);
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mpegFramesBetweenReference);
      var1.writeInt(this.bytesBetweenReference);
      var1.writeInt(this.millisecondsBetweenReference);
      var1.writeIntArray(this.bytesDeviations);
      var1.writeIntArray(this.millisecondsDeviations);
   }
}
