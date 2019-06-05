package kotlin.properties;

import kotlin.reflect.KProperty;

public interface ReadWriteProperty {
   Object getValue(Object var1, KProperty var2);

   void setValue(Object var1, KProperty var2, Object var3);
}
