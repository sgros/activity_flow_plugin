package menion.android.whereyougo.gui.extension.dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.utils.Utils;

public class CustomDialog {
   public static final int BOTTOM_COLOR_A3 = -2236963;
   public static final int NO_IMAGE = Integer.MIN_VALUE;
   private static final int TITLE_BUTTON_LEFT = 2;
   private static final int TITLE_BUTTON_RIGHT = 1;

   private static void addViewToContent(View var0, LayoutParams var1, View var2) {
      LinearLayout var3 = (LinearLayout)var0;
      var3.removeAllViews();
      if (var1 == null) {
         var3.addView(var2, new LayoutParams(-1, -2));
      } else {
         var3.addView(var2, var1);
      }

   }

   public static void setBottom(Activity var0, String var1, CustomDialog.OnClickListener var2, String var3, CustomDialog.OnClickListener var4, String var5, CustomDialog.OnClickListener var6) {
      setCustomDialogBottom(var0.findViewById(2131492930), var1, var2, var3, var4, var5, var6);
   }

   private static boolean setButton(View var0, int var1, final int var2, String var3, final CustomDialog.OnClickListener var4) {
      boolean var5 = false;
      if (var3 != null && var4 != null) {
         Button var6 = (Button)var0.findViewById(var1);
         var6.setText(var3);
         var6.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View var1) {
               var4.onClick((CustomDialog)null, var1, var2);
            }
         });
         var6.setVisibility(0);
         var5 = true;
      } else {
         var0.findViewById(var1).setVisibility(8);
      }

      return var5;
   }

   public static void setContent(Activity var0, View var1, int var2, boolean var3, boolean var4) {
      byte var5 = -2;
      if (var4) {
         UtilsGUI.setWindowDialogCorrectWidth(var0.getWindow());
      }

      byte var6;
      if (var3) {
         var6 = -1;
      } else {
         var6 = -2;
      }

      LayoutParams var7 = new LayoutParams(-1, var6);
      if (var2 > 0) {
         var7.setMargins(var2, var0.getResources().getDimensionPixelSize(2131427330) + var2, var2, var2);
      }

      LinearLayout var8 = (LinearLayout)var0.findViewById(2131492894);
      byte var9 = var5;
      if (var3) {
         var9 = -1;
      }

      var8.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(-1, var9));
      addViewToContent(var8, var7, var1);
   }

   private static void setCustomDialogBottom(View var0, String var1, CustomDialog.OnClickListener var2, String var3, CustomDialog.OnClickListener var4, String var5, CustomDialog.OnClickListener var6) {
      if (Utils.isAndroid30OrMore()) {
         var0.findViewById(2131492930).setBackgroundColor(-2236963);
      }

      int var7 = 0;
      if (setButton(var0, 2131492932, -1, var1, var2)) {
         var7 = 0 + 1;
      }

      int var8 = var7;
      if (setButton(var0, 2131492934, -2, var5, var6)) {
         var8 = var7 + 1;
      }

      var7 = var8;
      if (setButton(var0, 2131492933, -3, var3, var4)) {
         var7 = var8 + 1;
      }

      if (var7 == 0) {
         var0.findViewById(2131492930).setVisibility(8);
      } else if (var7 == 1) {
         var0.findViewById(2131492930).setVisibility(0);
         var0.findViewById(2131492931).setVisibility(0);
         var0.findViewById(2131492935).setVisibility(0);
      } else {
         var0.findViewById(2131492930).setVisibility(0);
         var0.findViewById(2131492931).setVisibility(8);
         var0.findViewById(2131492935).setVisibility(8);
      }

   }

   private static void setCustomDialogTitle(View var0, CharSequence var1, Bitmap var2, int var3, CustomDialog.OnClickListener var4, int var5, CustomDialog.OnClickListener var6) {
      if (var2 == null && var1 == null && var3 == Integer.MIN_VALUE && var5 == Integer.MIN_VALUE) {
         var0.findViewById(2131492887).setVisibility(8);
      } else {
         if (var2 == null) {
            var0.findViewById(2131492888).setVisibility(4);
         } else {
            ((ImageView)var0.findViewById(2131492888)).setImageBitmap(var2);
         }

         ((TextView)var0.findViewById(2131492889)).setText(var1);
         setCustomDialogTitleButton(var0, 1, var3, var4);
         setCustomDialogTitleButton(var0, 2, var5, var6);
      }

   }

   private static void setCustomDialogTitleButton(View var0, int var1, int var2, final CustomDialog.OnClickListener var3) {
      if (var2 != Integer.MIN_VALUE && var3 != null) {
         ImageView var6;
         ImageButton var7;
         if (var1 == 1) {
            ImageView var4 = (ImageView)var0.findViewById(2131492892);
            ImageButton var5 = (ImageButton)var0.findViewById(2131492893);
            var6 = var4;
            var7 = var5;
         } else {
            ImageView var8 = (ImageView)var0.findViewById(2131492890);
            var7 = (ImageButton)var0.findViewById(2131492891);
            var6 = var8;
         }

         var6.setVisibility(0);
         var7.setVisibility(0);
         var7.setImageResource(var2);
         var7.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View var1) {
               var3.onClick((CustomDialog)null, var1, 0);
            }
         });
      }

   }

   public static void setTitle(Activity var0, CharSequence var1, Bitmap var2, int var3, CustomDialog.OnClickListener var4) {
      setCustomDialogTitle(var0.findViewById(2131492886), var1, var2, var3, var4, Integer.MIN_VALUE, (CustomDialog.OnClickListener)null);
   }

   public interface OnClickListener {
      boolean onClick(CustomDialog var1, View var2, int var3);
   }
}
