// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.platform;

import okhttp3.internal.Util;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import okhttp3.Protocol;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import javax.net.ssl.SSLSocket;
import java.lang.reflect.Method;

class JdkWithJettyBootPlatform extends Platform
{
    private final Class<?> clientProviderClass;
    private final Method getMethod;
    private final Method putMethod;
    private final Method removeMethod;
    private final Class<?> serverProviderClass;
    
    public JdkWithJettyBootPlatform(final Method putMethod, final Method getMethod, final Method removeMethod, final Class<?> clientProviderClass, final Class<?> serverProviderClass) {
        this.putMethod = putMethod;
        this.getMethod = getMethod;
        this.removeMethod = removeMethod;
        this.clientProviderClass = clientProviderClass;
        this.serverProviderClass = serverProviderClass;
    }
    
    public static Platform buildIfSupported() {
        try {
            final Class<?> forName = Class.forName("org.eclipse.jetty.alpn.ALPN");
            return new JdkWithJettyBootPlatform(forName.getMethod("put", SSLSocket.class, Class.forName("org.eclipse.jetty.alpn.ALPN" + "$Provider")), forName.getMethod("get", SSLSocket.class), forName.getMethod("remove", SSLSocket.class), Class.forName("org.eclipse.jetty.alpn.ALPN" + "$ClientProvider"), Class.forName("org.eclipse.jetty.alpn.ALPN" + "$ServerProvider"));
        }
        catch (ClassNotFoundException ex) {}
        catch (NoSuchMethodException ex2) {
            goto Label_0156;
        }
    }
    
    @Override
    public void afterHandshake(final SSLSocket sslSocket) {
        try {
            this.removeMethod.invoke(null, sslSocket);
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex2) {
            goto Label_0019;
        }
    }
    
    @Override
    public void configureTlsExtensions(final SSLSocket ex, final String s, final List<Protocol> list) {
        final List<String> alpnProtocolNames = Platform.alpnProtocolNames(list);
        try {
            this.putMethod.invoke(null, ex, Proxy.newProxyInstance(Platform.class.getClassLoader(), new Class[] { this.clientProviderClass, this.serverProviderClass }, new JettyNegoProvider(alpnProtocolNames)));
        }
        catch (IllegalAccessException ex2) {}
        catch (InvocationTargetException ex) {
            goto Label_0077;
        }
    }
    
    @Override
    public String getSelectedProtocol(final SSLSocket sslSocket) {
        final String s = null;
        try {
            final JettyNegoProvider jettyNegoProvider = (JettyNegoProvider)Proxy.getInvocationHandler(this.getMethod.invoke(null, sslSocket));
            String selected;
            if (!jettyNegoProvider.unsupported && jettyNegoProvider.selected == null) {
                Platform.get().log(4, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", null);
                selected = s;
            }
            else {
                selected = s;
                if (!jettyNegoProvider.unsupported) {
                    selected = jettyNegoProvider.selected;
                }
            }
            return selected;
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex2) {
            goto Label_0071;
        }
    }
    
    private static class JettyNegoProvider implements InvocationHandler
    {
        private final List<String> protocols;
        String selected;
        boolean unsupported;
        
        public JettyNegoProvider(final List<String> protocols) {
            this.protocols = protocols;
        }
        
        @Override
        public Object invoke(Object o, final Method method, final Object[] array) throws Throwable {
            final String name = method.getName();
            final Class<?> returnType = method.getReturnType();
            Object[] empty_STRING_ARRAY = array;
            if (array == null) {
                empty_STRING_ARRAY = Util.EMPTY_STRING_ARRAY;
            }
            if (name.equals("supports") && Boolean.TYPE == returnType) {
                o = true;
            }
            else if (name.equals("unsupported") && Void.TYPE == returnType) {
                this.unsupported = true;
                o = null;
            }
            else if (name.equals("protocols") && empty_STRING_ARRAY.length == 0) {
                o = this.protocols;
            }
            else if ((name.equals("selectProtocol") || name.equals("select")) && String.class == returnType && empty_STRING_ARRAY.length == 1 && empty_STRING_ARRAY[0] instanceof List) {
                final List list = (List)empty_STRING_ARRAY[0];
                for (int i = 0; i < list.size(); ++i) {
                    if (this.protocols.contains(list.get(i))) {
                        o = list.get(i);
                        this.selected = (String)o;
                        return o;
                    }
                }
                o = this.protocols.get(0);
                this.selected = (String)o;
            }
            else if ((name.equals("protocolSelected") || name.equals("selected")) && empty_STRING_ARRAY.length == 1) {
                this.selected = (String)empty_STRING_ARRAY[0];
                o = null;
            }
            else {
                o = method.invoke(this, empty_STRING_ARRAY);
            }
            return o;
        }
    }
}