package org.mozilla.focus.screenshot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import org.mozilla.focus.Inject;
import org.mozilla.focus.fragment.BrowserFragment;
import org.mozilla.focus.fragment.ScreenCaptureDialogFragment;
import org.mozilla.focus.utils.Settings;

public class CaptureRunnable extends ScreenshotCaptureTask implements Runnable, BrowserFragment.ScreenshotCallback {
   final WeakReference refBrowserFragment;
   final WeakReference refContainerView;
   final WeakReference refContext;
   final WeakReference refScreenCaptureDialogFragment;

   public CaptureRunnable(Context var1, BrowserFragment var2, ScreenCaptureDialogFragment var3, View var4) {
      super(var1);
      this.refContext = new WeakReference(var1);
      this.refBrowserFragment = new WeakReference(var2);
      this.refScreenCaptureDialogFragment = new WeakReference(var3);
      this.refContainerView = new WeakReference(var4);
   }

   private void promptScreenshotResult(boolean var1) {
      Context var2 = (Context)this.refContext.get();
      if (var2 != null) {
         if (this.refBrowserFragment != null) {
            BrowserFragment var3 = (BrowserFragment)this.refBrowserFragment.get();
            if (var3 != null && var3.getCaptureStateListener() != null) {
               var3.getCaptureStateListener().onPromptScreenshotResult();
            }

            if (var3 != null && var1 && !Settings.getInstance(var2).getEventHistory().contains("show_my_shot_on_boarding_dialog")) {
               var3.showMyShotOnBoarding();
               return;
            }
         }

         int var4;
         if (var1) {
            var4 = 2131755389;
         } else {
            var4 = 2131755376;
         }

         Toast.makeText(var2, var4, 0).show();
      }
   }

   public void onCaptureComplete(String var1, String var2, Bitmap var3) {
      if ((Context)this.refContext.get() != null) {
         this.execute(new Object[]{var1, var2, var3});
      }
   }

   protected void onPostExecute(String var1) {
      ScreenCaptureDialogFragment var2 = (ScreenCaptureDialogFragment)this.refScreenCaptureDialogFragment.get();
      if (var2 == null) {
         this.cancel(true);
      } else {
         final boolean var3 = TextUtils.isEmpty(var1) ^ true;
         if (var3) {
            Settings.getInstance((Context)this.refContext.get()).setHasUnreadMyShot(true);
         }

         var2.getDialog().setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface var1) {
               CaptureRunnable.this.promptScreenshotResult(var3);
            }
         });
         if (TextUtils.isEmpty(var1)) {
            var2.dismiss();
         } else {
            var2.dismiss(Inject.isUnderEspressoTest() ^ true);
         }

      }
   }

   public void run() {
      BrowserFragment var1 = (BrowserFragment)this.refBrowserFragment.get();
      if (var1 != null) {
         if (!var1.capturePage(this)) {
            ScreenCaptureDialogFragment var2 = (ScreenCaptureDialogFragment)this.refScreenCaptureDialogFragment.get();
            if (var2 != null) {
               var2.dismiss();
            }

            this.promptScreenshotResult(false);
         }

      }
   }

   public interface CaptureStateListener {
      void onPromptScreenshotResult();
   }
}
