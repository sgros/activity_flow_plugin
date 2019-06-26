package com.google.android.exoplayer2.extractor;

import java.lang.reflect.Constructor;

public final class DefaultExtractorsFactory implements ExtractorsFactory {
   private static final Constructor FLAC_EXTRACTOR_CONSTRUCTOR;
   private int adtsFlags;
   private int amrFlags;
   private boolean constantBitrateSeekingEnabled;
   private int fragmentedMp4Flags;
   private int matroskaFlags;
   private int mp3Flags;
   private int mp4Flags;
   private int tsFlags;
   private int tsMode = 1;

   static {
      Constructor var0;
      try {
         var0 = Class.forName("com.google.android.exoplayer2.ext.flac.FlacExtractor").asSubclass(Extractor.class).getConstructor();
      } catch (ClassNotFoundException var1) {
         var0 = null;
      } catch (Exception var2) {
         throw new RuntimeException("Error instantiating FLAC extension", var2);
      }

      FLAC_EXTRACTOR_CONSTRUCTOR = var0;
   }

   public Extractor[] createExtractors() {
      // $FF: Couldn't be decompiled
   }
}
