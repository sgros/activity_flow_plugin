package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import java.util.HashMap;
import menion.android.whereyougo.maps.mapsforge.TapEventListener;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.model.GeoPoint;

public class PointListOverlay extends ListOverlay {
   HashMap hitMap = new HashMap();
   TapEventListener onTapListener;

   public boolean checkItemHit(GeoPoint var1, MapView var2) {
      synchronized(this){}

      Throwable var10000;
      label1692: {
         Projection var4;
         Point var5;
         boolean var10001;
         try {
            StringBuilder var3 = new StringBuilder();
            Log.e("litezee", var3.append("check hit ").append(var1.latitude).append(" ").append(var1.longitude).toString());
            var4 = var2.getProjection();
            var5 = var4.toPixels(var1, (Point)null);
         } catch (Throwable var199) {
            var10000 = var199;
            var10001 = false;
            break label1692;
         }

         boolean var6;
         if (var5 == null) {
            var6 = false;
            return var6;
         }

         int var7;
         Point var203;
         try {
            var203 = new Point();
            var7 = this.getOverlayItems().size() - 1;
         } catch (Throwable var198) {
            var10000 = var198;
            var10001 = false;
            break label1692;
         }

         while(true) {
            if (var7 < 0) {
               var6 = false;
               return var6;
            }

            label1676: {
               Point var202;
               label1694: {
                  OverlayItem var201;
                  label1674: {
                     try {
                        var201 = (OverlayItem)this.getOverlayItems().get(var7);
                        if (var201 instanceof PointOverlay) {
                           break label1674;
                        }
                     } catch (Throwable var197) {
                        var10000 = var197;
                        var10001 = false;
                        break;
                     }

                     var202 = var203;
                     break label1694;
                  }

                  PointOverlay var8;
                  try {
                     var8 = (PointOverlay)var201;
                  } catch (Throwable var188) {
                     var10000 = var188;
                     var10001 = false;
                     break;
                  }

                  var202 = var203;

                  try {
                     if (var8.getGeoPoint() == null) {
                        break label1694;
                     }

                     var203 = var4.toPixels(var8.getGeoPoint(), var203);
                  } catch (Throwable var196) {
                     var10000 = var196;
                     var10001 = false;
                     break;
                  }

                  var202 = var203;
                  if (var203 != null) {
                     label1695: {
                        Rect var9;
                        try {
                           var9 = var8.getDrawable().getBounds();
                        } catch (Throwable var187) {
                           var10000 = var187;
                           var10001 = false;
                           break;
                        }

                        var202 = var203;

                        try {
                           if (var9.left == var9.right) {
                              break label1695;
                           }
                        } catch (Throwable var195) {
                           var10000 = var195;
                           var10001 = false;
                           break;
                        }

                        var202 = var203;

                        int var10;
                        int var11;
                        int var12;
                        int var13;
                        int var14;
                        int var15;
                        int var16;
                        int var17;
                        try {
                           if (var9.top == var9.bottom) {
                              break label1695;
                           }

                           var10 = var203.x;
                           var11 = var9.left;
                           var12 = var203.x;
                           var13 = var9.right;
                           var14 = var203.y;
                           var15 = var9.top;
                           var16 = var203.y;
                           var17 = var9.bottom;
                        } catch (Throwable var194) {
                           var10000 = var194;
                           var10001 = false;
                           break;
                        }

                        var202 = var203;

                        try {
                           if (var12 + var13 < var5.x) {
                              break label1695;
                           }
                        } catch (Throwable var193) {
                           var10000 = var193;
                           var10001 = false;
                           break;
                        }

                        var202 = var203;

                        try {
                           if (var10 + var11 > var5.x) {
                              break label1695;
                           }
                        } catch (Throwable var192) {
                           var10000 = var192;
                           var10001 = false;
                           break;
                        }

                        var202 = var203;

                        try {
                           if (var16 + var17 < var5.y) {
                              break label1695;
                           }
                        } catch (Throwable var191) {
                           var10000 = var191;
                           var10001 = false;
                           break;
                        }

                        var202 = var203;

                        try {
                           if (var14 + var15 > var5.y) {
                              break label1695;
                           }
                        } catch (Throwable var190) {
                           var10000 = var190;
                           var10001 = false;
                           break;
                        }

                        var202 = var203;

                        try {
                           if (this.onTap(var8)) {
                              this.hitMap.put(var1, var8);
                              break label1676;
                           }
                        } catch (Throwable var189) {
                           var10000 = var189;
                           var10001 = false;
                           break;
                        }
                     }
                  }
               }

               --var7;
               var203 = var202;
               continue;
            }

            var6 = true;
            return var6;
         }
      }

      Throwable var200 = var10000;
      throw var200;
   }

   public void clear() {
      synchronized(this){}

      try {
         this.getOverlayItems().clear();
         this.hitMap.clear();
      } finally {
         ;
      }

   }

   public void onTap(GeoPoint var1) {
      synchronized(this){}

      try {
         int var2 = ((PointOverlay)this.hitMap.remove(var1)).getId();
         StringBuilder var5 = new StringBuilder();
         Log.d("MAP", var5.append("tapped ").append(var2).toString());
      } finally {
         ;
      }

   }

   public boolean onTap(PointOverlay var1) {
      synchronized(this){}

      try {
         StringBuilder var2 = new StringBuilder();
         Log.d("MAP", var2.append("tapped bool ").append(var1.getId()).toString());
         if (this.onTapListener != null) {
            this.onTapListener.onTap(var1);
         }
      } finally {
         ;
      }

      return true;
   }

   public void registerOnTapEvent(TapEventListener var1) {
      this.onTapListener = var1;
   }

   public void unregisterOnTapEvent() {
      this.onTapListener = null;
   }
}
