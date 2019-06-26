package com.google.android.exoplayer2.util;

import android.text.TextUtils;
import java.util.ArrayList;

public final class MimeTypes {
   public static final String APPLICATION_CAMERA_MOTION = "application/x-camera-motion";
   public static final String APPLICATION_CEA608 = "application/cea-608";
   public static final String APPLICATION_CEA708 = "application/cea-708";
   public static final String APPLICATION_DVBSUBS = "application/dvbsubs";
   public static final String APPLICATION_EMSG = "application/x-emsg";
   public static final String APPLICATION_EXIF = "application/x-exif";
   public static final String APPLICATION_ICY = "application/x-icy";
   public static final String APPLICATION_ID3 = "application/id3";
   public static final String APPLICATION_M3U8 = "application/x-mpegURL";
   public static final String APPLICATION_MP4 = "application/mp4";
   public static final String APPLICATION_MP4CEA608 = "application/x-mp4-cea-608";
   public static final String APPLICATION_MP4VTT = "application/x-mp4-vtt";
   public static final String APPLICATION_MPD = "application/dash+xml";
   public static final String APPLICATION_PGS = "application/pgs";
   public static final String APPLICATION_RAWCC = "application/x-rawcc";
   public static final String APPLICATION_SCTE35 = "application/x-scte35";
   public static final String APPLICATION_SS = "application/vnd.ms-sstr+xml";
   public static final String APPLICATION_SUBRIP = "application/x-subrip";
   public static final String APPLICATION_TTML = "application/ttml+xml";
   public static final String APPLICATION_TX3G = "application/x-quicktime-tx3g";
   public static final String APPLICATION_VOBSUB = "application/vobsub";
   public static final String APPLICATION_WEBM = "application/webm";
   public static final String AUDIO_AAC = "audio/mp4a-latm";
   public static final String AUDIO_AC3 = "audio/ac3";
   public static final String AUDIO_ALAC = "audio/alac";
   public static final String AUDIO_ALAW = "audio/g711-alaw";
   public static final String AUDIO_AMR_NB = "audio/3gpp";
   public static final String AUDIO_AMR_WB = "audio/amr-wb";
   public static final String AUDIO_DTS = "audio/vnd.dts";
   public static final String AUDIO_DTS_EXPRESS = "audio/vnd.dts.hd;profile=lbr";
   public static final String AUDIO_DTS_HD = "audio/vnd.dts.hd";
   public static final String AUDIO_E_AC3 = "audio/eac3";
   public static final String AUDIO_E_AC3_JOC = "audio/eac3-joc";
   public static final String AUDIO_FLAC = "audio/flac";
   public static final String AUDIO_MLAW = "audio/g711-mlaw";
   public static final String AUDIO_MP4 = "audio/mp4";
   public static final String AUDIO_MPEG = "audio/mpeg";
   public static final String AUDIO_MPEG_L1 = "audio/mpeg-L1";
   public static final String AUDIO_MPEG_L2 = "audio/mpeg-L2";
   public static final String AUDIO_MSGSM = "audio/gsm";
   public static final String AUDIO_OPUS = "audio/opus";
   public static final String AUDIO_RAW = "audio/raw";
   public static final String AUDIO_TRUEHD = "audio/true-hd";
   public static final String AUDIO_UNKNOWN = "audio/x-unknown";
   public static final String AUDIO_VORBIS = "audio/vorbis";
   public static final String AUDIO_WEBM = "audio/webm";
   public static final String BASE_TYPE_APPLICATION = "application";
   public static final String BASE_TYPE_AUDIO = "audio";
   public static final String BASE_TYPE_TEXT = "text";
   public static final String BASE_TYPE_VIDEO = "video";
   public static final String TEXT_SSA = "text/x-ssa";
   public static final String TEXT_VTT = "text/vtt";
   public static final String VIDEO_H263 = "video/3gpp";
   public static final String VIDEO_H264 = "video/avc";
   public static final String VIDEO_H265 = "video/hevc";
   public static final String VIDEO_MP4 = "video/mp4";
   public static final String VIDEO_MP4V = "video/mp4v-es";
   public static final String VIDEO_MPEG = "video/mpeg";
   public static final String VIDEO_MPEG2 = "video/mpeg2";
   public static final String VIDEO_UNKNOWN = "video/x-unknown";
   public static final String VIDEO_VC1 = "video/wvc1";
   public static final String VIDEO_VP8 = "video/x-vnd.on2.vp8";
   public static final String VIDEO_VP9 = "video/x-vnd.on2.vp9";
   public static final String VIDEO_WEBM = "video/webm";
   private static final ArrayList customMimeTypes = new ArrayList();

