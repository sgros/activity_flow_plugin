package org.osmdroid.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import java.util.Iterator;
import java.util.LinkedList;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MyMath;
import org.osmdroid.util.TileSystem;

public class MapController implements IMapController, MapView.OnFirstLayoutListener {
   private Animator mCurrentAnimator;
   protected final MapView mMapView;
   private MapController.ReplayController mReplayController;
   private double mTargetZoomLevel = 0.0D;
   private ScaleAnimation mZoomInAnimationOld;
   private ScaleAnimation mZoomOutAnimationOld;

   public MapController(MapView var1) {
      this.mMapView = var1;
      this.mReplayController = new MapController.ReplayController();
      if (!this.mMapView.isLayoutOccurred()) {
         this.mMapView.addOnFirstLayoutListener(this);
      }

      if (VERSION.SDK_INT < 11) {
         MapController.ZoomAnimationListener var2 = new MapController.ZoomAnimationListener(this);
         this.mZoomInAnimationOld = new ScaleAnimation(1.0F, 2.0F, 1.0F, 2.0F, 1, 0.5F, 1, 0.5F);
         this.mZoomOutAnimationOld = new ScaleAnimation(1.0F, 0.5F, 1.0F, 0.5F, 1, 0.5F, 1, 0.5F);
         this.mZoomInAnimationOld.setDuration((long)Configuration.getInstance().getAnimationSpeedShort());
         this.mZoomOutAnimationOld.setDuration((long)Configuration.getInstance().getAnimationSpeedShort());
         this.mZoomInAnimationOld.setAnimationListener(var2);
         this.mZoomOutAnimationOld.setAnimationListener(var2);
      }

   }

   public void animateTo(int var1, int var2) {
      if (!this.mMapView.isLayoutOccurred()) {
         this.mReplayController.animateTo(var1, var2);
      } else {
         if (!this.mMapView.isAnimating()) {
            MapView var3 = this.mMapView;
            var3.mIsFlinging = false;
            int var4 = (int)var3.getMapScrollX();
            int var5 = (int)this.mMapView.getMapScrollY();
            var1 -= this.mMapView.getWidth() / 2;
            var2 -= this.mMapView.getHeight() / 2;
            if (var1 != var4 || var2 != var5) {
               this.mMapView.getScroller().startScroll(var4, var5, var1, var2, Configuration.getInstance().getAnimationSpeedDefault());
               this.mMapView.postInvalidate();
            }
         }

      }
   }

   public void animateTo(IGeoPoint var1) {
      this.animateTo(var1, (Double)null, (Long)null);
   }

   public void animateTo(IGeoPoint var1, Double var2, Long var3) {
      this.animateTo(var1, var2, var3, (Float)null);
   }

   public void animateTo(IGeoPoint var1, Double var2, Long var3, Float var4) {
      this.animateTo(var1, var2, var3, var4, (Boolean)null);
   }

   public void animateTo(IGeoPoint var1, Double var2, Long var3, Float var4, Boolean var5) {
      if (!this.mMapView.isLayoutOccurred()) {
         this.mReplayController.animateTo(var1, var2, var3, var4, var5);
      } else if (VERSION.SDK_INT >= 11) {
         GeoPoint var6 = new GeoPoint(this.mMapView.getProjection().getCurrentCenter());
         MapController.MapAnimatorListener var9 = new MapController.MapAnimatorListener(this, this.mMapView.getZoomLevelDouble(), var2, var6, var1, this.mMapView.getMapOrientation(), var4, var5);
         ValueAnimator var8 = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
         var8.addListener(var9);
         var8.addUpdateListener(var9);
         if (var3 == null) {
            var8.setDuration((long)Configuration.getInstance().getAnimationSpeedDefault());
         } else {
            var8.setDuration(var3);
         }

         Animator var10 = this.mCurrentAnimator;
         if (var10 != null) {
            var10.end();
         }

         this.mCurrentAnimator = var8;
         var8.start();
      } else {
         Point var7 = this.mMapView.getProjection().toPixels(var1, (Point)null);
         this.animateTo(var7.x, var7.y);
      }
   }

   protected void onAnimationEnd() {
      this.mMapView.mIsAnimating.set(false);
      this.mMapView.resetMultiTouchScale();
      if (VERSION.SDK_INT >= 11) {
         this.mCurrentAnimator = null;
      } else {
         this.mMapView.clearAnimation();
         this.mZoomInAnimationOld.reset();
         this.mZoomOutAnimationOld.reset();
         this.setZoom(this.mTargetZoomLevel);
      }

      this.mMapView.invalidate();
   }

