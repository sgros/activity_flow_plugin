package menion.android.whereyougo.gui.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ListView;
import java.util.ArrayList;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Utils;
import p005cz.matejcik.openwig.Engine;

public class UtilsGUI {
    public static final int DIALOG_EDIT_TEXT_ID = 10005;
    public static final int DIALOG_SPINNER_ID = 10006;

    public static ListView createListView(Context context, boolean setMultiple, ArrayList<DataInfo> adapterData) {
        ListView lv = new ListView(context);
        setListView(context, lv, setMultiple, adapterData);
        return lv;
    }

    public static void dialogDoItem(Activity activity, CharSequence title, int icon, CharSequence msg, String posText, OnClickListener posLis, String negText, OnClickListener negLis) {
        final Activity activity2 = activity;
        final CharSequence charSequence = title;
        final int i = icon;
        final CharSequence charSequence2 = msg;
        final String str = posText;
        final OnClickListener onClickListener = posLis;
        final String str2 = negText;
        final OnClickListener onClickListener2 = negLis;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (!activity2.isFinishing()) {
                    Builder b = new Builder(activity2);
                    b.setCancelable(false);
                    b.setTitle(charSequence);
                    b.setIcon(i);
                    b.setMessage(charSequence2);
                    if (!TextUtils.isEmpty(str)) {
                        b.setPositiveButton(str, onClickListener);
                    }
                    if (!TextUtils.isEmpty(str2)) {
                        b.setNegativeButton(str2, onClickListener2);
                    }
                    if (!activity2.isFinishing()) {
                        b.show();
                    }
                }
            }
        });
    }

    public static void dialogDoItem(Activity activity, CharSequence title, int icon, CharSequence msg, String posText, OnClickListener posLis, String negText, OnClickListener negLis, String cancelText, OnClickListener cancelLis) {
        final Activity activity2 = activity;
        final CharSequence charSequence = title;
        final int i = icon;
        final CharSequence charSequence2 = msg;
        final String str = posText;
        final OnClickListener onClickListener = posLis;
        final String str2 = negText;
        final OnClickListener onClickListener2 = negLis;
        final String str3 = cancelText;
        final OnClickListener onClickListener3 = cancelLis;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (!activity2.isFinishing()) {
                    Builder b = new Builder(activity2);
                    b.setCancelable(true);
                    b.setTitle(charSequence);
                    b.setIcon(i);
                    b.setMessage(charSequence2);
                    if (!TextUtils.isEmpty(str)) {
                        b.setPositiveButton(str, onClickListener);
                    }
                    if (!TextUtils.isEmpty(str2)) {
                        b.setNegativeButton(str2, onClickListener2);
                    }
                    if (!TextUtils.isEmpty(str3)) {
                        b.setNeutralButton(str3, onClickListener3);
                    }
                    if (!activity2.isFinishing()) {
                        b.show();
                    }
                }
            }
        });
    }

    public static int getDialogWidth() {
        return -1;
    }

    public static WebView getFilledWebView(Activity activity, String data) {
        WebView webView = new WebView(activity);
        try {
            webView.loadDataWithBaseURL(null, data.replaceAll("\\+", " "), "text/html", "utf-8", null);
        } catch (Exception e) {
        }
        webView.setLayoutParams(new LayoutParams(getDialogWidth(), -2));
        webView.setBackgroundColor(-1);
        return webView;
    }

    public static int getUniqueId() {
        return (int) (Math.random() * 2.147483647E9d);
    }

    public static void setButtons(Activity a, int[] btns, View.OnClickListener onClick, OnLongClickListener onLongClick) {
        for (int btn : btns) {
            if (onClick != null) {
                a.findViewById(btn).setOnClickListener(onClick);
            }
            if (onLongClick != null) {
                a.findViewById(btn).setOnLongClickListener(onLongClick);
            }
        }
    }

    public static void setListView(Context context, ListView lv, boolean setMultiple, ArrayList<DataInfo> adapterData) {
        if (setMultiple) {
            lv.setChoiceMode(2);
        }
        IconedListAdapter adapter = new IconedListAdapter(context, adapterData, lv);
        adapter.setTextView02Visible(0, true);
        if (adapterData.size() > 50) {
            lv.setFastScrollEnabled(true);
        }
        lv.setAdapter(adapter);
    }

    public static void setWindowDialogCorrectWidth(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = getDialogWidth();
        window.setAttributes(params);
    }

    public static void setWindowFloatingRight(Activity activity) {
        int height = Math.min(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.width = getDialogWidth();
        params.height = height;
        params.x = (int) (((float) (Const.SCREEN_WIDTH - params.width)) - Utils.getDpPixels(10.0f));
        activity.getWindow().setAttributes(params);
    }

    public static void showDialogDeleteItem(Activity activity, String itemName, OnClickListener posLis) {
        CharSequence string;
        String string2 = activity.getString(C0254R.string.question);
        if (itemName != null) {
            string = activity.getString(C0254R.string.do_you_really_want_to_delete_x, new Object[]{Html.fromHtml(itemName)});
        } else {
            string = activity.getText(C0254R.string.do_you_really_want_to_delete_selected_items);
        }
        dialogDoItem(activity, string2, C0254R.C0252drawable.ic_question_default, string, activity.getString(C0254R.string.yes), posLis, activity.getString(C0254R.string.f47no), null);
    }

    public static void showDialogError(Activity activity, CharSequence msg) {
        showDialogError(activity, msg, null);
    }

    public static void showDialogError(Activity activity, CharSequence msg, OnClickListener cancelList) {
        dialogDoItem(activity, activity.getText(C0254R.string.error), C0254R.C0252drawable.ic_info_default, msg, null, null, activity.getString(C0254R.string.close), cancelList);
    }

    public static void showDialogError(Activity activity, int msg, OnClickListener cancelList) {
        showDialogError(activity, activity.getText(msg), cancelList);
    }

    public static void showDialogInfo(Activity activity, CharSequence msg) {
        dialogDoItem(activity, activity.getText(C0254R.string.info), C0254R.C0252drawable.ic_warning_default, msg, null, null, activity.getString(C0254R.string.close), null);
    }

    public static void showDialogInfo(Activity activity, int msg) {
        showDialogInfo(activity, activity.getText(msg));
    }

    public static void showDialogInfo(Activity activity, int msg, OnClickListener cancelList) {
        dialogDoItem(activity, activity.getText(C0254R.string.info), C0254R.C0252drawable.ic_warning_default, activity.getText(msg), null, null, activity.getString(C0254R.string.close), cancelList);
    }

    public static void showDialogQuestion(Activity activity, CharSequence msg, OnClickListener posLis) {
        showDialogQuestion(activity, msg, posLis, null);
    }

    public static void showDialogQuestion(Activity activity, CharSequence msg, OnClickListener posLis, OnClickListener negLis) {
        dialogDoItem(activity, activity.getText(C0254R.string.question), C0254R.C0252drawable.var_empty, msg, activity.getString(C0254R.string.yes), posLis, activity.getString(C0254R.string.f47no), negLis);
    }

    public static void showDialogQuestion(Activity activity, CharSequence msg, OnClickListener posLis, OnClickListener negLis, OnClickListener cancelLis) {
        dialogDoItem(activity, activity.getText(C0254R.string.question), C0254R.C0252drawable.var_empty, msg, activity.getString(C0254R.string.yes), posLis, activity.getString(C0254R.string.f47no), negLis, activity.getString(C0254R.string.cancel), cancelLis);
    }

    public static void showDialogQuestion(Activity activity, int msg, OnClickListener posLis) {
        showDialogQuestion(activity, activity.getText(msg), posLis, null);
    }

    public static void showDialogQuestion(Activity activity, int msg, OnClickListener posLis, OnClickListener negLis) {
        showDialogQuestion(activity, activity.getText(msg), posLis, negLis);
    }

    public static void showDialogQuestion(Activity activity, int msg, OnClickListener posLis, OnClickListener negLis, OnClickListener cancelLis) {
        showDialogQuestion(activity, activity.getText(msg), posLis, negLis, cancelLis);
    }

    public static void showDialogWebView(Activity activity, int title, String msg) {
        showDialogWebView(activity, activity.getString(title), msg);
    }

    public static void showDialogWebView(final Activity activity, final String title, final String msg) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (!activity.isFinishing()) {
                    Builder b = new Builder(activity);
                    b.setCancelable(false);
                    b.setTitle(title);
                    b.setView(UtilsGUI.getFilledWebView(activity, msg));
                    b.setPositiveButton(C0254R.string.close, null);
                    if (!activity.isFinishing()) {
                        b.show();
                    }
                }
            }
        });
    }

    public static CharSequence simpleHtml(String s) {
        return s == null ? s : Engine.removeHtml(s);
    }

    public static CharSequence html(String s, boolean preserveWhitespaces) {
        if (s == null) {
            return null;
        }
        if (preserveWhitespaces) {
            s = s.replaceAll("\\n", "<br>").replaceAll("  ", "&nbsp;&nbsp;");
        }
        return Html.fromHtml(s).toString();
    }
}
