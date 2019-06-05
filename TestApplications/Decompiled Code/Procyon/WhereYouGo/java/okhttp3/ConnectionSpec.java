// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.internal.Util;

public final class ConnectionSpec
{
    private static final CipherSuite[] APPROVED_CIPHER_SUITES;
    public static final ConnectionSpec CLEARTEXT;
    public static final ConnectionSpec COMPATIBLE_TLS;
    public static final ConnectionSpec MODERN_TLS;
    final String[] cipherSuites;
    final boolean supportsTlsExtensions;
    final boolean tls;
    final String[] tlsVersions;
    
    static {
        APPROVED_CIPHER_SUITES = new CipherSuite[] { CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA };
        MODERN_TLS = new Builder(true).cipherSuites(ConnectionSpec.APPROVED_CIPHER_SUITES).tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
        COMPATIBLE_TLS = new Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
        CLEARTEXT = new Builder(false).build();
    }
    
    ConnectionSpec(final Builder builder) {
        this.tls = builder.tls;
        this.cipherSuites = builder.cipherSuites;
        this.tlsVersions = builder.tlsVersions;
        this.supportsTlsExtensions = builder.supportsTlsExtensions;
    }
    
    private static boolean nonEmptyIntersection(final String[] array, final String[] array2) {
        boolean b2;
        final boolean b = b2 = false;
        if (array != null) {
            b2 = b;
            if (array2 != null) {
                b2 = b;
                if (array.length != 0) {
                    if (array2.length == 0) {
                        b2 = b;
                    }
                    else {
                        final int length = array.length;
                        int n = 0;
                        while (true) {
                            b2 = b;
                            if (n >= length) {
                                return b2;
                            }
                            if (Util.indexOf(array2, array[n]) != -1) {
                                break;
                            }
                            ++n;
                        }
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    private ConnectionSpec supportedSpec(final SSLSocket sslSocket, final boolean b) {
        String[] enabledCipherSuites;
        if (this.cipherSuites != null) {
            enabledCipherSuites = Util.intersect(String.class, this.cipherSuites, sslSocket.getEnabledCipherSuites());
        }
        else {
            enabledCipherSuites = sslSocket.getEnabledCipherSuites();
        }
        String[] enabledProtocols;
        if (this.tlsVersions != null) {
            enabledProtocols = Util.intersect(String.class, this.tlsVersions, sslSocket.getEnabledProtocols());
        }
        else {
            enabledProtocols = sslSocket.getEnabledProtocols();
        }
        String[] concat = enabledCipherSuites;
        if (b) {
            concat = enabledCipherSuites;
            if (Util.indexOf(sslSocket.getSupportedCipherSuites(), "TLS_FALLBACK_SCSV") != -1) {
                concat = Util.concat(enabledCipherSuites, "TLS_FALLBACK_SCSV");
            }
        }
        return new Builder(this).cipherSuites(concat).tlsVersions(enabledProtocols).build();
    }
    
    void apply(final SSLSocket sslSocket, final boolean b) {
        final ConnectionSpec supportedSpec = this.supportedSpec(sslSocket, b);
        if (supportedSpec.tlsVersions != null) {
            sslSocket.setEnabledProtocols(supportedSpec.tlsVersions);
        }
        if (supportedSpec.cipherSuites != null) {
            sslSocket.setEnabledCipherSuites(supportedSpec.cipherSuites);
        }
    }
    
    public List<CipherSuite> cipherSuites() {
        List<CipherSuite> unmodifiableList;
        if (this.cipherSuites == null) {
            unmodifiableList = null;
        }
        else {
            final ArrayList<CipherSuite> list = new ArrayList<CipherSuite>(this.cipherSuites.length);
            final String[] cipherSuites = this.cipherSuites;
            for (int length = cipherSuites.length, i = 0; i < length; ++i) {
                list.add(CipherSuite.forJavaName(cipherSuites[i]));
            }
            unmodifiableList = (List<CipherSuite>)Collections.unmodifiableList((List<?>)list);
        }
        return unmodifiableList;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (!(o instanceof ConnectionSpec)) {
            b2 = b;
        }
        else if (o == this) {
            b2 = true;
        }
        else {
            final ConnectionSpec connectionSpec = (ConnectionSpec)o;
            b2 = b;
            if (this.tls == connectionSpec.tls) {
                if (this.tls) {
                    b2 = b;
                    if (!Arrays.equals(this.cipherSuites, connectionSpec.cipherSuites)) {
                        return b2;
                    }
                    b2 = b;
                    if (!Arrays.equals(this.tlsVersions, connectionSpec.tlsVersions)) {
                        return b2;
                    }
                    b2 = b;
                    if (this.supportsTlsExtensions != connectionSpec.supportsTlsExtensions) {
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    @Override
    public int hashCode() {
        int n = 17;
        if (this.tls) {
            final int hashCode = Arrays.hashCode(this.cipherSuites);
            final int hashCode2 = Arrays.hashCode(this.tlsVersions);
            int n2;
            if (this.supportsTlsExtensions) {
                n2 = 0;
            }
            else {
                n2 = 1;
            }
            n = ((hashCode + 527) * 31 + hashCode2) * 31 + n2;
        }
        return n;
    }
    
    public boolean isCompatible(final SSLSocket sslSocket) {
        final boolean b = false;
        boolean b2;
        if (!this.tls) {
            b2 = b;
        }
        else {
            if (this.tlsVersions != null) {
                b2 = b;
                if (!nonEmptyIntersection(this.tlsVersions, sslSocket.getEnabledProtocols())) {
                    return b2;
                }
            }
            if (this.cipherSuites != null) {
                b2 = b;
                if (!nonEmptyIntersection(this.cipherSuites, sslSocket.getEnabledCipherSuites())) {
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    public boolean isTls() {
        return this.tls;
    }
    
    public boolean supportsTlsExtensions() {
        return this.supportsTlsExtensions;
    }
    
    public List<TlsVersion> tlsVersions() {
        List<TlsVersion> unmodifiableList;
        if (this.tlsVersions == null) {
            unmodifiableList = null;
        }
        else {
            final ArrayList<TlsVersion> list = new ArrayList<TlsVersion>(this.tlsVersions.length);
            final String[] tlsVersions = this.tlsVersions;
            for (int length = tlsVersions.length, i = 0; i < length; ++i) {
                list.add(TlsVersion.forJavaName(tlsVersions[i]));
            }
            unmodifiableList = (List<TlsVersion>)Collections.unmodifiableList((List<?>)list);
        }
        return unmodifiableList;
    }
    
    @Override
    public String toString() {
        String string;
        if (!this.tls) {
            string = "ConnectionSpec()";
        }
        else {
            String string2;
            if (this.cipherSuites != null) {
                string2 = this.cipherSuites().toString();
            }
            else {
                string2 = "[all enabled]";
            }
            String string3;
            if (this.tlsVersions != null) {
                string3 = this.tlsVersions().toString();
            }
            else {
                string3 = "[all enabled]";
            }
            string = "ConnectionSpec(cipherSuites=" + string2 + ", tlsVersions=" + string3 + ", supportsTlsExtensions=" + this.supportsTlsExtensions + ")";
        }
        return string;
    }
    
    public static final class Builder
    {
        String[] cipherSuites;
        boolean supportsTlsExtensions;
        boolean tls;
        String[] tlsVersions;
        
        public Builder(final ConnectionSpec connectionSpec) {
            this.tls = connectionSpec.tls;
            this.cipherSuites = connectionSpec.cipherSuites;
            this.tlsVersions = connectionSpec.tlsVersions;
            this.supportsTlsExtensions = connectionSpec.supportsTlsExtensions;
        }
        
        Builder(final boolean tls) {
            this.tls = tls;
        }
        
        public Builder allEnabledCipherSuites() {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            this.cipherSuites = null;
            return this;
        }
        
        public Builder allEnabledTlsVersions() {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            this.tlsVersions = null;
            return this;
        }
        
        public ConnectionSpec build() {
            return new ConnectionSpec(this);
        }
        
        public Builder cipherSuites(final String... array) {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            if (array.length == 0) {
                throw new IllegalArgumentException("At least one cipher suite is required");
            }
            this.cipherSuites = array.clone();
            return this;
        }
        
        public Builder cipherSuites(final CipherSuite... array) {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            final String[] array2 = new String[array.length];
            for (int i = 0; i < array.length; ++i) {
                array2[i] = array[i].javaName;
            }
            return this.cipherSuites(array2);
        }
        
        public Builder supportsTlsExtensions(final boolean supportsTlsExtensions) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS extensions for cleartext connections");
            }
            this.supportsTlsExtensions = supportsTlsExtensions;
            return this;
        }
        
        public Builder tlsVersions(final String... array) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            if (array.length == 0) {
                throw new IllegalArgumentException("At least one TLS version is required");
            }
            this.tlsVersions = array.clone();
            return this;
        }
        
        public Builder tlsVersions(final TlsVersion... array) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            final String[] array2 = new String[array.length];
            for (int i = 0; i < array.length; ++i) {
                array2[i] = array[i].javaName;
            }
            return this.tlsVersions(array2);
        }
    }
}
