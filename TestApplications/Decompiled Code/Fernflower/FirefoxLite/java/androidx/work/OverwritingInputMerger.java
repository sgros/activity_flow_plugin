package androidx.work;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class OverwritingInputMerger extends InputMerger {
   public Data merge(List var1) {
      Data.Builder var2 = new Data.Builder();
      HashMap var3 = new HashMap();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         var3.putAll(((Data)var4.next()).getKeyValueMap());
      }

      var2.putAll((Map)var3);
      return var2.build();
   }
}
