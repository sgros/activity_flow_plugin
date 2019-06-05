package kotlin.properties;

import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;

public abstract class ObservableProperty implements ReadWriteProperty {
   private Object value;

   public ObservableProperty(Object var1) {
      this.value = var1;
   }

   protected void afterChange(KProperty var1, Object var2, Object var3) {
      Intrinsics.checkParameterIsNotNull(var1, "property");
   }

   protected boolean beforeChange(KProperty var1, Object var2, Object var3) {
      Intrinsics.checkParameterIsNotNull(var1, "property");
      return true;
   }

   public Object getValue(Object var1, KProperty var2) {
      Intrinsics.checkParameterIsNotNull(var2, "property");
      return this.value;
   }

   public void setValue(Object var1, KProperty var2, Object var3) {
      Intrinsics.checkParameterIsNotNull(var2, "property");
      var1 = this.value;
      if (this.beforeChange(var2, var1, var3)) {
         this.value = var3;
         this.afterChange(var2, var1, var3);
      }
   }
}
