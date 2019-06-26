package org.osmdroid.events;

import org.osmdroid.views.MapView;

public class ScrollEvent implements MapEvent {
   protected MapView source;
   protected int x;
   protected int y;

   public ScrollEvent(MapView var1, int var2, int var3) {
      this.source = var1;
      this.x = var2;
      this.y = var3;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ScrollEvent [source=");
      var1.append(this.source);
      var1.append(", x=");
      var1.append(this.x);
      var1.append(", y=");
      var1.append(this.y);
      var1.append("]");
      return var1.toString();
   }
}
