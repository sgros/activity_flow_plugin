package okhttp3;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

public final class Route {
   final Address address;
   final InetSocketAddress inetSocketAddress;
   final Proxy proxy;

   public Route(Address var1, Proxy var2, InetSocketAddress var3) {
      if (var1 == null) {
         throw new NullPointerException("address == null");
      } else if (var2 == null) {
         throw new NullPointerException("proxy == null");
      } else if (var3 == null) {
         throw new NullPointerException("inetSocketAddress == null");
      } else {
         this.address = var1;
         this.proxy = var2;
         this.inetSocketAddress = var3;
      }
   }

   public Address address() {
      return this.address;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 instanceof Route) {
         Route var4 = (Route)var1;
         var3 = var2;
         if (this.address.equals(var4.address)) {
            var3 = var2;
            if (this.proxy.equals(var4.proxy)) {
               var3 = var2;
               if (this.inetSocketAddress.equals(var4.inetSocketAddress)) {
                  var3 = true;
               }
            }
         }
      }

      return var3;
   }

   public int hashCode() {
      return ((this.address.hashCode() + 527) * 31 + this.proxy.hashCode()) * 31 + this.inetSocketAddress.hashCode();
   }

   public Proxy proxy() {
      return this.proxy;
   }

   public boolean requiresTunnel() {
      boolean var1;
      if (this.address.sslSocketFactory != null && this.proxy.type() == Type.HTTP) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public InetSocketAddress socketAddress() {
      return this.inetSocketAddress;
   }

   public String toString() {
      return "Route{" + this.inetSocketAddress + "}";
   }
}
