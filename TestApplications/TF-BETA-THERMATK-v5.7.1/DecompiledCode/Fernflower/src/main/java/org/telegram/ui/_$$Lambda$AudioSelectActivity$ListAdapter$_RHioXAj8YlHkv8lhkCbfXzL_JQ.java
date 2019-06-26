package org.telegram.ui;

import org.telegram.messenger.MessageObject;
import org.telegram.ui.Cells.AudioCell;

// $FF: synthetic class
public final class _$$Lambda$AudioSelectActivity$ListAdapter$_RHioXAj8YlHkv8lhkCbfXzL_JQ implements AudioCell.AudioCellDelegate {
   // $FF: synthetic field
   private final AudioSelectActivity.ListAdapter f$0;

   // $FF: synthetic method
   public _$$Lambda$AudioSelectActivity$ListAdapter$_RHioXAj8YlHkv8lhkCbfXzL_JQ(AudioSelectActivity.ListAdapter var1) {
      this.f$0 = var1;
   }

   public final void startedPlayingAudio(MessageObject var1) {
      this.f$0.lambda$onCreateViewHolder$0$AudioSelectActivity$ListAdapter(var1);
   }
}
