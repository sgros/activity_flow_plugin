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
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.gui.extension.activity.MediaActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.utils.C0322A;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.EventTable;
import p005cz.matejcik.openwig.Media;
import p009se.krka.kahlua.p010vm.LuaTable;

public class InputScreenActivity extends MediaActivity {
    private static final String TAG = "InputScreen";
    private static EventTable input;
    private InputType inputType = InputType.NONE;

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.InputScreenActivity$1 */
    class C02731 implements OnClickListener {
        C02731() {
        }

        public void onClick(View v) {
            new IntentIntegrator(InputScreenActivity.this).initiateScan();
        }
    }

    private enum InputType {
        NONE,
        TEXT,
        MULTI
    }

    public static void setInput(EventTable input) {
        input = input;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (C0322A.getMain() == null || Engine.instance == null || input == null || input.table == null) {
            finish();
            return;
        }
        setContentView(C0254R.layout.layout_input);
        setMedia((Media) input.table.rawget("Media"));
        ((TextView) findViewById(C0254R.C0253id.layoutInputTextView)).setText(UtilsGUI.simpleHtml((String) input.table.rawget("Text")));
        final EditText editText = (EditText) findViewById(C0254R.C0253id.layoutInputEditText);
        editText.setVisibility(8);
        Button scanButton = (Button) findViewById(C0254R.C0253id.layoutInputScanButton);
        scanButton.setOnClickListener(new C02731());
        scanButton.setVisibility(8);
        Spinner spinner = (Spinner) findViewById(C0254R.C0253id.layoutInputSpinner);
        spinner.setVisibility(8);
        String type = (String) input.table.rawget("InputType");
        if ("Text".equals(type)) {
            editText.setText("");
            editText.setVisibility(0);
            scanButton.setVisibility(0);
            this.inputType = InputType.TEXT;
        } else if ("MultipleChoice".equals(type)) {
            LuaTable choices = (LuaTable) input.table.rawget("Choices");
            String[] data = new String[choices.len()];
            for (int i = 0; i < choices.len(); i++) {
                data[i] = (String) choices.rawget(Double.valueOf((double) (i + 1)));
                if (data[i] == null) {
                    data[i] = "-";
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter(this, 17367048, data);
            adapter.setDropDownViewResource(17367049);
            spinner.setAdapter(adapter);
            spinner.setVisibility(0);
            this.inputType = InputType.MULTI;
        }
        final Spinner spinner2 = spinner;
        CustomDialog.setBottom(this, Locale.getString(C0254R.string.answer), new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog dialog, View v, int btn) {
                if (InputScreenActivity.this.inputType == InputType.TEXT) {
                    Engine.callEvent(InputScreenActivity.input, "OnGetInput", editText.getText().toString());
                } else if (InputScreenActivity.this.inputType == InputType.MULTI) {
                    Engine.callEvent(InputScreenActivity.input, "OnGetInput", String.valueOf(spinner2.getSelectedItem()));
                } else {
                    Engine.callEvent(InputScreenActivity.input, "OnGetInput", null);
                }
                InputScreenActivity.this.finish();
                return true;
            }
        }, null, null, null, null);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() != 4) {
            return super.onKeyDown(keyCode, event);
        }
        Engine.callEvent(input, "OnGetInput", null);
        finish();
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null || result.getContents() == null) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            ((EditText) findViewById(C0254R.C0253id.layoutInputEditText)).setText(result.getContents());
        }
    }
}
