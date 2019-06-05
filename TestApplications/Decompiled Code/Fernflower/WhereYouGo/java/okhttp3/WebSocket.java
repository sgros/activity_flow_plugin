package okhttp3;

import okio.ByteString;

public interface WebSocket {
   void cancel();

   boolean close(int var1, String var2);

   long queueSize();

   Request request();

   boolean send(String var1);

   boolean send(ByteString var1);

   public interface Factory {
      WebSocket newWebSocket(Request var1, WebSocketListener var2);
   }
}
