package com.google.android.exoplayer2.extractor.flv;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Collections;
import java.util.List;

final class AudioTagPayloadReader extends TagPayloadReader {
   private static final int[] AUDIO_SAMPLING_RATE_TABLE = new int[]{5512, 11025, 22050, 44100};
   private int audioFormat;
   private boolean hasOutputFormat;
   private boolean hasParsedAudioDataHeader;

   public AudioTagPayloadReader(TrackOutput var1) {
      super(var1);
   }

   protected boolean parseHeader(ParsableByteArray var1) throws TagPayloadReader.UnsupportedFormatException {
      if (!this.hasParsedAudioDataHeader) {
         int var2 = var1.readUnsignedByte();
         this.audioFormat = var2 >> 4 & 15;
         int var3 = this.audioFormat;
         Format var6;
         if (var3 == 2) {
            var6 = Format.createAudioSampleFormat((String)null, "audio/mpeg", (String)null, -1, -1, 1, AUDIO_SAMPLING_RATE_TABLE[var2 >> 2 & 3], (List)null, (DrmInitData)null, 0, (String)null);
            super.output.format(var6);
            this.hasOutputFormat = true;
         } else if (var3 != 7 && var3 != 8) {
            if (var3 != 10) {
               StringBuilder var7 = new StringBuilder();
               var7.append("Audio format not supported: ");
               var7.append(this.audioFormat);
               throw new TagPayloadReader.UnsupportedFormatException(var7.toString());
            }
         } else {
            String var4;
            if (this.audioFormat == 7) {
               var4 = "audio/g711-alaw";
            } else {
               var4 = "audio/g711-mlaw";
            }

            byte var5;
            if ((var2 & 1) == 1) {
               var5 = 2;
            } else {
               var5 = 3;
            }

            var6 = Format.createAudioSampleFormat((String)null, var4, (String)null, -1, -1, 1, 8000, var5, (List)null, (DrmInitData)null, 0, (String)null);
            super.output.format(var6);
            this.hasOutputFormat = true;
         }

         this.hasParsedAudioDataHeader = true;
      } else {
         var1.skipBytes(1);
      }

      return true;
   }

   protected void parsePayload(ParsableByteArray var1, long var2) throws ParserException {
      int var4;
      if (this.audioFormat == 2) {
         var4 = var1.bytesLeft();
         super.output.sampleData(var1, var4);
         super.output.sampleMetadata(var2, 1, var4, 0, (TrackOutput.CryptoData)null);
      } else {
         var4 = var1.readUnsignedByte();
         if (var4 == 0 && !this.hasOutputFormat) {
            byte[] var5 = new byte[var1.bytesLeft()];
            var1.readBytes(var5, 0, var5.length);
            Pair var6 = CodecSpecificDataUtil.parseAacAudioSpecificConfig(var5);
            Format var7 = Format.createAudioSampleFormat((String)null, "audio/mp4a-latm", (String)null, -1, -1, (Integer)var6.second, (Integer)var6.first, Collections.singletonList(var5), (DrmInitData)null, 0, (String)null);
            super.output.format(var7);
            this.hasOutputFormat = true;
         } else if (this.audioFormat != 10 || var4 == 1) {
            var4 = var1.bytesLeft();
            super.output.sampleData(var1, var4);
            super.output.sampleMetadata(var2, 1, var4, 0, (TrackOutput.CryptoData)null);
         }
      }

   }
}
