package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Id3Peeker;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public final class FlacExtractor implements Extractor {
   public static final ExtractorsFactory FACTORY;
   private static final byte[] FLAC_SIGNATURE;
   public static final int FLAG_DISABLE_ID3_METADATA = 1;
   private FlacDecoderJni decoderJni;
   private ExtractorOutput extractorOutput;
   private FlacBinarySearchSeeker flacBinarySearchSeeker;
   private Metadata id3Metadata;
   private final Id3Peeker id3Peeker;
   private final boolean isId3MetadataDisabled;
   private ParsableByteArray outputBuffer;
   private ByteBuffer outputByteBuffer;
   private BinarySearchSeeker.OutputFrameHolder outputFrameHolder;
   private boolean readPastStreamInfo;
   private FlacStreamInfo streamInfo;
   private TrackOutput trackOutput;

   static {
      FACTORY = _$$Lambda$FlacExtractor$hclvvK8AqHrca9y8kXj1zx0IKB4.INSTANCE;
      FLAC_SIGNATURE = new byte[]{102, 76, 97, 67, 0, 0, 0, 34};
   }

   public FlacExtractor() {
      this(0);
   }

   public FlacExtractor(int var1) {
      this.id3Peeker = new Id3Peeker();
      boolean var2 = true;
      if ((var1 & 1) == 0) {
         var2 = false;
      }

      this.isId3MetadataDisabled = var2;
   }

   private FlacStreamInfo decodeStreamInfo(ExtractorInput param1) throws InterruptedException, IOException {
      // $FF: Couldn't be decompiled
   }

   private SeekMap getSeekMapForNonSeekTableFlac(ExtractorInput var1, FlacStreamInfo var2) {
      long var3 = var1.getLength();
      if (var3 != -1L) {
         this.flacBinarySearchSeeker = new FlacBinarySearchSeeker(var2, this.decoderJni.getDecodePosition(), var3, this.decoderJni);
         return this.flacBinarySearchSeeker.getSeekMap();
      } else {
         return new SeekMap.Unseekable(var2.durationUs());
      }
   }

   private int handlePendingSeek(ExtractorInput var1, PositionHolder var2) throws InterruptedException, IOException {
      int var3 = this.flacBinarySearchSeeker.handlePendingSeek(var1, var2, this.outputFrameHolder);
      ByteBuffer var4 = this.outputFrameHolder.byteBuffer;
      if (var3 == 0 && var4.limit() > 0) {
         this.writeLastSampleToOutput(var4.limit(), this.outputFrameHolder.timeUs);
      }

      return var3;
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new FlacExtractor()};
   }

   private void outputFormat(FlacStreamInfo var1) {
      int var2 = var1.bitRate();
      int var3 = var1.maxDecodedFrameSize();
      int var4 = var1.channels;
      int var5 = var1.sampleRate;
      int var6 = Util.getPcmEncoding(var1.bitsPerSample);
      Metadata var7;
      if (this.isId3MetadataDisabled) {
         var7 = null;
      } else {
         var7 = this.id3Metadata;
      }

      Format var8 = Format.createAudioSampleFormat((String)null, "audio/raw", (String)null, var2, var3, var4, var5, var6, 0, 0, (List)null, (DrmInitData)null, 0, (String)null, var7);
      this.trackOutput.format(var8);
   }

   private void outputSeekMap(ExtractorInput var1, FlacStreamInfo var2) {
      boolean var3;
      if (this.decoderJni.getSeekPosition(0L) != -1L) {
         var3 = true;
      } else {
         var3 = false;
      }

      Object var4;
      if (var3) {
         var4 = new FlacExtractor.FlacSeekMap(var2.durationUs(), this.decoderJni);
      } else {
         var4 = this.getSeekMapForNonSeekTableFlac(var1, var2);
      }

      this.extractorOutput.seekMap((SeekMap)var4);
   }

   private boolean peekFlacSignature(ExtractorInput var1) throws IOException, InterruptedException {
      byte[] var2 = FLAC_SIGNATURE;
      byte[] var3 = new byte[var2.length];
      var1.peekFully(var3, 0, var2.length);
      return Arrays.equals(var3, FLAC_SIGNATURE);
   }

   private Metadata peekId3Data(ExtractorInput var1) throws IOException, InterruptedException {
      var1.resetPeekPosition();
      Id3Decoder.FramePredicate var2;
      if (this.isId3MetadataDisabled) {
         var2 = Id3Decoder.NO_FRAMES_PREDICATE;
      } else {
         var2 = null;
      }

      return this.id3Peeker.peekId3Data(var1, var2);
   }

   private void readPastStreamInfo(ExtractorInput var1) throws InterruptedException, IOException {
      if (!this.readPastStreamInfo) {
         FlacStreamInfo var2 = this.decodeStreamInfo(var1);
         this.readPastStreamInfo = true;
         if (this.streamInfo == null) {
            this.updateFlacStreamInfo(var1, var2);
         }

      }
   }

   private void updateFlacStreamInfo(ExtractorInput var1, FlacStreamInfo var2) {
      this.streamInfo = var2;
      this.outputSeekMap(var1, var2);
      this.outputFormat(var2);
      this.outputBuffer = new ParsableByteArray(var2.maxDecodedFrameSize());
      this.outputByteBuffer = ByteBuffer.wrap(this.outputBuffer.data);
      this.outputFrameHolder = new BinarySearchSeeker.OutputFrameHolder(this.outputByteBuffer);
   }

   private void writeLastSampleToOutput(int var1, long var2) {
      this.outputBuffer.setPosition(0);
      this.trackOutput.sampleData(this.outputBuffer, var1);
      this.trackOutput.sampleMetadata(var2, 1, var1, 0, (TrackOutput.CryptoData)null);
   }

   public void init(ExtractorOutput var1) {
      this.extractorOutput = var1;
      this.trackOutput = this.extractorOutput.track(0, 1);
      this.extractorOutput.endTracks();

      try {
         FlacDecoderJni var3 = new FlacDecoderJni();
         this.decoderJni = var3;
      } catch (FlacDecoderException var2) {
         throw new RuntimeException(var2);
      }
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      if (var1.getPosition() == 0L && !this.isId3MetadataDisabled && this.id3Metadata == null) {
         this.id3Metadata = this.peekId3Data(var1);
      }

      this.decoderJni.setData(var1);
      this.readPastStreamInfo(var1);
      FlacBinarySearchSeeker var3 = this.flacBinarySearchSeeker;
      if (var3 != null && var3.isSeeking()) {
         return this.handlePendingSeek(var1, var2);
      } else {
         long var4 = this.decoderJni.getDecodePosition();

         try {
            this.decoderJni.decodeSampleWithBacktrackPosition(this.outputByteBuffer, var4);
         } catch (FlacDecoderJni.FlacFrameDecodeException var8) {
            StringBuilder var9 = new StringBuilder();
            var9.append("Cannot read frame at position ");
            var9.append(var4);
            throw new IOException(var9.toString(), var8);
         }

         int var6 = this.outputByteBuffer.limit();
         byte var7 = -1;
         if (var6 == 0) {
            return -1;
         } else {
            this.writeLastSampleToOutput(var6, this.decoderJni.getLastFrameTimestamp());
            if (!this.decoderJni.isEndOfData()) {
               var7 = 0;
            }

            return var7;
         }
      }
   }

   public void release() {
      this.flacBinarySearchSeeker = null;
      FlacDecoderJni var1 = this.decoderJni;
      if (var1 != null) {
         var1.release();
         this.decoderJni = null;
      }

   }

   public void seek(long var1, long var3) {
      if (var1 == 0L) {
         this.readPastStreamInfo = false;
      }

      FlacDecoderJni var5 = this.decoderJni;
      if (var5 != null) {
         var5.reset(var1);
      }

      FlacBinarySearchSeeker var6 = this.flacBinarySearchSeeker;
      if (var6 != null) {
         var6.setSeekTargetUs(var3);
      }

   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      if (var1.getPosition() == 0L) {
         this.id3Metadata = this.peekId3Data(var1);
      }

      return this.peekFlacSignature(var1);
   }

   private static final class FlacSeekMap implements SeekMap {
      private final FlacDecoderJni decoderJni;
      private final long durationUs;

      public FlacSeekMap(long var1, FlacDecoderJni var3) {
         this.durationUs = var1;
         this.decoderJni = var3;
      }

      public long getDurationUs() {
         return this.durationUs;
      }

      public SeekMap.SeekPoints getSeekPoints(long var1) {
         return new SeekMap.SeekPoints(new SeekPoint(var1, this.decoderJni.getSeekPosition(var1)));
      }

      public boolean isSeekable() {
         return true;
      }
   }

   @Documented
   @Retention(RetentionPolicy.SOURCE)
   public @interface Flags {
   }
}
