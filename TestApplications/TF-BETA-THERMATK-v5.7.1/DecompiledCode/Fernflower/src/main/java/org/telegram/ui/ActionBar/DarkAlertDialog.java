package org.telegram.ui.ActionBar;

import android.content.Context;

public class DarkAlertDialog extends AlertDialog {
   public DarkAlertDialog(Context var1, int var2) {
      super(var1, var2);
   }

   protected int getThemeColor(String var1) {
      byte var2;
      label36: {
         switch(var1.hashCode()) {
         case -1849805674:
            if (var1.equals("dialogBackground")) {
               var2 = 0;
               break label36;
            }
            break;
         case -451706526:
            if (var1.equals("dialogScrollGlow")) {
               var2 = 3;
               break label36;
            }
            break;
         case -93324646:
            if (var1.equals("dialogButton")) {
               var2 = 2;
               break label36;
            }
            break;
         case 1828201066:
            if (var1.equals("dialogTextBlack")) {
               var2 = 1;
               break label36;
            }
         }

         var2 = -1;
      }

      if (var2 != 0) {
         return var2 != 1 && var2 != 2 && var2 != 3 ? super.getThemeColor(var1) : -1;
      } else {
         return -14277082;
      }
   }

   public static class Builder extends AlertDialog.Builder {
      public Builder(Context var1) {
         super((AlertDialog)(new DarkAlertDialog(var1, 0)));
      }

      public Builder(Context var1, int var2) {
         super((AlertDialog)(new DarkAlertDialog(var1, var2)));
      }
   }
}
