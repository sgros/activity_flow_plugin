// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.utils;

import cz.matejcik.openwig.Engine;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.Const;
import android.view.WindowManager$LayoutParams;
import android.view.Window;
import android.widget.ListAdapter;
import android.view.View;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.text.Html;
import android.view.ViewGroup$LayoutParams;
import android.webkit.WebView;
import android.text.TextUtils;
import android.app.AlertDialog$Builder;
import android.content.DialogInterface$OnClickListener;
import android.app.Activity;
import android.widget.ListView;
import menion.android.whereyougo.gui.extension.DataInfo;
import java.util.ArrayList;
import android.content.Context;

public class UtilsGUI
{
    public static final int DIALOG_EDIT_TEXT_ID = 10005;
    public static final int DIALOG_SPINNER_ID = 10006;
    
    public static ListView createListView(final Context context, final boolean b, final ArrayList<DataInfo> list) {
        final ListView listView = new ListView(context);
        setListView(context, listView, b, list);
        return listView;
    }
    
    public static void dialogDoItem(final Activity activity, final CharSequence charSequence, final int n, final CharSequence charSequence2, final String s, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final String s2, final DialogInterface$OnClickListener dialogInterface$OnClickListener2) {
        activity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)activity);
                    alertDialog$Builder.setCancelable(false);
                    alertDialog$Builder.setTitle(charSequence);
                    alertDialog$Builder.setIcon(n);
                    alertDialog$Builder.setMessage(charSequence2);
                    if (!TextUtils.isEmpty((CharSequence)s)) {
                        alertDialog$Builder.setPositiveButton((CharSequence)s, dialogInterface$OnClickListener);
                    }
                    if (!TextUtils.isEmpty((CharSequence)s2)) {
                        alertDialog$Builder.setNegativeButton((CharSequence)s2, dialogInterface$OnClickListener2);
                    }
                    if (!activity.isFinishing()) {
                        alertDialog$Builder.show();
                    }
                }
            }
        });
    }
    
    public static void dialogDoItem(final Activity activity, final CharSequence charSequence, final int n, final CharSequence charSequence2, final String s, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final String s2, final DialogInterface$OnClickListener dialogInterface$OnClickListener2, final String s3, final DialogInterface$OnClickListener dialogInterface$OnClickListener3) {
        activity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)activity);
                    alertDialog$Builder.setCancelable(true);
                    alertDialog$Builder.setTitle(charSequence);
                    alertDialog$Builder.setIcon(n);
                    alertDialog$Builder.setMessage(charSequence2);
                    if (!TextUtils.isEmpty((CharSequence)s)) {
                        alertDialog$Builder.setPositiveButton((CharSequence)s, dialogInterface$OnClickListener);
                    }
                    if (!TextUtils.isEmpty((CharSequence)s2)) {
                        alertDialog$Builder.setNegativeButton((CharSequence)s2, dialogInterface$OnClickListener2);
                    }
                    if (!TextUtils.isEmpty((CharSequence)s3)) {
                        alertDialog$Builder.setNeutralButton((CharSequence)s3, dialogInterface$OnClickListener3);
                    }
                    if (!activity.isFinishing()) {
                        alertDialog$Builder.show();
                    }
                }
            }
        });
    }
    
    public static int getDialogWidth() {
        return -1;
    }
    
    public static WebView getFilledWebView(Activity activity, final String s) {
        activity = (Activity)new WebView((Context)activity);
        while (true) {
            try {
                ((WebView)activity).loadDataWithBaseURL((String)null, s.replaceAll("\\+", " "), "text/html", "utf-8", (String)null);
                ((WebView)activity).setLayoutParams(new ViewGroup$LayoutParams(getDialogWidth(), -2));
                ((WebView)activity).setBackgroundColor(-1);
                return (WebView)activity;
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public static int getUniqueId() {
        return (int)(Math.random() * 2.147483647E9);
    }
    
    public static CharSequence html(String string, final boolean b) {
        if (string == null) {
            string = null;
        }
        else {
            String replaceAll = string;
            if (b) {
                replaceAll = string.replaceAll("\\n", "<br>").replaceAll("  ", "&nbsp;&nbsp;");
            }
            string = Html.fromHtml(replaceAll).toString();
        }
        return string;
    }
    
    public static void setButtons(final Activity activity, final int[] array, final View$OnClickListener onClickListener, final View$OnLongClickListener onLongClickListener) {
        for (final int n : array) {
            if (onClickListener != null) {
                activity.findViewById(n).setOnClickListener(onClickListener);
            }
            if (onLongClickListener != null) {
                activity.findViewById(n).setOnLongClickListener(onLongClickListener);
            }
        }
    }
    
    public static void setListView(final Context context, final ListView listView, final boolean b, final ArrayList<DataInfo> list) {
        if (b) {
            listView.setChoiceMode(2);
        }
        final IconedListAdapter adapter = new IconedListAdapter(context, list, (View)listView);
        adapter.setTextView02Visible(0, true);
        if (list.size() > 50) {
            listView.setFastScrollEnabled(true);
        }
        listView.setAdapter((ListAdapter)adapter);
    }
    
    public static void setWindowDialogCorrectWidth(final Window window) {
        final WindowManager$LayoutParams attributes = window.getAttributes();
        attributes.width = getDialogWidth();
        window.setAttributes(attributes);
    }
    
    public static void setWindowFloatingRight(final Activity activity) {
        final int min = Math.min(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        final WindowManager$LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.width = getDialogWidth();
        attributes.height = min;
        attributes.x = (int)(Const.SCREEN_WIDTH - attributes.width - Utils.getDpPixels(10.0f));
        activity.getWindow().setAttributes(attributes);
    }
    
    public static void showDialogDeleteItem(final Activity activity, String charSequence, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        final String string = activity.getString(2131165302);
        if (charSequence != null) {
            charSequence = activity.getString(2131165197, new Object[] { Html.fromHtml((String)charSequence) });
        }
        else {
            charSequence = activity.getText(2131165196);
        }
        dialogDoItem(activity, string, 2130837547, charSequence, activity.getString(2131165317), dialogInterface$OnClickListener, activity.getString(2131165224), null);
    }
    
    public static void showDialogError(final Activity activity, final int n, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        showDialogError(activity, activity.getText(n), dialogInterface$OnClickListener);
    }
    
    public static void showDialogError(final Activity activity, final CharSequence charSequence) {
        showDialogError(activity, charSequence, null);
    }
    
    public static void showDialogError(final Activity activity, final CharSequence charSequence, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        dialogDoItem(activity, activity.getText(2131165200), 2130837531, charSequence, null, null, activity.getString(2131165192), dialogInterface$OnClickListener);
    }
    
    public static void showDialogInfo(final Activity activity, final int n) {
        showDialogInfo(activity, activity.getText(n));
    }
    
    public static void showDialogInfo(final Activity activity, final int n, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        dialogDoItem(activity, activity.getText(2131165213), 2130837552, activity.getText(n), null, null, activity.getString(2131165192), dialogInterface$OnClickListener);
    }
    
    public static void showDialogInfo(final Activity activity, final CharSequence charSequence) {
        dialogDoItem(activity, activity.getText(2131165213), 2130837552, charSequence, null, null, activity.getString(2131165192), null);
    }
    
    public static void showDialogQuestion(final Activity activity, final int n, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        showDialogQuestion(activity, activity.getText(n), dialogInterface$OnClickListener, null);
    }
    
    public static void showDialogQuestion(final Activity activity, final int n, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final DialogInterface$OnClickListener dialogInterface$OnClickListener2) {
        showDialogQuestion(activity, activity.getText(n), dialogInterface$OnClickListener, dialogInterface$OnClickListener2);
    }
    
    public static void showDialogQuestion(final Activity activity, final int n, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final DialogInterface$OnClickListener dialogInterface$OnClickListener2, final DialogInterface$OnClickListener dialogInterface$OnClickListener3) {
        showDialogQuestion(activity, activity.getText(n), dialogInterface$OnClickListener, dialogInterface$OnClickListener2, dialogInterface$OnClickListener3);
    }
    
    public static void showDialogQuestion(final Activity activity, final CharSequence charSequence, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        showDialogQuestion(activity, charSequence, dialogInterface$OnClickListener, null);
    }
    
    public static void showDialogQuestion(final Activity activity, final CharSequence charSequence, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final DialogInterface$OnClickListener dialogInterface$OnClickListener2) {
        dialogDoItem(activity, activity.getText(2131165302), 2130837578, charSequence, activity.getString(2131165317), dialogInterface$OnClickListener, activity.getString(2131165224), dialogInterface$OnClickListener2);
    }
    
    public static void showDialogQuestion(final Activity activity, final CharSequence charSequence, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final DialogInterface$OnClickListener dialogInterface$OnClickListener2, final DialogInterface$OnClickListener dialogInterface$OnClickListener3) {
        dialogDoItem(activity, activity.getText(2131165302), 2130837578, charSequence, activity.getString(2131165317), dialogInterface$OnClickListener, activity.getString(2131165224), dialogInterface$OnClickListener2, activity.getString(2131165190), dialogInterface$OnClickListener3);
    }
    
    public static void showDialogWebView(final Activity activity, final int n, final String s) {
        showDialogWebView(activity, activity.getString(n), s);
    }
    
    public static void showDialogWebView(final Activity activity, final String s, final String s2) {
        activity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)activity);
                    alertDialog$Builder.setCancelable(false);
                    alertDialog$Builder.setTitle((CharSequence)s);
                    alertDialog$Builder.setView((View)UtilsGUI.getFilledWebView(activity, s2));
                    alertDialog$Builder.setPositiveButton(2131165192, (DialogInterface$OnClickListener)null);
                    if (!activity.isFinishing()) {
                        alertDialog$Builder.show();
                    }
                }
            }
        });
    }
    
    public static CharSequence simpleHtml(String removeHtml) {
        if (removeHtml != null) {
            removeHtml = Engine.removeHtml(removeHtml);
        }
        return removeHtml;
    }
}
