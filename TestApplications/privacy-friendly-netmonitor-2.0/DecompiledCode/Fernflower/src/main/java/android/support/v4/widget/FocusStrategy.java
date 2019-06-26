package android.support.v4.widget;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy {
   private static boolean beamBeats(int var0, @NonNull Rect var1, @NonNull Rect var2, @NonNull Rect var3) {
      boolean var4 = beamsOverlap(var0, var1, var2);
      if (!beamsOverlap(var0, var1, var3) && var4) {
         boolean var5 = isToDirectionOf(var0, var1, var3);
         var4 = true;
         if (!var5) {
            return true;
         } else if (var0 != 17 && var0 != 66) {
            if (majorAxisDistance(var0, var1, var2) >= majorAxisDistanceToFarEdge(var0, var1, var3)) {
               var4 = false;
            }

            return var4;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   private static boolean beamsOverlap(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      boolean var4;
      boolean var5;
      label32: {
         boolean var3 = false;
         var4 = false;
         if (var0 != 17) {
            if (var0 == 33) {
               break label32;
            }

            if (var0 != 66) {
               if (var0 != 130) {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               }
               break label32;
            }
         }

         var5 = var3;
         if (var2.bottom >= var1.top) {
            var5 = var3;
            if (var2.top <= var1.bottom) {
               var5 = true;
            }
         }

         return var5;
      }

      var5 = var4;
      if (var2.right >= var1.left) {
         var5 = var4;
         if (var2.left <= var1.right) {
            var5 = true;
         }
      }

      return var5;
   }

   public static Object findNextFocusInAbsoluteDirection(@NonNull Object var0, @NonNull FocusStrategy.CollectionAdapter var1, @NonNull FocusStrategy.BoundsAdapter var2, @Nullable Object var3, @NonNull Rect var4, int var5) {
      Rect var6 = new Rect(var4);
      int var7 = 0;
      if (var5 != 17) {
         if (var5 != 33) {
            if (var5 != 66) {
               if (var5 != 130) {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               }

               var6.offset(0, -(var4.height() + 1));
            } else {
               var6.offset(-(var4.width() + 1), 0);
            }
         } else {
            var6.offset(0, var4.height() + 1);
         }
      } else {
         var6.offset(var4.width() + 1, 0);
      }

      Object var8 = null;
      int var9 = var1.size(var0);

      for(Rect var10 = new Rect(); var7 < var9; ++var7) {
         Object var11 = var1.get(var0, var7);
         if (var11 != var3) {
            var2.obtainBounds(var11, var10);
            if (isBetterCandidate(var5, var4, var10, var6)) {
               var6.set(var10);
               var8 = var11;
            }
         }
      }

      return var8;
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
         return getPreviousFocusable(var3, var8, var6);
      case 2:
         return getNextFocusable(var3, var8, var6);
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
      }
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
         return var1.get(var4);
      } else {
         return var2 && var3 > 0 ? var1.get(0) : null;
      }
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
         return var1.get(var4);
      } else {
         return var2 && var3 > 0 ? var1.get(var3 - 1) : null;
      }
   }

   private static int getWeightedDistanceFor(int var0, int var1) {
      return 13 * var0 * var0 + var1 * var1;
   }

   private static boolean isBetterCandidate(int var0, @NonNull Rect var1, @NonNull Rect var2, @NonNull Rect var3) {
      boolean var4 = isCandidate(var1, var2, var0);
      boolean var5 = false;
      if (!var4) {
         return false;
      } else if (!isCandidate(var1, var3, var0)) {
         return true;
      } else if (beamBeats(var0, var1, var2, var3)) {
         return true;
      } else if (beamBeats(var0, var1, var3, var2)) {
         return false;
      } else {
         if (getWeightedDistanceFor(majorAxisDistance(var0, var1, var2), minorAxisDistance(var0, var1, var2)) < getWeightedDistanceFor(majorAxisDistance(var0, var1, var3), minorAxisDistance(var0, var1, var3))) {
            var5 = true;
         }

         return var5;
      }
   }

   private static boolean isCandidate(@NonNull Rect var0, @NonNull Rect var1, int var2) {
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7;
      if (var2 != 17) {
         if (var2 != 33) {
            if (var2 != 66) {
               if (var2 != 130) {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               } else {
                  if (var0.top >= var1.top) {
                     var7 = var6;
                     if (var0.bottom > var1.top) {
                        return var7;
                     }
                  }

                  var7 = var6;
                  if (var0.bottom < var1.bottom) {
                     var7 = true;
                  }

                  return var7;
               }
            } else {
               if (var0.left >= var1.left) {
                  var7 = var3;
                  if (var0.right > var1.left) {
                     return var7;
                  }
               }

               var7 = var3;
               if (var0.right < var1.right) {
                  var7 = true;
               }

               return var7;
            }
         } else {
            if (var0.bottom <= var1.bottom) {
               var7 = var4;
               if (var0.top < var1.bottom) {
                  return var7;
               }
            }

            var7 = var4;
            if (var0.top > var1.top) {
               var7 = true;
            }

            return var7;
         }
      } else {
         if (var0.right <= var1.right) {
            var7 = var5;
            if (var0.left < var1.right) {
               return var7;
            }
         }

         var7 = var5;
         if (var0.left > var1.left) {
            var7 = true;
         }

         return var7;
      }
   }

   private static boolean isToDirectionOf(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      if (var0 != 17) {
         if (var0 != 33) {
            if (var0 != 66) {
               if (var0 != 130) {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               } else {
                  if (var1.bottom <= var2.top) {
                     var6 = true;
                  }

                  return var6;
               }
            } else {
               var6 = var3;
               if (var1.right <= var2.left) {
                  var6 = true;
               }

               return var6;
            }
         } else {
            var6 = var4;
            if (var1.top >= var2.bottom) {
               var6 = true;
            }

            return var6;
         }
      } else {
         var6 = var5;
         if (var1.left >= var2.right) {
            var6 = true;
         }

         return var6;
      }
   }

   private static int majorAxisDistance(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      return Math.max(0, majorAxisDistanceRaw(var0, var1, var2));
   }

   private static int majorAxisDistanceRaw(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      if (var0 != 17) {
         if (var0 != 33) {
            if (var0 != 66) {
               if (var0 != 130) {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               } else {
                  return var2.top - var1.bottom;
               }
            } else {
               return var2.left - var1.right;
            }
         } else {
            return var1.top - var2.bottom;
         }
      } else {
         return var1.left - var2.right;
      }
   }

   private static int majorAxisDistanceToFarEdge(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      return Math.max(1, majorAxisDistanceToFarEdgeRaw(var0, var1, var2));
   }

   private static int majorAxisDistanceToFarEdgeRaw(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      if (var0 != 17) {
         if (var0 != 33) {
            if (var0 != 66) {
               if (var0 != 130) {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               } else {
                  return var2.bottom - var1.bottom;
               }
            } else {
               return var2.right - var1.right;
            }
         } else {
            return var1.top - var2.top;
         }
      } else {
         return var1.left - var2.left;
      }
   }

   private static int minorAxisDistance(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      if (var0 != 17) {
         if (var0 == 33) {
            return Math.abs(var1.left + var1.width() / 2 - (var2.left + var2.width() / 2));
         }

         if (var0 != 66) {
            if (var0 != 130) {
               throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }

            return Math.abs(var1.left + var1.width() / 2 - (var2.left + var2.width() / 2));
         }
      }

      return Math.abs(var1.top + var1.height() / 2 - (var2.top + var2.height() / 2));
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
         Rect var3 = this.mTemp1;
         Rect var4 = this.mTemp2;
         this.mAdapter.obtainBounds(var1, var3);
         this.mAdapter.obtainBounds(var2, var4);
         int var5 = var3.top;
         int var6 = var4.top;
         byte var7 = -1;
         if (var5 < var6) {
            return -1;
         } else if (var3.top > var4.top) {
            return 1;
         } else if (var3.left < var4.left) {
            if (this.mIsLayoutRtl) {
               var7 = 1;
            }

            return var7;
         } else if (var3.left > var4.left) {
            if (!this.mIsLayoutRtl) {
               var7 = 1;
            }

            return var7;
         } else if (var3.bottom < var4.bottom) {
            return -1;
         } else if (var3.bottom > var4.bottom) {
            return 1;
         } else if (var3.right < var4.right) {
            if (this.mIsLayoutRtl) {
               var7 = 1;
            }

            return var7;
         } else if (var3.right > var4.right) {
            if (!this.mIsLayoutRtl) {
               var7 = 1;
            }

            return var7;
         } else {
            return 0;
         }
      }
   }
}
