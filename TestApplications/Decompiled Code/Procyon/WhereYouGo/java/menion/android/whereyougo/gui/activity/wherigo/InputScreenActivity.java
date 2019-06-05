// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity.wherigo;

import android.view.KeyEvent;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.preferences.Locale;
import android.widget.SpinnerAdapter;
import android.content.Context;
import android.widget.ArrayAdapter;
import se.krka.kahlua.vm.LuaTable;
import android.widget.Spinner;
import android.app.Activity;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.Button;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import android.widget.TextView;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.Engine;
import menion.android.whereyougo.utils.A;
import android.os.Bundle;
import com.google.zxing.integration.android.IntentResult;
import android.widget.EditText;
import com.google.zxing.integration.android.IntentIntegrator;
import android.content.Intent;
import cz.matejcik.openwig.EventTable;
import menion.android.whereyougo.gui.extension.activity.MediaActivity;

public class InputScreenActivity extends MediaActivity
{
    private static final String TAG = "InputScreen";
    private static EventTable input;
    private InputType inputType;
    
    public InputScreenActivity() {
        this.inputType = InputType.NONE;
    }
    
    public static void setInput(final EventTable input) {
        InputScreenActivity.input = input;
    }
    
    @Override
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        final IntentResult activityResult = IntentIntegrator.parseActivityResult(n, n2, intent);
        if (activityResult != null && activityResult.getContents() != null) {
            ((EditText)this.findViewById(2131492958)).setText((CharSequence)activityResult.getContents());
        }
        else {
            super.onActivityResult(n, n2, intent);
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (A.getMain() == null || Engine.instance == null || InputScreenActivity.input == null || InputScreenActivity.input.table == null) {
            this.finish();
        }
        else {
            this.setContentView(2130903054);
            this.setMedia((Media)InputScreenActivity.input.table.rawget("Media"));
            ((TextView)this.findViewById(2131492957)).setText(UtilsGUI.simpleHtml((String)InputScreenActivity.input.table.rawget("Text")));
            final EditText editText = (EditText)this.findViewById(2131492958);
            editText.setVisibility(8);
            final Button button = (Button)this.findViewById(2131492959);
            button.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    new IntentIntegrator(InputScreenActivity.this).initiateScan();
                }
            });
            button.setVisibility(8);
            final Spinner spinner = (Spinner)this.findViewById(2131492960);
            spinner.setVisibility(8);
            final String s = (String)InputScreenActivity.input.table.rawget("InputType");
            if ("Text".equals(s)) {
                editText.setText((CharSequence)"");
                editText.setVisibility(0);
                button.setVisibility(0);
                this.inputType = InputType.TEXT;
            }
            else if ("MultipleChoice".equals(s)) {
                final LuaTable luaTable = (LuaTable)InputScreenActivity.input.table.rawget("Choices");
                final String[] array = new String[luaTable.len()];
                for (int i = 0; i < luaTable.len(); ++i) {
                    array[i] = (String)luaTable.rawget((double)(i + 1));
                    if (array[i] == null) {
                        array[i] = "-";
                    }
                }
                final ArrayAdapter adapter = new ArrayAdapter((Context)this, 17367048, (Object[])array);
                adapter.setDropDownViewResource(17367049);
                spinner.setAdapter((SpinnerAdapter)adapter);
                spinner.setVisibility(0);
                this.inputType = InputType.MULTI;
            }
            CustomDialog.setBottom(this, Locale.getString(2131165187), (CustomDialog.OnClickListener)new CustomDialog.OnClickListener() {
                @Override
                public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                    if (InputScreenActivity.this.inputType == InputType.TEXT) {
                        Engine.callEvent(InputScreenActivity.input, "OnGetInput", editText.getText().toString());
                    }
                    else if (InputScreenActivity.this.inputType == InputType.MULTI) {
                        Engine.callEvent(InputScreenActivity.input, "OnGetInput", String.valueOf(spinner.getSelectedItem()));
                    }
                    else {
                        Engine.callEvent(InputScreenActivity.input, "OnGetInput", null);
                    }
                    InputScreenActivity.this.finish();
                    return true;
                }
            }, null, null, null, null);
        }
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        boolean onKeyDown;
        if (keyEvent.getKeyCode() == 4) {
            Engine.callEvent(InputScreenActivity.input, "OnGetInput", null);
            this.finish();
            onKeyDown = true;
        }
        else {
            onKeyDown = super.onKeyDown(n, keyEvent);
        }
        return onKeyDown;
    }
    
    private enum InputType
    {
        MULTI, 
        NONE, 
        TEXT;
    }
}
