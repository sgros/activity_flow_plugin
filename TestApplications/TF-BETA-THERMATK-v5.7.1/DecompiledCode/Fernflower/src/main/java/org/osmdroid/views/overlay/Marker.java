package org.osmdroid.views.overlay;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.MotionEvent;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.RectL;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapViewRepository;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class Marker extends OverlayWithIW {
   protected float mAlpha;
   protected float mAnchorU;
   protected float mAnchorV;
   protected float mBearing;
   private boolean mDisplayed;
   protected float mDragOffsetY;
   protected boolean mDraggable;
   protected boolean mFlat;
   protected float mIWAnchorU;
   protected float mIWAnchorV;
   protected Drawable mIcon;
   protected Drawable mImage;
   protected boolean mIsDragged;
   private MapViewRepository mMapViewRepository;
   protected Marker.OnMarkerClickListener mOnMarkerClickListener;
   protected Marker.OnMarkerDragListener mOnMarkerDragListener;
   private final Rect mOrientedMarkerRect;
   private Paint mPaint;
   protected boolean mPanToView;
   protected GeoPoint mPosition;
   protected Point mPositionPixels;
   private final Rect mRect;
   protected Resources mResources;
   protected int mTextLabelBackgroundColor;
   protected int mTextLabelFontSize;
   protected int mTextLabelForegroundColor;

   public Marker(MapView var1) {
      this(var1, var1.getContext());
   }

   public Marker(MapView var1, Context var2) {
      this.mTextLabelBackgroundColor = -1;
      this.mTextLabelForegroundColor = -16777216;
      this.mTextLabelFontSize = 24;
      this.mRect = new Rect();
      this.mOrientedMarkerRect = new Rect();
      this.mMapViewRepository = var1.getRepository();
      this.mResources = var1.getContext().getResources();
      this.mBearing = 0.0F;
      this.mAlpha = 1.0F;
      this.mPosition = new GeoPoint(0.0D, 0.0D);
      this.mAnchorU = 0.5F;
      this.mAnchorV = 0.5F;
      this.mIWAnchorU = 0.5F;
      this.mIWAnchorV = 0.0F;
      this.mDraggable = false;
      this.mIsDragged = false;
      this.mPositionPixels = new Point();
      this.mPanToView = true;
      this.mDragOffsetY = 0.0F;
      this.mFlat = false;
      this.mOnMarkerClickListener = null;
      this.mOnMarkerDragListener = null;
      this.setDefaultIcon();
      this.setInfoWindow(this.mMapViewRepository.getDefaultMarkerInfoWindow());
   }

   public void draw(Canvas var1, Projection var2) {
      if (this.mIcon != null) {
         var2.toPixels(this.mPosition, this.mPositionPixels);
         float var3;
         if (this.mFlat) {
            var3 = -this.mBearing;
         } else {
            var3 = -var2.getOrientation() - this.mBearing;
         }

         Point var4 = this.mPositionPixels;
         this.drawAt(var1, var4.x, var4.y, var3);
         if (this.isInfoWindowShown()) {
            super.mInfoWindow.draw();
         }

      }
   }

   protected void drawAt(Canvas var1, int var2, int var3, float var4) {
      int var5 = this.mIcon.getIntrinsicWidth();
      int var6 = this.mIcon.getIntrinsicHeight();
      int var7 = var2 - Math.round((float)var5 * this.mAnchorU);
      int var8 = var3 - Math.round((float)var6 * this.mAnchorV);
      this.mRect.set(var7, var8, var5 + var7, var6 + var8);
      RectL.getBounds(this.mRect, var2, var3, (double)var4, this.mOrientedMarkerRect);
      this.mDisplayed = Rect.intersects(this.mOrientedMarkerRect, var1.getClipBounds());
      if (this.mDisplayed) {
         if (this.mAlpha != 0.0F) {
            if (var4 != 0.0F) {
               var1.save();
               var1.rotate(var4, (float)var2, (float)var3);
            }

            Drawable var9 = this.mIcon;
            if (var9 instanceof BitmapDrawable) {
               Paint var10;
               if (this.mAlpha == 1.0F) {
                  var10 = null;
               } else {
                  if (this.mPaint == null) {
                     this.mPaint = new Paint();
                  }

                  this.mPaint.setAlpha((int)(this.mAlpha * 255.0F));
                  var10 = this.mPaint;
               }

               var1.drawBitmap(((BitmapDrawable)this.mIcon).getBitmap(), (float)var7, (float)var8, var10);
            } else {
               var9.setAlpha((int)(this.mAlpha * 255.0F));
               this.mIcon.setBounds(this.mRect);
               this.mIcon.draw(var1);
            }

            if (var4 != 0.0F) {
               var1.restore();
            }

         }
      }
   }

   public Drawable getImage() {
      return this.mImage;
   }

   public GeoPoint getPosition() {
      return this.mPosition;
   }

   public boolean hitTest(MotionEvent var1, MapView var2) {
      boolean var3;
      if (this.mIcon != null && this.mDisplayed && this.mOrientedMarkerRect.contains((int)var1.getX(), (int)var1.getY())) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isInfoWindowShown() {
      InfoWindow var1 = super.mInfoWindow;
      if (!(var1 instanceof MarkerInfoWindow)) {
         return super.isInfoWindowOpen();
      } else {
         MarkerInfoWindow var3 = (MarkerInfoWindow)var1;
         boolean var2;
         if (var3 != null && var3.isOpen() && var3.getMarkerReference() == this) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   public void moveToEventPosition(MotionEvent var1, MapView var2) {
      float var3 = TypedValue.applyDimension(5, this.mDragOffsetY, var2.getContext().getResources().getDisplayMetrics());
      this.mPosition = (GeoPoint)var2.getProjection().fromPixels((int)var1.getX(), (int)(var1.getY() - var3));
      var2.invalidate();
   }

   public void onDetach(MapView var1) {
      BitmapPool.getInstance().asyncRecycle(this.mIcon);
      this.mIcon = null;
      BitmapPool.getInstance().asyncRecycle(this.mImage);
      this.mOnMarkerClickListener = null;
      this.mOnMarkerDragListener = null;
      this.mResources = null;
      this.setRelatedObject((Object)null);
      if (this.isInfoWindowShown()) {
         this.closeInfoWindow();
      }

      this.mMapViewRepository = null;
      this.setInfoWindow((MarkerInfoWindow)null);
      this.onDestroy();
      super.onDetach(var1);
   }

   public boolean onLongPress(MotionEvent var1, MapView var2) {
      boolean var3 = this.hitTest(var1, var2);
      if (var3 && this.mDraggable) {
         this.mIsDragged = true;
         this.closeInfoWindow();
         Marker.OnMarkerDragListener var4 = this.mOnMarkerDragListener;
         if (var4 != null) {
            var4.onMarkerDragStart(this);
         }

         this.moveToEventPosition(var1, var2);
      }

      return var3;
   }

   protected boolean onMarkerClickDefault(Marker var1, MapView var2) {
      var1.showInfoWindow();
      if (var1.mPanToView) {
         var2.getController().animateTo(var1.getPosition());
      }

      return true;
   }

   public boolean onSingleTapConfirmed(MotionEvent var1, MapView var2) {
      boolean var3 = this.hitTest(var1, var2);
      boolean var4 = var3;
      if (var3) {
         Marker.OnMarkerClickListener var5 = this.mOnMarkerClickListener;
         if (var5 == null) {
            return this.onMarkerClickDefault(this, var2);
         }

         var4 = var5.onMarkerClick(this, var2);
      }

      return var4;
   }

   public boolean onTouchEvent(MotionEvent var1, MapView var2) {
      if (this.mDraggable && this.mIsDragged) {
         Marker.OnMarkerDragListener var3;
         if (var1.getAction() == 1) {
            this.mIsDragged = false;
            var3 = this.mOnMarkerDragListener;
            if (var3 != null) {
               var3.onMarkerDragEnd(this);
            }

            return true;
         }

         if (var1.getAction() == 2) {
            this.moveToEventPosition(var1, var2);
            var3 = this.mOnMarkerDragListener;
            if (var3 != null) {
               var3.onMarkerDrag(this);
            }

            return true;
         }
      }

      return false;
   }

   public void setAnchor(float var1, float var2) {
      this.mAnchorU = var1;
      this.mAnchorV = var2;
   }

   public void setDefaultIcon() {
      this.mIcon = this.mMapViewRepository.getDefaultMarkerIcon();
      this.setAnchor(0.5F, 1.0F);
   }

   public void setIcon(Drawable var1) {
      if (var1 != null) {
         this.mIcon = var1;
      } else {
         this.setDefaultIcon();
      }

   }

   public void setInfoWindow(MarkerInfoWindow var1) {
      super.mInfoWindow = var1;
   }

   public void setOnMarkerClickListener(Marker.OnMarkerClickListener var1) {
      this.mOnMarkerClickListener = var1;
   }

   public void setPosition(GeoPoint var1) {
      this.mPosition = var1.clone();
      if (this.isInfoWindowShown()) {
         this.closeInfoWindow();
         this.showInfoWindow();
      }

   }

   public void showInfoWindow() {
      if (super.mInfoWindow != null) {
         int var1 = this.mIcon.getIntrinsicWidth();
         int var2 = this.mIcon.getIntrinsicHeight();
         var1 = (int)((float)var1 * (this.mIWAnchorU - this.mAnchorU));
         var2 = (int)((float)var2 * (this.mIWAnchorV - this.mAnchorV));
         float var3 = this.mBearing;
         if (var3 == 0.0F) {
            super.mInfoWindow.open(this, this.mPosition, var1, var2);
         } else {
            double var4 = (double)(-var3);
            Double.isNaN(var4);
            double var6 = var4 * 3.141592653589793D / 180.0D;
            var4 = Math.cos(var6);
            var6 = Math.sin(var6);
            long var8 = (long)var1;
            long var10 = (long)var2;
            var2 = (int)RectL.getRotatedX(var8, var10, 0L, 0L, var4, var6);
            var1 = (int)RectL.getRotatedY(var8, var10, 0L, 0L, var4, var6);
            super.mInfoWindow.open(this, this.mPosition, var2, var1);
         }
      }
   }

   public interface OnMarkerClickListener {
      boolean onMarkerClick(Marker var1, MapView var2);
   }

   public interface OnMarkerDragListener {
      void onMarkerDrag(Marker var1);

      void onMarkerDragEnd(Marker var1);

      void onMarkerDragStart(Marker var1);
   }
}
