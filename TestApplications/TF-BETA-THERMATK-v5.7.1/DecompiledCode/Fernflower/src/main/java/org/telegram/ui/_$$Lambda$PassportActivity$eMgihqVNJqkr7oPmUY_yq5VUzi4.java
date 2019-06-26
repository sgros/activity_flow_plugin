package org.telegram.ui;

import android.view.View;
import android.view.View.OnLongClickListener;
import org.telegram.messenger.SecureDocument;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$eMgihqVNJqkr7oPmUY_yq5VUzi4 implements OnLongClickListener {
   // $FF: synthetic field
   private final PassportActivity f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final SecureDocument f$2;
   // $FF: synthetic field
   private final PassportActivity.SecureDocumentCell f$3;
   // $FF: synthetic field
   private final String f$4;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$eMgihqVNJqkr7oPmUY_yq5VUzi4(PassportActivity var1, int var2, SecureDocument var3, PassportActivity.SecureDocumentCell var4, String var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final boolean onLongClick(View var1) {
      return this.f$0.lambda$addDocumentView$58$PassportActivity(this.f$1, this.f$2, this.f$3, this.f$4, var1);
   }
}
