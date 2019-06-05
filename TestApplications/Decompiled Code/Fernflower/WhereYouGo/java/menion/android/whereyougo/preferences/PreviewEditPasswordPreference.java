package menion.android.whereyougo.preferences;

import android.content.Context;
import android.util.AttributeSet;

public class PreviewEditPasswordPreference extends PreviewEditTextPreference {
   protected CharSequence previewTemplate = "";
   protected CharSequence summaryTemplate = "";

   public PreviewEditPasswordPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public CharSequence getSummary() {
      String var1 = this.previewTemplate.toString();
      String var2 = this.getText();
      String var3 = "";

      for(int var4 = 0; var4 < var2.length(); ++var4) {
         var3 = var3 + "•";
      }

      if (var1.length() == 0) {
         var3 = "(" + var3 + ")";
      } else {
         var3 = var1.replace("%1$", var3);
      }

      return var3 + " " + this.summaryTemplate;
   }
}
