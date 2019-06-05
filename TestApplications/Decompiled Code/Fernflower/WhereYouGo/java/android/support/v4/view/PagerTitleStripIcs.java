package android.support.v4.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;

@TargetApi(14)
@RequiresApi(14)
class PagerTitleStripIcs {
   public static void setSingleLineAllCaps(TextView var0) {
      var0.setTransformationMethod(new PagerTitleStripIcs.SingleLineAllCapsTransform(var0.getContext()));
   }

   private static class SingleLineAllCapsTransform extends SingleLineTransformationMethod {
      private static final String TAG = "SingleLineAllCapsTransform";
      private Locale mLocale;

      public SingleLineAllCapsTransform(Context var1) {
         this.mLocale = var1.getResources().getConfiguration().locale;
      }

      public CharSequence getTransformation(CharSequence var1, View var2) {
         var1 = super.getTransformation(var1, var2);
         String var3;
         if (var1 != null) {
            var3 = var1.toString().toUpperCase(this.mLocale);
         } else {
            var3 = null;
         }

         return var3;
      }
   }
}
