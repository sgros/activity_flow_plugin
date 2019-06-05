package android.arch.lifecycle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class ClassesInfoCache {
   static ClassesInfoCache sInstance = new ClassesInfoCache();
   private final Map mCallbackMap = new HashMap();
   private final Map mHasLifecycleMethods = new HashMap();

   private ClassesInfoCache.CallbackInfo createInfo(Class var1, Method[] var2) {
      Class var3 = var1.getSuperclass();
      HashMap var4 = new HashMap();
      if (var3 != null) {
         ClassesInfoCache.CallbackInfo var12 = this.getInfo(var3);
         if (var12 != null) {
            var4.putAll(var12.mHandlerToEvent);
         }
      }

      Class[] var13 = var1.getInterfaces();
      int var5 = var13.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Iterator var7 = this.getInfo(var13[var6]).mHandlerToEvent.entrySet().iterator();

         while(var7.hasNext()) {
            Entry var8 = (Entry)var7.next();
            this.verifyAndPutHandler(var4, (ClassesInfoCache.MethodReference)var8.getKey(), (Lifecycle.Event)var8.getValue(), var1);
         }
      }

      if (var2 == null) {
         var2 = this.getDeclaredMethods(var1);
      }

      int var9 = var2.length;
      var5 = 0;

      boolean var10;
      for(var10 = false; var5 < var9; ++var5) {
         Method var17 = var2[var5];
         OnLifecycleEvent var15 = (OnLifecycleEvent)var17.getAnnotation(OnLifecycleEvent.class);
         if (var15 != null) {
            var13 = var17.getParameterTypes();
            byte var14;
            if (var13.length > 0) {
               if (!var13[0].isAssignableFrom(LifecycleOwner.class)) {
                  throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
               }

               var14 = 1;
            } else {
               var14 = 0;
            }

            Lifecycle.Event var16 = var15.value();
            if (var13.length > 1) {
               if (!var13[1].isAssignableFrom(Lifecycle.Event.class)) {
                  throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
               }

               if (var16 != Lifecycle.Event.ON_ANY) {
                  throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
               }

               var14 = 2;
            }

            if (var13.length > 2) {
               throw new IllegalArgumentException("cannot have more than 2 params");
            }

            this.verifyAndPutHandler(var4, new ClassesInfoCache.MethodReference(var14, var17), var16, var1);
            var10 = true;
         }
      }

      ClassesInfoCache.CallbackInfo var11 = new ClassesInfoCache.CallbackInfo(var4);
      this.mCallbackMap.put(var1, var11);
      this.mHasLifecycleMethods.put(var1, var10);
      return var11;
   }

   private Method[] getDeclaredMethods(Class var1) {
      try {
         Method[] var3 = var1.getDeclaredMethods();
         return var3;
      } catch (NoClassDefFoundError var2) {
         throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", var2);
      }
   }

   private void verifyAndPutHandler(Map var1, ClassesInfoCache.MethodReference var2, Lifecycle.Event var3, Class var4) {
      Lifecycle.Event var5 = (Lifecycle.Event)var1.get(var2);
      if (var5 != null && var3 != var5) {
         Method var7 = var2.mMethod;
         StringBuilder var6 = new StringBuilder();
         var6.append("Method ");
         var6.append(var7.getName());
         var6.append(" in ");
         var6.append(var4.getName());
         var6.append(" already declared with different @OnLifecycleEvent value: previous value ");
         var6.append(var5);
         var6.append(", new value ");
         var6.append(var3);
         throw new IllegalArgumentException(var6.toString());
      } else {
         if (var5 == null) {
            var1.put(var2, var3);
         }

      }
   }

   ClassesInfoCache.CallbackInfo getInfo(Class var1) {
      ClassesInfoCache.CallbackInfo var2 = (ClassesInfoCache.CallbackInfo)this.mCallbackMap.get(var1);
      return var2 != null ? var2 : this.createInfo(var1, (Method[])null);
   }

   boolean hasLifecycleMethods(Class var1) {
      if (this.mHasLifecycleMethods.containsKey(var1)) {
         return (Boolean)this.mHasLifecycleMethods.get(var1);
      } else {
         Method[] var2 = this.getDeclaredMethods(var1);
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            if ((OnLifecycleEvent)var2[var4].getAnnotation(OnLifecycleEvent.class) != null) {
               this.createInfo(var1, var2);
               return true;
            }
         }

         this.mHasLifecycleMethods.put(var1, false);
         return false;
      }
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

      private static void invokeMethodsForEvent(List var0, LifecycleOwner var1, Lifecycle.Event var2, Object var3) {
         if (var0 != null) {
            for(int var4 = var0.size() - 1; var4 >= 0; --var4) {
               ((ClassesInfoCache.MethodReference)var0.get(var4)).invokeCallback(var1, var2, var3);
            }
         }

      }

      void invokeCallbacks(LifecycleOwner var1, Lifecycle.Event var2, Object var3) {
         invokeMethodsForEvent((List)this.mEventToHandlers.get(var2), var1, var2, var3);
         invokeMethodsForEvent((List)this.mEventToHandlers.get(Lifecycle.Event.ON_ANY), var1, var2, var3);
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
            ClassesInfoCache.MethodReference var3 = (ClassesInfoCache.MethodReference)var1;
            if (this.mCallType != var3.mCallType || !this.mMethod.getName().equals(var3.mMethod.getName())) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.mCallType * 31 + this.mMethod.getName().hashCode();
      }

      void invokeCallback(LifecycleOwner var1, Lifecycle.Event var2, Object var3) {
         try {
            switch(this.mCallType) {
            case 0:
               this.mMethod.invoke(var3);
               break;
            case 1:
               this.mMethod.invoke(var3, var1);
               break;
            case 2:
               this.mMethod.invoke(var3, var1, var2);
            }

         } catch (InvocationTargetException var4) {
            throw new RuntimeException("Failed to call observer method", var4.getCause());
         } catch (IllegalAccessException var5) {
            throw new RuntimeException(var5);
         }
      }
   }
}
