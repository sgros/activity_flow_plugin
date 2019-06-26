package org.telegram.ui.Components;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

public class SlideView extends LinearLayout {
   public SlideView(Context var1) {
      super(var1);
   }

   public String getHeaderName() {
      return "";
   }

   public boolean needBackButton() {
      return false;
   }

   public boolean onBackPressed(boolean var1) {
      return true;
   }

   public void onCancelPressed() {
   }

   public void onDestroyActivity() {
   }

   public void onNextPressed() {
   }

   public void onShow() {
   }

   public void restoreStateParams(Bundle var1) {
   }

   public void saveStateParams(Bundle var1) {
   }

   public void setParams(Bundle var1, boolean var2) {
   }
}
