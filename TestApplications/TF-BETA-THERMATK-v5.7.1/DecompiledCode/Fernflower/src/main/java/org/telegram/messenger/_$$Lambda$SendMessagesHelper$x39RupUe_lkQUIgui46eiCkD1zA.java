package org.telegram.messenger;

import androidx.core.view.inputmethod.InputContentInfoCompat;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$x39RupUe_lkQUIgui46eiCkD1zA implements Runnable {
   // $FF: synthetic field
   private final ArrayList f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final boolean f$4;
   // $FF: synthetic field
   private final MessageObject f$5;
   // $FF: synthetic field
   private final MessageObject f$6;
   // $FF: synthetic field
   private final InputContentInfoCompat f$7;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$x39RupUe_lkQUIgui46eiCkD1zA(ArrayList var1, long var2, int var4, boolean var5, boolean var6, MessageObject var7, MessageObject var8, InputContentInfoCompat var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var7;
      this.f$6 = var8;
      this.f$7 = var9;
   }

   public final void run() {
      SendMessagesHelper.lambda$prepareSendingMedia$63(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
