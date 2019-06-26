package android.support.v4.widget;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy {
   private static boolean beamBeats(int var0, @NonNull Rect var1, @NonNull Rect var2, @NonNull Rect var3) {
      boolean var4 = true;
      boolean var5 = beamsOverlap(var0, var1, var2);
      if (!beamsOverlap(var0, var1, var3) && var5) {
         var5 = var4;
         if (isToDirectionOf(var0, var1, var3)) {
            var5 = var4;
            if (var0 != 17) {
               var5 = var4;
               if (var0 != 66) {
                  var5 = var4;
                  if (majorAxisDistance(var0, var1, var2) >= majorAxisDistanceToFarEdge(var0, var1, var3)) {
                     var5 = false;
                  }
               }
            }
         }
      } else {
         var5 = false;
      }

      return var5;
   }

   private static boolean beamsOverlap(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      boolean var3 = true;
      switch(var0) {
      case 17:
      case 66:
         if (var2.bottom < var1.top || var2.top > var1.bottom) {
            var3 = false;
         }
         break;
      case 33:
      case 130:
         if (var2.right < var1.left || var2.left > var1.right) {
            var3 = false;
         }
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      return var3;
   }

   public static Object findNextFocusInAbsoluteDirection(@NonNull Object var0, @NonNull FocusStrategy.CollectionAdapter var1, @NonNull FocusStrategy.BoundsAdapter var2, @Nullable Object var3, @NonNull Rect var4, int var5) {
      Rect var6 = new Rect(var4);
      switch(var5) {
      case 17:
         var6.offset(var4.width() + 1, 0);
         break;
      case 33:
         var6.offset(0, var4.height() + 1);
         break;
      case 66:
         var6.offset(-(var4.width() + 1), 0);
         break;
      case 130:
         var6.offset(0, -(var4.height() + 1));
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      Object var7 = null;
      int var8 = var1.size(var0);
      Rect var9 = new Rect();

      for(int var10 = 0; var10 < var8; ++var10) {
         Object var11 = var1.get(var0, var10);
         if (var11 != var3) {
            var2.obtainBounds(var11, var9);
            if (isBetterCandidate(var5, var4, var9, var6)) {
               var6.set(var9);
               var7 = var11;
            }
         }
      }

      return var7;
   }

   public static Object findNextFocusInRelativeDirection(@NonNull Object var0, @NonNull FocusStrategy.CollectionAdapter var1, @NonNull FocusStrategy.BoundsAdapter var2, @Nullable Object var3, int var4, boolean var5, boolean var6) {
      int var7 = var1.size(var0);
      ArrayList var8 = new ArrayList(var7);

      for(int var9 = 0; var9 < var7; ++var9) {
         var8.add(var1.get(var0, var9));
      }

      Collections.sort(var8, new FocusStrategy.SequentialComparator(var5, var2));
      switch(var4) {
      case 1:
         var0 = getPreviousFocusable(var3, var8, var6);
         break;
      case 2:
         var0 = getNextFocusable(var3, var8, var6);
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
      }

      return var0;
   }

   private static Object getNextFocusable(Object var0, ArrayList var1, boolean var2) {
      int var3 = var1.size();
      int var4;
      if (var0 == null) {
         var4 = -1;
      } else {
         var4 = var1.lastIndexOf(var0);
      }

      ++var4;
      if (var4 < var3) {
         var0 = var1.get(var4);
      } else if (var2 && var3 > 0) {
         var0 = var1.get(0);
      } else {
         var0 = null;
      }

      return var0;
   }

   private static Object getPreviousFocusable(Object var0, ArrayList var1, boolean var2) {
      int var3 = var1.size();
      int var4;
      if (var0 == null) {
         var4 = var3;
      } else {
         var4 = var1.indexOf(var0);
      }

      --var4;
      if (var4 >= 0) {
         var0 = var1.get(var4);
      } else if (var2 && var3 > 0) {
         var0 = var1.get(var3 - 1);
      } else {
         var0 = null;
      }

      return var0;
   }

   private static int getWeightedDistanceFor(int var0, int var1) {
      return var0 * 13 * var0 + var1 * var1;
   }

   private static boolean isBetterCandidate(int var0, @NonNull Rect var1, @NonNull Rect var2, @NonNull Rect var3) {
      boolean var4 = true;
      boolean var5;
      if (!isCandidate(var1, var2, var0)) {
         var5 = false;
      } else {
         var5 = var4;
         if (isCandidate(var1, var3, var0)) {
            var5 = var4;
            if (!beamBeats(var0, var1, var2, var3)) {
               if (beamBeats(var0, var1, var3, var2)) {
                  var5 = false;
               } else {
                  var5 = var4;
                  if (getWeightedDistanceFor(majorAxisDistance(var0, var1, var2), minorAxisDistance(var0, var1, var2)) >= getWeightedDistanceFor(majorAxisDistance(var0, var1, var3), minorAxisDistance(var0, var1, var3))) {
                     var5 = false;
                  }
               }
            }
         }
      }

      return var5;
   }

   private static boolean isCandidate(@NonNull Rect var0, @NonNull Rect var1, int var2) {
      boolean var3 = true;
      switch(var2) {
      case 17:
         if (var0.right <= var1.right && var0.left < var1.right || var0.left <= var1.left) {
            var3 = false;
         }
         break;
      case 33:
         if (var0.bottom <= var1.bottom && var0.top < var1.bottom || var0.top <= var1.top) {
            var3 = false;
         }
         break;
      case 66:
         if (var0.left >= var1.left && var0.right > var1.left || var0.right >= var1.right) {
            var3 = false;
         }
         break;
      case 130:
         if (var0.top >= var1.top && var0.bottom > var1.top || var0.bottom >= var1.bottom) {
            var3 = false;
         }
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      return var3;
   }

   private static boolean isToDirectionOf(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      boolean var3 = true;
      switch(var0) {
      case 17:
         if (var1.left < var2.right) {
            var3 = false;
         }
         break;
      case 33:
         if (var1.top < var2.bottom) {
            var3 = false;
         }
         break;
      case 66:
         if (var1.right > var2.left) {
            var3 = false;
         }
         break;
      case 130:
         if (var1.bottom > var2.top) {
            var3 = false;
         }
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      return var3;
   }

   private static int majorAxisDistance(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      return Math.max(0, majorAxisDistanceRaw(var0, var1, var2));
   }

   private static int majorAxisDistanceRaw(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      switch(var0) {
      case 17:
         var0 = var1.left - var2.right;
         break;
      case 33:
         var0 = var1.top - var2.bottom;
         break;
      case 66:
         var0 = var2.left - var1.right;
         break;
      case 130:
         var0 = var2.top - var1.bottom;
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      return var0;
   }

   private static int majorAxisDistanceToFarEdge(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      return Math.max(1, majorAxisDistanceToFarEdgeRaw(var0, var1, var2));
   }

   private static int majorAxisDistanceToFarEdgeRaw(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      switch(var0) {
      case 17:
         var0 = var1.left - var2.left;
         break;
      case 33:
         var0 = var1.top - var2.top;
         break;
      case 66:
         var0 = var2.right - var1.right;
         break;
      case 130:
         var0 = var2.bottom - var1.bottom;
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      return var0;
   }

   private static int minorAxisDistance(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      switch(var0) {
      case 17:
      case 66:
         var0 = Math.abs(var1.top + var1.height() / 2 - (var2.top + var2.height() / 2));
         break;
      case 33:
      case 130:
         var0 = Math.abs(var1.left + var1.width() / 2 - (var2.left + var2.width() / 2));
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      return var0;
   }

   public interface BoundsAdapter {
      void obtainBounds(Object var1, Rect var2);
   }

   public interface CollectionAdapter {
      Object get(Object var1, int var2);

      int size(Object var1);
   }

   private static class SequentialComparator implements Comparator {
      private final FocusStrategy.BoundsAdapter mAdapter;
      private final boolean mIsLayoutRtl;
      private final Rect mTemp1 = new Rect();
      private final Rect mTemp2 = new Rect();

      SequentialComparator(boolean var1, FocusStrategy.BoundsAdapter var2) {
         this.mIsLayoutRtl = var1;
         this.mAdapter = var2;
      }

      public int compare(Object var1, Object var2) {
         byte var3 = 1;
         byte var4 = 1;
         byte var5 = -1;
         Rect var6 = this.mTemp1;
         Rect var7 = this.mTemp2;
         this.mAdapter.obtainBounds(var1, var6);
         this.mAdapter.obtainBounds(var2, var7);
         if (var6.top < var7.top) {
            var4 = var5;
         } else if (var6.top > var7.top) {
            var4 = 1;
         } else if (var6.left < var7.left) {
            if (!this.mIsLayoutRtl) {
               var4 = -1;
            }
         } else if (var6.left > var7.left) {
            var4 = var5;
            if (!this.mIsLayoutRtl) {
               var4 = 1;
            }
         } else {
            var4 = var5;
            if (var6.bottom >= var7.bottom) {
               if (var6.bottom > var7.bottom) {
                  var4 = 1;
               } else if (var6.right < var7.right) {
                  if (this.mIsLayoutRtl) {
                     var4 = var3;
                  } else {
                     var4 = -1;
                  }
               } else if (var6.right > var7.right) {
                  var4 = var5;
                  if (!this.mIsLayoutRtl) {
                     var4 = 1;
                  }
               } else {
                  var4 = 0;
               }
            }
         }

         return var4;
      }
   }
}