   protected void onAnimationStart() {
      this.mMapView.mIsAnimating.set(true);
   }

   public void onFirstLayout(View var1, int var2, int var3, int var4, int var5) {
      this.mReplayController.replayCalls();
   }

   public void setCenter(IGeoPoint var1) {
      if (!this.mMapView.isLayoutOccurred()) {
         this.mReplayController.setCenter(var1);
      } else {
         this.mMapView.setExpectedCenter(var1);
      }
   }

   public double setZoom(double var1) {
      return this.mMapView.setZoomLevel(var1);
   }

   public void stopAnimation(boolean var1) {
      if (!this.mMapView.getScroller().isFinished()) {
         if (var1) {
            MapView var2 = this.mMapView;
            var2.mIsFlinging = false;
            var2.getScroller().abortAnimation();
         } else {
            this.stopPanning();
         }
      }

      if (VERSION.SDK_INT >= 11) {
         Animator var3 = this.mCurrentAnimator;
         if (this.mMapView.mIsAnimating.get()) {
            if (var1) {
               var3.end();
            } else {
               var3.cancel();
            }
         }
      } else if (this.mMapView.mIsAnimating.get()) {
         this.mMapView.clearAnimation();
      }

   }

   public void stopPanning() {
      MapView var1 = this.mMapView;
      var1.mIsFlinging = false;
      var1.getScroller().forceFinished(true);
   }

   public boolean zoomIn() {
      return this.zoomIn((Long)null);
   }

   public boolean zoomIn(Long var1) {
      return this.zoomTo(this.mMapView.getZoomLevelDouble() + 1.0D, var1);
   }

   public boolean zoomInFixing(int var1, int var2) {
      return this.zoomInFixing(var1, var2, (Long)null);
   }

   public boolean zoomInFixing(int var1, int var2, Long var3) {
      return this.zoomToFixing(this.mMapView.getZoomLevelDouble() + 1.0D, var1, var2, var3);
   }

   public boolean zoomOut() {
      return this.zoomOut((Long)null);
   }

   public boolean zoomOut(Long var1) {
      return this.zoomTo(this.mMapView.getZoomLevelDouble() - 1.0D, var1);
   }

   @Deprecated
   public boolean zoomOutFixing(int var1, int var2) {
      return this.zoomToFixing(this.mMapView.getZoomLevelDouble() - 1.0D, var1, var2, (Long)null);
   }

   public boolean zoomTo(double var1, Long var3) {
      return this.zoomToFixing(var1, this.mMapView.getWidth() / 2, this.mMapView.getHeight() / 2, var3);
   }

   public boolean zoomToFixing(double var1, int var3, int var4, Long var5) {
      if (var1 > this.mMapView.getMaxZoomLevel()) {
         var1 = this.mMapView.getMaxZoomLevel();
      }

      double var6 = var1;
      if (var1 < this.mMapView.getMinZoomLevel()) {
         var6 = this.mMapView.getMinZoomLevel();
      }

      var1 = this.mMapView.getZoomLevelDouble();
      boolean var8;
      if ((var6 >= var1 || !this.mMapView.canZoomOut()) && (var6 <= var1 || !this.mMapView.canZoomIn())) {
         var8 = false;
      } else {
         var8 = true;
      }

      if (!var8) {
         return false;
      } else if (this.mMapView.mIsAnimating.getAndSet(true)) {
         return false;
      } else {
         ZoomEvent var9 = null;

         MapListener var11;
         for(Iterator var10 = this.mMapView.mListners.iterator(); var10.hasNext(); var11.onZoom(var9)) {
            var11 = (MapListener)var10.next();
            if (var9 == null) {
               var9 = new ZoomEvent(this.mMapView, var6);
            }
         }

         this.mMapView.setMultiTouchScaleInitPoint((float)var3, (float)var4);
         this.mMapView.startAnimation();
         float var12 = (float)Math.pow(2.0D, var6 - var1);
         if (VERSION.SDK_INT >= 11) {
            MapController.MapAnimatorListener var14 = new MapController.MapAnimatorListener(this, var1, var6, (IGeoPoint)null, (IGeoPoint)null, (Float)null, (Float)null, (Boolean)null);
            ValueAnimator var15 = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
            var15.addListener(var14);
            var15.addUpdateListener(var14);
            if (var5 == null) {
               var15.setDuration((long)Configuration.getInstance().getAnimationSpeedShort());
            } else {
               var15.setDuration(var5);
            }

            this.mCurrentAnimator = var15;
            var15.start();
            return true;
         } else {
            this.mTargetZoomLevel = var6;
            if (var6 > var1) {
               this.mMapView.startAnimation(this.mZoomInAnimationOld);
            } else {
               this.mMapView.startAnimation(this.mZoomOutAnimationOld);
            }

            ScaleAnimation var13 = new ScaleAnimation(1.0F, var12, 1.0F, var12, 1, 0.5F, 1, 0.5F);
            if (var5 == null) {
               var13.setDuration((long)Configuration.getInstance().getAnimationSpeedShort());
            } else {
               var13.setDuration(var5);
            }

            var13.setAnimationListener(new MapController.ZoomAnimationListener(this));
            return true;
         }
      }
   }

