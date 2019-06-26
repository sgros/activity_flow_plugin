package org.telegram.messenger;

public class SecureDocumentKey {
   public byte[] file_iv;
   public byte[] file_key;

   public SecureDocumentKey(byte[] var1, byte[] var2) {
      this.file_key = var1;
      this.file_iv = var2;
   }
}
