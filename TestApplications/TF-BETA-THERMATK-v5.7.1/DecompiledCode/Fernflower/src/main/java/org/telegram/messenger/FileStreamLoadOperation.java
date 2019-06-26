package org.telegram.messenger;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.BaseDataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLRPC;

public class FileStreamLoadOperation extends BaseDataSource implements FileLoadOperationStream {
   private long bytesRemaining;
   private CountDownLatch countDownLatch;
   private int currentAccount;
   private int currentOffset;
   private TLRPC.Document document;
   private RandomAccessFile file;
   private FileLoadOperation loadOperation;
   private boolean opened;
   private Object parentObject;
   private Uri uri;

   public FileStreamLoadOperation() {
      super(false);
   }

   @Deprecated
   public FileStreamLoadOperation(TransferListener var1) {
      this();
      if (var1 != null) {
         this.addTransferListener(var1);
      }

   }

   public void close() {
      FileLoadOperation var1 = this.loadOperation;
      if (var1 != null) {
         var1.removeStreamListener(this);
      }

      CountDownLatch var3 = this.countDownLatch;
      if (var3 != null) {
         var3.countDown();
      }

      RandomAccessFile var4 = this.file;
      if (var4 != null) {
         try {
            var4.close();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

         this.file = null;
      }

      this.uri = null;
      if (this.opened) {
         this.opened = false;
         this.transferEnded();
      }

   }

   public Uri getUri() {
      return this.uri;
   }

   public void newDataAvailable() {
      CountDownLatch var1 = this.countDownLatch;
      if (var1 != null) {
         var1.countDown();
      }

   }

   public long open(DataSpec var1) throws IOException {
      this.uri = var1.uri;
      this.currentAccount = Utilities.parseInt(this.uri.getQueryParameter("account"));
      this.parentObject = FileLoader.getInstance(this.currentAccount).getParentObject(Utilities.parseInt(this.uri.getQueryParameter("rid")));
      this.document = new TLRPC.TL_document();
      this.document.access_hash = Utilities.parseLong(this.uri.getQueryParameter("hash"));
      this.document.id = Utilities.parseLong(this.uri.getQueryParameter("id"));
      this.document.size = Utilities.parseInt(this.uri.getQueryParameter("size"));
      this.document.dc_id = Utilities.parseInt(this.uri.getQueryParameter("dc"));
      this.document.mime_type = this.uri.getQueryParameter("mime");
      this.document.file_reference = Utilities.hexToBytes(this.uri.getQueryParameter("reference"));
      TLRPC.TL_documentAttributeFilename var2 = new TLRPC.TL_documentAttributeFilename();
      var2.file_name = this.uri.getQueryParameter("name");
      this.document.attributes.add(var2);
      if (this.document.mime_type.startsWith("video")) {
         this.document.attributes.add(new TLRPC.TL_documentAttributeVideo());
      } else if (this.document.mime_type.startsWith("audio")) {
         this.document.attributes.add(new TLRPC.TL_documentAttributeAudio());
      }

      FileLoader var11 = FileLoader.getInstance(this.currentAccount);
      TLRPC.Document var3 = this.document;
      Object var4 = this.parentObject;
      int var5 = (int)var1.position;
      this.currentOffset = var5;
      this.loadOperation = var11.loadStreamFile(this, var3, var4, var5);
      long var6 = var1.length;
      long var8 = var6;
      if (var6 == -1L) {
         var8 = (long)this.document.size - var1.position;
      }

      this.bytesRemaining = var8;
      if (this.bytesRemaining >= 0L) {
         this.opened = true;
         this.transferStarted(var1);
         FileLoadOperation var10 = this.loadOperation;
         if (var10 != null) {
            this.file = new RandomAccessFile(var10.getCurrentFile(), "r");
            this.file.seek((long)this.currentOffset);
         }

         return this.bytesRemaining;
      } else {
         throw new EOFException();
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      byte var4 = 0;
      if (var3 == 0) {
         return 0;
      } else {
         long var5 = this.bytesRemaining;
         if (var5 == 0L) {
            return -1;
         } else {
            int var7 = var4;
            int var8 = var3;
            if (var5 < (long)var3) {
               var8 = (int)var5;
               var7 = var4;
            }

            while(true) {
               Exception var10000;
               boolean var10001;
               if (var7 == 0) {
                  label46: {
                     try {
                        var3 = this.loadOperation.getDownloadedLengthFromOffset(this.currentOffset, var8);
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label46;
                     }

                     var7 = var3;
                     if (var3 != 0) {
                        continue;
                     }

                     try {
                        StringBuilder var9 = new StringBuilder();
                        var9.append("not found bytes ");
                        var9.append(var2);
                        FileLog.d(var9.toString());
                        FileLoader.getInstance(this.currentAccount).loadStreamFile(this, this.document, this.parentObject, this.currentOffset);
                        CountDownLatch var14 = new CountDownLatch(1);
                        this.countDownLatch = var14;
                        this.countDownLatch.await();
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label46;
                     }

                     var7 = var3;
                     continue;
                  }
               } else {
                  try {
                     this.file.readFully(var1, var2, var7);
                     this.currentOffset += var7;
                     this.bytesRemaining -= (long)var7;
                     this.bytesTransferred(var7);
                     return var7;
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                  }
               }

               Exception var13 = var10000;
               throw new IOException(var13);
            }
         }
      }
   }
}
