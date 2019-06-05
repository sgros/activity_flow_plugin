package kotlin.reflect;

import kotlin.jvm.functions.Function1;

public interface KProperty1 extends Function1, KProperty {
   Object get(Object var1);

   KProperty1.Getter getGetter();

   public interface Getter extends Function1, KProperty.Getter {
   }
}
