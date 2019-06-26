package org.telegram.ui;

import java.util.HashMap;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.AlertsCreator;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$oQA0vgj3X1YdD8d67UmGyFJyjHY implements AlertsCreator.AccountSelectDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final Integer f$10;
   // $FF: synthetic field
   private final String f$11;
   // $FF: synthetic field
   private final HashMap f$12;
   // $FF: synthetic field
   private final String f$13;
   // $FF: synthetic field
   private final String f$14;
   // $FF: synthetic field
   private final String f$15;
   // $FF: synthetic field
   private final TLRPC.TL_wallPaper f$16;
   // $FF: synthetic field
   private final String f$2;
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
   private final boolean f$8;
   // $FF: synthetic field
   private final Integer f$9;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$oQA0vgj3X1YdD8d67UmGyFJyjHY(LaunchActivity var1, int var2, String var3, String var4, String var5, String var6, String var7, String var8, boolean var9, Integer var10, Integer var11, String var12, HashMap var13, String var14, String var15, String var16, TLRPC.TL_wallPaper var17) {
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
   }

   public final void didSelectAccount(int var1) {
      this.f$0.lambda$runLinkRequest$8$LaunchActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12, this.f$13, this.f$14, this.f$15, this.f$16, var1);
   }
}
