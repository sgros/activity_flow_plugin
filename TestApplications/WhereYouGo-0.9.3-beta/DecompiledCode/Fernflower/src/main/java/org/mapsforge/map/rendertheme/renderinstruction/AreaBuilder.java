package org.mapsforge.map.rendertheme.renderinstruction;

import java.io.IOException;
import org.mapsforge.map.graphics.Cap;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Style;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AreaBuilder {
   static final String FILL = "fill";
   static final String SRC = "src";
   static final String STROKE = "stroke";
   static final String STROKE_WIDTH = "stroke-width";
   final Paint fill;
   final int level;
   final Paint stroke;
   float strokeWidth;

   public AreaBuilder(GraphicAdapter var1, String var2, Attributes var3, int var4, String var5) throws IOException, SAXException {
      this.level = var4;
      this.fill = var1.getPaint();
      this.fill.setColor(var1.getColor(GraphicAdapter.Color.BLACK));
      this.fill.setStyle(Style.FILL);
      this.fill.setStrokeCap(Cap.ROUND);
      this.stroke = var1.getPaint();
      this.stroke.setColor(var1.getColor(GraphicAdapter.Color.TRANSPARENT));
      this.stroke.setStyle(Style.STROKE);
      this.stroke.setStrokeCap(Cap.ROUND);
      this.extractValues(var1, var2, var3, var5);
   }

   private void extractValues(GraphicAdapter var1, String var2, Attributes var3, String var4) throws IOException, SAXException {
      for(int var5 = 0; var5 < var3.getLength(); ++var5) {
         String var6 = var3.getQName(var5);
         String var7 = var3.getValue(var5);
         if ("src".equals(var6)) {
            this.fill.setBitmapShader(XmlUtils.createBitmap(var1, var4, var7));
         } else if ("fill".equals(var6)) {
            this.fill.setColor(var1.parseColor(var7));
         } else if ("stroke".equals(var6)) {
            this.stroke.setColor(var1.parseColor(var7));
         } else {
            if (!"stroke-width".equals(var6)) {
               throw XmlUtils.createSAXException(var2, var6, var7, var5);
            }

            this.strokeWidth = XmlUtils.parseNonNegativeFloat(var6, var7);
         }
      }

   }

   public Area build() {
      return new Area(this);
   }
}
