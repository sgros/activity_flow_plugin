package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SpliceScheduleCommand extends SpliceCommand {
   public static final Creator CREATOR = new Creator() {
      public SpliceScheduleCommand createFromParcel(Parcel var1) {
         return new SpliceScheduleCommand(var1);
      }

      public SpliceScheduleCommand[] newArray(int var1) {
         return new SpliceScheduleCommand[var1];
      }
   };
   public final List events;

   private SpliceScheduleCommand(Parcel var1) {
      int var2 = var1.readInt();
      ArrayList var3 = new ArrayList(var2);

      for(int var4 = 0; var4 < var2; ++var4) {
         var3.add(SpliceScheduleCommand.Event.createFromParcel(var1));
      }

      this.events = Collections.unmodifiableList(var3);
   }

   // $FF: synthetic method
   SpliceScheduleCommand(Parcel var1, Object var2) {
      this(var1);
   }

   private SpliceScheduleCommand(List var1) {
      this.events = Collections.unmodifiableList(var1);
   }

   static SpliceScheduleCommand parseFromSection(ParsableByteArray var0) {
      int var1 = var0.readUnsignedByte();
      ArrayList var2 = new ArrayList(var1);

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.add(SpliceScheduleCommand.Event.parseFromSection(var0));
      }

      return new SpliceScheduleCommand(var2);
   }

   public void writeToParcel(Parcel var1, int var2) {
      int var3 = this.events.size();
      var1.writeInt(var3);

      for(var2 = 0; var2 < var3; ++var2) {
         ((SpliceScheduleCommand.Event)this.events.get(var2)).writeToParcel(var1);
      }

   }

   public static final class ComponentSplice {
      public final int componentTag;
      public final long utcSpliceTime;

      private ComponentSplice(int var1, long var2) {
         this.componentTag = var1;
         this.utcSpliceTime = var2;
      }

      // $FF: synthetic method
      ComponentSplice(int var1, long var2, Object var4) {
         this(var1, var2);
      }

      private static SpliceScheduleCommand.ComponentSplice createFromParcel(Parcel var0) {
         return new SpliceScheduleCommand.ComponentSplice(var0.readInt(), var0.readLong());
      }

      private void writeToParcel(Parcel var1) {
         var1.writeInt(this.componentTag);
         var1.writeLong(this.utcSpliceTime);
      }
   }

   public static final class Event {
      public final boolean autoReturn;
      public final int availNum;
      public final int availsExpected;
      public final long breakDurationUs;
      public final List componentSpliceList;
      public final boolean outOfNetworkIndicator;
      public final boolean programSpliceFlag;
      public final boolean spliceEventCancelIndicator;
      public final long spliceEventId;
      public final int uniqueProgramId;
      public final long utcSpliceTime;

      private Event(long var1, boolean var3, boolean var4, boolean var5, List var6, long var7, boolean var9, long var10, int var12, int var13, int var14) {
         this.spliceEventId = var1;
         this.spliceEventCancelIndicator = var3;
         this.outOfNetworkIndicator = var4;
         this.programSpliceFlag = var5;
         this.componentSpliceList = Collections.unmodifiableList(var6);
         this.utcSpliceTime = var7;
         this.autoReturn = var9;
         this.breakDurationUs = var10;
         this.uniqueProgramId = var12;
         this.availNum = var13;
         this.availsExpected = var14;
      }

      private Event(Parcel var1) {
         this.spliceEventId = var1.readLong();
         byte var2 = var1.readByte();
         boolean var3 = false;
         boolean var4;
         if (var2 == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.spliceEventCancelIndicator = var4;
         if (var1.readByte() == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.outOfNetworkIndicator = var4;
         if (var1.readByte() == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.programSpliceFlag = var4;
         int var5 = var1.readInt();
         ArrayList var6 = new ArrayList(var5);

         for(int var7 = 0; var7 < var5; ++var7) {
            var6.add(SpliceScheduleCommand.ComponentSplice.createFromParcel(var1));
         }

         this.componentSpliceList = Collections.unmodifiableList(var6);
         this.utcSpliceTime = var1.readLong();
         var4 = var3;
         if (var1.readByte() == 1) {
            var4 = true;
         }

         this.autoReturn = var4;
         this.breakDurationUs = var1.readLong();
         this.uniqueProgramId = var1.readInt();
         this.availNum = var1.readInt();
         this.availsExpected = var1.readInt();
      }

      private static SpliceScheduleCommand.Event createFromParcel(Parcel var0) {
         return new SpliceScheduleCommand.Event(var0);
      }

      private static SpliceScheduleCommand.Event parseFromSection(ParsableByteArray var0) {
         long var1 = var0.readUnsignedInt();
         boolean var3;
         if ((var0.readUnsignedByte() & 128) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         ArrayList var4 = new ArrayList();
         int var5;
         boolean var6;
         boolean var7;
         long var8;
         int var10;
         int var11;
         long var12;
         boolean var14;
         if (!var3) {
            var5 = var0.readUnsignedByte();
            if ((var5 & 128) != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            if ((var5 & 64) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            boolean var17;
            if ((var5 & 32) != 0) {
               var17 = true;
            } else {
               var17 = false;
            }

            if (var7) {
               var8 = var0.readUnsignedInt();
            } else {
               var8 = -9223372036854775807L;
            }

            if (!var7) {
               var10 = var0.readUnsignedByte();
               var4 = new ArrayList(var10);

               for(var11 = 0; var11 < var10; ++var11) {
                  var4.add(new SpliceScheduleCommand.ComponentSplice(var0.readUnsignedByte(), var0.readUnsignedInt()));
               }
            }

            if (var17) {
               var12 = (long)var0.readUnsignedByte();
               if ((128L & var12) != 0L) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               var12 = ((var12 & 1L) << 32 | var0.readUnsignedInt()) * 1000L / 90L;
            } else {
               var14 = false;
               var12 = -9223372036854775807L;
            }

            var11 = var0.readUnsignedShort();
            var5 = var0.readUnsignedByte();
            var10 = var0.readUnsignedByte();
            long var15 = var12;
            var12 = var8;
            var8 = var15;
         } else {
            var6 = false;
            var12 = -9223372036854775807L;
            var14 = false;
            var8 = -9223372036854775807L;
            var11 = 0;
            var5 = 0;
            var10 = 0;
            var7 = false;
         }

         return new SpliceScheduleCommand.Event(var1, var3, var6, var7, var4, var12, var14, var8, var11, var5, var10);
      }

      private void writeToParcel(Parcel var1) {
         var1.writeLong(this.spliceEventId);
         var1.writeByte((byte)this.spliceEventCancelIndicator);
         var1.writeByte((byte)this.outOfNetworkIndicator);
         var1.writeByte((byte)this.programSpliceFlag);
         int var2 = this.componentSpliceList.size();
         var1.writeInt(var2);

         for(int var3 = 0; var3 < var2; ++var3) {
            ((SpliceScheduleCommand.ComponentSplice)this.componentSpliceList.get(var3)).writeToParcel(var1);
         }

         var1.writeLong(this.utcSpliceTime);
         var1.writeByte((byte)this.autoReturn);
         var1.writeLong(this.breakDurationUs);
         var1.writeInt(this.uniqueProgramId);
         var1.writeInt(this.availNum);
         var1.writeInt(this.availsExpected);
      }
   }
}
