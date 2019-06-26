package org.telegram.messenger.browser;

import android.content.Context;
import android.net.Uri;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$Browser$UGNHp8wusapZ_BRGXGaD_Ayz4_I implements RequestDelegate {
   // $FF: synthetic field
   private final AlertDialog[] f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final Uri f$2;
   // $FF: synthetic field
   private final Context f$3;
   // $FF: synthetic field
   private final boolean f$4;

   // $FF: synthetic method
   public _$$Lambda$Browser$UGNHp8wusapZ_BRGXGaD_Ayz4_I(AlertDialog[] var1, int var2, Uri var3, Context var4, boolean var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      Browser.lambda$openUrl$1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
