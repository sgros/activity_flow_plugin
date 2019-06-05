package android.support.design.widget;

import android.widget.ImageButton;

public class VisibilityAwareImageButton extends ImageButton {
   private int userSetVisibility;

   public final int getUserSetVisibility() {
      return this.userSetVisibility;
   }

   public final void internalSetVisibility(int var1, boolean var2) {
      super.setVisibility(var1);
      if (var2) {
         this.userSetVisibility = var1;
      }

   }

   public void setVisibility(int var1) {
      this.internalSetVisibility(var1, true);
   }
}
