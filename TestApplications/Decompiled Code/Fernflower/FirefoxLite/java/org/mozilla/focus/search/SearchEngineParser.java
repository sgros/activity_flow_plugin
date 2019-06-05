package org.mozilla.focus.search;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

class SearchEngineParser {
   public static SearchEngine load(AssetManager param0, String param1, String param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   static SearchEngine load(String var0, InputStream var1) throws IOException, XmlPullParserException {
      SearchEngine var3 = new SearchEngine(var0);
      XmlPullParser var2 = XmlPullParserFactory.newInstance().newPullParser();
      var2.setInput(new InputStreamReader(var1, StandardCharsets.UTF_8));
      var2.next();
      readSearchPlugin(var2, var3);
      return var3;
   }

   private static void readImage(XmlPullParser var0, SearchEngine var1) throws IOException, XmlPullParserException {
      var0.require(2, (String)null, "Image");
      if (var0.next() == 4) {
         String var2 = var0.getText();
         if (var2.startsWith("data:image/png;base64,")) {
            byte[] var3 = Base64.decode(var2.substring("data:image/png;base64,".length()), 0);
            var1.icon = BitmapFactory.decodeByteArray(var3, 0, var3.length);
            var0.nextTag();
         }
      }
   }

   private static void readSearchPlugin(XmlPullParser var0, SearchEngine var1) throws XmlPullParserException, IOException {
      StringBuilder var3;
      if (2 == var0.getEventType()) {
         String var2 = var0.getName();
         if (!"SearchPlugin".equals(var2) && !"OpenSearchDescription".equals(var2)) {
            var3 = new StringBuilder();
            var3.append("Expected <SearchPlugin> or <OpenSearchDescription> as root tag: ");
            var3.append(var0.getPositionDescription());
            throw new XmlPullParserException(var3.toString());
         } else {
            while(var0.next() != 3) {
               if (var0.getEventType() == 2) {
                  var2 = var0.getName();
                  if (var2.equals("ShortName")) {
                     readShortName(var0, var1);
                  } else if (var2.equals("Url")) {
                     readUrl(var0, var1);
                  } else if (var2.equals("Image")) {
                     readImage(var0, var1);
                  } else {
                     skip(var0);
                  }
               }
            }

         }
      } else {
         var3 = new StringBuilder();
         var3.append("Expected start tag: ");
         var3.append(var0.getPositionDescription());
         throw new XmlPullParserException(var3.toString());
      }
   }

   private static void readShortName(XmlPullParser var0, SearchEngine var1) throws IOException, XmlPullParserException {
      var0.require(2, (String)null, "ShortName");
      if (var0.next() == 4) {
         var1.name = var0.getText();
         var0.nextTag();
      }

   }

   private static void readUrl(XmlPullParser var0, SearchEngine var1) throws XmlPullParserException, IOException {
      var0.require(2, (String)null, "Url");
      String var2 = var0.getAttributeValue((String)null, "type");
      String var3 = var0.getAttributeValue((String)null, "template");
      String var4 = var0.getAttributeValue((String)null, "rel");
      Uri var7 = Uri.parse(var3);

      while(var0.next() != 3) {
         if (var0.getEventType() == 2) {
            if (var0.getName().equals("Param")) {
               String var5 = var0.getAttributeValue((String)null, "name");
               String var6 = var0.getAttributeValue((String)null, "value");
               var7 = var7.buildUpon().appendQueryParameter(var5, var6).build();
               var0.nextTag();
            } else {
               skip(var0);
            }
         }
      }

      if (var2.equals("text/html")) {
         if (var4 != null && var4.equals("mobile")) {
            var1.resultsUris.add(0, var7);
         } else {
            var1.resultsUris.add(var7);
         }
      } else if (var2.equals("application/x-suggestions+json")) {
         var1.suggestUri = var7;
      }

   }

   private static void skip(XmlPullParser var0) throws XmlPullParserException, IOException {
      if (var0.getEventType() == 2) {
         int var1 = 1;

         while(var1 != 0) {
            switch(var0.next()) {
            case 2:
               ++var1;
               break;
            case 3:
               --var1;
            }
         }

      } else {
         throw new IllegalStateException();
      }
   }
}
