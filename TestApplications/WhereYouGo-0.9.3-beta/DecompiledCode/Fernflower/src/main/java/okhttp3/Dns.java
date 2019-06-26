package okhttp3;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public interface Dns {
   Dns SYSTEM = new Dns() {
      public List lookup(String var1) throws UnknownHostException {
         if (var1 == null) {
            throw new UnknownHostException("hostname == null");
         } else {
            return Arrays.asList(InetAddress.getAllByName(var1));
         }
      }
   };

   List lookup(String var1) throws UnknownHostException;
}
