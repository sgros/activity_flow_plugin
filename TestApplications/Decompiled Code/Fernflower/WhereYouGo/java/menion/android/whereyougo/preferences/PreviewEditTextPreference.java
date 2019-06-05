package menion.android.whereyougo.preferences;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class PreviewEditTextPreference extends EditTextPreference {
   protected CharSequence previewTemplate = "";
   protected CharSequence summaryTemplate = "";

   public PreviewEditTextPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.summaryTemplate = super.getSummary();

      for(int var3 = 0; var3 < var2.getAttributeCount(); ++var3) {
         String var5 = var2.getAttributeName(var3);
         String var4 = var2.getAttributeValue(var3);
         if (var5.equalsIgnoreCase("previewTemplate")) {
            this.previewTemplate = var4;
         }
      }

   }

   public CharSequence getSummary() {
      String var1 = this.previewTemplate.toString();
      String var2 = this.getText();
      if (var1.length() == 0) {
         var1 = "(" + var2 + ")";
      } else {
         var1 = var1.replace("%1$", var2);
      }

      return var1 + " " + this.summaryTemplate;
   }

   protected void onDialogClosed(boolean var1) {
      super.onDialogClosed(var1);
      this.setSummary(this.getSummary());
   }
}
