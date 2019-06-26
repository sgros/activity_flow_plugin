package org.telegram.ui;

import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.Cells.NotificationsCheckCell;

// $FF: synthetic class
public final class _$$Lambda$NotificationsCustomSettingsActivity$ZmFDQGaW1AFFelUamuZaIwn_BvM implements MessagesStorage.IntCallback {
   // $FF: synthetic field
   private final NotificationsCustomSettingsActivity f$0;
   // $FF: synthetic field
   private final NotificationsCheckCell f$1;
   // $FF: synthetic field
   private final RecyclerView.ViewHolder f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$NotificationsCustomSettingsActivity$ZmFDQGaW1AFFelUamuZaIwn_BvM(NotificationsCustomSettingsActivity var1, NotificationsCheckCell var2, RecyclerView.ViewHolder var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(int var1) {
      this.f$0.lambda$null$3$NotificationsCustomSettingsActivity(this.f$1, this.f$2, this.f$3, var1);
   }
}
