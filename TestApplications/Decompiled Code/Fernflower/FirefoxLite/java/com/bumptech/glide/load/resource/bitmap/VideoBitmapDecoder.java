package com.bumptech.glide.load.resource.bitmap;

import android.media.MediaMetadataRetriever;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class VideoBitmapDecoder implements ResourceDecoder {
   private static final VideoBitmapDecoder.MediaMetadataRetrieverFactory DEFAULT_FACTORY = new VideoBitmapDecoder.MediaMetadataRetrieverFactory();
   public static final Option FRAME_OPTION = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.FrameOption", (Object)null, new Option.CacheKeyUpdater() {
      private final ByteBuffer buffer = ByteBuffer.allocate(4);

      public void update(byte[] param1, Integer param2, MessageDigest param3) {
         // $FF: Couldn't be decompiled
      }
   });
   public static final Option TARGET_FRAME = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.TargetFrame", -1L, new Option.CacheKeyUpdater() {
      private final ByteBuffer buffer = ByteBuffer.allocate(8);

      public void update(byte[] param1, Long param2, MessageDigest param3) {
         // $FF: Couldn't be decompiled
      }
   });
   private final BitmapPool bitmapPool;
   private final VideoBitmapDecoder.MediaMetadataRetrieverFactory factory;

   public VideoBitmapDecoder(BitmapPool var1) {
      this(var1, DEFAULT_FACTORY);
   }

   VideoBitmapDecoder(BitmapPool var1, VideoBitmapDecoder.MediaMetadataRetrieverFactory var2) {
      this.bitmapPool = var1;
      this.factory = var2;
   }

   public Resource decode(ParcelFileDescriptor param1, int param2, int param3, Options param4) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public boolean handles(ParcelFileDescriptor var1, Options var2) {
      return true;
   }

   static class MediaMetadataRetrieverFactory {
      public MediaMetadataRetriever build() {
         return new MediaMetadataRetriever();
      }
   }
}
