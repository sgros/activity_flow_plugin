package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class FlacReader extends StreamReader {
   private FlacReader.FlacOggSeeker flacOggSeeker;
   private FlacStreamInfo streamInfo;

   private int getFlacFrameBlockSize(ParsableByteArray var1) {
      int var2 = (var1.data[2] & 255) >> 4;
      short var3;
      switch(var2) {
      case 1:
         return 192;
      case 2:
      case 3:
      case 4:
      case 5:
         var3 = 576;
         var2 -= 2;
         break;
      case 6:
      case 7:
         var1.skipBytes(4);
         var1.readUtf8EncodedLong();
         int var4;
         if (var2 == 6) {
            var4 = var1.readUnsignedByte();
         } else {
            var4 = var1.readUnsignedShort();
         }

         var1.setPosition(0);
         return var4 + 1;
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
         var3 = 256;
         var2 -= 8;
         break;
      default:
         return -1;
      }

      return var3 << var2;
   }

   private static boolean isAudioPacket(byte[] var0) {
      boolean var1 = false;
      if (var0[0] == -1) {
         var1 = true;
      }

      return var1;
   }

   public static boolean verifyBitstreamType(ParsableByteArray var0) {
      boolean var1;
      if (var0.bytesLeft() >= 5 && var0.readUnsignedByte() == 127 && var0.readUnsignedInt() == 1179402563L) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected long preparePayload(ParsableByteArray var1) {
      return !isAudioPacket(var1.data) ? -1L : (long)this.getFlacFrameBlockSize(var1);
   }

   protected boolean readHeaders(ParsableByteArray var1, long var2, StreamReader.SetupData var4) throws IOException, InterruptedException {
      byte[] var5 = var1.data;
      if (this.streamInfo == null) {
         this.streamInfo = new FlacStreamInfo(var5, 17);
         byte[] var7 = Arrays.copyOfRange(var5, 9, var1.limit());
         var7[4] = (byte)-128;
         List var10 = Collections.singletonList(var7);
         int var6 = this.streamInfo.bitRate();
         FlacStreamInfo var8 = this.streamInfo;
         var4.format = Format.createAudioSampleFormat((String)null, "audio/flac", (String)null, -1, var6, var8.channels, var8.sampleRate, var10, (DrmInitData)null, 0, (String)null);
      } else if ((var5[0] & 127) == 3) {
         this.flacOggSeeker = new FlacReader.FlacOggSeeker();
         this.flacOggSeeker.parseSeekTable(var1);
      } else if (isAudioPacket(var5)) {
         FlacReader.FlacOggSeeker var9 = this.flacOggSeeker;
         if (var9 != null) {
            var9.setFirstFrameOffset(var2);
            var4.oggSeeker = this.flacOggSeeker;
         }

         return false;
      }

      return true;
   }

   protected void reset(boolean var1) {
      super.reset(var1);
      if (var1) {
         this.streamInfo = null;
         this.flacOggSeeker = null;
      }

   }

   private class FlacOggSeeker implements OggSeeker, SeekMap {
      private long firstFrameOffset = -1L;
      private long pendingSeekGranule = -1L;
      private long[] seekPointGranules;
      private long[] seekPointOffsets;

      public FlacOggSeeker() {
      }

      public SeekMap createSeekMap() {
         return this;
      }

      public long getDurationUs() {
         return FlacReader.this.streamInfo.durationUs();
      }

      public SeekMap.SeekPoints getSeekPoints(long var1) {
         long var3 = FlacReader.this.convertTimeToGranule(var1);
         int var5 = Util.binarySearchFloor(this.seekPointGranules, var3, true, true);
         var3 = FlacReader.this.convertGranuleToTime(this.seekPointGranules[var5]);
         SeekPoint var6 = new SeekPoint(var3, this.firstFrameOffset + this.seekPointOffsets[var5]);
         if (var3 < var1) {
            long[] var7 = this.seekPointGranules;
            if (var5 != var7.length - 1) {
               FlacReader var8 = FlacReader.this;
               ++var5;
               return new SeekMap.SeekPoints(var6, new SeekPoint(var8.convertGranuleToTime(var7[var5]), this.firstFrameOffset + this.seekPointOffsets[var5]));
            }
         }

         return new SeekMap.SeekPoints(var6);
      }

      public boolean isSeekable() {
         return true;
      }

      public void parseSeekTable(ParsableByteArray var1) {
         var1.skipBytes(1);
         int var2 = var1.readUnsignedInt24() / 18;
         this.seekPointGranules = new long[var2];
         this.seekPointOffsets = new long[var2];

         for(int var3 = 0; var3 < var2; ++var3) {
            this.seekPointGranules[var3] = var1.readLong();
            this.seekPointOffsets[var3] = var1.readLong();
            var1.skipBytes(2);
         }

      }

      public long read(ExtractorInput var1) throws IOException, InterruptedException {
         long var2 = this.pendingSeekGranule;
         if (var2 >= 0L) {
            var2 = -(var2 + 2L);
            this.pendingSeekGranule = -1L;
            return var2;
         } else {
            return -1L;
         }
      }

      public void setFirstFrameOffset(long var1) {
         this.firstFrameOffset = var1;
      }

      public long startSeek(long var1) {
         var1 = FlacReader.this.convertTimeToGranule(var1);
         int var3 = Util.binarySearchFloor(this.seekPointGranules, var1, true, true);
         this.pendingSeekGranule = this.seekPointGranules[var3];
         return var1;
      }
   }
}
