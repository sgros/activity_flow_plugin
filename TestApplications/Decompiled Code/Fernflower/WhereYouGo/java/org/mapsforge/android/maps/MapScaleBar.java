package org.mapsforge.android.maps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import java.util.HashMap;
import java.util.Map;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;

public class MapScaleBar {
   private static final int BITMAP_HEIGHT = 50;
   private static final int BITMAP_WIDTH = 150;
   private static final double LATITUDE_REDRAW_THRESHOLD = 0.2D;
   private static final int MARGIN_BOTTOM = 15;
   private static final int MARGIN_LEFT = 5;
   private static final double METER_FOOT_RATIO = 0.3048D;
   private static final int ONE_KILOMETER = 1000;
   private static final int ONE_MILE = 5280;
   private static final Paint SCALE_BAR = new Paint(1);
   private static final Paint SCALE_BAR_STROKE = new Paint(1);
   private static final int[] SCALE_BAR_VALUES_IMPERIAL = new int[]{26400000, 10560000, 5280000, 2640000, 1056000, 528000, 264000, 105600, 52800, 26400, 10560, 5280, 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1};
   private static final int[] SCALE_BAR_VALUES_METRIC = new int[]{10000000, 5000000, 2000000, 1000000, 500000, 200000, 100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1};
   private static final Paint SCALE_TEXT = new Paint(1);
   private static final Paint SCALE_TEXT_STROKE = new Paint(1);
   private boolean imperialUnits;
   private MapPosition mapPosition;
   private final Bitmap mapScaleBitmap;
   private final Canvas mapScaleCanvas;
   private final MapView mapView;
   private boolean redrawNeeded;
   private boolean showMapScaleBar;
   private final Map textFields;

   MapScaleBar(MapView var1) {
      this.mapView = var1;
      this.mapScaleBitmap = Bitmap.createBitmap(150, 50, Config.ARGB_4444);
      this.mapScaleCanvas = new Canvas(this.mapScaleBitmap);
      this.textFields = new HashMap();
      this.setDefaultTexts();
      configurePaints();
   }

   private static void configurePaints() {
      SCALE_BAR.setStrokeWidth(2.0F);
      SCALE_BAR.setStrokeCap(Cap.SQUARE);
      SCALE_BAR.setColor(-16777216);
      SCALE_BAR_STROKE.setStrokeWidth(5.0F);
      SCALE_BAR_STROKE.setStrokeCap(Cap.SQUARE);
      SCALE_BAR_STROKE.setColor(-1);
      SCALE_TEXT.setTypeface(Typeface.defaultFromStyle(1));
      SCALE_TEXT.setTextSize(17.0F);
      SCALE_TEXT.setColor(-16777216);
      SCALE_TEXT_STROKE.setTypeface(Typeface.defaultFromStyle(1));
      SCALE_TEXT_STROKE.setStyle(Style.STROKE);
      SCALE_TEXT_STROKE.setColor(-1);
      SCALE_TEXT_STROKE.setStrokeWidth(2.0F);
      SCALE_TEXT_STROKE.setTextSize(17.0F);
   }

   private void drawScaleBar(float var1, Paint var2) {
      this.mapScaleCanvas.drawLine(7.0F, 25.0F, 3.0F + var1, 25.0F, var2);
      this.mapScaleCanvas.drawLine(5.0F, 10.0F, 5.0F, 40.0F, var2);
      this.mapScaleCanvas.drawLine(var1 + 5.0F, 10.0F, var1 + 5.0F, 40.0F, var2);
   }

   private void drawScaleText(int var1, String var2, Paint var3) {
      this.mapScaleCanvas.drawText(var1 + var2, 12.0F, 18.0F, var3);
   }

   private boolean isRedrawNecessary() {
      boolean var1 = true;
      boolean var2 = var1;
      if (!this.redrawNeeded) {
         if (this.mapPosition == null) {
            var2 = var1;
         } else {
            MapPosition var3 = this.mapView.getMapViewPosition().getMapPosition();
            var2 = var1;
            if (var3.zoomLevel == this.mapPosition.zoomLevel) {
               var2 = var1;
               if (Math.abs(var3.geoPoint.latitude - this.mapPosition.geoPoint.latitude) <= 0.2D) {
                  var2 = false;
               }
            }
         }
      }

      return var2;
   }

   private void redrawMapScaleBitmap(float var1, int var2) {
      this.mapScaleBitmap.eraseColor(0);
      this.drawScaleBar(var1, SCALE_BAR_STROKE);
      this.drawScaleBar(var1, SCALE_BAR);
      String var3;
      if (this.imperialUnits) {
         if (var2 < 5280) {
            var3 = (String)this.textFields.get(MapScaleBar.TextField.FOOT);
         } else {
            var2 /= 5280;
            var3 = (String)this.textFields.get(MapScaleBar.TextField.MILE);
         }
      } else if (var2 < 1000) {
         var3 = (String)this.textFields.get(MapScaleBar.TextField.METER);
      } else {
         var2 /= 1000;
         var3 = (String)this.textFields.get(MapScaleBar.TextField.KILOMETER);
      }

      this.drawScaleText(var2, var3, SCALE_TEXT_STROKE);
      this.drawScaleText(var2, var3, SCALE_TEXT);
   }

   private void setDefaultTexts() {
      this.textFields.put(MapScaleBar.TextField.FOOT, " ft");
      this.textFields.put(MapScaleBar.TextField.MILE, " mi");
      this.textFields.put(MapScaleBar.TextField.METER, " m");
      this.textFields.put(MapScaleBar.TextField.KILOMETER, " km");
   }

   void destroy() {
      this.mapScaleBitmap.recycle();
   }

   void draw(Canvas var1) {
      int var2 = this.mapView.getHeight();
      var1.drawBitmap(this.mapScaleBitmap, 5.0F, (float)(var2 - 50 - 15), (Paint)null);
   }

   public boolean isImperialUnits() {
      return this.imperialUnits;
   }

   public boolean isShowMapScaleBar() {
      return this.showMapScaleBar;
   }

   void redrawScaleBar() {
      if (this.isRedrawNecessary()) {
         this.mapPosition = this.mapView.getMapViewPosition().getMapPosition();
         double var1 = MercatorProjection.calculateGroundResolution(this.mapPosition.geoPoint.latitude, this.mapPosition.zoomLevel);
         int[] var3;
         if (this.imperialUnits) {
            var1 /= 0.3048D;
            var3 = SCALE_BAR_VALUES_IMPERIAL;
         } else {
            var3 = SCALE_BAR_VALUES_METRIC;
         }

         float var4 = 0.0F;
         int var5 = 0;

         for(int var6 = 0; var6 < var3.length; ++var6) {
            var5 = var3[var6];
            var4 = (float)var5 / (float)var1;
            if (var4 < 140.0F) {
               break;
            }
         }

         this.redrawMapScaleBitmap(var4, var5);
         this.redrawNeeded = false;
      }

   }

   public void setImperialUnits(boolean var1) {
      this.imperialUnits = var1;
      this.redrawNeeded = true;
   }

   public void setShowMapScaleBar(boolean var1) {
      this.showMapScaleBar = var1;
   }

   public void setText(MapScaleBar.TextField var1, String var2) {
      this.textFields.put(var1, var2);
      this.redrawNeeded = true;
   }

   public static enum TextField {
      FOOT,
      KILOMETER,
      METER,
      MILE;
   }
}
