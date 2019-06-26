package org.osmdroid.events;

import org.osmdroid.views.MapView;

public class ZoomEvent implements MapEvent {
   protected MapView source;
   protected double zoomLevel;

   public ZoomEvent(MapView var1, double var2) {
      this.source = var1;
      this.zoomLevel = var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ZoomEvent [source=");
      var1.append(this.source);
      var1.append(", zoomLevel=");
      var1.append(this.zoomLevel);
      var1.append("]");
      return var1.toString();
   }
}
