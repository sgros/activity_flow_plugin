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
      int var1 = 0;

      String[] var2;
      try {
         var2 = SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
      } catch (NoSuchAlgorithmException var8) {
         var2 = new String[0];
      }

      int var3 = var2.length;
      boolean var4 = false;

      boolean var5;
      boolean var7;
      for(var5 = false; var1 < var3; var4 = var7) {
         String var6 = var2[var1];
         if (var6.equals("TLSv1.1")) {
            var7 = true;
         } else {
            var7 = var4;
            if (var6.equals("TLSv1.2")) {
               var5 = true;
               var7 = var4;
            }
         }

         ++var1;
      }

      this.tlsv11Supported = var4;
      this.tlsv12Supported = var5;
   }

   private Socket fixupSocket(Socket var1) {
      if (!(var1 instanceof SSLSocket)) {
         return var1;
      } else {
         SSLSocket var3 = (SSLSocket)var1;
         HashSet var2 = new HashSet(Arrays.asList(var3.getEnabledProtocols()));
         if (this.tlsv11Supported) {
            var2.add("TLSv1.1");
         }

         if (this.tlsv12Supported) {
            var2.add("TLSv1.2");
         }

         var3.setEnabledProtocols((String[])var2.toArray(new String[0]));
         return var3;
      }
   }

   public Socket createSocket(String var1, int var2) throws IOException {
      return this.fixupSocket(this.under.createSocket(var1, var2));
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) throws IOException {
      return this.fixupSocket(this.under.createSocket(var1, var2, var3, var4));
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      return this.fixupSocket(this.under.createSocket(var1, var2));
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws IOException {
      return this.fixupSocket(this.under.createSocket(var1, var2, var3, var4));
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException {
      return this.fixupSocket(this.under.createSocket(var1, var2, var3, var4));
   }

   public String[] getDefaultCipherSuites() {
      return this.under.getDefaultCipherSuites();
   }

   public String[] getSupportedCipherSuites() {
      return this.under.getSupportedCipherSuites();
   }
}
