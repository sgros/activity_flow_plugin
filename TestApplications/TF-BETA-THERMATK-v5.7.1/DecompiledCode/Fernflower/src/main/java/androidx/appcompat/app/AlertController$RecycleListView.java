package androidx.appcompat.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;
import androidx.appcompat.R$styleable;

public class AlertController$RecycleListView extends ListView {
   private final int mPaddingBottomNoButtons;
   private final int mPaddingTopNoTitle;

   public AlertController$RecycleListView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AlertController$RecycleListView(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.RecycleListView);
      this.mPaddingBottomNoButtons = var3.getDimensionPixelOffset(R$styleable.RecycleListView_paddingBottomNoButtons, -1);
      this.mPaddingTopNoTitle = var3.getDimensionPixelOffset(R$styleable.RecycleListView_paddingTopNoTitle, -1);
   }
}
