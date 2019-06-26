package org.osmdroid.views.overlay;

import org.osmdroid.views.overlay.infowindow.InfoWindow;

public abstract class OverlayWithIW extends Overlay {
   protected InfoWindow mInfoWindow;
   protected Object mRelatedObject;
   protected String mSnippet;
   protected String mSubDescription;
   protected String mTitle;

   public void closeInfoWindow() {
      InfoWindow var1 = this.mInfoWindow;
      if (var1 != null) {
         var1.close();
      }

   }

   public String getSnippet() {
      return this.mSnippet;
   }

   public String getSubDescription() {
      return this.mSubDescription;
   }

   public String getTitle() {
      return this.mTitle;
   }

   public boolean isInfoWindowOpen() {
      InfoWindow var1 = this.mInfoWindow;
      boolean var2;
      if (var1 != null && var1.isOpen()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void onDestroy() {
      InfoWindow var1 = this.mInfoWindow;
      if (var1 != null) {
         var1.close();
         this.mInfoWindow.onDetach();
         this.mInfoWindow = null;
         this.mRelatedObject = null;
      }

   }

   public void setRelatedObject(Object var1) {
      this.mRelatedObject = var1;
   }
}
