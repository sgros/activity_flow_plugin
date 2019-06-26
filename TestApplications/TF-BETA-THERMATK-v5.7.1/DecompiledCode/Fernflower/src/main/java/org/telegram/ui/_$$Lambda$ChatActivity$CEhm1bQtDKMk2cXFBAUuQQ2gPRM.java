package org.telegram.ui;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$CEhm1bQtDKMk2cXFBAUuQQ2gPRM implements OnTouchListener {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final View f$1;
   // $FF: synthetic field
   private final Rect f$2;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$CEhm1bQtDKMk2cXFBAUuQQ2gPRM(ChatActivity var1, View var2, Rect var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final boolean onTouch(View var1, MotionEvent var2) {
      return this.f$0.lambda$createMenu$63$ChatActivity(this.f$1, this.f$2, var1, var2);
   }
}
