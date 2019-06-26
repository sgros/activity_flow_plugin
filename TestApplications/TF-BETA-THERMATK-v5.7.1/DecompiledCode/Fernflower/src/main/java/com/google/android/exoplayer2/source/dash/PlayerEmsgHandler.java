package com.google.android.exoplayer2.source.dash;

import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.metadata.emsg.EventMessageDecoder;
import com.google.android.exoplayer2.source.SampleQueue;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

public final class PlayerEmsgHandler implements Callback {
   private final Allocator allocator;
   private final EventMessageDecoder decoder;
   private long expiredManifestPublishTimeUs;
   private final Handler handler;
   private boolean isWaitingForManifestRefresh;
   private long lastLoadedChunkEndTimeBeforeRefreshUs;
   private long lastLoadedChunkEndTimeUs;
   private DashManifest manifest;
   private final TreeMap manifestPublishTimeToExpiryTimeUs;
   private final PlayerEmsgHandler.PlayerEmsgCallback playerEmsgCallback;
   private boolean released;

   public PlayerEmsgHandler(DashManifest var1, PlayerEmsgHandler.PlayerEmsgCallback var2, Allocator var3) {
      this.manifest = var1;
      this.playerEmsgCallback = var2;
      this.allocator = var3;
      this.manifestPublishTimeToExpiryTimeUs = new TreeMap();
      this.handler = Util.createHandler(this);
      this.decoder = new EventMessageDecoder();
      this.lastLoadedChunkEndTimeUs = -9223372036854775807L;
      this.lastLoadedChunkEndTimeBeforeRefreshUs = -9223372036854775807L;
   }

   private Entry ceilingExpiryEntryForPublishTime(long var1) {
      return this.manifestPublishTimeToExpiryTimeUs.ceilingEntry(var1);
   }

   private static long getManifestPublishTimeMsInEmsg(EventMessage var0) {
      try {
         long var1 = Util.parseXsDateTime(Util.fromUtf8Bytes(var0.messageData));
         return var1;
      } catch (ParserException var3) {
         return -9223372036854775807L;
      }
   }

   private void handleManifestExpiredMessage(long var1, long var3) {
      Long var5 = (Long)this.manifestPublishTimeToExpiryTimeUs.get(var3);
      if (var5 == null) {
         this.manifestPublishTimeToExpiryTimeUs.put(var3, var1);
      } else if (var5 > var1) {
         this.manifestPublishTimeToExpiryTimeUs.put(var3, var1);
      }

   }

