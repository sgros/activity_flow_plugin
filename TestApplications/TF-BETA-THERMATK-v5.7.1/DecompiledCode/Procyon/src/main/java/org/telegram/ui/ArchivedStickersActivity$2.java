// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.ui.Cells.ArchivedStickerSetCell;
import android.view.View;
import org.telegram.ui.Components.StickersAlert;

class ArchivedStickersActivity$2 implements StickersAlertInstallDelegate
{
    final /* synthetic */ ArchivedStickersActivity this$0;
    final /* synthetic */ View val$view;
    
    ArchivedStickersActivity$2(final ArchivedStickersActivity this$0, final View val$view) {
        this.this$0 = this$0;
        this.val$view = val$view;
    }
    
    @Override
    public void onStickerSetInstalled() {
        ((ArchivedStickerSetCell)this.val$view).setChecked(true);
    }
    
    @Override
    public void onStickerSetUninstalled() {
        ((ArchivedStickerSetCell)this.val$view).setChecked(false);
    }
}
