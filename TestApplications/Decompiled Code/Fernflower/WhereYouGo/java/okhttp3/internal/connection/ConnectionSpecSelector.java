package okhttp3.internal.connection;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.UnknownServiceException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSocket;
import okhttp3.ConnectionSpec;
import okhttp3.internal.Internal;

public final class ConnectionSpecSelector {
   private final List connectionSpecs;
   private boolean isFallback;
   private boolean isFallbackPossible;
   private int nextModeIndex = 0;

   public ConnectionSpecSelector(List var1) {
      this.connectionSpecs = var1;
   }

   private boolean isFallbackPossible(SSLSocket var1) {
      int var2 = this.nextModeIndex;

      boolean var3;
      while(true) {
         if (var2 >= this.connectionSpecs.size()) {
            var3 = false;
            break;
         }

         if (((ConnectionSpec)this.connectionSpecs.get(var2)).isCompatible(var1)) {
            var3 = true;
            break;
         }

         ++var2;
      }

      return var3;
   }

   public ConnectionSpec configureSecureSocket(SSLSocket var1) throws IOException {
      Object var2 = null;
      int var3 = this.nextModeIndex;
      int var4 = this.connectionSpecs.size();

      ConnectionSpec var5;
      while(true) {
         var5 = (ConnectionSpec)var2;
         if (var3 >= var4) {
            break;
         }

         var5 = (ConnectionSpec)this.connectionSpecs.get(var3);
         if (var5.isCompatible(var1)) {
            this.nextModeIndex = var3 + 1;
            break;
         }

         ++var3;
      }

      if (var5 == null) {
         throw new UnknownServiceException("Unable to find acceptable protocols. isFallback=" + this.isFallback + ", modes=" + this.connectionSpecs + ", supported protocols=" + Arrays.toString(var1.getEnabledProtocols()));
      } else {
         this.isFallbackPossible = this.isFallbackPossible(var1);
         Internal.instance.apply(var5, var1, this.isFallback);
         return var5;
      }
   }

   public boolean connectionFailed(IOException var1) {
      boolean var2 = false;
      this.isFallback = true;
      boolean var3;
      if (!this.isFallbackPossible) {
         var3 = var2;
      } else {
         var3 = var2;
         if (!(var1 instanceof ProtocolException)) {
            var3 = var2;
            if (!(var1 instanceof InterruptedIOException)) {
               if (var1 instanceof SSLHandshakeException) {
                  var3 = var2;
                  if (var1.getCause() instanceof CertificateException) {
                     return var3;
                  }
               }

               var3 = var2;
               if (!(var1 instanceof SSLPeerUnverifiedException)) {
                  if (!(var1 instanceof SSLHandshakeException)) {
                     var3 = var2;
                     if (!(var1 instanceof SSLProtocolException)) {
                        return var3;
                     }
                  }

                  var3 = true;
               }
            }
         }
      }

      return var3;
   }
}
