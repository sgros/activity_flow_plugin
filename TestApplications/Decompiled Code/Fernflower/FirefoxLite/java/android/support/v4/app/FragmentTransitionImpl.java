package android.support.v4.app;

import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class FragmentTransitionImpl {
   protected static void bfsAddViewChildren(List var0, View var1) {
      int var2 = var0.size();
      if (!containedBeforeIndex(var0, var1, var2)) {
         var0.add(var1);

         for(int var3 = var2; var3 < var0.size(); ++var3) {
            var1 = (View)var0.get(var3);
            if (var1 instanceof ViewGroup) {
               ViewGroup var4 = (ViewGroup)var1;
               int var5 = var4.getChildCount();

               for(int var6 = 0; var6 < var5; ++var6) {
                  var1 = var4.getChildAt(var6);
                  if (!containedBeforeIndex(var0, var1, var2)) {
                     var0.add(var1);
                  }
               }
            }
         }

      }
   }

   private static boolean containedBeforeIndex(List var0, View var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         if (var0.get(var3) == var1) {
            return true;
         }
      }

      return false;
   }

   static String findKeyForValue(Map var0, String var1) {
      Iterator var3 = var0.entrySet().iterator();

      Entry var2;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var2 = (Entry)var3.next();
      } while(!var1.equals(var2.getValue()));

      return (String)var2.getKey();
   }

   protected static boolean isNullOrEmpty(List var0) {
      boolean var1;
      if (var0 != null && !var0.isEmpty()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public abstract void addTarget(Object var1, View var2);

   public abstract void addTargets(Object var1, ArrayList var2);

   public abstract void beginDelayedTransition(ViewGroup var1, Object var2);

   public abstract boolean canHandle(Object var1);

   void captureTransitioningViews(ArrayList var1, View var2) {
      if (var2.getVisibility() == 0) {
         if (var2 instanceof ViewGroup) {
            ViewGroup var5 = (ViewGroup)var2;
            if (ViewGroupCompat.isTransitionGroup(var5)) {
               var1.add(var5);
            } else {
               int var3 = var5.getChildCount();

               for(int var4 = 0; var4 < var3; ++var4) {
                  this.captureTransitioningViews(var1, var5.getChildAt(var4));
               }
            }
         } else {
            var1.add(var2);
         }
      }

   }

   public abstract Object cloneTransition(Object var1);

   void findNamedViews(Map var1, View var2) {
      if (var2.getVisibility() == 0) {
         String var3 = ViewCompat.getTransitionName(var2);
         if (var3 != null) {
            var1.put(var3, var2);
         }

         if (var2 instanceof ViewGroup) {
            ViewGroup var6 = (ViewGroup)var2;
            int var4 = var6.getChildCount();

            for(int var5 = 0; var5 < var4; ++var5) {
               this.findNamedViews(var1, var6.getChildAt(var5));
            }
         }
      }

   }

   protected void getBoundsOnScreen(View var1, Rect var2) {
      int[] var3 = new int[2];
      var1.getLocationOnScreen(var3);
      var2.set(var3[0], var3[1], var3[0] + var1.getWidth(), var3[1] + var1.getHeight());
   }

   public abstract Object mergeTransitionsInSequence(Object var1, Object var2, Object var3);

   public abstract Object mergeTransitionsTogether(Object var1, Object var2, Object var3);

   ArrayList prepareSetNameOverridesReordered(ArrayList var1) {
      ArrayList var2 = new ArrayList();
      int var3 = var1.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         View var5 = (View)var1.get(var4);
         var2.add(ViewCompat.getTransitionName(var5));
         ViewCompat.setTransitionName(var5, (String)null);
      }

      return var2;
   }

   public abstract void removeTarget(Object var1, View var2);

   public abstract void replaceTargets(Object var1, ArrayList var2, ArrayList var3);

   public abstract void scheduleHideFragmentView(Object var1, View var2, ArrayList var3);

   void scheduleNameReset(ViewGroup var1, final ArrayList var2, final Map var3) {
      OneShotPreDrawListener.add(var1, new Runnable() {
         public void run() {
            int var1 = var2.size();

            for(int var2x = 0; var2x < var1; ++var2x) {
               View var3x = (View)var2.get(var2x);
               String var4 = ViewCompat.getTransitionName(var3x);
               ViewCompat.setTransitionName(var3x, (String)var3.get(var4));
            }

         }
      });
   }

   public abstract void scheduleRemoveTargets(Object var1, Object var2, ArrayList var3, Object var4, ArrayList var5, Object var6, ArrayList var7);

   public abstract void setEpicenter(Object var1, Rect var2);

   public abstract void setEpicenter(Object var1, View var2);

   void setNameOverridesOrdered(View var1, final ArrayList var2, final Map var3) {
      OneShotPreDrawListener.add(var1, new Runnable() {
         public void run() {
            int var1 = var2.size();

            for(int var2x = 0; var2x < var1; ++var2x) {
               View var3x = (View)var2.get(var2x);
               String var4 = ViewCompat.getTransitionName(var3x);
               if (var4 != null) {
                  ViewCompat.setTransitionName(var3x, FragmentTransitionImpl.findKeyForValue(var3, var4));
               }
            }

         }
      });
   }

   void setNameOverridesReordered(View var1, final ArrayList var2, final ArrayList var3, final ArrayList var4, Map var5) {
      final int var6 = var3.size();
      final ArrayList var7 = new ArrayList();

      for(int var8 = 0; var8 < var6; ++var8) {
         View var9 = (View)var2.get(var8);
         String var10 = ViewCompat.getTransitionName(var9);
         var7.add(var10);
         if (var10 != null) {
            ViewCompat.setTransitionName(var9, (String)null);
            String var12 = (String)var5.get(var10);

            for(int var11 = 0; var11 < var6; ++var11) {
               if (var12.equals(var4.get(var11))) {
                  ViewCompat.setTransitionName((View)var3.get(var11), var10);
                  break;
               }
            }
         }
      }

      OneShotPreDrawListener.add(var1, new Runnable() {
         public void run() {
            for(int var1 = 0; var1 < var6; ++var1) {
               ViewCompat.setTransitionName((View)var3.get(var1), (String)var4.get(var1));
               ViewCompat.setTransitionName((View)var2.get(var1), (String)var7.get(var1));
            }

         }
      });
   }

   public abstract void setSharedElementTargets(Object var1, View var2, ArrayList var3);

   public abstract void swapSharedElementTargets(Object var1, ArrayList var2, ArrayList var3);

   public abstract Object wrapTransitionInSet(Object var1);
}
