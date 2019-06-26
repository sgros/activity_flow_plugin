package org.telegram.messenger;

import android.telephony.PhoneStateListener;
import org.telegram.ui.Components.EmbedBottomSheet;

class MediaController$3 extends PhoneStateListener {
   // $FF: synthetic field
   final MediaController this$0;

   MediaController$3(MediaController var1) {
      this.this$0 = var1;
   }

   // $FF: synthetic method
   public void lambda$onCallStateChanged$0$MediaController$3(int var1) {
      EmbedBottomSheet var3;
      if (var1 == 1) {
         MediaController var2 = this.this$0;
         if (var2.isPlayingMessage(MediaController.access$000(var2)) && !this.this$0.isMessagePaused()) {
            var2 = this.this$0;
            var2.pauseMessage(MediaController.access$000(var2));
         } else if (MediaController.access$4100(this.this$0) != null || MediaController.access$4200(this.this$0) != null) {
            this.this$0.stopRecording(2);
         }

         var3 = EmbedBottomSheet.getInstance();
         if (var3 != null) {
            var3.pause();
         }

         MediaController.access$4302(this.this$0, true);
      } else if (var1 == 0) {
         MediaController.access$4302(this.this$0, false);
      } else if (var1 == 2) {
         var3 = EmbedBottomSheet.getInstance();
         if (var3 != null) {
            var3.pause();
         }

         MediaController.access$4302(this.this$0, true);
      }

   }

   public void onCallStateChanged(int var1, String var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MediaController$3$kfHEHMBmovTxgGbvrlQqhhaeP5A(this, var1));
   }
}
