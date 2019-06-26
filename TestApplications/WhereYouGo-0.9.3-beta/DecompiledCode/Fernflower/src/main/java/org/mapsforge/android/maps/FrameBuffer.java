package org.mapsforge.android.maps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import java.io.OutputStream;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;

public class FrameBuffer {
   static final int MAP_VIEW_BACKGROUND = Color.rgb(238, 238, 238);
   private int height;
   private final MapView mapView;
   private Bitmap mapViewBitmap1;
   private Bitmap mapViewBitmap2;
   private Canvas mapViewCanvas;
   private final Matrix matrix;
   private int width;

   FrameBuffer(MapView var1) {
      this.mapView = var1;
      this.mapViewCanvas = new Canvas();
      this.matrix = new Matrix();
   }

   void clear() {
      synchronized(this){}

      try {
         if (this.mapViewBitmap1 != null) {
            this.mapViewBitmap1.eraseColor(MAP_VIEW_BACKGROUND);
         }

         if (this.mapViewBitmap2 != null) {
            this.mapViewBitmap2.eraseColor(MAP_VIEW_BACKGROUND);
         }
      } finally {
         ;
      }

   }

   boolean compress(CompressFormat var1, int var2, OutputStream var3) {
      synchronized(this){}

      Throwable var10000;
      label79: {
         boolean var10001;
         Bitmap var4;
         try {
            var4 = this.mapViewBitmap1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label79;
         }

         boolean var5;
         if (var4 == null) {
            var5 = false;
            return var5;
         }

         label69:
         try {
            var5 = this.mapViewBitmap1.compress(var1, var2, var3);
            return var5;
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label69;
         }
      }

      Throwable var12 = var10000;
      throw var12;
   }

   void destroy() {
      synchronized(this){}

      try {
         if (this.mapViewBitmap1 != null) {
            this.mapViewBitmap1.recycle();
         }

         if (this.mapViewBitmap2 != null) {
            this.mapViewBitmap2.recycle();
         }

         this.mapViewCanvas = null;
      } finally {
         ;
      }

   }

   void draw(Canvas var1) {
      synchronized(this){}

      try {
         if (this.mapViewBitmap1 != null) {
            var1.drawBitmap(this.mapViewBitmap1, this.matrix, (Paint)null);
         }
      } finally {
         ;
      }

   }

