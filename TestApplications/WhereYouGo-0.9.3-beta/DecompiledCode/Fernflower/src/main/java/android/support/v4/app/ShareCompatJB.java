package android.support.v4.app;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.text.Html;

@TargetApi(16)
@RequiresApi(16)
class ShareCompatJB {
   public static String escapeHtml(CharSequence var0) {
      return Html.escapeHtml(var0);
   }
}
