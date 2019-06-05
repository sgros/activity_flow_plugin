package okhttp3.internal.platform;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.tls.BasicCertificateChainCleaner;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;
import okio.Buffer;

public class Platform {
   public static final int INFO = 4;
   private static final Platform PLATFORM = findPlatform();
   public static final int WARN = 5;
   private static final Logger logger = Logger.getLogger(OkHttpClient.class.getName());

   public static List alpnProtocolNames(List var0) {
      ArrayList var1 = new ArrayList(var0.size());
      int var2 = 0;

      for(int var3 = var0.size(); var2 < var3; ++var2) {
         Protocol var4 = (Protocol)var0.get(var2);
         if (var4 != Protocol.HTTP_1_0) {
            var1.add(var4.toString());
         }
      }

      return var1;
   }

   static byte[] concatLengthPrefixed(List var0) {
      Buffer var1 = new Buffer();
      int var2 = 0;

      for(int var3 = var0.size(); var2 < var3; ++var2) {
         Protocol var4 = (Protocol)var0.get(var2);
         if (var4 != Protocol.HTTP_1_0) {
            var1.writeByte(var4.toString().length());
            var1.writeUtf8(var4.toString());
         }
      }

      return var1.readByteArray();
   }

   private static Platform findPlatform() {
      Object var0 = AndroidPlatform.buildIfSupported();
      if (var0 == null) {
         var0 = Jdk9Platform.buildIfSupported();
         if (var0 == null) {
            var0 = JdkWithJettyBootPlatform.buildIfSupported();
            if (var0 == null) {
               var0 = new Platform();
            }
         }
      }

      return (Platform)var0;
   }

   public static Platform get() {
      return PLATFORM;
   }

   static Object readFieldOrNull(Object var0, Class var1, String var2) {
      Object var3 = null;
      Class var4 = var0.getClass();

      Object var13;
      while(true) {
         if (var4 == Object.class) {
            var13 = var3;
            if (!var2.equals("delegate")) {
               var0 = readFieldOrNull(var0, Object.class, "delegate");
               var13 = var3;
               if (var0 != null) {
                  var13 = readFieldOrNull(var0, var1, var2);
               }
            }
            break;
         }

         label65: {
            boolean var10001;
            Object var6;
            try {
               Field var5 = var4.getDeclaredField(var2);
               var5.setAccessible(true);
               var6 = var5.get(var0);
            } catch (NoSuchFieldException var9) {
               var10001 = false;
               break label65;
            } catch (IllegalAccessException var10) {
               var10001 = false;
               throw new AssertionError();
            }

            var13 = var3;
            if (var6 == null) {
               break;
            }

            label46: {
               try {
                  if (var1.isInstance(var6)) {
                     break label46;
                  }
               } catch (NoSuchFieldException var11) {
                  var10001 = false;
                  break label65;
               } catch (IllegalAccessException var12) {
                  var10001 = false;
                  throw new AssertionError();
               }

               var13 = var3;
               break;
            }

            try {
               var13 = var1.cast(var6);
               break;
            } catch (NoSuchFieldException var7) {
               var10001 = false;
            } catch (IllegalAccessException var8) {
               var10001 = false;
               throw new AssertionError();
            }
         }

         var4 = var4.getSuperclass();
      }

      return var13;
   }

   public void afterHandshake(SSLSocket var1) {
   }

   public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager var1) {
      return new BasicCertificateChainCleaner(TrustRootIndex.get(var1));
   }

   public void configureTlsExtensions(SSLSocket var1, String var2, List var3) {
   }

   public void connectSocket(Socket var1, InetSocketAddress var2, int var3) throws IOException {
      var1.connect(var2, var3);
   }

   public String getPrefix() {
      return "OkHttp";
   }

   public String getSelectedProtocol(SSLSocket var1) {
      return null;
   }

   public Object getStackTraceForCloseable(String var1) {
      Throwable var2;
      if (logger.isLoggable(Level.FINE)) {
         var2 = new Throwable(var1);
      } else {
         var2 = null;
      }

      return var2;
   }

   public boolean isCleartextTrafficPermitted(String var1) {
      return true;
   }

   public void log(int var1, String var2, Throwable var3) {
      Level var4;
      if (var1 == 5) {
         var4 = Level.WARNING;
      } else {
         var4 = Level.INFO;
      }

      logger.log(var4, var2, var3);
   }

   public void logCloseableLeak(String var1, Object var2) {
      String var3 = var1;
      if (var2 == null) {
         var3 = var1 + " To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);";
      }

      this.log(5, var3, (Throwable)var2);
   }

   public X509TrustManager trustManager(SSLSocketFactory param1) {
      // $FF: Couldn't be decompiled
   }
}
