package com.google.android.exoplayer2.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class XmlPullParserUtil {
   private XmlPullParserUtil() {
   }

   public static String getAttributeValue(XmlPullParser var0, String var1) {
      int var2 = var0.getAttributeCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var0.getAttributeName(var3).equals(var1)) {
            return var0.getAttributeValue(var3);
         }
      }

      return null;
   }

   public static String getAttributeValueIgnorePrefix(XmlPullParser var0, String var1) {
      int var2 = var0.getAttributeCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (stripPrefix(var0.getAttributeName(var3)).equals(var1)) {
            return var0.getAttributeValue(var3);
         }
      }

      return null;
   }

   public static boolean isEndTag(XmlPullParser var0) throws XmlPullParserException {
      boolean var1;
      if (var0.getEventType() == 3) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isEndTag(XmlPullParser var0, String var1) throws XmlPullParserException {
      boolean var2;
      if (isEndTag(var0) && var0.getName().equals(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static boolean isStartTag(XmlPullParser var0) throws XmlPullParserException {
      boolean var1;
      if (var0.getEventType() == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isStartTag(XmlPullParser var0, String var1) throws XmlPullParserException {
      boolean var2;
      if (isStartTag(var0) && var0.getName().equals(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static boolean isStartTagIgnorePrefix(XmlPullParser var0, String var1) throws XmlPullParserException {
      boolean var2;
      if (isStartTag(var0) && stripPrefix(var0.getName()).equals(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static String stripPrefix(String var0) {
      int var1 = var0.indexOf(58);
      if (var1 != -1) {
         var0 = var0.substring(var1 + 1);
      }

      return var0;
   }
}
