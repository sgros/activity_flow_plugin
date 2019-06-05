package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import java.util.List;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.graphics.android.AndroidGraphics;
import org.mapsforge.map.rendertheme.GraphicAdapter;

class CanvasRasterer {
   private static final Paint PAINT_BITMAP_FILTER = createAndroidPaint();
   private static final Paint PAINT_TILE_COORDINATES = createAndroidPaint();
   private static final Paint PAINT_TILE_COORDINATES_STROKE = createAndroidPaint();
   private static final Paint PAINT_TILE_FRAME = createAndroidPaint();
   private static final float[] TILE_FRAME = new float[]{0.0F, 0.0F, 0.0F, 256.0F, 0.0F, 256.0F, 256.0F, 256.0F, 256.0F, 256.0F, 256.0F, 0.0F, 256.0F, 0.0F, 0.0F, 0.0F};
   private final Canvas canvas = new Canvas();
   private final Path path = new Path();
   private final Matrix symbolMatrix = new Matrix();

   CanvasRasterer() {
      this.path.setFillType(FillType.EVEN_ODD);
      configurePaints();
   }

   private static void configurePaints() {
      PAINT_TILE_COORDINATES.setTypeface(Typeface.defaultFromStyle(1));
      PAINT_TILE_COORDINATES.setTextSize(20.0F);
      PAINT_TILE_COORDINATES.setTypeface(Typeface.defaultFromStyle(1));
      PAINT_TILE_COORDINATES_STROKE.setStyle(Style.STROKE);
      PAINT_TILE_COORDINATES_STROKE.setStrokeWidth(5.0F);
      PAINT_TILE_COORDINATES_STROKE.setTextSize(20.0F);
      PAINT_TILE_COORDINATES_STROKE.setColor(AndroidGraphics.INSTANCE.getColor(GraphicAdapter.Color.WHITE));
   }

   private static Paint createAndroidPaint() {
      return new Paint(1);
   }

   private void drawTileCoordinate(String var1, int var2) {
      this.canvas.drawText(var1, 20.0F, (float)var2, PAINT_TILE_COORDINATES_STROKE);
      this.canvas.drawText(var1, 20.0F, (float)var2, PAINT_TILE_COORDINATES);
   }

   void drawNodes(List var1) {
      for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
         PointTextContainer var3 = (PointTextContainer)var1.get(var2);
         Paint var4;
         if (var3.paintBack != null) {
            var4 = AndroidGraphics.getAndroidPaint(var3.paintBack);
            this.canvas.drawText(var3.text, (float)var3.x, (float)var3.y, var4);
         }

         var4 = AndroidGraphics.getAndroidPaint(var3.paintFront);
         this.canvas.drawText(var3.text, (float)var3.x, (float)var3.y, var4);
      }

   }

   void drawSymbols(List var1) {
      for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
         SymbolContainer var3 = (SymbolContainer)var1.get(var2);
         Point var4 = var3.point;
         if (var3.alignCenter) {
            int var5 = var3.symbol.getWidth() / 2;
            int var6 = var3.symbol.getHeight() / 2;
            this.symbolMatrix.setRotate(var3.rotation, (float)var5, (float)var6);
            this.symbolMatrix.postTranslate((float)(var4.x - (double)var5), (float)(var4.y - (double)var6));
         } else {
            this.symbolMatrix.setRotate(var3.rotation);
            this.symbolMatrix.postTranslate((float)var4.x, (float)var4.y);
         }

         Bitmap var7 = AndroidGraphics.getAndroidBitmap(var3.symbol);
         this.canvas.drawBitmap(var7, this.symbolMatrix, PAINT_BITMAP_FILTER);
      }

   }

   void drawTileCoordinates(Tile var1) {
      this.drawTileCoordinate("X: " + var1.tileX, 30);
      this.drawTileCoordinate("Y: " + var1.tileY, 60);
      this.drawTileCoordinate("Z: " + var1.zoomLevel, 90);
   }

   void drawTileFrame() {
      this.canvas.drawLines(TILE_FRAME, PAINT_TILE_FRAME);
   }

   void drawWayNames(List var1) {
      for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
         WayTextContainer var3 = (WayTextContainer)var1.get(var2);
         this.path.rewind();
         double[] var4 = var3.coordinates;
         this.path.moveTo((float)var4[0], (float)var4[1]);

         for(int var5 = 2; var5 < var4.length; var5 += 2) {
            this.path.lineTo((float)var4[var5], (float)var4[var5 + 1]);
         }

         Paint var6 = AndroidGraphics.getAndroidPaint(var3.paint);
         this.canvas.drawTextOnPath(var3.text, this.path, 0.0F, 3.0F, var6);
      }

   }

   void drawWays(List var1) {
      int var2 = ((List)var1.get(0)).size();
      int var3 = 0;

      for(int var4 = var1.size(); var3 < var4; ++var3) {
         List var5 = (List)var1.get(var3);

         for(int var6 = 0; var6 < var2; ++var6) {
            List var7 = (List)var5.get(var6);

            for(int var8 = var7.size() - 1; var8 >= 0; --var8) {
               ShapePaintContainer var9 = (ShapePaintContainer)var7.get(var8);
               this.path.rewind();
               switch(var9.shapeContainer.getShapeType()) {
               case CIRCLE:
                  CircleContainer var16 = (CircleContainer)var9.shapeContainer;
                  Point var17 = var16.point;
                  this.path.addCircle((float)var17.x, (float)var17.y, var16.radius, Direction.CCW);
                  break;
               case WAY:
                  Point[][] var11 = ((WayContainer)var9.shapeContainer).coordinates;
                  int var12 = 0;

                  for(; var12 < var11.length; ++var12) {
                     Point[] var10 = var11[var12];
                     if (var10.length >= 2) {
                        Point var13 = var10[0];
                        this.path.moveTo((float)var13.x, (float)var13.y);

                        for(int var14 = 1; var14 < var10.length; ++var14) {
                           var13 = var10[var14];
                           this.path.lineTo((float)var13.x, (float)var13.y);
                        }
                     }
                  }
               }

               Paint var15 = AndroidGraphics.getAndroidPaint(var9.paint);
               this.canvas.drawPath(this.path, var15);
            }
         }
      }

   }

   void fill(int var1) {
      this.canvas.drawColor(var1);
   }

   void setCanvasBitmap(Bitmap var1) {
      this.canvas.setBitmap(var1);
   }
}
