package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.AbstractCoroutineContextElement;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;

final class CoroutineId extends AbstractCoroutineContextElement {
   public static final CoroutineId.Key Key = new CoroutineId.Key((DefaultConstructorMarker)null);
   private final long id;

   public CoroutineId(long var1) {
      super((CoroutineContext.Key)Key);
      this.id = var1;
   }

   public final long getId() {
      return this.id;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("CoroutineId(");
      var1.append(this.id);
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
