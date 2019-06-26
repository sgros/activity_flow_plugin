package com.google.android.exoplayer2.mediacodec;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaCodecList;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseIntArray;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({"InlinedApi"})
@TargetApi(16)
public final class MediaCodecUtil {
   private static final SparseIntArray AVC_LEVEL_NUMBER_TO_CONST;
   private static final SparseIntArray AVC_PROFILE_NUMBER_TO_CONST = new SparseIntArray();
   private static final Map HEVC_CODEC_STRING_TO_PROFILE_LEVEL;
   private static final SparseIntArray MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE;
   private static final Pattern PROFILE_PATTERN = Pattern.compile("^\\D?(\\d+)$");
   private static final MediaCodecUtil.RawAudioCodecComparator RAW_AUDIO_CODEC_COMPARATOR = new MediaCodecUtil.RawAudioCodecComparator();
   private static final HashMap decoderInfosCache = new HashMap();
   private static int maxH264DecodableFrameSize = -1;

   static {
      AVC_PROFILE_NUMBER_TO_CONST.put(66, 1);
      AVC_PROFILE_NUMBER_TO_CONST.put(77, 2);
      AVC_PROFILE_NUMBER_TO_CONST.put(88, 4);
      AVC_PROFILE_NUMBER_TO_CONST.put(100, 8);
      AVC_PROFILE_NUMBER_TO_CONST.put(110, 16);
      AVC_PROFILE_NUMBER_TO_CONST.put(122, 32);
      AVC_PROFILE_NUMBER_TO_CONST.put(244, 64);
      AVC_LEVEL_NUMBER_TO_CONST = new SparseIntArray();
      AVC_LEVEL_NUMBER_TO_CONST.put(10, 1);
      AVC_LEVEL_NUMBER_TO_CONST.put(11, 4);
      AVC_LEVEL_NUMBER_TO_CONST.put(12, 8);
      AVC_LEVEL_NUMBER_TO_CONST.put(13, 16);
      AVC_LEVEL_NUMBER_TO_CONST.put(20, 32);
      AVC_LEVEL_NUMBER_TO_CONST.put(21, 64);
      AVC_LEVEL_NUMBER_TO_CONST.put(22, 128);
      AVC_LEVEL_NUMBER_TO_CONST.put(30, 256);
      AVC_LEVEL_NUMBER_TO_CONST.put(31, 512);
      AVC_LEVEL_NUMBER_TO_CONST.put(32, 1024);
      AVC_LEVEL_NUMBER_TO_CONST.put(40, 2048);
      AVC_LEVEL_NUMBER_TO_CONST.put(41, 4096);
      AVC_LEVEL_NUMBER_TO_CONST.put(42, 8192);
      AVC_LEVEL_NUMBER_TO_CONST.put(50, 16384);
      AVC_LEVEL_NUMBER_TO_CONST.put(51, 32768);
      AVC_LEVEL_NUMBER_TO_CONST.put(52, 65536);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL = new HashMap();
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L30", 1);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L60", 4);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L63", 16);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L90", 64);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L93", 256);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L120", 1024);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L123", 4096);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L150", 16384);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L153", 65536);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L156", 262144);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L180", 1048576);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L183", 4194304);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L186", 16777216);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H30", 2);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H60", 8);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H63", 32);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H90", 128);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H93", 512);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H120", 2048);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H123", 8192);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H150", 32768);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H153", 131072);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H156", 524288);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H180", 2097152);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H183", 8388608);
      HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H186", 33554432);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE = new SparseIntArray();
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(1, 1);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(2, 2);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(3, 3);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(4, 4);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(5, 5);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(6, 6);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(17, 17);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(20, 20);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(23, 23);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(29, 29);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(39, 39);
      MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.put(42, 42);
   }

   private static void applyWorkarounds(String var0, List var1) {
      if ("audio/raw".equals(var0)) {
         Collections.sort(var1, RAW_AUDIO_CODEC_COMPARATOR);
      }

   }

   private static int avcLevelToMaxFrameSize(int var0) {
      if (var0 != 1 && var0 != 2) {
         switch(var0) {
         case 8:
         case 16:
         case 32:
            return 101376;
         case 64:
            return 202752;
         case 128:
         case 256:
            return 414720;
         case 512:
            return 921600;
         case 1024:
            return 1310720;
         case 2048:
         case 4096:
            return 2097152;
         case 8192:
            return 2228224;
         case 16384:
            return 5652480;
         case 32768:
         case 65536:
            return 9437184;
         default:
            return -1;
         }
      } else {
         return 25344;
      }
   }

   private static boolean codecNeedsDisableAdaptationWorkaround(String var0) {
      boolean var1;
      if (Util.SDK_INT > 22 || !"ODROID-XU3".equals(Util.MODEL) && !"Nexus 10".equals(Util.MODEL) || !"OMX.Exynos.AVC.Decoder".equals(var0) && !"OMX.Exynos.AVC.Decoder.secure".equals(var0)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static Pair getAacCodecProfileAndLevel(String var0, String[] var1) {
      StringBuilder var5;
      if (var1.length != 3) {
         var5 = new StringBuilder();
         var5.append("Ignoring malformed MP4A codec string: ");
         var5.append(var0);
         Log.w("MediaCodecUtil", var5.toString());
         return null;
      } else {
         label34: {
            boolean var10001;
            int var2;
            try {
               if (!"audio/mp4a-latm".equals(MimeTypes.getMimeTypeFromMp4ObjectType(Integer.parseInt(var1[1], 16)))) {
                  return null;
               }

               var2 = Integer.parseInt(var1[2]);
               var2 = MP4A_AUDIO_OBJECT_TYPE_TO_PROFILE.get(var2, -1);
            } catch (NumberFormatException var4) {
               var10001 = false;
               break label34;
            }

            if (var2 == -1) {
               return null;
            }

            try {
               Pair var6 = new Pair(var2, 0);
               return var6;
            } catch (NumberFormatException var3) {
               var10001 = false;
            }
         }

         var5 = new StringBuilder();
         var5.append("Ignoring malformed MP4A codec string: ");
         var5.append(var0);
         Log.w("MediaCodecUtil", var5.toString());
         return null;
      }
   }

   private static Pair getAvcProfileAndLevel(String var0, String[] var1) {
      StringBuilder var7;
      if (var1.length < 2) {
         var7 = new StringBuilder();
         var7.append("Ignoring malformed AVC codec string: ");
         var7.append(var0);
         Log.w("MediaCodecUtil", var7.toString());
         return null;
      } else {
         int var2;
         Integer var6;
         Integer var9;
         label40: {
            Integer var3;
            label39: {
               try {
                  if (var1[1].length() == 6) {
                     var2 = Integer.parseInt(var1[1].substring(0, 2), 16);
                     var3 = Integer.parseInt(var1[1].substring(4), 16);
                     var9 = var2;
                     break label39;
                  }

                  if (var1.length < 3) {
                     var7 = new StringBuilder();
                     var7.append("Ignoring malformed AVC codec string: ");
                     var7.append(var0);
                     Log.w("MediaCodecUtil", var7.toString());
                     return null;
                  }

                  var3 = Integer.parseInt(var1[1]);
                  var9 = Integer.parseInt(var1[2]);
               } catch (NumberFormatException var5) {
                  var7 = new StringBuilder();
                  var7.append("Ignoring malformed AVC codec string: ");
                  var7.append(var0);
                  Log.w("MediaCodecUtil", var7.toString());
                  return null;
               }

               var6 = var9;
               var9 = var3;
               break label40;
            }

            var6 = var3;
         }

         var2 = AVC_PROFILE_NUMBER_TO_CONST.get(var9, -1);
         if (var2 == -1) {
            StringBuilder var8 = new StringBuilder();
            var8.append("Unknown AVC profile: ");
            var8.append(var9);
            Log.w("MediaCodecUtil", var8.toString());
            return null;
         } else {
            int var4 = AVC_LEVEL_NUMBER_TO_CONST.get(var6, -1);
            if (var4 == -1) {
               var7 = new StringBuilder();
               var7.append("Unknown AVC level: ");
               var7.append(var6);
               Log.w("MediaCodecUtil", var7.toString());
               return null;
            } else {
               return new Pair(var2, var4);
            }
         }
      }
   }

   public static Pair getCodecProfileAndLevel(String var0) {
      if (var0 == null) {
         return null;
      } else {
         String[] var1;
         byte var2;
         label46: {
            var1 = var0.split("\\.");
            var2 = 0;
            String var3 = var1[0];
            switch(var3.hashCode()) {
            case 3006243:
               if (var3.equals("avc1")) {
                  var2 = 2;
                  break label46;
               }
               break;
            case 3006244:
               if (var3.equals("avc2")) {
                  var2 = 3;
                  break label46;
               }
               break;
            case 3199032:
               if (var3.equals("hev1")) {
                  break label46;
               }
               break;
            case 3214780:
               if (var3.equals("hvc1")) {
                  var2 = 1;
                  break label46;
               }
               break;
            case 3356560:
               if (var3.equals("mp4a")) {
                  var2 = 4;
                  break label46;
               }
            }

            var2 = -1;
         }

         if (var2 != 0 && var2 != 1) {
            if (var2 != 2 && var2 != 3) {
               return var2 != 4 ? null : getAacCodecProfileAndLevel(var0, var1);
            } else {
               return getAvcProfileAndLevel(var0, var1);
            }
         } else {
            return getHevcProfileAndLevel(var0, var1);
         }
      }
   }

   public static MediaCodecInfo getDecoderInfo(String var0, boolean var1) throws MediaCodecUtil.DecoderQueryException {
      List var2 = getDecoderInfos(var0, var1);
      MediaCodecInfo var3;
      if (var2.isEmpty()) {
         var3 = null;
      } else {
         var3 = (MediaCodecInfo)var2.get(0);
      }

      return var3;
   }

   public static List getDecoderInfos(String var0, boolean var1) throws MediaCodecUtil.DecoderQueryException {
      synchronized(MediaCodecUtil.class){}

      List var118;
      label1077: {
         Throwable var10000;
         label1083: {
            MediaCodecUtil.CodecKey var2;
            List var3;
            boolean var10001;
            try {
               var2 = new MediaCodecUtil.CodecKey(var0, var1);
               var3 = (List)decoderInfosCache.get(var2);
            } catch (Throwable var116) {
               var10000 = var116;
               var10001 = false;
               break label1083;
            }

            if (var3 != null) {
               return var3;
            }

            Object var119;
            label1070: {
               try {
                  if (Util.SDK_INT >= 21) {
                     var119 = new MediaCodecUtil.MediaCodecListCompatV21(var1);
                     break label1070;
                  }
               } catch (Throwable var115) {
                  var10000 = var115;
                  var10001 = false;
                  break label1083;
               }

               try {
                  var119 = new MediaCodecUtil.MediaCodecListCompatV16();
               } catch (Throwable var114) {
                  var10000 = var114;
                  var10001 = false;
                  break label1083;
               }
            }

            ArrayList var4;
            try {
               var4 = getDecoderInfosInternal(var2, (MediaCodecUtil.MediaCodecListCompat)var119, var0);
            } catch (Throwable var113) {
               var10000 = var113;
               var10001 = false;
               break label1083;
            }

            Object var5 = var119;
            ArrayList var6 = var4;
            if (var1) {
               label1082: {
                  var5 = var119;
                  var6 = var4;

                  try {
                     if (!var4.isEmpty()) {
                        break label1082;
                     }
                  } catch (Throwable var112) {
                     var10000 = var112;
                     var10001 = false;
                     break label1083;
                  }

                  var5 = var119;
                  var6 = var4;

                  try {
                     if (21 > Util.SDK_INT) {
                        break label1082;
                     }
                  } catch (Throwable var111) {
                     var10000 = var111;
                     var10001 = false;
                     break label1083;
                  }

                  var5 = var119;
                  var6 = var4;

                  MediaCodecUtil.MediaCodecListCompatV16 var120;
                  try {
                     if (Util.SDK_INT > 23) {
                        break label1082;
                     }

                     var120 = new MediaCodecUtil.MediaCodecListCompatV16();
                     var4 = getDecoderInfosInternal(var2, var120, var0);
                  } catch (Throwable var110) {
                     var10000 = var110;
                     var10001 = false;
                     break label1083;
                  }

                  var5 = var120;
                  var6 = var4;

                  try {
                     if (var4.isEmpty()) {
                        break label1082;
                     }

                     StringBuilder var122 = new StringBuilder();
                     var122.append("MediaCodecList API didn't list secure decoder for: ");
                     var122.append(var0);
                     var122.append(". Assuming: ");
                     var122.append(((MediaCodecInfo)var4.get(0)).name);
                     Log.w("MediaCodecUtil", var122.toString());
                  } catch (Throwable var109) {
                     var10000 = var109;
                     var10001 = false;
                     break label1083;
                  }

                  var6 = var4;
                  var5 = var120;
               }
            }

            try {
               if ("audio/eac3-joc".equals(var0)) {
                  MediaCodecUtil.CodecKey var121 = new MediaCodecUtil.CodecKey("audio/eac3", var2.secure);
                  var6.addAll(getDecoderInfosInternal(var121, (MediaCodecUtil.MediaCodecListCompat)var5, var0));
               }
            } catch (Throwable var108) {
               var10000 = var108;
               var10001 = false;
               break label1083;
            }

            label1030:
            try {
               applyWorkarounds(var0, var6);
               var118 = Collections.unmodifiableList(var6);
               decoderInfosCache.put(var2, var118);
               break label1077;
            } catch (Throwable var107) {
               var10000 = var107;
               var10001 = false;
               break label1030;
            }
         }

         Throwable var117 = var10000;
         throw var117;
      }

      return var118;
   }

   private static ArrayList getDecoderInfosInternal(MediaCodecUtil.CodecKey var0, MediaCodecUtil.MediaCodecListCompat var1, String var2) throws MediaCodecUtil.DecoderQueryException {
      Exception var10000;
      label137: {
         ArrayList var3;
         String var4;
         int var5;
         boolean var6;
         boolean var10001;
         try {
            var3 = new ArrayList();
            var4 = var0.mimeType;
            var5 = var1.getCodecCount();
            var6 = var1.secureDecodersExplicit();
         } catch (Exception var30) {
            var10000 = var30;
            var10001 = false;
            break label137;
         }

         int var7 = 0;

         label131:
         while(true) {
            if (var7 >= var5) {
               return var3;
            }

            android.media.MediaCodecInfo var8;
            String var9;
            try {
               var8 = var1.getCodecInfoAt(var7);
               var9 = var8.getName();
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            int var10 = var5;

            String var14;
            Exception var16;
            label128: {
               label139: {
                  String[] var11;
                  int var12;
                  try {
                     if (!isCodecUsableDecoder(var8, var9, var6, var2)) {
                        break label139;
                     }

                     var11 = var8.getSupportedTypes();
                     var12 = var11.length;
                  } catch (Exception var24) {
                     var10000 = var24;
                     var10001 = false;
                     break;
                  }

                  int var13 = 0;

                  while(true) {
                     var10 = var5;
                     if (var13 >= var12) {
                        break;
                     }

                     var14 = var11[var13];

                     boolean var15;
                     try {
                        var15 = var14.equalsIgnoreCase(var4);
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label131;
                     }

                     if (var15) {
                        label141: {
                           label142: {
                              boolean var17;
                              CodecCapabilities var33;
                              try {
                                 var33 = var8.getCapabilitiesForType(var14);
                                 var17 = var1.isSecurePlaybackSupported(var4, var33);
                                 var15 = codecNeedsDisableAdaptationWorkaround(var9);
                              } catch (Exception var29) {
                                 var16 = var29;
                                 break label142;
                              }

                              label143: {
                                 label144: {
                                    label145: {
                                       if (var6) {
                                          try {
                                             if (var0.secure == var17) {
                                                break label145;
                                             }
                                          } catch (Exception var28) {
                                             var10000 = var28;
                                             var10001 = false;
                                             break label143;
                                          }
                                       }

                                       if (var6) {
                                          break label144;
                                       }

                                       try {
                                          if (var0.secure) {
                                             break label144;
                                          }
                                       } catch (Exception var27) {
                                          var10000 = var27;
                                          var10001 = false;
                                          break label143;
                                       }
                                    }

                                    try {
                                       var3.add(MediaCodecInfo.newInstance(var9, var4, var33, var15, false));
                                       break label141;
                                    } catch (Exception var26) {
                                       var10000 = var26;
                                       var10001 = false;
                                       break label143;
                                    }
                                 }

                                 if (var6 || !var17) {
                                    break label141;
                                 }

                                 try {
                                    StringBuilder var18 = new StringBuilder();
                                    var18.append(var9);
                                    var18.append(".secure");
                                    var3.add(MediaCodecInfo.newInstance(var18.toString(), var4, var33, var15, true));
                                    return var3;
                                 } catch (Exception var25) {
                                    var10000 = var25;
                                    var10001 = false;
                                 }
                              }

                              var16 = var10000;
                           }

                           try {
                              var10 = Util.SDK_INT;
                           } catch (Exception var20) {
                              var10000 = var20;
                              var10001 = false;
                              break label131;
                           }

                           if (var10 > 23) {
                              break label128;
                           }

                           try {
                              if (var3.isEmpty()) {
                                 break label128;
                              }

                              StringBuilder var34 = new StringBuilder();
                              var34.append("Skipping codec ");
                              var34.append(var9);
                              var34.append(" (failed to query capabilities)");
                              Log.e("MediaCodecUtil", var34.toString());
                           } catch (Exception var23) {
                              var10000 = var23;
                              var10001 = false;
                              break label131;
                           }
                        }
                     }

                     ++var13;
                  }
               }

               ++var7;
               var5 = var10;
               continue;
            }

            try {
               StringBuilder var32 = new StringBuilder();
               var32.append("Failed to query codec ");
               var32.append(var9);
               var32.append(" (");
               var32.append(var14);
               var32.append(")");
               Log.e("MediaCodecUtil", var32.toString());
               throw var16;
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break;
            }
         }
      }

      Exception var31 = var10000;
      throw new MediaCodecUtil.DecoderQueryException(var31);
   }

   private static Pair getHevcProfileAndLevel(String var0, String[] var1) {
      StringBuilder var6;
      if (var1.length < 4) {
         var6 = new StringBuilder();
         var6.append("Ignoring malformed HEVC codec string: ");
         var6.append(var0);
         Log.w("MediaCodecUtil", var6.toString());
         return null;
      } else {
         Matcher var2 = PROFILE_PATTERN.matcher(var1[1]);
         if (!var2.matches()) {
            var6 = new StringBuilder();
            var6.append("Ignoring malformed HEVC codec string: ");
            var6.append(var0);
            Log.w("MediaCodecUtil", var6.toString());
            return null;
         } else {
            var0 = var2.group(1);
            byte var3;
            if ("1".equals(var0)) {
               var3 = 1;
            } else {
               if (!"2".equals(var0)) {
                  var6 = new StringBuilder();
                  var6.append("Unknown HEVC profile string: ");
                  var6.append(var0);
                  Log.w("MediaCodecUtil", var6.toString());
                  return null;
               }

               var3 = 2;
            }

            Integer var4 = (Integer)HEVC_CODEC_STRING_TO_PROFILE_LEVEL.get(var1[3]);
            if (var4 == null) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Unknown HEVC level string: ");
               var5.append(var2.group(1));
               Log.w("MediaCodecUtil", var5.toString());
               return null;
            } else {
               return new Pair(Integer.valueOf(var3), var4);
            }
         }
      }
   }

   public static MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
      MediaCodecInfo var0 = getDecoderInfo("audio/raw", false);
      if (var0 == null) {
         var0 = null;
      } else {
         var0 = MediaCodecInfo.newPassthroughInstance(var0.name);
      }

      return var0;
   }

   private static boolean isCodecUsableDecoder(android.media.MediaCodecInfo var0, String var1, boolean var2, String var3) {
      if (var0.isEncoder() || !var2 && var1.endsWith(".secure")) {
         return false;
      } else if (Util.SDK_INT < 21 && ("CIPAACDecoder".equals(var1) || "CIPMP3Decoder".equals(var1) || "CIPVorbisDecoder".equals(var1) || "CIPAMRNBDecoder".equals(var1) || "AACDecoder".equals(var1) || "MP3Decoder".equals(var1))) {
         return false;
      } else if (Util.SDK_INT < 18 && "OMX.SEC.MP3.Decoder".equals(var1)) {
         return false;
      } else if ("OMX.SEC.mp3.dec".equals(var1) && (Util.MODEL.startsWith("GT-I9152") || Util.MODEL.startsWith("GT-I9515") || Util.MODEL.startsWith("GT-P5220") || Util.MODEL.startsWith("GT-S7580") || Util.MODEL.startsWith("SM-G350") || Util.MODEL.startsWith("SM-G386") || Util.MODEL.startsWith("SM-T231") || Util.MODEL.startsWith("SM-T530") || Util.MODEL.startsWith("SCH-I535") || Util.MODEL.startsWith("SPH-L710"))) {
         return false;
      } else if (!"OMX.brcm.audio.mp3.decoder".equals(var1) || !Util.MODEL.startsWith("GT-I9152") && !Util.MODEL.startsWith("GT-S7580") && !Util.MODEL.startsWith("SM-G350")) {
         if (Util.SDK_INT < 18 && "OMX.MTK.AUDIO.DECODER.AAC".equals(var1) && ("a70".equals(Util.DEVICE) || "Xiaomi".equals(Util.MANUFACTURER) && Util.DEVICE.startsWith("HM"))) {
            return false;
         } else if (Util.SDK_INT != 16 || !"OMX.qcom.audio.decoder.mp3".equals(var1) || !"dlxu".equals(Util.DEVICE) && !"protou".equals(Util.DEVICE) && !"ville".equals(Util.DEVICE) && !"villeplus".equals(Util.DEVICE) && !"villec2".equals(Util.DEVICE) && !Util.DEVICE.startsWith("gee") && !"C6602".equals(Util.DEVICE) && !"C6603".equals(Util.DEVICE) && !"C6606".equals(Util.DEVICE) && !"C6616".equals(Util.DEVICE) && !"L36h".equals(Util.DEVICE) && !"SO-02E".equals(Util.DEVICE)) {
            if (Util.SDK_INT == 16 && "OMX.qcom.audio.decoder.aac".equals(var1) && ("C1504".equals(Util.DEVICE) || "C1505".equals(Util.DEVICE) || "C1604".equals(Util.DEVICE) || "C1605".equals(Util.DEVICE))) {
               return false;
            } else if (Util.SDK_INT < 24 && ("OMX.SEC.aac.dec".equals(var1) || "OMX.Exynos.AAC.Decoder".equals(var1)) && "samsung".equals(Util.MANUFACTURER) && (Util.DEVICE.startsWith("zeroflte") || Util.DEVICE.startsWith("zerolte") || Util.DEVICE.startsWith("zenlte") || "SC-05G".equals(Util.DEVICE) || "marinelteatt".equals(Util.DEVICE) || "404SC".equals(Util.DEVICE) || "SC-04G".equals(Util.DEVICE) || "SCV31".equals(Util.DEVICE))) {
               return false;
            } else if (Util.SDK_INT > 19 || !"OMX.SEC.vp8.dec".equals(var1) || !"samsung".equals(Util.MANUFACTURER) || !Util.DEVICE.startsWith("d2") && !Util.DEVICE.startsWith("serrano") && !Util.DEVICE.startsWith("jflte") && !Util.DEVICE.startsWith("santos") && !Util.DEVICE.startsWith("t0")) {
               if (Util.SDK_INT <= 19 && Util.DEVICE.startsWith("jflte") && "OMX.qcom.video.decoder.vp8".equals(var1)) {
                  return false;
               } else {
                  return !"audio/eac3-joc".equals(var3) || !"OMX.MTK.AUDIO.DECODER.DSPAC3".equals(var1);
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static int maxH264DecodableFrameSize() throws MediaCodecUtil.DecoderQueryException {
      if (maxH264DecodableFrameSize == -1) {
         int var0 = 0;
         int var1 = 0;
         MediaCodecInfo var2 = getDecoderInfo("video/avc", false);
         if (var2 != null) {
            CodecProfileLevel[] var4 = var2.getProfileLevels();
            int var3 = var4.length;

            for(var0 = 0; var1 < var3; ++var1) {
               var0 = Math.max(avcLevelToMaxFrameSize(var4[var1].level), var0);
            }

            if (Util.SDK_INT >= 21) {
               var1 = 345600;
            } else {
               var1 = 172800;
            }

            var0 = Math.max(var0, var1);
         }

         maxH264DecodableFrameSize = var0;
      }

      return maxH264DecodableFrameSize;
   }

   private static final class CodecKey {
      public final String mimeType;
      public final boolean secure;

      public CodecKey(String var1, boolean var2) {
         this.mimeType = var1;
         this.secure = var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && var1.getClass() == MediaCodecUtil.CodecKey.class) {
            MediaCodecUtil.CodecKey var3 = (MediaCodecUtil.CodecKey)var1;
            if (!TextUtils.equals(this.mimeType, var3.mimeType) || this.secure != var3.secure) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         String var1 = this.mimeType;
         int var2;
         if (var1 == null) {
            var2 = 0;
         } else {
            var2 = var1.hashCode();
         }

         short var3;
         if (this.secure) {
            var3 = 1231;
         } else {
            var3 = 1237;
         }

         return (var2 + 31) * 31 + var3;
      }
   }

   public static class DecoderQueryException extends Exception {
      private DecoderQueryException(Throwable var1) {
         super("Failed to query underlying media codecs", var1);
      }

      // $FF: synthetic method
      DecoderQueryException(Throwable var1, Object var2) {
         this(var1);
      }
   }

   private interface MediaCodecListCompat {
      int getCodecCount();

      android.media.MediaCodecInfo getCodecInfoAt(int var1);

      boolean isSecurePlaybackSupported(String var1, CodecCapabilities var2);

      boolean secureDecodersExplicit();
   }

   private static final class MediaCodecListCompatV16 implements MediaCodecUtil.MediaCodecListCompat {
      private MediaCodecListCompatV16() {
      }

      // $FF: synthetic method
      MediaCodecListCompatV16(Object var1) {
         this();
      }

      public int getCodecCount() {
         return MediaCodecList.getCodecCount();
      }

      public android.media.MediaCodecInfo getCodecInfoAt(int var1) {
         return MediaCodecList.getCodecInfoAt(var1);
      }

      public boolean isSecurePlaybackSupported(String var1, CodecCapabilities var2) {
         return "video/avc".equals(var1);
      }

      public boolean secureDecodersExplicit() {
         return false;
      }
   }

   @TargetApi(21)
   private static final class MediaCodecListCompatV21 implements MediaCodecUtil.MediaCodecListCompat {
      private final int codecKind;
      private android.media.MediaCodecInfo[] mediaCodecInfos;

      public MediaCodecListCompatV21(boolean var1) {
         this.codecKind = var1;
      }

      private void ensureMediaCodecInfosInitialized() {
         if (this.mediaCodecInfos == null) {
            this.mediaCodecInfos = (new MediaCodecList(this.codecKind)).getCodecInfos();
         }

      }

      public int getCodecCount() {
         this.ensureMediaCodecInfosInitialized();
         return this.mediaCodecInfos.length;
      }

      public android.media.MediaCodecInfo getCodecInfoAt(int var1) {
         this.ensureMediaCodecInfosInitialized();
         return this.mediaCodecInfos[var1];
      }

      public boolean isSecurePlaybackSupported(String var1, CodecCapabilities var2) {
         return var2.isFeatureSupported("secure-playback");
      }

      public boolean secureDecodersExplicit() {
         return true;
      }
   }

   private static final class RawAudioCodecComparator implements Comparator {
      private RawAudioCodecComparator() {
      }

      // $FF: synthetic method
      RawAudioCodecComparator(Object var1) {
         this();
      }

      private static int scoreMediaCodecInfo(MediaCodecInfo var0) {
         String var1 = var0.name;
         if (!var1.startsWith("OMX.google") && !var1.startsWith("c2.android")) {
            return Util.SDK_INT < 26 && var1.equals("OMX.MTK.AUDIO.DECODER.RAW") ? 1 : 0;
         } else {
            return -1;
         }
      }

      public int compare(MediaCodecInfo var1, MediaCodecInfo var2) {
         return scoreMediaCodecInfo(var1) - scoreMediaCodecInfo(var2);
      }
   }
}
