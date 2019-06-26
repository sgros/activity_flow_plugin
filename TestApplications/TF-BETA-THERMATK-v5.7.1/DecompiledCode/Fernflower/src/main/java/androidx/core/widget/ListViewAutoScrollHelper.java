package androidx.core.widget;

import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper {
   private final ListView mTarget;

   public ListViewAutoScrollHelper(ListView var1) {
      super(var1);
      this.mTarget = var1;
   }

   public boolean canTargetScrollHorizontally(int var1) {
      return false;
   }

   public boolean canTargetScrollVertically(int var1) {
      ListView var2 = this.mTarget;
      int var3 = var2.getCount();
      if (var3 == 0) {
         return false;
      } else {
         int var4 = var2.getChildCount();
         int var5 = var2.getFirstVisiblePosition();
         if (var1 > 0) {
            if (var5 + var4 >= var3 && var2.getChildAt(var4 - 1).getBottom() <= var2.getHeight()) {
               return false;
            }
         } else {
            if (var1 >= 0) {
               return false;
            }

            if (var5 <= 0 && var2.getChildAt(0).getTop() >= 0) {
               return false;
            }
         }

         return true;
      }
   }

   public void scrollTargetBy(int var1, int var2) {
      ListViewCompat.scrollListBy(this.mTarget, var2);
   }
}
