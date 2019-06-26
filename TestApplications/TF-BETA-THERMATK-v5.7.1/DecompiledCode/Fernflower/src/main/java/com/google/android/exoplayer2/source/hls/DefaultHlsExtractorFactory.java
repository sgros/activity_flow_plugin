package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.google.android.exoplayer2.extractor.ts.Ac3Extractor;
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.io.EOFException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class DefaultHlsExtractorFactory implements HlsExtractorFactory {
   private final int payloadReaderFactoryFlags;

   public DefaultHlsExtractorFactory() {
      this(0);
   }

   public DefaultHlsExtractorFactory(int var1) {
      this.payloadReaderFactoryFlags = var1;
   }

   private static Pair buildResult(Extractor var0) {
      boolean var1;
      if (!(var0 instanceof AdtsExtractor) && !(var0 instanceof Ac3Extractor) && !(var0 instanceof Mp3Extractor)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return new Pair(var0, var1);
   }

   private Extractor createExtractorByFileExtension(Uri var1, Format var2, List var3, DrmInitData var4, TimestampAdjuster var5) {
      String var6 = var1.getLastPathSegment();
      String var7 = var6;
      if (var6 == null) {
         var7 = "";
      }

      if (!"text/vtt".equals(var2.sampleMimeType) && !var7.endsWith(".webvtt") && !var7.endsWith(".vtt")) {
         if (var7.endsWith(".aac")) {
            return new AdtsExtractor();
         } else if (!var7.endsWith(".ac3") && !var7.endsWith(".ec3")) {
            if (var7.endsWith(".mp3")) {
               return new Mp3Extractor(0, 0L);
            } else if (!var7.endsWith(".mp4") && !var7.startsWith(".m4", var7.length() - 4) && !var7.startsWith(".mp4", var7.length() - 5) && !var7.startsWith(".cmf", var7.length() - 5)) {
               return createTsExtractor(this.payloadReaderFactoryFlags, var2, var3, var5);
            } else {
               if (var3 == null) {
                  var3 = Collections.emptyList();
               }

               return new FragmentedMp4Extractor(0, var5, (Track)null, var4, var3);
            }
         } else {
            return new Ac3Extractor();
         }
      } else {
         return new WebvttExtractor(var2.language, var5);
      }
   }

   private static TsExtractor createTsExtractor(int var0, Format var1, List var2, TimestampAdjuster var3) {
      var0 |= 16;
      if (var2 != null) {
         var0 |= 32;
      } else {
         var2 = Collections.singletonList(Format.createTextSampleFormat((String)null, "application/cea-608", 0, (String)null));
      }

      String var6 = var1.codecs;
      int var4 = var0;
      if (!TextUtils.isEmpty(var6)) {
         int var5 = var0;
         if (!"audio/mp4a-latm".equals(MimeTypes.getAudioMediaMimeType(var6))) {
            var5 = var0 | 2;
         }

         var4 = var5;
         if (!"video/avc".equals(MimeTypes.getVideoMediaMimeType(var6))) {
            var4 = var5 | 4;
         }
      }

      return new TsExtractor(2, var3, new DefaultTsPayloadReaderFactory(var4, var2));
   }

   private static boolean sniffQuietly(Extractor var0, ExtractorInput var1) throws InterruptedException, IOException {
      boolean var2;
      try {
         var2 = var0.sniff(var1);
         return var2;
      } catch (EOFException var5) {
      } finally {
         var1.resetPeekPosition();
      }

      var2 = false;
      return var2;
   }

   public Pair createExtractor(Extractor var1, Uri var2, Format var3, List var4, DrmInitData var5, TimestampAdjuster var6, Map var7, ExtractorInput var8) throws InterruptedException, IOException {
      if (var1 != null) {
         if (!(var1 instanceof TsExtractor) && !(var1 instanceof FragmentedMp4Extractor)) {
            if (var1 instanceof WebvttExtractor) {
               return buildResult(new WebvttExtractor(var3.language, var6));
            } else if (var1 instanceof AdtsExtractor) {
               return buildResult(new AdtsExtractor());
            } else if (var1 instanceof Ac3Extractor) {
               return buildResult(new Ac3Extractor());
            } else if (var1 instanceof Mp3Extractor) {
               return buildResult(new Mp3Extractor());
            } else {
               StringBuilder var14 = new StringBuilder();
               var14.append("Unexpected previousExtractor type: ");
               var14.append(var1.getClass().getSimpleName());
               throw new IllegalArgumentException(var14.toString());
            }
         } else {
            return buildResult(var1);
         }
      } else {
         Extractor var12 = this.createExtractorByFileExtension(var2, var3, var4, var5, var6);
         var8.resetPeekPosition();
         if (sniffQuietly(var12, var8)) {
            return buildResult(var12);
         } else {
            if (!(var12 instanceof WebvttExtractor)) {
               WebvttExtractor var9 = new WebvttExtractor(var3.language, var6);
               if (sniffQuietly(var9, var8)) {
                  return buildResult(var9);
               }
            }

            if (!(var12 instanceof AdtsExtractor)) {
               AdtsExtractor var10 = new AdtsExtractor();
               if (sniffQuietly(var10, var8)) {
                  return buildResult(var10);
               }
            }

            if (!(var12 instanceof Ac3Extractor)) {
               Ac3Extractor var11 = new Ac3Extractor();
               if (sniffQuietly(var11, var8)) {
                  return buildResult(var11);
               }
            }

            if (!(var12 instanceof Mp3Extractor)) {
               Mp3Extractor var13 = new Mp3Extractor(0, 0L);
               if (sniffQuietly(var13, var8)) {
                  return buildResult(var13);
               }
            }

            if (!(var12 instanceof FragmentedMp4Extractor)) {
               List var15;
               if (var4 != null) {
                  var15 = var4;
               } else {
                  var15 = Collections.emptyList();
               }

               FragmentedMp4Extractor var16 = new FragmentedMp4Extractor(0, var6, (Track)null, var5, var15);
               if (sniffQuietly(var16, var8)) {
                  return buildResult(var16);
               }
            }

            if (!(var12 instanceof TsExtractor)) {
               TsExtractor var17 = createTsExtractor(this.payloadReaderFactoryFlags, var3, var4, var6);
               if (sniffQuietly(var17, var8)) {
                  return buildResult(var17);
               }
            }

            return buildResult(var12);
         }
      }
   }
}
