package org.mozilla.focus.utils;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnLongClickListener;

// $FF: synthetic class
public final class _$$Lambda$DialogUtils$MkweB_OU2aR2wuLefGHxJ6Ab9o0 implements OnLongClickListener {
   // $FF: synthetic field
   private final Dialog f$0;
   // $FF: synthetic field
   private final View f$1;

   // $FF: synthetic method
   public _$$Lambda$DialogUtils$MkweB_OU2aR2wuLefGHxJ6Ab9o0(Dialog var1, View var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final boolean onLongClick(View var1) {
      return DialogUtils.lambda$createSpotlightDialog$8(this.f$0, this.f$1, var1);
   }
}
