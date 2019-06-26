// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.tls;

import java.security.PublicKey;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;
import java.security.cert.TrustAnchor;
import java.lang.reflect.Method;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public abstract class TrustRootIndex
{
    public static TrustRootIndex get(X509TrustManager value) {
        try {
            final Method declaredMethod = value.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
            declaredMethod.setAccessible(true);
            value = new AndroidTrustRootIndex((X509TrustManager)value, declaredMethod);
            return (TrustRootIndex)value;
        }
        catch (NoSuchMethodException ex) {
            value = get(((X509TrustManager)value).getAcceptedIssuers());
            return (TrustRootIndex)value;
        }
    }
    
    public static TrustRootIndex get(final X509Certificate... array) {
        return new BasicTrustRootIndex(array);
    }
    
    public abstract X509Certificate findByIssuerAndSignature(final X509Certificate p0);
    
    static final class AndroidTrustRootIndex extends TrustRootIndex
    {
        private final Method findByIssuerAndSignatureMethod;
        private final X509TrustManager trustManager;
        
        AndroidTrustRootIndex(final X509TrustManager trustManager, final Method findByIssuerAndSignatureMethod) {
            this.findByIssuerAndSignatureMethod = findByIssuerAndSignatureMethod;
            this.trustManager = trustManager;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (o != this) {
                if (!(o instanceof AndroidTrustRootIndex)) {
                    b = false;
                }
                else {
                    final AndroidTrustRootIndex androidTrustRootIndex = (AndroidTrustRootIndex)o;
                    if (!this.trustManager.equals(androidTrustRootIndex.trustManager) || !this.findByIssuerAndSignatureMethod.equals(androidTrustRootIndex.findByIssuerAndSignatureMethod)) {
                        b = false;
                    }
                }
            }
            return b;
        }
        
        @Override
        public X509Certificate findByIssuerAndSignature(X509Certificate trustedCert) {
            final X509Certificate x509Certificate = null;
            try {
                final TrustAnchor trustAnchor = (TrustAnchor)this.findByIssuerAndSignatureMethod.invoke(this.trustManager, trustedCert);
                trustedCert = x509Certificate;
                if (trustAnchor != null) {
                    trustedCert = trustAnchor.getTrustedCert();
                }
                return trustedCert;
            }
            catch (IllegalAccessException ex) {
                throw new AssertionError();
            }
            catch (InvocationTargetException ex2) {
                trustedCert = x509Certificate;
                return trustedCert;
            }
        }
        
        @Override
        public int hashCode() {
            return this.trustManager.hashCode() + this.findByIssuerAndSignatureMethod.hashCode() * 31;
        }
    }
    
    static final class BasicTrustRootIndex extends TrustRootIndex
    {
        private final Map<X500Principal, Set<X509Certificate>> subjectToCaCerts;
        
        public BasicTrustRootIndex(final X509Certificate... array) {
            this.subjectToCaCerts = new LinkedHashMap<X500Principal, Set<X509Certificate>>();
            for (final X509Certificate x509Certificate : array) {
                final X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
                Set<X509Certificate> set;
                if ((set = this.subjectToCaCerts.get(subjectX500Principal)) == null) {
                    set = new LinkedHashSet<X509Certificate>(1);
                    this.subjectToCaCerts.put(subjectX500Principal, set);
                }
                set.add(x509Certificate);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (o != this && (!(o instanceof BasicTrustRootIndex) || !((BasicTrustRootIndex)o).subjectToCaCerts.equals(this.subjectToCaCerts))) {
                b = false;
            }
            return b;
        }
        
        @Override
        public X509Certificate findByIssuerAndSignature(X509Certificate x509Certificate) {
            final Set<X509Certificate> set = this.subjectToCaCerts.get(x509Certificate.getIssuerX500Principal());
            if (set == null) {
                x509Certificate = null;
            }
            else {
                for (final X509Certificate x509Certificate2 : set) {
                    final PublicKey publicKey = x509Certificate2.getPublicKey();
                    try {
                        x509Certificate.verify(publicKey);
                        x509Certificate = x509Certificate2;
                        return x509Certificate;
                    }
                    catch (Exception ex) {
                        continue;
                    }
                    break;
                }
                x509Certificate = null;
            }
            return x509Certificate;
        }
        
        @Override
        public int hashCode() {
            return this.subjectToCaCerts.hashCode();
        }
    }
}
