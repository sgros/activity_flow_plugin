package com.google.android.exoplayer2.extractor.wav;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.WavUtil;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

final class WavHeaderReader {
   public static WavHeader peek(ExtractorInput var0) throws IOException, InterruptedException {
      Assertions.checkNotNull(var0);
      ParsableByteArray var1 = new ParsableByteArray(16);
      if (WavHeaderReader.ChunkHeader.peek(var0, var1).id != WavUtil.RIFF_FOURCC) {
         return null;
      } else {
         var0.peekFully(var1.data, 0, 4);
         var1.setPosition(0);
         int var2 = var1.readInt();
         StringBuilder var11;
         if (var2 != WavUtil.WAVE_FOURCC) {
            var11 = new StringBuilder();
            var11.append("Unsupported RIFF format: ");
            var11.append(var2);
            Log.e("WavHeaderReader", var11.toString());
            return null;
         } else {
            WavHeaderReader.ChunkHeader var3;
            for(var3 = WavHeaderReader.ChunkHeader.peek(var0, var1); var3.id != WavUtil.FMT_FOURCC; var3 = WavHeaderReader.ChunkHeader.peek(var0, var1)) {
               var0.advancePeekPosition((int)var3.size);
            }

            boolean var4;
            if (var3.size >= 16L) {
               var4 = true;
            } else {
               var4 = false;
            }

            Assertions.checkState(var4);
            var0.peekFully(var1.data, 0, 16);
            var1.setPosition(0);
            var2 = var1.readLittleEndianUnsignedShort();
            int var5 = var1.readLittleEndianUnsignedShort();
            int var6 = var1.readLittleEndianUnsignedIntToInt();
            int var7 = var1.readLittleEndianUnsignedIntToInt();
            int var8 = var1.readLittleEndianUnsignedShort();
            int var9 = var1.readLittleEndianUnsignedShort();
            int var10 = var5 * var9 / 8;
            if (var8 == var10) {
               var10 = WavUtil.getEncodingForType(var2, var9);
               if (var10 == 0) {
                  var11 = new StringBuilder();
                  var11.append("Unsupported WAV format: ");
                  var11.append(var9);
                  var11.append(" bit/sample, type ");
                  var11.append(var2);
                  Log.e("WavHeaderReader", var11.toString());
                  return null;
               } else {
                  var0.advancePeekPosition((int)var3.size - 16);
                  return new WavHeader(var5, var6, var7, var8, var9, var10);
               }
            } else {
               var11 = new StringBuilder();
               var11.append("Expected block alignment: ");
               var11.append(var10);
               var11.append("; got: ");
               var11.append(var8);
               throw new ParserException(var11.toString());
            }
         }
      }
   }

   public static void skipToData(ExtractorInput var0, WavHeader var1) throws IOException, InterruptedException {
      Assertions.checkNotNull(var0);
      Assertions.checkNotNull(var1);
      var0.resetPeekPosition();
      ParsableByteArray var2 = new ParsableByteArray(8);

      WavHeaderReader.ChunkHeader var3;
      for(var3 = WavHeaderReader.ChunkHeader.peek(var0, var2); var3.id != Util.getIntegerCodeForString("data"); var3 = WavHeaderReader.ChunkHeader.peek(var0, var2)) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Ignoring unknown WAV chunk: ");
         var4.append(var3.id);
         Log.w("WavHeaderReader", var4.toString());
         long var5 = var3.size + 8L;
         if (var3.id == Util.getIntegerCodeForString("RIFF")) {
            var5 = 12L;
         }

         if (var5 > 2147483647L) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Chunk is too large (~2GB+) to skip; id: ");
            var7.append(var3.id);
            throw new ParserException(var7.toString());
         }

         var0.skipFully((int)var5);
      }

      var0.skipFully(8);
      var1.setDataBounds(var0.getPosition(), var3.size);
   }

   private static final class ChunkHeader {
      public final int id;
      public final long size;

      private ChunkHeader(int var1, long var2) {
         this.id = var1;
         this.size = var2;
      }

      public static WavHeaderReader.ChunkHeader peek(ExtractorInput var0, ParsableByteArray var1) throws IOException, InterruptedException {
         var0.peekFully(var1.data, 0, 8);
         var1.setPosition(0);
         return new WavHeaderReader.ChunkHeader(var1.readInt(), var1.readLittleEndianUnsignedInt());
      }
   }
}
