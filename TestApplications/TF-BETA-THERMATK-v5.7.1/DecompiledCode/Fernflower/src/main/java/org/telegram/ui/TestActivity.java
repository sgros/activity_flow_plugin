package org.telegram.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MediaActionDrawable;

public class TestActivity extends BaseFragment {
   int num = 0;
   int p = 0;

   // $FF: synthetic method
   static void lambda$createView$0(MediaActionDrawable var0, View var1) {
      int var2 = var0.getCurrentIcon();
      boolean var3 = true;
      int var4;
      if (var2 == 2) {
         var4 = 3;
      } else if (var2 == 3) {
         var4 = 0;
      } else {
         var4 = var2;
         if (var2 == 0) {
            var4 = 2;
            var3 = false;
         }
      }

      var0.setIcon(var4, var3);
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle("Test");
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               TestActivity.this.finishFragment();
            }

         }
      });
      FrameLayout var2 = new FrameLayout(var1);
      var2.setBackgroundColor(-16777216);
      super.fragmentView = var2;
      MediaActionDrawable var3 = new MediaActionDrawable();
      var3.setIcon(2, false);
      ImageView var4 = new ImageView(var1);
      var4.setImageDrawable(var3);
      var4.getClass();
      var3.setDelegate(new _$$Lambda$pmzqDjiJ3K2EPQb0Rq1MYHdTzL0(var4));
      var2.addView(var4, LayoutHelper.createFrame(48, 48, 17));
      var2.setOnClickListener(new _$$Lambda$TestActivity$8SA6jL3MHG2fMuwJNTKXrp0KhUw(var3));
      return super.fragmentView;
   }
}
