package com.airbnb.lottie;

import java.util.Arrays;

public final class LottieResult {
   private final Throwable exception;
   private final Object value;

   public LottieResult(Object var1) {
      this.value = var1;
      this.exception = null;
   }

   public LottieResult(Throwable var1) {
      this.exception = var1;
      this.value = null;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof LottieResult)) {
         return false;
      } else {
         LottieResult var2 = (LottieResult)var1;
         if (this.getValue() != null && this.getValue().equals(var2.getValue())) {
            return true;
         } else {
            return this.getException() != null && var2.getException() != null ? this.getException().toString().equals(this.getException().toString()) : false;
         }
      }
   }

   public Throwable getException() {
      return this.exception;
   }

   public Object getValue() {
      return this.value;
   }

   public int hashCode() {
      return Arrays.hashCode(new Object[]{this.getValue(), this.getException()});
   }
}
