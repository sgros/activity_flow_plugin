package kotlin.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public final class CombinedContext implements CoroutineContext {
   private final CoroutineContext.Element element;
   private final CoroutineContext left;

   public CombinedContext(CoroutineContext var1, CoroutineContext.Element var2) {
      Intrinsics.checkParameterIsNotNull(var1, "left");
      Intrinsics.checkParameterIsNotNull(var2, "element");
      super();
      this.left = var1;
      this.element = var2;
   }

   private final boolean contains(CoroutineContext.Element var1) {
      return Intrinsics.areEqual(this.get(var1.getKey()), var1);
   }

   private final boolean containsAll(CombinedContext var1) {
      while(this.contains(var1.element)) {
         CoroutineContext var2 = var1.left;
         if (!(var2 instanceof CombinedContext)) {
            if (var2 != null) {
               return this.contains((CoroutineContext.Element)var2);
            }

            throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.CoroutineContext.Element");
         }

         var1 = (CombinedContext)var2;
      }

      return false;
   }

   private final int size() {
      int var1;
      if (this.left instanceof CombinedContext) {
         var1 = ((CombinedContext)this.left).size() + 1;
      } else {
         var1 = 2;
      }

      return var1;
   }

   public boolean equals(Object var1) {
      boolean var2;
      label28: {
         if ((CombinedContext)this != var1) {
            if (!(var1 instanceof CombinedContext)) {
               break label28;
            }

            CombinedContext var3 = (CombinedContext)var1;
            if (var3.size() != this.size() || !var3.containsAll(this)) {
               break label28;
            }
         }

         var2 = true;
         return var2;
      }

      var2 = false;
      return var2;
   }

   public Object fold(Object var1, Function2 var2) {
      Intrinsics.checkParameterIsNotNull(var2, "operation");
      return var2.invoke(this.left.fold(var1, var2), this.element);
   }

   public CoroutineContext.Element get(CoroutineContext.Key var1) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      CombinedContext var2 = (CombinedContext)this;

      while(true) {
         CoroutineContext.Element var3 = var2.element.get(var1);
         if (var3 != null) {
            return var3;
         }

         CoroutineContext var4 = var2.left;
         if (!(var4 instanceof CombinedContext)) {
            return var4.get(var1);
         }

         var2 = (CombinedContext)var4;
      }
   }

   public int hashCode() {
      return this.left.hashCode() + this.element.hashCode();
   }

   public CoroutineContext minusKey(CoroutineContext.Key var1) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      if (this.element.get(var1) != null) {
         return this.left;
      } else {
         CoroutineContext var2 = this.left.minusKey(var1);
         if (var2 == this.left) {
            var2 = (CoroutineContext)this;
         } else if (var2 == EmptyCoroutineContext.INSTANCE) {
            var2 = (CoroutineContext)this.element;
         } else {
            var2 = (CoroutineContext)(new CombinedContext(var2, this.element));
         }

         return var2;
      }
   }

   public CoroutineContext plus(CoroutineContext var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      return CoroutineContext.DefaultImpls.plus(this, var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");
      var1.append((String)this.fold("", (Function2)null.INSTANCE));
      var1.append("]");
      return var1.toString();
   }
}
