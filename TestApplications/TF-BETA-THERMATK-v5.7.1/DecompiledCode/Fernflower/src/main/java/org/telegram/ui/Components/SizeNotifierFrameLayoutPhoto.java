package org.telegram.ui.Components;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;

public class SizeNotifierFrameLayoutPhoto extends FrameLayout {
   private SizeNotifierFrameLayoutPhoto.SizeNotifierFrameLayoutPhotoDelegate delegate;
   private int keyboardHeight;
   private android.graphics.Rect rect = new android.graphics.Rect();
   private WindowManager windowManager;
   private boolean withoutWindow;

   public SizeNotifierFrameLayoutPhoto(Context var1) {
      super(var1);
   }

   public int getKeyboardHeight() {
      View var1 = this.getRootView();
      this.getWindowVisibleDisplayFrame(this.rect);
      boolean var2 = this.withoutWindow;
      int var3 = 0;
      int var4;
      int var5;
      if (var2) {
         var4 = var1.getHeight();
         if (this.rect.top != 0) {
            var3 = AndroidUtilities.statusBarHeight;
         }

         var5 = AndroidUtilities.getViewInset(var1);
         android.graphics.Rect var6 = this.rect;
         return var4 - var3 - var5 - (var6.bottom - var6.top);
      } else {
         var4 = var1.getHeight();
         var3 = AndroidUtilities.getViewInset(var1);
         var5 = this.rect.top;
         var4 = AndroidUtilities.displaySize.y - var5 - (var4 - var3);
         var3 = var4;
         if (var4 <= Math.max(AndroidUtilities.dp(10.0F), AndroidUtilities.statusBarHeight)) {
            var3 = 0;
         }

         return var3;
      }
   }

   public void notifyHeightChanged() {
      if (this.delegate != null) {
         this.keyboardHeight = this.getKeyboardHeight();
         android.graphics.Point var1 = AndroidUtilities.displaySize;
         final boolean var2;
         if (var1.x > var1.y) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.post(new Runnable() {
            public void run() {
               if (SizeNotifierFrameLayoutPhoto.this.delegate != null) {
                  SizeNotifierFrameLayoutPhoto.this.delegate.onSizeChanged(SizeNotifierFrameLayoutPhoto.this.keyboardHeight, var2);
               }

            }
         });
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.notifyHeightChanged();
   }

   public void setDelegate(SizeNotifierFrameLayoutPhoto.SizeNotifierFrameLayoutPhotoDelegate var1) {
      this.delegate = var1;
   }

   public void setWithoutWindow(boolean var1) {
      this.withoutWindow = var1;
   }

   public interface SizeNotifierFrameLayoutPhotoDelegate {
      void onSizeChanged(int var1, boolean var2);
   }
}
