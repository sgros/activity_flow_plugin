package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import java.util.ArrayList;

final class VorbisReader extends StreamReader {
   private VorbisUtil.CommentHeader commentHeader;
   private int previousPacketBlockSize;
   private boolean seenFirstAudioPacket;
   private VorbisUtil.VorbisIdHeader vorbisIdHeader;
   private VorbisReader.VorbisSetup vorbisSetup;

   static void appendNumberOfSamples(ParsableByteArray var0, long var1) {
      var0.setLimit(var0.limit() + 4);
      var0.data[var0.limit() - 4] = (byte)((byte)((int)(var1 & 255L)));
      var0.data[var0.limit() - 3] = (byte)((byte)((int)(var1 >>> 8 & 255L)));
      var0.data[var0.limit() - 2] = (byte)((byte)((int)(var1 >>> 16 & 255L)));
      var0.data[var0.limit() - 1] = (byte)((byte)((int)(var1 >>> 24 & 255L)));
   }

   private static int decodeBlockSize(byte var0, VorbisReader.VorbisSetup var1) {
      int var2 = readBits(var0, var1.iLogModes, 1);
      if (!var1.modes[var2].blockFlag) {
         var2 = var1.idHeader.blockSize0;
      } else {
         var2 = var1.idHeader.blockSize1;
      }

      return var2;
   }

   static int readBits(byte var0, int var1, int var2) {
      return var0 >> var2 & 255 >>> 8 - var1;
   }

   public static boolean verifyBitstreamType(ParsableByteArray var0) {
      try {
         boolean var1 = VorbisUtil.verifyVorbisHeaderCapturePattern(1, var0, true);
         return var1;
      } catch (ParserException var2) {
         return false;
      }
   }

   protected void onSeekEnd(long var1) {
      super.onSeekEnd(var1);
      int var3 = 0;
      boolean var4;
      if (var1 != 0L) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.seenFirstAudioPacket = var4;
      VorbisUtil.VorbisIdHeader var5 = this.vorbisIdHeader;
      if (var5 != null) {
         var3 = var5.blockSize0;
      }

      this.previousPacketBlockSize = var3;
   }

   protected long preparePayload(ParsableByteArray var1) {
      byte[] var2 = var1.data;
      int var3 = 0;
      if ((var2[0] & 1) == 1) {
         return -1L;
      } else {
         int var4 = decodeBlockSize(var2[0], this.vorbisSetup);
         if (this.seenFirstAudioPacket) {
            var3 = (this.previousPacketBlockSize + var4) / 4;
         }

         long var5 = (long)var3;
         appendNumberOfSamples(var1, var5);
         this.seenFirstAudioPacket = true;
         this.previousPacketBlockSize = var4;
         return var5;
      }
   }

   protected boolean readHeaders(ParsableByteArray var1, long var2, StreamReader.SetupData var4) throws IOException, InterruptedException {
      if (this.vorbisSetup != null) {
         return false;
      } else {
         this.vorbisSetup = this.readSetupHeaders(var1);
         if (this.vorbisSetup == null) {
            return true;
         } else {
            ArrayList var5 = new ArrayList();
            var5.add(this.vorbisSetup.idHeader.data);
            var5.add(this.vorbisSetup.setupHeaderData);
            VorbisUtil.VorbisIdHeader var6 = this.vorbisSetup.idHeader;
            var4.format = Format.createAudioSampleFormat((String)null, "audio/vorbis", (String)null, var6.bitrateNominal, -1, var6.channels, (int)var6.sampleRate, var5, (DrmInitData)null, 0, (String)null);
            return true;
         }
      }
   }

   VorbisReader.VorbisSetup readSetupHeaders(ParsableByteArray var1) throws IOException {
      if (this.vorbisIdHeader == null) {
         this.vorbisIdHeader = VorbisUtil.readVorbisIdentificationHeader(var1);
         return null;
      } else if (this.commentHeader == null) {
         this.commentHeader = VorbisUtil.readVorbisCommentHeader(var1);
         return null;
      } else {
         byte[] var2 = new byte[var1.limit()];
         System.arraycopy(var1.data, 0, var2, 0, var1.limit());
         VorbisUtil.Mode[] var4 = VorbisUtil.readVorbisModes(var1, this.vorbisIdHeader.channels);
         int var3 = VorbisUtil.iLog(var4.length - 1);
         return new VorbisReader.VorbisSetup(this.vorbisIdHeader, this.commentHeader, var2, var4, var3);
      }
   }

   protected void reset(boolean var1) {
      super.reset(var1);
      if (var1) {
         this.vorbisSetup = null;
         this.vorbisIdHeader = null;
         this.commentHeader = null;
      }

      this.previousPacketBlockSize = 0;
      this.seenFirstAudioPacket = false;
   }

   static final class VorbisSetup {
      public final VorbisUtil.CommentHeader commentHeader;
      public final int iLogModes;
      public final VorbisUtil.VorbisIdHeader idHeader;
      public final VorbisUtil.Mode[] modes;
      public final byte[] setupHeaderData;

      public VorbisSetup(VorbisUtil.VorbisIdHeader var1, VorbisUtil.CommentHeader var2, byte[] var3, VorbisUtil.Mode[] var4, int var5) {
         this.idHeader = var1;
         this.commentHeader = var2;
         this.setupHeaderData = var3;
         this.modes = var4;
         this.iLogModes = var5;
      }
   }
}
