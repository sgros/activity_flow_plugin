package org.telegram.tgnet;

public interface RequestDelegate {
   void run(TLObject var1, TLRPC.TL_error var2);
}
