// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.platform;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.Certificate;
import java.lang.reflect.Method;
import javax.net.ssl.SSLSocketFactory;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import okhttp3.internal.Util;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import okhttp3.Protocol;
import java.util.List;
import javax.net.ssl.SSLSocket;
import java.security.cert.X509Certificate;
import okhttp3.internal.tls.CertificateChainCleaner;
import javax.net.ssl.X509TrustManager;
import java.net.Socket;

class AndroidPlatform extends Platform
{
    private static final int MAX_LOG_LENGTH = 4000;
    private final CloseGuard closeGuard;
    private final OptionalMethod<Socket> getAlpnSelectedProtocol;
    private final OptionalMethod<Socket> setAlpnProtocols;
    private final OptionalMethod<Socket> setHostname;
    private final OptionalMethod<Socket> setUseSessionTickets;
    private final Class<?> sslParametersClass;
    
    public AndroidPlatform(final Class<?> sslParametersClass, final OptionalMethod<Socket> setUseSessionTickets, final OptionalMethod<Socket> setHostname, final OptionalMethod<Socket> getAlpnSelectedProtocol, final OptionalMethod<Socket> setAlpnProtocols) {
        this.closeGuard = CloseGuard.get();
        this.sslParametersClass = sslParametersClass;
        this.setUseSessionTickets = setUseSessionTickets;
        this.setHostname = setHostname;
        this.getAlpnSelectedProtocol = getAlpnSelectedProtocol;
        this.setAlpnProtocols = setAlpnProtocols;
    }
    
