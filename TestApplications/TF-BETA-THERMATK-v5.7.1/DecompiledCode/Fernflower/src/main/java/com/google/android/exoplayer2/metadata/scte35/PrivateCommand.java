package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.ParsableByteArray;

public final class PrivateCommand extends SpliceCommand {
   public static final Creator CREATOR = new Creator() {
      public PrivateCommand createFromParcel(Parcel var1) {
         return new PrivateCommand(var1);
      }

      public PrivateCommand[] newArray(int var1) {
         return new PrivateCommand[var1];
      }
   };
   public final byte[] commandBytes;
   public final long identifier;
   public final long ptsAdjustment;

   private PrivateCommand(long var1, byte[] var3, long var4) {
      this.ptsAdjustment = var4;
      this.identifier = var1;
      this.commandBytes = var3;
   }

   private PrivateCommand(Parcel var1) {
      this.ptsAdjustment = var1.readLong();
      this.identifier = var1.readLong();
      this.commandBytes = new byte[var1.readInt()];
      var1.readByteArray(this.commandBytes);
   }

   // $FF: synthetic method
   PrivateCommand(Parcel var1, Object var2) {
      this(var1);
   }

   static PrivateCommand parseFromSection(ParsableByteArray var0, int var1, long var2) {
      long var4 = var0.readUnsignedInt();
      byte[] var6 = new byte[var1 - 4];
      var0.readBytes(var6, 0, var6.length);
      return new PrivateCommand(var4, var6, var2);
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeLong(this.ptsAdjustment);
      var1.writeLong(this.identifier);
      var1.writeInt(this.commandBytes.length);
      var1.writeByteArray(this.commandBytes);
   }
}
