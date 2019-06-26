package org.mapsforge.map.rendertheme.renderinstruction;

import java.io.IOException;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LineSymbolBuilder {
   static final String ALIGN_CENTER = "align-center";
   static final String REPEAT = "repeat";
   static final String SRC = "src";
   boolean alignCenter;
   Bitmap bitmap;
   boolean repeat;

   public LineSymbolBuilder(GraphicAdapter var1, String var2, Attributes var3, String var4) throws IOException, SAXException {
      this.extractValues(var1, var2, var3, var4);
   }

   private void extractValues(GraphicAdapter var1, String var2, Attributes var3, String var4) throws IOException, SAXException {
      for(int var5 = 0; var5 < var3.getLength(); ++var5) {
         String var6 = var3.getQName(var5);
         String var7 = var3.getValue(var5);
         if ("src".equals(var6)) {
            this.bitmap = XmlUtils.createBitmap(var1, var4, var7);
         } else if ("align-center".equals(var6)) {
            this.alignCenter = Boolean.parseBoolean(var7);
         } else {
            if (!"repeat".equals(var6)) {
               throw XmlUtils.createSAXException(var2, var6, var7, var5);
            }

            this.repeat = Boolean.parseBoolean(var7);
         }
      }

      XmlUtils.checkMandatoryAttribute(var2, "src", this.bitmap);
   }

   public LineSymbol build() {
      return new LineSymbol(this);
   }
}