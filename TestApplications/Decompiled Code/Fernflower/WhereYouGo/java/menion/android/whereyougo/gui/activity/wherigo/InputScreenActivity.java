package menion.android.whereyougo.gui.activity.wherigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Media;
import menion.android.whereyougo.gui.extension.activity.MediaActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.utils.A;
import se.krka.kahlua.vm.LuaTable;

public class InputScreenActivity extends MediaActivity {
   private static final String TAG = "InputScreen";
   private static EventTable input;
   private InputScreenActivity.InputType inputType;

   public InputScreenActivity() {
      this.inputType = InputScreenActivity.InputType.NONE;
   }

   public static void setInput(EventTable var0) {
      input = var0;
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      IntentResult var4 = IntentIntegrator.parseActivityResult(var1, var2, var3);
      if (var4 != null && var4.getContents() != null) {
         ((EditText)this.findViewById(2131492958)).setText(var4.getContents());
      } else {
         super.onActivityResult(var1, var2, var3);
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (A.getMain() != null && Engine.instance != null && input != null && input.table != null) {
         this.setContentView(2130903054);
         this.setMedia((Media)input.table.rawget("Media"));
         ((TextView)this.findViewById(2131492957)).setText(UtilsGUI.simpleHtml((String)input.table.rawget("Text")));
         final EditText var6 = (EditText)this.findViewById(2131492958);
         var6.setVisibility(8);
         Button var2 = (Button)this.findViewById(2131492959);
         var2.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               (new IntentIntegrator(InputScreenActivity.this)).initiateScan();
            }
         });
         var2.setVisibility(8);
         final Spinner var3 = (Spinner)this.findViewById(2131492960);
         var3.setVisibility(8);
         String var4 = (String)input.table.rawget("InputType");
         if ("Text".equals(var4)) {
            var6.setText("");
            var6.setVisibility(0);
            var2.setVisibility(0);
            this.inputType = InputScreenActivity.InputType.TEXT;
         } else if ("MultipleChoice".equals(var4)) {
            LuaTable var8 = (LuaTable)input.table.rawget("Choices");
            String[] var7 = new String[var8.len()];

            for(int var5 = 0; var5 < var8.len(); ++var5) {
               var7[var5] = (String)var8.rawget((double)(var5 + 1));
               if (var7[var5] == null) {
                  var7[var5] = "-";
               }
            }

            ArrayAdapter var9 = new ArrayAdapter(this, 17367048, var7);
            var9.setDropDownViewResource(17367049);
            var3.setAdapter(var9);
            var3.setVisibility(0);
            this.inputType = InputScreenActivity.InputType.MULTI;
         }

         CustomDialog.setBottom(this, Locale.getString(2131165187), new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3x) {
               if (InputScreenActivity.this.inputType == InputScreenActivity.InputType.TEXT) {
                  Engine.callEvent(InputScreenActivity.input, "OnGetInput", var6.getText().toString());
               } else if (InputScreenActivity.this.inputType == InputScreenActivity.InputType.MULTI) {
                  String var4 = String.valueOf(var3.getSelectedItem());
                  Engine.callEvent(InputScreenActivity.input, "OnGetInput", var4);
               } else {
                  Engine.callEvent(InputScreenActivity.input, "OnGetInput", (Object)null);
               }

               InputScreenActivity.this.finish();
               return true;
            }
         }, (String)null, (CustomDialog.OnClickListener)null, (String)null, (CustomDialog.OnClickListener)null);
      } else {
         this.finish();
      }

   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3;
      if (var2.getKeyCode() == 4) {
         Engine.callEvent(input, "OnGetInput", (Object)null);
         this.finish();
         var3 = true;
      } else {
         var3 = super.onKeyDown(var1, var2);
      }

      return var3;
   }

   private static enum InputType {
      MULTI,
      NONE,
      TEXT;
   }
}
