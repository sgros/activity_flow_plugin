package com.google.android.exoplayer2.extractor.ts;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Arrays;
import java.util.Collections;

public final class H262Reader implements ElementaryStreamReader {
   private static final double[] FRAME_RATE_VALUES = new double[]{23.976023976023978D, 24.0D, 25.0D, 29.97002997002997D, 30.0D, 50.0D, 59.94005994005994D, 60.0D};
   private final H262Reader.CsdBuffer csdBuffer;
   private String formatId;
   private long frameDurationUs;
   private boolean hasOutputFormat;
   private TrackOutput output;
   private long pesTimeUs;
   private final boolean[] prefixFlags;
   private boolean sampleHasPicture;
   private boolean sampleIsKeyframe;
   private long samplePosition;
   private long sampleTimeUs;
   private boolean startedFirstSample;
   private long totalBytesWritten;
   private final NalUnitTargetBuffer userData;
   private final ParsableByteArray userDataParsable;
   private final UserDataReader userDataReader;

   public H262Reader() {
      this((UserDataReader)null);
   }

   public H262Reader(UserDataReader var1) {
      this.userDataReader = var1;
      this.prefixFlags = new boolean[4];
      this.csdBuffer = new H262Reader.CsdBuffer(128);
      if (var1 != null) {
         this.userData = new NalUnitTargetBuffer(178, 128);
         this.userDataParsable = new ParsableByteArray();
      } else {
         this.userData = null;
         this.userDataParsable = null;
      }

   }

   private static Pair parseCsdBuffer(H262Reader.CsdBuffer var0, String var1) {
      byte[] var2;
      int var4;
      float var6;
      int var19;
      int var20;
      label28: {
         var2 = Arrays.copyOf(var0.data, var0.length);
         byte var3 = var2[4];
         var4 = var2[5] & 255;
         byte var5 = var2[6];
         var19 = (var3 & 255) << 4 | var4 >> 4;
         var20 = (var4 & 15) << 8 | var5 & 255;
         var4 = (var2[7] & 240) >> 4;
         if (var4 != 2) {
            if (var4 != 3) {
               if (var4 != 4) {
                  var6 = 1.0F;
                  break label28;
               }

               var6 = (float)(var20 * 121);
               var4 = var19 * 100;
            } else {
               var6 = (float)(var20 * 16);
               var4 = var19 * 9;
            }
         } else {
            var6 = (float)(var20 * 4);
            var4 = var19 * 3;
         }

         var6 /= (float)var4;
      }

      Format var7 = Format.createVideoSampleFormat(var1, "video/mpeg2", (String)null, -1, -1, var19, var20, -1.0F, Collections.singletonList(var2), -1, var6, (DrmInitData)null);
      long var8 = 0L;
      var4 = (var2[7] & 15) - 1;
      long var10 = var8;
      if (var4 >= 0) {
         double[] var18 = FRAME_RATE_VALUES;
         var10 = var8;
         if (var4 < var18.length) {
            double var12 = var18[var4];
            var19 = var0.sequenceExtensionPosition + 9;
            var4 = (var2[var19] & 96) >> 5;
            var19 = var2[var19] & 31;
            double var14 = var12;
            if (var4 != var19) {
               var14 = (double)var4;
               Double.isNaN(var14);
               double var16 = (double)(var19 + 1);
               Double.isNaN(var16);
               var14 = var12 * ((var14 + 1.0D) / var16);
            }

            var10 = (long)(1000000.0D / var14);
         }
      }

      return Pair.create(var7, var10);
   }

