package android.arch.lifecycle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lifecycling {
   private static Map sCallbackCache = new HashMap();
   private static Map sClassToAdapters = new HashMap();

   private static GeneratedAdapter createGeneratedAdapter(Constructor var0, Object var1) {
      try {
         GeneratedAdapter var5 = (GeneratedAdapter)var0.newInstance(var1);
         return var5;
      } catch (IllegalAccessException var2) {
         throw new RuntimeException(var2);
      } catch (InstantiationException var3) {
         throw new RuntimeException(var3);
      } catch (InvocationTargetException var4) {
         throw new RuntimeException(var4);
      }
   }

   private static Constructor generatedConstructor(Class var0) {
      NoSuchMethodException var10000;
      label82: {
         Package var1;
         String var2;
         boolean var10001;
         try {
            var1 = var0.getPackage();
            var2 = var0.getCanonicalName();
         } catch (ClassNotFoundException var16) {
            var10001 = false;
            return null;
         } catch (NoSuchMethodException var17) {
            var10000 = var17;
            var10001 = false;
            break label82;
         }

         String var20;
         if (var1 != null) {
            try {
               var20 = var1.getName();
            } catch (ClassNotFoundException var14) {
               var10001 = false;
               return null;
            } catch (NoSuchMethodException var15) {
               var10000 = var15;
               var10001 = false;
               break label82;
            }
         } else {
            var20 = "";
         }

         label83: {
            label68:
            try {
               if (!var20.isEmpty()) {
                  break label68;
               }
               break label83;
            } catch (ClassNotFoundException var12) {
               var10001 = false;
               return null;
            } catch (NoSuchMethodException var13) {
               var10000 = var13;
               var10001 = false;
               break label82;
            }

            try {
               var2 = var2.substring(var20.length() + 1);
            } catch (ClassNotFoundException var10) {
               var10001 = false;
               return null;
            } catch (NoSuchMethodException var11) {
               var10000 = var11;
               var10001 = false;
               break label82;
            }
         }

         label61: {
            label60: {
               try {
                  var2 = getAdapterName(var2);
                  if (!var20.isEmpty()) {
                     break label60;
                  }
               } catch (ClassNotFoundException var8) {
                  var10001 = false;
                  return null;
               } catch (NoSuchMethodException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label82;
               }

               var20 = var2;
               break label61;
            }

            try {
               StringBuilder var3 = new StringBuilder();
               var3.append(var20);
               var3.append(".");
               var3.append(var2);
               var20 = var3.toString();
            } catch (ClassNotFoundException var6) {
               var10001 = false;
               return null;
            } catch (NoSuchMethodException var7) {
               var10000 = var7;
               var10001 = false;
               break label82;
            }
         }

         try {
            Constructor var19 = Class.forName(var20).getDeclaredConstructor(var0);
            if (!var19.isAccessible()) {
               var19.setAccessible(true);
            }

            return var19;
         } catch (ClassNotFoundException var4) {
            var10001 = false;
            return null;
         } catch (NoSuchMethodException var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      NoSuchMethodException var18 = var10000;
      throw new RuntimeException(var18);
   }

   public static String getAdapterName(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(var0.replace(".", "_"));
      var1.append("_LifecycleAdapter");
      return var1.toString();
   }

   static GenericLifecycleObserver getCallback(Object var0) {
      if (var0 instanceof FullLifecycleObserver) {
         return new FullLifecycleObserverAdapter((FullLifecycleObserver)var0);
      } else if (var0 instanceof GenericLifecycleObserver) {
         return (GenericLifecycleObserver)var0;
      } else {
         Class var1 = var0.getClass();
         if (getObserverConstructorType(var1) != 2) {
            return new ReflectiveGenericLifecycleObserver(var0);
         } else {
            List var2 = (List)sClassToAdapters.get(var1);
            int var3 = var2.size();
            int var4 = 0;
            if (var3 == 1) {
               return new SingleGeneratedAdapterObserver(createGeneratedAdapter((Constructor)var2.get(0), var0));
            } else {
               GeneratedAdapter[] var5;
               for(var5 = new GeneratedAdapter[var2.size()]; var4 < var2.size(); ++var4) {
                  var5[var4] = createGeneratedAdapter((Constructor)var2.get(var4), var0);
               }

               return new CompositeGeneratedAdaptersObserver(var5);
            }
         }
      }
   }

   private static int getObserverConstructorType(Class var0) {
      if (sCallbackCache.containsKey(var0)) {
         return (Integer)sCallbackCache.get(var0);
      } else {
         int var1 = resolveObserverCallbackType(var0);
         sCallbackCache.put(var0, var1);
         return var1;
      }
   }

   private static boolean isLifecycleParent(Class var0) {
      boolean var1;
      if (var0 != null && LifecycleObserver.class.isAssignableFrom(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static int resolveObserverCallbackType(Class var0) {
      if (var0.getCanonicalName() == null) {
         return 1;
      } else {
         Constructor var1 = generatedConstructor(var0);
         if (var1 != null) {
            sClassToAdapters.put(var0, Collections.singletonList(var1));
            return 2;
         } else if (ClassesInfoCache.sInstance.hasLifecycleMethods(var0)) {
            return 1;
         } else {
            Class var7 = var0.getSuperclass();
            ArrayList var2 = null;
            if (isLifecycleParent(var7)) {
               if (getObserverConstructorType(var7) == 1) {
                  return 1;
               }

               var2 = new ArrayList((Collection)sClassToAdapters.get(var7));
            }

            Class[] var3 = var0.getInterfaces();
            int var4 = var3.length;

            ArrayList var8;
            for(int var5 = 0; var5 < var4; var2 = var8) {
               Class var6 = var3[var5];
               if (!isLifecycleParent(var6)) {
                  var8 = var2;
               } else {
                  if (getObserverConstructorType(var6) == 1) {
                     return 1;
                  }

                  var8 = var2;
                  if (var2 == null) {
                     var8 = new ArrayList();
                  }

                  var8.addAll((Collection)sClassToAdapters.get(var6));
               }

               ++var5;
            }

            if (var2 != null) {
               sClassToAdapters.put(var0, var2);
               return 2;
            } else {
               return 1;
            }
         }
      }
   }
}
