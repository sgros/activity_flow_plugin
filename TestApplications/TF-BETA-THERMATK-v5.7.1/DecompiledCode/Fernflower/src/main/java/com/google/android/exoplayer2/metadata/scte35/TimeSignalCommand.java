package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;

public final class TimeSignalCommand extends SpliceCommand {
   public static final Creator CREATOR = new Creator() {
      public TimeSignalCommand createFromParcel(Parcel var1) {
         return new TimeSignalCommand(var1.readLong(), var1.readLong());
      }

      public TimeSignalCommand[] newArray(int var1) {
         return new TimeSignalCommand[var1];
      }
   };
   public final long playbackPositionUs;
   public final long ptsTime;

   private TimeSignalCommand(long var1, long var3) {
      this.ptsTime = var1;
      this.playbackPositionUs = var3;
   }

   // $FF: synthetic method
   TimeSignalCommand(long var1, long var3, Object var5) {
      this(var1, var3);
   }

   static TimeSignalCommand parseFromSection(ParsableByteArray var0, long var1, TimestampAdjuster var3) {
      var1 = parseSpliceTime(var0, var1);
      return new TimeSignalCommand(var1, var3.adjustTsTimestamp(var1));
   }

   static long parseSpliceTime(ParsableByteArray var0, long var1) {
      long var3 = (long)var0.readUnsignedByte();
      if ((128L & var3) != 0L) {
         var1 = 8589934591L & ((var3 & 1L) << 32 | var0.readUnsignedInt()) + var1;
      } else {
         var1 = -9223372036854775807L;
      }

      return var1;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeLong(this.ptsTime);
      var1.writeLong(this.playbackPositionUs);
   }
}
