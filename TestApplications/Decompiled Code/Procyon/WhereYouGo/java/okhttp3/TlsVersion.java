// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

public enum TlsVersion
{
    SSL_3_0("SSLv3"), 
    TLS_1_0("TLSv1"), 
    TLS_1_1("TLSv1.1"), 
    TLS_1_2("TLSv1.2"), 
    TLS_1_3("TLSv1.3");
    
    final String javaName;
    
    private TlsVersion(final String javaName) {
        this.javaName = javaName;
    }
    
    public static TlsVersion forJavaName(final String str) {
        TlsVersion tlsVersion = null;
        switch (str) {
            default: {
                throw new IllegalArgumentException("Unexpected TLS version: " + str);
            }
            case "TLSv1.3": {
                tlsVersion = TlsVersion.TLS_1_3;
                break;
            }
            case "TLSv1.2": {
                tlsVersion = TlsVersion.TLS_1_2;
                break;
            }
            case "TLSv1.1": {
                tlsVersion = TlsVersion.TLS_1_1;
                break;
            }
            case "TLSv1": {
                tlsVersion = TlsVersion.TLS_1_0;
                break;
            }
            case "SSLv3": {
                tlsVersion = TlsVersion.SSL_3_0;
                break;
            }
        }
        return tlsVersion;
    }
    
    public String javaName() {
        return this.javaName;
    }
}
