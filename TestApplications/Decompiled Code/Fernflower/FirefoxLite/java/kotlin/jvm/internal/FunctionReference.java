package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KFunction;

public class FunctionReference extends CallableReference implements FunctionBase, KFunction {
   private final int arity;

   public FunctionReference(int var1, Object var2) {
      super(var2);
      this.arity = var1;
   }

   protected KCallable computeReflected() {
      return Reflection.function(this);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof FunctionReference)) {
         return var1 instanceof KFunction ? var1.equals(this.compute()) : false;
      } else {
         label31: {
            FunctionReference var3 = (FunctionReference)var1;
            if (this.getOwner() == null) {
               if (var3.getOwner() != null) {
                  break label31;
               }
            } else if (!this.getOwner().equals(var3.getOwner())) {
               break label31;
            }

            if (this.getName().equals(var3.getName()) && this.getSignature().equals(var3.getSignature()) && Intrinsics.areEqual(this.getBoundReceiver(), var3.getBoundReceiver())) {
               return var2;
            }
         }

         var2 = false;
         return var2;
      }
   }

   public int getArity() {
      return this.arity;
   }

   protected KFunction getReflected() {
      return (KFunction)super.getReflected();
   }

   public int hashCode() {
      int var1;
      if (this.getOwner() == null) {
         var1 = 0;
      } else {
         var1 = this.getOwner().hashCode() * 31;
      }

      return (var1 + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
   }

   public String toString() {
      KCallable var1 = this.compute();
      if (var1 != this) {
         return var1.toString();
      } else {
         String var2;
         if ("<init>".equals(this.getName())) {
            var2 = "constructor (Kotlin reflection is not available)";
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("function ");
            var3.append(this.getName());
            var3.append(" (Kotlin reflection is not available)");
            var2 = var3.toString();
         }

         return var2;
      }
   }
}
