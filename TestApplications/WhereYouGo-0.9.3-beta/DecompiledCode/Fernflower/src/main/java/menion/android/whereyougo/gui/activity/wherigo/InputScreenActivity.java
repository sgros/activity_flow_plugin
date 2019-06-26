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
         final EditText var2 = (EditText)this.findViewById(2131492958);
         var2.setVisibility(8);
         Button var3 = (Button)this.findViewById(2131492959);
         var3.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               (new IntentIntegrator(InputScreenActivity.this)).initiateScan();
            }
         });
         var3.setVisibility(8);
         final Spinner var6 = (Spinner)this.findViewById(2131492960);
         var6.setVisibility(8);
         String var4 = (String)input.table.rawget("InputType");
         if ("Text".equals(var4)) {
            var2.setText("");
            var2.setVisibility(0);
            var3.setVisibility(0);
            this.inputType = InputScreenActivity.InputType.TEXT;
         } else if ("MultipleChoice".equals(var4)) {
            LuaTable var7 = (LuaTable)input.table.rawget("Choices");
            String[] var9 = new String[var7.len()];

            for(int var5 = 0; var5 < var7.len(); ++var5) {
               var9[var5] = (String)var7.rawget((double)(var5 + 1));
               if (var9[var5] == null) {
                  var9[var5] = "-";
               }
            }

            ArrayAdapter var8 = new ArrayAdapter(this, 17367048, var9);
            var8.setDropDownViewResource(17367049);
            var6.setAdapter(var8);
            var6.setVisibility(0);
            this.inputType = InputScreenActivity.InputType.MULTI;
         }

         CustomDialog.setBottom(this, Locale.getString(2131165187), new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2x, int var3) {
               if (InputScreenActivity.this.inputType == InputScreenActivity.InputType.TEXT) {
                  Engine.callEvent(InputScreenActivity.input, "OnGetInput", var2.getText().toString());
               } else if (InputScreenActivity.this.inputType == InputScreenActivity.InputType.MULTI) {
                  String var4 = String.valueOf(var6.getSelectedItem());
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