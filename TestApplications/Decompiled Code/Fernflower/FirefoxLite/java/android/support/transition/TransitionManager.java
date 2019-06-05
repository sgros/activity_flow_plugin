package android.support.transition;

import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class TransitionManager {
   private static Transition sDefaultTransition = new AutoTransition();
   static ArrayList sPendingTransitions = new ArrayList();
   private static ThreadLocal sRunningTransitions = new ThreadLocal();

   public static void beginDelayedTransition(ViewGroup var0, Transition var1) {
      if (!sPendingTransitions.contains(var0) && ViewCompat.isLaidOut(var0)) {
         sPendingTransitions.add(var0);
         Transition var2 = var1;
         if (var1 == null) {
            var2 = sDefaultTransition;
         }

         var1 = var2.clone();
         sceneChangeSetup(var0, var1);
         Scene.setCurrentScene(var0, (Scene)null);
         sceneChangeRunTransition(var0, var1);
      }

   }

   static ArrayMap getRunningTransitions() {
      WeakReference var0 = (WeakReference)sRunningTransitions.get();
      ArrayMap var2;
      if (var0 != null) {
         var2 = (ArrayMap)var0.get();
         if (var2 != null) {
            return var2;
         }
      }

      var2 = new ArrayMap();
      WeakReference var1 = new WeakReference(var2);
      sRunningTransitions.set(var1);
      return var2;
   }

   private static void sceneChangeRunTransition(ViewGroup var0, Transition var1) {
      if (var1 != null && var0 != null) {
         TransitionManager.MultiListener var2 = new TransitionManager.MultiListener(var1, var0);
         var0.addOnAttachStateChangeListener(var2);
         var0.getViewTreeObserver().addOnPreDrawListener(var2);
      }

   }

   private static void sceneChangeSetup(ViewGroup var0, Transition var1) {
      ArrayList var2 = (ArrayList)getRunningTransitions().get(var0);
      if (var2 != null && var2.size() > 0) {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            ((Transition)var4.next()).pause(var0);
         }
      }

      if (var1 != null) {
         var1.captureValues(var0, true);
      }

      Scene var3 = Scene.getCurrentScene(var0);
      if (var3 != null) {
         var3.exit();
      }

   }

   private static class MultiListener implements OnAttachStateChangeListener, OnPreDrawListener {
      ViewGroup mSceneRoot;
      Transition mTransition;

      MultiListener(Transition var1, ViewGroup var2) {
         this.mTransition = var1;
         this.mSceneRoot = var2;
      }

      private void removeListeners() {
         this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
         this.mSceneRoot.removeOnAttachStateChangeListener(this);
      }

      public boolean onPreDraw() {
         this.removeListeners();
         if (!TransitionManager.sPendingTransitions.remove(this.mSceneRoot)) {
            return true;
         } else {
            final ArrayMap var1 = TransitionManager.getRunningTransitions();
            ArrayList var2 = (ArrayList)var1.get(this.mSceneRoot);
            ArrayList var3 = null;
            ArrayList var4;
            if (var2 == null) {
               var4 = new ArrayList();
               var1.put(this.mSceneRoot, var4);
            } else {
               var4 = var2;
               if (var2.size() > 0) {
                  var3 = new ArrayList(var2);
                  var4 = var2;
               }
            }

            var4.add(this.mTransition);
            this.mTransition.addListener(new TransitionListenerAdapter() {
               public void onTransitionEnd(Transition var1x) {
                  ((ArrayList)var1.get(MultiListener.this.mSceneRoot)).remove(var1x);
               }
            });
            this.mTransition.captureValues(this.mSceneRoot, false);
            if (var3 != null) {
               Iterator var5 = var3.iterator();

               while(var5.hasNext()) {
                  ((Transition)var5.next()).resume(this.mSceneRoot);
               }
            }

            this.mTransition.playTransition(this.mSceneRoot);
            return true;
         }
      }

      public void onViewAttachedToWindow(View var1) {
      }

      public void onViewDetachedFromWindow(View var1) {
         this.removeListeners();
         TransitionManager.sPendingTransitions.remove(this.mSceneRoot);
         ArrayList var2 = (ArrayList)TransitionManager.getRunningTransitions().get(this.mSceneRoot);
         if (var2 != null && var2.size() > 0) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               ((Transition)var3.next()).resume(this.mSceneRoot);
            }
         }

         this.mTransition.clearValues(true);
      }
   }
}
