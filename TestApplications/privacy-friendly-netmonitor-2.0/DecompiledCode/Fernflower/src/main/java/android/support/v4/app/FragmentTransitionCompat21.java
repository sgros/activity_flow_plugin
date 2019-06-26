package android.support.v4.app;

import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.Transition.EpicenterCallback;
import android.transition.Transition.TransitionListener;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@RequiresApi(21)
class FragmentTransitionCompat21 {
   public static void addTarget(Object var0, View var1) {
      if (var0 != null) {
         ((Transition)var0).addTarget(var1);
      }

   }

   public static void addTargets(Object var0, ArrayList var1) {
      Transition var6 = (Transition)var0;
      if (var6 != null) {
         boolean var2 = var6 instanceof TransitionSet;
         byte var3 = 0;
         int var4 = 0;
         if (var2) {
            TransitionSet var7 = (TransitionSet)var6;

            for(int var8 = var7.getTransitionCount(); var4 < var8; ++var4) {
               addTargets(var7.getTransitionAt(var4), var1);
            }
         } else if (!hasSimpleTarget(var6) && isNullOrEmpty(var6.getTargets())) {
            int var5 = var1.size();

            for(var4 = var3; var4 < var5; ++var4) {
               var6.addTarget((View)var1.get(var4));
            }
         }

      }
   }

   public static void beginDelayedTransition(ViewGroup var0, Object var1) {
      TransitionManager.beginDelayedTransition(var0, (Transition)var1);
   }

   private static void bfsAddViewChildren(List var0, View var1) {
      int var2 = var0.size();
      if (!containedBeforeIndex(var0, var1, var2)) {
         var0.add(var1);

         for(int var3 = var2; var3 < var0.size(); ++var3) {
            var1 = (View)var0.get(var3);
            if (var1 instanceof ViewGroup) {
               ViewGroup var7 = (ViewGroup)var1;
               int var4 = var7.getChildCount();

               for(int var5 = 0; var5 < var4; ++var5) {
                  View var6 = var7.getChildAt(var5);
                  if (!containedBeforeIndex(var0, var6, var2)) {
                     var0.add(var6);
                  }
               }
            }
         }

      }
   }

   public static void captureTransitioningViews(ArrayList var0, View var1) {
      if (var1.getVisibility() == 0) {
         if (var1 instanceof ViewGroup) {
            ViewGroup var4 = (ViewGroup)var1;
            if (var4.isTransitionGroup()) {
               var0.add(var4);
            } else {
               int var2 = var4.getChildCount();

               for(int var3 = 0; var3 < var2; ++var3) {
                  captureTransitioningViews(var0, var4.getChildAt(var3));
               }
            }
         } else {
            var0.add(var1);
         }
      }

   }

   public static Object cloneTransition(Object var0) {
      Transition var1;
      if (var0 != null) {
         var1 = ((Transition)var0).clone();
      } else {
         var1 = null;
      }

      return var1;
   }

   private static boolean containedBeforeIndex(List var0, View var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         if (var0.get(var3) == var1) {
            return true;
         }
      }

