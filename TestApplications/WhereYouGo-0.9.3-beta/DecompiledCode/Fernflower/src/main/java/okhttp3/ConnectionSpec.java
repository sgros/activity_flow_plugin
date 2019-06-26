package okhttp3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.internal.Util;

public final class ConnectionSpec {
   private static final CipherSuite[] APPROVED_CIPHER_SUITES;
   public static final ConnectionSpec CLEARTEXT;
   public static final ConnectionSpec COMPATIBLE_TLS;
   public static final ConnectionSpec MODERN_TLS;
   final String[] cipherSuites;
   final boolean supportsTlsExtensions;
   final boolean tls;
   final String[] tlsVersions;

   static {
      APPROVED_CIPHER_SUITES = new CipherSuite[]{CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA};
      MODERN_TLS = (new ConnectionSpec.Builder(true)).cipherSuites(APPROVED_CIPHER_SUITES).tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
      COMPATIBLE_TLS = (new ConnectionSpec.Builder(MODERN_TLS)).tlsVersions(TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
      CLEARTEXT = (new ConnectionSpec.Builder(false)).build();
   }

   ConnectionSpec(ConnectionSpec.Builder var1) {
      this.tls = var1.tls;
      this.cipherSuites = var1.cipherSuites;
      this.tlsVersions = var1.tlsVersions;
      this.supportsTlsExtensions = var1.supportsTlsExtensions;
   }

   private static boolean nonEmptyIntersection(String[] var0, String[] var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var0 != null) {
         var3 = var2;
         if (var1 != null) {
            var3 = var2;
            if (var0.length != 0) {
               if (var1.length == 0) {
                  var3 = var2;
               } else {
                  int var4 = var0.length;
                  int var5 = 0;

                  while(true) {
                     var3 = var2;
                     if (var5 >= var4) {
                        break;
                     }

                     if (Util.indexOf(var1, var0[var5]) != -1) {
                        var3 = true;
                        break;
                     }

                     ++var5;
                  }
               }
            }
         }
      }

      return var3;
   }

   private ConnectionSpec supportedSpec(SSLSocket var1, boolean var2) {
      String[] var3;
      if (this.cipherSuites != null) {
         var3 = (String[])Util.intersect(String.class, this.cipherSuites, var1.getEnabledCipherSuites());
      } else {
         var3 = var1.getEnabledCipherSuites();
      }

      String[] var4;
      if (this.tlsVersions != null) {
         var4 = (String[])Util.intersect(String.class, this.tlsVersions, var1.getEnabledProtocols());
      } else {
         var4 = var1.getEnabledProtocols();
      }

      String[] var5 = var3;
      if (var2) {
         var5 = var3;
         if (Util.indexOf(var1.getSupportedCipherSuites(), "TLS_FALLBACK_SCSV") != -1) {
            var5 = Util.concat(var3, "TLS_FALLBACK_SCSV");
         }
      }

      return (new ConnectionSpec.Builder(this)).cipherSuites(var5).tlsVersions(var4).build();
   }

   void apply(SSLSocket var1, boolean var2) {
      ConnectionSpec var3 = this.supportedSpec(var1, var2);
      if (var3.tlsVersions != null) {
         var1.setEnabledProtocols(var3.tlsVersions);
      }

      if (var3.cipherSuites != null) {
         var1.setEnabledCipherSuites(var3.cipherSuites);
      }

   }

   public List cipherSuites() {
      List var1;
      if (this.cipherSuites == null) {
         var1 = null;
      } else {
         ArrayList var5 = new ArrayList(this.cipherSuites.length);
         String[] var2 = this.cipherSuites;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            var5.add(CipherSuite.forJavaName(var2[var4]));
         }

         var1 = Collections.unmodifiableList(var5);
      }

