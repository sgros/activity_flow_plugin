// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

class SendMessagesHelper$2 implements Runnable
{
    final /* synthetic */ SendMessagesHelper this$0;
    final /* synthetic */ Runnable val$finishRunnable;
    final /* synthetic */ String val$key;
    
    SendMessagesHelper$2(final SendMessagesHelper this$0, final String val$key, final Runnable val$finishRunnable) {
        this.this$0 = this$0;
        this.val$key = val$key;
        this.val$finishRunnable = val$finishRunnable;
    }
    
    @Override
    public void run() {
        this.this$0.waitingForVote.remove(this.val$key);
        final Runnable val$finishRunnable = this.val$finishRunnable;
        if (val$finishRunnable != null) {
            val$finishRunnable.run();
        }
    }
}