   public void zoomToSpan(double var1, double var3) {
      if (var1 > 0.0D && var3 > 0.0D) {
         if (!this.mMapView.isLayoutOccurred()) {
            this.mReplayController.zoomToSpan(var1, var3);
            return;
         }

         BoundingBox var5 = this.mMapView.getProjection().getBoundingBox();
         double var6 = this.mMapView.getProjection().getZoomLevel();
         double var8 = var5.getLatitudeSpan();
         double var10 = var5.getLongitudeSpan();
         var1 = Math.max(var1 / var8, var3 / var10);
         MapView var12;
         if (var1 > 1.0D) {
            var12 = this.mMapView;
            var1 = (double)MyMath.getNextSquareNumberAbove((float)var1);
            Double.isNaN(var1);
            var12.setZoomLevel(var6 - var1);
         } else if (var1 < 0.5D) {
            var12 = this.mMapView;
            var1 = (double)MyMath.getNextSquareNumberAbove(1.0F / (float)var1);
            Double.isNaN(var1);
            var12.setZoomLevel(var6 + var1 - 1.0D);
         }
      }

   }

   public void zoomToSpan(int var1, int var2) {
      double var3 = (double)var1;
      Double.isNaN(var3);
      double var5 = (double)var2;
      Double.isNaN(var5);
      this.zoomToSpan(var3 * 1.0E-6D, var5 * 1.0E-6D);
   }

   @TargetApi(11)
   private static class MapAnimatorListener implements AnimatorListener, AnimatorUpdateListener {
      private final GeoPoint mCenter = new GeoPoint(0.0D, 0.0D);
      private final IGeoPoint mCenterEnd;
      private final IGeoPoint mCenterStart;
      private final MapController mMapController;
      private final Float mOrientationSpan;
      private final Float mOrientationStart;
      private final Double mZoomEnd;
      private final Double mZoomStart;

      public MapAnimatorListener(MapController var1, Double var2, Double var3, IGeoPoint var4, IGeoPoint var5, Float var6, Float var7, Boolean var8) {
         this.mMapController = var1;
         this.mZoomStart = var2;
         this.mZoomEnd = var3;
         this.mCenterStart = var4;
         this.mCenterEnd = var5;
         if (var7 == null) {
            this.mOrientationStart = null;
            this.mOrientationSpan = null;
         } else {
            this.mOrientationStart = var6;
            this.mOrientationSpan = (float)MyMath.getAngleDifference((double)this.mOrientationStart, (double)var7, var8);
         }

      }

      public void onAnimationCancel(Animator var1) {
         this.mMapController.onAnimationEnd();
      }

      public void onAnimationEnd(Animator var1) {
         this.mMapController.onAnimationEnd();
      }

      public void onAnimationRepeat(Animator var1) {
      }

      public void onAnimationStart(Animator var1) {
         this.mMapController.onAnimationStart();
      }

      public void onAnimationUpdate(ValueAnimator var1) {
         float var2 = (Float)var1.getAnimatedValue();
         double var3;
         double var5;
         double var7;
         double var9;
         if (this.mZoomEnd != null) {
            var3 = this.mZoomStart;
            var5 = this.mZoomEnd;
            var7 = this.mZoomStart;
            var9 = (double)var2;
            Double.isNaN(var9);
            this.mMapController.mMapView.setZoomLevel(var3 + (var5 - var7) * var9);
         }

         if (this.mOrientationSpan != null) {
            float var11 = this.mOrientationStart;
            float var12 = this.mOrientationSpan;
            this.mMapController.mMapView.setMapOrientation(var11 + var12 * var2);
         }

         if (this.mCenterEnd != null) {
            MapView var13 = this.mMapController.mMapView;
            TileSystem var14 = MapView.getTileSystem();
            var7 = var14.cleanLongitude(this.mCenterStart.getLongitude());
            var5 = var14.cleanLongitude(this.mCenterEnd.getLongitude());
            var3 = (double)var2;
            Double.isNaN(var3);
            var5 = var14.cleanLongitude(var7 + (var5 - var7) * var3);
            var9 = var14.cleanLatitude(this.mCenterStart.getLatitude());
            var7 = var14.cleanLatitude(this.mCenterEnd.getLatitude());
            Double.isNaN(var3);
            var3 = var14.cleanLatitude(var9 + (var7 - var9) * var3);
            this.mCenter.setCoords(var3, var5);
            this.mMapController.mMapView.setExpectedCenter(this.mCenter);
         }

         this.mMapController.mMapView.invalidate();
      }
   }

