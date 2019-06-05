// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.animation;

import java.util.Collection;
import android.animation.ValueAnimator;
import android.animation.Animator;
import java.util.List;
import android.animation.AnimatorSet;

public class AnimatorSetCompat
{
    public static void playTogether(final AnimatorSet set, final List<Animator> list) {
        final int size = list.size();
        long max = 0L;
        for (int i = 0; i < size; ++i) {
            final Animator animator = list.get(i);
            max = Math.max(max, animator.getStartDelay() + animator.getDuration());
        }
        final ValueAnimator ofInt = ValueAnimator.ofInt(new int[] { 0, 0 });
        ((Animator)ofInt).setDuration(max);
        list.add(0, (Animator)ofInt);
        set.playTogether((Collection)list);
    }
}