      return false;
   }

   private static String findKeyForValue(Map var0, String var1) {
      Iterator var2 = var0.entrySet().iterator();

      Entry var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Entry)var2.next();
      } while(!var1.equals(var3.getValue()));

      return (String)var3.getKey();
   }

   public static void findNamedViews(Map var0, View var1) {
      if (var1.getVisibility() == 0) {
         String var2 = var1.getTransitionName();
         if (var2 != null) {
            var0.put(var2, var1);
         }

         if (var1 instanceof ViewGroup) {
            ViewGroup var5 = (ViewGroup)var1;
            int var3 = var5.getChildCount();

            for(int var4 = 0; var4 < var3; ++var4) {
               findNamedViews(var0, var5.getChildAt(var4));
            }
         }
      }

   }

   public static void getBoundsOnScreen(View var0, Rect var1) {
      int[] var2 = new int[2];
      var0.getLocationOnScreen(var2);
      var1.set(var2[0], var2[1], var2[0] + var0.getWidth(), var2[1] + var0.getHeight());
   }

   private static boolean hasSimpleTarget(Transition var0) {
      boolean var1;
      if (isNullOrEmpty(var0.getTargetIds()) && isNullOrEmpty(var0.getTargetNames()) && isNullOrEmpty(var0.getTargetTypes())) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isNullOrEmpty(List var0) {
      boolean var1;
      if (var0 != null && !var0.isEmpty()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static Object mergeTransitionsInSequence(Object var0, Object var1, Object var2) {
      var0 = (Transition)var0;
      Transition var3 = (Transition)var1;
      Transition var5 = (Transition)var2;
      if (var0 != null && var3 != null) {
         var0 = (new TransitionSet()).addTransition((Transition)var0).addTransition(var3).setOrdering(1);
      } else if (var0 == null) {
         if (var3 != null) {
            var0 = var3;
         } else {
            var0 = null;
         }
      }

      if (var5 != null) {
         TransitionSet var4 = new TransitionSet();
         if (var0 != null) {
            var4.addTransition((Transition)var0);
         }

         var4.addTransition(var5);
         return var4;
      } else {
         return var0;
      }
   }

   public static Object mergeTransitionsTogether(Object var0, Object var1, Object var2) {
      TransitionSet var3 = new TransitionSet();
      if (var0 != null) {
         var3.addTransition((Transition)var0);
      }

      if (var1 != null) {
         var3.addTransition((Transition)var1);
      }

      if (var2 != null) {
         var3.addTransition((Transition)var2);
      }

      return var3;
   }

   public static ArrayList prepareSetNameOverridesReordered(ArrayList var0) {
      ArrayList var1 = new ArrayList();
      int var2 = var0.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = (View)var0.get(var3);
         var1.add(var4.getTransitionName());
         var4.setTransitionName((String)null);
      }

      return var1;
   }

   public static void removeTarget(Object var0, View var1) {
      if (var0 != null) {
         ((Transition)var0).removeTarget(var1);
      }

   }

   public static void replaceTargets(Object var0, ArrayList var1, ArrayList var2) {
      Transition var7 = (Transition)var0;
      boolean var3 = var7 instanceof TransitionSet;
      int var4 = 0;
      int var5 = 0;
      if (var3) {
         TransitionSet var8 = (TransitionSet)var7;

         for(var4 = var8.getTransitionCount(); var5 < var4; ++var5) {
            replaceTargets(var8.getTransitionAt(var5), var1, var2);
         }
      } else if (!hasSimpleTarget(var7)) {
         List var6 = var7.getTargets();
         if (var6 != null && var6.size() == var1.size() && var6.containsAll(var1)) {
            if (var2 == null) {
               var5 = 0;
            } else {
               var5 = var2.size();
            }

            while(var4 < var5) {
               var7.addTarget((View)var2.get(var4));
               ++var4;
            }

            for(var5 = var1.size() - 1; var5 >= 0; --var5) {
               var7.removeTarget((View)var1.get(var5));
            }
         }
      }

   }

   public static void scheduleHideFragmentView(Object var0, final View var1, final ArrayList var2) {
      ((Transition)var0).addListener(new TransitionListener() {
         public void onTransitionCancel(Transition var1x) {
         }

         public void onTransitionEnd(Transition var1x) {
            var1x.removeListener(this);
            var1.setVisibility(8);
            int var2x = var2.size();

            for(int var3 = 0; var3 < var2x; ++var3) {
               ((View)var2.get(var3)).setVisibility(0);
            }

         }

         public void onTransitionPause(Transition var1x) {
         }

         public void onTransitionResume(Transition var1x) {
         }

         public void onTransitionStart(Transition var1x) {
         }
      });
   }

   public static void scheduleNameReset(ViewGroup var0, final ArrayList var1, final Map var2) {
      OneShotPreDrawListener.add(var0, new Runnable() {
         public void run() {
            int var1x = var1.size();

            for(int var2x = 0; var2x < var1x; ++var2x) {
               View var3 = (View)var1.get(var2x);
               String var4 = var3.getTransitionName();
               var3.setTransitionName((String)var2.get(var4));
            }

         }
      });
   }

   public static void scheduleRemoveTargets(Object var0, final Object var1, final ArrayList var2, final Object var3, final ArrayList var4, final Object var5, final ArrayList var6) {
      ((Transition)var0).addListener(new TransitionListener() {
         public void onTransitionCancel(Transition var1x) {
         }

         public void onTransitionEnd(Transition var1x) {
         }

         public void onTransitionPause(Transition var1x) {
         }

         public void onTransitionResume(Transition var1x) {
         }

         public void onTransitionStart(Transition var1x) {
            if (var1 != null) {
               FragmentTransitionCompat21.replaceTargets(var1, var2, (ArrayList)null);
            }

            if (var3 != null) {
               FragmentTransitionCompat21.replaceTargets(var3, var4, (ArrayList)null);
            }

            if (var5 != null) {
               FragmentTransitionCompat21.replaceTargets(var5, var6, (ArrayList)null);
            }

         }
      });
   }

   public static void setEpicenter(Object var0, final Rect var1) {
      if (var0 != null) {
         ((Transition)var0).setEpicenterCallback(new EpicenterCallback() {
            public Rect onGetEpicenter(Transition var1x) {
               return var1 != null && !var1.isEmpty() ? var1 : null;
            }
         });
      }

   }

   public static void setEpicenter(Object var0, View var1) {
      if (var1 != null) {
         Transition var3 = (Transition)var0;
         final Rect var2 = new Rect();
         getBoundsOnScreen(var1, var2);
         var3.setEpicenterCallback(new EpicenterCallback() {
            public Rect onGetEpicenter(Transition var1) {
               return var2;
            }
         });
      }

   }

   public static void setNameOverridesOrdered(View var0, final ArrayList var1, final Map var2) {
      OneShotPreDrawListener.add(var0, new Runnable() {
         public void run() {
            int var1x = var1.size();

            for(int var2x = 0; var2x < var1x; ++var2x) {
               View var3 = (View)var1.get(var2x);
               String var4 = var3.getTransitionName();
               if (var4 != null) {
                  var3.setTransitionName(FragmentTransitionCompat21.findKeyForValue(var2, var4));
               }
            }

         }
      });
   }

   public static void setNameOverridesReordered(View var0, final ArrayList var1, final ArrayList var2, final ArrayList var3, Map var4) {
      final int var5 = var2.size();
      final ArrayList var6 = new ArrayList();

      for(int var7 = 0; var7 < var5; ++var7) {
         View var8 = (View)var1.get(var7);
         String var9 = var8.getTransitionName();
         var6.add(var9);
         if (var9 != null) {
            var8.setTransitionName((String)null);
            String var11 = (String)var4.get(var9);

            for(int var10 = 0; var10 < var5; ++var10) {
               if (var11.equals(var3.get(var10))) {
                  ((View)var2.get(var10)).setTransitionName(var9);
                  break;
               }
            }
         }
      }

      OneShotPreDrawListener.add(var0, new Runnable() {
         public void run() {
            for(int var1x = 0; var1x < var5; ++var1x) {
               ((View)var2.get(var1x)).setTransitionName((String)var3.get(var1x));
               ((View)var1.get(var1x)).setTransitionName((String)var6.get(var1x));
            }

         }
      });
   }

   public static void setSharedElementTargets(Object var0, View var1, ArrayList var2) {
      TransitionSet var6 = (TransitionSet)var0;
      List var3 = var6.getTargets();
      var3.clear();
      int var4 = var2.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         bfsAddViewChildren(var3, (View)var2.get(var5));
      }

      var3.add(var1);
      var2.add(var1);
      addTargets(var6, var2);
   }

   public static void swapSharedElementTargets(Object var0, ArrayList var1, ArrayList var2) {
      TransitionSet var3 = (TransitionSet)var0;
      if (var3 != null) {
         var3.getTargets().clear();
         var3.getTargets().addAll(var2);
         replaceTargets(var3, var1, var2);
      }

   }

   public static Object wrapTransitionInSet(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         TransitionSet var1 = new TransitionSet();
         var1.addTransition((Transition)var0);
         return var1;
      }
   }
}
