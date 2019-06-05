package kotlin.ranges;

import kotlin.jvm.internal.DefaultConstructorMarker;

public final class IntRange extends IntProgression {
   public static final IntRange.Companion Companion = new IntRange.Companion((DefaultConstructorMarker)null);
   private static final IntRange EMPTY = new IntRange(1, 0);

   public IntRange(int var1, int var2) {
      super(var1, var2, 1);
   }

   public boolean equals(Object var1) {
      boolean var3;
      label27: {
         if (var1 instanceof IntRange) {
            if (this.isEmpty() && ((IntRange)var1).isEmpty()) {
               break label27;
            }

            int var2 = this.getFirst();
            IntRange var4 = (IntRange)var1;
            if (var2 == var4.getFirst() && this.getLast() == var4.getLast()) {
               break label27;
            }
         }

         var3 = false;
         return var3;
      }

      var3 = true;
      return var3;
   }

   public Integer getEndInclusive() {
      return this.getLast();
   }

   public Integer getStart() {
      return this.getFirst();
   }

   public int hashCode() {
      int var1;
      if (this.isEmpty()) {
         var1 = -1;
      } else {
         var1 = this.getFirst() * 31 + this.getLast();
      }

      return var1;
   }

   public boolean isEmpty() {
      boolean var1;
      if (this.getFirst() > this.getLast()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getFirst());
      var1.append("..");
      var1.append(this.getLast());
      return var1.toString();
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final IntRange getEMPTY() {
         return IntRange.EMPTY;
      }
   }
}
