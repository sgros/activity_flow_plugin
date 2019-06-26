package org.telegram.ui.Components;

import android.util.SparseArray;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$qKrVsguRKMu04xHOh5j6MH_69nE implements Runnable {
   // $FF: synthetic field
   private final AlertDialog[] f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final MessageObject.GroupedMessages f$10;
   // $FF: synthetic field
   private final Runnable f$11;
   // $FF: synthetic field
   private final BaseFragment f$2;
   // $FF: synthetic field
   private final TLRPC.User f$3;
   // $FF: synthetic field
   private final TLRPC.Chat f$4;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$5;
   // $FF: synthetic field
   private final TLRPC.ChatFull f$6;
   // $FF: synthetic field
   private final long f$7;
   // $FF: synthetic field
   private final MessageObject f$8;
   // $FF: synthetic field
   private final SparseArray[] f$9;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$qKrVsguRKMu04xHOh5j6MH_69nE(AlertDialog[] var1, TLObject var2, BaseFragment var3, TLRPC.User var4, TLRPC.Chat var5, TLRPC.EncryptedChat var6, TLRPC.ChatFull var7, long var8, MessageObject var10, SparseArray[] var11, MessageObject.GroupedMessages var12, Runnable var13) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var10;
      this.f$9 = var11;
      this.f$10 = var12;
      this.f$11 = var13;
   }

   public final void run() {
      AlertsCreator.lambda$null$41(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11);
   }
}
