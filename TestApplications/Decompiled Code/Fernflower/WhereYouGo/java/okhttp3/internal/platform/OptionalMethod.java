package okhttp3.internal.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class OptionalMethod {
   private final String methodName;
   private final Class[] methodParams;
   private final Class returnType;

   public OptionalMethod(Class var1, String var2, Class... var3) {
      this.returnType = var1;
      this.methodName = var2;
      this.methodParams = var3;
   }

   private Method getMethod(Class var1) {
      Method var2 = null;
      if (this.methodName != null) {
         Method var3 = getPublicMethod(var1, this.methodName, this.methodParams);
         var2 = var3;
         if (var3 != null) {
            var2 = var3;
            if (this.returnType != null) {
               var2 = var3;
               if (!this.returnType.isAssignableFrom(var3.getReturnType())) {
                  var2 = null;
               }
            }
         }
      }

      return var2;
   }

   private static Method getPublicMethod(Class var0, String var1, Class[] var2) {
      Method var3 = null;

      boolean var10001;
      Method var7;
      try {
         var7 = var0.getMethod(var1, var2);
      } catch (NoSuchMethodException var6) {
         var10001 = false;
         return var3;
      }

      var3 = var7;

      int var4;
      try {
         var4 = var7.getModifiers();
      } catch (NoSuchMethodException var5) {
         var10001 = false;
         return var3;
      }

      var3 = var7;
      if ((var4 & 1) == 0) {
         var3 = null;
      }

      return var3;
   }

   public Object invoke(Object var1, Object... var2) throws InvocationTargetException {
      Method var3 = this.getMethod(var1.getClass());
      if (var3 == null) {
         throw new AssertionError("Method " + this.methodName + " not supported for object " + var1);
      } else {
         try {
            var1 = var3.invoke(var1, var2);
            return var1;
         } catch (IllegalAccessException var4) {
            AssertionError var5 = new AssertionError("Unexpectedly could not call: " + var3);
            var5.initCause(var4);
            throw var5;
         }
      }
   }

   public Object invokeOptional(Object var1, Object... var2) throws InvocationTargetException {
      Object var3 = null;
      Method var4 = this.getMethod(var1.getClass());
      if (var4 == null) {
         var1 = var3;
      } else {
         try {
            var1 = var4.invoke(var1, var2);
         } catch (IllegalAccessException var5) {
            var1 = var3;
         }
      }

      return var1;
   }

   public Object invokeOptionalWithoutCheckedException(Object var1, Object... var2) {
      try {
         var1 = this.invokeOptional(var1, var2);
         return var1;
      } catch (InvocationTargetException var3) {
         Throwable var4 = var3.getTargetException();
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            AssertionError var5 = new AssertionError("Unexpected exception");
            var5.initCause(var4);
            throw var5;
         }
      }
   }

   public Object invokeWithoutCheckedException(Object var1, Object... var2) {
      try {
         var1 = this.invoke(var1, var2);
         return var1;
      } catch (InvocationTargetException var3) {
         Throwable var4 = var3.getTargetException();
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            AssertionError var5 = new AssertionError("Unexpected exception");
            var5.initCause(var4);
            throw var5;
         }
      }
   }

   public boolean isSupported(Object var1) {
      boolean var2;
      if (this.getMethod(var1.getClass()) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
