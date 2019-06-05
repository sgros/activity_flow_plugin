package kotlin.jvm.internal;

import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KProperty1;

public class ReflectionFactory {
   public KFunction function(FunctionReference var1) {
      return var1;
   }

   public KClass getOrCreateKotlinClass(Class var1) {
      return new ClassReference(var1);
   }

   public KMutableProperty1 mutableProperty1(MutablePropertyReference1 var1) {
      return var1;
   }

   public KProperty1 property1(PropertyReference1 var1) {
      return var1;
   }

   public String renderLambdaToString(FunctionBase var1) {
      String var2 = var1.getClass().getGenericInterfaces()[0].toString();
      String var3 = var2;
      if (var2.startsWith("kotlin.jvm.functions.")) {
         var3 = var2.substring("kotlin.jvm.functions.".length());
      }

      return var3;
   }

   public String renderLambdaToString(Lambda var1) {
      return this.renderLambdaToString((FunctionBase)var1);
   }
}
