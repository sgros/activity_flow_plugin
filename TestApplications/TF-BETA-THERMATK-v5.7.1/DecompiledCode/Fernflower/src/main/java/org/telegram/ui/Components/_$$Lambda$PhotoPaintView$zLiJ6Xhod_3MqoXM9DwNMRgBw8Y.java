package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$PhotoPaintView$zLiJ6Xhod_3MqoXM9DwNMRgBw8Y implements OnClickListener {
   // $FF: synthetic field
   private final Runnable f$0;

   // $FF: synthetic method
   public _$$Lambda$PhotoPaintView$zLiJ6Xhod_3MqoXM9DwNMRgBw8Y(Runnable var1) {
      this.f$0 = var1;
   }

   public final void onClick(DialogInterface var1, int var2) {
      PhotoPaintView.lambda$maybeShowDismissalAlert$6(this.f$0, var1, var2);
   }
}
