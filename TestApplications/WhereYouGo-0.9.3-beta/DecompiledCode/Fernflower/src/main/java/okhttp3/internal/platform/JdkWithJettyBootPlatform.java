package okhttp3.internal.platform;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.internal.Util;

class JdkWithJettyBootPlatform extends Platform {
   private final Class clientProviderClass;
   private final Method getMethod;
   private final Method putMethod;
   private final Method removeMethod;
   private final Class serverProviderClass;

   public JdkWithJettyBootPlatform(Method var1, Method var2, Method var3, Class var4, Class var5) {
      this.putMethod = var1;
      this.getMethod = var2;
      this.removeMethod = var3;
      this.clientProviderClass = var4;
      this.serverProviderClass = var5;
   }

   public static Platform buildIfSupported() {
      JdkWithJettyBootPlatform var0;
      try {
         Class var8 = Class.forName("org.eclipse.jetty.alpn.ALPN");
         StringBuilder var1 = new StringBuilder();
         Class var2 = Class.forName(var1.append("org.eclipse.jetty.alpn.ALPN").append("$Provider").toString());
         var1 = new StringBuilder();
         Class var9 = Class.forName(var1.append("org.eclipse.jetty.alpn.ALPN").append("$ClientProvider").toString());
         StringBuilder var3 = new StringBuilder();
         Class var11 = Class.forName(var3.append("org.eclipse.jetty.alpn.ALPN").append("$ServerProvider").toString());
         Method var10 = var8.getMethod("put", SSLSocket.class, var2);
         Method var4 = var8.getMethod("get", SSLSocket.class);
         Method var5 = var8.getMethod("remove", SSLSocket.class);
         var0 = new JdkWithJettyBootPlatform(var10, var4, var5, var9, var11);
         return var0;
      } catch (ClassNotFoundException var6) {
      } catch (NoSuchMethodException var7) {
      }

      var0 = null;
      return var0;
   }

   public void afterHandshake(SSLSocket var1) {
      try {
         this.removeMethod.invoke((Object)null, var1);
         return;
      } catch (IllegalAccessException var2) {
      } catch (InvocationTargetException var3) {
      }

      throw new AssertionError();
   }

   public void configureTlsExtensions(SSLSocket var1, String var2, List var3) {
      List var4 = alpnProtocolNames(var3);

      Object var9;
      try {
         ClassLoader var12 = Platform.class.getClassLoader();
         Class var5 = this.clientProviderClass;
         Class var6 = this.serverProviderClass;
         JdkWithJettyBootPlatform.JettyNegoProvider var10 = new JdkWithJettyBootPlatform.JettyNegoProvider(var4);
         Object var11 = Proxy.newProxyInstance(var12, new Class[]{var5, var6}, var10);
         this.putMethod.invoke((Object)null, var1, var11);
         return;
      } catch (InvocationTargetException var7) {
         var9 = var7;
      } catch (IllegalAccessException var8) {
         var9 = var8;
      }

      throw new AssertionError(var9);
   }

   public String getSelectedProtocol(SSLSocket var1) {
      Object var2 = null;

      String var8;
      label60: {
         boolean var10001;
         JdkWithJettyBootPlatform.JettyNegoProvider var3;
         try {
            var3 = (JdkWithJettyBootPlatform.JettyNegoProvider)Proxy.getInvocationHandler(this.getMethod.invoke((Object)null, var1));
            if (!var3.unsupported && var3.selected == null) {
               Platform.get().log(4, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", (Throwable)null);
               break label60;
            }
         } catch (InvocationTargetException var6) {
            var10001 = false;
            throw new AssertionError();
         } catch (IllegalAccessException var7) {
            var10001 = false;
            throw new AssertionError();
         }

         var8 = (String)var2;

         try {
            if (!var3.unsupported) {
               var8 = var3.selected;
            }

            return var8;
         } catch (InvocationTargetException var4) {
            var10001 = false;
         } catch (IllegalAccessException var5) {
            var10001 = false;
         }

         throw new AssertionError();
      }

      var8 = (String)var2;
      return var8;
   }

   private static class JettyNegoProvider implements InvocationHandler {
      private final List protocols;
      String selected;
      boolean unsupported;

      public JettyNegoProvider(List var1) {
         this.protocols = var1;
      }

      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         String var4 = var2.getName();
         Class var5 = var2.getReturnType();
         var1 = var3;
         if (var3 == null) {
            var1 = Util.EMPTY_STRING_ARRAY;
         }

         if (var4.equals("supports") && Boolean.TYPE == var5) {
            var1 = true;
         } else if (var4.equals("unsupported") && Void.TYPE == var5) {
            this.unsupported = true;
            var1 = null;
         } else if (var4.equals("protocols") && ((Object[])var1).length == 0) {
            var1 = this.protocols;
         } else if ((var4.equals("selectProtocol") || var4.equals("select")) && String.class == var5 && ((Object[])var1).length == 1 && ((Object[])var1)[0] instanceof List) {
            List var8 = (List)((Object[])var1)[0];
            int var6 = 0;
            int var7 = var8.size();

            while(true) {
               if (var6 >= var7) {
                  var1 = (String)this.protocols.get(0);
                  this.selected = (String)var1;
                  break;
               }

               if (this.protocols.contains(var8.get(var6))) {
                  var1 = (String)var8.get(var6);
                  this.selected = (String)var1;
                  break;
               }

               ++var6;
            }
         } else if ((var4.equals("protocolSelected") || var4.equals("selected")) && ((Object[])var1).length == 1) {
            this.selected = (String)((Object[])var1)[0];
            var1 = null;
         } else {
            var1 = var2.invoke(this, (Object[])var1);
         }

         return var1;
      }
   }
}
