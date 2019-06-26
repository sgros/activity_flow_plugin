package com.google.android.exoplayer2.extractor.amr;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ConstantBitrateSeekMap;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class AmrExtractor implements Extractor {
   public static final ExtractorsFactory FACTORY;
   private static final int MAX_FRAME_SIZE_BYTES;
   private static final byte[] amrSignatureNb;
   private static final byte[] amrSignatureWb;
   private static final int[] frameSizeBytesByTypeNb;
   private static final int[] frameSizeBytesByTypeWb;
   private int currentSampleBytesRemaining;
   private int currentSampleSize;
   private long currentSampleTimeUs;
   private ExtractorOutput extractorOutput;
   private long firstSamplePosition;
   private int firstSampleSize;
   private final int flags;
   private boolean hasOutputFormat;
   private boolean hasOutputSeekMap;
   private boolean isWideBand;
   private int numSamplesWithSameSize;
   private final byte[] scratch;
   private SeekMap seekMap;
   private long timeOffsetUs;
   private TrackOutput trackOutput;

   static {
      FACTORY = _$$Lambda$AmrExtractor$lVuGuaAcylUV__XE4_hSR1hBylI.INSTANCE;
      frameSizeBytesByTypeNb = new int[]{13, 14, 16, 18, 20, 21, 27, 32, 6, 7, 6, 6, 1, 1, 1, 1};
      frameSizeBytesByTypeWb = new int[]{18, 24, 33, 37, 41, 47, 51, 59, 61, 6, 1, 1, 1, 1, 1, 1};
      amrSignatureNb = Util.getUtf8Bytes("#!AMR\n");
      amrSignatureWb = Util.getUtf8Bytes("#!AMR-WB\n");
      MAX_FRAME_SIZE_BYTES = frameSizeBytesByTypeWb[8];
   }

   public AmrExtractor() {
      this(0);
   }

   public AmrExtractor(int var1) {
      this.flags = var1;
      this.scratch = new byte[1];
      this.firstSampleSize = -1;
   }

   private static int getBitrateFromFrameSize(int var0, long var1) {
      return (int)((long)(var0 * 8) * 1000000L / var1);
   }

   private SeekMap getConstantBitrateSeekMap(long var1) {
      int var3 = getBitrateFromFrameSize(this.firstSampleSize, 20000L);
      return new ConstantBitrateSeekMap(var1, this.firstSamplePosition, var3, this.firstSampleSize);
   }

   private int getFrameSizeInBytes(int var1) throws ParserException {
      if (!this.isValidFrameType(var1)) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Illegal AMR ");
         String var3;
         if (this.isWideBand) {
            var3 = "WB";
         } else {
            var3 = "NB";
         }

         var2.append(var3);
         var2.append(" frame type ");
         var2.append(var1);
         throw new ParserException(var2.toString());
      } else {
         if (this.isWideBand) {
            var1 = frameSizeBytesByTypeWb[var1];
         } else {
            var1 = frameSizeBytesByTypeNb[var1];
         }

         return var1;
      }
   }

   private boolean isNarrowBandValidFrameType(int var1) {
      boolean var2;
      if (this.isWideBand || var1 >= 12 && var1 <= 14) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private boolean isValidFrameType(int var1) {
      boolean var2;
      if (var1 < 0 || var1 > 15 || !this.isWideBandValidFrameType(var1) && !this.isNarrowBandValidFrameType(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private boolean isWideBandValidFrameType(int var1) {
      boolean var2;
      if (!this.isWideBand || var1 >= 10 && var1 <= 13) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new AmrExtractor()};
   }

   private void maybeOutputFormat() {
      if (!this.hasOutputFormat) {
         this.hasOutputFormat = true;
         String var1;
         if (this.isWideBand) {
            var1 = "audio/amr-wb";
         } else {
            var1 = "audio/3gpp";
         }

         short var2;
         if (this.isWideBand) {
            var2 = 16000;
         } else {
            var2 = 8000;
         }

         this.trackOutput.format(Format.createAudioSampleFormat((String)null, var1, (String)null, -1, MAX_FRAME_SIZE_BYTES, 1, var2, -1, (List)null, (DrmInitData)null, 0, (String)null));
      }

   }

   private void maybeOutputSeekMap(long var1, int var3) {
      if (!this.hasOutputSeekMap) {
         if ((this.flags & 1) != 0 && var1 != -1L) {
            int var4 = this.firstSampleSize;
            if (var4 == -1 || var4 == this.currentSampleSize) {
               if (this.numSamplesWithSameSize >= 20 || var3 == -1) {
                  this.seekMap = this.getConstantBitrateSeekMap(var1);
                  this.extractorOutput.seekMap(this.seekMap);
                  this.hasOutputSeekMap = true;
               }

               return;
            }
         }

         this.seekMap = new SeekMap.Unseekable(-9223372036854775807L);
         this.extractorOutput.seekMap(this.seekMap);
         this.hasOutputSeekMap = true;
      }
   }

   private boolean peekAmrSignature(ExtractorInput var1, byte[] var2) throws IOException, InterruptedException {
      var1.resetPeekPosition();
      byte[] var3 = new byte[var2.length];
      var1.peekFully(var3, 0, var2.length);
      return Arrays.equals(var3, var2);
   }

   private int peekNextSampleSize(ExtractorInput var1) throws IOException, InterruptedException {
      var1.resetPeekPosition();
      var1.peekFully(this.scratch, 0, 1);
      byte var2 = this.scratch[0];
      if ((var2 & 131) <= 0) {
         return this.getFrameSizeInBytes(var2 >> 3 & 15);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Invalid padding bits for frame header ");
         var3.append(var2);
         throw new ParserException(var3.toString());
      }
   }

   private boolean readAmrHeader(ExtractorInput var1) throws IOException, InterruptedException {
      if (this.peekAmrSignature(var1, amrSignatureNb)) {
         this.isWideBand = false;
         var1.skipFully(amrSignatureNb.length);
         return true;
      } else if (this.peekAmrSignature(var1, amrSignatureWb)) {
         this.isWideBand = true;
         var1.skipFully(amrSignatureWb.length);
         return true;
      } else {
         return false;
      }
   }

   private int readSample(ExtractorInput var1) throws IOException, InterruptedException {
      if (this.currentSampleBytesRemaining == 0) {
         try {
            this.currentSampleSize = this.peekNextSampleSize(var1);
         } catch (EOFException var3) {
            return -1;
         }

         this.currentSampleBytesRemaining = this.currentSampleSize;
         if (this.firstSampleSize == -1) {
            this.firstSamplePosition = var1.getPosition();
            this.firstSampleSize = this.currentSampleSize;
         }

         if (this.firstSampleSize == this.currentSampleSize) {
            ++this.numSamplesWithSameSize;
         }
      }

      int var2 = this.trackOutput.sampleData(var1, this.currentSampleBytesRemaining, true);
      if (var2 == -1) {
         return -1;
      } else {
         this.currentSampleBytesRemaining -= var2;
         if (this.currentSampleBytesRemaining > 0) {
            return 0;
         } else {
            this.trackOutput.sampleMetadata(this.timeOffsetUs + this.currentSampleTimeUs, 1, this.currentSampleSize, 0, (TrackOutput.CryptoData)null);
            this.currentSampleTimeUs += 20000L;
            return 0;
         }
      }
   }

   public void init(ExtractorOutput var1) {
      this.extractorOutput = var1;
      this.trackOutput = var1.track(0, 1);
      var1.endTracks();
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      if (var1.getPosition() == 0L && !this.readAmrHeader(var1)) {
         throw new ParserException("Could not find AMR header.");
      } else {
         this.maybeOutputFormat();
         int var3 = this.readSample(var1);
         this.maybeOutputSeekMap(var1.getLength(), var3);
         return var3;
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      this.currentSampleTimeUs = 0L;
      this.currentSampleSize = 0;
      this.currentSampleBytesRemaining = 0;
      if (var1 != 0L) {
         SeekMap var5 = this.seekMap;
         if (var5 instanceof ConstantBitrateSeekMap) {
            this.timeOffsetUs = ((ConstantBitrateSeekMap)var5).getTimeUsAtPosition(var1);
            return;
         }
      }

      this.timeOffsetUs = 0L;
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      return this.readAmrHeader(var1);
   }
}