    public static Platform buildIfSupported() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //     5: astore_0       
        //     6: new             Lokhttp3/internal/platform/OptionalMethod;
        //     9: astore_1       
        //    10: aload_1        
        //    11: aconst_null    
        //    12: ldc             "setUseSessionTickets"
        //    14: iconst_1       
        //    15: anewarray       Ljava/lang/Class;
        //    18: dup            
        //    19: iconst_0       
        //    20: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
        //    23: aastore        
        //    24: invokespecial   okhttp3/internal/platform/OptionalMethod.<init>:(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
        //    27: new             Lokhttp3/internal/platform/OptionalMethod;
        //    30: astore_2       
        //    31: aload_2        
        //    32: aconst_null    
        //    33: ldc             "setHostname"
        //    35: iconst_1       
        //    36: anewarray       Ljava/lang/Class;
        //    39: dup            
        //    40: iconst_0       
        //    41: ldc             Ljava/lang/String;.class
        //    43: aastore        
        //    44: invokespecial   okhttp3/internal/platform/OptionalMethod.<init>:(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
        //    47: aconst_null    
        //    48: astore_3       
        //    49: aconst_null    
        //    50: astore          4
        //    52: ldc             "android.net.Network"
        //    54: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //    57: pop            
        //    58: new             Lokhttp3/internal/platform/OptionalMethod;
        //    61: astore          5
        //    63: aload           5
        //    65: ldc             [B.class
        //    67: ldc             "getAlpnSelectedProtocol"
        //    69: iconst_0       
        //    70: anewarray       Ljava/lang/Class;
        //    73: invokespecial   okhttp3/internal/platform/OptionalMethod.<init>:(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
        //    76: new             Lokhttp3/internal/platform/OptionalMethod;
        //    79: astore_3       
        //    80: aload_3        
        //    81: aconst_null    
        //    82: ldc             "setAlpnProtocols"
        //    84: iconst_1       
        //    85: anewarray       Ljava/lang/Class;
        //    88: dup            
        //    89: iconst_0       
        //    90: ldc             [B.class
        //    92: aastore        
        //    93: invokespecial   okhttp3/internal/platform/OptionalMethod.<init>:(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
        //    96: aload_3        
        //    97: astore          4
        //    99: new             Lokhttp3/internal/platform/AndroidPlatform;
        //   102: astore_3       
        //   103: aload_3        
        //   104: aload_0        
        //   105: aload_1        
        //   106: aload_2        
        //   107: aload           5
        //   109: aload           4
        //   111: invokespecial   okhttp3/internal/platform/AndroidPlatform.<init>:(Ljava/lang/Class;Lokhttp3/internal/platform/OptionalMethod;Lokhttp3/internal/platform/OptionalMethod;Lokhttp3/internal/platform/OptionalMethod;Lokhttp3/internal/platform/OptionalMethod;)V
        //   114: aload_3        
        //   115: astore          5
        //   117: aload           5
        //   119: areturn        
        //   120: astore          5
        //   122: ldc             "org.apache.harmony.xnet.provider.jsse.SSLParametersImpl"
        //   124: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   127: astore_0       
        //   128: goto            6
        //   131: astore          5
        //   133: aconst_null    
        //   134: astore          5
        //   136: goto            117
        //   139: astore          5
        //   141: aload_3        
        //   142: astore          5
        //   144: goto            99
        //   147: astore_3       
        //   148: goto            99
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  0      6      120    131    Ljava/lang/ClassNotFoundException;
        //  6      47     131    139    Ljava/lang/ClassNotFoundException;
        //  52     76     139    147    Ljava/lang/ClassNotFoundException;
        //  76     96     147    151    Ljava/lang/ClassNotFoundException;
        //  99     114    131    139    Ljava/lang/ClassNotFoundException;
        //  122    128    131    139    Ljava/lang/ClassNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 84 out-of-bounds for length 84
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager buildCertificateChainCleaner) {
        try {
            final Class<?> forName = Class.forName("android.net.http.X509TrustManagerExtensions");
            buildCertificateChainCleaner = new AndroidCertificateChainCleaner(forName.getConstructor(X509TrustManager.class).newInstance(buildCertificateChainCleaner), forName.getMethod("checkServerTrusted", X509Certificate[].class, String.class, String.class));
            return (CertificateChainCleaner)buildCertificateChainCleaner;
        }
        catch (Exception ex) {
            buildCertificateChainCleaner = super.buildCertificateChainCleaner((X509TrustManager)buildCertificateChainCleaner);
            return (CertificateChainCleaner)buildCertificateChainCleaner;
        }
    }
    
    @Override
    public void configureTlsExtensions(final SSLSocket sslSocket, final String s, final List<Protocol> list) {
        if (s != null) {
            this.setUseSessionTickets.invokeOptionalWithoutCheckedException(sslSocket, true);
            this.setHostname.invokeOptionalWithoutCheckedException(sslSocket, s);
        }
        if (this.setAlpnProtocols != null && this.setAlpnProtocols.isSupported(sslSocket)) {
            this.setAlpnProtocols.invokeWithoutCheckedException(sslSocket, Platform.concatLengthPrefixed(list));
        }
    }
    
    @Override
    public void connectSocket(final Socket socket, final InetSocketAddress endpoint, final int timeout) throws IOException {
        try {
            socket.connect(endpoint, timeout);
        }
        catch (AssertionError cause) {
            if (Util.isAndroidGetsocknameError(cause)) {
                throw new IOException(cause);
            }
            throw cause;
        }
        catch (SecurityException cause2) {
            final IOException ex = new IOException("Exception in connect");
            ex.initCause(cause2);
            throw ex;
        }
    }
    
    @Override
    public String getSelectedProtocol(final SSLSocket sslSocket) {
        String s = null;
        if (this.getAlpnSelectedProtocol != null && this.getAlpnSelectedProtocol.isSupported(sslSocket)) {
            final byte[] bytes = (byte[])this.getAlpnSelectedProtocol.invokeWithoutCheckedException(sslSocket, new Object[0]);
            String s2;
            if (bytes != null) {
                s2 = new String(bytes, Util.UTF_8);
            }
            else {
                s2 = null;
            }
            s = s2;
        }
        return s;
    }
    
    @Override
    public Object getStackTraceForCloseable(final String s) {
        return this.closeGuard.createAndOpen(s);
    }
    
    @Override
    public boolean isCleartextTrafficPermitted(final String s) {
        try {
            final Class<?> forName = Class.forName("android.security.NetworkSecurityPolicy");
            return (boolean)forName.getMethod("isCleartextTrafficPermitted", String.class).invoke(forName.getMethod("getInstance", (Class<?>[])new Class[0]).invoke(null, new Object[0]), s);
        }
        catch (ClassNotFoundException ex) {}
        catch (IllegalAccessException ex2) {}
        catch (IllegalArgumentException ex3) {
            goto Label_0075;
        }
        catch (InvocationTargetException ex4) {
            goto Label_0075;
        }
        catch (NoSuchMethodException ex5) {
            goto Label_0064;
        }
    }
    
    @Override
    public void log(int i, final String str, final Throwable t) {
        int n = 5;
        if (i != 5) {
            n = 3;
        }
        String string = str;
        if (t != null) {
            string = str + '\n' + Log.getStackTraceString(t);
        }
        i = 0;
        int min;
        for (int length = string.length(); i < length; i = min + 1) {
            int index = string.indexOf(10, i);
            if (index == -1) {
                index = length;
            }
            do {
                min = Math.min(index, i + 4000);
                Log.println(n, "OkHttp", string.substring(i, min));
            } while ((i = min) < index);
        }
    }
    
    @Override
    public void logCloseableLeak(final String s, final Object o) {
        if (!this.closeGuard.warnIfOpen(o)) {
            this.log(5, s, null);
        }
    }
    
    @Override
    public X509TrustManager trustManager(SSLSocketFactory trustManager) {
        Label_0040: {
            Object o;
            if ((o = Platform.readFieldOrNull(trustManager, this.sslParametersClass, "sslParameters")) != null) {
                break Label_0040;
            }
            while (true) {
                try {
                    o = Platform.readFieldOrNull(trustManager, Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, trustManager.getClass().getClassLoader()), "sslParameters");
                    trustManager = Platform.readFieldOrNull(o, X509TrustManager.class, "x509TrustManager");
                    if (trustManager != null) {
                        return (X509TrustManager)trustManager;
                    }
                }
                catch (ClassNotFoundException ex) {
                    trustManager = super.trustManager((SSLSocketFactory)trustManager);
                    return (X509TrustManager)trustManager;
                }
                trustManager = Platform.readFieldOrNull(o, X509TrustManager.class, "trustManager");
                return (X509TrustManager)trustManager;
            }
        }
    }
    
    static final class AndroidCertificateChainCleaner extends CertificateChainCleaner
    {
        private final Method checkServerTrusted;
        private final Object x509TrustManagerExtensions;
        
        AndroidCertificateChainCleaner(final Object x509TrustManagerExtensions, final Method checkServerTrusted) {
            this.x509TrustManagerExtensions = x509TrustManagerExtensions;
            this.checkServerTrusted = checkServerTrusted;
        }
        
        @Override
        public List<Certificate> clean(final List<Certificate> list, final String s) throws SSLPeerUnverifiedException {
            try {
                return (List<Certificate>)this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, list.toArray(new X509Certificate[list.size()]), "RSA", s);
            }
            catch (InvocationTargetException cause) {
                final SSLPeerUnverifiedException ex = new SSLPeerUnverifiedException(cause.getMessage());
                ex.initCause(cause);
                throw ex;
            }
            catch (IllegalAccessException detailMessage) {
                throw new AssertionError((Object)detailMessage);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof AndroidCertificateChainCleaner;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
    }
    
    static final class CloseGuard
    {
        private final Method getMethod;
        private final Method openMethod;
        private final Method warnIfOpenMethod;
        
        CloseGuard(final Method getMethod, final Method openMethod, final Method warnIfOpenMethod) {
            this.getMethod = getMethod;
            this.openMethod = openMethod;
            this.warnIfOpenMethod = warnIfOpenMethod;
        }
        
        static CloseGuard get() {
            try {
                final Class<?> forName = Class.forName("dalvik.system.CloseGuard");
                final Method method = forName.getMethod("get", (Class<?>[])new Class[0]);
                final Method method2 = forName.getMethod("open", String.class);
                final Method method3 = forName.getMethod("warnIfOpen", (Class<?>[])new Class[0]);
                return new CloseGuard(method, method2, method3);
            }
            catch (Exception ex) {
                final Method method = null;
                final Method method2 = null;
                final Method method3 = null;
                return new CloseGuard(method, method2, method3);
            }
        }
        
        Object createAndOpen(final String s) {
            if (this.getMethod == null) {
                return null;
            }
            try {
                final Object invoke = this.getMethod.invoke(null, new Object[0]);
                this.openMethod.invoke(invoke, s);
                return invoke;
            }
            catch (Exception ex) {}
            return null;
        }
        
        boolean warnIfOpen(final Object obj) {
            boolean b = false;
            if (obj == null) {
                return b;
            }
            try {
                this.warnIfOpenMethod.invoke(obj, new Object[0]);
                b = true;
                return b;
            }
            catch (Exception ex) {
                b = b;
                return b;
            }
        }
    }
}
