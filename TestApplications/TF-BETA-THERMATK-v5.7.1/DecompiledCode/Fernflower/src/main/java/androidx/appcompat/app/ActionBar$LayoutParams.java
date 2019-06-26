package androidx.appcompat.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.appcompat.R$styleable;

public class ActionBar$LayoutParams extends MarginLayoutParams {
   public int gravity = 0;

   public ActionBar$LayoutParams(int var1, int var2) {
      super(var1, var2);
      this.gravity = 8388627;
   }

   public ActionBar$LayoutParams(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.ActionBarLayout);
      this.gravity = var3.getInt(R$styleable.ActionBarLayout_android_layout_gravity, 0);
      var3.recycle();
   }

   public ActionBar$LayoutParams(LayoutParams var1) {
      super(var1);
   }

   public ActionBar$LayoutParams(ActionBar$LayoutParams var1) {
      super(var1);
      this.gravity = var1.gravity;
   }
}
