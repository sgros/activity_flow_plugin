package okhttp3.internal.tls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

public abstract class TrustRootIndex {
   public static TrustRootIndex get(X509TrustManager var0) {
      TrustRootIndex.AndroidTrustRootIndex var2;
      Object var4;
      try {
         Method var1 = var0.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
         var1.setAccessible(true);
         var2 = new TrustRootIndex.AndroidTrustRootIndex(var0, var1);
      } catch (NoSuchMethodException var3) {
         var4 = get(var0.getAcceptedIssuers());
         return (TrustRootIndex)var4;
      }

      var4 = var2;
      return (TrustRootIndex)var4;
   }

   public static TrustRootIndex get(X509Certificate... var0) {
      return new TrustRootIndex.BasicTrustRootIndex(var0);
   }

   public abstract X509Certificate findByIssuerAndSignature(X509Certificate var1);

   static final class AndroidTrustRootIndex extends TrustRootIndex {
      private final Method findByIssuerAndSignatureMethod;
      private final X509TrustManager trustManager;

      AndroidTrustRootIndex(X509TrustManager var1, Method var2) {
         this.findByIssuerAndSignatureMethod = var2;
         this.trustManager = var1;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (var1 != this) {
            if (!(var1 instanceof TrustRootIndex.AndroidTrustRootIndex)) {
               var2 = false;
            } else {
               TrustRootIndex.AndroidTrustRootIndex var3 = (TrustRootIndex.AndroidTrustRootIndex)var1;
               if (!this.trustManager.equals(var3.trustManager) || !this.findByIssuerAndSignatureMethod.equals(var3.findByIssuerAndSignatureMethod)) {
                  var2 = false;
               }
            }
         }

         return var2;
      }

      public X509Certificate findByIssuerAndSignature(X509Certificate var1) {
         Object var2 = null;

         label34: {
            boolean var10001;
            TrustAnchor var3;
            try {
               var3 = (TrustAnchor)this.findByIssuerAndSignatureMethod.invoke(this.trustManager, var1);
            } catch (IllegalAccessException var6) {
               var10001 = false;
               throw new AssertionError();
            } catch (InvocationTargetException var7) {
               var10001 = false;
               break label34;
            }

            var1 = (X509Certificate)var2;
            if (var3 == null) {
               return var1;
            }

            try {
               var1 = var3.getTrustedCert();
               return var1;
            } catch (IllegalAccessException var4) {
               var10001 = false;
               throw new AssertionError();
            } catch (InvocationTargetException var5) {
               var10001 = false;
            }
         }

         var1 = (X509Certificate)var2;
         return var1;
      }

      public int hashCode() {
         return this.trustManager.hashCode() + this.findByIssuerAndSignatureMethod.hashCode() * 31;
      }
   }

   static final class BasicTrustRootIndex extends TrustRootIndex {
      private final Map subjectToCaCerts = new LinkedHashMap();

      public BasicTrustRootIndex(X509Certificate... var1) {
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            X509Certificate var4 = var1[var3];
            X500Principal var5 = var4.getSubjectX500Principal();
            Set var6 = (Set)this.subjectToCaCerts.get(var5);
            Object var7 = var6;
            if (var6 == null) {
               var7 = new LinkedHashSet(1);
               this.subjectToCaCerts.put(var5, var7);
            }

            ((Set)var7).add(var4);
         }

      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (var1 != this && (!(var1 instanceof TrustRootIndex.BasicTrustRootIndex) || !((TrustRootIndex.BasicTrustRootIndex)var1).subjectToCaCerts.equals(this.subjectToCaCerts))) {
            var2 = false;
         }

         return var2;
      }

      public X509Certificate findByIssuerAndSignature(X509Certificate var1) {
         X500Principal var2 = var1.getIssuerX500Principal();
         Set var6 = (Set)this.subjectToCaCerts.get(var2);
         if (var6 == null) {
            var1 = null;
         } else {
            Iterator var3 = var6.iterator();

            while(true) {
               if (var3.hasNext()) {
                  X509Certificate var7 = (X509Certificate)var3.next();
                  PublicKey var4 = var7.getPublicKey();

                  try {
                     var1.verify(var4);
                  } catch (Exception var5) {
                     continue;
                  }

                  var1 = var7;
                  break;
               }

               var1 = null;
               break;
            }
         }

         return var1;
      }

      public int hashCode() {
         return this.subjectToCaCerts.hashCode();
      }
   }
}
