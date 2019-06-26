package android.arch.lifecycle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class ReflectiveGenericLifecycleObserver implements GenericLifecycleObserver {
   private static final int CALL_TYPE_NO_ARG = 0;
   private static final int CALL_TYPE_PROVIDER = 1;
   private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
   static final Map sInfoCache = new HashMap();
   private final ReflectiveGenericLifecycleObserver.CallbackInfo mInfo;
   private final Object mWrapped;

   ReflectiveGenericLifecycleObserver(Object var1) {
      this.mWrapped = var1;
      this.mInfo = getInfo(this.mWrapped.getClass());
   }

   private static ReflectiveGenericLifecycleObserver.CallbackInfo createInfo(Class var0) {
      Class var1 = var0.getSuperclass();
      HashMap var2 = new HashMap();
      if (var1 != null) {
         ReflectiveGenericLifecycleObserver.CallbackInfo var9 = getInfo(var1);
         if (var9 != null) {
            var2.putAll(var9.mHandlerToEvent);
         }
      }

      Method[] var10 = var0.getDeclaredMethods();
      Class[] var3 = var0.getInterfaces();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Iterator var6 = getInfo(var3[var5]).mHandlerToEvent.entrySet().iterator();

         while(var6.hasNext()) {
            Entry var7 = (Entry)var6.next();
            verifyAndPutHandler(var2, (ReflectiveGenericLifecycleObserver.MethodReference)var7.getKey(), (Lifecycle.Event)var7.getValue(), var0);
         }
      }

      int var8 = var10.length;

      for(var4 = 0; var4 < var8; ++var4) {
         Method var15 = var10[var4];
         OnLifecycleEvent var12 = (OnLifecycleEvent)var15.getAnnotation(OnLifecycleEvent.class);
         if (var12 != null) {
            Class[] var16 = var15.getParameterTypes();
            byte var14;
            if (var16.length > 0) {
               if (!var16[0].isAssignableFrom(LifecycleOwner.class)) {
                  throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
               }

               var14 = 1;
            } else {
               var14 = 0;
            }

            Lifecycle.Event var13 = var12.value();
            if (var16.length > 1) {
               if (!var16[1].isAssignableFrom(Lifecycle.Event.class)) {
                  throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
               }

               if (var13 != Lifecycle.Event.ON_ANY) {
                  throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
               }

               var14 = 2;
            }

            if (var16.length > 2) {
               throw new IllegalArgumentException("cannot have more than 2 params");
            }

            verifyAndPutHandler(var2, new ReflectiveGenericLifecycleObserver.MethodReference(var14, var15), var13, var0);
         }
      }

      ReflectiveGenericLifecycleObserver.CallbackInfo var11 = new ReflectiveGenericLifecycleObserver.CallbackInfo(var2);
      sInfoCache.put(var0, var11);
      return var11;
   }

   private static ReflectiveGenericLifecycleObserver.CallbackInfo getInfo(Class var0) {
      ReflectiveGenericLifecycleObserver.CallbackInfo var1 = (ReflectiveGenericLifecycleObserver.CallbackInfo)sInfoCache.get(var0);
      return var1 != null ? var1 : createInfo(var0);
   }

   private void invokeCallback(ReflectiveGenericLifecycleObserver.MethodReference var1, LifecycleOwner var2, Lifecycle.Event var3) {
      try {
         switch(var1.mCallType) {
         case 0:
            var1.mMethod.invoke(this.mWrapped);
            break;
         case 1:
            var1.mMethod.invoke(this.mWrapped, var2);
            break;
         case 2:
            var1.mMethod.invoke(this.mWrapped, var2, var3);
         }

      } catch (InvocationTargetException var4) {
         throw new RuntimeException("Failed to call observer method", var4.getCause());
      } catch (IllegalAccessException var5) {
         throw new RuntimeException(var5);
      }
   }

   private void invokeCallbacks(ReflectiveGenericLifecycleObserver.CallbackInfo var1, LifecycleOwner var2, Lifecycle.Event var3) {
      this.invokeMethodsForEvent((List)var1.mEventToHandlers.get(var3), var2, var3);
      this.invokeMethodsForEvent((List)var1.mEventToHandlers.get(Lifecycle.Event.ON_ANY), var2, var3);
   }

   private void invokeMethodsForEvent(List var1, LifecycleOwner var2, Lifecycle.Event var3) {
      if (var1 != null) {
         for(int var4 = var1.size() - 1; var4 >= 0; --var4) {
            this.invokeCallback((ReflectiveGenericLifecycleObserver.MethodReference)var1.get(var4), var2, var3);
         }
      }

   }

   private static void verifyAndPutHandler(Map var0, ReflectiveGenericLifecycleObserver.MethodReference var1, Lifecycle.Event var2, Class var3) {
      Lifecycle.Event var4 = (Lifecycle.Event)var0.get(var1);
      if (var4 != null && var2 != var4) {
         Method var5 = var1.mMethod;
         StringBuilder var6 = new StringBuilder();
         var6.append("Method ");
         var6.append(var5.getName());
         var6.append(" in ");
         var6.append(var3.getName());
         var6.append(" already declared with different @OnLifecycleEvent value: previous");
         var6.append(" value ");
         var6.append(var4);
         var6.append(", new value ");
         var6.append(var2);
         throw new IllegalArgumentException(var6.toString());
      } else {
         if (var4 == null) {
            var0.put(var1, var2);
         }

      }
   }

   public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
      this.invokeCallbacks(this.mInfo, var1, var2);
   }

   static class CallbackInfo {
      final Map mEventToHandlers;
      final Map mHandlerToEvent;

      CallbackInfo(Map var1) {
         this.mHandlerToEvent = var1;
         this.mEventToHandlers = new HashMap();

         Entry var3;
         Object var6;
         for(Iterator var2 = var1.entrySet().iterator(); var2.hasNext(); ((List)var6).add(var3.getKey())) {
            var3 = (Entry)var2.next();
            Lifecycle.Event var4 = (Lifecycle.Event)var3.getValue();
            List var5 = (List)this.mEventToHandlers.get(var4);
            var6 = var5;
            if (var5 == null) {
               var6 = new ArrayList();
               this.mEventToHandlers.put(var4, var6);
            }
         }

      }
   }

   static class MethodReference {
      final int mCallType;
      final Method mMethod;

      MethodReference(int var1, Method var2) {
         this.mCallType = var1;
         this.mMethod = var2;
         this.mMethod.setAccessible(true);
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            ReflectiveGenericLifecycleObserver.MethodReference var3 = (ReflectiveGenericLifecycleObserver.MethodReference)var1;
            if (this.mCallType != var3.mCallType || !this.mMethod.getName().equals(var3.mMethod.getName())) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return 31 * this.mCallType + this.mMethod.getName().hashCode();
      }
   }
}