   private MimeTypes() {
   }

   public static String getAudioMediaMimeType(String var0) {
      if (var0 == null) {
         return null;
      } else {
         String[] var4 = Util.splitCodecs(var0);
         int var1 = var4.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            String var3 = getMediaMimeType(var4[var2]);
            if (var3 != null && isAudio(var3)) {
               return var3;
            }
         }

         return null;
      }
   }

   private static String getCustomMimeTypeForCodec(String var0) {
      int var1 = customMimeTypes.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         MimeTypes.CustomMimeType var3 = (MimeTypes.CustomMimeType)customMimeTypes.get(var2);
         if (var0.startsWith(var3.codecPrefix)) {
            return var3.mimeType;
         }
      }

      return null;
   }

   public static int getEncoding(String var0) {
      byte var1;
      label52: {
         switch(var0.hashCode()) {
         case -2123537834:
            if (var0.equals("audio/eac3-joc")) {
               var1 = 2;
               break label52;
            }
            break;
         case -1095064472:
            if (var0.equals("audio/vnd.dts")) {
               var1 = 3;
               break label52;
            }
            break;
         case 187078296:
            if (var0.equals("audio/ac3")) {
               var1 = 0;
               break label52;
            }
            break;
         case 1504578661:
            if (var0.equals("audio/eac3")) {
               var1 = 1;
               break label52;
            }
            break;
         case 1505942594:
            if (var0.equals("audio/vnd.dts.hd")) {
               var1 = 4;
               break label52;
            }
            break;
         case 1556697186:
            if (var0.equals("audio/true-hd")) {
               var1 = 5;
               break label52;
            }
         }

         var1 = -1;
      }

      if (var1 != 0) {
         if (var1 != 1 && var1 != 2) {
            if (var1 != 3) {
               if (var1 != 4) {
                  return var1 != 5 ? 0 : 14;
               } else {
                  return 8;
               }
            } else {
               return 7;
            }
         } else {
            return 6;
         }
      } else {
         return 5;
      }
   }

   public static String getMediaMimeType(String var0) {
      String var1 = null;
      if (var0 == null) {
         return null;
      } else {
         String var2 = Util.toLowerInvariant(var0.trim());
         if (!var2.startsWith("avc1") && !var2.startsWith("avc3")) {
            if (!var2.startsWith("hev1") && !var2.startsWith("hvc1")) {
               if (!var2.startsWith("vp9") && !var2.startsWith("vp09")) {
                  if (!var2.startsWith("vp8") && !var2.startsWith("vp08")) {
                     if (var2.startsWith("mp4a")) {
                        var0 = var1;
                        if (var2.startsWith("mp4a.")) {
                           var2 = var2.substring(5);
                           var0 = var1;
                           if (var2.length() >= 2) {
                              try {
                                 var0 = getMimeTypeFromMp4ObjectType(Integer.parseInt(Util.toUpperInvariant(var2.substring(0, 2)), 16));
                              } catch (NumberFormatException var3) {
                                 var0 = var1;
                              }
                           }
                        }

                        var1 = var0;
                        if (var0 == null) {
                           var1 = "audio/mp4a-latm";
                        }

                        return var1;
                     } else if (!var2.startsWith("ac-3") && !var2.startsWith("dac3")) {
                        if (!var2.startsWith("ec-3") && !var2.startsWith("dec3")) {
                           if (var2.startsWith("ec+3")) {
                              return "audio/eac3-joc";
                           } else if (!var2.startsWith("dtsc") && !var2.startsWith("dtse")) {
                              if (!var2.startsWith("dtsh") && !var2.startsWith("dtsl")) {
                                 if (var2.startsWith("opus")) {
                                    return "audio/opus";
                                 } else if (var2.startsWith("vorbis")) {
                                    return "audio/vorbis";
                                 } else {
                                    return var2.startsWith("flac") ? "audio/flac" : getCustomMimeTypeForCodec(var2);
                                 }
                              } else {
                                 return "audio/vnd.dts.hd";
                              }
                           } else {
                              return "audio/vnd.dts";
                           }
                        } else {
                           return "audio/eac3";
                        }
                     } else {
                        return "audio/ac3";
                     }
                  } else {
                     return "video/x-vnd.on2.vp8";
                  }
               } else {
                  return "video/x-vnd.on2.vp9";
               }
            } else {
               return "video/hevc";
            }
         } else {
            return "video/avc";
         }
      }
   }

   public static String getMimeTypeFromMp4ObjectType(int var0) {
      if (var0 != 32) {
         if (var0 != 33) {
            if (var0 != 35) {
               if (var0 != 64) {
                  if (var0 == 163) {
                     return "video/wvc1";
                  }

                  if (var0 == 177) {
                     return "video/x-vnd.on2.vp9";
                  }

                  if (var0 == 165) {
                     return "audio/ac3";
                  }

                  if (var0 == 166) {
                     return "audio/eac3";
                  }

                  switch(var0) {
                  case 96:
                  case 97:
                  case 98:
                  case 99:
                  case 100:
                  case 101:
                     return "video/mpeg2";
                  case 102:
                  case 103:
                  case 104:
                     break;
                  case 105:
                  case 107:
                     return "audio/mpeg";
                  case 106:
                     return "video/mpeg";
                  default:
                     switch(var0) {
                     case 169:
                     case 172:
                        return "audio/vnd.dts";
                     case 170:
                     case 171:
                        return "audio/vnd.dts.hd";
                     case 173:
                        return "audio/opus";
                     default:
                        return null;
                     }
                  }
               }

               return "audio/mp4a-latm";
            } else {
               return "video/hevc";
            }
         } else {
            return "video/avc";
         }
      } else {
         return "video/mp4v-es";
      }
   }

   private static String getTopLevelType(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf(47);
         if (var1 != -1) {
            return var0.substring(0, var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Invalid mime type: ");
            var2.append(var0);
            throw new IllegalArgumentException(var2.toString());
         }
      }
   }

   public static int getTrackType(String var0) {
      if (TextUtils.isEmpty(var0)) {
         return -1;
      } else if (isAudio(var0)) {
         return 1;
      } else if (isVideo(var0)) {
         return 2;
      } else if (!isText(var0) && !"application/cea-608".equals(var0) && !"application/cea-708".equals(var0) && !"application/x-mp4-cea-608".equals(var0) && !"application/x-subrip".equals(var0) && !"application/ttml+xml".equals(var0) && !"application/x-quicktime-tx3g".equals(var0) && !"application/x-mp4-vtt".equals(var0) && !"application/x-rawcc".equals(var0) && !"application/vobsub".equals(var0) && !"application/pgs".equals(var0) && !"application/dvbsubs".equals(var0)) {
         if (!"application/id3".equals(var0) && !"application/x-emsg".equals(var0) && !"application/x-scte35".equals(var0)) {
            return "application/x-camera-motion".equals(var0) ? 5 : getTrackTypeForCustomMimeType(var0);
         } else {
            return 4;
         }
      } else {
         return 3;
      }
   }

   private static int getTrackTypeForCustomMimeType(String var0) {
      int var1 = customMimeTypes.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         MimeTypes.CustomMimeType var3 = (MimeTypes.CustomMimeType)customMimeTypes.get(var2);
         if (var0.equals(var3.mimeType)) {
            return var3.trackType;
         }
      }

      return -1;
   }

   public static int getTrackTypeOfCodec(String var0) {
      return getTrackType(getMediaMimeType(var0));
   }

   public static String getVideoMediaMimeType(String var0) {
      if (var0 == null) {
         return null;
      } else {
         String[] var4 = Util.splitCodecs(var0);
         int var1 = var4.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            String var3 = getMediaMimeType(var4[var2]);
            if (var3 != null && isVideo(var3)) {
               return var3;
            }
         }

         return null;
      }
   }

   public static boolean isApplication(String var0) {
      return "application".equals(getTopLevelType(var0));
   }

   public static boolean isAudio(String var0) {
      return "audio".equals(getTopLevelType(var0));
   }

   public static boolean isText(String var0) {
      return "text".equals(getTopLevelType(var0));
   }

   public static boolean isVideo(String var0) {
      return "video".equals(getTopLevelType(var0));
   }

   public static void registerCustomMimeType(String var0, String var1, int var2) {
      MimeTypes.CustomMimeType var4 = new MimeTypes.CustomMimeType(var0, var1, var2);
      int var3 = customMimeTypes.size();

      for(var2 = 0; var2 < var3; ++var2) {
         if (var0.equals(((MimeTypes.CustomMimeType)customMimeTypes.get(var2)).mimeType)) {
            customMimeTypes.remove(var2);
            break;
         }
      }

      customMimeTypes.add(var4);
   }

   private static final class CustomMimeType {
      public final String codecPrefix;
      public final String mimeType;
      public final int trackType;

      public CustomMimeType(String var1, String var2, int var3) {
         this.mimeType = var1;
         this.codecPrefix = var2;
         this.trackType = var3;
      }
   }
}
