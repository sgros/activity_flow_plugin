package org.mozilla.focus.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;

public class FileChooseAction {
   private ValueCallback callback;
   private Fragment hostFragment;
   private FileChooserParams params;
   private Uri[] uris;

   public FileChooseAction(Fragment var1, ValueCallback var2, FileChooserParams var3) {
      this.hostFragment = var1;
      this.callback = var2;
      this.params = var3;
   }

   private Intent createChooserIntent(FileChooserParams var1) {
      String[] var2 = var1.getAcceptTypes();
      CharSequence var3 = var1.getTitle();
      Object var4 = var3;
      if (TextUtils.isEmpty(var3)) {
         var4 = this.hostFragment.getString(2131755196);
      }

      return FilePickerUtil.getFilePickerIntent(this.hostFragment.getActivity(), (CharSequence)var4, var2);
   }

   public void cancel() {
      this.callback.onReceiveValue((Object)null);
   }

   public boolean onFileChose(int var1, Intent var2) {
      if (this.callback == null) {
         return true;
      } else if (var1 == -1 && var2 != null) {
         label38: {
            Exception var10000;
            label45: {
               boolean var10001;
               Uri var3;
               try {
                  var3 = var2.getData();
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label45;
               }

               Uri[] var7;
               if (var3 == null) {
                  var7 = null;
               } else {
                  try {
                     var7 = new Uri[1];
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label45;
                  }

                  var7[0] = var3;
               }

               try {
                  this.uris = var7;
                  this.callback.onReceiveValue(this.uris);
                  break label38;
               } catch (Exception var4) {
                  var10000 = var4;
                  var10001 = false;
               }
            }

            Exception var8 = var10000;
            this.callback.onReceiveValue((Object)null);
            var8.printStackTrace();
         }

         this.callback = null;
         return true;
      } else {
         this.callback.onReceiveValue((Object)null);
         this.callback = null;
         return true;
      }
   }

   public void startChooserActivity() {
      this.hostFragment.startActivityForResult(this.createChooserIntent(this.params), 103);
   }
}
