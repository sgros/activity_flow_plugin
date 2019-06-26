package okhttp3;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.auth.x500.X500Principal;
import okhttp3.internal.Util;

public final class Handshake {
   private final CipherSuite cipherSuite;
   private final List localCertificates;
   private final List peerCertificates;
   private final TlsVersion tlsVersion;

   private Handshake(TlsVersion var1, CipherSuite var2, List var3, List var4) {
      this.tlsVersion = var1;
      this.cipherSuite = var2;
      this.peerCertificates = var3;
      this.localCertificates = var4;
   }

   public static Handshake get(SSLSession var0) {
      String var1 = var0.getCipherSuite();
      if (var1 == null) {
         throw new IllegalStateException("cipherSuite == null");
      } else {
         CipherSuite var2 = CipherSuite.forJavaName(var1);
         var1 = var0.getProtocol();
         if (var1 == null) {
            throw new IllegalStateException("tlsVersion == null");
         } else {
            TlsVersion var3 = TlsVersion.forJavaName(var1);

            Certificate[] var7;
            try {
               var7 = var0.getPeerCertificates();
            } catch (SSLPeerUnverifiedException var4) {
               var7 = null;
            }

            List var8;
            if (var7 != null) {
               var8 = Util.immutableList((Object[])var7);
            } else {
               var8 = Collections.emptyList();
            }

            Certificate[] var5 = var0.getLocalCertificates();
            List var6;
            if (var5 != null) {
               var6 = Util.immutableList((Object[])var5);
            } else {
               var6 = Collections.emptyList();
            }

            return new Handshake(var3, var2, var8, var6);
         }
      }
   }

   public static Handshake get(TlsVersion var0, CipherSuite var1, List var2, List var3) {
      if (var1 == null) {
         throw new NullPointerException("cipherSuite == null");
      } else {
         return new Handshake(var0, var1, Util.immutableList(var2), Util.immutableList(var3));
      }
   }

   public CipherSuite cipherSuite() {
      return this.cipherSuite;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof Handshake)) {
         var3 = var2;
      } else {
         Handshake var4 = (Handshake)var1;
         var3 = var2;
         if (Util.equal(this.cipherSuite, var4.cipherSuite)) {
            var3 = var2;
            if (this.cipherSuite.equals(var4.cipherSuite)) {
               var3 = var2;
               if (this.peerCertificates.equals(var4.peerCertificates)) {
                  var3 = var2;
                  if (this.localCertificates.equals(var4.localCertificates)) {
                     var3 = true;
                  }
               }
            }
         }
      }

      return var3;
   }

   public int hashCode() {
      int var1;
      if (this.tlsVersion != null) {
         var1 = this.tlsVersion.hashCode();
      } else {
         var1 = 0;
      }

      return (((var1 + 527) * 31 + this.cipherSuite.hashCode()) * 31 + this.peerCertificates.hashCode()) * 31 + this.localCertificates.hashCode();
   }

   public List localCertificates() {
      return this.localCertificates;
   }

   public Principal localPrincipal() {
      X500Principal var1;
      if (!this.localCertificates.isEmpty()) {
         var1 = ((X509Certificate)this.localCertificates.get(0)).getSubjectX500Principal();
      } else {
         var1 = null;
      }

      return var1;
   }

   public List peerCertificates() {
      return this.peerCertificates;
   }

   public Principal peerPrincipal() {
      X500Principal var1;
      if (!this.peerCertificates.isEmpty()) {
         var1 = ((X509Certificate)this.peerCertificates.get(0)).getSubjectX500Principal();
      } else {
         var1 = null;
      }

      return var1;
   }

   public TlsVersion tlsVersion() {
      return this.tlsVersion;
   }
}
