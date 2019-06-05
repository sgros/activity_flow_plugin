package androidx.work;

import android.annotation.SuppressLint;

public interface Operation {
   @SuppressLint({"SyntheticAccessor"})
   Operation.State.IN_PROGRESS IN_PROGRESS = new Operation.State.IN_PROGRESS();
   @SuppressLint({"SyntheticAccessor"})
   Operation.State.SUCCESS SUCCESS = new Operation.State.SUCCESS();

   public abstract static class State {
      State() {
      }

      public static final class FAILURE extends Operation.State {
         private final Throwable mThrowable;

         public FAILURE(Throwable var1) {
            this.mThrowable = var1;
         }

         public Throwable getThrowable() {
            return this.mThrowable;
         }

         public String toString() {
            return String.format("FAILURE (%s)", this.mThrowable.getMessage());
         }
      }

      public static final class IN_PROGRESS extends Operation.State {
         private IN_PROGRESS() {
         }

         // $FF: synthetic method
         IN_PROGRESS(Object var1) {
            this();
         }

         public String toString() {
            return "IN_PROGRESS";
         }
      }

      public static final class SUCCESS extends Operation.State {
         private SUCCESS() {
         }

         // $FF: synthetic method
         SUCCESS(Object var1) {
            this();
         }

         public String toString() {
            return "SUCCESS";
         }
      }
   }
}
