package com.google.android.exoplayer2.metadata;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Handler.Callback;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class MetadataRenderer extends BaseRenderer implements Callback {
   private final MetadataInputBuffer buffer;
   private MetadataDecoder decoder;
   private final MetadataDecoderFactory decoderFactory;
   private final FormatHolder formatHolder;
   private boolean inputStreamEnded;
   private final MetadataOutput output;
   private final Handler outputHandler;
   private final Metadata[] pendingMetadata;
   private int pendingMetadataCount;
   private int pendingMetadataIndex;
   private final long[] pendingMetadataTimestamps;

   public MetadataRenderer(MetadataOutput var1, Looper var2) {
      this(var1, var2, MetadataDecoderFactory.DEFAULT);
   }

   public MetadataRenderer(MetadataOutput var1, Looper var2, MetadataDecoderFactory var3) {
      super(4);
      Assertions.checkNotNull(var1);
      this.output = (MetadataOutput)var1;
      Handler var4;
      if (var2 == null) {
         var4 = null;
      } else {
         var4 = Util.createHandler(var2, this);
      }

      this.outputHandler = var4;
      Assertions.checkNotNull(var3);
      this.decoderFactory = (MetadataDecoderFactory)var3;
      this.formatHolder = new FormatHolder();
      this.buffer = new MetadataInputBuffer();
      this.pendingMetadata = new Metadata[5];
      this.pendingMetadataTimestamps = new long[5];
   }

   private void flushPendingMetadata() {
      Arrays.fill(this.pendingMetadata, (Object)null);
      this.pendingMetadataIndex = 0;
      this.pendingMetadataCount = 0;
   }

   private void invokeRenderer(Metadata var1) {
      Handler var2 = this.outputHandler;
      if (var2 != null) {
         var2.obtainMessage(0, var1).sendToTarget();
      } else {
         this.invokeRendererInternal(var1);
      }

   }

   private void invokeRendererInternal(Metadata var1) {
      this.output.onMetadata(var1);
   }

   public boolean handleMessage(Message var1) {
      if (var1.what == 0) {
         this.invokeRendererInternal((Metadata)var1.obj);
         return true;
      } else {
         throw new IllegalStateException();
      }
   }

   public boolean isEnded() {
      return this.inputStreamEnded;
   }

   public boolean isReady() {
      return true;
   }

   protected void onDisabled() {
      this.flushPendingMetadata();
      this.decoder = null;
   }

   protected void onPositionReset(long var1, boolean var3) {
      this.flushPendingMetadata();
      this.inputStreamEnded = false;
   }

   protected void onStreamChanged(Format[] var1, long var2) throws ExoPlaybackException {
      this.decoder = this.decoderFactory.createDecoder(var1[0]);
   }

   public void render(long var1, long var3) throws ExoPlaybackException {
      int var6;
      if (!this.inputStreamEnded && this.pendingMetadataCount < 5) {
         this.buffer.clear();
         if (this.readSource(this.formatHolder, this.buffer, false) == -4) {
            if (this.buffer.isEndOfStream()) {
               this.inputStreamEnded = true;
            } else if (!this.buffer.isDecodeOnly()) {
               MetadataInputBuffer var5 = this.buffer;
               var5.subsampleOffsetUs = this.formatHolder.format.subsampleOffsetUs;
               var5.flip();
               var6 = (this.pendingMetadataIndex + this.pendingMetadataCount) % 5;
               Metadata var7 = this.decoder.decode(this.buffer);
               if (var7 != null) {
                  this.pendingMetadata[var6] = var7;
                  this.pendingMetadataTimestamps[var6] = this.buffer.timeUs;
                  ++this.pendingMetadataCount;
               }
            }
         }
      }

      if (this.pendingMetadataCount > 0) {
         long[] var8 = this.pendingMetadataTimestamps;
         var6 = this.pendingMetadataIndex;
         if (var8[var6] <= var1) {
            this.invokeRenderer(this.pendingMetadata[var6]);
            Metadata[] var9 = this.pendingMetadata;
            var6 = this.pendingMetadataIndex;
            var9[var6] = null;
            this.pendingMetadataIndex = (var6 + 1) % 5;
            --this.pendingMetadataCount;
         }
      }

   }

   public int supportsFormat(Format var1) {
      if (this.decoderFactory.supportsFormat(var1)) {
         byte var2;
         if (BaseRenderer.supportsFormatDrm((DrmSessionManager)null, var1.drmInitData)) {
            var2 = 4;
         } else {
            var2 = 2;
         }

         return var2;
      } else {
         return 0;
      }
   }
}
