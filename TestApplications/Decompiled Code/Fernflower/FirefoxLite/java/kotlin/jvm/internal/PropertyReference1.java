package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty1;

public abstract class PropertyReference1 extends PropertyReference implements KProperty1 {
   protected KCallable computeReflected() {
      return Reflection.property1(this);
   }

   public KProperty1.Getter getGetter() {
      return ((KProperty1)this.getReflected()).getGetter();
   }

   public Object invoke(Object var1) {
      return this.get(var1);
   }
}
