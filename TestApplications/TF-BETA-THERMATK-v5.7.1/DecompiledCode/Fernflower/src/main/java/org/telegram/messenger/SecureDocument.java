package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class SecureDocument extends TLObject {
   public byte[] fileHash;
   public byte[] fileSecret;
   public TLRPC.TL_inputFile inputFile;
   public String path;
   public SecureDocumentKey secureDocumentKey;
   public TLRPC.TL_secureFile secureFile;
   public int type;

   public SecureDocument(SecureDocumentKey var1, TLRPC.TL_secureFile var2, String var3, byte[] var4, byte[] var5) {
      this.secureDocumentKey = var1;
      this.secureFile = var2;
      this.path = var3;
      this.fileHash = var4;
      this.fileSecret = var5;
   }
}
