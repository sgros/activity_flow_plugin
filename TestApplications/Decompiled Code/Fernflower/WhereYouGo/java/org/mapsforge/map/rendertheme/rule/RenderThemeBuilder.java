package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RenderThemeBuilder {
   private static final String BASE_STROKE_WIDTH = "base-stroke-width";
   private static final String BASE_TEXT_SIZE = "base-text-size";
   private static final String MAP_BACKGROUND = "map-background";
   private static final int RENDER_THEME_VERSION = 2;
   private static final String VERSION = "version";
   private static final String XMLNS = "xmlns";
   private static final String XMLNS_XSI = "xmlns:xsi";
   private static final String XSI_SCHEMALOCATION = "xsi:schemaLocation";
   float baseStrokeWidth = 1.0F;
   float baseTextSize = 1.0F;
   int mapBackground;
   private Integer version;

   public RenderThemeBuilder(GraphicAdapter var1, String var2, Attributes var3) throws SAXException {
      this.mapBackground = var1.getColor(GraphicAdapter.Color.WHITE);
      this.extractValues(var1, var2, var3);
   }

   private void extractValues(GraphicAdapter var1, String var2, Attributes var3) throws SAXException {
      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         String var5 = var3.getQName(var4);
         String var6 = var3.getValue(var4);
         if (!"xmlns".equals(var5) && !"xmlns:xsi".equals(var5) && !"xsi:schemaLocation".equals(var5)) {
            if ("version".equals(var5)) {
               this.version = XmlUtils.parseNonNegativeInteger(var5, var6);
            } else if ("map-background".equals(var5)) {
               this.mapBackground = var1.parseColor(var6);
            } else if ("base-stroke-width".equals(var5)) {
               this.baseStrokeWidth = XmlUtils.parseNonNegativeFloat(var5, var6);
            } else {
               if (!"base-text-size".equals(var5)) {
                  throw XmlUtils.createSAXException(var2, var5, var6, var4);
               }

               this.baseTextSize = XmlUtils.parseNonNegativeFloat(var5, var6);
            }
         }
      }

      this.validate(var2);
   }

   private void validate(String var1) throws SAXException {
      XmlUtils.checkMandatoryAttribute(var1, "version", this.version);
      if (this.version != 2) {
         throw new SAXException("unsupported render theme version: " + this.version);
      }
   }

   public RenderTheme build() {
      return new RenderTheme(this);
   }
}
