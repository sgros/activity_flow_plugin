package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.Collections;
import java.util.List;

public final class HlsMediaPlaylist extends HlsPlaylist {
   public final int discontinuitySequence;
   public final long durationUs;
   public final boolean hasDiscontinuitySequence;
   public final boolean hasEndTag;
   public final boolean hasProgramDateTime;
   public final long mediaSequence;
   public final int playlistType;
   public final DrmInitData protectionSchemes;
   public final List segments;
   public final long startOffsetUs;
   public final long startTimeUs;
   public final long targetDurationUs;
   public final int version;

   public HlsMediaPlaylist(int var1, String var2, List var3, long var4, long var6, boolean var8, int var9, long var10, int var12, long var13, boolean var15, boolean var16, boolean var17, DrmInitData var18, List var19) {
      super(var2, var3, var15);
      this.playlistType = var1;
      this.startTimeUs = var6;
      this.hasDiscontinuitySequence = var8;
      this.discontinuitySequence = var9;
      this.mediaSequence = var10;
      this.version = var12;
      this.targetDurationUs = var13;
      this.hasEndTag = var16;
      this.hasProgramDateTime = var17;
      this.protectionSchemes = var18;
      this.segments = Collections.unmodifiableList(var19);
      if (!var19.isEmpty()) {
         HlsMediaPlaylist.Segment var20 = (HlsMediaPlaylist.Segment)var19.get(var19.size() - 1);
         this.durationUs = var20.relativeStartTimeUs + var20.durationUs;
      } else {
         this.durationUs = 0L;
      }

      if (var4 == -9223372036854775807L) {
         var4 = -9223372036854775807L;
      } else if (var4 < 0L) {
         var4 += this.durationUs;
      }

      this.startOffsetUs = var4;
   }

   public HlsMediaPlaylist copyWith(long var1, int var3) {
      return new HlsMediaPlaylist(this.playlistType, super.baseUri, super.tags, this.startOffsetUs, var1, true, var3, this.mediaSequence, this.version, this.targetDurationUs, super.hasIndependentSegments, this.hasEndTag, this.hasProgramDateTime, this.protectionSchemes, this.segments);
   }

   public HlsMediaPlaylist copyWithEndTag() {
      return this.hasEndTag ? this : new HlsMediaPlaylist(this.playlistType, super.baseUri, super.tags, this.startOffsetUs, this.startTimeUs, this.hasDiscontinuitySequence, this.discontinuitySequence, this.mediaSequence, this.version, this.targetDurationUs, super.hasIndependentSegments, true, this.hasProgramDateTime, this.protectionSchemes, this.segments);
   }

   public long getEndTimeUs() {
      return this.startTimeUs + this.durationUs;
   }

   public boolean isNewerThan(HlsMediaPlaylist var1) {
      boolean var2 = true;
      boolean var3 = var2;
      if (var1 != null) {
         long var4 = this.mediaSequence;
         long var6 = var1.mediaSequence;
         if (var4 > var6) {
            var3 = var2;
         } else {
            if (var4 < var6) {
               return false;
            }

            int var8 = this.segments.size();
            int var9 = var1.segments.size();
            var3 = var2;
            if (var8 <= var9) {
               if (var8 == var9 && this.hasEndTag && !var1.hasEndTag) {
                  var3 = var2;
               } else {
                  var3 = false;
               }
            }
         }
      }

      return var3;
   }

   public static final class Segment implements Comparable {
      public final long byterangeLength;
      public final long byterangeOffset;
      public final DrmInitData drmInitData;
      public final long durationUs;
      public final String encryptionIV;
      public final String fullSegmentEncryptionKeyUri;
      public final boolean hasGapTag;
      public final HlsMediaPlaylist.Segment initializationSegment;
      public final int relativeDiscontinuitySequence;
      public final long relativeStartTimeUs;
      public final String title;
      public final String url;

      public Segment(String var1, long var2, long var4) {
         this(var1, (HlsMediaPlaylist.Segment)null, "", 0L, -1, -9223372036854775807L, (DrmInitData)null, (String)null, (String)null, var2, var4, false);
      }

      public Segment(String var1, HlsMediaPlaylist.Segment var2, String var3, long var4, int var6, long var7, DrmInitData var9, String var10, String var11, long var12, long var14, boolean var16) {
         this.url = var1;
         this.initializationSegment = var2;
         this.title = var3;
         this.durationUs = var4;
         this.relativeDiscontinuitySequence = var6;
         this.relativeStartTimeUs = var7;
         this.drmInitData = var9;
         this.fullSegmentEncryptionKeyUri = var10;
         this.encryptionIV = var11;
         this.byterangeOffset = var12;
         this.byterangeLength = var14;
         this.hasGapTag = var16;
      }

      public int compareTo(Long var1) {
         byte var2;
         if (this.relativeStartTimeUs > var1) {
            var2 = 1;
         } else if (this.relativeStartTimeUs < var1) {
            var2 = -1;
         } else {
            var2 = 0;
         }

         return var2;
      }
   }
}
