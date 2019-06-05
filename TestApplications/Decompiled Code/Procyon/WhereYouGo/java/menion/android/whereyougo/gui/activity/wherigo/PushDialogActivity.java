// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity.wherigo;

import android.view.KeyEvent;
import android.app.Activity;
import android.view.View;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.utils.A;
import android.os.Bundle;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import cz.matejcik.openwig.Engine;
import menion.android.whereyougo.utils.Logger;
import android.widget.TextView;
import cz.matejcik.openwig.Media;
import se.krka.kahlua.vm.LuaClosure;
import menion.android.whereyougo.gui.extension.activity.MediaActivity;

public class PushDialogActivity extends MediaActivity
{
    private static final String TAG = "PushDialog";
    private static LuaClosure callback;
    private static Media[] media;
    private static String menu01Text;
    private static String menu02Text;
    private static int page;
    private static String[] texts;
    private TextView tvText;
    
    static {
        PushDialogActivity.menu01Text = null;
        PushDialogActivity.menu02Text = null;
        PushDialogActivity.page = -1;
    }
    
    private void nextPage() {
        synchronized (PushDialogActivity.class) {
            Logger.d("PushDialog", "nextpage() - page:" + PushDialogActivity.page + ", texts:" + PushDialogActivity.texts.length + ", callback:" + (PushDialogActivity.callback != null));
            ++PushDialogActivity.page;
            if (PushDialogActivity.page >= PushDialogActivity.texts.length) {
                if (PushDialogActivity.callback != null) {
                    final LuaClosure callback = PushDialogActivity.callback;
                    PushDialogActivity.callback = null;
                    Engine.invokeCallback(callback, "Button1");
                }
                this.finish();
            }
            else {
                this.setMedia(PushDialogActivity.media[PushDialogActivity.page]);
                this.tvText.setText(UtilsGUI.simpleHtml(PushDialogActivity.texts[PushDialogActivity.page]));
            }
            // monitorexit(PushDialogActivity.class)
        }
    }
    
    public static void setDialog(final String[] texts, final Media[] media, final String s, final String menu02Text, final LuaClosure callback) {
        synchronized (PushDialogActivity.class) {
            PushDialogActivity.texts = texts;
            PushDialogActivity.media = media;
            PushDialogActivity.callback = callback;
            PushDialogActivity.page = -1;
            String string = s;
            if (s == null) {
                string = Locale.getString(2131165230);
            }
            PushDialogActivity.menu01Text = string;
            PushDialogActivity.menu02Text = menu02Text;
            Logger.d("PushDialog", "setDialog() - finish, callBack:" + (callback != null));
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (A.getMain() == null || Engine.instance == null) {
            this.finish();
        }
        else {
            this.setContentView(2130903052);
            this.findViewById(2131492940).setVisibility(8);
            this.findViewById(2131492941).setVisibility(8);
            this.findViewById(2131492942).setVisibility(8);
            this.tvText = (TextView)this.findViewById(2131492943);
            if (PushDialogActivity.menu02Text == null || PushDialogActivity.menu02Text.length() == 0) {
                PushDialogActivity.menu02Text = null;
            }
            CustomDialog.setBottom(this, PushDialogActivity.menu01Text, (CustomDialog.OnClickListener)new CustomDialog.OnClickListener() {
                @Override
                public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                    PushDialogActivity.this.nextPage();
                    return true;
                }
            }, null, null, PushDialogActivity.menu02Text, (CustomDialog.OnClickListener)new CustomDialog.OnClickListener() {
                @Override
                public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                    if (PushDialogActivity.callback != null) {
                        Engine.invokeCallback(PushDialogActivity.callback, "Button2");
                    }
                    PushDialogActivity.callback = null;
                    PushDialogActivity.this.finish();
                    return true;
                }
            });
            if (PushDialogActivity.page == -1) {
                this.nextPage();
            }
        }
    }
    
    public boolean onKeyDown(final int i, final KeyEvent obj) {
        Logger.d("PushDialog", "onKeyDown(" + i + ", " + obj + ")");
        return obj.getKeyCode() == 4 || super.onKeyDown(i, obj);
    }
}
