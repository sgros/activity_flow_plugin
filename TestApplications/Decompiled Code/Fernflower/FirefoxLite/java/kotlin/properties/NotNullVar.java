package kotlin.properties;

import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;

final class NotNullVar implements ReadWriteProperty {
   private Object value;

   public NotNullVar() {
   }

   public Object getValue(Object var1, KProperty var2) {
      Intrinsics.checkParameterIsNotNull(var2, "property");
      var1 = this.value;
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Property ");
         var3.append(var2.getName());
         var3.append(" should be initialized before get.");
         throw (Throwable)(new IllegalStateException(var3.toString()));
      }
   }

   public void setValue(Object var1, KProperty var2, Object var3) {
      Intrinsics.checkParameterIsNotNull(var2, "property");
      Intrinsics.checkParameterIsNotNull(var3, "value");
      this.value = var3;
   }
}
