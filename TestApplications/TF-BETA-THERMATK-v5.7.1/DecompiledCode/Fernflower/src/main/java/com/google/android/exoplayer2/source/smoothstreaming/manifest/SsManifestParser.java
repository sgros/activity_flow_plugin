package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class SsManifestParser implements ParsingLoadable.Parser {
   private final XmlPullParserFactory xmlParserFactory;

   public SsManifestParser() {
      try {
         this.xmlParserFactory = XmlPullParserFactory.newInstance();
      } catch (XmlPullParserException var2) {
         throw new RuntimeException("Couldn't create XmlPullParserFactory instance", var2);
      }
   }

   public SsManifest parse(Uri var1, InputStream var2) throws IOException {
      try {
         XmlPullParser var3 = this.xmlParserFactory.newPullParser();
         var3.setInput(var2, (String)null);
         SsManifestParser.SmoothStreamingMediaParser var6 = new SsManifestParser.SmoothStreamingMediaParser((SsManifestParser.ElementParser)null, var1.toString());
         SsManifest var5 = (SsManifest)var6.parse(var3);
         return var5;
      } catch (XmlPullParserException var4) {
         throw new ParserException(var4);
      }
   }

   private abstract static class ElementParser {
      private final String baseUri;
      private final List normalizedAttributes;
      private final SsManifestParser.ElementParser parent;
      private final String tag;

      public ElementParser(SsManifestParser.ElementParser var1, String var2, String var3) {
         this.parent = var1;
         this.baseUri = var2;
         this.tag = var3;
         this.normalizedAttributes = new LinkedList();
      }

      private SsManifestParser.ElementParser newChildParser(SsManifestParser.ElementParser var1, String var2, String var3) {
         if ("QualityLevel".equals(var2)) {
            return new SsManifestParser.QualityLevelParser(var1, var3);
         } else if ("Protection".equals(var2)) {
            return new SsManifestParser.ProtectionParser(var1, var3);
         } else {
            return "StreamIndex".equals(var2) ? new SsManifestParser.StreamIndexParser(var1, var3) : null;
         }
      }

      protected void addChild(Object var1) {
      }

      protected abstract Object build();

      protected final Object getNormalizedAttribute(String var1) {
         for(int var2 = 0; var2 < this.normalizedAttributes.size(); ++var2) {
            Pair var3 = (Pair)this.normalizedAttributes.get(var2);
            if (((String)var3.first).equals(var1)) {
               return var3.second;
            }
         }

         SsManifestParser.ElementParser var5 = this.parent;
         Object var4;
         if (var5 == null) {
            var4 = null;
         } else {
            var4 = var5.getNormalizedAttribute(var1);
         }

         return var4;
      }

      protected boolean handleChildInline(String var1) {
         return false;
      }

      public final Object parse(XmlPullParser var1) throws XmlPullParserException, IOException {
         boolean var2 = false;
         int var3 = 0;

         while(true) {
            int var4 = var1.getEventType();
            if (var4 == 1) {
               return null;
            }

            boolean var5;
            String var6;
            if (var4 != 2) {
               if (var4 != 3) {
                  if (var4 != 4) {
                     var5 = var2;
                     var4 = var3;
                  } else {
                     var5 = var2;
                     var4 = var3;
                     if (var2) {
                        var5 = var2;
                        var4 = var3;
                        if (var3 == 0) {
                           this.parseText(var1);
                           var5 = var2;
                           var4 = var3;
                        }
                     }
                  }
               } else {
                  var5 = var2;
                  var4 = var3;
                  if (var2) {
                     if (var3 > 0) {
                        var4 = var3 - 1;
                        var5 = var2;
                     } else {
                        var6 = var1.getName();
                        this.parseEndTag(var1);
                        var5 = var2;
                        var4 = var3;
                        if (!this.handleChildInline(var6)) {
                           return this.build();
                        }
                     }
                  }
               }
            } else {
               var6 = var1.getName();
               if (this.tag.equals(var6)) {
                  this.parseStartTag(var1);
                  var5 = true;
                  var4 = var3;
               } else {
                  var5 = var2;
                  var4 = var3;
                  if (var2) {
                     if (var3 > 0) {
                        var4 = var3 + 1;
                        var5 = var2;
                     } else if (this.handleChildInline(var6)) {
                        this.parseStartTag(var1);
                        var5 = var2;
                        var4 = var3;
                     } else {
                        SsManifestParser.ElementParser var7 = this.newChildParser(this, var6, this.baseUri);
                        if (var7 == null) {
                           var4 = 1;
                           var5 = var2;
                        } else {
                           this.addChild(var7.parse(var1));
                           var4 = var3;
                           var5 = var2;
                        }
                     }
                  }
               }
            }

            var1.next();
            var2 = var5;
            var3 = var4;
         }
      }

      protected final boolean parseBoolean(XmlPullParser var1, String var2, boolean var3) {
         String var4 = var1.getAttributeValue((String)null, var2);
         return var4 != null ? Boolean.parseBoolean(var4) : var3;
      }

      protected void parseEndTag(XmlPullParser var1) {
      }

      protected final int parseInt(XmlPullParser var1, String var2, int var3) throws ParserException {
         String var5 = var1.getAttributeValue((String)null, var2);
         if (var5 != null) {
            try {
               var3 = Integer.parseInt(var5);
               return var3;
            } catch (NumberFormatException var4) {
               throw new ParserException(var4);
            }
         } else {
            return var3;
         }
      }

      protected final long parseLong(XmlPullParser var1, String var2, long var3) throws ParserException {
         String var6 = var1.getAttributeValue((String)null, var2);
         if (var6 != null) {
            try {
               var3 = Long.parseLong(var6);
               return var3;
            } catch (NumberFormatException var5) {
               throw new ParserException(var5);
            }
         } else {
            return var3;
         }
      }

      protected final int parseRequiredInt(XmlPullParser var1, String var2) throws ParserException {
         String var5 = var1.getAttributeValue((String)null, var2);
         if (var5 != null) {
            try {
               int var3 = Integer.parseInt(var5);
               return var3;
            } catch (NumberFormatException var4) {
               throw new ParserException(var4);
            }
         } else {
            throw new SsManifestParser.MissingFieldException(var2);
         }
      }

      protected final long parseRequiredLong(XmlPullParser var1, String var2) throws ParserException {
         String var6 = var1.getAttributeValue((String)null, var2);
         if (var6 != null) {
            try {
               long var3 = Long.parseLong(var6);
               return var3;
            } catch (NumberFormatException var5) {
               throw new ParserException(var5);
            }
         } else {
            throw new SsManifestParser.MissingFieldException(var2);
         }
      }

      protected final String parseRequiredString(XmlPullParser var1, String var2) throws SsManifestParser.MissingFieldException {
         String var3 = var1.getAttributeValue((String)null, var2);
         if (var3 != null) {
            return var3;
         } else {
            throw new SsManifestParser.MissingFieldException(var2);
         }
      }

      protected abstract void parseStartTag(XmlPullParser var1) throws ParserException;

      protected void parseText(XmlPullParser var1) {
      }

      protected final void putNormalizedAttribute(String var1, Object var2) {
         this.normalizedAttributes.add(Pair.create(var1, var2));
      }
   }

   public static class MissingFieldException extends ParserException {
      public MissingFieldException(String var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Missing required field: ");
         var2.append(var1);
         super(var2.toString());
      }
   }

   private static class ProtectionParser extends SsManifestParser.ElementParser {
      private boolean inProtectionHeader;
      private byte[] initData;
      private UUID uuid;

      public ProtectionParser(SsManifestParser.ElementParser var1, String var2) {
         super(var1, var2, "Protection");
      }

      private static TrackEncryptionBox[] buildTrackEncryptionBoxes(byte[] var0) {
         return new TrackEncryptionBox[]{new TrackEncryptionBox(true, (String)null, 8, getProtectionElementKeyId(var0), 0, 0, (byte[])null)};
      }

      private static byte[] getProtectionElementKeyId(byte[] var0) {
         StringBuilder var1 = new StringBuilder();

         for(int var2 = 0; var2 < var0.length; var2 += 2) {
            var1.append((char)var0[var2]);
         }

         String var3 = var1.toString();
         var0 = Base64.decode(var3.substring(var3.indexOf("<KID>") + 5, var3.indexOf("</KID>")), 0);
         swap(var0, 0, 3);
         swap(var0, 1, 2);
         swap(var0, 4, 5);
         swap(var0, 6, 7);
         return var0;
      }

      private static String stripCurlyBraces(String var0) {
         String var1 = var0;
         if (var0.charAt(0) == '{') {
            var1 = var0;
            if (var0.charAt(var0.length() - 1) == '}') {
               var1 = var0.substring(1, var0.length() - 1);
            }
         }

         return var1;
      }

      private static void swap(byte[] var0, int var1, int var2) {
         byte var3 = var0[var1];
         var0[var1] = (byte)var0[var2];
         var0[var2] = (byte)var3;
      }

      public Object build() {
         UUID var1 = this.uuid;
         return new SsManifest.ProtectionElement(var1, PsshAtomUtil.buildPsshAtom(var1, this.initData), buildTrackEncryptionBoxes(this.initData));
      }

      public boolean handleChildInline(String var1) {
         return "ProtectionHeader".equals(var1);
      }

      public void parseEndTag(XmlPullParser var1) {
         if ("ProtectionHeader".equals(var1.getName())) {
            this.inProtectionHeader = false;
         }

      }

      public void parseStartTag(XmlPullParser var1) {
         if ("ProtectionHeader".equals(var1.getName())) {
            this.inProtectionHeader = true;
            this.uuid = UUID.fromString(stripCurlyBraces(var1.getAttributeValue((String)null, "SystemID")));
         }

      }

      public void parseText(XmlPullParser var1) {
         if (this.inProtectionHeader) {
            this.initData = Base64.decode(var1.getText(), 0);
         }

      }
   }

   private static class QualityLevelParser extends SsManifestParser.ElementParser {
      private Format format;

      public QualityLevelParser(SsManifestParser.ElementParser var1, String var2) {
         super(var1, var2, "QualityLevel");
      }

      private static List buildCodecSpecificData(String var0) {
         ArrayList var1 = new ArrayList();
         if (!TextUtils.isEmpty(var0)) {
            byte[] var2 = Util.getBytesFromHexString(var0);
            byte[][] var3 = CodecSpecificDataUtil.splitNalUnits(var2);
            if (var3 == null) {
               var1.add(var2);
            } else {
               Collections.addAll(var1, var3);
            }
         }

         return var1;
      }

      private static String fourCCToMimeType(String var0) {
         if (!var0.equalsIgnoreCase("H264") && !var0.equalsIgnoreCase("X264") && !var0.equalsIgnoreCase("AVC1") && !var0.equalsIgnoreCase("DAVC")) {
            if (!var0.equalsIgnoreCase("AAC") && !var0.equalsIgnoreCase("AACL") && !var0.equalsIgnoreCase("AACH") && !var0.equalsIgnoreCase("AACP")) {
               if (!var0.equalsIgnoreCase("TTML") && !var0.equalsIgnoreCase("DFXP")) {
                  if (!var0.equalsIgnoreCase("ac-3") && !var0.equalsIgnoreCase("dac3")) {
                     if (!var0.equalsIgnoreCase("ec-3") && !var0.equalsIgnoreCase("dec3")) {
                        if (var0.equalsIgnoreCase("dtsc")) {
                           return "audio/vnd.dts";
                        } else if (!var0.equalsIgnoreCase("dtsh") && !var0.equalsIgnoreCase("dtsl")) {
                           if (var0.equalsIgnoreCase("dtse")) {
                              return "audio/vnd.dts.hd;profile=lbr";
                           } else {
                              return var0.equalsIgnoreCase("opus") ? "audio/opus" : null;
                           }
                        } else {
                           return "audio/vnd.dts.hd";
                        }
                     } else {
                        return "audio/eac3";
                     }
                  } else {
                     return "audio/ac3";
                  }
               } else {
                  return "application/ttml+xml";
               }
            } else {
               return "audio/mp4a-latm";
            }
         } else {
            return "video/avc";
         }
      }

      public Object build() {
         return this.format;
      }

      public void parseStartTag(XmlPullParser var1) throws ParserException {
         int var2 = (Integer)this.getNormalizedAttribute("Type");
         String var3 = var1.getAttributeValue((String)null, "Index");
         String var4 = (String)this.getNormalizedAttribute("Name");
         int var5 = this.parseRequiredInt(var1, "Bitrate");
         String var6 = fourCCToMimeType(this.parseRequiredString(var1, "FourCC"));
         if (var2 == 2) {
            this.format = Format.createVideoContainerFormat(var3, var4, "video/mp4", var6, (String)null, var5, this.parseRequiredInt(var1, "MaxWidth"), this.parseRequiredInt(var1, "MaxHeight"), -1.0F, buildCodecSpecificData(var1.getAttributeValue((String)null, "CodecPrivateData")), 0);
         } else if (var2 == 1) {
            String var7 = var6;
            if (var6 == null) {
               var7 = "audio/mp4a-latm";
            }

            var2 = this.parseRequiredInt(var1, "Channels");
            int var8 = this.parseRequiredInt(var1, "SamplingRate");
            List var10 = buildCodecSpecificData(var1.getAttributeValue((String)null, "CodecPrivateData"));
            List var9 = var10;
            if (var10.isEmpty()) {
               var9 = var10;
               if ("audio/mp4a-latm".equals(var7)) {
                  var9 = Collections.singletonList(CodecSpecificDataUtil.buildAacLcAudioSpecificConfig(var8, var2));
               }
            }

            this.format = Format.createAudioContainerFormat(var3, var4, "audio/mp4", var7, (String)null, var5, var2, var8, var9, 0, (String)this.getNormalizedAttribute("Language"));
         } else if (var2 == 3) {
            this.format = Format.createTextContainerFormat(var3, var4, "application/mp4", var6, (String)null, var5, 0, (String)this.getNormalizedAttribute("Language"));
         } else {
            this.format = Format.createContainerFormat(var3, var4, "application/mp4", var6, (String)null, var5, 0, (String)null);
         }

      }
   }

   private static class SmoothStreamingMediaParser extends SsManifestParser.ElementParser {
      private long duration;
      private long dvrWindowLength;
      private boolean isLive;
      private int lookAheadCount = -1;
      private int majorVersion;
      private int minorVersion;
      private SsManifest.ProtectionElement protectionElement = null;
      private final List streamElements = new LinkedList();
      private long timescale;

      public SmoothStreamingMediaParser(SsManifestParser.ElementParser var1, String var2) {
         super(var1, var2, "SmoothStreamingMedia");
      }

      public void addChild(Object var1) {
         if (var1 instanceof SsManifest.StreamElement) {
            this.streamElements.add((SsManifest.StreamElement)var1);
         } else if (var1 instanceof SsManifest.ProtectionElement) {
            boolean var2;
            if (this.protectionElement == null) {
               var2 = true;
            } else {
               var2 = false;
            }

            Assertions.checkState(var2);
            this.protectionElement = (SsManifest.ProtectionElement)var1;
         }

      }

      public Object build() {
         SsManifest.StreamElement[] var1 = new SsManifest.StreamElement[this.streamElements.size()];
         this.streamElements.toArray(var1);
         SsManifest.ProtectionElement var2 = this.protectionElement;
         if (var2 != null) {
            DrmInitData var7 = new DrmInitData(new DrmInitData.SchemeData[]{new DrmInitData.SchemeData(var2.uuid, "video/mp4", var2.data)});
            int var3 = var1.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               SsManifest.StreamElement var5 = var1[var4];
               int var6 = var5.type;
               if (var6 == 2 || var6 == 1) {
                  Format[] var8 = var5.formats;

                  for(var6 = 0; var6 < var8.length; ++var6) {
                     var8[var6] = var8[var6].copyWithDrmInitData(var7);
                  }
               }
            }
         }

         return new SsManifest(this.majorVersion, this.minorVersion, this.timescale, this.duration, this.dvrWindowLength, this.lookAheadCount, this.isLive, this.protectionElement, var1);
      }

      public void parseStartTag(XmlPullParser var1) throws ParserException {
         this.majorVersion = this.parseRequiredInt(var1, "MajorVersion");
         this.minorVersion = this.parseRequiredInt(var1, "MinorVersion");
         this.timescale = this.parseLong(var1, "TimeScale", 10000000L);
         this.duration = this.parseRequiredLong(var1, "Duration");
         this.dvrWindowLength = this.parseLong(var1, "DVRWindowLength", 0L);
         this.lookAheadCount = this.parseInt(var1, "LookaheadCount", -1);
         this.isLive = this.parseBoolean(var1, "IsLive", false);
         this.putNormalizedAttribute("TimeScale", this.timescale);
      }
   }

   private static class StreamIndexParser extends SsManifestParser.ElementParser {
      private final String baseUri;
      private int displayHeight;
      private int displayWidth;
      private final List formats;
      private String language;
      private long lastChunkDuration;
      private int maxHeight;
      private int maxWidth;
      private String name;
      private ArrayList startTimes;
      private String subType;
      private long timescale;
      private int type;
      private String url;

      public StreamIndexParser(SsManifestParser.ElementParser var1, String var2) {
         super(var1, var2, "StreamIndex");
         this.baseUri = var2;
         this.formats = new LinkedList();
      }

      private void parseStreamElementStartTag(XmlPullParser var1) throws ParserException {
         this.type = this.parseType(var1);
         this.putNormalizedAttribute("Type", this.type);
         if (this.type == 3) {
            this.subType = this.parseRequiredString(var1, "Subtype");
         } else {
            this.subType = var1.getAttributeValue((String)null, "Subtype");
         }

         this.name = var1.getAttributeValue((String)null, "Name");
         this.url = this.parseRequiredString(var1, "Url");
         this.maxWidth = this.parseInt(var1, "MaxWidth", -1);
         this.maxHeight = this.parseInt(var1, "MaxHeight", -1);
         this.displayWidth = this.parseInt(var1, "DisplayWidth", -1);
         this.displayHeight = this.parseInt(var1, "DisplayHeight", -1);
         this.language = var1.getAttributeValue((String)null, "Language");
         this.putNormalizedAttribute("Language", this.language);
         this.timescale = (long)this.parseInt(var1, "TimeScale", -1);
         if (this.timescale == -1L) {
            this.timescale = (Long)this.getNormalizedAttribute("TimeScale");
         }

         this.startTimes = new ArrayList();
      }

      private void parseStreamFragmentStartTag(XmlPullParser var1) throws ParserException {
         int var2 = this.startTimes.size();
         long var3 = this.parseLong(var1, "t", -9223372036854775807L);
         byte var5 = 1;
         long var6 = var3;
         if (var3 == -9223372036854775807L) {
            if (var2 == 0) {
               var6 = 0L;
            } else {
               if (this.lastChunkDuration == -1L) {
                  throw new ParserException("Unable to infer start time");
               }

               var6 = (Long)this.startTimes.get(var2 - 1) + this.lastChunkDuration;
            }
         }

         this.startTimes.add(var6);
         this.lastChunkDuration = this.parseLong(var1, "d", -9223372036854775807L);
         var3 = this.parseLong(var1, "r", 1L);
         var2 = var5;
         if (var3 > 1L) {
            if (this.lastChunkDuration == -9223372036854775807L) {
               throw new ParserException("Repeated chunk with unspecified duration");
            }

            var2 = var5;
         }

         while(true) {
            long var8 = (long)var2;
            if (var8 >= var3) {
               return;
            }

            this.startTimes.add(this.lastChunkDuration * var8 + var6);
            ++var2;
         }
      }

      private int parseType(XmlPullParser var1) throws ParserException {
         String var3 = var1.getAttributeValue((String)null, "Type");
         if (var3 != null) {
            if ("audio".equalsIgnoreCase(var3)) {
               return 1;
            } else if ("video".equalsIgnoreCase(var3)) {
               return 2;
            } else if ("text".equalsIgnoreCase(var3)) {
               return 3;
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("Invalid key value[");
               var2.append(var3);
               var2.append("]");
               throw new ParserException(var2.toString());
            }
         } else {
            throw new SsManifestParser.MissingFieldException("Type");
         }
      }

      public void addChild(Object var1) {
         if (var1 instanceof Format) {
            this.formats.add((Format)var1);
         }

      }

      public Object build() {
         Format[] var1 = new Format[this.formats.size()];
         this.formats.toArray(var1);
         return new SsManifest.StreamElement(this.baseUri, this.url, this.type, this.subType, this.timescale, this.name, this.maxWidth, this.maxHeight, this.displayWidth, this.displayHeight, this.language, var1, this.startTimes, this.lastChunkDuration);
      }

      public boolean handleChildInline(String var1) {
         return "c".equals(var1);
      }

      public void parseStartTag(XmlPullParser var1) throws ParserException {
         if ("c".equals(var1.getName())) {
            this.parseStreamFragmentStartTag(var1);
         } else {
            this.parseStreamElementStartTag(var1);
         }

      }
   }
}
