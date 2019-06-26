package org.mapsforge.map.rendertheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.mapsforge.map.graphics.Bitmap;
import org.xml.sax.SAXException;

public final class XmlUtils {
   private static final String PREFIX_FILE = "file:";
   private static final String PREFIX_JAR = "jar:";

   private XmlUtils() {
      throw new IllegalStateException();
   }

   private static void checkForNegativeValue(String var0, float var1) throws SAXException {
      if (var1 < 0.0F) {
         throw new SAXException("Attribute '" + var0 + "' must not be negative: " + var1);
      }
   }

   public static void checkMandatoryAttribute(String var0, String var1, Object var2) throws SAXException {
      if (var2 == null) {
         throw new SAXException("missing attribute '" + var1 + "' for element: " + var0);
      }
   }

   public static Bitmap createBitmap(GraphicAdapter var0, String var1, String var2) throws IOException {
      Bitmap var3;
      if (var2 != null && var2.length() != 0) {
         InputStream var4 = createInputStream(var1, var2);
         var3 = var0.decodeStream(var4);
         var4.close();
      } else {
         var3 = null;
      }

      return var3;
   }

   private static InputStream createInputStream(String var0, String var1) throws FileNotFoundException {
      Object var3;
      if (var1.startsWith("jar:")) {
         String var2 = getAbsoluteName(var0, var1.substring("jar:".length()));
         InputStream var5 = XmlUtils.class.getResourceAsStream(var2);
         var3 = var5;
         if (var5 == null) {
            throw new FileNotFoundException("resource not found: " + var2);
         }
      } else {
         if (!var1.startsWith("file:")) {
            throw new FileNotFoundException("invalid bitmap source: " + var1);
         }

         File var4 = getFile(var0, var1.substring("file:".length()));
         if (!var4.exists()) {
            throw new FileNotFoundException("file does not exist: " + var4.getAbsolutePath());
         }

         if (!var4.isFile()) {
            throw new FileNotFoundException("not a file: " + var4.getAbsolutePath());
         }

         if (!var4.canRead()) {
            throw new FileNotFoundException("cannot read file: " + var4.getAbsolutePath());
         }

         var3 = new FileInputStream(var4);
      }

      return (InputStream)var3;
   }

   public static SAXException createSAXException(String var0, String var1, String var2, int var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append("unknown attribute (");
      var4.append(var3);
      var4.append(") in element '");
      var4.append(var0);
      var4.append("': ");
      var4.append(var1);
      var4.append('=');
      var4.append(var2);
      return new SAXException(var4.toString());
   }

   private static String getAbsoluteName(String var0, String var1) {
      if (var1.charAt(0) != '/') {
         var1 = var0 + var1;
      }

      return var1;
   }

   private static File getFile(String var0, String var1) {
      File var2;
      if (var1.charAt(0) == File.separatorChar) {
         var2 = new File(var1);
      } else {
         var2 = new File(var0, var1);
      }

      return var2;
   }

   public static byte parseNonNegativeByte(String var0, String var1) throws SAXException {
      byte var2 = Byte.parseByte(var1);
      checkForNegativeValue(var0, (float)var2);
      return var2;
   }

   public static float parseNonNegativeFloat(String var0, String var1) throws SAXException {
      float var2 = Float.parseFloat(var1);
      checkForNegativeValue(var0, var2);
      return var2;
   }

   public static int parseNonNegativeInteger(String var0, String var1) throws SAXException {
      int var2 = Integer.parseInt(var1);
      checkForNegativeValue(var0, (float)var2);
      return var2;
   }
}
