package com.stripe.android.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class StripeSSLSocketFactory extends SSLSocketFactory {
    private final boolean tlsv11Supported;
    private final boolean tlsv12Supported;
    private final SSLSocketFactory under = HttpsURLConnection.getDefaultSSLSocketFactory();

    public StripeSSLSocketFactory() {
        String[] protocols;
        int i = 0;
        try {
            protocols = SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
        } catch (NoSuchAlgorithmException unused) {
            protocols = new String[0];
        }
        int length = protocols.length;
        boolean z = false;
        boolean z2 = false;
        while (i < length) {
            String str = protocols[i];
            if (str.equals("TLSv1.1")) {
                z = true;
            } else if (str.equals("TLSv1.2")) {
                z2 = true;
            }
            i++;
        }
        this.tlsv11Supported = z;
        this.tlsv12Supported = z2;
    }

    public String[] getDefaultCipherSuites() {
        return this.under.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return this.under.getSupportedCipherSuites();
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        return fixupSocket(this.under.createSocket(socket, str, i, z));
    }

    public Socket createSocket(String str, int i) throws IOException {
        return fixupSocket(this.under.createSocket(str, i));
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        return fixupSocket(this.under.createSocket(str, i, inetAddress, i2));
    }

    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return fixupSocket(this.under.createSocket(inetAddress, i));
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return fixupSocket(this.under.createSocket(inetAddress, i, inetAddress2, i2));
    }

    private Socket fixupSocket(Socket socket) {
        if (!(socket instanceof SSLSocket)) {
            return socket;
        }
        SSLSocket sSLSocket = (SSLSocket) socket;
        HashSet hashSet = new HashSet(Arrays.asList(sSLSocket.getEnabledProtocols()));
        if (this.tlsv11Supported) {
            hashSet.add("TLSv1.1");
        }
        if (this.tlsv12Supported) {
            hashSet.add("TLSv1.2");
        }
        sSLSocket.setEnabledProtocols((String[]) hashSet.toArray(new String[0]));
        return sSLSocket;
    }
}
