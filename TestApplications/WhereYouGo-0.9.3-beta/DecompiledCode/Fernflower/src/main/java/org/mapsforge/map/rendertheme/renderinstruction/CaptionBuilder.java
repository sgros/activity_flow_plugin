package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.Locale;
import org.mapsforge.map.graphics.Align;
import org.mapsforge.map.graphics.FontFamily;
import org.mapsforge.map.graphics.FontStyle;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Style;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CaptionBuilder {
   static final String DY = "dy";
   static final String FILL = "fill";
   static final String FONT_FAMILY = "font-family";
   static final String FONT_SIZE = "font-size";
   static final String FONT_STYLE = "font-style";
   static final String K = "k";
   static final String STROKE = "stroke";
   static final String STROKE_WIDTH = "stroke-width";
   float dy;
   final Paint fill;
   float fontSize;
   final Paint stroke;
   TextKey textKey;

   public CaptionBuilder(GraphicAdapter var1, String var2, Attributes var3) throws SAXException {
      this.fill = var1.getPaint();
      this.fill.setColor(var1.getColor(GraphicAdapter.Color.BLACK));
      this.fill.setStyle(Style.FILL);
      this.fill.setTextAlign(Align.LEFT);
      this.stroke = var1.getPaint();
      this.stroke.setColor(var1.getColor(GraphicAdapter.Color.BLACK));
      this.stroke.setStyle(Style.STROKE);
      this.stroke.setTextAlign(Align.LEFT);
      this.extractValues(var1, var2, var3);
   }

   private void extractValues(GraphicAdapter var1, String var2, Attributes var3) throws SAXException {
      FontFamily var4 = FontFamily.DEFAULT;
      FontStyle var5 = FontStyle.NORMAL;

      for(int var6 = 0; var6 < var3.getLength(); ++var6) {
         String var7 = var3.getQName(var6);
         String var8 = var3.getValue(var6);
         if ("k".equals(var7)) {
            this.textKey = TextKey.getInstance(var8);
         } else if ("dy".equals(var7)) {
            this.dy = Float.parseFloat(var8);
         } else if ("font-family".equals(var7)) {
            var4 = FontFamily.valueOf(var8.toUpperCase(Locale.ENGLISH));
         } else if ("font-style".equals(var7)) {
            var5 = FontStyle.valueOf(var8.toUpperCase(Locale.ENGLISH));
         } else if ("font-size".equals(var7)) {
            this.fontSize = XmlUtils.parseNonNegativeFloat(var7, var8);
         } else if ("fill".equals(var7)) {
            this.fill.setColor(var1.parseColor(var8));
         } else if ("stroke".equals(var7)) {
            this.stroke.setColor(var1.parseColor(var8));
         } else {
            if (!"stroke-width".equals(var7)) {
               throw XmlUtils.createSAXException(var2, var7, var8, var6);
            }

            this.stroke.setStrokeWidth(XmlUtils.parseNonNegativeFloat(var7, var8));
         }
      }

      this.fill.setTypeface(var4, var5);
      this.stroke.setTypeface(var4, var5);
      XmlUtils.checkMandatoryAttribute(var2, "k", this.textKey);
   }

   public Caption build() {
      return new Caption(this);
   }
}
