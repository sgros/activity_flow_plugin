package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator.AnimatorListener;
import android.os.Build.VERSION;
import java.util.ArrayList;

class AnimatorUtils {
   static void addPauseListener(Animator var0, AnimatorListenerAdapter var1) {
      if (VERSION.SDK_INT >= 19) {
         var0.addPauseListener(var1);
      }

   }

   static void pause(Animator var0) {
      if (VERSION.SDK_INT >= 19) {
         var0.pause();
      } else {
         ArrayList var1 = var0.getListeners();
         if (var1 != null) {
            int var2 = 0;

            for(int var3 = var1.size(); var2 < var3; ++var2) {
               AnimatorListener var4 = (AnimatorListener)var1.get(var2);
               if (var4 instanceof AnimatorUtils.AnimatorPauseListenerCompat) {
                  ((AnimatorUtils.AnimatorPauseListenerCompat)var4).onAnimationPause(var0);
               }
            }
         }
      }

   }

   static void resume(Animator var0) {
      if (VERSION.SDK_INT >= 19) {
         var0.resume();
      } else {
         ArrayList var1 = var0.getListeners();
         if (var1 != null) {
            int var2 = 0;

            for(int var3 = var1.size(); var2 < var3; ++var2) {
               AnimatorListener var4 = (AnimatorListener)var1.get(var2);
               if (var4 instanceof AnimatorUtils.AnimatorPauseListenerCompat) {
                  ((AnimatorUtils.AnimatorPauseListenerCompat)var4).onAnimationResume(var0);
               }
            }
         }
      }

   }

   interface AnimatorPauseListenerCompat {
      void onAnimationPause(Animator var1);

      void onAnimationResume(Animator var1);
   }
}
