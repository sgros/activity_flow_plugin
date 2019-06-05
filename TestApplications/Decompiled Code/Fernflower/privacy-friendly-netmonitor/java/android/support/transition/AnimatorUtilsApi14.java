package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator.AnimatorListener;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;

@RequiresApi(14)
class AnimatorUtilsApi14 implements AnimatorUtilsImpl {
   public void addPauseListener(@NonNull Animator var1, @NonNull AnimatorListenerAdapter var2) {
   }

   public void pause(@NonNull Animator var1) {
      ArrayList var2 = var1.getListeners();
      if (var2 != null) {
         int var3 = 0;

         for(int var4 = var2.size(); var3 < var4; ++var3) {
            AnimatorListener var5 = (AnimatorListener)var2.get(var3);
            if (var5 instanceof AnimatorUtilsApi14.AnimatorPauseListenerCompat) {
               ((AnimatorUtilsApi14.AnimatorPauseListenerCompat)var5).onAnimationPause(var1);
            }
         }
      }

   }

   public void resume(@NonNull Animator var1) {
      ArrayList var2 = var1.getListeners();
      if (var2 != null) {
         int var3 = 0;

         for(int var4 = var2.size(); var3 < var4; ++var3) {
            AnimatorListener var5 = (AnimatorListener)var2.get(var3);
            if (var5 instanceof AnimatorUtilsApi14.AnimatorPauseListenerCompat) {
               ((AnimatorUtilsApi14.AnimatorPauseListenerCompat)var5).onAnimationResume(var1);
            }
         }
      }

   }

   interface AnimatorPauseListenerCompat {
      void onAnimationPause(Animator var1);

      void onAnimationResume(Animator var1);
   }
}
