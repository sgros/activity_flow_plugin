// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

import javax.security.auth.x500.X500Principal;
import java.security.cert.X509Certificate;
import java.security.Principal;
import java.util.Collections;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.Util;
import javax.net.ssl.SSLSession;
import java.security.cert.Certificate;
import java.util.List;

public final class Handshake
{
    private final CipherSuite cipherSuite;
    private final List<Certificate> localCertificates;
    private final List<Certificate> peerCertificates;
    private final TlsVersion tlsVersion;
    
    private Handshake(final TlsVersion tlsVersion, final CipherSuite cipherSuite, final List<Certificate> peerCertificates, final List<Certificate> localCertificates) {
        this.tlsVersion = tlsVersion;
        this.cipherSuite = cipherSuite;
        this.peerCertificates = peerCertificates;
        this.localCertificates = localCertificates;
    }
    
    public static Handshake get(SSLSession sslSession) {
        final String cipherSuite = sslSession.getCipherSuite();
        if (cipherSuite == null) {
            throw new IllegalStateException("cipherSuite == null");
        }
        final CipherSuite forJavaName = CipherSuite.forJavaName(cipherSuite);
        final String protocol = sslSession.getProtocol();
        if (protocol == null) {
            throw new IllegalStateException("tlsVersion == null");
        }
        TlsVersion forJavaName2;
        Certificate[] peerCertificates;
        List<Certificate> list;
        Label_0068_Outer:Label_0084_Outer:
        while (true) {
            forJavaName2 = TlsVersion.forJavaName(protocol);
            while (true) {
            Label_0109:
                while (true) {
                    while (true) {
                        try {
                            peerCertificates = sslSession.getPeerCertificates();
                            if (peerCertificates != null) {
                                list = Util.immutableList(peerCertificates);
                                sslSession = (SSLSession)(Object)sslSession.getLocalCertificates();
                                if (sslSession != null) {
                                    sslSession = (SSLSession)Util.immutableList((Object[])(Object)sslSession);
                                    return new Handshake(forJavaName2, forJavaName, list, (List<Certificate>)sslSession);
                                }
                                break Label_0109;
                            }
                        }
                        catch (SSLPeerUnverifiedException ex) {
                            peerCertificates = null;
                            continue Label_0068_Outer;
                        }
                        break;
                    }
                    list = Collections.emptyList();
                    continue Label_0084_Outer;
                }
                sslSession = (SSLSession)Collections.emptyList();
                continue;
            }
        }
    }
    
    public static Handshake get(final TlsVersion tlsVersion, final CipherSuite cipherSuite, final List<Certificate> list, final List<Certificate> list2) {
        if (cipherSuite == null) {
            throw new NullPointerException("cipherSuite == null");
        }
        return new Handshake(tlsVersion, cipherSuite, Util.immutableList(list), Util.immutableList(list2));
    }
    
    public CipherSuite cipherSuite() {
        return this.cipherSuite;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (!(o instanceof Handshake)) {
            b2 = b;
        }
        else {
            final Handshake handshake = (Handshake)o;
            b2 = b;
            if (Util.equal(this.cipherSuite, handshake.cipherSuite)) {
                b2 = b;
                if (this.cipherSuite.equals(handshake.cipherSuite)) {
                    b2 = b;
                    if (this.peerCertificates.equals(handshake.peerCertificates)) {
                        b2 = b;
                        if (this.localCertificates.equals(handshake.localCertificates)) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public int hashCode() {
        int hashCode;
        if (this.tlsVersion != null) {
            hashCode = this.tlsVersion.hashCode();
        }
        else {
            hashCode = 0;
        }
        return (((hashCode + 527) * 31 + this.cipherSuite.hashCode()) * 31 + this.peerCertificates.hashCode()) * 31 + this.localCertificates.hashCode();
    }
    
    public List<Certificate> localCertificates() {
        return this.localCertificates;
    }
    
    public Principal localPrincipal() {
        X500Principal subjectX500Principal;
        if (!this.localCertificates.isEmpty()) {
            subjectX500Principal = this.localCertificates.get(0).getSubjectX500Principal();
        }
        else {
            subjectX500Principal = null;
        }
        return subjectX500Principal;
    }
    
    public List<Certificate> peerCertificates() {
        return this.peerCertificates;
    }
    
    public Principal peerPrincipal() {
        X500Principal subjectX500Principal;
        if (!this.peerCertificates.isEmpty()) {
            subjectX500Principal = this.peerCertificates.get(0).getSubjectX500Principal();
        }
        else {
            subjectX500Principal = null;
        }
        return subjectX500Principal;
    }
    
    public TlsVersion tlsVersion() {
        return this.tlsVersion;
    }
}
