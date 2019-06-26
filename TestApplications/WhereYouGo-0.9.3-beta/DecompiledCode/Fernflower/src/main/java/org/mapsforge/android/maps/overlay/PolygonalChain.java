package org.mapsforge.android.maps.overlay;

import android.graphics.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class PolygonalChain {
   private final List geoPoints;

   public PolygonalChain(Collection var1) {
      if (var1 == null) {
         this.geoPoints = Collections.synchronizedList(new ArrayList());
      } else {
         this.geoPoints = Collections.synchronizedList(new ArrayList(var1));
      }

   }

   protected Path draw(byte var1, Point var2, boolean var3) {
      List var4 = this.geoPoints;
      synchronized(var4){}

      Throwable var10000;
      boolean var10001;
      label798: {
         int var5;
         try {
            var5 = this.geoPoints.size();
         } catch (Throwable var102) {
            var10000 = var102;
            var10001 = false;
            break label798;
         }

         Path var103;
         if (var5 < 2) {
            var103 = null;

            try {
               ;
            } catch (Throwable var95) {
               var10000 = var95;
               var10001 = false;
               break label798;
            }
         } else {
            Path var6;
            try {
               var6 = new Path();
            } catch (Throwable var101) {
               var10000 = var101;
               var10001 = false;
               break label798;
            }

            for(int var7 = 0; var7 < var5; ++var7) {
               float var11;
               float var12;
               try {
                  GeoPoint var8 = (GeoPoint)this.geoPoints.get(var7);
                  double var9 = var8.latitude;
                  var11 = (float)(MercatorProjection.longitudeToPixelX(var8.longitude, var1) - var2.x);
                  var12 = (float)(MercatorProjection.latitudeToPixelY(var9, var1) - var2.y);
               } catch (Throwable var100) {
                  var10000 = var100;
                  var10001 = false;
                  break label798;
               }

               if (var7 == 0) {
                  try {
                     var6.moveTo(var11, var12);
                  } catch (Throwable var99) {
                     var10000 = var99;
                     var10001 = false;
                     break label798;
                  }
               } else {
                  try {
                     var6.lineTo(var11, var12);
                  } catch (Throwable var98) {
                     var10000 = var98;
                     var10001 = false;
                     break label798;
                  }
               }
            }

            if (var3) {
               try {
                  if (!this.isClosed()) {
                     var6.close();
                  }
               } catch (Throwable var97) {
                  var10000 = var97;
                  var10001 = false;
                  break label798;
               }
            }

            try {
               ;
            } catch (Throwable var96) {
               var10000 = var96;
               var10001 = false;
               break label798;
            }

            var103 = var6;
         }

         return var103;
      }

      while(true) {
         Throwable var104 = var10000;

         try {
            throw var104;
         } catch (Throwable var94) {
            var10000 = var94;
            var10001 = false;
            continue;
         }
      }
   }

   public List getGeoPoints() {
      // $FF: Couldn't be decompiled
   }

   public boolean isClosed() {
      boolean var1 = false;
      List var2 = this.geoPoints;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label228: {
         int var3;
         try {
            var3 = this.geoPoints.size();
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label228;
         }

         if (var3 < 2) {
            try {
               ;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label228;
            }
         } else {
            try {
               var1 = ((GeoPoint)this.geoPoints.get(0)).equals((GeoPoint)this.geoPoints.get(var3 - 1));
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label228;
            }
         }

         label216:
         try {
            return var1;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label216;
         }
      }

      while(true) {
         Throwable var4 = var10000;

         try {
            throw var4;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }
}
