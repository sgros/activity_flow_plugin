package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.SparseArray;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$OgrqERKmCgdU4Q6tzVkBNsq_WCY implements OnClickListener {
   // $FF: synthetic field
   private final MessageObject f$0;
   // $FF: synthetic field
   private final MessageObject.GroupedMessages f$1;
   // $FF: synthetic field
   private final Runnable f$10;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final boolean[] f$4;
   // $FF: synthetic field
   private final SparseArray[] f$5;
   // $FF: synthetic field
   private final TLRPC.User f$6;
   // $FF: synthetic field
   private final boolean[] f$7;
   // $FF: synthetic field
   private final TLRPC.Chat f$8;
   // $FF: synthetic field
   private final TLRPC.ChatFull f$9;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$OgrqERKmCgdU4Q6tzVkBNsq_WCY(MessageObject var1, MessageObject.GroupedMessages var2, TLRPC.EncryptedChat var3, int var4, boolean[] var5, SparseArray[] var6, TLRPC.User var7, boolean[] var8, TLRPC.Chat var9, TLRPC.ChatFull var10, Runnable var11) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
      this.f$9 = var10;
      this.f$10 = var11;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createDeleteMessagesAlert$49(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, var1, var2);
   }
}
