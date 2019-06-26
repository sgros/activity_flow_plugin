package org.osmdroid.tileprovider.util;

import android.os.Handler;
import android.os.Message;
import android.view.View;

public class SimpleInvalidationHandler extends Handler {
   private View mView;

   public SimpleInvalidationHandler(View var1) {
      this.mView = var1;
   }

   public void destroy() {
      this.mView = null;
   }

   public void handleMessage(Message var1) {
      if (var1.what == 0) {
         View var2 = this.mView;
         if (var2 != null) {
            var2.invalidate();
         }
      }

   }
}
