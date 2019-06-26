package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

class FileLoader$1 implements FileUploadOperation.FileUploadOperationDelegate {
   // $FF: synthetic field
   final FileLoader this$0;
   // $FF: synthetic field
   final boolean val$encrypted;
   // $FF: synthetic field
   final String val$location;
   // $FF: synthetic field
   final boolean val$small;

   FileLoader$1(FileLoader var1, boolean var2, String var3, boolean var4) {
      this.this$0 = var1;
      this.val$encrypted = var2;
      this.val$location = var3;
      this.val$small = var4;
   }

   public void didChangedUploadProgress(FileUploadOperation var1, float var2) {
      if (FileLoader.access$100(this.this$0) != null) {
         FileLoader.access$100(this.this$0).fileUploadProgressChanged(this.val$location, var2, this.val$encrypted);
      }

   }

   public void didFailedUploadingFile(FileUploadOperation var1) {
      FileLoader.access$300().postRunnable(new _$$Lambda$FileLoader$1$4L6YnCrX_lUiw2AHHpo_e3FY75I(this, this.val$encrypted, this.val$location, this.val$small));
   }

   public void didFinishUploadingFile(FileUploadOperation var1, TLRPC.InputFile var2, TLRPC.InputEncryptedFile var3, byte[] var4, byte[] var5) {
      FileLoader.access$300().postRunnable(new _$$Lambda$FileLoader$1$8qI8Hd84Wsy2NQjG71Z0jivjIew(this, this.val$encrypted, this.val$location, this.val$small, var2, var3, var4, var5, var1));
   }

   // $FF: synthetic method
   public void lambda$didFailedUploadingFile$1$FileLoader$1(boolean var1, String var2, boolean var3) {
      if (var1) {
         FileLoader.access$400(this.this$0).remove(var2);
      } else {
         FileLoader.access$500(this.this$0).remove(var2);
      }

      if (FileLoader.access$100(this.this$0) != null) {
         FileLoader.access$100(this.this$0).fileDidFailedUpload(var2, var1);
      }

      FileUploadOperation var4;
      if (var3) {
         FileLoader.access$610(this.this$0);
         if (FileLoader.access$600(this.this$0) < 1) {
            var4 = (FileUploadOperation)FileLoader.access$700(this.this$0).poll();
            if (var4 != null) {
               FileLoader.access$608(this.this$0);
               var4.start();
            }
         }
      } else {
         FileLoader.access$810(this.this$0);
         if (FileLoader.access$800(this.this$0) < 1) {
            var4 = (FileUploadOperation)FileLoader.access$900(this.this$0).poll();
            if (var4 != null) {
               FileLoader.access$808(this.this$0);
               var4.start();
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$didFinishUploadingFile$0$FileLoader$1(boolean var1, String var2, boolean var3, TLRPC.InputFile var4, TLRPC.InputEncryptedFile var5, byte[] var6, byte[] var7, FileUploadOperation var8) {
      if (var1) {
         FileLoader.access$400(this.this$0).remove(var2);
      } else {
         FileLoader.access$500(this.this$0).remove(var2);
      }

      FileUploadOperation var9;
      if (var3) {
         FileLoader.access$610(this.this$0);
         if (FileLoader.access$600(this.this$0) < 1) {
            var9 = (FileUploadOperation)FileLoader.access$700(this.this$0).poll();
            if (var9 != null) {
               FileLoader.access$608(this.this$0);
               var9.start();
            }
         }
      } else {
         FileLoader.access$810(this.this$0);
         if (FileLoader.access$800(this.this$0) < 1) {
            var9 = (FileUploadOperation)FileLoader.access$900(this.this$0).poll();
            if (var9 != null) {
               FileLoader.access$808(this.this$0);
               var9.start();
            }
         }
      }

      if (FileLoader.access$100(this.this$0) != null) {
         FileLoader.access$100(this.this$0).fileDidUploaded(var2, var4, var5, var6, var7, var8.getTotalFileSize());
      }

   }
}
