package org.telegram.ui.ActionBar;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

// $FF: synthetic class
public final class _$$Lambda$ActionBarMenuItem$DSACM5xoXBBb_9TAnJG5eS_F3HQ implements OnEditorActionListener {
   // $FF: synthetic field
   private final ActionBarMenuItem f$0;

   // $FF: synthetic method
   public _$$Lambda$ActionBarMenuItem$DSACM5xoXBBb_9TAnJG5eS_F3HQ(ActionBarMenuItem var1) {
      this.f$0 = var1;
   }

   public final boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
      return this.f$0.lambda$setIsSearchField$7$ActionBarMenuItem(var1, var2, var3);
   }
}
