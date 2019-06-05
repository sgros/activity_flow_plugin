package android.support.design.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import java.util.List;

public class MotionSpec {
   private final SimpleArrayMap timings = new SimpleArrayMap();

   private static void addTimingFromAnimator(MotionSpec var0, Animator var1) {
      if (var1 instanceof ObjectAnimator) {
         ObjectAnimator var3 = (ObjectAnimator)var1;
         var0.setTiming(var3.getPropertyName(), MotionTiming.createFromAnimator(var3));
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Animator must be an ObjectAnimator: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public static MotionSpec createFromResource(Context param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   private static MotionSpec createSpecFromAnimators(List var0) {
      MotionSpec var1 = new MotionSpec();
      int var2 = var0.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         addTimingFromAnimator(var1, (Animator)var0.get(var3));
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         MotionSpec var2 = (MotionSpec)var1;
         return this.timings.equals(var2.timings);
      } else {
         return false;
      }
   }

   public MotionTiming getTiming(String var1) {
      if (this.hasTiming(var1)) {
         return (MotionTiming)this.timings.get(var1);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public long getTotalDuration() {
      int var1 = this.timings.size();
      long var2 = 0L;

      for(int var4 = 0; var4 < var1; ++var4) {
         MotionTiming var5 = (MotionTiming)this.timings.valueAt(var4);
         var2 = Math.max(var2, var5.getDelay() + var5.getDuration());
      }

      return var2;
   }

   public boolean hasTiming(String var1) {
      boolean var2;
      if (this.timings.get(var1) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public int hashCode() {
      return this.timings.hashCode();
   }

   public void setTiming(String var1, MotionTiming var2) {
      this.timings.put(var1, var2);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append('\n');
      var1.append(this.getClass().getName());
      var1.append('{');
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(" timings: ");
      var1.append(this.timings);
      var1.append("}\n");
      return var1.toString();
   }
}
