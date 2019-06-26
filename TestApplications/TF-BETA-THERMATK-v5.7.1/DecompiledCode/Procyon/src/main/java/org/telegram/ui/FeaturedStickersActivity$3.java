// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.util.LongSparseArray;
import org.telegram.ui.Cells.FeaturedStickerSetCell;
import android.view.View;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.StickersAlert;

class FeaturedStickersActivity$3 implements StickersAlertInstallDelegate
{
    final /* synthetic */ FeaturedStickersActivity this$0;
    final /* synthetic */ TLRPC.StickerSetCovered val$stickerSet;
    final /* synthetic */ View val$view;
    
    FeaturedStickersActivity$3(final FeaturedStickersActivity this$0, final View val$view, final TLRPC.StickerSetCovered val$stickerSet) {
        this.this$0 = this$0;
        this.val$view = val$view;
        this.val$stickerSet = val$stickerSet;
    }
    
    @Override
    public void onStickerSetInstalled() {
        ((FeaturedStickerSetCell)this.val$view).setDrawProgress(true);
        final LongSparseArray access$300 = this.this$0.installingStickerSets;
        final TLRPC.StickerSetCovered val$stickerSet = this.val$stickerSet;
        access$300.put(val$stickerSet.set.id, (Object)val$stickerSet);
    }
    
    @Override
    public void onStickerSetUninstalled() {
    }
}
