package mozilla.components.support.base.observer;

import android.arch.lifecycle.LifecycleOwner;
import java.util.List;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public interface Observable {
   void notifyObservers(Function1 var1);

   void register(Object var1);

   void register(Object var1, LifecycleOwner var2, boolean var3);

   void unregister(Object var1);

   void unregisterObservers();

   List wrapConsumers(Function2 var1);
}
