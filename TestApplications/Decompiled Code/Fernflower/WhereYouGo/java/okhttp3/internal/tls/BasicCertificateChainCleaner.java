package okhttp3.internal.tls;

import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;

public final class BasicCertificateChainCleaner extends CertificateChainCleaner {
   private static final int MAX_SIGNERS = 9;
   private final TrustRootIndex trustRootIndex;

   public BasicCertificateChainCleaner(TrustRootIndex var1) {
      this.trustRootIndex = var1;
   }

   private boolean verifySignature(X509Certificate var1, X509Certificate var2) {
      boolean var3 = false;
      if (var1.getIssuerDN().equals(var2.getSubjectDN())) {
         try {
            var1.verify(var2.getPublicKey());
         } catch (GeneralSecurityException var4) {
            return var3;
         }

         var3 = true;
      }

      return var3;
   }

   public List clean(List var1, String var2) throws SSLPeerUnverifiedException {
      ArrayDeque var9 = new ArrayDeque(var1);
      ArrayList var8 = new ArrayList();
      var8.add(var9.removeFirst());
      boolean var3 = false;

      for(int var4 = 0; var4 < 9; ++var4) {
         X509Certificate var5 = (X509Certificate)var8.get(var8.size() - 1);
         X509Certificate var6 = this.trustRootIndex.findByIssuerAndSignature(var5);
         if (var6 != null) {
            if (var8.size() > 1 || !var5.equals(var6)) {
               var8.add(var6);
            }

            if (this.verifySignature(var6, var6)) {
               return var8;
            }

            var3 = true;
         } else {
            Iterator var10 = var9.iterator();

            X509Certificate var7;
            do {
               if (!var10.hasNext()) {
                  if (!var3) {
                     throw new SSLPeerUnverifiedException("Failed to find a trusted cert that signed " + var5);
                  }

                  return var8;
               }

               var7 = (X509Certificate)var10.next();
            } while(!this.verifySignature(var5, var7));

            var10.remove();
            var8.add(var7);
         }
      }

      throw new SSLPeerUnverifiedException("Certificate chain too long: " + var8);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (var1 != this && (!(var1 instanceof BasicCertificateChainCleaner) || !((BasicCertificateChainCleaner)var1).trustRootIndex.equals(this.trustRootIndex))) {
         var2 = false;
      }

      return var2;
   }

   public int hashCode() {
      return this.trustRootIndex.hashCode();
   }
}
