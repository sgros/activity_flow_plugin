package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import android.util.Xml;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class DashManifestParser extends DefaultHandler implements ParsingLoadable.Parser {
   private static final Pattern CEA_608_ACCESSIBILITY_PATTERN = Pattern.compile("CC([1-4])=.*");
   private static final Pattern CEA_708_ACCESSIBILITY_PATTERN = Pattern.compile("([1-9]|[1-5][0-9]|6[0-3])=.*");
   private static final Pattern FRAME_RATE_PATTERN = Pattern.compile("(\\d+)(?:/(\\d+))?");
   private final XmlPullParserFactory xmlParserFactory;

   public DashManifestParser() {
      try {
         this.xmlParserFactory = XmlPullParserFactory.newInstance();
      } catch (XmlPullParserException var2) {
         throw new RuntimeException("Couldn't create XmlPullParserFactory instance", var2);
      }
   }

   private static int checkContentTypeConsistency(int var0, int var1) {
      if (var0 == -1) {
         return var1;
      } else if (var1 == -1) {
         return var0;
      } else {
         boolean var2;
         if (var0 == var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         Assertions.checkState(var2);
         return var0;
      }
   }

   private static String checkLanguageConsistency(String var0, String var1) {
      if (var0 == null) {
         return var1;
      } else if (var1 == null) {
         return var0;
      } else {
         Assertions.checkState(var0.equals(var1));
         return var0;
      }
   }

   private static void filterRedundantIncompleteSchemeDatas(ArrayList var0) {
      for(int var1 = var0.size() - 1; var1 >= 0; --var1) {
         DrmInitData.SchemeData var2 = (DrmInitData.SchemeData)var0.get(var1);
         if (!var2.hasData()) {
            for(int var3 = 0; var3 < var0.size(); ++var3) {
               if (((DrmInitData.SchemeData)var0.get(var3)).canReplace(var2)) {
                  var0.remove(var1);
                  break;
               }
            }
         }
      }

   }

   private static String getSampleMimeType(String var0, String var1) {
      if (MimeTypes.isAudio(var0)) {
         return MimeTypes.getAudioMediaMimeType(var1);
      } else if (MimeTypes.isVideo(var0)) {
         return MimeTypes.getVideoMediaMimeType(var1);
      } else if (mimeTypeIsRawText(var0)) {
         return var0;
      } else {
         if ("application/mp4".equals(var0)) {
            if (var1 != null) {
               if (var1.startsWith("stpp")) {
                  return "application/ttml+xml";
               }

               if (var1.startsWith("wvtt")) {
                  return "application/x-mp4-vtt";
               }
            }
         } else if ("application/x-rawcc".equals(var0) && var1 != null) {
            if (var1.contains("cea708")) {
               return "application/cea-708";
            }

            if (var1.contains("eia608") || var1.contains("cea608")) {
               return "application/cea-608";
            }
         }

         return null;
      }
   }

   public static void maybeSkipTag(XmlPullParser var0) throws IOException, XmlPullParserException {
      if (XmlPullParserUtil.isStartTag(var0)) {
         int var1 = 1;

         while(var1 != 0) {
            var0.next();
            if (XmlPullParserUtil.isStartTag(var0)) {
               ++var1;
            } else if (XmlPullParserUtil.isEndTag(var0)) {
               --var1;
            }
         }

      }
   }

   private static boolean mimeTypeIsRawText(String var0) {
      boolean var1;
      if (!MimeTypes.isText(var0) && !"application/ttml+xml".equals(var0) && !"application/x-mp4-vtt".equals(var0) && !"application/cea-708".equals(var0) && !"application/cea-608".equals(var0)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   protected static String parseBaseUrl(XmlPullParser var0, String var1) throws XmlPullParserException, IOException {
      var0.next();
      return UriUtil.resolve(var1, var0.getText());
   }

   protected static int parseCea608AccessibilityChannel(List var0) {
      for(int var1 = 0; var1 < var0.size(); ++var1) {
         Descriptor var2 = (Descriptor)var0.get(var1);
         if ("urn:scte:dash:cc:cea-608:2015".equals(var2.schemeIdUri)) {
            String var3 = var2.value;
            if (var3 != null) {
               Matcher var4 = CEA_608_ACCESSIBILITY_PATTERN.matcher(var3);
               if (var4.matches()) {
                  return Integer.parseInt(var4.group(1));
               }

               StringBuilder var5 = new StringBuilder();
               var5.append("Unable to parse CEA-608 channel number from: ");
               var5.append(var2.value);
               Log.w("MpdParser", var5.toString());
            }
         }
      }

      return -1;
   }

   protected static int parseCea708AccessibilityChannel(List var0) {
      for(int var1 = 0; var1 < var0.size(); ++var1) {
         Descriptor var2 = (Descriptor)var0.get(var1);
         if ("urn:scte:dash:cc:cea-708:2015".equals(var2.schemeIdUri)) {
            String var3 = var2.value;
            if (var3 != null) {
               Matcher var4 = CEA_708_ACCESSIBILITY_PATTERN.matcher(var3);
               if (var4.matches()) {
                  return Integer.parseInt(var4.group(1));
               }

               StringBuilder var5 = new StringBuilder();
               var5.append("Unable to parse CEA-708 service block number from: ");
               var5.append(var2.value);
               Log.w("MpdParser", var5.toString());
            }
         }
      }

      return -1;
   }

   protected static long parseDateTime(XmlPullParser var0, String var1, long var2) throws ParserException {
      String var4 = var0.getAttributeValue((String)null, var1);
      return var4 == null ? var2 : Util.parseXsDateTime(var4);
   }

   protected static Descriptor parseDescriptor(XmlPullParser var0, String var1) throws XmlPullParserException, IOException {
      String var2 = parseString(var0, "schemeIdUri", "");
      String var3 = parseString(var0, "value", (String)null);
      String var4 = parseString(var0, "id", (String)null);

      do {
         var0.next();
      } while(!XmlPullParserUtil.isEndTag(var0, var1));

      return new Descriptor(var2, var3, var4);
   }

   protected static int parseDolbyChannelConfiguration(XmlPullParser var0) {
      String var2 = Util.toLowerInvariant(var0.getAttributeValue((String)null, "value"));
      if (var2 == null) {
         return -1;
      } else {
         byte var1;
         label43: {
            switch(var2.hashCode()) {
            case 1596796:
               if (var2.equals("4000")) {
                  var1 = 0;
                  break label43;
               }
               break;
            case 2937391:
               if (var2.equals("a000")) {
                  var1 = 1;
                  break label43;
               }
               break;
            case 3094035:
               if (var2.equals("f801")) {
                  var1 = 2;
                  break label43;
               }
               break;
            case 3133436:
               if (var2.equals("fa01")) {
                  var1 = 3;
                  break label43;
               }
            }

            var1 = -1;
         }

         if (var1 != 0) {
            if (var1 != 1) {
               if (var1 != 2) {
                  return var1 != 3 ? -1 : 8;
               } else {
                  return 6;
               }
            } else {
               return 2;
            }
         } else {
            return 1;
         }
      }
   }

   protected static long parseDuration(XmlPullParser var0, String var1, long var2) {
      String var4 = var0.getAttributeValue((String)null, var1);
      return var4 == null ? var2 : Util.parseXsDuration(var4);
   }

   protected static String parseEac3SupplementalProperties(List var0) {
      for(int var1 = 0; var1 < var0.size(); ++var1) {
         Descriptor var2 = (Descriptor)var0.get(var1);
         if ("tag:dolby.com,2014:dash:DolbyDigitalPlusExtensionType:2014".equals(var2.schemeIdUri) && "ec+3".equals(var2.value)) {
            return "audio/eac3-joc";
         }
      }

      return "audio/eac3";
   }

   protected static float parseFrameRate(XmlPullParser var0, float var1) {
      String var4 = var0.getAttributeValue((String)null, "frameRate");
      float var2 = var1;
      if (var4 != null) {
         Matcher var5 = FRAME_RATE_PATTERN.matcher(var4);
         var2 = var1;
         if (var5.matches()) {
            int var3 = Integer.parseInt(var5.group(1));
            var4 = var5.group(2);
            if (!TextUtils.isEmpty(var4)) {
               var2 = (float)var3 / (float)Integer.parseInt(var4);
            } else {
               var2 = (float)var3;
            }
         }
      }

      return var2;
   }

   protected static int parseInt(XmlPullParser var0, String var1, int var2) {
      String var3 = var0.getAttributeValue((String)null, var1);
      if (var3 != null) {
         var2 = Integer.parseInt(var3);
      }

      return var2;
   }

   protected static long parseLong(XmlPullParser var0, String var1, long var2) {
      String var4 = var0.getAttributeValue((String)null, var1);
      if (var4 != null) {
         var2 = Long.parseLong(var4);
      }

      return var2;
   }

   protected static String parseString(XmlPullParser var0, String var1, String var2) {
      var1 = var0.getAttributeValue((String)null, var1);
      String var3 = var1;
      if (var1 == null) {
         var3 = var2;
      }

      return var3;
   }

   protected AdaptationSet buildAdaptationSet(int var1, int var2, List var3, List var4, List var5) {
      return new AdaptationSet(var1, var2, var3, var4, var5);
   }

   protected EventMessage buildEvent(String var1, String var2, long var3, long var5, byte[] var7, long var8) {
      return new EventMessage(var1, var2, var5, var3, var7, var8);
   }

   protected EventStream buildEventStream(String var1, String var2, long var3, long[] var5, EventMessage[] var6) {
      return new EventStream(var1, var2, var3, var5, var6);
   }

   protected Format buildFormat(String var1, String var2, String var3, int var4, int var5, float var6, int var7, int var8, int var9, String var10, int var11, List var12, String var13, List var14) {
      String var15 = getSampleMimeType(var3, var13);
      String var17;
      if (var15 != null) {
         String var16 = var15;
         if ("audio/eac3".equals(var15)) {
            var16 = parseEac3SupplementalProperties(var14);
         }

         if (MimeTypes.isVideo(var16)) {
            return Format.createVideoContainerFormat(var1, var2, var3, var16, var13, var9, var4, var5, var6, (List)null, var11);
         }

         if (MimeTypes.isAudio(var16)) {
            return Format.createAudioContainerFormat(var1, var2, var3, var16, var13, var9, var7, var8, (List)null, var11, var10);
         }

         var17 = var16;
         if (mimeTypeIsRawText(var16)) {
            if ("application/cea-608".equals(var16)) {
               var4 = parseCea608AccessibilityChannel(var12);
            } else if ("application/cea-708".equals(var16)) {
               var4 = parseCea708AccessibilityChannel(var12);
            } else {
               var4 = -1;
            }

            return Format.createTextContainerFormat(var1, var2, var3, var16, var13, var9, var11, var10, var4);
         }
      } else {
         var17 = var15;
      }

      return Format.createContainerFormat(var1, var2, var3, var17, var13, var9, var11, var10);
   }

   protected DashManifest buildMediaPresentationDescription(long var1, long var3, long var5, boolean var7, long var8, long var10, long var12, long var14, ProgramInformation var16, UtcTimingElement var17, Uri var18, List var19) {
      return new DashManifest(var1, var3, var5, var7, var8, var10, var12, var14, var16, var17, var18, var19);
   }

   protected Period buildPeriod(String var1, long var2, List var4, List var5) {
      return new Period(var1, var2, var4, var5);
   }

   protected RangedUri buildRangedUri(String var1, long var2, long var4) {
      return new RangedUri(var1, var2, var4);
   }

   protected Representation buildRepresentation(DashManifestParser.RepresentationInfo var1, String var2, ArrayList var3, ArrayList var4) {
      Format var5 = var1.format;
      String var6 = var1.drmSchemeType;
      if (var6 != null) {
         var2 = var6;
      }

      ArrayList var7 = var1.drmSchemeDatas;
      var7.addAll(var3);
      Format var9 = var5;
      if (!var7.isEmpty()) {
         filterRedundantIncompleteSchemeDatas(var7);
         var9 = var5.copyWithDrmInitData(new DrmInitData(var2, var7));
      }

      ArrayList var8 = var1.inbandEventStreams;
      var8.addAll(var4);
      return Representation.newInstance(var1.revisionId, var9, var1.baseUrl, var1.segmentBase, var8);
   }

   protected SegmentBase.SegmentList buildSegmentList(RangedUri var1, long var2, long var4, long var6, long var8, List var10, List var11) {
      return new SegmentBase.SegmentList(var1, var2, var4, var6, var8, var10, var11);
   }

   protected SegmentBase.SegmentTemplate buildSegmentTemplate(RangedUri var1, long var2, long var4, long var6, long var8, List var10, UrlTemplate var11, UrlTemplate var12) {
      return new SegmentBase.SegmentTemplate(var1, var2, var4, var6, var8, var10, var11, var12);
   }

   protected SegmentBase.SegmentTimelineElement buildSegmentTimelineElement(long var1, long var3) {
      return new SegmentBase.SegmentTimelineElement(var1, var3);
   }

   protected SegmentBase.SingleSegmentBase buildSingleSegmentBase(RangedUri var1, long var2, long var4, long var6, long var8) {
      return new SegmentBase.SingleSegmentBase(var1, var2, var4, var6, var8);
   }

   protected UtcTimingElement buildUtcTimingElement(String var1, String var2) {
      return new UtcTimingElement(var1, var2);
   }

   protected int getContentType(Format var1) {
      String var2 = var1.sampleMimeType;
      if (TextUtils.isEmpty(var2)) {
         return -1;
      } else if (MimeTypes.isVideo(var2)) {
         return 2;
      } else if (MimeTypes.isAudio(var2)) {
         return 1;
      } else {
         return mimeTypeIsRawText(var2) ? 3 : -1;
      }
   }

   public DashManifest parse(Uri var1, InputStream var2) throws IOException {
      try {
         XmlPullParser var3 = this.xmlParserFactory.newPullParser();
         var3.setInput(var2, (String)null);
         if (var3.next() == 2 && "MPD".equals(var3.getName())) {
            return this.parseMediaPresentationDescription(var3, var1.toString());
         } else {
            ParserException var5 = new ParserException("inputStream does not contain a valid media presentation description");
            throw var5;
         }
      } catch (XmlPullParserException var4) {
         throw new ParserException(var4);
      }
   }

   protected AdaptationSet parseAdaptationSet(XmlPullParser var1, String var2, SegmentBase var3) throws XmlPullParserException, IOException {
      XmlPullParser var4 = var1;
      int var5 = parseInt(var1, "id", -1);
      int var6 = this.parseContentType(var1);
      Object var7 = null;
      String var8 = var1.getAttributeValue((String)null, "mimeType");
      String var9 = var1.getAttributeValue((String)null, "codecs");
      int var10 = parseInt(var1, "width", -1);
      int var11 = parseInt(var1, "height", -1);
      float var12 = parseFrameRate(var1, -1.0F);
      int var13 = parseInt(var1, "audioSamplingRate", -1);
      String var14 = "lang";
      String var15 = var1.getAttributeValue((String)null, "lang");
      String var16 = var1.getAttributeValue((String)null, "label");
      ArrayList var17 = new ArrayList();
      ArrayList var18 = new ArrayList();
      ArrayList var19 = new ArrayList();
      ArrayList var20 = new ArrayList();
      ArrayList var21 = new ArrayList();
      String var22 = var2;
      Object var23 = var3;
      String var31 = var15;
      var2 = null;
      boolean var24 = false;
      int var25 = 0;
      int var26 = -1;

      do {
         var1.next();
         if (XmlPullParserUtil.isStartTag(var4, "BaseURL")) {
            if (!var24) {
               var22 = parseBaseUrl(var4, var22);
               var24 = true;
            }
         } else {
            int var28;
            int var29;
            if (XmlPullParserUtil.isStartTag(var4, "ContentProtection")) {
               Pair var34 = this.parseContentProtection(var1);
               Object var27 = var34.first;
               if (var27 != null) {
                  var2 = (String)var27;
               }

               var27 = var34.second;
               var28 = var25;
               var29 = var26;
               var15 = var2;
               if (var27 != null) {
                  var17.add(var27);
                  var28 = var25;
                  var29 = var26;
                  var15 = var2;
               }
            } else {
               if (XmlPullParserUtil.isStartTag(var4, "ContentComponent")) {
                  var31 = checkLanguageConsistency(var31, var4.getAttributeValue((String)var7, var14));
                  var6 = checkContentTypeConsistency(var6, this.parseContentType(var1));
                  continue;
               }

               if (XmlPullParserUtil.isStartTag(var4, "Role")) {
                  var28 = var25 | this.parseRole(var1);
                  var15 = var2;
                  var29 = var26;
               } else {
                  if (!XmlPullParserUtil.isStartTag(var4, "AudioChannelConfiguration")) {
                     if (XmlPullParserUtil.isStartTag(var4, "Accessibility")) {
                        var19.add(parseDescriptor(var4, "Accessibility"));
                        continue;
                     }

                     if (XmlPullParserUtil.isStartTag(var4, "SupplementalProperty")) {
                        var20.add(parseDescriptor(var4, "SupplementalProperty"));
                        continue;
                     }

                     if (XmlPullParserUtil.isStartTag(var4, "Representation")) {
                        DashManifestParser.RepresentationInfo var33 = this.parseRepresentation(var1, var22, var16, var8, var9, var10, var11, var12, var26, var13, var31, var25, var19, (SegmentBase)var23);
                        var6 = checkContentTypeConsistency(var6, this.getContentType(var33.format));
                        var21.add(var33);
                        var4 = var1;
                        continue;
                     }

                     Object var32;
                     if (XmlPullParserUtil.isStartTag(var1, "SegmentBase")) {
                        var32 = this.parseSegmentBase(var1, (SegmentBase.SingleSegmentBase)var23);
                     } else if (XmlPullParserUtil.isStartTag(var1, "SegmentList")) {
                        var32 = this.parseSegmentList(var1, (SegmentBase.SegmentList)var23);
                     } else {
                        if (!XmlPullParserUtil.isStartTag(var1, "SegmentTemplate")) {
                           if (XmlPullParserUtil.isStartTag(var1, "InbandEventStream")) {
                              var18.add(parseDescriptor(var1, "InbandEventStream"));
                              var4 = var1;
                           } else {
                              var4 = var1;
                              if (XmlPullParserUtil.isStartTag(var1)) {
                                 this.parseAdaptationSetChild(var1);
                                 var4 = var1;
                              }
                           }
                           continue;
                        }

                        var32 = this.parseSegmentTemplate(var1, (SegmentBase.SegmentTemplate)var23);
                     }

                     var23 = var32;
                     var4 = var1;
                     var6 = var6;
                     continue;
                  }

                  var29 = this.parseAudioChannelConfiguration(var1);
                  var28 = var25;
                  var15 = var2;
               }
            }

            var25 = var28;
            var26 = var29;
            var2 = var15;
         }
      } while(!XmlPullParserUtil.isEndTag(var4, "AdaptationSet"));

      ArrayList var30 = new ArrayList(var21.size());

      for(var26 = 0; var26 < var21.size(); ++var26) {
         var30.add(this.buildRepresentation((DashManifestParser.RepresentationInfo)var21.get(var26), var2, var17, var18));
      }

      return this.buildAdaptationSet(var5, var6, var30, var19, var20);
   }

   protected void parseAdaptationSetChild(XmlPullParser var1) throws XmlPullParserException, IOException {
      maybeSkipTag(var1);
   }

   protected int parseAudioChannelConfiguration(XmlPullParser var1) throws XmlPullParserException, IOException {
      String var2 = parseString(var1, "schemeIdUri", (String)null);
      boolean var3 = "urn:mpeg:dash:23003:3:audio_channel_configuration:2011".equals(var2);
      int var4 = -1;
      if (var3) {
         var4 = parseInt(var1, "value", -1);
      } else if ("tag:dolby.com,2014:dash:audio_channel_configuration:2011".equals(var2)) {
         var4 = parseDolbyChannelConfiguration(var1);
      }

      do {
         var1.next();
      } while(!XmlPullParserUtil.isEndTag(var1, "AudioChannelConfiguration"));

      return var4;
   }

   protected Pair parseContentProtection(XmlPullParser var1) throws XmlPullParserException, IOException {
      Object var5;
      byte[] var6;
      boolean var8;
      String var9;
      Object var11;
      label109: {
         label108: {
            String var2 = var1.getAttributeValue((String)null, "schemeIdUri");
            if (var2 != null) {
               var2 = Util.toLowerInvariant(var2);
               byte var3 = -1;
               int var4 = var2.hashCode();
               if (var4 != 489446379) {
                  if (var4 != 755418770) {
                     if (var4 == 1812765994 && var2.equals("urn:mpeg:dash:mp4protection:2011")) {
                        var3 = 0;
                     }
                  } else if (var2.equals("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed")) {
                     var3 = 2;
                  }
               } else if (var2.equals("urn:uuid:9a04f079-9840-4286-ab92-e65be0885f95")) {
                  var3 = 1;
               }

               if (var3 == 0) {
                  String var14 = var1.getAttributeValue((String)null, "value");
                  var2 = XmlPullParserUtil.getAttributeValueIgnorePrefix(var1, "default_KID");
                  if (!TextUtils.isEmpty(var2) && !"00000000-0000-0000-0000-000000000000".equals(var2)) {
                     String[] var15 = var2.split("\\s+");
                     UUID[] var12 = new UUID[var15.length];

                     for(int var13 = 0; var13 < var15.length; ++var13) {
                        var12[var13] = UUID.fromString(var15[var13]);
                     }

                     var6 = PsshAtomUtil.buildPsshAtom(C.COMMON_PSSH_UUID, var12, (byte[])null);
                     UUID var7 = C.COMMON_PSSH_UUID;
                     var11 = null;
                     var8 = false;
                     var9 = var14;
                     var5 = var7;
                  } else {
                     var6 = null;
                     var11 = var6;
                     var9 = var14;
                     var8 = false;
                     var5 = var6;
                  }
                  break label109;
               }

               if (var3 == 1) {
                  var5 = C.PLAYREADY_UUID;
                  break label108;
               }

               if (var3 == 2) {
                  var5 = C.WIDEVINE_UUID;
                  break label108;
               }
            }

            var5 = null;
         }

         var6 = null;
         var9 = null;
         var11 = var9;
         var8 = false;
      }

      while(true) {
         byte[] var18;
         Object var20;
         label86: {
            var1.next();
            if (XmlPullParserUtil.isStartTag(var1, "ms:laurl")) {
               var11 = var1.getAttributeValue((String)null, "licenseUrl");
            } else if (XmlPullParserUtil.isStartTag(var1, "widevine:license")) {
               String var16 = var1.getAttributeValue((String)null, "robustness_level");
               if (var16 != null && var16.startsWith("HW")) {
                  var8 = true;
               } else {
                  var8 = false;
               }
            } else {
               if (var6 == null && XmlPullParserUtil.isStartTagIgnorePrefix(var1, "pssh") && var1.next() == 4) {
                  var18 = Base64.decode(var1.getText(), 0);
                  var20 = PsshAtomUtil.parseUuid(var18);
                  if (var20 == null) {
                     Log.w("MpdParser", "Skipping malformed cenc:pssh data");
                     var18 = null;
                  }
                  break label86;
               }

               if (var6 == null && C.PLAYREADY_UUID.equals(var5) && XmlPullParserUtil.isStartTag(var1, "mspr:pro") && var1.next() == 4) {
                  var6 = PsshAtomUtil.buildPsshAtom(C.PLAYREADY_UUID, Base64.decode(var1.getText(), 0));
               } else {
                  maybeSkipTag(var1);
               }
            }

            var18 = var6;
            var20 = var5;
         }

         if (XmlPullParserUtil.isEndTag(var1, "ContentProtection")) {
            DrmInitData.SchemeData var10;
            if (var20 != null) {
               var10 = new DrmInitData.SchemeData((UUID)var20, (String)var11, "video/mp4", var18, var8);
            } else {
               var10 = null;
            }

            return Pair.create(var9, var10);
         }

         var5 = var20;
         var6 = var18;
      }
   }

   protected int parseContentType(XmlPullParser var1) {
      String var4 = var1.getAttributeValue((String)null, "contentType");
      boolean var2 = TextUtils.isEmpty(var4);
      byte var3 = -1;
      if (!var2) {
         if ("audio".equals(var4)) {
            var3 = 1;
         } else if ("video".equals(var4)) {
            var3 = 2;
         } else if ("text".equals(var4)) {
            var3 = 3;
         }
      }

      return var3;
   }

   protected EventMessage parseEvent(XmlPullParser var1, String var2, String var3, long var4, ByteArrayOutputStream var6) throws IOException, XmlPullParserException {
      long var7 = parseLong(var1, "id", 0L);
      long var9 = parseLong(var1, "duration", -9223372036854775807L);
      long var11 = parseLong(var1, "presentationTime", 0L);
      var9 = Util.scaleLargeTimestamp(var9, 1000L, var4);
      var4 = Util.scaleLargeTimestamp(var11, 1000000L, var4);
      String var13 = parseString(var1, "messageData", (String)null);
      byte[] var14 = this.parseEventObject(var1, var6);
      if (var13 != null) {
         var14 = Util.getUtf8Bytes(var13);
      }

      return this.buildEvent(var2, var3, var7, var9, var14, var4);
   }

   protected byte[] parseEventObject(XmlPullParser var1, ByteArrayOutputStream var2) throws XmlPullParserException, IOException {
      var2.reset();
      XmlSerializer var3 = Xml.newSerializer();
      var3.setOutput(var2, "UTF-8");
      var1.nextToken();

      label34:
      for(; !XmlPullParserUtil.isEndTag(var1, "Event"); var1.nextToken()) {
         int var4 = var1.getEventType();
         int var5 = 0;
         switch(var4) {
         case 0:
            var3.startDocument((String)null, false);
            break;
         case 1:
            var3.endDocument();
            break;
         case 2:
            var3.startTag(var1.getNamespace(), var1.getName());

            while(true) {
               if (var5 >= var1.getAttributeCount()) {
                  continue label34;
               }

               var3.attribute(var1.getAttributeNamespace(var5), var1.getAttributeName(var5), var1.getAttributeValue(var5));
               ++var5;
            }
         case 3:
            var3.endTag(var1.getNamespace(), var1.getName());
            break;
         case 4:
            var3.text(var1.getText());
            break;
         case 5:
            var3.cdsect(var1.getText());
            break;
         case 6:
            var3.entityRef(var1.getText());
            break;
         case 7:
            var3.ignorableWhitespace(var1.getText());
            break;
         case 8:
            var3.processingInstruction(var1.getText());
            break;
         case 9:
            var3.comment(var1.getText());
            break;
         case 10:
            var3.docdecl(var1.getText());
         }
      }

      var3.flush();
      return var2.toByteArray();
   }

   protected EventStream parseEventStream(XmlPullParser var1) throws XmlPullParserException, IOException {
      String var2 = parseString(var1, "schemeIdUri", "");
      String var3 = parseString(var1, "value", "");
      long var4 = parseLong(var1, "timescale", 1L);
      ArrayList var6 = new ArrayList();
      ByteArrayOutputStream var7 = new ByteArrayOutputStream(512);

      do {
         var1.next();
         if (XmlPullParserUtil.isStartTag(var1, "Event")) {
            var6.add(this.parseEvent(var1, var2, var3, var4, var7));
         } else {
            maybeSkipTag(var1);
         }
      } while(!XmlPullParserUtil.isEndTag(var1, "EventStream"));

      long[] var11 = new long[var6.size()];
      EventMessage[] var8 = new EventMessage[var6.size()];

      for(int var9 = 0; var9 < var6.size(); ++var9) {
         EventMessage var10 = (EventMessage)var6.get(var9);
         var11[var9] = var10.presentationTimeUs;
         var8[var9] = var10;
      }

      return this.buildEventStream(var2, var3, var4, var11, var8);
   }

   protected RangedUri parseInitialization(XmlPullParser var1) {
      return this.parseRangedUrl(var1, "sourceURL", "range");
   }

   protected DashManifest parseMediaPresentationDescription(XmlPullParser var1, String var2) throws XmlPullParserException, IOException {
      long var3 = parseDateTime(var1, "availabilityStartTime", -9223372036854775807L);
      long var5 = parseDuration(var1, "mediaPresentationDuration", -9223372036854775807L);
      long var7 = parseDuration(var1, "minBufferTime", -9223372036854775807L);
      String var9 = var1.getAttributeValue((String)null, "type");
      boolean var10 = false;
      boolean var11;
      if (var9 != null && "dynamic".equals(var9)) {
         var11 = true;
      } else {
         var11 = false;
      }

      long var12;
      if (var11) {
         var12 = parseDuration(var1, "minimumUpdatePeriod", -9223372036854775807L);
      } else {
         var12 = -9223372036854775807L;
      }

      long var14;
      if (var11) {
         var14 = parseDuration(var1, "timeShiftBufferDepth", -9223372036854775807L);
      } else {
         var14 = -9223372036854775807L;
      }

      long var16;
      if (var11) {
         var16 = parseDuration(var1, "suggestedPresentationDelay", -9223372036854775807L);
      } else {
         var16 = -9223372036854775807L;
      }

      long var18 = parseDateTime(var1, "publishTime", -9223372036854775807L);
      ArrayList var20 = new ArrayList();
      long var21;
      if (var11) {
         var21 = -9223372036854775807L;
      } else {
         var21 = 0L;
      }

      ProgramInformation var23 = null;
      Object var30 = var23;
      boolean var25 = false;
      String var26 = var2;
      Object var29 = var23;

      do {
         var1.next();
         if (XmlPullParserUtil.isStartTag(var1, "BaseURL")) {
            if (!var10) {
               var26 = parseBaseUrl(var1, var26);
               var10 = true;
            }
         } else if (XmlPullParserUtil.isStartTag(var1, "ProgramInformation")) {
            var23 = this.parseProgramInformation(var1);
         } else if (XmlPullParserUtil.isStartTag(var1, "UTCTiming")) {
            var30 = this.parseUtcTiming(var1);
         } else if (XmlPullParserUtil.isStartTag(var1, "Location")) {
            var29 = Uri.parse(var1.nextText());
         } else if (XmlPullParserUtil.isStartTag(var1, "Period") && !var25) {
            Pair var27 = this.parsePeriod(var1, var26, var21);
            Period var24 = (Period)var27.first;
            if (var24.startMs == -9223372036854775807L) {
               if (!var11) {
                  StringBuilder var28 = new StringBuilder();
                  var28.append("Unable to determine start of period ");
                  var28.append(var20.size());
                  throw new ParserException(var28.toString());
               }

               var25 = true;
            } else {
               var21 = (Long)var27.second;
               if (var21 == -9223372036854775807L) {
                  var21 = -9223372036854775807L;
               } else {
                  var21 += var24.startMs;
               }

               var20.add(var24);
            }
         } else {
            maybeSkipTag(var1);
         }
      } while(!XmlPullParserUtil.isEndTag(var1, "MPD"));

      label75: {
         if (var5 == -9223372036854775807L) {
            if (var21 != -9223372036854775807L) {
               break label75;
            }

            if (!var11) {
               throw new ParserException("Unable to determine duration of static manifest.");
            }
         }

         var21 = var5;
      }

      if (!var20.isEmpty()) {
         return this.buildMediaPresentationDescription(var3, var21, var7, var11, var12, var14, var16, var18, var23, (UtcTimingElement)var30, (Uri)var29, var20);
      } else {
         throw new ParserException("No periods found.");
      }
   }

   protected Pair parsePeriod(XmlPullParser var1, String var2, long var3) throws XmlPullParserException, IOException {
      String var5 = var1.getAttributeValue((String)null, "id");
      long var6 = parseDuration(var1, "start", var3);
      var3 = parseDuration(var1, "duration", -9223372036854775807L);
      ArrayList var8 = new ArrayList();
      ArrayList var9 = new ArrayList();
      boolean var10 = false;
      Object var11 = null;
      String var12 = var2;

      do {
         var1.next();
         boolean var13;
         String var14;
         Object var15;
         if (XmlPullParserUtil.isStartTag(var1, "BaseURL")) {
            var13 = var10;
            var15 = var11;
            var14 = var12;
            if (!var10) {
               var14 = parseBaseUrl(var1, var12);
               var13 = true;
               var15 = var11;
            }
         } else if (XmlPullParserUtil.isStartTag(var1, "AdaptationSet")) {
            var8.add(this.parseAdaptationSet(var1, var12, (SegmentBase)var11));
            var13 = var10;
            var15 = var11;
            var14 = var12;
         } else if (XmlPullParserUtil.isStartTag(var1, "EventStream")) {
            var9.add(this.parseEventStream(var1));
            var13 = var10;
            var15 = var11;
            var14 = var12;
         } else if (XmlPullParserUtil.isStartTag(var1, "SegmentBase")) {
            var15 = this.parseSegmentBase(var1, (SegmentBase.SingleSegmentBase)null);
            var13 = var10;
            var14 = var12;
         } else if (XmlPullParserUtil.isStartTag(var1, "SegmentList")) {
            var15 = this.parseSegmentList(var1, (SegmentBase.SegmentList)null);
            var13 = var10;
            var14 = var12;
         } else if (XmlPullParserUtil.isStartTag(var1, "SegmentTemplate")) {
            var15 = this.parseSegmentTemplate(var1, (SegmentBase.SegmentTemplate)null);
            var13 = var10;
            var14 = var12;
         } else {
            maybeSkipTag(var1);
            var14 = var12;
            var15 = var11;
            var13 = var10;
         }

         var10 = var13;
         var11 = var15;
         var12 = var14;
      } while(!XmlPullParserUtil.isEndTag(var1, "Period"));

      return Pair.create(this.buildPeriod(var5, var6, var8, var9), var3);
   }

   protected ProgramInformation parseProgramInformation(XmlPullParser var1) throws IOException, XmlPullParserException {
      String var2 = null;
      String var3 = parseString(var1, "moreInformationURL", (String)null);
      String var4 = parseString(var1, "lang", (String)null);
      String var5 = null;
      String var6 = var5;

      do {
         var1.next();
         if (XmlPullParserUtil.isStartTag(var1, "Title")) {
            var2 = var1.nextText();
         } else if (XmlPullParserUtil.isStartTag(var1, "Source")) {
            var5 = var1.nextText();
         } else if (XmlPullParserUtil.isStartTag(var1, "Copyright")) {
            var6 = var1.nextText();
         } else {
            maybeSkipTag(var1);
         }
      } while(!XmlPullParserUtil.isEndTag(var1, "ProgramInformation"));

      return new ProgramInformation(var2, var5, var6, var3, var4);
   }

   protected RangedUri parseRangedUrl(XmlPullParser var1, String var2, String var3) {
      var2 = var1.getAttributeValue((String)null, var2);
      String var10 = var1.getAttributeValue((String)null, var3);
      long var4;
      long var6;
      if (var10 != null) {
         String[] var11 = var10.split("-");
         var4 = Long.parseLong(var11[0]);
         var6 = var4;
         if (var11.length == 2) {
            long var8 = Long.parseLong(var11[1]) - var4 + 1L;
            var6 = var4;
            var4 = var8;
            return this.buildRangedUri(var2, var6, var4);
         }
      } else {
         var6 = 0L;
      }

      var4 = -1L;
      return this.buildRangedUri(var2, var6, var4);
   }

   protected DashManifestParser.RepresentationInfo parseRepresentation(XmlPullParser var1, String var2, String var3, String var4, String var5, int var6, int var7, float var8, int var9, int var10, String var11, int var12, List var13, SegmentBase var14) throws XmlPullParserException, IOException {
      String var15 = var1.getAttributeValue((String)null, "id");
      int var16 = parseInt(var1, "bandwidth", -1);
      String var17 = parseString(var1, "mimeType", var4);
      String var18 = parseString(var1, "codecs", var5);
      int var19 = parseInt(var1, "width", var6);
      int var20 = parseInt(var1, "height", var7);
      var8 = parseFrameRate(var1, var8);
      var10 = parseInt(var1, "audioSamplingRate", var10);
      ArrayList var21 = new ArrayList();
      ArrayList var22 = new ArrayList();
      ArrayList var23 = new ArrayList();
      boolean var28 = false;
      var6 = var9;
      Object var27 = var14;
      var14 = null;
      var4 = var2;
      var2 = var14;

      do {
         Object var26;
         String var29;
         label56: {
            var1.next();
            if (XmlPullParserUtil.isStartTag(var1, "BaseURL")) {
               if (!var28) {
                  var29 = parseBaseUrl(var1, var4);
                  var28 = true;
                  var26 = var27;
                  var5 = var29;
                  break label56;
               }

               var29 = var2;
            } else {
               if (XmlPullParserUtil.isStartTag(var1, "AudioChannelConfiguration")) {
                  var6 = this.parseAudioChannelConfiguration(var1);
                  continue;
               }

               if (XmlPullParserUtil.isStartTag(var1, "SegmentBase")) {
                  SegmentBase.SingleSegmentBase var33 = this.parseSegmentBase(var1, (SegmentBase.SingleSegmentBase)var27);
                  var5 = var4;
                  var26 = var33;
                  break label56;
               }

               if (XmlPullParserUtil.isStartTag(var1, "SegmentList")) {
                  SegmentBase.SegmentList var32 = this.parseSegmentList(var1, (SegmentBase.SegmentList)var27);
                  var5 = var4;
                  var26 = var32;
                  break label56;
               }

               if (XmlPullParserUtil.isStartTag(var1, "SegmentTemplate")) {
                  SegmentBase.SegmentTemplate var31 = this.parseSegmentTemplate(var1, (SegmentBase.SegmentTemplate)var27);
                  var5 = var4;
                  var26 = var31;
                  break label56;
               }

               if (XmlPullParserUtil.isStartTag(var1, "ContentProtection")) {
                  Pair var30 = this.parseContentProtection(var1);
                  Object var24 = var30.first;
                  if (var24 != null) {
                     var2 = (String)var24;
                  }

                  var24 = var30.second;
                  var29 = var2;
                  if (var24 != null) {
                     var21.add(var24);
                     var29 = var2;
                  }
               } else if (XmlPullParserUtil.isStartTag(var1, "InbandEventStream")) {
                  var22.add(parseDescriptor(var1, "InbandEventStream"));
                  var29 = var2;
               } else if (XmlPullParserUtil.isStartTag(var1, "SupplementalProperty")) {
                  var23.add(parseDescriptor(var1, "SupplementalProperty"));
                  var29 = var2;
               } else {
                  maybeSkipTag(var1);
                  var29 = var2;
               }
            }

            var2 = var29;
            continue;
         }

         var29 = var5;
         var27 = var26;
         var4 = var29;
      } while(!XmlPullParserUtil.isEndTag(var1, "Representation"));

      Format var25 = this.buildFormat(var15, var3, var17, var19, var20, var8, var6, var10, var16, var11, var12, var13, var18, var23);
      if (var27 == null) {
         var27 = new SegmentBase.SingleSegmentBase();
      }

      return new DashManifestParser.RepresentationInfo(var25, var4, (SegmentBase)var27, var2, var21, var22, -1L);
   }

   protected int parseRole(XmlPullParser var1) throws XmlPullParserException, IOException {
      String var2 = parseString(var1, "schemeIdUri", (String)null);
      String var3 = parseString(var1, "value", (String)null);

      do {
         var1.next();
      } while(!XmlPullParserUtil.isEndTag(var1, "Role"));

      byte var4;
      if ("urn:mpeg:dash:role:2011".equals(var2) && "main".equals(var3)) {
         var4 = 1;
      } else {
         var4 = 0;
      }

      return var4;
   }

   protected SegmentBase.SingleSegmentBase parseSegmentBase(XmlPullParser var1, SegmentBase.SingleSegmentBase var2) throws XmlPullParserException, IOException {
      long var3;
      if (var2 != null) {
         var3 = var2.timescale;
      } else {
         var3 = 1L;
      }

      long var5 = parseLong(var1, "timescale", var3);
      long var7 = 0L;
      if (var2 != null) {
         var3 = var2.presentationTimeOffset;
      } else {
         var3 = 0L;
      }

      long var9 = parseLong(var1, "presentationTimeOffset", var3);
      if (var2 != null) {
         var3 = var2.indexStart;
      } else {
         var3 = 0L;
      }

      if (var2 != null) {
         var7 = var2.indexLength;
      }

      RangedUri var11 = null;
      String var12 = var1.getAttributeValue((String)null, "indexRange");
      if (var12 != null) {
         String[] var13 = var12.split("-");
         var3 = Long.parseLong(var13[0]);
         var7 = Long.parseLong(var13[1]) - var3 + 1L;
      }

      if (var2 != null) {
         var11 = var2.initialization;
      }

      RangedUri var14;
      do {
         var1.next();
         if (XmlPullParserUtil.isStartTag(var1, "Initialization")) {
            var14 = this.parseInitialization(var1);
         } else {
            maybeSkipTag(var1);
            var14 = var11;
         }

         var11 = var14;
      } while(!XmlPullParserUtil.isEndTag(var1, "SegmentBase"));

      return this.buildSingleSegmentBase(var14, var5, var9, var3, var7);
   }

   protected SegmentBase.SegmentList parseSegmentList(XmlPullParser var1, SegmentBase.SegmentList var2) throws XmlPullParserException, IOException {
      long var3 = 1L;
      long var5;
      if (var2 != null) {
         var5 = var2.timescale;
      } else {
         var5 = 1L;
      }

      long var7 = parseLong(var1, "timescale", var5);
      if (var2 != null) {
         var5 = var2.presentationTimeOffset;
      } else {
         var5 = 0L;
      }

      long var9 = parseLong(var1, "presentationTimeOffset", var5);
      if (var2 != null) {
         var5 = var2.duration;
      } else {
         var5 = -9223372036854775807L;
      }

      long var11 = parseLong(var1, "duration", var5);
      var5 = var3;
      if (var2 != null) {
         var5 = var2.startNumber;
      }

      var5 = parseLong(var1, "startNumber", var5);
      ArrayList var13 = null;
      RangedUri var14 = null;
      Object var15 = var14;

      RangedUri var16;
      ArrayList var17;
      Object var18;
      do {
         var1.next();
         if (XmlPullParserUtil.isStartTag(var1, "Initialization")) {
            var16 = this.parseInitialization(var1);
            var17 = var13;
            var18 = var15;
         } else if (XmlPullParserUtil.isStartTag(var1, "SegmentTimeline")) {
            var18 = this.parseSegmentTimeline(var1);
            var17 = var13;
            var16 = var14;
         } else if (XmlPullParserUtil.isStartTag(var1, "SegmentURL")) {
            var17 = var13;
            if (var13 == null) {
               var17 = new ArrayList();
            }

            var17.add(this.parseSegmentUrl(var1));
            var16 = var14;
            var18 = var15;
         } else {
            maybeSkipTag(var1);
            var18 = var15;
            var16 = var14;
            var17 = var13;
         }

         var13 = var17;
         var14 = var16;
         var15 = var18;
      } while(!XmlPullParserUtil.isEndTag(var1, "SegmentList"));

      Object var20 = var17;
      RangedUri var21 = var16;
      Object var19 = var18;
      if (var2 != null) {
         if (var16 == null) {
            var16 = var2.initialization;
         }

         if (var18 == null) {
            var18 = var2.segmentTimeline;
         }

         if (var17 != null) {
            var20 = var17;
            var21 = var16;
            var19 = var18;
         } else {
            var20 = var2.mediaSegments;
            var19 = var18;
            var21 = var16;
         }
      }

      return this.buildSegmentList(var21, var7, var9, var5, var11, (List)var19, (List)var20);
   }

   protected SegmentBase.SegmentTemplate parseSegmentTemplate(XmlPullParser var1, SegmentBase.SegmentTemplate var2) throws XmlPullParserException, IOException {
      long var3 = 1L;
      long var5;
      if (var2 != null) {
         var5 = var2.timescale;
      } else {
         var5 = 1L;
      }

      long var7 = parseLong(var1, "timescale", var5);
      if (var2 != null) {
         var5 = var2.presentationTimeOffset;
      } else {
         var5 = 0L;
      }

      long var9 = parseLong(var1, "presentationTimeOffset", var5);
      if (var2 != null) {
         var5 = var2.duration;
      } else {
         var5 = -9223372036854775807L;
      }

      long var11 = parseLong(var1, "duration", var5);
      var5 = var3;
      if (var2 != null) {
         var5 = var2.startNumber;
      }

      var5 = parseLong(var1, "startNumber", var5);
      RangedUri var13 = null;
      UrlTemplate var14;
      if (var2 != null) {
         var14 = var2.mediaTemplate;
      } else {
         var14 = null;
      }

      UrlTemplate var15 = this.parseUrlTemplate(var1, "media", var14);
      if (var2 != null) {
         var14 = var2.initializationTemplate;
      } else {
         var14 = null;
      }

      UrlTemplate var16 = this.parseUrlTemplate(var1, "initialization", var14);
      List var17 = null;

      List var18;
      RangedUri var20;
      do {
         var1.next();
         if (XmlPullParserUtil.isStartTag(var1, "Initialization")) {
            var20 = this.parseInitialization(var1);
            var18 = var17;
         } else if (XmlPullParserUtil.isStartTag(var1, "SegmentTimeline")) {
            var18 = this.parseSegmentTimeline(var1);
            var20 = var13;
         } else {
            maybeSkipTag(var1);
            var18 = var17;
            var20 = var13;
         }

         var13 = var20;
         var17 = var18;
      } while(!XmlPullParserUtil.isEndTag(var1, "SegmentTemplate"));

      RangedUri var21 = var20;
      List var19 = var18;
      if (var2 != null) {
         if (var20 == null) {
            var20 = var2.initialization;
         }

         if (var18 != null) {
            var21 = var20;
            var19 = var18;
         } else {
            var19 = var2.segmentTimeline;
            var21 = var20;
         }
      }

      return this.buildSegmentTemplate(var21, var7, var9, var5, var11, var19, var16, var15);
   }

   protected List parseSegmentTimeline(XmlPullParser var1) throws XmlPullParserException, IOException {
      ArrayList var2 = new ArrayList();
      long var3 = 0L;

      do {
         var1.next();
         if (XmlPullParserUtil.isStartTag(var1, "S")) {
            long var5 = parseLong(var1, "t", var3);
            long var7 = parseLong(var1, "d", -9223372036854775807L);
            int var9 = 0;
            int var10 = parseInt(var1, "r", 0);

            while(true) {
               var3 = var5;
               if (var9 >= var10 + 1) {
                  break;
               }

               var2.add(this.buildSegmentTimelineElement(var5, var7));
               var5 += var7;
               ++var9;
            }
         } else {
            maybeSkipTag(var1);
         }
      } while(!XmlPullParserUtil.isEndTag(var1, "SegmentTimeline"));

      return var2;
   }

   protected RangedUri parseSegmentUrl(XmlPullParser var1) {
      return this.parseRangedUrl(var1, "media", "mediaRange");
   }

   protected UrlTemplate parseUrlTemplate(XmlPullParser var1, String var2, UrlTemplate var3) {
      String var4 = var1.getAttributeValue((String)null, var2);
      return var4 != null ? UrlTemplate.compile(var4) : var3;
   }

   protected UtcTimingElement parseUtcTiming(XmlPullParser var1) {
      return this.buildUtcTimingElement(var1.getAttributeValue((String)null, "schemeIdUri"), var1.getAttributeValue((String)null, "value"));
   }

   protected static final class RepresentationInfo {
      public final String baseUrl;
      public final ArrayList drmSchemeDatas;
      public final String drmSchemeType;
      public final Format format;
      public final ArrayList inbandEventStreams;
      public final long revisionId;
      public final SegmentBase segmentBase;

      public RepresentationInfo(Format var1, String var2, SegmentBase var3, String var4, ArrayList var5, ArrayList var6, long var7) {
         this.format = var1;
         this.baseUrl = var2;
         this.segmentBase = var3;
         this.drmSchemeType = var4;
         this.drmSchemeDatas = var5;
         this.inbandEventStreams = var6;
         this.revisionId = var7;
      }
   }
}
