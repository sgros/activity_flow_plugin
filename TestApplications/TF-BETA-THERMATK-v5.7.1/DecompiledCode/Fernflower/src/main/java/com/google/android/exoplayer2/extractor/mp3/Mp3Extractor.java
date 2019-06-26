package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.Id3Peeker;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.metadata.id3.MlltFrame;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;

public final class Mp3Extractor implements Extractor {
   public static final ExtractorsFactory FACTORY;
   private static final Id3Decoder.FramePredicate REQUIRED_ID3_FRAME_PREDICATE;
   private static final int SEEK_HEADER_INFO;
   private static final int SEEK_HEADER_VBRI;
   private static final int SEEK_HEADER_XING;
   private long basisTimeUs;
   private ExtractorOutput extractorOutput;
   private final int flags;
   private final long forcedFirstSampleTimestampUs;
   private final GaplessInfoHolder gaplessInfoHolder;
   private final Id3Peeker id3Peeker;
   private Metadata metadata;
   private int sampleBytesRemaining;
   private long samplesRead;
   private final ParsableByteArray scratch;
   private Mp3Extractor.Seeker seeker;
   private final MpegAudioHeader synchronizedHeader;
   private int synchronizedHeaderData;
   private TrackOutput trackOutput;

   static {
      FACTORY = _$$Lambda$Mp3Extractor$6eyGfoogMVGFHZKg1gVp93FAKZA.INSTANCE;
      REQUIRED_ID3_FRAME_PREDICATE = _$$Lambda$Mp3Extractor$bb754AZIAMUosKBF4SefP1vYq88.INSTANCE;
      SEEK_HEADER_XING = Util.getIntegerCodeForString("Xing");
      SEEK_HEADER_INFO = Util.getIntegerCodeForString("Info");
      SEEK_HEADER_VBRI = Util.getIntegerCodeForString("VBRI");
   }

   public Mp3Extractor() {
      this(0);
   }

   public Mp3Extractor(int var1) {
      this(var1, -9223372036854775807L);
   }

   public Mp3Extractor(int var1, long var2) {
      this.flags = var1;
      this.forcedFirstSampleTimestampUs = var2;
      this.scratch = new ParsableByteArray(10);
      this.synchronizedHeader = new MpegAudioHeader();
      this.gaplessInfoHolder = new GaplessInfoHolder();
      this.basisTimeUs = -9223372036854775807L;
      this.id3Peeker = new Id3Peeker();
   }

   private Mp3Extractor.Seeker getConstantBitrateSeeker(ExtractorInput var1) throws IOException, InterruptedException {
      var1.peekFully(this.scratch.data, 0, 4);
      this.scratch.setPosition(0);
      MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
      return new ConstantBitrateSeeker(var1.getLength(), var1.getPosition(), this.synchronizedHeader);
   }

   private static int getSeekFrameHeader(ParsableByteArray var0, int var1) {
      if (var0.limit() >= var1 + 4) {
         var0.setPosition(var1);
         var1 = var0.readInt();
         if (var1 == SEEK_HEADER_XING || var1 == SEEK_HEADER_INFO) {
            return var1;
         }
      }

      if (var0.limit() >= 40) {
         var0.setPosition(36);
         int var2 = var0.readInt();
         var1 = SEEK_HEADER_VBRI;
         if (var2 == var1) {
            return var1;
         }
      }

      return 0;
   }

   private static boolean headersMatch(int var0, long var1) {
      boolean var3;
      if ((long)(var0 & -128000) == (var1 & -128000L)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new Mp3Extractor()};
   }

   // $FF: synthetic method
   static boolean lambda$static$1(int var0, int var1, int var2, int var3, int var4) {
      boolean var5;
      if ((var1 != 67 || var2 != 79 || var3 != 77 || var4 != 77 && var0 != 2) && (var1 != 77 || var2 != 76 || var3 != 76 || var4 != 84 && var0 != 2)) {
         var5 = false;
      } else {
         var5 = true;
      }

      return var5;
   }

   private static MlltSeeker maybeHandleSeekMetadata(Metadata var0, long var1) {
      if (var0 != null) {
         int var3 = var0.length();

         for(int var4 = 0; var4 < var3; ++var4) {
            Metadata.Entry var5 = var0.get(var4);
            if (var5 instanceof MlltFrame) {
               return MlltSeeker.create(var1, (MlltFrame)var5);
            }
         }
      }

      return null;
   }

