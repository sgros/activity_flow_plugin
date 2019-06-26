package org.osmdroid.views;

import android.graphics.drawable.Drawable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.osmdroid.library.R$drawable;
import org.osmdroid.library.R$layout;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class MapViewRepository {
   private Drawable mDefaultMarkerIcon;
   private MarkerInfoWindow mDefaultMarkerInfoWindow;
   private BasicInfoWindow mDefaultPolygonInfoWindow;
   private BasicInfoWindow mDefaultPolylineInfoWindow;
   private final Set mInfoWindowList = new HashSet();
   private MapView mMapView;

   public MapViewRepository(MapView var1) {
      this.mMapView = var1;
   }

   public void add(InfoWindow var1) {
      this.mInfoWindowList.add(var1);
   }

   public Drawable getDefaultMarkerIcon() {
      if (this.mDefaultMarkerIcon == null) {
         this.mDefaultMarkerIcon = this.mMapView.getContext().getResources().getDrawable(R$drawable.marker_default);
      }

      return this.mDefaultMarkerIcon;
   }

   public MarkerInfoWindow getDefaultMarkerInfoWindow() {
      if (this.mDefaultMarkerInfoWindow == null) {
         this.mDefaultMarkerInfoWindow = new MarkerInfoWindow(R$layout.bonuspack_bubble, this.mMapView);
      }

      return this.mDefaultMarkerInfoWindow;
   }

   public void onDetach() {
      Set var1 = this.mInfoWindowList;
      synchronized(var1){}

      label215: {
         Throwable var10000;
         boolean var10001;
         label210: {
            Iterator var2;
            try {
               var2 = this.mInfoWindowList.iterator();
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label210;
            }

            while(true) {
               try {
                  if (var2.hasNext()) {
                     ((InfoWindow)var2.next()).onDetach();
                     continue;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }

               try {
                  this.mInfoWindowList.clear();
                  break label215;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break;
               }
            }
         }

         while(true) {
            Throwable var23 = var10000;

            try {
               throw var23;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      }

      this.mMapView = null;
      this.mDefaultMarkerInfoWindow = null;
      this.mDefaultPolylineInfoWindow = null;
      this.mDefaultPolygonInfoWindow = null;
      this.mDefaultMarkerIcon = null;
   }
}
