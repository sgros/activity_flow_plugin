// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

import java.io.IOException;

public enum Protocol
{
    HTTP_1_0("http/1.0"), 
    HTTP_1_1("http/1.1"), 
    HTTP_2("h2"), 
    SPDY_3("spdy/3.1");
    
    private final String protocol;
    
    private Protocol(final String protocol) {
        this.protocol = protocol;
    }
    
    public static Protocol get(final String str) throws IOException {
        Protocol protocol;
        if (str.equals(Protocol.HTTP_1_0.protocol)) {
            protocol = Protocol.HTTP_1_0;
        }
        else if (str.equals(Protocol.HTTP_1_1.protocol)) {
            protocol = Protocol.HTTP_1_1;
        }
        else if (str.equals(Protocol.HTTP_2.protocol)) {
            protocol = Protocol.HTTP_2;
        }
        else {
            if (!str.equals(Protocol.SPDY_3.protocol)) {
                throw new IOException("Unexpected protocol: " + str);
            }
            protocol = Protocol.SPDY_3;
        }
        return protocol;
    }
    
    @Override
    public String toString() {
        return this.protocol;
    }
}
