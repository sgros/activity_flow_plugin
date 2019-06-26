// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.net;

import java.net.InetAddress;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import javax.net.ssl.SSLSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class StripeSSLSocketFactory extends SSLSocketFactory
{
    private final boolean tlsv11Supported;
    private final boolean tlsv12Supported;
    private final SSLSocketFactory under;
    
    public StripeSSLSocketFactory() {
        this.under = HttpsURLConnection.getDefaultSSLSocketFactory();
        int i = 0;
        String[] protocols;
        try {
            protocols = SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
        }
        catch (NoSuchAlgorithmException ex) {
            protocols = new String[0];
        }
        final int length = protocols.length;
        boolean tlsv11Supported = false;
        boolean tlsv12Supported = false;
        while (i < length) {
            final String s = protocols[i];
            boolean b;
            if (s.equals("TLSv1.1")) {
                b = true;
            }
            else {
                b = tlsv11Supported;
                if (s.equals("TLSv1.2")) {
                    tlsv12Supported = true;
                    b = tlsv11Supported;
                }
            }
            ++i;
            tlsv11Supported = b;
        }
        this.tlsv11Supported = tlsv11Supported;
        this.tlsv12Supported = tlsv12Supported;
    }
    
    private Socket fixupSocket(final Socket socket) {
        if (!(socket instanceof SSLSocket)) {
            return socket;
        }
        final SSLSocket sslSocket = (SSLSocket)socket;
        final HashSet set = new HashSet<String>(Arrays.asList(sslSocket.getEnabledProtocols()));
        if (this.tlsv11Supported) {
            set.add("TLSv1.1");
        }
        if (this.tlsv12Supported) {
            set.add("TLSv1.2");
        }
        sslSocket.setEnabledProtocols(set.toArray(new String[0]));
        return sslSocket;
    }
    
    @Override
    public Socket createSocket(final String s, final int n) throws IOException {
        return this.fixupSocket(this.under.createSocket(s, n));
    }
    
    @Override
    public Socket createSocket(final String s, final int n, final InetAddress inetAddress, final int n2) throws IOException {
        return this.fixupSocket(this.under.createSocket(s, n, inetAddress, n2));
    }
    
    @Override
    public Socket createSocket(final InetAddress inetAddress, final int n) throws IOException {
        return this.fixupSocket(this.under.createSocket(inetAddress, n));
    }
    
    @Override
    public Socket createSocket(final InetAddress inetAddress, final int n, final InetAddress inetAddress2, final int n2) throws IOException {
        return this.fixupSocket(this.under.createSocket(inetAddress, n, inetAddress2, n2));
    }
    
    @Override
    public Socket createSocket(final Socket socket, final String s, final int n, final boolean b) throws IOException {
        return this.fixupSocket(this.under.createSocket(socket, s, n, b));
    }
    
    @Override
    public String[] getDefaultCipherSuites() {
        return this.under.getDefaultCipherSuites();
    }
    
    @Override
    public String[] getSupportedCipherSuites() {
        return this.under.getSupportedCipherSuites();
    }
}
