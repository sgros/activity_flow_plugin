package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.dash.DashSegmentIndex;
import java.util.Collections;
import java.util.List;

public abstract class Representation {
   public final String baseUrl;
   public final Format format;
   public final List inbandEventStreams;
   private final RangedUri initializationUri;
   public final long presentationTimeOffsetUs;
   public final long revisionId;

   private Representation(long var1, Format var3, String var4, SegmentBase var5, List var6) {
      this.revisionId = var1;
      this.format = var3;
      this.baseUrl = var4;
      List var7;
      if (var6 == null) {
         var7 = Collections.emptyList();
      } else {
         var7 = Collections.unmodifiableList(var6);
      }

      this.inbandEventStreams = var7;
      this.initializationUri = var5.getInitialization(this);
      this.presentationTimeOffsetUs = var5.getPresentationTimeOffsetUs();
   }

   // $FF: synthetic method
   Representation(long var1, Format var3, String var4, SegmentBase var5, List var6, Object var7) {
      this(var1, var3, var4, var5, var6);
   }

   public static Representation newInstance(long var0, Format var2, String var3, SegmentBase var4, List var5) {
      return newInstance(var0, var2, var3, var4, var5, (String)null);
   }

   public static Representation newInstance(long var0, Format var2, String var3, SegmentBase var4, List var5, String var6) {
      if (var4 instanceof SegmentBase.SingleSegmentBase) {
         return new Representation.SingleSegmentRepresentation(var0, var2, var3, (SegmentBase.SingleSegmentBase)var4, var5, var6, -1L);
      } else if (var4 instanceof SegmentBase.MultiSegmentBase) {
         return new Representation.MultiSegmentRepresentation(var0, var2, var3, (SegmentBase.MultiSegmentBase)var4, var5);
      } else {
         throw new IllegalArgumentException("segmentBase must be of type SingleSegmentBase or MultiSegmentBase");
      }
   }

   public abstract String getCacheKey();

   public abstract DashSegmentIndex getIndex();

   public abstract RangedUri getIndexUri();

   public RangedUri getInitializationUri() {
      return this.initializationUri;
   }

   public static class MultiSegmentRepresentation extends Representation implements DashSegmentIndex {
      private final SegmentBase.MultiSegmentBase segmentBase;

      public MultiSegmentRepresentation(long var1, Format var3, String var4, SegmentBase.MultiSegmentBase var5, List var6) {
         super(var1, var3, var4, var5, var6, null);
         this.segmentBase = var5;
      }

      public String getCacheKey() {
         return null;
      }

      public long getDurationUs(long var1, long var3) {
         return this.segmentBase.getSegmentDurationUs(var1, var3);
      }

      public long getFirstSegmentNum() {
         return this.segmentBase.getFirstSegmentNum();
      }

      public DashSegmentIndex getIndex() {
         return this;
      }

      public RangedUri getIndexUri() {
         return null;
      }

      public int getSegmentCount(long var1) {
         return this.segmentBase.getSegmentCount(var1);
      }

      public long getSegmentNum(long var1, long var3) {
         return this.segmentBase.getSegmentNum(var1, var3);
      }

      public RangedUri getSegmentUrl(long var1) {
         return this.segmentBase.getSegmentUrl(this, var1);
      }

      public long getTimeUs(long var1) {
         return this.segmentBase.getSegmentTimeUs(var1);
      }

      public boolean isExplicit() {
         return this.segmentBase.isExplicit();
      }
   }

   public static class SingleSegmentRepresentation extends Representation {
      private final String cacheKey;
      public final long contentLength;
      private final RangedUri indexUri;
      private final SingleSegmentIndex segmentIndex;
      public final Uri uri;

      public SingleSegmentRepresentation(long var1, Format var3, String var4, SegmentBase.SingleSegmentBase var5, List var6, String var7, long var8) {
         super(var1, var3, var4, var5, var6, null);
         this.uri = Uri.parse(var4);
         this.indexUri = var5.getIndex();
         this.cacheKey = var7;
         this.contentLength = var8;
         SingleSegmentIndex var10;
         if (this.indexUri != null) {
            var10 = null;
         } else {
            var10 = new SingleSegmentIndex(new RangedUri((String)null, 0L, var8));
         }

         this.segmentIndex = var10;
      }

      public String getCacheKey() {
         return this.cacheKey;
      }

      public DashSegmentIndex getIndex() {
         return this.segmentIndex;
      }

      public RangedUri getIndexUri() {
         return this.indexUri;
      }
   }
}
