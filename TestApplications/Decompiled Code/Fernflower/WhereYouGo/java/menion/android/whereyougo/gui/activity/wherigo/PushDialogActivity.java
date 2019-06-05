package menion.android.whereyougo.gui.activity.wherigo;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Media;
import menion.android.whereyougo.gui.extension.activity.MediaActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import se.krka.kahlua.vm.LuaClosure;

public class PushDialogActivity extends MediaActivity {
   private static final String TAG = "PushDialog";
   private static LuaClosure callback;
   private static Media[] media;
   private static String menu01Text = null;
   private static String menu02Text = null;
   private static int page = -1;
   private static String[] texts;
   private TextView tvText;

   private void nextPage() {
      synchronized(PushDialogActivity.class){}

      Throwable var10000;
      boolean var10001;
      label405: {
         StringBuilder var1;
         boolean var2;
         label400: {
            label399: {
               try {
                  var1 = new StringBuilder();
                  var1 = var1.append("nextpage() - page:").append(page).append(", texts:").append(texts.length).append(", callback:");
                  if (callback != null) {
                     break label399;
                  }
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label405;
               }

               var2 = false;
               break label400;
            }

            var2 = true;
         }

         label392: {
            label391: {
               try {
                  Logger.d("PushDialog", var1.append(var2).toString());
                  ++page;
                  if (page < texts.length) {
                     break label391;
                  }

                  if (callback != null) {
                     LuaClosure var45 = callback;
                     callback = null;
                     Engine.invokeCallback(var45, "Button1");
                  }
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label405;
               }

               try {
                  this.finish();
                  break label392;
               } catch (Throwable var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label405;
               }
            }

            try {
               this.setMedia(media[page]);
               this.tvText.setText(UtilsGUI.simpleHtml(texts[page]));
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               break label405;
            }
         }

         label382:
         try {
            return;
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            break label382;
         }
      }

      while(true) {
         Throwable var46 = var10000;

         try {
            throw var46;
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            continue;
         }
      }
   }

   public static void setDialog(String[] var0, Media[] var1, String var2, String var3, LuaClosure var4) {
      synchronized(PushDialogActivity.class){}

      Throwable var10000;
      boolean var10001;
      label271: {
         try {
            texts = var0;
            media = var1;
            callback = var4;
            page = -1;
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label271;
         }

         String var36 = var2;
         if (var2 == null) {
            try {
               var36 = Locale.getString(2131165230);
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label271;
            }
         }

         StringBuilder var37;
         try {
            menu01Text = var36;
            menu02Text = var3;
            var37 = new StringBuilder();
            var37 = var37.append("setDialog() - finish, callBack:");
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label271;
         }

         boolean var5;
         if (var4 != null) {
            var5 = true;
         } else {
            var5 = false;
         }

         label256:
         try {
            Logger.d("PushDialog", var37.append(var5).toString());
            return;
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label256;
         }
      }

      while(true) {
         Throwable var38 = var10000;

         try {
            throw var38;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            continue;
         }
      }
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (A.getMain() != null && Engine.instance != null) {
         this.setContentView(2130903052);
         this.findViewById(2131492940).setVisibility(8);
         this.findViewById(2131492941).setVisibility(8);
         this.findViewById(2131492942).setVisibility(8);
         this.tvText = (TextView)this.findViewById(2131492943);
         if (menu02Text == null || menu02Text.length() == 0) {
            menu02Text = null;
         }

         CustomDialog.setBottom(this, menu01Text, new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               PushDialogActivity.this.nextPage();
               return true;
            }
         }, (String)null, (CustomDialog.OnClickListener)null, menu02Text, new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               if (PushDialogActivity.callback != null) {
                  Engine.invokeCallback(PushDialogActivity.callback, "Button2");
               }

               PushDialogActivity.callback = null;
               PushDialogActivity.this.finish();
               return true;
            }
         });
         if (page == -1) {
            this.nextPage();
         }
      } else {
         this.finish();
      }

   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      Logger.d("PushDialog", "onKeyDown(" + var1 + ", " + var2 + ")");
      boolean var3;
      if (var2.getKeyCode() != 4 && !super.onKeyDown(var1, var2)) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }
}
