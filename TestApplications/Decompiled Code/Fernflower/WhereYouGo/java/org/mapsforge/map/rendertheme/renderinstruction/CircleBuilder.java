package org.mapsforge.map.rendertheme.renderinstruction;

import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Style;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CircleBuilder {
   static final String FILL = "fill";
   static final String RADIUS = "radius";
   static final String SCALE_RADIUS = "scale-radius";
   static final String STROKE = "stroke";
   static final String STROKE_WIDTH = "stroke-width";
   final Paint fill;
   final int level;
   Float radius;
   boolean scaleRadius;
   final Paint stroke;
   float strokeWidth;

   public CircleBuilder(GraphicAdapter var1, String var2, Attributes var3, int var4) throws SAXException {
      this.level = var4;
      this.fill = var1.getPaint();
      this.fill.setColor(var1.getColor(GraphicAdapter.Color.TRANSPARENT));
      this.fill.setStyle(Style.FILL);
      this.stroke = var1.getPaint();
      this.stroke.setColor(var1.getColor(GraphicAdapter.Color.TRANSPARENT));
      this.stroke.setStyle(Style.STROKE);
      this.extractValues(var1, var2, var3);
   }

   private void extractValues(GraphicAdapter var1, String var2, Attributes var3) throws SAXException {
      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         String var5 = var3.getQName(var4);
         String var6 = var3.getValue(var4);
         if ("radius".equals(var5)) {
            this.radius = XmlUtils.parseNonNegativeFloat(var5, var6);
         } else if ("scale-radius".equals(var5)) {
            this.scaleRadius = Boolean.parseBoolean(var6);
         } else if ("fill".equals(var5)) {
            this.fill.setColor(var1.parseColor(var6));
         } else if ("stroke".equals(var5)) {
            this.stroke.setColor(var1.parseColor(var6));
         } else {
            if (!"stroke-width".equals(var5)) {
               throw XmlUtils.createSAXException(var2, var5, var6, var4);
            }

            this.strokeWidth = XmlUtils.parseNonNegativeFloat(var5, var6);
         }
      }

      XmlUtils.checkMandatoryAttribute(var2, "radius", this.radius);
   }

   public Circle build() {
      return new Circle(this);
   }
}