   private Mp3Extractor.Seeker maybeReadSeekFrame(ExtractorInput var1) throws IOException, InterruptedException {
      ParsableByteArray var2;
      byte var4;
      label40: {
         var2 = new ParsableByteArray(this.synchronizedHeader.frameSize);
         var1.peekFully(var2.data, 0, this.synchronizedHeader.frameSize);
         MpegAudioHeader var3 = this.synchronizedHeader;
         if ((var3.version & 1) != 0) {
            if (var3.channels != 1) {
               var4 = 36;
               break label40;
            }
         } else if (var3.channels == 1) {
            var4 = 13;
            break label40;
         }

         var4 = 21;
      }

      int var5 = getSeekFrameHeader(var2, var4);
      Object var6;
      if (var5 != SEEK_HEADER_XING && var5 != SEEK_HEADER_INFO) {
         if (var5 == SEEK_HEADER_VBRI) {
            var6 = VbriSeeker.create(var1.getLength(), var1.getPosition(), this.synchronizedHeader, var2);
            var1.skipFully(this.synchronizedHeader.frameSize);
         } else {
            var6 = null;
            var1.resetPeekPosition();
         }
      } else {
         XingSeeker var7 = XingSeeker.create(var1.getLength(), var1.getPosition(), this.synchronizedHeader, var2);
         if (var7 != null && !this.gaplessInfoHolder.hasGaplessInfo()) {
            var1.resetPeekPosition();
            var1.advancePeekPosition(var4 + 141);
            var1.peekFully(this.scratch.data, 0, 3);
            this.scratch.setPosition(0);
            this.gaplessInfoHolder.setFromXingHeaderValue(this.scratch.readUnsignedInt24());
         }

         var1.skipFully(this.synchronizedHeader.frameSize);
         var6 = var7;
         if (var7 != null) {
            var6 = var7;
            if (!var7.isSeekable()) {
               var6 = var7;
               if (var5 == SEEK_HEADER_INFO) {
                  return this.getConstantBitrateSeeker(var1);
               }
            }
         }
      }

      return (Mp3Extractor.Seeker)var6;
   }

   private boolean peekEndOfStreamOrHeader(ExtractorInput var1) throws IOException, InterruptedException {
      Mp3Extractor.Seeker var2 = this.seeker;
      boolean var3 = true;
      boolean var4;
      if (var2 != null) {
         var4 = var3;
         if (var1.getPeekPosition() == this.seeker.getDataEndPosition()) {
            return var4;
         }
      }

      if (!var1.peekFully(this.scratch.data, 0, 4, true)) {
         var4 = var3;
      } else {
         var4 = false;
      }

      return var4;
   }

   private int readSample(ExtractorInput var1) throws IOException, InterruptedException {
      int var2;
      long var3;
      if (this.sampleBytesRemaining == 0) {
         var1.resetPeekPosition();
         if (this.peekEndOfStreamOrHeader(var1)) {
            return -1;
         }

         this.scratch.setPosition(0);
         var2 = this.scratch.readInt();
         if (!headersMatch(var2, (long)this.synchronizedHeaderData) || MpegAudioHeader.getFrameSize(var2) == -1) {
            var1.skipFully(1);
            this.synchronizedHeaderData = 0;
            return 0;
         }

         MpegAudioHeader.populateHeader(var2, this.synchronizedHeader);
         if (this.basisTimeUs == -9223372036854775807L) {
            this.basisTimeUs = this.seeker.getTimeUs(var1.getPosition());
            if (this.forcedFirstSampleTimestampUs != -9223372036854775807L) {
               var3 = this.seeker.getTimeUs(0L);
               this.basisTimeUs += this.forcedFirstSampleTimestampUs - var3;
            }
         }

         this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
      }

      var2 = this.trackOutput.sampleData(var1, this.sampleBytesRemaining, true);
      if (var2 == -1) {
         return -1;
      } else {
         this.sampleBytesRemaining -= var2;
         if (this.sampleBytesRemaining > 0) {
            return 0;
         } else {
            var3 = this.basisTimeUs;
            long var5 = this.samplesRead;
            MpegAudioHeader var7 = this.synchronizedHeader;
            var5 = var5 * 1000000L / (long)var7.sampleRate;
            this.trackOutput.sampleMetadata(var3 + var5, 1, var7.frameSize, 0, (TrackOutput.CryptoData)null);
            this.samplesRead += (long)this.synchronizedHeader.samplesPerFrame;
            this.sampleBytesRemaining = 0;
            return 0;
         }
      }
   }

