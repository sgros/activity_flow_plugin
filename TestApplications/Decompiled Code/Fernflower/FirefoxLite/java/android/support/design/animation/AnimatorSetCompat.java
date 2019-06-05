package android.support.design.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import java.util.List;

public class AnimatorSetCompat {
   public static void playTogether(AnimatorSet var0, List var1) {
      int var2 = var1.size();
      long var3 = 0L;

      for(int var5 = 0; var5 < var2; ++var5) {
         Animator var6 = (Animator)var1.get(var5);
         var3 = Math.max(var3, var6.getStartDelay() + var6.getDuration());
      }

      ValueAnimator var7 = ValueAnimator.ofInt(new int[]{0, 0});
      var7.setDuration(var3);
      var1.add(0, var7);
      var0.playTogether(var1);
   }
}
