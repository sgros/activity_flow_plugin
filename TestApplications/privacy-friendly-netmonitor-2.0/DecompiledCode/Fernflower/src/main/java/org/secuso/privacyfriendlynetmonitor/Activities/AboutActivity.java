package org.secuso.privacyfriendlynetmonitor.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;

public class AboutActivity extends AppCompatActivity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131427355);
      RunStore.setContext(this);
      ActionBar var2 = this.getSupportActionBar();
      if (var2 != null) {
         var2.setDisplayHomeAsUpEnabled(true);
      }

      View var3 = this.findViewById(2131296393);
      if (var3 != null) {
         var3.setAlpha(0.0F);
         var3.animate().alpha(1.0F).setDuration(250L);
      }

      this.overridePendingTransition(0, 0);
      ((TextView)this.findViewById(2131296379)).setMovementMethod(LinkMovementMethod.getInstance());
      ((TextView)this.findViewById(2131296257)).setMovementMethod(LinkMovementMethod.getInstance());
      ((TextView)this.findViewById(2131296477)).setMovementMethod(LinkMovementMethod.getInstance());
      ((TextView)this.findViewById(2131296359)).setMovementMethod(LinkMovementMethod.getInstance());
      ((TextView)this.findViewById(2131296513)).setText("2.0");
   }
}
