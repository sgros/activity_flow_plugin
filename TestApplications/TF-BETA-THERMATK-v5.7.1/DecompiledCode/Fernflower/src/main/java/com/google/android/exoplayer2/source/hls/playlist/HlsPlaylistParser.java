package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import android.util.Base64;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.source.UnrecognizedInputFormatException;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HlsPlaylistParser implements ParsingLoadable.Parser {
   private static final Pattern REGEX_ATTR_BYTERANGE = Pattern.compile("BYTERANGE=\"(\\d+(?:@\\d+)?)\\b\"");
   private static final Pattern REGEX_AUDIO = Pattern.compile("AUDIO=\"(.+?)\"");
   private static final Pattern REGEX_AUTOSELECT = compileBooleanAttrPattern("AUTOSELECT");
   private static final Pattern REGEX_AVERAGE_BANDWIDTH = Pattern.compile("AVERAGE-BANDWIDTH=(\\d+)\\b");
   private static final Pattern REGEX_BANDWIDTH = Pattern.compile("[^-]BANDWIDTH=(\\d+)\\b");
   private static final Pattern REGEX_BYTERANGE = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
   private static final Pattern REGEX_CODECS = Pattern.compile("CODECS=\"(.+?)\"");
   private static final Pattern REGEX_DEFAULT = compileBooleanAttrPattern("DEFAULT");
   private static final Pattern REGEX_FORCED = compileBooleanAttrPattern("FORCED");
   private static final Pattern REGEX_FRAME_RATE = Pattern.compile("FRAME-RATE=([\\d\\.]+)\\b");
   private static final Pattern REGEX_GROUP_ID = Pattern.compile("GROUP-ID=\"(.+?)\"");
   private static final Pattern REGEX_IMPORT = Pattern.compile("IMPORT=\"(.+?)\"");
   private static final Pattern REGEX_INSTREAM_ID = Pattern.compile("INSTREAM-ID=\"((?:CC|SERVICE)\\d+)\"");
   private static final Pattern REGEX_IV = Pattern.compile("IV=([^,.*]+)");
   private static final Pattern REGEX_KEYFORMAT = Pattern.compile("KEYFORMAT=\"(.+?)\"");
   private static final Pattern REGEX_KEYFORMATVERSIONS = Pattern.compile("KEYFORMATVERSIONS=\"(.+?)\"");
   private static final Pattern REGEX_LANGUAGE = Pattern.compile("LANGUAGE=\"(.+?)\"");
   private static final Pattern REGEX_MEDIA_DURATION = Pattern.compile("#EXTINF:([\\d\\.]+)\\b");
   private static final Pattern REGEX_MEDIA_SEQUENCE = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
   private static final Pattern REGEX_MEDIA_TITLE = Pattern.compile("#EXTINF:[\\d\\.]+\\b,(.+)");
   private static final Pattern REGEX_METHOD = Pattern.compile("METHOD=(NONE|AES-128|SAMPLE-AES|SAMPLE-AES-CENC|SAMPLE-AES-CTR)\\s*(?:,|$)");
   private static final Pattern REGEX_NAME = Pattern.compile("NAME=\"(.+?)\"");
   private static final Pattern REGEX_PLAYLIST_TYPE = Pattern.compile("#EXT-X-PLAYLIST-TYPE:(.+)\\b");
   private static final Pattern REGEX_RESOLUTION = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
   private static final Pattern REGEX_TARGET_DURATION = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");
   private static final Pattern REGEX_TIME_OFFSET = Pattern.compile("TIME-OFFSET=(-?[\\d\\.]+)\\b");
   private static final Pattern REGEX_TYPE = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
   private static final Pattern REGEX_URI = Pattern.compile("URI=\"(.+?)\"");
   private static final Pattern REGEX_VALUE = Pattern.compile("VALUE=\"(.+?)\"");
   private static final Pattern REGEX_VARIABLE_REFERENCE = Pattern.compile("\\{\\$([a-zA-Z0-9\\-_]+)\\}");
   private static final Pattern REGEX_VERSION = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
   private final HlsMasterPlaylist masterPlaylist;

   public HlsPlaylistParser() {
      this(HlsMasterPlaylist.EMPTY);
   }

   public HlsPlaylistParser(HlsMasterPlaylist var1) {
      this.masterPlaylist = var1;
   }

   private static boolean checkPlaylistHeader(BufferedReader var0) throws IOException {
      int var1 = var0.read();
      int var2 = var1;
      if (var1 == 239) {
         if (var0.read() != 187 || var0.read() != 191) {
            return false;
         }

         var2 = var0.read();
      }

      var2 = skipIgnorableWhitespace(var0, true, var2);

      for(var1 = 0; var1 < 7; ++var1) {
         if (var2 != "#EXTM3U".charAt(var1)) {
            return false;
         }

         var2 = var0.read();
      }

      return Util.isLinebreak(skipIgnorableWhitespace(var0, false, var2));
   }

   private static Pattern compileBooleanAttrPattern(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(var0);
      var1.append("=(");
      var1.append("NO");
      var1.append("|");
      var1.append("YES");
      var1.append(")");
      return Pattern.compile(var1.toString());
   }

   private static boolean isMediaTagMuxed(List var0, String var1) {
      if (var1 == null) {
         return true;
      } else {
         for(int var2 = 0; var2 < var0.size(); ++var2) {
            if (var1.equals(((HlsMasterPlaylist.HlsUrl)var0.get(var2)).url)) {
               return true;
            }
         }

         return false;
      }
   }

   private static double parseDoubleAttr(String var0, Pattern var1) throws ParserException {
      return Double.parseDouble(parseStringAttr(var0, var1, Collections.emptyMap()));
   }

   private static int parseIntAttr(String var0, Pattern var1) throws ParserException {
      return Integer.parseInt(parseStringAttr(var0, var1, Collections.emptyMap()));
   }

   private static long parseLongAttr(String var0, Pattern var1) throws ParserException {
      return Long.parseLong(parseStringAttr(var0, var1, Collections.emptyMap()));
   }

   private static HlsMasterPlaylist parseMasterPlaylist(HlsPlaylistParser.LineIterator var0, String var1) throws IOException {
      HashSet var2 = new HashSet();
      HashMap var3 = new HashMap();
      HashMap var4 = new HashMap();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      ArrayList var8 = new ArrayList();
      ArrayList var9 = new ArrayList();
      boolean var10 = false;
      boolean var11 = false;

      String var12;
      int var14;
      String var15;
      String var16;
      int var17;
      int var18;
      while(var0.hasNext()) {
         var12 = var0.next();
         if (var12.startsWith("#EXT")) {
            var9.add(var12);
         }

         if (var12.startsWith("#EXT-X-DEFINE")) {
            var4.put(parseStringAttr(var12, REGEX_NAME, var4), parseStringAttr(var12, REGEX_VALUE, var4));
         } else if (var12.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
            var11 = true;
         } else if (var12.startsWith("#EXT-X-MEDIA")) {
            var8.add(var12);
         } else if (var12.startsWith("#EXT-X-STREAM-INF")) {
            boolean var13 = var10 | var12.contains("CLOSED-CAPTIONS=NONE");
            var14 = parseIntAttr(var12, REGEX_BANDWIDTH);
            var15 = parseOptionalStringAttr(var12, REGEX_AVERAGE_BANDWIDTH, var4);
            if (var15 != null) {
               var14 = Integer.parseInt(var15);
            }

            var15 = parseOptionalStringAttr(var12, REGEX_CODECS, var4);
            var16 = parseOptionalStringAttr(var12, REGEX_RESOLUTION, var4);
            if (var16 != null) {
               String[] var27 = var16.split("x");
               int var26 = Integer.parseInt(var27[0]);
               var17 = Integer.parseInt(var27[1]);
               if (var26 <= 0 || var17 <= 0) {
                  var26 = -1;
                  var17 = -1;
               }

               var18 = var17;
               var17 = var26;
            } else {
               var17 = -1;
               var18 = -1;
            }

            var16 = parseOptionalStringAttr(var12, REGEX_FRAME_RATE, var4);
            float var19;
            if (var16 != null) {
               var19 = Float.parseFloat(var16);
            } else {
               var19 = -1.0F;
            }

            var12 = parseOptionalStringAttr(var12, REGEX_AUDIO, var4);
            if (var12 != null && var15 != null) {
               var3.put(var12, Util.getCodecsOfType(var15, 1));
            }

            var12 = replaceVariableReferences(var0.next(), var4);
            var10 = var13;
            if (var2.add(var12)) {
               var5.add(new HlsMasterPlaylist.HlsUrl(var12, Format.createVideoContainerFormat(Integer.toString(var5.size()), (String)null, "application/x-mpegURL", (String)null, var15, var14, var17, var18, var19, (List)null, 0)));
               var10 = var13;
            }
         }
      }

      var14 = 0;
      Format var25 = null;

      Object var24;
      for(var24 = null; var14 < var8.size(); ++var14) {
         String var20;
         String var21;
         byte var30;
         String var31;
         label96: {
            var15 = (String)var8.get(var14);
            var18 = parseSelectionFlags(var15);
            var12 = parseOptionalStringAttr(var15, REGEX_URI, var4);
            var16 = parseStringAttr(var15, REGEX_NAME, var4);
            var20 = parseOptionalStringAttr(var15, REGEX_LANGUAGE, var4);
            var21 = parseOptionalStringAttr(var15, REGEX_GROUP_ID, var4);
            StringBuilder var22 = new StringBuilder();
            var22.append(var21);
            var22.append(":");
            var22.append(var16);
            var31 = var22.toString();
            String var23 = parseStringAttr(var15, REGEX_TYPE, var4);
            var17 = var23.hashCode();
            if (var17 != -959297733) {
               if (var17 != -333210994) {
                  if (var17 == 62628790 && var23.equals("AUDIO")) {
                     var30 = 0;
                     break label96;
                  }
               } else if (var23.equals("CLOSED-CAPTIONS")) {
                  var30 = 2;
                  break label96;
               }
            } else if (var23.equals("SUBTITLES")) {
               var30 = 1;
               break label96;
            }

            var30 = -1;
         }

         if (var30 != 0) {
            if (var30 != 1) {
               if (var30 == 2) {
                  var15 = parseStringAttr(var15, REGEX_INSTREAM_ID, var4);
                  if (var15.startsWith("CC")) {
                     var17 = Integer.parseInt(var15.substring(2));
                     var12 = "application/cea-608";
                  } else {
                     var17 = Integer.parseInt(var15.substring(7));
                     var12 = "application/cea-708";
                  }

                  Object var28 = var24;
                  if (var24 == null) {
                     var28 = new ArrayList();
                  }

                  ((List)var28).add(Format.createTextContainerFormat(var31, var16, (String)null, var12, (String)null, -1, var18, var20, var17));
                  var24 = var28;
               }
            } else {
               var7.add(new HlsMasterPlaylist.HlsUrl(var12, Format.createTextContainerFormat(var31, var16, "application/x-mpegURL", "text/vtt", (String)null, -1, var18, var20)));
            }
         } else {
            var21 = (String)var3.get(var21);
            if (var21 != null) {
               var15 = MimeTypes.getMediaMimeType(var21);
            } else {
               var15 = null;
            }

            Format var29 = Format.createAudioContainerFormat(var31, var16, "application/x-mpegURL", var15, var21, -1, -1, -1, (List)null, var18, var20);
            if (isMediaTagMuxed(var5, var12)) {
               var25 = var29;
            } else {
               var6.add(new HlsMasterPlaylist.HlsUrl(var12, var29));
            }
         }
      }

      if (var10) {
         var24 = Collections.emptyList();
      }

      return new HlsMasterPlaylist(var1, var9, var5, var6, var7, var25, (List)var24, var11, var4);
   }

   private static HlsMediaPlaylist parseMediaPlaylist(HlsMasterPlaylist var0, HlsPlaylistParser.LineIterator var1, String var2) throws IOException {
      boolean var3 = var0.hasIndependentSegments;
      HashMap var4 = new HashMap();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      TreeMap var7 = new TreeMap();
      long var8 = -9223372036854775807L;
      long var10 = var8;
      String var12 = "";
      boolean var13 = false;
      byte var14 = 0;
      String var15 = null;
      long var16 = 0L;
      int var18 = 0;
      long var19 = 0L;
      int var21 = 1;
      boolean var22 = false;
      DrmInitData var23 = null;
      long var24 = 0L;
      long var26 = 0L;
      DrmInitData var28 = null;
      boolean var29 = false;
      long var30 = -1L;
      int var32 = 0;
      long var33 = 0L;
      String var35 = null;
      String var36 = null;
      HlsMediaPlaylist.Segment var37 = null;
      long var38 = 0L;

      while(true) {
         while(var1.hasNext()) {
            String var40 = var1.next();
            if (var40.startsWith("#EXT")) {
               var6.add(var40);
            }

            String var41;
            if (var40.startsWith("#EXT-X-PLAYLIST-TYPE")) {
               var41 = parseStringAttr(var40, REGEX_PLAYLIST_TYPE, var4);
               if ("VOD".equals(var41)) {
                  var14 = 1;
               } else if ("EVENT".equals(var41)) {
                  var14 = 2;
               }
            } else if (var40.startsWith("#EXT-X-START")) {
               var8 = (long)(parseDoubleAttr(var40, REGEX_TIME_OFFSET) * 1000000.0D);
            } else {
               long var42;
               if (var40.startsWith("#EXT-X-MAP")) {
                  var41 = parseStringAttr(var40, REGEX_URI, var4);
                  String var51 = parseOptionalStringAttr(var40, REGEX_ATTR_BYTERANGE, var4);
                  var42 = var24;
                  if (var51 != null) {
                     String[] var52 = var51.split("@");
                     long var44 = Long.parseLong(var52[0]);
                     var42 = var24;
                     var30 = var44;
                     if (var52.length > 1) {
                        var42 = Long.parseLong(var52[1]);
                        var30 = var44;
                     }
                  }

                  var37 = new HlsMediaPlaylist.Segment(var41, var42, var30);
                  var24 = 0L;
                  var30 = -1L;
               } else if (var40.startsWith("#EXT-X-TARGETDURATION")) {
                  var10 = 1000000L * (long)parseIntAttr(var40, REGEX_TARGET_DURATION);
               } else if (var40.startsWith("#EXT-X-MEDIA-SEQUENCE")) {
                  var26 = parseLongAttr(var40, REGEX_MEDIA_SEQUENCE);
                  var19 = var26;
               } else if (var40.startsWith("#EXT-X-VERSION")) {
                  var21 = parseIntAttr(var40, REGEX_VERSION);
               } else if (var40.startsWith("#EXT-X-DEFINE")) {
                  var41 = parseOptionalStringAttr(var40, REGEX_IMPORT, var4);
                  if (var41 != null) {
                     var40 = (String)var0.variableDefinitions.get(var41);
                     if (var40 != null) {
                        var4.put(var41, var40);
                     }
                  } else {
                     var4.put(parseStringAttr(var40, REGEX_NAME, var4), parseStringAttr(var40, REGEX_VALUE, var4));
                  }
               } else if (var40.startsWith("#EXTINF")) {
                  var38 = (long)(parseDoubleAttr(var40, REGEX_MEDIA_DURATION) * 1000000.0D);
                  var12 = parseOptionalStringAttr(var40, REGEX_MEDIA_TITLE, "", var4);
               } else if (var40.startsWith("#EXT-X-KEY")) {
                  String var46 = parseStringAttr(var40, REGEX_METHOD, var4);
                  String var54 = parseOptionalStringAttr(var40, REGEX_KEYFORMAT, "identity", var4);
                  if ("NONE".equals(var46)) {
                     var7.clear();
                     var28 = null;
                     var35 = null;
                     var36 = null;
                  } else {
                     label140: {
                        var41 = parseOptionalStringAttr(var40, REGEX_IV, var4);
                        if ("identity".equals(var54)) {
                           var35 = var15;
                           if ("AES-128".equals(var46)) {
                              var35 = parseStringAttr(var40, REGEX_URI, var4);
                              var36 = var41;
                              continue;
                           }
                        } else {
                           var36 = var15;
                           if (var15 == null) {
                              if (!"SAMPLE-AES-CENC".equals(var46) && !"SAMPLE-AES-CTR".equals(var46)) {
                                 var36 = "cbcs";
                              } else {
                                 var36 = "cenc";
                              }
                           }

                           DrmInitData.SchemeData var49;
                           if ("com.microsoft.playready".equals(var54)) {
                              var49 = parsePlayReadySchemeData(var40, var4);
                           } else {
                              var49 = parseWidevineSchemeData(var40, var54, var4);
                           }

                           var35 = var36;
                           if (var49 != null) {
                              var7.put(var54, var49);
                              var28 = null;
                              break label140;
                           }
                        }

                        var36 = var35;
                     }

                     var35 = null;
                     var15 = var36;
                     var36 = var41;
                  }
               } else if (var40.startsWith("#EXT-X-BYTERANGE")) {
                  String[] var53 = parseStringAttr(var40, REGEX_BYTERANGE, var4).split("@");
                  var42 = Long.parseLong(var53[0]);
                  var30 = var42;
                  if (var53.length > 1) {
                     var24 = Long.parseLong(var53[1]);
                     var30 = var42;
                  }
               } else if (var40.startsWith("#EXT-X-DISCONTINUITY-SEQUENCE")) {
                  var18 = Integer.parseInt(var40.substring(var40.indexOf(58) + 1));
                  var13 = true;
               } else if (var40.equals("#EXT-X-DISCONTINUITY")) {
                  ++var32;
               } else if (var40.startsWith("#EXT-X-PROGRAM-DATE-TIME")) {
                  if (var16 == 0L) {
                     var16 = C.msToUs(Util.parseXsDateTime(var40.substring(var40.indexOf(58) + 1))) - var33;
                  }
               } else if (var40.equals("#EXT-X-GAP")) {
                  var29 = true;
               } else if (var40.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                  var3 = true;
               } else if (var40.equals("#EXT-X-ENDLIST")) {
                  var22 = true;
               } else if (!var40.startsWith("#")) {
                  if (var35 == null) {
                     var41 = null;
                  } else if (var36 != null) {
                     var41 = var36;
                  } else {
                     var41 = Long.toHexString(var26);
                  }

                  var42 = var24;
                  if (var30 == -1L) {
                     var42 = 0L;
                  }

                  if (var28 == null && !var7.isEmpty()) {
                     DrmInitData.SchemeData[] var47 = (DrmInitData.SchemeData[])var7.values().toArray(new DrmInitData.SchemeData[0]);
                     var28 = new DrmInitData(var15, var47);
                     if (var23 == null) {
                        DrmInitData.SchemeData[] var50 = new DrmInitData.SchemeData[var47.length];

                        for(int var48 = 0; var48 < var47.length; ++var48) {
                           var50[var48] = var47[var48].copyWithData((byte[])null);
                        }

                        var23 = new DrmInitData(var15, var50);
                     }
                  }

                  var5.add(new HlsMediaPlaylist.Segment(replaceVariableReferences(var40, var4), var37, var12, var38, var32, var33, var28, var35, var41, var42, var30, var29));
                  var33 += var38;
                  var24 = var42;
                  if (var30 != -1L) {
                     var24 = var42 + var30;
                  }

                  var29 = false;
                  var38 = 0L;
                  var12 = "";
                  ++var26;
                  var30 = -1L;
               }
            }
         }

         if (var16 != 0L) {
            var29 = true;
         } else {
            var29 = false;
         }

         return new HlsMediaPlaylist(var14, var2, var6, var8, var16, var13, var18, var19, var21, var10, var3, var22, var29, var23, var5);
      }
   }

   private static boolean parseOptionalBooleanAttribute(String var0, Pattern var1, boolean var2) {
      Matcher var3 = var1.matcher(var0);
      return var3.find() ? var3.group(1).equals("YES") : var2;
   }

   private static String parseOptionalStringAttr(String var0, Pattern var1, String var2, Map var3) {
      Matcher var4 = var1.matcher(var0);
      if (var4.find()) {
         var2 = var4.group(1);
      }

      var0 = var2;
      if (!var3.isEmpty()) {
         if (var2 == null) {
            var0 = var2;
         } else {
            var0 = replaceVariableReferences(var2, var3);
         }
      }

      return var0;
   }

   private static String parseOptionalStringAttr(String var0, Pattern var1, Map var2) {
      return parseOptionalStringAttr(var0, var1, (String)null, var2);
   }

   private static DrmInitData.SchemeData parsePlayReadySchemeData(String var0, Map var1) throws ParserException {
      if (!"1".equals(parseOptionalStringAttr(var0, REGEX_KEYFORMATVERSIONS, "1", var1))) {
         return null;
      } else {
         var0 = parseStringAttr(var0, REGEX_URI, var1);
         byte[] var2 = Base64.decode(var0.substring(var0.indexOf(44)), 0);
         var2 = PsshAtomUtil.buildPsshAtom(C.PLAYREADY_UUID, var2);
         return new DrmInitData.SchemeData(C.PLAYREADY_UUID, "video/mp4", var2);
      }
   }

   private static int parseSelectionFlags(String var0) {
      byte var1;
      if (parseOptionalBooleanAttribute(var0, REGEX_DEFAULT, false)) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      int var2 = var1;
      if (parseOptionalBooleanAttribute(var0, REGEX_FORCED, false)) {
         var2 = var1 | 2;
      }

      int var3 = var2;
      if (parseOptionalBooleanAttribute(var0, REGEX_AUTOSELECT, false)) {
         var3 = var2 | 4;
      }

      return var3;
   }

   private static String parseStringAttr(String var0, Pattern var1, Map var2) throws ParserException {
      String var3 = parseOptionalStringAttr(var0, var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Couldn't match ");
         var4.append(var1.pattern());
         var4.append(" in ");
         var4.append(var0);
         throw new ParserException(var4.toString());
      }
   }

   private static DrmInitData.SchemeData parseWidevineSchemeData(String var0, String var1, Map var2) throws ParserException {
      if ("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed".equals(var1)) {
         var0 = parseStringAttr(var0, REGEX_URI, var2);
         return new DrmInitData.SchemeData(C.WIDEVINE_UUID, "video/mp4", Base64.decode(var0.substring(var0.indexOf(44)), 0));
      } else if ("com.widevine".equals(var1)) {
         try {
            DrmInitData.SchemeData var4 = new DrmInitData.SchemeData(C.WIDEVINE_UUID, "hls", var0.getBytes("UTF-8"));
            return var4;
         } catch (UnsupportedEncodingException var3) {
            throw new ParserException(var3);
         }
      } else {
         return null;
      }
   }

   private static String replaceVariableReferences(String var0, Map var1) {
      Matcher var2 = REGEX_VARIABLE_REFERENCE.matcher(var0);
      StringBuffer var3 = new StringBuffer();

      while(var2.find()) {
         var0 = var2.group(1);
         if (var1.containsKey(var0)) {
            var2.appendReplacement(var3, Matcher.quoteReplacement((String)var1.get(var0)));
         }
      }

      var2.appendTail(var3);
      return var3.toString();
   }

   private static int skipIgnorableWhitespace(BufferedReader var0, boolean var1, int var2) throws IOException {
      while(var2 != -1 && Character.isWhitespace(var2) && (var1 || !Util.isLinebreak(var2))) {
         var2 = var0.read();
      }

      return var2;
   }

   public HlsPlaylist parse(Uri var1, InputStream var2) throws IOException {
      BufferedReader var81 = new BufferedReader(new InputStreamReader(var2));
      ArrayDeque var3 = new ArrayDeque();

      Throwable var10000;
      label886: {
         boolean var10001;
         label885: {
            try {
               if (checkPlaylistHeader(var81)) {
                  break label885;
               }
            } catch (Throwable var77) {
               var10000 = var77;
               var10001 = false;
               break label886;
            }

            try {
               UnrecognizedInputFormatException var82 = new UnrecognizedInputFormatException("Input does not start with the #EXTM3U header.", var1);
               throw var82;
            } catch (Throwable var71) {
               var10000 = var71;
               var10001 = false;
               break label886;
            }
         }

         while(true) {
            String var4;
            try {
               var4 = var81.readLine();
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break;
            }

            if (var4 == null) {
               Util.closeQuietly((Closeable)var81);
               throw new ParserException("Failed to parse the playlist, could not identify any tags.");
            }

            try {
               var4 = var4.trim();
               if (var4.isEmpty()) {
                  continue;
               }
            } catch (Throwable var76) {
               var10000 = var76;
               var10001 = false;
               break;
            }

            HlsPlaylistParser.LineIterator var83;
            label872: {
               HlsMasterPlaylist var78;
               try {
                  if (!var4.startsWith("#EXT-X-STREAM-INF")) {
                     break label872;
                  }

                  var3.add(var4);
                  var83 = new HlsPlaylistParser.LineIterator(var3, var81);
                  var78 = parseMasterPlaylist(var83, var1.toString());
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break;
               }

               Util.closeQuietly((Closeable)var81);
               return var78;
            }

            label901: {
               label863:
               try {
                  if (!var4.startsWith("#EXT-X-TARGETDURATION") && !var4.startsWith("#EXT-X-MEDIA-SEQUENCE") && !var4.startsWith("#EXTINF") && !var4.startsWith("#EXT-X-KEY") && !var4.startsWith("#EXT-X-BYTERANGE") && !var4.equals("#EXT-X-DISCONTINUITY") && !var4.equals("#EXT-X-DISCONTINUITY-SEQUENCE") && !var4.equals("#EXT-X-ENDLIST")) {
                     break label863;
                  }
                  break label901;
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break;
               }

               try {
                  var3.add(var4);
                  continue;
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break;
               }
            }

            HlsMediaPlaylist var79;
            try {
               var3.add(var4);
               HlsMasterPlaylist var5 = this.masterPlaylist;
               var83 = new HlsPlaylistParser.LineIterator(var3, var81);
               var79 = parseMediaPlaylist(var5, var83, var1.toString());
            } catch (Throwable var70) {
               var10000 = var70;
               var10001 = false;
               break;
            }

            Util.closeQuietly((Closeable)var81);
            return var79;
         }
      }

      Throwable var80 = var10000;
      Util.closeQuietly((Closeable)var81);
      throw var80;
   }

   private static class LineIterator {
      private final Queue extraLines;
      private String next;
      private final BufferedReader reader;

      public LineIterator(Queue var1, BufferedReader var2) {
         this.extraLines = var1;
         this.reader = var2;
      }

      public boolean hasNext() throws IOException {
         if (this.next != null) {
            return true;
         } else if (!this.extraLines.isEmpty()) {
            this.next = (String)this.extraLines.poll();
            return true;
         } else {
            do {
               String var1 = this.reader.readLine();
               this.next = var1;
               if (var1 == null) {
                  return false;
               }

               this.next = this.next.trim();
            } while(this.next.isEmpty());

            return true;
         }
      }

      public String next() throws IOException {
         String var1;
         if (this.hasNext()) {
            var1 = this.next;
            this.next = null;
         } else {
            var1 = null;
         }

         return var1;
      }
   }
}
