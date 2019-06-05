package kotlin.coroutines.experimental.jvm.internal;

import kotlin.TypeCastException;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

public abstract class CoroutineImpl extends Lambda implements Continuation {
   private final CoroutineContext _context;
   private Continuation _facade;
   protected Continuation completion;
   protected int label;

   public CoroutineImpl(int var1, Continuation var2) {
      super(var1);
      this.completion = var2;
      byte var3;
      if (this.completion != null) {
         var3 = 0;
      } else {
         var3 = -1;
      }

      this.label = var3;
      var2 = this.completion;
      CoroutineContext var4;
      if (var2 != null) {
         var4 = var2.getContext();
      } else {
         var4 = null;
      }

      this._context = var4;
   }

   public Continuation create(Object var1, Continuation var2) {
      Intrinsics.checkParameterIsNotNull(var2, "completion");
      throw (Throwable)(new IllegalStateException("create(Any?;Continuation) has not been overridden"));
   }

   protected abstract Object doResume(Object var1, Throwable var2);

   public CoroutineContext getContext() {
      CoroutineContext var1 = this._context;
      if (var1 == null) {
         Intrinsics.throwNpe();
      }

      return var1;
   }

   public final Continuation getFacade() {
      if (this._facade == null) {
         CoroutineContext var1 = this._context;
         if (var1 == null) {
            Intrinsics.throwNpe();
         }

         this._facade = CoroutineIntrinsics.interceptContinuationIfNeeded(var1, (Continuation)this);
      }

      Continuation var2 = this._facade;
      if (var2 == null) {
         Intrinsics.throwNpe();
      }

      return var2;
   }

   public void resume(Object var1) {
      Continuation var2 = this.completion;
      if (var2 == null) {
         Intrinsics.throwNpe();
      }

      Throwable var10000;
      label31: {
         boolean var10001;
         try {
            var1 = this.doResume(var1, (Throwable)null);
            if (var1 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
               return;
            }
         } catch (Throwable var5) {
            var10000 = var5;
            var10001 = false;
            break label31;
         }

         if (var2 != null) {
            try {
               var2.resume(var1);
               return;
            } catch (Throwable var4) {
               var10000 = var4;
               var10001 = false;
            }
         } else {
            try {
               TypeCastException var7 = new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
               throw var7;
            } catch (Throwable var3) {
               var10000 = var3;
               var10001 = false;
            }
         }
      }

      Throwable var6 = var10000;
      var2.resumeWithException(var6);
   }

   public void resumeWithException(Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      Continuation var2 = this.completion;
      if (var2 == null) {
         Intrinsics.throwNpe();
      }

      Throwable var10000;
      label31: {
         boolean var10001;
         Object var6;
         try {
            var6 = this.doResume((Object)null, var1);
            if (var6 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
               return;
            }
         } catch (Throwable var5) {
            var10000 = var5;
            var10001 = false;
            break label31;
         }

         if (var2 != null) {
            try {
               var2.resume(var6);
               return;
            } catch (Throwable var4) {
               var10000 = var4;
               var10001 = false;
            }
         } else {
            try {
               TypeCastException var7 = new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
               throw var7;
            } catch (Throwable var3) {
               var10000 = var3;
               var10001 = false;
            }
         }
      }

      var1 = var10000;
      var2.resumeWithException(var1);
   }
}
