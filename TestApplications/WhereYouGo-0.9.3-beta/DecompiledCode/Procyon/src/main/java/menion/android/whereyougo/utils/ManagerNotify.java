// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

import android.widget.Toast;
import android.app.Activity;
import menion.android.whereyougo.preferences.PreferenceValues;
import android.content.Context;
import menion.android.whereyougo.preferences.Locale;

public class ManagerNotify
{
    private static final String TAG = "ManagerNotify";
    
    public static void toastInternetProblem() {
        toastLongMessage(Locale.getString(2131165301));
    }
    
    public static void toastLongMessage(final int n) {
        toastLongMessage(Locale.getString(n));
    }
    
    public static void toastLongMessage(final Context context, final String s) {
        toastMessage(context, s, 1);
    }
    
    public static void toastLongMessage(final String s) {
        toastLongMessage((Context)PreferenceValues.getCurrentActivity(), s);
    }
    
    private static void toastMessage(final Context context, final String s, final int n) {
        Logger.d("ManagerNotify", "toastMessage(" + context + ", " + s + ", " + n + ")");
        if (context != null && s != null && s.length() != 0) {
            Label_0151: {
                try {
                    if (!(context instanceof Activity)) {
                        break Label_0151;
                    }
                    ((Activity)context).runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, (CharSequence)s, n).show();
                        }
                    });
                }
                catch (Exception ex) {
                    Logger.e("ManagerNotify", "toastMessage(" + context + ", " + s + ", " + n + ")", ex);
                }
                return;
            }
            Toast.makeText(context, (CharSequence)s, n).show();
        }
    }
    
    public static void toastShortMessage(final int n) {
        toastShortMessage(Locale.getString(n));
    }
    
    public static void toastShortMessage(final Context context, final String s) {
        toastMessage(context, s, 0);
    }
    
    public static void toastShortMessage(final String s) {
        toastShortMessage((Context)PreferenceValues.getCurrentActivity(), s);
    }
    
    public static void toastUnexpectedProblem() {
        toastLongMessage(Locale.getString(2131165312));
    }
}
