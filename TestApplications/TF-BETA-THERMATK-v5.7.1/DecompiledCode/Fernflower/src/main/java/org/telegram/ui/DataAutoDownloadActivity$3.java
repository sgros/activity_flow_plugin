package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Cells.MaxFileSizeCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;

class DataAutoDownloadActivity$3 extends MaxFileSizeCell {
   // $FF: synthetic field
   final DataAutoDownloadActivity this$0;
   // $FF: synthetic field
   final AnimatorSet[] val$animatorSet;
   // $FF: synthetic field
   final TextCheckCell[] val$checkCell;
   // $FF: synthetic field
   final TextInfoPrivacyCell val$infoCell;
   // $FF: synthetic field
   final int val$position;

   DataAutoDownloadActivity$3(DataAutoDownloadActivity var1, Context var2, int var3, TextInfoPrivacyCell var4, TextCheckCell[] var5, AnimatorSet[] var6) {
      super(var2);
      this.this$0 = var1;
      this.val$position = var3;
      this.val$infoCell = var4;
      this.val$checkCell = var5;
      this.val$animatorSet = var6;
   }

   protected void didChangedSizeValue(int var1) {
      if (this.val$position == DataAutoDownloadActivity.access$2200(this.this$0)) {
         TextInfoPrivacyCell var2 = this.val$infoCell;
         boolean var3 = true;
         var2.setText(LocaleController.formatString("AutoDownloadPreloadVideoInfo", 2131558772, AndroidUtilities.formatFileSize((long)var1)));
         if (var1 <= 2097152) {
            var3 = false;
         }

         if (var3 != this.val$checkCell[0].isEnabled()) {
            ArrayList var5 = new ArrayList();
            this.val$checkCell[0].setEnabled(var3, var5);
            AnimatorSet[] var4 = this.val$animatorSet;
            if (var4[0] != null) {
               var4[0].cancel();
               this.val$animatorSet[0] = null;
            }

            this.val$animatorSet[0] = new AnimatorSet();
            this.val$animatorSet[0].playTogether(var5);
            this.val$animatorSet[0].addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (var1.equals(DataAutoDownloadActivity$3.this.val$animatorSet[0])) {
                     DataAutoDownloadActivity$3.this.val$animatorSet[0] = null;
                  }

               }
            });
            this.val$animatorSet[0].setDuration(150L);
            this.val$animatorSet[0].start();
         }
      }

   }
}
