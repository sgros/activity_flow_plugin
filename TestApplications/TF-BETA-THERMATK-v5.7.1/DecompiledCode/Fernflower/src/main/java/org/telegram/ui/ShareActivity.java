package org.telegram.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.ShareAlert;

public class ShareActivity extends Activity {
   private Dialog visibleDialog;

   // $FF: synthetic method
   public void lambda$onCreate$0$ShareActivity(DialogInterface var1) {
      if (!this.isFinishing()) {
         this.finish();
      }

      this.visibleDialog = null;
   }

   protected void onCreate(Bundle var1) {
      ApplicationLoader.postInitApplication();
      AndroidUtilities.checkDisplaySize(this, this.getResources().getConfiguration());
      this.requestWindowFeature(1);
      this.setTheme(2131624216);
      super.onCreate(var1);
      this.setContentView(new View(this), new LayoutParams(-1, -1));
      Intent var6 = this.getIntent();
      if (var6 != null && "android.intent.action.VIEW".equals(var6.getAction()) && var6.getData() != null) {
         Uri var7 = var6.getData();
         String var2 = var7.getScheme();
         String var3 = var7.toString();
         String var8 = var7.getQueryParameter("hash");
         if ("tgb".equals(var2) && var3.toLowerCase().startsWith("tgb://share_game_score") && !TextUtils.isEmpty(var8)) {
            SharedPreferences var9 = ApplicationLoader.applicationContext.getSharedPreferences("botshare", 0);
            StringBuilder var13 = new StringBuilder();
            var13.append(var8);
            var13.append("_m");
            var3 = var9.getString(var13.toString(), (String)null);
            if (TextUtils.isEmpty(var3)) {
               this.finish();
            } else {
               SerializedData var4 = new SerializedData(Utilities.hexToBytes(var3));
               TLRPC.Message var14 = TLRPC.Message.TLdeserialize(var4, var4.readInt32(false), false);
               var14.readAttachPath(var4, 0);
               var4.cleanup();
               if (var14 == null) {
                  this.finish();
               } else {
                  StringBuilder var15 = new StringBuilder();
                  var15.append(var8);
                  var15.append("_link");
                  var8 = var9.getString(var15.toString(), (String)null);
                  MessageObject var10 = new MessageObject(UserConfig.selectedAccount, var14, false);
                  var10.messageOwner.with_my_score = true;

                  try {
                     this.visibleDialog = ShareAlert.createShareAlert(this, var10, (String)null, false, var8, false);
                     this.visibleDialog.setCanceledOnTouchOutside(true);
                     Dialog var12 = this.visibleDialog;
                     _$$Lambda$ShareActivity$8CDJt1az5uGqAsSjal6N7RJDepQ var11 = new _$$Lambda$ShareActivity$8CDJt1az5uGqAsSjal6N7RJDepQ(this);
                     var12.setOnDismissListener(var11);
                     this.visibleDialog.show();
                  } catch (Exception var5) {
                     FileLog.e((Throwable)var5);
                     this.finish();
                  }

               }
            }
         } else {
            this.finish();
         }
      } else {
         this.finish();
      }
   }

   public void onPause() {
      super.onPause();

      try {
         if (this.visibleDialog != null && this.visibleDialog.isShowing()) {
            this.visibleDialog.dismiss();
            this.visibleDialog = null;
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }
}
