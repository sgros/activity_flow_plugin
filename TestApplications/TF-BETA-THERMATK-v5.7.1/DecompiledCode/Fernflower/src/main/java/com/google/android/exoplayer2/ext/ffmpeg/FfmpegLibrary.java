package com.google.android.exoplayer2.ext.ffmpeg;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;

public final class FfmpegLibrary {
   static {
      ExoPlayerLibraryInfo.registerModule("goog.exo.ffmpeg");
   }

   private FfmpegLibrary() {
   }

   private static native String ffmpegGetVersion();

   private static native boolean ffmpegHasDecoder(String var0);

   static String getCodecName(String var0, int var1) {
      byte var2;
      label86: {
         switch(var0.hashCode()) {
         case -2123537834:
            if (var0.equals("audio/eac3-joc")) {
               var2 = 6;
               break label86;
            }
            break;
         case -1606874997:
            if (var0.equals("audio/amr-wb")) {
               var2 = 13;
               break label86;
            }
            break;
         case -1095064472:
            if (var0.equals("audio/vnd.dts")) {
               var2 = 8;
               break label86;
            }
            break;
         case -1003765268:
            if (var0.equals("audio/vorbis")) {
               var2 = 10;
               break label86;
            }
            break;
         case -432837260:
            if (var0.equals("audio/mpeg-L1")) {
               var2 = 2;
               break label86;
            }
            break;
         case -432837259:
            if (var0.equals("audio/mpeg-L2")) {
               var2 = 3;
               break label86;
            }
            break;
         case -53558318:
            if (var0.equals("audio/mp4a-latm")) {
               var2 = 0;
               break label86;
            }
            break;
         case 187078296:
            if (var0.equals("audio/ac3")) {
               var2 = 4;
               break label86;
            }
            break;
         case 187094639:
            if (var0.equals("audio/raw")) {
               var2 = 16;
               break label86;
            }
            break;
         case 1503095341:
            if (var0.equals("audio/3gpp")) {
               var2 = 12;
               break label86;
            }
            break;
         case 1504470054:
            if (var0.equals("audio/alac")) {
               var2 = 15;
               break label86;
            }
            break;
         case 1504578661:
            if (var0.equals("audio/eac3")) {
               var2 = 5;
               break label86;
            }
            break;
         case 1504619009:
            if (var0.equals("audio/flac")) {
               var2 = 14;
               break label86;
            }
            break;
         case 1504831518:
            if (var0.equals("audio/mpeg")) {
               var2 = 1;
               break label86;
            }
            break;
         case 1504891608:
            if (var0.equals("audio/opus")) {
               var2 = 11;
               break label86;
            }
            break;
         case 1505942594:
            if (var0.equals("audio/vnd.dts.hd")) {
               var2 = 9;
               break label86;
            }
            break;
         case 1556697186:
            if (var0.equals("audio/true-hd")) {
               var2 = 7;
               break label86;
            }
         }

         var2 = -1;
      }

      switch(var2) {
      case 0:
         return "aac";
      case 1:
      case 2:
      case 3:
         return "mp3";
      case 4:
         return "ac3";
      case 5:
      case 6:
         return "eac3";
      case 7:
         return "truehd";
      case 8:
      case 9:
         return "dca";
      case 10:
         return "vorbis";
      case 11:
         return "opus";
      case 12:
         return "amrnb";
      case 13:
         return "amrwb";
      case 14:
         return "flac";
      case 15:
         return "alac";
      case 16:
         if (var1 == 268435456) {
            return "pcm_mulaw";
         } else {
            if (var1 == 536870912) {
               return "pcm_alaw";
            }

            return null;
         }
      default:
         return null;
      }
   }

   public static String getVersion() {
      return ffmpegGetVersion();
   }

   public static boolean supportsFormat(String var0, int var1) {
      var0 = getCodecName(var0, var1);
      boolean var2;
      if (var0 != null && ffmpegHasDecoder(var0)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
