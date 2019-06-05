package android.support.transition;

import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TransitionValues {
   final ArrayList mTargetedTransitions = new ArrayList();
   public final Map values = new HashMap();
   public View view;

   public boolean equals(Object var1) {
      if (var1 instanceof TransitionValues) {
         View var2 = this.view;
         TransitionValues var3 = (TransitionValues)var1;
         if (var2 == var3.view && this.values.equals(var3.values)) {
            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      return this.view.hashCode() * 31 + this.values.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("TransitionValues@");
      var1.append(Integer.toHexString(this.hashCode()));
      var1.append(":\n");
      String var2 = var1.toString();
      var1 = new StringBuilder();
      var1.append(var2);
      var1.append("    view = ");
      var1.append(this.view);
      var1.append("\n");
      String var5 = var1.toString();
      StringBuilder var6 = new StringBuilder();
      var6.append(var5);
      var6.append("    values:");
      var5 = var6.toString();

      StringBuilder var4;
      for(Iterator var7 = this.values.keySet().iterator(); var7.hasNext(); var5 = var4.toString()) {
         String var3 = (String)var7.next();
         var4 = new StringBuilder();
         var4.append(var5);
         var4.append("    ");
         var4.append(var3);
         var4.append(": ");
         var4.append(this.values.get(var3));
         var4.append("\n");
      }

      return var5;
   }
}