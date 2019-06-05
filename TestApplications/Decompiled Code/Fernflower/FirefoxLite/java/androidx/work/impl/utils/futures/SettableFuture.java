package androidx.work.impl.utils.futures;

import com.google.common.util.concurrent.ListenableFuture;

public final class SettableFuture extends AbstractFuture {
   private SettableFuture() {
   }

   public static SettableFuture create() {
      return new SettableFuture();
   }

   public boolean set(Object var1) {
      return super.set(var1);
   }

   public boolean setException(Throwable var1) {
      return super.setException(var1);
   }

   public boolean setFuture(ListenableFuture var1) {
      return super.setFuture(var1);
   }
}
