package org.mozilla.rocket.component;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.rocket.content.HomeFragmentViewState;

public final class RocketLauncherActivity extends AppCompatActivity {
   private final void dispatchNormalIntent() {
      Intent var1 = new Intent(this.getIntent());
      var1.setClass(this.getApplicationContext(), MainActivity.class);
      this.filterFlags(var1);
      this.startActivity(var1);
      this.finish();
   }

   private final void filterFlags(Intent var1) {
      var1.setFlags(var1.getFlags() & -268435457);
      var1.setFlags(var1.getFlags() & -32769);
   }

   protected void onCreate(Bundle var1) {
      // $FF: Couldn't be decompiled
   }
}
