package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Cells.ArchivedStickerSetCell;
import org.telegram.ui.Components.StickersAlert;

class ArchivedStickersActivity$2 implements StickersAlert.StickersAlertInstallDelegate {
   // $FF: synthetic field
   final ArchivedStickersActivity this$0;
   // $FF: synthetic field
   final View val$view;

   ArchivedStickersActivity$2(ArchivedStickersActivity var1, View var2) {
      this.this$0 = var1;
      this.val$view = var2;
   }

   public void onStickerSetInstalled() {
      ((ArchivedStickerSetCell)this.val$view).setChecked(true);
   }

   public void onStickerSetUninstalled() {
      ((ArchivedStickerSetCell)this.val$view).setChecked(false);
   }
}
