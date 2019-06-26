package org.osmdroid.tileprovider.modules;

import java.util.concurrent.ThreadFactory;

public class ConfigurablePriorityThreadFactory implements ThreadFactory {
   private final String mName;
   private final int mPriority;

   public ConfigurablePriorityThreadFactory(int var1, String var2) {
      this.mPriority = var1;
      this.mName = var2;
   }

   public Thread newThread(Runnable var1) {
      Thread var2 = new Thread(var1);
      var2.setPriority(this.mPriority);
      String var3 = this.mName;
      if (var3 != null) {
         var2.setName(var3);
      }

      return var2;
   }
}
