package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer2.offline.FilterableManifest;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import java.util.List;
import java.util.UUID;

public class SsManifest implements FilterableManifest {
   public final long durationUs;
   public final long dvrWindowLengthUs;
   public final boolean isLive;
   public final int lookAheadCount;
   public final int majorVersion;
   public final int minorVersion;
   public final SsManifest.ProtectionElement protectionElement;
   public final SsManifest.StreamElement[] streamElements;

   private SsManifest(int var1, int var2, long var3, long var5, int var7, boolean var8, SsManifest.ProtectionElement var9, SsManifest.StreamElement[] var10) {
      this.majorVersion = var1;
      this.minorVersion = var2;
      this.durationUs = var3;
      this.dvrWindowLengthUs = var5;
      this.lookAheadCount = var7;
      this.isLive = var8;
      this.protectionElement = var9;
      this.streamElements = var10;
   }

   public SsManifest(int var1, int var2, long var3, long var5, long var7, int var9, boolean var10, SsManifest.ProtectionElement var11, SsManifest.StreamElement[] var12) {
      long var13 = -9223372036854775807L;
      if (var5 == 0L) {
         var5 = -9223372036854775807L;
      } else {
         var5 = Util.scaleLargeTimestamp(var5, 1000000L, var3);
      }

      if (var7 == 0L) {
         var3 = var13;
      } else {
         var3 = Util.scaleLargeTimestamp(var7, 1000000L, var3);
      }

      this(var1, var2, var5, var3, var9, var10, var11, var12);
   }

   public static class ProtectionElement {
      public final byte[] data;
      public final TrackEncryptionBox[] trackEncryptionBoxes;
      public final UUID uuid;

      public ProtectionElement(UUID var1, byte[] var2, TrackEncryptionBox[] var3) {
         this.uuid = var1;
         this.data = var2;
         this.trackEncryptionBoxes = var3;
      }
   }

   public static class StreamElement {
      private final String baseUri;
      public final int chunkCount;
      private final List chunkStartTimes;
      private final long[] chunkStartTimesUs;
      private final String chunkTemplate;
      public final int displayHeight;
      public final int displayWidth;
      public final Format[] formats;
      public final String language;
      private final long lastChunkDurationUs;
      public final int maxHeight;
      public final int maxWidth;
      public final String name;
      public final String subType;
      public final long timescale;
      public final int type;

      public StreamElement(String var1, String var2, int var3, String var4, long var5, String var7, int var8, int var9, int var10, int var11, String var12, Format[] var13, List var14, long var15) {
         this(var1, var2, var3, var4, var5, var7, var8, var9, var10, var11, var12, var13, var14, Util.scaleLargeTimestamps(var14, 1000000L, var5), Util.scaleLargeTimestamp(var15, 1000000L, var5));
      }

      private StreamElement(String var1, String var2, int var3, String var4, long var5, String var7, int var8, int var9, int var10, int var11, String var12, Format[] var13, List var14, long[] var15, long var16) {
         this.baseUri = var1;
         this.chunkTemplate = var2;
         this.type = var3;
         this.subType = var4;
         this.timescale = var5;
         this.name = var7;
         this.maxWidth = var8;
         this.maxHeight = var9;
         this.displayWidth = var10;
         this.displayHeight = var11;
         this.language = var12;
         this.formats = var13;
         this.chunkStartTimes = var14;
         this.chunkStartTimesUs = var15;
         this.lastChunkDurationUs = var16;
         this.chunkCount = var14.size();
      }

      public Uri buildRequestUri(int var1, int var2) {
         Format[] var3 = this.formats;
         boolean var4 = true;
         boolean var5;
         if (var3 != null) {
            var5 = true;
         } else {
            var5 = false;
         }

         Assertions.checkState(var5);
         if (this.chunkStartTimes != null) {
            var5 = true;
         } else {
            var5 = false;
         }

         Assertions.checkState(var5);
         if (var2 < this.chunkStartTimes.size()) {
            var5 = var4;
         } else {
            var5 = false;
         }

         Assertions.checkState(var5);
         String var7 = Integer.toString(this.formats[var1].bitrate);
         String var6 = ((Long)this.chunkStartTimes.get(var2)).toString();
         var7 = this.chunkTemplate.replace("{bitrate}", var7).replace("{Bitrate}", var7).replace("{start time}", var6).replace("{start_time}", var6);
         return UriUtil.resolveToUri(this.baseUri, var7);
      }

      public long getChunkDurationUs(int var1) {
         long var2;
         if (var1 == this.chunkCount - 1) {
            var2 = this.lastChunkDurationUs;
         } else {
            long[] var4 = this.chunkStartTimesUs;
            var2 = var4[var1 + 1] - var4[var1];
         }

         return var2;
      }

      public int getChunkIndex(long var1) {
         return Util.binarySearchFloor(this.chunkStartTimesUs, var1, true, true);
      }

      public long getStartTimeUs(int var1) {
         return this.chunkStartTimesUs[var1];
      }
   }
}
