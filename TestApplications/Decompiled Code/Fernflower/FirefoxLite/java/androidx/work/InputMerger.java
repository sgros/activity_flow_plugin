package androidx.work;

import java.util.List;

public abstract class InputMerger {
   private static final String TAG = Logger.tagWithPrefix("InputMerger");

   public static InputMerger fromClassName(String var0) {
      try {
         InputMerger var6 = (InputMerger)Class.forName(var0).newInstance();
         return var6;
      } catch (Exception var5) {
         Logger var3 = Logger.get();
         String var1 = TAG;
         StringBuilder var4 = new StringBuilder();
         var4.append("Trouble instantiating + ");
         var4.append(var0);
         var3.error(var1, var4.toString(), var5);
         return null;
      }
   }

   public abstract Data merge(List var1);
}
