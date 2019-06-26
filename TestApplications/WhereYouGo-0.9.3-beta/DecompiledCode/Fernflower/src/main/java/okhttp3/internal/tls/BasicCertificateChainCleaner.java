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
      ArrayDeque var3 = new ArrayDeque(var1);
      ArrayList var9 = new ArrayList();
      var9.add(var3.removeFirst());
      boolean var4 = false;

      for(int var5 = 0; var5 < 9; ++var5) {
         X509Certificate var8 = (X509Certificate)var9.get(var9.size() - 1);
         X509Certificate var6 = this.trustRootIndex.findByIssuerAndSignature(var8);
         if (var6 != null) {
            if (var9.size() > 1 || !var8.equals(var6)) {
               var9.add(var6);
            }

            if (this.verifySignature(var6, var6)) {
               return var9;
            }

            var4 = true;
         } else {
            Iterator var7 = var3.iterator();

            do {
               if (!var7.hasNext()) {
                  if (!var4) {
                     throw new SSLPeerUnverifiedException("Failed to find a trusted cert that signed " + var8);
                  }

                  return var9;
               }

               var6 = (X509Certificate)var7.next();
            } while(!this.verifySignature(var8, var6));

            var7.remove();
            var9.add(var6);
         }
      }

      throw new SSLPeerUnverifiedException("Certificate chain too long: " + var9);
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
