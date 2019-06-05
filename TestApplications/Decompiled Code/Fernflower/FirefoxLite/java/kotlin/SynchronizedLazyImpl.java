package kotlin;

import java.io.Serializable;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

final class SynchronizedLazyImpl implements Serializable, Lazy {
   private volatile Object _value;
   private Function0 initializer;
   private final Object lock;

   public SynchronizedLazyImpl(Function0 var1, Object var2) {
      Intrinsics.checkParameterIsNotNull(var1, "initializer");
      super();
      this.initializer = var1;
      this._value = UNINITIALIZED_VALUE.INSTANCE;
      if (var2 == null) {
         var2 = this;
      }

      this.lock = var2;
   }

   // $FF: synthetic method
   public SynchronizedLazyImpl(Function0 var1, Object var2, int var3, DefaultConstructorMarker var4) {
      if ((var3 & 2) != 0) {
         var2 = null;
      }

      this(var1, var2);
   }

   public Object getValue() {
      Object var1 = this._value;
      if (var1 != UNINITIALIZED_VALUE.INSTANCE) {
         return var1;
      } else {
         Object var2 = this.lock;
         synchronized(var2){}

         Throwable var10000;
         label196: {
            boolean var10001;
            try {
               var1 = this._value;
               if (var1 != UNINITIALIZED_VALUE.INSTANCE) {
                  return var1;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label196;
            }

            Function0 var23;
            try {
               var23 = this.initializer;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label196;
            }

            if (var23 == null) {
               try {
                  Intrinsics.throwNpe();
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label196;
               }
            }

            label179:
            try {
               var1 = var23.invoke();
               this._value = var1;
               this.initializer = (Function0)null;
               return var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label179;
            }
         }

         Throwable var24 = var10000;
         throw var24;
      }
   }

   public boolean isInitialized() {
      boolean var1;
      if (this._value != UNINITIALIZED_VALUE.INSTANCE) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public String toString() {
      String var1;
      if (this.isInitialized()) {
         var1 = String.valueOf(this.getValue());
      } else {
         var1 = "Lazy value not initialized yet.";
      }

      return var1;
   }
}
