package kotlin.jvm.internal;

import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;

public final class ClassReference implements ClassBasedDeclarationContainer, KClass {
   private final Class jClass;

   public ClassReference(Class var1) {
      Intrinsics.checkParameterIsNotNull(var1, "jClass");
      super();
      this.jClass = var1;
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof ClassReference && Intrinsics.areEqual(JvmClassMappingKt.getJavaObjectType(this), JvmClassMappingKt.getJavaObjectType((KClass)var1))) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Class getJClass() {
      return this.jClass;
   }

   public int hashCode() {
      return JvmClassMappingKt.getJavaObjectType(this).hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getJClass().toString());
      var1.append(" (Kotlin reflection is not available)");
      return var1.toString();
   }
}
