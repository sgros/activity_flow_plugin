// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.tls;

import java.util.Iterator;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayDeque;
import java.security.cert.Certificate;
import java.util.List;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

public final class BasicCertificateChainCleaner extends CertificateChainCleaner
{
    private static final int MAX_SIGNERS = 9;
    private final TrustRootIndex trustRootIndex;
    
    public BasicCertificateChainCleaner(final TrustRootIndex trustRootIndex) {
        this.trustRootIndex = trustRootIndex;
    }
    
    private boolean verifySignature(final X509Certificate x509Certificate, final X509Certificate x509Certificate2) {
        boolean b = false;
        if (x509Certificate.getIssuerDN().equals(x509Certificate2.getSubjectDN())) {
            try {
                x509Certificate.verify(x509Certificate2.getPublicKey());
                b = true;
            }
            catch (GeneralSecurityException ex) {}
        }
        return b;
    }
    
    @Override
    public List<Certificate> clean(final List<Certificate> c, final String s) throws SSLPeerUnverifiedException {
        final ArrayDeque<Certificate> arrayDeque = new ArrayDeque<Certificate>(c);
        final ArrayList obj = new ArrayList<Object>();
        obj.add(arrayDeque.removeFirst());
        boolean b = false;
    Label_0121:
        for (int i = 0; i < 9; ++i) {
            final X509Certificate obj2 = obj.get(obj.size() - 1);
            final X509Certificate byIssuerAndSignature = this.trustRootIndex.findByIssuerAndSignature(obj2);
            if (byIssuerAndSignature != null) {
                if (obj.size() > 1 || !obj2.equals(byIssuerAndSignature)) {
                    obj.add(byIssuerAndSignature);
                }
                if (this.verifySignature(byIssuerAndSignature, byIssuerAndSignature)) {
                    return (List<Certificate>)obj;
                }
                b = true;
            }
            else {
                final Iterator<X509Certificate> iterator = arrayDeque.iterator();
                while (iterator.hasNext()) {
                    final X509Certificate x509Certificate = iterator.next();
                    if (this.verifySignature(obj2, x509Certificate)) {
                        iterator.remove();
                        obj.add(x509Certificate);
                        continue Label_0121;
                    }
                }
                if (!b) {
                    throw new SSLPeerUnverifiedException("Failed to find a trusted cert that signed " + obj2);
                }
                return (List<Certificate>)obj;
            }
        }
        throw new SSLPeerUnverifiedException("Certificate chain too long: " + obj);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (o != this && (!(o instanceof BasicCertificateChainCleaner) || !((BasicCertificateChainCleaner)o).trustRootIndex.equals(this.trustRootIndex))) {
            b = false;
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        return this.trustRootIndex.hashCode();
    }
}
