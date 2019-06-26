package com.google.android.exoplayer2.text.ttml;

import android.text.Layout.Alignment;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.ColorParser;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public final class TtmlDecoder extends SimpleSubtitleDecoder {
   private static final Pattern CELL_RESOLUTION = Pattern.compile("^(\\d+) (\\d+)$");
   private static final Pattern CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
   private static final TtmlDecoder.CellResolution DEFAULT_CELL_RESOLUTION = new TtmlDecoder.CellResolution(32, 15);
   private static final TtmlDecoder.FrameAndTickRate DEFAULT_FRAME_AND_TICK_RATE = new TtmlDecoder.FrameAndTickRate(30.0F, 1, 1);
   private static final Pattern FONT_SIZE = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");
   private static final Pattern OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
   private static final Pattern PERCENTAGE_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)% (\\d+\\.?\\d*?)%$");
   private static final Pattern PIXEL_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)px (\\d+\\.?\\d*?)px$");
   private final XmlPullParserFactory xmlParserFactory;

   public TtmlDecoder() {
      super("TtmlDecoder");

      try {
         this.xmlParserFactory = XmlPullParserFactory.newInstance();
         this.xmlParserFactory.setNamespaceAware(true);
      } catch (XmlPullParserException var2) {
         throw new RuntimeException("Couldn't create XmlPullParserFactory instance", var2);
      }
   }

   private TtmlStyle createIfNull(TtmlStyle var1) {
      TtmlStyle var2 = var1;
      if (var1 == null) {
         var2 = new TtmlStyle();
      }

      return var2;
   }

   private static boolean isSupportedTag(String var0) {
      boolean var1;
      if (!var0.equals("tt") && !var0.equals("head") && !var0.equals("body") && !var0.equals("div") && !var0.equals("p") && !var0.equals("span") && !var0.equals("br") && !var0.equals("style") && !var0.equals("styling") && !var0.equals("layout") && !var0.equals("region") && !var0.equals("metadata") && !var0.equals("image") && !var0.equals("data") && !var0.equals("information")) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private TtmlDecoder.CellResolution parseCellResolution(XmlPullParser var1, TtmlDecoder.CellResolution var2) throws SubtitleDecoderException {
      String var10 = var1.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "cellResolution");
      if (var10 == null) {
         return var2;
      } else {
         Matcher var3 = CELL_RESOLUTION.matcher(var10);
         StringBuilder var11;
         if (!var3.matches()) {
            var11 = new StringBuilder();
            var11.append("Ignoring malformed cell resolution: ");
            var11.append(var10);
            Log.w("TtmlDecoder", var11.toString());
            return var2;
         } else {
            label39: {
               boolean var10001;
               int var4;
               int var5;
               try {
                  var4 = Integer.parseInt(var3.group(1));
                  var5 = Integer.parseInt(var3.group(2));
               } catch (NumberFormatException var9) {
                  var10001 = false;
                  break label39;
               }

               if (var4 != 0 && var5 != 0) {
                  try {
                     return new TtmlDecoder.CellResolution(var4, var5);
                  } catch (NumberFormatException var7) {
                     var10001 = false;
                  }
               } else {
                  try {
                     var11 = new StringBuilder();
                     var11.append("Invalid cell resolution ");
                     var11.append(var4);
                     var11.append(" ");
                     var11.append(var5);
                     SubtitleDecoderException var6 = new SubtitleDecoderException(var11.toString());
                     throw var6;
                  } catch (NumberFormatException var8) {
                     var10001 = false;
                  }
               }
            }

            var11 = new StringBuilder();
            var11.append("Ignoring malformed cell resolution: ");
            var11.append(var10);
            Log.w("TtmlDecoder", var11.toString());
            return var2;
         }
      }
   }

   private static void parseFontSize(String var0, TtmlStyle var1) throws SubtitleDecoderException {
      String[] var2 = Util.split(var0, "\\s+");
      Matcher var7;
      if (var2.length == 1) {
         var7 = FONT_SIZE.matcher(var0);
      } else {
         if (var2.length != 2) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Invalid number of entries for fontSize: ");
            var5.append(var2.length);
            var5.append(".");
            throw new SubtitleDecoderException(var5.toString());
         }

         var7 = FONT_SIZE.matcher(var2[1]);
         Log.w("TtmlDecoder", "Multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.");
      }

      StringBuilder var6;
      if (var7.matches()) {
         var0 = var7.group(3);
         byte var3 = -1;
         int var4 = var0.hashCode();
         if (var4 != 37) {
            if (var4 != 3240) {
               if (var4 == 3592 && var0.equals("px")) {
                  var3 = 0;
               }
            } else if (var0.equals("em")) {
               var3 = 1;
            }
         } else if (var0.equals("%")) {
            var3 = 2;
         }

         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 != 2) {
                  var6 = new StringBuilder();
                  var6.append("Invalid unit for fontSize: '");
                  var6.append(var0);
                  var6.append("'.");
                  throw new SubtitleDecoderException(var6.toString());
               }

               var1.setFontSizeUnit(3);
            } else {
               var1.setFontSizeUnit(2);
            }
         } else {
            var1.setFontSizeUnit(1);
         }

         var1.setFontSize(Float.valueOf(var7.group(1)));
      } else {
         var6 = new StringBuilder();
         var6.append("Invalid expression for fontSize: '");
         var6.append(var0);
         var6.append("'.");
         throw new SubtitleDecoderException(var6.toString());
      }
   }

   private TtmlDecoder.FrameAndTickRate parseFrameAndTickRates(XmlPullParser var1) throws SubtitleDecoderException {
      String var2 = var1.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "frameRate");
      int var3;
      if (var2 != null) {
         var3 = Integer.parseInt(var2);
      } else {
         var3 = 30;
      }

      float var4 = 1.0F;
      var2 = var1.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "frameRateMultiplier");
      if (var2 != null) {
         String[] var8 = Util.split(var2, " ");
         if (var8.length != 2) {
            throw new SubtitleDecoderException("frameRateMultiplier doesn't have 2 parts");
         }

         var4 = (float)Integer.parseInt(var8[0]) / (float)Integer.parseInt(var8[1]);
      }

      int var5 = DEFAULT_FRAME_AND_TICK_RATE.subFrameRate;
      var2 = var1.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "subFrameRate");
      if (var2 != null) {
         var5 = Integer.parseInt(var2);
      }

      int var6 = DEFAULT_FRAME_AND_TICK_RATE.tickRate;
      String var7 = var1.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "tickRate");
      if (var7 != null) {
         var6 = Integer.parseInt(var7);
      }

      return new TtmlDecoder.FrameAndTickRate((float)var3 * var4, var5, var6);
   }

   private Map parseHeader(XmlPullParser var1, Map var2, TtmlDecoder.CellResolution var3, TtmlDecoder.TtsExtent var4, Map var5, Map var6) throws IOException, XmlPullParserException {
      do {
         var1.next();
         if (XmlPullParserUtil.isStartTag(var1, "style")) {
            String var7 = XmlPullParserUtil.getAttributeValue(var1, "style");
            TtmlStyle var8 = this.parseStyleAttributes(var1, new TtmlStyle());
            if (var7 != null) {
               String[] var11 = this.parseStyleIds(var7);
               int var9 = var11.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  var8.chain((TtmlStyle)var2.get(var11[var10]));
               }
            }

            if (var8.getId() != null) {
               var2.put(var8.getId(), var8);
            }
         } else if (XmlPullParserUtil.isStartTag(var1, "region")) {
            TtmlRegion var12 = this.parseRegionAttributes(var1, var3, var4);
            if (var12 != null) {
               var5.put(var12.id, var12);
            }
         } else if (XmlPullParserUtil.isStartTag(var1, "metadata")) {
            this.parseMetadata(var1, var6);
         }
      } while(!XmlPullParserUtil.isEndTag(var1, "head"));

      return var2;
   }

   private void parseMetadata(XmlPullParser var1, Map var2) throws IOException, XmlPullParserException {
      do {
         var1.next();
         if (XmlPullParserUtil.isStartTag(var1, "image")) {
            String var3 = XmlPullParserUtil.getAttributeValue(var1, "id");
            if (var3 != null) {
               var2.put(var3, var1.nextText());
            }
         }
      } while(!XmlPullParserUtil.isEndTag(var1, "metadata"));

   }

   private TtmlNode parseNode(XmlPullParser var1, TtmlNode var2, Map var3, TtmlDecoder.FrameAndTickRate var4) throws SubtitleDecoderException {
      int var5 = var1.getAttributeCount();
      TtmlStyle var6 = this.parseStyleAttributes(var1, (TtmlStyle)null);
      String var7 = null;
      Object var8 = var7;
      String var9 = "";
      int var10 = 0;
      long var11 = -9223372036854775807L;
      long var13 = -9223372036854775807L;

      long var15;
      long var20;
      long var22;
      long var24;
      Object var27;
      for(var15 = -9223372036854775807L; var10 < var5; var8 = var27) {
         String var17;
         String var18;
         byte var19;
         label90: {
            var17 = var1.getAttributeName(var10);
            var18 = var1.getAttributeValue(var10);
            switch(var17.hashCode()) {
            case -934795532:
               if (var17.equals("region")) {
                  var19 = 4;
                  break label90;
               }
               break;
            case 99841:
               if (var17.equals("dur")) {
                  var19 = 2;
                  break label90;
               }
               break;
            case 100571:
               if (var17.equals("end")) {
                  var19 = 1;
                  break label90;
               }
               break;
            case 93616297:
               if (var17.equals("begin")) {
                  var19 = 0;
                  break label90;
               }
               break;
            case 109780401:
               if (var17.equals("style")) {
                  var19 = 3;
                  break label90;
               }
               break;
            case 1292595405:
               if (var17.equals("backgroundImage")) {
                  var19 = 5;
                  break label90;
               }
            }

            var19 = -1;
         }

         String var26;
         if (var19 != 0) {
            if (var19 != 1) {
               if (var19 != 2) {
                  if (var19 != 3) {
                     if (var19 != 4) {
                        if (var19 == 5 && var18.startsWith("#")) {
                           var7 = var18.substring(1);
                        }

                        var20 = var11;
                        var22 = var13;
                        var24 = var15;
                        var17 = var9;
                        var26 = var7;
                        var27 = var8;
                     } else {
                        var20 = var11;
                        var22 = var13;
                        var24 = var15;
                        var17 = var9;
                        var26 = var7;
                        var27 = var8;
                        if (var3.containsKey(var18)) {
                           var17 = var18;
                           var20 = var11;
                           var22 = var13;
                           var24 = var15;
                           var26 = var7;
                           var27 = var8;
                        }
                     }
                  } else {
                     String[] var30 = this.parseStyleIds(var18);
                     var20 = var11;
                     var22 = var13;
                     var24 = var15;
                     var17 = var9;
                     var26 = var7;
                     var27 = var8;
                     if (var30.length > 0) {
                        var27 = var30;
                        var20 = var11;
                        var22 = var13;
                        var24 = var15;
                        var17 = var9;
                        var26 = var7;
                     }
                  }
               } else {
                  var24 = parseTimeExpression(var18, var4);
                  var20 = var11;
                  var22 = var13;
                  var17 = var9;
                  var26 = var7;
                  var27 = var8;
               }
            } else {
               var22 = parseTimeExpression(var18, var4);
               var20 = var11;
               var24 = var15;
               var17 = var9;
               var26 = var7;
               var27 = var8;
            }
         } else {
            var20 = parseTimeExpression(var18, var4);
            var27 = var8;
            var26 = var7;
            var17 = var9;
            var24 = var15;
            var22 = var13;
         }

         ++var10;
         var11 = var20;
         var13 = var22;
         var15 = var24;
         var9 = var17;
         var7 = var26;
      }

      if (var2 != null) {
         long var28 = var2.startTimeUs;
         var24 = var11;
         var22 = var13;
         if (var28 != -9223372036854775807L) {
            var20 = var11;
            if (var11 != -9223372036854775807L) {
               var20 = var11 + var28;
            }

            var24 = var20;
            var22 = var13;
            if (var13 != -9223372036854775807L) {
               var22 = var13 + var2.startTimeUs;
               var24 = var20;
            }
         }
      } else {
         var22 = var13;
         var24 = var11;
      }

      if (var22 == -9223372036854775807L) {
         if (var15 != -9223372036854775807L) {
            var11 = var15 + var24;
            return TtmlNode.buildNode(var1.getName(), var24, var11, var6, (String[])var8, var9, var7);
         }

         if (var2 != null) {
            var11 = var2.endTimeUs;
            if (var11 != -9223372036854775807L) {
               return TtmlNode.buildNode(var1.getName(), var24, var11, var6, (String[])var8, var9, var7);
            }
         }
      }

      var11 = var22;
      return TtmlNode.buildNode(var1.getName(), var24, var11, var6, (String[])var8, var9, var7);
   }

   private TtmlRegion parseRegionAttributes(XmlPullParser var1, TtmlDecoder.CellResolution var2, TtmlDecoder.TtsExtent var3) {
      String var4 = XmlPullParserUtil.getAttributeValue(var1, "id");
      if (var4 == null) {
         return null;
      } else {
         String var5 = XmlPullParserUtil.getAttributeValue(var1, "origin");
         if (var5 == null) {
            Log.w("TtmlDecoder", "Ignoring region without an origin");
            return null;
         } else {
            StringBuilder var20;
            label133: {
               Matcher var6 = PERCENTAGE_COORDINATES.matcher(var5);
               Matcher var7 = PIXEL_COORDINATES.matcher(var5);
               float var8;
               float var9;
               int var10;
               int var11;
               boolean var10001;
               if (var6.matches()) {
                  try {
                     var8 = Float.parseFloat(var6.group(1)) / 100.0F;
                     var9 = Float.parseFloat(var6.group(2));
                  } catch (NumberFormatException var15) {
                     var20 = new StringBuilder();
                     var20.append("Ignoring region with malformed origin: ");
                     var20.append(var5);
                     Log.w("TtmlDecoder", var20.toString());
                     return null;
                  }

                  var9 /= 100.0F;
               } else {
                  if (!var7.matches()) {
                     var20 = new StringBuilder();
                     var20.append("Ignoring region with unsupported origin: ");
                     var20.append(var5);
                     Log.w("TtmlDecoder", var20.toString());
                     return null;
                  }

                  if (var3 == null) {
                     var20 = new StringBuilder();
                     var20.append("Ignoring region with missing tts:extent: ");
                     var20.append(var5);
                     Log.w("TtmlDecoder", var20.toString());
                     return null;
                  }

                  try {
                     var10 = Integer.parseInt(var7.group(1));
                     var11 = Integer.parseInt(var7.group(2));
                     var8 = (float)var10 / (float)var3.width;
                  } catch (NumberFormatException var19) {
                     var10001 = false;
                     break label133;
                  }

                  var9 = (float)var11;

                  try {
                     var11 = var3.height;
                  } catch (NumberFormatException var18) {
                     var10001 = false;
                     break label133;
                  }

                  var9 /= (float)var11;
               }

               String var22 = XmlPullParserUtil.getAttributeValue(var1, "extent");
               if (var22 == null) {
                  Log.w("TtmlDecoder", "Ignoring region without an extent");
                  return null;
               }

               var7 = PERCENTAGE_COORDINATES.matcher(var22);
               var6 = PIXEL_COORDINATES.matcher(var22);
               float var12;
               float var13;
               if (var7.matches()) {
                  try {
                     var12 = Float.parseFloat(var7.group(1)) / 100.0F;
                     var13 = Float.parseFloat(var7.group(2));
                  } catch (NumberFormatException var14) {
                     var20 = new StringBuilder();
                     var20.append("Ignoring region with malformed extent: ");
                     var20.append(var5);
                     Log.w("TtmlDecoder", var20.toString());
                     return null;
                  }

                  var13 /= 100.0F;
               } else {
                  if (!var6.matches()) {
                     var20 = new StringBuilder();
                     var20.append("Ignoring region with unsupported extent: ");
                     var20.append(var5);
                     Log.w("TtmlDecoder", var20.toString());
                     return null;
                  }

                  if (var3 == null) {
                     var20 = new StringBuilder();
                     var20.append("Ignoring region with missing tts:extent: ");
                     var20.append(var5);
                     Log.w("TtmlDecoder", var20.toString());
                     return null;
                  }

                  label92: {
                     label121: {
                        try {
                           var10 = Integer.parseInt(var6.group(1));
                           var11 = Integer.parseInt(var6.group(2));
                           var12 = (float)var10 / (float)var3.width;
                        } catch (NumberFormatException var17) {
                           var10001 = false;
                           break label121;
                        }

                        var13 = (float)var11;

                        try {
                           var11 = var3.height;
                           break label92;
                        } catch (NumberFormatException var16) {
                           var10001 = false;
                        }
                     }

                     var20 = new StringBuilder();
                     var20.append("Ignoring region with malformed extent: ");
                     var20.append(var5);
                     Log.w("TtmlDecoder", var20.toString());
                     return null;
                  }

                  var13 /= (float)var11;
               }

               String var21 = XmlPullParserUtil.getAttributeValue(var1, "displayAlign");
               byte var24;
               if (var21 != null) {
                  var21 = Util.toLowerInvariant(var21);
                  byte var23 = -1;
                  var10 = var21.hashCode();
                  if (var10 != -1364013995) {
                     if (var10 == 92734940 && var21.equals("after")) {
                        var23 = 1;
                     }
                  } else if (var21.equals("center")) {
                     var23 = 0;
                  }

                  if (var23 == 0) {
                     var9 += var13 / 2.0F;
                     var24 = 1;
                     return new TtmlRegion(var4, var8, var9, 0, var24, var12, 1, 1.0F / (float)var2.rows);
                  }

                  if (var23 == 1) {
                     var9 += var13;
                     var24 = 2;
                     return new TtmlRegion(var4, var8, var9, 0, var24, var12, 1, 1.0F / (float)var2.rows);
                  }
               }

               var24 = 0;
               return new TtmlRegion(var4, var8, var9, 0, var24, var12, 1, 1.0F / (float)var2.rows);
            }

            var20 = new StringBuilder();
            var20.append("Ignoring region with malformed origin: ");
            var20.append(var5);
            Log.w("TtmlDecoder", var20.toString());
            return null;
         }
      }
   }

   private TtmlStyle parseStyleAttributes(XmlPullParser var1, TtmlStyle var2) {
      int var3 = var1.getAttributeCount();
      int var4 = 0;

      TtmlStyle var5;
      for(var5 = var2; var4 < var3; var5 = var2) {
         String var6;
         byte var8;
         String var13;
         byte var15;
         label140: {
            var6 = var1.getAttributeValue(var4);
            var13 = var1.getAttributeName(var4);
            int var7 = var13.hashCode();
            var8 = -1;
            switch(var7) {
            case -1550943582:
               if (var13.equals("fontStyle")) {
                  var15 = 6;
                  break label140;
               }
               break;
            case -1224696685:
               if (var13.equals("fontFamily")) {
                  var15 = 3;
                  break label140;
               }
               break;
            case -1065511464:
               if (var13.equals("textAlign")) {
                  var15 = 7;
                  break label140;
               }
               break;
            case -879295043:
               if (var13.equals("textDecoration")) {
                  var15 = 8;
                  break label140;
               }
               break;
            case -734428249:
               if (var13.equals("fontWeight")) {
                  var15 = 5;
                  break label140;
               }
               break;
            case 3355:
               if (var13.equals("id")) {
                  var15 = 0;
                  break label140;
               }
               break;
            case 94842723:
               if (var13.equals("color")) {
                  var15 = 2;
                  break label140;
               }
               break;
            case 365601008:
               if (var13.equals("fontSize")) {
                  var15 = 4;
                  break label140;
               }
               break;
            case 1287124693:
               if (var13.equals("backgroundColor")) {
                  var15 = 1;
                  break label140;
               }
            }

            var15 = -1;
         }

         StringBuilder var14;
         switch(var15) {
         case 0:
            var2 = var5;
            if ("style".equals(var1.getName())) {
               var2 = this.createIfNull(var5);
               var2.setId(var6);
            }
            break;
         case 1:
            var2 = this.createIfNull(var5);

            try {
               var2.setBackgroundColor(ColorParser.parseTtmlColor(var6));
            } catch (IllegalArgumentException var9) {
               var14 = new StringBuilder();
               var14.append("Failed parsing background value: ");
               var14.append(var6);
               Log.w("TtmlDecoder", var14.toString());
            }
            break;
         case 2:
            var2 = this.createIfNull(var5);

            try {
               var2.setFontColor(ColorParser.parseTtmlColor(var6));
            } catch (IllegalArgumentException var10) {
               var14 = new StringBuilder();
               var14.append("Failed parsing color value: ");
               var14.append(var6);
               Log.w("TtmlDecoder", var14.toString());
            }
            break;
         case 3:
            var2 = this.createIfNull(var5);
            var2.setFontFamily(var6);
            break;
         case 4:
            var2 = var5;

            label150: {
               label160: {
                  boolean var10001;
                  try {
                     var5 = this.createIfNull(var5);
                  } catch (SubtitleDecoderException var12) {
                     var10001 = false;
                     break label160;
                  }

                  var2 = var5;

                  try {
                     parseFontSize(var6, var5);
                     break label150;
                  } catch (SubtitleDecoderException var11) {
                     var10001 = false;
                  }
               }

               var14 = new StringBuilder();
               var14.append("Failed parsing fontSize value: ");
               var14.append(var6);
               Log.w("TtmlDecoder", var14.toString());
               break;
            }

            var2 = var5;
            break;
         case 5:
            var2 = this.createIfNull(var5);
            var2.setBold("bold".equalsIgnoreCase(var6));
            break;
         case 6:
            var2 = this.createIfNull(var5);
            var2.setItalic("italic".equalsIgnoreCase(var6));
            break;
         case 7:
            var13 = Util.toLowerInvariant(var6);
            switch(var13.hashCode()) {
            case -1364013995:
               var15 = var8;
               if (var13.equals("center")) {
                  var15 = 4;
               }
               break;
            case 100571:
               var15 = var8;
               if (var13.equals("end")) {
                  var15 = 3;
               }
               break;
            case 3317767:
               var15 = var8;
               if (var13.equals("left")) {
                  var15 = 0;
               }
               break;
            case 108511772:
               var15 = var8;
               if (var13.equals("right")) {
                  var15 = 2;
               }
               break;
            case 109757538:
               var15 = var8;
               if (var13.equals("start")) {
                  var15 = 1;
               }
               break;
            default:
               var15 = var8;
            }

            if (var15 != 0) {
               if (var15 != 1) {
                  if (var15 != 2) {
                     if (var15 != 3) {
                        if (var15 != 4) {
                           var2 = var5;
                        } else {
                           var2 = this.createIfNull(var5);
                           var2.setTextAlign(Alignment.ALIGN_CENTER);
                        }
                     } else {
                        var2 = this.createIfNull(var5);
                        var2.setTextAlign(Alignment.ALIGN_OPPOSITE);
                     }
                  } else {
                     var2 = this.createIfNull(var5);
                     var2.setTextAlign(Alignment.ALIGN_OPPOSITE);
                  }
               } else {
                  var2 = this.createIfNull(var5);
                  var2.setTextAlign(Alignment.ALIGN_NORMAL);
               }
            } else {
               var2 = this.createIfNull(var5);
               var2.setTextAlign(Alignment.ALIGN_NORMAL);
            }
            break;
         case 8:
            var13 = Util.toLowerInvariant(var6);
            switch(var13.hashCode()) {
            case -1461280213:
               var15 = var8;
               if (var13.equals("nounderline")) {
                  var15 = 3;
               }
               break;
            case -1026963764:
               var15 = var8;
               if (var13.equals("underline")) {
                  var15 = 2;
               }
               break;
            case 913457136:
               var15 = var8;
               if (var13.equals("nolinethrough")) {
                  var15 = 1;
               }
               break;
            case 1679736913:
               var15 = var8;
               if (var13.equals("linethrough")) {
                  var15 = 0;
               }
               break;
            default:
               var15 = var8;
            }

            if (var15 != 0) {
               if (var15 != 1) {
                  if (var15 != 2) {
                     if (var15 != 3) {
                        var2 = var5;
                     } else {
                        var2 = this.createIfNull(var5);
                        var2.setUnderline(false);
                     }
                  } else {
                     var2 = this.createIfNull(var5);
                     var2.setUnderline(true);
                  }
               } else {
                  var2 = this.createIfNull(var5);
                  var2.setLinethrough(false);
               }
            } else {
               var2 = this.createIfNull(var5);
               var2.setLinethrough(true);
            }
            break;
         default:
            var2 = var5;
         }

         ++var4;
      }

      return var5;
   }

   private String[] parseStyleIds(String var1) {
      var1 = var1.trim();
      String[] var2;
      if (var1.isEmpty()) {
         var2 = new String[0];
      } else {
         var2 = Util.split(var1, "\\s+");
      }

      return var2;
   }

   private static long parseTimeExpression(String var0, TtmlDecoder.FrameAndTickRate var1) throws SubtitleDecoderException {
      Matcher var2 = CLOCK_TIME.matcher(var0);
      double var11;
      double var13;
      if (var2.matches()) {
         double var3 = (double)(Long.parseLong(var2.group(1)) * 3600L);
         double var5 = (double)(Long.parseLong(var2.group(2)) * 60L);
         Double.isNaN(var3);
         Double.isNaN(var5);
         double var7 = (double)Long.parseLong(var2.group(3));
         Double.isNaN(var7);
         var0 = var2.group(4);
         double var9 = 0.0D;
         if (var0 != null) {
            var11 = Double.parseDouble(var0);
         } else {
            var11 = 0.0D;
         }

         var0 = var2.group(5);
         if (var0 != null) {
            var13 = (double)((float)Long.parseLong(var0) / var1.effectiveFrameRate);
         } else {
            var13 = 0.0D;
         }

         var0 = var2.group(6);
         if (var0 != null) {
            double var15 = (double)Long.parseLong(var0);
            var9 = (double)var1.subFrameRate;
            Double.isNaN(var15);
            Double.isNaN(var9);
            var15 /= var9;
            var9 = (double)var1.effectiveFrameRate;
            Double.isNaN(var9);
            var9 = var15 / var9;
         }

         return (long)((var3 + var5 + var7 + var11 + var13 + var9) * 1000000.0D);
      } else {
         var2 = OFFSET_TIME.matcher(var0);
         if (!var2.matches()) {
            StringBuilder var18 = new StringBuilder();
            var18.append("Malformed time expression: ");
            var18.append(var0);
            throw new SubtitleDecoderException(var18.toString());
         } else {
            byte var19;
            label84: {
               var13 = Double.parseDouble(var2.group(1));
               var0 = var2.group(2);
               int var17 = var0.hashCode();
               if (var17 != 102) {
                  if (var17 != 104) {
                     if (var17 != 109) {
                        if (var17 != 3494) {
                           if (var17 != 115) {
                              if (var17 == 116 && var0.equals("t")) {
                                 var19 = 5;
                                 break label84;
                              }
                           } else if (var0.equals("s")) {
                              var19 = 2;
                              break label84;
                           }
                        } else if (var0.equals("ms")) {
                           var19 = 3;
                           break label84;
                        }
                     } else if (var0.equals("m")) {
                        var19 = 1;
                        break label84;
                     }
                  } else if (var0.equals("h")) {
                     var19 = 0;
                     break label84;
                  }
               } else if (var0.equals("f")) {
                  var19 = 4;
                  break label84;
               }

               var19 = -1;
            }

            if (var19 != 0) {
               if (var19 != 1) {
                  var11 = var13;
                  if (var19 != 2) {
                     if (var19 != 3) {
                        if (var19 != 4) {
                           if (var19 != 5) {
                              var11 = var13;
                              return (long)(var11 * 1000000.0D);
                           }

                           var11 = (double)var1.tickRate;
                           Double.isNaN(var11);
                        } else {
                           var11 = (double)var1.effectiveFrameRate;
                           Double.isNaN(var11);
                        }
                     } else {
                        var11 = 1000.0D;
                     }

                     var11 = var13 / var11;
                  }

                  return (long)(var11 * 1000000.0D);
               }

               var11 = 60.0D;
            } else {
               var11 = 3600.0D;
            }

            var11 = var13 * var11;
            return (long)(var11 * 1000000.0D);
         }
      }
   }

   private TtmlDecoder.TtsExtent parseTtsExtent(XmlPullParser var1) {
      String var4 = XmlPullParserUtil.getAttributeValue(var1, "extent");
      if (var4 == null) {
         return null;
      } else {
         Matcher var2 = PIXEL_COORDINATES.matcher(var4);
         StringBuilder var5;
         if (!var2.matches()) {
            var5 = new StringBuilder();
            var5.append("Ignoring non-pixel tts extent: ");
            var5.append(var4);
            Log.w("TtmlDecoder", var5.toString());
            return null;
         } else {
            try {
               TtmlDecoder.TtsExtent var6 = new TtmlDecoder.TtsExtent(Integer.parseInt(var2.group(1)), Integer.parseInt(var2.group(2)));
               return var6;
            } catch (NumberFormatException var3) {
               var5 = new StringBuilder();
               var5.append("Ignoring malformed tts extent: ");
               var5.append(var4);
               Log.w("TtmlDecoder", var5.toString());
               return null;
            }
         }
      }
   }

   protected TtmlSubtitle decode(byte[] param1, int param2, boolean param3) throws SubtitleDecoderException {
      // $FF: Couldn't be decompiled
   }

   private static final class CellResolution {
      final int columns;
      final int rows;

      CellResolution(int var1, int var2) {
         this.columns = var1;
         this.rows = var2;
      }
   }

   private static final class FrameAndTickRate {
      final float effectiveFrameRate;
      final int subFrameRate;
      final int tickRate;

      FrameAndTickRate(float var1, int var2, int var3) {
         this.effectiveFrameRate = var1;
         this.subFrameRate = var2;
         this.tickRate = var3;
      }
   }

   private static final class TtsExtent {
      final int height;
      final int width;

      TtsExtent(int var1, int var2) {
         this.width = var1;
         this.height = var2;
      }
   }
}
