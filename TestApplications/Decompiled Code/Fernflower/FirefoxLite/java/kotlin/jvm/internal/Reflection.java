package kotlin.jvm.internal;

import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KProperty1;

public class Reflection {
   private static final KClass[] EMPTY_K_CLASS_ARRAY;
   private static final ReflectionFactory factory;

   static {
      ReflectionFactory var0 = null;

      label18: {
         ReflectionFactory var1;
         try {
            var1 = (ReflectionFactory)Class.forName("kotlin.reflect.jvm.internal.ReflectionFactoryImpl").newInstance();
         } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException var2) {
            break label18;
         }

         var0 = var1;
      }

      if (var0 == null) {
         var0 = new ReflectionFactory();
      }

      factory = var0;
      EMPTY_K_CLASS_ARRAY = new KClass[0];
   }

   public static KFunction function(FunctionReference var0) {
      return factory.function(var0);
   }

   public static KClass getOrCreateKotlinClass(Class var0) {
      return factory.getOrCreateKotlinClass(var0);
   }

   public static KMutableProperty1 mutableProperty1(MutablePropertyReference1 var0) {
      return factory.mutableProperty1(var0);
   }

   public static KProperty1 property1(PropertyReference1 var0) {
      return factory.property1(var0);
   }

   public static String renderLambdaToString(Lambda var0) {
      return factory.renderLambdaToString(var0);
   }
}
