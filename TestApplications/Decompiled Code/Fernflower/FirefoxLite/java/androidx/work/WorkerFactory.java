package androidx.work;

import android.content.Context;

public abstract class WorkerFactory {
   private static final String TAG = Logger.tagWithPrefix("WorkerFactory");

   public static WorkerFactory getDefaultWorkerFactory() {
      return new WorkerFactory() {
         public ListenableWorker createWorker(Context var1, String var2, WorkerParameters var3) {
            return null;
         }
      };
   }

   public abstract ListenableWorker createWorker(Context var1, String var2, WorkerParameters var3);

   public final ListenableWorker createWorkerWithDefaultFallback(Context var1, String var2, WorkerParameters var3) {
      ListenableWorker var4 = this.createWorker(var1, var2, var3);
      if (var4 != null) {
         return var4;
      } else {
         Class var13;
         try {
            var13 = Class.forName(var2).asSubclass(ListenableWorker.class);
         } catch (ClassNotFoundException var7) {
            Logger var10 = Logger.get();
            String var8 = TAG;
            StringBuilder var12 = new StringBuilder();
            var12.append("Class not found: ");
            var12.append(var2);
            var10.error(var8, var12.toString());
            return null;
         }

         try {
            ListenableWorker var9 = (ListenableWorker)var13.getDeclaredConstructor(Context.class, WorkerParameters.class).newInstance(var1, var3);
            return var9;
         } catch (Exception var6) {
            Logger var5 = Logger.get();
            String var14 = TAG;
            StringBuilder var11 = new StringBuilder();
            var11.append("Could not instantiate ");
            var11.append(var2);
            var5.error(var14, var11.toString(), var6);
            return null;
         }
      }
   }
}
