package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$s2Q7bjKkmqsnurRGdapKRuUwrkY implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$s2Q7bjKkmqsnurRGdapKRuUwrkY(MessagesStorage var1, int var2, int var3, long var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$markMentionMessageAsRead$58$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}