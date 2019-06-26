package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class Aes128DataSource implements DataSource {
   private CipherInputStream cipherInputStream;
   private final byte[] encryptionIv;
   private final byte[] encryptionKey;
   private final DataSource upstream;

   public Aes128DataSource(DataSource var1, byte[] var2, byte[] var3) {
      this.upstream = var1;
      this.encryptionKey = var2;
      this.encryptionIv = var3;
   }

   public final void addTransferListener(TransferListener var1) {
      this.upstream.addTransferListener(var1);
   }

   public void close() throws IOException {
      if (this.cipherInputStream != null) {
         this.cipherInputStream = null;
         this.upstream.close();
      }

   }

   protected Cipher getCipherInstance() throws NoSuchPaddingException, NoSuchAlgorithmException {
      return Cipher.getInstance("AES/CBC/PKCS7Padding");
   }

   public final Map getResponseHeaders() {
      return this.upstream.getResponseHeaders();
   }

   public final Uri getUri() {
      return this.upstream.getUri();
   }

   public final long open(DataSpec var1) throws IOException {
      Cipher var2;
      Object var9;
      label26: {
         try {
            var2 = this.getCipherInstance();
            break label26;
         } catch (NoSuchAlgorithmException var7) {
            var9 = var7;
         } catch (NoSuchPaddingException var8) {
            var9 = var8;
         }

         throw new RuntimeException((Throwable)var9);
      }

      SecretKeySpec var3 = new SecretKeySpec(this.encryptionKey, "AES");
      IvParameterSpec var4 = new IvParameterSpec(this.encryptionIv);

      label21: {
         try {
            var2.init(2, var3, var4);
            break label21;
         } catch (InvalidKeyException var5) {
            var9 = var5;
         } catch (InvalidAlgorithmParameterException var6) {
            var9 = var6;
         }

         throw new RuntimeException((Throwable)var9);
      }

      DataSourceInputStream var10 = new DataSourceInputStream(this.upstream, var1);
      this.cipherInputStream = new CipherInputStream(var10, var2);
      var10.open();
      return -1L;
   }

   public final int read(byte[] var1, int var2, int var3) throws IOException {
      Assertions.checkNotNull(this.cipherInputStream);
      var3 = this.cipherInputStream.read(var1, var2, var3);
      var2 = var3;
      if (var3 < 0) {
         var2 = -1;
      }

      return var2;
   }
}
