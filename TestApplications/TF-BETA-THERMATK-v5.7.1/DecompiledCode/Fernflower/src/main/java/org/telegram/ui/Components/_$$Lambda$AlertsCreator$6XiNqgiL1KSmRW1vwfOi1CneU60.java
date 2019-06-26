package org.telegram.ui.Components;

import android.util.SparseArray;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$6XiNqgiL1KSmRW1vwfOi1CneU60 implements RequestDelegate {
   // $FF: synthetic field
   private final AlertDialog[] f$0;
   // $FF: synthetic field
   private final BaseFragment f$1;
   // $FF: synthetic field
   private final Runnable f$10;
   // $FF: synthetic field
   private final TLRPC.User f$2;
   // $FF: synthetic field
   private final TLRPC.Chat f$3;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$4;
   // $FF: synthetic field
   private final TLRPC.ChatFull f$5;
   // $FF: synthetic field
   private final long f$6;
   // $FF: synthetic field
   private final MessageObject f$7;
   // $FF: synthetic field
   private final SparseArray[] f$8;
   // $FF: synthetic field
   private final MessageObject.GroupedMessages f$9;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$6XiNqgiL1KSmRW1vwfOi1CneU60(AlertDialog[] var1, BaseFragment var2, TLRPC.User var3, TLRPC.Chat var4, TLRPC.EncryptedChat var5, TLRPC.ChatFull var6, long var7, MessageObject var9, SparseArray[] var10, MessageObject.GroupedMessages var11, Runnable var12) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var9;
      this.f$8 = var10;
      this.f$9 = var11;
      this.f$10 = var12;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      AlertsCreator.lambda$createDeleteMessagesAlert$42(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, var1, var2);
   }
}
