package androidx.appcompat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ActivityChooserView$InnerLayout extends LinearLayout {
   private static final int[] TINT_ATTRS = new int[]{16842964};

   public ActivityChooserView$InnerLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      TintTypedArray var3 = TintTypedArray.obtainStyledAttributes(var1, var2, TINT_ATTRS);
      this.setBackgroundDrawable(var3.getDrawable(0));
      var3.recycle();
   }
}
