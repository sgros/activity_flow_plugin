package locus.api.objects.extra;

import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class KmlVec2 {
   public double x = 0.5D;
   public KmlVec2.Units xUnits;
   public double y;
   public KmlVec2.Units yUnits;

   public KmlVec2() {
      this.xUnits = KmlVec2.Units.FRACTION;
      this.y = 0.5D;
      this.yUnits = KmlVec2.Units.FRACTION;
   }

   public KmlVec2(double var1, KmlVec2.Units var3, double var4, KmlVec2.Units var6) {
      this.xUnits = KmlVec2.Units.FRACTION;
      this.y = 0.5D;
      this.yUnits = KmlVec2.Units.FRACTION;
      this.x = var1;
      this.xUnits = var3;
      this.y = var4;
      this.yUnits = var6;
   }

   public static KmlVec2 read(DataReaderBigEndian var0) throws IOException {
      KmlVec2 var1 = new KmlVec2();
      var1.x = var0.readDouble();
      var1.xUnits = KmlVec2.Units.values()[var0.readInt()];
      var1.y = var0.readDouble();
      var1.yUnits = KmlVec2.Units.values()[var0.readInt()];
      return var1;
   }

   public String getAsXmlText() {
      StringBuilder var1 = new StringBuilder();
      var1.append("\t\t\t<hotSpot x=\"").append(this.x).append("\" y=\"").append(this.y).append("\"");
      var1.append(" xunits=\"");
      switch(this.xUnits) {
      case FRACTION:
         var1.append("fraction");
         break;
      case PIXELS:
         var1.append("pixels");
         break;
      case INSET_PIXELS:
         var1.append("insetPixels");
      }

      var1.append("\"");
      var1.append(" yunits=\"");
      switch(this.yUnits) {
      case FRACTION:
         var1.append("fraction");
         break;
      case PIXELS:
         var1.append("pixels");
         break;
      case INSET_PIXELS:
         var1.append("insetPixels");
      }

      var1.append("\"");
      var1.append(" />");
      return var1.toString();
   }

   public double[] getCoords(double var1, double var3) {
      return this.getCoords(var1, var3, new double[2]);
   }

   public double[] getCoords(double var1, double var3, double[] var5) {
      double[] var6;
      label31: {
         if (var5 != null) {
            var6 = var5;
            if (var5.length == 2) {
               break label31;
            }
         }

         var6 = new double[2];
      }

      if (this.xUnits == KmlVec2.Units.FRACTION) {
         var6[0] = this.x * var1;
      } else if (this.xUnits == KmlVec2.Units.PIXELS) {
         var6[0] = this.x;
      } else if (this.xUnits == KmlVec2.Units.INSET_PIXELS) {
         var6[0] = var1 - this.x;
      }

      if (this.yUnits == KmlVec2.Units.FRACTION) {
         var6[1] = (1.0D - this.y) * var3;
      } else if (this.yUnits == KmlVec2.Units.PIXELS) {
         var6[1] = var3 - this.y;
      } else if (this.yUnits == KmlVec2.Units.INSET_PIXELS) {
         var6[1] = this.y;
      }

      return var6;
   }

   public KmlVec2 getCopy() {
      KmlVec2 var1 = new KmlVec2();
      var1.x = this.x;
      var1.xUnits = this.xUnits;
      var1.y = this.y;
      var1.yUnits = this.yUnits;
      return var1;
   }

   public void write(DataWriterBigEndian var1) throws IOException {
      var1.writeDouble(this.x);
      var1.writeInt(this.xUnits.ordinal());
      var1.writeDouble(this.y);
      var1.writeInt(this.yUnits.ordinal());
   }

   public static enum Units {
      FRACTION,
      INSET_PIXELS,
      PIXELS;
   }
}
