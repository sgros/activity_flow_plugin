package androidx.work;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class ArrayCreatingInputMerger extends InputMerger {
   private Object concatenateArrayAndNonArray(Object var1, Object var2) {
      int var3 = Array.getLength(var1);
      Object var4 = Array.newInstance(var2.getClass(), var3 + 1);
      System.arraycopy(var1, 0, var4, 0, var3);
      Array.set(var4, var3, var2);
      return var4;
   }

   private Object concatenateArrays(Object var1, Object var2) {
      int var3 = Array.getLength(var1);
      int var4 = Array.getLength(var2);
      Object var5 = Array.newInstance(var1.getClass().getComponentType(), var3 + var4);
      System.arraycopy(var1, 0, var5, 0, var3);
      System.arraycopy(var2, 0, var5, var3, var4);
      return var5;
   }

   private Object concatenateNonArrays(Object var1, Object var2) {
      Object var3 = Array.newInstance(var1.getClass(), 2);
      Array.set(var3, 0, var1);
      Array.set(var3, 1, var2);
      return var3;
   }

   private Object createArrayFor(Object var1) {
      Object var2 = Array.newInstance(var1.getClass(), 1);
      Array.set(var2, 0, var1);
      return var2;
   }

   public Data merge(List var1) {
      Data.Builder var2 = new Data.Builder();
      HashMap var3 = new HashMap();
      Iterator var4 = var1.iterator();

      String var6;
      Object var11;
      while(var4.hasNext()) {
         for(Iterator var5 = ((Data)var4.next()).getKeyValueMap().entrySet().iterator(); var5.hasNext(); var3.put(var6, var11)) {
            Entry var10 = (Entry)var5.next();
            var6 = (String)var10.getKey();
            var11 = var10.getValue();
            Class var7 = var11.getClass();
            Object var8 = var3.get(var6);
            if (var8 == null) {
               if (!var7.isArray()) {
                  var11 = this.createArrayFor(var11);
               }
            } else {
               Class var9 = var8.getClass();
               if (var9.equals(var7)) {
                  if (var9.isArray()) {
                     var11 = this.concatenateArrays(var8, var11);
                  } else {
                     var11 = this.concatenateNonArrays(var8, var11);
                  }
               } else if (var9.isArray() && var9.getComponentType().equals(var7)) {
                  var11 = this.concatenateArrayAndNonArray(var8, var11);
               } else {
                  if (!var7.isArray() || !var7.getComponentType().equals(var9)) {
                     throw new IllegalArgumentException();
                  }

                  var11 = this.concatenateArrayAndNonArray(var11, var8);
               }
            }
         }
      }

      var2.putAll((Map)var3);
      return var2.build();
   }
}
