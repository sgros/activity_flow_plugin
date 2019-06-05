package androidx.work.impl.constraints;

public class NetworkState {
   private boolean mIsConnected;
   private boolean mIsMetered;
   private boolean mIsNotRoaming;
   private boolean mIsValidated;

   public NetworkState(boolean var1, boolean var2, boolean var3, boolean var4) {
      this.mIsConnected = var1;
      this.mIsValidated = var2;
      this.mIsMetered = var3;
      this.mIsNotRoaming = var4;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         NetworkState var3 = (NetworkState)var1;
         if (this.mIsConnected != var3.mIsConnected || this.mIsValidated != var3.mIsValidated || this.mIsMetered != var3.mIsMetered || this.mIsNotRoaming != var3.mIsNotRoaming) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      byte var1;
      if (this.mIsConnected) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      int var2 = var1;
      if (this.mIsValidated) {
         var2 = var1 + 16;
      }

      int var3 = var2;
      if (this.mIsMetered) {
         var3 = var2 + 256;
      }

      var2 = var3;
      if (this.mIsNotRoaming) {
         var2 = var3 + 4096;
      }

      return var2;
   }

   public boolean isConnected() {
      return this.mIsConnected;
   }

   public boolean isMetered() {
      return this.mIsMetered;
   }

   public boolean isNotRoaming() {
      return this.mIsNotRoaming;
   }

   public boolean isValidated() {
      return this.mIsValidated;
   }

   public String toString() {
      return String.format("[ Connected=%b Validated=%b Metered=%b NotRoaming=%b ]", this.mIsConnected, this.mIsValidated, this.mIsMetered, this.mIsNotRoaming);
   }
}
