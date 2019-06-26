package org.mapsforge.core.model;

import java.io.Serializable;

public class Tile implements Serializable {
   public static final int TILE_SIZE = 256;
   private static final long serialVersionUID = 1L;
   public final long tileX;
   public final long tileY;
   public final byte zoomLevel;

   public Tile(long var1, long var3, byte var5) {
      this.tileX = var1;
      this.tileY = var3;
      this.zoomLevel = (byte)var5;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof Tile)) {
            var2 = false;
         } else {
            Tile var3 = (Tile)var1;
            if (this.tileX != var3.tileX) {
               var2 = false;
            } else if (this.tileY != var3.tileY) {
               var2 = false;
            } else if (this.zoomLevel != var3.zoomLevel) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public long getPixelX() {
      return this.tileX * 256L;
   }

   public long getPixelY() {
      return this.tileY * 256L;
   }

   public int hashCode() {
      return (((int)(this.tileX ^ this.tileX >>> 32) + 217) * 31 + (int)(this.tileY ^ this.tileY >>> 32)) * 31 + this.zoomLevel;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("tileX=");
      var1.append(this.tileX);
      var1.append(", tileY=");
      var1.append(this.tileY);
      var1.append(", zoomLevel=");
      var1.append(this.zoomLevel);
      return var1.toString();
   }
}