   private class ReplayController {
      private LinkedList mReplayList;

      private ReplayController() {
         this.mReplayList = new LinkedList();
      }

      // $FF: synthetic method
      ReplayController(Object var2) {
         this();
      }

      public void animateTo(int var1, int var2) {
         this.mReplayList.add(new MapController.ReplayController.ReplayClass(MapController.ReplayType.AnimateToPoint, new Point(var1, var2), (IGeoPoint)null));
      }

      public void animateTo(IGeoPoint var1, Double var2, Long var3, Float var4, Boolean var5) {
         this.mReplayList.add(new MapController.ReplayController.ReplayClass(MapController.ReplayType.AnimateToGeoPoint, (Point)null, var1, var2, var3, var4, var5));
      }

      public void replayCalls() {
         Iterator var1 = this.mReplayList.iterator();

         while(var1.hasNext()) {
            MapController.ReplayController.ReplayClass var2 = (MapController.ReplayController.ReplayClass)var1.next();
            int var3 = null.$SwitchMap$org$osmdroid$views$MapController$ReplayType[var2.mReplayType.ordinal()];
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 != 3) {
                     if (var3 == 4 && var2.mPoint != null) {
                        MapController.this.zoomToSpan(var2.mPoint.x, var2.mPoint.y);
                     }
                  } else if (var2.mGeoPoint != null) {
                     MapController.this.setCenter(var2.mGeoPoint);
                  }
               } else if (var2.mPoint != null) {
                  MapController.this.animateTo(var2.mPoint.x, var2.mPoint.y);
               }
            } else if (var2.mGeoPoint != null) {
               MapController.this.animateTo(var2.mGeoPoint, var2.mZoom, var2.mSpeed, var2.mOrientation, var2.mClockwise);
            }
         }

         this.mReplayList.clear();
      }

      public void setCenter(IGeoPoint var1) {
         this.mReplayList.add(new MapController.ReplayController.ReplayClass(MapController.ReplayType.SetCenterPoint, (Point)null, var1));
      }

      public void zoomToSpan(double var1, double var3) {
         this.mReplayList.add(new MapController.ReplayController.ReplayClass(MapController.ReplayType.ZoomToSpanPoint, new Point((int)(var1 * 1000000.0D), (int)(var3 * 1000000.0D)), (IGeoPoint)null));
      }

      private class ReplayClass {
         private final Boolean mClockwise;
         private IGeoPoint mGeoPoint;
         private final Float mOrientation;
         private Point mPoint;
         private MapController.ReplayType mReplayType;
         private final Long mSpeed;
         private final Double mZoom;

         public ReplayClass(MapController.ReplayType var2, Point var3, IGeoPoint var4) {
            this(var2, var3, var4, (Double)null, (Long)null, (Float)null, (Boolean)null);
         }

         public ReplayClass(MapController.ReplayType var2, Point var3, IGeoPoint var4, Double var5, Long var6, Float var7, Boolean var8) {
            this.mReplayType = var2;
            this.mPoint = var3;
            this.mGeoPoint = var4;
            this.mSpeed = var6;
            this.mZoom = var5;
            this.mOrientation = var7;
            this.mClockwise = var8;
         }
      }
   }

   private static enum ReplayType {
      AnimateToGeoPoint,
      AnimateToPoint,
      SetCenterPoint,
      ZoomToSpanPoint;
   }

   protected static class ZoomAnimationListener implements AnimationListener {
      private MapController mMapController;

      public ZoomAnimationListener(MapController var1) {
         this.mMapController = var1;
      }

      public void onAnimationEnd(Animation var1) {
         this.mMapController.onAnimationEnd();
      }

      public void onAnimationRepeat(Animation var1) {
      }

      public void onAnimationStart(Animation var1) {
         this.mMapController.onAnimationStart();
      }
   }
}
