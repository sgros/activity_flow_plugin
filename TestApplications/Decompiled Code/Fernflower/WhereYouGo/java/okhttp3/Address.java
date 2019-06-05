package okhttp3;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.internal.Util;

public final class Address {
   final CertificatePinner certificatePinner;
   final List connectionSpecs;
   final Dns dns;
   final HostnameVerifier hostnameVerifier;
   final List protocols;
   final Proxy proxy;
   final Authenticator proxyAuthenticator;
   final ProxySelector proxySelector;
   final SocketFactory socketFactory;
   final SSLSocketFactory sslSocketFactory;
   final HttpUrl url;

   public Address(String var1, int var2, Dns var3, SocketFactory var4, SSLSocketFactory var5, HostnameVerifier var6, CertificatePinner var7, Authenticator var8, Proxy var9, List var10, List var11, ProxySelector var12) {
      HttpUrl.Builder var13 = new HttpUrl.Builder();
      String var14;
      if (var5 != null) {
         var14 = "https";
      } else {
         var14 = "http";
      }

      this.url = var13.scheme(var14).host(var1).port(var2).build();
      if (var3 == null) {
         throw new NullPointerException("dns == null");
      } else {
         this.dns = var3;
         if (var4 == null) {
            throw new NullPointerException("socketFactory == null");
         } else {
            this.socketFactory = var4;
            if (var8 == null) {
               throw new NullPointerException("proxyAuthenticator == null");
            } else {
               this.proxyAuthenticator = var8;
               if (var10 == null) {
                  throw new NullPointerException("protocols == null");
               } else {
                  this.protocols = Util.immutableList(var10);
                  if (var11 == null) {
                     throw new NullPointerException("connectionSpecs == null");
                  } else {
                     this.connectionSpecs = Util.immutableList(var11);
                     if (var12 == null) {
                        throw new NullPointerException("proxySelector == null");
                     } else {
                        this.proxySelector = var12;
                        this.proxy = var9;
                        this.sslSocketFactory = var5;
                        this.hostnameVerifier = var6;
                        this.certificatePinner = var7;
                     }
                  }
               }
            }
         }
      }
   }

   public CertificatePinner certificatePinner() {
      return this.certificatePinner;
   }

   public List connectionSpecs() {
      return this.connectionSpecs;
   }

   public Dns dns() {
      return this.dns;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 instanceof Address) {
         Address var4 = (Address)var1;
         var3 = var2;
         if (this.url.equals(var4.url)) {
            var3 = var2;
            if (this.dns.equals(var4.dns)) {
               var3 = var2;
               if (this.proxyAuthenticator.equals(var4.proxyAuthenticator)) {
                  var3 = var2;
                  if (this.protocols.equals(var4.protocols)) {
                     var3 = var2;
                     if (this.connectionSpecs.equals(var4.connectionSpecs)) {
                        var3 = var2;
                        if (this.proxySelector.equals(var4.proxySelector)) {
                           var3 = var2;
                           if (Util.equal(this.proxy, var4.proxy)) {
                              var3 = var2;
                              if (Util.equal(this.sslSocketFactory, var4.sslSocketFactory)) {
                                 var3 = var2;
                                 if (Util.equal(this.hostnameVerifier, var4.hostnameVerifier)) {
                                    var3 = var2;
                                    if (Util.equal(this.certificatePinner, var4.certificatePinner)) {
                                       var3 = true;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   public int hashCode() {
      int var1 = 0;
      int var2 = this.url.hashCode();
      int var3 = this.dns.hashCode();
      int var4 = this.proxyAuthenticator.hashCode();
      int var5 = this.protocols.hashCode();
      int var6 = this.connectionSpecs.hashCode();
      int var7 = this.proxySelector.hashCode();
      int var8;
      if (this.proxy != null) {
         var8 = this.proxy.hashCode();
      } else {
         var8 = 0;
      }

      int var9;
      if (this.sslSocketFactory != null) {
         var9 = this.sslSocketFactory.hashCode();
      } else {
         var9 = 0;
      }

      int var10;
      if (this.hostnameVerifier != null) {
         var10 = this.hostnameVerifier.hashCode();
      } else {
         var10 = 0;
      }

      if (this.certificatePinner != null) {
         var1 = this.certificatePinner.hashCode();
      }

      return (((((((((var2 + 527) * 31 + var3) * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var7) * 31 + var8) * 31 + var9) * 31 + var10) * 31 + var1;
   }

   public HostnameVerifier hostnameVerifier() {
      return this.hostnameVerifier;
   }

   public List protocols() {
      return this.protocols;
   }

   public Proxy proxy() {
      return this.proxy;
   }

   public Authenticator proxyAuthenticator() {
      return this.proxyAuthenticator;
   }

   public ProxySelector proxySelector() {
      return this.proxySelector;
   }

   public SocketFactory socketFactory() {
      return this.socketFactory;
   }

   public SSLSocketFactory sslSocketFactory() {
      return this.sslSocketFactory;
   }

   public String toString() {
      StringBuilder var1 = (new StringBuilder()).append("Address{").append(this.url.host()).append(":").append(this.url.port());
      if (this.proxy != null) {
         var1.append(", proxy=").append(this.proxy);
      } else {
         var1.append(", proxySelector=").append(this.proxySelector);
      }

      var1.append("}");
      return var1.toString();
   }

   public HttpUrl url() {
      return this.url;
   }
}
