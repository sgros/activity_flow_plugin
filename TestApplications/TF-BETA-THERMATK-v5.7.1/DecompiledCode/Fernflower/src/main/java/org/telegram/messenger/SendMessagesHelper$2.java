package org.telegram.messenger;

class SendMessagesHelper$2 implements Runnable {
   // $FF: synthetic field
   final SendMessagesHelper this$0;
   // $FF: synthetic field
   final Runnable val$finishRunnable;
   // $FF: synthetic field
   final String val$key;

   SendMessagesHelper$2(SendMessagesHelper var1, String var2, Runnable var3) {
      this.this$0 = var1;
      this.val$key = var2;
      this.val$finishRunnable = var3;
   }

   public void run() {
      SendMessagesHelper.access$1300(this.this$0).remove(this.val$key);
      Runnable var1 = this.val$finishRunnable;
      if (var1 != null) {
         var1.run();
      }

   }
}
