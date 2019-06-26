package menion.android.whereyougo.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;

public class PreviewPreference extends Preference {
   protected CharSequence defaultValue = "";
   protected String mValue = "";
   protected CharSequence previewTemplate = "";
   protected CharSequence summaryTemplate = "";

   public PreviewPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.summaryTemplate = super.getSummary();

      for(int var3 = 0; var3 < var2.getAttributeCount(); ++var3) {
         String var4 = var2.getAttributeName(var3);
         String var5 = var2.getAttributeValue(var3);
         if (var4.equalsIgnoreCase("previewTemplate")) {
            this.previewTemplate = var5;
         }

         if (var4.equalsIgnoreCase("previewTemplate")) {
            this.previewTemplate = var5;
         }

         if (var4.equalsIgnoreCase("defaultValue")) {
            this.defaultValue = var5;
         }
      }

   }

   public CharSequence getSummary() {
      String var1 = this.previewTemplate.toString();
      if (var1.length() == 0) {
         var1 = "(" + this.mValue + ")";
      } else {
         var1 = var1.replace("%1$", this.mValue);
      }

      return var1 + " " + this.summaryTemplate;
   }

   protected Object onGetDefaultValue(TypedArray var1, int var2) {
      return var1.getString(var2);
   }

   protected void onSetInitialValue(boolean var1, Object var2) {
      String var3;
      if (var1) {
         var3 = this.getPersistedString((String)var2);
      } else {
         var3 = (String)var2;
      }

      this.setValue(var3);
   }

   public void setValue(String var1) {
      this.mValue = var1;
      this.persistString(this.mValue);
      this.notifyChanged();
   }
}
