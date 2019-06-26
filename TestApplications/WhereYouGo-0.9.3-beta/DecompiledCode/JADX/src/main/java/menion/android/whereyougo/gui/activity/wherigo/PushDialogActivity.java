package menion.android.whereyougo.gui.activity.wherigo;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.gui.extension.activity.MediaActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog.OnClickListener;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Logger;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.Media;
import p009se.krka.kahlua.p010vm.LuaClosure;

public class PushDialogActivity extends MediaActivity {
    private static final String TAG = "PushDialog";
    private static LuaClosure callback;
    private static Media[] media;
    private static String menu01Text = null;
    private static String menu02Text = null;
    private static int page = -1;
    private static String[] texts;
    private TextView tvText;

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.PushDialogActivity$1 */
    class C04221 implements OnClickListener {
        C04221() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            PushDialogActivity.this.nextPage();
            return true;
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.wherigo.PushDialogActivity$2 */
    class C04232 implements OnClickListener {
        C04232() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            if (PushDialogActivity.callback != null) {
                Engine.invokeCallback(PushDialogActivity.callback, "Button2");
            }
            PushDialogActivity.callback = null;
            PushDialogActivity.this.finish();
            return true;
        }
    }

    public static void setDialog(String[] texts, Media[] media, String button1, String button2, LuaClosure callback) {
        synchronized (PushDialogActivity.class) {
            texts = texts;
            media = media;
            callback = callback;
            page = -1;
            if (button1 == null) {
                button1 = Locale.getString(C0254R.string.f48ok);
            }
            menu01Text = button1;
            menu02Text = button2;
            Logger.m20d(TAG, "setDialog() - finish, callBack:" + (callback != null));
        }
    }

    private void nextPage() {
        synchronized (PushDialogActivity.class) {
            Logger.m20d(TAG, "nextpage() - page:" + page + ", texts:" + texts.length + ", callback:" + (callback != null));
            page++;
            if (page >= texts.length) {
                if (callback != null) {
                    LuaClosure call = callback;
                    callback = null;
                    Engine.invokeCallback(call, "Button1");
                }
                finish();
                return;
            }
            setMedia(media[page]);
            this.tvText.setText(UtilsGUI.simpleHtml(texts[page]));
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (C0322A.getMain() == null || Engine.instance == null) {
            finish();
            return;
        }
        setContentView(C0254R.layout.layout_details);
        findViewById(C0254R.C0253id.layoutDetailsTextViewName).setVisibility(8);
        findViewById(C0254R.C0253id.layoutDetailsTextViewState).setVisibility(8);
        findViewById(C0254R.C0253id.layoutDetailsTextViewDistance).setVisibility(8);
        this.tvText = (TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewDescription);
        if (menu02Text == null || menu02Text.length() == 0) {
            menu02Text = null;
        }
        CustomDialog.setBottom(this, menu01Text, new C04221(), null, null, menu02Text, new C04232());
        if (page == -1) {
            nextPage();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logger.m20d(TAG, "onKeyDown(" + keyCode + ", " + event + ")");
        return event.getKeyCode() == 4 || super.onKeyDown(keyCode, event);
    }
}
