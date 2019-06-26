package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class DefaultOverlayManager extends AbstractList implements OverlayManager {
   private final CopyOnWriteArrayList mOverlayList;
   private TilesOverlay mTilesOverlay;

   public DefaultOverlayManager(TilesOverlay var1) {
      this.setTilesOverlay(var1);
      this.mOverlayList = new CopyOnWriteArrayList();
   }

   private void onDrawHelper(Canvas var1, MapView var2, Projection var3) {
      TilesOverlay var4 = this.mTilesOverlay;
      if (var4 != null) {
         var4.protectDisplayedTilesForCache(var1, var3);
      }

      Iterator var5 = this.mOverlayList.iterator();

      while(var5.hasNext()) {
         Overlay var6 = (Overlay)var5.next();
         if (var6 != null && var6.isEnabled() && var6 instanceof TilesOverlay) {
            ((TilesOverlay)var6).protectDisplayedTilesForCache(var1, var3);
         }
      }

      var4 = this.mTilesOverlay;
      if (var4 != null && var4.isEnabled()) {
         if (var2 != null) {
            this.mTilesOverlay.draw(var1, var2, false);
         } else {
            this.mTilesOverlay.draw(var1, var3);
         }
      }

      Iterator var7 = this.mOverlayList.iterator();

      while(var7.hasNext()) {
         Overlay var8 = (Overlay)var7.next();
         if (var8 != null && var8.isEnabled()) {
            if (var2 != null) {
               var8.draw(var1, var2, false);
            } else {
               var8.draw(var1, var3);
            }
         }
      }

   }

   public void add(int var1, Overlay var2) {
      if (var2 == null) {
         Log.e("OsmDroid", "Attempt to add a null overlay to the collection. This is probably a bug and should be reported!", new Exception());
      } else {
         this.mOverlayList.add(var1, var2);
      }

   }

   public Overlay get(int var1) {
      return (Overlay)this.mOverlayList.get(var1);
   }

   public void onDetach(MapView var1) {
      TilesOverlay var2 = this.mTilesOverlay;
      if (var2 != null) {
         var2.onDetach(var1);
      }

      Iterator var3 = this.overlaysReversed().iterator();

      while(var3.hasNext()) {
         ((Overlay)var3.next()).onDetach(var1);
      }

      this.clear();
   }

   public boolean onDoubleTap(MotionEvent var1, MapView var2) {
      Iterator var3 = this.overlaysReversed().iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }
      } while(!((Overlay)var3.next()).onDoubleTap(var1, var2));

      return true;
   }

   public boolean onDoubleTapEvent(MotionEvent var1, MapView var2) {
      Iterator var3 = this.overlaysReversed().iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }
      } while(!((Overlay)var3.next()).onDoubleTapEvent(var1, var2));

      return true;
   }

   public boolean onDown(MotionEvent var1, MapView var2) {
      Iterator var3 = this.overlaysReversed().iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }
      } while(!((Overlay)var3.next()).onDown(var1, var2));

      return true;
   }

   public void onDraw(Canvas var1, MapView var2) {
      this.onDrawHelper(var1, var2, var2.getProjection());
   }

   public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4, MapView var5) {
      Iterator var6 = this.overlaysReversed().iterator();

      do {
         if (!var6.hasNext()) {
            return false;
         }
      } while(!((Overlay)var6.next()).onFling(var1, var2, var3, var4, var5));

      return true;
   }

   public boolean onKeyDown(int var1, KeyEvent var2, MapView var3) {
      Iterator var4 = this.overlaysReversed().iterator();

      do {
         if (!var4.hasNext()) {
            return false;
         }
      } while(!((Overlay)var4.next()).onKeyDown(var1, var2, var3));

      return true;
   }

   public boolean onKeyUp(int var1, KeyEvent var2, MapView var3) {
      Iterator var4 = this.overlaysReversed().iterator();

      do {
         if (!var4.hasNext()) {
            return false;
         }
      } while(!((Overlay)var4.next()).onKeyUp(var1, var2, var3));

      return true;
   }

   public boolean onLongPress(MotionEvent var1, MapView var2) {
      Iterator var3 = this.overlaysReversed().iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }
      } while(!((Overlay)var3.next()).onLongPress(var1, var2));

      return true;
   }

   public void onPause() {
      TilesOverlay var1 = this.mTilesOverlay;
      if (var1 != null) {
         var1.onPause();
      }

      Iterator var2 = this.overlaysReversed().iterator();

      while(var2.hasNext()) {
         ((Overlay)var2.next()).onPause();
      }

   }

   public void onResume() {
      TilesOverlay var1 = this.mTilesOverlay;
      if (var1 != null) {
         var1.onResume();
      }

      Iterator var2 = this.overlaysReversed().iterator();

      while(var2.hasNext()) {
         ((Overlay)var2.next()).onResume();
      }

   }

   public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4, MapView var5) {
      Iterator var6 = this.overlaysReversed().iterator();

      do {
         if (!var6.hasNext()) {
            return false;
         }
      } while(!((Overlay)var6.next()).onScroll(var1, var2, var3, var4, var5));

      return true;
   }

   public void onShowPress(MotionEvent var1, MapView var2) {
      Iterator var3 = this.overlaysReversed().iterator();

      while(var3.hasNext()) {
         ((Overlay)var3.next()).onShowPress(var1, var2);
      }

   }

   public boolean onSingleTapConfirmed(MotionEvent var1, MapView var2) {
      Iterator var3 = this.overlaysReversed().iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }
      } while(!((Overlay)var3.next()).onSingleTapConfirmed(var1, var2));

      return true;
   }

   public boolean onSingleTapUp(MotionEvent var1, MapView var2) {
      Iterator var3 = this.overlaysReversed().iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }
      } while(!((Overlay)var3.next()).onSingleTapUp(var1, var2));

      return true;
   }

   public boolean onSnapToItem(int var1, int var2, Point var3, IMapView var4) {
      Iterator var5 = this.overlaysReversed().iterator();

      Overlay var6;
      do {
         if (!var5.hasNext()) {
            return false;
         }

         var6 = (Overlay)var5.next();
      } while(!(var6 instanceof Overlay.Snappable) || !((Overlay.Snappable)var6).onSnapToItem(var1, var2, var3, var4));

      return true;
   }

   public boolean onTouchEvent(MotionEvent var1, MapView var2) {
      Iterator var3 = this.overlaysReversed().iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }
      } while(!((Overlay)var3.next()).onTouchEvent(var1, var2));

      return true;
   }

   public boolean onTrackballEvent(MotionEvent var1, MapView var2) {
      Iterator var3 = this.overlaysReversed().iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }
      } while(!((Overlay)var3.next()).onTrackballEvent(var1, var2));

      return true;
   }

   public List overlays() {
      return this.mOverlayList;
   }

   public Iterable overlaysReversed() {
      return new Iterable() {
         private ListIterator bulletProofReverseListIterator() {
            while(true) {
               try {
                  ListIterator var1 = DefaultOverlayManager.this.mOverlayList.listIterator(DefaultOverlayManager.this.mOverlayList.size());
                  return var1;
               } catch (IndexOutOfBoundsException var2) {
               }
            }
         }

         public Iterator iterator() {
            return new Iterator(this.bulletProofReverseListIterator()) {
               // $FF: synthetic field
               final ListIterator val$i;

               {
                  this.val$i = var2;
               }

               public boolean hasNext() {
                  return this.val$i.hasPrevious();
               }

               public Overlay next() {
                  return (Overlay)this.val$i.previous();
               }

               public void remove() {
                  this.val$i.remove();
               }
            };
         }
      };
   }

   public Overlay remove(int var1) {
      return (Overlay)this.mOverlayList.remove(var1);
   }

   public Overlay set(int var1, Overlay var2) {
      if (var2 == null) {
         Log.e("OsmDroid", "Attempt to set a null overlay to the collection. This is probably a bug and should be reported!", new Exception());
         return null;
      } else {
         return (Overlay)this.mOverlayList.set(var1, var2);
      }
   }

   public void setTilesOverlay(TilesOverlay var1) {
      this.mTilesOverlay = var1;
   }

   public int size() {
      return this.mOverlayList.size();
   }
}
