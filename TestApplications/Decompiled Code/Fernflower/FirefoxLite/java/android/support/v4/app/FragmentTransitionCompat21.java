package android.support.v4.app;

import android.graphics.Rect;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.Transition.EpicenterCallback;
import android.transition.Transition.TransitionListener;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

class FragmentTransitionCompat21 extends FragmentTransitionImpl {
   private static boolean hasSimpleTarget(Transition var0) {
      boolean var1;
      if (isNullOrEmpty(var0.getTargetIds()) && isNullOrEmpty(var0.getTargetNames()) && isNullOrEmpty(var0.getTargetTypes())) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void addTarget(Object var1, View var2) {
      if (var1 != null) {
         ((Transition)var1).addTarget(var2);
      }

   }

   public void addTargets(Object var1, ArrayList var2) {
      Transition var7 = (Transition)var1;
      if (var7 != null) {
         boolean var3 = var7 instanceof TransitionSet;
         byte var4 = 0;
         int var5 = 0;
         if (var3) {
            TransitionSet var8 = (TransitionSet)var7;

            for(int var9 = var8.getTransitionCount(); var5 < var9; ++var5) {
               this.addTargets(var8.getTransitionAt(var5), var2);
            }
         } else if (!hasSimpleTarget(var7) && isNullOrEmpty(var7.getTargets())) {
            int var6 = var2.size();

            for(var5 = var4; var5 < var6; ++var5) {
               var7.addTarget((View)var2.get(var5));
            }
         }

      }
   }

   public void beginDelayedTransition(ViewGroup var1, Object var2) {
      TransitionManager.beginDelayedTransition(var1, (Transition)var2);
   }

   public boolean canHandle(Object var1) {
      return var1 instanceof Transition;
   }

   public Object cloneTransition(Object var1) {
      Transition var2;
      if (var1 != null) {
         var2 = ((Transition)var1).clone();
      } else {
         var2 = null;
      }

      return var2;
   }

   public Object mergeTransitionsInSequence(Object var1, Object var2, Object var3) {
      var1 = (Transition)var1;
      Transition var4 = (Transition)var2;
      Transition var6 = (Transition)var3;
      if (var1 != null && var4 != null) {
         var1 = (new TransitionSet()).addTransition((Transition)var1).addTransition(var4).setOrdering(1);
      } else if (var1 == null) {
         if (var4 != null) {
            var1 = var4;
         } else {
            var1 = null;
         }
      }

      if (var6 != null) {
         TransitionSet var5 = new TransitionSet();
         if (var1 != null) {
            var5.addTransition((Transition)var1);
         }

         var5.addTransition(var6);
         return var5;
      } else {
         return var1;
      }
   }

   public Object mergeTransitionsTogether(Object var1, Object var2, Object var3) {
      TransitionSet var4 = new TransitionSet();
      if (var1 != null) {
         var4.addTransition((Transition)var1);
      }

      if (var2 != null) {
         var4.addTransition((Transition)var2);
      }

      if (var3 != null) {
         var4.addTransition((Transition)var3);
      }

      return var4;
   }

   public void removeTarget(Object var1, View var2) {
      if (var1 != null) {
         ((Transition)var1).removeTarget(var2);
      }

   }

   public void replaceTargets(Object var1, ArrayList var2, ArrayList var3) {
      Transition var8 = (Transition)var1;
      boolean var4 = var8 instanceof TransitionSet;
      int var5 = 0;
      int var6 = 0;
      if (var4) {
         TransitionSet var9 = (TransitionSet)var8;

         for(var5 = var9.getTransitionCount(); var6 < var5; ++var6) {
            this.replaceTargets(var9.getTransitionAt(var6), var2, var3);
         }
      } else if (!hasSimpleTarget(var8)) {
         List var7 = var8.getTargets();
         if (var7 != null && var7.size() == var2.size() && var7.containsAll(var2)) {
            if (var3 == null) {
               var6 = 0;
            } else {
               var6 = var3.size();
            }

            while(var5 < var6) {
               var8.addTarget((View)var3.get(var5));
               ++var5;
            }

            for(var6 = var2.size() - 1; var6 >= 0; --var6) {
               var8.removeTarget((View)var2.get(var6));
            }
         }
      }

   }

   public void scheduleHideFragmentView(Object var1, final View var2, final ArrayList var3) {
      ((Transition)var1).addListener(new TransitionListener() {
         public void onTransitionCancel(Transition var1) {
         }

         public void onTransitionEnd(Transition var1) {
            var1.removeListener(this);
            var2.setVisibility(8);
            int var2x = var3.size();

            for(int var3x = 0; var3x < var2x; ++var3x) {
               ((View)var3.get(var3x)).setVisibility(0);
            }

         }

         public void onTransitionPause(Transition var1) {
         }

         public void onTransitionResume(Transition var1) {
         }

         public void onTransitionStart(Transition var1) {
         }
      });
   }

   public void scheduleRemoveTargets(Object var1, final Object var2, final ArrayList var3, final Object var4, final ArrayList var5, final Object var6, final ArrayList var7) {
      ((Transition)var1).addListener(new TransitionListener() {
         public void onTransitionCancel(Transition var1) {
         }

         public void onTransitionEnd(Transition var1) {
         }

         public void onTransitionPause(Transition var1) {
         }

         public void onTransitionResume(Transition var1) {
         }

         public void onTransitionStart(Transition var1) {
            if (var2 != null) {
               FragmentTransitionCompat21.this.replaceTargets(var2, var3, (ArrayList)null);
            }

            if (var4 != null) {
               FragmentTransitionCompat21.this.replaceTargets(var4, var5, (ArrayList)null);
            }

            if (var6 != null) {
               FragmentTransitionCompat21.this.replaceTargets(var6, var7, (ArrayList)null);
            }

         }
      });
   }

   public void setEpicenter(Object var1, final Rect var2) {
      if (var1 != null) {
         ((Transition)var1).setEpicenterCallback(new EpicenterCallback() {
            public Rect onGetEpicenter(Transition var1) {
               return var2 != null && !var2.isEmpty() ? var2 : null;
            }
         });
      }

   }

   public void setEpicenter(Object var1, View var2) {
      if (var2 != null) {
         Transition var3 = (Transition)var1;
         final Rect var4 = new Rect();
         this.getBoundsOnScreen(var2, var4);
         var3.setEpicenterCallback(new EpicenterCallback() {
            public Rect onGetEpicenter(Transition var1) {
               return var4;
            }
         });
      }

   }

   public void setSharedElementTargets(Object var1, View var2, ArrayList var3) {
      TransitionSet var4 = (TransitionSet)var1;
      List var7 = var4.getTargets();
      var7.clear();
      int var5 = var3.size();

      for(int var6 = 0; var6 < var5; ++var6) {
         bfsAddViewChildren(var7, (View)var3.get(var6));
      }

      var7.add(var2);
      var3.add(var2);
      this.addTargets(var4, var3);
   }

   public void swapSharedElementTargets(Object var1, ArrayList var2, ArrayList var3) {
      TransitionSet var4 = (TransitionSet)var1;
      if (var4 != null) {
         var4.getTargets().clear();
         var4.getTargets().addAll(var3);
         this.replaceTargets(var4, var2, var3);
      }

   }

   public Object wrapTransitionInSet(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         TransitionSet var2 = new TransitionSet();
         var2.addTransition((Transition)var1);
         return var2;
      }
   }
}
