package okhttp3;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;
import okio.ByteString;

public final class CertificatePinner {
   public static final CertificatePinner DEFAULT = (new CertificatePinner.Builder()).build();
   private final CertificateChainCleaner certificateChainCleaner;
   private final Set pins;

   CertificatePinner(Set var1, CertificateChainCleaner var2) {
      this.pins = var1;
      this.certificateChainCleaner = var2;
   }

   public static String pin(Certificate var0) {
      if (!(var0 instanceof X509Certificate)) {
         throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
      } else {
         return "sha256/" + sha256((X509Certificate)var0).base64();
      }
   }

   static ByteString sha1(X509Certificate var0) {
      return ByteString.of(var0.getPublicKey().getEncoded()).sha1();
   }

   static ByteString sha256(X509Certificate var0) {
      return ByteString.of(var0.getPublicKey().getEncoded()).sha256();
   }

   public void check(String var1, List var2) throws SSLPeerUnverifiedException {
      List var3 = this.findMatchingPins(var1);
      if (!var3.isEmpty()) {
         List var4 = var2;
         if (this.certificateChainCleaner != null) {
            var4 = this.certificateChainCleaner.clean(var2, var1);
         }

         int var5 = 0;

         int var9;
         for(int var6 = var4.size(); var5 < var6; ++var5) {
            X509Certificate var7 = (X509Certificate)var4.get(var5);
            ByteString var8 = null;
            ByteString var14 = null;
            var9 = 0;

            for(int var10 = var3.size(); var9 < var10; ++var9) {
               CertificatePinner.Pin var11 = (CertificatePinner.Pin)var3.get(var9);
               ByteString var12;
               if (var11.hashAlgorithm.equals("sha256/")) {
                  var12 = var14;
                  if (var14 == null) {
                     var12 = sha256(var7);
                  }

                  if (var11.hash.equals(var12)) {
                     return;
                  }

                  var14 = var12;
               } else {
                  if (!var11.hashAlgorithm.equals("sha1/")) {
                     throw new AssertionError();
                  }

                  var12 = var8;
                  if (var8 == null) {
                     var12 = sha1(var7);
                  }

                  var8 = var12;
                  if (var11.hash.equals(var12)) {
                     return;
                  }
               }
            }
         }

         StringBuilder var15 = (new StringBuilder()).append("Certificate pinning failure!").append("\n  Peer certificate chain:");
         var5 = 0;

         for(var9 = var4.size(); var5 < var9; ++var5) {
            X509Certificate var16 = (X509Certificate)var4.get(var5);
            var15.append("\n    ").append(pin(var16)).append(": ").append(var16.getSubjectDN().getName());
         }

         var15.append("\n  Pinned certificates for ").append(var1).append(":");
         var5 = 0;

         for(var9 = var3.size(); var5 < var9; ++var5) {
            CertificatePinner.Pin var13 = (CertificatePinner.Pin)var3.get(var5);
            var15.append("\n    ").append(var13);
         }

         throw new SSLPeerUnverifiedException(var15.toString());
      }
   }

   public void check(String var1, Certificate... var2) throws SSLPeerUnverifiedException {
      this.check(var1, Arrays.asList(var2));
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (var1 != this) {
         if (var1 instanceof CertificatePinner && Util.equal(this.certificateChainCleaner, ((CertificatePinner)var1).certificateChainCleaner) && this.pins.equals(((CertificatePinner)var1).pins)) {
            var2 = true;
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   List findMatchingPins(String var1) {
      Object var2 = Collections.emptyList();
      Iterator var3 = this.pins.iterator();

      while(var3.hasNext()) {
         CertificatePinner.Pin var4 = (CertificatePinner.Pin)var3.next();
         if (var4.matches(var1)) {
            Object var5 = var2;
            if (((List)var2).isEmpty()) {
               var5 = new ArrayList();
            }

            ((List)var5).add(var4);
            var2 = var5;
         }
      }

      return (List)var2;
   }

   public int hashCode() {
      int var1;
      if (this.certificateChainCleaner != null) {
         var1 = this.certificateChainCleaner.hashCode();
      } else {
         var1 = 0;
      }

      return var1 * 31 + this.pins.hashCode();
   }

   CertificatePinner withCertificateChainCleaner(CertificateChainCleaner var1) {
      CertificatePinner var2;
      if (Util.equal(this.certificateChainCleaner, var1)) {
         var2 = this;
      } else {
         var2 = new CertificatePinner(this.pins, var1);
      }

      return var2;
   }

   public static final class Builder {
      private final List pins = new ArrayList();

      public CertificatePinner.Builder add(String var1, String... var2) {
         if (var1 == null) {
            throw new NullPointerException("pattern == null");
         } else {
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               String var5 = var2[var4];
               this.pins.add(new CertificatePinner.Pin(var1, var5));
            }

            return this;
         }
      }

      public CertificatePinner build() {
         return new CertificatePinner(new LinkedHashSet(this.pins), (CertificateChainCleaner)null);
      }
   }

   static final class Pin {
      private static final String WILDCARD = "*.";
      final String canonicalHostname;
      final ByteString hash;
      final String hashAlgorithm;
      final String pattern;

      Pin(String var1, String var2) {
         this.pattern = var1;
         if (var1.startsWith("*.")) {
            var1 = HttpUrl.parse("http://" + var1.substring("*.".length())).host();
         } else {
            var1 = HttpUrl.parse("http://" + var1).host();
         }

         this.canonicalHostname = var1;
         if (var2.startsWith("sha1/")) {
            this.hashAlgorithm = "sha1/";
            this.hash = ByteString.decodeBase64(var2.substring("sha1/".length()));
         } else {
            if (!var2.startsWith("sha256/")) {
               throw new IllegalArgumentException("pins must start with 'sha256/' or 'sha1/': " + var2);
            }

            this.hashAlgorithm = "sha256/";
            this.hash = ByteString.decodeBase64(var2.substring("sha256/".length()));
         }

         if (this.hash == null) {
            throw new IllegalArgumentException("pins must be base64: " + var2);
         }
      }

      public boolean equals(Object var1) {
         boolean var2;
         if (var1 instanceof CertificatePinner.Pin && this.pattern.equals(((CertificatePinner.Pin)var1).pattern) && this.hashAlgorithm.equals(((CertificatePinner.Pin)var1).hashAlgorithm) && this.hash.equals(((CertificatePinner.Pin)var1).hash)) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public int hashCode() {
         return ((this.pattern.hashCode() + 527) * 31 + this.hashAlgorithm.hashCode()) * 31 + this.hash.hashCode();
      }

      boolean matches(String var1) {
         boolean var2;
         if (this.pattern.startsWith("*.")) {
            var2 = var1.regionMatches(false, var1.indexOf(46) + 1, this.canonicalHostname, 0, this.canonicalHostname.length());
         } else {
            var2 = var1.equals(this.canonicalHostname);
         }

         return var2;
      }

      public String toString() {
         return this.hashAlgorithm + this.hash.base64();
      }
   }
}
