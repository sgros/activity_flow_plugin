package org.mozilla.focus.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;
import java.util.Iterator;
import org.mozilla.focus.download.GetImgHeaderTask;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.rocket.tabs.utils.TabUtil;
import org.mozilla.rocket.tabs.web.Download;
import org.mozilla.rocket.tabs.web.DownloadCallback;
import org.mozilla.urlutils.UrlUtils;

public class WebContextMenu {
   private static boolean canOpenInNewTab(Activity var0, String var1) {
      boolean var2;
      if (var0 != null && !TextUtils.isEmpty(var1) && TabsSessionProvider.getOrNull(var0) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static View createTitleView(Context var0, String var1) {
      TextView var2 = (TextView)LayoutInflater.from(var0).inflate(2131492925, (ViewGroup)null);
      var2.setText(var1);
      return var2;
   }

   private static void openInNewTab(TabView var0, Dialog var1, String var2) {
      SessionManager var6 = TabsSessionProvider.getOrThrow(var1.getOwnerActivity());
      Iterator var3 = var6.getTabs().iterator();

      String var5;
      while(true) {
         if (var3.hasNext()) {
            Session var4 = (Session)var3.next();
            if (var4.getEngineSession().getTabView() != var0) {
               continue;
            }

            var5 = var4.getId();
            break;
         }

         var5 = null;
         break;
      }

      Bundle var7 = TabUtil.argument(var5, false, false);
      var7.putInt("extra_bkg_tab_src", 0);
      var6.addTab(var2, var7);
      TelemetryWrapper.addNewTabFromContextMenu();
   }

   private static void setupMenuForHitTarget(boolean var0, final Dialog var1, NavigationView var2, final DownloadCallback var3, final TabView.HitTarget var4) {
      var2.inflateMenu(2131558401);
      final String var5;
      if (var4.isLink) {
         var5 = var4.linkURL;
      } else {
         var5 = var4.imageURL;
      }

      boolean var6 = canOpenInNewTab(var1.getOwnerActivity(), var5);
      boolean var7 = true;
      boolean var8;
      if (var6 && !var0) {
         var8 = true;
      } else {
         var8 = false;
      }

      MenuItem var9 = var2.getMenu().findItem(2131296518);
      if (var8 && var4.isLink) {
         var0 = true;
      } else {
         var0 = false;
      }

      var9.setVisible(var0);
      var9 = var2.getMenu().findItem(2131296519);
      if (var8 && !var4.isLink && var4.isImage) {
         var0 = true;
      } else {
         var0 = false;
      }

      var9.setVisible(var0);
      var2.getMenu().findItem(2131296516).setVisible(var4.isLink);
      var2.getMenu().findItem(2131296515).setVisible(var4.isLink);
      var2.getMenu().findItem(2131296514).setVisible(var4.isImage);
      var2.getMenu().findItem(2131296512).setVisible(var4.isImage);
      var9 = var2.getMenu().findItem(2131296513);
      if (var4.isImage && UrlUtils.isHttpOrHttps(var4.imageURL)) {
         var0 = var7;
      } else {
         var0 = false;
      }

      var9.setVisible(var0);
      var2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
         public boolean onNavigationItemSelected(MenuItem var1x) {
            var1.dismiss();
            Intent var3x;
            switch(var1x.getItemId()) {
            case 2131296512:
            case 2131296515:
               ClipboardManager var2 = (ClipboardManager)var1.getContext().getSystemService("clipboard");
               Uri var7;
               if (var1x.getItemId() == 2131296515) {
                  TelemetryWrapper.copyLinkEvent();
                  var7 = Uri.parse(var4.linkURL);
               } else {
                  if (var1x.getItemId() != 2131296512) {
                     throw new IllegalStateException("Unknown hitTarget type - cannot copy to clipboard");
                  }

                  TelemetryWrapper.copyImageEvent();
                  var7 = Uri.parse(var4.imageURL);
               }

               var2.setPrimaryClip(ClipData.newUri(var1.getContext().getContentResolver(), "URI", var7));
               return true;
            case 2131296513:
               if (URLUtil.guessFileName(var4.imageURL, (String)null, (String)null).endsWith(".bin")) {
                  GetImgHeaderTask var4x = new GetImgHeaderTask();
                  var4x.setCallback(new GetImgHeaderTask.Callback() {
                     public void setMIMEType(String var1x) {
                        Download var2 = new Download(var4.imageURL, (String)null, (String)null, (String)null, var1x, -1L, true);
                        var3.onDownloadStart(var2);
                     }
                  });
                  var4x.execute(new String[]{var4.imageURL});
               } else {
                  Download var6 = new Download(var4.imageURL, (String)null, (String)null, (String)null, (String)null, -1L, true);
                  var3.onDownloadStart(var6);
               }

               TelemetryWrapper.saveImageEvent();
               return true;
            case 2131296514:
               TelemetryWrapper.shareImageEvent();
               var3x = new Intent("android.intent.action.SEND");
               var3x.setType("text/plain");
               var3x.putExtra("android.intent.extra.TEXT", var4.imageURL);
               var1.getContext().startActivity(Intent.createChooser(var3x, var1.getContext().getString(2131755412)));
               return true;
            case 2131296516:
               TelemetryWrapper.shareLinkEvent();
               var3x = new Intent("android.intent.action.SEND");
               var3x.setType("text/plain");
               var3x.putExtra("android.intent.extra.TEXT", var4.linkURL);
               var1.getContext().startActivity(Intent.createChooser(var3x, var1.getContext().getString(2131755412)));
               return true;
            case 2131296517:
            default:
               StringBuilder var5x = new StringBuilder();
               var5x.append("Unhandled menu item id=");
               var5x.append(var1x.getItemId());
               throw new IllegalArgumentException(var5x.toString());
            case 2131296518:
            case 2131296519:
               WebContextMenu.openInNewTab(var4.source, var1, var5);
               return true;
            }
         }
      });
   }

   public static Dialog show(boolean var0, Activity var1, DownloadCallback var2, TabView.HitTarget var3) {
      if (!var3.isLink && !var3.isImage) {
         throw new IllegalStateException("WebContextMenu can only handle long-press on images and/or links.");
      } else {
         TelemetryWrapper.openWebContextMenuEvent();
         AlertDialog.Builder var4 = new AlertDialog.Builder(var1);
         View var5;
         if (var3.isLink) {
            var5 = createTitleView(var1, var3.linkURL);
         } else {
            if (!var3.isImage) {
               throw new IllegalStateException("Unhandled long press target type");
            }

            var5 = createTitleView(var1, var3.imageURL);
         }

         var4.setCustomTitle(var5);
         var5 = LayoutInflater.from(var1).inflate(2131492924, (ViewGroup)null);
         var4.setView(var5);
         var4.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface var1) {
               TelemetryWrapper.cancelWebContextMenuEvent();
            }
         });
         AlertDialog var6 = var4.create();
         var6.setOwnerActivity(var1);
         setupMenuForHitTarget(var0, var6, (NavigationView)var5.findViewById(2131296380), var2, var3);
         var6.show();
         return var6;
      }
   }
}
