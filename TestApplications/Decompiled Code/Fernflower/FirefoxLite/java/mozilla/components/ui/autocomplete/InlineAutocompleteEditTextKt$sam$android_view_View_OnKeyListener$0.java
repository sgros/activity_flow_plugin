package mozilla.components.ui.autocomplete;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

final class InlineAutocompleteEditTextKt$sam$android_view_View_OnKeyListener$0 implements OnKeyListener {
   // $FF: synthetic field
   private final Function3 function;

   InlineAutocompleteEditTextKt$sam$android_view_View_OnKeyListener$0(Function3 var1) {
      this.function = var1;
   }

   // $FF: synthetic method
   public final boolean onKey(View var1, int var2, KeyEvent var3) {
      Object var4 = this.function.invoke(var1, var2, var3);
      Intrinsics.checkExpressionValueIsNotNull(var4, "invoke(...)");
      return (Boolean)var4;
   }
}
