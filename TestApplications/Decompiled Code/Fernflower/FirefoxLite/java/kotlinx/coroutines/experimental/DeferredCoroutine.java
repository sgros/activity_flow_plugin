package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.internal.Intrinsics;

class DeferredCoroutine extends AbstractCoroutine implements Deferred {
   public DeferredCoroutine(CoroutineContext var1, boolean var2) {
      Intrinsics.checkParameterIsNotNull(var1, "parentContext");
      super(var1, var2);
   }

   // $FF: synthetic method
   static Object await$suspendImpl(final DeferredCoroutine var0, Continuation var1) {
      Object var6;
      label28: {
         if (var1 instanceof <undefinedtype>) {
            <undefinedtype> var2 = (<undefinedtype>)var1;
            if ((var2.getLabel() & Integer.MIN_VALUE) != 0) {
               var2.setLabel(var2.getLabel() + Integer.MIN_VALUE);
               var6 = var2;
               break label28;
            }
         }

         var6 = new CoroutineImpl(var1) {
            Object L$0;
            // $FF: synthetic field
            Object data;
            // $FF: synthetic field
            Throwable exception;

            public final Object doResume(Object var1, Throwable var2) {
               this.data = var1;
               this.exception = var2;
               super.label |= Integer.MIN_VALUE;
               return DeferredCoroutine.await$suspendImpl(var0, this);
            }

            // $FF: synthetic method
            final int getLabel() {
               return super.label;
            }

            // $FF: synthetic method
            final void setLabel(int var1) {
               super.label = var1;
            }
         };
      }

      Object var7 = ((<undefinedtype>)var6).data;
      Throwable var3 = ((<undefinedtype>)var6).exception;
      Object var4 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      Object var5;
      switch(((<undefinedtype>)var6).getLabel()) {
      case 0:
         if (var3 != null) {
            throw var3;
         }

         ((<undefinedtype>)var6).L$0 = var0;
         ((<undefinedtype>)var6).setLabel(1);
         var6 = var0.awaitInternal$kotlinx_coroutines_core((Continuation)var6);
         var5 = var6;
         if (var6 == var4) {
            return var4;
         }
         break;
      case 1:
         var0 = (DeferredCoroutine)((<undefinedtype>)var6).L$0;
         if (var3 != null) {
            throw var3;
         }

         var5 = var7;
         break;
      default:
         throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      return var5;
   }

   public Object await(Continuation var1) {
      return await$suspendImpl(this, var1);
   }
}