   public void consume(ParsableByteArray var1) {
      int var2 = var1.getPosition();
      int var3 = var1.limit();
      byte[] var4 = var1.data;
      this.totalBytesWritten += (long)var1.bytesLeft();
      this.output.sampleData(var1, var1.bytesLeft());

      while(true) {
         int var5 = NalUnitUtil.findNalUnit(var4, var2, var3, this.prefixFlags);
         if (var5 == var3) {
            if (!this.hasOutputFormat) {
               this.csdBuffer.onData(var4, var2, var3);
            }

            if (this.userDataReader != null) {
               this.userData.appendToNalUnit(var4, var2, var3);
            }

            return;
         }

         byte[] var6 = var1.data;
         int var7 = var5 + 3;
         int var8 = var6[var7] & 255;
         int var9 = var5 - var2;
         boolean var10 = this.hasOutputFormat;
         boolean var11 = false;
         int var12;
         if (!var10) {
            if (var9 > 0) {
               this.csdBuffer.onData(var4, var2, var5);
            }

            if (var9 < 0) {
               var12 = -var9;
            } else {
               var12 = 0;
            }

            if (this.csdBuffer.onStartCode(var8, var12)) {
               Pair var16 = parseCsdBuffer(this.csdBuffer, this.formatId);
               this.output.format((Format)var16.first);
               this.frameDurationUs = (Long)var16.second;
               this.hasOutputFormat = true;
            }
         }

         if (this.userDataReader != null) {
            if (var9 > 0) {
               this.userData.appendToNalUnit(var4, var2, var5);
               var2 = 0;
            } else {
               var2 = -var9;
            }

            if (this.userData.endNalUnit(var2)) {
               NalUnitTargetBuffer var17 = this.userData;
               var2 = NalUnitUtil.unescapeStream(var17.nalData, var17.nalLength);
               this.userDataParsable.reset(this.userData.nalData, var2);
               this.userDataReader.consume(this.sampleTimeUs, this.userDataParsable);
            }

            if (var8 == 178 && var1.data[var5 + 2] == 1) {
               this.userData.startNalUnit(var8);
            }
         }

         if (var8 != 0 && var8 != 179) {
            if (var8 == 184) {
               this.sampleIsKeyframe = true;
            }
         } else {
            var2 = var3 - var5;
            if (this.startedFirstSample && this.sampleHasPicture && this.hasOutputFormat) {
               byte var15 = this.sampleIsKeyframe;
               var12 = (int)(this.totalBytesWritten - this.samplePosition);
               this.output.sampleMetadata(this.sampleTimeUs, var15, var12 - var2, var2, (TrackOutput.CryptoData)null);
            }

            if (!this.startedFirstSample || this.sampleHasPicture) {
               this.samplePosition = this.totalBytesWritten - (long)var2;
               long var13 = this.pesTimeUs;
               if (var13 == -9223372036854775807L) {
                  if (this.startedFirstSample) {
                     var13 = this.sampleTimeUs + this.frameDurationUs;
                  } else {
                     var13 = 0L;
                  }
               }

               this.sampleTimeUs = var13;
               this.sampleIsKeyframe = false;
               this.pesTimeUs = -9223372036854775807L;
               this.startedFirstSample = true;
            }

            if (var8 == 0) {
               var11 = true;
            }

            this.sampleHasPicture = var11;
         }

         var2 = var7;
      }
   }

   public void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2) {
      var2.generateNewId();
      this.formatId = var2.getFormatId();
      this.output = var1.track(var2.getTrackId(), 2);
      UserDataReader var3 = this.userDataReader;
      if (var3 != null) {
         var3.createTracks(var1, var2);
      }

   }

   public void packetFinished() {
   }

   public void packetStarted(long var1, int var3) {
      this.pesTimeUs = var1;
   }

   public void seek() {
      NalUnitUtil.clearPrefixFlags(this.prefixFlags);
      this.csdBuffer.reset();
      if (this.userDataReader != null) {
         this.userData.reset();
      }

      this.totalBytesWritten = 0L;
      this.startedFirstSample = false;
   }

   private static final class CsdBuffer {
      private static final byte[] START_CODE = new byte[]{0, 0, 1};
      public byte[] data;
      private boolean isFilling;
      public int length;
      public int sequenceExtensionPosition;

      public CsdBuffer(int var1) {
         this.data = new byte[var1];
      }

      public void onData(byte[] var1, int var2, int var3) {
         if (this.isFilling) {
            int var4 = var3 - var2;
            byte[] var5 = this.data;
            int var6 = var5.length;
            var3 = this.length;
            if (var6 < var3 + var4) {
               this.data = Arrays.copyOf(var5, (var3 + var4) * 2);
            }

            System.arraycopy(var1, var2, this.data, this.length, var4);
            this.length += var4;
         }
      }

      public boolean onStartCode(int var1, int var2) {
         if (this.isFilling) {
            this.length -= var2;
            if (this.sequenceExtensionPosition != 0 || var1 != 181) {
               this.isFilling = false;
               return true;
            }

            this.sequenceExtensionPosition = this.length;
         } else if (var1 == 179) {
            this.isFilling = true;
         }

         byte[] var3 = START_CODE;
         this.onData(var3, 0, var3.length);
         return false;
      }

      public void reset() {
         this.isFilling = false;
         this.length = 0;
         this.sequenceExtensionPosition = 0;
      }
   }
}
