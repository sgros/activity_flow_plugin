package org.osmdroid.api;

public interface IMapController {
   void animateTo(IGeoPoint var1);

   void animateTo(IGeoPoint var1, Double var2, Long var3);

   void setCenter(IGeoPoint var1);

   double setZoom(double var1);

   void stopAnimation(boolean var1);

   boolean zoomIn();

   boolean zoomInFixing(int var1, int var2);

   boolean zoomOut();

   boolean zoomOutFixing(int var1, int var2);
}
