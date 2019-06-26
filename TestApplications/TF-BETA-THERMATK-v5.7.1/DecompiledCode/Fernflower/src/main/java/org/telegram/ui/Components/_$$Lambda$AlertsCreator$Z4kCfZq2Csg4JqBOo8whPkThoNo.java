package org.telegram.ui.Components;

import android.view.View;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$Z4kCfZq2Csg4JqBOo8whPkThoNo implements OnClickListener {
   // $FF: synthetic field
   private final long f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final MessagesStorage.IntCallback f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final BaseFragment f$5;
   // $FF: synthetic field
   private final ArrayList f$6;
   // $FF: synthetic field
   private final MessagesStorage.IntCallback f$7;
   // $FF: synthetic field
   private final AlertDialog.Builder f$8;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$Z4kCfZq2Csg4JqBOo8whPkThoNo(long var1, int var3, boolean var4, MessagesStorage.IntCallback var5, int var6, BaseFragment var7, ArrayList var8, MessagesStorage.IntCallback var9, AlertDialog.Builder var10) {
      this.f$0 = var1;
      this.f$1 = var3;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var7;
      this.f$6 = var8;
      this.f$7 = var9;
      this.f$8 = var10;
   }

   public final void onClick(View var1) {
      AlertsCreator.lambda$showCustomNotificationsDialog$3(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, var1);
   }
}
