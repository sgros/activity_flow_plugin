package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.AbstractCoroutineContextElement;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class CoroutineName extends AbstractCoroutineContextElement {
   public static final CoroutineName.Key Key = new CoroutineName.Key((DefaultConstructorMarker)null);
   private final String name;

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof CoroutineName)) {
            return false;
         }

         CoroutineName var2 = (CoroutineName)var1;
         if (!Intrinsics.areEqual(this.name, var2.name)) {
            return false;
         }
      }

      return true;
   }

   public final String getName() {
      return this.name;
   }

   public int hashCode() {
      String var1 = this.name;
      int var2;
      if (var1 != null) {
         var2 = var1.hashCode();
      } else {
         var2 = 0;
      }

      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("CoroutineName(");
      var1.append(this.name);
      var1.append(')');
      return var1.toString();
   }

   public static final class Key implements CoroutineContext.Key {
      private Key() {
      }

      // $FF: synthetic method
      public Key(DefaultConstructorMarker var1) {
         this();
      }
   }
}