   public boolean drawBitmap(Tile var1, Bitmap var2) {
      Throwable var10000;
      boolean var10001;
      label1687: {
         MapPosition var3 = this.mapView.getMapViewPosition().getMapPosition();
         synchronized(this){}
         boolean var4;
         if (var2 == null) {
            var4 = false;

            try {
               ;
            } catch (Throwable var216) {
               var10000 = var216;
               var10001 = false;
               break label1687;
            }
         } else {
            label1686: {
               label1682: {
                  try {
                     if (var1.zoomLevel != var3.zoomLevel) {
                        break label1682;
                     }
                  } catch (Throwable var221) {
                     var10000 = var221;
                     var10001 = false;
                     break label1687;
                  }

                  label1683: {
                     try {
                        if (!this.mapView.isZoomAnimatorRunning()) {
                           break label1683;
                        }
                     } catch (Throwable var220) {
                        var10000 = var220;
                        var10001 = false;
                        break label1687;
                     }

                     var4 = false;

                     try {
                        break label1686;
                     } catch (Throwable var214) {
                        var10000 = var214;
                        var10001 = false;
                        break label1687;
                     }
                  }

                  label1684: {
                     double var6;
                     double var8;
                     try {
                        GeoPoint var5 = var3.geoPoint;
                        var6 = MercatorProjection.longitudeToPixelX(var5.longitude, var3.zoomLevel);
                        var8 = MercatorProjection.latitudeToPixelY(var5.latitude, var3.zoomLevel);
                        var6 -= (double)(this.width >> 1);
                        var8 -= (double)(this.height >> 1);
                        if (var6 - (double)var1.getPixelX() > 256.0D || (double)this.width + var6 < (double)var1.getPixelX()) {
                           break label1684;
                        }
                     } catch (Throwable var219) {
                        var10000 = var219;
                        var10001 = false;
                        break label1687;
                     }

                     label1685: {
                        try {
                           if (var8 - (double)var1.getPixelY() > 256.0D || (double)this.height + var8 < (double)var1.getPixelY()) {
                              break label1685;
                           }
                        } catch (Throwable var218) {
                           var10000 = var218;
                           var10001 = false;
                           break label1687;
                        }

                        try {
                           if (!this.matrix.isIdentity()) {
                              this.mapViewBitmap2.eraseColor(MAP_VIEW_BACKGROUND);
                              this.mapViewCanvas.setBitmap(this.mapViewBitmap2);
                              this.mapViewCanvas.drawBitmap(this.mapViewBitmap1, this.matrix, (Paint)null);
                              this.matrix.reset();
                              Bitmap var223 = this.mapViewBitmap1;
                              this.mapViewBitmap1 = this.mapViewBitmap2;
                              this.mapViewBitmap2 = var223;
                           }
                        } catch (Throwable var217) {
                           var10000 = var217;
                           var10001 = false;
                           break label1687;
                        }

                        try {
                           float var10 = (float)((double)var1.getPixelX() - var6);
                           float var11 = (float)((double)var1.getPixelY() - var8);
                           this.mapViewCanvas.drawBitmap(var2, var10, var11, (Paint)null);
                        } catch (Throwable var211) {
                           var10000 = var211;
                           var10001 = false;
                           break label1687;
                        }

                        var4 = true;

                        try {
                           break label1686;
                        } catch (Throwable var210) {
                           var10000 = var210;
                           var10001 = false;
                           break label1687;
                        }
                     }

                     var4 = false;

                     try {
                        break label1686;
                     } catch (Throwable var212) {
                        var10000 = var212;
                        var10001 = false;
                        break label1687;
                     }
                  }

                  var4 = false;

                  try {
                     break label1686;
                  } catch (Throwable var213) {
                     var10000 = var213;
                     var10001 = false;
                     break label1687;
                  }
               }

               var4 = false;

               try {
                  ;
               } catch (Throwable var215) {
                  var10000 = var215;
                  var10001 = false;
                  break label1687;
               }
            }
         }

         label1625:
         try {
            return var4;
         } catch (Throwable var209) {
            var10000 = var209;
            var10001 = false;
            break label1625;
         }
      }

      while(true) {
         Throwable var222 = var10000;

         try {
            throw var222;
         } catch (Throwable var208) {
            var10000 = var208;
            var10001 = false;
            continue;
         }
      }
   }

   public void matrixPostScale(float var1, float var2, float var3, float var4) {
      synchronized(this){}

      try {
         this.matrix.postScale(var1, var2, var3, var4);
         this.mapView.getOverlayController().postScale(var1, var2, var3, var4);
      } finally {
         ;
      }

   }

   public void matrixPostTranslate(float var1, float var2) {
      synchronized(this){}

      try {
         this.matrix.postTranslate(var1, var2);
         this.mapView.getOverlayController().postTranslate(var1, var2);
      } finally {
         ;
      }

   }

   void onSizeChanged() {
      synchronized(this){}

      try {
         this.destroy();
         Canvas var1 = new Canvas();
         this.mapViewCanvas = var1;
         this.width = this.mapView.getWidth();
         this.height = this.mapView.getHeight();
         this.mapViewBitmap1 = Bitmap.createBitmap(this.width, this.height, Config.RGB_565);
         this.mapViewBitmap2 = Bitmap.createBitmap(this.width, this.height, Config.RGB_565);
         this.clear();
         this.mapViewCanvas.setBitmap(this.mapViewBitmap1);
      } finally {
         ;
      }

   }
}
