package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.video.AvcConfig;

final class VideoTagPayloadReader extends TagPayloadReader {
   private int frameType;
   private boolean hasOutputFormat;
   private final ParsableByteArray nalLength;
   private final ParsableByteArray nalStartCode;
   private int nalUnitLengthFieldLength;

   public VideoTagPayloadReader(TrackOutput var1) {
      super(var1);
      this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
      this.nalLength = new ParsableByteArray(4);
   }

   protected boolean parseHeader(ParsableByteArray var1) throws TagPayloadReader.UnsupportedFormatException {
      int var2 = var1.readUnsignedByte();
      int var3 = var2 >> 4 & 15;
      var2 &= 15;
      if (var2 == 7) {
         this.frameType = var3;
         boolean var4;
         if (var3 != 5) {
            var4 = true;
         } else {
            var4 = false;
         }

         return var4;
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Video format not supported: ");
         var5.append(var2);
         throw new TagPayloadReader.UnsupportedFormatException(var5.toString());
      }
   }

   protected void parsePayload(ParsableByteArray var1, long var2) throws ParserException {
      int var4 = var1.readUnsignedByte();
      long var5 = (long)var1.readInt24();
      if (var4 == 0 && !this.hasOutputFormat) {
         ParsableByteArray var13 = new ParsableByteArray(new byte[var1.bytesLeft()]);
         var1.readBytes(var13.data, 0, var1.bytesLeft());
         AvcConfig var11 = AvcConfig.parse(var13);
         this.nalUnitLengthFieldLength = var11.nalUnitLengthFieldLength;
         Format var12 = Format.createVideoSampleFormat((String)null, "video/avc", (String)null, -1, -1, var11.width, var11.height, -1.0F, var11.initializationData, -1, var11.pixelWidthAspectRatio, (DrmInitData)null);
         super.output.format(var12);
         this.hasOutputFormat = true;
      } else if (var4 == 1 && this.hasOutputFormat) {
         byte[] var7 = this.nalLength.data;
         var7[0] = (byte)0;
         var7[1] = (byte)0;
         var7[2] = (byte)0;
         int var8 = this.nalUnitLengthFieldLength;

         int var9;
         for(var4 = 0; var1.bytesLeft() > 0; var4 = var4 + 4 + var9) {
            var1.readBytes(this.nalLength.data, 4 - var8, this.nalUnitLengthFieldLength);
            this.nalLength.setPosition(0);
            var9 = this.nalLength.readUnsignedIntToInt();
            this.nalStartCode.setPosition(0);
            super.output.sampleData(this.nalStartCode, 4);
            super.output.sampleData(var1, var9);
         }

         TrackOutput var10 = super.output;
         byte var14;
         if (this.frameType == 1) {
            var14 = 1;
         } else {
            var14 = 0;
         }

         var10.sampleMetadata(var2 + var5 * 1000L, var14, var4, 0, (TrackOutput.CryptoData)null);
      }

   }
}
