package org.telegram.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$ihsVyPyraLc397NrumrLt_QVanE implements OnTouchListener {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final ContentPreviewViewer.ContentPreviewViewerDelegate f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$ihsVyPyraLc397NrumrLt_QVanE(ChatActivity var1, ContentPreviewViewer.ContentPreviewViewerDelegate var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final boolean onTouch(View var1, MotionEvent var2) {
      return this.f$0.lambda$createView$24$ChatActivity(this.f$1, var1, var2);
   }
}
