package com.google.android.exoplayer2.metadata.scte35;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataDecoder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.nio.ByteBuffer;

public final class SpliceInfoDecoder implements MetadataDecoder {
   private final ParsableByteArray sectionData = new ParsableByteArray();
   private final ParsableBitArray sectionHeader = new ParsableBitArray();
   private TimestampAdjuster timestampAdjuster;

   public Metadata decode(MetadataInputBuffer var1) {
      TimestampAdjuster var2 = this.timestampAdjuster;
      if (var2 == null || var1.subsampleOffsetUs != var2.getTimestampOffsetUs()) {
         this.timestampAdjuster = new TimestampAdjuster(var1.timeUs);
         this.timestampAdjuster.adjustSampleTimestamp(var1.timeUs - var1.subsampleOffsetUs);
      }

      ByteBuffer var9 = var1.data;
      byte[] var7 = var9.array();
      int var3 = var9.limit();
      this.sectionData.reset(var7, var3);
      this.sectionHeader.reset(var7, var3);
      this.sectionHeader.skipBits(39);
      long var4 = (long)this.sectionHeader.readBits(1) << 32 | (long)this.sectionHeader.readBits(32);
      this.sectionHeader.skipBits(20);
      var3 = this.sectionHeader.readBits(12);
      int var6 = this.sectionHeader.readBits(8);
      Object var8 = null;
      this.sectionData.skipBytes(14);
      if (var6 != 0) {
         if (var6 != 255) {
            if (var6 != 4) {
               if (var6 != 5) {
                  if (var6 == 6) {
                     var8 = TimeSignalCommand.parseFromSection(this.sectionData, var4, this.timestampAdjuster);
                  }
               } else {
                  var8 = SpliceInsertCommand.parseFromSection(this.sectionData, var4, this.timestampAdjuster);
               }
            } else {
               var8 = SpliceScheduleCommand.parseFromSection(this.sectionData);
            }
         } else {
            var8 = PrivateCommand.parseFromSection(this.sectionData, var3, var4);
         }
      } else {
         var8 = new SpliceNullCommand();
      }

      Metadata var10;
      if (var8 == null) {
         var10 = new Metadata(new Metadata.Entry[0]);
      } else {
         var10 = new Metadata(new Metadata.Entry[]{(Metadata.Entry)var8});
      }

      return var10;
   }
}
