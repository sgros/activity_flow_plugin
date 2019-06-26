package org.osmdroid.views.overlay.infowindow;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.drawing.MapSnapshot;

public abstract class InfoWindow {
   protected boolean mIsVisible;
   protected MapView mMapView;
   private int mOffsetX;
   private int mOffsetY;
   private GeoPoint mPosition;
   protected Object mRelatedObject;
   protected View mView;

   public InfoWindow(int var1, MapView var2) {
      this.mMapView = var2;
      this.mMapView.getRepository().add(this);
      this.mIsVisible = false;
      ViewGroup var3 = (ViewGroup)var2.getParent();
      this.mView = ((LayoutInflater)var2.getContext().getSystemService("layout_inflater")).inflate(var1, var3, false);
      this.mView.setTag(this);
   }

   public void close() {
      if (this.mIsVisible) {
         this.mIsVisible = false;
         ((ViewGroup)this.mView.getParent()).removeView(this.mView);
         this.onClose();
      }

   }

   public void draw() {
      if (this.mIsVisible) {
         try {
            MapView.LayoutParams var1 = new MapView.LayoutParams(-2, -2, this.mPosition, 8, this.mOffsetX, this.mOffsetY);
            this.mMapView.updateViewLayout(this.mView, var1);
         } catch (Exception var2) {
            if (MapSnapshot.isUIThread()) {
               throw var2;
            }
         }

      }
   }

   public boolean isOpen() {
      return this.mIsVisible;
   }

   public abstract void onClose();

   public void onDetach() {
      this.close();
      View var1 = this.mView;
      if (var1 != null) {
         var1.setTag((Object)null);
      }

      this.mView = null;
      this.mMapView = null;
      if (Configuration.getInstance().isDebugMode()) {
         Log.d("OsmDroid", "Marked detached");
      }

   }

   public abstract void onOpen(Object var1);

   public void open(Object var1, GeoPoint var2, int var3, int var4) {
      this.close();
      this.mRelatedObject = var1;
      this.mPosition = var2;
      this.mOffsetX = var3;
      this.mOffsetY = var4;
      this.onOpen(var1);
      MapView.LayoutParams var6 = new MapView.LayoutParams(-2, -2, this.mPosition, 8, this.mOffsetX, this.mOffsetY);
      MapView var9 = this.mMapView;
      if (var9 != null) {
         View var5 = this.mView;
         if (var5 != null) {
            var9.addView(var5, var6);
            this.mIsVisible = true;
            return;
         }
      }

      StringBuilder var11 = new StringBuilder();
      var11.append("Error trapped, InfoWindow.open mMapView: ");
      MapView var7 = this.mMapView;
      String var10 = "null";
      String var8;
      if (var7 == null) {
         var8 = "null";
      } else {
         var8 = "ok";
      }

      var11.append(var8);
      var11.append(" mView: ");
      if (this.mView == null) {
         var8 = var10;
      } else {
         var8 = "ok";
      }

      var11.append(var8);
      Log.w("OsmDroid", var11.toString());
   }
}
