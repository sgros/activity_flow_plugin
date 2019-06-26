package com.google.android.exoplayer2.text;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.text.cea.Cea608Decoder;
import com.google.android.exoplayer2.text.cea.Cea708Decoder;
import com.google.android.exoplayer2.text.dvb.DvbDecoder;
import com.google.android.exoplayer2.text.pgs.PgsDecoder;
import com.google.android.exoplayer2.text.ssa.SsaDecoder;
import com.google.android.exoplayer2.text.subrip.SubripDecoder;
import com.google.android.exoplayer2.text.ttml.TtmlDecoder;
import com.google.android.exoplayer2.text.tx3g.Tx3gDecoder;
import com.google.android.exoplayer2.text.webvtt.Mp4WebvttDecoder;
import com.google.android.exoplayer2.text.webvtt.WebvttDecoder;

public interface SubtitleDecoderFactory {
   SubtitleDecoderFactory DEFAULT = new SubtitleDecoderFactory() {
      public SubtitleDecoder createDecoder(Format var1) {
         byte var3;
         label57: {
            String var2 = var1.sampleMimeType;
            switch(var2.hashCode()) {
            case -1351681404:
               if (var2.equals("application/dvbsubs")) {
                  var3 = 9;
                  break label57;
               }
               break;
            case -1248334819:
               if (var2.equals("application/pgs")) {
                  var3 = 10;
                  break label57;
               }
               break;
            case -1026075066:
               if (var2.equals("application/x-mp4-vtt")) {
                  var3 = 2;
                  break label57;
               }
               break;
            case -1004728940:
               if (var2.equals("text/vtt")) {
                  var3 = 0;
                  break label57;
               }
               break;
            case 691401887:
               if (var2.equals("application/x-quicktime-tx3g")) {
                  var3 = 5;
                  break label57;
               }
               break;
            case 822864842:
               if (var2.equals("text/x-ssa")) {
                  var3 = 1;
                  break label57;
               }
               break;
            case 930165504:
               if (var2.equals("application/x-mp4-cea-608")) {
                  var3 = 7;
                  break label57;
               }
               break;
            case 1566015601:
               if (var2.equals("application/cea-608")) {
                  var3 = 6;
                  break label57;
               }
               break;
            case 1566016562:
               if (var2.equals("application/cea-708")) {
                  var3 = 8;
                  break label57;
               }
               break;
            case 1668750253:
               if (var2.equals("application/x-subrip")) {
                  var3 = 4;
                  break label57;
               }
               break;
            case 1693976202:
               if (var2.equals("application/ttml+xml")) {
                  var3 = 3;
                  break label57;
               }
            }

            var3 = -1;
         }

         switch(var3) {
         case 0:
            return new WebvttDecoder();
         case 1:
            return new SsaDecoder(var1.initializationData);
         case 2:
            return new Mp4WebvttDecoder();
         case 3:
            return new TtmlDecoder();
         case 4:
            return new SubripDecoder();
         case 5:
            return new Tx3gDecoder(var1.initializationData);
         case 6:
         case 7:
            return new Cea608Decoder(var1.sampleMimeType, var1.accessibilityChannel);
         case 8:
            return new Cea708Decoder(var1.accessibilityChannel, var1.initializationData);
         case 9:
            return new DvbDecoder(var1.initializationData);
         case 10:
            return new PgsDecoder();
         default:
            throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
         }
      }

      public boolean supportsFormat(Format var1) {
         String var3 = var1.sampleMimeType;
         boolean var2;
         if (!"text/vtt".equals(var3) && !"text/x-ssa".equals(var3) && !"application/ttml+xml".equals(var3) && !"application/x-mp4-vtt".equals(var3) && !"application/x-subrip".equals(var3) && !"application/x-quicktime-tx3g".equals(var3) && !"application/cea-608".equals(var3) && !"application/x-mp4-cea-608".equals(var3) && !"application/cea-708".equals(var3) && !"application/dvbsubs".equals(var3) && !"application/pgs".equals(var3)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }
   };

   SubtitleDecoder createDecoder(Format var1);

   boolean supportsFormat(Format var1);
}