   public static boolean isPlayerEmsgEvent(String var0, String var1) {
      boolean var2;
      if (!"urn:mpeg:dash:event:2012".equals(var0) || !"1".equals(var1) && !"2".equals(var1) && !"3".equals(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private void maybeNotifyDashManifestRefreshNeeded() {
      long var1 = this.lastLoadedChunkEndTimeBeforeRefreshUs;
      if (var1 == -9223372036854775807L || var1 != this.lastLoadedChunkEndTimeUs) {
         this.isWaitingForManifestRefresh = true;
         this.lastLoadedChunkEndTimeBeforeRefreshUs = this.lastLoadedChunkEndTimeUs;
         this.playerEmsgCallback.onDashManifestRefreshRequested();
      }
   }

   private void notifyManifestPublishTimeExpired() {
      this.playerEmsgCallback.onDashManifestPublishTimeExpired(this.expiredManifestPublishTimeUs);
   }

   private void removePreviouslyExpiredManifestPublishTimeValues() {
      Iterator var1 = this.manifestPublishTimeToExpiryTimeUs.entrySet().iterator();

      while(var1.hasNext()) {
         if ((Long)((Entry)var1.next()).getKey() < this.manifest.publishTimeMs) {
            var1.remove();
         }
      }

   }

   public boolean handleMessage(Message var1) {
      if (this.released) {
         return true;
      } else if (var1.what != 1) {
         return false;
      } else {
         PlayerEmsgHandler.ManifestExpiryEventInfo var2 = (PlayerEmsgHandler.ManifestExpiryEventInfo)var1.obj;
         this.handleManifestExpiredMessage(var2.eventTimeUs, var2.manifestPublishTimeMsInEmsg);
         return true;
      }
   }

   boolean maybeRefreshManifestBeforeLoadingNextChunk(long var1) {
      DashManifest var3 = this.manifest;
      boolean var4 = var3.dynamic;
      boolean var5 = false;
      if (!var4) {
         return false;
      } else if (this.isWaitingForManifestRefresh) {
         return true;
      } else {
         Entry var6 = this.ceilingExpiryEntryForPublishTime(var3.publishTimeMs);
         var4 = var5;
         if (var6 != null) {
            var4 = var5;
            if ((Long)var6.getValue() < var1) {
               this.expiredManifestPublishTimeUs = (Long)var6.getKey();
               this.notifyManifestPublishTimeExpired();
               var4 = true;
            }
         }

         if (var4) {
            this.maybeNotifyDashManifestRefreshNeeded();
         }

         return var4;
      }
   }

   boolean maybeRefreshManifestOnLoadingError(Chunk var1) {
      if (!this.manifest.dynamic) {
         return false;
      } else if (this.isWaitingForManifestRefresh) {
         return true;
      } else {
         long var2 = this.lastLoadedChunkEndTimeUs;
         boolean var4;
         if (var2 != -9223372036854775807L && var2 < var1.startTimeUs) {
            var4 = true;
         } else {
            var4 = false;
         }

         if (var4) {
            this.maybeNotifyDashManifestRefreshNeeded();
            return true;
         } else {
            return false;
         }
      }
   }

   public PlayerEmsgHandler.PlayerTrackEmsgHandler newPlayerTrackEmsgHandler() {
      return new PlayerEmsgHandler.PlayerTrackEmsgHandler(new SampleQueue(this.allocator));
   }

   void onChunkLoadCompleted(Chunk var1) {
      long var2 = this.lastLoadedChunkEndTimeUs;
      if (var2 != -9223372036854775807L || var1.endTimeUs > var2) {
         this.lastLoadedChunkEndTimeUs = var1.endTimeUs;
      }

   }

   public void release() {
      this.released = true;
      this.handler.removeCallbacksAndMessages((Object)null);
   }

   public void updateManifest(DashManifest var1) {
      this.isWaitingForManifestRefresh = false;
      this.expiredManifestPublishTimeUs = -9223372036854775807L;
      this.manifest = var1;
      this.removePreviouslyExpiredManifestPublishTimeValues();
   }

   private static final class ManifestExpiryEventInfo {
      public final long eventTimeUs;
      public final long manifestPublishTimeMsInEmsg;

      public ManifestExpiryEventInfo(long var1, long var3) {
         this.eventTimeUs = var1;
         this.manifestPublishTimeMsInEmsg = var3;
      }
   }

   public interface PlayerEmsgCallback {
      void onDashManifestPublishTimeExpired(long var1);

      void onDashManifestRefreshRequested();
   }

   public final class PlayerTrackEmsgHandler implements TrackOutput {
      private final MetadataInputBuffer buffer;
      private final FormatHolder formatHolder;
      private final SampleQueue sampleQueue;

      PlayerTrackEmsgHandler(SampleQueue var2) {
         this.sampleQueue = var2;
         this.formatHolder = new FormatHolder();
         this.buffer = new MetadataInputBuffer();
      }

      private MetadataInputBuffer dequeueSample() {
         this.buffer.clear();
         if (this.sampleQueue.read(this.formatHolder, this.buffer, false, false, 0L) == -4) {
            this.buffer.flip();
            return this.buffer;
         } else {
            return null;
         }
      }

      private void onManifestExpiredMessageEncountered(long var1, long var3) {
         PlayerEmsgHandler.ManifestExpiryEventInfo var5 = new PlayerEmsgHandler.ManifestExpiryEventInfo(var1, var3);
         PlayerEmsgHandler.this.handler.sendMessage(PlayerEmsgHandler.this.handler.obtainMessage(1, var5));
      }

      private void parseAndDiscardSamples() {
         while(this.sampleQueue.hasNextSample()) {
            MetadataInputBuffer var1 = this.dequeueSample();
            if (var1 != null) {
               long var2 = var1.timeUs;
               EventMessage var4 = (EventMessage)PlayerEmsgHandler.this.decoder.decode(var1).get(0);
               if (PlayerEmsgHandler.isPlayerEmsgEvent(var4.schemeIdUri, var4.value)) {
                  this.parsePlayerEmsgEvent(var2, var4);
               }
            }
         }

         this.sampleQueue.discardToRead();
      }

      private void parsePlayerEmsgEvent(long var1, EventMessage var3) {
         long var4 = PlayerEmsgHandler.getManifestPublishTimeMsInEmsg(var3);
         if (var4 != -9223372036854775807L) {
            this.onManifestExpiredMessageEncountered(var1, var4);
         }
      }

      public void format(Format var1) {
         this.sampleQueue.format(var1);
      }

      public boolean maybeRefreshManifestBeforeLoadingNextChunk(long var1) {
         return PlayerEmsgHandler.this.maybeRefreshManifestBeforeLoadingNextChunk(var1);
      }

      public boolean maybeRefreshManifestOnLoadingError(Chunk var1) {
         return PlayerEmsgHandler.this.maybeRefreshManifestOnLoadingError(var1);
      }

      public void onChunkLoadCompleted(Chunk var1) {
         PlayerEmsgHandler.this.onChunkLoadCompleted(var1);
      }

      public void release() {
         this.sampleQueue.reset();
      }

      public int sampleData(ExtractorInput var1, int var2, boolean var3) throws IOException, InterruptedException {
         return this.sampleQueue.sampleData(var1, var2, var3);
      }

      public void sampleData(ParsableByteArray var1, int var2) {
         this.sampleQueue.sampleData(var1, var2);
      }

      public void sampleMetadata(long var1, int var3, int var4, int var5, TrackOutput.CryptoData var6) {
         this.sampleQueue.sampleMetadata(var1, var3, var4, var5, var6);
         this.parseAndDiscardSamples();
      }
   }
}