      return var1;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof ConnectionSpec)) {
         var3 = var2;
      } else if (var1 == this) {
         var3 = true;
      } else {
         ConnectionSpec var4 = (ConnectionSpec)var1;
         var3 = var2;
         if (this.tls == var4.tls) {
            if (this.tls) {
               var3 = var2;
               if (!Arrays.equals(this.cipherSuites, var4.cipherSuites)) {
                  return var3;
               }

               var3 = var2;
               if (!Arrays.equals(this.tlsVersions, var4.tlsVersions)) {
                  return var3;
               }

               var3 = var2;
               if (this.supportsTlsExtensions != var4.supportsTlsExtensions) {
                  return var3;
               }
            }

            var3 = true;
         }
      }

      return var3;
   }

   public int hashCode() {
      int var1 = 17;
      if (this.tls) {
         int var2 = Arrays.hashCode(this.cipherSuites);
         int var3 = Arrays.hashCode(this.tlsVersions);
         byte var4;
         if (this.supportsTlsExtensions) {
            var4 = 0;
         } else {
            var4 = 1;
         }

         var1 = ((var2 + 527) * 31 + var3) * 31 + var4;
      }

      return var1;
   }

   public boolean isCompatible(SSLSocket var1) {
      boolean var2 = false;
      boolean var3;
      if (!this.tls) {
         var3 = var2;
      } else {
         if (this.tlsVersions != null) {
            var3 = var2;
            if (!nonEmptyIntersection(this.tlsVersions, var1.getEnabledProtocols())) {
               return var3;
            }
         }

         if (this.cipherSuites != null) {
            var3 = var2;
            if (!nonEmptyIntersection(this.cipherSuites, var1.getEnabledCipherSuites())) {
               return var3;
            }
         }

         var3 = true;
      }

      return var3;
   }

   public boolean isTls() {
      return this.tls;
   }

   public boolean supportsTlsExtensions() {
      return this.supportsTlsExtensions;
   }

   public List tlsVersions() {
      List var1;
      if (this.tlsVersions == null) {
         var1 = null;
      } else {
         ArrayList var2 = new ArrayList(this.tlsVersions.length);
         String[] var5 = this.tlsVersions;
         int var3 = var5.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            var2.add(TlsVersion.forJavaName(var5[var4]));
         }

         var1 = Collections.unmodifiableList(var2);
      }

      return var1;
   }

   public String toString() {
      String var1;
      if (!this.tls) {
         var1 = "ConnectionSpec()";
      } else {
         if (this.cipherSuites != null) {
            var1 = this.cipherSuites().toString();
         } else {
            var1 = "[all enabled]";
         }

         String var2;
         if (this.tlsVersions != null) {
            var2 = this.tlsVersions().toString();
         } else {
            var2 = "[all enabled]";
         }

         var1 = "ConnectionSpec(cipherSuites=" + var1 + ", tlsVersions=" + var2 + ", supportsTlsExtensions=" + this.supportsTlsExtensions + ")";
      }

      return var1;
   }

   public static final class Builder {
      String[] cipherSuites;
      boolean supportsTlsExtensions;
      boolean tls;
      String[] tlsVersions;

      public Builder(ConnectionSpec var1) {
         this.tls = var1.tls;
         this.cipherSuites = var1.cipherSuites;
         this.tlsVersions = var1.tlsVersions;
         this.supportsTlsExtensions = var1.supportsTlsExtensions;
      }

      Builder(boolean var1) {
         this.tls = var1;
      }

      public ConnectionSpec.Builder allEnabledCipherSuites() {
         if (!this.tls) {
            throw new IllegalStateException("no cipher suites for cleartext connections");
         } else {
            this.cipherSuites = null;
            return this;
         }
      }

      public ConnectionSpec.Builder allEnabledTlsVersions() {
         if (!this.tls) {
            throw new IllegalStateException("no TLS versions for cleartext connections");
         } else {
            this.tlsVersions = null;
            return this;
         }
      }

      public ConnectionSpec build() {
         return new ConnectionSpec(this);
      }

      public ConnectionSpec.Builder cipherSuites(String... var1) {
         if (!this.tls) {
            throw new IllegalStateException("no cipher suites for cleartext connections");
         } else if (var1.length == 0) {
            throw new IllegalArgumentException("At least one cipher suite is required");
         } else {
            this.cipherSuites = (String[])var1.clone();
            return this;
         }
      }

      public ConnectionSpec.Builder cipherSuites(CipherSuite... var1) {
         if (!this.tls) {
            throw new IllegalStateException("no cipher suites for cleartext connections");
         } else {
            String[] var2 = new String[var1.length];

            for(int var3 = 0; var3 < var1.length; ++var3) {
               var2[var3] = var1[var3].javaName;
            }

            return this.cipherSuites(var2);
         }
      }

      public ConnectionSpec.Builder supportsTlsExtensions(boolean var1) {
         if (!this.tls) {
            throw new IllegalStateException("no TLS extensions for cleartext connections");
         } else {
            this.supportsTlsExtensions = var1;
            return this;
         }
      }

      public ConnectionSpec.Builder tlsVersions(String... var1) {
         if (!this.tls) {
            throw new IllegalStateException("no TLS versions for cleartext connections");
         } else if (var1.length == 0) {
            throw new IllegalArgumentException("At least one TLS version is required");
         } else {
            this.tlsVersions = (String[])var1.clone();
            return this;
         }
      }

      public ConnectionSpec.Builder tlsVersions(TlsVersion... var1) {
         if (!this.tls) {
            throw new IllegalStateException("no TLS versions for cleartext connections");
         } else {
            String[] var2 = new String[var1.length];

            for(int var3 = 0; var3 < var1.length; ++var3) {
               var2[var3] = var1[var3].javaName;
            }

            return this.tlsVersions(var2);
         }
      }
   }
}
