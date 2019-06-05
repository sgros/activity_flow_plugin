package okhttp3;

import okhttp3.internal.Util;

public final class Challenge {
   private final String realm;
   private final String scheme;

   public Challenge(String var1, String var2) {
      this.scheme = var1;
      this.realm = var2;
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof Challenge && Util.equal(this.scheme, ((Challenge)var1).scheme) && Util.equal(this.realm, ((Challenge)var1).realm)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public int hashCode() {
      int var1 = 0;
      int var2;
      if (this.realm != null) {
         var2 = this.realm.hashCode();
      } else {
         var2 = 0;
      }

      if (this.scheme != null) {
         var1 = this.scheme.hashCode();
      }

      return (var2 + 899) * 31 + var1;
   }

   public String realm() {
      return this.realm;
   }

   public String scheme() {
      return this.scheme;
   }

   public String toString() {
      return this.scheme + " realm=\"" + this.realm + "\"";
   }
}
