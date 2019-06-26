package org.telegram.ui;

import android.util.LongSparseArray;
import android.view.View;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.FeaturedStickerSetCell;
import org.telegram.ui.Components.StickersAlert;

class FeaturedStickersActivity$3 implements StickersAlert.StickersAlertInstallDelegate {
   // $FF: synthetic field
   final FeaturedStickersActivity this$0;
   // $FF: synthetic field
   final TLRPC.StickerSetCovered val$stickerSet;
   // $FF: synthetic field
   final View val$view;

   FeaturedStickersActivity$3(FeaturedStickersActivity var1, View var2, TLRPC.StickerSetCovered var3) {
      this.this$0 = var1;
      this.val$view = var2;
      this.val$stickerSet = var3;
   }

   public void onStickerSetInstalled() {
      ((FeaturedStickerSetCell)this.val$view).setDrawProgress(true);
      LongSparseArray var1 = FeaturedStickersActivity.access$300(this.this$0);
      TLRPC.StickerSetCovered var2 = this.val$stickerSet;
      var1.put(var2.set.id, var2);
   }

   public void onStickerSetUninstalled() {
   }
}
