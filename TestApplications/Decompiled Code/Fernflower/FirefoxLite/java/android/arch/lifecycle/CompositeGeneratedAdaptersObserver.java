package android.arch.lifecycle;

public class CompositeGeneratedAdaptersObserver implements GenericLifecycleObserver {
   private final GeneratedAdapter[] mGeneratedAdapters;

   CompositeGeneratedAdaptersObserver(GeneratedAdapter[] var1) {
      this.mGeneratedAdapters = var1;
   }

   public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
      MethodCallsLogger var3 = new MethodCallsLogger();
      GeneratedAdapter[] var4 = this.mGeneratedAdapters;
      int var5 = var4.length;
      byte var6 = 0;

      int var7;
      for(var7 = 0; var7 < var5; ++var7) {
         var4[var7].callMethods(var1, var2, false, var3);
      }

      var4 = this.mGeneratedAdapters;
      var5 = var4.length;

      for(var7 = var6; var7 < var5; ++var7) {
         var4[var7].callMethods(var1, var2, true, var3);
      }

   }
}
