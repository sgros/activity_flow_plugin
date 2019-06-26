package okhttp3.internal.platform;

import android.util.Log;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;

class AndroidPlatform extends Platform {
   private static final int MAX_LOG_LENGTH = 4000;
   private final AndroidPlatform.CloseGuard closeGuard = AndroidPlatform.CloseGuard.get();
   private final OptionalMethod getAlpnSelectedProtocol;
   private final OptionalMethod setAlpnProtocols;
   private final OptionalMethod setHostname;
   private final OptionalMethod setUseSessionTickets;
   private final Class sslParametersClass;

   public AndroidPlatform(Class var1, OptionalMethod var2, OptionalMethod var3, OptionalMethod var4, OptionalMethod var5) {
      this.sslParametersClass = var1;
      this.setUseSessionTickets = var2;
      this.setHostname = var3;
      this.getAlpnSelectedProtocol = var4;
      this.setAlpnProtocols = var5;
   }

   public static Platform buildIfSupported() {
      AndroidPlatform var12;
      AndroidPlatform var13;
      label59: {
         label52: {
            Class var0;
            boolean var10001;
            try {
               var0 = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
            } catch (ClassNotFoundException var11) {
               try {
                  var0 = Class.forName("org.apache.harmony.xnet.provider.jsse.SSLParametersImpl");
               } catch (ClassNotFoundException var10) {
                  var10001 = false;
                  break label52;
               }
            }

            OptionalMethod var1;
            OptionalMethod var2;
            try {
               var1 = new OptionalMethod((Class)null, "setUseSessionTickets", new Class[]{Boolean.TYPE});
               var2 = new OptionalMethod((Class)null, "setHostname", new Class[]{String.class});
            } catch (ClassNotFoundException var9) {
               var10001 = false;
               break label52;
            }

            OptionalMethod var3 = null;
            OptionalMethod var4 = null;

            OptionalMethod var5;
            label54: {
               try {
                  Class.forName("android.net.Network");
                  var5 = new OptionalMethod(byte[].class, "getAlpnSelectedProtocol", new Class[0]);
               } catch (ClassNotFoundException var7) {
                  var5 = var3;
                  break label54;
               }

               try {
                  var3 = new OptionalMethod((Class)null, "setAlpnProtocols", new Class[]{byte[].class});
               } catch (ClassNotFoundException var6) {
                  break label54;
               }

               var4 = var3;
            }

            try {
               var12 = new AndroidPlatform(var0, var1, var2, var5, var4);
               break label59;
            } catch (ClassNotFoundException var8) {
               var10001 = false;
            }
         }

         var13 = null;
         return var13;
      }

      var13 = var12;
      return var13;
   }

