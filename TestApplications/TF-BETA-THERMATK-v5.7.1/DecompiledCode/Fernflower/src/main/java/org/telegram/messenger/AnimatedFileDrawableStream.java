package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLRPC;

public class AnimatedFileDrawableStream implements FileLoadOperationStream {
   private volatile boolean canceled;
   private CountDownLatch countDownLatch;
   private int currentAccount;
   private TLRPC.Document document;
   private int lastOffset;
   private FileLoadOperation loadOperation;
   private Object parentObject;
   private final Object sync = new Object();
   private boolean waitingForLoad;

   public AnimatedFileDrawableStream(TLRPC.Document var1, Object var2, int var3) {
      this.document = var1;
      this.parentObject = var2;
      this.currentAccount = var3;
      this.loadOperation = FileLoader.getInstance(this.currentAccount).loadStreamFile(this, this.document, this.parentObject, 0);
   }

   public void cancel() {
      this.cancel(true);
   }

   public void cancel(boolean var1) {
      Object var2 = this.sync;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label193: {
         label192: {
            try {
               if (this.countDownLatch == null) {
                  break label192;
               }

               this.countDownLatch.countDown();
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label193;
            }

            if (var1) {
               try {
                  if (!this.canceled) {
                     FileLoader.getInstance(this.currentAccount).removeLoadingVideo(this.document, false, true);
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label193;
               }
            }
         }

         label184:
         try {
            this.canceled = true;
            return;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label184;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public int getCurrentAccount() {
      return this.currentAccount;
   }

   public TLRPC.Document getDocument() {
      return this.document;
   }

   public Object getParentObject() {
      return this.document;
   }

   public boolean isWaitingForLoad() {
      return this.waitingForLoad;
   }

   public void newDataAvailable() {
      CountDownLatch var1 = this.countDownLatch;
      if (var1 != null) {
         var1.countDown();
      }

   }

   public int read(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public void reset() {
      // $FF: Couldn't be decompiled
   }
}
