package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KProperty1;

public abstract class MutablePropertyReference1 extends MutablePropertyReference implements KMutableProperty1 {
   protected KCallable computeReflected() {
      return Reflection.mutableProperty1(this);
   }

   public KProperty1.Getter getGetter() {
      return ((KMutableProperty1)this.getReflected()).getGetter();
   }

   public Object invoke(Object var1) {
      return this.get(var1);
   }
}
