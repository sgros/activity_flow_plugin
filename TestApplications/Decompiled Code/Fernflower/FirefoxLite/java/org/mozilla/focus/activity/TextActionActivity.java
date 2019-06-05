package org.mozilla.focus.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import org.mozilla.focus.utils.SafeIntent;
import org.mozilla.focus.utils.SearchUtils;
import org.mozilla.rocket.component.LaunchIntentDispatcher;

public class TextActionActivity extends Activity {
   protected void onCreate(Bundle var1) {
      SafeIntent var5;
      byte var8;
      label28: {
         super.onCreate(var1);
         var5 = new SafeIntent(this.getIntent());
         String var2 = var5.getAction();
         int var3 = var2.hashCode();
         if (var3 != 1703997026) {
            if (var3 == 1937529752 && var2.equals("android.intent.action.WEB_SEARCH")) {
               var8 = 1;
               break label28;
            }
         } else if (var2.equals("android.intent.action.PROCESS_TEXT")) {
            var8 = 0;
            break label28;
         }

         var8 = -1;
      }

      String var6;
      Object var7;
      switch(var8) {
      case 0:
         var7 = var5.getCharSequenceExtra("android.intent.extra.PROCESS_TEXT");
         var6 = LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_TEXT_SELECTION.getValue();
         break;
      case 1:
         var7 = var5.getStringExtra("query");
         var6 = LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_WEB_SEARCH.getValue();
         break;
      default:
         var7 = "";
         var6 = null;
      }

      String var4 = SearchUtils.createSearchUrl(this, ((CharSequence)var7).toString());
      Intent var9 = new Intent();
      var9.setClassName(this, "org.mozilla.rocket.activity.MainActivity");
      var9.setAction("android.intent.action.VIEW");
      if (!TextUtils.isEmpty(var6)) {
         var9.putExtra(var6, true);
      }

      var9.setData(Uri.parse(var4));
      this.startActivity(var9);
      this.finish();
   }
}
