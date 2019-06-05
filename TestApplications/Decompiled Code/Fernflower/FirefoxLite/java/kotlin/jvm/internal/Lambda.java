package kotlin.jvm.internal;

import java.io.Serializable;

public abstract class Lambda implements Serializable, FunctionBase {
   private final int arity;

   public Lambda(int var1) {
      this.arity = var1;
   }

   public int getArity() {
      return this.arity;
   }

   public String toString() {
      String var1 = Reflection.renderLambdaToString(this);
      Intrinsics.checkExpressionValueIsNotNull(var1, "Reflection.renderLambdaToString(this)");
      return var1;
   }
}
