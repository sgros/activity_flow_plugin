package org.telegram.messenger.browser;

import android.content.Context;
import android.net.Uri;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$Browser$37_x46nyg5sVKyQShvHeLalxsGQ implements Runnable {
   // $FF: synthetic field
   private final AlertDialog[] f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final Uri f$3;
   // $FF: synthetic field
   private final Context f$4;
   // $FF: synthetic field
   private final boolean f$5;

   // $FF: synthetic method
   public _$$Lambda$Browser$37_x46nyg5sVKyQShvHeLalxsGQ(AlertDialog[] var1, TLObject var2, int var3, Uri var4, Context var5, boolean var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      Browser.lambda$null$0(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
