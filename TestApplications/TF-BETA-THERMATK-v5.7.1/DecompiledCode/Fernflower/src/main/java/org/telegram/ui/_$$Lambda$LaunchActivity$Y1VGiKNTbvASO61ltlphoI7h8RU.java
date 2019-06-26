package org.telegram.ui;

import java.util.HashMap;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$Y1VGiKNTbvASO61ltlphoI7h8RU implements RequestDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final Integer f$10;
   // $FF: synthetic field
   private final Integer f$11;
   // $FF: synthetic field
   private final String f$12;
   // $FF: synthetic field
   private final HashMap f$13;
   // $FF: synthetic field
   private final String f$14;
   // $FF: synthetic field
   private final String f$15;
   // $FF: synthetic field
   private final String f$16;
   // $FF: synthetic field
   private final TLRPC.TL_wallPaper f$17;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final String f$4;
   // $FF: synthetic field
   private final String f$5;
   // $FF: synthetic field
   private final String f$6;
   // $FF: synthetic field
   private final String f$7;
   // $FF: synthetic field
   private final String f$8;
   // $FF: synthetic field
   private final boolean f$9;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$Y1VGiKNTbvASO61ltlphoI7h8RU(LaunchActivity var1, AlertDialog var2, int var3, String var4, String var5, String var6, String var7, String var8, String var9, boolean var10, Integer var11, Integer var12, String var13, HashMap var14, String var15, String var16, String var17, TLRPC.TL_wallPaper var18) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
      this.f$9 = var10;
      this.f$10 = var11;
      this.f$11 = var12;
      this.f$12 = var13;
      this.f$13 = var14;
      this.f$14 = var15;
      this.f$15 = var16;
      this.f$16 = var17;
      this.f$17 = var18;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$runLinkRequest$15$LaunchActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12, this.f$13, this.f$14, this.f$15, this.f$16, this.f$17, var1, var2);
   }
}
