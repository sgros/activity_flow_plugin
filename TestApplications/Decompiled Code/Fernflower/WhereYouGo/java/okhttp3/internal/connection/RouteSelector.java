package okhttp3.internal.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.Address;
import okhttp3.HttpUrl;
import okhttp3.Route;
import okhttp3.internal.Util;

public final class RouteSelector {
   private final Address address;
   private List inetSocketAddresses = Collections.emptyList();
   private InetSocketAddress lastInetSocketAddress;
   private Proxy lastProxy;
   private int nextInetSocketAddressIndex;
   private int nextProxyIndex;
   private final List postponedRoutes = new ArrayList();
   private List proxies = Collections.emptyList();
   private final RouteDatabase routeDatabase;

   public RouteSelector(Address var1, RouteDatabase var2) {
      this.address = var1;
      this.routeDatabase = var2;
      this.resetNextProxy(var1.url(), var1.proxy());
   }

   static String getHostString(InetSocketAddress var0) {
      InetAddress var1 = var0.getAddress();
      String var2;
      if (var1 == null) {
         var2 = var0.getHostName();
      } else {
         var2 = var1.getHostAddress();
      }

      return var2;
   }

   private boolean hasNextInetSocketAddress() {
      boolean var1;
      if (this.nextInetSocketAddressIndex < this.inetSocketAddresses.size()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean hasNextPostponed() {
      boolean var1;
      if (!this.postponedRoutes.isEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean hasNextProxy() {
      boolean var1;
      if (this.nextProxyIndex < this.proxies.size()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private InetSocketAddress nextInetSocketAddress() throws IOException {
      if (!this.hasNextInetSocketAddress()) {
         throw new SocketException("No route to " + this.address.url().host() + "; exhausted inet socket addresses: " + this.inetSocketAddresses);
      } else {
         List var1 = this.inetSocketAddresses;
         int var2 = this.nextInetSocketAddressIndex++;
         return (InetSocketAddress)var1.get(var2);
      }
   }

   private Route nextPostponed() {
      return (Route)this.postponedRoutes.remove(0);
   }

   private Proxy nextProxy() throws IOException {
      if (!this.hasNextProxy()) {
         throw new SocketException("No route to " + this.address.url().host() + "; exhausted proxy configurations: " + this.proxies);
      } else {
         List var1 = this.proxies;
         int var2 = this.nextProxyIndex++;
         Proxy var3 = (Proxy)var1.get(var2);
         this.resetNextInetSocketAddress(var3);
         return var3;
      }
   }

   private void resetNextInetSocketAddress(Proxy var1) throws IOException {
      this.inetSocketAddresses = new ArrayList();
      String var2;
      int var3;
      if (var1.type() != Type.DIRECT && var1.type() != Type.SOCKS) {
         SocketAddress var8 = var1.address();
         if (!(var8 instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Proxy.address() is not an InetSocketAddress: " + var8.getClass());
         }

         InetSocketAddress var4 = (InetSocketAddress)var8;
         var2 = getHostString(var4);
         var3 = var4.getPort();
      } else {
         var2 = this.address.url().host();
         var3 = this.address.url().port();
      }

      if (var3 >= 1 && var3 <= 65535) {
         if (var1.type() == Type.SOCKS) {
            this.inetSocketAddresses.add(InetSocketAddress.createUnresolved(var2, var3));
         } else {
            List var7 = this.address.dns().lookup(var2);
            int var5 = 0;

            for(int var6 = var7.size(); var5 < var6; ++var5) {
               InetAddress var9 = (InetAddress)var7.get(var5);
               this.inetSocketAddresses.add(new InetSocketAddress(var9, var3));
            }
         }

         this.nextInetSocketAddressIndex = 0;
      } else {
         throw new SocketException("No route to " + var2 + ":" + var3 + "; port is out of range");
      }
   }

   private void resetNextProxy(HttpUrl var1, Proxy var2) {
      if (var2 != null) {
         this.proxies = Collections.singletonList(var2);
      } else {
         List var3 = this.address.proxySelector().select(var1.uri());
         if (var3 != null && !var3.isEmpty()) {
            var3 = Util.immutableList(var3);
         } else {
            var3 = Util.immutableList((Object[])(Proxy.NO_PROXY));
         }

         this.proxies = var3;
      }

      this.nextProxyIndex = 0;
   }

   public void connectFailed(Route var1, IOException var2) {
      if (var1.proxy().type() != Type.DIRECT && this.address.proxySelector() != null) {
         this.address.proxySelector().connectFailed(this.address.url().uri(), var1.proxy().address(), var2);
      }

      this.routeDatabase.failed(var1);
   }

   public boolean hasNext() {
      boolean var1;
      if (!this.hasNextInetSocketAddress() && !this.hasNextProxy() && !this.hasNextPostponed()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public Route next() throws IOException {
      Route var1;
      if (!this.hasNextInetSocketAddress()) {
         if (!this.hasNextProxy()) {
            if (!this.hasNextPostponed()) {
               throw new NoSuchElementException();
            }

            var1 = this.nextPostponed();
            return var1;
         }

         this.lastProxy = this.nextProxy();
      }

      this.lastInetSocketAddress = this.nextInetSocketAddress();
      Route var2 = new Route(this.address, this.lastProxy, this.lastInetSocketAddress);
      var1 = var2;
      if (this.routeDatabase.shouldPostpone(var2)) {
         this.postponedRoutes.add(var2);
         var1 = this.next();
      }

      return var1;
   }
}
