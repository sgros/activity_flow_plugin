package com.google.android.exoplayer2.text.webvtt;

import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Collections;

public final class Mp4WebvttDecoder extends SimpleSubtitleDecoder {
   private static final int TYPE_payl = Util.getIntegerCodeForString("payl");
   private static final int TYPE_sttg = Util.getIntegerCodeForString("sttg");
   private static final int TYPE_vttc = Util.getIntegerCodeForString("vttc");
   private final WebvttCue.Builder builder = new WebvttCue.Builder();
   private final ParsableByteArray sampleData = new ParsableByteArray();

   public Mp4WebvttDecoder() {
      super("Mp4WebvttDecoder");
   }

   private static Cue parseVttCueBox(ParsableByteArray var0, WebvttCue.Builder var1, int var2) throws SubtitleDecoderException {
      var1.reset();

      while(var2 > 0) {
         if (var2 < 8) {
            throw new SubtitleDecoderException("Incomplete vtt cue box header found.");
         }

         int var3 = var0.readInt();
         int var4 = var0.readInt();
         var3 -= 8;
         String var5 = Util.fromUtf8Bytes(var0.data, var0.getPosition(), var3);
         var0.skipBytes(var3);
         var3 = var2 - 8 - var3;
         if (var4 == TYPE_sttg) {
            WebvttCueParser.parseCueSettingsList(var5, var1);
            var2 = var3;
         } else {
            var2 = var3;
            if (var4 == TYPE_payl) {
               WebvttCueParser.parseCueText((String)null, var5.trim(), var1, Collections.emptyList());
               var2 = var3;
            }
         }
      }

      return var1.build();
   }

   protected Mp4WebvttSubtitle decode(byte[] var1, int var2, boolean var3) throws SubtitleDecoderException {
      this.sampleData.reset(var1, var2);
      ArrayList var4 = new ArrayList();

      while(this.sampleData.bytesLeft() > 0) {
         if (this.sampleData.bytesLeft() < 8) {
            throw new SubtitleDecoderException("Incomplete Mp4Webvtt Top Level box header found.");
         }

         var2 = this.sampleData.readInt();
         if (this.sampleData.readInt() == TYPE_vttc) {
            var4.add(parseVttCueBox(this.sampleData, this.builder, var2 - 8));
         } else {
            this.sampleData.skipBytes(var2 - 8);
         }
      }

      return new Mp4WebvttSubtitle(var4);
   }
}
