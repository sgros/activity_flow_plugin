package android.support.design.widget;

import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class TextInputEditText extends AppCompatEditText {
   private CharSequence getHintFromLayout() {
      TextInputLayout var1 = this.getTextInputLayout();
      CharSequence var2;
      if (var1 != null) {
         var2 = var1.getHint();
      } else {
         var2 = null;
      }

      return var2;
   }

   private TextInputLayout getTextInputLayout() {
      for(ViewParent var1 = this.getParent(); var1 instanceof View; var1 = var1.getParent()) {
         if (var1 instanceof TextInputLayout) {
            return (TextInputLayout)var1;
         }
      }

      return null;
   }

   public CharSequence getHint() {
      TextInputLayout var1 = this.getTextInputLayout();
      return var1 != null && var1.isProvidingHint() ? var1.getHint() : super.getHint();
   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      InputConnection var2 = super.onCreateInputConnection(var1);
      if (var2 != null && var1.hintText == null) {
         var1.hintText = this.getHintFromLayout();
      }

      return var2;
   }
}
