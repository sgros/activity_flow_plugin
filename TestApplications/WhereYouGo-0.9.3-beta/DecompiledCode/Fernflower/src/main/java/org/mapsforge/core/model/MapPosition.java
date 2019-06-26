package org.mapsforge.core.model;

import java.io.Serializable;

public class MapPosition implements Serializable {
   private static final long serialVersionUID = 1L;
   public final GeoPoint geoPoint;
   public final byte zoomLevel;

   public MapPosition(GeoPoint var1, byte var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("geoPoint must not be null");
      } else if (var2 < 0) {
         throw new IllegalArgumentException("zoomLevel must not be negative: " + var2);
      } else {
         this.geoPoint = var1;
         this.zoomLevel = (byte)var2;
      }
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof MapPosition)) {
            var2 = false;
         } else {
            MapPosition var3 = (MapPosition)var1;
            if (this.geoPoint == null) {
               if (var3.geoPoint != null) {
                  var2 = false;
               }
            } else if (!this.geoPoint.equals(var3.geoPoint)) {
               var2 = false;
            } else if (this.zoomLevel != var3.zoomLevel) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public int hashCode() {
      int var1;
      if (this.geoPoint == null) {
         var1 = 0;
      } else {
         var1 = this.geoPoint.hashCode();
      }

      return (var1 + 31) * 31 + this.zoomLevel;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("geoPoint=");
      var1.append(this.geoPoint);
      var1.append(", zoomLevel=");
      var1.append(this.zoomLevel);
      return var1.toString();
   }
}
