package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SpliceInsertCommand extends SpliceCommand {
   public static final Creator CREATOR = new Creator() {
      public SpliceInsertCommand createFromParcel(Parcel var1) {
         return new SpliceInsertCommand(var1);
      }

      public SpliceInsertCommand[] newArray(int var1) {
         return new SpliceInsertCommand[var1];
      }
   };
   public final boolean autoReturn;
   public final int availNum;
   public final int availsExpected;
   public final long breakDurationUs;
   public final List componentSpliceList;
   public final boolean outOfNetworkIndicator;
   public final boolean programSpliceFlag;
   public final long programSplicePlaybackPositionUs;
   public final long programSplicePts;
   public final boolean spliceEventCancelIndicator;
   public final long spliceEventId;
   public final boolean spliceImmediateFlag;
   public final int uniqueProgramId;

   private SpliceInsertCommand(long var1, boolean var3, boolean var4, boolean var5, boolean var6, long var7, long var9, List var11, boolean var12, long var13, int var15, int var16, int var17) {
      this.spliceEventId = var1;
      this.spliceEventCancelIndicator = var3;
      this.outOfNetworkIndicator = var4;
      this.programSpliceFlag = var5;
      this.spliceImmediateFlag = var6;
      this.programSplicePts = var7;
      this.programSplicePlaybackPositionUs = var9;
      this.componentSpliceList = Collections.unmodifiableList(var11);
      this.autoReturn = var12;
      this.breakDurationUs = var13;
      this.uniqueProgramId = var15;
      this.availNum = var16;
      this.availsExpected = var17;
   }

   private SpliceInsertCommand(Parcel var1) {
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
      if (var1.readByte() == 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.spliceImmediateFlag = var4;
      this.programSplicePts = var1.readLong();
      this.programSplicePlaybackPositionUs = var1.readLong();
      int var5 = var1.readInt();
      ArrayList var6 = new ArrayList(var5);

      for(int var7 = 0; var7 < var5; ++var7) {
         var6.add(SpliceInsertCommand.ComponentSplice.createFromParcel(var1));
      }

      this.componentSpliceList = Collections.unmodifiableList(var6);
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

   // $FF: synthetic method
   SpliceInsertCommand(Parcel var1, Object var2) {
      this(var1);
   }

   static SpliceInsertCommand parseFromSection(ParsableByteArray var0, long var1, TimestampAdjuster var3) {
      long var4 = var0.readUnsignedInt();
      boolean var6;
      if ((var0.readUnsignedByte() & 128) != 0) {
         var6 = true;
      } else {
         var6 = false;
      }

      Object var7 = Collections.emptyList();
      int var8;
      boolean var9;
      boolean var10;
      boolean var12;
      long var13;
      int var15;
      boolean var20;
      Object var22;
      int var23;
      if (!var6) {
         var8 = var0.readUnsignedByte();
         if ((var8 & 128) != 0) {
            var9 = true;
         } else {
            var9 = false;
         }

         if ((var8 & 64) != 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         boolean var11;
         if ((var8 & 32) != 0) {
            var11 = true;
         } else {
            var11 = false;
         }

         if ((var8 & 16) != 0) {
            var12 = true;
         } else {
            var12 = false;
         }

         if (var10 && !var12) {
            var13 = TimeSignalCommand.parseSpliceTime(var0, var1);
         } else {
            var13 = -9223372036854775807L;
         }

         if (!var10) {
            var15 = var0.readUnsignedByte();
            ArrayList var16 = new ArrayList(var15);
            var8 = 0;

            while(true) {
               var7 = var16;
               if (var8 >= var15) {
                  break;
               }

               int var17 = var0.readUnsignedByte();
               long var18;
               if (!var12) {
                  var18 = TimeSignalCommand.parseSpliceTime(var0, var1);
               } else {
                  var18 = -9223372036854775807L;
               }

               var16.add(new SpliceInsertCommand.ComponentSplice(var17, var18, var3.adjustTsTimestamp(var18)));
               ++var8;
            }
         }

         if (var11) {
            var1 = (long)var0.readUnsignedByte();
            if ((128L & var1) != 0L) {
               var20 = true;
            } else {
               var20 = false;
            }

            var1 = ((var1 & 1L) << 32 | var0.readUnsignedInt()) * 1000L / 90L;
         } else {
            var1 = -9223372036854775807L;
            var20 = false;
         }

         var15 = var0.readUnsignedShort();
         var23 = var0.readUnsignedByte();
         var8 = var0.readUnsignedByte();
         var22 = var7;
         boolean var21 = var20;
         var20 = var9;
         var9 = var21;
      } else {
         var22 = var7;
         var20 = false;
         var12 = false;
         var13 = -9223372036854775807L;
         var9 = false;
         var1 = -9223372036854775807L;
         var15 = 0;
         var23 = 0;
         var8 = 0;
         var10 = false;
      }

      return new SpliceInsertCommand(var4, var6, var20, var10, var12, var13, var3.adjustTsTimestamp(var13), (List)var22, var9, var1, var15, var23, var8);
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeLong(this.spliceEventId);
      var1.writeByte((byte)this.spliceEventCancelIndicator);
      var1.writeByte((byte)this.outOfNetworkIndicator);
      var1.writeByte((byte)this.programSpliceFlag);
      var1.writeByte((byte)this.spliceImmediateFlag);
      var1.writeLong(this.programSplicePts);
      var1.writeLong(this.programSplicePlaybackPositionUs);
      int var3 = this.componentSpliceList.size();
      var1.writeInt(var3);

      for(var2 = 0; var2 < var3; ++var2) {
         ((SpliceInsertCommand.ComponentSplice)this.componentSpliceList.get(var2)).writeToParcel(var1);
      }

      var1.writeByte((byte)this.autoReturn);
      var1.writeLong(this.breakDurationUs);
      var1.writeInt(this.uniqueProgramId);
      var1.writeInt(this.availNum);
      var1.writeInt(this.availsExpected);
   }

   public static final class ComponentSplice {
      public final long componentSplicePlaybackPositionUs;
      public final long componentSplicePts;
      public final int componentTag;

      private ComponentSplice(int var1, long var2, long var4) {
         this.componentTag = var1;
         this.componentSplicePts = var2;
         this.componentSplicePlaybackPositionUs = var4;
      }

      // $FF: synthetic method
      ComponentSplice(int var1, long var2, long var4, Object var6) {
         this(var1, var2, var4);
      }

      public static SpliceInsertCommand.ComponentSplice createFromParcel(Parcel var0) {
         return new SpliceInsertCommand.ComponentSplice(var0.readInt(), var0.readLong(), var0.readLong());
      }

      public void writeToParcel(Parcel var1) {
         var1.writeInt(this.componentTag);
         var1.writeLong(this.componentSplicePts);
         var1.writeLong(this.componentSplicePlaybackPositionUs);
      }
   }
}
