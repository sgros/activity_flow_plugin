package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.util.concurrent.atomic.AtomicInteger;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.util.constants.OverlayConstants;

public abstract class Overlay implements OverlayConstants {
   protected static final float SHADOW_X_SKEW = -0.9F;
   protected static final float SHADOW_Y_SCALE = 0.5F;
   private static final Rect mRect = new Rect();
   private static AtomicInteger sOrdinal = new AtomicInteger();
   protected BoundingBox mBounds;
   private boolean mEnabled = true;
   private final TileSystem tileSystem = MapView.getTileSystem();

   public Overlay() {
      this.mBounds = new BoundingBox(this.tileSystem.getMaxLatitude(), this.tileSystem.getMaxLongitude(), this.tileSystem.getMinLatitude(), this.tileSystem.getMinLongitude());
   }

   @Deprecated
   public Overlay(Context var1) {
      this.mBounds = new BoundingBox(this.tileSystem.getMaxLatitude(), this.tileSystem.getMaxLongitude(), this.tileSystem.getMinLatitude(), this.tileSystem.getMinLongitude());
   }

   protected static void drawAt(Canvas var0, Drawable var1, int var2, int var3, boolean var4, float var5) {
      synchronized(Overlay.class){}

      try {
         var0.save();
         var0.rotate(-var5, (float)var2, (float)var3);
         var1.copyBounds(mRect);
         var1.setBounds(mRect.left + var2, mRect.top + var3, mRect.right + var2, mRect.bottom + var3);
         var1.draw(var0);
         var1.setBounds(mRect);
         var0.restore();
      } finally {
         ;
      }

   }

   protected static final int getSafeMenuId() {
      return sOrdinal.getAndIncrement();
   }

   protected static final int getSafeMenuIdSequence(int var0) {
      return sOrdinal.getAndAdd(var0);
   }

   public void draw(Canvas var1, MapView var2, boolean var3) {
      if (!var3) {
         this.draw(var1, var2.getProjection());
      }
   }

   public void draw(Canvas var1, Projection var2) {
   }

   public BoundingBox getBounds() {
      return this.mBounds;
   }

   public boolean isEnabled() {
      return this.mEnabled;
   }

   public void onDetach(MapView var1) {
   }

   public boolean onDoubleTap(MotionEvent var1, MapView var2) {
      return false;
   }

   public boolean onDoubleTapEvent(MotionEvent var1, MapView var2) {
      return false;
   }

   public boolean onDown(MotionEvent var1, MapView var2) {
      return false;
   }

   public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4, MapView var5) {
      return false;
   }

   public boolean onKeyDown(int var1, KeyEvent var2, MapView var3) {
      return false;
   }

   public boolean onKeyUp(int var1, KeyEvent var2, MapView var3) {
      return false;
   }

   public boolean onLongPress(MotionEvent var1, MapView var2) {
      return false;
   }

   public void onPause() {
   }

   public void onResume() {
   }

   public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4, MapView var5) {
      return false;
   }

   public void onShowPress(MotionEvent var1, MapView var2) {
   }

   public boolean onSingleTapConfirmed(MotionEvent var1, MapView var2) {
      return false;
   }

   public boolean onSingleTapUp(MotionEvent var1, MapView var2) {
      return false;
   }

   public boolean onTouchEvent(MotionEvent var1, MapView var2) {
      return false;
   }

   public boolean onTrackballEvent(MotionEvent var1, MapView var2) {
      return false;
   }

   public void setEnabled(boolean var1) {
      this.mEnabled = var1;
   }

   public interface Snappable {
      boolean onSnapToItem(int var1, int var2, Point var3, IMapView var4);
   }
}
