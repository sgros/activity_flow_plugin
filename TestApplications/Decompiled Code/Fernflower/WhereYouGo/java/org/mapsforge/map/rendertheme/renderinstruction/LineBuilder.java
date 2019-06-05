package org.mapsforge.map.rendertheme.renderinstruction;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;
import org.mapsforge.map.graphics.Cap;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Style;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LineBuilder {
   private static final Pattern SPLIT_PATTERN = Pattern.compile(",");
   static final String SRC = "src";
   static final String STROKE = "stroke";
   static final String STROKE_DASHARRAY = "stroke-dasharray";
   static final String STROKE_LINECAP = "stroke-linecap";
   static final String STROKE_WIDTH = "stroke-width";
   final int level;
   final Paint stroke;
   float strokeWidth;

   public LineBuilder(GraphicAdapter var1, String var2, Attributes var3, int var4, String var5) throws IOException, SAXException {
      this.level = var4;
      this.stroke = var1.getPaint();
      this.stroke.setColor(var1.getColor(GraphicAdapter.Color.BLACK));
      this.stroke.setStyle(Style.STROKE);
      this.stroke.setStrokeCap(Cap.ROUND);
      this.extractValues(var1, var2, var3, var5);
   }

   private void extractValues(GraphicAdapter var1, String var2, Attributes var3, String var4) throws IOException, SAXException {
      for(int var5 = 0; var5 < var3.getLength(); ++var5) {
         String var6 = var3.getQName(var5);
         String var7 = var3.getValue(var5);
         if ("src".equals(var6)) {
            this.stroke.setBitmapShader(XmlUtils.createBitmap(var1, var4, var7));
         } else if ("stroke".equals(var6)) {
            this.stroke.setColor(var1.parseColor(var7));
         } else if ("stroke-width".equals(var6)) {
            this.strokeWidth = XmlUtils.parseNonNegativeFloat(var6, var7);
         } else if ("stroke-dasharray".equals(var6)) {
            this.stroke.setDashPathEffect(parseFloatArray(var6, var7));
         } else {
            if (!"stroke-linecap".equals(var6)) {
               throw XmlUtils.createSAXException(var2, var6, var7, var5);
            }

            this.stroke.setStrokeCap(Cap.valueOf(var7.toUpperCase(Locale.ENGLISH)));
         }
      }

   }

   private static float[] parseFloatArray(String var0, String var1) throws SAXException {
      String[] var2 = SPLIT_PATTERN.split(var1);
      float[] var4 = new float[var2.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var4[var3] = XmlUtils.parseNonNegativeFloat(var0, var2[var3]);
      }

      return var4;
   }

   public Line build() {
      return new Line(this);
   }
}
