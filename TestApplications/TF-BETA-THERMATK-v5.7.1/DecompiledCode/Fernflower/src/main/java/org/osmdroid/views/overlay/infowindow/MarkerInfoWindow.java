package org.osmdroid.views.overlay.infowindow;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MarkerInfoWindow extends BasicInfoWindow {
   protected Marker mMarkerRef;

   public MarkerInfoWindow(int var1, MapView var2) {
      super(var1, var2);
   }

   public Marker getMarkerReference() {
      return this.mMarkerRef;
   }

   public void onClose() {
      super.onClose();
      this.mMarkerRef = null;
   }

   public void onOpen(Object var1) {
      super.onOpen(var1);
      this.mMarkerRef = (Marker)var1;
      View var3 = super.mView;
      if (var3 == null) {
         Log.w("OsmDroid", "Error trapped, MarkerInfoWindow.open, mView is null!");
      } else {
         ImageView var2 = (ImageView)var3.findViewById(BasicInfoWindow.mImageId);
         Drawable var4 = this.mMarkerRef.getImage();
         if (var4 != null) {
            var2.setImageDrawable(var4);
            var2.setScaleType(ScaleType.CENTER_INSIDE);
            var2.setVisibility(0);
         } else {
            var2.setVisibility(8);
         }

      }
   }
}