   private boolean synchronize(ExtractorInput var1, boolean var2) throws IOException, InterruptedException {
      int var3;
      if (var2) {
         var3 = 16384;
      } else {
         var3 = 131072;
      }

      var1.resetPeekPosition();
      int var6;
      int var7;
      int var8;
      int var12;
      if (var1.getPosition() == 0L) {
         boolean var4;
         if ((this.flags & 2) == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         Id3Decoder.FramePredicate var5;
         if (var4) {
            var5 = null;
         } else {
            var5 = REQUIRED_ID3_FRAME_PREDICATE;
         }

         this.metadata = this.id3Peeker.peekId3Data(var1, var5);
         Metadata var13 = this.metadata;
         if (var13 != null) {
            this.gaplessInfoHolder.setFromMetadata(var13);
         }

         var6 = (int)var1.getPeekPosition();
         if (!var2) {
            var1.skipFully(var6);
         }

         var12 = 0;
         var7 = 0;
         var8 = 0;
      } else {
         var12 = 0;
         var7 = 0;
         var8 = 0;
         var6 = 0;
      }

      while(true) {
         if (this.peekEndOfStreamOrHeader(var1)) {
            if (var7 <= 0) {
               throw new EOFException();
            }
            break;
         }

         this.scratch.setPosition(0);
         int var9 = this.scratch.readInt();
         if (var12 == 0 || headersMatch(var9, (long)var12)) {
            int var10 = MpegAudioHeader.getFrameSize(var9);
            if (var10 != -1) {
               int var11 = var7 + 1;
               if (var11 == 1) {
                  MpegAudioHeader.populateHeader(var9, this.synchronizedHeader);
                  var7 = var9;
               } else {
                  var7 = var12;
                  if (var11 == 4) {
                     break;
                  }
               }

               var1.advancePeekPosition(var10 - 4);
               var12 = var7;
               var7 = var11;
               continue;
            }
         }

         var12 = var8 + 1;
         if (var8 == var3) {
            if (var2) {
               return false;
            }

            throw new ParserException("Searched too many bytes.");
         }

         if (var2) {
            var1.resetPeekPosition();
            var1.advancePeekPosition(var6 + var12);
         } else {
            var1.skipFully(1);
         }

         var8 = var12;
         var12 = 0;
         var7 = 0;
      }

      if (var2) {
         var1.skipFully(var6 + var8);
      } else {
         var1.resetPeekPosition();
      }

      this.synchronizedHeaderData = var12;
      return true;
   }

   public void init(ExtractorOutput var1) {
      this.extractorOutput = var1;
      this.trackOutput = this.extractorOutput.track(0, 1);
      this.extractorOutput.endTracks();
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      if (this.synchronizedHeaderData == 0) {
         try {
            this.synchronize(var1, false);
         } catch (EOFException var9) {
            return -1;
         }
      }

      if (this.seeker == null) {
         Mp3Extractor.Seeker var3 = this.maybeReadSeekFrame(var1);
         MlltSeeker var10 = maybeHandleSeekMetadata(this.metadata, var1.getPosition());
         if (var10 != null) {
            this.seeker = var10;
         } else if (var3 != null) {
            this.seeker = var3;
         }

         Mp3Extractor.Seeker var11 = this.seeker;
         if (var11 == null || !var11.isSeekable() && (this.flags & 1) != 0) {
            this.seeker = this.getConstantBitrateSeeker(var1);
         }

         this.extractorOutput.seekMap(this.seeker);
         TrackOutput var13 = this.trackOutput;
         MpegAudioHeader var12 = this.synchronizedHeader;
         String var4 = var12.mimeType;
         int var5 = var12.channels;
         int var6 = var12.sampleRate;
         GaplessInfoHolder var14 = this.gaplessInfoHolder;
         int var7 = var14.encoderDelay;
         int var8 = var14.encoderPadding;
         Metadata var15;
         if ((this.flags & 2) != 0) {
            var15 = null;
         } else {
            var15 = this.metadata;
         }

         var13.format(Format.createAudioSampleFormat((String)null, var4, (String)null, -1, 4096, var5, var6, -1, var7, var8, (List)null, (DrmInitData)null, 0, (String)null, var15));
      }

      return this.readSample(var1);
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      this.synchronizedHeaderData = 0;
      this.basisTimeUs = -9223372036854775807L;
      this.samplesRead = 0L;
      this.sampleBytesRemaining = 0;
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      return this.synchronize(var1, true);
   }

   interface Seeker extends SeekMap {
      long getDataEndPosition();

      long getTimeUs(long var1);
   }
}