   public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager var1) {
      Object var6;
      AndroidPlatform.AndroidCertificateChainCleaner var7;
      try {
         Class var2 = Class.forName("android.net.http.X509TrustManagerExtensions");
         Object var3 = var2.getConstructor(X509TrustManager.class).newInstance(var1);
         Method var4 = var2.getMethod("checkServerTrusted", X509Certificate[].class, String.class, String.class);
         var7 = new AndroidPlatform.AndroidCertificateChainCleaner(var3, var4);
      } catch (Exception var5) {
         var6 = super.buildCertificateChainCleaner(var1);
         return (CertificateChainCleaner)var6;
      }

      var6 = var7;
      return (CertificateChainCleaner)var6;
   }

   public void configureTlsExtensions(SSLSocket var1, String var2, List var3) {
      if (var2 != null) {
         this.setUseSessionTickets.invokeOptionalWithoutCheckedException(var1, true);
         this.setHostname.invokeOptionalWithoutCheckedException(var1, var2);
      }

      if (this.setAlpnProtocols != null && this.setAlpnProtocols.isSupported(var1)) {
         byte[] var4 = concatLengthPrefixed(var3);
         this.setAlpnProtocols.invokeWithoutCheckedException(var1, var4);
      }

   }

   public void connectSocket(Socket var1, InetSocketAddress var2, int var3) throws IOException {
      try {
         var1.connect(var2, var3);
      } catch (AssertionError var4) {
         if (Util.isAndroidGetsocknameError(var4)) {
            throw new IOException(var4);
         } else {
            throw var4;
         }
      } catch (SecurityException var5) {
         IOException var6 = new IOException("Exception in connect");
         var6.initCause(var5);
         throw var6;
      }
   }

   public String getSelectedProtocol(SSLSocket var1) {
      String var2 = null;
      if (this.getAlpnSelectedProtocol != null && this.getAlpnSelectedProtocol.isSupported(var1)) {
         byte[] var3 = (byte[])this.getAlpnSelectedProtocol.invokeWithoutCheckedException(var1);
         String var4;
         if (var3 != null) {
            var4 = new String(var3, Util.UTF_8);
         } else {
            var4 = null;
         }

         var2 = var4;
      }

      return var2;
   }

   public Object getStackTraceForCloseable(String var1) {
      return this.closeGuard.createAndOpen(var1);
   }

   public boolean isCleartextTrafficPermitted(String var1) {
      boolean var4;
      try {
         Class var2 = Class.forName("android.security.NetworkSecurityPolicy");
         Object var3 = var2.getMethod("getInstance").invoke((Object)null);
         var4 = (Boolean)var2.getMethod("isCleartextTrafficPermitted", String.class).invoke(var3, var1);
         return var4;
      } catch (ClassNotFoundException var5) {
      } catch (NoSuchMethodException var6) {
      } catch (IllegalAccessException var7) {
         throw new AssertionError();
      } catch (IllegalArgumentException var8) {
         throw new AssertionError();
      } catch (InvocationTargetException var9) {
         throw new AssertionError();
      }

      var4 = super.isCleartextTrafficPermitted(var1);
      return var4;
   }

   public void log(int var1, String var2, Throwable var3) {
      byte var4 = 5;
      if (var1 != 5) {
         var4 = 3;
      }

      String var5 = var2;
      if (var3 != null) {
         var5 = var2 + '\n' + Log.getStackTraceString(var3);
      }

      var1 = 0;

      int var8;
      for(int var6 = var5.length(); var1 < var6; var1 = var8 + 1) {
         int var7 = var5.indexOf(10, var1);
         if (var7 == -1) {
            var7 = var6;
         }

         do {
            var8 = Math.min(var7, var1 + 4000);
            Log.println(var4, "OkHttp", var5.substring(var1, var8));
            var1 = var8;
         } while(var8 < var7);
      }

   }

   public void logCloseableLeak(String var1, Object var2) {
      if (!this.closeGuard.warnIfOpen(var2)) {
         this.log(5, var1, (Throwable)null);
      }

   }

   public X509TrustManager trustManager(SSLSocketFactory var1) {
      Object var2 = readFieldOrNull(var1, this.sslParametersClass, "sslParameters");
      Object var3 = var2;
      X509TrustManager var5;
      if (var2 == null) {
         try {
            var3 = readFieldOrNull(var1, Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, var1.getClass().getClassLoader()), "sslParameters");
         } catch (ClassNotFoundException var4) {
            var5 = super.trustManager(var1);
            return var5;
         }
      }

      var5 = (X509TrustManager)readFieldOrNull(var3, X509TrustManager.class, "x509TrustManager");
      if (var5 == null) {
         var5 = (X509TrustManager)readFieldOrNull(var3, X509TrustManager.class, "trustManager");
      }

      return var5;
   }

   static final class AndroidCertificateChainCleaner extends CertificateChainCleaner {
      private final Method checkServerTrusted;
      private final Object x509TrustManagerExtensions;

      AndroidCertificateChainCleaner(Object var1, Method var2) {
         this.x509TrustManagerExtensions = var1;
         this.checkServerTrusted = var2;
      }

      public List clean(List var1, String var2) throws SSLPeerUnverifiedException {
         try {
            X509Certificate[] var6 = (X509Certificate[])var1.toArray(new X509Certificate[var1.size()]);
            var1 = (List)this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, var6, "RSA", var2);
            return var1;
         } catch (InvocationTargetException var3) {
            SSLPeerUnverifiedException var5 = new SSLPeerUnverifiedException(var3.getMessage());
            var5.initCause(var3);
            throw var5;
         } catch (IllegalAccessException var4) {
            throw new AssertionError(var4);
         }
      }

      public boolean equals(Object var1) {
         return var1 instanceof AndroidPlatform.AndroidCertificateChainCleaner;
      }

      public int hashCode() {
         return 0;
      }
   }

   static final class CloseGuard {
      private final Method getMethod;
      private final Method openMethod;
      private final Method warnIfOpenMethod;

      CloseGuard(Method var1, Method var2, Method var3) {
         this.getMethod = var1;
         this.openMethod = var2;
         this.warnIfOpenMethod = var3;
      }

      static AndroidPlatform.CloseGuard get() {
         Method var0;
         Method var1;
         Method var2;
         try {
            Class var4 = Class.forName("dalvik.system.CloseGuard");
            var1 = var4.getMethod("get");
            var2 = var4.getMethod("open", String.class);
            var0 = var4.getMethod("warnIfOpen");
         } catch (Exception var3) {
            var1 = null;
            var2 = null;
            var0 = null;
         }

         return new AndroidPlatform.CloseGuard(var1, var2, var0);
      }

      Object createAndOpen(String var1) {
         Object var4;
         if (this.getMethod != null) {
            label23: {
               Object var2;
               try {
                  var2 = this.getMethod.invoke((Object)null);
                  this.openMethod.invoke(var2, var1);
               } catch (Exception var3) {
                  break label23;
               }

               var4 = var2;
               return var4;
            }
         }

         var4 = null;
         return var4;
      }

      boolean warnIfOpen(Object var1) {
         boolean var2 = false;
         boolean var3 = var2;
         if (var1 != null) {
            try {
               this.warnIfOpenMethod.invoke(var1);
            } catch (Exception var4) {
               var3 = var2;
               return var3;
            }

            var3 = true;
         }

         return var3;
      }
   }
}
